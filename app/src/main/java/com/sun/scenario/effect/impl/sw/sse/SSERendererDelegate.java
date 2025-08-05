package com.sun.scenario.effect.impl.sw.sse;

import com.sun.glass.utils.NativeLibLoader;
import com.sun.scenario.effect.Effect;
import com.sun.scenario.effect.impl.sw.RendererDelegate;
import java.security.AccessController;

/* loaded from: jfxrt.jar:com/sun/scenario/effect/impl/sw/sse/SSERendererDelegate.class */
public class SSERendererDelegate implements RendererDelegate {
    public static native boolean isSupported();

    static {
        AccessController.doPrivileged(() -> {
            NativeLibLoader.loadLibrary("decora_sse");
            return null;
        });
    }

    public SSERendererDelegate() {
        if (!isSupported()) {
            throw new UnsupportedOperationException("required instruction set (SSE2) not supported on this processor");
        }
    }

    @Override // com.sun.scenario.effect.impl.sw.RendererDelegate
    public Effect.AccelType getAccelType() {
        return Effect.AccelType.SIMD;
    }

    @Override // com.sun.scenario.effect.impl.sw.RendererDelegate
    public String getPlatformPeerName(String name, int unrollCount) {
        return "com.sun.scenario.effect.impl.sw.sse.SSE" + name + "Peer";
    }
}
