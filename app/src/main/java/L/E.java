package L;

/* loaded from: TunerStudioMS.jar:L/E.class */
public class E extends ax.ac {

    /* renamed from: b, reason: collision with root package name */
    private ax.ab f1529b;

    /* renamed from: c, reason: collision with root package name */
    private ax.ab f1530c;

    /* renamed from: a, reason: collision with root package name */
    long f1531a = -1;

    public E(ax.ab abVar, ax.ab abVar2) {
        this.f1529b = abVar;
        this.f1530c = abVar2;
    }

    public double a(ax.S s2) {
        if (this.f1529b.b(s2) != 0.0d) {
            if (this.f1531a < 0) {
                this.f1531a = System.currentTimeMillis();
            }
            return ((double) (System.currentTimeMillis() - this.f1531a)) >= this.f1530c.b(s2) * 1000.0d ? 1.0d : 0.0d;
        }
        if (this.f1531a <= 0) {
            return 0.0d;
        }
        this.f1531a = -1L;
        return 0.0d;
    }

    @Override // ax.ab
    public double b(ax.S s2) {
        return a(s2);
    }

    public String toString() {
        return "isTrueFor( " + this.f1529b.toString() + ", " + this.f1530c.toString() + " )";
    }
}
