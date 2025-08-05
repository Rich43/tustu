package com.sun.javafx.geom;

import java.util.Vector;
import org.icepdf.core.util.PdfOps;

/* loaded from: jfxrt.jar:com/sun/javafx/geom/Curve.class */
public abstract class Curve {
    public static final int INCREASING = 1;
    public static final int DECREASING = -1;
    protected int direction;
    public static final double TMIN = 0.001d;

    public abstract int getOrder();

    public abstract double getXTop();

    public abstract double getYTop();

    public abstract double getXBot();

    public abstract double getYBot();

    public abstract double getXMin();

    public abstract double getXMax();

    public abstract double getX0();

    public abstract double getY0();

    public abstract double getX1();

    public abstract double getY1();

    public abstract double XforY(double d2);

    public abstract double TforY(double d2);

    public abstract double XforT(double d2);

    public abstract double YforT(double d2);

    public abstract double dXforT(double d2, int i2);

    public abstract double dYforT(double d2, int i2);

    public abstract double nextVertical(double d2, double d3);

    public abstract void enlarge(RectBounds rectBounds);

    public abstract Curve getReversedCurve();

    public abstract Curve getSubCurve(double d2, double d3, int i2);

    public abstract int getSegment(float[] fArr);

    public static void insertMove(Vector curves, double x2, double y2) {
        curves.add(new Order0(x2, y2));
    }

    public static void insertLine(Vector curves, double x0, double y0, double x1, double y1) {
        if (y0 < y1) {
            curves.add(new Order1(x0, y0, x1, y1, 1));
        } else if (y0 > y1) {
            curves.add(new Order1(x1, y1, x0, y0, -1));
        }
    }

    public static void insertQuad(Vector curves, double[] tmp, double x0, double y0, double cx0, double cy0, double x1, double y1) {
        if (y0 > y1) {
            Order2.insert(curves, tmp, x1, y1, cx0, cy0, x0, y0, -1);
        } else {
            if (y0 == y1 && y0 == cy0) {
                return;
            }
            Order2.insert(curves, tmp, x0, y0, cx0, cy0, x1, y1, 1);
        }
    }

    public static void insertCubic(Vector curves, double[] tmp, double x0, double y0, double cx0, double cy0, double cx1, double cy1, double x1, double y1) {
        if (y0 > y1) {
            Order3.insert(curves, tmp, x1, y1, cx1, cy1, cx0, cy0, x0, y0, -1);
        } else {
            if (y0 == y1 && y0 == cy0 && y0 == cy1) {
                return;
            }
            Order3.insert(curves, tmp, x0, y0, cx0, cy0, cx1, cy1, x1, y1, 1);
        }
    }

    public Curve(int direction) {
        this.direction = direction;
    }

    public final int getDirection() {
        return this.direction;
    }

    public final Curve getWithDirection(int direction) {
        return this.direction == direction ? this : getReversedCurve();
    }

    public static double round(double v2) {
        return v2;
    }

    public static int orderof(double x1, double x2) {
        if (x1 < x2) {
            return -1;
        }
        if (x1 > x2) {
            return 1;
        }
        return 0;
    }

    public static long signeddiffbits(double y1, double y2) {
        return Double.doubleToLongBits(y1) - Double.doubleToLongBits(y2);
    }

    public static long diffbits(double y1, double y2) {
        return Math.abs(Double.doubleToLongBits(y1) - Double.doubleToLongBits(y2));
    }

    public static double prev(double v2) {
        return Double.longBitsToDouble(Double.doubleToLongBits(v2) - 1);
    }

    public static double next(double v2) {
        return Double.longBitsToDouble(Double.doubleToLongBits(v2) + 1);
    }

    public String toString() {
        return "Curve[" + getOrder() + ", (" + round(getX0()) + ", " + round(getY0()) + "), " + controlPointString() + "(" + round(getX1()) + ", " + round(getY1()) + "), " + (this.direction == 1 ? PdfOps.D_TOKEN : "U") + "]";
    }

    public String controlPointString() {
        return "";
    }

    public int crossingsFor(double x2, double y2) {
        if (y2 < getYTop() || y2 >= getYBot() || x2 >= getXMax()) {
            return 0;
        }
        if (x2 < getXMin() || x2 < XforY(y2)) {
            return 1;
        }
        return 0;
    }

