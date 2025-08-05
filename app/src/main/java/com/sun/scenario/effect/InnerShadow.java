package com.sun.scenario.effect;

import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.Point2D;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.scenario.effect.AbstractShadow;
import com.sun.scenario.effect.Blend;

/* loaded from: jfxrt.jar:com/sun/scenario/effect/InnerShadow.class */
public class InnerShadow extends DelegateEffect {
    private final InvertMask invert;
    private AbstractShadow shadow;
    private final Blend blend;

    public InnerShadow() {
        this(DefaultInput, DefaultInput);
    }

    public InnerShadow(Effect input) {
        this(input, input);
    }

    public InnerShadow(Effect shadowSourceInput, Effect contentInput) {
        super(shadowSourceInput, contentInput);
        this.invert = new InvertMask(10, shadowSourceInput);
        this.shadow = new GaussianShadow(10.0f, Color4f.BLACK, this.invert);
        this.blend = new Blend(Blend.Mode.SRC_ATOP, contentInput, this.shadow);
    }

    public AbstractShadow.ShadowMode getShadowMode() {
        return this.shadow.getMode();
    }

    public void setShadowMode(AbstractShadow.ShadowMode mode) {
        this.shadow.getMode();
        AbstractShadow s2 = this.shadow.implFor(mode);
        if (s2 != this.shadow) {
            this.blend.setTopInput(s2);
        }
        this.shadow = s2;
    }

    @Override // com.sun.scenario.effect.DelegateEffect
    protected Effect getDelegate() {
        return this.blend;
    }

    @Override // com.sun.scenario.effect.DelegateEffect, com.sun.scenario.effect.Effect
    public BaseBounds getBounds(BaseTransform transform, Effect defaultInput) {
        Effect input = getDefaultedInput(getContentInput(), defaultInput);
        return input.getBounds(transform, defaultInput);
    }

    public final Effect getShadowSourceInput() {
        return this.invert.getInput();
    }

    public void setShadowSourceInput(Effect shadowSourceInput) {
        this.invert.setInput(shadowSourceInput);
    }

    public final Effect getContentInput() {
        return this.blend.getBottomInput();
    }

    public void setContentInput(Effect contentInput) {
        this.blend.setBottomInput(contentInput);
    }

    public float getRadius() {
        return this.shadow.getGaussianRadius();
    }

    public void setRadius(float radius) {
        this.shadow.getGaussianRadius();
        this.invert.setPad((int) Math.ceil(radius));
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
        float maxr = (Math.max(w2, this.shadow.getGaussianHeight()) - 1.0f) / 2.0f;
        this.invert.setPad((int) Math.ceil(maxr));
        this.shadow.setGaussianWidth(w2);
    }

    public void setGaussianHeight(float h2) {
        this.shadow.getGaussianHeight();
        float maxr = (Math.max(this.shadow.getGaussianWidth(), h2) - 1.0f) / 2.0f;
        this.invert.setPad((int) Math.ceil(maxr));
        this.shadow.setGaussianHeight(h2);
    }

    public float getChoke() {
        return this.shadow.getSpread();
    }

    public void setChoke(float choke) {
        this.shadow.getSpread();
        this.shadow.setSpread(choke);
    }

    public Color4f getColor() {
        return this.shadow.getColor();
    }

    public void setColor(Color4f color) {
        this.shadow.getColor();
        this.shadow.setColor(color);
    }

    public int getOffsetX() {
        return this.invert.getOffsetX();
    }

    public void setOffsetX(int xoff) {
        this.invert.getOffsetX();
        this.invert.setOffsetX(xoff);
    }

    public int getOffsetY() {
        return this.invert.getOffsetY();
    }

    public void setOffsetY(int yoff) {
        this.invert.getOffsetY();
        this.invert.setOffsetY(yoff);
    }

    @Override // com.sun.scenario.effect.DelegateEffect, com.sun.scenario.effect.Effect
    public Point2D transform(Point2D p2, Effect defaultInput) {
        return getDefaultedInput(1, defaultInput).transform(p2, defaultInput);
    }

    @Override // com.sun.scenario.effect.DelegateEffect, com.sun.scenario.effect.Effect
    public Point2D untransform(Point2D p2, Effect defaultInput) {
        return getDefaultedInput(1, defaultInput).untransform(p2, defaultInput);
    }
}
