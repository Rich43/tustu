package com.sun.prism.shader;

import com.sun.prism.ps.Shader;
import com.sun.prism.ps.ShaderFactory;
import java.io.InputStream;
import java.util.HashMap;
import javax.swing.text.AbstractDocument;

/* loaded from: jfxrt.jar:com/sun/prism/shader/AlphaTextureDifference_ImagePattern_Loader.class */
public class AlphaTextureDifference_ImagePattern_Loader {
    private AlphaTextureDifference_ImagePattern_Loader() {
    }

    public static Shader loadShader(ShaderFactory factory, InputStream pixelShaderCode) {
        HashMap<String, Integer> samplers = new HashMap<>();
        samplers.put("inputTex", 1);
        samplers.put("maskInput", 0);
        HashMap<String, Integer> params = new HashMap<>();
        params.put("innerOffset", 0);
        params.put(AbstractDocument.ContentElementName, 1);
        return factory.createShader(pixelShaderCode, samplers, params, 1, false, true);
    }
}
