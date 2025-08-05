package com.efiAnalytics.ui;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/bP.class */
class bP implements InterfaceC1648ef {

    /* renamed from: a, reason: collision with root package name */
    double f10962a;

    /* renamed from: b, reason: collision with root package name */
    double f10963b;

    /* renamed from: c, reason: collision with root package name */
    int f10964c;

    /* renamed from: d, reason: collision with root package name */
    int f10965d;

    /* renamed from: f, reason: collision with root package name */
    private double f10966f;

    /* renamed from: e, reason: collision with root package name */
    final /* synthetic */ bN f10967e;

    bP(bN bNVar) {
        this.f10967e = bNVar;
        this.f10962a = 0.0d;
        this.f10963b = 0.0d;
        this.f10964c = -1;
        this.f10965d = -1;
        this.f10966f = Double.NaN;
    }

    bP(bN bNVar, int i2, int i3) {
        this.f10967e = bNVar;
        this.f10962a = 0.0d;
        this.f10963b = 0.0d;
        this.f10964c = -1;
        this.f10965d = -1;
        this.f10966f = Double.NaN;
        this.f10964c = i3;
        this.f10965d = i2;
    }

    bP(bN bNVar, double d2, double d3, int i2, int i3) {
        this.f10967e = bNVar;
        this.f10962a = 0.0d;
        this.f10963b = 0.0d;
        this.f10964c = -1;
        this.f10965d = -1;
        this.f10966f = Double.NaN;
        a(d2);
        b(d3);
        this.f10964c = i3;
        this.f10965d = i2;
    }

    public double c() {
        return this.f10962a;
    }

    protected int d() {
        int width = (this.f10967e.getWidth() - this.f10967e.F().left) - this.f10967e.F().right;
        if (!this.f10967e.T()) {
            return this.f10967e.h() == this.f10967e.i() ? this.f10967e.F().left : this.f10967e.F().left + ((int) (((this.f10962a - this.f10967e.i()) / (this.f10967e.h() - this.f10967e.i())) * width));
        }
        int i2 = this.f10965d;
        if (i2 < 0) {
            i2 = 0;
        }
        bQ bQVarM = this.f10967e.m(i2);
        int i3 = this.f10964c < 0 ? 0 : this.f10964c;
        while (((bP) bQVarM.get(i3)).c() < this.f10962a && i3 < bQVarM.size() - 2) {
            i3++;
        }
        if (i3 >= bQVarM.size() - 1) {
            return this.f10967e.F().left + ((int) ((i3 / (bQVarM.size() - 1)) * width));
        }
        bP bPVar = (bP) bQVarM.get(i3);
        return this.f10967e.F().left + ((int) ((i3 / (bQVarM.size() - 1)) * width)) + ((int) (((this.f10962a - bPVar.c()) / (((bP) bQVarM.get(i3 + 1)).c() - bPVar.c())) * (width / (bQVarM.size() - 1))));
    }

    protected void b(int i2) {
        a(Math.rint((this.f10967e.i() + (((i2 - this.f10967e.F().left) / ((this.f10967e.getWidth() - this.f10967e.F().left) - this.f10967e.F().right)) * (this.f10967e.h() - this.f10967e.i()))) / this.f10967e.f10888G) * this.f10967e.f10888G);
    }

    public void a(double d2) {
        this.f10962a = Math.rint(d2 * 1.0E7d) / 1.0E7d;
    }

    public double e() {
        return this.f10963b;
    }

    protected int f() {
        return (this.f10967e.getHeight() - this.f10967e.F().bottom) - ((int) Math.round(((this.f10963b - this.f10967e.k()) / (this.f10967e.j() - this.f10967e.k())) * ((this.f10967e.getHeight() - this.f10967e.F().top) - this.f10967e.F().bottom)));
    }

    protected void c(int i2) {
        b(Math.rint((this.f10967e.k() + ((((this.f10967e.getHeight() - this.f10967e.F().bottom) - i2) / ((this.f10967e.getHeight() - this.f10967e.F().top) - this.f10967e.F().bottom)) * (this.f10967e.j() - this.f10967e.k()))) / this.f10967e.f10889H.a(this.f10964c)) * this.f10967e.f10889H.a(this.f10964c));
    }

    public void b(double d2) {
        this.f10963b = Math.rint(d2 * 1.0E7d) / 1.0E7d;
    }

    @Override // com.efiAnalytics.ui.InterfaceC1648ef
    public int b() {
        return this.f10964c;
    }

    @Override // com.efiAnalytics.ui.InterfaceC1648ef
    public int a() {
        return this.f10965d;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof bP)) {
            return super.equals(obj);
        }
        bP bPVar = (bP) obj;
        return bPVar.f10965d == this.f10965d && bPVar.f10964c == this.f10964c;
    }

    @Override // com.efiAnalytics.ui.InterfaceC1648ef
    public void a(int i2) {
        this.f10965d = i2;
    }

    public double g() {
        return this.f10966f;
    }

    public void c(double d2) {
        this.f10966f = d2;
    }
}
