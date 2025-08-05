package sun.font;

import java.awt.FontFormatException;
import java.awt.font.FontRenderContext;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/* loaded from: rt.jar:sun/font/NativeFont.class */
public class NativeFont extends PhysicalFont {
    public NativeFont(String str, boolean z2) throws FontFormatException {
        throw new FontFormatException("NativeFont not used on Windows");
    }

    static boolean hasExternalBitmaps(String str) {
        return false;
    }

    @Override // sun.font.Font2D
    public CharToGlyphMapper getMapper() {
        return null;
    }

    PhysicalFont getDelegateFont() {
        return null;
    }

    @Override // sun.font.Font2D
    FontStrike createStrike(FontStrikeDesc fontStrikeDesc) {
        return null;
    }

    public Rectangle2D getMaxCharBounds(FontRenderContext fontRenderContext) {
        return null;
    }

    @Override // sun.font.PhysicalFont
    StrikeMetrics getFontMetrics(long j2) {
        return null;
    }

    @Override // sun.font.PhysicalFont
    public GeneralPath getGlyphOutline(long j2, int i2, float f2, float f3) {
        return null;
    }

    @Override // sun.font.PhysicalFont
    public GeneralPath getGlyphVectorOutline(long j2, int[] iArr, int i2, float f2, float f3) {
        return null;
    }

    @Override // sun.font.PhysicalFont
    long getGlyphImage(long j2, int i2) {
        return 0L;
    }

    @Override // sun.font.PhysicalFont
    void getGlyphMetrics(long j2, int i2, Point2D.Float r5) {
    }

    @Override // sun.font.PhysicalFont
    float getGlyphAdvance(long j2, int i2) {
        return 0.0f;
    }

    @Override // sun.font.PhysicalFont
    Rectangle2D.Float getGlyphOutlineBounds(long j2, int i2) {
        return new Rectangle2D.Float(0.0f, 0.0f, 0.0f, 0.0f);
    }
}
