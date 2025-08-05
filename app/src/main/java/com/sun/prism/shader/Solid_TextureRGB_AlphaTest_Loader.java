package com.sun.prism.shader;

import com.sun.prism.ps.Shader;
import com.sun.prism.ps.ShaderFactory;
import java.io.InputStream;
import java.util.HashMap;

/* loaded from: jfxrt.jar:com/sun/prism/shader/Solid_TextureRGB_AlphaTest_Loader.class */
public class Solid_TextureRGB_AlphaTest_Loader {
    private Solid_TextureRGB_AlphaTest_Loader() {
    }

    public static Shader loadShader(ShaderFactory factory, InputStream pixelShaderCode) {
        HashMap<String, Integer> samplers = new HashMap<>();
        samplers.put("inputTex", 0);
        HashMap<String, Integer> params = new HashMap<>();
        return factory.createShader(pixelShaderCode, samplers, params, 0, false, true);
    }
}
