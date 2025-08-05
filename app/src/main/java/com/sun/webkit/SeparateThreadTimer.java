package com.sun.webkit;

import java.lang.Thread;

/* compiled from: Timer.java */
/* loaded from: jfxrt.jar:com/sun/webkit/SeparateThreadTimer.class */
final class SeparateThreadTimer extends Timer implements Runnable {
    private final Invoker invoker = Invoker.getInvoker();
    private final FireRunner fireRunner = new FireRunner();
    private final Thread thread = new Thread(this, "WebPane-Timer");
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !SeparateThreadTimer.class.desiredAssertionStatus();
    }

    SeparateThreadTimer() {
        this.thread.setDaemon(true);
    }

    /* compiled from: Timer.java */
    /* loaded from: jfxrt.jar:com/sun/webkit/SeparateThreadTimer$FireRunner.class */
    private final class FireRunner implements Runnable {
        private volatile long time;

        private FireRunner() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public Runnable forTime(long time) {
            this.time = time;
            return this;
        }

        @Override // java.lang.Runnable
        public void run() {
            SeparateThreadTimer.this.fireTimerEvent(this.time);
        }
    }

    @Override // com.sun.webkit.Timer
    synchronized void setFireTime(long time) {
        super.setFireTime(time);
        if (this.thread.getState() == Thread.State.NEW) {
            this.thread.start();
        }
        notifyAll();
    }

    @Override // java.lang.Runnable
    public synchronized void run() {
        while (true) {
            try {
                if (this.fireTime > 0) {
                    for (long curTime = System.currentTimeMillis(); this.fireTime > curTime; curTime = System.currentTimeMillis()) {
                        wait(this.fireTime - curTime);
                    }
                    if (this.fireTime > 0) {
                        this.invoker.invokeOnEventThread(this.fireRunner.forTime(this.fireTime));
                    }
                }
                wait();
            } catch (InterruptedException e2) {
                return;
            }
        }
    }

    @Override // com.sun.webkit.Timer
    public void notifyTick() {
        if (!$assertionsDisabled) {
            throw new AssertionError();
        }
    }
}
