package sun.java2d.marlin;

import sun.misc.Unsafe;

/* loaded from: rt.jar:sun/java2d/marlin/MarlinCache.class */
public final class MarlinCache implements MarlinConst {
    static final int RLE_MAX_WIDTH = 8388608;
    static final byte[] ALPHA_MAP;
    static final OffHeapArray ALPHA_MAP_UNSAFE;
    int bboxX0;
    int bboxY0;
    int bboxX1;
    int bboxY1;
    final OffHeapArray rowAAChunk;
    long rowAAChunkPos;
    final RendererContext rdrCtx;
    static final boolean FORCE_RLE = MarlinProperties.isForceRLE();
    static final boolean FORCE_NO_RLE = MarlinProperties.isForceNoRLE();
    static final int RLE_MIN_WIDTH = Math.max(BLOCK_SIZE, MarlinProperties.getRLEMinWidth());
    static final long INITIAL_CHUNK_ARRAY = TILE_SIZE * INITIAL_PIXEL_DIM;
    final long[] rowAAChunkIndex = new long[TILE_SIZE];
    final int[] rowAAx0 = new int[TILE_SIZE];
    final int[] rowAAx1 = new int[TILE_SIZE];
    final int[] rowAAEnc = new int[TILE_SIZE];
    final long[] rowAALen = new long[TILE_SIZE];
    final long[] rowAAPos = new long[TILE_SIZE];
    final int[] touchedTile_initial = new int[256];
    boolean useRLE = false;
    int[] touchedTile = this.touchedTile_initial;
    int tileMin = Integer.MAX_VALUE;
    int tileMax = Integer.MIN_VALUE;

    static {
        byte[] bArrBuildAlphaMap = buildAlphaMap(MAX_AA_ALPHA);
        ALPHA_MAP_UNSAFE = new OffHeapArray(bArrBuildAlphaMap, bArrBuildAlphaMap.length);
        ALPHA_MAP = bArrBuildAlphaMap;
        Unsafe unsafe = OffHeapArray.unsafe;
        long j2 = ALPHA_MAP_UNSAFE.address;
        for (int i2 = 0; i2 < bArrBuildAlphaMap.length; i2++) {
            unsafe.putByte(j2 + i2, bArrBuildAlphaMap[i2]);
        }
    }

    MarlinCache(RendererContext rendererContext) {
        this.rdrCtx = rendererContext;
        this.rowAAChunk = new OffHeapArray(rendererContext, INITIAL_CHUNK_ARRAY);
    }

    void init(int i2, int i3, int i4, int i5, int i6) {
        this.bboxX0 = i2;
        this.bboxY0 = i3;
        this.bboxX1 = i4;
        this.bboxY1 = i5;
        int i7 = i4 - i2;
        if (FORCE_NO_RLE) {
            this.useRLE = false;
        } else if (FORCE_RLE) {
            this.useRLE = true;
        } else if (i7 <= RLE_MIN_WIDTH || i7 >= 8388608) {
            this.useRLE = false;
        } else {
            int i8 = ((i5 - i3) << SUBPIXEL_LG_POSITIONS_Y) << this.rdrCtx.stroking;
            this.useRLE = i6 <= (i8 << 1) || i7 * i8 > ((i6 - i8) << BLOCK_SIZE_LG);
            if (doTrace && !this.useRLE) {
                float f2 = i6 / i8;
                System.out.println("High complexity:  for bbox[width = " + i7 + " height = " + (i5 - i3) + "] edgeSumDeltaY = " + i6 + " heightSubPixel = " + i8 + " meanCrossings = " + f2 + " meanDist = " + (i7 / (f2 - 1.0f)) + " width =  " + (i7 * i8) + " <= criteria:  " + ((i6 - i8) << BLOCK_SIZE_LG));
            }
        }
        int i9 = (i7 + TILE_SIZE) >> TILE_SIZE_LG;
        if (i9 > 256) {
            if (doStats) {
                RendererContext.stats.stat_array_marlincache_touchedTile.add(i9);
            }
            this.touchedTile = this.rdrCtx.getIntArray(i9);
        }
    }

    void dispose() {
        resetTileLine(0);
        if (this.touchedTile != this.touchedTile_initial) {
            this.rdrCtx.putIntArray(this.touchedTile, 0, 0);
            this.touchedTile = this.touchedTile_initial;
        }
        if (this.rowAAChunk.length != INITIAL_CHUNK_ARRAY) {
            this.rowAAChunk.resize(INITIAL_CHUNK_ARRAY);
        }
    }

