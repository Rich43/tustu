package L;

/* loaded from: TunerStudioMS.jar:L/M.class */
public class M extends ax.ac {

    /* renamed from: a, reason: collision with root package name */
    ax.ab f1559a;

    /* renamed from: b, reason: collision with root package name */
    ax.ab f1560b;

    /* renamed from: c, reason: collision with root package name */
    ax.ab f1561c;

    /* renamed from: d, reason: collision with root package name */
    double f1562d = Double.NaN;

    /* renamed from: e, reason: collision with root package name */
    double f1563e = Double.NaN;

    /* renamed from: f, reason: collision with root package name */
    double f1564f = Double.NaN;

    protected M(ax.ab abVar, ax.ab abVar2, ax.ab abVar3) {
        this.f1559a = abVar2;
        this.f1560b = abVar;
        this.f1561c = abVar3;
    }

    public double a(ax.S s2) {
        double dB = this.f1559a.b(s2);
        if (Double.isNaN(this.f1562d) || this.f1562d != dB) {
            this.f1562d = dB;
            a();
        }
        double dPow = (((this.f1563e + (this.f1564f * Math.pow(this.f1560b.b(s2), 2.5d))) * (this.f1561c.b(s2) / 1000.0d)) * this.f1560b.b(s2)) / 375.0d;
        if (Double.isNaN(dPow)) {
            return 0.0d;
        }
        return dPow;
    }

    private void a() {
        this.f1563e = (0.034667d * (this.f1562d - 35.0d) * (this.f1562d - 45.0d)) + ((-0.063333d) * (this.f1562d - 20.0d) * (this.f1562d - 45.0d)) + (0.032d * (this.f1562d - 20.0d) * (this.f1562d - 35.0d));
        this.f1564f = (7.704E-7d * (this.f1562d - 35.0d) * (this.f1562d - 45.0d)) + ((-8.33333E-7d) * (this.f1562d - 20.0d) * (this.f1562d - 45.0d)) + (3.148E-7d * (this.f1562d - 20.0d) * (this.f1562d - 35.0d));
    }

    @Override // ax.ab
    public double b(ax.S s2) {
        return a(s2);
    }

    public String toString() {
        return "rollingDragHp( " + this.f1560b.toString() + " ," + this.f1559a.toString() + ", " + this.f1561c.toString() + " )";
    }
}
