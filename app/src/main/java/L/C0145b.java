package L;

import G.dh;

/* renamed from: L.b, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:L/b.class */
public class C0145b extends ax.ac implements dh {

    /* renamed from: a, reason: collision with root package name */
    ax.ab f1642a;

    /* renamed from: b, reason: collision with root package name */
    ax.ab f1643b;

    /* renamed from: c, reason: collision with root package name */
    ax.ab f1644c;

    /* renamed from: d, reason: collision with root package name */
    double f1645d;

    /* renamed from: e, reason: collision with root package name */
    double f1646e;

    protected C0145b(ax.ab abVar, ax.ab abVar2) {
        this.f1644c = null;
        this.f1645d = Double.NaN;
        this.f1646e = Double.NaN;
        this.f1642a = abVar2;
        this.f1643b = abVar;
    }

    protected C0145b(ax.ab abVar, ax.ab abVar2, ax.ab abVar3) {
        this.f1644c = null;
        this.f1645d = Double.NaN;
        this.f1646e = Double.NaN;
        this.f1642a = abVar2;
        this.f1643b = abVar;
        this.f1644c = abVar3;
    }

    public double a(ax.S s2) {
        double dB = this.f1642a.b(s2);
        if (Double.isNaN(this.f1646e) || this.f1646e != dB) {
            if (!Double.isNaN(this.f1645d)) {
                this.f1645d += this.f1643b.b(s2);
            } else if (this.f1644c == null || this.f1644c.b(s2) == 0.0d) {
                this.f1645d = this.f1643b.b(s2);
            } else {
                this.f1645d = this.f1644c.b(s2);
            }
            this.f1646e = dB;
        }
        return this.f1645d;
    }

    @Override // ax.ab
    public double b(ax.S s2) {
        return a(s2);
    }

    public String toString() {
        return this.f1644c == null ? "accumulate( " + this.f1643b.toString() + " )" : "accumulate( " + this.f1643b.toString() + ", " + this.f1644c.toString() + " )";
    }

    @Override // G.dh
    public double a() {
        return a(0);
    }

    @Override // G.dh
    public double a(int i2) {
        return this.f1645d;
    }
}
