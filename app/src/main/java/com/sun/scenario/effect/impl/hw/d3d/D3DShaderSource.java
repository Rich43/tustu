package com.sun.scenario.effect.impl.hw.d3d;

import com.sun.scenario.effect.Effect;
import com.sun.scenario.effect.impl.hw.ShaderSource;
import java.io.InputStream;

/* loaded from: jfxrt.jar:com/sun/scenario/effect/impl/hw/d3d/D3DShaderSource.class */
public class D3DShaderSource implements ShaderSource {
    @Override // com.sun.scenario.effect.impl.hw.ShaderSource
    public InputStream loadSource(String name) {
        return D3DShaderSource.class.getResourceAsStream("hlsl/" + name + ".obj");
    }

    @Override // com.sun.scenario.effect.impl.hw.ShaderSource
    public Effect.AccelType getAccelType() {
        return Effect.AccelType.DIRECT3D;
    }
}
