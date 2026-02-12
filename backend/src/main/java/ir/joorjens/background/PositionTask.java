package ir.joorjens.background;

import ir.joorjens.common.ThreadFactory;
import ir.joorjens.common.Utility;
import ir.joorjens.common.response.JoorJensException;
import ir.joorjens.dao.interfaces.PositionRepository;
import ir.joorjens.dao.manager.RepositoryManager;
import ir.joorjens.jmx.Config;
import ir.joorjens.model.entity.Position;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * This class will add {@link ir.joorjens.model.entity.Position} objects to it`s related table, in background and in batch mode.
 * <ul>
 * you can add your {@link ir.joorjens.model.entity.Position} via {@link #addPosition(ir.joorjens.model.entity.Position)}
 * </ul>
 * <ul>
 * It works in two way:
 * <li>It awakes every {@link ir.joorjens.jmx.Config#bgPositionSleep} and truncates {@link #POSITIONS}(lat&lon object list) to {@link ir.joorjens.model.entity.Position} table.</li>
 * <li>When you are adding your {@link ir.joorjens.model.entity.Position} in {@link #addPosition(ir.joorjens.model.entity.Position)} function,
 * if {@link #POSITIONS}`s size exceeds {@link ir.joorjens.jmx.Config#bgPositionListSizeMax} It will be awake and ...</li>
 * </ul>
 */
public class PositionTask implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(PositionTask.class);
    private static final PositionRepository POSITION_REPO = (PositionRepository) RepositoryManager.getByEntity(Position.class);

    /**
     * hold list of {@link Position} that will be inserted to DB.
     */
    private static final List<Position> POSITIONS = Collections.synchronizedList(new ArrayList<Position>());

    /**
     * add your Lat&Lon object to it`s list. the list will be inserted to db after {@link ir.joorjens.jmx.Config#bgPositionSleep} at max.
     *
     * @param position refer to {@link Position}
     */
    public static void addPosition(Position position) {
        POSITIONS.add(position);
        if (POSITIONS.size() > Config.bgPositionListSizeMax) {
            synchronized (LOCK_NOTIFY) {
                LOCK_NOTIFY.notifyAll();
            }
        }
    }

    static long insertedCount = 0;
    private static final Object LOCK_NOTIFY = new Object();
    private final Object LOCK_THREAD = new Object();
    private final ExecutorService THREAD_POOL = Executors.newFixedThreadPool(Config.BG_POSITION_WORKER_SIZE,
            new ThreadFactory(PositionTask.class.getSimpleName()));
    private int threadSize = Config.BG_POSITION_WORKER_SIZE;

    @Override
    public void run() {

        final PositionTaskNotify notifier = new PositionTaskNotify();
        final Thread notifierThread = new Thread(notifier, PositionTaskNotify.class.getSimpleName());
        notifierThread.setPriority(Thread.MAX_PRIORITY);
        notifierThread.start();

        while (true) {
            execute("Timer");
            try {
                Thread.sleep(Utility.getTimeUntil(Config.bgPositionSleep));
            } catch (InterruptedException e) {
                logger.warn(String.format("InterruptedException@MainRun. Message: %s", e.getMessage()));
            }
        }
    }

    /**
     * this class notifies it`s parent({@link PositionTask}) when {@link #POSITIONS}`s size exceeds!
     * so the parent will awake and insert all of them to table.
     */
    private class PositionTaskNotify implements Runnable {
        @Override
        public void run() {
            while (true) {
                try {
                    synchronized (LOCK_NOTIFY) {
                        LOCK_NOTIFY.wait();
                    }
                    execute("Notifier");
                } catch (InterruptedException e) {
                    logger.warn(String.format("InterruptedException@PositionTaskNotify. Message: %s", e.getMessage()));
                }
            }
        }
    } //end private class

    /**
     * delivers list of {@link #POSITIONS} to {@link PositionTaskInsert} Thread.
     */
    private void execute(String from) {
        int size = POSITIONS.size();
        if (size == 0) {
            return;
        }
        logger.debug(String.format("execute called with size(%d) from(%s)", size, from));

        final List<Position> positions = new ArrayList<>();
        synchronized (POSITIONS) {
            positions.addAll(POSITIONS);
            POSITIONS.clear();
        }
        size = positions.size();

        if (size > 0) {
            synchronized (LOCK_THREAD) {
                while (threadSize <= 0) {
                    try {
                        LOCK_THREAD.wait();
                    } catch (InterruptedException e) {
                        logger.error(String.format("InterruptedException@execute, Sleeping. Message: %s", e.getMessage()));
                    }
                }
                --threadSize;
            }
            THREAD_POOL.execute(new PositionTaskInsert(positions));
        }
    }

    /**
     * Inserts {@link #POSITIONS} list to it`s table in batch mode!
     */
    private class PositionTaskInsert implements Runnable {

        private final List<Position> positions;
        private final long time;

        PositionTaskInsert(final List<Position> positions) {
            this.positions = positions;
            this.time = System.currentTimeMillis();
        }

        @Override
        public void run() {

            int size = 0;
            try {
                POSITION_REPO.persistBatch(positions);
                size = positions.size();
                logger.debug(String.format("list(%d) was successfully inserted in %d (ms).",
                        positions.size(), (System.currentTimeMillis() - this.time)));

            } catch (JoorJensException e) {
                logger.error(String.format("JabejaException@PositionTaskInsert. Message: %s, Data: %s",
                        e.getMessage(), Utility.toJson(positions)));
            }
            positions.clear();

            synchronized (LOCK_THREAD) {
                insertedCount += size;
                if (++threadSize > 0) {
                    LOCK_THREAD.notify();
                }
            }
        }
    } //end private class
}