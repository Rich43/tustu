package L;

/* loaded from: TunerStudioMS.jar:L/aa.class */
public class aa extends ax.ac {

    /* renamed from: a, reason: collision with root package name */
    ax.ab f1628a;

    /* renamed from: b, reason: collision with root package name */
    ax.ab f1629b;

    /* renamed from: c, reason: collision with root package name */
    boolean f1630c;

    /* renamed from: d, reason: collision with root package name */
    boolean f1631d;

    /* renamed from: e, reason: collision with root package name */
    double f1632e;

    protected aa(ax.ab abVar, ax.ab abVar2) {
        this.f1629b = null;
        this.f1630c = false;
        this.f1631d = false;
        this.f1632e = 0.0d;
        this.f1628a = abVar;
        this.f1629b = abVar2;
    }

    protected aa(ax.ab abVar) {
        this.f1629b = null;
        this.f1630c = false;
        this.f1631d = false;
        this.f1632e = 0.0d;
        this.f1628a = abVar;
    }

    public double a(ax.S s2) {
        if (!this.f1631d) {
            if (this.f1629b != null) {
                this.f1630c = this.f1629b.b(s2) != 0.0d;
            } else {
                this.f1630c = false;
            }
            this.f1632e = this.f1628a.b(s2);
            this.f1631d = true;
        }
        double dB = this.f1628a.b(s2);
        if (this.f1632e == 0.0d && dB != 0.0d) {
            this.f1630c = !this.f1630c;
        }
        this.f1632e = dB;
        return this.f1630c ? 1.0d : 0.0d;
    }

    @Override // ax.ab
    public double b(ax.S s2) {
        return a(s2);
    }

    public String toString() {
        return this.f1629b != null ? "toggle( " + this.f1628a.toString() + ", " + this.f1629b.toString() + " )" : "toggle( " + this.f1628a.toString() + " )";
    }
}
