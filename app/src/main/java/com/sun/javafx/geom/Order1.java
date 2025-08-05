package com.sun.javafx.geom;

/* loaded from: jfxrt.jar:com/sun/javafx/geom/Order1.class */
final class Order1 extends Curve {
    private double x0;
    private double y0;
    private double x1;
    private double y1;
    private double xmin;
    private double xmax;

    public Order1(double x0, double y0, double x1, double y1, int direction) {
        super(direction);
        this.x0 = x0;
        this.y0 = y0;
        this.x1 = x1;
        this.y1 = y1;
        if (x0 < x1) {
            this.xmin = x0;
            this.xmax = x1;
        } else {
            this.xmin = x1;
            this.xmax = x0;
        }
    }

    @Override // com.sun.javafx.geom.Curve
    public int getOrder() {
        return 1;
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
        if (this.x0 == this.x1 || y2 <= this.y0) {
            return this.x0;
        }
        if (y2 >= this.y1) {
            return this.x1;
        }
        return this.x0 + (((y2 - this.y0) * (this.x1 - this.x0)) / (this.y1 - this.y0));
    }

    @Override // com.sun.javafx.geom.Curve
    public double TforY(double y2) {
        if (y2 <= this.y0) {
            return 0.0d;
        }
        if (y2 >= this.y1) {
            return 1.0d;
        }
        return (y2 - this.y0) / (this.y1 - this.y0);
    }

    @Override // com.sun.javafx.geom.Curve
    public double XforT(double t2) {
        return this.x0 + (t2 * (this.x1 - this.x0));
    }

    @Override // com.sun.javafx.geom.Curve
    public double YforT(double t2) {
        return this.y0 + (t2 * (this.y1 - this.y0));
    }

    @Override // com.sun.javafx.geom.Curve
    public double dXforT(double t2, int deriv) {
        switch (deriv) {
            case 0:
                return this.x0 + (t2 * (this.x1 - this.x0));
            case 1:
                return this.x1 - this.x0;
            default:
                return 0.0d;
        }
    }

    @Override // com.sun.javafx.geom.Curve
    public double dYforT(double t2, int deriv) {
        switch (deriv) {
            case 0:
                return this.y0 + (t2 * (this.y1 - this.y0));
            case 1:
                return this.y1 - this.y0;
            default:
                return 0.0d;
        }
    }

    @Override // com.sun.javafx.geom.Curve
    public double nextVertical(double t0, double t1) {
        return t1;
    }

    @Override // com.sun.javafx.geom.Curve
    public boolean accumulateCrossings(Crossings c2) {
        double ystart;
        double xstart;
        double yend;
        double xend;
        double xlo = c2.getXLo();
        double ylo = c2.getYLo();
        double xhi = c2.getXHi();
        double yhi = c2.getYHi();
        if (this.xmin >= xhi) {
            return false;
        }
        if (this.y0 < ylo) {
            if (this.y1 <= ylo) {
                return false;
            }
            ystart = ylo;
            xstart = XforY(ylo);
        } else {
            if (this.y0 >= yhi) {
                return false;
            }
            ystart = this.y0;
            xstart = this.x0;
        }
        if (this.y1 > yhi) {
            yend = yhi;
            xend = XforY(yhi);
        } else {
            yend = this.y1;
            xend = this.x1;
        }
        if (xstart >= xhi && xend >= xhi) {
            return false;
        }
        if (xstart > xlo || xend > xlo) {
            return true;
        }
        c2.record(ystart, yend, this.direction);
        return false;
    }

    @Override // com.sun.javafx.geom.Curve
    public void enlarge(RectBounds r2) {
        r2.add((float) this.x0, (float) this.y0);
        r2.add((float) this.x1, (float) this.y1);
    }

    @Override // com.sun.javafx.geom.Curve
    public Curve getSubCurve(double ystart, double yend, int dir) {
        if (ystart == this.y0 && yend == this.y1) {
            return getWithDirection(dir);
        }
        if (this.x0 == this.x1) {
            return new Order1(this.x0, ystart, this.x1, yend, dir);
        }
        double num = this.x0 - this.x1;
        double denom = this.y0 - this.y1;
        double xstart = this.x0 + (((ystart - this.y0) * num) / denom);
        double xend = this.x0 + (((yend - this.y0) * num) / denom);
        return new Order1(xstart, ystart, xend, yend, dir);
    }

    @Override // com.sun.javafx.geom.Curve
    public Curve getReversedCurve() {
        return new Order1(this.x0, this.y0, this.x1, this.y1, -this.direction);
    }

    @Override // com.sun.javafx.geom.Curve
    public int compareTo(Curve other, double[] yrange) {
        double y2;
        if (!(other instanceof Order1)) {
            return super.compareTo(other, yrange);
        }
        Order1 c1 = (Order1) other;
        if (yrange[1] <= yrange[0]) {
            throw new InternalError("yrange already screwed up...");
        }
        yrange[1] = Math.min(Math.min(yrange[1], this.y1), c1.y1);
        if (yrange[1] <= yrange[0]) {
            throw new InternalError("backstepping from " + yrange[0] + " to " + yrange[1]);
        }
        if (this.xmax <= c1.xmin) {
            return this.xmin == c1.xmax ? 0 : -1;
        }
        if (this.xmin >= c1.xmax) {
            return 1;
        }
        double dxa = this.x1 - this.x0;
        double dya = this.y1 - this.y0;
        double dxb = c1.x1 - c1.x0;
        double dyb = c1.y1 - c1.y0;
        double denom = (dxb * dya) - (dxa * dyb);
        if (denom != 0.0d) {
            double num = ((((this.x0 - c1.x0) * dya) * dyb) - ((this.y0 * dxa) * dyb)) + (c1.y0 * dxb * dya);
            double y3 = num / denom;
            if (y3 <= yrange[0]) {
                y2 = Math.min(this.y1, c1.y1);
            } else {
                if (y3 < yrange[1]) {
                    yrange[1] = y3;
                }
                y2 = Math.max(this.y0, c1.y0);
            }
        } else {
            y2 = Math.max(this.y0, c1.y0);
        }
        return orderof(XforY(y2), c1.XforY(y2));
    }

    @Override // com.sun.javafx.geom.Curve
    public int getSegment(float[] coords) {
        if (this.direction == 1) {
            coords[0] = (float) this.x1;
            coords[1] = (float) this.y1;
            return 1;
        }
        coords[0] = (float) this.x0;
        coords[1] = (float) this.y0;
        return 1;
    }
}
