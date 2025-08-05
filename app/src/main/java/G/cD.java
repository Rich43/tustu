package G;

/* loaded from: TunerStudioMS.jar:G/cD.class */
public class cD extends C0130m implements InterfaceC0131n {

    /* renamed from: a, reason: collision with root package name */
    int f1038a;

    /* renamed from: c, reason: collision with root package name */
    private int f1039c;

    /* renamed from: d, reason: collision with root package name */
    private int f1040d;

    /* renamed from: b, reason: collision with root package name */
    C0130m f1041b;

    public cD(F f2, C0130m c0130m, int i2) {
        super(f2);
        this.f1040d = 0;
        this.f1038a = i2;
        this.f1041b = c0130m;
        b(this);
    }

    @Override // G.InterfaceC0131n
    public void e() {
    }

    @Override // G.InterfaceC0131n
    public void a(double d2) {
        if (this.f1041b != null) {
            this.f1041b.b(((d2 * this.f1039c) + this.f1040d) / this.f1038a);
        }
    }

    @Override // G.InterfaceC0131n
    public void a(C0132o c0132o) {
    }

    public void j(int i2) {
        this.f1039c = i2;
    }

    public void k(int i2) {
        this.f1040d = i2;
    }
}
