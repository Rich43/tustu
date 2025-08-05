package com.sun.scenario.effect.impl.state;

import com.sun.scenario.effect.Color4f;

/* loaded from: jfxrt.jar:com/sun/scenario/effect/impl/state/BoxShadowState.class */
public class BoxShadowState extends BoxBlurState {
    private Color4f shadowColor;
    private float spread;

    @Override // com.sun.scenario.effect.impl.state.BoxBlurState
    public Color4f getShadowColor() {
        return this.shadowColor;
    }

    public void setShadowColor(Color4f shadowColor) {
        if (shadowColor == null) {
            throw new IllegalArgumentException("Color must be non-null");
        }
        this.shadowColor = shadowColor;
    }

    @Override // com.sun.scenario.effect.impl.state.BoxBlurState
    public float getSpread() {
        return this.spread;
    }

    public void setSpread(float spread) {
        if (spread < 0.0f || spread > 1.0f) {
            throw new IllegalArgumentException("Spread must be in the range [0,1]");
        }
        this.spread = spread;
    }

    @Override // com.sun.scenario.effect.impl.state.BoxBlurState, com.sun.scenario.effect.impl.state.LinearConvolveKernel
    public boolean isNop() {
        return false;
    }

    @Override // com.sun.scenario.effect.impl.state.LinearConvolveKernel
    public boolean isShadow() {
        return true;
    }
}
