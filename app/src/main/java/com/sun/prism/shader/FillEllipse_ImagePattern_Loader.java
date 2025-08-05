package com.sun.prism.shader;

import com.sun.prism.ps.Shader;
import com.sun.prism.ps.ShaderFactory;
import java.io.InputStream;
import java.util.HashMap;
import javax.swing.text.AbstractDocument;

/* loaded from: jfxrt.jar:com/sun/prism/shader/FillEllipse_ImagePattern_Loader.class */
public class FillEllipse_ImagePattern_Loader {
    private FillEllipse_ImagePattern_Loader() {
    }

    public static Shader loadShader(ShaderFactory factory, InputStream pixelShaderCode) {
        HashMap<String, Integer> samplers = new HashMap<>();
        samplers.put("inputTex", 0);
        HashMap<String, Integer> params = new HashMap<>();
        params.put("perspVec", 2);
        params.put("xParams", 0);
        params.put("yParams", 1);
        params.put(AbstractDocument.ContentElementName, 3);
        return factory.createShader(pixelShaderCode, samplers, params, 1, true, true);
    }
}
