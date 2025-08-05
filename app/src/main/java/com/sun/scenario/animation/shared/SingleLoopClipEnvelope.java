package com.sun.scenario.animation.shared;

import javafx.animation.Animation;
import javafx.util.Duration;

/* loaded from: jfxrt.jar:com/sun/scenario/animation/shared/SingleLoopClipEnvelope.class */
public class SingleLoopClipEnvelope extends ClipEnvelope {
    private int cycleCount;

    @Override // com.sun.scenario.animation.shared.ClipEnvelope
    public void setRate(double rate) {
        Animation.Status status = this.animation.getStatus();
        if (status != Animation.Status.STOPPED) {
            if (status == Animation.Status.RUNNING) {
                setCurrentRate(Math.abs(this.currentRate - this.rate) < 1.0E-12d ? rate : -rate);
            }
            this.deltaTicks = this.ticks - Math.round(((this.ticks - this.deltaTicks) * rate) / this.rate);
            abortCurrentPulse();
        }
        this.rate = rate;
    }

    @Override // com.sun.scenario.animation.shared.ClipEnvelope
    public void setAutoReverse(boolean autoReverse) {
    }

    @Override // com.sun.scenario.animation.shared.ClipEnvelope
    protected double calculateCurrentRate() {
        return this.rate;
    }

    protected SingleLoopClipEnvelope(Animation animation) {
        super(animation);
        if (animation != null) {
            this.cycleCount = animation.getCycleCount();
        }
    }

    @Override // com.sun.scenario.animation.shared.ClipEnvelope
    public boolean wasSynched() {
        return super.wasSynched() && this.cycleCount != 0;
    }

    @Override // com.sun.scenario.animation.shared.ClipEnvelope
    public ClipEnvelope setCycleDuration(Duration cycleDuration) {
        if (this.cycleCount != 1 && !cycleDuration.isIndefinite()) {
            return create(this.animation);
        }
        updateCycleTicks(cycleDuration);
        return this;
    }

    @Override // com.sun.scenario.animation.shared.ClipEnvelope
    public ClipEnvelope setCycleCount(int cycleCount) {
        if (cycleCount != 1 && this.cycleTicks != Long.MAX_VALUE) {
            return create(this.animation);
        }
        this.cycleCount = cycleCount;
        return this;
    }

    @Override // com.sun.scenario.animation.shared.ClipEnvelope
    public void timePulse(long currentTick) {
        if (this.cycleTicks == 0) {
            return;
        }
        this.aborted = false;
        this.inTimePulse = true;
        try {
            this.ticks = ClipEnvelope.checkBounds(this.deltaTicks + Math.round(currentTick * this.currentRate), this.cycleTicks);
            AnimationAccessor.getDefault().playTo(this.animation, this.ticks, this.cycleTicks);
            boolean reachedEnd = this.currentRate > 0.0d ? this.ticks == this.cycleTicks : this.ticks == 0;
            if (reachedEnd && !this.aborted) {
                AnimationAccessor.getDefault().finished(this.animation);
            }
        } finally {
            this.inTimePulse = false;
        }
    }

    @Override // com.sun.scenario.animation.shared.ClipEnvelope
    public void jumpTo(long ticks) {
        if (this.cycleTicks == 0) {
            return;
        }
        long newTicks = ClipEnvelope.checkBounds(ticks, this.cycleTicks);
        this.deltaTicks += newTicks - this.ticks;
        this.ticks = newTicks;
        AnimationAccessor.getDefault().jumpTo(this.animation, newTicks, this.cycleTicks, false);
        abortCurrentPulse();
    }
}
