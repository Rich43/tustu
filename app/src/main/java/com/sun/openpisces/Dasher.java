package com.sun.openpisces;

import com.sun.javafx.geom.PathConsumer2D;

/* loaded from: jfxrt.jar:com/sun/openpisces/Dasher.class */
public final class Dasher implements PathConsumer2D {
    private final PathConsumer2D out;
    private float[] dash;
    private float startPhase;
    private boolean startDashOn;
    private int startIdx;
    private boolean starting;
    private boolean needsMoveTo;
    private int idx;
    private boolean dashOn;
    private float phase;
    private float sx;
    private float sy;
    private float x0;
    private float y0;
    private float[] curCurvepts;
    static float MAX_CYCLES = 1.6E7f;
    private float[] firstSegmentsBuffer;
    private int firstSegidx;
    private LengthIterator li;

    public Dasher(PathConsumer2D out, float[] dash, float phase) {
        this(out);
        reset(dash, phase);
    }

    public Dasher(PathConsumer2D out) {
        this.firstSegmentsBuffer = new float[7];
        this.firstSegidx = 0;
        this.li = null;
        this.out = out;
        this.curCurvepts = new float[16];
    }

    public void reset(float[] dash, float phase) {
        int sidx = 0;
        this.dashOn = true;
        float sum = 0.0f;
        for (float f2 : dash) {
            sum += f2;
        }
        float cycles = phase / sum;
        if (phase < 0.0f) {
            if ((-cycles) >= MAX_CYCLES) {
                phase = 0.0f;
            } else {
                int fullcycles = (int) Math.floor(-cycles);
                if ((fullcycles & dash.length & 1) != 0) {
                    this.dashOn = !this.dashOn;
                }
                phase += fullcycles * sum;
                while (phase < 0.0f) {
                    sidx--;
                    if (sidx < 0) {
                        sidx = dash.length - 1;
                    }
                    phase += dash[sidx];
                    this.dashOn = !this.dashOn;
                }
            }
        } else if (phase > 0.0f) {
            if (cycles >= MAX_CYCLES) {
                phase = 0.0f;
            } else {
                int fullcycles2 = (int) Math.floor(cycles);
                if ((fullcycles2 & dash.length & 1) != 0) {
                    this.dashOn = !this.dashOn;
                }
                phase -= fullcycles2 * sum;
                while (true) {
                    float f3 = phase;
                    float d2 = dash[sidx];
                    if (f3 < d2) {
                        break;
                    }
                    phase -= d2;
                    sidx = (sidx + 1) % dash.length;
                    this.dashOn = !this.dashOn;
                }
            }
        }
        this.dash = dash;
        float f4 = phase;
        this.phase = f4;
        this.startPhase = f4;
        this.startDashOn = this.dashOn;
        this.startIdx = sidx;
        this.starting = true;
    }

    @Override // com.sun.javafx.geom.PathConsumer2D
    public void moveTo(float x0, float y0) {
        if (this.firstSegidx > 0) {
            this.out.moveTo(this.sx, this.sy);
            emitFirstSegments();
        }
        this.needsMoveTo = true;
        this.idx = this.startIdx;
        this.dashOn = this.startDashOn;
        this.phase = this.startPhase;
        this.x0 = x0;
        this.sx = x0;
        this.y0 = y0;
        this.sy = y0;
        this.starting = true;
    }

    private void emitSeg(float[] buf, int off, int type) {
        switch (type) {
            case 4:
                this.out.lineTo(buf[off], buf[off + 1]);
                break;
            case 6:
                this.out.quadTo(buf[off + 0], buf[off + 1], buf[off + 2], buf[off + 3]);
                break;
            case 8:
                this.out.curveTo(buf[off + 0], buf[off + 1], buf[off + 2], buf[off + 3], buf[off + 4], buf[off + 5]);
                break;
        }
    }

    private void emitFirstSegments() {
        int i2 = 0;
        while (true) {
            int i3 = i2;
            if (i3 < this.firstSegidx) {
                emitSeg(this.firstSegmentsBuffer, i3 + 1, (int) this.firstSegmentsBuffer[i3]);
                i2 = i3 + (((int) this.firstSegmentsBuffer[i3]) - 1);
            } else {
                this.firstSegidx = 0;
                return;
            }
        }
    }

