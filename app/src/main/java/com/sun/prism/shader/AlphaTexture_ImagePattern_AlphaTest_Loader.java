package com.sun.prism.shader;

import com.sun.prism.ps.Shader;
import com.sun.prism.ps.ShaderFactory;
import java.io.InputStream;
import java.util.HashMap;
import javax.swing.text.AbstractDocument;

/* loaded from: jfxrt.jar:com/sun/prism/shader/AlphaTexture_ImagePattern_AlphaTest_Loader.class */
public class AlphaTexture_ImagePattern_AlphaTest_Loader {
    private AlphaTexture_ImagePattern_AlphaTest_Loader() {
    }

    public static Shader loadShader(ShaderFactory factory, InputStream pixelShaderCode) {
        HashMap<String, Integer> samplers = new HashMap<>();
        samplers.put("inputTex", 1);
        samplers.put("maskInput", 0);
        HashMap<String, Integer> params = new HashMap<>();
        params.put(AbstractDocument.ContentElementName, 0);
        return factory.createShader(pixelShaderCode, samplers, params, 1, false, true);
    }
}
