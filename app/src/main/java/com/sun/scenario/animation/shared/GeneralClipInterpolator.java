package com.sun.scenario.animation.shared;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.beans.value.WritableValue;

/* loaded from: jfxrt.jar:com/sun/scenario/animation/shared/GeneralClipInterpolator.class */
class GeneralClipInterpolator extends ClipInterpolator {
    private KeyFrame[] keyFrames;
    private long[] keyFrameTicks;
    private InterpolationInterval[][] interval = new InterpolationInterval[0];
    private int[] undefinedStartValues = new int[0];
    private boolean invalid = true;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !GeneralClipInterpolator.class.desiredAssertionStatus();
    }

    /* JADX WARN: Type inference failed for: r1v1, types: [com.sun.scenario.animation.shared.InterpolationInterval[], com.sun.scenario.animation.shared.InterpolationInterval[][]] */
    GeneralClipInterpolator(KeyFrame[] keyFrames, long[] keyFrameTicks) {
        this.keyFrames = keyFrames;
        this.keyFrameTicks = keyFrameTicks;
    }

    @Override // com.sun.scenario.animation.shared.ClipInterpolator
    ClipInterpolator setKeyFrames(KeyFrame[] keyFrames, long[] keyFrameTicks) {
        if (ClipInterpolator.getRealKeyFrameCount(keyFrames) == 2) {
            return ClipInterpolator.create(keyFrames, keyFrameTicks);
        }
        this.keyFrames = keyFrames;
        this.keyFrameTicks = keyFrameTicks;
        this.invalid = true;
        return this;
    }

    /* JADX WARN: Type inference failed for: r1v23, types: [com.sun.scenario.animation.shared.InterpolationInterval[], com.sun.scenario.animation.shared.InterpolationInterval[][]] */
    @Override // com.sun.scenario.animation.shared.ClipInterpolator
    void validate(boolean forceSync) {
        if (!this.invalid) {
            if (forceSync) {
                int n2 = this.undefinedStartValues.length;
                for (int i2 = 0; i2 < n2; i2++) {
                    int index = this.undefinedStartValues[i2];
                    this.interval[index][0].recalculateStartValue();
                }
                return;
            }
            return;
        }
        Map<WritableValue<?>, KeyValue> lastKeyValues = new HashMap<>();
        int n3 = this.keyFrames.length;
        int index2 = 0;
        while (index2 < n3) {
            KeyFrame keyFrame = this.keyFrames[index2];
            if (this.keyFrameTicks[index2] != 0) {
                break;
            }
            for (KeyValue keyValue : keyFrame.getValues()) {
                lastKeyValues.put(keyValue.getTarget(), keyValue);
            }
            index2++;
        }
        Map<WritableValue<?>, List<InterpolationInterval>> map = new HashMap<>();
        Set<WritableValue<?>> undefinedValues = new HashSet<>();
        while (index2 < n3) {
            KeyFrame keyFrame2 = this.keyFrames[index2];
            long ticks = this.keyFrameTicks[index2];
            for (KeyValue rightKeyValue : keyFrame2.getValues()) {
                WritableValue<?> target = rightKeyValue.getTarget();
                List<InterpolationInterval> list = map.get(target);
                KeyValue leftKeyValue = lastKeyValues.get(target);
                if (list == null) {
                    List<InterpolationInterval> list2 = new ArrayList<>();
                    map.put(target, list2);
                    if (leftKeyValue == null) {
                        list2.add(InterpolationInterval.create(rightKeyValue, ticks));
                        undefinedValues.add(target);
                    } else {
                        list2.add(InterpolationInterval.create(rightKeyValue, ticks, leftKeyValue, ticks));
                    }
                } else {
                    if (!$assertionsDisabled && leftKeyValue == null) {
                        throw new AssertionError();
                    }
                    list.add(InterpolationInterval.create(rightKeyValue, ticks, leftKeyValue, ticks - list.get(list.size() - 1).ticks));
                }
                lastKeyValues.put(target, rightKeyValue);
            }
            index2++;
        }
        int targetCount = map.size();
        if (this.interval.length != targetCount) {
            this.interval = new InterpolationInterval[targetCount];
        }
        int undefinedStartValuesCount = undefinedValues.size();
        if (this.undefinedStartValues.length != undefinedStartValuesCount) {
            this.undefinedStartValues = new int[undefinedStartValuesCount];
        }
        int undefinedStartValuesIndex = 0;
        Iterator<Map.Entry<WritableValue<?>, List<InterpolationInterval>>> iterator = map.entrySet().iterator();
        for (int i3 = 0; i3 < targetCount; i3++) {
            Map.Entry<WritableValue<?>, List<InterpolationInterval>> entry = iterator.next();
            this.interval[i3] = new InterpolationInterval[entry.getValue().size()];
            entry.getValue().toArray(this.interval[i3]);
            if (undefinedValues.contains(entry.getKey())) {
                int i4 = undefinedStartValuesIndex;
                undefinedStartValuesIndex++;
                this.undefinedStartValues[i4] = i3;
            }
        }
        this.invalid = false;
    }

    @Override // com.sun.scenario.animation.shared.ClipInterpolator
    void interpolate(long ticks) {
        int targetCount = this.interval.length;
        for (int targetIndex = 0; targetIndex < targetCount; targetIndex++) {
            InterpolationInterval[] intervalList = this.interval[targetIndex];
            int intervalCount = intervalList.length;
            long leftTicks = 0;
            int intervalIndex = 0;
            while (true) {
                if (intervalIndex < intervalCount - 1) {
                    InterpolationInterval i2 = intervalList[intervalIndex];
                    long rightTicks = i2.ticks;
                    if (ticks <= rightTicks) {
                        double frac = (ticks - leftTicks) / (rightTicks - leftTicks);
                        i2.interpolate(frac);
                        break;
                    } else {
                        leftTicks = rightTicks;
                        intervalIndex++;
                    }
                } else {
                    InterpolationInterval i3 = intervalList[intervalCount - 1];
                    double frac2 = Math.min(1.0d, (ticks - leftTicks) / (i3.ticks - leftTicks));
                    i3.interpolate(frac2);
                    break;
                }
            }
        }
    }
}
