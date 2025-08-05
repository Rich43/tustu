package sun.java2d.marlin;

import java.awt.Event;
import sun.awt.geom.PathConsumer2D;
import sun.misc.Unsafe;

/* loaded from: rt.jar:sun/java2d/marlin/Renderer.class */
final class Renderer implements PathConsumer2D, MarlinConst {
    static final boolean DISABLE_RENDER = false;
    private static final int ALL_BUT_LSB = -2;
    private static final int ERR_STEP_MAX = Integer.MAX_VALUE;
    private static final double POWER_2_TO_32 = 4.294967296E9d;
    public static final int WIND_EVEN_ODD = 0;
    public static final int WIND_NON_ZERO = 1;
    public static final long OFF_CURX_OR = 0;
    public static final int CUB_COUNT_LG = 2;
    private static final int CUB_COUNT = 4;
    private static final int CUB_COUNT_2 = 16;
    private static final int CUB_COUNT_3 = 64;
    private static final float CUB_INV_COUNT = 0.25f;
    private static final float CUB_INV_COUNT_2 = 0.0625f;
    private static final float CUB_INV_COUNT_3 = 0.015625f;
    private final OffHeapArray edges;
    private int buckets_minY;
    private int buckets_maxY;
    private int edgeSumDeltaY;
    final MarlinCache cache;
    private int boundsMinX;
    private int boundsMinY;
    private int boundsMaxX;
    private int boundsMaxY;
    private int windingRule;
    private float x0;
    private float y0;
    private float sx0;
    private float sy0;
    final RendererContext rdrCtx;
    private final Curve curve;
    private int bbox_spminX;
    private int bbox_spmaxX;
    private int bbox_spminY;
    private int bbox_spmaxY;
    static final boolean ENABLE_BLOCK_FLAGS = MarlinProperties.isUseTileFlags();
    static final boolean ENABLE_BLOCK_FLAGS_HEURISTICS = MarlinProperties.isUseTileFlagsWithHeuristics();
    public static final float f_SUBPIXEL_POSITIONS_X = SUBPIXEL_POSITIONS_X;
    public static final float f_SUBPIXEL_POSITIONS_Y = SUBPIXEL_POSITIONS_Y;
    public static final int SUBPIXEL_MASK_X = SUBPIXEL_POSITIONS_X - 1;
    public static final int SUBPIXEL_MASK_Y = SUBPIXEL_POSITIONS_Y - 1;
    private static final int SUBPIXEL_TILE = TILE_SIZE << SUBPIXEL_LG_POSITIONS_Y;
    static final int INITIAL_BUCKET_ARRAY = INITIAL_PIXEL_DIM * SUBPIXEL_POSITIONS_Y;
    public static final long OFF_ERROR = 0 + OffHeapArray.SIZE_INT;
    public static final long OFF_BUMP_X = OFF_ERROR + OffHeapArray.SIZE_INT;
    public static final long OFF_BUMP_ERR = OFF_BUMP_X + OffHeapArray.SIZE_INT;
    public static final long OFF_NEXT = OFF_BUMP_ERR + OffHeapArray.SIZE_INT;
    public static final long OFF_YMAX = OFF_NEXT + OffHeapArray.SIZE_INT;
    public static final int SIZEOF_EDGE_BYTES = (int) (OFF_YMAX + OffHeapArray.SIZE_INT);
    private static final float CUB_DEC_ERR_SUBPIX = 2.5f * (NORM_SUBPIXELS / 8.0f);
    private static final float CUB_INC_ERR_SUBPIX = 1.0f * (NORM_SUBPIXELS / 8.0f);
    public static final float CUB_DEC_BND = 8.0f * CUB_DEC_ERR_SUBPIX;
    public static final float CUB_INC_BND = 8.0f * CUB_INC_ERR_SUBPIX;
    private static final float QUAD_DEC_ERR_SUBPIX = 1.0f * (NORM_SUBPIXELS / 8.0f);
    public static final float QUAD_DEC_BND = 8.0f * QUAD_DEC_ERR_SUBPIX;
    private final int[] crossings_initial = new int[1024];
    private final int[] edgePtrs_initial = new int[Event.INSERT];
    private final int[] aux_crossings_initial = new int[1024];
    private final int[] aux_edgePtrs_initial = new int[Event.INSERT];
    private int edgeMinY = Integer.MAX_VALUE;
    private int edgeMaxY = Integer.MIN_VALUE;
    private float edgeMinX = Float.POSITIVE_INFINITY;
    private float edgeMaxX = Float.NEGATIVE_INFINITY;
    private final int[] edgeBuckets_initial = new int[INITIAL_BUCKET_ARRAY + 1];
    private final int[] edgeBucketCounts_initial = new int[INITIAL_BUCKET_ARRAY + 1];
    private final int[] alphaLine_initial = new int[INITIAL_AA_ARRAY];
    private boolean enableBlkFlags = false;
    private boolean prevUseBlkFlags = false;
    private final int[] blkFlags_initial = new int[256];
    private int[] blkFlags = this.blkFlags_initial;
    private int[] edgeBuckets = this.edgeBuckets_initial;
    private int[] edgeBucketCounts = this.edgeBucketCounts_initial;
    private int[] alphaLine = this.alphaLine_initial;
    private int[] crossings = this.crossings_initial;
    private int[] aux_crossings = this.aux_crossings_initial;
    private int[] edgePtrs = this.edgePtrs_initial;
    private int[] aux_edgePtrs = this.aux_edgePtrs_initial;
    private int edgeCount = 0;
    private int activeEdgeMaxUsed = 0;

