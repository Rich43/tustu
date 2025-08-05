package sun.font;

import java.awt.font.GlyphVector;
import sun.java2d.loops.FontInfo;

/* loaded from: rt.jar:sun/font/GlyphList.class */
public final class GlyphList {
    private static final int MINGRAYLENGTH = 1024;
    private static final int MAXGRAYLENGTH = 8192;
    private static final int DEFAULT_LENGTH = 32;
    int glyphindex;
    int[] metrics;
    byte[] graybits;
    Object strikelist;
    int len = 0;
    int maxLen = 0;
    int maxPosLen = 0;
    int[] glyphData;
    char[] chData;
    long[] images;
    float[] positions;

    /* renamed from: x, reason: collision with root package name */
    float f13552x;

    /* renamed from: y, reason: collision with root package name */
    float f13553y;
    float gposx;
    float gposy;
    boolean usePositions;
    boolean lcdRGBOrder;
    boolean lcdSubPixPos;
    private static GlyphList reusableGL = new GlyphList();
    private static boolean inUse;

    void ensureCapacity(int i2) {
        if (i2 < 0) {
            i2 = 0;
        }
        if (this.usePositions && i2 > this.maxPosLen) {
            this.positions = new float[(i2 * 2) + 2];
            this.maxPosLen = i2;
        }
        if (this.maxLen == 0 || i2 > this.maxLen) {
            this.glyphData = new int[i2];
            this.chData = new char[i2];
            this.images = new long[i2];
            this.maxLen = i2;
        }
    }

    private GlyphList() {
    }

    public static GlyphList getInstance() {
        if (inUse) {
            return new GlyphList();
        }
        synchronized (GlyphList.class) {
            if (inUse) {
                return new GlyphList();
            }
            inUse = true;
            return reusableGL;
        }
    }

    public boolean setFromString(FontInfo fontInfo, String str, float f2, float f3) {
        this.f13552x = f2;
        this.f13553y = f3;
        this.strikelist = fontInfo.fontStrike;
        this.lcdRGBOrder = fontInfo.lcdRGBOrder;
        this.lcdSubPixPos = fontInfo.lcdSubPixPos;
        this.len = str.length();
        ensureCapacity(this.len);
        str.getChars(0, this.len, this.chData, 0);
        return mapChars(fontInfo, this.len);
    }

    public boolean setFromChars(FontInfo fontInfo, char[] cArr, int i2, int i3, float f2, float f3) {
        this.f13552x = f2;
        this.f13553y = f3;
        this.strikelist = fontInfo.fontStrike;
        this.lcdRGBOrder = fontInfo.lcdRGBOrder;
        this.lcdSubPixPos = fontInfo.lcdSubPixPos;
        this.len = i3;
        if (i3 < 0) {
            this.len = 0;
        } else {
            this.len = i3;
        }
        ensureCapacity(this.len);
        System.arraycopy(cArr, i2, this.chData, 0, this.len);
        return mapChars(fontInfo, this.len);
    }

    private final boolean mapChars(FontInfo fontInfo, int i2) {
        if (fontInfo.font2D.getMapper().charsToGlyphsNS(i2, this.chData, this.glyphData)) {
            return false;
        }
        fontInfo.fontStrike.getGlyphImagePtrs(this.glyphData, this.images, i2);
        this.glyphindex = -1;
        return true;
    }

    public void setFromGlyphVector(FontInfo fontInfo, GlyphVector glyphVector, float f2, float f3) {
        this.f13552x = f2;
        this.f13553y = f3;
        this.lcdRGBOrder = fontInfo.lcdRGBOrder;
        this.lcdSubPixPos = fontInfo.lcdSubPixPos;
        StandardGlyphVector standardGV = StandardGlyphVector.getStandardGV(glyphVector, fontInfo);
        this.usePositions = standardGV.needsPositions(fontInfo.devTx);
        this.len = standardGV.getNumGlyphs();
        ensureCapacity(this.len);
        this.strikelist = standardGV.setupGlyphImages(this.images, this.usePositions ? this.positions : null, fontInfo.devTx);
        this.glyphindex = -1;
    }

    public int[] getBounds() {
        if (this.glyphindex >= 0) {
            throw new InternalError("calling getBounds after setGlyphIndex");
        }
        if (this.metrics == null) {
            this.metrics = new int[5];
        }
        this.gposx = this.f13552x + 0.5f;
        this.gposy = this.f13553y + 0.5f;
        fillBounds(this.metrics);
        return this.metrics;
    }

