package com.sun.scenario.effect;

import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.DirtyRegionContainer;
import com.sun.javafx.geom.DirtyRegionPool;
import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.scenario.effect.AbstractShadow;
import com.sun.scenario.effect.Effect;
import com.sun.scenario.effect.impl.Renderer;
import com.sun.scenario.effect.impl.state.GaussianShadowState;
import com.sun.scenario.effect.impl.state.LinearConvolveKernel;

/* loaded from: jfxrt.jar:com/sun/scenario/effect/GaussianShadow.class */
public class GaussianShadow extends AbstractShadow {
    private GaussianShadowState state;

    public GaussianShadow() {
        this(10.0f);
    }

    public GaussianShadow(float radius) {
        this(radius, Color4f.BLACK);
    }

    public GaussianShadow(float radius, Color4f color) {
        this(radius, color, DefaultInput);
    }

    public GaussianShadow(float radius, Color4f color, Effect input) {
        super(input);
        this.state = new GaussianShadowState();
        this.state.setRadius(radius);
        this.state.setShadowColor(color);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.sun.scenario.effect.LinearConvolveCoreEffect, com.sun.scenario.effect.Effect
    public LinearConvolveKernel getState() {
        return this.state;
    }

    @Override // com.sun.scenario.effect.LinearConvolveCoreEffect, com.sun.scenario.effect.CoreEffect, com.sun.scenario.effect.Effect
    public Effect.AccelType getAccelType(FilterContext fctx) {
        return Renderer.getRenderer(fctx).getAccelType();
    }

    @Override // com.sun.scenario.effect.AbstractShadow
    public final Effect getInput() {
        return getInputs().get(0);
    }

    @Override // com.sun.scenario.effect.AbstractShadow
    public void setInput(Effect input) {
        setInput(0, input);
    }

    public float getRadius() {
        return this.state.getRadius();
    }

    public void setRadius(float radius) {
        this.state.getRadius();
        this.state.setRadius(radius);
    }

    public float getHRadius() {
        return this.state.getHRadius();
    }

    public void setHRadius(float hradius) {
        this.state.getHRadius();
        this.state.setHRadius(hradius);
    }

    public float getVRadius() {
        return this.state.getVRadius();
    }

    public void setVRadius(float vradius) {
        this.state.getVRadius();
        this.state.setVRadius(vradius);
    }

    @Override // com.sun.scenario.effect.AbstractShadow
    public float getSpread() {
        return this.state.getSpread();
    }

    @Override // com.sun.scenario.effect.AbstractShadow
    public void setSpread(float spread) {
        this.state.getSpread();
        this.state.setSpread(spread);
    }

    @Override // com.sun.scenario.effect.AbstractShadow
    public Color4f getColor() {
        return this.state.getShadowColor();
    }

    @Override // com.sun.scenario.effect.AbstractShadow
    public void setColor(Color4f color) {
        this.state.getShadowColor();
        this.state.setShadowColor(color);
    }

    @Override // com.sun.scenario.effect.AbstractShadow
    public float getGaussianRadius() {
        return getRadius();
    }

    @Override // com.sun.scenario.effect.AbstractShadow
    public float getGaussianWidth() {
        return (getHRadius() * 2.0f) + 1.0f;
    }

    @Override // com.sun.scenario.effect.AbstractShadow
    public float getGaussianHeight() {
        return (getVRadius() * 2.0f) + 1.0f;
    }

    @Override // com.sun.scenario.effect.AbstractShadow
    public void setGaussianRadius(float r2) {
        setRadius(r2);
    }

    @Override // com.sun.scenario.effect.AbstractShadow
    public void setGaussianWidth(float w2) {
        setHRadius(w2 < 1.0f ? 0.0f : (w2 - 1.0f) / 2.0f);
    }

    @Override // com.sun.scenario.effect.AbstractShadow
    public void setGaussianHeight(float h2) {
        setVRadius(h2 < 1.0f ? 0.0f : (h2 - 1.0f) / 2.0f);
    }

    @Override // com.sun.scenario.effect.AbstractShadow
    public AbstractShadow.ShadowMode getMode() {
        return AbstractShadow.ShadowMode.GAUSSIAN;
    }

    @Override // com.sun.scenario.effect.AbstractShadow
    public AbstractShadow implFor(AbstractShadow.ShadowMode mode) {
        int passes = 0;
        switch (mode) {
            case GAUSSIAN:
                return this;
            case ONE_PASS_BOX:
                passes = 1;
                break;
            case TWO_PASS_BOX:
                passes = 2;
                break;
            case THREE_PASS_BOX:
                passes = 3;
                break;
        }
        BoxShadow box = new BoxShadow();
        box.setInput(getInput());
        box.setGaussianWidth(getGaussianWidth());
        box.setGaussianHeight(getGaussianHeight());
        box.setColor(getColor());
        box.setPasses(passes);
        box.setSpread(getSpread());
        return box;
    }

    @Override // com.sun.scenario.effect.FilterEffect, com.sun.scenario.effect.Effect
    public BaseBounds getBounds(BaseTransform transform, Effect defaultInput) {
        BaseBounds r2 = super.getBounds(null, defaultInput);
        int hpad = this.state.getPad(0);
        int vpad = this.state.getPad(1);
        RectBounds ret = new RectBounds(r2.getMinX(), r2.getMinY(), r2.getMaxX(), r2.getMaxY());
        ret.grow(hpad, vpad);
        return transformBounds(transform, ret);
    }

    @Override // com.sun.scenario.effect.Effect
    public Rectangle getResultBounds(BaseTransform transform, Rectangle outputClip, ImageData... inputDatas) {
        Rectangle r2 = super.getResultBounds(transform, outputClip, inputDatas);
        int hpad = this.state.getPad(0);
        int vpad = this.state.getPad(1);
        Rectangle ret = new Rectangle(r2);
        ret.grow(hpad, vpad);
        return ret;
    }

    @Override // com.sun.scenario.effect.Effect
    public boolean reducesOpaquePixels() {
        return true;
    }

    @Override // com.sun.scenario.effect.Effect
    public DirtyRegionContainer getDirtyRegions(Effect defaultInput, DirtyRegionPool regionPool) {
        Effect di = getDefaultedInput(0, defaultInput);
        DirtyRegionContainer drc = di.getDirtyRegions(defaultInput, regionPool);
        drc.grow(this.state.getPad(0), this.state.getPad(1));
        return drc;
    }
}
