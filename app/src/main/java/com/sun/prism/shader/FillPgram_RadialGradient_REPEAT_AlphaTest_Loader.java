package com.sun.prism.shader;

import com.sun.prism.ps.Shader;
import com.sun.prism.ps.ShaderFactory;
import java.io.InputStream;
import java.util.HashMap;

/* loaded from: jfxrt.jar:com/sun/prism/shader/FillPgram_RadialGradient_REPEAT_AlphaTest_Loader.class */
public class FillPgram_RadialGradient_REPEAT_AlphaTest_Loader {
    private FillPgram_RadialGradient_REPEAT_AlphaTest_Loader() {
    }

    public static Shader loadShader(ShaderFactory factory, InputStream pixelShaderCode) {
        HashMap<String, Integer> samplers = new HashMap<>();
        samplers.put("colors", 0);
        HashMap<String, Integer> params = new HashMap<>();
        params.put("perspVec", 16);
        params.put("m0", 13);
        params.put("offset", 12);
        params.put("m1", 14);
        params.put("precalc", 15);
        params.put("fractions", 0);
        return factory.createShader(pixelShaderCode, samplers, params, 1, true, true);
    }
}
