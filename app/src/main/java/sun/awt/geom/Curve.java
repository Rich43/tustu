package sun.awt.geom;

import java.awt.geom.IllegalPathStateException;
import java.awt.geom.PathIterator;
import java.awt.geom.Rectangle2D;
import java.util.Vector;
import org.icepdf.core.util.PdfOps;

/* loaded from: rt.jar:sun/awt/geom/Curve.class */
public abstract class Curve {
    public static final int INCREASING = 1;
    public static final int DECREASING = -1;
    protected int direction;
    public static final int RECT_INTERSECTS = Integer.MIN_VALUE;
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

    public abstract void enlarge(Rectangle2D rectangle2D);

    public abstract Curve getReversedCurve();

    public abstract Curve getSubCurve(double d2, double d3, int i2);

    public abstract int getSegment(double[] dArr);

    public static void insertMove(Vector vector, double d2, double d3) {
        vector.add(new Order0(d2, d3));
    }

    public static void insertLine(Vector vector, double d2, double d3, double d4, double d5) {
        if (d3 < d5) {
            vector.add(new Order1(d2, d3, d4, d5, 1));
        } else if (d3 > d5) {
            vector.add(new Order1(d4, d5, d2, d3, -1));
        }
    }

    public static void insertQuad(Vector vector, double d2, double d3, double[] dArr) {
        double d4 = dArr[3];
        if (d3 > d4) {
            Order2.insert(vector, dArr, dArr[2], d4, dArr[0], dArr[1], d2, d3, -1);
        } else {
            if (d3 == d4 && d3 == dArr[1]) {
                return;
            }
            Order2.insert(vector, dArr, d2, d3, dArr[0], dArr[1], dArr[2], d4, 1);
        }
    }

    public static void insertCubic(Vector vector, double d2, double d3, double[] dArr) {
        double d4 = dArr[5];
        if (d3 > d4) {
            Order3.insert(vector, dArr, dArr[4], d4, dArr[2], dArr[3], dArr[0], dArr[1], d2, d3, -1);
        } else {
            if (d3 == d4 && d3 == dArr[1] && d3 == dArr[3]) {
                return;
            }
            Order3.insert(vector, dArr, d2, d3, dArr[0], dArr[1], dArr[2], dArr[3], dArr[4], d4, 1);
        }
    }

    public static int pointCrossingsForPath(PathIterator pathIterator, double d2, double d3) {
        if (pathIterator.isDone()) {
            return 0;
        }
        double[] dArr = new double[6];
        if (pathIterator.currentSegment(dArr) != 0) {
            throw new IllegalPathStateException("missing initial moveto in path definition");
        }
        pathIterator.next();
        double d4 = dArr[0];
        double d5 = dArr[1];
        double d6 = d4;
        double d7 = d5;
        int iPointCrossingsForLine = 0;
        while (!pathIterator.isDone()) {
            switch (pathIterator.currentSegment(dArr)) {
                case 0:
                    if (d7 != d5) {
                        iPointCrossingsForLine += pointCrossingsForLine(d2, d3, d6, d7, d4, d5);
                    }
                    double d8 = dArr[0];
                    d6 = d8;
                    d4 = d8;
                    double d9 = dArr[1];
                    d7 = d9;
                    d5 = d9;
                    break;
                case 1:
                    double d10 = dArr[0];
                    double d11 = dArr[1];
                    iPointCrossingsForLine += pointCrossingsForLine(d2, d3, d6, d7, d10, d11);
                    d6 = d10;
                    d7 = d11;
                    break;
                case 2:
                    double d12 = dArr[2];
                    double d13 = dArr[3];
                    iPointCrossingsForLine += pointCrossingsForQuad(d2, d3, d6, d7, dArr[0], dArr[1], d12, d13, 0);
                    d6 = d12;
                    d7 = d13;
                    break;
                case 3:
                    double d14 = dArr[4];
                    double d15 = dArr[5];
                    iPointCrossingsForLine += pointCrossingsForCubic(d2, d3, d6, d7, dArr[0], dArr[1], dArr[2], dArr[3], d14, d15, 0);
                    d6 = d14;
                    d7 = d15;
                    break;
                case 4:
                    if (d7 != d5) {
                        iPointCrossingsForLine += pointCrossingsForLine(d2, d3, d6, d7, d4, d5);
                    }
                    d6 = d4;
                    d7 = d5;
                    break;
            }
            pathIterator.next();
        }
        if (d7 != d5) {
            iPointCrossingsForLine += pointCrossingsForLine(d2, d3, d6, d7, d4, d5);
        }
        return iPointCrossingsForLine;
    }

