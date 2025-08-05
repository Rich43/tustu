package sun.awt.geom;

import java.awt.geom.PathIterator;
import java.util.Enumeration;
import java.util.Vector;

/* loaded from: rt.jar:sun/awt/geom/Crossings.class */
public abstract class Crossings {
    public static final boolean debug = false;
    double xlo;
    double ylo;
    double xhi;
    double yhi;
    int limit = 0;
    double[] yranges = new double[10];
    private Vector tmp = new Vector();

    public abstract void record(double d2, double d3, int i2);

    public abstract boolean covers(double d2, double d3);

    public Crossings(double d2, double d3, double d4, double d5) {
        this.xlo = d2;
        this.ylo = d3;
        this.xhi = d4;
        this.yhi = d5;
    }

    public final double getXLo() {
        return this.xlo;
    }

    public final double getYLo() {
        return this.ylo;
    }

    public final double getXHi() {
        return this.xhi;
    }

    public final double getYHi() {
        return this.yhi;
    }

    public void print() {
        System.out.println("Crossings [");
        System.out.println("  bounds = [" + this.ylo + ", " + this.yhi + "]");
        for (int i2 = 0; i2 < this.limit; i2 += 2) {
            System.out.println("  [" + this.yranges[i2] + ", " + this.yranges[i2 + 1] + "]");
        }
        System.out.println("]");
    }

    public final boolean isEmpty() {
        return this.limit == 0;
    }

    public static Crossings findCrossings(Vector vector, double d2, double d3, double d4, double d5) {
        EvenOdd evenOdd = new EvenOdd(d2, d3, d4, d5);
        Enumeration enumerationElements = vector.elements();
        while (enumerationElements.hasMoreElements()) {
            if (((Curve) enumerationElements.nextElement()).accumulateCrossings(evenOdd)) {
                return null;
            }
        }
        return evenOdd;
    }

    public static Crossings findCrossings(PathIterator pathIterator, double d2, double d3, double d4, double d5) {
        double d6;
        Crossings nonZero;
        if (pathIterator.getWindingRule() == 0) {
            d6 = d2;
            nonZero = new EvenOdd(d6, d3, d4, d5);
        } else {
            d6 = d2;
            nonZero = new NonZero(d6, d3, d4, d5);
        }
        double[] dArr = new double[23];
        double d7 = 0.0d;
        double d8 = 0.0d;
        double d9 = 0.0d;
        double d10 = 0.0d;
        while (!pathIterator.isDone()) {
            switch (pathIterator.currentSegment(dArr)) {
                case 0:
                    if (d8 != d10) {
                        d6 = d10;
                        if (nonZero.accumulateLine(d9, d6, d7, d8)) {
                            return null;
                        }
                    }
                    d9 = d6;
                    d7 = dArr[0];
                    d10 = d6;
                    d8 = dArr[1];
                    break;
                case 1:
                    double d11 = dArr[0];
                    double d12 = dArr[1];
                    d6 = d10;
                    if (nonZero.accumulateLine(d9, d6, d11, d12)) {
                        return null;
                    }
                    d9 = d11;
                    d10 = d12;
                    break;
                case 2:
                    double d13 = dArr[2];
                    double d14 = dArr[3];
                    d6 = d10;
                    if (nonZero.accumulateQuad(d9, d6, dArr)) {
                        return null;
                    }
                    d9 = d13;
                    d10 = d14;
                    break;
                case 3:
                    double d15 = dArr[4];
                    double d16 = dArr[5];
                    d6 = d10;
                    if (nonZero.accumulateCubic(d9, d6, dArr)) {
                        return null;
                    }
                    d9 = d15;
                    d10 = d16;
                    break;
                case 4:
                    if (d8 != d10) {
                        d6 = d10;
                        if (nonZero.accumulateLine(d9, d6, d7, d8)) {
                            return null;
                        }
                    }
                    d9 = d7;
                    d10 = d8;
                    break;
            }
            pathIterator.next();
        }
        if (d8 != d10 && nonZero.accumulateLine(d9, d10, d7, d8)) {
            return null;
        }
        return nonZero;
    }

    public boolean accumulateLine(double d2, double d3, double d4, double d5) {
        if (d3 <= d5) {
            return accumulateLine(d2, d3, d4, d5, 1);
        }
        return accumulateLine(d4, d5, d2, d3, -1);
    }

    public boolean accumulateLine(double d2, double d3, double d4, double d5, int i2) {
        double d6;
        double d7;
        double d8;
        double d9;
        if (this.yhi <= d3 || this.ylo >= d5) {
            return false;
        }
        if (d2 >= this.xhi && d4 >= this.xhi) {
            return false;
        }
        if (d3 == d5) {
            return d2 >= this.xlo || d4 >= this.xlo;
        }
        double d10 = d4 - d2;
        double d11 = d5 - d3;
        if (d3 < this.ylo) {
            d6 = d2 + (((this.ylo - d3) * d10) / d11);
            d7 = this.ylo;
        } else {
            d6 = d2;
            d7 = d3;
        }
        if (this.yhi < d5) {
            d8 = d2 + (((this.yhi - d3) * d10) / d11);
            d9 = this.yhi;
        } else {
            d8 = d4;
            d9 = d5;
        }
        if (d6 >= this.xhi && d8 >= this.xhi) {
            return false;
        }
        if (d6 > this.xlo || d8 > this.xlo) {
            return true;
        }
        record(d7, d9, i2);
        return false;
    }

