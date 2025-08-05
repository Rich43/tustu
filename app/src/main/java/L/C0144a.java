package L;

/* renamed from: L.a, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:L/a.class */
public class C0144a extends ax.ac {

    /* renamed from: a, reason: collision with root package name */
    ax.ab f1624a;

    /* renamed from: b, reason: collision with root package name */
    ax.ab f1625b;

    /* renamed from: c, reason: collision with root package name */
    ax.ab f1626c;

    /* renamed from: d, reason: collision with root package name */
    ax.ab f1627d;

    protected C0144a(ax.ab abVar, ax.ab abVar2, ax.ab abVar3, ax.ab abVar4) {
        this.f1625b = abVar;
        this.f1624a = abVar2;
        this.f1626c = abVar3;
        this.f1627d = abVar4;
    }

    public double a(ax.S s2) {
        double dB = ((((this.f1627d.b(s2) / 32.0d) * ((this.f1624a.b(s2) * 1.46667d) / this.f1626c.b(s2))) * this.f1625b.b(s2)) * 1.46667d) / 550.0d;
        if (Double.isNaN(dB)) {
            return 0.0d;
        }
        return dB;
    }

    @Override // ax.ab
    public double b(ax.S s2) {
        return a(s2);
    }

    public String toString() {
        return "accelHp( " + this.f1625b.toString() + " ," + this.f1624a.toString() + ", " + this.f1626c.toString() + ", " + this.f1627d.toString() + " )";
    }
}
