package java.awt.geom;

import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;
import java.util.Arrays;
import sun.awt.geom.Curve;

/* loaded from: rt.jar:java/awt/geom/CubicCurve2D.class */
public abstract class CubicCurve2D implements Shape, Cloneable {
    public abstract double getX1();

    public abstract double getY1();

    public abstract Point2D getP1();

    public abstract double getCtrlX1();

    public abstract double getCtrlY1();

    public abstract Point2D getCtrlP1();

    public abstract double getCtrlX2();

    public abstract double getCtrlY2();

    public abstract Point2D getCtrlP2();

    public abstract double getX2();

    public abstract double getY2();

    public abstract Point2D getP2();

    public abstract void setCurve(double d2, double d3, double d4, double d5, double d6, double d7, double d8, double d9);

    /* loaded from: rt.jar:java/awt/geom/CubicCurve2D$Float.class */
    public static class Float extends CubicCurve2D implements Serializable {
        public float x1;
        public float y1;
        public float ctrlx1;
        public float ctrly1;
        public float ctrlx2;
        public float ctrly2;
        public float x2;
        public float y2;
        private static final long serialVersionUID = -1272015596714244385L;

        public Float() {
        }

        public Float(float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9) {
            setCurve(f2, f3, f4, f5, f6, f7, f8, f9);
        }

        @Override // java.awt.geom.CubicCurve2D
        public double getX1() {
            return this.x1;
        }

        @Override // java.awt.geom.CubicCurve2D
        public double getY1() {
            return this.y1;
        }

        @Override // java.awt.geom.CubicCurve2D
        public Point2D getP1() {
            return new Point2D.Float(this.x1, this.y1);
        }

        @Override // java.awt.geom.CubicCurve2D
        public double getCtrlX1() {
            return this.ctrlx1;
        }

        @Override // java.awt.geom.CubicCurve2D
        public double getCtrlY1() {
            return this.ctrly1;
        }

        @Override // java.awt.geom.CubicCurve2D
        public Point2D getCtrlP1() {
            return new Point2D.Float(this.ctrlx1, this.ctrly1);
        }

        @Override // java.awt.geom.CubicCurve2D
        public double getCtrlX2() {
            return this.ctrlx2;
        }

        @Override // java.awt.geom.CubicCurve2D
        public double getCtrlY2() {
            return this.ctrly2;
        }

        @Override // java.awt.geom.CubicCurve2D
        public Point2D getCtrlP2() {
            return new Point2D.Float(this.ctrlx2, this.ctrly2);
        }

        @Override // java.awt.geom.CubicCurve2D
        public double getX2() {
            return this.x2;
        }

        @Override // java.awt.geom.CubicCurve2D
        public double getY2() {
            return this.y2;
        }

        @Override // java.awt.geom.CubicCurve2D
        public Point2D getP2() {
            return new Point2D.Float(this.x2, this.y2);
        }

        @Override // java.awt.geom.CubicCurve2D
        public void setCurve(double d2, double d3, double d4, double d5, double d6, double d7, double d8, double d9) {
            this.x1 = (float) d2;
            this.y1 = (float) d3;
            this.ctrlx1 = (float) d4;
            this.ctrly1 = (float) d5;
            this.ctrlx2 = (float) d6;
            this.ctrly2 = (float) d7;
            this.x2 = (float) d8;
            this.y2 = (float) d9;
        }

        public void setCurve(float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9) {
            this.x1 = f2;
            this.y1 = f3;
            this.ctrlx1 = f4;
            this.ctrly1 = f5;
            this.ctrlx2 = f6;
            this.ctrly2 = f7;
            this.x2 = f8;
            this.y2 = f9;
        }

        @Override // java.awt.Shape
        public Rectangle2D getBounds2D() {
            float fMin = Math.min(Math.min(this.x1, this.x2), Math.min(this.ctrlx1, this.ctrlx2));
            float fMin2 = Math.min(Math.min(this.y1, this.y2), Math.min(this.ctrly1, this.ctrly2));
            return new Rectangle2D.Float(fMin, fMin2, Math.max(Math.max(this.x1, this.x2), Math.max(this.ctrlx1, this.ctrlx2)) - fMin, Math.max(Math.max(this.y1, this.y2), Math.max(this.ctrly1, this.ctrly2)) - fMin2);
        }
    }

