package com.sun.prism.ps;

import com.sun.javafx.geom.transform.Affine3D;
import com.sun.prism.Graphics;
import com.sun.prism.Texture;

/* loaded from: jfxrt.jar:com/sun/prism/ps/ShaderGraphics.class */
public interface ShaderGraphics extends Graphics {
    void getPaintShaderTransform(Affine3D affine3D);

    void setExternalShader(Shader shader);

    void drawTextureRaw2(Texture texture, Texture texture2, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9, float f10, float f11, float f12, float f13);

    void drawMappedTextureRaw2(Texture texture, Texture texture2, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9, float f10, float f11, float f12, float f13, float f14, float f15, float f16, float f17, float f18, float f19, float f20, float f21);
}
