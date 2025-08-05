package com.sun.scenario.effect;

import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.scenario.effect.Effect;
import com.sun.scenario.effect.impl.EffectPeer;
import com.sun.scenario.effect.impl.Renderer;
import com.sun.scenario.effect.impl.state.LinearConvolveKernel;
import com.sun.scenario.effect.impl.state.LinearConvolveRenderState;

/* loaded from: jfxrt.jar:com/sun/scenario/effect/LinearConvolveCoreEffect.class */
public abstract class LinearConvolveCoreEffect extends CoreEffect<LinearConvolveRenderState> {
    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.sun.scenario.effect.Effect
    public abstract LinearConvolveKernel getState();

    @Override // com.sun.scenario.effect.CoreEffect, com.sun.scenario.effect.Effect
    public /* bridge */ /* synthetic */ Effect.AccelType getAccelType(FilterContext filterContext) {
        return super.getAccelType(filterContext);
    }

    public LinearConvolveCoreEffect(Effect input) {
        super(input);
    }

    @Override // com.sun.scenario.effect.FilterEffect
    public final LinearConvolveRenderState getRenderState(FilterContext fctx, BaseTransform transform, Rectangle outputClip, Object renderHelper, Effect defaultInput) {
        return getState().getRenderState(transform);
    }

    @Override // com.sun.scenario.effect.CoreEffect, com.sun.scenario.effect.FilterEffect
    public ImageData filterImageDatas(FilterContext fctx, BaseTransform transform, Rectangle outputClip, LinearConvolveRenderState lcrstate, ImageData... inputs) {
        ImageData src = inputs[0];
        src.addref();
        if (lcrstate.isNop()) {
            return src;
        }
        Rectangle approxBounds = inputs[0].getUntransformedBounds();
        int approxW = approxBounds.width;
        int approxH = approxBounds.height;
        Renderer r2 = Renderer.getRenderer(fctx, this, approxW, approxH);
        for (int pass = 0; pass < 2; pass++) {
            src = lcrstate.validatePassInput(src, pass);
            EffectPeer peer = lcrstate.getPassPeer(r2, fctx);
            if (peer != null) {
                peer.setPass(pass);
                ImageData res = peer.filter(this, lcrstate, transform, outputClip, src);
                src.unref();
                src = res;
                if (!src.validate(fctx)) {
                    src.unref();
                    return src;
                }
            }
        }
        return src;
    }
}
