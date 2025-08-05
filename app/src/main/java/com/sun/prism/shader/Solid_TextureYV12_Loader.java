package com.sun.prism.shader;

import com.sun.prism.ps.Shader;
import com.sun.prism.ps.ShaderFactory;
import java.io.InputStream;
import java.util.HashMap;

/* loaded from: jfxrt.jar:com/sun/prism/shader/Solid_TextureYV12_Loader.class */
public class Solid_TextureYV12_Loader {
    private Solid_TextureYV12_Loader() {
    }

    public static Shader loadShader(ShaderFactory factory, InputStream pixelShaderCode) {
        HashMap<String, Integer> samplers = new HashMap<>();
        samplers.put("crTex", 1);
        samplers.put("alphaTex", 3);
        samplers.put("cbTex", 2);
        samplers.put("lumaTex", 0);
        HashMap<String, Integer> params = new HashMap<>();
        params.put("cbCrScale", 1);
        params.put("lumaAlphaScale", 0);
        return factory.createShader(pixelShaderCode, samplers, params, 0, false, true);
    }
}
