package com.sun.scenario.effect.impl.state;

import com.sun.scenario.effect.Color4f;

/* loaded from: jfxrt.jar:com/sun/scenario/effect/impl/state/GaussianShadowState.class */
public class GaussianShadowState extends GaussianBlurState {
    private Color4f shadowColor;
    private float spread;

    @Override // com.sun.scenario.effect.impl.state.GaussianBlurState
    void checkRadius(float radius) {
        if (radius < 0.0f || radius > 127.0f) {
            throw new IllegalArgumentException("Radius must be in the range [1,127]");
        }
    }

    @Override // com.sun.scenario.effect.impl.state.GaussianBlurState
    public Color4f getShadowColor() {
        return this.shadowColor;
    }

    public void setShadowColor(Color4f shadowColor) {
        if (shadowColor == null) {
            throw new IllegalArgumentException("Color must be non-null");
        }
        this.shadowColor = shadowColor;
    }

    @Override // com.sun.scenario.effect.impl.state.GaussianBlurState
    public float getSpread() {
        return this.spread;
    }

    public void setSpread(float spread) {
        if (spread < 0.0f || spread > 1.0f) {
            throw new IllegalArgumentException("Spread must be in the range [0,1]");
        }
        this.spread = spread;
    }

    @Override // com.sun.scenario.effect.impl.state.GaussianBlurState, com.sun.scenario.effect.impl.state.LinearConvolveKernel
    public boolean isNop() {
        return false;
    }

    @Override // com.sun.scenario.effect.impl.state.LinearConvolveKernel
    public boolean isShadow() {
        return true;
    }
}