    private void quadBreakIntoLinesAndAdd(float f2, float f3, Curve curve, float f4, float f5) {
        int i2 = 1;
        float fMax = FloatMath.max(Math.abs(curve.dbx), Math.abs(curve.dby));
        float f6 = QUAD_DEC_BND;
        while (fMax >= f6) {
            fMax /= 4.0f;
            i2 <<= 1;
            if (doStats) {
                RendererContext.stats.stat_rdr_quadBreak_dec.add(i2);
            }
        }
        int i3 = 0;
        if (i2 > 1) {
            float f7 = 1.0f / i2;
            float f8 = f7 * f7;
            float f9 = curve.dbx * f8;
            float f10 = curve.dby * f8;
            float f11 = (curve.f13564bx * f8) + (curve.cx * f7);
            float f12 = (curve.f13565by * f8) + (curve.cy * f7);
            while (true) {
                i2--;
                if (i2 <= 0) {
                    break;
                }
                float f13 = f2 + f11;
                f11 += f9;
                float f14 = f3 + f12;
                f12 += f10;
                addLine(f2, f3, f13, f14);
                if (doStats) {
                    i3++;
                }
                f2 = f13;
                f3 = f14;
            }
        }
        addLine(f2, f3, f4, f5);
        if (doStats) {
            RendererContext.stats.stat_rdr_quadBreak.add(i3 + 1);
        }
    }

    private void curveBreakIntoLinesAndAdd(float f2, float f3, Curve curve, float f4, float f5) {
        int i2 = 4;
        float f6 = 2.0f * curve.dax * CUB_INV_COUNT_3;
        float f7 = 2.0f * curve.day * CUB_INV_COUNT_3;
        float f8 = f6 + (curve.dbx * CUB_INV_COUNT_2);
        float f9 = f7 + (curve.dby * CUB_INV_COUNT_2);
        float f10 = (curve.f13562ax * CUB_INV_COUNT_3) + (curve.f13564bx * CUB_INV_COUNT_2) + (curve.cx * CUB_INV_COUNT);
        float f11 = (curve.f13563ay * CUB_INV_COUNT_3) + (curve.f13565by * CUB_INV_COUNT_2) + (curve.cy * CUB_INV_COUNT);
        float f12 = f2;
        float f13 = f3;
        int i3 = 0;
        float f14 = CUB_DEC_BND;
        float f15 = CUB_INC_BND;
        while (i2 > 0) {
            while (true) {
                if (Math.abs(f8) < f14 && Math.abs(f9) < f14) {
                    break;
                }
                f6 /= 8.0f;
                f7 /= 8.0f;
                f8 = (f8 / 4.0f) - f6;
                f9 = (f9 / 4.0f) - f7;
                f10 = (f10 - f8) / 2.0f;
                f11 = (f11 - f9) / 2.0f;
                i2 <<= 1;
                if (doStats) {
                    RendererContext.stats.stat_rdr_curveBreak_dec.add(i2);
                }
            }
            while (i2 % 2 == 0 && Math.abs(f10) <= f15 && Math.abs(f11) <= f15) {
                f10 = (2.0f * f10) + f8;
                f11 = (2.0f * f11) + f9;
                f8 = 4.0f * (f8 + f6);
                f9 = 4.0f * (f9 + f7);
                f6 *= 8.0f;
                f7 *= 8.0f;
                i2 >>= 1;
                if (doStats) {
                    RendererContext.stats.stat_rdr_curveBreak_inc.add(i2);
                }
            }
            i2--;
            if (i2 > 0) {
                f12 += f10;
                f10 += f8;
                f8 += f6;
                f13 += f11;
                f11 += f9;
                f9 += f7;
            } else {
                f12 = f4;
                f13 = f5;
            }
            addLine(f2, f3, f12, f13);
            if (doStats) {
                i3++;
            }
            f2 = f12;
            f3 = f13;
        }
        if (doStats) {
            RendererContext.stats.stat_rdr_curveBreak.add(i3);
        }
    }