    private void goTo(float[] pts, int off, int type) {
        float x2 = pts[(off + type) - 4];
        float y2 = pts[(off + type) - 3];
        if (this.dashOn) {
            if (this.starting) {
                this.firstSegmentsBuffer = Helpers.widenArray(this.firstSegmentsBuffer, this.firstSegidx, type - 1);
                float[] fArr = this.firstSegmentsBuffer;
                int i2 = this.firstSegidx;
                this.firstSegidx = i2 + 1;
                fArr[i2] = type;
                System.arraycopy(pts, off, this.firstSegmentsBuffer, this.firstSegidx, type - 2);
                this.firstSegidx += type - 2;
            } else {
                if (this.needsMoveTo) {
                    this.out.moveTo(this.x0, this.y0);
                    this.needsMoveTo = false;
                }
                emitSeg(pts, off, type);
            }
        } else {
            this.starting = false;
            this.needsMoveTo = true;
        }
        this.x0 = x2;
        this.y0 = y2;
    }

    @Override // com.sun.javafx.geom.PathConsumer2D
    public void lineTo(float x1, float y1) {
        float leftInThisDashSegment;
        float dx = x1 - this.x0;
        float dy = y1 - this.y0;
        float len = (float) Math.sqrt((dx * dx) + (dy * dy));
        if (len == 0.0f) {
            return;
        }
        float cx = dx / len;
        float cy = dy / len;
        while (true) {
            leftInThisDashSegment = this.dash[this.idx] - this.phase;
            if (len <= leftInThisDashSegment) {
                break;
            }
            float dashdx = this.dash[this.idx] * cx;
            float dashdy = this.dash[this.idx] * cy;
            if (this.phase == 0.0f) {
                this.curCurvepts[0] = this.x0 + dashdx;
                this.curCurvepts[1] = this.y0 + dashdy;
            } else {
                float p2 = leftInThisDashSegment / this.dash[this.idx];
                this.curCurvepts[0] = this.x0 + (p2 * dashdx);
                this.curCurvepts[1] = this.y0 + (p2 * dashdy);
            }
            goTo(this.curCurvepts, 0, 4);
            len -= leftInThisDashSegment;
            this.idx = (this.idx + 1) % this.dash.length;
            this.dashOn = !this.dashOn;
            this.phase = 0.0f;
        }
        this.curCurvepts[0] = x1;
        this.curCurvepts[1] = y1;
        goTo(this.curCurvepts, 0, 4);
        this.phase += len;
        if (len == leftInThisDashSegment) {
            this.phase = 0.0f;
            this.idx = (this.idx + 1) % this.dash.length;
            this.dashOn = !this.dashOn;
        }
    }

    private void somethingTo(int type) {
        if (pointCurve(this.curCurvepts, type)) {
            return;
        }
        if (this.li == null) {
            this.li = new LengthIterator(4, 0.01f);
        }
        this.li.initializeIterationOnCurve(this.curCurvepts, type);
        int curCurveoff = 0;
        float lastSplitT = 0.0f;
        float f2 = this.dash[this.idx] - this.phase;
        while (true) {
            float leftInThisDashSegment = f2;
            float t2 = this.li.next(leftInThisDashSegment);
            if (t2 >= 1.0f) {
                break;
            }
            if (t2 != 0.0f) {
                Helpers.subdivideAt((t2 - lastSplitT) / (1.0f - lastSplitT), this.curCurvepts, curCurveoff, this.curCurvepts, 0, this.curCurvepts, type, type);
                lastSplitT = t2;
                goTo(this.curCurvepts, 2, type);
                curCurveoff = type;
            }
            this.idx = (this.idx + 1) % this.dash.length;
            this.dashOn = !this.dashOn;
            this.phase = 0.0f;
            f2 = this.dash[this.idx];
        }
        goTo(this.curCurvepts, curCurveoff + 2, type);
        this.phase += this.li.lastSegLen();
        if (this.phase >= this.dash[this.idx]) {
            this.phase = 0.0f;
            this.idx = (this.idx + 1) % this.dash.length;
            this.dashOn = !this.dashOn;
        }
    }

    private static boolean pointCurve(float[] curve, int type) {
        for (int i2 = 2; i2 < type; i2++) {
            if (curve[i2] != curve[i2 - 2]) {
                return false;
            }
        }
        return true;
    }

