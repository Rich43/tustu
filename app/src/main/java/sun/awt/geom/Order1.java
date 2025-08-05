package sun.awt.geom;

import java.awt.geom.Rectangle2D;

/* loaded from: rt.jar:sun/awt/geom/Order1.class */
final class Order1 extends Curve {
    private double x0;
    private double y0;
    private double x1;
    private double y1;
    private double xmin;
    private double xmax;

    public Order1(double d2, double d3, double d4, double d5, int i2) {
        super(i2);
        this.x0 = d2;
        this.y0 = d3;
        this.x1 = d4;
        this.y1 = d5;
        if (d2 < d4) {
            this.xmin = d2;
            this.xmax = d4;
        } else {
            this.xmin = d4;
            this.xmax = d2;
        }
    }

    @Override // sun.awt.geom.Curve
    public int getOrder() {
        return 1;
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
        if (this.x0 == this.x1 || d2 <= this.y0) {
            return this.x0;
        }
        if (d2 >= this.y1) {
            return this.x1;
        }
        return this.x0 + (((d2 - this.y0) * (this.x1 - this.x0)) / (this.y1 - this.y0));
    }

    @Override // sun.awt.geom.Curve
    public double TforY(double d2) {
        if (d2 <= this.y0) {
            return 0.0d;
        }
        if (d2 >= this.y1) {
            return 1.0d;
        }
        return (d2 - this.y0) / (this.y1 - this.y0);
    }

    @Override // sun.awt.geom.Curve
    public double XforT(double d2) {
        return this.x0 + (d2 * (this.x1 - this.x0));
    }

    @Override // sun.awt.geom.Curve
    public double YforT(double d2) {
        return this.y0 + (d2 * (this.y1 - this.y0));
    }

    @Override // sun.awt.geom.Curve
    public double dXforT(double d2, int i2) {
        switch (i2) {
            case 0:
                return this.x0 + (d2 * (this.x1 - this.x0));
            case 1:
                return this.x1 - this.x0;
            default:
                return 0.0d;
        }
    }

    @Override // sun.awt.geom.Curve
    public double dYforT(double d2, int i2) {
        switch (i2) {
            case 0:
                return this.y0 + (d2 * (this.y1 - this.y0));
            case 1:
                return this.y1 - this.y0;
            default:
                return 0.0d;
        }
    }

    @Override // sun.awt.geom.Curve
    public double nextVertical(double d2, double d3) {
        return d3;
    }

    @Override // sun.awt.geom.Curve
    public boolean accumulateCrossings(Crossings crossings) {
        double d2;
        double dXforY;
        double d3;
        double dXforY2;
        double xLo = crossings.getXLo();
        double yLo = crossings.getYLo();
        double xHi = crossings.getXHi();
        double yHi = crossings.getYHi();
        if (this.xmin >= xHi) {
            return false;
        }
        if (this.y0 < yLo) {
            if (this.y1 <= yLo) {
                return false;
            }
            d2 = yLo;
            dXforY = XforY(yLo);
        } else {
            if (this.y0 >= yHi) {
                return false;
            }
            d2 = this.y0;
            dXforY = this.x0;
        }
        if (this.y1 > yHi) {
            d3 = yHi;
            dXforY2 = XforY(yHi);
        } else {
            d3 = this.y1;
            dXforY2 = this.x1;
        }
        if (dXforY >= xHi && dXforY2 >= xHi) {
            return false;
        }
        if (dXforY > xLo || dXforY2 > xLo) {
            return true;
        }
        crossings.record(d2, d3, this.direction);
        return false;
    }

    @Override // sun.awt.geom.Curve
    public void enlarge(Rectangle2D rectangle2D) {
        rectangle2D.add(this.x0, this.y0);
        rectangle2D.add(this.x1, this.y1);
    }

    @Override // sun.awt.geom.Curve
    public Curve getSubCurve(double d2, double d3, int i2) {
        if (d2 == this.y0 && d3 == this.y1) {
            return getWithDirection(i2);
        }
        if (this.x0 == this.x1) {
            return new Order1(this.x0, d2, this.x1, d3, i2);
        }
        double d4 = this.x0 - this.x1;
        double d5 = this.y0 - this.y1;
        return new Order1(this.x0 + (((d2 - this.y0) * d4) / d5), d2, this.x0 + (((d3 - this.y0) * d4) / d5), d3, i2);
    }

    @Override // sun.awt.geom.Curve
    public Curve getReversedCurve() {
        return new Order1(this.x0, this.y0, this.x1, this.y1, -this.direction);
    }

    @Override // sun.awt.geom.Curve
    public int compareTo(Curve curve, double[] dArr) {
        double dMax;
        if (!(curve instanceof Order1)) {
            return super.compareTo(curve, dArr);
        }
        Order1 order1 = (Order1) curve;
        if (dArr[1] <= dArr[0]) {
            throw new InternalError("yrange already screwed up...");
        }
        dArr[1] = Math.min(Math.min(dArr[1], this.y1), order1.y1);
        if (dArr[1] <= dArr[0]) {
            throw new InternalError("backstepping from " + dArr[0] + " to " + dArr[1]);
        }
        if (this.xmax <= order1.xmin) {
            return this.xmin == order1.xmax ? 0 : -1;
        }
        if (this.xmin >= order1.xmax) {
            return 1;
        }
        double d2 = this.x1 - this.x0;
        double d3 = this.y1 - this.y0;
        double d4 = order1.x1 - order1.x0;
        double d5 = order1.y1 - order1.y0;
        double d6 = (d4 * d3) - (d2 * d5);
        if (d6 != 0.0d) {
            double d7 = (((((this.x0 - order1.x0) * d3) * d5) - ((this.y0 * d2) * d5)) + ((order1.y0 * d4) * d3)) / d6;
            if (d7 <= dArr[0]) {
                dMax = Math.min(this.y1, order1.y1);
            } else {
                if (d7 < dArr[1]) {
                    dArr[1] = d7;
                }
                dMax = Math.max(this.y0, order1.y0);
            }
        } else {
            dMax = Math.max(this.y0, order1.y0);
        }
        return orderof(XforY(dMax), order1.XforY(dMax));
    }

    @Override // sun.awt.geom.Curve
    public int getSegment(double[] dArr) {
        if (this.direction == 1) {
            dArr[0] = this.x1;
            dArr[1] = this.y1;
            return 1;
        }
        dArr[0] = this.x0;
        dArr[1] = this.y0;
        return 1;
    }
}
