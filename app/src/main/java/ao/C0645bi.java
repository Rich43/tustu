package ao;

import javax.swing.JPanel;

/* renamed from: ao.bi, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ao/bi.class */
public class C0645bi {

    /* renamed from: a, reason: collision with root package name */
    private static C0645bi f5403a = null;

    /* renamed from: b, reason: collision with root package name */
    private bP f5404b = null;

    /* renamed from: c, reason: collision with root package name */
    private C0625ap f5405c = null;

    /* renamed from: d, reason: collision with root package name */
    private C0823u f5406d = null;

    /* renamed from: e, reason: collision with root package name */
    private InterfaceC0642bf f5407e = null;

    /* renamed from: f, reason: collision with root package name */
    private aQ f5408f = null;

    /* renamed from: g, reason: collision with root package name */
    private C0659bw f5409g = null;

    /* renamed from: h, reason: collision with root package name */
    private JPanel f5410h = null;

    /* renamed from: i, reason: collision with root package name */
    private C0815m f5411i = null;

    /* renamed from: j, reason: collision with root package name */
    private C0759fp f5412j = null;

    private C0645bi() {
    }

    public static C0645bi a() {
        if (f5403a == null) {
            f5403a = new C0645bi();
        }
        return f5403a;
    }

    public bP b() {
        return this.f5404b;
    }

    public void a(bP bPVar) {
        this.f5404b = bPVar;
    }

    public C0625ap c() {
        return this.f5405c;
    }

    public void a(C0625ap c0625ap) {
        this.f5405c = c0625ap;
    }

    public C0823u d() {
        return this.f5406d;
    }

    public void a(C0823u c0823u) {
        this.f5406d = c0823u;
    }

    public aQ e() {
        return this.f5408f;
    }

    public void a(aQ aQVar) {
        this.f5408f = aQVar;
    }

    public C0659bw f() {
        return this.f5409g;
    }

    public void a(C0659bw c0659bw) {
        this.f5409g = c0659bw;
    }

    public C0815m g() {
        return this.f5411i;
    }

    public void a(C0815m c0815m) {
        this.f5411i = c0815m;
    }

    public JPanel h() {
        return this.f5410h;
    }

    public void a(JPanel jPanel) {
        this.f5410h = jPanel;
    }

    public InterfaceC0642bf i() {
        return this.f5407e;
    }

    public void a(InterfaceC0642bf interfaceC0642bf) {
        this.f5407e = interfaceC0642bf;
    }

    public C0759fp a(C0764fu c0764fu) {
        if (this.f5412j == null) {
            this.f5412j = new C0759fp(b(), c0764fu);
        } else if (!c0764fu.equals(this.f5412j.getParent())) {
            this.f5412j.a(c0764fu);
        }
        return this.f5412j;
    }

    public C0759fp j() {
        return this.f5412j;
    }
}
