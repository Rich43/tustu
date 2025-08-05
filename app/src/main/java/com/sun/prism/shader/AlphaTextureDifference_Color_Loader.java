package com.sun.prism.shader;

import com.sun.prism.ps.Shader;
import com.sun.prism.ps.ShaderFactory;
import java.io.InputStream;
import java.util.HashMap;

/* loaded from: jfxrt.jar:com/sun/prism/shader/AlphaTextureDifference_Color_Loader.class */
public class AlphaTextureDifference_Color_Loader {
    private AlphaTextureDifference_Color_Loader() {
    }

    public static Shader loadShader(ShaderFactory factory, InputStream pixelShaderCode) {
        HashMap<String, Integer> samplers = new HashMap<>();
        samplers.put("maskInput", 0);
        HashMap<String, Integer> params = new HashMap<>();
        params.put("innerOffset", 0);
        return factory.createShader(pixelShaderCode, samplers, params, 0, false, true);
    }
}
