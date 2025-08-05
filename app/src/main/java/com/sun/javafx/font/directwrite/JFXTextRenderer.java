package com.sun.javafx.font.directwrite;

/* loaded from: jfxrt.jar:com/sun/javafx/font/directwrite/JFXTextRenderer.class */
class JFXTextRenderer extends IUnknown {
    JFXTextRenderer(long ptr) {
        super(ptr);
    }

    boolean Next() {
        return OS.JFXTextRendererNext(this.ptr);
    }

    int GetStart() {
        return OS.JFXTextRendererGetStart(this.ptr);
    }

    int GetLength() {
        return OS.JFXTextRendererGetLength(this.ptr);
    }

    int GetGlyphCount() {
        return OS.JFXTextRendererGetGlyphCount(this.ptr);
    }

    int GetTotalGlyphCount() {
        return OS.JFXTextRendererGetTotalGlyphCount(this.ptr);
    }

    IDWriteFontFace GetFontFace() {
        long result = OS.JFXTextRendererGetFontFace(this.ptr);
        if (result != 0) {
            return new IDWriteFontFace(result);
        }
        return null;
    }

    int GetGlyphIndices(int[] glyphs, int start, int slot) {
        return OS.JFXTextRendererGetGlyphIndices(this.ptr, glyphs, start, slot);
    }

    int GetGlyphAdvances(float[] advances, int start) {
        return OS.JFXTextRendererGetGlyphAdvances(this.ptr, advances, start);
    }

    int GetGlyphOffsets(float[] offsets, int start) {
        return OS.JFXTextRendererGetGlyphOffsets(this.ptr, offsets, start);
    }

    int GetClusterMap(short[] clusterMap, int textStart, int glyphStart) {
        return OS.JFXTextRendererGetClusterMap(this.ptr, clusterMap, textStart, glyphStart);
    }
}
