package com.sun.scenario.animation.shared;

import javafx.animation.Animation;

/* loaded from: jfxrt.jar:com/sun/scenario/animation/shared/AnimationAccessor.class */
public abstract class AnimationAccessor {
    public static AnimationAccessor DEFAULT;
    static final /* synthetic */ boolean $assertionsDisabled;

    public abstract void setCurrentRate(Animation animation, double d2);

    public abstract void setCurrentTicks(Animation animation, long j2);

    public abstract void playTo(Animation animation, long j2, long j3);

    public abstract void jumpTo(Animation animation, long j2, long j3, boolean z2);

    public abstract void finished(Animation animation);

    static {
        $assertionsDisabled = !AnimationAccessor.class.desiredAssertionStatus();
    }

    public static AnimationAccessor getDefault() {
        if (DEFAULT != null) {
            return DEFAULT;
        }
        try {
            Class.forName(Animation.class.getName());
        } catch (ClassNotFoundException ex) {
            if (!$assertionsDisabled) {
                throw new AssertionError(ex);
            }
        }
        if ($assertionsDisabled || DEFAULT != null) {
            return DEFAULT;
        }
        throw new AssertionError((Object) "The DEFAULT field must be initialized");
    }
}
