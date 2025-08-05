package com.sun.scenario.effect.impl.sw;

import com.sun.scenario.effect.Effect;

/* loaded from: jfxrt.jar:com/sun/scenario/effect/impl/sw/RendererDelegate.class */
public interface RendererDelegate {
    Effect.AccelType getAccelType();

    String getPlatformPeerName(String str, int i2);
}
