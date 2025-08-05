package L;

/* loaded from: TunerStudioMS.jar:L/P.class */
public class P extends ax.ac {

    /* renamed from: a, reason: collision with root package name */
    private ax.ab f1573a;

    /* renamed from: b, reason: collision with root package name */
    private ax.ab f1574b;

    /* renamed from: c, reason: collision with root package name */
    private double f1575c = Double.NaN;

    public P(ax.ab abVar, ax.ab abVar2) {
        this.f1573a = abVar2;
        this.f1574b = abVar;
    }

    public double a(ax.S s2) {
        if (Double.isNaN(this.f1575c)) {
            this.f1575c = this.f1574b.b(s2);
        } else {
            this.f1575c = ((this.f1575c * (this.f1573a.b(s2) - 1.0d)) + this.f1574b.b(s2)) / this.f1573a.b(s2);
        }
        return this.f1575c;
    }

    @Override // ax.ab
    public double b(ax.S s2) {
        return a(s2);
    }

    public String toString() {
        return "smoothBasic( " + this.f1574b.toString() + ", " + this.f1573a.toString() + " )";
    }
}
