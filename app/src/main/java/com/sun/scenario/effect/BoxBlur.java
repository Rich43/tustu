package com.sun.scenario.effect;

import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.DirtyRegionContainer;
import com.sun.javafx.geom.DirtyRegionPool;
import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.scenario.effect.Effect;
import com.sun.scenario.effect.impl.Renderer;
import com.sun.scenario.effect.impl.state.BoxBlurState;
import com.sun.scenario.effect.impl.state.LinearConvolveKernel;

/* loaded from: jfxrt.jar:com/sun/scenario/effect/BoxBlur.class */
public class BoxBlur extends LinearConvolveCoreEffect {
    private final BoxBlurState state;

    public BoxBlur() {
        this(1, 1);
    }

    public BoxBlur(int hsize, int vsize) {
        this(hsize, vsize, 1, DefaultInput);
    }

    public BoxBlur(int hsize, int vsize, int passes) {
        this(hsize, vsize, passes, DefaultInput);
    }

    public BoxBlur(int hsize, int vsize, int passes, Effect input) {
        super(input);
        this.state = new BoxBlurState();
        setHorizontalSize(hsize);
        setVerticalSize(vsize);
        setPasses(passes);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.sun.scenario.effect.LinearConvolveCoreEffect, com.sun.scenario.effect.Effect
    public LinearConvolveKernel getState() {
        return this.state;
    }

    public final Effect getInput() {
        return getInputs().get(0);
    }

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
        Rectangle r2 = inputDatas[0].getTransformedBounds(null);
        Rectangle r3 = this.state.getResultBounds(this.state.getResultBounds(r2, 0), 1);
        r3.intersectWith(outputClip);
        return r3;
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
        drc.grow(this.state.getKernelSize(0) / 2, this.state.getKernelSize(1) / 2);
        return drc;
    }
}