    /* loaded from: jfxrt.jar:com/sun/openpisces/Dasher$LengthIterator.class */
    private static class LengthIterator {
        private float[][] recCurveStack;
        private Side[] sides;
        private int curveType;
        private final int limit;
        private final float ERR;
        private final float minTincrement;
        private float lastT;
        private float lenAtLastT;
        private float[] curLeafCtrlPolyLengths = new float[3];
        private int cachedHaveLowAcceleration = -1;
        private float[] nextRoots = new float[4];
        private float[] flatLeafCoefCache = {0.0f, 0.0f, -1.0f, 0.0f};
        private float nextT = Float.MAX_VALUE;
        private float lenAtNextT = Float.MAX_VALUE;
        private float lenAtLastSplit = Float.MIN_VALUE;
        private int recLevel = Integer.MIN_VALUE;
        private float lastSegLen = Float.MAX_VALUE;
        private boolean done = true;

        /* loaded from: jfxrt.jar:com/sun/openpisces/Dasher$LengthIterator$Side.class */
        private enum Side {
            LEFT,
            RIGHT
        }

        public LengthIterator(int reclimit, float err) {
            this.limit = reclimit;
            this.minTincrement = 1.0f / (1 << this.limit);
            this.ERR = err;
            this.recCurveStack = new float[reclimit + 1][8];
            this.sides = new Side[reclimit];
        }

        public void initializeIterationOnCurve(float[] pts, int type) {
            System.arraycopy(pts, 0, this.recCurveStack[0], 0, type);
            this.curveType = type;
            this.recLevel = 0;
            this.lastT = 0.0f;
            this.lenAtLastT = 0.0f;
            this.nextT = 0.0f;
            this.lenAtNextT = 0.0f;
            goLeft();
            this.lenAtLastSplit = 0.0f;
            if (this.recLevel > 0) {
                this.sides[0] = Side.LEFT;
                this.done = false;
            } else {
                this.sides[0] = Side.RIGHT;
                this.done = true;
            }
            this.lastSegLen = 0.0f;
        }

        private boolean haveLowAcceleration(float err) {
            if (this.cachedHaveLowAcceleration != -1) {
                return this.cachedHaveLowAcceleration == 1;
            }
            float len1 = this.curLeafCtrlPolyLengths[0];
            float len2 = this.curLeafCtrlPolyLengths[1];
            if (!Helpers.within(len1, len2, err * len2)) {
                this.cachedHaveLowAcceleration = 0;
                return false;
            }
            if (this.curveType == 8) {
                float len3 = this.curLeafCtrlPolyLengths[2];
                if (!Helpers.within(len2, len3, err * len3) || !Helpers.within(len1, len3, err * len3)) {
                    this.cachedHaveLowAcceleration = 0;
                    return false;
                }
            }
            this.cachedHaveLowAcceleration = 1;
            return true;
        }

        public float next(float len) {
            float targetLength = this.lenAtLastSplit + len;
            while (this.lenAtNextT < targetLength) {
                if (this.done) {
                    this.lastSegLen = this.lenAtNextT - this.lenAtLastSplit;
                    return 1.0f;
                }
                goToNextLeaf();
            }
            this.lenAtLastSplit = targetLength;
            float leaflen = this.lenAtNextT - this.lenAtLastT;
            float t2 = (targetLength - this.lenAtLastT) / leaflen;
            if (!haveLowAcceleration(0.05f)) {
                if (this.flatLeafCoefCache[2] < 0.0f) {
                    float x2 = 0.0f + this.curLeafCtrlPolyLengths[0];
                    float y2 = x2 + this.curLeafCtrlPolyLengths[1];
                    if (this.curveType == 8) {
                        float z2 = y2 + this.curLeafCtrlPolyLengths[2];
                        this.flatLeafCoefCache[0] = (3.0f * (x2 - y2)) + z2;
                        this.flatLeafCoefCache[1] = 3.0f * (y2 - (2.0f * x2));
                        this.flatLeafCoefCache[2] = 3.0f * x2;
                        this.flatLeafCoefCache[3] = -z2;
                    } else if (this.curveType == 6) {
                        this.flatLeafCoefCache[0] = 0.0f;
                        this.flatLeafCoefCache[1] = y2 - (2.0f * x2);
                        this.flatLeafCoefCache[2] = 2.0f * x2;
                        this.flatLeafCoefCache[3] = -y2;
                    }
                }
                float a2 = this.flatLeafCoefCache[0];
                float b2 = this.flatLeafCoefCache[1];
                float c2 = this.flatLeafCoefCache[2];
                float d2 = t2 * this.flatLeafCoefCache[3];
                int n2 = Helpers.cubicRootsInAB(a2, b2, c2, d2, this.nextRoots, 0, 0.0f, 1.0f);
                if (n2 == 1 && !Float.isNaN(this.nextRoots[0])) {
                    t2 = this.nextRoots[0];
                }
            }
            float t3 = (t2 * (this.nextT - this.lastT)) + this.lastT;
            if (t3 >= 1.0f) {
                t3 = 1.0f;
                this.done = true;
            }
            this.lastSegLen = len;
            return t3;
        }

