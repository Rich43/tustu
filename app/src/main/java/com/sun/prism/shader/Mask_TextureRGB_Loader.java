package com.sun.prism.shader;

import com.sun.prism.ps.Shader;
import com.sun.prism.ps.ShaderFactory;
import java.io.InputStream;
import java.util.HashMap;

/* loaded from: jfxrt.jar:com/sun/prism/shader/Mask_TextureRGB_Loader.class */
public class Mask_TextureRGB_Loader {
    private Mask_TextureRGB_Loader() {
    }

    public static Shader loadShader(ShaderFactory factory, InputStream pixelShaderCode) {
        HashMap<String, Integer> samplers = new HashMap<>();
        samplers.put("maskTex", 1);
        samplers.put("imageTex", 0);
        HashMap<String, Integer> params = new HashMap<>();
        return factory.createShader(pixelShaderCode, samplers, params, 1, false, true);
    }
}
