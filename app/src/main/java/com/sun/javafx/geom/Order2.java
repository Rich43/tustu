package com.sun.javafx.geom;

import java.util.Vector;

/* loaded from: jfxrt.jar:com/sun/javafx/geom/Order2.class */
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

    public static void insert(Vector curves, double[] tmp, double x0, double y0, double cx0, double cy0, double x1, double y1, int direction) {
        int numparams = getHorizontalParams(y0, cy0, y1, tmp);
        if (numparams == 0) {
            addInstance(curves, x0, y0, cx0, cy0, x1, y1, direction);
            return;
        }
        double t2 = tmp[0];
        tmp[0] = x0;
        tmp[1] = y0;
        tmp[2] = cx0;
        tmp[3] = cy0;
        tmp[4] = x1;
        tmp[5] = y1;
        split(tmp, 0, t2);
        int i0 = direction == 1 ? 0 : 4;
        int i1 = 4 - i0;
        addInstance(curves, tmp[i0], tmp[i0 + 1], tmp[i0 + 2], tmp[i0 + 3], tmp[i0 + 4], tmp[i0 + 5], direction);
        addInstance(curves, tmp[i1], tmp[i1 + 1], tmp[i1 + 2], tmp[i1 + 3], tmp[i1 + 4], tmp[i1 + 5], direction);
    }

    public static void addInstance(Vector curves, double x0, double y0, double cx0, double cy0, double x1, double y1, int direction) {
        if (y0 > y1) {
            curves.add(new Order2(x1, y1, cx0, cy0, x0, y0, -direction));
        } else if (y1 > y0) {
            curves.add(new Order2(x0, y0, cx0, cy0, x1, y1, direction));
        }
    }

    public static int getHorizontalParams(double c0, double cp, double c1, double[] ret) {
        if (c0 <= cp && cp <= c1) {
            return 0;
        }
        double c02 = c0 - cp;
        double denom = c02 + (c1 - cp);
        if (denom == 0.0d) {
            return 0;
        }
        double t2 = c02 / denom;
        if (t2 <= 0.0d || t2 >= 1.0d) {
            return 0;
        }
        ret[0] = t2;
        return 1;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static void split(double[] dArr, int pos, double t2) {
        dArr[pos + 8] = dArr[pos + 4];
        dArr[pos + 9] = dArr[pos + 5];
        double d2 = dArr[pos + 2];
        double d3 = dArr[pos + 3];
        double x1 = d2 + ((dArr - d2) * t2);
        double y1 = d3 + ((dArr - d3) * t2);
        double d4 = dArr[pos + 0];
        double d5 = dArr[pos + 1];
        double x0 = d4 + ((d2 - d4) * t2);
        double y0 = d5 + ((d3 - d5) * t2);
        double cx = x0 + ((x1 - x0) * t2);
        double cy = y0 + ((y1 - y0) * t2);
        dArr[pos + 2] = x0;
        dArr[pos + 3] = y0;
        dArr[pos + 4] = cx;
        dArr[pos + 5] = cy;
        dArr[pos + 6] = x1;
        dArr[pos + 7] = y1;
    }

    public Order2(double x0, double y0, double cx0, double cy0, double x1, double y1, int direction) {
        super(direction);
        if (cy0 < y0) {
            cy0 = y0;
        } else if (cy0 > y1) {
            cy0 = y1;
        }
        this.x0 = x0;
        this.y0 = y0;
        this.cx0 = cx0;
        this.cy0 = cy0;
        this.x1 = x1;
        this.y1 = y1;
        this.xmin = Math.min(Math.min(x0, x1), cx0);
        this.xmax = Math.max(Math.max(x0, x1), cx0);
        this.xcoeff0 = x0;
        this.xcoeff1 = ((cx0 + cx0) - x0) - x0;
        this.xcoeff2 = ((x0 - cx0) - cx0) + x1;
        this.ycoeff0 = y0;
        this.ycoeff1 = ((cy0 + cy0) - y0) - y0;
        this.ycoeff2 = ((y0 - cy0) - cy0) + y1;
    }

    @Override // com.sun.javafx.geom.Curve
    public int getOrder() {
        return 2;
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
        return this.cx0;
    }

    public double getCY0() {
        return this.cy0;
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
    public double TforY(double y2) {
        if (y2 <= this.y0) {
            return 0.0d;
        }
        if (y2 >= this.y1) {
            return 1.0d;
        }
        return TforY(y2, this.ycoeff0, this.ycoeff1, this.ycoeff2);
    }

    public static double TforY(double y2, double ycoeff0, double ycoeff1, double ycoeff2) {
        double ycoeff02 = ycoeff0 - y2;
        if (ycoeff2 == 0.0d) {
            double root = (-ycoeff02) / ycoeff1;
            if (root >= 0.0d && root <= 1.0d) {
                return root;
            }
        } else {
            double d2 = (ycoeff1 * ycoeff1) - ((4.0d * ycoeff2) * ycoeff02);
            if (d2 >= 0.0d) {
                double d3 = Math.sqrt(d2);
                if (ycoeff1 < 0.0d) {
                    d3 = -d3;
                }
                double q2 = (ycoeff1 + d3) / (-2.0d);
                double root2 = q2 / ycoeff2;
                if (root2 >= 0.0d && root2 <= 1.0d) {
                    return root2;
                }
                if (q2 != 0.0d) {
                    double root3 = ycoeff02 / q2;
                    if (root3 >= 0.0d && root3 <= 1.0d) {
                        return root3;
                    }
                }
            }
        }
        double y1 = ycoeff02 + ycoeff1 + ycoeff2;
        return 0.0d < (ycoeff02 + y1) / 2.0d ? 0.0d : 1.0d;
    }

    @Override // com.sun.javafx.geom.Curve
    public double XforT(double t2) {
        return (((this.xcoeff2 * t2) + this.xcoeff1) * t2) + this.xcoeff0;
    }

    @Override // com.sun.javafx.geom.Curve
    public double YforT(double t2) {
        return (((this.ycoeff2 * t2) + this.ycoeff1) * t2) + this.ycoeff0;
    }

    @Override // com.sun.javafx.geom.Curve
    public double dXforT(double t2, int deriv) {
        switch (deriv) {
            case 0:
                return (((this.xcoeff2 * t2) + this.xcoeff1) * t2) + this.xcoeff0;
            case 1:
                return (2.0d * this.xcoeff2 * t2) + this.xcoeff1;
            case 2:
                return 2.0d * this.xcoeff2;
            default:
                return 0.0d;
        }
    }

    @Override // com.sun.javafx.geom.Curve
    public double dYforT(double t2, int deriv) {
        switch (deriv) {
            case 0:
                return (((this.ycoeff2 * t2) + this.ycoeff1) * t2) + this.ycoeff0;
            case 1:
                return (2.0d * this.ycoeff2 * t2) + this.ycoeff1;
            case 2:
                return 2.0d * this.ycoeff2;
            default:
                return 0.0d;
        }
    }

    @Override // com.sun.javafx.geom.Curve
    public double nextVertical(double t0, double t1) {
        double t2 = (-this.xcoeff1) / (2.0d * this.xcoeff2);
        if (t2 > t0 && t2 < t1) {
            return t2;
        }
        return t1;
    }

    @Override // com.sun.javafx.geom.Curve
    public void enlarge(RectBounds r2) {
        r2.add((float) this.x0, (float) this.y0);
        double t2 = (-this.xcoeff1) / (2.0d * this.xcoeff2);
        if (t2 > 0.0d && t2 < 1.0d) {
            r2.add((float) XforT(t2), (float) YforT(t2));
        }
        r2.add((float) this.x1, (float) this.y1);
    }

    @Override // com.sun.javafx.geom.Curve
    public Curve getSubCurve(double ystart, double yend, int dir) {
        double t0;
        double t1;
        int i2;
        if (ystart > this.y0) {
            t0 = TforY(ystart, this.ycoeff0, this.ycoeff1, this.ycoeff2);
        } else {
            if (yend >= this.y1) {
                return getWithDirection(dir);
            }
            t0 = 0.0d;
        }
        if (yend >= this.y1) {
            t1 = 1.0d;
        } else {
            t1 = TforY(yend, this.ycoeff0, this.ycoeff1, this.ycoeff2);
        }
        double[] eqn = {this.x0, this.y0, this.cx0, this.cy0, this.x1, this.y1, 0.0d, 0.0d, 0.0d, 0.0d};
        if (t1 < 1.0d) {
            split(eqn, 0, t1);
        }
        if (t0 <= 0.0d) {
            i2 = 0;
        } else {
            split(eqn, 0, t0 / t1);
            i2 = 4;
        }
        return new Order2(eqn[i2 + 0], ystart, eqn[i2 + 2], eqn[i2 + 3], eqn[i2 + 4], yend, dir);
    }

    @Override // com.sun.javafx.geom.Curve
    public Curve getReversedCurve() {
        return new Order2(this.x0, this.y0, this.cx0, this.cy0, this.x1, this.y1, -this.direction);
    }

    @Override // com.sun.javafx.geom.Curve
    public int getSegment(float[] coords) {
        coords[0] = (float) this.cx0;
        coords[1] = (float) this.cy0;
        if (this.direction == 1) {
            coords[2] = (float) this.x1;
            coords[3] = (float) this.y1;
            return 2;
        }
        coords[2] = (float) this.x0;
        coords[3] = (float) this.y0;
        return 2;
    }

    @Override // com.sun.javafx.geom.Curve
    public String controlPointString() {
        return "(" + round(this.cx0) + ", " + round(this.cy0) + "), ";
    }
}
