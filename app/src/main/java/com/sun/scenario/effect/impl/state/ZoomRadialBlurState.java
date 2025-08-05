package com.sun.scenario.effect.impl.state;

import com.sun.scenario.effect.ZoomRadialBlur;

/* loaded from: jfxrt.jar:com/sun/scenario/effect/impl/state/ZoomRadialBlurState.class */
public class ZoomRadialBlurState {
    private float dx = -1.0f;
    private float dy = -1.0f;
    private final ZoomRadialBlur effect;

    public ZoomRadialBlurState(ZoomRadialBlur effect) {
        this.effect = effect;
    }

    public int getRadius() {
        return this.effect.getRadius();
    }

    public void updateDeltas(float dx, float dy) {
        this.dx = dx;
        this.dy = dy;
    }

    public void invalidateDeltas() {
        this.dx = -1.0f;
        this.dy = -1.0f;
    }

    public float getDx() {
        return this.dx;
    }

    public float getDy() {
        return this.dy;
    }

    public int getNumSteps() {
        int r2 = getRadius();
        return (r2 * 2) + 1;
    }

    public float getAlpha() {
        float r2 = getRadius();
        return 1.0f / ((2.0f * r2) + 1.0f);
    }
}
