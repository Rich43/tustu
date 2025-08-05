package sun.misc;

/* loaded from: rt.jar:sun/misc/Timer.class */
public class Timer {
    public Timeable owner;
    long interval;
    long remainingTime;
    Timer next;
    static TimerThread timerThread = null;
    boolean regular = true;
    long sleepUntil = System.currentTimeMillis();
    boolean stopped = true;

    public Timer(Timeable timeable, long j2) {
        this.owner = timeable;
        this.interval = j2;
        this.remainingTime = j2;
        synchronized (getClass()) {
            if (timerThread == null) {
                timerThread = new TimerThread();
            }
        }
    }

    public synchronized boolean isStopped() {
        return this.stopped;
    }

    public void stop() {
        long jCurrentTimeMillis = System.currentTimeMillis();
        synchronized (timerThread) {
            synchronized (this) {
                if (!this.stopped) {
                    TimerThread.dequeue(this);
                    this.remainingTime = Math.max(0L, this.sleepUntil - jCurrentTimeMillis);
                    this.sleepUntil = jCurrentTimeMillis;
                    this.stopped = true;
                }
            }
        }
    }

    public void cont() {
        synchronized (timerThread) {
            synchronized (this) {
                if (this.stopped) {
                    this.sleepUntil = Math.max(this.sleepUntil + 1, System.currentTimeMillis() + this.remainingTime);
                    TimerThread.enqueue(this);
                    this.stopped = false;
                }
            }
        }
    }

    public void reset() {
        synchronized (timerThread) {
            synchronized (this) {
                setRemainingTime(this.interval);
            }
        }
    }

    public synchronized long getStopTime() {
        return this.sleepUntil;
    }

    public synchronized long getInterval() {
        return this.interval;
    }

    public synchronized void setInterval(long j2) {
        this.interval = j2;
    }

    public synchronized long getRemainingTime() {
        return this.remainingTime;
    }

    public void setRemainingTime(long j2) {
        synchronized (timerThread) {
            synchronized (this) {
                if (this.stopped) {
                    this.remainingTime = j2;
                } else {
                    stop();
                    this.remainingTime = j2;
                    cont();
                }
            }
        }
    }

    public synchronized void setRegular(boolean z2) {
        this.regular = z2;
    }

    protected Thread getTimerThread() {
        return TimerThread.timerThread;
    }
}