    public boolean accumulateQuad(double d2, double d3, double[] dArr) {
        if (d3 < this.ylo && dArr[1] < this.ylo && dArr[3] < this.ylo) {
            return false;
        }
        if (d3 > this.yhi && dArr[1] > this.yhi && dArr[3] > this.yhi) {
            return false;
        }
        if (d2 > this.xhi && dArr[0] > this.xhi && dArr[2] > this.xhi) {
            return false;
        }
        if (d2 >= this.xlo || dArr[0] >= this.xlo || dArr[2] >= this.xlo) {
            Curve.insertQuad(this.tmp, d2, d3, dArr);
            Enumeration enumerationElements = this.tmp.elements();
            while (enumerationElements.hasMoreElements()) {
                if (((Curve) enumerationElements.nextElement()).accumulateCrossings(this)) {
                    return true;
                }
            }
            this.tmp.clear();
            return false;
        }
        if (d3 < dArr[3]) {
            record(Math.max(d3, this.ylo), Math.min(dArr[3], this.yhi), 1);
            return false;
        }
        if (d3 > dArr[3]) {
            record(Math.max(dArr[3], this.ylo), Math.min(d3, this.yhi), -1);
            return false;
        }
        return false;
    }

    public boolean accumulateCubic(double d2, double d3, double[] dArr) {
        if (d3 < this.ylo && dArr[1] < this.ylo && dArr[3] < this.ylo && dArr[5] < this.ylo) {
            return false;
        }
        if (d3 > this.yhi && dArr[1] > this.yhi && dArr[3] > this.yhi && dArr[5] > this.yhi) {
            return false;
        }
        if (d2 > this.xhi && dArr[0] > this.xhi && dArr[2] > this.xhi && dArr[4] > this.xhi) {
            return false;
        }
        if (d2 < this.xlo && dArr[0] < this.xlo && dArr[2] < this.xlo && dArr[4] < this.xlo) {
            if (d3 <= dArr[5]) {
                record(Math.max(d3, this.ylo), Math.min(dArr[5], this.yhi), 1);
                return false;
            }
            record(Math.max(dArr[5], this.ylo), Math.min(d3, this.yhi), -1);
            return false;
        }
        Curve.insertCubic(this.tmp, d2, d3, dArr);
        Enumeration enumerationElements = this.tmp.elements();
        while (enumerationElements.hasMoreElements()) {
            if (((Curve) enumerationElements.nextElement()).accumulateCrossings(this)) {
                return true;
            }
        }
        this.tmp.clear();
        return false;
    }

    /* loaded from: rt.jar:sun/awt/geom/Crossings$EvenOdd.class */
    public static final class EvenOdd extends Crossings {
        public EvenOdd(double d2, double d3, double d4, double d5) {
            super(d2, d3, d4, d5);
        }

        @Override // sun.awt.geom.Crossings
        public final boolean covers(double d2, double d3) {
            return this.limit == 2 && this.yranges[0] <= d2 && this.yranges[1] >= d3;
        }

        @Override // sun.awt.geom.Crossings
        public void record(double d2, double d3, int i2) {
            double d4;
            double d5;
            double d6;
            double d7;
            if (d2 >= d3) {
                return;
            }
            int i3 = 0;
            while (i3 < this.limit && d2 > this.yranges[i3 + 1]) {
                i3 += 2;
            }
            int i4 = i3;
            while (i3 < this.limit) {
                int i5 = i3;
                int i6 = i3 + 1;
                double d8 = this.yranges[i5];
                i3 = i6 + 1;
                double d9 = this.yranges[i6];
                if (d3 < d8) {
                    int i7 = i4;
                    int i8 = i4 + 1;
                    this.yranges[i7] = d2;
                    i4 = i8 + 1;
                    this.yranges[i8] = d3;
                    d2 = d8;
                    d3 = d9;
                } else {
                    if (d2 < d8) {
                        d4 = d2;
                        d5 = d8;
                    } else {
                        d4 = d8;
                        d5 = d2;
                    }
                    if (d3 < d9) {
                        d6 = d3;
                        d7 = d9;
                    } else {
                        d6 = d9;
                        d7 = d3;
                    }
                    if (d5 == d6) {
                        d2 = d4;
                        d3 = d7;
                    } else {
                        if (d5 > d6) {
                            double d10 = d6;
                            d6 = d5;
                            d5 = d10;
                        }
                        if (d4 != d5) {
                            int i9 = i4;
                            int i10 = i4 + 1;
                            this.yranges[i9] = d4;
                            i4 = i10 + 1;
                            this.yranges[i10] = d5;
                        }
                        d2 = d6;
                        d3 = d7;
                    }
                    if (d2 >= d3) {
                        break;
                    }
                }
            }
            if (i4 < i3 && i3 < this.limit) {
                System.arraycopy(this.yranges, i3, this.yranges, i4, this.limit - i3);
            }
            int i11 = i4 + (this.limit - i3);
            if (d2 < d3) {
                if (i11 >= this.yranges.length) {
                    double[] dArr = new double[i11 + 10];
                    System.arraycopy(this.yranges, 0, dArr, 0, i11);
                    this.yranges = dArr;
                }
                int i12 = i11 + 1;
                this.yranges[i11] = d2;
                i11 = i12 + 1;
                this.yranges[i12] = d3;
            }
            this.limit = i11;
        }
    }

