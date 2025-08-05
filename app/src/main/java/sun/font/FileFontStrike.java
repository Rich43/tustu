package sun.font;

import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.concurrent.ConcurrentHashMap;

/* loaded from: rt.jar:sun/font/FileFontStrike.class */
public class FileFontStrike extends PhysicalStrike {
    static final int INVISIBLE_GLYPHS = 65534;
    private FileFont fileFont;
    private static final int UNINITIALISED = 0;
    private static final int INTARRAY = 1;
    private static final int LONGARRAY = 2;
    private static final int SEGINTARRAY = 3;
    private static final int SEGLONGARRAY = 4;
    private volatile int glyphCacheFormat;
    private static final int SEGSHIFT = 5;
    private static final int SEGSIZE = 32;
    private boolean segmentedCache;
    private int[][] segIntGlyphImages;
    private long[][] segLongGlyphImages;
    private float[] horizontalAdvances;
    private float[][] segHorizontalAdvances;
    ConcurrentHashMap<Integer, Rectangle2D.Float> boundsMap;
    SoftReference<ConcurrentHashMap<Integer, Point2D.Float>> glyphMetricsMapRef;
    AffineTransform invertDevTx;
    boolean useNatives;
    NativeStrike[] nativeStrikes;
    private int intPtSize;
    private static boolean isXPorLater;
    private WeakReference<ConcurrentHashMap<Integer, GeneralPath>> outlineMapRef;

    private static native boolean initNative();

    private native long _getGlyphImageFromWindows(String str, int i2, int i3, int i4, boolean z2, int i5);

    static {
        isXPorLater = false;
        if (FontUtilities.isWindows && !FontUtilities.useT2K && !GraphicsEnvironment.isHeadless()) {
            isXPorLater = initNative();
        }
    }

    /* JADX WARN: Type inference failed for: r1v47, types: [float[], float[][]] */
    FileFontStrike(FileFont fileFont, FontStrikeDesc fontStrikeDesc) {
        super(fileFont, fontStrikeDesc);
        this.glyphCacheFormat = 0;
        this.fileFont = fileFont;
        if (fontStrikeDesc.style != fileFont.style) {
            if ((fontStrikeDesc.style & 2) == 2 && (fileFont.style & 2) == 0) {
                this.algoStyle = true;
                this.italic = 0.7f;
            }
            if ((fontStrikeDesc.style & 1) == 1 && (fileFont.style & 1) == 0) {
                this.algoStyle = true;
                this.boldness = 1.33f;
            }
        }
        double[] dArr = new double[4];
        AffineTransform affineTransform = fontStrikeDesc.glyphTx;
        affineTransform.getMatrix(dArr);
        if (!fontStrikeDesc.devTx.isIdentity() && fontStrikeDesc.devTx.getType() != 1) {
            try {
                this.invertDevTx = fontStrikeDesc.devTx.createInverse();
            } catch (NoninvertibleTransformException e2) {
            }
        }
        boolean z2 = fontStrikeDesc.aaHint != 1 && fileFont.familyName.startsWith("Amble");
        if (Double.isNaN(dArr[0]) || Double.isNaN(dArr[1]) || Double.isNaN(dArr[2]) || Double.isNaN(dArr[3]) || fileFont.getScaler() == null) {
            this.pScalerContext = NullFontScaler.getNullScalerContext();
        } else {
            this.pScalerContext = fileFont.getScaler().createScalerContext(dArr, fontStrikeDesc.aaHint, fontStrikeDesc.fmHint, this.boldness, this.italic, z2);
        }
        this.mapper = fileFont.getMapper();
        int numGlyphs = this.mapper.getNumGlyphs();
        float f2 = (float) dArr[3];
        int i2 = (int) f2;
        this.intPtSize = i2;
        this.segmentedCache = numGlyphs > 256 || (numGlyphs > 64 && (!((affineTransform.getType() & 124) == 0) || f2 != ((float) i2) || i2 < 6 || i2 > 36));
        if (this.pScalerContext == 0) {
            this.disposer = new FontStrikeDisposer(fileFont, fontStrikeDesc);
            initGlyphCache();
            this.pScalerContext = NullFontScaler.getNullScalerContext();
            SunFontManager.getInstance().deRegisterBadFont(fileFont);
            return;
        }
        if (FontUtilities.isWindows && isXPorLater && !FontUtilities.useT2K && !GraphicsEnvironment.isHeadless() && !fileFont.useJavaRasterizer && ((fontStrikeDesc.aaHint == 4 || fontStrikeDesc.aaHint == 5) && dArr[1] == 0.0d && dArr[2] == 0.0d && dArr[0] == dArr[3] && dArr[0] >= 3.0d && dArr[0] <= 100.0d && !((TrueTypeFont) fileFont).useEmbeddedBitmapsForSize(this.intPtSize))) {
            this.useNatives = true;
        } else if (fileFont.checkUseNatives() && fontStrikeDesc.aaHint == 0 && !this.algoStyle && dArr[1] == 0.0d && dArr[2] == 0.0d && dArr[0] >= 6.0d && dArr[0] <= 36.0d && dArr[0] == dArr[3]) {
            this.useNatives = true;
            int length = fileFont.nativeFonts.length;
            this.nativeStrikes = new NativeStrike[length];
            for (int i3 = 0; i3 < length; i3++) {
                this.nativeStrikes[i3] = new NativeStrike(fileFont.nativeFonts[i3], fontStrikeDesc, false);
            }
        }
        if (FontUtilities.isLogging() && FontUtilities.isWindows) {
            FontUtilities.getLogger().info("Strike for " + ((Object) fileFont) + " at size = " + this.intPtSize + " use natives = " + this.useNatives + " useJavaRasteriser = " + fileFont.useJavaRasterizer + " AAHint = " + fontStrikeDesc.aaHint + " Has Embedded bitmaps = " + ((TrueTypeFont) fileFont).useEmbeddedBitmapsForSize(this.intPtSize));
        }
        this.disposer = new FontStrikeDisposer(fileFont, fontStrikeDesc, this.pScalerContext);
        this.getImageWithAdvance = Math.abs(affineTransform.getScaleX()) <= 48.0d && Math.abs(affineTransform.getScaleY()) <= 48.0d && Math.abs(affineTransform.getShearX()) <= 48.0d && Math.abs(affineTransform.getShearY()) <= 48.0d;
        if (!this.getImageWithAdvance) {
            if (!this.segmentedCache) {
                this.horizontalAdvances = new float[numGlyphs];
                for (int i4 = 0; i4 < numGlyphs; i4++) {
                    this.horizontalAdvances[i4] = Float.MAX_VALUE;
                }
                return;
            }
            this.segHorizontalAdvances = new float[((numGlyphs + 32) - 1) / 32];
        }
    }