    public static int pointCrossingsForLine(double d2, double d3, double d4, double d5, double d6, double d7) {
        if (d3 < d5 && d3 < d7) {
            return 0;
        }
        if (d3 >= d5 && d3 >= d7) {
            return 0;
        }
        if (d2 >= d4 && d2 >= d6) {
            return 0;
        }
        if (d2 < d4 && d2 < d6) {
            return d5 < d7 ? 1 : -1;
        }
        if (d2 >= d4 + (((d3 - d5) * (d6 - d4)) / (d7 - d5))) {
            return 0;
        }
        return d5 < d7 ? 1 : -1;
    }

    public static int pointCrossingsForQuad(double d2, double d3, double d4, double d5, double d6, double d7, double d8, double d9, int i2) {
        if (d3 < d5 && d3 < d7 && d3 < d9) {
            return 0;
        }
        if (d3 >= d5 && d3 >= d7 && d3 >= d9) {
            return 0;
        }
        if (d2 >= d4 && d2 >= d6 && d2 >= d8) {
            return 0;
        }
        if (d2 < d4 && d2 < d6 && d2 < d8) {
            return d3 >= d5 ? d3 < d9 ? 1 : 0 : d3 >= d9 ? -1 : 0;
        }
        if (i2 > 52) {
            return pointCrossingsForLine(d2, d3, d4, d5, d8, d9);
        }
        double d10 = (d4 + d6) / 2.0d;
        double d11 = (d5 + d7) / 2.0d;
        double d12 = (d6 + d8) / 2.0d;
        double d13 = (d7 + d9) / 2.0d;
        double d14 = (d10 + d12) / 2.0d;
        double d15 = (d11 + d13) / 2.0d;
        if (Double.isNaN(d14) || Double.isNaN(d15)) {
            return 0;
        }
        return pointCrossingsForQuad(d2, d3, d4, d5, d10, d11, d14, d15, i2 + 1) + pointCrossingsForQuad(d2, d3, d14, d15, d12, d13, d8, d9, i2 + 1);
    }

    public static int pointCrossingsForCubic(double d2, double d3, double d4, double d5, double d6, double d7, double d8, double d9, double d10, double d11, int i2) {
        if (d3 < d5 && d3 < d7 && d3 < d9 && d3 < d11) {
            return 0;
        }
        if (d3 >= d5 && d3 >= d7 && d3 >= d9 && d3 >= d11) {
            return 0;
        }
        if (d2 >= d4 && d2 >= d6 && d2 >= d8 && d2 >= d10) {
            return 0;
        }
        if (d2 < d4 && d2 < d6 && d2 < d8 && d2 < d10) {
            return d3 >= d5 ? d3 < d11 ? 1 : 0 : d3 >= d11 ? -1 : 0;
        }
        if (i2 > 52) {
            return pointCrossingsForLine(d2, d3, d4, d5, d10, d11);
        }
        double d12 = (d6 + d8) / 2.0d;
        double d13 = (d7 + d9) / 2.0d;
        double d14 = (d4 + d6) / 2.0d;
        double d15 = (d5 + d7) / 2.0d;
        double d16 = (d8 + d10) / 2.0d;
        double d17 = (d9 + d11) / 2.0d;
        double d18 = (d14 + d12) / 2.0d;
        double d19 = (d15 + d13) / 2.0d;
        double d20 = (d12 + d16) / 2.0d;
        double d21 = (d13 + d17) / 2.0d;
        double d22 = (d18 + d20) / 2.0d;
        double d23 = (d19 + d21) / 2.0d;
        if (Double.isNaN(d22) || Double.isNaN(d23)) {
            return 0;
        }
        return pointCrossingsForCubic(d2, d3, d4, d5, d14, d15, d18, d19, d22, d23, i2 + 1) + pointCrossingsForCubic(d2, d3, d22, d23, d20, d21, d16, d17, d10, d11, i2 + 1);
    }

