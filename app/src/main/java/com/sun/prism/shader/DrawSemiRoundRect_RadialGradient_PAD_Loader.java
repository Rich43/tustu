package com.sun.prism.shader;

import com.sun.prism.ps.Shader;
import com.sun.prism.ps.ShaderFactory;
import java.io.InputStream;
import java.util.HashMap;

/* loaded from: jfxrt.jar:com/sun/prism/shader/DrawSemiRoundRect_RadialGradient_PAD_Loader.class */
public class DrawSemiRoundRect_RadialGradient_PAD_Loader {
    private DrawSemiRoundRect_RadialGradient_PAD_Loader() {
    }

    public static Shader loadShader(ShaderFactory factory, InputStream pixelShaderCode) {
        HashMap<String, Integer> samplers = new HashMap<>();
        samplers.put("colors", 0);
        HashMap<String, Integer> params = new HashMap<>();
        params.put("m0", 15);
        params.put("offset", 14);
        params.put("m1", 16);
        params.put("idim", 1);
        params.put("oinvarcradii", 0);
        params.put("perspVec", 18);
        params.put("precalc", 17);
        params.put("fractions", 2);
        return factory.createShader(pixelShaderCode, samplers, params, 1, true, true);
    }
}
