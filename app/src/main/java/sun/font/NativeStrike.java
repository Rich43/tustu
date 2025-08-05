package sun.font;

import java.awt.Rectangle;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/* loaded from: rt.jar:sun/font/NativeStrike.class */
public class NativeStrike extends PhysicalStrike {
    NativeFont nativeFont;

    NativeStrike(NativeFont nativeFont, FontStrikeDesc fontStrikeDesc) {
        super(nativeFont, fontStrikeDesc);
        throw new RuntimeException("NativeFont not used on Windows");
    }

    NativeStrike(NativeFont nativeFont, FontStrikeDesc fontStrikeDesc, boolean z2) {
        super(nativeFont, fontStrikeDesc);
        throw new RuntimeException("NativeFont not used on Windows");
    }

    @Override // sun.font.FontStrike
    void getGlyphImagePtrs(int[] iArr, long[] jArr, int i2) {
    }

    @Override // sun.font.FontStrike
    long getGlyphImagePtr(int i2) {
        return 0L;
    }

    long getGlyphImagePtrNoCache(int i2) {
        return 0L;
    }

    @Override // sun.font.FontStrike
    void getGlyphImageBounds(int i2, Point2D.Float r3, Rectangle rectangle) {
    }

    @Override // sun.font.FontStrike
    Point2D.Float getGlyphMetrics(int i2) {
        return null;
    }

    @Override // sun.font.FontStrike
    float getGlyphAdvance(int i2) {
        return 0.0f;
    }

    @Override // sun.font.FontStrike
    Rectangle2D.Float getGlyphOutlineBounds(int i2) {
        return null;
    }

    @Override // sun.font.FontStrike
    GeneralPath getGlyphOutline(int i2, float f2, float f3) {
        return null;
    }

    @Override // sun.font.FontStrike
    GeneralPath getGlyphVectorOutline(int[] iArr, float f2, float f3) {
        return null;
    }
}