    public boolean accumulateCrossings(Crossings c2) {
        double ystart;
        double tstart;
        double yend;
        double tend;
        double xhi = c2.getXHi();
        if (getXMin() >= xhi) {
            return false;
        }
        double xlo = c2.getXLo();
        double ylo = c2.getYLo();
        double yhi = c2.getYHi();
        double y0 = getYTop();
        double y1 = getYBot();
        if (y0 < ylo) {
            if (y1 <= ylo) {
                return false;
            }
            ystart = ylo;
            tstart = TforY(ylo);
        } else {
            if (y0 >= yhi) {
                return false;
            }
            ystart = y0;
            tstart = 0.0d;
        }
        if (y1 > yhi) {
            yend = yhi;
            tend = TforY(yhi);
        } else {
            yend = y1;
            tend = 1.0d;
        }
        boolean hitLo = false;
        boolean hitHi = false;
        while (true) {
            double x2 = XforT(tstart);
            if (x2 < xhi) {
                if (hitHi || x2 > xlo) {
                    return true;
                }
                hitLo = true;
            } else {
                if (hitLo) {
                    return true;
                }
                hitHi = true;
            }
            if (tstart < tend) {
                tstart = nextVertical(tstart, tend);
            } else {
                if (hitLo) {
                    c2.record(ystart, yend, this.direction);
                    return false;
                }
                return false;
            }
        }
    }

    public Curve getSubCurve(double ystart, double yend) {
        return getSubCurve(ystart, yend, this.direction);
    }

    public int compareTo(Curve that, double[] yrange) {
        double y2;
        double y0 = yrange[0];
        double y1 = Math.min(Math.min(yrange[1], getYBot()), that.getYBot());
        if (y1 <= yrange[0]) {
            System.err.println("this == " + ((Object) this));
            System.err.println("that == " + ((Object) that));
            System.out.println("target range = " + yrange[0] + "=>" + yrange[1]);
            throw new InternalError("backstepping from " + yrange[0] + " to " + y1);
        }
        yrange[1] = y1;
        if (getXMax() <= that.getXMin()) {
            if (getXMin() == that.getXMax()) {
                return 0;
            }
            return -1;
        }
        if (getXMin() >= that.getXMax()) {
            return 1;
        }
        double s0 = TforY(y0);
        double ys0 = YforT(s0);
        if (ys0 < y0) {
            s0 = refineTforY(s0, y0);
            ys0 = YforT(s0);
        }
        double s1 = TforY(y1);
        if (YforT(s1) < y0) {
            s1 = refineTforY(s1, y0);
        }
        double t0 = that.TforY(y0);
        double yt0 = that.YforT(t0);
        if (yt0 < y0) {
            t0 = that.refineTforY(t0, y0);
            yt0 = that.YforT(t0);
        }
        double t1 = that.TforY(y1);
        if (that.YforT(t1) < y0) {
            t1 = that.refineTforY(t1, y0);
        }
        double xs0 = XforT(s0);
        double xt0 = that.XforT(t0);
        double scale = Math.max(Math.abs(y0), Math.abs(y1));
        double ymin = Math.max(scale * 1.0E-14d, 1.0E-300d);
        if (fairlyClose(xs0, xt0)) {
            double bump = ymin;
            double maxbump = Math.min(ymin * 1.0E13d, (y1 - y0) * 0.1d);
            double d2 = y0;
            while (true) {
                y2 = d2 + bump;
                if (y2 > y1) {
                    break;
                }
                double dXforY = XforY(y2);
                double dXforY2 = that.XforY(y2);
                if (fairlyClose(dXforY, dXforY2)) {
                    double d3 = bump * 2.0d;
                    bump = dXforY2;
                    if (d3 > maxbump) {
                        bump = maxbump;
                    }
                    d2 = y2;
                } else {
                    y2 -= bump;
                    while (true) {
                        bump /= 2.0d;
                        double newy = y2 + bump;
                        if (newy <= y2) {
                            break;
                        }
                        if (fairlyClose(XforY(newy), that.XforY(newy))) {
                            y2 = newy;
                        }
                    }
                }
            }
            if (y2 > y0) {
                if (y2 < y1) {
                    yrange[1] = y2;
                    return 0;
                }
                return 0;
            }
        }
        if (ymin <= 0.0d) {
            System.out.println("ymin = " + ymin);
        }
        while (true) {
            if (s0 >= s1 || t0 >= t1) {
                break;
            }
            double sh = nextVertical(s0, s1);
            double xsh = XforT(sh);
            double ysh = YforT(sh);
            double th = that.nextVertical(t0, t1);
            double xth = that.XforT(th);
            double yth = that.YforT(th);
            try {
                if (findIntersect(that, yrange, ymin, 0, 0, s0, xs0, ys0, sh, xsh, ysh, t0, xt0, yt0, th, xth, yth)) {
                    break;
                }
                if (ysh < yth) {
                    if (ysh <= yrange[0]) {
                        s0 = sh;
                        xs0 = xsh;
                        ys0 = ysh;
                    } else if (ysh < yrange[1]) {
                        yrange[1] = ysh;
                    }
                } else if (yth <= yrange[0]) {
                    t0 = th;
                    xt0 = xth;
                    yt0 = yth;
                } else if (yth < yrange[1]) {
                    yrange[1] = yth;
                }
            } catch (Throwable t2) {
                System.err.println("Error: " + ((Object) t2));
                System.err.println("y range was " + yrange[0] + "=>" + yrange[1]);
                System.err.println("s y range is " + ys0 + "=>" + ysh);
                System.err.println("t y range is " + yt0 + "=>" + yth);
                System.err.println("ymin is " + ymin);
                return 0;
            }
        }
        double ymid = (yrange[0] + yrange[1]) / 2.0d;
        return orderof(XforY(ymid), that.XforY(ymid));
    }

