package com.sun.prism.shader;

import com.sun.prism.ps.Shader;
import com.sun.prism.ps.ShaderFactory;
import java.io.InputStream;
import java.util.HashMap;
import javax.swing.text.AbstractDocument;

/* loaded from: jfxrt.jar:com/sun/prism/shader/AlphaTextureDifference_LinearGradient_Loader.class */
public class AlphaTextureDifference_LinearGradient_Loader {
    private AlphaTextureDifference_LinearGradient_Loader() {
    }

    public static Shader loadShader(ShaderFactory factory, InputStream pixelShaderCode) {
        HashMap<String, Integer> samplers = new HashMap<>();
        samplers.put("maskInput", 0);
        samplers.put("colors", 1);
        HashMap<String, Integer> params = new HashMap<>();
        params.put("innerOffset", 0);
        params.put(AbstractDocument.ContentElementName, 1);
        return factory.createShader(pixelShaderCode, samplers, params, 1, false, true);
    }
}
