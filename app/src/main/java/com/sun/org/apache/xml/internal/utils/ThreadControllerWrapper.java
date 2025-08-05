package com.sun.org.apache.xml.internal.utils;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/utils/ThreadControllerWrapper.class */
public class ThreadControllerWrapper {
    private static ThreadController m_tpool = new ThreadController();

    public static Thread runThread(Runnable runnable, int priority) {
        return m_tpool.run(runnable, priority);
    }

    public static void waitThread(Thread worker, Runnable task) throws InterruptedException {
        m_tpool.waitThread(worker, task);
    }

    /* loaded from: rt.jar:com/sun/org/apache/xml/internal/utils/ThreadControllerWrapper$ThreadController.class */
    public static class ThreadController {

        /* loaded from: rt.jar:com/sun/org/apache/xml/internal/utils/ThreadControllerWrapper$ThreadController$SafeThread.class */
        final class SafeThread extends Thread {
            private volatile boolean ran;

            public SafeThread(Runnable target) {
                super(target);
                this.ran = false;
            }

            @Override // java.lang.Thread, java.lang.Runnable
            public final void run() {
                if (Thread.currentThread() != this) {
                    throw new IllegalStateException("The run() method in a SafeThread cannot be called from another thread.");
                }
                synchronized (this) {
                    if (!this.ran) {
                        this.ran = true;
                    } else {
                        throw new IllegalStateException("The run() method in a SafeThread cannot be called more than once.");
                    }
                }
                super.run();
            }
        }

        public Thread run(Runnable task, int priority) {
            Thread t2 = new SafeThread(task);
            t2.start();
            return t2;
        }

        public void waitThread(Thread worker, Runnable task) throws InterruptedException {
            worker.join();
        }
    }
}
