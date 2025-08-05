package com.sun.javafx.geom;

import com.sun.javafx.geom.transform.BaseTransform;

/* loaded from: jfxrt.jar:com/sun/javafx/geom/QuadCurve2D.class */
public class QuadCurve2D extends Shape {
    public float x1;
    public float y1;
    public float ctrlx;
    public float ctrly;
    public float x2;
    public float y2;
    private static final int BELOW = -2;
    private static final int LOWEDGE = -1;
    private static final int INSIDE = 0;
    private static final int HIGHEDGE = 1;
    private static final int ABOVE = 2;

    public QuadCurve2D() {
    }

    public QuadCurve2D(float x1, float y1, float ctrlx, float ctrly, float x2, float y2) {
        setCurve(x1, y1, ctrlx, ctrly, x2, y2);
    }

    public void setCurve(float x1, float y1, float ctrlx, float ctrly, float x2, float y2) {
        this.x1 = x1;
        this.y1 = y1;
        this.ctrlx = ctrlx;
        this.ctrly = ctrly;
        this.x2 = x2;
        this.y2 = y2;
    }

    @Override // com.sun.javafx.geom.Shape
    public RectBounds getBounds() {
        float left = Math.min(Math.min(this.x1, this.x2), this.ctrlx);
        float top = Math.min(Math.min(this.y1, this.y2), this.ctrly);
        float right = Math.max(Math.max(this.x1, this.x2), this.ctrlx);
        float bottom = Math.max(Math.max(this.y1, this.y2), this.ctrly);
        return new RectBounds(left, top, right, bottom);
    }

    public CubicCurve2D toCubic() {
        return new CubicCurve2D(this.x1, this.y1, (this.x1 + (2.0f * this.ctrlx)) / 3.0f, (this.y1 + (2.0f * this.ctrly)) / 3.0f, ((2.0f * this.ctrlx) + this.x2) / 3.0f, ((2.0f * this.ctrly) + this.y2) / 3.0f, this.x2, this.y2);
    }

    public void setCurve(float[] coords, int offset) {
        setCurve(coords[offset + 0], coords[offset + 1], coords[offset + 2], coords[offset + 3], coords[offset + 4], coords[offset + 5]);
    }

    public void setCurve(Point2D p1, Point2D cp, Point2D p2) {
        setCurve(p1.f11907x, p1.f11908y, cp.f11907x, cp.f11908y, p2.f11907x, p2.f11908y);
    }

    public void setCurve(Point2D[] pts, int offset) {
        setCurve(pts[offset + 0].f11907x, pts[offset + 0].f11908y, pts[offset + 1].f11907x, pts[offset + 1].f11908y, pts[offset + 2].f11907x, pts[offset + 2].f11908y);
    }

    public void setCurve(QuadCurve2D c2) {
        setCurve(c2.x1, c2.y1, c2.ctrlx, c2.ctrly, c2.x2, c2.y2);
    }

    public static float getFlatnessSq(float x1, float y1, float ctrlx, float ctrly, float x2, float y2) {
        return Line2D.ptSegDistSq(x1, y1, x2, y2, ctrlx, ctrly);
    }

    public static float getFlatness(float x1, float y1, float ctrlx, float ctrly, float x2, float y2) {
        return Line2D.ptSegDist(x1, y1, x2, y2, ctrlx, ctrly);
    }

    public static float getFlatnessSq(float[] coords, int offset) {
        return Line2D.ptSegDistSq(coords[offset + 0], coords[offset + 1], coords[offset + 4], coords[offset + 5], coords[offset + 2], coords[offset + 3]);
    }

    public static float getFlatness(float[] coords, int offset) {
        return Line2D.ptSegDist(coords[offset + 0], coords[offset + 1], coords[offset + 4], coords[offset + 5], coords[offset + 2], coords[offset + 3]);
    }

    public float getFlatnessSq() {
        return Line2D.ptSegDistSq(this.x1, this.y1, this.x2, this.y2, this.ctrlx, this.ctrly);
    }

    public float getFlatness() {
        return Line2D.ptSegDist(this.x1, this.y1, this.x2, this.y2, this.ctrlx, this.ctrly);
    }

    public void subdivide(QuadCurve2D left, QuadCurve2D right) {
        subdivide(this, left, right);
    }

