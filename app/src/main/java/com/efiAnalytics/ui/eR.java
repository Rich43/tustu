package com.efiAnalytics.ui;

import java.util.ArrayList;
import java.util.Iterator;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/eR.class */
public class eR {

    /* renamed from: b, reason: collision with root package name */
    private double[][] f11534b = (double[][]) null;

    /* renamed from: c, reason: collision with root package name */
    private double[] f11535c = null;

    /* renamed from: d, reason: collision with root package name */
    private double[] f11536d = null;

    /* renamed from: e, reason: collision with root package name */
    private double[][] f11537e = (double[][]) null;

    /* renamed from: f, reason: collision with root package name */
    private String f11538f = null;

    /* renamed from: g, reason: collision with root package name */
    private String f11539g = null;

    /* renamed from: h, reason: collision with root package name */
    private String f11540h = null;

    /* renamed from: i, reason: collision with root package name */
    private double f11541i = 0.0d;

    /* renamed from: j, reason: collision with root package name */
    private double f11542j = 0.0d;

    /* renamed from: k, reason: collision with root package name */
    private double f11543k = 1.0d;

    /* renamed from: l, reason: collision with root package name */
    private double f11544l = Double.MAX_VALUE;

    /* renamed from: m, reason: collision with root package name */
    private double f11545m = Double.MIN_VALUE;

    /* renamed from: a, reason: collision with root package name */
    ArrayList f11546a = new ArrayList();

    public eR(int i2, int i3) {
        a(i2, i3);
    }

    public eR() {
        a(1, 1);
    }

    public int a() {
        if (this.f11536d == null) {
            return 0;
        }
        return this.f11536d.length;
    }

    public int b() {
        if (this.f11535c == null) {
            return 0;
        }
        return this.f11535c.length;
    }

    public void a(int i2, int i3) {
        this.f11535c = new double[i3];
        this.f11536d = new double[i2];
        this.f11534b = new double[i2][i3];
        this.f11541i = 0.0d;
        this.f11542j = 0.0d;
    }

    public void a(int i2, int i3, double d2) {
        if (d2 < this.f11545m) {
            d2 = this.f11545m;
        }
        if (d2 > this.f11544l) {
            d2 = this.f11544l;
        }
        if (s() > 0.0d) {
            double d3 = d2 - this.f11534b[i2][i3];
            double dS = d3 % s();
            if (dS != 0.0d) {
                d2 = (dS > (s() / 2.0d) ? 1 : (dS == (s() / 2.0d) ? 0 : -1)) > 0 ? ((this.f11534b[i2][i3] + d3) - dS) + s() : (dS > ((-s()) / 2.0d) ? 1 : (dS == ((-s()) / 2.0d) ? 0 : -1)) < 0 ? ((this.f11534b[i2][i3] + d3) - dS) - s() : (this.f11534b[i2][i3] + d3) - dS;
            }
        }
        if (this.f11534b[i2][i3] != d2) {
            this.f11534b[i2][i3] = d2;
            t();
            b(i2, i3, d2);
        }
    }

    public double b(int i2, int i3) {
        return this.f11537e[i2][i3];
    }

    public boolean c(int i2, int i3) {
        return this.f11537e == null || this.f11537e[i2][i3] == this.f11534b[i2][i3];
    }

    public void c() {
        this.f11537e = new double[this.f11534b.length][this.f11534b[0].length];
        for (int i2 = 0; i2 < this.f11534b.length; i2++) {
            for (int i3 = 0; i3 < this.f11534b[0].length; i3++) {
                this.f11537e[i2][i3] = this.f11534b[i2][i3];
            }
        }
    }

    private void t() {
        double d2 = Double.MAX_VALUE;
        double d3 = Double.MIN_VALUE;
        for (int i2 = 0; i2 < this.f11534b.length; i2++) {
            for (int i3 = 0; i3 < this.f11534b[0].length; i3++) {
                if (this.f11534b[i2][i3] < d2) {
                    d2 = this.f11534b[i2][i3];
                }
                if (this.f11534b[i2][i3] > d3) {
                    d3 = this.f11534b[i2][i3];
                }
            }
        }
        this.f11541i = d2;
        this.f11542j = d3;
    }

    public double d(int i2, int i3) {
        return this.f11534b[i2][i3];
    }

    public double a(double d2) {
        for (int i2 = 0; i2 < this.f11535c.length; i2++) {
            if (d2 < this.f11535c[i2]) {
                if (i2 == 0) {
                    return 0.0d;
                }
                if (i2 < this.f11535c.length - 1) {
                    double d3 = this.f11535c[i2];
                    double d4 = this.f11535c[i2 - 1];
                    return (i2 - 1) + ((d2 - d4) / (d3 - d4));
                }
            }
        }
        return this.f11535c.length - 1;
    }

