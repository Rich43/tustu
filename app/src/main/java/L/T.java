package L;

import G.aI;
import G.aM;

/* loaded from: TunerStudioMS.jar:L/T.class */
public class T extends ax.ac {

    /* renamed from: a, reason: collision with root package name */
    aI f1580a;

    /* renamed from: b, reason: collision with root package name */
    ax.ab f1581b;

    /* renamed from: c, reason: collision with root package name */
    ax.ab f1582c;

    /* renamed from: d, reason: collision with root package name */
    ax.ab f1583d;

    /* renamed from: e, reason: collision with root package name */
    ax.ab f1584e;

    /* renamed from: f, reason: collision with root package name */
    ax.ab f1585f;

    /* renamed from: g, reason: collision with root package name */
    aM f1586g = null;

    /* renamed from: h, reason: collision with root package name */
    aM f1587h = null;

    /* renamed from: i, reason: collision with root package name */
    aM f1588i = null;

    /* renamed from: j, reason: collision with root package name */
    double[] f1589j = null;

    /* renamed from: k, reason: collision with root package name */
    double[] f1590k = null;

    public T(aI aIVar, ax.ab abVar, ax.ab abVar2, ax.ab abVar3, ax.ab abVar4, ax.ab abVar5) {
        this.f1580a = aIVar;
        this.f1581b = abVar;
        this.f1582c = abVar2;
        this.f1583d = abVar3;
        this.f1584e = abVar4;
        this.f1585f = abVar5;
    }

    public double a(ax.S s2) throws ax.U {
        c(s2);
        try {
            double[] dArrA = a();
            double[] dArrB = b();
            double dB = this.f1584e.b(s2);
            return a(a(dArrB, this.f1585f.b(s2)), b(dArrA, dB));
        } catch (V.g e2) {
            throw new ax.U("Unable to evaluate tableLookup: " + e2.getLocalizedMessage());
        }
    }

    @Override // ax.ab
    public double b(ax.S s2) {
        return a(s2);
    }

    private void c(ax.S s2) throws ax.U {
        if (this.f1586g == null) {
            this.f1586g = C0155l.a().a((int) this.f1581b.b(s2));
            if (this.f1586g == null) {
                throw new ax.U("Z Parameter not found!");
            }
        }
        if (this.f1587h == null) {
            this.f1587h = C0155l.a().a((int) this.f1582c.b(s2));
            if (this.f1587h == null) {
                throw new ax.U("X Parameter not found!");
            }
        }
        if (this.f1588i == null) {
            this.f1588i = C0155l.a().a((int) this.f1583d.b(s2));
            if (this.f1588i == null) {
                throw new ax.U("Y Parameter not found!");
            }
        }
    }

    public double a(double d2, double d3) throws V.g {
        int i2 = (int) d2;
        int i3 = (int) d3;
        double[][] dArrI = this.f1586g.i(this.f1580a.h());
        int i4 = i2 < dArrI.length - 1 ? i2 + 1 : i2;
        int i5 = i3 < dArrI[0].length - 1 ? i3 + 1 : i3;
        double d4 = d2 - i2;
        double d5 = d3 - i3;
        return (dArrI[i2][i3] * (1.0d - d5) * (1.0d - d4)) + (dArrI[i2][i5] * d5 * (1.0d - d4)) + (dArrI[i4][i3] * (1.0d - d5) * d4) + (dArrI[i4][i5] * d5 * d4);
    }

    public static double a(double[] dArr, double d2) {
        double d3 = 10.0d;
        try {
            d3 = dArr[dArr.length - 1];
        } catch (Exception e2) {
            System.out.println("axisValues=" + ((Object) dArr));
            System.out.println("Exception in getYaxisPosition, axisValues[axisValues.length-1]=" + dArr[dArr.length - 1] + ", axisValues.length=" + dArr.length);
        }
        double d4 = 0.0d;
        int length = dArr.length - 1;
        while (true) {
            if (length < 0) {
                break;
            }
            double d5 = dArr[length];
            if (d5 == d2) {
                d4 = length;
                break;
            }
            if (d5 < d2) {
                d4 = length == dArr.length - 1 ? length : length + ((d5 - d2) / (d5 - d3));
            } else {
                if (length == 0) {
                    return length;
                }
                d3 = d5;
                length--;
            }
        }
        return d4;
    }

    public static double b(double[] dArr, double d2) {
        double d3 = 0.0d;
        double length = dArr.length - 1;
        int i2 = 0;
        while (true) {
            if (i2 >= dArr.length) {
                break;
            }
            double d4 = dArr[i2];
            if (d4 == d2) {
                length = i2;
                break;
            }
            if (d4 > d2) {
                length = (i2 != 0 || d2 > d4) ? (i2 - 1.0d) + ((d2 - d3) / (d4 - d3)) : 0.0d;
            } else {
                d3 = d4;
                i2++;
            }
        }
        return length;
    }

    private double[] a() throws V.g {
        double[][] dArrI = this.f1587h.i(this.f1580a.h());
        if (this.f1589j == null) {
            this.f1589j = new double[dArrI.length];
        }
        for (int i2 = 0; i2 < this.f1589j.length; i2++) {
            this.f1589j[i2] = dArrI[i2][0];
        }
        return this.f1589j;
    }

    private double[] b() throws V.g {
        double[][] dArrI = this.f1588i.i(this.f1580a.h());
        if (this.f1590k == null) {
            this.f1590k = new double[this.f1586g.a()];
        }
        if (dArrI.length == this.f1590k.length * 2) {
            for (int i2 = 0; i2 < this.f1589j.length; i2++) {
                if (i2 % 2 == 0) {
                    this.f1590k[i2 / 2] = dArrI[i2][0];
                }
            }
        } else {
            for (int i3 = 0; i3 < this.f1589j.length; i3++) {
                this.f1590k[i3] = dArrI[i3][0];
            }
        }
        return this.f1590k;
    }

    public String toString() {
        return "tableLookup(" + this.f1581b.toString() + ", " + this.f1582c.toString() + ", " + this.f1583d.toString() + ", " + this.f1584e.toString() + ", " + this.f1585f.toString() + " )";
    }
}