    public void setGlyphIndex(int i2) {
        this.glyphindex = i2;
        if (this.images[i2] == 0) {
            this.metrics[0] = (int) this.gposx;
            this.metrics[1] = (int) this.gposy;
            this.metrics[2] = 0;
            this.metrics[3] = 0;
            this.metrics[4] = 0;
            return;
        }
        float f2 = StrikeCache.unsafe.getFloat(this.images[i2] + StrikeCache.topLeftXOffset);
        float f3 = StrikeCache.unsafe.getFloat(this.images[i2] + StrikeCache.topLeftYOffset);
        if (this.usePositions) {
            this.metrics[0] = (int) Math.floor(this.positions[i2 << 1] + this.gposx + f2);
            this.metrics[1] = (int) Math.floor(this.positions[(i2 << 1) + 1] + this.gposy + f3);
        } else {
            this.metrics[0] = (int) Math.floor(this.gposx + f2);
            this.metrics[1] = (int) Math.floor(this.gposy + f3);
            this.gposx += StrikeCache.unsafe.getFloat(this.images[i2] + StrikeCache.xAdvanceOffset);
            this.gposy += StrikeCache.unsafe.getFloat(this.images[i2] + StrikeCache.yAdvanceOffset);
        }
        this.metrics[2] = StrikeCache.unsafe.getChar(this.images[i2] + StrikeCache.widthOffset);
        this.metrics[3] = StrikeCache.unsafe.getChar(this.images[i2] + StrikeCache.heightOffset);
        this.metrics[4] = StrikeCache.unsafe.getChar(this.images[i2] + StrikeCache.rowBytesOffset);
    }

    public int[] getMetrics() {
        return this.metrics;
    }

    public byte[] getGrayBits() {
        int i2 = this.metrics[4] * this.metrics[3];
        if (this.graybits == null) {
            this.graybits = new byte[Math.max(i2, 1024)];
        } else if (i2 > this.graybits.length) {
            this.graybits = new byte[i2];
        }
        if (this.images[this.glyphindex] == 0) {
            return this.graybits;
        }
        long address = StrikeCache.unsafe.getAddress(this.images[this.glyphindex] + StrikeCache.pixelDataOffset);
        if (address == 0) {
            return this.graybits;
        }
        for (int i3 = 0; i3 < i2; i3++) {
            this.graybits[i3] = StrikeCache.unsafe.getByte(address + i3);
        }
        return this.graybits;
    }

    public long[] getImages() {
        return this.images;
    }

    public boolean usePositions() {
        return this.usePositions;
    }

    public float[] getPositions() {
        return this.positions;
    }

    public float getX() {
        return this.f13552x;
    }

    public float getY() {
        return this.f13553y;
    }

    public Object getStrike() {
        return this.strikelist;
    }

    public boolean isSubPixPos() {
        return this.lcdSubPixPos;
    }

    public boolean isRGBOrder() {
        return this.lcdRGBOrder;
    }

    public void dispose() {
        if (this == reusableGL) {
            if (this.graybits != null && this.graybits.length > 8192) {
                this.graybits = null;
            }
            this.usePositions = false;
            this.strikelist = null;
            inUse = false;
        }
    }

    public int getNumGlyphs() {
        return this.len;
    }

    private void fillBounds(int[] iArr) {
        float f2;
        float f3;
        int i2 = StrikeCache.topLeftXOffset;
        int i3 = StrikeCache.topLeftYOffset;
        int i4 = StrikeCache.widthOffset;
        int i5 = StrikeCache.heightOffset;
        int i6 = StrikeCache.xAdvanceOffset;
        int i7 = StrikeCache.yAdvanceOffset;
        if (this.len == 0) {
            iArr[3] = 0;
            iArr[2] = 0;
            iArr[1] = 0;
            iArr[0] = 0;
            return;
        }
        float f4 = Float.POSITIVE_INFINITY;
        float f5 = Float.POSITIVE_INFINITY;
        float f6 = Float.NEGATIVE_INFINITY;
        float f7 = Float.NEGATIVE_INFINITY;
        int i8 = 0;
        float f8 = this.f13552x + 0.5f;
        float f9 = this.f13553y + 0.5f;
        for (int i9 = 0; i9 < this.len; i9++) {
            if (this.images[i9] != 0) {
                float f10 = StrikeCache.unsafe.getFloat(this.images[i9] + i2);
                float f11 = StrikeCache.unsafe.getFloat(this.images[i9] + i3);
                char c2 = StrikeCache.unsafe.getChar(this.images[i9] + i4);
                char c3 = StrikeCache.unsafe.getChar(this.images[i9] + i5);
                if (this.usePositions) {
                    int i10 = i8;
                    int i11 = i8 + 1;
                    f2 = this.positions[i10] + f10 + f8;
                    i8 = i11 + 1;
                    f3 = this.positions[i11] + f11 + f9;
                } else {
                    f2 = f8 + f10;
                    f3 = f9 + f11;
                    f8 += StrikeCache.unsafe.getFloat(this.images[i9] + i6);
                    f9 += StrikeCache.unsafe.getFloat(this.images[i9] + i7);
                }
                float f12 = f2 + c2;
                float f13 = f3 + c3;
                if (f5 > f2) {
                    f5 = f2;
                }
                if (f4 > f3) {
                    f4 = f3;
                }
                if (f7 < f12) {
                    f7 = f12;
                }
                if (f6 < f13) {
                    f6 = f13;
                }
            }
        }
        iArr[0] = (int) Math.floor(f5);
        iArr[1] = (int) Math.floor(f4);
        iArr[2] = (int) Math.floor(f7);
        iArr[3] = (int) Math.floor(f6);
    }
}
