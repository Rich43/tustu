package com.sun.scenario.animation.shared;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.beans.value.WritableValue;
import javafx.util.Duration;

/* loaded from: jfxrt.jar:com/sun/scenario/animation/shared/SimpleClipInterpolator.class */
class SimpleClipInterpolator extends ClipInterpolator {
    private static final KeyFrame ZERO_FRAME = new KeyFrame(Duration.ZERO, new KeyValue[0]);
    private KeyFrame startKeyFrame;
    private KeyFrame endKeyFrame;
    private long endTicks;
    private InterpolationInterval[] interval;
    private int undefinedStartValueCount;
    private long ticks;
    private boolean invalid;

    SimpleClipInterpolator(KeyFrame startKeyFrame, KeyFrame endKeyFrame, long ticks) {
        this.invalid = true;
        this.startKeyFrame = startKeyFrame;
        this.endKeyFrame = endKeyFrame;
        this.endTicks = ticks;
    }

    SimpleClipInterpolator(KeyFrame endKeyFrame, long ticks) {
        this.invalid = true;
        this.startKeyFrame = ZERO_FRAME;
        this.endKeyFrame = endKeyFrame;
        this.endTicks = ticks;
    }

    @Override // com.sun.scenario.animation.shared.ClipInterpolator
    ClipInterpolator setKeyFrames(KeyFrame[] keyFrames, long[] keyFrameTicks) {
        if (ClipInterpolator.getRealKeyFrameCount(keyFrames) != 2) {
            return ClipInterpolator.create(keyFrames, keyFrameTicks);
        }
        if (keyFrames.length == 1) {
            this.startKeyFrame = ZERO_FRAME;
            this.endKeyFrame = keyFrames[0];
            this.endTicks = keyFrameTicks[0];
        } else {
            this.startKeyFrame = keyFrames[0];
            this.endKeyFrame = keyFrames[1];
            this.endTicks = keyFrameTicks[1];
        }
        this.invalid = true;
        return this;
    }

    @Override // com.sun.scenario.animation.shared.ClipInterpolator
    void validate(boolean forceSync) {
        if (!this.invalid) {
            if (forceSync) {
                int n2 = this.interval.length;
                for (int i2 = n2 - this.undefinedStartValueCount; i2 < n2; i2++) {
                    this.interval[i2].recalculateStartValue();
                }
                return;
            }
            return;
        }
        this.ticks = this.endTicks;
        Map<WritableValue<?>, KeyValue> map = new HashMap<>();
        for (KeyValue keyValue : this.endKeyFrame.getValues()) {
            map.put(keyValue.getTarget(), keyValue);
        }
        int valueCount = map.size();
        this.interval = new InterpolationInterval[valueCount];
        int i3 = 0;
        for (KeyValue startKeyValue : this.startKeyFrame.getValues()) {
            WritableValue<?> target = startKeyValue.getTarget();
            KeyValue endKeyValue = map.get(target);
            if (endKeyValue != null) {
                int i4 = i3;
                i3++;
                this.interval[i4] = InterpolationInterval.create(endKeyValue, this.ticks, startKeyValue, this.ticks);
                map.remove(target);
            }
        }
        this.undefinedStartValueCount = map.values().size();
        Iterator<KeyValue> it = map.values().iterator();
        while (it.hasNext()) {
            int i5 = i3;
            i3++;
            this.interval[i5] = InterpolationInterval.create(it.next(), this.ticks);
        }
        this.invalid = false;
    }

    @Override // com.sun.scenario.animation.shared.ClipInterpolator
    void interpolate(long ticks) {
        double frac = ticks / this.ticks;
        int n2 = this.interval.length;
        for (int i2 = 0; i2 < n2; i2++) {
            this.interval[i2].interpolate(frac);
        }
    }
}
