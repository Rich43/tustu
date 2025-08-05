package L;

import a.C0202b;

/* loaded from: TunerStudioMS.jar:L/O.class */
public class O extends ax.ac {

    /* renamed from: d, reason: collision with root package name */
    private ax.ab f1568d;

    /* renamed from: e, reason: collision with root package name */
    private ax.ab f1569e;

    /* renamed from: f, reason: collision with root package name */
    private double f1570f = Double.NaN;

    /* renamed from: a, reason: collision with root package name */
    double f1571a = Double.NaN;

    /* renamed from: b, reason: collision with root package name */
    double f1572b = Double.NaN;

    /* renamed from: c, reason: collision with root package name */
    private M.a f1567c = M.a.a(0.0d, 0.0d, 50.0d, Math.pow(3.0d, 2.0d) / 2.0d, Math.pow(5000.0d, 2.0d));

    public O(ax.ab abVar, ax.ab abVar2) {
        this.f1569e = abVar2;
        this.f1568d = abVar;
    }

    /* JADX WARN: Type inference failed for: r3v1, types: [double[], double[][]] */
    /* JADX WARN: Type inference failed for: r3v4, types: [double[], double[][]] */
    public double a(ax.S s2) {
        double dB = this.f1569e.b(s2);
        if (Double.isNaN(this.f1571a) || this.f1571a != dB) {
            if (Double.isNaN(this.f1570f)) {
                this.f1570f = this.f1568d.b(s2);
                this.f1567c.i(new C0202b(new double[]{new double[]{this.f1570f}, new double[]{0.0d}, new double[]{0.0d}, new double[]{0.0d}}));
            } else {
                this.f1567c.a();
                double dB2 = this.f1568d.b(s2);
                if (!Double.isNaN(dB2) && !Double.isNaN(this.f1570f) && !Double.isInfinite(dB2) && !Double.isInfinite(this.f1570f)) {
                    this.f1567c.a(new C0202b(new double[]{new double[]{dB2}, new double[]{this.f1570f}}));
                }
                this.f1570f = dB2;
            }
            this.f1572b = this.f1567c.b().a(0, 0);
            this.f1571a = dB;
        }
        return this.f1572b;
    }

    @Override // ax.ab
    public double b(ax.S s2) {
        return a(s2);
    }

    public String toString() {
        return "smoothFiltered( " + this.f1568d.toString() + " )";
    }
}
