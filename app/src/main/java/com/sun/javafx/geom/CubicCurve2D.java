package com.sun.javafx.geom;

import com.sun.javafx.geom.transform.BaseTransform;
import java.util.Arrays;

/* loaded from: jfxrt.jar:com/sun/javafx/geom/CubicCurve2D.class */
public class CubicCurve2D extends Shape {
    public float x1;
    public float y1;
    public float ctrlx1;
    public float ctrly1;
    public float ctrlx2;
    public float ctrly2;
    public float x2;
    public float y2;
    private static final int BELOW = -2;
    private static final int LOWEDGE = -1;
    private static final int INSIDE = 0;
    private static final int HIGHEDGE = 1;
    private static final int ABOVE = 2;

    public CubicCurve2D() {
    }

    public CubicCurve2D(float x1, float y1, float ctrlx1, float ctrly1, float ctrlx2, float ctrly2, float x2, float y2) {
        setCurve(x1, y1, ctrlx1, ctrly1, ctrlx2, ctrly2, x2, y2);
    }

    public void setCurve(float x1, float y1, float ctrlx1, float ctrly1, float ctrlx2, float ctrly2, float x2, float y2) {
        this.x1 = x1;
        this.y1 = y1;
        this.ctrlx1 = ctrlx1;
        this.ctrly1 = ctrly1;
        this.ctrlx2 = ctrlx2;
        this.ctrly2 = ctrly2;
        this.x2 = x2;
        this.y2 = y2;
    }

    @Override // com.sun.javafx.geom.Shape
    public RectBounds getBounds() {
        float left = Math.min(Math.min(this.x1, this.x2), Math.min(this.ctrlx1, this.ctrlx2));
        float top = Math.min(Math.min(this.y1, this.y2), Math.min(this.ctrly1, this.ctrly2));
        float right = Math.max(Math.max(this.x1, this.x2), Math.max(this.ctrlx1, this.ctrlx2));
        float bottom = Math.max(Math.max(this.y1, this.y2), Math.max(this.ctrly1, this.ctrly2));
        return new RectBounds(left, top, right, bottom);
    }

    public Point2D eval(float t2) {
        Point2D result = new Point2D();
        eval(t2, result);
        return result;
    }

    public void eval(float td, Point2D result) {
        result.setLocation(calcX(td), calcY(td));
    }

    public Point2D evalDt(float t2) {
        Point2D result = new Point2D();
        evalDt(t2, result);
        return result;
    }

    public void evalDt(float td, Point2D result) {
        float u2 = 1.0f - td;
        float x2 = 3.0f * (((this.ctrlx1 - this.x1) * u2 * u2) + (2.0f * (this.ctrlx2 - this.ctrlx1) * u2 * td) + ((this.x2 - this.ctrlx2) * td * td));
        float y2 = 3.0f * (((this.ctrly1 - this.y1) * u2 * u2) + (2.0f * (this.ctrly2 - this.ctrly1) * u2 * td) + ((this.y2 - this.ctrly2) * td * td));
        result.setLocation(x2, y2);
    }

    public void setCurve(float[] coords, int offset) {
        setCurve(coords[offset + 0], coords[offset + 1], coords[offset + 2], coords[offset + 3], coords[offset + 4], coords[offset + 5], coords[offset + 6], coords[offset + 7]);
    }

    public void setCurve(Point2D p1, Point2D cp1, Point2D cp2, Point2D p2) {
        setCurve(p1.f11907x, p1.f11908y, cp1.f11907x, cp1.f11908y, cp2.f11907x, cp2.f11908y, p2.f11907x, p2.f11908y);
    }

    public void setCurve(Point2D[] pts, int offset) {
        setCurve(pts[offset + 0].f11907x, pts[offset + 0].f11908y, pts[offset + 1].f11907x, pts[offset + 1].f11908y, pts[offset + 2].f11907x, pts[offset + 2].f11908y, pts[offset + 3].f11907x, pts[offset + 3].f11908y);
    }

    public void setCurve(CubicCurve2D c2) {
        setCurve(c2.x1, c2.y1, c2.ctrlx1, c2.ctrly1, c2.ctrlx2, c2.ctrly2, c2.x2, c2.y2);
    }

    public static float getFlatnessSq(float x1, float y1, float ctrlx1, float ctrly1, float ctrlx2, float ctrly2, float x2, float y2) {
        return Math.max(Line2D.ptSegDistSq(x1, y1, x2, y2, ctrlx1, ctrly1), Line2D.ptSegDistSq(x1, y1, x2, y2, ctrlx2, ctrly2));
    }