    /* loaded from: rt.jar:java/awt/geom/CubicCurve2D$Double.class */
    public static class Double extends CubicCurve2D implements Serializable {
        public double x1;
        public double y1;
        public double ctrlx1;
        public double ctrly1;
        public double ctrlx2;
        public double ctrly2;
        public double x2;
        public double y2;
        private static final long serialVersionUID = -4202960122839707295L;

        public Double() {
        }

        public Double(double d2, double d3, double d4, double d5, double d6, double d7, double d8, double d9) {
            setCurve(d2, d3, d4, d5, d6, d7, d8, d9);
        }

        @Override // java.awt.geom.CubicCurve2D
        public double getX1() {
            return this.x1;
        }

        @Override // java.awt.geom.CubicCurve2D
        public double getY1() {
            return this.y1;
        }

        @Override // java.awt.geom.CubicCurve2D
        public Point2D getP1() {
            return new Point2D.Double(this.x1, this.y1);
        }

        @Override // java.awt.geom.CubicCurve2D
        public double getCtrlX1() {
            return this.ctrlx1;
        }

        @Override // java.awt.geom.CubicCurve2D
        public double getCtrlY1() {
            return this.ctrly1;
        }

        @Override // java.awt.geom.CubicCurve2D
        public Point2D getCtrlP1() {
            return new Point2D.Double(this.ctrlx1, this.ctrly1);
        }

        @Override // java.awt.geom.CubicCurve2D
        public double getCtrlX2() {
            return this.ctrlx2;
        }

        @Override // java.awt.geom.CubicCurve2D
        public double getCtrlY2() {
            return this.ctrly2;
        }

        @Override // java.awt.geom.CubicCurve2D
        public Point2D getCtrlP2() {
            return new Point2D.Double(this.ctrlx2, this.ctrly2);
        }

        @Override // java.awt.geom.CubicCurve2D
        public double getX2() {
            return this.x2;
        }

        @Override // java.awt.geom.CubicCurve2D
        public double getY2() {
            return this.y2;
        }

        @Override // java.awt.geom.CubicCurve2D
        public Point2D getP2() {
            return new Point2D.Double(this.x2, this.y2);
        }

        @Override // java.awt.geom.CubicCurve2D
        public void setCurve(double d2, double d3, double d4, double d5, double d6, double d7, double d8, double d9) {
            this.x1 = d2;
            this.y1 = d3;
            this.ctrlx1 = d4;
            this.ctrly1 = d5;
            this.ctrlx2 = d6;
            this.ctrly2 = d7;
            this.x2 = d8;
            this.y2 = d9;
        }

        @Override // java.awt.Shape
        public Rectangle2D getBounds2D() {
            double dMin = Math.min(Math.min(this.x1, this.x2), Math.min(this.ctrlx1, this.ctrlx2));
            double dMin2 = Math.min(Math.min(this.y1, this.y2), Math.min(this.ctrly1, this.ctrly2));
            return new Rectangle2D.Double(dMin, dMin2, Math.max(Math.max(this.x1, this.x2), Math.max(this.ctrlx1, this.ctrlx2)) - dMin, Math.max(Math.max(this.y1, this.y2), Math.max(this.ctrly1, this.ctrly2)) - dMin2);
        }
    }

    protected CubicCurve2D() {
    }

    public void setCurve(double[] dArr, int i2) {
        setCurve(dArr[i2 + 0], dArr[i2 + 1], dArr[i2 + 2], dArr[i2 + 3], dArr[i2 + 4], dArr[i2 + 5], dArr[i2 + 6], dArr[i2 + 7]);
    }

    public void setCurve(Point2D point2D, Point2D point2D2, Point2D point2D3, Point2D point2D4) {
        setCurve(point2D.getX(), point2D.getY(), point2D2.getX(), point2D2.getY(), point2D3.getX(), point2D3.getY(), point2D4.getX(), point2D4.getY());
    }