    @Override // sun.font.PhysicalStrike, sun.font.FontStrike
    public int getNumGlyphs() {
        return this.fileFont.getNumGlyphs();
    }

    long getGlyphImageFromNative(int i2) {
        if (FontUtilities.isWindows) {
            return getGlyphImageFromWindows(i2);
        }
        return getGlyphImageFromX11(i2);
    }

    long getGlyphImageFromWindows(int i2) {
        String familyName = this.fileFont.getFamilyName(null);
        int style = (this.desc.style & 1) | (this.desc.style & 2) | this.fileFont.getStyle();
        int i3 = this.intPtSize;
        long j_getGlyphImageFromWindows = _getGlyphImageFromWindows(familyName, style, i3, i2, this.desc.fmHint == 2, ((TrueTypeFont) this.fileFont).fontDataSize);
        if (j_getGlyphImageFromWindows != 0) {
            StrikeCache.unsafe.putFloat(j_getGlyphImageFromWindows + StrikeCache.xAdvanceOffset, getGlyphAdvance(i2, false));
            return j_getGlyphImageFromWindows;
        }
        if (FontUtilities.isLogging()) {
            FontUtilities.getLogger().warning("Failed to render glyph using GDI: code=" + i2 + ", fontFamily=" + familyName + ", style=" + style + ", size=" + i3);
        }
        return this.fileFont.getGlyphImage(this.pScalerContext, i2);
    }

    long getGlyphImageFromX11(int i2) {
        char c2 = this.fileFont.glyphToCharMap[i2];
        for (int i3 = 0; i3 < this.nativeStrikes.length; i3++) {
            CharToGlyphMapper mapper = this.fileFont.nativeFonts[i3].getMapper();
            int iCharToGlyph = mapper.charToGlyph(c2) & 65535;
            if (iCharToGlyph != mapper.getMissingGlyphCode()) {
                long glyphImagePtrNoCache = this.nativeStrikes[i3].getGlyphImagePtrNoCache(iCharToGlyph);
                if (glyphImagePtrNoCache != 0) {
                    return glyphImagePtrNoCache;
                }
            }
        }
        return this.fileFont.getGlyphImage(this.pScalerContext, i2);
    }

