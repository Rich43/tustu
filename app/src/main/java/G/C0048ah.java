package G;

import java.io.Serializable;

/* renamed from: G.ah, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:G/ah.class */
public class C0048ah extends Q implements bH.Q, Serializable {

    /* renamed from: a, reason: collision with root package name */
    protected dh f746a = new B(0.0d);

    /* renamed from: b, reason: collision with root package name */
    protected dh f747b = new B(100.0d);

    /* renamed from: c, reason: collision with root package name */
    protected dh f748c = new B(-100000.0d);

    /* renamed from: d, reason: collision with root package name */
    protected dh f749d = new B(-100000.0d);

    /* renamed from: e, reason: collision with root package name */
    protected dh f750e = new B(85.0d);

    /* renamed from: f, reason: collision with root package name */
    protected dh f751f = new B(93.0d);

    /* renamed from: g, reason: collision with root package name */
    protected String f752g = null;

    /* renamed from: h, reason: collision with root package name */
    protected cZ f753h = new C0094c("Units");

    /* renamed from: i, reason: collision with root package name */
    protected cZ f754i = new C0094c("Gauge title");

    /* renamed from: j, reason: collision with root package name */
    protected String f755j = null;

    /* renamed from: k, reason: collision with root package name */
    protected dh f756k = new B(0.0d);

    /* renamed from: l, reason: collision with root package name */
    protected dh f757l = new B(0.0d);

    /* renamed from: m, reason: collision with root package name */
    protected int f758m = 5;

    /* renamed from: n, reason: collision with root package name */
    protected double f759n = -1.0d;

    /* renamed from: o, reason: collision with root package name */
    protected double f760o = -1.0d;

    /* renamed from: p, reason: collision with root package name */
    private boolean f761p = false;

    /* renamed from: q, reason: collision with root package name */
    private String f762q = "";

    /* renamed from: r, reason: collision with root package name */
    private String f763r = "";

    public double a() {
        return this.f746a.a();
    }

    public dh b() {
        return this.f746a;
    }

    public void a(dh dhVar) {
        this.f746a = dhVar;
    }

    public void a(double d2) {
        this.f746a = new B(d2);
    }

    public double d() {
        return this.f747b.a();
    }

    public dh e() {
        return this.f747b;
    }

    public void b(dh dhVar) {
        this.f747b = dhVar;
    }

    public void b(double d2) {
        this.f747b = new B(d2);
    }

    public dh f() {
        return this.f748c;
    }

    public void c(dh dhVar) {
        this.f748c = dhVar;
    }

    public void c(double d2) {
        this.f748c = new B(d2);
    }

    public dh g() {
        return this.f750e;
    }

    public void d(dh dhVar) {
        this.f750e = dhVar;
    }

    public void d(double d2) {
        this.f750e = new B(d2);
    }

    public dh h() {
        return this.f751f;
    }

    public void e(dh dhVar) {
        this.f751f = dhVar;
    }

    public void e(double d2) {
        this.f751f = new B(d2);
    }

    public String i() {
        return this.f752g;
    }

    public void a(String str) {
        this.f752g = str;
    }

    public cZ j() {
        return this.f753h;
    }

    public void b(String str) {
        this.f753h = new C0094c(str);
    }

    public void a(cZ cZVar) {
        this.f753h = cZVar;
    }

    public cZ k() {
        return this.f754i;
    }

    public void c(String str) {
        this.f754i = new C0094c(str);
    }

    public void b(cZ cZVar) {
        this.f754i = cZVar;
    }

    public int l() {
        return (int) this.f756k.a();
    }

    public dh m() {
        return this.f756k;
    }

    public void f(double d2) {
        this.f756k = new B(d2);
    }

    public void f(dh dhVar) {
        this.f756k = dhVar;
    }

    public int n() {
        return (int) this.f757l.a();
    }

    public void g(double d2) {
        this.f757l = new B(d2);
    }

    public void g(dh dhVar) {
        this.f757l = dhVar;
    }

    public dh o() {
        return this.f749d;
    }

    public void h(dh dhVar) {
        this.f749d = dhVar;
    }

    public void h(double d2) {
        this.f749d = new B(d2);
    }

    @Override // bH.Q
    public String c() {
        return (this.f755j == null ? "1" : this.f755j) + ((Object) k()) + " " + aJ();
    }

    public String p() {
        return this.f755j;
    }

    public void d(String str) {
        this.f755j = str;
    }

    public boolean q() {
        return this.f761p;
    }

    public void a(boolean z2) {
        this.f761p = z2;
    }

    public String r() {
        return this.f762q;
    }

    public String s() {
        return this.f763r;
    }
}