    public void setCurve(Point2D[] point2DArr, int i2) {
        setCurve(point2DArr[i2 + 0].getX(), point2DArr[i2 + 0].getY(), point2DArr[i2 + 1].getX(), point2DArr[i2 + 1].getY(), point2DArr[i2 + 2].getX(), point2DArr[i2 + 2].getY(), point2DArr[i2 + 3].getX(), point2DArr[i2 + 3].getY());
    }

    public void setCurve(CubicCurve2D cubicCurve2D) {
        setCurve(cubicCurve2D.getX1(), cubicCurve2D.getY1(), cubicCurve2D.getCtrlX1(), cubicCurve2D.getCtrlY1(), cubicCurve2D.getCtrlX2(), cubicCurve2D.getCtrlY2(), cubicCurve2D.getX2(), cubicCurve2D.getY2());
    }

    public static double getFlatnessSq(double d2, double d3, double d4, double d5, double d6, double d7, double d8, double d9) {
        return Math.max(Line2D.ptSegDistSq(d2, d3, d8, d9, d4, d5), Line2D.ptSegDistSq(d2, d3, d8, d9, d6, d7));
    }

    public static double getFlatness(double d2, double d3, double d4, double d5, double d6, double d7, double d8, double d9) {
        return Math.sqrt(getFlatnessSq(d2, d3, d4, d5, d6, d7, d8, d9));
    }

    public static double getFlatnessSq(double[] dArr, int i2) {
        return getFlatnessSq(dArr[i2 + 0], dArr[i2 + 1], dArr[i2 + 2], dArr[i2 + 3], dArr[i2 + 4], dArr[i2 + 5], dArr[i2 + 6], dArr[i2 + 7]);
    }

    public static double getFlatness(double[] dArr, int i2) {
        return getFlatness(dArr[i2 + 0], dArr[i2 + 1], dArr[i2 + 2], dArr[i2 + 3], dArr[i2 + 4], dArr[i2 + 5], dArr[i2 + 6], dArr[i2 + 7]);
    }

    public double getFlatnessSq() {
        return getFlatnessSq(getX1(), getY1(), getCtrlX1(), getCtrlY1(), getCtrlX2(), getCtrlY2(), getX2(), getY2());
    }

    public double getFlatness() {
        return getFlatness(getX1(), getY1(), getCtrlX1(), getCtrlY1(), getCtrlX2(), getCtrlY2(), getX2(), getY2());
    }

    public void subdivide(CubicCurve2D cubicCurve2D, CubicCurve2D cubicCurve2D2) {
        subdivide(this, cubicCurve2D, cubicCurve2D2);
    }

    public static void subdivide(CubicCurve2D cubicCurve2D, CubicCurve2D cubicCurve2D2, CubicCurve2D cubicCurve2D3) {
        double x1 = cubicCurve2D.getX1();
        double y1 = cubicCurve2D.getY1();
        double ctrlX1 = cubicCurve2D.getCtrlX1();
        double ctrlY1 = cubicCurve2D.getCtrlY1();
        double ctrlX2 = cubicCurve2D.getCtrlX2();
        double ctrlY2 = cubicCurve2D.getCtrlY2();
        double x2 = cubicCurve2D.getX2();
        double y2 = cubicCurve2D.getY2();
        double d2 = (ctrlX1 + ctrlX2) / 2.0d;
        double d3 = (ctrlY1 + ctrlY2) / 2.0d;
        double d4 = (x1 + ctrlX1) / 2.0d;
        double d5 = (y1 + ctrlY1) / 2.0d;
        double d6 = (x2 + ctrlX2) / 2.0d;
        double d7 = (y2 + ctrlY2) / 2.0d;
        double d8 = (d4 + d2) / 2.0d;
        double d9 = (d5 + d3) / 2.0d;
        double d10 = (d6 + d2) / 2.0d;
        double d11 = (d7 + d3) / 2.0d;
        double d12 = (d8 + d10) / 2.0d;
        double d13 = (d9 + d11) / 2.0d;
        if (cubicCurve2D2 != null) {
            cubicCurve2D2.setCurve(x1, y1, d4, d5, d8, d9, d12, d13);
        }
        if (cubicCurve2D3 != null) {
            cubicCurve2D3.setCurve(d12, d13, d10, d11, d6, d7, x2, y2);
        }
    }