    public static float getFlatness(float x1, float y1, float ctrlx1, float ctrly1, float ctrlx2, float ctrly2, float x2, float y2) {
        return (float) Math.sqrt(getFlatnessSq(x1, y1, ctrlx1, ctrly1, ctrlx2, ctrly2, x2, y2));
    }

    public static float getFlatnessSq(float[] coords, int offset) {
        return getFlatnessSq(coords[offset + 0], coords[offset + 1], coords[offset + 2], coords[offset + 3], coords[offset + 4], coords[offset + 5], coords[offset + 6], coords[offset + 7]);
    }

    public static float getFlatness(float[] coords, int offset) {
        return getFlatness(coords[offset + 0], coords[offset + 1], coords[offset + 2], coords[offset + 3], coords[offset + 4], coords[offset + 5], coords[offset + 6], coords[offset + 7]);
    }

    public float getFlatnessSq() {
        return getFlatnessSq(this.x1, this.y1, this.ctrlx1, this.ctrly1, this.ctrlx2, this.ctrly2, this.x2, this.y2);
    }

    public float getFlatness() {
        return getFlatness(this.x1, this.y1, this.ctrlx1, this.ctrly1, this.ctrlx2, this.ctrly2, this.x2, this.y2);
    }

    public void subdivide(float t2, CubicCurve2D left, CubicCurve2D right) {
        if (left == null && right == null) {
            return;
        }
        float npx = calcX(t2);
        float npy = calcY(t2);
        float x1 = this.x1;
        float y1 = this.y1;
        float c1x = this.ctrlx1;
        float c1y = this.ctrly1;
        float c2x = this.ctrlx2;
        float c2y = this.ctrly2;
        float x2 = this.x2;
        float y2 = this.y2;
        float u2 = 1.0f - t2;
        float hx = (u2 * c1x) + (t2 * c2x);
        float hy = (u2 * c1y) + (t2 * c2y);
        if (left != null) {
            float lc1x = (u2 * x1) + (t2 * c1x);
            float lc1y = (u2 * y1) + (t2 * c1y);
            float lc2x = (u2 * lc1x) + (t2 * hx);
            float lc2y = (u2 * lc1y) + (t2 * hy);
            left.setCurve(x1, y1, lc1x, lc1y, lc2x, lc2y, npx, npy);
        }
        if (right != null) {
            float rc2x = (u2 * c2x) + (t2 * x2);
            float rc2y = (u2 * c2y) + (t2 * y2);
            float rc1x = (u2 * hx) + (t2 * rc2x);
            float rc1y = (u2 * hy) + (t2 * rc2y);
            right.setCurve(npx, npy, rc1x, rc1y, rc2x, rc2y, x2, y2);
        }
    }

    public void subdivide(CubicCurve2D left, CubicCurve2D right) {
        subdivide(this, left, right);
    }

    public static void subdivide(CubicCurve2D src, CubicCurve2D left, CubicCurve2D right) {
        float x1 = src.x1;
        float y1 = src.y1;
        float ctrlx1 = src.ctrlx1;
        float ctrly1 = src.ctrly1;
        float ctrlx2 = src.ctrlx2;
        float ctrly2 = src.ctrly2;
        float x2 = src.x2;
        float y2 = src.y2;
        float centerx = (ctrlx1 + ctrlx2) / 2.0f;
        float centery = (ctrly1 + ctrly2) / 2.0f;
        float ctrlx12 = (x1 + ctrlx1) / 2.0f;
        float ctrly12 = (y1 + ctrly1) / 2.0f;
        float ctrlx22 = (x2 + ctrlx2) / 2.0f;
        float ctrly22 = (y2 + ctrly2) / 2.0f;
        float ctrlx122 = (ctrlx12 + centerx) / 2.0f;
        float ctrly122 = (ctrly12 + centery) / 2.0f;
        float ctrlx21 = (ctrlx22 + centerx) / 2.0f;
        float ctrly21 = (ctrly22 + centery) / 2.0f;
        float centerx2 = (ctrlx122 + ctrlx21) / 2.0f;
        float centery2 = (ctrly122 + ctrly21) / 2.0f;
        if (left != null) {
            left.setCurve(x1, y1, ctrlx12, ctrly12, ctrlx122, ctrly122, centerx2, centery2);
        }
        if (right != null) {
            right.setCurve(centerx2, centery2, ctrlx21, ctrly21, ctrlx22, ctrly22, x2, y2);
        }
    }