    private void addLine(float f2, float f3, float f4, float f5) {
        if (doStats) {
            RendererContext.stats.stat_rdr_addLine.add(1);
        }
        int i2 = 1;
        if (f5 < f3) {
            i2 = 0;
            f5 = f3;
            f3 = f5;
            f4 = f2;
            f2 = f4;
        }
        int iMax = FloatMath.max(FloatMath.ceil_int(f3), this.boundsMinY);
        int iMin = FloatMath.min(FloatMath.ceil_int(f5), this.boundsMaxY);
        if (iMax >= iMin) {
            if (doStats) {
                RendererContext.stats.stat_rdr_addLine_skip.add(1);
                return;
            }
            return;
        }
        if (iMax < this.edgeMinY) {
            this.edgeMinY = iMax;
        }
        if (iMin > this.edgeMaxY) {
            this.edgeMaxY = iMin;
        }
        double d2 = f2;
        double d3 = f3;
        double d4 = (d2 - f4) / (d3 - f5);
        if (d4 >= 0.0d) {
            if (f2 < this.edgeMinX) {
                this.edgeMinX = f2;
            }
            if (f4 > this.edgeMaxX) {
                this.edgeMaxX = f4;
            }
        } else {
            if (f4 < this.edgeMinX) {
                this.edgeMinX = f4;
            }
            if (f2 > this.edgeMaxX) {
                this.edgeMaxX = f2;
            }
        }
        int i3 = SIZEOF_EDGE_BYTES;
        OffHeapArray offHeapArray = this.edges;
        int i4 = offHeapArray.used;
        if (offHeapArray.length - i4 < i3) {
            long newLargeSize = ArrayCache.getNewLargeSize(offHeapArray.length, i4 + i3);
            if (doStats) {
                RendererContext.stats.stat_rdr_edges_resizes.add(newLargeSize);
            }
            offHeapArray.resize(newLargeSize);
        }
        Unsafe unsafe = OffHeapArray.unsafe;
        long j2 = offHeapArray.address + i4;
        long j3 = ((long) (POWER_2_TO_32 * (d2 + ((iMax - d3) * d4)))) + 2147483647L;
        unsafe.putInt(j2, (((int) (j3 >> 31)) & (-2)) | i2);
        long j4 = j2 + 4;
        unsafe.putInt(j4, ((int) j3) >>> 1);
        long j5 = j4 + 4;
        long j6 = (long) (POWER_2_TO_32 * d4);
        unsafe.putInt(j5, ((int) (j6 >> 31)) & (-2));
        long j7 = j5 + 4;
        unsafe.putInt(j7, ((int) j6) >>> 1);
        long j8 = j7 + 4;
        int[] iArr = this.edgeBuckets;
        int[] iArr2 = this.edgeBucketCounts;
        int i5 = this.boundsMinY;
        int i6 = iMax - i5;
        unsafe.putInt(j8, iArr[i6]);
        unsafe.putInt(j8 + 4, iMin);
        iArr[i6] = i4;
        iArr2[i6] = iArr2[i6] + 2;
        int i7 = iMin - i5;
        iArr2[i7] = iArr2[i7] | 1;
        this.edgeSumDeltaY += iMin - iMax;
        offHeapArray.used += i3;
    }

    Renderer(RendererContext rendererContext) {
        this.rdrCtx = rendererContext;
        this.edges = new OffHeapArray(rendererContext, 98304L);
        this.curve = rendererContext.curve;
        this.cache = rendererContext.cache;
    }

    Renderer init(int i2, int i3, int i4, int i5, int i6) {
        this.windingRule = i6;
        this.boundsMinX = i2 << SUBPIXEL_LG_POSITIONS_X;
        this.boundsMaxX = (i2 + i4) << SUBPIXEL_LG_POSITIONS_X;
        this.boundsMinY = i3 << SUBPIXEL_LG_POSITIONS_Y;
        this.boundsMaxY = (i3 + i5) << SUBPIXEL_LG_POSITIONS_Y;
        if (doLogBounds) {
            MarlinUtils.logInfo("boundsXY = [" + this.boundsMinX + " ... " + this.boundsMaxX + "[ [" + this.boundsMinY + " ... " + this.boundsMaxY + "[");
        }
        int i7 = (this.boundsMaxY - this.boundsMinY) + 1;
        if (i7 > INITIAL_BUCKET_ARRAY) {
            if (doStats) {
                RendererContext.stats.stat_array_renderer_edgeBuckets.add(i7);
                RendererContext.stats.stat_array_renderer_edgeBucketCounts.add(i7);
            }
            this.edgeBuckets = this.rdrCtx.getIntArray(i7);
            this.edgeBucketCounts = this.rdrCtx.getIntArray(i7);
        }
        this.edgeMinY = Integer.MAX_VALUE;
        this.edgeMaxY = Integer.MIN_VALUE;
        this.edgeMinX = Float.POSITIVE_INFINITY;
        this.edgeMaxX = Float.NEGATIVE_INFINITY;
        this.edgeCount = 0;
        this.activeEdgeMaxUsed = 0;
        this.edges.used = 0;
        this.edgeSumDeltaY = 0;
        return this;
    }

