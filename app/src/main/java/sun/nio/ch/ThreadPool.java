package sun.nio.ch;

import java.security.AccessController;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import sun.misc.InnocuousThread;
import sun.security.action.GetPropertyAction;

/* loaded from: rt.jar:sun/nio/ch/ThreadPool.class */
public class ThreadPool {
    private static final String DEFAULT_THREAD_POOL_THREAD_FACTORY = "java.nio.channels.DefaultThreadPool.threadFactory";
    private static final String DEFAULT_THREAD_POOL_INITIAL_SIZE = "java.nio.channels.DefaultThreadPool.initialSize";
    private final ExecutorService executor;
    private final boolean isFixed;
    private final int poolSize;

    private ThreadPool(ExecutorService executorService, boolean z2, int i2) {
        this.executor = executorService;
        this.isFixed = z2;
        this.poolSize = i2;
    }

    ExecutorService executor() {
        return this.executor;
    }

    boolean isFixedThreadPool() {
        return this.isFixed;
    }

    int poolSize() {
        return this.poolSize;
    }

    static ThreadFactory defaultThreadFactory() {
        if (System.getSecurityManager() == null) {
            return runnable -> {
                Thread thread = new Thread(runnable);
                thread.setDaemon(true);
                return thread;
            };
        }
        return runnable2 -> {
            return (Thread) AccessController.doPrivileged(() -> {
                InnocuousThread innocuousThread = new InnocuousThread(runnable2);
                innocuousThread.setDaemon(true);
                return innocuousThread;
            });
        };
    }

    /* loaded from: rt.jar:sun/nio/ch/ThreadPool$DefaultThreadPoolHolder.class */
    private static class DefaultThreadPoolHolder {
        static final ThreadPool defaultThreadPool = ThreadPool.createDefault();

        private DefaultThreadPoolHolder() {
        }
    }

    static ThreadPool getDefault() {
        return DefaultThreadPoolHolder.defaultThreadPool;
    }

    static ThreadPool createDefault() {
        int defaultThreadPoolInitialSize = getDefaultThreadPoolInitialSize();
        if (defaultThreadPoolInitialSize < 0) {
            defaultThreadPoolInitialSize = Runtime.getRuntime().availableProcessors();
        }
        ThreadFactory defaultThreadPoolThreadFactory = getDefaultThreadPoolThreadFactory();
        if (defaultThreadPoolThreadFactory == null) {
            defaultThreadPoolThreadFactory = defaultThreadFactory();
        }
        return new ThreadPool(Executors.newCachedThreadPool(defaultThreadPoolThreadFactory), false, defaultThreadPoolInitialSize);
    }

    static ThreadPool create(int i2, ThreadFactory threadFactory) {
        if (i2 <= 0) {
            throw new IllegalArgumentException("'nThreads' must be > 0");
        }
        return new ThreadPool(Executors.newFixedThreadPool(i2, threadFactory), true, i2);
    }

    public static ThreadPool wrap(ExecutorService executorService, int i2) {
        if (executorService == null) {
            throw new NullPointerException("'executor' is null");
        }
        if (executorService instanceof ThreadPoolExecutor) {
            if (((ThreadPoolExecutor) executorService).getMaximumPoolSize() == Integer.MAX_VALUE) {
                if (i2 < 0) {
                    i2 = Runtime.getRuntime().availableProcessors();
                } else {
                    i2 = 0;
                }
            }
        } else if (i2 < 0) {
            i2 = 0;
        }
        return new ThreadPool(executorService, false, i2);
    }

    private static int getDefaultThreadPoolInitialSize() {
        String str = (String) AccessController.doPrivileged(new GetPropertyAction(DEFAULT_THREAD_POOL_INITIAL_SIZE));
        if (str != null) {
            try {
                return Integer.parseInt(str);
            } catch (NumberFormatException e2) {
                throw new Error("Value of property 'java.nio.channels.DefaultThreadPool.initialSize' is invalid: " + ((Object) e2));
            }
        }
        return -1;
    }

    private static ThreadFactory getDefaultThreadPoolThreadFactory() {
        String str = (String) AccessController.doPrivileged(new GetPropertyAction(DEFAULT_THREAD_POOL_THREAD_FACTORY));
        if (str != null) {
            try {
                return (ThreadFactory) Class.forName(str, true, ClassLoader.getSystemClassLoader()).newInstance();
            } catch (ClassNotFoundException e2) {
                throw new Error(e2);
            } catch (IllegalAccessException e3) {
                throw new Error(e3);
            } catch (InstantiationException e4) {
                throw new Error(e4);
            }
        }
        return null;
    }
}
