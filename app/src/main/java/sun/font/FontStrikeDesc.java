package sun.font;

import java.awt.Font;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import sun.awt.SunHints;

/* loaded from: rt.jar:sun/font/FontStrikeDesc.class */
public class FontStrikeDesc {
    static final int AA_ON = 16;
    static final int AA_LCD_H = 32;
    static final int AA_LCD_V = 64;
    static final int FRAC_METRICS_ON = 256;
    static final int FRAC_METRICS_SP = 512;
    AffineTransform devTx;
    AffineTransform glyphTx;
    int style;
    int aaHint;
    int fmHint;
    private int hashCode;
    private int valuemask;

    public int hashCode() {
        if (this.hashCode == 0) {
            this.hashCode = this.glyphTx.hashCode() + this.devTx.hashCode() + this.valuemask;
        }
        return this.hashCode;
    }

    public boolean equals(Object obj) {
        try {
            FontStrikeDesc fontStrikeDesc = (FontStrikeDesc) obj;
            if (fontStrikeDesc.valuemask == this.valuemask && fontStrikeDesc.glyphTx.equals(this.glyphTx)) {
                if (fontStrikeDesc.devTx.equals(this.devTx)) {
                    return true;
                }
            }
            return false;
        } catch (Exception e2) {
            return false;
        }
    }

    FontStrikeDesc() {
    }

    public static int getAAHintIntVal(Object obj, Font2D font2D, int i2) {
        if (FontUtilities.isMacOSX14 && (obj == SunHints.VALUE_TEXT_ANTIALIAS_OFF || obj == SunHints.VALUE_TEXT_ANTIALIAS_DEFAULT || obj == SunHints.VALUE_TEXT_ANTIALIAS_ON || obj == SunHints.VALUE_TEXT_ANTIALIAS_GASP)) {
            return 2;
        }
        if (obj == SunHints.VALUE_TEXT_ANTIALIAS_OFF || obj == SunHints.VALUE_TEXT_ANTIALIAS_DEFAULT) {
            return 1;
        }
        if (obj == SunHints.VALUE_TEXT_ANTIALIAS_ON) {
            return 2;
        }
        if (obj == SunHints.VALUE_TEXT_ANTIALIAS_GASP) {
            if (font2D.useAAForPtSize(i2)) {
                return 2;
            }
            return 1;
        }
        if (obj == SunHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB || obj == SunHints.VALUE_TEXT_ANTIALIAS_LCD_HBGR) {
            return 4;
        }
        if (obj == SunHints.VALUE_TEXT_ANTIALIAS_LCD_VRGB || obj == SunHints.VALUE_TEXT_ANTIALIAS_LCD_VBGR) {
            return 6;
        }
        return 1;
    }

    public static int getAAHintIntVal(Font2D font2D, Font font, FontRenderContext fontRenderContext) {
        int iAbs;
        Object antiAliasingHint = fontRenderContext.getAntiAliasingHint();
        if (FontUtilities.isMacOSX14 && (antiAliasingHint == SunHints.VALUE_TEXT_ANTIALIAS_OFF || antiAliasingHint == SunHints.VALUE_TEXT_ANTIALIAS_DEFAULT || antiAliasingHint == SunHints.VALUE_TEXT_ANTIALIAS_ON || antiAliasingHint == SunHints.VALUE_TEXT_ANTIALIAS_GASP)) {
            return 2;
        }
        if (antiAliasingHint == SunHints.VALUE_TEXT_ANTIALIAS_OFF || antiAliasingHint == SunHints.VALUE_TEXT_ANTIALIAS_DEFAULT) {
            return 1;
        }
        if (antiAliasingHint == SunHints.VALUE_TEXT_ANTIALIAS_ON) {
            return 2;
        }
        if (antiAliasingHint == SunHints.VALUE_TEXT_ANTIALIAS_GASP) {
            AffineTransform transform = fontRenderContext.getTransform();
            if (transform.isIdentity() && !font.isTransformed()) {
                iAbs = font.getSize();
            } else {
                float size2D = font.getSize2D();
                if (transform.isIdentity()) {
                    transform = font.getTransform();
                    transform.scale(size2D, size2D);
                } else {
                    transform.scale(size2D, size2D);
                    if (font.isTransformed()) {
                        transform.concatenate(font.getTransform());
                    }
                }
                double shearX = transform.getShearX();
                double scaleY = transform.getScaleY();
                if (shearX != 0.0d) {
                    scaleY = Math.sqrt((shearX * shearX) + (scaleY * scaleY));
                }
                iAbs = (int) (Math.abs(scaleY) + 0.5d);
            }
            if (font2D.useAAForPtSize(iAbs)) {
                return 2;
            }
            return 1;
        }
        if (antiAliasingHint == SunHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB || antiAliasingHint == SunHints.VALUE_TEXT_ANTIALIAS_LCD_HBGR) {
            return 4;
        }
        if (antiAliasingHint == SunHints.VALUE_TEXT_ANTIALIAS_LCD_VRGB || antiAliasingHint == SunHints.VALUE_TEXT_ANTIALIAS_LCD_VBGR) {
            return 6;
        }
        return 1;
    }

    public static int getFMHintIntVal(Object obj) {
        if (obj == SunHints.VALUE_FRACTIONALMETRICS_OFF || obj == SunHints.VALUE_FRACTIONALMETRICS_DEFAULT) {
            return 1;
        }
        return 2;
    }

    public FontStrikeDesc(AffineTransform affineTransform, AffineTransform affineTransform2, int i2, int i3, int i4) {
        this.devTx = affineTransform;
        this.glyphTx = affineTransform2;
        this.style = i2;
        this.aaHint = i3;
        this.fmHint = i4;
        this.valuemask = i2;
        switch (i3) {
            case 2:
                this.valuemask |= 16;
                break;
            case 4:
            case 5:
                this.valuemask |= 32;
                break;
            case 6:
            case 7:
                this.valuemask |= 64;
                break;
        }
        if (i4 == 2) {
            this.valuemask |= 256;
        }
    }

    FontStrikeDesc(FontStrikeDesc fontStrikeDesc) {
        this.devTx = fontStrikeDesc.devTx;
        this.glyphTx = (AffineTransform) fontStrikeDesc.glyphTx.clone();
        this.style = fontStrikeDesc.style;
        this.aaHint = fontStrikeDesc.aaHint;
        this.fmHint = fontStrikeDesc.fmHint;
        this.hashCode = fontStrikeDesc.hashCode;
        this.valuemask = fontStrikeDesc.valuemask;
    }

    public String toString() {
        return "FontStrikeDesc: Style=" + this.style + " AA=" + this.aaHint + " FM=" + this.fmHint + " devTx=" + ((Object) this.devTx) + " devTx.FontTx.ptSize=" + ((Object) this.glyphTx);
    }
}
