package java.awt.geom;

import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;

/* loaded from: rt.jar:java/awt/geom/QuadCurve2D.class */
public abstract class QuadCurve2D implements Shape, Cloneable {
    private static final int BELOW = -2;
    private static final int LOWEDGE = -1;
    private static final int INSIDE = 0;
    private static final int HIGHEDGE = 1;
    private static final int ABOVE = 2;

    public abstract double getX1();

    public abstract double getY1();

    public abstract Point2D getP1();

    public abstract double getCtrlX();

    public abstract double getCtrlY();

    public abstract Point2D getCtrlPt();

    public abstract double getX2();

    public abstract double getY2();

    public abstract Point2D getP2();

    public abstract void setCurve(double d2, double d3, double d4, double d5, double d6, double d7);

    /* loaded from: rt.jar:java/awt/geom/QuadCurve2D$Float.class */
    public static class Float extends QuadCurve2D implements Serializable {
        public float x1;
        public float y1;
        public float ctrlx;
        public float ctrly;
        public float x2;
        public float y2;
        private static final long serialVersionUID = -8511188402130719609L;

        public Float() {
        }

        public Float(float f2, float f3, float f4, float f5, float f6, float f7) {
            setCurve(f2, f3, f4, f5, f6, f7);
        }

        @Override // java.awt.geom.QuadCurve2D
        public double getX1() {
            return this.x1;
        }

        @Override // java.awt.geom.QuadCurve2D
        public double getY1() {
            return this.y1;
        }

        @Override // java.awt.geom.QuadCurve2D
        public Point2D getP1() {
            return new Point2D.Float(this.x1, this.y1);
        }

        @Override // java.awt.geom.QuadCurve2D
        public double getCtrlX() {
            return this.ctrlx;
        }

        @Override // java.awt.geom.QuadCurve2D
        public double getCtrlY() {
            return this.ctrly;
        }

        @Override // java.awt.geom.QuadCurve2D
        public Point2D getCtrlPt() {
            return new Point2D.Float(this.ctrlx, this.ctrly);
        }

        @Override // java.awt.geom.QuadCurve2D
        public double getX2() {
            return this.x2;
        }

        @Override // java.awt.geom.QuadCurve2D
        public double getY2() {
            return this.y2;
        }

        @Override // java.awt.geom.QuadCurve2D
        public Point2D getP2() {
            return new Point2D.Float(this.x2, this.y2);
        }

        @Override // java.awt.geom.QuadCurve2D
        public void setCurve(double d2, double d3, double d4, double d5, double d6, double d7) {
            this.x1 = (float) d2;
            this.y1 = (float) d3;
            this.ctrlx = (float) d4;
            this.ctrly = (float) d5;
            this.x2 = (float) d6;
            this.y2 = (float) d7;
        }

        public void setCurve(float f2, float f3, float f4, float f5, float f6, float f7) {
            this.x1 = f2;
            this.y1 = f3;
            this.ctrlx = f4;
            this.ctrly = f5;
            this.x2 = f6;
            this.y2 = f7;
        }

        @Override // java.awt.Shape
        public Rectangle2D getBounds2D() {
            float fMin = Math.min(Math.min(this.x1, this.x2), this.ctrlx);
            float fMin2 = Math.min(Math.min(this.y1, this.y2), this.ctrly);
            return new Rectangle2D.Float(fMin, fMin2, Math.max(Math.max(this.x1, this.x2), this.ctrlx) - fMin, Math.max(Math.max(this.y1, this.y2), this.ctrly) - fMin2);
        }
    }

    /* loaded from: rt.jar:java/awt/geom/QuadCurve2D$Double.class */
    public static class Double extends QuadCurve2D implements Serializable {
        public double x1;
        public double y1;
        public double ctrlx;
        public double ctrly;
        public double x2;
        public double y2;
        private static final long serialVersionUID = 4217149928428559721L;

        public Double() {
        }

        public Double(double d2, double d3, double d4, double d5, double d6, double d7) {
            setCurve(d2, d3, d4, d5, d6, d7);
        }

        @Override // java.awt.geom.QuadCurve2D
        public double getX1() {
            return this.x1;
        }

        @Override // java.awt.geom.QuadCurve2D
        public double getY1() {
            return this.y1;
        }

        @Override // java.awt.geom.QuadCurve2D
        public Point2D getP1() {
            return new Point2D.Double(this.x1, this.y1);
        }

