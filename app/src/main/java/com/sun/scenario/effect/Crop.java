package com.sun.scenario.effect;

import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.DirtyRegionContainer;
import com.sun.javafx.geom.DirtyRegionPool;
import com.sun.javafx.geom.Point2D;
import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.scenario.effect.Effect;
import com.sun.scenario.effect.impl.state.RenderState;

/* loaded from: jfxrt.jar:com/sun/scenario/effect/Crop.class */
public class Crop extends CoreEffect<RenderState> {
    @Override // com.sun.scenario.effect.CoreEffect, com.sun.scenario.effect.Effect
    public /* bridge */ /* synthetic */ Effect.AccelType getAccelType(FilterContext filterContext) {
        return super.getAccelType(filterContext);
    }

    @Override // com.sun.scenario.effect.CoreEffect, com.sun.scenario.effect.FilterEffect
    public /* bridge */ /* synthetic */ ImageData filterImageDatas(FilterContext filterContext, BaseTransform baseTransform, Rectangle rectangle, RenderState renderState, ImageData[] imageDataArr) {
        return super.filterImageDatas(filterContext, baseTransform, rectangle, renderState, imageDataArr);
    }

    public Crop(Effect source) {
        this(source, DefaultInput);
    }

    public Crop(Effect source, Effect boundsInput) {
        super(source, boundsInput);
        updatePeerKey("Crop");
    }

    public final Effect getInput() {
        return getInputs().get(0);
    }

    public void setInput(Effect input) {
        setInput(0, input);
    }

    public final Effect getBoundsInput() {
        return getInputs().get(1);
    }

    public void setBoundsInput(Effect input) {
        setInput(1, input);
    }

    private Effect getBoundsInput(Effect defaultInput) {
        return getDefaultedInput(1, defaultInput);
    }

    @Override // com.sun.scenario.effect.FilterEffect, com.sun.scenario.effect.Effect
    public BaseBounds getBounds(BaseTransform transform, Effect defaultInput) {
        return getBoundsInput(defaultInput).getBounds(transform, defaultInput);
    }

    @Override // com.sun.scenario.effect.FilterEffect, com.sun.scenario.effect.Effect
    public Point2D transform(Point2D p2, Effect defaultInput) {
        return getDefaultedInput(0, defaultInput).transform(p2, defaultInput);
    }

    @Override // com.sun.scenario.effect.FilterEffect, com.sun.scenario.effect.Effect
    public Point2D untransform(Point2D p2, Effect defaultInput) {
        return getDefaultedInput(0, defaultInput).untransform(p2, defaultInput);
    }

    @Override // com.sun.scenario.effect.FilterEffect, com.sun.scenario.effect.Effect
    public ImageData filter(FilterContext fctx, BaseTransform transform, Rectangle outputClip, Object renderHelper, Effect defaultInput) {
        Effect input1 = getDefaultedInput(1, defaultInput);
        BaseBounds cropBounds = input1.getBounds(transform, defaultInput);
        Rectangle cropRect = new Rectangle(cropBounds);
        cropRect.intersectWith(outputClip);
        Effect input0 = getDefaultedInput(0, defaultInput);
        ImageData id = input0.filter(fctx, transform, cropRect, null, defaultInput);
        if (!id.validate(fctx)) {
            return new ImageData(fctx, (Filterable) null, (Rectangle) null);
        }
        RenderState rstate = getRenderState(fctx, transform, cropRect, renderHelper, defaultInput);
        ImageData ret = filterImageDatas(fctx, transform, cropRect, rstate, new ImageData[]{id});
        id.unref();
        return ret;
    }

    @Override // com.sun.scenario.effect.FilterEffect
    public RenderState getRenderState(FilterContext fctx, BaseTransform transform, Rectangle outputClip, Object renderHelper, Effect defaultInput) {
        return RenderState.RenderSpaceRenderState;
    }

    @Override // com.sun.scenario.effect.Effect
    public boolean reducesOpaquePixels() {
        return true;
    }

    @Override // com.sun.scenario.effect.Effect
    public DirtyRegionContainer getDirtyRegions(Effect defaultInput, DirtyRegionPool regionPool) {
        Effect di0 = getDefaultedInput(0, defaultInput);
        DirtyRegionContainer drc = di0.getDirtyRegions(defaultInput, regionPool);
        Effect di1 = getDefaultedInput(1, defaultInput);
        BaseBounds cropBounds = di1.getBounds(BaseTransform.IDENTITY_TRANSFORM, defaultInput);
        int i2 = 0;
        while (i2 < drc.size()) {
            drc.getDirtyRegion(i2).intersectWith(cropBounds);
            if (drc.checkAndClearRegion(i2)) {
                i2--;
            }
            i2++;
        }
        return drc;
    }
}