    public double b(double d2) {
        for (int i2 = 0; i2 < this.f11536d.length; i2++) {
            if (d2 < this.f11536d[i2]) {
                if (i2 == 0) {
                    return 0.0d;
                }
                if (i2 < this.f11536d.length - 1) {
                    double d3 = this.f11536d[i2];
                    double d4 = this.f11536d[i2 - 1];
                    return (i2 - 1) + ((d2 - d4) / (d3 - d4));
                }
            }
        }
        return this.f11536d.length - 1;
    }

    public double a(int i2) {
        return this.f11535c[i2];
    }

    public double b(int i2) {
        return this.f11534b.length == this.f11536d.length / 2 ? this.f11536d[(i2 * 2) + 1] : this.f11536d[i2];
    }

    public void a(int i2, double d2) {
        if (this.f11535c[i2] != d2) {
            this.f11535c[i2] = d2;
            c(i2, d2);
        }
    }

    public void b(int i2, double d2) {
        if (this.f11536d[i2] != d2) {
            this.f11536d[i2] = d2;
            d(i2, d2);
        }
    }

    public void a(eS eSVar) {
        this.f11546a.add(eSVar);
    }

    private void b(int i2, int i3, double d2) {
        Iterator it = this.f11546a.iterator();
        while (it.hasNext()) {
            ((eS) it.next()).a(i2, i3, d2);
        }
    }

    private void c(int i2, double d2) {
        Iterator it = this.f11546a.iterator();
        while (it.hasNext()) {
            ((eS) it.next()).a(i2, d2);
        }
    }

    private void d(int i2, double d2) {
        Iterator it = this.f11546a.iterator();
        while (it.hasNext()) {
            ((eS) it.next()).b(i2, d2);
        }
    }

    public void a(double[][] dArr) {
        if ((dArr.length != this.f11536d.length && dArr.length != this.f11536d.length / 2) || dArr[0].length != this.f11535c.length) {
            throw new IndexOutOfBoundsException("Z Dimensions do not match X and Y dimensions");
        }
        this.f11534b = dArr;
        t();
    }

    public double[] d() {
        return this.f11535c;
    }

    public double[] e() {
        return this.f11536d;
    }

    public String f() {
        return this.f11538f;
    }

    public void a(String str) {
        this.f11538f = str;
    }

    public String g() {
        return this.f11539g;
    }

    public void b(String str) {
        this.f11539g = str;
    }

    public String h() {
        return this.f11540h;
    }

    public void c(String str) {
        this.f11540h = str;
    }

    public double i() {
        return this.f11535c[0];
    }

    public double j() {
        return this.f11535c[this.f11535c.length - 1];
    }

    public double k() {
        return this.f11536d[0];
    }

    public double l() {
        return this.f11536d[this.f11536d.length - 1];
    }

    public double m() {
        return this.f11541i;
    }

    public double n() {
        return this.f11542j;
    }

    public int o() {
        return this.f11535c.length;
    }

    public int p() {
        return this.f11534b.length;
    }

    public double q() {
        return this.f11544l;
    }

    public void c(double d2) {
        this.f11544l = d2;
    }

    public double r() {
        return this.f11545m;
    }

    public void d(double d2) {
        this.f11545m = d2;
    }

    public double s() {
        return this.f11543k;
    }

    public void e(double d2) {
        this.f11543k = d2;
    }

    private double a(double d2, double[] dArr) {
        if (dArr == null || d2 <= dArr[0]) {
            return 0.0d;
        }
        if (d2 >= dArr[dArr.length - 1]) {
            return dArr.length - 1;
        }
        for (int i2 = 0; i2 < dArr.length - 1; i2++) {
            if (d2 == dArr[i2]) {
                return i2;
            }
            if (d2 > dArr[i2] && d2 < dArr[i2 + 1]) {
                return i2 + ((d2 - dArr[i2]) / (dArr[i2 + 1] - dArr[i2]));
            }
        }
        return 0.0d;
    }

    public double a(double d2, double d3) {
        double dA = a(d2, this.f11536d);
        double dA2 = a(d3, this.f11535c);
        if (this.f11536d.length / 2 == this.f11534b.length) {
            dA /= 2.0d;
        }
        int i2 = (int) dA;
        int i3 = (int) dA2;
        int i4 = i2 < this.f11534b.length - 1 ? i2 + 1 : i2;
        int i5 = i3 < this.f11534b[0].length - 1 ? i3 + 1 : i3;
        double d4 = dA - i2;
        double d5 = dA2 - i3;
        return (this.f11534b[i2][i3] * (1.0d - d5) * (1.0d - d4)) + (this.f11534b[i2][i5] * d5 * (1.0d - d4)) + (this.f11534b[i4][i3] * (1.0d - d5) * d4) + (this.f11534b[i4][i5] * d5 * d4);
    }
}