    public static void subdivide(float[] src, int srcoff, float[] left, int leftoff, float[] right, int rightoff) {
        float x1 = src[srcoff + 0];
        float y1 = src[srcoff + 1];
        float ctrlx1 = src[srcoff + 2];
        float ctrly1 = src[srcoff + 3];
        float ctrlx2 = src[srcoff + 4];
        float ctrly2 = src[srcoff + 5];
        float x2 = src[srcoff + 6];
        float y2 = src[srcoff + 7];
        if (left != null) {
            left[leftoff + 0] = x1;
            left[leftoff + 1] = y1;
        }
        if (right != null) {
            right[rightoff + 6] = x2;
            right[rightoff + 7] = y2;
        }
        float x12 = (x1 + ctrlx1) / 2.0f;
        float y12 = (y1 + ctrly1) / 2.0f;
        float x22 = (x2 + ctrlx2) / 2.0f;
        float y22 = (y2 + ctrly2) / 2.0f;
        float centerx = (ctrlx1 + ctrlx2) / 2.0f;
        float centery = (ctrly1 + ctrly2) / 2.0f;
        float ctrlx12 = (x12 + centerx) / 2.0f;
        float ctrly12 = (y12 + centery) / 2.0f;
        float ctrlx22 = (x22 + centerx) / 2.0f;
        float ctrly22 = (y22 + centery) / 2.0f;
        float centerx2 = (ctrlx12 + ctrlx22) / 2.0f;
        float centery2 = (ctrly12 + ctrly22) / 2.0f;
        if (left != null) {
            left[leftoff + 2] = x12;
            left[leftoff + 3] = y12;
            left[leftoff + 4] = ctrlx12;
            left[leftoff + 5] = ctrly12;
            left[leftoff + 6] = centerx2;
            left[leftoff + 7] = centery2;
        }
        if (right != null) {
            right[rightoff + 0] = centerx2;
            right[rightoff + 1] = centery2;
            right[rightoff + 2] = ctrlx22;
            right[rightoff + 3] = ctrly22;
            right[rightoff + 4] = x22;
            right[rightoff + 5] = y22;
        }
    }

    public static int solveCubic(float[] eqn) {
        return solveCubic(eqn, eqn);
    }

    public static int solveCubic(float[] eqn, float[] res) {
        int roots;
        float d2 = eqn[3];
        if (d2 == 0.0f) {
            return QuadCurve2D.solveQuadratic(eqn, res);
        }
        float a2 = eqn[2] / d2;
        float b2 = eqn[1] / d2;
        float c2 = eqn[0] / d2;
        float Q2 = ((a2 * a2) - (3.0f * b2)) / 9.0f;
        float R2 = (((((2.0f * a2) * a2) * a2) - ((9.0f * a2) * b2)) + (27.0f * c2)) / 54.0f;
        float R22 = R2 * R2;
        float Q3 = Q2 * Q2 * Q2;
        float a3 = a2 / 3.0f;
        if (R22 < Q3) {
            float theta = (float) Math.acos(R2 / Math.sqrt(Q3));
            float Q4 = (float) ((-2.0d) * Math.sqrt(Q2));
            if (res == eqn) {
                eqn = new float[4];
                System.arraycopy(res, 0, eqn, 0, 4);
            }
            int roots2 = 0 + 1;
            res[0] = (float) ((Q4 * Math.cos(theta / 3.0f)) - a3);
            int roots3 = roots2 + 1;
            res[roots2] = (float) ((Q4 * Math.cos((theta + 6.283185307179586d) / 3.0d)) - a3);
            roots = roots3 + 1;
            res[roots3] = (float) ((Q4 * Math.cos((theta - 6.283185307179586d) / 3.0d)) - a3);
            fixRoots(res, eqn);
        } else {
            boolean neg = R2 < 0.0f;
            float S2 = (float) Math.sqrt(R22 - Q3);
            if (neg) {
                R2 = -R2;
            }
            float A2 = (float) Math.pow(R2 + S2, 0.3333333432674408d);
            if (!neg) {
                A2 = -A2;
            }
            float B2 = A2 == 0.0f ? 0.0f : Q2 / A2;
            roots = 0 + 1;
            res[0] = (A2 + B2) - a3;
        }
        return roots;
    }

    private static void fixRoots(float[] res, float[] eqn) {
        for (int i2 = 0; i2 < 3; i2++) {
            float t2 = res[i2];
            if (Math.abs(t2) < 1.0E-5f) {
                res[i2] = findZero(t2, 0.0f, eqn);
            } else if (Math.abs(t2 - 1.0f) < 1.0E-5f) {
                res[i2] = findZero(t2, 1.0f, eqn);
            }
        }
    }

