package com.sun.scenario.effect;

import com.sun.javafx.geom.Point2D;
import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.scenario.effect.Effect;
import com.sun.scenario.effect.impl.state.RenderState;

/* loaded from: jfxrt.jar:com/sun/scenario/effect/Merge.class */
public class Merge extends CoreEffect<RenderState> {
    @Override // com.sun.scenario.effect.CoreEffect, com.sun.scenario.effect.Effect
    public /* bridge */ /* synthetic */ Effect.AccelType getAccelType(FilterContext filterContext) {
        return super.getAccelType(filterContext);
    }

    @Override // com.sun.scenario.effect.CoreEffect, com.sun.scenario.effect.FilterEffect
    public /* bridge */ /* synthetic */ ImageData filterImageDatas(FilterContext filterContext, BaseTransform baseTransform, Rectangle rectangle, RenderState renderState, ImageData[] imageDataArr) {
        return super.filterImageDatas(filterContext, baseTransform, rectangle, renderState, imageDataArr);
    }

    public Merge(Effect bottomInput, Effect topInput) {
        super(bottomInput, topInput);
        updatePeerKey("Merge");
    }

    public final Effect getBottomInput() {
        return getInputs().get(0);
    }

    public void setBottomInput(Effect bottomInput) {
        setInput(0, bottomInput);
    }

    public final Effect getTopInput() {
        return getInputs().get(1);
    }

    public void setTopInput(Effect topInput) {
        setInput(1, topInput);
    }

    @Override // com.sun.scenario.effect.FilterEffect, com.sun.scenario.effect.Effect
    public Point2D transform(Point2D p2, Effect defaultInput) {
        return getDefaultedInput(1, defaultInput).transform(p2, defaultInput);
    }

    @Override // com.sun.scenario.effect.FilterEffect, com.sun.scenario.effect.Effect
    public Point2D untransform(Point2D p2, Effect defaultInput) {
        return getDefaultedInput(1, defaultInput).untransform(p2, defaultInput);
    }

    @Override // com.sun.scenario.effect.FilterEffect, com.sun.scenario.effect.Effect
    public ImageData filter(FilterContext fctx, BaseTransform transform, Rectangle outputClip, Object renderHelper, Effect defaultInput) {
        Effect botinput = getDefaultedInput(0, defaultInput);
        Effect topinput = getDefaultedInput(1, defaultInput);
        ImageData botimg = botinput.filter(fctx, transform, outputClip, renderHelper, defaultInput);
        if (botimg != null) {
            if (!botimg.validate(fctx)) {
                return new ImageData(fctx, (Filterable) null, (Rectangle) null);
            }
            if (renderHelper instanceof ImageDataRenderer) {
                ImageDataRenderer imgr = (ImageDataRenderer) renderHelper;
                imgr.renderImage(botimg, BaseTransform.IDENTITY_TRANSFORM, fctx);
                botimg.unref();
                botimg = null;
            }
        }
        if (botimg == null) {
            return topinput.filter(fctx, transform, outputClip, renderHelper, defaultInput);
        }
        ImageData topimg = topinput.filter(fctx, transform, outputClip, null, defaultInput);
        if (!topimg.validate(fctx)) {
            return new ImageData(fctx, (Filterable) null, (Rectangle) null);
        }
        RenderState rstate = getRenderState(fctx, transform, outputClip, renderHelper, defaultInput);
        ImageData ret = filterImageDatas(fctx, transform, outputClip, rstate, new ImageData[]{botimg, topimg});
        botimg.unref();
        topimg.unref();
        return ret;
    }

    @Override // com.sun.scenario.effect.FilterEffect
    public RenderState getRenderState(FilterContext fctx, BaseTransform transform, Rectangle outputClip, Object renderHelper, Effect defaultInput) {
        return RenderState.RenderSpaceRenderState;
    }

    @Override // com.sun.scenario.effect.Effect
    public boolean reducesOpaquePixels() {
        Effect topInput = getTopInput();
        Effect bottomInput = getBottomInput();
        return topInput != null && topInput.reducesOpaquePixels() && bottomInput != null && bottomInput.reducesOpaquePixels();
    }
}
