package com.sun.openpisces;

import java.util.Iterator;

/* loaded from: jfxrt.jar:com/sun/openpisces/Curve.class */
final class Curve {

    /* renamed from: ax, reason: collision with root package name */
    float f11989ax;

    /* renamed from: ay, reason: collision with root package name */
    float f11990ay;

    /* renamed from: bx, reason: collision with root package name */
    float f11991bx;

    /* renamed from: by, reason: collision with root package name */
    float f11992by;
    float cx;
    float cy;
    float dx;
    float dy;
    float dax;
    float day;
    float dbx;
    float dby;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !Curve.class.desiredAssertionStatus();
    }

    Curve() {
    }

    void set(float[] points, int type) {
        switch (type) {
            case 6:
                set(points[0], points[1], points[2], points[3], points[4], points[5]);
                return;
            case 8:
                set(points[0], points[1], points[2], points[3], points[4], points[5], points[6], points[7]);
                return;
            default:
                throw new InternalError("Curves can only be cubic or quadratic");
        }
    }

    void set(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4) {
        this.f11989ax = ((3.0f * (x2 - x3)) + x4) - x1;
        this.f11990ay = ((3.0f * (y2 - y3)) + y4) - y1;
        this.f11991bx = 3.0f * ((x1 - (2.0f * x2)) + x3);
        this.f11992by = 3.0f * ((y1 - (2.0f * y2)) + y3);
        this.cx = 3.0f * (x2 - x1);
        this.cy = 3.0f * (y2 - y1);
        this.dx = x1;
        this.dy = y1;
        this.dax = 3.0f * this.f11989ax;
        this.day = 3.0f * this.f11990ay;
        this.dbx = 2.0f * this.f11991bx;
        this.dby = 2.0f * this.f11992by;
    }

    void set(float x1, float y1, float x2, float y2, float x3, float y3) {
        this.f11990ay = 0.0f;
        this.f11989ax = 0.0f;
        this.f11991bx = (x1 - (2.0f * x2)) + x3;
        this.f11992by = (y1 - (2.0f * y2)) + y3;
        this.cx = 2.0f * (x2 - x1);
        this.cy = 2.0f * (y2 - y1);
        this.dx = x1;
        this.dy = y1;
        this.dax = 0.0f;
        this.day = 0.0f;
        this.dbx = 2.0f * this.f11991bx;
        this.dby = 2.0f * this.f11992by;
    }

    float xat(float t2) {
        return (t2 * ((t2 * ((t2 * this.f11989ax) + this.f11991bx)) + this.cx)) + this.dx;
    }

    float yat(float t2) {
        return (t2 * ((t2 * ((t2 * this.f11990ay) + this.f11992by)) + this.cy)) + this.dy;
    }

    float dxat(float t2) {
        return (t2 * ((t2 * this.dax) + this.dbx)) + this.cx;
    }

    float dyat(float t2) {
        return (t2 * ((t2 * this.day) + this.dby)) + this.cy;
    }

    int dxRoots(float[] roots, int off) {
        return Helpers.quadraticRoots(this.dax, this.dbx, this.cx, roots, off);
    }

    int dyRoots(float[] roots, int off) {
        return Helpers.quadraticRoots(this.day, this.dby, this.cy, roots, off);
    }

    int infPoints(float[] pts, int off) {
        float a2 = (this.dax * this.dby) - (this.dbx * this.day);
        float b2 = 2.0f * ((this.cy * this.dax) - (this.day * this.cx));
        float c2 = (this.cy * this.dbx) - (this.cx * this.dby);
        return Helpers.quadraticRoots(a2, b2, c2, pts, off);
    }

    private int perpendiculardfddf(float[] pts, int off) {
        if (!$assertionsDisabled && pts.length < off + 4) {
            throw new AssertionError();
        }
        float a2 = 2.0f * ((this.dax * this.dax) + (this.day * this.day));
        float b2 = 3.0f * ((this.dax * this.dbx) + (this.day * this.dby));
        float c2 = (2.0f * ((this.dax * this.cx) + (this.day * this.cy))) + (this.dbx * this.dbx) + (this.dby * this.dby);
        float d2 = (this.dbx * this.cx) + (this.dby * this.cy);
        return Helpers.cubicRootsInAB(a2, b2, c2, d2, pts, off, 0.0f, 1.0f);
    }

    int rootsOfROCMinusW(float[] roots, int off, float w2, float err) {
        if (!$assertionsDisabled && (off > 6 || roots.length < 10)) {
            throw new AssertionError();
        }
        int ret = off;
        int numPerpdfddf = perpendiculardfddf(roots, off);
        float t0 = 0.0f;
        float ft0 = ROCsq(0.0f) - (w2 * w2);
        roots[off + numPerpdfddf] = 1.0f;
        int numPerpdfddf2 = numPerpdfddf + 1;
        for (int i2 = off; i2 < off + numPerpdfddf2; i2++) {
            float t1 = roots[i2];
            float ft1 = ROCsq(t1) - (w2 * w2);
            if (ft0 == 0.0f) {
                int i3 = ret;
                ret++;
                roots[i3] = t0;
            } else if (ft1 * ft0 < 0.0f) {
                int i4 = ret;
                ret++;
                roots[i4] = falsePositionROCsqMinusX(t0, t1, w2 * w2, err);
            }
            t0 = t1;
            ft0 = ft1;
        }
        return ret - off;
    }

    private static float eliminateInf(float x2) {
        if (x2 == Float.POSITIVE_INFINITY) {
            return Float.MAX_VALUE;
        }
        if (x2 == Float.NEGATIVE_INFINITY) {
            return Float.MIN_VALUE;
        }
        return x2;
    }

    private float falsePositionROCsqMinusX(float x0, float x1, float x2, float err) {
        int side = 0;
        float t2 = x1;
        float ft = eliminateInf(ROCsq(t2) - x2);
        float s2 = x0;
        float fs = eliminateInf(ROCsq(s2) - x2);
        float r2 = s2;
        for (int i2 = 0; i2 < 100 && Math.abs(t2 - s2) > err * Math.abs(t2 + s2); i2++) {
            r2 = ((fs * t2) - (ft * s2)) / (fs - ft);
            float fr = ROCsq(r2) - x2;
            if (sameSign(fr, ft)) {
                ft = fr;
                t2 = r2;
                if (side < 0) {
                    fs /= 1 << (-side);
                    side--;
                } else {
                    side = -1;
                }
            } else {
                if (fr * fs <= 0.0f) {
                    break;
                }
                fs = fr;
                s2 = r2;
                if (side > 0) {
                    ft /= 1 << side;
                    side++;
                } else {
                    side = 1;
                }
            }
        }
        return r2;
    }

    private static boolean sameSign(double x2, double y2) {
        return (x2 < 0.0d && y2 < 0.0d) || (x2 > 0.0d && y2 > 0.0d);
    }

    private float ROCsq(float t2) {
        float dx = (t2 * ((t2 * this.dax) + this.dbx)) + this.cx;
        float dy = (t2 * ((t2 * this.day) + this.dby)) + this.cy;
        float ddx = (2.0f * this.dax * t2) + this.dbx;
        float ddy = (2.0f * this.day * t2) + this.dby;
        float dx2dy2 = (dx * dx) + (dy * dy);
        float ddx2ddy2 = (ddx * ddx) + (ddy * ddy);
        float ddxdxddydy = (ddx * dx) + (ddy * dy);
        return dx2dy2 * ((dx2dy2 * dx2dy2) / ((dx2dy2 * ddx2ddy2) - (ddxdxddydy * ddxdxddydy)));
    }

    static Iterator<Integer> breakPtsAtTs(final float[] pts, final int type, final float[] Ts, final int numTs) {
        if ($assertionsDisabled || (pts.length >= 2 * type && numTs <= Ts.length)) {
            return new Iterator<Integer>() { // from class: com.sun.openpisces.Curve.1
                final Integer itype;
                final Integer i0 = 0;
                int nextCurveIdx = 0;
                Integer curCurveOff = this.i0;
                float prevT = 0.0f;

                {
                    this.itype = Integer.valueOf(type);
                }

                @Override // java.util.Iterator
                public boolean hasNext() {
                    return this.nextCurveIdx < numTs + 1;
                }

                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.util.Iterator
                public Integer next() {
                    Integer ret;
                    if (this.nextCurveIdx < numTs) {
                        float curT = Ts[this.nextCurveIdx];
                        float splitT = (curT - this.prevT) / (1.0f - this.prevT);
                        Helpers.subdivideAt(splitT, pts, this.curCurveOff.intValue(), pts, 0, pts, type, type);
                        this.prevT = curT;
                        ret = this.i0;
                        this.curCurveOff = this.itype;
                    } else {
                        ret = this.curCurveOff;
                    }
                    this.nextCurveIdx++;
                    return ret;
                }

                @Override // java.util.Iterator
                public void remove() {
                }
            };
        }
        throw new AssertionError();
    }
}