    @Override // sun.font.FontStrike
    long getGlyphImagePtr(int i2) {
        if (i2 >= 65534) {
            return StrikeCache.invisibleGlyphPtr;
        }
        long cachedGlyphPtr = getCachedGlyphPtr(i2);
        long glyphImage = cachedGlyphPtr;
        if (cachedGlyphPtr != 0) {
            return glyphImage;
        }
        if (this.useNatives) {
            glyphImage = getGlyphImageFromNative(i2);
            if (glyphImage == 0 && FontUtilities.isLogging()) {
                FontUtilities.getLogger().info("Strike for " + ((Object) this.fileFont) + " at size = " + this.intPtSize + " couldn't get native glyph for code = " + i2);
            }
        }
        if (glyphImage == 0) {
            glyphImage = this.fileFont.getGlyphImage(this.pScalerContext, i2);
        }
        return setCachedGlyphPtr(i2, glyphImage);
    }

    @Override // sun.font.FontStrike
    void getGlyphImagePtrs(int[] iArr, long[] jArr, int i2) {
        for (int i3 = 0; i3 < i2; i3++) {
            int i4 = iArr[i3];
            if (i4 >= 65534) {
                jArr[i3] = StrikeCache.invisibleGlyphPtr;
            } else {
                long cachedGlyphPtr = getCachedGlyphPtr(i4);
                jArr[i3] = cachedGlyphPtr;
                if (cachedGlyphPtr == 0) {
                    long glyphImage = 0;
                    if (this.useNatives) {
                        glyphImage = getGlyphImageFromNative(i4);
                    }
                    if (glyphImage == 0) {
                        glyphImage = this.fileFont.getGlyphImage(this.pScalerContext, i4);
                    }
                    jArr[i3] = setCachedGlyphPtr(i4, glyphImage);
                }
            }
        }
    }

    @Override // sun.font.PhysicalStrike
    int getSlot0GlyphImagePtrs(int[] iArr, long[] jArr, int i2) {
        int i3 = 0;
        for (int i4 = 0; i4 < i2; i4++) {
            int i5 = iArr[i4];
            if ((i5 >>> 24) != 0) {
                return i3;
            }
            i3++;
            if (i5 >= 65534) {
                jArr[i4] = StrikeCache.invisibleGlyphPtr;
            } else {
                long cachedGlyphPtr = getCachedGlyphPtr(i5);
                jArr[i4] = cachedGlyphPtr;
                if (cachedGlyphPtr == 0) {
                    long glyphImage = 0;
                    if (this.useNatives) {
                        glyphImage = getGlyphImageFromNative(i5);
                    }
                    if (glyphImage == 0) {
                        glyphImage = this.fileFont.getGlyphImage(this.pScalerContext, i5);
                    }
                    jArr[i4] = setCachedGlyphPtr(i5, glyphImage);
                }
            }
        }
        return i3;
    }

    long getCachedGlyphPtr(int i2) {
        try {
            return getCachedGlyphPtrInternal(i2);
        } catch (Exception e2) {
            return ((NullFontScaler) FontScaler.getNullScaler()).getGlyphImage(NullFontScaler.getNullScalerContext(), i2);
        }
    }

    private long getCachedGlyphPtrInternal(int i2) {
        switch (this.glyphCacheFormat) {
            case 1:
                return this.intGlyphImages[i2] & 4294967295L;
            case 2:
                return this.longGlyphImages[i2];
            case 3:
                if (this.segIntGlyphImages[i2 >> 5] != null) {
                    return this.segIntGlyphImages[r0][i2 % 32] & 4294967295L;
                }
                return 0L;
            case 4:
                int i3 = i2 >> 5;
                if (this.segLongGlyphImages[i3] != null) {
                    return this.segLongGlyphImages[i3][i2 % 32];
                }
                return 0L;
            default:
                return 0L;
        }
    }

    private synchronized long setCachedGlyphPtr(int i2, long j2) {
        try {
            return setCachedGlyphPtrInternal(i2, j2);
        } catch (Exception e2) {
            switch (this.glyphCacheFormat) {
                case 1:
                case 3:
                    StrikeCache.freeIntPointer((int) j2);
                    break;
                case 2:
                case 4:
                    StrikeCache.freeLongPointer(j2);
                    break;
            }
            return ((NullFontScaler) FontScaler.getNullScaler()).getGlyphImage(NullFontScaler.getNullScalerContext(), i2);
        }
    }

