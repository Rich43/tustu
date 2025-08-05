package sun.java2d.marlin;

import sun.java2d.pipe.AATileGenerator;
import sun.misc.Unsafe;

/* loaded from: rt.jar:sun/java2d/marlin/MarlinTileGenerator.class */
final class MarlinTileGenerator implements AATileGenerator, MarlinConst {
    private static final int MAX_TILE_ALPHA_SUM = (TILE_SIZE * TILE_SIZE) * MAX_AA_ALPHA;
    private final Renderer rdr;
    private final MarlinCache cache;

    /* renamed from: x, reason: collision with root package name */
    private int f13566x;

    /* renamed from: y, reason: collision with root package name */
    private int f13567y;

    MarlinTileGenerator(Renderer renderer) {
        this.rdr = renderer;
        this.cache = renderer.cache;
    }

    MarlinTileGenerator init() {
        this.f13566x = this.cache.bboxX0;
        this.f13567y = this.cache.bboxY0;
        return this;
    }

    @Override // sun.java2d.pipe.AATileGenerator
    public void dispose() {
        this.cache.dispose();
        this.rdr.dispose();
        MarlinRenderingEngine.returnRendererContext(this.rdr.rdrCtx);
    }

    void getBbox(int[] iArr) {
        iArr[0] = this.cache.bboxX0;
        iArr[1] = this.cache.bboxY0;
        iArr[2] = this.cache.bboxX1;
        iArr[3] = this.cache.bboxY1;
    }

    @Override // sun.java2d.pipe.AATileGenerator
    public int getTileWidth() {
        return TILE_SIZE;
    }

    @Override // sun.java2d.pipe.AATileGenerator
    public int getTileHeight() {
        return TILE_SIZE;
    }

    @Override // sun.java2d.pipe.AATileGenerator
    public int getTypicalAlpha() {
        int iAlphaSumInTile = this.cache.alphaSumInTile(this.f13566x);
        int i2 = iAlphaSumInTile == 0 ? 0 : iAlphaSumInTile == MAX_TILE_ALPHA_SUM ? 255 : 128;
        if (doStats) {
            RendererContext.stats.hist_tile_generator_alpha.add(i2);
        }
        return i2;
    }

    @Override // sun.java2d.pipe.AATileGenerator
    public void nextTile() {
        int i2 = this.f13566x + TILE_SIZE;
        this.f13566x = i2;
        if (i2 >= this.cache.bboxX1) {
            this.f13566x = this.cache.bboxX0;
            this.f13567y += TILE_SIZE;
            if (this.f13567y < this.cache.bboxY1) {
                this.rdr.endRendering(this.f13567y);
            }
        }
    }

    @Override // sun.java2d.pipe.AATileGenerator
    public void getAlpha(byte[] bArr, int i2, int i3) {
        if (this.cache.useRLE) {
            getAlphaRLE(bArr, i2, i3);
        } else {
            getAlphaNoRLE(bArr, i2, i3);
        }
    }

    private void getAlphaNoRLE(byte[] bArr, int i2, int i3) {
        int i4;
        MarlinCache marlinCache = this.cache;
        long[] jArr = marlinCache.rowAAChunkIndex;
        int[] iArr = marlinCache.rowAAx0;
        int[] iArr2 = marlinCache.rowAAx1;
        int i5 = this.f13566x;
        int iMin = FloatMath.min(i5 + TILE_SIZE, marlinCache.bboxX1);
        int iMin2 = FloatMath.min(this.f13567y + TILE_SIZE, marlinCache.bboxY1) - this.f13567y;
        if (doLogBounds) {
            MarlinUtils.logInfo("getAlpha = [" + i5 + " ... " + iMin + "[ [0 ... " + iMin2 + "[");
        }
        Unsafe unsafe = OffHeapArray.unsafe;
        long j2 = marlinCache.rowAAChunk.address;
        int i6 = i3 - (iMin - i5);
        int i7 = i2;
        for (int i8 = 0; i8 < iMin2; i8++) {
            int i9 = i5;
            int i10 = iArr2[i8];
            if (i10 > i5 && (i4 = iArr[i8]) < iMin) {
                i9 = i4;
                if (i9 <= i5) {
                    i9 = i5;
                } else {
                    for (int i11 = i5; i11 < i9; i11++) {
                        int i12 = i7;
                        i7++;
                        bArr[i12] = 0;
                    }
                }
                long j3 = j2 + jArr[i8] + (i9 - i4);
                int i13 = i10 <= iMin ? i10 : iMin;
                while (i9 < i13) {
                    int i14 = i7;
                    i7++;
                    bArr[i14] = unsafe.getByte(j3);
                    j3++;
                    i9++;
                }
            }
            while (i9 < iMin) {
                int i15 = i7;
                i7++;
                bArr[i15] = 0;
                i9++;
            }
            if (doTrace) {
                for (int i16 = i7 - (iMin - i5); i16 < i7; i16++) {
                    System.out.print(hex(bArr[i16], 2));
                }
                System.out.println();
            }
            i7 += i6;
        }
        nextTile();
    }

