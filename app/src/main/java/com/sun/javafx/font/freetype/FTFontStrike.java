package com.sun.javafx.font.freetype;

import com.sun.javafx.font.DisposerRecord;
import com.sun.javafx.font.FontStrikeDesc;
import com.sun.javafx.font.Glyph;
import com.sun.javafx.font.PrismFontFactory;
import com.sun.javafx.font.PrismFontStrike;
import com.sun.javafx.geom.Path2D;
import com.sun.javafx.geom.transform.BaseTransform;

/* loaded from: jfxrt.jar:com/sun/javafx/font/freetype/FTFontStrike.class */
class FTFontStrike extends PrismFontStrike<FTFontFile> {
    FT_Matrix matrix;

    protected FTFontStrike(FTFontFile fontResource, float size, BaseTransform tx, int aaMode, FontStrikeDesc desc) {
        super(fontResource, size, tx, aaMode, desc);
        float maxDim = PrismFontFactory.getFontSizeLimit();
        if (tx.isTranslateOrIdentity()) {
            this.drawShapes = size > maxDim;
            return;
        }
        BaseTransform tx2d = getTransform();
        this.matrix = new FT_Matrix();
        this.matrix.xx = (int) (tx2d.getMxx() * 65536.0d);
        this.matrix.yx = (int) ((-tx2d.getMyx()) * 65536.0d);
        this.matrix.xy = (int) ((-tx2d.getMxy()) * 65536.0d);
        this.matrix.yy = (int) (tx2d.getMyy() * 65536.0d);
        if (Math.abs(tx2d.getMxx() * size) > maxDim || Math.abs(tx2d.getMyx() * size) > maxDim || Math.abs(tx2d.getMxy() * size) > maxDim || Math.abs(tx2d.getMyy() * size) > maxDim) {
            this.drawShapes = true;
        }
    }

    @Override // com.sun.javafx.font.PrismFontStrike
    protected DisposerRecord createDisposer(FontStrikeDesc desc) {
        return null;
    }

    @Override // com.sun.javafx.font.PrismFontStrike
    protected Glyph createGlyph(int glyphCode) {
        return new FTGlyph(this, glyphCode, this.drawShapes);
    }

    @Override // com.sun.javafx.font.PrismFontStrike
    protected Path2D createGlyphOutline(int glyphCode) {
        FTFontFile fontResource = getFontResource();
        return fontResource.createGlyphOutline(glyphCode, getSize());
    }

    void initGlyph(FTGlyph glyph) {
        FTFontFile fontResource = getFontResource();
        fontResource.initGlyph(glyph, this);
    }
}
