package com.sun.javafx.geom;

import java.util.Vector;

/* loaded from: jfxrt.jar:com/sun/javafx/geom/Order3.class */
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

    public static void insert(Vector curves, double[] tmp, double x0, double y0, double cx0, double cy0, double cx1, double cy1, double x1, double y1, int direction) {
        int numparams = getHorizontalParams(y0, cy0, cy1, y1, tmp);
        if (numparams == 0) {
            addInstance(curves, x0, y0, cx0, cy0, cx1, cy1, x1, y1, direction);
            return;
        }
        tmp[3] = x0;
        tmp[4] = y0;
        tmp[5] = cx0;
        tmp[6] = cy0;
        tmp[7] = cx1;
        tmp[8] = cy1;
        tmp[9] = x1;
        tmp[10] = y1;
        double t2 = tmp[0];
        if (numparams > 1 && t2 > tmp[1]) {
            tmp[0] = tmp[1];
            tmp[1] = t2;
            t2 = tmp[0];
        }
        split(tmp, 3, t2);
        if (numparams > 1) {
            split(tmp, 9, (tmp[1] - t2) / (1.0d - t2));
        }
        int index = 3;
        if (direction == -1) {
            index = 3 + (numparams * 6);
        }
        while (numparams >= 0) {
            addInstance(curves, tmp[index + 0], tmp[index + 1], tmp[index + 2], tmp[index + 3], tmp[index + 4], tmp[index + 5], tmp[index + 6], tmp[index + 7], direction);
            numparams--;
            if (direction == 1) {
                index += 6;
            } else {
                index -= 6;
            }
        }
    }

    public static void addInstance(Vector curves, double x0, double y0, double cx0, double cy0, double cx1, double cy1, double x1, double y1, int direction) {
        if (y0 > y1) {
            curves.add(new Order3(x1, y1, cx1, cy1, cx0, cy0, x0, y0, -direction));
        } else if (y1 > y0) {
            curves.add(new Order3(x0, y0, cx0, cy0, cx1, cy1, x1, y1, direction));
        }
    }

    public static int solveQuadratic(double[] eqn, double[] res) {
        int roots;
        double a2 = eqn[2];
        double b2 = eqn[1];
        double c2 = eqn[0];
        if (a2 != 0.0d) {
            double d2 = (b2 * b2) - ((4.0d * a2) * c2);
            if (d2 < 0.0d) {
                return 0;
            }
            double d3 = Math.sqrt(d2);
            if (b2 < 0.0d) {
                d3 = -d3;
            }
            double q2 = (b2 + d3) / (-2.0d);
            roots = 0 + 1;
            res[0] = q2 / a2;
            if (q2 != 0.0d) {
                roots++;
                res[roots] = c2 / q2;
            }
        } else if (b2 != 0.0d) {
            roots = 0 + 1;
            res[0] = (-c2) / b2;
        } else {
            return -1;
        }
        return roots;
    }

    public static int getHorizontalParams(double c0, double cp0, double cp1, double c1, double[] ret) {
        if (c0 <= cp0 && cp0 <= cp1 && cp1 <= c1) {
            return 0;
        }
        double cp12 = cp1 - cp0;
        double cp02 = cp0 - c0;
        ret[0] = cp02;
        ret[1] = (cp12 - cp02) * 2.0d;
        ret[2] = (((c1 - cp1) - cp12) - cp12) + cp02;
        int numroots = solveQuadratic(ret, ret);
        int j2 = 0;
        for (int i2 = 0; i2 < numroots; i2++) {
            double t2 = ret[i2];
            if (t2 > 0.0d && t2 < 1.0d) {
                if (j2 < i2) {
                    ret[j2] = t2;
                }
                j2++;
            }
        }
        return j2;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static void split(double[] dArr, int pos, double t2) {
        dArr[pos + 12] = dArr[pos + 6];
        dArr[pos + 13] = dArr[pos + 7];
        double d2 = dArr[pos + 4];
        double d3 = dArr[pos + 5];
        double x1 = d2 + ((dArr - d2) * t2);
        double y1 = d3 + ((dArr - d3) * t2);
        double d4 = dArr[pos + 0];
        double d5 = dArr[pos + 1];
        double d6 = dArr[pos + 2];
        double d7 = dArr[pos + 3];
        double x0 = d4 + ((d6 - d4) * t2);
        double y0 = d5 + ((d7 - d5) * t2);
        double cx0 = d6 + ((d2 - d6) * t2);
        double cy0 = d7 + ((d3 - d7) * t2);
        double cx1 = cx0 + ((x1 - cx0) * t2);
        double cy1 = cy0 + ((y1 - cy0) * t2);
        double cx02 = x0 + ((cx0 - x0) * t2);
        double cy02 = y0 + ((cy0 - y0) * t2);
        dArr[pos + 2] = x0;
        dArr[pos + 3] = y0;
        dArr[pos + 4] = cx02;
        dArr[pos + 5] = cy02;
        dArr[pos + 6] = cx02 + ((cx1 - cx02) * t2);
        dArr[pos + 7] = cy02 + ((cy1 - cy02) * t2);
        dArr[pos + 8] = cx1;
        dArr[pos + 9] = cy1;
        dArr[pos + 10] = x1;
        dArr[pos + 11] = y1;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public Order3(double x0, double d2, double cx0, double cy0, double cx1, double cy1, double x1, double y1, int direction) {
        super(direction);
        cy0 = cy0 < d2 ? d2 : cy0;
        cy1 = cy1 > y1 ? y1 : cy1;
        this.x0 = x0;
        this.y0 = d2;
        this.cx0 = cx0;
        this.cy0 = cy0;
        this.cx1 = cx1;
        this.cy1 = cy1;
        this.x1 = x1;
        this.y1 = y1;
        this.xmin = Math.min(Math.min(x0, x1), Math.min(cx0, cx1));
        this.xmax = Math.max(Math.max(x0, x1), Math.max(cx0, cx1));
        this.xcoeff0 = x0;
        this.xcoeff1 = (cx0 - x0) * 3.0d;
        this.xcoeff2 = (((cx1 - cx0) - cx0) + x0) * 3.0d;
        this.xcoeff3 = (x1 - ((cx1 - cx0) * 3.0d)) - x0;
        this.ycoeff0 = d2;
        this.ycoeff1 = (cy0 - d2) * 3.0d;
        this.ycoeff2 = (((cy1 - cy0) - cy0) + d2) * 3.0d;
        this.ycoeff3 = (y1 - ((cy1 - cy0) * 3.0d)) - d2;
        this.YforT3 = d2;
        this.YforT2 = d2;
        d2.YforT1 = this;
    }

    @Override // com.sun.javafx.geom.Curve
    public int getOrder() {
        return 3;
    }

    @Override // com.sun.javafx.geom.Curve
    public double getXTop() {
        return this.x0;
    }

    @Override // com.sun.javafx.geom.Curve
    public double getYTop() {
        return this.y0;
    }

    @Override // com.sun.javafx.geom.Curve
    public double getXBot() {
        return this.x1;
    }

    @Override // com.sun.javafx.geom.Curve
    public double getYBot() {
        return this.y1;
    }

    @Override // com.sun.javafx.geom.Curve
    public double getXMin() {
        return this.xmin;
    }

    @Override // com.sun.javafx.geom.Curve
    public double getXMax() {
        return this.xmax;
    }

    @Override // com.sun.javafx.geom.Curve
    public double getX0() {
        return this.direction == 1 ? this.x0 : this.x1;
    }

    @Override // com.sun.javafx.geom.Curve
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

    @Override // com.sun.javafx.geom.Curve
    public double getX1() {
        return this.direction == -1 ? this.x0 : this.x1;
    }

    @Override // com.sun.javafx.geom.Curve
    public double getY1() {
        return this.direction == -1 ? this.y0 : this.y1;
    }

    @Override // com.sun.javafx.geom.Curve
    public double TforY(double y2) {
        double t2;
        if (y2 <= this.y0) {
            return 0.0d;
        }
        if (y2 >= this.y1) {
            return 1.0d;
        }
        if (y2 == this.YforT1) {
            return this.TforY1;
        }
        if (y2 == this.YforT2) {
            return this.TforY2;
        }
        if (y2 == this.YforT3) {
            return this.TforY3;
        }
        if (this.ycoeff3 == 0.0d) {
            return Order2.TforY(y2, this.ycoeff0, this.ycoeff1, this.ycoeff2);
        }
        double a2 = this.ycoeff2 / this.ycoeff3;
        double b2 = this.ycoeff1 / this.ycoeff3;
        double c2 = (this.ycoeff0 - y2) / this.ycoeff3;
        double Q2 = ((a2 * a2) - (3.0d * b2)) / 9.0d;
        double R2 = (((((2.0d * a2) * a2) * a2) - ((9.0d * a2) * b2)) + (27.0d * c2)) / 54.0d;
        double R22 = R2 * R2;
        double Q3 = Q2 * Q2 * Q2;
        double a_3 = a2 / 3.0d;
        if (R22 < Q3) {
            double theta = Math.acos(R2 / Math.sqrt(Q3));
            double Q4 = (-2.0d) * Math.sqrt(Q2);
            t2 = refine(a2, b2, c2, y2, (Q4 * Math.cos(theta / 3.0d)) - a_3);
            if (t2 < 0.0d) {
                t2 = refine(a2, b2, c2, y2, (Q4 * Math.cos((theta + 6.283185307179586d) / 3.0d)) - a_3);
            }
            if (t2 < 0.0d) {
                t2 = refine(a2, b2, c2, y2, (Q4 * Math.cos((theta - 6.283185307179586d) / 3.0d)) - a_3);
            }
        } else {
            boolean neg = R2 < 0.0d;
            double S2 = Math.sqrt(R22 - Q3);
            if (neg) {
                R2 = -R2;
            }
            double A2 = Math.pow(R2 + S2, 0.3333333333333333d);
            if (!neg) {
                A2 = -A2;
            }
            double B2 = A2 == 0.0d ? 0.0d : Q2 / A2;
            t2 = refine(a2, b2, c2, y2, (A2 + B2) - a_3);
        }
        if (t2 < 0.0d) {
            double t0 = 0.0d;
            double t1 = 1.0d;
            while (true) {
                t2 = (t0 + t1) / 2.0d;
                if (t2 != t0 && t2 != t1) {
                    double yt = YforT(t2);
                    if (yt >= y2) {
                        if (yt <= y2) {
                            break;
                        }
                        t1 = t2;
                    } else {
                        t0 = t2;
                    }
                } else {
                    break;
                }
            }
        }
        if (t2 >= 0.0d) {
            this.TforY3 = this.TforY2;
            this.YforT3 = this.YforT2;
            this.TforY2 = this.TforY1;
            this.YforT2 = this.YforT1;
            this.TforY1 = t2;
            this.YforT1 = y2;
        }
        return t2;
    }

    public double refine(double a2, double b2, double c2, double target, double t2) {
        double t0;
        double t1;
        if (t2 < -0.1d || t2 > 1.1d) {
            return -1.0d;
        }
        double y2 = YforT(t2);
        if (y2 < target) {
            t0 = t2;
            t1 = 1.0d;
        } else {
            t0 = 0.0d;
            t1 = t2;
        }
        boolean useslope = true;
        while (y2 != target) {
            if (!useslope) {
                double t22 = (t0 + t1) / 2.0d;
                if (t22 == t0 || t22 == t1) {
                    break;
                }
                t2 = t22;
            } else {
                double slope = dYforT(t2, 1);
                if (slope == 0.0d) {
                    useslope = false;
                } else {
                    double t23 = t2 + ((target - y2) / slope);
                    if (t23 == t2 || t23 <= t0 || t23 >= t1) {
                        useslope = false;
                    } else {
                        t2 = t23;
                    }
                }
            }
            y2 = YforT(t2);
            if (y2 >= target) {
                if (y2 <= target) {
                    break;
                }
                t1 = t2;
            } else {
                t0 = t2;
            }
        }
        if (t2 > 1.0d) {
            return -1.0d;
        }
        return t2;
    }

    @Override // com.sun.javafx.geom.Curve
    public double XforY(double y2) {
        if (y2 <= this.y0) {
            return this.x0;
        }
        if (y2 >= this.y1) {
            return this.x1;
        }
        return XforT(TforY(y2));
    }

    @Override // com.sun.javafx.geom.Curve
    public double XforT(double t2) {
        return (((((this.xcoeff3 * t2) + this.xcoeff2) * t2) + this.xcoeff1) * t2) + this.xcoeff0;
    }

    @Override // com.sun.javafx.geom.Curve
    public double YforT(double t2) {
        return (((((this.ycoeff3 * t2) + this.ycoeff2) * t2) + this.ycoeff1) * t2) + this.ycoeff0;
    }

    @Override // com.sun.javafx.geom.Curve
    public double dXforT(double t2, int deriv) {
        switch (deriv) {
            case 0:
                return (((((this.xcoeff3 * t2) + this.xcoeff2) * t2) + this.xcoeff1) * t2) + this.xcoeff0;
            case 1:
                return (((3.0d * this.xcoeff3 * t2) + (2.0d * this.xcoeff2)) * t2) + this.xcoeff1;
            case 2:
                return (6.0d * this.xcoeff3 * t2) + (2.0d * this.xcoeff2);
            case 3:
                return 6.0d * this.xcoeff3;
            default:
                return 0.0d;
        }
    }

    @Override // com.sun.javafx.geom.Curve
    public double dYforT(double t2, int deriv) {
        switch (deriv) {
            case 0:
                return (((((this.ycoeff3 * t2) + this.ycoeff2) * t2) + this.ycoeff1) * t2) + this.ycoeff0;
            case 1:
                return (((3.0d * this.ycoeff3 * t2) + (2.0d * this.ycoeff2)) * t2) + this.ycoeff1;
            case 2:
                return (6.0d * this.ycoeff3 * t2) + (2.0d * this.ycoeff2);
            case 3:
                return 6.0d * this.ycoeff3;
            default:
                return 0.0d;
        }
    }

    @Override // com.sun.javafx.geom.Curve
    public double nextVertical(double t0, double t1) {
        double[] eqn = {this.xcoeff1, 2.0d * this.xcoeff2, 3.0d * this.xcoeff3};
        int numroots = solveQuadratic(eqn, eqn);
        for (int i2 = 0; i2 < numroots; i2++) {
            if (eqn[i2] > t0 && eqn[i2] < t1) {
                t1 = eqn[i2];
            }
        }
        return t1;
    }

    @Override // com.sun.javafx.geom.Curve
    public void enlarge(RectBounds r2) {
        r2.add((float) this.x0, (float) this.y0);
        double[] eqn = {this.xcoeff1, 2.0d * this.xcoeff2, 3.0d * this.xcoeff3};
        int numroots = solveQuadratic(eqn, eqn);
        for (int i2 = 0; i2 < numroots; i2++) {
            double t2 = eqn[i2];
            if (t2 > 0.0d && t2 < 1.0d) {
                r2.add((float) XforT(t2), (float) YforT(t2));
            }
        }
        r2.add((float) this.x1, (float) this.y1);
    }

    @Override // com.sun.javafx.geom.Curve
    public Curve getSubCurve(double ystart, double yend, int dir) {
        int i2;
        if (ystart <= this.y0 && yend >= this.y1) {
            return getWithDirection(dir);
        }
        double t0 = TforY(ystart);
        double t1 = TforY(yend);
        double[] eqn = {this.x0, this.y0, this.cx0, this.cy0, this.cx1, this.cy1, this.x1, this.y1, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d, 0.0d};
        if (t0 > t1) {
            t0 = t1;
            t1 = t0;
        }
        if (t1 < 1.0d) {
            split(eqn, 0, t1);
        }
        if (t0 <= 0.0d) {
            i2 = 0;
        } else {
            split(eqn, 0, t0 / t1);
            i2 = 6;
        }
        return new Order3(eqn[i2 + 0], ystart, eqn[i2 + 2], eqn[i2 + 3], eqn[i2 + 4], eqn[i2 + 5], eqn[i2 + 6], yend, dir);
    }

    @Override // com.sun.javafx.geom.Curve
    public Curve getReversedCurve() {
        return new Order3(this.x0, this.y0, this.cx0, this.cy0, this.cx1, this.cy1, this.x1, this.y1, -this.direction);
    }

    @Override // com.sun.javafx.geom.Curve
    public int getSegment(float[] coords) {
        if (this.direction == 1) {
            coords[0] = (float) this.cx0;
            coords[1] = (float) this.cy0;
            coords[2] = (float) this.cx1;
            coords[3] = (float) this.cy1;
            coords[4] = (float) this.x1;
            coords[5] = (float) this.y1;
            return 3;
        }
        coords[0] = (float) this.cx1;
        coords[1] = (float) this.cy1;
        coords[2] = (float) this.cx0;
        coords[3] = (float) this.cy0;
        coords[4] = (float) this.x0;
        coords[5] = (float) this.y0;
        return 3;
    }

    @Override // com.sun.javafx.geom.Curve
    public String controlPointString() {
        return "(" + round(getCX0()) + ", " + round(getCY0()) + "), (" + round(getCX1()) + ", " + round(getCY1()) + "), ";
    }
}