    public static int rectCrossingsForPath(PathIterator pathIterator, double d2, double d3, double d4, double d5) {
        double d6;
        if (d4 <= d2 || d5 <= d3 || pathIterator.isDone()) {
            return 0;
        }
        double[] dArr = new double[6];
        if (pathIterator.currentSegment(dArr) != 0) {
            throw new IllegalPathStateException("missing initial moveto in path definition");
        }
        pathIterator.next();
        double d7 = dArr[0];
        double d8 = d7;
        double d9 = d7;
        double d10 = dArr[1];
        double d11 = d10;
        double d12 = d10;
        int iRectCrossingsForLine = 0;
        while (iRectCrossingsForLine != Integer.MIN_VALUE && !pathIterator.isDone()) {
            switch (pathIterator.currentSegment(dArr)) {
                case 0:
                    if (d9 != d8 || d12 != d11) {
                        d6 = d3;
                        iRectCrossingsForLine = rectCrossingsForLine(iRectCrossingsForLine, d2, d6, d4, d5, d9, d12, d8, d11);
                    }
                    d9 = d6;
                    d8 = dArr[0];
                    d12 = d6;
                    d11 = dArr[1];
                    break;
                case 1:
                    double d13 = dArr[0];
                    double d14 = dArr[1];
                    d6 = d3;
                    iRectCrossingsForLine = rectCrossingsForLine(iRectCrossingsForLine, d2, d6, d4, d5, d9, d12, d13, d14);
                    d9 = d13;
                    d12 = d14;
                    break;
                case 2:
                    double d15 = dArr[2];
                    double d16 = dArr[3];
                    d6 = d3;
                    iRectCrossingsForLine = rectCrossingsForQuad(iRectCrossingsForLine, d2, d6, d4, d5, d9, d12, dArr[0], dArr[1], d15, d16, 0);
                    d9 = d15;
                    d12 = d16;
                    break;
                case 3:
                    double d17 = dArr[4];
                    double d18 = dArr[5];
                    d6 = d3;
                    iRectCrossingsForLine = rectCrossingsForCubic(iRectCrossingsForLine, d2, d6, d4, d5, d9, d12, dArr[0], dArr[1], dArr[2], dArr[3], d17, d18, 0);
                    d9 = d17;
                    d12 = d18;
                    break;
                case 4:
                    if (d9 != d8 || d12 != d11) {
                        d6 = d3;
                        iRectCrossingsForLine = rectCrossingsForLine(iRectCrossingsForLine, d2, d6, d4, d5, d9, d12, d8, d11);
                    }
                    d9 = d8;
                    d12 = d11;
                    break;
            }
            pathIterator.next();
        }
        if (iRectCrossingsForLine != Integer.MIN_VALUE && (d9 != d8 || d12 != d11)) {
            iRectCrossingsForLine = rectCrossingsForLine(iRectCrossingsForLine, d2, d3, d4, d5, d9, d12, d8, d11);
        }
        return iRectCrossingsForLine;
    }

    public static int rectCrossingsForLine(int i2, double d2, double d3, double d4, double d5, double d6, double d7, double d8, double d9) {
        if (d7 >= d5 && d9 >= d5) {
            return i2;
        }
        if (d7 <= d3 && d9 <= d3) {
            return i2;
        }
        if (d6 <= d2 && d8 <= d2) {
            return i2;
        }
        if (d6 >= d4 && d8 >= d4) {
            if (d7 < d9) {
                if (d7 <= d3) {
                    i2++;
                }
                if (d9 >= d5) {
                    i2++;
                }
            } else if (d9 < d7) {
                if (d9 <= d3) {
                    i2--;
                }
                if (d7 >= d5) {
                    i2--;
                }
            }
            return i2;
        }
        if (d6 > d2 && d6 < d4 && d7 > d3 && d7 < d5) {
            return Integer.MIN_VALUE;
        }
        if (d8 > d2 && d8 < d4 && d9 > d3 && d9 < d5) {
            return Integer.MIN_VALUE;
        }
        double d10 = d6;
        if (d7 < d3) {
            d10 += ((d3 - d7) * (d8 - d6)) / (d9 - d7);
        } else if (d7 > d5) {
            d10 += ((d5 - d7) * (d8 - d6)) / (d9 - d7);
        }
        double d11 = d8;
        if (d9 < d3) {
            d11 += ((d3 - d9) * (d6 - d8)) / (d7 - d9);
        } else if (d9 > d5) {
            d11 += ((d5 - d9) * (d6 - d8)) / (d7 - d9);
        }
        if (d10 <= d2 && d11 <= d2) {
            return i2;
        }
        if (d10 >= d4 && d11 >= d4) {
            if (d7 < d9) {
                if (d7 <= d3) {
                    i2++;
                }
                if (d9 >= d5) {
                    i2++;
                }
            } else if (d9 < d7) {
                if (d9 <= d3) {
                    i2--;
                }
                if (d7 >= d5) {
                    i2--;
                }
            }
            return i2;
        }
        return Integer.MIN_VALUE;
    }

