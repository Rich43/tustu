package com.sun.prism.shader;

import com.sun.prism.ps.Shader;
import com.sun.prism.ps.ShaderFactory;
import java.io.InputStream;
import java.util.HashMap;

/* loaded from: jfxrt.jar:com/sun/prism/shader/DrawPgram_RadialGradient_PAD_AlphaTest_Loader.class */
public class DrawPgram_RadialGradient_PAD_AlphaTest_Loader {
    private DrawPgram_RadialGradient_PAD_AlphaTest_Loader() {
    }

    public static Shader loadShader(ShaderFactory factory, InputStream pixelShaderCode) {
        HashMap<String, Integer> samplers = new HashMap<>();
        samplers.put("colors", 0);
        HashMap<String, Integer> params = new HashMap<>();
        params.put("perspVec", 17);
        params.put("m0", 14);
        params.put("offset", 13);
        params.put("m1", 15);
        params.put("precalc", 16);
        params.put("fractions", 1);
        params.put("idim", 0);
        return factory.createShader(pixelShaderCode, samplers, params, 1, true, true);
    }
}