    private void getAlphaRLE(byte[] bArr, int i2, int i3) {
        int i4;
        MarlinCache marlinCache = this.cache;
        long[] jArr = marlinCache.rowAAChunkIndex;
        int[] iArr = marlinCache.rowAAx0;
        int[] iArr2 = marlinCache.rowAAx1;
        int[] iArr3 = marlinCache.rowAAEnc;
        long[] jArr2 = marlinCache.rowAALen;
        long[] jArr3 = marlinCache.rowAAPos;
        int i5 = this.f13566x;
        int iMin = FloatMath.min(i5 + TILE_SIZE, marlinCache.bboxX1);
        int iMin2 = FloatMath.min(this.f13567y + TILE_SIZE, marlinCache.bboxY1) - this.f13567y;
        if (doLogBounds) {
            MarlinUtils.logInfo("getAlpha = [" + i5 + " ... " + iMin + "[ [0 ... " + iMin2 + "[");
        }
        Unsafe unsafe = OffHeapArray.unsafe;
        long j2 = marlinCache.rowAAChunk.address;
        int i6 = i3 - (iMin - i5);
        int i7 = i2;
        for (int i8 = 0; i8 < iMin2; i8++) {
            int i9 = i5;
            if (iArr3[i8] == 0) {
                int i10 = iArr2[i8];
                if (i10 > i5 && (i4 = iArr[i8]) < iMin) {
                    i9 = i4;
                    if (i9 <= i5) {
                        i9 = i5;
                    } else {
                        for (int i11 = i5; i11 < i9; i11++) {
                            int i12 = i7;
                            i7++;
                            bArr[i12] = 0;
                        }
                    }
                    long j3 = j2 + jArr[i8] + (i9 - i4);
                    int i13 = i10 <= iMin ? i10 : iMin;
                    while (i9 < i13) {
                        int i14 = i7;
                        i7++;
                        bArr[i14] = unsafe.getByte(j3);
                        j3++;
                        i9++;
                    }
                }
            } else if (iArr2[i8] > i5) {
                i9 = iArr[i8];
                if (i9 > iMin) {
                    i9 = iMin;
                }
                for (int i15 = i5; i15 < i9; i15++) {
                    int i16 = i7;
                    i7++;
                    bArr[i16] = 0;
                }
                long j4 = j2 + jArr[i8];
                long j5 = j4 + jArr2[i8];
                long j6 = j4 + jArr3[i8];
                long j7 = 0;
                while (i9 < iMin && j6 < j5) {
                    j7 = j6;
                    int i17 = unsafe.getInt(j6);
                    int i18 = i17 >> 8;
                    j6 += 4;
                    int i19 = i9;
                    if (i19 < i5) {
                        i19 = i5;
                    }
                    i9 = i18;
                    int i20 = i18;
                    if (i20 > iMin) {
                        i20 = iMin;
                        i9 = iMin;
                    }
                    int i21 = i20 - i19;
                    if (i21 > 0) {
                        byte b2 = (byte) (i17 & 255);
                        do {
                            int i22 = i7;
                            i7++;
                            bArr[i22] = b2;
                            i21--;
                        } while (i21 > 0);
                    }
                }
                if (j7 != 0) {
                    iArr[i8] = i9;
                    jArr3[i8] = j7 - j4;
                }
            }
            while (i9 < iMin) {
                int i23 = i7;
                i7++;
                bArr[i23] = 0;
                i9++;
            }
            if (doTrace) {
                for (int i24 = i7 - (iMin - i5); i24 < i7; i24++) {
                    System.out.print(hex(bArr[i24], 2));
                }
                System.out.println();
            }
            i7 += i6;
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
}
