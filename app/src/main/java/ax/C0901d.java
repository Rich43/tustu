package ax;

/* renamed from: ax.d, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ax/d.class */
class C0901d extends ac {

    /* renamed from: a, reason: collision with root package name */
    private ab f6394a;

    public C0901d(ab abVar) {
        this.f6394a = null;
        this.f6394a = abVar;
    }

    public double a(S s2) {
        return Math.atan(this.f6394a.b(s2));
    }

    @Override // ax.ab
    public double b(S s2) {
        return a(s2);
    }

    public String toString() {
        return "atan( " + this.f6394a.toString() + " )";
    }
}