    void dispose() {
        if (doStats) {
            RendererContext.stats.stat_rdr_activeEdges.add(this.activeEdgeMaxUsed);
            RendererContext.stats.stat_rdr_edges.add(this.edges.used);
            RendererContext.stats.stat_rdr_edges_count.add(this.edges.used / SIZEOF_EDGE_BYTES);
        }
        if (this.crossings != this.crossings_initial) {
            this.rdrCtx.putDirtyIntArray(this.crossings);
            this.crossings = this.crossings_initial;
            if (this.aux_crossings != this.aux_crossings_initial) {
                this.rdrCtx.putDirtyIntArray(this.aux_crossings);
                this.aux_crossings = this.aux_crossings_initial;
            }
        }
        if (this.edgePtrs != this.edgePtrs_initial) {
            this.rdrCtx.putDirtyIntArray(this.edgePtrs);
            this.edgePtrs = this.edgePtrs_initial;
            if (this.aux_edgePtrs != this.aux_edgePtrs_initial) {
                this.rdrCtx.putDirtyIntArray(this.aux_edgePtrs);
                this.aux_edgePtrs = this.aux_edgePtrs_initial;
            }
        }
        if (this.alphaLine != this.alphaLine_initial) {
            this.rdrCtx.putIntArray(this.alphaLine, 0, 0);
            this.alphaLine = this.alphaLine_initial;
        }
        if (this.blkFlags != this.blkFlags_initial) {
            this.rdrCtx.putIntArray(this.blkFlags, 0, 0);
            this.blkFlags = this.blkFlags_initial;
        }
        if (this.edgeMinY != Integer.MAX_VALUE) {
            if (this.rdrCtx.dirty) {
                this.buckets_minY = 0;
                this.buckets_maxY = this.boundsMaxY - this.boundsMinY;
            }
            if (this.edgeBuckets == this.edgeBuckets_initial) {
                IntArrayCache.fill(this.edgeBuckets, this.buckets_minY, this.buckets_maxY, 0);
                IntArrayCache.fill(this.edgeBucketCounts, this.buckets_minY, this.buckets_maxY + 1, 0);
            } else {
                this.rdrCtx.putIntArray(this.edgeBuckets, this.buckets_minY, this.buckets_maxY);
                this.edgeBuckets = this.edgeBuckets_initial;
                this.rdrCtx.putIntArray(this.edgeBucketCounts, this.buckets_minY, this.buckets_maxY + 1);
                this.edgeBucketCounts = this.edgeBucketCounts_initial;
            }
        } else if (this.edgeBuckets != this.edgeBuckets_initial) {
            this.rdrCtx.putIntArray(this.edgeBuckets, 0, 0);
            this.edgeBuckets = this.edgeBuckets_initial;
            this.rdrCtx.putIntArray(this.edgeBucketCounts, 0, 0);
            this.edgeBucketCounts = this.edgeBucketCounts_initial;
        }
        if (this.edges.length != 98304) {
            this.edges.resize(98304L);
        }
    }

    private static float tosubpixx(float f2) {
        return f_SUBPIXEL_POSITIONS_X * f2;
    }

    private static float tosubpixy(float f2) {
        return (f_SUBPIXEL_POSITIONS_Y * f2) - 0.5f;
    }

    @Override // sun.awt.geom.PathConsumer2D
    public void moveTo(float f2, float f3) {
        closePath();
        float f4 = tosubpixx(f2);
        float f5 = tosubpixy(f3);
        this.sx0 = f4;
        this.sy0 = f5;
        this.x0 = f4;
        this.y0 = f5;
    }

    @Override // sun.awt.geom.PathConsumer2D
    public void lineTo(float f2, float f3) {
        float f4 = tosubpixx(f2);
        float f5 = tosubpixy(f3);
        addLine(this.x0, this.y0, f4, f5);
        this.x0 = f4;
        this.y0 = f5;
    }

    @Override // sun.awt.geom.PathConsumer2D
    public void curveTo(float f2, float f3, float f4, float f5, float f6, float f7) {
        float f8 = tosubpixx(f6);
        float f9 = tosubpixy(f7);
        this.curve.set(this.x0, this.y0, tosubpixx(f2), tosubpixy(f3), tosubpixx(f4), tosubpixy(f5), f8, f9);
        curveBreakIntoLinesAndAdd(this.x0, this.y0, this.curve, f8, f9);
        this.x0 = f8;
        this.y0 = f9;
    }

    @Override // sun.awt.geom.PathConsumer2D
    public void quadTo(float f2, float f3, float f4, float f5) {
        float f6 = tosubpixx(f4);
        float f7 = tosubpixy(f5);
        this.curve.set(this.x0, this.y0, tosubpixx(f2), tosubpixy(f3), f6, f7);
        quadBreakIntoLinesAndAdd(this.x0, this.y0, this.curve, f6, f7);
        this.x0 = f6;
        this.y0 = f7;
    }

    @Override // sun.awt.geom.PathConsumer2D
    public void closePath() {
        addLine(this.x0, this.y0, this.sx0, this.sy0);
        this.x0 = this.sx0;
        this.y0 = this.sy0;
    }

