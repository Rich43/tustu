package com.sun.prism.shader;

import com.sun.prism.ps.Shader;
import com.sun.prism.ps.ShaderFactory;
import java.io.InputStream;
import java.util.HashMap;

/* loaded from: jfxrt.jar:com/sun/prism/shader/DrawSemiRoundRect_Color_AlphaTest_Loader.class */
public class DrawSemiRoundRect_Color_AlphaTest_Loader {
    private DrawSemiRoundRect_Color_AlphaTest_Loader() {
    }

    public static Shader loadShader(ShaderFactory factory, InputStream pixelShaderCode) {
        HashMap<String, Integer> samplers = new HashMap<>();
        HashMap<String, Integer> params = new HashMap<>();
        params.put("idim", 1);
        params.put("oinvarcradii", 0);
        return factory.createShader(pixelShaderCode, samplers, params, 1, false, true);
    }
}
