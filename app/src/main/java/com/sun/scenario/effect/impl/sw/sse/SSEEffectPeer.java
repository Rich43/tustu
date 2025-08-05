package com.sun.scenario.effect.impl.sw.sse;

import com.sun.scenario.effect.FilterContext;
import com.sun.scenario.effect.impl.EffectPeer;
import com.sun.scenario.effect.impl.Renderer;
import com.sun.scenario.effect.impl.state.RenderState;

/* loaded from: jfxrt.jar:com/sun/scenario/effect/impl/sw/sse/SSEEffectPeer.class */
public abstract class SSEEffectPeer<T extends RenderState> extends EffectPeer<T> {
    protected SSEEffectPeer(FilterContext fctx, Renderer r2, String uniqueName) {
        super(fctx, r2, uniqueName);
    }
}
