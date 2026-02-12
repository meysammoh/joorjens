package ir.joorjens.background;

import ir.joorjens.common.ThreadFactory;
import ir.joorjens.common.Utility;
import ir.joorjens.common.response.JoorJensException;
import ir.joorjens.jmx.Config;
import ir.joorjens.model.entity.Customer;
import ir.joorjens.model.entity.Log;
import ir.joorjens.model.util.TypeEnumeration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class TestLog {
    private static final Logger logger = LoggerFactory.getLogger(TestLog.class);

    private static final int THREAD_COUNT = 5;
    private static final int LOOP_MAIN = 6;
    private static final int LOOP_SUB = Config.bgLogListSizeMax + 1;
    private static final int INSERTED_COUNT = (THREAD_COUNT * LOOP_MAIN * LOOP_SUB);

    private static final Object LOCK = new Object();
    private static long timeStart = 0;

    //@Test(timeout = 30_000)
    public void test() throws InterruptedException, JoorJensException {

        Config.bgLogSleep = 5 * 1000;

        LogTask logTask = new LogTask();
        Thread thread = new Thread(logTask, ThreadFactory.getThreadName(Adder.class));
        thread.setPriority(Thread.MAX_PRIORITY);
        thread.start();

        for (int i = 0; i < THREAD_COUNT; i++) {
            Thread t = new Thread(new Adder(i), ThreadFactory.getThreadName(Adder.class, i));
            t.setPriority(Thread.MAX_PRIORITY);
            t.start();
        }
//
        while (INSERTED_COUNT != LogTask.insertedCount) {
            logger.info(String.format("insertedCount: %d", LogTask.insertedCount));
            Thread.sleep(1000);
        }
        long time = System.currentTimeMillis() - timeStart;
        double insertPerSecond = LogTask.insertedCount / (time / 1000.0);
        logger.info(String.format("insertedCount: %d, take %d (ms), so %.2f ips :)",
                LogTask.insertedCount, time, insertPerSecond));
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
            Customer customer = new Customer();
            customer.setId(1);
            logger.info(String.format("Thread-%d started ...", this.i));
            for (int j = 0; j < LOOP_MAIN; j++) {
                for (int i = 0; i < LOOP_SUB; i++) {
                    LogTask.addLog(new Log(0, TypeEnumeration.ACTION_DELETE.getId(), TypeEnumeration.ACTION_STATUS_OK.getId()
                            , Customer.class.getSimpleName(), "id=" + i * j, "{}"));
                }
            }
            logger.info(String.format("Thread-%d finished :)", this.i));
        }
    }
}