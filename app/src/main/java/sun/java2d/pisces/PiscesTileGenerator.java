package sun.java2d.pisces;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import sun.java2d.pipe.AATileGenerator;

/* loaded from: rt.jar:sun/java2d/pisces/PiscesTileGenerator.class */
final class PiscesTileGenerator implements AATileGenerator {
    public static final int TILE_SIZE = 32;
    private static final Map<Integer, byte[]> alphaMapsCache;
    PiscesCache cache;

    /* renamed from: x, reason: collision with root package name */
    int f13573x;

    /* renamed from: y, reason: collision with root package name */
    int f13574y;
    final int maxalpha;
    private final int maxTileAlphaSum;
    byte[] alphaMap;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !PiscesTileGenerator.class.desiredAssertionStatus();
        alphaMapsCache = new ConcurrentHashMap();
    }

    public PiscesTileGenerator(Renderer renderer, int i2) {
        this.cache = renderer.getCache();
        this.f13573x = this.cache.bboxX0;
        this.f13574y = this.cache.bboxY0;
        this.alphaMap = getAlphaMap(i2);
        this.maxalpha = i2;
        this.maxTileAlphaSum = 1024 * i2;
    }

    private static byte[] buildAlphaMap(int i2) {
        byte[] bArr = new byte[i2 + 1];
        int i3 = i2 >> 2;
        for (int i4 = 0; i4 <= i2; i4++) {
            bArr[i4] = (byte) (((i4 * 255) + i3) / i2);
        }
        return bArr;
    }

    public static byte[] getAlphaMap(int i2) {
        if (!alphaMapsCache.containsKey(Integer.valueOf(i2))) {
            alphaMapsCache.put(Integer.valueOf(i2), buildAlphaMap(i2));
        }
        return alphaMapsCache.get(Integer.valueOf(i2));
    }

    public void getBbox(int[] iArr) {
        iArr[0] = this.cache.bboxX0;
        iArr[1] = this.cache.bboxY0;
        iArr[2] = this.cache.bboxX1;
        iArr[3] = this.cache.bboxY1;
    }

    @Override // sun.java2d.pipe.AATileGenerator
    public int getTileWidth() {
        return 32;
    }

    @Override // sun.java2d.pipe.AATileGenerator
    public int getTileHeight() {
        return 32;
    }

    @Override // sun.java2d.pipe.AATileGenerator
    public int getTypicalAlpha() {
        int iAlphaSumInTile = this.cache.alphaSumInTile(this.f13573x, this.f13574y);
        if (iAlphaSumInTile == 0) {
            return 0;
        }
        return iAlphaSumInTile == this.maxTileAlphaSum ? 255 : 128;
    }

    @Override // sun.java2d.pipe.AATileGenerator
    public void nextTile() {
        int i2 = this.f13573x + 32;
        this.f13573x = i2;
        if (i2 >= this.cache.bboxX1) {
            this.f13573x = this.cache.bboxX0;
            this.f13574y += 32;
        }
    }

    @Override // sun.java2d.pipe.AATileGenerator
    public void getAlpha(byte[] bArr, int i2, int i3) {
        int i4 = this.f13573x;
        int i5 = i4 + 32;
        int i6 = this.f13574y;
        int i7 = i6 + 32;
        if (i5 > this.cache.bboxX1) {
            i5 = this.cache.bboxX1;
        }
        if (i7 > this.cache.bboxY1) {
            i7 = this.cache.bboxY1;
        }
        int i8 = i6 - this.cache.bboxY0;
        int i9 = i7 - this.cache.bboxY0;
        int i10 = i2;
        for (int i11 = i8; i11 < i9; i11++) {
            int[] iArr = this.cache.rowAARLE[i11];
            if (!$assertionsDisabled && iArr == null) {
                throw new AssertionError();
            }
            int iMinTouched = this.cache.minTouched(i11);
            if (iMinTouched > i5) {
                iMinTouched = i5;
            }
            for (int i12 = i4; i12 < iMinTouched; i12++) {
                int i13 = i10;
                i10++;
                bArr[i13] = 0;
            }
            for (int i14 = 2; iMinTouched < i5 && i14 < iArr[1]; i14 += 2) {
                int i15 = 0;
                if (!$assertionsDisabled && iArr[1] <= 2) {
                    throw new AssertionError();
                }
                try {
                    byte b2 = this.alphaMap[iArr[i14]];
                    i15 = iArr[i14 + 1];
                    if (!$assertionsDisabled && i15 <= 0) {
                        throw new AssertionError();
                    }
                    int i16 = iMinTouched;
                    iMinTouched += i15;
                    int i17 = iMinTouched;
                    if (i16 < i4) {
                        i16 = i4;
                    }
                    if (i17 > i5) {
                        i17 = i5;
                    }
                    int i18 = i17 - i16;
                    while (true) {
                        i18--;
                        if (i18 >= 0) {
                            try {
                                int i19 = i10;
                                i10++;
                                bArr[i19] = b2;
                            } catch (RuntimeException e2) {
                                System.out.println("maxalpha = " + this.maxalpha);
                                System.out.println("tile[" + i4 + ", " + i8 + " => " + i5 + ", " + i9 + "]");
                                System.out.println("cx = " + iMinTouched + ", cy = " + i11);
                                System.out.println("idx = " + i10 + ", pos = " + i14);
                                System.out.println("rx0 = " + i16 + ", rx1 = " + i17);
                                System.out.println("len = " + i18);
                                System.out.print(this.cache.toString());
                                e2.printStackTrace();
                                throw e2;
                            }
                        }
                    }
                } catch (RuntimeException e3) {
                    System.out.println("maxalpha = " + this.maxalpha);
                    System.out.println("tile[" + i4 + ", " + i8 + " => " + i5 + ", " + i9 + "]");
                    System.out.println("cx = " + iMinTouched + ", cy = " + i11);
                    System.out.println("idx = " + i10 + ", pos = " + i14);
                    System.out.println("len = " + i15);
                    System.out.print(this.cache.toString());
                    e3.printStackTrace();
                    throw e3;
                }
            }
            if (iMinTouched < i4) {
                iMinTouched = i4;
            }
            while (iMinTouched < i5) {
                int i20 = i10;
                i10++;
                bArr[i20] = 0;
                iMinTouched++;
            }
            i10 += i3 - (i5 - i4);
        }
        nextTile();
    }

    static String hex(int i2, int i3) {
        String hexString = Integer.toHexString(i2);
        while (true) {
            String str = hexString;
            if (str.length() < i3) {
                hexString = "0" + str;
            } else {
                return str.substring(0, i3);
            }
        }
    }

    @Override // sun.java2d.pipe.AATileGenerator
    public void dispose() {
    }
}