    private long setCachedGlyphPtrInternal(int i2, long j2) {
        switch (this.glyphCacheFormat) {
            case 1:
                if (this.intGlyphImages[i2] == 0) {
                    this.intGlyphImages[i2] = (int) j2;
                    return j2;
                }
                StrikeCache.freeIntPointer((int) j2);
                return this.intGlyphImages[i2] & 4294967295L;
            case 2:
                if (this.longGlyphImages[i2] == 0) {
                    this.longGlyphImages[i2] = j2;
                    return j2;
                }
                StrikeCache.freeLongPointer(j2);
                return this.longGlyphImages[i2];
            case 3:
                int i3 = i2 >> 5;
                int i4 = i2 % 32;
                if (this.segIntGlyphImages[i3] == null) {
                    this.segIntGlyphImages[i3] = new int[32];
                }
                if (this.segIntGlyphImages[i3][i4] == 0) {
                    this.segIntGlyphImages[i3][i4] = (int) j2;
                    return j2;
                }
                StrikeCache.freeIntPointer((int) j2);
                return this.segIntGlyphImages[i3][i4] & 4294967295L;
            case 4:
                int i5 = i2 >> 5;
                int i6 = i2 % 32;
                if (this.segLongGlyphImages[i5] == null) {
                    this.segLongGlyphImages[i5] = new long[32];
                }
                if (this.segLongGlyphImages[i5][i6] == 0) {
                    this.segLongGlyphImages[i5][i6] = j2;
                    return j2;
                }
                StrikeCache.freeLongPointer(j2);
                return this.segLongGlyphImages[i5][i6];
            default:
                initGlyphCache();
                return setCachedGlyphPtr(i2, j2);
        }
    }

    /* JADX WARN: Type inference failed for: r1v13, types: [int[], int[][]] */
    /* JADX WARN: Type inference failed for: r1v17, types: [long[], long[][]] */
    private synchronized void initGlyphCache() {
        int i2;
        int numGlyphs = this.mapper.getNumGlyphs();
        if (this.segmentedCache) {
            int i3 = ((numGlyphs + 32) - 1) / 32;
            if (longAddresses) {
                i2 = 4;
                this.segLongGlyphImages = new long[i3];
                this.disposer.segLongGlyphImages = this.segLongGlyphImages;
            } else {
                i2 = 3;
                this.segIntGlyphImages = new int[i3];
                this.disposer.segIntGlyphImages = this.segIntGlyphImages;
            }
        } else if (longAddresses) {
            i2 = 2;
            this.longGlyphImages = new long[numGlyphs];
            this.disposer.longGlyphImages = this.longGlyphImages;
        } else {
            i2 = 1;
            this.intGlyphImages = new int[numGlyphs];
            this.disposer.intGlyphImages = this.intGlyphImages;
        }
        this.glyphCacheFormat = i2;
    }

    @Override // sun.font.FontStrike
    float getGlyphAdvance(int i2) {
        return getGlyphAdvance(i2, true);
    }

    private float getGlyphAdvance(int i2, boolean z2) {
        float glyphAdvance;
        long cachedGlyphPtr;
        if (i2 >= 65534) {
            return 0.0f;
        }
        if (this.horizontalAdvances != null) {
            float f2 = this.horizontalAdvances[i2];
            if (f2 != Float.MAX_VALUE) {
                if (!z2 && this.invertDevTx != null) {
                    Point2D.Float r0 = new Point2D.Float(f2, 0.0f);
                    this.desc.devTx.deltaTransform(r0, r0);
                    return r0.f12396x;
                }
                return f2;
            }
        } else if (this.segmentedCache && this.segHorizontalAdvances != null) {
            float[] fArr = this.segHorizontalAdvances[i2 >> 5];
            if (fArr != null) {
                float f3 = fArr[i2 % 32];
                if (f3 != Float.MAX_VALUE) {
                    if (!z2 && this.invertDevTx != null) {
                        Point2D.Float r02 = new Point2D.Float(f3, 0.0f);
                        this.desc.devTx.deltaTransform(r02, r02);
                        return r02.f12396x;
                    }
                    return f3;
                }
            }
        }
        if (!z2 && this.invertDevTx != null) {
            Point2D.Float r03 = new Point2D.Float();
            this.fileFont.getGlyphMetrics(this.pScalerContext, i2, r03);
            return r03.f12396x;
        }
        if (this.invertDevTx != null || !z2) {
            glyphAdvance = getGlyphMetrics(i2, z2).f12396x;
        } else {
            if (this.getImageWithAdvance) {
                cachedGlyphPtr = getGlyphImagePtr(i2);
            } else {
                cachedGlyphPtr = getCachedGlyphPtr(i2);
            }
            if (cachedGlyphPtr != 0) {
                glyphAdvance = StrikeCache.unsafe.getFloat(cachedGlyphPtr + StrikeCache.xAdvanceOffset);
            } else {
                glyphAdvance = this.fileFont.getGlyphAdvance(this.pScalerContext, i2);
            }
        }
        if (this.horizontalAdvances != null) {
            this.horizontalAdvances[i2] = glyphAdvance;
        } else if (this.segmentedCache && this.segHorizontalAdvances != null) {
            int i3 = i2 >> 5;
            int i4 = i2 % 32;
            if (this.segHorizontalAdvances[i3] == null) {
                this.segHorizontalAdvances[i3] = new float[32];
                for (int i5 = 0; i5 < 32; i5++) {
                    this.segHorizontalAdvances[i3][i5] = Float.MAX_VALUE;
                }
            }
            this.segHorizontalAdvances[i3][i4] = glyphAdvance;
        }
        return glyphAdvance;
    }

