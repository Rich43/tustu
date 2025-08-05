package com.sun.scenario.animation.shared;

import com.sun.javafx.animation.TickCalculation;
import javafx.animation.Animation;
import javafx.util.Duration;

/* loaded from: jfxrt.jar:com/sun/scenario/animation/shared/ClipEnvelope.class */
public abstract class ClipEnvelope {
    protected static final long INDEFINITE = Long.MAX_VALUE;
    protected static final double EPSILON = 1.0E-12d;
    protected Animation animation;
    protected double rate;
    protected long cycleTicks;
    protected double currentRate;
    protected long deltaTicks = 0;
    protected long ticks = 0;
    protected boolean inTimePulse = false;
    protected boolean aborted = false;

    public abstract ClipEnvelope setCycleDuration(Duration duration);

    public abstract void setRate(double d2);

    public abstract void setAutoReverse(boolean z2);

    public abstract ClipEnvelope setCycleCount(int i2);

    public abstract void timePulse(long j2);

    public abstract void jumpTo(long j2);

    protected abstract double calculateCurrentRate();

    protected ClipEnvelope(Animation animation) {
        this.rate = 1.0d;
        this.cycleTicks = 0L;
        this.currentRate = this.rate;
        this.animation = animation;
        if (animation != null) {
            Duration cycleDuration = animation.getCycleDuration();
            this.cycleTicks = TickCalculation.fromDuration(cycleDuration);
            this.rate = animation.getRate();
        }
    }

    public static ClipEnvelope create(Animation animation) {
        if (animation.getCycleCount() == 1 || animation.getCycleDuration().isIndefinite()) {
            return new SingleLoopClipEnvelope(animation);
        }
        if (animation.getCycleCount() == -1) {
            return new InfiniteClipEnvelope(animation);
        }
        return new FiniteClipEnvelope(animation);
    }

    protected void updateCycleTicks(Duration cycleDuration) {
        this.cycleTicks = TickCalculation.fromDuration(cycleDuration);
    }

    public boolean wasSynched() {
        return this.cycleTicks != 0;
    }

    public void start() {
        setCurrentRate(calculateCurrentRate());
        this.deltaTicks = this.ticks;
    }

    public void abortCurrentPulse() {
        if (this.inTimePulse) {
            this.aborted = true;
            this.inTimePulse = false;
        }
    }

    protected void setCurrentRate(double currentRate) {
        this.currentRate = currentRate;
        AnimationAccessor.getDefault().setCurrentRate(this.animation, currentRate);
    }

    protected static long checkBounds(long value, long max) {
        return Math.max(0L, Math.min(value, max));
    }

    public double getCurrentRate() {
        return this.currentRate;
    }
}
