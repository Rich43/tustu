package com.sun.scenario.effect;

import com.sun.scenario.effect.AbstractShadow;
import com.sun.scenario.effect.Effect;

/* loaded from: jfxrt.jar:com/sun/scenario/effect/DropShadow.class */
public class DropShadow extends DelegateEffect {
    private AbstractShadow shadow;
    private final Offset offset;
    private final Merge merge;

    public DropShadow() {
        this(DefaultInput, DefaultInput);
    }

    public DropShadow(Effect input) {
        this(input, input);
    }

    public DropShadow(Effect shadowSourceInput, Effect contentInput) {
        super(shadowSourceInput, contentInput);
        this.shadow = new GaussianShadow(10.0f, Color4f.BLACK, shadowSourceInput);
        this.offset = new Offset(0, 0, this.shadow);
        this.merge = new Merge(this.offset, contentInput);
    }

    public AbstractShadow.ShadowMode getShadowMode() {
        return this.shadow.getMode();
    }

    public void setShadowMode(AbstractShadow.ShadowMode mode) {
        this.shadow.getMode();
        AbstractShadow s2 = this.shadow.implFor(mode);
        if (s2 != this.shadow) {
            this.offset.setInput(s2);
        }
        this.shadow = s2;
    }

    @Override // com.sun.scenario.effect.DelegateEffect
    protected Effect getDelegate() {
        return this.merge;
    }

    public final Effect getShadowSourceInput() {
        return this.shadow.getInput();
    }

    public void setShadowSourceInput(Effect shadowSourceInput) {
        this.shadow.setInput(shadowSourceInput);
    }

    public final Effect getContentInput() {
        return this.merge.getTopInput();
    }

    public void setContentInput(Effect contentInput) {
        this.merge.setTopInput(contentInput);
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

    public int getOffsetX() {
        return this.offset.getX();
    }

    public void setOffsetX(int xoff) {
        this.offset.getX();
        this.offset.setX(xoff);
    }

    public int getOffsetY() {
        return this.offset.getY();
    }

    public void setOffsetY(int yoff) {
        this.offset.getY();
        this.offset.setY(yoff);
    }

    @Override // com.sun.scenario.effect.DelegateEffect, com.sun.scenario.effect.Effect
    public Effect.AccelType getAccelType(FilterContext fctx) {
        return this.shadow.getAccelType(fctx);
    }
}
