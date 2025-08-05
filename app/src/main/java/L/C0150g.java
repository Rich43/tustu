package L;

/* renamed from: L.g, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:L/g.class */
public class C0150g extends ax.ac {

    /* renamed from: a, reason: collision with root package name */
    String f1657a;

    /* renamed from: b, reason: collision with root package name */
    boolean f1658b = false;

    C0150g(String str) {
        this.f1657a = "";
        this.f1657a = str;
    }

    public double a(ax.S s2) {
        if (this.f1658b) {
            return Double.NaN;
        }
        bH.C.d(this.f1657a + " not supported in this edition.");
        this.f1658b = true;
        return Double.NaN;
    }

    @Override // ax.ab
    public double b(ax.S s2) {
        return a(s2);
    }
}