    public static int rectCrossingsForQuad(int i2, double d2, double d3, double d4, double d5, double d6, double d7, double d8, double d9, double d10, double d11, int i3) {
        if (d7 >= d5 && d9 >= d5 && d11 >= d5) {
            return i2;
        }
        if (d7 <= d3 && d9 <= d3 && d11 <= d3) {
            return i2;
        }
        if (d6 <= d2 && d8 <= d2 && d10 <= d2) {
            return i2;
        }
        if (d6 >= d4 && d8 >= d4 && d10 >= d4) {
            if (d7 < d11) {
                if (d7 <= d3 && d11 > d3) {
                    i2++;
                }
                if (d7 < d5 && d11 >= d5) {
                    i2++;
                }
            } else if (d11 < d7) {
                if (d11 <= d3 && d7 > d3) {
                    i2--;
                }
                if (d11 < d5 && d7 >= d5) {
                    i2--;
                }
            }
            return i2;
        }
        if (d6 < d4 && d6 > d2 && d7 < d5 && d7 > d3) {
            return Integer.MIN_VALUE;
        }
        if (d10 < d4 && d10 > d2 && d11 < d5 && d11 > d3) {
            return Integer.MIN_VALUE;
        }
        if (i3 > 52) {
            return rectCrossingsForLine(i2, d2, d3, d4, d5, d6, d7, d10, d11);
        }
        double d12 = (d6 + d8) / 2.0d;
        double d13 = (d7 + d9) / 2.0d;
        double d14 = (d8 + d10) / 2.0d;
        double d15 = (d9 + d11) / 2.0d;
        double d16 = (d12 + d14) / 2.0d;
        double d17 = (d13 + d15) / 2.0d;
        if (Double.isNaN(d16) || Double.isNaN(d17)) {
            return 0;
        }
        int iRectCrossingsForQuad = rectCrossingsForQuad(i2, d2, d3, d4, d5, d6, d7, d12, d13, d16, d17, i3 + 1);
        if (iRectCrossingsForQuad != Integer.MIN_VALUE) {
            iRectCrossingsForQuad = rectCrossingsForQuad(iRectCrossingsForQuad, d2, d3, d4, d5, d16, d17, d14, d15, d10, d11, i3 + 1);
        }
        return iRectCrossingsForQuad;
    }

