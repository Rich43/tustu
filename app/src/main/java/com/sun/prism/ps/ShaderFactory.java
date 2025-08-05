package com.sun.prism.ps;

import com.sun.prism.ResourceFactory;
import java.io.InputStream;
import java.util.Map;

/* loaded from: jfxrt.jar:com/sun/prism/ps/ShaderFactory.class */
public interface ShaderFactory extends ResourceFactory {
    Shader createShader(InputStream inputStream, Map<String, Integer> map, Map<String, Integer> map2, int i2, boolean z2, boolean z3);

    Shader createStockShader(String str);
}