    @Override // sun.awt.geom.PathConsumer2D
    public void pathDone() {
        closePath();
    }

    @Override // sun.awt.geom.PathConsumer2D
    public long getNativeConsumer() {
        throw new InternalError("Renderer does not use a native consumer.");
    }

    private void _endRendering(int i2, int i3) {
        int i4 = this.bbox_spminX;
        int i5 = this.bbox_spmaxX;
        boolean z2 = this.windingRule == 0;
        int[] iArr = this.alphaLine;
        MarlinCache marlinCache = this.cache;
        OffHeapArray offHeapArray = this.edges;
        int[] iArr2 = this.edgeBuckets;
        int[] iArr3 = this.edgeBucketCounts;
        int[] iArr4 = this.crossings;
        int[] iArr5 = this.edgePtrs;
        int[] iArr6 = this.aux_crossings;
        int[] iArr7 = this.aux_edgePtrs;
        long j2 = OFF_ERROR;
        long j3 = OFF_BUMP_X;
        long j4 = OFF_BUMP_ERR;
        long j5 = OFF_NEXT;
        long j6 = OFF_YMAX;
        Unsafe unsafe = OffHeapArray.unsafe;
        long j7 = offHeapArray.address;
        int i6 = SUBPIXEL_LG_POSITIONS_X;
        int i7 = SUBPIXEL_LG_POSITIONS_Y;
        int i8 = SUBPIXEL_MASK_X;
        int i9 = SUBPIXEL_MASK_Y;
        int i10 = SUBPIXEL_POSITIONS_X;
        int i11 = Integer.MAX_VALUE;
        int i12 = Integer.MIN_VALUE;
        int i13 = i2;
        int i14 = i13 - this.boundsMinY;
        int i15 = this.edgeCount;
        int length = iArr5.length;
        int length2 = iArr4.length;
        int i16 = this.activeEdgeMaxUsed;
        int i17 = 0;
        int[] iArr8 = this.blkFlags;
        int i18 = BLOCK_SIZE_LG;
        int i19 = BLOCK_SIZE;
        boolean z3 = ENABLE_BLOCK_FLAGS_HEURISTICS && this.enableBlkFlags;
        boolean z4 = this.prevUseBlkFlags;
        int i20 = this.rdrCtx.stroking;
        int i21 = -1;
        while (i13 < i3) {
            int i22 = iArr3[i14];
            int i23 = i15;
            if (i22 != 0) {
                if (doStats) {
                    RendererContext.stats.stat_rdr_activeEdges_updates.add(i15);
                }
                if ((i22 & 1) != 0) {
                    long j8 = j7 + j6;
                    int i24 = 0;
                    for (int i25 = 0; i25 < i15; i25++) {
                        int i26 = iArr5[i25];
                        if (unsafe.getInt(j8 + i26) > i13) {
                            int i27 = i24;
                            i24++;
                            iArr5[i27] = i26;
                        }
                    }
                    int i28 = i24;
                    i15 = i28;
                    i23 = i28;
                }
                i17 = i22 >> 1;
                if (i17 != 0) {
                    if (doStats) {
                        RendererContext.stats.stat_rdr_activeEdges_adds.add(i17);
                        if (i17 > 10) {
                            RendererContext.stats.stat_rdr_activeEdges_adds_high.add(i17);
                        }
                    }
                    int i29 = i15 + i17;
                    if (length < i29) {
                        if (doStats) {
                            RendererContext.stats.stat_array_renderer_edgePtrs.add(i29);
                        }
                        int[] iArrWidenDirtyIntArray = this.rdrCtx.widenDirtyIntArray(iArr5, i15, i29);
                        iArr5 = iArrWidenDirtyIntArray;
                        this.edgePtrs = iArrWidenDirtyIntArray;
                        length = iArr5.length;
                        if (iArr7 != this.aux_edgePtrs_initial) {
                            this.rdrCtx.putDirtyIntArray(iArr7);
                        }
                        if (doStats) {
                            RendererContext.stats.stat_array_renderer_aux_edgePtrs.add(i29);
                        }
                        int[] dirtyIntArray = this.rdrCtx.getDirtyIntArray(ArrayCache.getNewSize(i15, i29));
                        iArr7 = dirtyIntArray;
                        this.aux_edgePtrs = dirtyIntArray;
                    }
                    long j9 = j7 + j5;
                    int i30 = iArr2[i14];
                    while (i15 < i29) {
                        iArr5[i15] = i30;
                        i30 = unsafe.getInt(j9 + i30);
                        i15++;
                    }
                    if (length2 < i15) {
                        if (iArr4 != this.crossings_initial) {
                            this.rdrCtx.putDirtyIntArray(iArr4);
                        }
                        if (doStats) {
                            RendererContext.stats.stat_array_renderer_crossings.add(i15);
                        }
                        int[] dirtyIntArray2 = this.rdrCtx.getDirtyIntArray(i15);
                        iArr4 = dirtyIntArray2;
                        this.crossings = dirtyIntArray2;
                        if (iArr6 != this.aux_crossings_initial) {
                            this.rdrCtx.putDirtyIntArray(iArr6);
                        }
                        if (doStats) {
                            RendererContext.stats.stat_array_renderer_aux_crossings.add(i15);
                        }
                        int[] dirtyIntArray3 = this.rdrCtx.getDirtyIntArray(i15);
                        iArr6 = dirtyIntArray3;
                        this.aux_crossings = dirtyIntArray3;
                        length2 = iArr4.length;
                    }
                    if (doStats && i15 > i16) {
                        i16 = i15;
                    }
                }
            }
            if (i15 != 0) {
                if (i17 < 10 || i15 < 40) {
                    if (doStats) {
                        RendererContext.stats.hist_rdr_crossings.add(i15);
                        RendererContext.stats.hist_rdr_crossings_adds.add(i17);
                    }
                    boolean z5 = i15 >= 20;
                    int i31 = Integer.MIN_VALUE;
                    for (int i32 = 0; i32 < i15; i32++) {
                        int i33 = iArr5[i32];
                        long j10 = j7 + i33;
                        int i34 = unsafe.getInt(j10);
                        int i35 = i34 + unsafe.getInt(j10 + j3);
                        int i36 = unsafe.getInt(j10 + j2) + unsafe.getInt(j10 + j4);
                        unsafe.putInt(j10, i35 - ((i36 >> 30) & (-2)));
                        unsafe.putInt(j10 + j2, i36 & Integer.MAX_VALUE);
                        if (doStats) {
                            RendererContext.stats.stat_rdr_crossings_updates.add(i15);
                        }
                        if (i34 >= i31) {
                            i31 = i34;
                            iArr4[i32] = i34;
                        } else {
                            if (doStats) {
                                RendererContext.stats.stat_rdr_crossings_sorts.add(i32);
                            }
                            if (z5 && i32 >= i23) {
                                if (doStats) {
                                    RendererContext.stats.stat_rdr_crossings_bsearch.add(i32);
                                }
                                int i37 = 0;
                                int i38 = i32 - 1;
                                do {
                                    int i39 = (i37 + i38) >> 1;
                                    if (iArr4[i39] < i34) {
                                        i37 = i39 + 1;
                                    } else {
                                        i38 = i39 - 1;
                                    }
                                } while (i37 <= i38);
                                for (int i40 = i32 - 1; i40 >= i37; i40--) {
                                    iArr4[i40 + 1] = iArr4[i40];
                                    iArr5[i40 + 1] = iArr5[i40];
                                }
                                iArr4[i37] = i34;
                                iArr5[i37] = i33;
                            } else {
                                int i41 = i32 - 1;
                                iArr4[i32] = iArr4[i41];
                                iArr5[i32] = iArr5[i41];
                                while (true) {
                                    i41--;
                                    if (i41 < 0 || iArr4[i41] <= i34) {
                                        break;
                                    }
                                    iArr4[i41 + 1] = iArr4[i41];
                                    iArr5[i41 + 1] = iArr5[i41];
                                }
                                iArr4[i41 + 1] = i34;
                                iArr5[i41 + 1] = i33;
                            }
                        }
                    }
                } else {
                    if (doStats) {
                        RendererContext.stats.stat_rdr_crossings_msorts.add(i15);
                        RendererContext.stats.hist_rdr_crossings_ratio.add((1000 * i17) / i15);
                        RendererContext.stats.hist_rdr_crossings_msorts.add(i15);
                        RendererContext.stats.hist_rdr_crossings_msorts_adds.add(i17);
                    }
                    int i42 = Integer.MIN_VALUE;
                    for (int i43 = 0; i43 < i15; i43++) {
                        int i44 = iArr5[i43];
                        long j11 = j7 + i44;
                        int i45 = unsafe.getInt(j11);
                        int i46 = i45 + unsafe.getInt(j11 + j3);
                        int i47 = unsafe.getInt(j11 + j2) + unsafe.getInt(j11 + j4);
                        unsafe.putInt(j11, i46 - ((i47 >> 30) & (-2)));
                        unsafe.putInt(j11 + j2, i47 & Integer.MAX_VALUE);
                        if (doStats) {
                            RendererContext.stats.stat_rdr_crossings_updates.add(i15);
                        }
                        if (i43 >= i23) {
                            iArr4[i43] = i45;
                        } else if (i45 >= i42) {
                            i42 = i45;
                            iArr6[i43] = i45;
                            iArr7[i43] = i44;
                        } else {
                            if (doStats) {
                                RendererContext.stats.stat_rdr_crossings_sorts.add(i43);
                            }
                            int i48 = i43 - 1;
                            iArr6[i43] = iArr6[i48];
                            iArr7[i43] = iArr7[i48];
                            while (true) {
                                i48--;
                                if (i48 < 0 || iArr6[i48] <= i45) {
                                    break;
                                }
                                iArr6[i48 + 1] = iArr6[i48];
                                iArr7[i48 + 1] = iArr7[i48];
                            }
                            iArr6[i48 + 1] = i45;
                            iArr7[i48 + 1] = i44;
                        }
                    }
                    MergeSort.mergeSortNoCopy(iArr4, iArr5, iArr6, iArr7, i15, i23);
                }
                i17 = 0;
                int i49 = iArr4[0];
                int i50 = i49 >> 1;
                if (i50 < i11) {
                    i11 = i50;
                }
                int i51 = iArr4[i15 - 1] >> 1;
                if (i51 > i12) {
                    i12 = i51;
                }
                int i52 = i50;
                int i53 = i50;
                int i54 = ((i49 & 1) << 1) - 1;
                if (z2) {
                    int i55 = i54;
                    for (int i56 = 1; i56 < i15; i56++) {
                        int i57 = iArr4[i56];
                        int i58 = i57 >> 1;
                        int i59 = ((i57 & 1) << 1) - 1;
                        if ((i55 & 1) != 0) {
                            int i60 = i53 > i4 ? i53 : i4;
                            int i61 = i58 < i5 ? i58 : i5;
                            if (i60 < i61) {
                                int i62 = i60 - i4;
                                int i63 = i61 - i4;
                                int i64 = i62 >> i6;
                                if (i64 == ((i63 - 1) >> i6)) {
                                    int i65 = i63 - i62;
                                    iArr[i64] = iArr[i64] + i65;
                                    int i66 = i64 + 1;
                                    iArr[i66] = iArr[i66] - i65;
                                    if (z4) {
                                        iArr8[i64 >> i18] = 1;
                                    }
                                } else {
                                    int i67 = i62 & i8;
                                    iArr[i64] = iArr[i64] + (i10 - i67);
                                    int i68 = i64 + 1;
                                    iArr[i68] = iArr[i68] + i67;
                                    int i69 = i63 >> i6;
                                    int i70 = i63 & i8;
                                    iArr[i69] = iArr[i69] - (i10 - i70);
                                    int i71 = i69 + 1;
                                    iArr[i71] = iArr[i71] - i70;
                                    if (z4) {
                                        iArr8[i64 >> i18] = 1;
                                        iArr8[i69 >> i18] = 1;
                                    }
                                }
                            }
                        }
                        i55 += i59;
                        i53 = i58;
                    }
                } else {
                    int i72 = 1;
                    int i73 = 0;
                    while (true) {
                        i73 += i54;
                        if (i73 != 0) {
                            if (i53 > i52) {
                                i53 = i52;
                            }
                        } else {
                            int i74 = i53 > i4 ? i53 : i4;
                            int i75 = i52 < i5 ? i52 : i5;
                            if (i74 < i75) {
                                int i76 = i74 - i4;
                                int i77 = i75 - i4;
                                int i78 = i76 >> i6;
                                if (i78 == ((i77 - 1) >> i6)) {
                                    int i79 = i77 - i76;
                                    iArr[i78] = iArr[i78] + i79;
                                    int i80 = i78 + 1;
                                    iArr[i80] = iArr[i80] - i79;
                                    if (z4) {
                                        iArr8[i78 >> i18] = 1;
                                    }
                                } else {
                                    int i81 = i76 & i8;
                                    iArr[i78] = iArr[i78] + (i10 - i81);
                                    int i82 = i78 + 1;
                                    iArr[i82] = iArr[i82] + i81;
                                    int i83 = i77 >> i6;
                                    int i84 = i77 & i8;
                                    iArr[i83] = iArr[i83] - (i10 - i84);
                                    int i85 = i83 + 1;
                                    iArr[i85] = iArr[i85] - i84;
                                    if (z4) {
                                        iArr8[i78 >> i18] = 1;
                                        iArr8[i83 >> i18] = 1;
                                    }
                                }
                            }
                            i53 = Integer.MAX_VALUE;
                        }
                        if (i72 == i15) {
                            break;
                        }
                        int i86 = iArr4[i72];
                        i52 = i86 >> 1;
                        i54 = ((i86 & 1) << 1) - 1;
                        i72++;
                    }
                }
            }
            if ((i13 & i9) == i9) {
                i21 = i13 >> i7;
                int iMax = FloatMath.max(i11, i4) >> i6;
                int iMin = FloatMath.min(i12, i5) >> i6;
                if (iMin >= iMax) {
                    copyAARow(iArr, i21, iMax, iMin + 2, z4);
                    if (z3) {
                        int i87 = iMin - iMax;
                        z4 = i87 > i19 && i87 > (((i15 >> i20) - 1) << i18);
                        if (doStats) {
                            RendererContext.stats.hist_tile_generator_encoding_dist.add(i87 / FloatMath.max(1, (i15 >> i20) - 1));
                        }
                    }
                } else {
                    marlinCache.clearAARow(i21);
                }
                i11 = Integer.MAX_VALUE;
                i12 = Integer.MIN_VALUE;
            }
            i13++;
            i14++;
        }
        int i88 = (i13 - 1) >> i7;
        int iMax2 = FloatMath.max(i11, i4) >> i6;
        int iMin2 = FloatMath.min(i12, i5) >> i6;
        if (iMin2 >= iMax2) {
            copyAARow(iArr, i88, iMax2, iMin2 + 2, z4);
        } else if (i88 != i21) {
            marlinCache.clearAARow(i88);
        }
        this.edgeCount = i15;
        this.prevUseBlkFlags = z4;
        if (doStats) {
            this.activeEdgeMaxUsed = i16;
        }
    }

