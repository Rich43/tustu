package com.sun.openpisces;

import com.sun.javafx.geom.PathConsumer2D;
import java.util.Arrays;

/* loaded from: jfxrt.jar:com/sun/openpisces/Renderer.class */
public final class Renderer implements PathConsumer2D {
    private static final int YMAX = 0;
    private static final int CURX = 1;
    private static final int OR = 2;
    private static final int SLOPE = 3;
    private static final int NEXT = 4;
    private static final int SIZEOF_EDGE = 5;
    private static final int MAX_EDGE_IDX = 16777216;
    private int sampleRowMin;
    private int sampleRowMax;
    private float edgeMinX;
    private float edgeMaxX;
    private float[] edges;
    private int[] edgeBuckets;
    private int numEdges;
    private static final float DEC_BND = 1.0f;
    private static final float INC_BND = 0.4f;
    public static final int WIND_EVEN_ODD = 0;
    public static final int WIND_NON_ZERO = 1;
    private final int SUBPIXEL_LG_POSITIONS_X;
    private final int SUBPIXEL_LG_POSITIONS_Y;
    private final int SUBPIXEL_POSITIONS_X;
    private final int SUBPIXEL_POSITIONS_Y;
    private final int SUBPIXEL_MASK_X;
    private final int SUBPIXEL_MASK_Y;
    final int MAX_AA_ALPHA;
    private int boundsMinX;
    private int boundsMinY;
    private int boundsMaxX;
    private int boundsMaxY;
    private int windingRule;
    private float x0;
    private float y0;
    private float pix_sx0;
    private float pix_sy0;

    /* renamed from: c, reason: collision with root package name */
    private Curve f11993c;
    private int[] savedAlpha;
    private ScanlineIterator savedIterator;

    /* loaded from: jfxrt.jar:com/sun/openpisces/Renderer$ScanlineIterator.class */
    private final class ScanlineIterator {
        private int[] crossings;
        private int[] edgePtrs;
        private int edgeCount;
        private int nextY;
        private static final int INIT_CROSSINGS_SIZE = 10;

        private ScanlineIterator() {
            this.crossings = new int[10];
            this.edgePtrs = new int[10];
            reset();
        }

