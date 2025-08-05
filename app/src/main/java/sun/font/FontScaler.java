package sun.font;

import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.lang.ref.WeakReference;
import java.lang.reflect.Constructor;
import sun.java2d.Disposer;
import sun.java2d.DisposerRecord;

/* loaded from: rt.jar:sun/font/FontScaler.class */
public abstract class FontScaler implements DisposerRecord {
    private static FontScaler nullScaler = null;
    private static Constructor<FontScaler> scalerConstructor;
    protected WeakReference<Font2D> font = null;
    protected long nativeScaler = 0;
    protected boolean disposed = false;

    abstract StrikeMetrics getFontMetrics(long j2) throws FontScalerException;

    abstract float getGlyphAdvance(long j2, int i2) throws FontScalerException;

    abstract void getGlyphMetrics(long j2, int i2, Point2D.Float r4) throws FontScalerException;

    abstract long getGlyphImage(long j2, int i2) throws FontScalerException;

    abstract Rectangle2D.Float getGlyphOutlineBounds(long j2, int i2) throws FontScalerException;

    abstract GeneralPath getGlyphOutline(long j2, int i2, float f2, float f3) throws FontScalerException;

    abstract GeneralPath getGlyphVectorOutline(long j2, int[] iArr, int i2, float f2, float f3) throws FontScalerException;

    abstract int getNumGlyphs() throws FontScalerException;

    abstract int getMissingGlyphCode() throws FontScalerException;

    abstract int getGlyphCode(char c2) throws FontScalerException;

    abstract long getLayoutTableCache() throws FontScalerException;

    abstract Point2D.Float getGlyphPoint(long j2, int i2, int i3) throws FontScalerException;

    abstract long getUnitsPerEm();

    abstract long createScalerContext(double[] dArr, int i2, int i3, float f2, float f3, boolean z2);

    abstract void invalidateScalerContext(long j2);

    static {
        Class<?> cls;
        scalerConstructor = null;
        Class<?>[] clsArr = {Font2D.class, Integer.TYPE, Boolean.TYPE, Integer.TYPE};
        try {
            if (FontUtilities.isOpenJDK) {
                cls = Class.forName("sun.font.FreetypeFontScaler");
            } else {
                cls = Class.forName("sun.font.T2KFontScaler");
            }
        } catch (ClassNotFoundException e2) {
            cls = NullFontScaler.class;
        }
        try {
            scalerConstructor = cls.getConstructor(clsArr);
        } catch (NoSuchMethodException e3) {
        }
    }

    public static FontScaler getScaler(Font2D font2D, int i2, boolean z2, int i3) {
        FontScaler fontScalerNewInstance;
        try {
            fontScalerNewInstance = scalerConstructor.newInstance(font2D, Integer.valueOf(i2), Boolean.valueOf(z2), Integer.valueOf(i3));
            Disposer.addObjectRecord(font2D, fontScalerNewInstance);
        } catch (Throwable th) {
            fontScalerNewInstance = nullScaler;
            FontManagerFactory.getInstance().deRegisterBadFont(font2D);
        }
        return fontScalerNewInstance;
    }

    public static synchronized FontScaler getNullScaler() {
        if (nullScaler == null) {
            nullScaler = new NullFontScaler();
        }
        return nullScaler;
    }

    @Override // sun.java2d.DisposerRecord
    public void dispose() {
    }

    public void disposeScaler() {
    }
}
