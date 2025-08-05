package com.sun.javafx.font.freetype;

import com.sun.istack.internal.localization.Localizable;
import com.sun.javafx.font.Disposer;
import com.sun.javafx.font.FontStrikeDesc;
import com.sun.javafx.font.PrismFontFactory;
import com.sun.javafx.font.PrismFontFile;
import com.sun.javafx.font.PrismFontStrike;
import com.sun.javafx.geom.Path2D;
import com.sun.javafx.geom.transform.BaseTransform;

/* loaded from: jfxrt.jar:com/sun/javafx/font/freetype/FTFontFile.class */
class FTFontFile extends PrismFontFile {
    private long library;
    private long face;
    private FTDisposer disposer;

    FTFontFile(String name, String filename, int fIndex, boolean register, boolean embedded, boolean copy, boolean tracked) throws Exception {
        super(name, filename, fIndex, register, embedded, copy, tracked);
        init();
    }

    private synchronized void init() throws Exception {
        long[] ptr = new long[1];
        int error = OSFreetype.FT_Init_FreeType(ptr);
        if (error != 0) {
            throw new Exception("FT_Init_FreeType Failed error " + error);
        }
        this.library = ptr[0];
        if (FTFactory.LCD_SUPPORT) {
            OSFreetype.FT_Library_SetLcdFilter(this.library, 1);
        }
        String file = getFileName();
        int fontIndex = getFontIndex();
        byte[] buffer = (file + Localizable.NOT_LOCALIZABLE).getBytes();
        int error2 = OSFreetype.FT_New_Face(this.library, buffer, fontIndex, ptr);
        if (error2 != 0) {
            throw new Exception("FT_New_Face Failed error " + error2 + " Font File " + file + " Font Index " + fontIndex);
        }
        this.face = ptr[0];
        if (!isRegistered()) {
            this.disposer = new FTDisposer(this.library, this.face);
            Disposer.addRecord(this, this.disposer);
        }
    }

    @Override // com.sun.javafx.font.PrismFontFile
    protected PrismFontStrike<?> createStrike(float size, BaseTransform transform, int aaMode, FontStrikeDesc desc) {
        return new FTFontStrike(this, size, transform, aaMode, desc);
    }

    @Override // com.sun.javafx.font.PrismFontFile
    protected synchronized int[] createGlyphBoundingBox(int gc) {
        OSFreetype.FT_Load_Glyph(this.face, gc, 1);
        int[] bbox = new int[4];
        FT_GlyphSlotRec glyphRec = OSFreetype.getGlyphSlot(this.face);
        if (glyphRec != null && glyphRec.metrics != null) {
            FT_Glyph_Metrics gm = glyphRec.metrics;
            bbox[0] = (int) gm.horiBearingX;
            bbox[1] = (int) (gm.horiBearingY - gm.height);
            bbox[2] = (int) (gm.horiBearingX + gm.width);
            bbox[3] = (int) gm.horiBearingY;
        }
        return bbox;
    }

    synchronized Path2D createGlyphOutline(int gc, float size) {
        int size26dot6 = (int) (size * 64.0f);
        OSFreetype.FT_Set_Char_Size(this.face, 0L, size26dot6, 72, 72);
        OSFreetype.FT_Load_Glyph(this.face, gc, 2058);
        return OSFreetype.FT_Outline_Decompose(this.face);
    }

    synchronized void initGlyph(FTGlyph glyph, FTFontStrike strike) {
        int flags;
        FT_Bitmap bitmap;
        byte[] buffer;
        float size = strike.getSize();
        if (size == 0.0f) {
            glyph.buffer = new byte[0];
            glyph.bitmap = new FT_Bitmap();
            return;
        }
        int size26dot6 = (int) (size * 64.0f);
        OSFreetype.FT_Set_Char_Size(this.face, 0L, size26dot6, 72, 72);
        boolean lcd = strike.getAAMode() == 1 && FTFactory.LCD_SUPPORT;
        int flags2 = 14;
        FT_Matrix matrix = strike.matrix;
        if (matrix != null) {
            OSFreetype.FT_Set_Transform(this.face, matrix, 0L, 0L);
        } else {
            flags2 = 14 | 2048;
        }
        if (lcd) {
            flags = flags2 | 196608;
        } else {
            flags = flags2 | 0;
        }
        int glyphCode = glyph.getGlyphCode();
        int error = OSFreetype.FT_Load_Glyph(this.face, glyphCode, flags);
        if (error != 0) {
            if (PrismFontFactory.debugFonts) {
                System.err.println("FT_Load_Glyph failed " + error + " glyph code " + glyphCode + " load falgs " + flags);
                return;
            }
            return;
        }
        FT_GlyphSlotRec glyphRec = OSFreetype.getGlyphSlot(this.face);
        if (glyphRec == null || (bitmap = glyphRec.bitmap) == null) {
            return;
        }
        int pixelMode = bitmap.pixel_mode;
        int width = bitmap.width;
        int height = bitmap.rows;
        int pitch = bitmap.pitch;
        if (pixelMode != 2 && pixelMode != 5) {
            if (PrismFontFactory.debugFonts) {
                System.err.println("Unexpected pixel mode: " + pixelMode + " glyph code " + glyphCode + " load falgs " + flags);
                return;
            }
            return;
        }
        if (width != 0 && height != 0) {
            buffer = OSFreetype.getBitmapData(this.face);
            if (buffer != null && pitch != width) {
                byte[] newBuffer = new byte[width * height];
                int src = 0;
                int dst = 0;
                for (int y2 = 0; y2 < height; y2++) {
                    for (int x2 = 0; x2 < width; x2++) {
                        newBuffer[dst + x2] = buffer[src + x2];
                    }
                    dst += width;
                    src += pitch;
                }
                buffer = newBuffer;
            }
        } else {
            buffer = new byte[0];
        }
        glyph.buffer = buffer;
        glyph.bitmap = bitmap;
        glyph.bitmap_left = glyphRec.bitmap_left;
        glyph.bitmap_top = glyphRec.bitmap_top;
        glyph.advanceX = glyphRec.advance_x / 64.0f;
        glyph.advanceY = glyphRec.advance_y / 64.0f;
        glyph.userAdvance = glyphRec.linearHoriAdvance / 65536.0f;
        glyph.lcd = lcd;
    }
}
