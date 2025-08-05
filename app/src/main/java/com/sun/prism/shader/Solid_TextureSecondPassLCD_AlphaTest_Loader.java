package com.sun.prism.shader;

import com.sun.prism.ps.Shader;
import com.sun.prism.ps.ShaderFactory;
import java.io.InputStream;
import java.util.HashMap;

/* loaded from: jfxrt.jar:com/sun/prism/shader/Solid_TextureSecondPassLCD_AlphaTest_Loader.class */
public class Solid_TextureSecondPassLCD_AlphaTest_Loader {
    private Solid_TextureSecondPassLCD_AlphaTest_Loader() {
    }

    public static Shader loadShader(ShaderFactory factory, InputStream pixelShaderCode) {
        HashMap<String, Integer> samplers = new HashMap<>();
        samplers.put("dstColor", 0);
        samplers.put("glyphColor", 1);
        HashMap<String, Integer> params = new HashMap<>();
        params.put("gamma", 0);
        return factory.createShader(pixelShaderCode, samplers, params, 1, false, true);
    }
}
