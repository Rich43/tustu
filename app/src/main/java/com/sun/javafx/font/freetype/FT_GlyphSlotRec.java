package com.sun.javafx.font.freetype;

/* loaded from: jfxrt.jar:com/sun/javafx/font/freetype/FT_GlyphSlotRec.class */
class FT_GlyphSlotRec {
    long linearHoriAdvance;
    long linearVertAdvance;
    long advance_x;
    long advance_y;
    int format;
    int bitmap_left;
    int bitmap_top;
    FT_Glyph_Metrics metrics = new FT_Glyph_Metrics();
    FT_Bitmap bitmap = new FT_Bitmap();

    FT_GlyphSlotRec() {
    }
}
