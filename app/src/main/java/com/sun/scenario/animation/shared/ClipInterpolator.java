package com.sun.scenario.animation.shared;

import javafx.animation.KeyFrame;
import javafx.util.Duration;

/* loaded from: jfxrt.jar:com/sun/scenario/animation/shared/ClipInterpolator.class */
public abstract class ClipInterpolator {
    abstract ClipInterpolator setKeyFrames(KeyFrame[] keyFrameArr, long[] jArr);

    abstract void interpolate(long j2);

    abstract void validate(boolean z2);

    static ClipInterpolator create(KeyFrame[] keyFrames, long[] keyFrameTicks) {
        return getRealKeyFrameCount(keyFrames) == 2 ? keyFrames.length == 1 ? new SimpleClipInterpolator(keyFrames[0], keyFrameTicks[0]) : new SimpleClipInterpolator(keyFrames[0], keyFrames[1], keyFrameTicks[1]) : new GeneralClipInterpolator(keyFrames, keyFrameTicks);
    }

    static int getRealKeyFrameCount(KeyFrame[] keyFrames) {
        int length = keyFrames.length;
        if (length == 0) {
            return 0;
        }
        return keyFrames[0].getTime().greaterThan(Duration.ZERO) ? length + 1 : length;
    }
}