    public boolean findIntersect(Curve that, double[] yrange, double ymin, int slevel, int tlevel, double s0, double xs0, double ys0, double s1, double xs1, double ys1, double t0, double xt0, double yt0, double t1, double xt1, double yt1) {
        if (ys0 > yt1 || yt0 > ys1 || Math.min(xs0, xs1) > Math.max(xt0, xt1) || Math.max(xs0, xs1) < Math.min(xt0, xt1)) {
            return false;
        }
        if (s1 - s0 > 0.001d) {
            double s2 = (s0 + s1) / 2.0d;
            double xs = XforT(s2);
            double ys = YforT(s2);
            if (s2 == s0 || s2 == s1) {
                System.out.println("s0 = " + s0);
                System.out.println("s1 = " + s1);
                throw new InternalError("no s progress!");
            }
            if (t1 - t0 > 0.001d) {
                double t2 = (t0 + t1) / 2.0d;
                double xt = that.XforT(t2);
                double yt = that.YforT(t2);
                if (t2 == t0 || t2 == t1) {
                    System.out.println("t0 = " + t0);
                    System.out.println("t1 = " + t1);
                    throw new InternalError("no t progress!");
                }
                if (ys >= yt0 && yt >= ys0 && findIntersect(that, yrange, ymin, slevel + 1, tlevel + 1, s0, xs0, ys0, s2, xs, ys, t0, xt0, yt0, t2, xt, yt)) {
                    return true;
                }
                if (ys >= yt && findIntersect(that, yrange, ymin, slevel + 1, tlevel + 1, s0, xs0, ys0, s2, xs, ys, t2, xt, yt, t1, xt1, yt1)) {
                    return true;
                }
                if (yt >= ys && findIntersect(that, yrange, ymin, slevel + 1, tlevel + 1, s2, xs, ys, s1, xs1, ys1, t0, xt0, yt0, t2, xt, yt)) {
                    return true;
                }
                if (ys1 >= yt && yt1 >= ys && findIntersect(that, yrange, ymin, slevel + 1, tlevel + 1, s2, xs, ys, s1, xs1, ys1, t2, xt, yt, t1, xt1, yt1)) {
                    return true;
                }
                return false;
            }
            if (ys >= yt0 && findIntersect(that, yrange, ymin, slevel + 1, tlevel, s0, xs0, ys0, s2, xs, ys, t0, xt0, yt0, t1, xt1, yt1)) {
                return true;
            }
            if (yt1 >= ys && findIntersect(that, yrange, ymin, slevel + 1, tlevel, s2, xs, ys, s1, xs1, ys1, t0, xt0, yt0, t1, xt1, yt1)) {
                return true;
            }
            return false;
        }
        if (t1 - t0 > 0.001d) {
            double t3 = (t0 + t1) / 2.0d;
            double xt2 = that.XforT(t3);
            double yt2 = that.YforT(t3);
            if (t3 == t0 || t3 == t1) {
                System.out.println("t0 = " + t0);
                System.out.println("t1 = " + t1);
                throw new InternalError("no t progress!");
            }
            if (yt2 >= ys0 && findIntersect(that, yrange, ymin, slevel, tlevel + 1, s0, xs0, ys0, s1, xs1, ys1, t0, xt0, yt0, t3, xt2, yt2)) {
                return true;
            }
            if (ys1 >= yt2 && findIntersect(that, yrange, ymin, slevel, tlevel + 1, s0, xs0, ys0, s1, xs1, ys1, t3, xt2, yt2, t1, xt1, yt1)) {
                return true;
            }
            return false;
        }
        double xlk = xs1 - xs0;
        double ylk = ys1 - ys0;
        double xnm = xt1 - xt0;
        double ynm = yt1 - yt0;
        double xmk = xt0 - xs0;
        double ymk = yt0 - ys0;
        double det = (xnm * ylk) - (ynm * xlk);
        if (det != 0.0d) {
            double detinv = 1.0d / det;
            double s3 = ((xnm * ymk) - (ynm * xmk)) * detinv;
            double t4 = ((xlk * ymk) - (ylk * xmk)) * detinv;
            if (s3 >= 0.0d && s3 <= 1.0d && t4 >= 0.0d && t4 <= 1.0d) {
                double s4 = s0 + (s3 * (s1 - s0));
                double t5 = t0 + (t4 * (t1 - t0));
                if (s4 < 0.0d || s4 > 1.0d || t5 < 0.0d || t5 > 1.0d) {
                    System.out.println("Uh oh!");
                }
                double y2 = (YforT(s4) + that.YforT(t5)) / 2.0d;
                if (y2 <= yrange[1] && y2 > yrange[0]) {
                    yrange[1] = y2;
                    return true;
                }
                return false;
            }
            return false;
        }
        return false;
    }

