package L;

/* loaded from: TunerStudioMS.jar:L/Z.class */
public class Z extends ax.ac {

    /* renamed from: c, reason: collision with root package name */
    private ax.ab f1621c;

    /* renamed from: a, reason: collision with root package name */
    long f1622a = -1;

    /* renamed from: b, reason: collision with root package name */
    double f1623b = 0.0d;

    public Z(ax.ab abVar) {
        this.f1621c = abVar;
    }

    public double a(ax.S s2) {
        if (this.f1621c.b(s2) != 0.0d) {
            if (this.f1622a < 0) {
                this.f1622a = System.currentTimeMillis();
            }
            this.f1623b = (System.currentTimeMillis() - this.f1622a) / 1000.0d;
        } else if (this.f1622a > 0) {
            this.f1622a = -1L;
            this.f1623b = 0.0d;
        }
        return this.f1623b;
    }

    @Override // ax.ab
    public double b(ax.S s2) {
        return a(s2);
    }

    public String toString() {
        return "timeTrue( " + this.f1621c.toString() + " )";
    }
}