    private static float solveEqn(float[] eqn, int order, float t2) {
        float f2 = eqn[order];
        while (true) {
            float v2 = f2;
            order--;
            if (order >= 0) {
                f2 = (v2 * t2) + eqn[order];
            } else {
                return v2;
            }
        }
    }

    private static float findZero(float t2, float target, float[] eqn) {
        int tag;
        float[] slopeqn = {eqn[1], 2.0f * eqn[2], 3.0f * eqn[3]};
        float origdelta = 0.0f;
        while (true) {
            float slope = solveEqn(slopeqn, 2, t2);
            if (slope == 0.0f) {
                return t2;
            }
            float y2 = solveEqn(eqn, 3, t2);
            if (y2 == 0.0f) {
                return t2;
            }
            float delta = -(y2 / slope);
            if (origdelta == 0.0f) {
                origdelta = delta;
            }
            if (t2 < target) {
                if (delta < 0.0f) {
                    return t2;
                }
            } else {
                if (t2 <= target) {
                    return delta > 0.0f ? target + Float.MIN_VALUE : target - Float.MIN_VALUE;
                }
                if (delta > 0.0f) {
                    return t2;
                }
            }
            float newt = t2 + delta;
            if (t2 == newt) {
                return t2;
            }
            if (delta * origdelta < 0.0f) {
                if (t2 < t2) {
                    tag = getTag(target, t2, t2);
                } else {
                    tag = getTag(target, t2, t2);
                }
                int tag2 = tag;
                if (tag2 != 0) {
                    return (t2 + t2) / 2.0f;
                }
                t2 = target;
            } else {
                t2 = newt;
            }
        }
    }

    @Override // com.sun.javafx.geom.Shape
    public boolean contains(float x2, float y2) {
        if ((x2 * 0.0f) + (y2 * 0.0f) != 0.0f) {
            return false;
        }
        int crossings = Shape.pointCrossingsForLine(x2, y2, this.x1, this.y1, this.x2, this.y2) + Shape.pointCrossingsForCubic(x2, y2, this.x1, this.y1, this.ctrlx1, this.ctrly1, this.ctrlx2, this.ctrly2, this.x2, this.y2, 0);
        return (crossings & 1) == 1;
    }

    @Override // com.sun.javafx.geom.Shape
    public boolean contains(Point2D p2) {
        return contains(p2.f11907x, p2.f11908y);
    }

    private static void fillEqn(float[] eqn, float val, float c1, float cp1, float cp2, float c2) {
        eqn[0] = c1 - val;
        eqn[1] = (cp1 - c1) * 3.0f;
        eqn[2] = (((cp2 - cp1) - cp1) + c1) * 3.0f;
        eqn[3] = (c2 + ((cp1 - cp2) * 3.0f)) - c1;
    }

