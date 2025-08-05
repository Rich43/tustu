package ax;

/* renamed from: ax.M, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ax/M.class */
class C0897M extends ac {

    /* renamed from: a, reason: collision with root package name */
    private ab f6334a;

    public C0897M(ab abVar) {
        this.f6334a = null;
        this.f6334a = abVar;
    }

    public double a(S s2) {
        return Math.log(this.f6334a.b(s2));
    }

    @Override // ax.ab
    public double b(S s2) {
        return a(s2);
    }

    public String toString() {
        return "log( " + this.f6334a.toString() + " )";
    }
}
