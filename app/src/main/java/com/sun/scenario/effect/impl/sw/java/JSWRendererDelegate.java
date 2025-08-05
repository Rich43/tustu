package com.sun.scenario.effect.impl.sw.java;

import com.sun.scenario.effect.Effect;
import com.sun.scenario.effect.impl.sw.RendererDelegate;

/* loaded from: jfxrt.jar:com/sun/scenario/effect/impl/sw/java/JSWRendererDelegate.class */
public class JSWRendererDelegate implements RendererDelegate {
    @Override // com.sun.scenario.effect.impl.sw.RendererDelegate
    public Effect.AccelType getAccelType() {
        return Effect.AccelType.NONE;
    }

    @Override // com.sun.scenario.effect.impl.sw.RendererDelegate
    public String getPlatformPeerName(String name, int unrollCount) {
        return "com.sun.scenario.effect.impl.sw.java.JSW" + name + "Peer";
    }
}