    public static void subdivide(QuadCurve2D src, QuadCurve2D left, QuadCurve2D right) {
        float x1 = src.x1;
        float y1 = src.y1;
        float ctrlx = src.ctrlx;
        float ctrly = src.ctrly;
        float x2 = src.x2;
        float y2 = src.y2;
        float ctrlx1 = (x1 + ctrlx) / 2.0f;
        float ctrly1 = (y1 + ctrly) / 2.0f;
        float ctrlx2 = (x2 + ctrlx) / 2.0f;
        float ctrly2 = (y2 + ctrly) / 2.0f;
        float ctrlx3 = (ctrlx1 + ctrlx2) / 2.0f;
        float ctrly3 = (ctrly1 + ctrly2) / 2.0f;
        if (left != null) {
            left.setCurve(x1, y1, ctrlx1, ctrly1, ctrlx3, ctrly3);
        }
        if (right != null) {
            right.setCurve(ctrlx3, ctrly3, ctrlx2, ctrly2, x2, y2);
        }
    }

    public static void subdivide(float[] src, int srcoff, float[] left, int leftoff, float[] right, int rightoff) {
        float x1 = src[srcoff + 0];
        float y1 = src[srcoff + 1];
        float ctrlx = src[srcoff + 2];
        float ctrly = src[srcoff + 3];
        float x2 = src[srcoff + 4];
        float y2 = src[srcoff + 5];
        if (left != null) {
            left[leftoff + 0] = x1;
            left[leftoff + 1] = y1;
        }
        if (right != null) {
            right[rightoff + 4] = x2;
            right[rightoff + 5] = y2;
        }
        float x12 = (x1 + ctrlx) / 2.0f;
        float y12 = (y1 + ctrly) / 2.0f;
        float x22 = (x2 + ctrlx) / 2.0f;
        float y22 = (y2 + ctrly) / 2.0f;
        float ctrlx2 = (x12 + x22) / 2.0f;
        float ctrly2 = (y12 + y22) / 2.0f;
        if (left != null) {
            left[leftoff + 2] = x12;
            left[leftoff + 3] = y12;
            left[leftoff + 4] = ctrlx2;
            left[leftoff + 5] = ctrly2;
        }
        if (right != null) {
            right[rightoff + 0] = ctrlx2;
            right[rightoff + 1] = ctrly2;
            right[rightoff + 2] = x22;
            right[rightoff + 3] = y22;
        }
    }

    public static int solveQuadratic(float[] eqn) {
        return solveQuadratic(eqn, eqn);
    }

    public static int solveQuadratic(float[] eqn, float[] res) {
        int roots;
        float a2 = eqn[2];
        float b2 = eqn[1];
        float c2 = eqn[0];
        if (a2 != 0.0f) {
            float d2 = (b2 * b2) - ((4.0f * a2) * c2);
            if (d2 < 0.0f) {
                return 0;
            }
            float d3 = (float) Math.sqrt(d2);
            if (b2 < 0.0f) {
                d3 = -d3;
            }
            float q2 = (b2 + d3) / (-2.0f);
            roots = 0 + 1;
            res[0] = q2 / a2;
            if (q2 != 0.0f) {
                roots++;
                res[roots] = c2 / q2;
            }
        } else if (b2 != 0.0f) {
            roots = 0 + 1;
            res[0] = (-c2) / b2;
        } else {
            return -1;
        }
        return roots;
    }

    @Override // com.sun.javafx.geom.Shape
    public boolean contains(float x2, float y2) {
        float x1 = this.x1;
        float y1 = this.y1;
        float xc = this.ctrlx;
        float yc = this.ctrly;
        float x22 = this.x2;
        float y22 = this.y2;
        float kx = (x1 - (2.0f * xc)) + x22;
        float ky = (y1 - (2.0f * yc)) + y22;
        float dx = x2 - x1;
        float dy = y2 - y1;
        float dxl = x22 - x1;
        float dyl = y22 - y1;
        float t0 = ((dx * ky) - (dy * kx)) / ((dxl * ky) - (dyl * kx));
        if (t0 < 0.0f || t0 > 1.0f || t0 != t0) {
            return false;
        }
        float xb = (kx * t0 * t0) + (2.0f * (xc - x1) * t0) + x1;
        float yb = (ky * t0 * t0) + (2.0f * (yc - y1) * t0) + y1;
        float xl = (dxl * t0) + x1;
        float yl = (dyl * t0) + y1;
        return (x2 >= xb && x2 < xl) || (x2 >= xl && x2 < xb) || ((y2 >= yb && y2 < yl) || (y2 >= yl && y2 < yb));
    }

    @Override // com.sun.javafx.geom.Shape
    public boolean contains(Point2D p2) {
        return contains(p2.f11907x, p2.f11908y);
    }

    private static void fillEqn(float[] eqn, float val, float c1, float cp, float c2) {
        eqn[0] = c1 - val;
        eqn[1] = ((cp + cp) - c1) - c1;
        eqn[2] = ((c1 - cp) - cp) + c2;
    }

