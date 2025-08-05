package sun.java2d.pisces;

import sun.awt.geom.PathConsumer2D;

/* loaded from: rt.jar:sun/java2d/pisces/Dasher.class */
final class Dasher implements PathConsumer2D {
    private final PathConsumer2D out;
    private final float[] dash;
    private final float startPhase;
    private final boolean startDashOn;
    private final int startIdx;
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
    private float[] firstSegmentsBuffer = new float[7];
    private int firstSegidx = 0;
    private LengthIterator li = null;

    public Dasher(PathConsumer2D pathConsumer2D, float[] fArr, float f2) {
        if (f2 < 0.0f) {
            throw new IllegalArgumentException("phase < 0 !");
        }
        this.out = pathConsumer2D;
        int length = 0;
        this.dashOn = true;
        while (true) {
            float f3 = f2;
            float f4 = fArr[length];
            if (f3 >= f4) {
                f2 -= f4;
                length = (length + 1) % fArr.length;
                this.dashOn = !this.dashOn;
            } else {
                this.dash = fArr;
                float f5 = f2;
                this.phase = f5;
                this.startPhase = f5;
                this.startDashOn = this.dashOn;
                this.startIdx = length;
                this.starting = true;
                this.curCurvepts = new float[16];
                return;
            }
        }
    }

    @Override // sun.awt.geom.PathConsumer2D
    public void moveTo(float f2, float f3) {
        if (this.firstSegidx > 0) {
            this.out.moveTo(this.sx, this.sy);
            emitFirstSegments();
        }
        this.needsMoveTo = true;
        this.idx = this.startIdx;
        this.dashOn = this.startDashOn;
        this.phase = this.startPhase;
        this.x0 = f2;
        this.sx = f2;
        this.y0 = f3;
        this.sy = f3;
        this.starting = true;
    }

