package com.sun.javafx.font.coretext;

import com.sun.javafx.font.DisposerRecord;
import com.sun.javafx.font.FontStrikeDesc;
import com.sun.javafx.font.Glyph;
import com.sun.javafx.font.PrismFontFactory;
import com.sun.javafx.font.PrismFontStrike;
import com.sun.javafx.geom.Path2D;
import com.sun.javafx.geom.Point2D;
import com.sun.javafx.geom.transform.BaseTransform;

/* loaded from: jfxrt.jar:com/sun/javafx/font/coretext/CTFontStrike.class */
class CTFontStrike extends PrismFontStrike<CTFontFile> {
    private long fontRef;
    CGAffineTransform matrix;
    static final float SUBPIXEL4_SIZE = 12.0f;
    static final float SUBPIXEL3_SIZE = 18.0f;
    static final float SUBPIXEL2_SIZE = 34.0f;
    private static final boolean SUBPIXEL;

    static {
        int mode = PrismFontFactory.getFontFactory().getSubPixelMode();
        SUBPIXEL = (mode & 1) != 0;
    }

    CTFontStrike(CTFontFile fontResource, float size, BaseTransform graphicsTransform, int aaMode, FontStrikeDesc desc) {
        super(fontResource, size, graphicsTransform, aaMode, desc);
        float maxDim = PrismFontFactory.getFontSizeLimit();
        if (graphicsTransform.isTranslateOrIdentity()) {
            this.drawShapes = size > maxDim;
        } else {
            BaseTransform tx2d = getTransform();
            this.matrix = new CGAffineTransform();
            this.matrix.f11880a = tx2d.getMxx();
            this.matrix.f11881b = -tx2d.getMyx();
            this.matrix.f11882c = -tx2d.getMxy();
            this.matrix.f11883d = tx2d.getMyy();
            if (Math.abs(this.matrix.f11880a * size) > maxDim || Math.abs(this.matrix.f11881b * size) > maxDim || Math.abs(this.matrix.f11882c * size) > maxDim || Math.abs(this.matrix.f11883d * size) > maxDim) {
                this.drawShapes = true;
            }
        }
        if (fontResource.isEmbeddedFont()) {
            long cgFontRef = fontResource.getCGFontRef();
            if (cgFontRef != 0) {
                this.fontRef = OS.CTFontCreateWithGraphicsFont(cgFontRef, size, this.matrix, 0L);
            }
        } else {
            long psNameRef = OS.CFStringCreate(fontResource.getPSName());
            if (psNameRef != 0) {
                this.fontRef = OS.CTFontCreateWithName(psNameRef, size, this.matrix);
                OS.CFRelease(psNameRef);
            }
        }
        if (this.fontRef == 0 && PrismFontFactory.debugFonts) {
            System.err.println("Failed to create CTFont for " + ((Object) this));
        }
    }

    long getFontRef() {
        return this.fontRef;
    }

    @Override // com.sun.javafx.font.PrismFontStrike
    protected DisposerRecord createDisposer(FontStrikeDesc desc) {
        CTFontFile fontResource = getFontResource();
        return new CTStrikeDisposer(fontResource, desc, this.fontRef);
    }

    @Override // com.sun.javafx.font.PrismFontStrike
    protected Glyph createGlyph(int glyphCode) {
        return new CTGlyph(this, glyphCode, this.drawShapes);
    }

    @Override // com.sun.javafx.font.PrismFontStrike, com.sun.javafx.font.FontStrike
    public int getQuantizedPosition(Point2D point) {
        if (SUBPIXEL && this.matrix == null) {
            if (getSize() < 12.0f) {
                float subPixelX = point.f11907x;
                point.f11907x = (int) point.f11907x;
                float subPixelX2 = subPixelX - point.f11907x;
                point.f11908y = Math.round(point.f11908y);
                if (subPixelX2 >= 0.75f) {
                    return 3;
                }
                if (subPixelX2 >= 0.5f) {
                    return 2;
                }
                return subPixelX2 >= 0.25f ? 1 : 0;
            }
            if (getAAMode() == 0) {
                if (getSize() < SUBPIXEL3_SIZE) {
                    float subPixelX3 = point.f11907x;
                    point.f11907x = (int) point.f11907x;
                    float subPixelX4 = subPixelX3 - point.f11907x;
                    point.f11908y = Math.round(point.f11908y);
                    if (subPixelX4 >= 0.66f) {
                        return 2;
                    }
                    return subPixelX4 >= 0.33f ? 1 : 0;
                }
                if (getSize() < SUBPIXEL2_SIZE) {
                    float subPixelX5 = point.f11907x;
                    point.f11907x = (int) point.f11907x;
                    float subPixelX6 = subPixelX5 - point.f11907x;
                    point.f11908y = Math.round(point.f11908y);
                    return subPixelX6 >= 0.5f ? 1 : 0;
                }
                return 0;
            }
        }
        return super.getQuantizedPosition(point);
    }

    float getSubPixelPosition(int index) {
        if (index == 0) {
            return 0.0f;
        }
        float size = getSize();
        if (size < 12.0f) {
            if (index == 3) {
                return 0.75f;
            }
            if (index == 2) {
                return 0.5f;
            }
            return index == 1 ? 0.25f : 0.0f;
        }
        if (getAAMode() == 1) {
            return 0.0f;
        }
        if (size >= SUBPIXEL3_SIZE) {
            return (size >= SUBPIXEL2_SIZE || index != 1) ? 0.0f : 0.5f;
        }
        if (index == 2) {
            return 0.66f;
        }
        return index == 1 ? 0.33f : 0.0f;
    }

    boolean isSubPixelGlyph() {
        return SUBPIXEL && this.matrix == null;
    }

    @Override // com.sun.javafx.font.PrismFontStrike
    protected Path2D createGlyphOutline(int glyphCode) {
        CTFontFile fontResource = getFontResource();
        return fontResource.getGlyphOutline(glyphCode, getSize());
    }

    CGRect getBBox(int glyphCode) {
        CTFontFile fontResource = getFontResource();
        return fontResource.getBBox(glyphCode, getSize());
    }
}
