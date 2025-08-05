package sun.awt.geom;

import java.awt.geom.Rectangle2D;
import java.util.Vector;

/* loaded from: rt.jar:sun/awt/geom/Order2.class */
final class Order2 extends Curve {
    private double x0;
    private double y0;
    private double cx0;
    private double cy0;
    private double x1;
    private double y1;
    private double xmin;
    private double xmax;
    private double xcoeff0;
    private double xcoeff1;
    private double xcoeff2;
    private double ycoeff0;
    private double ycoeff1;
    private double ycoeff2;

    public static void insert(Vector vector, double[] dArr, double d2, double d3, double d4, double d5, double d6, double d7, int i2) {
        if (getHorizontalParams(d3, d5, d7, dArr) == 0) {
            addInstance(vector, d2, d3, d4, d5, d6, d7, i2);
            return;
        }
        double d8 = dArr[0];
        dArr[0] = d2;
        dArr[1] = d3;
        dArr[2] = d4;
        dArr[3] = d5;
        dArr[4] = d6;
        dArr[5] = d7;
        split(dArr, 0, d8);
        int i3 = i2 == 1 ? 0 : 4;
        int i4 = 4 - i3;
        addInstance(vector, dArr[i3], dArr[i3 + 1], dArr[i3 + 2], dArr[i3 + 3], dArr[i3 + 4], dArr[i3 + 5], i2);
        addInstance(vector, dArr[i4], dArr[i4 + 1], dArr[i4 + 2], dArr[i4 + 3], dArr[i4 + 4], dArr[i4 + 5], i2);
    }

    public static void addInstance(Vector vector, double d2, double d3, double d4, double d5, double d6, double d7, int i2) {
        if (d3 > d7) {
            vector.add(new Order2(d6, d7, d4, d5, d2, d3, -i2));
        } else if (d7 > d3) {
            vector.add(new Order2(d2, d3, d4, d5, d6, d7, i2));
        }
    }