    /* JADX WARN: Removed duplicated region for block: B:12:0x0027  */
    /* JADX WARN: Removed duplicated region for block: B:19:0x003c  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private static int evalQuadratic(float[] r6, int r7, boolean r8, boolean r9, float[] r10, float r11, float r12, float r13) {
        /*
            r0 = 0
            r14 = r0
            r0 = 0
            r15 = r0
        L6:
            r0 = r15
            r1 = r7
            if (r0 >= r1) goto L83
            r0 = r6
            r1 = r15
            r0 = r0[r1]
            r16 = r0
            r0 = r8
            if (r0 == 0) goto L20
            r0 = r16
            r1 = 0
            int r0 = (r0 > r1 ? 1 : (r0 == r1 ? 0 : -1))
            if (r0 < 0) goto L7d
            goto L27
        L20:
            r0 = r16
            r1 = 0
            int r0 = (r0 > r1 ? 1 : (r0 == r1 ? 0 : -1))
            if (r0 <= 0) goto L7d
        L27:
            r0 = r9
            if (r0 == 0) goto L35
            r0 = r16
            r1 = 1065353216(0x3f800000, float:1.0)
            int r0 = (r0 > r1 ? 1 : (r0 == r1 ? 0 : -1))
            if (r0 > 0) goto L7d
            goto L3c
        L35:
            r0 = r16
            r1 = 1065353216(0x3f800000, float:1.0)
            int r0 = (r0 > r1 ? 1 : (r0 == r1 ? 0 : -1))
            if (r0 >= 0) goto L7d
        L3c:
            r0 = r10
            if (r0 == 0) goto L54
            r0 = r10
            r1 = 1
            r0 = r0[r1]
            r1 = 1073741824(0x40000000, float:2.0)
            r2 = r10
            r3 = 2
            r2 = r2[r3]
            float r1 = r1 * r2
            r2 = r16
            float r1 = r1 * r2
            float r0 = r0 + r1
            r1 = 0
            int r0 = (r0 > r1 ? 1 : (r0 == r1 ? 0 : -1))
            if (r0 == 0) goto L7d
        L54:
            r0 = 1065353216(0x3f800000, float:1.0)
            r1 = r16
            float r0 = r0 - r1
            r17 = r0
            r0 = r6
            r1 = r14
            int r14 = r14 + 1
            r2 = r11
            r3 = r17
            float r2 = r2 * r3
            r3 = r17
            float r2 = r2 * r3
            r3 = 1073741824(0x40000000, float:2.0)
            r4 = r12
            float r3 = r3 * r4
            r4 = r16
            float r3 = r3 * r4
            r4 = r17
            float r3 = r3 * r4
            float r2 = r2 + r3
            r3 = r13
            r4 = r16
            float r3 = r3 * r4
            r4 = r16
            float r3 = r3 * r4
            float r2 = r2 + r3
            r0[r1] = r2
        L7d:
            int r15 = r15 + 1
            goto L6
        L83:
            r0 = r14
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.javafx.geom.QuadCurve2D.evalQuadratic(float[], int, boolean, boolean, float[], float, float, float):int");
    }

    private static int getTag(float coord, float low, float high) {
        if (coord <= low) {
            return coord < low ? -2 : -1;
        }
        if (coord >= high) {
            return coord > high ? 2 : 1;
        }
        return 0;
    }

    private static boolean inwards(int pttag, int opt1tag, int opt2tag) {
        switch (pttag) {
            case -2:
            case 2:
            default:
                return false;
            case -1:
                return opt1tag >= 0 || opt2tag >= 0;
            case 0:
                return true;
            case 1:
                return opt1tag <= 0 || opt2tag <= 0;
        }
    }

    @Override // com.sun.javafx.geom.Shape
    public boolean intersects(float x2, float y2, float w2, float h2) {
        int c1tag;
        int c2tag;
        if (w2 <= 0.0f || h2 <= 0.0f) {
            return false;
        }
        float x1 = this.x1;
        float y1 = this.y1;
        int x1tag = getTag(x1, x2, x2 + w2);
        int y1tag = getTag(y1, y2, y2 + h2);
        if (x1tag == 0 && y1tag == 0) {
            return true;
        }
        float x22 = this.x2;
        float y22 = this.y2;
        int x2tag = getTag(x22, x2, x2 + w2);
        int y2tag = getTag(y22, y2, y2 + h2);
        if (x2tag == 0 && y2tag == 0) {
            return true;
        }
        float ctrlx = this.ctrlx;
        float ctrly = this.ctrly;
        int ctrlxtag = getTag(ctrlx, x2, x2 + w2);
        int ctrlytag = getTag(ctrly, y2, y2 + h2);
        if (x1tag < 0 && x2tag < 0 && ctrlxtag < 0) {
            return false;
        }
        if (y1tag < 0 && y2tag < 0 && ctrlytag < 0) {
            return false;
        }
        if (x1tag > 0 && x2tag > 0 && ctrlxtag > 0) {
            return false;
        }
        if (y1tag > 0 && y2tag > 0 && ctrlytag > 0) {
            return false;
        }
        if (inwards(x1tag, x2tag, ctrlxtag) && inwards(y1tag, y2tag, ctrlytag)) {
            return true;
        }
        if (inwards(x2tag, x1tag, ctrlxtag) && inwards(y2tag, y1tag, ctrlytag)) {
            return true;
        }
        boolean xoverlap = x1tag * x2tag <= 0;
        boolean yoverlap = y1tag * y2tag <= 0;
        if (x1tag == 0 && x2tag == 0 && yoverlap) {
            return true;
        }
        if (y1tag == 0 && y2tag == 0 && xoverlap) {
            return true;
        }
        float[] eqn = new float[3];
        float[] res = new float[3];
        if (!yoverlap) {
            fillEqn(eqn, y1tag < 0 ? y2 : y2 + h2, y1, ctrly, y22);
            return solveQuadratic(eqn, res) == 2 && evalQuadratic(res, 2, true, true, null, x1, ctrlx, x22) == 2 && getTag(res[0], x2, x2 + w2) * getTag(res[1], x2, x2 + w2) <= 0;
        }
        if (!xoverlap) {
            fillEqn(eqn, x1tag < 0 ? x2 : x2 + w2, x1, ctrlx, x22);
            return solveQuadratic(eqn, res) == 2 && evalQuadratic(res, 2, true, true, null, y1, ctrly, y22) == 2 && getTag(res[0], y2, y2 + h2) * getTag(res[1], y2, y2 + h2) <= 0;
        }
        float dx = x22 - x1;
        float dy = y22 - y1;
        float k2 = (y22 * x1) - (x22 * y1);
        if (y1tag == 0) {
            c1tag = x1tag;
        } else {
            c1tag = getTag((k2 + (dx * (y1tag < 0 ? y2 : y2 + h2))) / dy, x2, x2 + w2);
        }
        if (y2tag == 0) {
            c2tag = x2tag;
        } else {
            c2tag = getTag((k2 + (dx * (y2tag < 0 ? y2 : y2 + h2))) / dy, x2, x2 + w2);
        }
        if (c1tag * c2tag <= 0) {
            return true;
        }
        int c1tag2 = c1tag * x1tag <= 0 ? y1tag : y2tag;
        fillEqn(eqn, c2tag < 0 ? x2 : x2 + w2, x1, ctrlx, x22);
        int num = solveQuadratic(eqn, res);
        evalQuadratic(res, num, true, true, null, y1, ctrly, y22);
        int c2tag2 = getTag(res[0], y2, y2 + h2);
        return c1tag2 * c2tag2 <= 0;
    }

    @Override // com.sun.javafx.geom.Shape
    public boolean contains(float x2, float y2, float w2, float h2) {
        return w2 > 0.0f && h2 > 0.0f && contains(x2, y2) && contains(x2 + w2, y2) && contains(x2 + w2, y2 + h2) && contains(x2, y2 + h2);
    }

    @Override // com.sun.javafx.geom.Shape
    public PathIterator getPathIterator(BaseTransform tx) {
        return new QuadIterator(this, tx);
    }

    @Override // com.sun.javafx.geom.Shape
    public PathIterator getPathIterator(BaseTransform tx, float flatness) {
        return new FlatteningPathIterator(getPathIterator(tx), flatness);
    }

    @Override // com.sun.javafx.geom.Shape
    public QuadCurve2D copy() {
        return new QuadCurve2D(this.x1, this.y1, this.ctrlx, this.ctrly, this.x2, this.y2);
    }

    public int hashCode() {
        int bits = Float.floatToIntBits(this.x1);
        return bits + (Float.floatToIntBits(this.y1) * 37) + (Float.floatToIntBits(this.x2) * 43) + (Float.floatToIntBits(this.y2) * 47) + (Float.floatToIntBits(this.ctrlx) * 53) + (Float.floatToIntBits(this.ctrly) * 59);
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof QuadCurve2D) {
            QuadCurve2D curve = (QuadCurve2D) obj;
            return this.x1 == curve.x1 && this.y1 == curve.y1 && this.x2 == curve.x2 && this.y2 == curve.y2 && this.ctrlx == curve.ctrlx && this.ctrly == curve.ctrly;
        }
        return false;
    }
}