    /* JADX WARN: Removed duplicated region for block: B:12:0x0027  */
    /* JADX WARN: Removed duplicated region for block: B:19:0x003c  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private static int evalCubic(float[] r6, int r7, boolean r8, boolean r9, float[] r10, float r11, float r12, float r13, float r14) {
        /*
            Method dump skipped, instructions count: 170
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.javafx.geom.CubicCurve2D.evalCubic(float[], int, boolean, boolean, float[], float, float, float, float):int");
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
        float ctrlx1 = this.ctrlx1;
        float ctrly1 = this.ctrly1;
        float ctrlx2 = this.ctrlx2;
        float ctrly2 = this.ctrly2;
        int ctrlx1tag = getTag(ctrlx1, x2, x2 + w2);
        int ctrly1tag = getTag(ctrly1, y2, y2 + h2);
        int ctrlx2tag = getTag(ctrlx2, x2, x2 + w2);
        int ctrly2tag = getTag(ctrly2, y2, y2 + h2);
        if (x1tag < 0 && x2tag < 0 && ctrlx1tag < 0 && ctrlx2tag < 0) {
            return false;
        }
        if (y1tag < 0 && y2tag < 0 && ctrly1tag < 0 && ctrly2tag < 0) {
            return false;
        }
        if (x1tag > 0 && x2tag > 0 && ctrlx1tag > 0 && ctrlx2tag > 0) {
            return false;
        }
        if (y1tag > 0 && y2tag > 0 && ctrly1tag > 0 && ctrly2tag > 0) {
            return false;
        }
        if (inwards(x1tag, x2tag, ctrlx1tag) && inwards(y1tag, y2tag, ctrly1tag)) {
            return true;
        }
        if (inwards(x2tag, x1tag, ctrlx2tag) && inwards(y2tag, y1tag, ctrly2tag)) {
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
        float[] eqn = new float[4];
        float[] res = new float[4];
        if (!yoverlap) {
            fillEqn(eqn, y1tag < 0 ? y2 : y2 + h2, y1, ctrly1, ctrly2, y22);
            return evalCubic(res, solveCubic(eqn, res), true, true, null, x1, ctrlx1, ctrlx2, x22) == 2 && getTag(res[0], x2, x2 + w2) * getTag(res[1], x2, x2 + w2) <= 0;
        }
        if (!xoverlap) {
            fillEqn(eqn, x1tag < 0 ? x2 : x2 + w2, x1, ctrlx1, ctrlx2, x22);
            return evalCubic(res, solveCubic(eqn, res), true, true, null, y1, ctrly1, ctrly2, y22) == 2 && getTag(res[0], y2, y2 + h2) * getTag(res[1], y2, y2 + h2) <= 0;
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
        fillEqn(eqn, c2tag < 0 ? x2 : x2 + w2, x1, ctrlx1, ctrlx2, x22);
        int num = evalCubic(res, solveCubic(eqn, res), true, true, null, y1, ctrly1, ctrly2, y22);
        int[] tags = new int[num + 1];
        for (int i2 = 0; i2 < num; i2++) {
            tags[i2] = getTag(res[i2], y2, y2 + h2);
        }
        tags[num] = c1tag2;
        Arrays.sort(tags);
        return (num >= 1 && tags[0] * tags[1] <= 0) || (num >= 3 && tags[2] * tags[3] <= 0);
    }

    @Override // com.sun.javafx.geom.Shape
    public boolean contains(float x2, float y2, float w2, float h2) {
        return w2 > 0.0f && h2 > 0.0f && contains(x2, y2) && contains(x2 + w2, y2) && contains(x2 + w2, y2 + h2) && contains(x2, y2 + h2) && !Shape.intersectsLine(x2, y2, w2, h2, this.x1, this.y1, this.x2, this.y2);
    }

    @Override // com.sun.javafx.geom.Shape
    public PathIterator getPathIterator(BaseTransform tx) {
        return new CubicIterator(this, tx);
    }

    @Override // com.sun.javafx.geom.Shape
    public PathIterator getPathIterator(BaseTransform tx, float flatness) {
        return new FlatteningPathIterator(getPathIterator(tx), flatness);
    }

    @Override // com.sun.javafx.geom.Shape
    public CubicCurve2D copy() {
        return new CubicCurve2D(this.x1, this.y1, this.ctrlx1, this.ctrly1, this.ctrlx2, this.ctrly2, this.x2, this.y2);
    }

    public int hashCode() {
        int bits = Float.floatToIntBits(this.x1);
        return bits + (Float.floatToIntBits(this.y1) * 37) + (Float.floatToIntBits(this.x2) * 43) + (Float.floatToIntBits(this.y2) * 47) + (Float.floatToIntBits(this.ctrlx1) * 53) + (Float.floatToIntBits(this.ctrly1) * 59) + (Float.floatToIntBits(this.ctrlx2) * 61) + (Float.floatToIntBits(this.ctrly2) * 101);
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof CubicCurve2D) {
            CubicCurve2D curve = (CubicCurve2D) obj;
            return this.x1 == curve.x1 && this.y1 == curve.y1 && this.x2 == curve.x2 && this.y2 == curve.y2 && this.ctrlx1 == curve.ctrlx1 && this.ctrly1 == curve.ctrly1 && this.ctrlx2 == curve.ctrlx2 && this.ctrly2 == curve.ctrly2;
        }
        return false;
    }

    private float calcX(float t2) {
        float u2 = 1.0f - t2;
        return (u2 * u2 * u2 * this.x1) + (3.0f * ((t2 * u2 * u2 * this.ctrlx1) + (t2 * t2 * u2 * this.ctrlx2))) + (t2 * t2 * t2 * this.x2);
    }

    private float calcY(float t2) {
        float u2 = 1.0f - t2;
        return (u2 * u2 * u2 * this.y1) + (3.0f * ((t2 * u2 * u2 * this.ctrly1) + (t2 * t2 * u2 * this.ctrly2))) + (t2 * t2 * t2 * this.y2);
    }
}