    boolean endRendering() {
        int i2;
        int i3;
        if (this.edgeMinY == Integer.MAX_VALUE) {
            return false;
        }
        int i4 = this.boundsMinY;
        int i5 = this.boundsMaxY;
        int iMax = FloatMath.max(FloatMath.ceil_int(this.edgeMinX - 0.5f), this.boundsMinX);
        int iMin = FloatMath.min(FloatMath.ceil_int(this.edgeMaxX - 0.5f), this.boundsMaxX - 1);
        int i6 = this.edgeMinY;
        int i7 = this.edgeMaxY;
        if (i7 <= i5 - 1) {
            i2 = i7;
        } else {
            i2 = i5 - 1;
            i7 = i5;
        }
        this.buckets_minY = i6 - i4;
        this.buckets_maxY = i7 - i4;
        if (doLogBounds) {
            MarlinUtils.logInfo("edgesXY = [" + this.edgeMinX + " ... " + this.edgeMaxX + "][" + this.edgeMinY + " ... " + this.edgeMaxY + "]");
            MarlinUtils.logInfo("spXY    = [" + iMax + " ... " + iMin + "][" + i6 + " ... " + i2 + "]");
        }
        if (iMax > iMin || i6 > i2) {
            return false;
        }
        int i8 = iMax >> SUBPIXEL_LG_POSITIONS_X;
        int i9 = (iMin + SUBPIXEL_MASK_X) >> SUBPIXEL_LG_POSITIONS_X;
        int i10 = i6 >> SUBPIXEL_LG_POSITIONS_Y;
        int i11 = (i2 + SUBPIXEL_MASK_Y) >> SUBPIXEL_LG_POSITIONS_Y;
        this.cache.init(i8, i10, i9, i11, this.edgeSumDeltaY);
        if (ENABLE_BLOCK_FLAGS) {
            this.enableBlkFlags = this.cache.useRLE;
            this.prevUseBlkFlags = this.enableBlkFlags && !ENABLE_BLOCK_FLAGS_HEURISTICS;
            if (this.enableBlkFlags && (i3 = ((i9 - i8) >> TILE_SIZE_LG) + 2) > 256) {
                this.blkFlags = this.rdrCtx.getIntArray(i3);
            }
        }
        this.bbox_spminX = i8 << SUBPIXEL_LG_POSITIONS_X;
        this.bbox_spmaxX = i9 << SUBPIXEL_LG_POSITIONS_X;
        this.bbox_spminY = i6;
        this.bbox_spmaxY = FloatMath.min(i2 + 1, i11 << SUBPIXEL_LG_POSITIONS_Y);
        if (doLogBounds) {
            MarlinUtils.logInfo("pXY       = [" + i8 + " ... " + i9 + "[ [" + i10 + " ... " + i11 + "[");
            MarlinUtils.logInfo("bbox_spXY = [" + this.bbox_spminX + " ... " + this.bbox_spmaxX + "[ [" + this.bbox_spminY + " ... " + this.bbox_spmaxY + "[");
        }
        int i12 = (i9 - i8) + 2;
        if (i12 > INITIAL_AA_ARRAY) {
            if (doStats) {
                RendererContext.stats.stat_array_renderer_alphaline.add(i12);
            }
            this.alphaLine = this.rdrCtx.getIntArray(i12);
        }
        endRendering(i10);
        return true;
    }

    void endRendering(int i2) {
        int i3 = i2 << SUBPIXEL_LG_POSITIONS_Y;
        int iMax = FloatMath.max(this.bbox_spminY, i3);
        if (iMax < this.bbox_spmaxY) {
            int iMin = FloatMath.min(this.bbox_spmaxY, i3 + SUBPIXEL_TILE);
            this.cache.resetTileLine(i2);
            _endRendering(iMax, iMin);
        }
    }

    void copyAARow(int[] iArr, int i2, int i3, int i4, boolean z2) {
        if (z2) {
            if (doStats) {
                RendererContext.stats.hist_tile_generator_encoding.add(1);
            }
            this.cache.copyAARowRLE_WithBlockFlags(this.blkFlags, iArr, i2, i3, i4);
        } else {
            if (doStats) {
                RendererContext.stats.hist_tile_generator_encoding.add(0);
            }
            this.cache.copyAARowNoRLE(iArr, i2, i3, i4);
        }
    }
}
