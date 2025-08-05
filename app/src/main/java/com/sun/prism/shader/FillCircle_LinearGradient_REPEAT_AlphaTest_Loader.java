package com.sun.prism.shader;

import com.sun.prism.ps.Shader;
import com.sun.prism.ps.ShaderFactory;
import java.io.InputStream;
import java.util.HashMap;

/* loaded from: jfxrt.jar:com/sun/prism/shader/FillCircle_LinearGradient_REPEAT_AlphaTest_Loader.class */
public class FillCircle_LinearGradient_REPEAT_AlphaTest_Loader {
    private FillCircle_LinearGradient_REPEAT_AlphaTest_Loader() {
    }

    public static Shader loadShader(ShaderFactory factory, InputStream pixelShaderCode) {
        HashMap<String, Integer> samplers = new HashMap<>();
        samplers.put("colors", 0);
        HashMap<String, Integer> params = new HashMap<>();
        params.put("perspVec", 14);
        params.put("gradParams", 13);
        params.put("offset", 12);
        params.put("fractions", 0);
        return factory.createShader(pixelShaderCode, samplers, params, 1, true, true);
    }
}
