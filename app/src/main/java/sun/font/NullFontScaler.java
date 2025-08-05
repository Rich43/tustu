package sun.font;

import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/* loaded from: rt.jar:sun/font/NullFontScaler.class */
class NullFontScaler extends FontScaler {
    static native long getNullScalerContext();

    @Override // sun.font.FontScaler
    native long getGlyphImage(long j2, int i2);

    NullFontScaler() {
    }

    public NullFontScaler(Font2D font2D, int i2, boolean z2, int i3) {
    }

    @Override // sun.font.FontScaler
    StrikeMetrics getFontMetrics(long j2) {
        return new StrikeMetrics(240.0f, 240.0f, 240.0f, 240.0f, 240.0f, 240.0f, 240.0f, 240.0f, 240.0f, 240.0f);
    }

    @Override // sun.font.FontScaler
    float getGlyphAdvance(long j2, int i2) {
        return 0.0f;
    }

    @Override // sun.font.FontScaler
    void getGlyphMetrics(long j2, int i2, Point2D.Float r7) {
        r7.f12396x = 0.0f;
        r7.f12397y = 0.0f;
    }

    @Override // sun.font.FontScaler
    Rectangle2D.Float getGlyphOutlineBounds(long j2, int i2) {
        return new Rectangle2D.Float(0.0f, 0.0f, 0.0f, 0.0f);
    }

    @Override // sun.font.FontScaler
    GeneralPath getGlyphOutline(long j2, int i2, float f2, float f3) {
        return new GeneralPath();
    }

    @Override // sun.font.FontScaler
    GeneralPath getGlyphVectorOutline(long j2, int[] iArr, int i2, float f2, float f3) {
        return new GeneralPath();
    }

    @Override // sun.font.FontScaler
    long getLayoutTableCache() {
        return 0L;
    }

    @Override // sun.font.FontScaler
    long createScalerContext(double[] dArr, int i2, int i3, float f2, float f3, boolean z2) {
        return getNullScalerContext();
    }

    @Override // sun.font.FontScaler
    void invalidateScalerContext(long j2) {
    }

    @Override // sun.font.FontScaler
    int getNumGlyphs() throws FontScalerException {
        return 1;
    }

    @Override // sun.font.FontScaler
    int getMissingGlyphCode() throws FontScalerException {
        return 0;
    }

    @Override // sun.font.FontScaler
    int getGlyphCode(char c2) throws FontScalerException {
        return 0;
    }

    @Override // sun.font.FontScaler
    long getUnitsPerEm() {
        return 2048L;
    }

    @Override // sun.font.FontScaler
    Point2D.Float getGlyphPoint(long j2, int i2, int i3) {
        return null;
    }
}