        @Override // java.awt.geom.QuadCurve2D
        public double getCtrlX() {
            return this.ctrlx;
        }

        @Override // java.awt.geom.QuadCurve2D
        public double getCtrlY() {
            return this.ctrly;
        }

        @Override // java.awt.geom.QuadCurve2D
        public Point2D getCtrlPt() {
            return new Point2D.Double(this.ctrlx, this.ctrly);
        }

        @Override // java.awt.geom.QuadCurve2D
        public double getX2() {
            return this.x2;
        }

        @Override // java.awt.geom.QuadCurve2D
        public double getY2() {
            return this.y2;
        }

        @Override // java.awt.geom.QuadCurve2D
        public Point2D getP2() {
            return new Point2D.Double(this.x2, this.y2);
        }

        @Override // java.awt.geom.QuadCurve2D
        public void setCurve(double d2, double d3, double d4, double d5, double d6, double d7) {
            this.x1 = d2;
            this.y1 = d3;
            this.ctrlx = d4;
            this.ctrly = d5;
            this.x2 = d6;
            this.y2 = d7;
        }

        @Override // java.awt.Shape
        public Rectangle2D getBounds2D() {
            double dMin = Math.min(Math.min(this.x1, this.x2), this.ctrlx);
            double dMin2 = Math.min(Math.min(this.y1, this.y2), this.ctrly);
            return new Rectangle2D.Double(dMin, dMin2, Math.max(Math.max(this.x1, this.x2), this.ctrlx) - dMin, Math.max(Math.max(this.y1, this.y2), this.ctrly) - dMin2);
        }
    }

    protected QuadCurve2D() {
    }

    public void setCurve(double[] dArr, int i2) {
        setCurve(dArr[i2 + 0], dArr[i2 + 1], dArr[i2 + 2], dArr[i2 + 3], dArr[i2 + 4], dArr[i2 + 5]);
    }

    public void setCurve(Point2D point2D, Point2D point2D2, Point2D point2D3) {
        setCurve(point2D.getX(), point2D.getY(), point2D2.getX(), point2D2.getY(), point2D3.getX(), point2D3.getY());
    }

    public void setCurve(Point2D[] point2DArr, int i2) {
        setCurve(point2DArr[i2 + 0].getX(), point2DArr[i2 + 0].getY(), point2DArr[i2 + 1].getX(), point2DArr[i2 + 1].getY(), point2DArr[i2 + 2].getX(), point2DArr[i2 + 2].getY());
    }

    public void setCurve(QuadCurve2D quadCurve2D) {
        setCurve(quadCurve2D.getX1(), quadCurve2D.getY1(), quadCurve2D.getCtrlX(), quadCurve2D.getCtrlY(), quadCurve2D.getX2(), quadCurve2D.getY2());
    }

    public static double getFlatnessSq(double d2, double d3, double d4, double d5, double d6, double d7) {
        return Line2D.ptSegDistSq(d2, d3, d6, d7, d4, d5);
    }

    public static double getFlatness(double d2, double d3, double d4, double d5, double d6, double d7) {
        return Line2D.ptSegDist(d2, d3, d6, d7, d4, d5);
    }

    public static double getFlatnessSq(double[] dArr, int i2) {
        return Line2D.ptSegDistSq(dArr[i2 + 0], dArr[i2 + 1], dArr[i2 + 4], dArr[i2 + 5], dArr[i2 + 2], dArr[i2 + 3]);
    }

    public static double getFlatness(double[] dArr, int i2) {
        return Line2D.ptSegDist(dArr[i2 + 0], dArr[i2 + 1], dArr[i2 + 4], dArr[i2 + 5], dArr[i2 + 2], dArr[i2 + 3]);
    }

    public double getFlatnessSq() {
        return Line2D.ptSegDistSq(getX1(), getY1(), getX2(), getY2(), getCtrlX(), getCtrlY());
    }

    public double getFlatness() {
        return Line2D.ptSegDist(getX1(), getY1(), getX2(), getY2(), getCtrlX(), getCtrlY());
    }

    public void subdivide(QuadCurve2D quadCurve2D, QuadCurve2D quadCurve2D2) {
        subdivide(this, quadCurve2D, quadCurve2D2);
    }