    @Override // sun.font.PhysicalStrike, sun.font.FontStrike
    float getCodePointAdvance(int i2) {
        return getGlyphAdvance(this.mapper.charToGlyph(i2));
    }

    @Override // sun.font.FontStrike
    void getGlyphImageBounds(int i2, Point2D.Float r9, Rectangle rectangle) {
        long glyphImagePtr = getGlyphImagePtr(i2);
        if (glyphImagePtr == 0) {
            rectangle.f12372x = (int) Math.floor(r9.f12396x);
            rectangle.f12373y = (int) Math.floor(r9.f12397y);
            rectangle.height = 0;
            rectangle.width = 0;
            return;
        }
        float f2 = StrikeCache.unsafe.getFloat(glyphImagePtr + StrikeCache.topLeftXOffset);
        float f3 = StrikeCache.unsafe.getFloat(glyphImagePtr + StrikeCache.topLeftYOffset);
        rectangle.f12372x = (int) Math.floor(r9.f12396x + f2);
        rectangle.f12373y = (int) Math.floor(r9.f12397y + f3);
        rectangle.width = StrikeCache.unsafe.getShort(glyphImagePtr + StrikeCache.widthOffset) & 65535;
        rectangle.height = StrikeCache.unsafe.getShort(glyphImagePtr + StrikeCache.heightOffset) & 65535;
        if ((this.desc.aaHint == 4 || this.desc.aaHint == 5) && f2 <= -2.0f && getGlyphImageMinX(glyphImagePtr, rectangle.f12372x) > rectangle.f12372x) {
            rectangle.f12372x++;
            rectangle.width--;
        }
    }

    private int getGlyphImageMinX(long j2, int i2) {
        int i3 = StrikeCache.unsafe.getChar(j2 + StrikeCache.widthOffset);
        int i4 = StrikeCache.unsafe.getChar(j2 + StrikeCache.heightOffset);
        if (StrikeCache.unsafe.getChar(j2 + StrikeCache.rowBytesOffset) == i3) {
            return i2;
        }
        long address = StrikeCache.unsafe.getAddress(j2 + StrikeCache.pixelDataOffset);
        if (address == 0) {
            return i2;
        }
        for (int i5 = 0; i5 < i4; i5++) {
            for (int i6 = 0; i6 < 3; i6++) {
                if (StrikeCache.unsafe.getByte(address + (i5 * r0) + i6) != 0) {
                    return i2;
                }
            }
        }
        return i2 + 1;
    }

    @Override // sun.font.PhysicalStrike, sun.font.FontStrike
    StrikeMetrics getFontMetrics() {
        if (this.strikeMetrics == null) {
            this.strikeMetrics = this.fileFont.getFontMetrics(this.pScalerContext);
            if (this.invertDevTx != null) {
                this.strikeMetrics.convertToUserSpace(this.invertDevTx);
            }
        }
        return this.strikeMetrics;
    }

    @Override // sun.font.FontStrike
    Point2D.Float getGlyphMetrics(int i2) {
        return getGlyphMetrics(i2, true);
    }

