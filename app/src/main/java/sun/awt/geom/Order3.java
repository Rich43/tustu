package sun.awt.geom;

import java.awt.geom.QuadCurve2D;
import java.awt.geom.Rectangle2D;
import java.util.Vector;

/* loaded from: rt.jar:sun/awt/geom/Order3.class */
final class Order3 extends Curve {
    private double x0;
    private double y0;
    private double cx0;
    private double cy0;
    private double cx1;
    private double cy1;
    private double x1;
    private double y1;
    private double xmin;
    private double xmax;
    private double xcoeff0;
    private double xcoeff1;
    private double xcoeff2;
    private double xcoeff3;
    private double ycoeff0;
    private double ycoeff1;
    private double ycoeff2;
    private double ycoeff3;
    private double TforY1;
    private double YforT1;
    private double TforY2;
    private double YforT2;
    private double TforY3;
    private double YforT3;

    public static void insert(Vector vector, double[] dArr, double d2, double d3, double d4, double d5, double d6, double d7, double d8, double d9, int i2) {
        int horizontalParams = getHorizontalParams(d3, d5, d7, d9, dArr);
        if (horizontalParams == 0) {
            addInstance(vector, d2, d3, d4, d5, d6, d7, d8, d9, i2);
            return;
        }
        dArr[3] = d2;
        dArr[4] = d3;
        dArr[5] = d4;
        dArr[6] = d5;
        dArr[7] = d6;
        dArr[8] = d7;
        dArr[9] = d8;
        dArr[10] = d9;
        double d10 = dArr[0];
        if (horizontalParams > 1 && d10 > dArr[1]) {
            dArr[0] = dArr[1];
            dArr[1] = d10;
            d10 = dArr[0];
        }
        split(dArr, 3, d10);
        if (horizontalParams > 1) {
            split(dArr, 9, (dArr[1] - d10) / (1.0d - d10));
        }
        int i3 = 3;
        if (i2 == -1) {
            i3 = 3 + (horizontalParams * 6);
        }
        while (horizontalParams >= 0) {
            addInstance(vector, dArr[i3 + 0], dArr[i3 + 1], dArr[i3 + 2], dArr[i3 + 3], dArr[i3 + 4], dArr[i3 + 5], dArr[i3 + 6], dArr[i3 + 7], i2);
            horizontalParams--;
            if (i2 == 1) {
                i3 += 6;
            } else {
                i3 -= 6;
            }
        }
    }

    public static void addInstance(Vector vector, double d2, double d3, double d4, double d5, double d6, double d7, double d8, double d9, int i2) {
        if (d3 > d9) {
            vector.add(new Order3(d8, d9, d6, d7, d4, d5, d2, d3, -i2));
        } else if (d9 > d3) {
            vector.add(new Order3(d2, d3, d4, d5, d6, d7, d8, d9, i2));
        }
    }

