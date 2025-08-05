package M;

import a.C0202b;

/* loaded from: TunerStudioMS.jar:M/a.class */
public class a {

    /* renamed from: a, reason: collision with root package name */
    protected C0202b f1728a;

    /* renamed from: b, reason: collision with root package name */
    protected C0202b f1729b;

    /* renamed from: c, reason: collision with root package name */
    protected C0202b f1730c;

    /* renamed from: d, reason: collision with root package name */
    protected C0202b f1731d;

    /* renamed from: e, reason: collision with root package name */
    protected C0202b f1732e;

    /* renamed from: f, reason: collision with root package name */
    protected C0202b f1733f;

    /* renamed from: g, reason: collision with root package name */
    protected C0202b f1734g;

    /* renamed from: h, reason: collision with root package name */
    protected C0202b f1735h;

    /* renamed from: i, reason: collision with root package name */
    protected C0202b f1736i;

    /* renamed from: j, reason: collision with root package name */
    protected C0202b f1737j;

    /* JADX WARN: Type inference failed for: r3v1, types: [double[], double[][]] */
    /* JADX WARN: Type inference failed for: r3v12, types: [double[], double[][]] */
    /* JADX WARN: Type inference failed for: r3v3, types: [double[], double[][]] */
    /* JADX WARN: Type inference failed for: r3v5, types: [double[], double[][]] */
    /* JADX WARN: Type inference failed for: r3v7, types: [double[], double[][]] */
    /* JADX WARN: Type inference failed for: r3v9, types: [double[], double[][]] */
    public static a a(double d2, double d3, double d4, double d5, double d6) {
        a aVar = new a();
        aVar.i(new C0202b(new double[]{new double[]{0.0d, 0.0d, 0.0d, 0.0d}}).f());
        aVar.e(C0202b.b(4, 4).a(0.0d));
        aVar.c(new C0202b(new double[]{new double[]{1.0d, 0.0d, d4, 0.0d}, new double[]{0.0d, 1.0d, 0.0d, d4}, new double[]{0.0d, 0.0d, 1.0d, 0.0d}, new double[]{0.0d, 0.0d, 0.0d, 1.0d}}));
        aVar.b(new C0202b(new double[]{new double[]{0.0d, 0.0d, 0.0d, 0.0d}}).f());
        aVar.h(new C0202b(new double[]{new double[]{0.0d}}));
        aVar.f(new C0202b(new double[]{new double[]{0.0d, 0.0d, 0.0d, 0.0d}, new double[]{0.0d, 0.0d, 0.0d, 0.0d}, new double[]{0.0d, 0.0d, 1.0d, 0.0d}, new double[]{0.0d, 0.0d, 0.0d, 1.0d}}).a(Math.pow(d5, 2.0d)));
        aVar.d(new C0202b(new double[]{new double[]{1.0d, 0.0d, 0.0d, 0.0d}, new double[]{0.0d, 1.0d, 0.0d, 0.0d}}));
        aVar.g(C0202b.b(2, 2).a(d6));
        return aVar;
    }

    public void a() {
        this.f1729b = this.f1730c.c(this.f1728a).a(this.f1731d.c(this.f1732e));
        this.f1737j = this.f1730c.c(this.f1736i).c(this.f1730c.f()).a(this.f1733f);
    }

    public void a(C0202b c0202b) {
        C0202b c0202bC = this.f1737j.c(this.f1734g.f()).c(this.f1734g.c(this.f1737j).c(this.f1734g.f()).a(this.f1735h).g());
        this.f1728a = this.f1729b.a(c0202bC.c(c0202b.b(this.f1734g.c(this.f1729b))));
        this.f1736i = C0202b.b(this.f1737j.d(), this.f1737j.e()).b(c0202bC.c(this.f1734g)).c(this.f1737j);
    }

    public void b(C0202b c0202b) {
        this.f1731d = c0202b;
    }

    public void c(C0202b c0202b) {
        this.f1730c = c0202b;
    }

    public void d(C0202b c0202b) {
        this.f1734g = c0202b;
    }

    public void e(C0202b c0202b) {
        this.f1736i = c0202b;
    }

    public void f(C0202b c0202b) {
        this.f1733f = c0202b;
    }

    public void g(C0202b c0202b) {
        this.f1735h = c0202b;
    }

    public void h(C0202b c0202b) {
        this.f1732e = c0202b;
    }

    public C0202b b() {
        return this.f1728a;
    }

    public void i(C0202b c0202b) {
        this.f1728a = c0202b;
    }
}
