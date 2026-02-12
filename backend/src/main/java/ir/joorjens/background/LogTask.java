package ir.joorjens.background;

import ir.joorjens.common.ThreadFactory;
import ir.joorjens.common.Utility;
import ir.joorjens.common.response.JoorJensException;
import ir.joorjens.dao.interfaces.LogRepository;
import ir.joorjens.dao.manager.RepositoryManager;
import ir.joorjens.jmx.Config;
import ir.joorjens.model.entity.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * This class will add {@link ir.joorjens.model.entity.Log} objects to it`s related table, in background and in batch mode.
 * <ul>
 * you can add your {@link ir.joorjens.model.entity.Log} via {@link #addLog(ir.joorjens.model.entity.Log)}
 * </ul>
 * <ul>
 * It works in two way:
 * <li>It awakes every {@link ir.joorjens.jmx.Config#bgLogSleep} and truncates {@link #LOGS}(log object list) to {@link ir.joorjens.model.entity.Log} table.</li>
 * <li>When you are adding your {@link ir.joorjens.model.entity.Log} in {@link #addLog(ir.joorjens.model.entity.Log)} function,
 * if {@link #LOGS}`s size exceeds {@link ir.joorjens.jmx.Config#bgLogListSizeMax} It will be awake and ...</li>
 * </ul>
 */
public class LogTask implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(LogTask.class);
    private static final List<Log> LOGS = Collections.synchronizedList(new ArrayList<Log>());
    private static final LogRepository LOG_REPO = (LogRepository) RepositoryManager.getByEntity(Log.class);

    /**
     * add your log object to it`s list. the list will be inserted to db after {@link ir.joorjens.jmx.Config#bgLogSleep} at max.
     *
     * @param log refer to {@link Log}
     */
    public static void addLog(Log log) {
        LOGS.add(log);
        if (LOGS.size() > Config.bgLogListSizeMax) {
            synchronized (LOCK_NOTIFY) {
                LOCK_NOTIFY.notifyAll();
            }
        }
    }

    static long insertedCount = 0;
    private static final Object LOCK_THREAD = new Object();
    private static final Object LOCK_NOTIFY = new Object();

    private final ExecutorService THREAD_POOL = Executors.newFixedThreadPool(Config.BG_LOG_WORKER_SIZE,
            new ThreadFactory(LogTask.class.getSimpleName()));
    private int threadSize = Config.BG_LOG_WORKER_SIZE;

    @Override
    public void run() {

        final LogTaskNotify notifier = new LogTaskNotify();
        final Thread notifierThread = new Thread(notifier, LogTaskNotify.class.getSimpleName());
        notifierThread.setPriority(Thread.MAX_PRIORITY);
        notifierThread.start();

        while (true) {
            execute("Timer");
            try {
                Thread.sleep(Utility.getTimeUntil(Config.bgLogSleep));
            } catch (InterruptedException e) {
                logger.warn(String.format("InterruptedException@MainRun. Message: %s", e.getMessage()));
            }
        }
    }

    /**
     * this class notifies it`s parent({@link LogTask}) when {@link #LOGS}`s size exceeds!
     * so the parent will awake and insert all of them to table.
     */
    private class LogTaskNotify implements Runnable {
        @Override
        public void run() {
            while (true) {
                try {
                    synchronized (LOCK_NOTIFY) {
                        LOCK_NOTIFY.wait();
                    }
                    execute("Notifier");
                } catch (InterruptedException e) {
                    logger.warn(String.format("InterruptedException@LogTaskNotify. Message: %s", e.getMessage()));
                }
            }
        }
    } //end private class

    /**
     * delivers list of {@link #LOGS} to {@link LogTaskInsert} Thread.
     */
    private void execute(String from) {
        int size = LOGS.size();
        if (size == 0) {
            return;
        }
        logger.debug(String.format("execute called with size(%d) from(%s)", size, from));

        final List<Log> logs = new ArrayList<>();
        synchronized (LOGS) {
            logs.addAll(LOGS);
            LOGS.clear();
        }
        size = logs.size();

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
            THREAD_POOL.execute(new LogTaskInsert(logs));
        }
    }

    /**
     * Inserts {@link #LOGS} list to it`s table in batch mode!
     */
    private class LogTaskInsert implements Runnable {

        private final List<Log> logs;
        private final long time;

        LogTaskInsert(final List<Log> logs) {
            this.logs = logs;
            this.time = System.currentTimeMillis();
        }

        @Override
        public void run() {

            int size = 0;
            try {
                LOG_REPO.persistBatch(logs);
                size = logs.size();
                logger.debug(String.format("list(%d) was successfully inserted in %d (ms).",
                        logs.size(), (System.currentTimeMillis() - this.time)));

            } catch (JoorJensException e) {
                logger.error(String.format("JabejaException@LogTaskInsert. Message: %s, Data: %s",
                        e.getMessage(), Utility.toJson(logs)));
            }
            logs.clear();

            synchronized (LOCK_THREAD) {
                insertedCount += size;
                if (++threadSize > 0) {
                    LOCK_THREAD.notify();
                }
            }
        }
    } //end private class
}