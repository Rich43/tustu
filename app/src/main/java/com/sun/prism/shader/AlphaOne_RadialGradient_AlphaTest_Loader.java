package com.sun.prism.shader;

import com.sun.prism.ps.Shader;
import com.sun.prism.ps.ShaderFactory;
import java.io.InputStream;
import java.util.HashMap;
import javax.swing.text.AbstractDocument;

/* loaded from: jfxrt.jar:com/sun/prism/shader/AlphaOne_RadialGradient_AlphaTest_Loader.class */
public class AlphaOne_RadialGradient_AlphaTest_Loader {
    private AlphaOne_RadialGradient_AlphaTest_Loader() {
    }

    public static Shader loadShader(ShaderFactory factory, InputStream pixelShaderCode) {
        HashMap<String, Integer> samplers = new HashMap<>();
        samplers.put("colors", 0);
        HashMap<String, Integer> params = new HashMap<>();
        params.put("precalc", 1);
        params.put(AbstractDocument.ContentElementName, 0);
        return factory.createShader(pixelShaderCode, samplers, params, 1, false, true);
    }
}
