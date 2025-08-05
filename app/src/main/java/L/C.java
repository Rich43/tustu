package L;

/* loaded from: TunerStudioMS.jar:L/C.class */
public class C extends ax.ac {

    /* renamed from: a, reason: collision with root package name */
    ax.ab f1526a;

    /* renamed from: b, reason: collision with root package name */
    boolean f1527b = false;

    C(ax.ab abVar) {
        this.f1526a = abVar;
    }

    public double a(ax.S s2) {
        return Double.isNaN(this.f1526a.b(s2)) ? 1.0d : 0.0d;
    }

    @Override // ax.ab
    public double b(ax.S s2) {
        return a(s2);
    }

    public String toString() {
        return "isNaN( " + this.f1526a.toString() + " )";
    }
}
