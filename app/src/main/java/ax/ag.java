package ax;

/* loaded from: TunerStudioMS.jar:ax/ag.class */
public class ag extends ac {

    /* renamed from: a, reason: collision with root package name */
    private ab f6382a;

    public ag(ab abVar) {
        this.f6382a = null;
        this.f6382a = abVar;
    }

    public double a(S s2) {
        return Math.round(this.f6382a.b(s2));
    }

    @Override // ax.ab
    public double b(S s2) {
        return a(s2);
    }

    public String toString() {
        return "round( " + this.f6382a.toString() + " )";
    }
}
