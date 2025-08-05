package com.sun.scenario.animation.shared;

import com.sun.javafx.animation.TickCalculation;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.util.Duration;

/* loaded from: jfxrt.jar:com/sun/scenario/animation/shared/TimelineClipCore.class */
public class TimelineClipCore {
    private static final int UNDEFINED_KEYFRAME = -1;
    private static final Comparator<KeyFrame> KEY_FRAME_COMPARATOR = (kf1, kf2) -> {
        return kf1.getTime().compareTo(kf2.getTime());
    };
    Timeline timeline;
    private KeyFrame[] keyFrames = new KeyFrame[0];
    private long[] keyFrameTicks = new long[0];
    private boolean canSkipFrames = true;
    private boolean aborted = false;
    private int lastKF = -1;
    private long curTicks = 0;
    private ClipInterpolator clipInterpolator = ClipInterpolator.create(this.keyFrames, this.keyFrameTicks);

    public TimelineClipCore(Timeline timeline) {
        this.timeline = timeline;
    }

    public Duration setKeyFrames(Collection<KeyFrame> keyFrames) {
        int n2 = keyFrames.size();
        KeyFrame[] sortedKeyFrames = new KeyFrame[n2];
        keyFrames.toArray(sortedKeyFrames);
        Arrays.sort(sortedKeyFrames, KEY_FRAME_COMPARATOR);
        this.canSkipFrames = true;
        this.keyFrames = sortedKeyFrames;
        this.keyFrameTicks = new long[n2];
        for (int i2 = 0; i2 < n2; i2++) {
            this.keyFrameTicks[i2] = TickCalculation.fromDuration(this.keyFrames[i2].getTime());
            if (this.canSkipFrames && this.keyFrames[i2].getOnFinished() != null) {
                this.canSkipFrames = false;
            }
        }
        this.clipInterpolator = this.clipInterpolator.setKeyFrames(sortedKeyFrames, this.keyFrameTicks);
        return n2 == 0 ? Duration.ZERO : sortedKeyFrames[n2 - 1].getTime();
    }

    public void notifyCurrentRateChanged() {
        if (this.timeline.getStatus() != Animation.Status.RUNNING) {
            clearLastKeyFrame();
        }
    }

    public void abort() {
        this.aborted = true;
    }

    private void clearLastKeyFrame() {
        this.lastKF = -1;
    }

    public void jumpTo(long ticks, boolean forceJump) {
        this.lastKF = -1;
        this.curTicks = ticks;
        if (this.timeline.getStatus() != Animation.Status.STOPPED || forceJump) {
            if (forceJump) {
                this.clipInterpolator.validate(false);
            }
            this.clipInterpolator.interpolate(ticks);
        }
    }

    public void start(boolean forceSync) {
        clearLastKeyFrame();
        this.clipInterpolator.validate(forceSync);
        if (this.curTicks > 0) {
            this.clipInterpolator.interpolate(this.curTicks);
        }
    }

    public void playTo(long ticks) {
        if (this.canSkipFrames) {
            clearLastKeyFrame();
            setTime(ticks);
            this.clipInterpolator.interpolate(ticks);
            return;
        }
        this.aborted = false;
        boolean forward = this.curTicks <= ticks;
        if (forward) {
            int fromKF = this.lastKF == -1 ? 0 : this.keyFrameTicks[this.lastKF] <= this.curTicks ? this.lastKF + 1 : this.lastKF;
            int toKF = this.keyFrames.length;
            int fi = fromKF;
            while (true) {
                if (fi >= toKF) {
                    break;
                }
                long kfTicks = this.keyFrameTicks[fi];
                if (kfTicks <= ticks) {
                    if (kfTicks >= this.curTicks) {
                        visitKeyFrame(fi, kfTicks);
                        if (this.aborted) {
                            break;
                        }
                    }
                    fi++;
                } else {
                    this.lastKF = fi - 1;
                    break;
                }
            }
        } else {
            int fromKF2 = this.lastKF == -1 ? this.keyFrames.length - 1 : this.keyFrameTicks[this.lastKF] >= this.curTicks ? this.lastKF - 1 : this.lastKF;
            int fi2 = fromKF2;
            while (true) {
                if (fi2 < 0) {
                    break;
                }
                long kfTicks2 = this.keyFrameTicks[fi2];
                if (kfTicks2 >= ticks) {
                    if (kfTicks2 <= this.curTicks) {
                        visitKeyFrame(fi2, kfTicks2);
                        if (this.aborted) {
                            break;
                        }
                    }
                    fi2--;
                } else {
                    this.lastKF = fi2 + 1;
                    break;
                }
            }
        }
        if (!this.aborted) {
            if (this.lastKF == -1 || this.keyFrameTicks[this.lastKF] != ticks || this.keyFrames[this.lastKF].getOnFinished() == null) {
                setTime(ticks);
                this.clipInterpolator.interpolate(ticks);
            }
        }
    }

    private void setTime(long ticks) {
        this.curTicks = ticks;
        AnimationAccessor.getDefault().setCurrentTicks(this.timeline, ticks);
    }

    private void visitKeyFrame(int kfIndex, long kfTicks) {
        if (kfIndex != this.lastKF) {
            this.lastKF = kfIndex;
            KeyFrame kf = this.keyFrames[kfIndex];
            EventHandler<ActionEvent> onFinished = kf.getOnFinished();
            if (onFinished != null) {
                setTime(kfTicks);
                this.clipInterpolator.interpolate(kfTicks);
                try {
                    onFinished.handle(new ActionEvent(kf, null));
                } catch (Throwable ex) {
                    Thread.currentThread().getUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), ex);
                }
            }
        }
    }
}
