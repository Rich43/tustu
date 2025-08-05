package com.sun.prism;

/* loaded from: jfxrt.jar:com/sun/prism/MaskTextureGraphics.class */
public interface MaskTextureGraphics extends Graphics {
    void drawPixelsMasked(RTTexture rTTexture, RTTexture rTTexture2, int i2, int i3, int i4, int i5, int i6, int i7, int i8, int i9);

    void maskInterpolatePixels(RTTexture rTTexture, RTTexture rTTexture2, int i2, int i3, int i4, int i5, int i6, int i7, int i8, int i9);
}
