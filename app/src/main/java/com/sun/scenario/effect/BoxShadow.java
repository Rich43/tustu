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
import com.sun.scenario.effect.impl.state.BoxShadowState;
import com.sun.scenario.effect.impl.state.LinearConvolveKernel;

/* loaded from: jfxrt.jar:com/sun/scenario/effect/BoxShadow.class */
public class BoxShadow extends AbstractShadow {
    private final BoxShadowState state;

    public BoxShadow() {
        this(1, 1);
    }

    public BoxShadow(int hsize, int vsize) {
        this(hsize, vsize, 1, DefaultInput);
    }

    public BoxShadow(int hsize, int vsize, int passes) {
        this(hsize, vsize, passes, DefaultInput);
    }

    public BoxShadow(int hsize, int vsize, int passes, Effect input) {
        super(input);
        this.state = new BoxShadowState();
        setHorizontalSize(hsize);
        setVerticalSize(vsize);
        setPasses(passes);
        setColor(Color4f.BLACK);
        setSpread(0.0f);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.sun.scenario.effect.LinearConvolveCoreEffect, com.sun.scenario.effect.Effect
    public LinearConvolveKernel getState() {
        return this.state;
    }

    @Override // com.sun.scenario.effect.AbstractShadow
    public final Effect getInput() {
        return getInputs().get(0);
    }

    @Override // com.sun.scenario.effect.AbstractShadow
    public void setInput(Effect input) {
        setInput(0, input);
    }

    public int getHorizontalSize() {
        return this.state.getHsize();
    }

    public final void setHorizontalSize(int hsize) {
        this.state.setHsize(hsize);
    }

    public int getVerticalSize() {
        return this.state.getVsize();
    }

    public final void setVerticalSize(int vsize) {
        this.state.setVsize(vsize);
    }

    public int getPasses() {
        return this.state.getBlurPasses();
    }

    public final void setPasses(int passes) {
        this.state.setBlurPasses(passes);
    }

    @Override // com.sun.scenario.effect.AbstractShadow
    public Color4f getColor() {
        return this.state.getShadowColor();
    }

    @Override // com.sun.scenario.effect.AbstractShadow
    public final void setColor(Color4f color) {
        this.state.setShadowColor(color);
    }

    @Override // com.sun.scenario.effect.AbstractShadow
    public float getSpread() {
        return this.state.getSpread();
    }

    @Override // com.sun.scenario.effect.AbstractShadow
    public final void setSpread(float spread) {
        this.state.setSpread(spread);
    }

    @Override // com.sun.scenario.effect.AbstractShadow
    public float getGaussianRadius() {
        float d2 = ((getHorizontalSize() + getVerticalSize()) / 2.0f) * 3.0f;
        if (d2 < 1.0f) {
            return 0.0f;
        }
        return (d2 - 1.0f) / 2.0f;
    }

    @Override // com.sun.scenario.effect.AbstractShadow
    public float getGaussianWidth() {
        return getHorizontalSize() * 3.0f;
    }

    @Override // com.sun.scenario.effect.AbstractShadow
    public float getGaussianHeight() {
        return getVerticalSize() * 3.0f;
    }

    @Override // com.sun.scenario.effect.AbstractShadow
    public void setGaussianRadius(float r2) {
        float d2 = (r2 * 2.0f) + 1.0f;
        setGaussianWidth(d2);
        setGaussianHeight(d2);
    }

    @Override // com.sun.scenario.effect.AbstractShadow
    public void setGaussianWidth(float w2) {
        setHorizontalSize(Math.round(w2 / 3.0f));
    }

    @Override // com.sun.scenario.effect.AbstractShadow
    public void setGaussianHeight(float h2) {
        setVerticalSize(Math.round(h2 / 3.0f));
    }

    @Override // com.sun.scenario.effect.AbstractShadow
    public AbstractShadow.ShadowMode getMode() {
        switch (getPasses()) {
            case 1:
                return AbstractShadow.ShadowMode.ONE_PASS_BOX;
            case 2:
                return AbstractShadow.ShadowMode.TWO_PASS_BOX;
            default:
                return AbstractShadow.ShadowMode.THREE_PASS_BOX;
        }
    }

    @Override // com.sun.scenario.effect.AbstractShadow
    public AbstractShadow implFor(AbstractShadow.ShadowMode mode) {
        switch (mode) {
            case GAUSSIAN:
                GaussianShadow gs = new GaussianShadow();
                gs.setInput(getInput());
                gs.setGaussianWidth(getGaussianWidth());
                gs.setGaussianHeight(getGaussianHeight());
                gs.setColor(getColor());
                gs.setSpread(getSpread());
                return gs;
            case ONE_PASS_BOX:
                setPasses(1);
                break;
            case TWO_PASS_BOX:
                setPasses(2);
                break;
            case THREE_PASS_BOX:
                setPasses(3);
                break;
        }
        return this;
    }

    @Override // com.sun.scenario.effect.LinearConvolveCoreEffect, com.sun.scenario.effect.CoreEffect, com.sun.scenario.effect.Effect
    public Effect.AccelType getAccelType(FilterContext fctx) {
        return Renderer.getRenderer(fctx).getAccelType();
    }

    @Override // com.sun.scenario.effect.FilterEffect, com.sun.scenario.effect.Effect
    public BaseBounds getBounds(BaseTransform transform, Effect defaultInput) {
        BaseBounds r2 = super.getBounds(null, defaultInput);
        int hgrow = this.state.getKernelSize(0) / 2;
        int vgrow = this.state.getKernelSize(1) / 2;
        RectBounds ret = new RectBounds(r2.getMinX(), r2.getMinY(), r2.getMaxX(), r2.getMaxY());
        ret.grow(hgrow, vgrow);
        return transformBounds(transform, ret);
    }

    @Override // com.sun.scenario.effect.Effect
    public Rectangle getResultBounds(BaseTransform transform, Rectangle outputClip, ImageData... inputDatas) {
        Rectangle r2 = inputDatas[0].getUntransformedBounds();
        Rectangle r3 = this.state.getResultBounds(this.state.getResultBounds(r2, 0), 1);
        r3.intersectWith(outputClip);
        return r3;
    }

    @Override // com.sun.scenario.effect.Effect
    public boolean reducesOpaquePixels() {
        return true;
    }

    @Override // com.sun.scenario.effect.Effect
    public DirtyRegionContainer getDirtyRegions(Effect defaultInput, DirtyRegionPool regionPool) {
        Effect di = getDefaultedInput(0, defaultInput);
        DirtyRegionContainer drc = di.getDirtyRegions(defaultInput, regionPool);
        drc.grow(this.state.getKernelSize(0) / 2, this.state.getKernelSize(1) / 2);
        return drc;
    }
}