    public static int rectCrossingsForCubic(int i2, double d2, double d3, double d4, double d5, double d6, double d7, double d8, double d9, double d10, double d11, double d12, double d13, int i3) {
        if (d7 >= d5 && d9 >= d5 && d11 >= d5 && d13 >= d5) {
            return i2;
        }
        if (d7 <= d3 && d9 <= d3 && d11 <= d3 && d13 <= d3) {
            return i2;
        }
        if (d6 <= d2 && d8 <= d2 && d10 <= d2 && d12 <= d2) {
            return i2;
        }
        if (d6 >= d4 && d8 >= d4 && d10 >= d4 && d12 >= d4) {
            if (d7 < d13) {
                if (d7 <= d3 && d13 > d3) {
                    i2++;
                }
                if (d7 < d5 && d13 >= d5) {
                    i2++;
                }
            } else if (d13 < d7) {
                if (d13 <= d3 && d7 > d3) {
                    i2--;
                }
                if (d13 < d5 && d7 >= d5) {
                    i2--;
                }
            }
            return i2;
        }
        if (d6 > d2 && d6 < d4 && d7 > d3 && d7 < d5) {
            return Integer.MIN_VALUE;
        }
        if (d12 > d2 && d12 < d4 && d13 > d3 && d13 < d5) {
            return Integer.MIN_VALUE;
        }
        if (i3 > 52) {
            return rectCrossingsForLine(i2, d2, d3, d4, d5, d6, d7, d12, d13);
        }
        double d14 = (d8 + d10) / 2.0d;
        double d15 = (d9 + d11) / 2.0d;
        double d16 = (d6 + d8) / 2.0d;
        double d17 = (d7 + d9) / 2.0d;
        double d18 = (d10 + d12) / 2.0d;
        double d19 = (d11 + d13) / 2.0d;
        double d20 = (d16 + d14) / 2.0d;
        double d21 = (d17 + d15) / 2.0d;
        double d22 = (d14 + d18) / 2.0d;
        double d23 = (d15 + d19) / 2.0d;
        double d24 = (d20 + d22) / 2.0d;
        double d25 = (d21 + d23) / 2.0d;
        if (Double.isNaN(d24) || Double.isNaN(d25)) {
            return 0;
        }
        int iRectCrossingsForCubic = rectCrossingsForCubic(i2, d2, d3, d4, d5, d6, d7, d16, d17, d20, d21, d24, d25, i3 + 1);
        if (iRectCrossingsForCubic != Integer.MIN_VALUE) {
            iRectCrossingsForCubic = rectCrossingsForCubic(iRectCrossingsForCubic, d2, d3, d4, d5, d24, d25, d22, d23, d18, d19, d12, d13, i3 + 1);
        }
        return iRectCrossingsForCubic;
    }

    public Curve(int i2) {
        this.direction = i2;
    }

    public final int getDirection() {
        return this.direction;
    }

    public final Curve getWithDirection(int i2) {
        return this.direction == i2 ? this : getReversedCurve();
    }

    public static double round(double d2) {
        return d2;
    }

    public static int orderof(double d2, double d3) {
        if (d2 < d3) {
            return -1;
        }
        if (d2 > d3) {
            return 1;
        }
        return 0;
    }

    public static long signeddiffbits(double d2, double d3) {
        return Double.doubleToLongBits(d2) - Double.doubleToLongBits(d3);
    }

    public static long diffbits(double d2, double d3) {
        return Math.abs(Double.doubleToLongBits(d2) - Double.doubleToLongBits(d3));
    }

    public static double prev(double d2) {
        return Double.longBitsToDouble(Double.doubleToLongBits(d2) - 1);
    }

    public static double next(double d2) {
        return Double.longBitsToDouble(Double.doubleToLongBits(d2) + 1);
    }

    public String toString() {
        return "Curve[" + getOrder() + ", (" + round(getX0()) + ", " + round(getY0()) + "), " + controlPointString() + "(" + round(getX1()) + ", " + round(getY1()) + "), " + (this.direction == 1 ? PdfOps.D_TOKEN : "U") + "]";
    }

    public String controlPointString() {
        return "";
    }

    public int crossingsFor(double d2, double d3) {
        if (d3 < getYTop() || d3 >= getYBot() || d2 >= getXMax()) {
            return 0;
        }
        if (d2 < getXMin() || d2 < XforY(d3)) {
            return 1;
        }
        return 0;
    }

    public boolean accumulateCrossings(Crossings crossings) {
        double d2;
        double dNextVertical;
        double d3;
        double dTforY;
        double xHi = crossings.getXHi();
        if (getXMin() >= xHi) {
            return false;
        }
        double xLo = crossings.getXLo();
        double yLo = crossings.getYLo();
        double yHi = crossings.getYHi();
        double yTop = getYTop();
        double yBot = getYBot();
        if (yTop < yLo) {
            if (yBot <= yLo) {
                return false;
            }
            d2 = yLo;
            dNextVertical = TforY(yLo);
        } else {
            if (yTop >= yHi) {
                return false;
            }
            d2 = yTop;
            dNextVertical = 0.0d;
        }
        if (yBot > yHi) {
            d3 = yHi;
            dTforY = TforY(yHi);
        } else {
            d3 = yBot;
            dTforY = 1.0d;
        }
        boolean z2 = false;
        boolean z3 = false;
        while (true) {
            double dXforT = XforT(dNextVertical);
            if (dXforT < xHi) {
                if (z3 || dXforT > xLo) {
                    return true;
                }
                z2 = true;
            } else {
                if (z2) {
                    return true;
                }
                z3 = true;
            }
            if (dNextVertical < dTforY) {
                dNextVertical = nextVertical(dNextVertical, dTforY);
            } else {
                if (z2) {
                    crossings.record(d2, d3, this.direction);
                    return false;
                }
                return false;
            }
        }
    }

