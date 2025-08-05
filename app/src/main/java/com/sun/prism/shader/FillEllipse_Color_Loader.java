package com.sun.prism.shader;

import com.sun.prism.ps.Shader;
import com.sun.prism.ps.ShaderFactory;
import java.io.InputStream;
import java.util.HashMap;

/* loaded from: jfxrt.jar:com/sun/prism/shader/FillEllipse_Color_Loader.class */
public class FillEllipse_Color_Loader {
    private FillEllipse_Color_Loader() {
    }

    public static Shader loadShader(ShaderFactory factory, InputStream pixelShaderCode) {
        HashMap<String, Integer> samplers = new HashMap<>();
        HashMap<String, Integer> params = new HashMap<>();
        return factory.createShader(pixelShaderCode, samplers, params, 1, false, true);
    }
}
