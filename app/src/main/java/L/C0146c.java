package L;

/* renamed from: L.c, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:L/c.class */
public class C0146c extends ax.ac {

    /* renamed from: a, reason: collision with root package name */
    ax.ab f1647a;

    /* renamed from: b, reason: collision with root package name */
    ax.ab f1648b;

    /* renamed from: c, reason: collision with root package name */
    ax.ab f1649c;

    /* renamed from: d, reason: collision with root package name */
    ax.ab f1650d;

    protected C0146c(ax.ab abVar, ax.ab abVar2, ax.ab abVar3, ax.ab abVar4) {
        this.f1648b = abVar;
        this.f1647a = abVar2;
        this.f1649c = abVar3;
        this.f1650d = abVar4;
    }

    public double a(ax.S s2) {
        return ((((this.f1647a.b(s2) * Math.pow(this.f1648b.b(s2), 3.0d)) * this.f1649c.b(s2)) * this.f1650d.b(s2)) / 2.0d) / 745.7d;
    }

    @Override // ax.ab
    public double b(ax.S s2) {
        return a(s2);
    }

    public String toString() {
        return "aerodynamicDragHp( " + this.f1648b.toString() + " ," + this.f1647a.toString() + ", " + this.f1649c.toString() + ", " + this.f1650d.toString() + " )";
    }
}
