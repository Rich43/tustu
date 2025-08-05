package com.sun.prism.shader;

import com.sun.prism.ps.Shader;
import com.sun.prism.ps.ShaderFactory;
import java.io.InputStream;
import java.util.HashMap;
import javax.swing.text.AbstractDocument;

/* loaded from: jfxrt.jar:com/sun/prism/shader/DrawSemiRoundRect_ImagePattern_Loader.class */
public class DrawSemiRoundRect_ImagePattern_Loader {
    private DrawSemiRoundRect_ImagePattern_Loader() {
    }

    public static Shader loadShader(ShaderFactory factory, InputStream pixelShaderCode) {
        HashMap<String, Integer> samplers = new HashMap<>();
        samplers.put("inputTex", 0);
        HashMap<String, Integer> params = new HashMap<>();
        params.put("perspVec", 4);
        params.put("xParams", 2);
        params.put("idim", 1);
        params.put("yParams", 3);
        params.put(AbstractDocument.ContentElementName, 5);
        params.put("oinvarcradii", 0);
        return factory.createShader(pixelShaderCode, samplers, params, 1, true, true);
    }
}
