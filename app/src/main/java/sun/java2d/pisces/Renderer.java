package sun.java2d.pisces;

import java.util.Arrays;
import sun.awt.geom.PathConsumer2D;

/* loaded from: rt.jar:sun/java2d/pisces/Renderer.class */
final class Renderer implements PathConsumer2D {
    private static final int YMAX = 0;
    private static final int CURX = 1;
    private static final int OR = 2;
    private static final int SLOPE = 3;
    private static final int NEXT = 4;
    private static final int SIZEOF_EDGE = 5;
    private static final int NULL = -5;
    private float[] edges;
    private static final int INIT_NUM_EDGES = 8;
    private int[] edgeBuckets;
    private int[] edgeBucketCounts;
    private static final float DEC_BND = 20.0f;
    private static final float INC_BND = 8.0f;
    public static final int WIND_EVEN_ODD = 0;
    public static final int WIND_NON_ZERO = 1;
    private final int SUBPIXEL_LG_POSITIONS_X;
    private final int SUBPIXEL_LG_POSITIONS_Y;
    private final int SUBPIXEL_POSITIONS_X;
    private final int SUBPIXEL_POSITIONS_Y;
    private final int SUBPIXEL_MASK_X;
    private final int SUBPIXEL_MASK_Y;
    final int MAX_AA_ALPHA;
    PiscesCache cache;
    private final int boundsMinX;
    private final int boundsMinY;
    private final int boundsMaxX;
    private final int boundsMaxY;
    private final int windingRule;
    private float x0;
    private float y0;
    private float pix_sx0;
    private float pix_sy0;
    private float edgeMinY = Float.POSITIVE_INFINITY;
    private float edgeMaxY = Float.NEGATIVE_INFINITY;
    private float edgeMinX = Float.POSITIVE_INFINITY;
    private float edgeMaxX = Float.NEGATIVE_INFINITY;

    /* renamed from: c, reason: collision with root package name */
    private Curve f13575c = new Curve();
    private int numEdges = 0;

    /* loaded from: rt.jar:sun/java2d/pisces/Renderer$ScanlineIterator.class */
    private class ScanlineIterator {
        private int[] crossings;
        private final int maxY;
        private int nextY;
        private int edgeCount;
        private int[] edgePtrs;
        private static final int INIT_CROSSINGS_SIZE = 10;