    public Curve getSubCurve(double d2, double d3) {
        return getSubCurve(d2, d3, this.direction);
    }

    public int compareTo(Curve curve, double[] dArr) {
        double d2;
        double d3 = dArr[0];
        double dMin = Math.min(Math.min(dArr[1], getYBot()), curve.getYBot());
        if (dMin <= dArr[0]) {
            System.err.println("this == " + ((Object) this));
            System.err.println("that == " + ((Object) curve));
            System.out.println("target range = " + dArr[0] + "=>" + dArr[1]);
            throw new InternalError("backstepping from " + dArr[0] + " to " + dMin);
        }
        dArr[1] = dMin;
        if (getXMax() <= curve.getXMin()) {
            if (getXMin() == curve.getXMax()) {
                return 0;
            }
            return -1;
        }
        if (getXMin() >= curve.getXMax()) {
            return 1;
        }
        double dTforY = TforY(d3);
        double dYforT = YforT(dTforY);
        if (dYforT < d3) {
            dTforY = refineTforY(dTforY, dYforT, d3);
            dYforT = YforT(dTforY);
        }
        double dTforY2 = TforY(dMin);
        if (YforT(dTforY2) < d3) {
            dTforY2 = refineTforY(dTforY2, YforT(dTforY2), d3);
        }
        double dTforY3 = curve.TforY(d3);
        double dYforT2 = curve.YforT(dTforY3);
        if (dYforT2 < d3) {
            dTforY3 = curve.refineTforY(dTforY3, dYforT2, d3);
            dYforT2 = curve.YforT(dTforY3);
        }
        double dTforY4 = curve.TforY(dMin);
        if (curve.YforT(dTforY4) < d3) {
            dTforY4 = curve.refineTforY(dTforY4, curve.YforT(dTforY4), d3);
        }
        double dXforT = XforT(dTforY);
        double dXforT2 = curve.XforT(dTforY3);
        double dMax = Math.max(Math.max(Math.abs(d3), Math.abs(dMin)) * 1.0E-14d, 1.0E-300d);
        if (fairlyClose(dXforT, dXforT2)) {
            double d4 = dMax;
            double dMin2 = Math.min(dMax * 1.0E13d, (dMin - d3) * 0.1d);
            double d5 = d3;
            while (true) {
                d2 = d5 + d4;
                if (d2 > dMin) {
                    break;
                }
                double dXforY = XforY(d2);
                double dXforY2 = curve.XforY(d2);
                if (fairlyClose(dXforY, dXforY2)) {
                    double d6 = d4 * 2.0d;
                    d4 = dXforY2;
                    if (d6 > dMin2) {
                        d4 = dMin2;
                    }
                    d5 = d2;
                } else {
                    d2 -= d4;
                    while (true) {
                        d4 /= 2.0d;
                        double d7 = d2 + d4;
                        if (d7 <= d2) {
                            break;
                        }
                        if (fairlyClose(XforY(d7), curve.XforY(d7))) {
                            d2 = d7;
                        }
                    }
                }
            }
            if (d2 > d3) {
                if (d2 < dMin) {
                    dArr[1] = d2;
                    return 0;
                }
                return 0;
            }
        }
        if (dMax <= 0.0d) {
            System.out.println("ymin = " + dMax);
        }
        while (true) {
            if (dTforY >= dTforY2 || dTforY3 >= dTforY4) {
                break;
            }
            double dNextVertical = nextVertical(dTforY, dTforY2);
            double dXforT3 = XforT(dNextVertical);
            double dYforT3 = YforT(dNextVertical);
            double dNextVertical2 = curve.nextVertical(dTforY3, dTforY4);
            double dXforT4 = curve.XforT(dNextVertical2);
            double dYforT4 = curve.YforT(dNextVertical2);
            try {
                if (findIntersect(curve, dArr, dMax, 0, 0, dTforY, dXforT, dYforT, dNextVertical, dXforT3, dYforT3, dTforY3, dXforT2, dYforT2, dNextVertical2, dXforT4, dYforT4)) {
                    break;
                }
                if (dYforT3 < dYforT4) {
                    if (dYforT3 <= dArr[0]) {
                        dTforY = dNextVertical;
                        dXforT = dXforT3;
                        dYforT = dYforT3;
                    } else if (dYforT3 < dArr[1]) {
                        dArr[1] = dYforT3;
                    }
                } else if (dYforT4 <= dArr[0]) {
                    dTforY3 = dNextVertical2;
                    dXforT2 = dXforT4;
                    dYforT2 = dYforT4;
                } else if (dYforT4 < dArr[1]) {
                    dArr[1] = dYforT4;
                }
            } catch (Throwable th) {
                System.err.println("Error: " + ((Object) th));
                System.err.println("y range was " + dArr[0] + "=>" + dArr[1]);
                System.err.println("s y range is " + dYforT + "=>" + dYforT3);
                System.err.println("t y range is " + dYforT2 + "=>" + dYforT4);
                System.err.println("ymin is " + dMax);
                return 0;
            }
        }
        double d8 = (dArr[0] + dArr[1]) / 2.0d;
        return orderof(XforY(d8), curve.XforY(d8));
    }