    void resetTileLine(int i2) {
        this.bboxY0 = i2;
        if (doStats) {
            RendererContext.stats.stat_cache_rowAAChunk.add(this.rowAAChunkPos);
        }
        this.rowAAChunkPos = 0L;
        if (this.tileMin != Integer.MAX_VALUE) {
            if (doStats) {
                RendererContext.stats.stat_cache_tiles.add(this.tileMax - this.tileMin);
            }
            if (this.tileMax == 1) {
                this.touchedTile[0] = 0;
            } else {
                IntArrayCache.fill(this.touchedTile, this.tileMin, this.tileMax, 0);
            }
            this.tileMin = Integer.MAX_VALUE;
            this.tileMax = Integer.MIN_VALUE;
        }
    }

    void clearAARow(int i2) {
        int i3 = i2 - this.bboxY0;
        this.rowAAx0[i3] = 0;
        this.rowAAx1[i3] = 0;
        this.rowAAEnc[i3] = 0;
    }

    void copyAARowNoRLE(int[] iArr, int i2, int i3, int i4) {
        int iMin = FloatMath.min(i4, this.bboxX1);
        if (doLogBounds) {
            MarlinUtils.logInfo("row = [" + i3 + " ... " + iMin + " (" + i4 + ") [ for y=" + i2);
        }
        int i5 = i2 - this.bboxY0;
        this.rowAAx0[i5] = i3;
        this.rowAAx1[i5] = iMin;
        this.rowAAEnc[i5] = 0;
        long j2 = this.rowAAChunkPos;
        this.rowAAChunkIndex[i5] = j2;
        long j3 = j2 + (((iMin - i3) + 3) & (-4));
        this.rowAAChunkPos = j3;
        OffHeapArray offHeapArray = this.rowAAChunk;
        if (offHeapArray.length < j3) {
            expandRowAAChunk(j3);
        }
        if (doStats) {
            RendererContext.stats.stat_cache_rowAA.add(iMin - i3);
        }
        int[] iArr2 = this.touchedTile;
        int i6 = TILE_SIZE_LG;
        int i7 = i3 - this.bboxX0;
        int i8 = iMin - this.bboxX0;
        Unsafe unsafe = OffHeapArray.unsafe;
        long j4 = ALPHA_MAP_UNSAFE.address;
        long j5 = offHeapArray.address + j2;
        int i9 = 0;
        for (int i10 = i7; i10 < i8; i10++) {
            i9 += iArr[i10];
            if (i9 == 0) {
                unsafe.putByte(j5, (byte) 0);
            } else {
                unsafe.putByte(j5, unsafe.getByte(j4 + i9));
                int i11 = i10 >> i6;
                iArr2[i11] = iArr2[i11] + i9;
            }
            j5++;
        }
        int i12 = i7 >> i6;
        if (i12 < this.tileMin) {
            this.tileMin = i12;
        }
        int i13 = ((i8 - 1) >> i6) + 1;
        if (i13 > this.tileMax) {
            this.tileMax = i13;
        }
        if (doLogBounds) {
            MarlinUtils.logInfo("clear = [" + i7 + " ... " + i8 + "[");
        }
        IntArrayCache.fill(iArr, i7, i4 - this.bboxX0, 0);
    }

