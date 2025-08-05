package ax;

/* renamed from: ax.p, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ax/p.class */
class C0913p extends ac {

    /* renamed from: a, reason: collision with root package name */
    private ab f6411a;

    public C0913p(ab abVar) {
        this.f6411a = null;
        this.f6411a = abVar;
    }

    public double a(S s2) {
        return Math.ceil(this.f6411a.b(s2));
    }

    @Override // ax.ab
    public double b(S s2) {
        return a(s2);
    }

    public String toString() {
        return "ceil( " + this.f6411a.toString() + " )";
    }
}
