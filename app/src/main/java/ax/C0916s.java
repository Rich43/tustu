package ax;

/* renamed from: ax.s, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ax/s.class */
public class C0916s implements am {

    /* renamed from: a, reason: collision with root package name */
    String f6414a;

    /* renamed from: b, reason: collision with root package name */
    double f6415b = Double.NaN;

    public C0916s(String str) {
        this.f6414a = null;
        this.f6414a = str;
    }

    @Override // ax.am
    public boolean b() {
        return this.f6415b != Double.NaN;
    }

    @Override // ax.am
    public void a(double d2) {
        this.f6415b = d2;
    }

    @Override // ax.ab
    public double b(S s2) {
        double d2 = 0.0d;
        if (this.f6415b != Double.NaN) {
            d2 = this.f6415b;
        }
        return d2;
    }

    @Override // ax.InterfaceC0888D
    public String a() {
        return this.f6414a;
    }

    public String toString() {
        return a();
    }
}
