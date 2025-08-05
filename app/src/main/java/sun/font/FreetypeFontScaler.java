package sun.font;

import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.lang.ref.WeakReference;

/* loaded from: rt.jar:sun/font/FreetypeFontScaler.class */
class FreetypeFontScaler extends FontScaler {
    private static final int TRUETYPE_FONT = 1;
    private static final int TYPE1_FONT = 2;

    private static native void initIDs(Class cls);

    private native long initNativeScaler(Font2D font2D, int i2, int i3, boolean z2, int i4);

    private native StrikeMetrics getFontMetricsNative(Font2D font2D, long j2, long j3);

    private native float getGlyphAdvanceNative(Font2D font2D, long j2, long j3, int i2);

    private native void getGlyphMetricsNative(Font2D font2D, long j2, long j3, int i2, Point2D.Float r7);

    private native long getGlyphImageNative(Font2D font2D, long j2, long j3, int i2);

    private native Rectangle2D.Float getGlyphOutlineBoundsNative(Font2D font2D, long j2, long j3, int i2);

    private native GeneralPath getGlyphOutlineNative(Font2D font2D, long j2, long j3, int i2, float f2, float f3);

    private native GeneralPath getGlyphVectorOutlineNative(Font2D font2D, long j2, long j3, int[] iArr, int i2, float f2, float f3);

    private native Point2D.Float getGlyphPointNative(Font2D font2D, long j2, long j3, int i2, int i3);

    private native long getLayoutTableCacheNative(long j2);

    private native void disposeNativeScaler(Font2D font2D, long j2);

    private native int getGlyphCodeNative(Font2D font2D, long j2, char c2);

    private native int getNumGlyphsNative(long j2);

    private native int getMissingGlyphCodeNative(long j2);

    private native long getUnitsPerEMNative(long j2);

    private native long createScalerContextNative(long j2, double[] dArr, int i2, int i3, float f2, float f3);

    static {
        FontManagerNativeLibrary.load();
        initIDs(FreetypeFontScaler.class);
    }

    private void invalidateScaler() throws FontScalerException {
        this.nativeScaler = 0L;
        this.font = null;
        throw new FontScalerException();
    }

    public FreetypeFontScaler(Font2D font2D, int i2, boolean z2, int i3) {
        this.nativeScaler = initNativeScaler(font2D, font2D instanceof Type1Font ? 2 : 1, i2, z2, i3);
        this.font = new WeakReference<>(font2D);
    }

    @Override // sun.font.FontScaler
    synchronized StrikeMetrics getFontMetrics(long j2) throws FontScalerException {
        if (this.nativeScaler != 0) {
            return getFontMetricsNative(this.font.get(), j2, this.nativeScaler);
        }
        return FontScaler.getNullScaler().getFontMetrics(0L);
    }

    @Override // sun.font.FontScaler
    synchronized float getGlyphAdvance(long j2, int i2) throws FontScalerException {
        if (this.nativeScaler != 0) {
            return getGlyphAdvanceNative(this.font.get(), j2, this.nativeScaler, i2);
        }
        return FontScaler.getNullScaler().getGlyphAdvance(0L, i2);
    }

    @Override // sun.font.FontScaler
    synchronized void getGlyphMetrics(long j2, int i2, Point2D.Float r13) throws FontScalerException {
        if (this.nativeScaler != 0) {
            getGlyphMetricsNative(this.font.get(), j2, this.nativeScaler, i2, r13);
        } else {
            FontScaler.getNullScaler().getGlyphMetrics(0L, i2, r13);
        }
    }

    @Override // sun.font.FontScaler
    synchronized long getGlyphImage(long j2, int i2) throws FontScalerException {
        if (this.nativeScaler != 0) {
            return getGlyphImageNative(this.font.get(), j2, this.nativeScaler, i2);
        }
        return FontScaler.getNullScaler().getGlyphImage(0L, i2);
    }