    public boolean findIntersect(Curve curve, double[] dArr, double d2, int i2, int i3, double d3, double d4, double d5, double d6, double d7, double d8, double d9, double d10, double d11, double d12, double d13, double d14) {
        if (d5 > d14 || d11 > d8 || Math.min(d4, d7) > Math.max(d10, d13) || Math.max(d4, d7) < Math.min(d10, d13)) {
            return false;
        }
        if (d6 - d3 > 0.001d) {
            double d15 = (d3 + d6) / 2.0d;
            double dXforT = XforT(d15);
            double dYforT = YforT(d15);
            if (d15 == d3 || d15 == d6) {
                System.out.println("s0 = " + d3);
                System.out.println("s1 = " + d6);
                throw new InternalError("no s progress!");
            }
            if (d12 - d9 > 0.001d) {
                double d16 = (d9 + d12) / 2.0d;
                double dXforT2 = curve.XforT(d16);
                double dYforT2 = curve.YforT(d16);
                if (d16 == d9 || d16 == d12) {
                    System.out.println("t0 = " + d9);
                    System.out.println("t1 = " + d12);
                    throw new InternalError("no t progress!");
                }
                if (dYforT >= d11 && dYforT2 >= d5 && findIntersect(curve, dArr, d2, i2 + 1, i3 + 1, d3, d4, d5, d15, dXforT, dYforT, d9, d10, d11, d16, dXforT2, dYforT2)) {
                    return true;
                }
                if (dYforT >= dYforT2 && findIntersect(curve, dArr, d2, i2 + 1, i3 + 1, d3, d4, d5, d15, dXforT, dYforT, d16, dXforT2, dYforT2, d12, d13, d14)) {
                    return true;
                }
                if (dYforT2 >= dYforT && findIntersect(curve, dArr, d2, i2 + 1, i3 + 1, d15, dXforT, dYforT, d6, d7, d8, d9, d10, d11, d16, dXforT2, dYforT2)) {
                    return true;
                }
                if (d8 >= dYforT2 && d14 >= dYforT && findIntersect(curve, dArr, d2, i2 + 1, i3 + 1, d15, dXforT, dYforT, d6, d7, d8, d16, dXforT2, dYforT2, d12, d13, d14)) {
                    return true;
                }
                return false;
            }
            if (dYforT >= d11 && findIntersect(curve, dArr, d2, i2 + 1, i3, d3, d4, d5, d15, dXforT, dYforT, d9, d10, d11, d12, d13, d14)) {
                return true;
            }
            if (d14 >= dYforT && findIntersect(curve, dArr, d2, i2 + 1, i3, d15, dXforT, dYforT, d6, d7, d8, d9, d10, d11, d12, d13, d14)) {
                return true;
            }
            return false;
        }
        if (d12 - d9 > 0.001d) {
            double d17 = (d9 + d12) / 2.0d;
            double dXforT3 = curve.XforT(d17);
            double dYforT3 = curve.YforT(d17);
            if (d17 == d9 || d17 == d12) {
                System.out.println("t0 = " + d9);
                System.out.println("t1 = " + d12);
                throw new InternalError("no t progress!");
            }
            if (dYforT3 >= d5 && findIntersect(curve, dArr, d2, i2, i3 + 1, d3, d4, d5, d6, d7, d8, d9, d10, d11, d17, dXforT3, dYforT3)) {
                return true;
            }
            if (d8 >= dYforT3 && findIntersect(curve, dArr, d2, i2, i3 + 1, d3, d4, d5, d6, d7, d8, d17, dXforT3, dYforT3, d12, d13, d14)) {
                return true;
            }
            return false;
        }
        double d18 = d7 - d4;
        double d19 = d8 - d5;
        double d20 = d13 - d10;
        double d21 = d14 - d11;
        double d22 = d10 - d4;
        double d23 = d11 - d5;
        double d24 = (d20 * d19) - (d21 * d18);
        if (d24 != 0.0d) {
            double d25 = 1.0d / d24;
            double d26 = ((d20 * d23) - (d21 * d22)) * d25;
            double d27 = ((d18 * d23) - (d19 * d22)) * d25;
            if (d26 >= 0.0d && d26 <= 1.0d && d27 >= 0.0d && d27 <= 1.0d) {
                double d28 = d3 + (d26 * (d6 - d3));
                double d29 = d9 + (d27 * (d12 - d9));
                if (d28 < 0.0d || d28 > 1.0d || d29 < 0.0d || d29 > 1.0d) {
                    System.out.println("Uh oh!");
                }
                double dYforT4 = (YforT(d28) + curve.YforT(d29)) / 2.0d;
                if (dYforT4 <= dArr[1] && dYforT4 > dArr[0]) {
                    dArr[1] = dYforT4;
                    return true;
                }
                return false;
            }
            return false;
        }
        return false;
    }

