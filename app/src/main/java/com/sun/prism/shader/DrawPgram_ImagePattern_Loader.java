package com.sun.prism.shader;

import com.sun.prism.ps.Shader;
import com.sun.prism.ps.ShaderFactory;
import java.io.InputStream;
import java.util.HashMap;
import javax.swing.text.AbstractDocument;

/* loaded from: jfxrt.jar:com/sun/prism/shader/DrawPgram_ImagePattern_Loader.class */
public class DrawPgram_ImagePattern_Loader {
    private DrawPgram_ImagePattern_Loader() {
    }

    public static Shader loadShader(ShaderFactory factory, InputStream pixelShaderCode) {
        HashMap<String, Integer> samplers = new HashMap<>();
        samplers.put("inputTex", 0);
        HashMap<String, Integer> params = new HashMap<>();
        params.put("perspVec", 3);
        params.put("xParams", 1);
        params.put("idim", 0);
        params.put("yParams", 2);
        params.put(AbstractDocument.ContentElementName, 4);
        return factory.createShader(pixelShaderCode, samplers, params, 1, true, true);
    }
}