        public float lastSegLen() {
            return this.lastSegLen;
        }

        private void goToNextLeaf() {
            this.recLevel--;
            while (this.sides[this.recLevel] == Side.RIGHT) {
                if (this.recLevel == 0) {
                    this.done = true;
                    return;
                }
                this.recLevel--;
            }
            this.sides[this.recLevel] = Side.RIGHT;
            System.arraycopy(this.recCurveStack[this.recLevel], 0, this.recCurveStack[this.recLevel + 1], 0, this.curveType);
            this.recLevel++;
            goLeft();
        }

        private void goLeft() {
            float len = onLeaf();
            if (len >= 0.0f) {
                this.lastT = this.nextT;
                this.lenAtLastT = this.lenAtNextT;
                this.nextT += (1 << (this.limit - this.recLevel)) * this.minTincrement;
                this.lenAtNextT += len;
                this.flatLeafCoefCache[2] = -1.0f;
                this.cachedHaveLowAcceleration = -1;
                return;
            }
            Helpers.subdivide(this.recCurveStack[this.recLevel], 0, this.recCurveStack[this.recLevel + 1], 0, this.recCurveStack[this.recLevel], 0, this.curveType);
            this.sides[this.recLevel] = Side.LEFT;
            this.recLevel++;
            goLeft();
        }

        private float onLeaf() {
            float[] curve = this.recCurveStack[this.recLevel];
            float polyLen = 0.0f;
            float x0 = curve[0];
            float y0 = curve[1];
            for (int i2 = 2; i2 < this.curveType; i2 += 2) {
                float x1 = curve[i2];
                float y1 = curve[i2 + 1];
                float len = Helpers.linelen(x0, y0, x1, y1);
                polyLen += len;
                this.curLeafCtrlPolyLengths[(i2 / 2) - 1] = len;
                x0 = x1;
                y0 = y1;
            }
            float lineLen = Helpers.linelen(curve[0], curve[1], curve[this.curveType - 2], curve[this.curveType - 1]);
            if (polyLen - lineLen < this.ERR || this.recLevel == this.limit) {
                return (polyLen + lineLen) / 2.0f;
            }
            return -1.0f;
        }
    }

    @Override // com.sun.javafx.geom.PathConsumer2D
    public void curveTo(float x1, float y1, float x2, float y2, float x3, float y3) {
        this.curCurvepts[0] = this.x0;
        this.curCurvepts[1] = this.y0;
        this.curCurvepts[2] = x1;
        this.curCurvepts[3] = y1;
        this.curCurvepts[4] = x2;
        this.curCurvepts[5] = y2;
        this.curCurvepts[6] = x3;
        this.curCurvepts[7] = y3;
        somethingTo(8);
    }

    @Override // com.sun.javafx.geom.PathConsumer2D
    public void quadTo(float x1, float y1, float x2, float y2) {
        this.curCurvepts[0] = this.x0;
        this.curCurvepts[1] = this.y0;
        this.curCurvepts[2] = x1;
        this.curCurvepts[3] = y1;
        this.curCurvepts[4] = x2;
        this.curCurvepts[5] = y2;
        somethingTo(6);
    }

    @Override // com.sun.javafx.geom.PathConsumer2D
    public void closePath() {
        lineTo(this.sx, this.sy);
        if (this.firstSegidx > 0) {
            if (!this.dashOn || this.needsMoveTo) {
                this.out.moveTo(this.sx, this.sy);
            }
            emitFirstSegments();
        }
        moveTo(this.sx, this.sy);
    }

    @Override // com.sun.javafx.geom.PathConsumer2D
    public void pathDone() {
        if (this.firstSegidx > 0) {
            this.out.moveTo(this.sx, this.sy);
            emitFirstSegments();
        }
        this.out.pathDone();
    }
}
