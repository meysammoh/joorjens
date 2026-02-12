package ir.joorjens.common;

/**
 * <ul>
 * set the worker`s name in thread pool
 * <li>name will be in 'prefix-counter' format</li>
 * </ul>
 *
 * @author Khalil Alijani Mamaqani
 * @since 12/19/16
 */
public class ThreadFactory implements java.util.concurrent.ThreadFactory {
    private int counter = 0;
    private String prefix = "";

    public ThreadFactory(String prefix) {
        this.prefix = prefix;
    }

    public ThreadFactory(int counter, String prefix) {
        this.counter = counter;
        this.prefix = prefix;
    }

    public static String getThreadName(Class className) {
        return getThreadName(className, "");
    }

    public static String getThreadName(Class className, int i) {
        return getThreadName(className, "") + "_" + i;
    }

    public static String getThreadName(Class className, String prefix) {
        return className.getSimpleName() + prefix + "Thread";
    }

    @Override
    public Thread newThread(Runnable r) {
        return new Thread(r, prefix + "-" + ++counter);
    }
}
