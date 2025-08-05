package sun.misc;

/* compiled from: Timer.java */
/* loaded from: rt.jar:sun/misc/TimerTickThread.class */
class TimerTickThread extends Thread {
    static final int MAX_POOL_SIZE = 3;
    static int curPoolSize = 0;
    static TimerTickThread pool = null;
    TimerTickThread next = null;
    Timer timer;
    long lastSleepUntil;

    TimerTickThread() {
    }

    protected static synchronized TimerTickThread call(Timer timer, long j2) {
        TimerTickThread timerTickThread = pool;
        if (timerTickThread == null) {
            timerTickThread = new TimerTickThread();
            timerTickThread.timer = timer;
            timerTickThread.lastSleepUntil = j2;
            timerTickThread.start();
        } else {
            pool = pool.next;
            timerTickThread.timer = timer;
            timerTickThread.lastSleepUntil = j2;
            synchronized (timerTickThread) {
                timerTickThread.notify();
            }
        }
        return timerTickThread;
    }

    private boolean returnToPool() {
        synchronized (getClass()) {
            if (curPoolSize >= 3) {
                return false;
            }
            this.next = pool;
            pool = this;
            curPoolSize++;
            this.timer = null;
            while (this.timer == null) {
                synchronized (this) {
                    try {
                        wait();
                    } catch (InterruptedException e2) {
                    }
                }
            }
            synchronized (getClass()) {
                curPoolSize--;
            }
            return true;
        }
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        do {
            this.timer.owner.tick(this.timer);
            synchronized (TimerThread.timerThread) {
                synchronized (this.timer) {
                    if (this.lastSleepUntil == this.timer.sleepUntil) {
                        TimerThread.requeue(this.timer);
                    }
                }
            }
        } while (returnToPool());
    }
}
