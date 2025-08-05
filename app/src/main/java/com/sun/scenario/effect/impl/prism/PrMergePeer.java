package com.sun.scenario.effect.impl.prism;

import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.prism.Graphics;
import com.sun.scenario.effect.Effect;
import com.sun.scenario.effect.FilterContext;
import com.sun.scenario.effect.Filterable;
import com.sun.scenario.effect.ImageData;
import com.sun.scenario.effect.Merge;
import com.sun.scenario.effect.impl.EffectPeer;
import com.sun.scenario.effect.impl.Renderer;
import com.sun.scenario.effect.impl.state.RenderState;

/* loaded from: jfxrt.jar:com/sun/scenario/effect/impl/prism/PrMergePeer.class */
public class PrMergePeer extends EffectPeer {
    public PrMergePeer(FilterContext fctx, Renderer r2, String uniqueName) {
        super(fctx, r2, uniqueName);
    }

    @Override // com.sun.scenario.effect.impl.EffectPeer
    public ImageData filter(Effect effect, RenderState rstate, BaseTransform transform, Rectangle outputClip, ImageData... inputs) {
        FilterContext fctx = getFilterContext();
        Merge merge = (Merge) effect;
        Rectangle unionbounds = merge.getResultBounds(transform, outputClip, inputs);
        PrDrawable dst = (PrDrawable) getRenderer().getCompatibleImage(unionbounds.width, unionbounds.height);
        if (dst == null) {
            return new ImageData(fctx, (Filterable) null, unionbounds);
        }
        Graphics gdst = dst.createGraphics();
        for (ImageData input : inputs) {
            PrEffectHelper.renderImageData(gdst, input, unionbounds);
        }
        return new ImageData(fctx, dst, unionbounds);
    }
}
