package com.sun.scenario.effect.impl.state;

import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.scenario.effect.Color4f;

/* loaded from: jfxrt.jar:com/sun/scenario/effect/impl/state/GaussianBlurState.class */
public class GaussianBlurState extends HVSeparableKernel {
    private float hradius;
    private float vradius;

    void checkRadius(float radius) {
        if (radius < 0.0f || radius > 63.0f) {
            throw new IllegalArgumentException("Radius must be in the range [1,63]");
        }
    }

    public float getRadius() {
        return (this.hradius + this.vradius) / 2.0f;
    }

    public void setRadius(float radius) {
        checkRadius(radius);
        this.hradius = radius;
        this.vradius = radius;
    }

    public float getHRadius() {
        return this.hradius;
    }

    public void setHRadius(float hradius) {
        checkRadius(hradius);
        this.hradius = hradius;
    }

    public float getVRadius() {
        return this.vradius;
    }

    public void setVRadius(float vradius) {
        checkRadius(vradius);
        this.vradius = vradius;
    }

    float getRadius(int pass) {
        return pass == 0 ? this.hradius : this.vradius;
    }

    @Override // com.sun.scenario.effect.impl.state.LinearConvolveKernel
    public boolean isNop() {
        return this.hradius == 0.0f && this.vradius == 0.0f;
    }

    public int getPad(int pass) {
        return (int) Math.ceil(getRadius(pass));
    }

    @Override // com.sun.scenario.effect.impl.state.LinearConvolveKernel
    public int getKernelSize(int pass) {
        return (getPad(pass) * 2) + 1;
    }

    public float getSpread() {
        return 0.0f;
    }

    public Color4f getShadowColor() {
        return null;
    }

    @Override // com.sun.scenario.effect.impl.state.LinearConvolveKernel
    public LinearConvolveRenderState getRenderState(BaseTransform filtertx) {
        return new GaussianRenderState(this.hradius, this.vradius, getSpread(), this instanceof GaussianShadowState, getShadowColor(), filtertx);
    }
}
