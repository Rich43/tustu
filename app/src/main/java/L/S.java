package L;

import java.io.FileNotFoundException;

/* loaded from: TunerStudioMS.jar:L/S.class */
public class S extends ax.ac {

    /* renamed from: a, reason: collision with root package name */
    ax.ab f1577a;

    /* renamed from: b, reason: collision with root package name */
    ax.ab f1578b;

    /* renamed from: c, reason: collision with root package name */
    String f1579c = null;

    public S(ax.ab abVar, ax.ab abVar2) {
        this.f1577a = null;
        this.f1578b = null;
        this.f1577a = abVar2;
        this.f1578b = abVar;
    }

    public double a(ax.S s2) throws ax.U {
        try {
            bH.E eA = V.a().a(Long.valueOf((long) this.f1577a.b(s2)));
            if (this.f1579c == null) {
                this.f1579c = eA.d() != null ? eA.d().getName() : null;
            }
            return eA.a(this.f1578b.b(s2));
        } catch (FileNotFoundException e2) {
            throw new ax.U("inc lookup failed: " + e2.getMessage());
        }
    }

    @Override // ax.ab
    public double b(ax.S s2) {
        return a(s2);
    }

    public String toString() {
        return "table( " + this.f1578b.toString() + ", \"" + this.f1579c + "\" )";
    }
}