    public static void subdivide(QuadCurve2D quadCurve2D, QuadCurve2D quadCurve2D2, QuadCurve2D quadCurve2D3) {
        double x1 = quadCurve2D.getX1();
        double y1 = quadCurve2D.getY1();
        double ctrlX = quadCurve2D.getCtrlX();
        double ctrlY = quadCurve2D.getCtrlY();
        double x2 = quadCurve2D.getX2();
        double y2 = quadCurve2D.getY2();
        double d2 = (x1 + ctrlX) / 2.0d;
        double d3 = (y1 + ctrlY) / 2.0d;
        double d4 = (x2 + ctrlX) / 2.0d;
        double d5 = (y2 + ctrlY) / 2.0d;
        double d6 = (d2 + d4) / 2.0d;
        double d7 = (d3 + d5) / 2.0d;
        if (quadCurve2D2 != null) {
            quadCurve2D2.setCurve(x1, y1, d2, d3, d6, d7);
        }
        if (quadCurve2D3 != null) {
            quadCurve2D3.setCurve(d6, d7, d4, d5, x2, y2);
        }
    }

    public static void subdivide(double[] dArr, int i2, double[] dArr2, int i3, double[] dArr3, int i4) {
        double d2 = dArr[i2 + 0];
        double d3 = dArr[i2 + 1];
        double d4 = dArr[i2 + 2];
        double d5 = dArr[i2 + 3];
        double d6 = dArr[i2 + 4];
        double d7 = dArr[i2 + 5];
        if (dArr2 != null) {
            dArr2[i3 + 0] = d2;
            dArr2[i3 + 1] = d3;
        }
        if (dArr3 != null) {
            dArr3[i4 + 4] = d6;
            dArr3[i4 + 5] = d7;
        }
        double d8 = (d2 + d4) / 2.0d;
        double d9 = (d3 + d5) / 2.0d;
        double d10 = (d6 + d4) / 2.0d;
        double d11 = (d7 + d5) / 2.0d;
        double d12 = (d8 + d10) / 2.0d;
        double d13 = (d9 + d11) / 2.0d;
        if (dArr2 != null) {
            dArr2[i3 + 2] = d8;
            dArr2[i3 + 3] = d9;
            dArr2[i3 + 4] = d12;
            dArr2[i3 + 5] = d13;
        }
        if (dArr3 != null) {
            dArr3[i4 + 0] = d12;
            dArr3[i4 + 1] = d13;
            dArr3[i4 + 2] = d10;
            dArr3[i4 + 3] = d11;
        }
    }

    public static int solveQuadratic(double[] dArr) {
        return solveQuadratic(dArr, dArr);
    }

    public static int solveQuadratic(double[] dArr, double[] dArr2) {
        int i2;
        double d2 = dArr[2];
        double d3 = dArr[1];
        double d4 = dArr[0];
        if (d2 != 0.0d) {
            double d5 = (d3 * d3) - ((4.0d * d2) * d4);
            if (d5 < 0.0d) {
                return 0;
            }
            double dSqrt = Math.sqrt(d5);
            if (d3 < 0.0d) {
                dSqrt = -dSqrt;
            }
            double d6 = (d3 + dSqrt) / (-2.0d);
            i2 = 0 + 1;
            dArr2[0] = d6 / d2;
            if (d6 != 0.0d) {
                i2++;
                dArr2[i2] = d4 / d6;
            }
        } else if (d3 != 0.0d) {
            i2 = 0 + 1;
            dArr2[0] = (-d4) / d3;
        } else {
            return -1;
        }
        return i2;
    }

    @Override // java.awt.Shape
    public boolean contains(double d2, double d3) {
        double x1 = getX1();
        double y1 = getY1();
        double ctrlX = getCtrlX();
        double ctrlY = getCtrlY();
        double x2 = getX2();
        double y2 = getY2();
        double d4 = (x1 - (2.0d * ctrlX)) + x2;
        double d5 = (y1 - (2.0d * ctrlY)) + y2;
        double d6 = d2 - x1;
        double d7 = d3 - y1;
        double d8 = x2 - x1;
        double d9 = y2 - y1;
        double d10 = ((d6 * d5) - (d7 * d4)) / ((d8 * d5) - (d9 * d4));
        if (d10 < 0.0d || d10 > 1.0d || d10 != d10) {
            return false;
        }
        double d11 = (d4 * d10 * d10) + (2.0d * (ctrlX - x1) * d10) + x1;
        double d12 = (d5 * d10 * d10) + (2.0d * (ctrlY - y1) * d10) + y1;
        double d13 = (d8 * d10) + x1;
        double d14 = (d9 * d10) + y1;
        return (d2 >= d11 && d2 < d13) || (d2 >= d13 && d2 < d11) || ((d3 >= d12 && d3 < d14) || (d3 >= d14 && d3 < d12));
    }

