package com.sun.scenario.effect;

import com.sun.scenario.effect.AbstractShadow;

/* loaded from: jfxrt.jar:com/sun/scenario/effect/GeneralShadow.class */
public class GeneralShadow extends DelegateEffect {
    private AbstractShadow shadow;

    public GeneralShadow() {
        this(DefaultInput);
    }

    public GeneralShadow(Effect input) {
        super(input);
        this.shadow = new GaussianShadow(10.0f, Color4f.BLACK, input);
    }

    public AbstractShadow.ShadowMode getShadowMode() {
        return this.shadow.getMode();
    }

    public void setShadowMode(AbstractShadow.ShadowMode mode) {
        this.shadow.getMode();
        this.shadow = this.shadow.implFor(mode);
    }

    @Override // com.sun.scenario.effect.DelegateEffect
    protected Effect getDelegate() {
        return this.shadow;
    }

    public final Effect getInput() {
        return this.shadow.getInput();
    }

    public void setInput(Effect input) {
        this.shadow.setInput(input);
    }

    public float getRadius() {
        return this.shadow.getGaussianRadius();
    }

    public void setRadius(float radius) {
        this.shadow.getGaussianRadius();
        this.shadow.setGaussianRadius(radius);
    }

    public float getGaussianRadius() {
        return this.shadow.getGaussianRadius();
    }

    public float getGaussianWidth() {
        return this.shadow.getGaussianWidth();
    }

    public float getGaussianHeight() {
        return this.shadow.getGaussianHeight();
    }

    public void setGaussianRadius(float r2) {
        setRadius(r2);
    }

    public void setGaussianWidth(float w2) {
        this.shadow.getGaussianWidth();
        this.shadow.setGaussianWidth(w2);
    }

    public void setGaussianHeight(float h2) {
        this.shadow.getGaussianHeight();
        this.shadow.setGaussianHeight(h2);
    }

    public float getSpread() {
        return this.shadow.getSpread();
    }

    public void setSpread(float spread) {
        this.shadow.getSpread();
        this.shadow.setSpread(spread);
    }

    public Color4f getColor() {
        return this.shadow.getColor();
    }

    public void setColor(Color4f color) {
        this.shadow.getColor();
        this.shadow.setColor(color);
    }
}
