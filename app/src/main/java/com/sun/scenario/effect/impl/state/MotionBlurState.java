package com.sun.scenario.effect.impl.state;

import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.transform.BaseTransform;

/* loaded from: jfxrt.jar:com/sun/scenario/effect/impl/state/MotionBlurState.class */
public class MotionBlurState extends LinearConvolveKernel {
    private float radius;
    private float angle;

    public float getRadius() {
        return this.radius;
    }

    public void setRadius(float radius) {
        if (radius < 0.0f || radius > 63.0f) {
            throw new IllegalArgumentException("Radius must be in the range [0,63]");
        }
        this.radius = radius;
    }

    public float getAngle() {
        return this.angle;
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }

    public int getHPad() {
        return (int) Math.ceil(Math.abs(Math.cos(this.angle)) * this.radius);
    }

    public int getVPad() {
        return (int) Math.ceil(Math.abs(Math.sin(this.angle)) * this.radius);
    }

    @Override // com.sun.scenario.effect.impl.state.LinearConvolveKernel
    public LinearConvolveRenderState getRenderState(BaseTransform filtertx) {
        float dx = (float) Math.cos(this.angle);
        float dy = (float) Math.sin(this.angle);
        return new GaussianRenderState(this.radius, dx, dy, filtertx);
    }

    @Override // com.sun.scenario.effect.impl.state.LinearConvolveKernel
    public boolean isNop() {
        return this.radius == 0.0f;
    }

    @Override // com.sun.scenario.effect.impl.state.LinearConvolveKernel
    public int getKernelSize(int pass) {
        return (((int) Math.ceil(this.radius)) * 2) + 1;
    }

    @Override // com.sun.scenario.effect.impl.state.LinearConvolveKernel
    public final Rectangle getResultBounds(Rectangle srcdimension, int pass) {
        Rectangle ret = new Rectangle(srcdimension);
        ret.grow(getHPad(), getVPad());
        return ret;
    }
}
