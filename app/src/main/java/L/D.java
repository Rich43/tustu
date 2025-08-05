package L;

import G.aI;

/* loaded from: TunerStudioMS.jar:L/D.class */
public class D extends ax.ac {

    /* renamed from: a, reason: collision with root package name */
    aI f1528a;

    public D(aI aIVar) {
        this.f1528a = aIVar;
    }

    public double a(ax.S s2) {
        return this.f1528a.R() ? 1.0d : 0.0d;
    }

    @Override // ax.ab
    public double b(ax.S s2) {
        return a(s2);
    }

    public String toString() {
        return "isOnline()";
    }
}