        private ScanlineIterator(int i2, int i3) {
            this.crossings = new int[10];
            this.edgePtrs = new int[10];
            this.nextY = i2;
            this.maxY = i3;
            this.edgeCount = 0;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public int next() {
            int i2;
            int i3 = this.nextY;
            this.nextY = i3 + 1;
            int i4 = i3 - Renderer.this.boundsMinY;
            int i5 = this.edgeCount;
            int[] iArr = this.edgePtrs;
            int i6 = Renderer.this.edgeBucketCounts[i4];
            if ((i6 & 1) != 0) {
                int i7 = 0;
                for (int i8 = 0; i8 < i5; i8++) {
                    int i9 = iArr[i8];
                    if (Renderer.this.edges[i9 + 0] > i3) {
                        int i10 = i7;
                        i7++;
                        iArr[i10] = i9;
                    }
                }
                i5 = i7;
            }
            int[] iArrWidenArray = Helpers.widenArray(iArr, i5, i6 >> 1);
            int i11 = Renderer.this.edgeBuckets[i4];
            while (true) {
                int i12 = i11;
                if (i12 == -5) {
                    break;
                }
                int i13 = i5;
                i5++;
                iArrWidenArray[i13] = i12;
                i11 = (int) Renderer.this.edges[i12 + 4];
            }
            this.edgePtrs = iArrWidenArray;
            this.edgeCount = i5;
            int[] iArr2 = this.crossings;
            if (iArr2.length < i5) {
                int[] iArr3 = new int[iArrWidenArray.length];
                iArr2 = iArr3;
                this.crossings = iArr3;
            }
            for (int i14 = 0; i14 < i5; i14++) {
                int i15 = iArrWidenArray[i14];
                float f2 = Renderer.this.edges[i15 + 1];
                int i16 = ((int) f2) << 1;
                Renderer.this.edges[i15 + 1] = f2 + Renderer.this.edges[i15 + 3];
                if (Renderer.this.edges[i15 + 2] > 0.0f) {
                    i16 |= 1;
                }
                int i17 = i14;
                while (true) {
                    i17--;
                    if (i17 < 0 || (i2 = iArr2[i17]) <= i16) {
                        break;
                    }
                    iArr2[i17 + 1] = i2;
                    iArrWidenArray[i17 + 1] = iArrWidenArray[i17];
                }
                iArr2[i17 + 1] = i16;
                iArrWidenArray[i17 + 1] = i15;
            }
            return i5;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean hasNext() {
            return this.nextY < this.maxY;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public int curY() {
            return this.nextY - 1;
        }
    }

    private void addEdgeToBucket(int i2, int i3) {
        this.edges[i2 + 4] = this.edgeBuckets[i3];
        this.edgeBuckets[i3] = i2;
        int[] iArr = this.edgeBucketCounts;
        iArr[i3] = iArr[i3] + 2;
    }

    private void quadBreakIntoLinesAndAdd(float f2, float f3, Curve curve, float f4, float f5) {
        int i2 = 16;
        int i3 = 16 * 16;
        float fMax = Math.max(curve.dbx / i3, curve.dby / i3);
        while (fMax > 32.0f) {
            fMax /= 4.0f;
            i2 <<= 1;
        }
        int i4 = i2 * i2;
        float f6 = curve.dbx / i4;
        float f7 = curve.dby / i4;
        float f8 = (curve.f13571bx / i4) + (curve.cx / i2);
        float f9 = (curve.f13572by / i4) + (curve.cy / i2);
        while (true) {
            int i5 = i2;
            i2--;
            if (i5 > 1) {
                float f10 = f2 + f8;
                f8 += f6;
                float f11 = f3 + f9;
                f9 += f7;
                addLine(f2, f3, f10, f11);
                f2 = f10;
                f3 = f11;
            } else {
                addLine(f2, f3, f4, f5);
                return;
            }
        }
    }

    private void curveBreakIntoLinesAndAdd(float f2, float f3, Curve curve, float f4, float f5) {
        int i2 = 8;
        float f6 = (2.0f * curve.dax) / 512.0f;
        float f7 = (2.0f * curve.day) / 512.0f;
        float f8 = f6 + (curve.dbx / 64.0f);
        float f9 = f7 + (curve.dby / 64.0f);
        float f10 = (curve.f13569ax / 512.0f) + (curve.f13571bx / 64.0f) + (curve.cx / INC_BND);
        float f11 = (curve.f13570ay / 512.0f) + (curve.f13572by / 64.0f) + (curve.cy / INC_BND);
        float f12 = f2;
        float f13 = f3;
        while (i2 > 0) {
            while (true) {
                if (Math.abs(f8) <= DEC_BND && Math.abs(f9) <= DEC_BND) {
                    break;
                }
                f6 /= INC_BND;
                f7 /= INC_BND;
                f8 = (f8 / 4.0f) - f6;
                f9 = (f9 / 4.0f) - f7;
                f10 = (f10 - f8) / 2.0f;
                f11 = (f11 - f9) / 2.0f;
                i2 <<= 1;
            }
            while (i2 % 2 == 0 && Math.abs(f10) <= INC_BND && Math.abs(f11) <= INC_BND) {
                f10 = (2.0f * f10) + f8;
                f11 = (2.0f * f11) + f9;
                f8 = 4.0f * (f8 + f6);
                f9 = 4.0f * (f9 + f7);
                f6 = INC_BND * f6;
                f7 = INC_BND * f7;
                i2 >>= 1;
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
            f2 = f12;
            f3 = f13;
        }
    }

    private void addLine(float f2, float f3, float f4, float f5) {
        float f6 = 1.0f;
        if (f5 < f3) {
            f5 = f3;
            f3 = f5;
            f4 = f2;
            f2 = f4;
            f6 = 0.0f;
        }
        int iMax = Math.max((int) Math.ceil(f3), this.boundsMinY);
        int iMin = Math.min((int) Math.ceil(f5), this.boundsMaxY);
        if (iMax >= iMin) {
            return;
        }
        if (f3 < this.edgeMinY) {
            this.edgeMinY = f3;
        }
        if (f5 > this.edgeMaxY) {
            this.edgeMaxY = f5;
        }
        float f7 = (f4 - f2) / (f5 - f3);
        if (f7 > 0.0f) {
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
        int i2 = this.numEdges * 5;
        this.edges = Helpers.widenArray(this.edges, i2, 5);
        this.numEdges++;
        this.edges[i2 + 2] = f6;
        this.edges[i2 + 1] = f2 + ((iMax - f3) * f7);
        this.edges[i2 + 3] = f7;
        this.edges[i2 + 0] = iMin;
        addEdgeToBucket(i2, iMax - this.boundsMinY);
        int[] iArr = this.edgeBucketCounts;
        int i3 = iMin - this.boundsMinY;
        iArr[i3] = iArr[i3] | 1;
    }

    public Renderer(int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
        this.edges = null;
        this.edgeBuckets = null;
        this.edgeBucketCounts = null;
        this.SUBPIXEL_LG_POSITIONS_X = i2;
        this.SUBPIXEL_LG_POSITIONS_Y = i3;
        this.SUBPIXEL_MASK_X = (1 << this.SUBPIXEL_LG_POSITIONS_X) - 1;
        this.SUBPIXEL_MASK_Y = (1 << this.SUBPIXEL_LG_POSITIONS_Y) - 1;
        this.SUBPIXEL_POSITIONS_X = 1 << this.SUBPIXEL_LG_POSITIONS_X;
        this.SUBPIXEL_POSITIONS_Y = 1 << this.SUBPIXEL_LG_POSITIONS_Y;
        this.MAX_AA_ALPHA = this.SUBPIXEL_POSITIONS_X * this.SUBPIXEL_POSITIONS_Y;
        this.windingRule = i8;
        this.boundsMinX = i4 * this.SUBPIXEL_POSITIONS_X;
        this.boundsMinY = i5 * this.SUBPIXEL_POSITIONS_Y;
        this.boundsMaxX = (i4 + i6) * this.SUBPIXEL_POSITIONS_X;
        this.boundsMaxY = (i5 + i7) * this.SUBPIXEL_POSITIONS_Y;
        this.edges = new float[40];
        this.edgeBuckets = new int[this.boundsMaxY - this.boundsMinY];
        Arrays.fill(this.edgeBuckets, -5);
        this.edgeBucketCounts = new int[this.edgeBuckets.length + 1];
    }

    private float tosubpixx(float f2) {
        return f2 * this.SUBPIXEL_POSITIONS_X;
    }

    private float tosubpixy(float f2) {
        return f2 * this.SUBPIXEL_POSITIONS_Y;
    }

    @Override // sun.awt.geom.PathConsumer2D
    public void moveTo(float f2, float f3) {
        closePath();
        this.pix_sx0 = f2;
        this.pix_sy0 = f3;
        this.y0 = tosubpixy(f3);
        this.x0 = tosubpixx(f2);
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
        this.f13575c.set(this.x0, this.y0, tosubpixx(f2), tosubpixy(f3), tosubpixx(f4), tosubpixy(f5), f8, f9);
        curveBreakIntoLinesAndAdd(this.x0, this.y0, this.f13575c, f8, f9);
        this.x0 = f8;
        this.y0 = f9;
    }

    @Override // sun.awt.geom.PathConsumer2D
    public void quadTo(float f2, float f3, float f4, float f5) {
        float f6 = tosubpixx(f4);
        float f7 = tosubpixy(f5);
        this.f13575c.set(this.x0, this.y0, tosubpixx(f2), tosubpixy(f3), f6, f7);
        quadBreakIntoLinesAndAdd(this.x0, this.y0, this.f13575c, f6, f7);
        this.x0 = f6;
        this.y0 = f7;
    }

    @Override // sun.awt.geom.PathConsumer2D
    public void closePath() {
        lineTo(this.pix_sx0, this.pix_sy0);
    }

    @Override // sun.awt.geom.PathConsumer2D
    public void pathDone() {
        closePath();
    }

    @Override // sun.awt.geom.PathConsumer2D
    public long getNativeConsumer() {
        throw new InternalError("Renderer does not use a native consumer.");
    }

    private void _endRendering(int i2, int i3, int i4, int i5) {
        int iMax;
        int iMin;
        int i6 = this.windingRule == 0 ? 1 : -1;
        int[] iArr = new int[(i3 - i2) + 2];
        int i7 = i2 << this.SUBPIXEL_LG_POSITIONS_X;
        int i8 = i3 << this.SUBPIXEL_LG_POSITIONS_X;
        int iMax2 = Integer.MIN_VALUE;
        int iMin2 = Integer.MAX_VALUE;
        int iCurY = this.boundsMinY;
        ScanlineIterator scanlineIterator = new ScanlineIterator(i4, i5);
        while (scanlineIterator.hasNext()) {
            int next = scanlineIterator.next();
            int[] iArr2 = scanlineIterator.crossings;
            iCurY = scanlineIterator.curY();
            if (next > 0) {
                int i9 = iArr2[0] >> 1;
                int i10 = iArr2[next - 1] >> 1;
                int iMax3 = Math.max(i9, i7);
                int iMin3 = Math.min(i10, i8);
                iMin2 = Math.min(iMin2, iMax3 >> this.SUBPIXEL_LG_POSITIONS_X);
                iMax2 = Math.max(iMax2, iMin3 >> this.SUBPIXEL_LG_POSITIONS_X);
            }
            int i11 = 0;
            int i12 = i7;
            for (int i13 = 0; i13 < next; i13++) {
                int i14 = iArr2[i13];
                int i15 = i14 >> 1;
                int i16 = ((i14 & 1) << 1) - 1;
                if ((i11 & i6) != 0 && (iMax = Math.max(i12, i7)) < (iMin = Math.min(i15, i8))) {
                    int i17 = iMax - i7;
                    int i18 = iMin - i7;
                    int i19 = i17 >> this.SUBPIXEL_LG_POSITIONS_X;
                    if (i19 == ((i18 - 1) >> this.SUBPIXEL_LG_POSITIONS_X)) {
                        iArr[i19] = iArr[i19] + (i18 - i17);
                        int i20 = i19 + 1;
                        iArr[i20] = iArr[i20] - (i18 - i17);
                    } else {
                        int i21 = i18 >> this.SUBPIXEL_LG_POSITIONS_X;
                        iArr[i19] = iArr[i19] + (this.SUBPIXEL_POSITIONS_X - (i17 & this.SUBPIXEL_MASK_X));
                        int i22 = i19 + 1;
                        iArr[i22] = iArr[i22] + (i17 & this.SUBPIXEL_MASK_X);
                        iArr[i21] = iArr[i21] - (this.SUBPIXEL_POSITIONS_X - (i18 & this.SUBPIXEL_MASK_X));
                        int i23 = i21 + 1;
                        iArr[i23] = iArr[i23] - (i18 & this.SUBPIXEL_MASK_X);
                    }
                }
                i11 += i16;
                i12 = i15;
            }
            if ((iCurY & this.SUBPIXEL_MASK_Y) == this.SUBPIXEL_MASK_Y) {
                emitRow(iArr, iCurY >> this.SUBPIXEL_LG_POSITIONS_Y, iMin2, iMax2);
                iMin2 = Integer.MAX_VALUE;
                iMax2 = Integer.MIN_VALUE;
            }
        }
        if (iMax2 >= iMin2) {
            emitRow(iArr, iCurY >> this.SUBPIXEL_LG_POSITIONS_Y, iMin2, iMax2);
        }
    }

    public void endRendering() {
        int iMax = Math.max((int) Math.ceil(this.edgeMinX), this.boundsMinX);
        int iMin = Math.min((int) Math.ceil(this.edgeMaxX), this.boundsMaxX);
        int iMax2 = Math.max((int) Math.ceil(this.edgeMinY), this.boundsMinY);
        int iMin2 = Math.min((int) Math.ceil(this.edgeMaxY), this.boundsMaxY);
        int i2 = iMax >> this.SUBPIXEL_LG_POSITIONS_X;
        int i3 = (iMin + this.SUBPIXEL_MASK_X) >> this.SUBPIXEL_LG_POSITIONS_X;
        int i4 = iMax2 >> this.SUBPIXEL_LG_POSITIONS_Y;
        int i5 = (iMin2 + this.SUBPIXEL_MASK_Y) >> this.SUBPIXEL_LG_POSITIONS_Y;
        if (i2 > i3 || i4 > i5) {
            this.cache = new PiscesCache(this.boundsMinX >> this.SUBPIXEL_LG_POSITIONS_X, this.boundsMinY >> this.SUBPIXEL_LG_POSITIONS_Y, this.boundsMaxX >> this.SUBPIXEL_LG_POSITIONS_X, this.boundsMaxY >> this.SUBPIXEL_LG_POSITIONS_Y);
        } else {
            this.cache = new PiscesCache(i2, i4, i3, i5);
            _endRendering(i2, i3, iMax2, iMin2);
        }
    }

    public PiscesCache getCache() {
        if (this.cache == null) {
            throw new InternalError("cache not yet initialized");
        }
        return this.cache;
    }

    private void emitRow(int[] iArr, int i2, int i3, int i4) {
        if (this.cache != null && i4 >= i3) {
            this.cache.startRow(i2, i3);
            int i5 = i3 - this.cache.bboxX0;
            int i6 = i4 - this.cache.bboxX0;
            int i7 = 1;
            int i8 = iArr[i5];
            for (int i9 = i5 + 1; i9 <= i6; i9++) {
                int i10 = i8 + iArr[i9];
                if (i10 == i8) {
                    i7++;
                } else {
                    this.cache.addRLERun(i8, i7);
                    i7 = 1;
                    i8 = i10;
                }
            }
            this.cache.addRLERun(i8, i7);
        }
        Arrays.fill(iArr, 0);
    }
}
