package com.sun.prism.shader;

import com.sun.prism.ps.Shader;
import com.sun.prism.ps.ShaderFactory;
import java.io.InputStream;
import java.util.HashMap;
import javax.swing.text.AbstractDocument;

/* loaded from: jfxrt.jar:com/sun/prism/shader/AlphaOne_ImagePattern_Loader.class */
public class AlphaOne_ImagePattern_Loader {
    private AlphaOne_ImagePattern_Loader() {
    }

    public static Shader loadShader(ShaderFactory factory, InputStream pixelShaderCode) {
        HashMap<String, Integer> samplers = new HashMap<>();
        samplers.put("inputTex", 0);
        HashMap<String, Integer> params = new HashMap<>();
        params.put(AbstractDocument.ContentElementName, 0);
        return factory.createShader(pixelShaderCode, samplers, params, 1, false, true);
    }
}
