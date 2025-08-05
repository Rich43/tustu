package com.sun.glass.ui;

/* loaded from: jfxrt.jar:com/sun/glass/ui/Timer.class */
public abstract class Timer {
    private static final double UNSET_PERIOD = -1.0d;
    private static final double SET_PERIOD = -2.0d;
    private final Runnable runnable;
    private long ptr;
    private double period = -1.0d;

    protected abstract long _start(Runnable runnable);

    protected abstract long _start(Runnable runnable, int i2);

    protected abstract void _stop(long j2);

    protected Timer(Runnable runnable) {
        if (runnable == null) {
            throw new IllegalArgumentException("runnable shouldn't be null");
        }
        this.runnable = runnable;
    }

    public static int getMinPeriod() {
        return Application.GetApplication().staticTimer_getMinPeriod();
    }

    public static int getMaxPeriod() {
        return Application.GetApplication().staticTimer_getMaxPeriod();
    }

    public synchronized void start(int period) {
        if (period < getMinPeriod() || period > getMaxPeriod()) {
            throw new IllegalArgumentException("period is out of range");
        }
        if (this.ptr != 0) {
            stop();
        }
        this.ptr = _start(this.runnable, period);
        if (this.ptr == 0) {
            this.period = -1.0d;
            throw new RuntimeException("Failed to start the timer");
        }
        this.period = period;
    }

    public synchronized void start() {
        if (this.ptr != 0) {
            stop();
        }
        this.ptr = _start(this.runnable);
        if (this.ptr == 0) {
            this.period = -1.0d;
            throw new RuntimeException("Failed to start the timer");
        }
        this.period = SET_PERIOD;
    }

    public synchronized void stop() {
        if (this.ptr != 0) {
            _stop(this.ptr);
            this.ptr = 0L;
            this.period = -1.0d;
        }
    }

    public synchronized boolean isRunning() {
        return this.period != -1.0d;
    }
}