    public static int getHorizontalParams(double d2, double d3, double d4, double d5, double[] dArr) {
        if (d2 <= d3 && d3 <= d4 && d4 <= d5) {
            return 0;
        }
        double d6 = d4 - d3;
        double d7 = d3 - d2;
        dArr[0] = d7;
        dArr[1] = (d6 - d7) * 2.0d;
        dArr[2] = (((d5 - d4) - d6) - d6) + d7;
        int iSolveQuadratic = QuadCurve2D.solveQuadratic(dArr, dArr);
        int i2 = 0;
        for (int i3 = 0; i3 < iSolveQuadratic; i3++) {
            double d8 = dArr[i3];
            if (d8 > 0.0d && d8 < 1.0d) {
                if (i2 < i3) {
                    dArr[i2] = d8;
                }
                i2++;
            }
        }
        return i2;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static void split(double[] dArr, int i2, double d2) {
        dArr[i2 + 12] = dArr[i2 + 6];
        dArr[i2 + 13] = dArr[i2 + 7];
        double d3 = dArr[i2 + 4];
        double d4 = dArr[i2 + 5];
        double d5 = d3 + ((dArr - d3) * d2);
        double d6 = d4 + ((dArr - d4) * d2);
        double d7 = dArr[i2 + 0];
        double d8 = dArr[i2 + 1];
        double d9 = dArr[i2 + 2];
        double d10 = dArr[i2 + 3];
        double d11 = d7 + ((d9 - d7) * d2);
        double d12 = d8 + ((d10 - d8) * d2);
        double d13 = d9 + ((d3 - d9) * d2);
        double d14 = d10 + ((d4 - d10) * d2);
        double d15 = d13 + ((d5 - d13) * d2);
        double d16 = d14 + ((d6 - d14) * d2);
        double d17 = d11 + ((d13 - d11) * d2);
        double d18 = d12 + ((d14 - d12) * d2);
        dArr[i2 + 2] = d11;
        dArr[i2 + 3] = d12;
        dArr[i2 + 4] = d17;
        dArr[i2 + 5] = d18;
        dArr[i2 + 6] = d17 + ((d15 - d17) * d2);
        dArr[i2 + 7] = d18 + ((d16 - d18) * d2);
        dArr[i2 + 8] = d15;
        dArr[i2 + 9] = d16;
        dArr[i2 + 10] = d5;
        dArr[i2 + 11] = d6;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public Order3(double d2, double d3, double d4, double d5, double d6, double d7, double d8, double d9, int i2) {
        super(i2);
        d5 = d5 < d3 ? d3 : d5;
        d7 = d7 > d9 ? d9 : d7;
        this.x0 = d2;
        this.y0 = d3;
        this.cx0 = d4;
        this.cy0 = d5;
        this.cx1 = d6;
        this.cy1 = d7;
        this.x1 = d8;
        this.y1 = d9;
        this.xmin = Math.min(Math.min(d2, d8), Math.min(d4, d6));
        this.xmax = Math.max(Math.max(d2, d8), Math.max(d4, d6));
        this.xcoeff0 = d2;
        this.xcoeff1 = (d4 - d2) * 3.0d;
        this.xcoeff2 = (((d6 - d4) - d4) + d2) * 3.0d;
        this.xcoeff3 = (d8 - ((d6 - d4) * 3.0d)) - d2;
        this.ycoeff0 = d3;
        this.ycoeff1 = (d5 - d3) * 3.0d;
        this.ycoeff2 = (((d7 - d5) - d5) + d3) * 3.0d;
        this.ycoeff3 = (d9 - ((d7 - d5) * 3.0d)) - d3;
        this.YforT3 = d3;
        this.YforT2 = d3;
        d3.YforT1 = this;
    }

    @Override // sun.awt.geom.Curve
    public int getOrder() {
        return 3;
    }

    @Override // sun.awt.geom.Curve
    public double getXTop() {
        return this.x0;
    }

    @Override // sun.awt.geom.Curve
    public double getYTop() {
        return this.y0;
    }

    @Override // sun.awt.geom.Curve
    public double getXBot() {
        return this.x1;
    }

    @Override // sun.awt.geom.Curve
    public double getYBot() {
        return this.y1;
    }

    @Override // sun.awt.geom.Curve
    public double getXMin() {
        return this.xmin;
    }

    @Override // sun.awt.geom.Curve
    public double getXMax() {
        return this.xmax;
    }

    @Override // sun.awt.geom.Curve
    public double getX0() {
        return this.direction == 1 ? this.x0 : this.x1;
    }

    @Override // sun.awt.geom.Curve
    public double getY0() {
        return this.direction == 1 ? this.y0 : this.y1;
    }

    public double getCX0() {
        return this.direction == 1 ? this.cx0 : this.cx1;
    }

    public double getCY0() {
        return this.direction == 1 ? this.cy0 : this.cy1;
    }

    public double getCX1() {
        return this.direction == -1 ? this.cx0 : this.cx1;
    }

    public double getCY1() {
        return this.direction == -1 ? this.cy0 : this.cy1;
    }

    @Override // sun.awt.geom.Curve
    public double getX1() {
        return this.direction == -1 ? this.x0 : this.x1;
    }

    @Override // sun.awt.geom.Curve
    public double getY1() {
        return this.direction == -1 ? this.y0 : this.y1;
    }

    @Override // sun.awt.geom.Curve
    public double TforY(double d2) {
        double dRefine;
        if (d2 <= this.y0) {
            return 0.0d;
        }
        if (d2 >= this.y1) {
            return 1.0d;
        }
        if (d2 == this.YforT1) {
            return this.TforY1;
        }
        if (d2 == this.YforT2) {
            return this.TforY2;
        }
        if (d2 == this.YforT3) {
            return this.TforY3;
        }
        if (this.ycoeff3 == 0.0d) {
            return Order2.TforY(d2, this.ycoeff0, this.ycoeff1, this.ycoeff2);
        }
        double d3 = this.ycoeff2 / this.ycoeff3;
        double d4 = this.ycoeff1 / this.ycoeff3;
        double d5 = (this.ycoeff0 - d2) / this.ycoeff3;
        double d6 = ((d3 * d3) - (3.0d * d4)) / 9.0d;
        double d7 = (((((2.0d * d3) * d3) * d3) - ((9.0d * d3) * d4)) + (27.0d * d5)) / 54.0d;
        double d8 = d7 * d7;
        double d9 = d6 * d6 * d6;
        double d10 = d3 / 3.0d;
        if (d8 < d9) {
            double dAcos = Math.acos(d7 / Math.sqrt(d9));
            double dSqrt = (-2.0d) * Math.sqrt(d6);
            dRefine = refine(d3, d4, d5, d2, (dSqrt * Math.cos(dAcos / 3.0d)) - d10);
            if (dRefine < 0.0d) {
                dRefine = refine(d3, d4, d5, d2, (dSqrt * Math.cos((dAcos + 6.283185307179586d) / 3.0d)) - d10);
            }
            if (dRefine < 0.0d) {
                dRefine = refine(d3, d4, d5, d2, (dSqrt * Math.cos((dAcos - 6.283185307179586d) / 3.0d)) - d10);
            }
        } else {
            boolean z2 = d7 < 0.0d;
            double dSqrt2 = Math.sqrt(d8 - d9);
            if (z2) {
                d7 = -d7;
            }
            double dPow = Math.pow(d7 + dSqrt2, 0.3333333333333333d);
            if (!z2) {
                dPow = -dPow;
            }
            dRefine = refine(d3, d4, d5, d2, (dPow + (dPow == 0.0d ? 0.0d : d6 / dPow)) - d10);
        }
        if (dRefine < 0.0d) {
            double d11 = 0.0d;
            double d12 = 1.0d;
            while (true) {
                dRefine = (d11 + d12) / 2.0d;
                if (dRefine != d11 && dRefine != d12) {
                    double dYforT = YforT(dRefine);
                    if (dYforT >= d2) {
                        if (dYforT <= d2) {
                            break;
                        }
                        d12 = dRefine;
                    } else {
                        d11 = dRefine;
                    }
                } else {
                    break;
                }
            }
        }
        if (dRefine >= 0.0d) {
            this.TforY3 = this.TforY2;
            this.YforT3 = this.YforT2;
            this.TforY2 = this.TforY1;
            this.YforT2 = this.YforT1;
            this.TforY1 = dRefine;
            this.YforT1 = d2;
        }
        return dRefine;
    }

    public double refine(double d2, double d3, double d4, double d5, double d6) {
        double d7;
        double d8;
        if (d6 < -0.1d || d6 > 1.1d) {
            return -1.0d;
        }
        double dYforT = YforT(d6);
        if (dYforT < d5) {
            d7 = d6;
            d8 = 1.0d;
        } else {
            d7 = 0.0d;
            d8 = d6;
        }
        boolean z2 = true;
        while (dYforT != d5) {
            if (!z2) {
                double d9 = (d7 + d8) / 2.0d;
                if (d9 == d7 || d9 == d8) {
                    break;
                }
                d6 = d9;
            } else {
                double dDYforT = dYforT(d6, 1);
                if (dDYforT == 0.0d) {
                    z2 = false;
                } else {
                    double d10 = d6 + ((d5 - dYforT) / dDYforT);
                    if (d10 == d6 || d10 <= d7 || d10 >= d8) {
                        z2 = false;
                    } else {
                        d6 = d10;
                    }
                }
            }
            dYforT = YforT(d6);
            if (dYforT >= d5) {
                if (dYforT <= d5) {
                    break;
                }
                d8 = d6;
            } else {
                d7 = d6;
            }
        }
        if (d6 > 1.0d) {
            return -1.0d;
        }
        return d6;
    }

    @Override // sun.awt.geom.Curve
    public double XforY(double d2) {
        if (d2 <= this.y0) {
            return this.x0;
        }
        if (d2 >= this.y1) {
            return this.x1;
        }
        return XforT(TforY(d2));
    }

    @Override // sun.awt.geom.Curve
    public double XforT(double d2) {
        return (((((this.xcoeff3 * d2) + this.xcoeff2) * d2) + this.xcoeff1) * d2) + this.xcoeff0;
    }

    @Override // sun.awt.geom.Curve
    public double YforT(double d2) {
        return (((((this.ycoeff3 * d2) + this.ycoeff2) * d2) + this.ycoeff1) * d2) + this.ycoeff0;
    }

    @Override // sun.awt.geom.Curve
    public double dXforT(double d2, int i2) {
        switch (i2) {
            case 0:
                return (((((this.xcoeff3 * d2) + this.xcoeff2) * d2) + this.xcoeff1) * d2) + this.xcoeff0;
            case 1:
                return (((3.0d * this.xcoeff3 * d2) + (2.0d * this.xcoeff2)) * d2) + this.xcoeff1;
            case 2:
                return (6.0d * this.xcoeff3 * d2) + (2.0d * this.xcoeff2);
            case 3:
                return 6.0d * this.xcoeff3;
            default:
                return 0.0d;
        }
    }

    @Override // sun.awt.geom.Curve
    public double dYforT(double d2, int i2) {
        switch (i2) {
            case 0:
                return (((((this.ycoeff3 * d2) + this.ycoeff2) * d2) + this.ycoeff1) * d2) + this.ycoeff0;
            case 1:
                return (((3.0d * this.ycoeff3 * d2) + (2.0d * this.ycoeff2)) * d2) + this.ycoeff1;
            case 2:
                return (6.0d * this.ycoeff3 * d2) + (2.0d * this.ycoeff2);
            case 3:
                return 6.0d * this.ycoeff3;
            default:
                return 0.0d;
        }
    }

    @Override // sun.awt.geom.Curve
    public double nextVertical(double d2, double d3) {
        double[] dArr = {this.xcoeff1, 2.0d * this.xcoeff2, 3.0d * this.xcoeff3};
        int iSolveQuadratic = QuadCurve2D.solveQuadratic(dArr, dArr);
        for (int i2 = 0; i2 < iSolveQuadratic; i2++) {
            if (dArr[i2] > d2 && dArr[i2] < d3) {
                d3 = dArr[i2];
            }
        }
        return d3;
    }

    @Override // sun.awt.geom.Curve
    public void enlarge(Rectangle2D rectangle2D) {
        rectangle2D.add(this.x0, this.y0);
        double[] dArr = {this.xcoeff1, 2.0d * this.xcoeff2, 3.0d * this.xcoeff3};
        int iSolveQuadratic = QuadCurve2D.solveQuadratic(dArr, dArr);
        for (int i2 = 0; i2 < iSolveQuadratic; i2++) {
            double d2 = dArr[i2];
            if (d2 > 0.0d && d2 < 1.0d) {
                rectangle2D.add(XforT(d2), YforT(d2));
            }
        }
        rectangle2D.add(this.x1, this.y1);
    }

    @Override // sun.awt.geom.Curve
    public Curve getSubCurve(double d2, double d3, int i2) {
        int i3;
        if (d2 <= this.y0 && d3 >= this.y1) {
            return getWithDirection(i2);
        }
        double dTforY = TforY(d2);
        double dTforY2 = TforY(d3);
        double[] dArr = {this.x0, this.y0, this.cx0, this.cy0, this.cx1, this.cy1, this.x1, this.y1, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d};
        if (dTforY > dTforY2) {
            dTforY = dTforY2;
            dTforY2 = dTforY;
        }
        if (dTforY2 < 1.0d) {
            split(dArr, 0, dTforY2);
        }
        if (dTforY <= 0.0d) {
            i3 = 0;
        } else {
            split(dArr, 0, dTforY / dTforY2);
            i3 = 6;
        }
        return new Order3(dArr[i3 + 0], d2, dArr[i3 + 2], dArr[i3 + 3], dArr[i3 + 4], dArr[i3 + 5], dArr[i3 + 6], d3, i2);
    }

    @Override // sun.awt.geom.Curve
    public Curve getReversedCurve() {
        return new Order3(this.x0, this.y0, this.cx0, this.cy0, this.cx1, this.cy1, this.x1, this.y1, -this.direction);
    }

    @Override // sun.awt.geom.Curve
    public int getSegment(double[] dArr) {
        if (this.direction == 1) {
            dArr[0] = this.cx0;
            dArr[1] = this.cy0;
            dArr[2] = this.cx1;
            dArr[3] = this.cy1;
            dArr[4] = this.x1;
            dArr[5] = this.y1;
            return 3;
        }
        dArr[0] = this.cx1;
        dArr[1] = this.cy1;
        dArr[2] = this.cx0;
        dArr[3] = this.cy0;
        dArr[4] = this.x0;
        dArr[5] = this.y0;
        return 3;
    }

    @Override // sun.awt.geom.Curve
    public String controlPointString() {
        return "(" + round(getCX0()) + ", " + round(getCY0()) + "), (" + round(getCX1()) + ", " + round(getCY1()) + "), ";
    }
}
