package com.sun.javafx.font.freetype;

import com.sun.javafx.font.Glyph;
import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.geom.Shape;

/* loaded from: jfxrt.jar:com/sun/javafx/font/freetype/FTGlyph.class */
class FTGlyph implements Glyph {
    FTFontStrike strike;
    int glyphCode;
    byte[] buffer;
    FT_Bitmap bitmap;
    int bitmap_left;
    int bitmap_top;
    float advanceX;
    float advanceY;
    float userAdvance;
    boolean lcd;

    FTGlyph(FTFontStrike strike, int glyphCode, boolean drawAsShape) {
        this.strike = strike;
        this.glyphCode = glyphCode;
    }

    @Override // com.sun.javafx.font.Glyph
    public int getGlyphCode() {
        return this.glyphCode;
    }

    private void init() {
        if (this.bitmap != null) {
            return;
        }
        this.strike.initGlyph(this);
    }

    @Override // com.sun.javafx.font.Glyph
    public RectBounds getBBox() {
        float[] bb2 = new float[4];
        FTFontFile fontResource = this.strike.getFontResource();
        fontResource.getGlyphBoundingBox(this.glyphCode, this.strike.getSize(), bb2);
        return new RectBounds(bb2[0], bb2[1], bb2[2], bb2[3]);
    }

    @Override // com.sun.javafx.font.Glyph
    public float getAdvance() {
        init();
        return this.userAdvance;
    }

    @Override // com.sun.javafx.font.Glyph
    public Shape getShape() {
        return this.strike.createGlyphOutline(this.glyphCode);
    }

    @Override // com.sun.javafx.font.Glyph
    public byte[] getPixelData() {
        init();
        return this.buffer;
    }

    @Override // com.sun.javafx.font.Glyph
    public byte[] getPixelData(int subPixel) {
        init();
        return this.buffer;
    }

    @Override // com.sun.javafx.font.Glyph
    public float getPixelXAdvance() {
        init();
        return this.advanceX;
    }

    @Override // com.sun.javafx.font.Glyph
    public float getPixelYAdvance() {
        init();
        return this.advanceY;
    }

    @Override // com.sun.javafx.font.Glyph
    public int getWidth() {
        init();
        if (this.bitmap != null) {
            return this.bitmap.width;
        }
        return 0;
    }

    @Override // com.sun.javafx.font.Glyph
    public int getHeight() {
        init();
        if (this.bitmap != null) {
            return this.bitmap.rows;
        }
        return 0;
    }

    @Override // com.sun.javafx.font.Glyph
    public int getOriginX() {
        init();
        return this.bitmap_left;
    }

    @Override // com.sun.javafx.font.Glyph
    public int getOriginY() {
        init();
        return -this.bitmap_top;
    }

    @Override // com.sun.javafx.font.Glyph
    public boolean isLCDGlyph() {
        return this.lcd;
    }
}
