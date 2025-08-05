package com.sun.javafx.font.directwrite;

/* loaded from: jfxrt.jar:com/sun/javafx/font/directwrite/IDWriteGlyphRunAnalysis.class */
class IDWriteGlyphRunAnalysis extends IUnknown {
    IDWriteGlyphRunAnalysis(long ptr) {
        super(ptr);
    }

    byte[] CreateAlphaTexture(int textureType, RECT textureBounds) {
        return OS.CreateAlphaTexture(this.ptr, textureType, textureBounds);
    }

    RECT GetAlphaTextureBounds(int textureType) {
        return OS.GetAlphaTextureBounds(this.ptr, textureType);
    }
}
