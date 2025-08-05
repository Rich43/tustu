package com.sun.prism.shader;

import com.sun.prism.ps.Shader;
import com.sun.prism.ps.ShaderFactory;
import java.io.InputStream;
import java.util.HashMap;

/* loaded from: jfxrt.jar:com/sun/prism/shader/DrawEllipse_LinearGradient_REPEAT_Loader.class */
public class DrawEllipse_LinearGradient_REPEAT_Loader {
    private DrawEllipse_LinearGradient_REPEAT_Loader() {
    }

    public static Shader loadShader(ShaderFactory factory, InputStream pixelShaderCode) {
        HashMap<String, Integer> samplers = new HashMap<>();
        samplers.put("colors", 0);
        HashMap<String, Integer> params = new HashMap<>();
        params.put("perspVec", 15);
        params.put("gradParams", 14);
        params.put("offset", 13);
        params.put("fractions", 1);
        params.put("idim", 0);
        return factory.createShader(pixelShaderCode, samplers, params, 1, true, true);
    }
}
