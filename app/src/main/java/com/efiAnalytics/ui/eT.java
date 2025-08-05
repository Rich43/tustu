package com.efiAnalytics.ui;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/eT.class */
public class eT {

    /* renamed from: a, reason: collision with root package name */
    final double f11547a = 3.141592653589793d;

    /* renamed from: b, reason: collision with root package name */
    final double f11548b = 1.5707963267948966d;

    /* renamed from: j, reason: collision with root package name */
    private eR f11549j = null;

    /* renamed from: k, reason: collision with root package name */
    private eM f11550k = null;

    /* renamed from: l, reason: collision with root package name */
    private double f11551l = 0.39269908169872414d;

    /* renamed from: m, reason: collision with root package name */
    private double f11552m = 5.890486225480862d;

    /* renamed from: c, reason: collision with root package name */
    int f11553c = 0;

    /* renamed from: d, reason: collision with root package name */
    int f11554d = 0;

    /* renamed from: e, reason: collision with root package name */
    double f11555e = 0.7d;

    /* renamed from: n, reason: collision with root package name */
    private double f11556n = 0.45d;

    /* renamed from: f, reason: collision with root package name */
    eZ f11557f = null;

    /* renamed from: g, reason: collision with root package name */
    eZ[][] f11558g = (eZ[][]) null;

    /* renamed from: o, reason: collision with root package name */
    private boolean f11559o = false;

    /* renamed from: h, reason: collision with root package name */
    final double f11560h = 1.5707963267948966d;

    /* renamed from: i, reason: collision with root package name */
    eZ f11561i = null;

    public eT(eM eMVar) {
        a(eMVar);
    }

    public eZ a(double d2, double d3, double d4) {
        return a(d2, d3, d4, null);
    }

    public eZ a() {
        if (this.f11550k.getWidth() < this.f11550k.getHeight() / 0.71d) {
            this.f11555e = 0.71d;
        } else {
            this.f11555e = this.f11550k.getHeight() / this.f11550k.getWidth();
        }
        this.f11555e *= 0.95d;
        double width = this.f11550k.getWidth() * this.f11555e;
        double width2 = this.f11550k.getWidth() * this.f11555e;
        double height = ((this.f11550k.getHeight() * f()) + ((0.5d * this.f11550k.getHeight()) / 4.0d)) - ((f() * this.f11550k.getHeight()) / 4.0d);
        int width3 = ((int) (this.f11550k.getWidth() / 2.0d)) - ((int) ((((this.f11550k.getWidth() * this.f11555e) * 1.41d) / 2.0d) * Math.cos(d() + 0.7853981633974483d)));
        int height2 = ((int) ((this.f11550k.getHeight() + (((width * Math.sin(d())) + (width2 * Math.cos(d()))) * Math.sin(e() - 3.141592653589793d))) + (height * Math.cos(e() + 3.141592653589793d)))) / 2;
        this.f11561i = new eZ();
        this.f11561i.f11567a = width3;
        this.f11561i.f11568b = height2;
        return this.f11561i;
    }

    public eZ a(double d2, double d3, double d4, eZ eZVar) {
        if (eZVar == null) {
            eZVar = new eZ();
        }
        double width = this.f11550k.getWidth() * this.f11555e * e(d2);
        double width2 = this.f11550k.getWidth() * this.f11555e * f(d3);
        double height = this.f11550k.getHeight() * f() * a(d4);
        this.f11557f = a();
        eZVar.f11567a = (int) Math.round(this.f11557f.f11567a + (width * Math.cos(d())) + (width2 * Math.cos(d() + 1.5707963267948966d)));
        eZVar.f11568b = (int) Math.round(this.f11557f.f11568b + (width2 * Math.cos(d()) * Math.sin(e())) + (width * Math.sin(d()) * Math.sin(e())) + (height * Math.sin(e() + 1.5707963267948966d)));
        return eZVar;
    }

    public double a(double d2, double d3, int i2) {
        double width = this.f11550k.getWidth() * this.f11555e * e(d2);
        double width2 = this.f11550k.getWidth() * this.f11555e * f(d3);
        double height = this.f11550k.getHeight() * f();
        this.f11557f = a();
        int iRound = (int) Math.round(this.f11557f.f11568b + (width2 * Math.cos(d()) * Math.sin(e())) + (width * Math.sin(d()) * Math.sin(e())) + (0.0d * Math.sin(e() + 1.5707963267948966d)));
        return this.f11549j.r() + ((this.f11549j.n() - this.f11549j.r()) * ((i2 - iRound) / (((int) Math.round(((this.f11557f.f11568b + ((width2 * Math.cos(d())) * Math.sin(e()))) + ((width * Math.sin(d())) * Math.sin(e()))) + (height * Math.sin(e() + 1.5707963267948966d)))) - iRound)));
    }

    public eZ[][] b() {
        long jNanoTime = System.nanoTime();
        this.f11558g = new eZ[this.f11549j.p()][this.f11549j.o()];
        for (int i2 = 0; i2 < this.f11549j.p(); i2++) {
            for (int i3 = 0; i3 < this.f11549j.o(); i3++) {
                this.f11558g[i2][i3] = a(this.f11549j.a(i3), this.f11549j.b(i2), this.f11549j.d(i2, i3), this.f11558g[i2][i3]);
            }
        }
        double dNanoTime = (System.nanoTime() - jNanoTime) / 1000000.0d;
        return this.f11558g;
    }

    private double e(double d2) {
        double[] dArrD = this.f11549j.d();
        if (!this.f11559o) {
            if (d2 >= dArrD[dArrD.length - 1]) {
                return 1.0d;
            }
            return (d2 - dArrD[0]) / (dArrD[dArrD.length - 1] - dArrD[0]);
        }
        for (int i2 = 1; i2 < dArrD.length; i2++) {
            if (dArrD[i2] >= d2) {
                return ((i2 - 1) / dArrD.length) + ((d2 - dArrD[i2 - 1]) / ((dArrD[i2] - dArrD[i2 - 1]) * dArrD.length));
            }
        }
        return dArrD[dArrD.length - 1];
    }

    private double f(double d2) {
        double[] dArrE = this.f11549j.e();
        if (!this.f11559o) {
            if (d2 >= dArrE[dArrE.length - 1]) {
                return 1.0d;
            }
            return (d2 - dArrE[0]) / (dArrE[dArrE.length - 1] - dArrE[0]);
        }
        for (int i2 = 1; i2 < dArrE.length; i2++) {
            if (dArrE[i2] >= d2) {
                return ((i2 - 1) / dArrE.length) + ((d2 - dArrE[i2 - 1]) / ((dArrE[i2] - dArrE[i2 - 1]) * dArrE.length));
            }
        }
        return dArrE[dArrE.length - 1];
    }

    public double a(double d2) {
        return (d2 - this.f11549j.r()) / (this.f11549j.n() - this.f11549j.r());
    }

    public void a(eR eRVar) {
        this.f11549j = eRVar;
    }

    public void a(eM eMVar) {
        this.f11550k = eMVar;
    }

    public void c() {
        this.f11561i = a();
    }

    public void a(boolean z2) {
        this.f11559o = z2;
    }

    public double d() {
        return this.f11551l;
    }

    public void b(double d2) {
        this.f11551l = d2;
    }

    public double e() {
        return this.f11552m;
    }

    public void c(double d2) {
        this.f11552m = d2;
    }

    public double f() {
        return this.f11556n;
    }

    public void d(double d2) {
        this.f11556n = d2;
    }
}
