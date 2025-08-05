package ax;

/* renamed from: ax.v, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ax/v.class */
class C0919v extends ac {

    /* renamed from: a, reason: collision with root package name */
    private ab f6418a;

    public C0919v(ab abVar) {
        this.f6418a = null;
        this.f6418a = abVar;
    }

    public double a(S s2) {
        return Math.exp(this.f6418a.b(s2));
    }

    @Override // ax.ab
    public double b(S s2) {
        return a(s2);
    }

    public String toString() {
        return "exp(" + this.f6418a.toString() + ")";
    }
}
