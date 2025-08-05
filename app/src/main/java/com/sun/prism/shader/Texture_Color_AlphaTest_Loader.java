package com.sun.prism.shader;

import com.sun.prism.ps.Shader;
import com.sun.prism.ps.ShaderFactory;
import java.io.InputStream;
import java.util.HashMap;

/* loaded from: jfxrt.jar:com/sun/prism/shader/Texture_Color_AlphaTest_Loader.class */
public class Texture_Color_AlphaTest_Loader {
    private Texture_Color_AlphaTest_Loader() {
    }

    public static Shader loadShader(ShaderFactory factory, InputStream pixelShaderCode) {
        HashMap<String, Integer> samplers = new HashMap<>();
        samplers.put("maskInput", 0);
        HashMap<String, Integer> params = new HashMap<>();
        return factory.createShader(pixelShaderCode, samplers, params, 0, false, true);
    }
}
