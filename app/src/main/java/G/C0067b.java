package G;

/* renamed from: G.b, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:G/b.class */
public class C0067b implements InterfaceC0131n {

    /* renamed from: a, reason: collision with root package name */
    private double f822a = 0.0d;

    /* renamed from: b, reason: collision with root package name */
    private C0132o f823b = null;

    @Override // G.InterfaceC0131n
    public void e() {
    }

    @Override // G.InterfaceC0131n
    public void a(double d2) {
        this.f822a = d2;
    }

    @Override // G.InterfaceC0131n
    public void a(C0132o c0132o) {
        this.f823b = c0132o;
    }

    public C0132o a() {
        return this.f823b;
    }

    public String b() {
        if (this.f823b != null) {
            return this.f823b.c();
        }
        return null;
    }
}
