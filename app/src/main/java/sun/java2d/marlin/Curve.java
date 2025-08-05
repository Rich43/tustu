package sun.java2d.marlin;

/* loaded from: rt.jar:sun/java2d/marlin/Curve.class */
final class Curve {

    /* renamed from: ax, reason: collision with root package name */
    float f13562ax;

    /* renamed from: ay, reason: collision with root package name */
    float f13563ay;

    /* renamed from: bx, reason: collision with root package name */
    float f13564bx;

    /* renamed from: by, reason: collision with root package name */
    float f13565by;
    float cx;
    float cy;
    float dx;
    float dy;
    float dax;
    float day;
    float dbx;
    float dby;
    private final BreakPtrIterator iterator = new BreakPtrIterator();
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !Curve.class.desiredAssertionStatus();
    }

    Curve() {
    }

    void set(float[] fArr, int i2) {
        switch (i2) {
            case 6:
                set(fArr[0], fArr[1], fArr[2], fArr[3], fArr[4], fArr[5]);
                return;
            case 8:
                set(fArr[0], fArr[1], fArr[2], fArr[3], fArr[4], fArr[5], fArr[6], fArr[7]);
                return;
            default:
                throw new InternalError("Curves can only be cubic or quadratic");
        }
    }

    void set(float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9) {
        this.f13562ax = ((3.0f * (f4 - f6)) + f8) - f2;
        this.f13563ay = ((3.0f * (f5 - f7)) + f9) - f3;
        this.f13564bx = 3.0f * ((f2 - (2.0f * f4)) + f6);
        this.f13565by = 3.0f * ((f3 - (2.0f * f5)) + f7);
        this.cx = 3.0f * (f4 - f2);
        this.cy = 3.0f * (f5 - f3);
        this.dx = f2;
        this.dy = f3;
        this.dax = 3.0f * this.f13562ax;
        this.day = 3.0f * this.f13563ay;
        this.dbx = 2.0f * this.f13564bx;
        this.dby = 2.0f * this.f13565by;
    }

    void set(float f2, float f3, float f4, float f5, float f6, float f7) {
        this.f13562ax = 0.0f;
        this.f13563ay = 0.0f;
        this.f13564bx = (f2 - (2.0f * f4)) + f6;
        this.f13565by = (f3 - (2.0f * f5)) + f7;
        this.cx = 2.0f * (f4 - f2);
        this.cy = 2.0f * (f5 - f3);
        this.dx = f2;
        this.dy = f3;
        this.dax = 0.0f;
        this.day = 0.0f;
        this.dbx = 2.0f * this.f13564bx;
        this.dby = 2.0f * this.f13565by;
    }

    float xat(float f2) {
        return (f2 * ((f2 * ((f2 * this.f13562ax) + this.f13564bx)) + this.cx)) + this.dx;
    }

    float yat(float f2) {
        return (f2 * ((f2 * ((f2 * this.f13563ay) + this.f13565by)) + this.cy)) + this.dy;
    }

    float dxat(float f2) {
        return (f2 * ((f2 * this.dax) + this.dbx)) + this.cx;
    }

    float dyat(float f2) {
        return (f2 * ((f2 * this.day) + this.dby)) + this.cy;
    }

    int dxRoots(float[] fArr, int i2) {
        return Helpers.quadraticRoots(this.dax, this.dbx, this.cx, fArr, i2);
    }

    int dyRoots(float[] fArr, int i2) {
        return Helpers.quadraticRoots(this.day, this.dby, this.cy, fArr, i2);
    }

    int infPoints(float[] fArr, int i2) {
        return Helpers.quadraticRoots((this.dax * this.dby) - (this.dbx * this.day), 2.0f * ((this.cy * this.dax) - (this.day * this.cx)), (this.cy * this.dbx) - (this.cx * this.dby), fArr, i2);
    }

    private int perpendiculardfddf(float[] fArr, int i2) {
        if ($assertionsDisabled || fArr.length >= i2 + 4) {
            return Helpers.cubicRootsInAB(2.0f * ((this.dax * this.dax) + (this.day * this.day)), 3.0f * ((this.dax * this.dbx) + (this.day * this.dby)), (2.0f * ((this.dax * this.cx) + (this.day * this.cy))) + (this.dbx * this.dbx) + (this.dby * this.dby), (this.dbx * this.cx) + (this.dby * this.cy), fArr, i2, 0.0f, 1.0f);
        }
        throw new AssertionError();
    }

    int rootsOfROCMinusW(float[] fArr, int i2, float f2, float f3) {
        if (!$assertionsDisabled && (i2 > 6 || fArr.length < 10)) {
            throw new AssertionError();
        }
        int i3 = i2;
        int iPerpendiculardfddf = perpendiculardfddf(fArr, i2);
        float f4 = 0.0f;
        float fROCsq = ROCsq(0.0f) - (f2 * f2);
        fArr[i2 + iPerpendiculardfddf] = 1.0f;
        int i4 = iPerpendiculardfddf + 1;
        for (int i5 = i2; i5 < i2 + i4; i5++) {
            float f5 = fArr[i5];
            float fROCsq2 = ROCsq(f5) - (f2 * f2);
            if (fROCsq == 0.0f) {
                int i6 = i3;
                i3++;
                fArr[i6] = f4;
            } else if (fROCsq2 * fROCsq < 0.0f) {
                int i7 = i3;
                i3++;
                fArr[i7] = falsePositionROCsqMinusX(f4, f5, f2 * f2, f3);
            }
            f4 = f5;
            fROCsq = fROCsq2;
        }
        return i3 - i2;
    }

    private static float eliminateInf(float f2) {
        if (f2 == Float.POSITIVE_INFINITY) {
            return Float.MAX_VALUE;
        }
        if (f2 == Float.NEGATIVE_INFINITY) {
            return Float.MIN_VALUE;
        }
        return f2;
    }

    private float falsePositionROCsqMinusX(float f2, float f3, float f4, float f5) {
        int i2 = 0;
        float f6 = f3;
        float fEliminateInf = eliminateInf(ROCsq(f6) - f4);
        float f7 = f2;
        float fEliminateInf2 = eliminateInf(ROCsq(f7) - f4);
        float f8 = f7;
        for (int i3 = 0; i3 < 100 && Math.abs(f6 - f7) > f5 * Math.abs(f6 + f7); i3++) {
            f8 = ((fEliminateInf2 * f6) - (fEliminateInf * f7)) / (fEliminateInf2 - fEliminateInf);
            float fROCsq = ROCsq(f8) - f4;
            if (sameSign(fROCsq, fEliminateInf)) {
                fEliminateInf = fROCsq;
                f6 = f8;
                if (i2 < 0) {
                    fEliminateInf2 /= 1 << (-i2);
                    i2--;
                } else {
                    i2 = -1;
                }
            } else {
                if (fROCsq * fEliminateInf2 <= 0.0f) {
                    break;
                }
                fEliminateInf2 = fROCsq;
                f7 = f8;
                if (i2 > 0) {
                    fEliminateInf /= 1 << i2;
                    i2++;
                } else {
                    i2 = 1;
                }
            }
        }
        return f8;
    }

    private static boolean sameSign(float f2, float f3) {
        return (f2 < 0.0f && f3 < 0.0f) || (f2 > 0.0f && f3 > 0.0f);
    }

    private float ROCsq(float f2) {
        float f3 = (f2 * ((f2 * this.dax) + this.dbx)) + this.cx;
        float f4 = (f2 * ((f2 * this.day) + this.dby)) + this.cy;
        float f5 = (2.0f * this.dax * f2) + this.dbx;
        float f6 = (2.0f * this.day * f2) + this.dby;
        float f7 = (f3 * f3) + (f4 * f4);
        float f8 = (f5 * f5) + (f6 * f6);
        float f9 = (f5 * f3) + (f6 * f4);
        return f7 * ((f7 * f7) / ((f7 * f8) - (f9 * f9)));
    }

    BreakPtrIterator breakPtsAtTs(float[] fArr, int i2, float[] fArr2, int i3) {
        if (!$assertionsDisabled && (fArr.length < 2 * i2 || i3 > fArr2.length)) {
            throw new AssertionError();
        }
        this.iterator.init(fArr, i2, fArr2, i3);
        return this.iterator;
    }

    /* loaded from: rt.jar:sun/java2d/marlin/Curve$BreakPtrIterator.class */
    static final class BreakPtrIterator {
        private int nextCurveIdx;
        private int curCurveOff;
        private float prevT;
        private float[] pts;
        private int type;
        private float[] ts;
        private int numTs;

        BreakPtrIterator() {
        }

        void init(float[] fArr, int i2, float[] fArr2, int i3) {
            this.pts = fArr;
            this.type = i2;
            this.ts = fArr2;
            this.numTs = i3;
            this.nextCurveIdx = 0;
            this.curCurveOff = 0;
            this.prevT = 0.0f;
        }

        public boolean hasNext() {
            return this.nextCurveIdx <= this.numTs;
        }

        public int next() {
            int i2;
            if (this.nextCurveIdx < this.numTs) {
                float f2 = this.ts[this.nextCurveIdx];
                Helpers.subdivideAt((f2 - this.prevT) / (1.0f - this.prevT), this.pts, this.curCurveOff, this.pts, 0, this.pts, this.type, this.type);
                this.prevT = f2;
                i2 = 0;
                this.curCurveOff = this.type;
            } else {
                i2 = this.curCurveOff;
            }
            this.nextCurveIdx++;
            return i2;
        }
    }
}
