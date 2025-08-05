package com.sun.scenario.animation.shared;

import javafx.animation.Animation;
import javafx.util.Duration;

/* loaded from: jfxrt.jar:com/sun/scenario/animation/shared/InfiniteClipEnvelope.class */
public class InfiniteClipEnvelope extends ClipEnvelope {
    private boolean autoReverse;
    private long pos;

    protected InfiniteClipEnvelope(Animation animation) {
        super(animation);
        if (animation != null) {
            this.autoReverse = animation.isAutoReverse();
        }
    }

    @Override // com.sun.scenario.animation.shared.ClipEnvelope
    public void setAutoReverse(boolean autoReverse) {
        this.autoReverse = autoReverse;
    }

    @Override // com.sun.scenario.animation.shared.ClipEnvelope
    protected double calculateCurrentRate() {
        if (this.autoReverse && this.ticks % (2 * this.cycleTicks) >= this.cycleTicks) {
            return -this.rate;
        }
        return this.rate;
    }

    @Override // com.sun.scenario.animation.shared.ClipEnvelope
    public ClipEnvelope setCycleDuration(Duration cycleDuration) {
        if (cycleDuration.isIndefinite()) {
            return create(this.animation);
        }
        updateCycleTicks(cycleDuration);
        return this;
    }

    @Override // com.sun.scenario.animation.shared.ClipEnvelope
    public ClipEnvelope setCycleCount(int cycleCount) {
        return cycleCount != -1 ? create(this.animation) : this;
    }

    @Override // com.sun.scenario.animation.shared.ClipEnvelope
    public void setRate(double rate) {
        Animation.Status status = this.animation.getStatus();
        if (status != Animation.Status.STOPPED) {
            if (status == Animation.Status.RUNNING) {
                setCurrentRate(Math.abs(this.currentRate - this.rate) < 1.0E-12d ? rate : -rate);
            }
            this.deltaTicks = this.ticks - Math.round((this.ticks - this.deltaTicks) * Math.abs(rate / this.rate));
            if (rate * this.rate < 0.0d) {
                long delta = (2 * this.cycleTicks) - this.pos;
                this.deltaTicks += delta;
                this.ticks += delta;
            }
            abortCurrentPulse();
        }
        this.rate = rate;
    }

    @Override // com.sun.scenario.animation.shared.ClipEnvelope
    public void timePulse(long currentTick) {
        if (this.cycleTicks == 0) {
            return;
        }
        this.aborted = false;
        this.inTimePulse = true;
        try {
            long oldTicks = this.ticks;
            this.ticks = Math.max(0L, this.deltaTicks + Math.round(currentTick * Math.abs(this.rate)));
            long overallDelta = this.ticks - oldTicks;
            if (overallDelta == 0) {
                return;
            }
            long cycleDelta = this.currentRate > 0.0d ? this.cycleTicks - this.pos : this.pos;
            while (overallDelta >= cycleDelta) {
                if (cycleDelta > 0) {
                    this.pos = this.currentRate > 0.0d ? this.cycleTicks : 0L;
                    overallDelta -= cycleDelta;
                    AnimationAccessor.getDefault().playTo(this.animation, this.pos, this.cycleTicks);
                    if (this.aborted) {
                        this.inTimePulse = false;
                        return;
                    }
                }
                if (this.autoReverse) {
                    setCurrentRate(-this.currentRate);
                } else {
                    this.pos = this.currentRate > 0.0d ? 0L : this.cycleTicks;
                    AnimationAccessor.getDefault().jumpTo(this.animation, this.pos, this.cycleTicks, false);
                }
                cycleDelta = this.cycleTicks;
            }
            if (overallDelta > 0) {
                this.pos += this.currentRate > 0.0d ? overallDelta : -overallDelta;
                AnimationAccessor.getDefault().playTo(this.animation, this.pos, this.cycleTicks);
            }
            this.inTimePulse = false;
        } finally {
            this.inTimePulse = false;
        }
    }

    @Override // com.sun.scenario.animation.shared.ClipEnvelope
    public void jumpTo(long newTicks) {
        if (this.cycleTicks == 0) {
            return;
        }
        long oldTicks = this.ticks;
        this.ticks = Math.max(0L, newTicks) % (2 * this.cycleTicks);
        long delta = this.ticks - oldTicks;
        if (delta != 0) {
            this.deltaTicks += delta;
            if (!this.autoReverse) {
                this.pos = this.ticks % this.cycleTicks;
                if (this.pos == 0) {
                    this.pos = this.ticks;
                }
            } else if (this.ticks > this.cycleTicks) {
                this.pos = (2 * this.cycleTicks) - this.ticks;
                if (this.animation.getStatus() == Animation.Status.RUNNING) {
                    setCurrentRate(-this.rate);
                }
            } else {
                this.pos = this.ticks;
                if (this.animation.getStatus() == Animation.Status.RUNNING) {
                    setCurrentRate(this.rate);
                }
            }
            AnimationAccessor.getDefault().jumpTo(this.animation, this.pos, this.cycleTicks, false);
            abortCurrentPulse();
        }
    }
}
