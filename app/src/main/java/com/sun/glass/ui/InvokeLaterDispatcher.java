package com.sun.glass.ui;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

/* loaded from: jfxrt.jar:com/sun/glass/ui/InvokeLaterDispatcher.class */
public final class InvokeLaterDispatcher extends Thread {
    private final BlockingDeque<Runnable> deque = new LinkedBlockingDeque();
    private final Object LOCK = new StringBuilder("InvokeLaterLock");
    private boolean nestedEventLoopEntered = false;
    private volatile boolean leavingNestedEventLoop = false;
    private final InvokeLaterSubmitter invokeLaterSubmitter;

    /* loaded from: jfxrt.jar:com/sun/glass/ui/InvokeLaterDispatcher$InvokeLaterSubmitter.class */
    public interface InvokeLaterSubmitter {
        void submitForLaterInvocation(Runnable runnable);
    }

    public InvokeLaterDispatcher(InvokeLaterSubmitter invokeLaterSubmitter) {
        setDaemon(true);
        this.invokeLaterSubmitter = invokeLaterSubmitter;
    }

    /* loaded from: jfxrt.jar:com/sun/glass/ui/InvokeLaterDispatcher$Future.class */
    private class Future implements Runnable {
        private boolean done = false;
        private final Runnable runnable;

        public Future(Runnable r2) {
            this.runnable = r2;
        }

        public boolean isDone() {
            return this.done;
        }

        @Override // java.lang.Runnable
        public void run() {
            try {
                this.runnable.run();
                synchronized (InvokeLaterDispatcher.this.LOCK) {
                    this.done = true;
                    InvokeLaterDispatcher.this.LOCK.notifyAll();
                }
            } catch (Throwable th) {
                synchronized (InvokeLaterDispatcher.this.LOCK) {
                    this.done = true;
                    InvokeLaterDispatcher.this.LOCK.notifyAll();
                    throw th;
                }
            }
        }
    }

    /* JADX WARN: Finally extract failed */
    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        while (true) {
            try {
                Runnable r2 = this.deque.takeFirst();
                if (this.leavingNestedEventLoop) {
                    this.deque.addFirst(r2);
                    synchronized (this.LOCK) {
                        while (this.leavingNestedEventLoop) {
                            this.LOCK.wait();
                        }
                    }
                } else {
                    Future future = new Future(r2);
                    this.invokeLaterSubmitter.submitForLaterInvocation(future);
                    synchronized (this.LOCK) {
                        while (!future.isDone() && !this.nestedEventLoopEntered) {
                            try {
                                this.LOCK.wait();
                            } catch (Throwable th) {
                                this.nestedEventLoopEntered = false;
                                throw th;
                            }
                        }
                        this.nestedEventLoopEntered = false;
                    }
                }
            } catch (InterruptedException e2) {
                return;
            }
        }
    }

    public void invokeAndWait(Runnable runnable) {
        Future future = new Future(runnable);
        this.invokeLaterSubmitter.submitForLaterInvocation(future);
        synchronized (this.LOCK) {
            while (!future.isDone()) {
                try {
                    this.LOCK.wait();
                } catch (InterruptedException e2) {
                }
            }
        }
    }

    public void invokeLater(Runnable command) {
        this.deque.addLast(command);
    }

    public void notifyEnteringNestedEventLoop() {
        synchronized (this.LOCK) {
            this.nestedEventLoopEntered = true;
            this.LOCK.notifyAll();
        }
    }

    public void notifyLeavingNestedEventLoop() {
        synchronized (this.LOCK) {
            this.leavingNestedEventLoop = true;
            this.LOCK.notifyAll();
        }
    }

    public void notifyLeftNestedEventLoop() {
        synchronized (this.LOCK) {
            this.leavingNestedEventLoop = false;
            this.LOCK.notifyAll();
        }
    }
}