    /* JADX WARN: Code restructure failed: missing block: B:8:0x001e, code lost:
    
        return r10;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public double refineTforY(double r6, double r8) {
        /*
            r5 = this;
            r0 = 4607182418800017408(0x3ff0000000000000, double:1.0)
            r10 = r0
        L3:
            r0 = r6
            r1 = r10
            double r0 = r0 + r1
            r1 = 4611686018427387904(0x4000000000000000, double:2.0)
            double r0 = r0 / r1
            r12 = r0
            r0 = r12
            r1 = r6
            int r0 = (r0 > r1 ? 1 : (r0 == r1 ? 0 : -1))
            if (r0 == 0) goto L1c
            r0 = r12
            r1 = r10
            int r0 = (r0 > r1 ? 1 : (r0 == r1 ? 0 : -1))
            if (r0 != 0) goto L1f
        L1c:
            r0 = r10
            return r0
        L1f:
            r0 = r5
            r1 = r12
            double r0 = r0.YforT(r1)
            r14 = r0
            r0 = r14
            r1 = r8
            int r0 = (r0 > r1 ? 1 : (r0 == r1 ? 0 : -1))
            if (r0 >= 0) goto L34
            r0 = r12
            r6 = r0
            goto L45
        L34:
            r0 = r14
            r1 = r8
            int r0 = (r0 > r1 ? 1 : (r0 == r1 ? 0 : -1))
            if (r0 <= 0) goto L42
            r0 = r12
            r10 = r0
            goto L45
        L42:
            r0 = r10
            return r0
        L45:
            goto L3
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.javafx.geom.Curve.refineTforY(double, double):double");
    }

    public boolean fairlyClose(double v1, double v2) {
        return Math.abs(v1 - v2) < Math.max(Math.abs(v1), Math.abs(v2)) * 1.0E-10d;
    }
}
