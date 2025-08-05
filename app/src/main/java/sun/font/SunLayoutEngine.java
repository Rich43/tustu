package sun.font;

import java.awt.geom.Point2D;
import java.lang.ref.SoftReference;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentHashMap;
import sun.font.GlyphLayout;

/* loaded from: rt.jar:sun/font/SunLayoutEngine.class */
public final class SunLayoutEngine implements GlyphLayout.LayoutEngine, GlyphLayout.LayoutEngineFactory {
    private GlyphLayout.LayoutEngineKey key;
    private static GlyphLayout.LayoutEngineFactory instance;
    private SoftReference cacheref = new SoftReference(null);
    static WeakHashMap<Font2D, Boolean> aatInfo;

    private static native void initGVIDs();

    private static native void nativeLayout(Font2D font2D, FontStrike fontStrike, float[] fArr, int i2, int i3, char[] cArr, int i4, int i5, int i6, int i7, int i8, int i9, int i10, Point2D.Float r13, GlyphLayout.GVData gVData, long j2, long j3);

    static {
        FontManagerNativeLibrary.load();
        initGVIDs();
        aatInfo = new WeakHashMap<>();
    }

    public static GlyphLayout.LayoutEngineFactory instance() {
        if (instance == null) {
            instance = new SunLayoutEngine();
        }
        return instance;
    }

    private SunLayoutEngine() {
    }

    @Override // sun.font.GlyphLayout.LayoutEngineFactory
    public GlyphLayout.LayoutEngine getEngine(Font2D font2D, int i2, int i3) {
        return getEngine(new GlyphLayout.LayoutEngineKey(font2D, i2, i3));
    }

    @Override // sun.font.GlyphLayout.LayoutEngineFactory
    public GlyphLayout.LayoutEngine getEngine(GlyphLayout.LayoutEngineKey layoutEngineKey) {
        ConcurrentHashMap concurrentHashMap = (ConcurrentHashMap) this.cacheref.get();
        if (concurrentHashMap == null) {
            concurrentHashMap = new ConcurrentHashMap();
            this.cacheref = new SoftReference(concurrentHashMap);
        }
        GlyphLayout.LayoutEngine sunLayoutEngine = (GlyphLayout.LayoutEngine) concurrentHashMap.get(layoutEngineKey);
        if (sunLayoutEngine == null) {
            GlyphLayout.LayoutEngineKey layoutEngineKeyCopy = layoutEngineKey.copy();
            sunLayoutEngine = new SunLayoutEngine(layoutEngineKeyCopy);
            concurrentHashMap.put(layoutEngineKeyCopy, sunLayoutEngine);
        }
        return sunLayoutEngine;
    }

    private SunLayoutEngine(GlyphLayout.LayoutEngineKey layoutEngineKey) {
        this.key = layoutEngineKey;
    }

    private boolean isAAT(Font2D font2D) {
        Boolean bool;
        synchronized (aatInfo) {
            bool = aatInfo.get(font2D);
        }
        if (bool != null) {
            return bool.booleanValue();
        }
        boolean z2 = false;
        if (font2D instanceof TrueTypeFont) {
            TrueTypeFont trueTypeFont = (TrueTypeFont) font2D;
            z2 = (trueTypeFont.getDirectoryEntry(TrueTypeFont.morxTag) == null && trueTypeFont.getDirectoryEntry(1836020340) == null) ? false : true;
        } else if (font2D instanceof PhysicalFont) {
            PhysicalFont physicalFont = (PhysicalFont) font2D;
            z2 = (physicalFont.getTableBytes(TrueTypeFont.morxTag) == null && physicalFont.getTableBytes(1836020340) == null) ? false : true;
        }
        synchronized (aatInfo) {
            aatInfo.put(font2D, Boolean.valueOf(z2));
        }
        return z2;
    }

    @Override // sun.font.GlyphLayout.LayoutEngine
    public void layout(FontStrikeDesc fontStrikeDesc, float[] fArr, int i2, int i3, TextRecord textRecord, int i4, Point2D.Float r27, GlyphLayout.GVData gVData) {
        Font2D font2DFont = this.key.font();
        nativeLayout(font2DFont, font2DFont.getStrike(fontStrikeDesc), fArr, i2, i3, textRecord.text, textRecord.start, textRecord.limit, textRecord.min, textRecord.max, this.key.script(), this.key.lang(), i4, r27, gVData, font2DFont.getUnitsPerEm(), ((i4 & Integer.MIN_VALUE) == 0 || !isAAT(font2DFont)) ? font2DFont.getLayoutTableCache() : 0L);
    }
}
