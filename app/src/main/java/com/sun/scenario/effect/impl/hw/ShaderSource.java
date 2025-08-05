package com.sun.scenario.effect.impl.hw;

import com.sun.scenario.effect.Effect;
import java.io.InputStream;

/* loaded from: jfxrt.jar:com/sun/scenario/effect/impl/hw/ShaderSource.class */
public interface ShaderSource {
    InputStream loadSource(String str);

    Effect.AccelType getAccelType();
}