    /* loaded from: rt.jar:sun/awt/geom/Crossings$NonZero.class */
    public static final class NonZero extends Crossings {
        private int[] crosscounts;

        public NonZero(double d2, double d3, double d4, double d5) {
            super(d2, d3, d4, d5);
            this.crosscounts = new int[this.yranges.length / 2];
        }

        @Override // sun.awt.geom.Crossings
        public final boolean covers(double d2, double d3) {
            int i2 = 0;
            while (i2 < this.limit) {
                int i3 = i2;
                int i4 = i2 + 1;
                double d4 = this.yranges[i3];
                i2 = i4 + 1;
                double d5 = this.yranges[i4];
                if (d2 < d5) {
                    if (d2 < d4) {
                        return false;
                    }
                    if (d3 <= d5) {
                        return true;
                    }
                    d2 = d5;
                }
            }
            return d2 >= d3;
        }

        public void remove(int i2) {
            this.limit -= 2;
            int i3 = this.limit - i2;
            if (i3 > 0) {
                System.arraycopy(this.yranges, i2 + 2, this.yranges, i2, i3);
                System.arraycopy(this.crosscounts, (i2 / 2) + 1, this.crosscounts, i2 / 2, i3 / 2);
            }
        }

        public void insert(int i2, double d2, double d3, int i3) {
            int i4 = this.limit - i2;
            double[] dArr = this.yranges;
            int[] iArr = this.crosscounts;
            if (this.limit >= this.yranges.length) {
                this.yranges = new double[this.limit + 10];
                System.arraycopy(dArr, 0, this.yranges, 0, i2);
                this.crosscounts = new int[(this.limit + 10) / 2];
                System.arraycopy(iArr, 0, this.crosscounts, 0, i2 / 2);
            }
            if (i4 > 0) {
                System.arraycopy(dArr, i2, this.yranges, i2 + 2, i4);
                System.arraycopy(iArr, i2 / 2, this.crosscounts, (i2 / 2) + 1, i4 / 2);
            }
            this.yranges[i2 + 0] = d2;
            this.yranges[i2 + 1] = d3;
            this.crosscounts[i2 / 2] = i3;
            this.limit += 2;
        }

        @Override // sun.awt.geom.Crossings
        public void record(double d2, double d3, int i2) {
            if (d2 >= d3) {
                return;
            }
            int i3 = 0;
            while (i3 < this.limit && d2 > this.yranges[i3 + 1]) {
                i3 += 2;
            }
            if (i3 < this.limit) {
                int i4 = this.crosscounts[i3 / 2];
                double d4 = this.yranges[i3 + 0];
                double d5 = this.yranges[i3 + 1];
                if (d5 == d2 && i4 == i2) {
                    if (i3 + 2 == this.limit) {
                        this.yranges[i3 + 1] = d3;
                        return;
                    }
                    remove(i3);
                    d2 = d4;
                    i4 = this.crosscounts[i3 / 2];
                    d4 = this.yranges[i3 + 0];
                    d5 = this.yranges[i3 + 1];
                }
                if (d3 < d4) {
                    insert(i3, d2, d3, i2);
                    return;
                }
                if (d3 == d4 && i4 == i2) {
                    this.yranges[i3] = d2;
                    return;
                }
                if (d2 < d4) {
                    insert(i3, d2, d4, i2);
                    i3 += 2;
                    d2 = d4;
                } else if (d4 < d2) {
                    insert(i3, d4, d2, i4);
                    i3 += 2;
                }
                int i5 = i4 + i2;
                double dMin = Math.min(d3, d5);
                if (i5 == 0) {
                    remove(i3);
                } else {
                    this.crosscounts[i3 / 2] = i5;
                    int i6 = i3;
                    int i7 = i3 + 1;
                    this.yranges[i6] = d2;
                    i3 = i7 + 1;
                    this.yranges[i7] = dMin;
                }
                d2 = dMin;
                if (dMin < d5) {
                    insert(i3, dMin, d5, i4);
                }
            }
            if (d2 < d3) {
                insert(i3, d2, d3, i2);
            }
        }
    }
}
