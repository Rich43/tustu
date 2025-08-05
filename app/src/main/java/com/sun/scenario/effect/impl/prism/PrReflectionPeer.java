package com.sun.scenario.effect.impl.prism;

import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.prism.Graphics;
import com.sun.prism.Texture;
import com.sun.scenario.effect.Effect;
import com.sun.scenario.effect.FilterContext;
import com.sun.scenario.effect.Filterable;
import com.sun.scenario.effect.ImageData;
import com.sun.scenario.effect.Reflection;
import com.sun.scenario.effect.impl.EffectPeer;
import com.sun.scenario.effect.impl.Renderer;
import com.sun.scenario.effect.impl.state.RenderState;

/* loaded from: jfxrt.jar:com/sun/scenario/effect/impl/prism/PrReflectionPeer.class */
public class PrReflectionPeer extends EffectPeer {
    public PrReflectionPeer(FilterContext fctx, Renderer r2, String uniqueName) {
        super(fctx, r2, uniqueName);
    }

    @Override // com.sun.scenario.effect.impl.EffectPeer
    public ImageData filter(Effect effect, RenderState rstate, BaseTransform transform, Rectangle outputClip, ImageData... inputs) {
        FilterContext fctx = getFilterContext();
        Reflection reflect = (Reflection) effect;
        Rectangle inputbounds = inputs[0].getUntransformedBounds();
        int srcW = inputbounds.width;
        int srcH = inputbounds.height;
        float refY = srcH + reflect.getTopOffset();
        float refH = reflect.getFraction() * srcH;
        int irefY1 = (int) Math.floor(refY);
        int irefY2 = (int) Math.ceil(refY + refH);
        int irefH = irefY2 - irefY1;
        int dstH = irefY2 > srcH ? irefY2 : srcH;
        PrDrawable dst = (PrDrawable) getRenderer().getCompatibleImage(srcW, dstH);
        if (!inputs[0].validate(fctx) || dst == null) {
            return new ImageData(fctx, (Filterable) null, inputbounds);
        }
        PrDrawable src = (PrDrawable) inputs[0].getUntransformedImage();
        Texture srctex = src.getTextureObject();
        Graphics gdst = dst.createGraphics();
        gdst.transform(inputs[0].getTransform());
        float sy1 = srcH - irefH;
        float sx2 = srcW;
        float sy2 = srcH;
        gdst.drawTextureVO(srctex, reflect.getBottomOpacity(), reflect.getTopOpacity(), 0.0f, irefY2, srcW, irefY1, 0.0f, sy1, sx2, sy2);
        gdst.drawTexture(srctex, 0.0f, 0.0f, srcW, srcH);
        Rectangle newbounds = new Rectangle(inputbounds.f11913x, inputbounds.f11914y, srcW, dstH);
        return new ImageData(fctx, dst, newbounds);
    }
}