    public static int getHorizontalParams(double d2, double d3, double d4, double[] dArr) {
        if (d2 <= d3 && d3 <= d4) {
            return 0;
        }
        double d5 = d2 - d3;
        double d6 = d5 + (d4 - d3);
        if (d6 == 0.0d) {
            return 0;
        }
        double d7 = d5 / d6;
        if (d7 <= 0.0d || d7 >= 1.0d) {
            return 0;
        }
        dArr[0] = d7;
        return 1;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static void split(double[] dArr, int i2, double d2) {
        dArr[i2 + 8] = dArr[i2 + 4];
        dArr[i2 + 9] = dArr[i2 + 5];
        double d3 = dArr[i2 + 2];
        double d4 = dArr[i2 + 3];
        double d5 = d3 + ((dArr - d3) * d2);
        double d6 = d4 + ((dArr - d4) * d2);
        double d7 = dArr[i2 + 0];
        double d8 = dArr[i2 + 1];
        double d9 = d7 + ((d3 - d7) * d2);
        double d10 = d8 + ((d4 - d8) * d2);
        double d11 = d9 + ((d5 - d9) * d2);
        dArr[i2 + 2] = d9;
        dArr[i2 + 3] = d10;
        dArr[i2 + 4] = d11;
        dArr[i2 + 5] = d10 + ((d6 - d10) * d2);
        dArr[i2 + 6] = d5;
        dArr[i2 + 7] = d6;
    }

    public Order2(double d2, double d3, double d4, double d5, double d6, double d7, int i2) {
        super(i2);
        if (d5 < d3) {
            d5 = d3;
        } else if (d5 > d7) {
            d5 = d7;
        }
        this.x0 = d2;
        this.y0 = d3;
        this.cx0 = d4;
        this.cy0 = d5;
        this.x1 = d6;
        this.y1 = d7;
        this.xmin = Math.min(Math.min(d2, d6), d4);
        this.xmax = Math.max(Math.max(d2, d6), d4);
        this.xcoeff0 = d2;
        this.xcoeff1 = ((d4 + d4) - d2) - d2;
        this.xcoeff2 = ((d2 - d4) - d4) + d6;
        this.ycoeff0 = d3;
        this.ycoeff1 = ((d5 + d5) - d3) - d3;
        this.ycoeff2 = ((d3 - d5) - d5) + d7;
    }

    @Override // sun.awt.geom.Curve
    public int getOrder() {
        return 2;
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
        return this.cx0;
    }

    public double getCY0() {
        return this.cy0;
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
    public double TforY(double d2) {
        if (d2 <= this.y0) {
            return 0.0d;
        }
        if (d2 >= this.y1) {
            return 1.0d;
        }
        return TforY(d2, this.ycoeff0, this.ycoeff1, this.ycoeff2);
    }

    public static double TforY(double d2, double d3, double d4, double d5) {
        double d6 = d3 - d2;
        if (d5 == 0.0d) {
            double d7 = (-d6) / d4;
            if (d7 >= 0.0d && d7 <= 1.0d) {
                return d7;
            }
        } else {
            double d8 = (d4 * d4) - ((4.0d * d5) * d6);
            if (d8 >= 0.0d) {
                double dSqrt = Math.sqrt(d8);
                if (d4 < 0.0d) {
                    dSqrt = -dSqrt;
                }
                double d9 = (d4 + dSqrt) / (-2.0d);
                double d10 = d9 / d5;
                if (d10 >= 0.0d && d10 <= 1.0d) {
                    return d10;
                }
                if (d9 != 0.0d) {
                    double d11 = d6 / d9;
                    if (d11 >= 0.0d && d11 <= 1.0d) {
                        return d11;
                    }
                }
            }
        }
        return 0.0d < (d6 + ((d6 + d4) + d5)) / 2.0d ? 0.0d : 1.0d;
    }

    @Override // sun.awt.geom.Curve
    public double XforT(double d2) {
        return (((this.xcoeff2 * d2) + this.xcoeff1) * d2) + this.xcoeff0;
    }

    @Override // sun.awt.geom.Curve
    public double YforT(double d2) {
        return (((this.ycoeff2 * d2) + this.ycoeff1) * d2) + this.ycoeff0;
    }

    @Override // sun.awt.geom.Curve
    public double dXforT(double d2, int i2) {
        switch (i2) {
            case 0:
                return (((this.xcoeff2 * d2) + this.xcoeff1) * d2) + this.xcoeff0;
            case 1:
                return (2.0d * this.xcoeff2 * d2) + this.xcoeff1;
            case 2:
                return 2.0d * this.xcoeff2;
            default:
                return 0.0d;
        }
    }

    @Override // sun.awt.geom.Curve
    public double dYforT(double d2, int i2) {
        switch (i2) {
            case 0:
                return (((this.ycoeff2 * d2) + this.ycoeff1) * d2) + this.ycoeff0;
            case 1:
                return (2.0d * this.ycoeff2 * d2) + this.ycoeff1;
            case 2:
                return 2.0d * this.ycoeff2;
            default:
                return 0.0d;
        }
    }

    @Override // sun.awt.geom.Curve
    public double nextVertical(double d2, double d3) {
        double d4 = (-this.xcoeff1) / (2.0d * this.xcoeff2);
        if (d4 > d2 && d4 < d3) {
            return d4;
        }
        return d3;
    }

    @Override // sun.awt.geom.Curve
    public void enlarge(Rectangle2D rectangle2D) {
        rectangle2D.add(this.x0, this.y0);
        double d2 = (-this.xcoeff1) / (2.0d * this.xcoeff2);
        if (d2 > 0.0d && d2 < 1.0d) {
            rectangle2D.add(XforT(d2), YforT(d2));
        }
        rectangle2D.add(this.x1, this.y1);
    }

    @Override // sun.awt.geom.Curve
    public Curve getSubCurve(double d2, double d3, int i2) {
        double dTforY;
        double dTforY2;
        int i3;
        if (d2 > this.y0) {
            dTforY = TforY(d2, this.ycoeff0, this.ycoeff1, this.ycoeff2);
        } else {
            if (d3 >= this.y1) {
                return getWithDirection(i2);
            }
            dTforY = 0.0d;
        }
        if (d3 >= this.y1) {
            dTforY2 = 1.0d;
        } else {
            dTforY2 = TforY(d3, this.ycoeff0, this.ycoeff1, this.ycoeff2);
        }
        double[] dArr = {this.x0, this.y0, this.cx0, this.cy0, this.x1, this.y1, 0.0d, 0.0d, 0.0d, 0.0d};
        if (dTforY2 < 1.0d) {
            split(dArr, 0, dTforY2);
        }
        if (dTforY <= 0.0d) {
            i3 = 0;
        } else {
            split(dArr, 0, dTforY / dTforY2);
            i3 = 4;
        }
        return new Order2(dArr[i3 + 0], d2, dArr[i3 + 2], dArr[i3 + 3], dArr[i3 + 4], d3, i2);
    }

    @Override // sun.awt.geom.Curve
    public Curve getReversedCurve() {
        return new Order2(this.x0, this.y0, this.cx0, this.cy0, this.x1, this.y1, -this.direction);
    }

    @Override // sun.awt.geom.Curve
    public int getSegment(double[] dArr) {
        dArr[0] = this.cx0;
        dArr[1] = this.cy0;
        if (this.direction == 1) {
            dArr[2] = this.x1;
            dArr[3] = this.y1;
            return 2;
        }
        dArr[2] = this.x0;
        dArr[3] = this.y0;
        return 2;
    }

    @Override // sun.awt.geom.Curve
    public String controlPointString() {
        return "(" + round(this.cx0) + ", " + round(this.cy0) + "), ";
    }
}