    @Override // java.awt.Shape
    public boolean contains(Point2D point2D) {
        return contains(point2D.getX(), point2D.getY());
    }

    private static void fillEqn(double[] dArr, double d2, double d3, double d4, double d5) {
        dArr[0] = d3 - d2;
        dArr[1] = ((d4 + d4) - d3) - d3;
        dArr[2] = ((d3 - d4) - d4) + d5;
    }

    /* JADX WARN: Removed duplicated region for block: B:12:0x0027  */
    /* JADX WARN: Removed duplicated region for block: B:19:0x003c  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private static int evalQuadratic(double[] r9, int r10, boolean r11, boolean r12, double[] r13, double r14, double r16, double r18) {
        /*
            r0 = 0
            r20 = r0
            r0 = 0
            r21 = r0
        L6:
            r0 = r21
            r1 = r10
            if (r0 >= r1) goto L87
            r0 = r9
            r1 = r21
            r0 = r0[r1]
            r22 = r0
            r0 = r11
            if (r0 == 0) goto L20
            r0 = r22
            r1 = 0
            int r0 = (r0 > r1 ? 1 : (r0 == r1 ? 0 : -1))
            if (r0 < 0) goto L81
            goto L27
        L20:
            r0 = r22
            r1 = 0
            int r0 = (r0 > r1 ? 1 : (r0 == r1 ? 0 : -1))
            if (r0 <= 0) goto L81
        L27:
            r0 = r12
            if (r0 == 0) goto L35
            r0 = r22
            r1 = 4607182418800017408(0x3ff0000000000000, double:1.0)
            int r0 = (r0 > r1 ? 1 : (r0 == r1 ? 0 : -1))
            if (r0 > 0) goto L81
            goto L3c
        L35:
            r0 = r22
            r1 = 4607182418800017408(0x3ff0000000000000, double:1.0)
            int r0 = (r0 > r1 ? 1 : (r0 == r1 ? 0 : -1))
            if (r0 >= 0) goto L81
        L3c:
            r0 = r13
            if (r0 == 0) goto L56
            r0 = r13
            r1 = 1
            r0 = r0[r1]
            r1 = 4611686018427387904(0x4000000000000000, double:2.0)
            r2 = r13
            r3 = 2
            r2 = r2[r3]
            double r1 = r1 * r2
            r2 = r22
            double r1 = r1 * r2
            double r0 = r0 + r1
            r1 = 0
            int r0 = (r0 > r1 ? 1 : (r0 == r1 ? 0 : -1))
            if (r0 == 0) goto L81
        L56:
            r0 = 4607182418800017408(0x3ff0000000000000, double:1.0)
            r1 = r22
            double r0 = r0 - r1
            r24 = r0
            r0 = r9
            r1 = r20
            int r20 = r20 + 1
            r2 = r14
            r3 = r24
            double r2 = r2 * r3
            r3 = r24
            double r2 = r2 * r3
            r3 = 4611686018427387904(0x4000000000000000, double:2.0)
            r4 = r16
            double r3 = r3 * r4
            r4 = r22
            double r3 = r3 * r4
            r4 = r24
            double r3 = r3 * r4
            double r2 = r2 + r3
            r3 = r18
            r4 = r22
            double r3 = r3 * r4
            r4 = r22
            double r3 = r3 * r4
            double r2 = r2 + r3
            r0[r1] = r2
        L81:
            int r21 = r21 + 1
            goto L6
        L87:
            r0 = r20
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: java.awt.geom.QuadCurve2D.evalQuadratic(double[], int, boolean, boolean, double[], double, double, double):int");
    }

    private static int getTag(double d2, double d3, double d4) {
        if (d2 <= d3) {
            return d2 < d3 ? -2 : -1;
        }
        if (d2 >= d4) {
            return d2 > d4 ? 2 : 1;
        }
        return 0;
    }

    private static boolean inwards(int i2, int i3, int i4) {
        switch (i2) {
            case -2:
            case 2:
            default:
                return false;
            case -1:
                return i3 >= 0 || i4 >= 0;
            case 0:
                return true;
            case 1:
                return i3 <= 0 || i4 <= 0;
        }
    }

    @Override // java.awt.Shape
    public boolean intersects(double d2, double d3, double d4, double d5) {
        int tag;
        int tag2;
        if (d4 <= 0.0d || d5 <= 0.0d) {
            return false;
        }
        double x1 = getX1();
        double y1 = getY1();
        int tag3 = getTag(x1, d2, d2 + d4);
        int tag4 = getTag(y1, d3, d3 + d5);
        if (tag3 == 0 && tag4 == 0) {
            return true;
        }
        double x2 = getX2();
        double y2 = getY2();
        int tag5 = getTag(x2, d2, d2 + d4);
        int tag6 = getTag(y2, d3, d3 + d5);
        if (tag5 == 0 && tag6 == 0) {
            return true;
        }
        double ctrlX = getCtrlX();
        double ctrlY = getCtrlY();
        int tag7 = getTag(ctrlX, d2, d2 + d4);
        int tag8 = getTag(ctrlY, d3, d3 + d5);
        if (tag3 < 0 && tag5 < 0 && tag7 < 0) {
            return false;
        }
        if (tag4 < 0 && tag6 < 0 && tag8 < 0) {
            return false;
        }
        if (tag3 > 0 && tag5 > 0 && tag7 > 0) {
            return false;
        }
        if (tag4 > 0 && tag6 > 0 && tag8 > 0) {
            return false;
        }
        if (inwards(tag3, tag5, tag7) && inwards(tag4, tag6, tag8)) {
            return true;
        }
        if (inwards(tag5, tag3, tag7) && inwards(tag6, tag4, tag8)) {
            return true;
        }
        boolean z2 = tag3 * tag5 <= 0;
        boolean z3 = tag4 * tag6 <= 0;
        if (tag3 == 0 && tag5 == 0 && z3) {
            return true;
        }
        if (tag4 == 0 && tag6 == 0 && z2) {
            return true;
        }
        double[] dArr = new double[3];
        double[] dArr2 = new double[3];
        if (!z3) {
            fillEqn(dArr, tag4 < 0 ? d3 : d3 + d5, y1, ctrlY, y2);
            return solveQuadratic(dArr, dArr2) == 2 && evalQuadratic(dArr2, 2, true, true, null, x1, ctrlX, x2) == 2 && getTag(dArr2[0], d2, d2 + d4) * getTag(dArr2[1], d2, d2 + d4) <= 0;
        }
        if (!z2) {
            fillEqn(dArr, tag3 < 0 ? d2 : d2 + d4, x1, ctrlX, x2);
            return solveQuadratic(dArr, dArr2) == 2 && evalQuadratic(dArr2, 2, true, true, null, y1, ctrlY, y2) == 2 && getTag(dArr2[0], d3, d3 + d5) * getTag(dArr2[1], d3, d3 + d5) <= 0;
        }
        double d6 = x2 - x1;
        double d7 = y2 - y1;
        double d8 = (y2 * x1) - (x2 * y1);
        if (tag4 == 0) {
            tag = tag3;
        } else {
            tag = getTag((d8 + (d6 * (tag4 < 0 ? d3 : d3 + d5))) / d7, d2, d2 + d4);
        }
        if (tag6 == 0) {
            tag2 = tag5;
        } else {
            tag2 = getTag((d8 + (d6 * (tag6 < 0 ? d3 : d3 + d5))) / d7, d2, d2 + d4);
        }
        if (tag * tag2 <= 0) {
            return true;
        }
        int i2 = tag * tag3 <= 0 ? tag4 : tag6;
        fillEqn(dArr, tag2 < 0 ? d2 : d2 + d4, x1, ctrlX, x2);
        evalQuadratic(dArr2, solveQuadratic(dArr, dArr2), true, true, null, y1, ctrlY, y2);
        return i2 * getTag(dArr2[0], d3, d3 + d5) <= 0;
    }

    @Override // java.awt.Shape
    public boolean intersects(Rectangle2D rectangle2D) {
        return intersects(rectangle2D.getX(), rectangle2D.getY(), rectangle2D.getWidth(), rectangle2D.getHeight());
    }

    @Override // java.awt.Shape
    public boolean contains(double d2, double d3, double d4, double d5) {
        return d4 > 0.0d && d5 > 0.0d && contains(d2, d3) && contains(d2 + d4, d3) && contains(d2 + d4, d3 + d5) && contains(d2, d3 + d5);
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
        return new QuadIterator(this, affineTransform);
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
