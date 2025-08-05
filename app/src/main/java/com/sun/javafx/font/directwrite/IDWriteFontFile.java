package com.sun.javafx.font.directwrite;

/* loaded from: jfxrt.jar:com/sun/javafx/font/directwrite/IDWriteFontFile.class */
class IDWriteFontFile extends IUnknown {
    IDWriteFontFile(long ptr) {
        super(ptr);
    }

    int Analyze(boolean[] isSupportedFontType, int[] fontFileType, int[] fontFaceType, int[] numberOfFaces) {
        return OS.Analyze(this.ptr, isSupportedFontType, fontFileType, fontFaceType, numberOfFaces);
    }
}