    @Override // sun.font.FontScaler
    synchronized Rectangle2D.Float getGlyphOutlineBounds(long j2, int i2) throws FontScalerException {
        if (this.nativeScaler != 0) {
            return getGlyphOutlineBoundsNative(this.font.get(), j2, this.nativeScaler, i2);
        }
        return FontScaler.getNullScaler().getGlyphOutlineBounds(0L, i2);
    }

    @Override // sun.font.FontScaler
    synchronized GeneralPath getGlyphOutline(long j2, int i2, float f2, float f3) throws FontScalerException {
        if (this.nativeScaler != 0) {
            return getGlyphOutlineNative(this.font.get(), j2, this.nativeScaler, i2, f2, f3);
        }
        return FontScaler.getNullScaler().getGlyphOutline(0L, i2, f2, f3);
    }

    @Override // sun.font.FontScaler
    synchronized GeneralPath getGlyphVectorOutline(long j2, int[] iArr, int i2, float f2, float f3) throws FontScalerException {
        if (this.nativeScaler != 0) {
            return getGlyphVectorOutlineNative(this.font.get(), j2, this.nativeScaler, iArr, i2, f2, f3);
        }
        return FontScaler.getNullScaler().getGlyphVectorOutline(0L, iArr, i2, f2, f3);
    }

    @Override // sun.font.FontScaler
    synchronized long getLayoutTableCache() throws FontScalerException {
        return getLayoutTableCacheNative(this.nativeScaler);
    }

    @Override // sun.font.FontScaler, sun.java2d.DisposerRecord
    public synchronized void dispose() {
        if (this.nativeScaler != 0) {
            disposeNativeScaler(this.font.get(), this.nativeScaler);
            this.nativeScaler = 0L;
        }
    }

    @Override // sun.font.FontScaler
    public synchronized void disposeScaler() {
        if (this.nativeScaler != 0) {
            new Thread(null, () -> {
                dispose();
            }, "free scaler", 0L).start();
        }
    }

    @Override // sun.font.FontScaler
    synchronized int getNumGlyphs() throws FontScalerException {
        if (this.nativeScaler != 0) {
            return getNumGlyphsNative(this.nativeScaler);
        }
        return FontScaler.getNullScaler().getNumGlyphs();
    }

    @Override // sun.font.FontScaler
    synchronized int getMissingGlyphCode() throws FontScalerException {
        if (this.nativeScaler != 0) {
            return getMissingGlyphCodeNative(this.nativeScaler);
        }
        return FontScaler.getNullScaler().getMissingGlyphCode();
    }

    @Override // sun.font.FontScaler
    synchronized int getGlyphCode(char c2) throws FontScalerException {
        if (this.nativeScaler != 0) {
            return getGlyphCodeNative(this.font.get(), this.nativeScaler, c2);
        }
        return FontScaler.getNullScaler().getGlyphCode(c2);
    }

    @Override // sun.font.FontScaler
    synchronized Point2D.Float getGlyphPoint(long j2, int i2, int i3) throws FontScalerException {
        if (this.nativeScaler != 0) {
            return getGlyphPointNative(this.font.get(), j2, this.nativeScaler, i2, i3);
        }
        return FontScaler.getNullScaler().getGlyphPoint(j2, i2, i3);
    }

    @Override // sun.font.FontScaler
    synchronized long getUnitsPerEm() {
        return getUnitsPerEMNative(this.nativeScaler);
    }

    @Override // sun.font.FontScaler
    synchronized long createScalerContext(double[] dArr, int i2, int i3, float f2, float f3, boolean z2) {
        if (this.nativeScaler != 0) {
            return createScalerContextNative(this.nativeScaler, dArr, i2, i3, f2, f3);
        }
        return NullFontScaler.getNullScalerContext();
    }

    @Override // sun.font.FontScaler
    void invalidateScalerContext(long j2) {
    }
}
