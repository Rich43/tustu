package com.sun.scenario.effect;

import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.DirtyRegionContainer;
import com.sun.javafx.geom.DirtyRegionPool;
import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.scenario.effect.Effect;
import com.sun.scenario.effect.impl.Renderer;
import com.sun.scenario.effect.impl.state.GaussianBlurState;
import com.sun.scenario.effect.impl.state.LinearConvolveKernel;

/* loaded from: jfxrt.jar:com/sun/scenario/effect/GaussianBlur.class */
public class GaussianBlur extends LinearConvolveCoreEffect {
    private GaussianBlurState state;

    public GaussianBlur() {
        this(10.0f, DefaultInput);
    }

    public GaussianBlur(float radius) {
        this(radius, DefaultInput);
    }

    public GaussianBlur(float radius, Effect input) {
        super(input);
        this.state = new GaussianBlurState();
        this.state.setRadius(radius);
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

    public final Effect getInput() {
        return getInputs().get(0);
    }

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
        if (!this.state.isNop()) {
            return true;
        }
        Effect input = getInput();
        return input != null && input.reducesOpaquePixels();
    }

    @Override // com.sun.scenario.effect.Effect
    public DirtyRegionContainer getDirtyRegions(Effect defaultInput, DirtyRegionPool regionPool) {
        Effect di = getDefaultedInput(0, defaultInput);
        DirtyRegionContainer drc = di.getDirtyRegions(defaultInput, regionPool);
        drc.grow(this.state.getPad(0), this.state.getPad(1));
        return drc;
    }
}
