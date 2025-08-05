package com.sun.javafx.geom;

import java.util.Enumeration;
import java.util.Vector;

/* loaded from: jfxrt.jar:com/sun/javafx/geom/Crossings.class */
public abstract class Crossings {
    public static final boolean debug = false;
    int limit = 0;
    double[] yranges = new double[10];
    double xlo;
    double ylo;
    double xhi;
    double yhi;

    public abstract void record(double d2, double d3, int i2);

    public abstract boolean covers(double d2, double d3);

    public Crossings(double xlo, double ylo, double xhi, double yhi) {
        this.xlo = xlo;
        this.ylo = ylo;
        this.xhi = xhi;
        this.yhi = yhi;
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

    public static Crossings findCrossings(Vector curves, double xlo, double ylo, double xhi, double yhi) {
        Crossings cross = new EvenOdd(xlo, ylo, xhi, yhi);
        Enumeration enum_ = curves.elements();
        while (enum_.hasMoreElements()) {
            Curve c2 = (Curve) enum_.nextElement();
            if (c2.accumulateCrossings(cross)) {
                return null;
            }
        }
        return cross;
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/geom/Crossings$EvenOdd.class */
    public static final class EvenOdd extends Crossings {
        public EvenOdd(double xlo, double ylo, double xhi, double yhi) {
            super(xlo, ylo, xhi, yhi);
        }

        @Override // com.sun.javafx.geom.Crossings
        public final boolean covers(double ystart, double yend) {
            return this.limit == 2 && this.yranges[0] <= ystart && this.yranges[1] >= yend;
        }

        @Override // com.sun.javafx.geom.Crossings
        public void record(double ystart, double yend, int direction) {
            double yll;
            double ylh;
            double yhl;
            double yhh;
            if (ystart >= yend) {
                return;
            }
            int from = 0;
            while (from < this.limit && ystart > this.yranges[from + 1]) {
                from += 2;
            }
            int to = from;
            while (from < this.limit) {
                int i2 = from;
                int from2 = from + 1;
                double yrlo = this.yranges[i2];
                from = from2 + 1;
                double yrhi = this.yranges[from2];
                if (yend < yrlo) {
                    int i3 = to;
                    int to2 = to + 1;
                    this.yranges[i3] = ystart;
                    to = to2 + 1;
                    this.yranges[to2] = yend;
                    ystart = yrlo;
                    yend = yrhi;
                } else {
                    if (ystart < yrlo) {
                        yll = ystart;
                        ylh = yrlo;
                    } else {
                        yll = yrlo;
                        ylh = ystart;
                    }
                    if (yend < yrhi) {
                        yhl = yend;
                        yhh = yrhi;
                    } else {
                        yhl = yrhi;
                        yhh = yend;
                    }
                    if (ylh == yhl) {
                        ystart = yll;
                        yend = yhh;
                    } else {
                        if (ylh > yhl) {
                            double ystart2 = yhl;
                            yhl = ylh;
                            ylh = ystart2;
                        }
                        if (yll != ylh) {
                            int i4 = to;
                            int to3 = to + 1;
                            this.yranges[i4] = yll;
                            to = to3 + 1;
                            this.yranges[to3] = ylh;
                        }
                        ystart = yhl;
                        yend = yhh;
                    }
                    if (ystart >= yend) {
                        break;
                    }
                }
            }
            if (to < from && from < this.limit) {
                System.arraycopy(this.yranges, from, this.yranges, to, this.limit - from);
            }
            int to4 = to + (this.limit - from);
            if (ystart < yend) {
                if (to4 >= this.yranges.length) {
                    double[] newranges = new double[to4 + 10];
                    System.arraycopy(this.yranges, 0, newranges, 0, to4);
                    this.yranges = newranges;
                }
                int to5 = to4 + 1;
                this.yranges[to4] = ystart;
                to4 = to5 + 1;
                this.yranges[to5] = yend;
            }
            this.limit = to4;
        }
    }
}