    /* JADX WARN: Code restructure failed: missing block: B:8:0x001e, code lost:
    
        return r12;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public double refineTforY(double r6, double r8, double r10) {
        /*
            r5 = this;
            r0 = 4607182418800017408(0x3ff0000000000000, double:1.0)
            r12 = r0
        L3:
            r0 = r6
            r1 = r12
            double r0 = r0 + r1
            r1 = 4611686018427387904(0x4000000000000000, double:2.0)
            double r0 = r0 / r1
            r14 = r0
            r0 = r14
            r1 = r6
            int r0 = (r0 > r1 ? 1 : (r0 == r1 ? 0 : -1))
            if (r0 == 0) goto L1c
            r0 = r14
            r1 = r12
            int r0 = (r0 > r1 ? 1 : (r0 == r1 ? 0 : -1))
            if (r0 != 0) goto L1f
        L1c:
            r0 = r12
            return r0
        L1f:
            r0 = r5
            r1 = r14
            double r0 = r0.YforT(r1)
            r16 = r0
            r0 = r16
            r1 = r10
            int r0 = (r0 > r1 ? 1 : (r0 == r1 ? 0 : -1))
            if (r0 >= 0) goto L38
            r0 = r14
            r6 = r0
            r0 = r16
            r8 = r0
            goto L4a
        L38:
            r0 = r16
            r1 = r10
            int r0 = (r0 > r1 ? 1 : (r0 == r1 ? 0 : -1))
            if (r0 <= 0) goto L47
            r0 = r14
            r12 = r0
            goto L4a
        L47:
            r0 = r12
            return r0
        L4a:
            goto L3
        */
        throw new UnsupportedOperationException("Method not decompiled: sun.awt.geom.Curve.refineTforY(double, double, double):double");
    }

    public boolean fairlyClose(double d2, double d3) {
        return Math.abs(d2 - d3) < Math.max(Math.abs(d2), Math.abs(d3)) * 1.0E-10d;
    }
}
