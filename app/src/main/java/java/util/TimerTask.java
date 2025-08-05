package java.util;

/* loaded from: rt.jar:java/util/TimerTask.class */
public abstract class TimerTask implements Runnable {
    static final int VIRGIN = 0;
    static final int SCHEDULED = 1;
    static final int EXECUTED = 2;
    static final int CANCELLED = 3;
    long nextExecutionTime;
    final Object lock = new Object();
    int state = 0;
    long period = 0;

    @Override // java.lang.Runnable
    public abstract void run();

    protected TimerTask() {
    }

    public boolean cancel() {
        boolean z2;
        synchronized (this.lock) {
            z2 = this.state == 1;
            this.state = 3;
        }
        return z2;
    }

    public long scheduledExecutionTime() {
        long j2;
        synchronized (this.lock) {
            j2 = this.period < 0 ? this.nextExecutionTime + this.period : this.nextExecutionTime - this.period;
        }
        return j2;
    }
}