        public void reset() {
            this.nextY = Renderer.this.sampleRowMin;
            this.edgeCount = 0;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public int next() {
            int jcross;
            int cury = this.nextY;
            this.nextY = cury + 1;
            int bucket = cury - Renderer.this.boundsMinY;
            int count = this.edgeCount;
            int[] ptrs = this.edgePtrs;
            float[] edges = Renderer.this.edges;
            int bucketcount = Renderer.this.edgeBuckets[(bucket * 2) + 1];
            if ((bucketcount & 1) != 0) {
                int newCount = 0;
                for (int i2 = 0; i2 < count; i2++) {
                    int ecur = ptrs[i2];
                    if (edges[ecur + 0] > cury) {
                        int i3 = newCount;
                        newCount++;
                        ptrs[i3] = ecur;
                    }
                }
                count = newCount;
            }
            int[] ptrs2 = Helpers.widenArray(ptrs, count, bucketcount >> 1);
            int i4 = Renderer.this.edgeBuckets[bucket * 2];
            while (true) {
                int ecur2 = i4;
                if (ecur2 == 0) {
                    break;
                }
                int i5 = count;
                count++;
                int ecur3 = ecur2 - 1;
                ptrs2[i5] = ecur3;
                i4 = (int) edges[ecur3 + 4];
            }
            this.edgePtrs = ptrs2;
            this.edgeCount = count;
            int[] xings = this.crossings;
            if (xings.length < count) {
                int[] iArr = new int[ptrs2.length];
                xings = iArr;
                this.crossings = iArr;
            }
            for (int i6 = 0; i6 < count; i6++) {
                int ecur4 = ptrs2[i6];
                float curx = edges[ecur4 + 1];
                int cross = ((int) Math.ceil(curx - 0.5f)) << 1;
                edges[ecur4 + 1] = curx + edges[ecur4 + 3];
                if (edges[ecur4 + 2] > 0.0f) {
                    cross |= 1;
                }
                int j2 = i6;
                while (true) {
                    j2--;
                    if (j2 < 0 || (jcross = xings[j2]) <= cross) {
                        break;
                    }
                    xings[j2 + 1] = jcross;
                    ptrs2[j2 + 1] = ptrs2[j2];
                }
                xings[j2 + 1] = cross;
                ptrs2[j2 + 1] = ecur4;
            }
            return count;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public boolean hasNext() {
            return this.nextY < Renderer.this.sampleRowMax;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public int curY() {
            return this.nextY - 1;
        }
    }

    private void addEdgeToBucket(int eptr, int bucket) {
        if (this.edgeBuckets[bucket * 2] >= 16777216) {
            throw new ArrayIndexOutOfBoundsException(this.edgeBuckets[bucket * 2]);
        }
        this.edges[eptr + 4] = this.edgeBuckets[bucket * 2];
        this.edgeBuckets[bucket * 2] = eptr + 1;
        int[] iArr = this.edgeBuckets;
        int i2 = (bucket * 2) + 1;
        iArr[i2] = iArr[i2] + 2;
    }

    private void quadBreakIntoLinesAndAdd(float x0, float y0, Curve c2, float x2, float y2) {
        int count = 16;
        int countsq = 16 * 16;
        float maxDD = Math.max(c2.dbx / countsq, c2.dby / countsq);
        while (maxDD > 32.0f) {
            maxDD /= 4.0f;
            count <<= 1;
        }
        int countsq2 = count * count;
        float ddx = c2.dbx / countsq2;
        float ddy = c2.dby / countsq2;
        float dx = (c2.f11991bx / countsq2) + (c2.cx / count);
        float dy = (c2.f11992by / countsq2) + (c2.cy / count);
        while (true) {
            int i2 = count;
            count--;
            if (i2 > 1) {
                float x1 = x0 + dx;
                dx += ddx;
                float y1 = y0 + dy;
                dy += ddy;
                addLine(x0, y0, x1, y1);
                x0 = x1;
                y0 = y1;
            } else {
                addLine(x0, y0, x2, y2);
                return;
            }
        }
    }

    private void curveBreakIntoLinesAndAdd(float x0, float y0, Curve c2, float x3, float y3) {
        int count = 8;
        float dddx = (2.0f * c2.dax) / 512.0f;
        float dddy = (2.0f * c2.day) / 512.0f;
        float ddx = dddx + (c2.dbx / 64.0f);
        float ddy = dddy + (c2.dby / 64.0f);
        float dx = (c2.f11989ax / 512.0f) + (c2.f11991bx / 64.0f) + (c2.cx / 8.0f);
        float dy = (c2.f11990ay / 512.0f) + (c2.f11992by / 64.0f) + (c2.cy / 8.0f);
        float x1 = x0;
        float y1 = y0;
        while (count > 0) {
            while (true) {
                if (Math.abs(ddx) <= 1.0f && Math.abs(ddy) <= 1.0f) {
                    break;
                }
                dddx /= 8.0f;
                dddy /= 8.0f;
                ddx = (ddx / 4.0f) - dddx;
                ddy = (ddy / 4.0f) - dddy;
                dx = (dx - ddx) / 2.0f;
                dy = (dy - ddy) / 2.0f;
                count <<= 1;
            }
            while (count % 2 == 0 && Math.abs(dx) <= 0.4f && Math.abs(dy) <= 0.4f) {
                dx = (2.0f * dx) + ddx;
                dy = (2.0f * dy) + ddy;
                ddx = 4.0f * (ddx + dddx);
                ddy = 4.0f * (ddy + dddy);
                dddx = 8.0f * dddx;
                dddy = 8.0f * dddy;
                count >>= 1;
            }
            count--;
            if (count > 0) {
                x1 += dx;
                dx += ddx;
                ddx += dddx;
                y1 += dy;
                dy += ddy;
                ddy += dddy;
            } else {
                x1 = x3;
                y1 = y3;
            }
            addLine(x0, y0, x1, y1);
            x0 = x1;
            y0 = y1;
        }
    }

    private void addLine(float x1, float y1, float x2, float y2) {
        float or = 1.0f;
        if (y2 < y1) {
            y2 = y1;
            y1 = y2;
            x2 = x1;
            x1 = x2;
            or = 0.0f;
        }
        int firstCrossing = Math.max((int) Math.ceil(y1 - 0.5f), this.boundsMinY);
        int lastCrossing = Math.min((int) Math.ceil(y2 - 0.5f), this.boundsMaxY);
        if (firstCrossing >= lastCrossing) {
            return;
        }
        if (firstCrossing < this.sampleRowMin) {
            this.sampleRowMin = firstCrossing;
        }
        if (lastCrossing > this.sampleRowMax) {
            this.sampleRowMax = lastCrossing;
        }
        float slope = (x2 - x1) / (y2 - y1);
        if (slope > 0.0f) {
            if (x1 < this.edgeMinX) {
                this.edgeMinX = x1;
            }
            if (x2 > this.edgeMaxX) {
                this.edgeMaxX = x2;
            }
        } else {
            if (x2 < this.edgeMinX) {
                this.edgeMinX = x2;
            }
            if (x1 > this.edgeMaxX) {
                this.edgeMaxX = x1;
            }
        }
        int bucketIdx = firstCrossing - this.boundsMinY;
        int nextCurrEdge = this.edgeBuckets[bucketIdx * 2];
        if (nextCurrEdge >= 16777216) {
            throw new ArrayIndexOutOfBoundsException(nextCurrEdge);
        }
        int ptr = this.numEdges * 5;
        this.edges = Helpers.widenArray(this.edges, ptr, 5);
        this.numEdges++;
        this.edges[ptr + 2] = or;
        this.edges[ptr + 1] = x1 + (((firstCrossing + 0.5f) - y1) * slope);
        this.edges[ptr + 3] = slope;
        this.edges[ptr + 0] = lastCrossing;
        addEdgeToBucket(ptr, bucketIdx);
        int[] iArr = this.edgeBuckets;
        int i2 = ((lastCrossing - this.boundsMinY) * 2) + 1;
        iArr[i2] = iArr[i2] | 1;
    }

    public Renderer(int subpixelLgPositionsX, int subpixelLgPositionsY) {
        this.f11993c = new Curve();
        this.SUBPIXEL_LG_POSITIONS_X = subpixelLgPositionsX;
        this.SUBPIXEL_LG_POSITIONS_Y = subpixelLgPositionsY;
        this.SUBPIXEL_POSITIONS_X = 1 << this.SUBPIXEL_LG_POSITIONS_X;
        this.SUBPIXEL_POSITIONS_Y = 1 << this.SUBPIXEL_LG_POSITIONS_Y;
        this.SUBPIXEL_MASK_X = this.SUBPIXEL_POSITIONS_X - 1;
        this.SUBPIXEL_MASK_Y = this.SUBPIXEL_POSITIONS_Y - 1;
        this.MAX_AA_ALPHA = this.SUBPIXEL_POSITIONS_X * this.SUBPIXEL_POSITIONS_Y;
    }

    public Renderer(int subpixelLgPositionsX, int subpixelLgPositionsY, int pix_boundsX, int pix_boundsY, int pix_boundsWidth, int pix_boundsHeight, int windingRule) {
        this(subpixelLgPositionsX, subpixelLgPositionsY);
        reset(pix_boundsX, pix_boundsY, pix_boundsWidth, pix_boundsHeight, windingRule);
    }

    public void reset(int pix_boundsX, int pix_boundsY, int pix_boundsWidth, int pix_boundsHeight, int windingRule) {
        this.windingRule = windingRule;
        this.boundsMinX = pix_boundsX * this.SUBPIXEL_POSITIONS_X;
        this.boundsMinY = pix_boundsY * this.SUBPIXEL_POSITIONS_Y;
        this.boundsMaxX = (pix_boundsX + pix_boundsWidth) * this.SUBPIXEL_POSITIONS_X;
        this.boundsMaxY = (pix_boundsY + pix_boundsHeight) * this.SUBPIXEL_POSITIONS_Y;
        this.edgeMinX = Float.POSITIVE_INFINITY;
        this.edgeMaxX = Float.NEGATIVE_INFINITY;
        this.sampleRowMax = this.boundsMinY;
        this.sampleRowMin = this.boundsMaxY;
        int numBuckets = this.boundsMaxY - this.boundsMinY;
        if (this.edgeBuckets == null || this.edgeBuckets.length < (numBuckets * 2) + 2) {
            this.edgeBuckets = new int[(numBuckets * 2) + 2];
        } else {
            Arrays.fill(this.edgeBuckets, 0, numBuckets * 2, 0);
        }
        if (this.edges == null) {
            this.edges = new float[160];
        }
        this.numEdges = 0;
        this.y0 = 0.0f;
        this.x0 = 0.0f;
        this.pix_sy0 = 0.0f;
        this.pix_sx0 = 0.0f;
    }

    private float tosubpixx(float pix_x) {
        return pix_x * this.SUBPIXEL_POSITIONS_X;
    }

    private float tosubpixy(float pix_y) {
        return pix_y * this.SUBPIXEL_POSITIONS_Y;
    }

    @Override // com.sun.javafx.geom.PathConsumer2D
    public void moveTo(float pix_x0, float pix_y0) {
        closePath();
        this.pix_sx0 = pix_x0;
        this.pix_sy0 = pix_y0;
        this.y0 = tosubpixy(pix_y0);
        this.x0 = tosubpixx(pix_x0);
    }

    @Override // com.sun.javafx.geom.PathConsumer2D
    public void lineTo(float pix_x1, float pix_y1) {
        float x1 = tosubpixx(pix_x1);
        float y1 = tosubpixy(pix_y1);
        addLine(this.x0, this.y0, x1, y1);
        this.x0 = x1;
        this.y0 = y1;
    }

    @Override // com.sun.javafx.geom.PathConsumer2D
    public void curveTo(float x1, float y1, float x2, float y2, float x3, float y3) {
        float xe = tosubpixx(x3);
        float ye = tosubpixy(y3);
        this.f11993c.set(this.x0, this.y0, tosubpixx(x1), tosubpixy(y1), tosubpixx(x2), tosubpixy(y2), xe, ye);
        curveBreakIntoLinesAndAdd(this.x0, this.y0, this.f11993c, xe, ye);
        this.x0 = xe;
        this.y0 = ye;
    }

    @Override // com.sun.javafx.geom.PathConsumer2D
    public void quadTo(float x1, float y1, float x2, float y2) {
        float xe = tosubpixx(x2);
        float ye = tosubpixy(y2);
        this.f11993c.set(this.x0, this.y0, tosubpixx(x1), tosubpixy(y1), xe, ye);
        quadBreakIntoLinesAndAdd(this.x0, this.y0, this.f11993c, xe, ye);
        this.x0 = xe;
        this.y0 = ye;
    }

    @Override // com.sun.javafx.geom.PathConsumer2D
    public void closePath() {
        lineTo(this.pix_sx0, this.pix_sy0);
    }

    @Override // com.sun.javafx.geom.PathConsumer2D
    public void pathDone() {
        closePath();
    }

    public void produceAlphas(AlphaConsumer ac2) {
        int x0;
        int x1;
        ac2.setMaxAlpha(this.MAX_AA_ALPHA);
        int mask = this.windingRule == 0 ? 1 : -1;
        int width = ac2.getWidth();
        int[] alpha = this.savedAlpha;
        if (alpha == null || alpha.length < width + 2) {
            int[] iArr = new int[width + 2];
            alpha = iArr;
            this.savedAlpha = iArr;
        } else {
            Arrays.fill(alpha, 0, width + 2, 0);
        }
        int bboxx0 = ac2.getOriginX() << this.SUBPIXEL_LG_POSITIONS_X;
        int bboxx1 = bboxx0 + (width << this.SUBPIXEL_LG_POSITIONS_X);
        int pix_maxX = bboxx1 >> this.SUBPIXEL_LG_POSITIONS_X;
        int pix_minX = bboxx0 >> this.SUBPIXEL_LG_POSITIONS_Y;
        int y2 = this.boundsMinY;
        ScanlineIterator it = this.savedIterator;
        if (it == null) {
            ScanlineIterator scanlineIterator = new ScanlineIterator();
            it = scanlineIterator;
            this.savedIterator = scanlineIterator;
        } else {
            it.reset();
        }
        while (it.hasNext()) {
            int numCrossings = it.next();
            int[] crossings = it.crossings;
            y2 = it.curY();
            if (numCrossings > 0) {
                int lowx = crossings[0] >> 1;
                int highx = crossings[numCrossings - 1] >> 1;
                int x02 = Math.max(lowx, bboxx0);
                int x12 = Math.min(highx, bboxx1);
                pix_minX = Math.min(pix_minX, x02 >> this.SUBPIXEL_LG_POSITIONS_X);
                pix_maxX = Math.max(pix_maxX, x12 >> this.SUBPIXEL_LG_POSITIONS_X);
            }
            int sum = 0;
            int prev = bboxx0;
            for (int i2 = 0; i2 < numCrossings; i2++) {
                int curxo = crossings[i2];
                int curx = curxo >> 1;
                int crorientation = ((curxo & 1) << 1) - 1;
                if ((sum & mask) != 0 && (x0 = Math.max(prev, bboxx0)) < (x1 = Math.min(curx, bboxx1))) {
                    int x03 = x0 - bboxx0;
                    int x13 = x1 - bboxx0;
                    int pix_x = x03 >> this.SUBPIXEL_LG_POSITIONS_X;
                    int pix_xmaxm1 = (x13 - 1) >> this.SUBPIXEL_LG_POSITIONS_X;
                    if (pix_x == pix_xmaxm1) {
                        int[] iArr2 = alpha;
                        iArr2[pix_x] = iArr2[pix_x] + (x13 - x03);
                        int[] iArr3 = alpha;
                        int i3 = pix_x + 1;
                        iArr3[i3] = iArr3[i3] - (x13 - x03);
                    } else {
                        int pix_xmax = x13 >> this.SUBPIXEL_LG_POSITIONS_X;
                        int[] iArr4 = alpha;
                        iArr4[pix_x] = iArr4[pix_x] + (this.SUBPIXEL_POSITIONS_X - (x03 & this.SUBPIXEL_MASK_X));
                        int[] iArr5 = alpha;
                        int i4 = pix_x + 1;
                        iArr5[i4] = iArr5[i4] + (x03 & this.SUBPIXEL_MASK_X);
                        int[] iArr6 = alpha;
                        iArr6[pix_xmax] = iArr6[pix_xmax] - (this.SUBPIXEL_POSITIONS_X - (x13 & this.SUBPIXEL_MASK_X));
                        int[] iArr7 = alpha;
                        int i5 = pix_xmax + 1;
                        iArr7[i5] = iArr7[i5] - (x13 & this.SUBPIXEL_MASK_X);
                    }
                }
                sum += crorientation;
                prev = curx;
            }
            if ((y2 & this.SUBPIXEL_MASK_Y) == this.SUBPIXEL_MASK_Y) {
                ac2.setAndClearRelativeAlphas(alpha, y2 >> this.SUBPIXEL_LG_POSITIONS_Y, pix_minX, pix_maxX);
                pix_maxX = bboxx1 >> this.SUBPIXEL_LG_POSITIONS_X;
                pix_minX = bboxx0 >> this.SUBPIXEL_LG_POSITIONS_Y;
            }
        }
        if ((y2 & this.SUBPIXEL_MASK_Y) < this.SUBPIXEL_MASK_Y) {
            ac2.setAndClearRelativeAlphas(alpha, y2 >> this.SUBPIXEL_LG_POSITIONS_Y, pix_minX, pix_maxX);
        }
    }

    public int getSubpixMinX() {
        int sampleColMin = (int) Math.ceil(this.edgeMinX - 0.5f);
        if (sampleColMin < this.boundsMinX) {
            sampleColMin = this.boundsMinX;
        }
        return sampleColMin;
    }

    public int getSubpixMaxX() {
        int sampleColMax = (int) Math.ceil(this.edgeMaxX - 0.5f);
        if (sampleColMax > this.boundsMaxX) {
            sampleColMax = this.boundsMaxX;
        }
        return sampleColMax;
    }

    public int getSubpixMinY() {
        return this.sampleRowMin;
    }

    public int getSubpixMaxY() {
        return this.sampleRowMax;
    }

    public int getOutpixMinX() {
        return getSubpixMinX() >> this.SUBPIXEL_LG_POSITIONS_X;
    }

    public int getOutpixMaxX() {
        return (getSubpixMaxX() + this.SUBPIXEL_MASK_X) >> this.SUBPIXEL_LG_POSITIONS_X;
    }

    public int getOutpixMinY() {
        return this.sampleRowMin >> this.SUBPIXEL_LG_POSITIONS_Y;
    }

    public int getOutpixMaxY() {
        return (this.sampleRowMax + this.SUBPIXEL_MASK_Y) >> this.SUBPIXEL_LG_POSITIONS_Y;
    }
}
