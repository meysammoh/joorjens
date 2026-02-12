package ir.joorjens.background;

import ir.joorjens.common.Utility;
import ir.joorjens.common.response.JoorJensException;
import ir.joorjens.jmx.Config;
import ir.joorjens.model.entity.Position;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestPosition {

    private static final Logger logger = LoggerFactory.getLogger(TestPosition.class);

    private static final int ADDER_COUNT = 5;
    private static final int LOOP_MAIN = 6;
    private static final int LOOP_SUB = Config.bgPositionListSizeMax + 1;
    private static final int INSERTED_COUNT = (ADDER_COUNT * LOOP_MAIN * LOOP_SUB);

    private static final Object LOCK = new Object();
    private static long timeStart = 0;

    //@Test(timeout = 30_000)
    public void test() throws InterruptedException, JoorJensException {

        Config.bgPositionSleep = 5 * 1000;

        PositionTask positionTask = new PositionTask();
        Thread thread = new Thread(positionTask);
        thread.setPriority(Thread.MAX_PRIORITY);
        thread.start();

        Adder[] adders = new Adder[ADDER_COUNT];
        Thread[] threads = new Thread[ADDER_COUNT];
        for (int i = 0; i < adders.length; i++) {
            //new Thread(new Adder((i+1))).start();
            adders[i] = new Adder((i + 1));
            threads[i] = new Thread(adders[i]);
            threads[i].setPriority(Thread.MAX_PRIORITY);
        }
        for (Thread t : threads) {
            t.start();
        }

        while (INSERTED_COUNT != PositionTask.insertedCount) {
            logger.info(String.format("insertedCount: %d", PositionTask.insertedCount));
            Thread.sleep(1000);
        }
        long time = System.currentTimeMillis() - timeStart;
        double insertPerSecond = PositionTask.insertedCount / (time / 1000.0);
        logger.info(String.format("insertedCount: %d, take %d (ms), so %.2f ips :)",
                PositionTask.insertedCount, time, insertPerSecond));
    }

    private class Adder implements Runnable {

        int i;

        Adder(int i) {
            this.i = i;
        }

        @Override
        public void run() {
            try {
                Thread.sleep(Utility.getTimeUntil(3 * 1000));
            } catch (InterruptedException ignored) {
            }

            synchronized (LOCK) {
                if (timeStart == 0) {
                    timeStart = System.currentTimeMillis();
                }
            }

            logger.info(String.format("Thread-%d started ...", this.i));
            for (int j = 0; j < LOOP_MAIN; j++) {
                for (int i = 0; i < LOOP_SUB; i++) {
                    PositionTask.addPosition(new Position(i, 35.7160571 + (.01 * i), 51.4074959 + (.01 * i)));
                }
            }
            logger.info(String.format("Thread-%d finished :)", this.i));
        }
    }

}