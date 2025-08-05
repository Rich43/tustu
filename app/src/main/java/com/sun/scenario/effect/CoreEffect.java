package com.sun.scenario.effect;

import com.sun.javafx.geom.Rectangle;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.scenario.effect.Effect;
import com.sun.scenario.effect.impl.EffectPeer;
import com.sun.scenario.effect.impl.Renderer;
import com.sun.scenario.effect.impl.state.RenderState;

/* loaded from: jfxrt.jar:com/sun/scenario/effect/CoreEffect.class */
abstract class CoreEffect<T extends RenderState> extends FilterEffect<T> {
    private String peerKey;
    private int peerCount;

    CoreEffect() {
        this.peerCount = -1;
    }

    CoreEffect(Effect input) {
        super(input);
        this.peerCount = -1;
    }

    CoreEffect(Effect input1, Effect input2) {
        super(input1, input2);
        this.peerCount = -1;
    }

    final void updatePeerKey(String key) {
        updatePeerKey(key, -1);
    }

    final void updatePeerKey(String key, int unrollCount) {
        this.peerKey = key;
        this.peerCount = unrollCount;
    }

    private EffectPeer getPeer(FilterContext fctx, int approxW, int approxH) {
        return Renderer.getRenderer(fctx, this, approxW, approxH).getPeerInstance(fctx, this.peerKey, this.peerCount);
    }

    final EffectPeer getPeer(FilterContext fctx, ImageData[] inputs) {
        int approxH;
        int approxW;
        if (inputs.length > 0) {
            Rectangle approxBounds = inputs[0].getUntransformedBounds();
            approxW = approxBounds.width;
            approxH = approxBounds.height;
        } else {
            approxH = 500;
            approxW = 500;
        }
        return getPeer(fctx, approxW, approxH);
    }

    @Override // com.sun.scenario.effect.FilterEffect
    public ImageData filterImageDatas(FilterContext fctx, BaseTransform transform, Rectangle outputClip, T rstate, ImageData... inputs) {
        return getPeer(fctx, inputs).filter(this, rstate, transform, outputClip, inputs);
    }

    @Override // com.sun.scenario.effect.Effect
    public Effect.AccelType getAccelType(FilterContext fctx) {
        EffectPeer peer = getPeer(fctx, 1024, 1024);
        return peer.getAccelType();
    }
}
