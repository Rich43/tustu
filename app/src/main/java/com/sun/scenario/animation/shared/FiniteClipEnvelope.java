package com.sun.scenario.animation.shared;

import javafx.animation.Animation;
import javafx.util.Duration;

/* loaded from: jfxrt.jar:com/sun/scenario/animation/shared/FiniteClipEnvelope.class */
public class FiniteClipEnvelope extends ClipEnvelope {
    private boolean autoReverse;
    private int cycleCount;
    private long totalTicks;
    private long pos;

    protected FiniteClipEnvelope(Animation animation) {
        super(animation);
        if (animation != null) {
            this.autoReverse = animation.isAutoReverse();
            this.cycleCount = animation.getCycleCount();
        }
        updateTotalTicks();
    }

    @Override // com.sun.scenario.animation.shared.ClipEnvelope
    public void setAutoReverse(boolean autoReverse) {
        this.autoReverse = autoReverse;
    }

    @Override // com.sun.scenario.animation.shared.ClipEnvelope
    protected double calculateCurrentRate() {
        if (this.autoReverse) {
            return (((this.ticks % (2 * this.cycleTicks)) > this.cycleTicks ? 1 : ((this.ticks % (2 * this.cycleTicks)) == this.cycleTicks ? 0 : -1)) < 0) == ((this.rate > 0.0d ? 1 : (this.rate == 0.0d ? 0 : -1)) > 0) ? this.rate : -this.rate;
        }
        return this.rate;
    }

    @Override // com.sun.scenario.animation.shared.ClipEnvelope
    public ClipEnvelope setCycleDuration(Duration cycleDuration) {
        if (cycleDuration.isIndefinite()) {
            return create(this.animation);
        }
        updateCycleTicks(cycleDuration);
        updateTotalTicks();
        return this;
    }

    @Override // com.sun.scenario.animation.shared.ClipEnvelope
    public ClipEnvelope setCycleCount(int cycleCount) {
        if (cycleCount == 1 || cycleCount == -1) {
            return create(this.animation);
        }
        this.cycleCount = cycleCount;
        updateTotalTicks();
        return this;
    }

    @Override // com.sun.scenario.animation.shared.ClipEnvelope
    public void setRate(double rate) {
        boolean toggled = rate * this.rate < 0.0d;
        long newTicks = toggled ? this.totalTicks - this.ticks : this.ticks;
        Animation.Status status = this.animation.getStatus();
        if (status != Animation.Status.STOPPED) {
            if (status == Animation.Status.RUNNING) {
                setCurrentRate(Math.abs(this.currentRate - this.rate) < 1.0E-12d ? rate : -rate);
            }
            this.deltaTicks = newTicks - Math.round((this.ticks - this.deltaTicks) * Math.abs(rate / this.rate));
            abortCurrentPulse();
        }
        this.ticks = newTicks;
        this.rate = rate;
    }

    private void updateTotalTicks() {
        this.totalTicks = this.cycleCount * this.cycleTicks;
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
            this.ticks = ClipEnvelope.checkBounds(this.deltaTicks + Math.round(currentTick * Math.abs(this.rate)), this.totalTicks);
            boolean reachedEnd = this.ticks >= this.totalTicks;
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
                if (!reachedEnd || overallDelta > 0) {
                    if (this.autoReverse) {
                        setCurrentRate(-this.currentRate);
                    } else {
                        this.pos = this.currentRate > 0.0d ? 0L : this.cycleTicks;
                        AnimationAccessor.getDefault().jumpTo(this.animation, this.pos, this.cycleTicks, false);
                    }
                }
                cycleDelta = this.cycleTicks;
            }
            if (overallDelta > 0 && !reachedEnd) {
                this.pos += this.currentRate > 0.0d ? overallDelta : -overallDelta;
                AnimationAccessor.getDefault().playTo(this.animation, this.pos, this.cycleTicks);
            }
            if (reachedEnd && !this.aborted) {
                AnimationAccessor.getDefault().finished(this.animation);
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
        if (this.rate < 0.0d) {
            newTicks = this.totalTicks - newTicks;
        }
        this.ticks = ClipEnvelope.checkBounds(newTicks, this.totalTicks);
        long delta = this.ticks - oldTicks;
        if (delta != 0) {
            this.deltaTicks += delta;
            if (this.autoReverse) {
                boolean forward = this.ticks % (2 * this.cycleTicks) < this.cycleTicks;
                if (forward == (this.rate > 0.0d)) {
                    this.pos = this.ticks % this.cycleTicks;
                    if (this.animation.getStatus() == Animation.Status.RUNNING) {
                        setCurrentRate(Math.abs(this.rate));
                    }
                } else {
                    this.pos = this.cycleTicks - (this.ticks % this.cycleTicks);
                    if (this.animation.getStatus() == Animation.Status.RUNNING) {
                        setCurrentRate(-Math.abs(this.rate));
                    }
                }
            } else {
                this.pos = this.ticks % this.cycleTicks;
                if (this.rate < 0.0d) {
                    this.pos = this.cycleTicks - this.pos;
                }
                if (this.pos == 0 && this.ticks > 0) {
                    this.pos = this.cycleTicks;
                }
            }
            AnimationAccessor.getDefault().jumpTo(this.animation, this.pos, this.cycleTicks, false);
            abortCurrentPulse();
        }
    }
}
