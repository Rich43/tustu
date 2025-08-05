package com.sun.javafx.font.directwrite;

import com.sun.javafx.font.DisposerRecord;
import com.sun.javafx.font.FontStrikeDesc;
import com.sun.javafx.font.Glyph;
import com.sun.javafx.font.PrismFontFactory;
import com.sun.javafx.font.PrismFontStrike;
import com.sun.javafx.geom.Path2D;
import com.sun.javafx.geom.Point2D;
import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.geom.transform.BaseTransform;

/* loaded from: jfxrt.jar:com/sun/javafx/font/directwrite/DWFontStrike.class */
class DWFontStrike extends PrismFontStrike<DWFontFile> {
    DWRITE_MATRIX matrix;
    static final boolean SUBPIXEL_ON;
    static final boolean SUBPIXEL_Y;
    static final boolean SUBPIXEL_NATIVE;

    static {
        int mode = PrismFontFactory.getFontFactory().getSubPixelMode();
        SUBPIXEL_ON = (mode & 1) != 0;
        SUBPIXEL_Y = (mode & 2) != 0;
        SUBPIXEL_NATIVE = (mode & 4) != 0;
    }

    DWFontStrike(DWFontFile fontResource, float size, BaseTransform tx, int aaMode, FontStrikeDesc desc) {
        super(fontResource, size, tx, aaMode, desc);
        float maxDim = PrismFontFactory.getFontSizeLimit();
        if (tx.isTranslateOrIdentity()) {
            this.drawShapes = size > maxDim;
            return;
        }
        BaseTransform tx2d = getTransform();
        this.matrix = new DWRITE_MATRIX();
        this.matrix.m11 = (float) tx2d.getMxx();
        this.matrix.m12 = (float) tx2d.getMyx();
        this.matrix.m21 = (float) tx2d.getMxy();
        this.matrix.m22 = (float) tx2d.getMyy();
        if (Math.abs(this.matrix.m11 * size) > maxDim || Math.abs(this.matrix.m12 * size) > maxDim || Math.abs(this.matrix.m21 * size) > maxDim || Math.abs(this.matrix.m22 * size) > maxDim) {
            this.drawShapes = true;
        }
    }

    @Override // com.sun.javafx.font.PrismFontStrike
    protected DisposerRecord createDisposer(FontStrikeDesc desc) {
        return null;
    }

    @Override // com.sun.javafx.font.PrismFontStrike, com.sun.javafx.font.FontStrike
    public int getQuantizedPosition(Point2D point) {
        if (SUBPIXEL_ON && ((this.matrix == null || SUBPIXEL_NATIVE) && (getAAMode() == 0 || SUBPIXEL_NATIVE))) {
            float subPixel = point.f11907x;
            point.f11907x = (int) point.f11907x;
            float subPixel2 = subPixel - point.f11907x;
            int index = 0;
            if (subPixel2 >= 0.66f) {
                index = 2;
            } else if (subPixel2 >= 0.33f) {
                index = 1;
            }
            if (SUBPIXEL_Y) {
                float subPixel3 = point.f11908y;
                point.f11908y = (int) point.f11908y;
                float subPixel4 = subPixel3 - point.f11908y;
                if (subPixel4 >= 0.66f) {
                    index += 6;
                } else if (subPixel4 >= 0.33f) {
                    index += 3;
                }
            } else {
                point.f11908y = Math.round(point.f11908y);
            }
            return index;
        }
        return super.getQuantizedPosition(point);
    }

    IDWriteFontFace getFontFace() {
        DWFontFile fontResource = getFontResource();
        return fontResource.getFontFace();
    }

    RectBounds getBBox(int glyphCode) {
        DWFontFile fontResource = getFontResource();
        return fontResource.getBBox(glyphCode, getSize());
    }

    int getUpem() {
        return getFontResource().getUnitsPerEm();
    }

    @Override // com.sun.javafx.font.PrismFontStrike
    protected Path2D createGlyphOutline(int glyphCode) {
        DWFontFile fontResource = getFontResource();
        return fontResource.getGlyphOutline(glyphCode, getSize());
    }

    @Override // com.sun.javafx.font.PrismFontStrike
    protected Glyph createGlyph(int glyphCode) {
        return new DWGlyph(this, glyphCode, this.drawShapes);
    }
}
