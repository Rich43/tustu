package ax;

/* renamed from: ax.r, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ax/r.class */
public class C0915r implements InterfaceC0914q {

    /* renamed from: a, reason: collision with root package name */
    private double f6412a;

    /* renamed from: b, reason: collision with root package name */
    private String f6413b;

    public C0915r(String str, double d2) {
        this.f6413b = str;
        this.f6412a = d2;
    }

    @Override // ax.ab
    public double b(S s2) {
        return this.f6412a;
    }

    @Override // ax.InterfaceC0888D
    public String a() {
        return this.f6413b;
    }

    public String toString() {
        return a();
    }
}