    private Point2D.Float getGlyphMetrics(int i2, boolean z2) {
        long cachedGlyphPtr;
        Point2D.Float r10 = new Point2D.Float();
        if (i2 >= 65534) {
            return r10;
        }
        if (this.getImageWithAdvance && z2) {
            cachedGlyphPtr = getGlyphImagePtr(i2);
        } else {
            cachedGlyphPtr = getCachedGlyphPtr(i2);
        }
        if (cachedGlyphPtr != 0) {
            r10 = new Point2D.Float();
            r10.f12396x = StrikeCache.unsafe.getFloat(cachedGlyphPtr + StrikeCache.xAdvanceOffset);
            r10.f12397y = StrikeCache.unsafe.getFloat(cachedGlyphPtr + StrikeCache.yAdvanceOffset);
            if (this.invertDevTx != null) {
                this.invertDevTx.deltaTransform(r10, r10);
            }
        } else {
            Integer numValueOf = Integer.valueOf(i2);
            Point2D.Float r14 = null;
            ConcurrentHashMap<Integer, Point2D.Float> concurrentHashMap = null;
            if (this.glyphMetricsMapRef != null) {
                concurrentHashMap = this.glyphMetricsMapRef.get();
            }
            if (concurrentHashMap != null) {
                r14 = concurrentHashMap.get(numValueOf);
                if (r14 != null) {
                    r10.f12396x = r14.f12396x;
                    r10.f12397y = r14.f12397y;
                    return r10;
                }
            }
            if (r14 == null) {
                this.fileFont.getGlyphMetrics(this.pScalerContext, i2, r10);
                if (this.invertDevTx != null) {
                    this.invertDevTx.deltaTransform(r10, r10);
                }
                Point2D.Float r0 = new Point2D.Float(r10.f12396x, r10.f12397y);
                if (concurrentHashMap == null) {
                    concurrentHashMap = new ConcurrentHashMap<>();
                    this.glyphMetricsMapRef = new SoftReference<>(concurrentHashMap);
                }
                concurrentHashMap.put(numValueOf, r0);
            }
        }
        return r10;
    }

    @Override // sun.font.PhysicalStrike, sun.font.FontStrike
    Point2D.Float getCharMetrics(char c2) {
        return getGlyphMetrics(this.mapper.charToGlyph(c2));
    }

    @Override // sun.font.FontStrike
    Rectangle2D.Float getGlyphOutlineBounds(int i2) {
        if (this.boundsMap == null) {
            this.boundsMap = new ConcurrentHashMap<>();
        }
        Integer numValueOf = Integer.valueOf(i2);
        Rectangle2D.Float glyphOutlineBounds = this.boundsMap.get(numValueOf);
        if (glyphOutlineBounds == null) {
            glyphOutlineBounds = this.fileFont.getGlyphOutlineBounds(this.pScalerContext, i2);
            this.boundsMap.put(numValueOf, glyphOutlineBounds);
        }
        return glyphOutlineBounds;
    }

    public Rectangle2D getOutlineBounds(int i2) {
        return this.fileFont.getGlyphOutlineBounds(this.pScalerContext, i2);
    }

    @Override // sun.font.FontStrike
    GeneralPath getGlyphOutline(int i2, float f2, float f3) {
        GeneralPath glyphOutline = null;
        ConcurrentHashMap<Integer, GeneralPath> concurrentHashMap = null;
        if (this.outlineMapRef != null) {
            concurrentHashMap = this.outlineMapRef.get();
            if (concurrentHashMap != null) {
                glyphOutline = concurrentHashMap.get(Integer.valueOf(i2));
            }
        }
        if (glyphOutline == null) {
            glyphOutline = this.fileFont.getGlyphOutline(this.pScalerContext, i2, 0.0f, 0.0f);
            if (concurrentHashMap == null) {
                concurrentHashMap = new ConcurrentHashMap<>();
                this.outlineMapRef = new WeakReference<>(concurrentHashMap);
            }
            concurrentHashMap.put(Integer.valueOf(i2), glyphOutline);
        }
        GeneralPath generalPath = (GeneralPath) glyphOutline.clone();
        if (f2 != 0.0f || f3 != 0.0f) {
            generalPath.transform(AffineTransform.getTranslateInstance(f2, f3));
        }
        return generalPath;
    }

    @Override // sun.font.FontStrike
    GeneralPath getGlyphVectorOutline(int[] iArr, float f2, float f3) {
        return this.fileFont.getGlyphVectorOutline(this.pScalerContext, iArr, iArr.length, f2, f3);
    }

    @Override // sun.font.PhysicalStrike
    protected void adjustPoint(Point2D.Float r5) {
        if (this.invertDevTx != null) {
            this.invertDevTx.deltaTransform(r5, r5);
        }
    }
}