    void copyAARowRLE_WithBlockFlags(int[] iArr, int[] iArr2, int i2, int i3, int i4) {
        int i5 = this.bboxX0;
        int i6 = i2 - this.bboxY0;
        int i7 = i3 - i5;
        int iMin = FloatMath.min(i4, this.bboxX1);
        int i8 = iMin - i5;
        if (doLogBounds) {
            MarlinUtils.logInfo("row = [" + i3 + " ... " + iMin + " (" + i4 + ") [ for y=" + i2);
        }
        long jStartRLERow = startRLERow(i6, i3, iMin);
        long j2 = jStartRLERow + ((i8 - i7) << 2);
        OffHeapArray offHeapArray = this.rowAAChunk;
        if (offHeapArray.length < j2) {
            expandRowAAChunk(j2);
        }
        Unsafe unsafe = OffHeapArray.unsafe;
        long j3 = ALPHA_MAP_UNSAFE.address;
        long j4 = offHeapArray.address + jStartRLERow;
        int[] iArr3 = this.touchedTile;
        int i9 = TILE_SIZE_LG;
        int i10 = BLOCK_SIZE_LG;
        int i11 = i7 >> i10;
        int i12 = (i8 >> i10) + 1;
        int i13 = 0;
        int i14 = i7;
        int i15 = Integer.MAX_VALUE;
        int i16 = 0;
        for (int i17 = i11; i17 <= i12; i17++) {
            if (iArr[i17] != 0) {
                iArr[i17] = 0;
                if (i15 == Integer.MAX_VALUE) {
                    i15 = i17;
                }
            } else if (i15 != Integer.MAX_VALUE) {
                int iMax = FloatMath.max(i15 << i10, i7);
                i15 = Integer.MAX_VALUE;
                int iMin2 = FloatMath.min((i17 << i10) + 1, i8);
                for (int i18 = iMax; i18 < iMin2; i18++) {
                    int i19 = iArr2[i18];
                    if (i19 != 0) {
                        iArr2[i18] = 0;
                        if (i18 != i14) {
                            int i20 = i18 - i14;
                            if (i13 == 0) {
                                unsafe.putInt(j4, (i5 + i18) << 8);
                            } else {
                                unsafe.putInt(j4, ((i5 + i18) << 8) | (unsafe.getByte(j3 + i13) & 255));
                                if (i20 == 1) {
                                    int i21 = i14 >> i9;
                                    iArr3[i21] = iArr3[i21] + i13;
                                } else {
                                    touchTile(i14, i13, i18, i20, iArr3);
                                }
                            }
                            j4 += 4;
                            if (doStats) {
                                RendererContext.stats.hist_tile_generator_encoding_runLen.add(i20);
                            }
                            i14 = i18;
                        }
                        i13 += i19;
                    }
                }
            } else if (doStats) {
                i16++;
            }
        }
        int i22 = i8 - i14;
        if (i13 == 0) {
            unsafe.putInt(j4, (i5 + i8) << 8);
        } else {
            unsafe.putInt(j4, ((i5 + i8) << 8) | (unsafe.getByte(j3 + i13) & 255));
            if (i22 == 1) {
                int i23 = i14 >> i9;
                iArr3[i23] = iArr3[i23] + i13;
            } else {
                touchTile(i14, i13, i8, i22, iArr3);
            }
        }
        long j5 = j4 + 4;
        if (doStats) {
            RendererContext.stats.hist_tile_generator_encoding_runLen.add(i22);
        }
        long j6 = j5 - offHeapArray.address;
        this.rowAALen[i6] = j6 - jStartRLERow;
        this.rowAAChunkPos = j6;
        if (doStats) {
            RendererContext.stats.stat_cache_rowAA.add(this.rowAALen[i6]);
            RendererContext.stats.hist_tile_generator_encoding_ratio.add((100 * i16) / (i12 - i11));
        }
        int i24 = i7 >> i9;
        if (i24 < this.tileMin) {
            this.tileMin = i24;
        }
        int i25 = ((i8 - 1) >> i9) + 1;
        if (i25 > this.tileMax) {
            this.tileMax = i25;
        }
        if (i4 > this.bboxX1) {
            iArr2[i8] = 0;
            iArr2[i8 + 1] = 0;
        }
        if (doChecks) {
            IntArrayCache.check(iArr, i11, i12, 0);
            IntArrayCache.check(iArr2, i7, i4 - this.bboxX0, 0);
        }
    }

    long startRLERow(int i2, int i3, int i4) {
        this.rowAAx0[i2] = i3;
        this.rowAAx1[i2] = i4;
        this.rowAAEnc[i2] = 1;
        this.rowAAPos[i2] = 0;
        long[] jArr = this.rowAAChunkIndex;
        long j2 = this.rowAAChunkPos;
        jArr[i2] = j2;
        return j2;
    }

    private void expandRowAAChunk(long j2) {
        if (doStats) {
            RendererContext.stats.stat_array_marlincache_rowAAChunk.add(j2);
        }
        this.rowAAChunk.resize(ArrayCache.getNewLargeSize(this.rowAAChunk.length, j2));
    }

    private void touchTile(int i2, int i3, int i4, int i5, int[] iArr) {
        int i6 = TILE_SIZE_LG;
        int i7 = i2 >> i6;
        if (i7 == (i4 >> i6)) {
            iArr[i7] = iArr[i7] + (i3 * i5);
            return;
        }
        int i8 = (i4 - 1) >> i6;
        if (i7 <= i8) {
            int i9 = (i7 + 1) << i6;
            i7++;
            iArr[i7] = iArr[i7] + (i3 * (i9 - i2));
        }
        if (i7 < i8) {
            int i10 = i3 << i6;
            while (i7 < i8) {
                int i11 = i7;
                iArr[i11] = iArr[i11] + i10;
                i7++;
            }
        }
        if (i7 == i8) {
            int i12 = i7 << i6;
            int i13 = (i7 + 1) << i6;
            int i14 = i7;
            iArr[i14] = iArr[i14] + (i3 * ((i13 <= i4 ? i13 : i4) - i12));
        }
    }

    int alphaSumInTile(int i2) {
        return this.touchedTile[(i2 - this.bboxX0) >> TILE_SIZE_LG];
    }

    public String toString() {
        return "bbox = [" + this.bboxX0 + ", " + this.bboxY0 + " => " + this.bboxX1 + ", " + this.bboxY1 + "]\n";
    }

    private static byte[] buildAlphaMap(int i2) {
        byte[] bArr = new byte[i2 << 1];
        int i3 = i2 >> 2;
        for (int i4 = 0; i4 <= i2; i4++) {
            bArr[i4] = (byte) (((i4 * 255) + i3) / i2);
        }
        return bArr;
    }
}
