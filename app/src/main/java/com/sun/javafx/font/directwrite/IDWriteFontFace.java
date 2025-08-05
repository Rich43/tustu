package com.sun.javafx.font.directwrite;

import com.sun.javafx.geom.Path2D;

/* loaded from: jfxrt.jar:com/sun/javafx/font/directwrite/IDWriteFontFace.class */
class IDWriteFontFace extends IUnknown {
    IDWriteFontFace(long ptr) {
        super(ptr);
    }

    DWRITE_GLYPH_METRICS GetDesignGlyphMetrics(short glyphIndex, boolean isSideways) {
        return OS.GetDesignGlyphMetrics(this.ptr, glyphIndex, isSideways);
    }

    Path2D GetGlyphRunOutline(float emSize, short glyphIndex, boolean isSideways) {
        return OS.GetGlyphRunOutline(this.ptr, emSize, glyphIndex, isSideways);
    }
}