    private void emitSeg(float[] fArr, int i2, int i3) {
        switch (i3) {
            case 4:
                this.out.lineTo(fArr[i2], fArr[i2 + 1]);
                break;
            case 6:
                this.out.quadTo(fArr[i2 + 0], fArr[i2 + 1], fArr[i2 + 2], fArr[i2 + 3]);
                break;
            case 8:
                this.out.curveTo(fArr[i2 + 0], fArr[i2 + 1], fArr[i2 + 2], fArr[i2 + 3], fArr[i2 + 4], fArr[i2 + 5]);
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

    private void goTo(float[] fArr, int i2, int i3) {
        float f2 = fArr[(i2 + i3) - 4];
        float f3 = fArr[(i2 + i3) - 3];
        if (this.dashOn) {
            if (this.starting) {
                this.firstSegmentsBuffer = Helpers.widenArray(this.firstSegmentsBuffer, this.firstSegidx, (i3 - 2) + 1);
                float[] fArr2 = this.firstSegmentsBuffer;
                int i4 = this.firstSegidx;
                this.firstSegidx = i4 + 1;
                fArr2[i4] = i3;
                System.arraycopy(fArr, i2, this.firstSegmentsBuffer, this.firstSegidx, i3 - 2);
                this.firstSegidx += i3 - 2;
            } else {
                if (this.needsMoveTo) {
                    this.out.moveTo(this.x0, this.y0);
                    this.needsMoveTo = false;
                }
                emitSeg(fArr, i2, i3);
            }
        } else {
            this.starting = false;
            this.needsMoveTo = true;
        }
        this.x0 = f2;
        this.y0 = f3;
    }

    @Override // sun.awt.geom.PathConsumer2D
    public void lineTo(float f2, float f3) {
        float f4;
        float f5 = f2 - this.x0;
        float f6 = f3 - this.y0;
        float fSqrt = (float) Math.sqrt((f5 * f5) + (f6 * f6));
        if (fSqrt == 0.0f) {
            return;
        }
        float f7 = f5 / fSqrt;
        float f8 = f6 / fSqrt;
        while (true) {
            f4 = this.dash[this.idx] - this.phase;
            if (fSqrt <= f4) {
                break;
            }
            float f9 = this.dash[this.idx] * f7;
            float f10 = this.dash[this.idx] * f8;
            if (this.phase == 0.0f) {
                this.curCurvepts[0] = this.x0 + f9;
                this.curCurvepts[1] = this.y0 + f10;
            } else {
                float f11 = f4 / this.dash[this.idx];
                this.curCurvepts[0] = this.x0 + (f11 * f9);
                this.curCurvepts[1] = this.y0 + (f11 * f10);
            }
            goTo(this.curCurvepts, 0, 4);
            fSqrt -= f4;
            this.idx = (this.idx + 1) % this.dash.length;
            this.dashOn = !this.dashOn;
            this.phase = 0.0f;
        }
        this.curCurvepts[0] = f2;
        this.curCurvepts[1] = f3;
        goTo(this.curCurvepts, 0, 4);
        this.phase += fSqrt;
        if (fSqrt == f4) {
            this.phase = 0.0f;
            this.idx = (this.idx + 1) % this.dash.length;
            this.dashOn = !this.dashOn;
        }
    }

    private void somethingTo(int i2) {
        if (pointCurve(this.curCurvepts, i2)) {
            return;
        }
        if (this.li == null) {
            this.li = new LengthIterator(4, 0.01f);
        }
        this.li.initializeIterationOnCurve(this.curCurvepts, i2);
        int i3 = 0;
        float f2 = 0.0f;
        float f3 = this.dash[this.idx] - this.phase;
        while (true) {
            float next = this.li.next(f3);
            if (next >= 1.0f) {
                break;
            }
            if (next != 0.0f) {
                Helpers.subdivideAt((next - f2) / (1.0f - f2), this.curCurvepts, i3, this.curCurvepts, 0, this.curCurvepts, i2, i2);
                f2 = next;
                goTo(this.curCurvepts, 2, i2);
                i3 = i2;
            }
            this.idx = (this.idx + 1) % this.dash.length;
            this.dashOn = !this.dashOn;
            this.phase = 0.0f;
            f3 = this.dash[this.idx];
        }
        goTo(this.curCurvepts, i3 + 2, i2);
        this.phase += this.li.lastSegLen();
        if (this.phase >= this.dash[this.idx]) {
            this.phase = 0.0f;
            this.idx = (this.idx + 1) % this.dash.length;
            this.dashOn = !this.dashOn;
        }
    }

    private static boolean pointCurve(float[] fArr, int i2) {
        for (int i3 = 2; i3 < i2; i3++) {
            if (fArr[i3] != fArr[i3 - 2]) {
                return false;
            }
        }
        return true;
    }

    /* loaded from: rt.jar:sun/java2d/pisces/Dasher$LengthIterator.class */
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

        /* loaded from: rt.jar:sun/java2d/pisces/Dasher$LengthIterator$Side.class */
        private enum Side {
            LEFT,
            RIGHT
        }

        public LengthIterator(int i2, float f2) {
            this.limit = i2;
            this.minTincrement = 1.0f / (1 << this.limit);
            this.ERR = f2;
            this.recCurveStack = new float[i2 + 1][8];
            this.sides = new Side[i2];
        }

        public void initializeIterationOnCurve(float[] fArr, int i2) {
            System.arraycopy(fArr, 0, this.recCurveStack[0], 0, i2);
            this.curveType = i2;
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

        private boolean haveLowAcceleration(float f2) {
            if (this.cachedHaveLowAcceleration != -1) {
                return this.cachedHaveLowAcceleration == 1;
            }
            float f3 = this.curLeafCtrlPolyLengths[0];
            float f4 = this.curLeafCtrlPolyLengths[1];
            if (!Helpers.within(f3, f4, f2 * f4)) {
                this.cachedHaveLowAcceleration = 0;
                return false;
            }
            if (this.curveType == 8) {
                float f5 = this.curLeafCtrlPolyLengths[2];
                if (!Helpers.within(f4, f5, f2 * f5) || !Helpers.within(f3, f5, f2 * f5)) {
                    this.cachedHaveLowAcceleration = 0;
                    return false;
                }
            }
            this.cachedHaveLowAcceleration = 1;
            return true;
        }

        public float next(float f2) {
            float f3 = this.lenAtLastSplit + f2;
            while (this.lenAtNextT < f3) {
                if (this.done) {
                    this.lastSegLen = this.lenAtNextT - this.lenAtLastSplit;
                    return 1.0f;
                }
                goToNextLeaf();
            }
            this.lenAtLastSplit = f3;
            float f4 = (f3 - this.lenAtLastT) / (this.lenAtNextT - this.lenAtLastT);
            if (!haveLowAcceleration(0.05f)) {
                if (this.flatLeafCoefCache[2] < 0.0f) {
                    float f5 = 0.0f + this.curLeafCtrlPolyLengths[0];
                    float f6 = f5 + this.curLeafCtrlPolyLengths[1];
                    if (this.curveType == 8) {
                        float f7 = f6 + this.curLeafCtrlPolyLengths[2];
                        this.flatLeafCoefCache[0] = (3.0f * (f5 - f6)) + f7;
                        this.flatLeafCoefCache[1] = 3.0f * (f6 - (2.0f * f5));
                        this.flatLeafCoefCache[2] = 3.0f * f5;
                        this.flatLeafCoefCache[3] = -f7;
                    } else if (this.curveType == 6) {
                        this.flatLeafCoefCache[0] = 0.0f;
                        this.flatLeafCoefCache[1] = f6 - (2.0f * f5);
                        this.flatLeafCoefCache[2] = 2.0f * f5;
                        this.flatLeafCoefCache[3] = -f6;
                    }
                }
                if (Helpers.cubicRootsInAB(this.flatLeafCoefCache[0], this.flatLeafCoefCache[1], this.flatLeafCoefCache[2], f4 * this.flatLeafCoefCache[3], this.nextRoots, 0, 0.0f, 1.0f) == 1 && !Float.isNaN(this.nextRoots[0])) {
                    f4 = this.nextRoots[0];
                }
            }
            float f8 = (f4 * (this.nextT - this.lastT)) + this.lastT;
            if (f8 >= 1.0f) {
                f8 = 1.0f;
                this.done = true;
            }
            this.lastSegLen = f2;
            return f8;
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
            float fOnLeaf = onLeaf();
            if (fOnLeaf >= 0.0f) {
                this.lastT = this.nextT;
                this.lenAtLastT = this.lenAtNextT;
                this.nextT += (1 << (this.limit - this.recLevel)) * this.minTincrement;
                this.lenAtNextT += fOnLeaf;
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
            float[] fArr = this.recCurveStack[this.recLevel];
            float f2 = 0.0f;
            float f3 = fArr[0];
            float f4 = fArr[1];
            for (int i2 = 2; i2 < this.curveType; i2 += 2) {
                float f5 = fArr[i2];
                float f6 = fArr[i2 + 1];
                float fLinelen = Helpers.linelen(f3, f4, f5, f6);
                f2 += fLinelen;
                this.curLeafCtrlPolyLengths[(i2 / 2) - 1] = fLinelen;
                f3 = f5;
                f4 = f6;
            }
            float fLinelen2 = Helpers.linelen(fArr[0], fArr[1], fArr[this.curveType - 2], fArr[this.curveType - 1]);
            if (f2 - fLinelen2 < this.ERR || this.recLevel == this.limit) {
                return (f2 + fLinelen2) / 2.0f;
            }
            return -1.0f;
        }
    }

    @Override // sun.awt.geom.PathConsumer2D
    public void curveTo(float f2, float f3, float f4, float f5, float f6, float f7) {
        this.curCurvepts[0] = this.x0;
        this.curCurvepts[1] = this.y0;
        this.curCurvepts[2] = f2;
        this.curCurvepts[3] = f3;
        this.curCurvepts[4] = f4;
        this.curCurvepts[5] = f5;
        this.curCurvepts[6] = f6;
        this.curCurvepts[7] = f7;
        somethingTo(8);
    }

    @Override // sun.awt.geom.PathConsumer2D
    public void quadTo(float f2, float f3, float f4, float f5) {
        this.curCurvepts[0] = this.x0;
        this.curCurvepts[1] = this.y0;
        this.curCurvepts[2] = f2;
        this.curCurvepts[3] = f3;
        this.curCurvepts[4] = f4;
        this.curCurvepts[5] = f5;
        somethingTo(6);
    }

    @Override // sun.awt.geom.PathConsumer2D
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

    @Override // sun.awt.geom.PathConsumer2D
    public void pathDone() {
        if (this.firstSegidx > 0) {
            this.out.moveTo(this.sx, this.sy);
            emitFirstSegments();
        }
        this.out.pathDone();
    }

    @Override // sun.awt.geom.PathConsumer2D
    public long getNativeConsumer() {
        throw new InternalError("Dasher does not use a native consumer");
    }
}