    public static void subdivide(double[] dArr, int i2, double[] dArr2, int i3, double[] dArr3, int i4) {
        double d2 = dArr[i2 + 0];
        double d3 = dArr[i2 + 1];
        double d4 = dArr[i2 + 2];
        double d5 = dArr[i2 + 3];
        double d6 = dArr[i2 + 4];
        double d7 = dArr[i2 + 5];
        double d8 = dArr[i2 + 6];
        double d9 = dArr[i2 + 7];
        if (dArr2 != null) {
            dArr2[i3 + 0] = d2;
            dArr2[i3 + 1] = d3;
        }
        if (dArr3 != null) {
            dArr3[i4 + 6] = d8;
            dArr3[i4 + 7] = d9;
        }
        double d10 = (d2 + d4) / 2.0d;
        double d11 = (d3 + d5) / 2.0d;
        double d12 = (d8 + d6) / 2.0d;
        double d13 = (d9 + d7) / 2.0d;
        double d14 = (d4 + d6) / 2.0d;
        double d15 = (d5 + d7) / 2.0d;
        double d16 = (d10 + d14) / 2.0d;
        double d17 = (d11 + d15) / 2.0d;
        double d18 = (d12 + d14) / 2.0d;
        double d19 = (d13 + d15) / 2.0d;
        double d20 = (d16 + d18) / 2.0d;
        double d21 = (d17 + d19) / 2.0d;
        if (dArr2 != null) {
            dArr2[i3 + 2] = d10;
            dArr2[i3 + 3] = d11;
            dArr2[i3 + 4] = d16;
            dArr2[i3 + 5] = d17;
            dArr2[i3 + 6] = d20;
            dArr2[i3 + 7] = d21;
        }
        if (dArr3 != null) {
            dArr3[i4 + 0] = d20;
            dArr3[i4 + 1] = d21;
            dArr3[i4 + 2] = d18;
            dArr3[i4 + 3] = d19;
            dArr3[i4 + 4] = d12;
            dArr3[i4 + 5] = d13;
        }
    }

    public static int solveCubic(double[] dArr) {
        return solveCubic(dArr, dArr);
    }

    public static int solveCubic(double[] dArr, double[] dArr2) {
        int iFixRoots;
        double d2 = dArr[3];
        if (d2 == 0.0d) {
            return QuadCurve2D.solveQuadratic(dArr, dArr2);
        }
        double d3 = dArr[2] / d2;
        double d4 = dArr[1] / d2;
        double d5 = dArr[0] / d2;
        double d6 = d3 * d3;
        double d7 = 0.3333333333333333d * (((-0.3333333333333333d) * d6) + d4);
        double d8 = 0.5d * ((((0.07407407407407407d * d3) * d6) - ((0.3333333333333333d * d3) * d4)) + d5);
        double d9 = d7 * d7 * d7;
        double d10 = (d8 * d8) + d9;
        double d11 = 0.3333333333333333d * d3;
        if (d10 < 0.0d) {
            double dAcos = 0.3333333333333333d * Math.acos((-d8) / Math.sqrt(-d9));
            double dSqrt = 2.0d * Math.sqrt(-d7);
            if (dArr2 == dArr) {
                dArr = Arrays.copyOf(dArr, 4);
            }
            dArr2[0] = dSqrt * Math.cos(dAcos);
            dArr2[1] = (-dSqrt) * Math.cos(dAcos + 1.0471975511965976d);
            dArr2[2] = (-dSqrt) * Math.cos(dAcos - 1.0471975511965976d);
            iFixRoots = 3;
            for (int i2 = 0; i2 < 3; i2++) {
                int i3 = i2;
                dArr2[i3] = dArr2[i3] - d11;
            }
        } else {
            double dSqrt2 = Math.sqrt(d10);
            double dCbrt = Math.cbrt(dSqrt2 - d8);
            double d12 = -Math.cbrt(dSqrt2 + d8);
            double d13 = dCbrt + d12;
            iFixRoots = 1;
            double dUlp = 1.2E9d * Math.ulp(Math.abs(d13) + Math.abs(d11));
            if (iszero(d10, dUlp) || within(dCbrt, d12, dUlp)) {
                if (dArr2 == dArr) {
                    dArr = Arrays.copyOf(dArr, 4);
                }
                dArr2[1] = (-(d13 / 2.0d)) - d11;
                iFixRoots = 2;
            }
            dArr2[0] = d13 - d11;
        }
        if (iFixRoots > 1) {
            iFixRoots = fixRoots(dArr, dArr2, iFixRoots);
        }
        if (iFixRoots > 2 && (dArr2[2] == dArr2[1] || dArr2[2] == dArr2[0])) {
            iFixRoots--;
        }
        if (iFixRoots > 1 && dArr2[1] == dArr2[0]) {
            iFixRoots--;
            dArr2[1] = dArr2[iFixRoots];
        }
        return iFixRoots;
    }

