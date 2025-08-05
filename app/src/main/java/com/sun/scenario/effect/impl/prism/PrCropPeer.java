package com.sun.scenario.effect.impl.prism;

import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.prism.Graphics;
import com.sun.scenario.effect.Effect;
import com.sun.scenario.effect.FilterContext;
import com.sun.scenario.effect.ImageData;
import com.sun.scenario.effect.impl.EffectPeer;
import com.sun.scenario.effect.impl.Renderer;
import com.sun.scenario.effect.impl.state.RenderState;

/* loaded from: jfxrt.jar:com/sun/scenario/effect/impl/prism/PrCropPeer.class */
public class PrCropPeer extends EffectPeer {
    public PrCropPeer(FilterContext fctx, Renderer r2, String uniqueName) {
        super(fctx, r2, uniqueName);
    }

    @Override // com.sun.scenario.effect.impl.EffectPeer
    public ImageData filter(Effect effect, RenderState rstate, BaseTransform transform, Rectangle outputClip, ImageData... inputs) {
        FilterContext fctx = getFilterContext();
        ImageData srcData = inputs[0];
        Rectangle srcBounds = srcData.getTransformedBounds(null);
        if (outputClip.contains(srcBounds)) {
            srcData.addref();
            return srcData;
        }
        Rectangle dstBounds = new Rectangle(srcBounds);
        dstBounds.intersectWith(outputClip);
        int w2 = dstBounds.width;
        int h2 = dstBounds.height;
        PrDrawable dst = (PrDrawable) getRenderer().getCompatibleImage(w2, h2);
        if (!srcData.validate(fctx) || dst == null) {
            dst = null;
        } else {
            Graphics gdst = dst.createGraphics();
            PrEffectHelper.renderImageData(gdst, srcData, dstBounds);
        }
        return new ImageData(fctx, dst, dstBounds);
    }
}
