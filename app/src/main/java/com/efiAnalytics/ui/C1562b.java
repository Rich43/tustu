package com.efiAnalytics.ui;

/* renamed from: com.efiAnalytics.ui.b, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/b.class */
public class C1562b {

    /* renamed from: p, reason: collision with root package name */
    private C1589c f10870p;

    /* renamed from: a, reason: collision with root package name */
    public static int f10859a = 1;

    /* renamed from: b, reason: collision with root package name */
    public static int f10860b = 2;

    /* renamed from: g, reason: collision with root package name */
    public static double f10868g = 5.0d;

    /* renamed from: m, reason: collision with root package name */
    private int f10861m = 0;

    /* renamed from: c, reason: collision with root package name */
    double f10862c = 0.0d;

    /* renamed from: n, reason: collision with root package name */
    private boolean f10863n = false;

    /* renamed from: o, reason: collision with root package name */
    private boolean f10864o = false;

    /* renamed from: d, reason: collision with root package name */
    double f10865d = 0.0d;

    /* renamed from: e, reason: collision with root package name */
    double f10866e = 0.0d;

    /* renamed from: f, reason: collision with root package name */
    int f10867f = 0;

    /* renamed from: h, reason: collision with root package name */
    double f10869h = 0.0d;

    /* renamed from: i, reason: collision with root package name */
    double f10871i = Double.NEGATIVE_INFINITY;

    /* renamed from: j, reason: collision with root package name */
    double f10872j = Double.POSITIVE_INFINITY;

    /* renamed from: k, reason: collision with root package name */
    double f10873k = 0.0d;

    /* renamed from: l, reason: collision with root package name */
    double f10874l = 0.0d;

    public C1562b() {
        this.f10870p = null;
        this.f10870p = new C1589c();
    }

    public C1562b(C1589c c1589c) {
        this.f10870p = null;
        this.f10870p = c1589c;
    }

    public void a(double d2) {
        a(d2, this.f10870p.h());
    }

    public void a(double d2, double d3) {
        this.f10862c = d2;
        this.f10869h = d2;
        this.f10861m = 0;
        this.f10870p.f(d3);
    }

    public void b(double d2) {
        a(d2);
        this.f10865d = this.f10870p.h();
        this.f10867f = 0;
        this.f10869h = Double.NaN;
        this.f10871i = Double.NEGATIVE_INFINITY;
        this.f10872j = Double.POSITIVE_INFINITY;
        this.f10873k = 0.0d;
        this.f10874l = 0.0d;
    }

    public double a() {
        return this.f10862c;
    }

    public String b() {
        return bH.W.a(this.f10862c);
    }

    public void a(Double d2, double d3) {
        if (this.f10870p == null) {
            this.f10870p = new C1589c();
        }
        if (d3 <= this.f10870p.a() || this.f10864o || Double.isNaN(d2.doubleValue())) {
            return;
        }
        double dI = this.f10865d >= this.f10870p.i() ? ((this.f10869h * this.f10870p.i()) + (d2.doubleValue() * d3)) / (this.f10870p.i() + d3) : Double.isNaN(this.f10869h) ? d2.doubleValue() : ((this.f10869h * (this.f10865d + this.f10870p.h())) + (d2.doubleValue() * d3)) / ((this.f10865d + this.f10870p.h()) + d3);
        if (Math.abs(dI - this.f10862c) > this.f10870p.d()) {
            this.f10861m &= f10860b;
            return;
        }
        if (!Double.isNaN(this.f10870p.e()) && Math.abs(dI - this.f10862c) / this.f10862c > this.f10870p.e()) {
            this.f10861m &= f10859a;
            return;
        }
        if (d2.doubleValue() > this.f10871i) {
            this.f10871i = d2.doubleValue();
        }
        if (d2.doubleValue() < this.f10872j) {
            this.f10872j = d2.doubleValue();
        }
        this.f10873k += d2.doubleValue() * d3;
        this.f10869h = dI;
        this.f10865d += d3;
        this.f10867f++;
        double dE = e();
        this.f10874l += (dE - d2.doubleValue()) * (dE - d2.doubleValue()) * d3;
    }

    public double c() {
        return Math.sqrt(d());
    }

    public double d() {
        return this.f10874l / this.f10865d;
    }

    public double e() {
        if (i() == null) {
            return 0.0d;
        }
        return i().doubleValue();
    }

    public Double f() {
        if (Double.isInfinite(this.f10871i)) {
            return null;
        }
        return Double.valueOf(this.f10871i);
    }

    public Double g() {
        if (Double.isInfinite(this.f10872j)) {
            return null;
        }
        return Double.valueOf(this.f10872j);
    }

    public void c(double d2) {
        this.f10866e = d2;
    }

    public double h() {
        return this.f10866e;
    }

    public Double i() {
        if (this.f10870p.c() > Math.abs(this.f10869h - this.f10862c)) {
            return Double.valueOf(this.f10862c);
        }
        if (Double.isNaN(this.f10869h) || this.f10870p.b() > this.f10865d) {
            return null;
        }
        return this.f10863n ? new Double(Math.round(this.f10869h)) : Double.valueOf(this.f10869h);
    }

    public int j() {
        return this.f10867f;
    }

    public double k() {
        return this.f10865d;
    }

    public String l() {
        return bH.W.a(k());
    }

    public String toString() {
        return i().intValue() + "";
    }

    public void a(C1589c c1589c) {
        this.f10870p = c1589c;
    }

    public boolean m() {
        return this.f10864o;
    }

    public void a(boolean z2) {
        this.f10864o = z2;
    }

    public void n() {
        this.f10867f = 0;
        this.f10865d = 0.0d;
    }
}
