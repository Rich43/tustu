package G;

/* loaded from: TunerStudioMS.jar:G/cE.class */
public class cE implements InterfaceC0131n {

    /* renamed from: a, reason: collision with root package name */
    C0130m f1043a;

    /* renamed from: b, reason: collision with root package name */
    F f1044b;

    /* renamed from: d, reason: collision with root package name */
    private int f1042d = 0;

    /* renamed from: c, reason: collision with root package name */
    int f1045c = 1;

    /* renamed from: e, reason: collision with root package name */
    private int f1046e = 0;

    public cE(F f2, C0130m c0130m) {
        this.f1043a = c0130m;
        this.f1044b = f2;
        for (int i2 = 0; i2 < f2.g(); i2++) {
            this.f1046e += f2.f(i2);
        }
    }

    @Override // G.InterfaceC0131n
    public void e() {
    }

    @Override // G.InterfaceC0131n
    public void a(double d2) {
        double dF = 0.0d;
        for (int i2 = 0; i2 < this.f1042d; i2++) {
            dF += this.f1044b.f(i2);
        }
        this.f1043a.b((dF + (d2 * this.f1045c)) / this.f1046e);
    }

    @Override // G.InterfaceC0131n
    public void a(C0132o c0132o) {
    }

    public void a(int i2) {
        this.f1042d = i2;
        this.f1045c = this.f1044b.f(i2);
    }
}
