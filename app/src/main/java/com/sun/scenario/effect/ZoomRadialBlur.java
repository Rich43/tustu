package com.sun.scenario.effect;

import com.sun.javafx.geom.DirtyRegionContainer;
import com.sun.javafx.geom.DirtyRegionPool;
import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.scenario.effect.Effect;
import com.sun.scenario.effect.impl.state.RenderState;
import com.sun.scenario.effect.impl.state.ZoomRadialBlurState;

/* loaded from: jfxrt.jar:com/sun/scenario/effect/ZoomRadialBlur.class */
public class ZoomRadialBlur extends CoreEffect<RenderState> {

    /* renamed from: r, reason: collision with root package name */
    private int f12037r;
    private float centerX;
    private float centerY;
    private final ZoomRadialBlurState state;

    @Override // com.sun.scenario.effect.CoreEffect, com.sun.scenario.effect.Effect
    public /* bridge */ /* synthetic */ Effect.AccelType getAccelType(FilterContext filterContext) {
        return super.getAccelType(filterContext);
    }

    public ZoomRadialBlur() {
        this(1);
    }

    public ZoomRadialBlur(int radius) {
        this(radius, DefaultInput);
    }

    public ZoomRadialBlur(int radius, Effect input) {
        super(input);
        this.state = new ZoomRadialBlurState(this);
        setRadius(radius);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.sun.scenario.effect.Effect
    public Object getState() {
        return this.state;
    }

    public final Effect getInput() {
        return getInputs().get(0);
    }

    public void setInput(Effect input) {
        setInput(0, input);
    }

    public int getRadius() {
        return this.f12037r;
    }

    public void setRadius(int radius) {
        if (radius < 1 || radius > 64) {
            throw new IllegalArgumentException("Radius must be in the range [1,64]");
        }
        int i2 = this.f12037r;
        this.f12037r = radius;
        this.state.invalidateDeltas();
        updatePeer();
    }

    private void updatePeer() {
        int psize = (4 + this.f12037r) - (this.f12037r % 4);
        updatePeerKey("ZoomRadialBlur", psize);
    }

    public float getCenterX() {
        return this.centerX;
    }

    public void setCenterX(float centerX) {
        float f2 = this.centerX;
        this.centerX = centerX;
    }

    public float getCenterY() {
        return this.centerY;
    }

    public void setCenterY(float centerY) {
        float f2 = this.centerY;
        this.centerY = centerY;
    }

    @Override // com.sun.scenario.effect.CoreEffect, com.sun.scenario.effect.FilterEffect
    public ImageData filterImageDatas(FilterContext fctx, BaseTransform transform, Rectangle outputClip, RenderState rstate, ImageData... inputs) {
        Rectangle bnd = inputs[0].getUntransformedBounds();
        this.state.updateDeltas(1.0f / bnd.width, 1.0f / bnd.height);
        return super.filterImageDatas(fctx, transform, outputClip, rstate, inputs);
    }

    @Override // com.sun.scenario.effect.FilterEffect
    public RenderState getRenderState(FilterContext fctx, BaseTransform transform, Rectangle outputClip, Object renderHelper, Effect defaultInput) {
        return RenderState.UserSpaceRenderState;
    }

    @Override // com.sun.scenario.effect.Effect
    public boolean reducesOpaquePixels() {
        return true;
    }

    @Override // com.sun.scenario.effect.Effect
    public DirtyRegionContainer getDirtyRegions(Effect defaultInput, DirtyRegionPool regionPool) {
        Effect di = getDefaultedInput(0, defaultInput);
        DirtyRegionContainer drc = di.getDirtyRegions(defaultInput, regionPool);
        int radius = getRadius();
        drc.grow(radius, radius);
        return drc;
    }
}
