package ir.joorjens.common.timer;

import ir.joorjens.common.ThreadFactory;
import ir.joorjens.common.Utility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Timer implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(Timer.class);
    //------------------------------------------------------------------------------
    //------------------------------------------------------------------------------
    private final Object LOCK_THREAD = new Object();
    private final TimerInterface[] timerInterface;
    private final ExecutorService THREAD_POOL;
    private final int interval;
    private int threadSize = 10;
    //------------------------------------------------------------------------------

    public Timer(TimerInterface[] timerInterface, int threadSize, int interval) {
        this.timerInterface = timerInterface;
        this.interval = interval;
        this.threadSize = threadSize;
        this.THREAD_POOL = Executors.newFixedThreadPool(this.threadSize,
                new ThreadFactory(TimeNotifier.class.getSimpleName()));
    }

    @Override
    public void run() {
        logger.debug(String.format("timer is run. listSize(%d), threadSize(%d), interval(%d).",
                this.timerInterface.length, this.threadSize, this.interval));
        while (true) {

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
            THREAD_POOL.execute(new TimeNotifier());

            try {
                Thread.sleep(Utility.getTimeUntil(this.interval));
            } catch (InterruptedException e) {
                logger.warn(String.format("InterruptedException@MainRun. Message: %s", e.getMessage()));
            }
        }
    }

    private class TimeNotifier implements Runnable {

        @Override
        public void run() {
            for (TimerInterface timer : timerInterface) {
                timer.execute("Timer");
            }

            synchronized (LOCK_THREAD) {
                if (++threadSize > 0) {
                    LOCK_THREAD.notify();
                }
            }
        }
    } //end private class
}