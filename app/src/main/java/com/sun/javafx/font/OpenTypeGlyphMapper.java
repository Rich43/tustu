package com.sun.javafx.font;

import com.sun.javafx.font.CMap;

/* loaded from: jfxrt.jar:com/sun/javafx/font/OpenTypeGlyphMapper.class */
public class OpenTypeGlyphMapper extends CharToGlyphMapper {
    PrismFontFile font;
    CMap cmap;

    public OpenTypeGlyphMapper(PrismFontFile font) {
        this.font = font;
        try {
            this.cmap = CMap.initialize(font);
        } catch (Exception e2) {
            this.cmap = null;
        }
        if (this.cmap == null) {
            handleBadCMAP();
        }
        this.missingGlyph = 0;
    }

    @Override // com.sun.javafx.font.CharToGlyphMapper
    public int getGlyphCode(int charCode) {
        try {
            return this.cmap.getGlyph(charCode);
        } catch (Exception e2) {
            handleBadCMAP();
            return this.missingGlyph;
        }
    }

    private void handleBadCMAP() {
        this.cmap = CMap.theNullCmap;
    }

    boolean hasSupplementaryChars() {
        return (this.cmap instanceof CMap.CMapFormat8) || (this.cmap instanceof CMap.CMapFormat10) || (this.cmap instanceof CMap.CMapFormat12);
    }
}