    private static int fixRoots(double[] dArr, double[] dArr2, int i2) {
        double[] dArr3 = {dArr[1], 2.0d * dArr[2], 3.0d * dArr[3]};
        int iSolveQuadratic = QuadCurve2D.solveQuadratic(dArr3, dArr3);
        if (iSolveQuadratic == 2 && dArr3[0] == dArr3[1]) {
            iSolveQuadratic--;
        }
        if (iSolveQuadratic == 2 && dArr3[0] > dArr3[1]) {
            double d2 = dArr3[0];
            dArr3[0] = dArr3[1];
            dArr3[1] = d2;
        }
        if (i2 != 3) {
            if (i2 == 2 && iSolveQuadratic == 2) {
                double d3 = dArr2[0];
                double d4 = dArr2[1];
                double d5 = dArr3[0];
                double d6 = dArr3[1];
                double d7 = Math.abs(d5 - d3) > Math.abs(d6 - d3) ? d5 : d6;
                double dSolveEqn = solveEqn(dArr, 3, d7);
                if (iszero(dSolveEqn, 1.0E7d * Math.ulp(d7))) {
                    dArr2[1] = Math.abs(solveEqn(dArr, 3, d4)) < Math.abs(dSolveEqn) ? d4 : d7;
                    return 2;
                }
                return 1;
            }
            return 1;
        }
        double rootUpperBound = getRootUpperBound(dArr);
        double d8 = -rootUpperBound;
        Arrays.sort(dArr2, 0, i2);
        if (iSolveQuadratic == 2) {
            dArr2[0] = refineRootWithHint(dArr, d8, dArr3[0], dArr2[0]);
            dArr2[1] = refineRootWithHint(dArr, dArr3[0], dArr3[1], dArr2[1]);
            dArr2[2] = refineRootWithHint(dArr, dArr3[1], rootUpperBound, dArr2[2]);
            return 3;
        }
        if (iSolveQuadratic != 1) {
            if (iSolveQuadratic == 0) {
                dArr2[0] = bisectRootWithHint(dArr, d8, rootUpperBound, dArr2[1]);
                return 1;
            }
            return 1;
        }
        double d9 = dArr[3];
        double d10 = -d9;
        double d11 = dArr3[0];
        double dSolveEqn2 = solveEqn(dArr, 3, d11);
        if (oppositeSigns(d10, dSolveEqn2)) {
            dArr2[0] = bisectRootWithHint(dArr, d8, d11, dArr2[0]);
            return 1;
        }
        if (oppositeSigns(dSolveEqn2, d9)) {
            dArr2[0] = bisectRootWithHint(dArr, d11, rootUpperBound, dArr2[2]);
            return 1;
        }
        dArr2[0] = d11;
        return 1;
    }

    private static double refineRootWithHint(double[] dArr, double d2, double d3, double d4) {
        if (!inInterval(d4, d2, d3)) {
            return d4;
        }
        double[] dArr2 = {dArr[1], 2.0d * dArr[2], 3.0d * dArr[3]};
        for (int i2 = 0; i2 < 3; i2++) {
            double dSolveEqn = solveEqn(dArr2, 2, d4);
            double dSolveEqn2 = solveEqn(dArr, 3, d4);
            double d5 = d4 + (-(dSolveEqn2 / dSolveEqn));
            if (dSolveEqn == 0.0d || dSolveEqn2 == 0.0d || d4 == d5) {
                break;
            }
            d4 = d5;
        }
        if (within(d4, d4, 1000.0d * Math.ulp(d4)) && inInterval(d4, d2, d3)) {
            return d4;
        }
        return d4;
    }

    private static double bisectRootWithHint(double[] dArr, double d2, double d3, double d4) {
        double dMin = Math.min(Math.abs(d4 - d2) / 64.0d, 0.0625d);
        double dMin2 = Math.min(Math.abs(d4 - d3) / 64.0d, 0.0625d);
        double d5 = d4 - dMin;
        double d6 = d4 + dMin2;
        double dSolveEqn = solveEqn(dArr, 3, d5);
        double dSolveEqn2 = solveEqn(dArr, 3, d6);
        while (true) {
            double d7 = dSolveEqn2;
            if (!oppositeSigns(dSolveEqn, d7)) {
                if (dSolveEqn == 0.0d) {
                    return d5;
                }
                if (d7 == 0.0d) {
                    return d6;
                }
                return bisectRoot(dArr, d2, d3);
            }
            if (d5 >= d6) {
                return d5;
            }
            d2 = d5;
            d3 = d6;
            dMin /= 64.0d;
            dMin2 /= 64.0d;
            d5 = d4 - dMin;
            d6 = d4 + dMin2;
            dSolveEqn = solveEqn(dArr, 3, d5);
            dSolveEqn2 = solveEqn(dArr, 3, d6);
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:17:0x005b, code lost:
    
        return r0;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private static double bisectRoot(double[] r7, double r8, double r10) {
        /*
            r0 = r7
            r1 = 3
            r2 = r8
            double r0 = solveEqn(r0, r1, r2)
            r12 = r0
            r0 = r8
            r1 = r10
            r2 = r8
            double r1 = r1 - r2
            r2 = 4611686018427387904(0x4000000000000000, double:2.0)
            double r1 = r1 / r2
            double r0 = r0 + r1
            r14 = r0
        L13:
            r0 = r14
            r1 = r8
            int r0 = (r0 > r1 ? 1 : (r0 == r1 ? 0 : -1))
            if (r0 == 0) goto L59
            r0 = r14
            r1 = r10
            int r0 = (r0 > r1 ? 1 : (r0 == r1 ? 0 : -1))
            if (r0 == 0) goto L59
            r0 = r7
            r1 = 3
            r2 = r14
            double r0 = solveEqn(r0, r1, r2)
            r16 = r0
            r0 = r16
            r1 = 0
            int r0 = (r0 > r1 ? 1 : (r0 == r1 ? 0 : -1))
            if (r0 != 0) goto L34
            r0 = r14
            return r0
        L34:
            r0 = r12
            r1 = r16
            boolean r0 = oppositeSigns(r0, r1)
            if (r0 == 0) goto L44
            r0 = r14
            r10 = r0
            goto L4b
        L44:
            r0 = r16
            r12 = r0
            r0 = r14
            r8 = r0
        L4b:
            r0 = r8
            r1 = r10
            r2 = r8
            double r1 = r1 - r2
            r2 = 4611686018427387904(0x4000000000000000, double:2.0)
            double r1 = r1 / r2
            double r0 = r0 + r1
            r14 = r0
            goto L13
        L59:
            r0 = r14
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: java.awt.geom.CubicCurve2D.bisectRoot(double[], double, double):double");
    }

    private static boolean inInterval(double d2, double d3, double d4) {
        return d3 <= d2 && d2 <= d4;
    }

    private static boolean within(double d2, double d3, double d4) {
        double d5 = d3 - d2;
        return d5 <= d4 && d5 >= (-d4);
    }

    private static boolean iszero(double d2, double d3) {
        return within(d2, 0.0d, d3);
    }

    private static boolean oppositeSigns(double d2, double d3) {
        return (d2 < 0.0d && d3 > 0.0d) || (d2 > 0.0d && d3 < 0.0d);
    }

    private static double solveEqn(double[] dArr, int i2, double d2) {
        double d3 = dArr[i2];
        while (true) {
            double d4 = d3;
            i2--;
            if (i2 >= 0) {
                d3 = (d4 * d2) + dArr[i2];
            } else {
                return d4;
            }
        }
    }

    private static double getRootUpperBound(double[] dArr) {
        double d2 = dArr[3];
        double dMax = 1.0d + (Math.max(Math.max(Math.abs(dArr[2]), Math.abs(dArr[1])), Math.abs(dArr[0])) / Math.abs(d2));
        return dMax + Math.ulp(dMax) + 1.0d;
    }

    @Override // java.awt.Shape
    public boolean contains(double d2, double d3) {
        if ((d2 * 0.0d) + (d3 * 0.0d) != 0.0d) {
            return false;
        }
        double x1 = getX1();
        double y1 = getY1();
        double x2 = getX2();
        double y2 = getY2();
        return ((Curve.pointCrossingsForLine(d2, d3, x1, y1, x2, y2) + Curve.pointCrossingsForCubic(d2, d3, x1, y1, getCtrlX1(), getCtrlY1(), getCtrlX2(), getCtrlY2(), x2, y2, 0)) & 1) == 1;
    }

    @Override // java.awt.Shape
    public boolean contains(Point2D point2D) {
        return contains(point2D.getX(), point2D.getY());
    }

    @Override // java.awt.Shape
    public boolean intersects(double d2, double d3, double d4, double d5) {
        return d4 > 0.0d && d5 > 0.0d && rectCrossings(d2, d3, d4, d5) != 0;
    }

    @Override // java.awt.Shape
    public boolean intersects(Rectangle2D rectangle2D) {
        return intersects(rectangle2D.getX(), rectangle2D.getY(), rectangle2D.getWidth(), rectangle2D.getHeight());
    }

    @Override // java.awt.Shape
    public boolean contains(double d2, double d3, double d4, double d5) {
        int iRectCrossings;
        return d4 > 0.0d && d5 > 0.0d && (iRectCrossings = rectCrossings(d2, d3, d4, d5)) != 0 && iRectCrossings != Integer.MIN_VALUE;
    }

    private int rectCrossings(double d2, double d3, double d4, double d5) {
        int iRectCrossingsForLine = 0;
        if (getX1() != getX2() || getY1() != getY2()) {
            iRectCrossingsForLine = Curve.rectCrossingsForLine(0, d2, d3, d2 + d4, d3 + d5, getX1(), getY1(), getX2(), getY2());
            if (iRectCrossingsForLine == Integer.MIN_VALUE) {
                return iRectCrossingsForLine;
            }
        }
        return Curve.rectCrossingsForCubic(iRectCrossingsForLine, d2, d3, d2 + d4, d3 + d5, getX2(), getY2(), getCtrlX2(), getCtrlY2(), getCtrlX1(), getCtrlY1(), getX1(), getY1(), 0);
    }

    @Override // java.awt.Shape
    public boolean contains(Rectangle2D rectangle2D) {
        return contains(rectangle2D.getX(), rectangle2D.getY(), rectangle2D.getWidth(), rectangle2D.getHeight());
    }

    @Override // java.awt.Shape
    public Rectangle getBounds() {
        return getBounds2D().getBounds();
    }

    @Override // java.awt.Shape
    public PathIterator getPathIterator(AffineTransform affineTransform) {
        return new CubicIterator(this, affineTransform);
    }

    @Override // java.awt.Shape
    public PathIterator getPathIterator(AffineTransform affineTransform, double d2) {
        return new FlatteningPathIterator(getPathIterator(affineTransform), d2);
    }

    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e2) {
            throw new InternalError(e2);
        }
    }
}
