package ax;

/* loaded from: TunerStudioMS.jar:ax/P.class */
class P extends ac {

    /* renamed from: a, reason: collision with root package name */
    private ab f6338a;

    /* renamed from: b, reason: collision with root package name */
    private ab f6339b;

    public P(ab abVar, ab abVar2) {
        this.f6338a = null;
        this.f6339b = null;
        this.f6338a = abVar;
        this.f6339b = abVar2;
    }

    public double a(S s2) {
        double d2 = ad.f6377b;
        if (this.f6338a.b(s2) != 0.0d || this.f6339b.b(s2) != 0.0d) {
            d2 = ad.f6376a;
        }
        return d2;
    }

    @Override // ax.ab
    public double b(S s2) {
        return a(s2);
    }

    public String toString() {
        return "( " + this.f6338a.toString() + " || " + this.f6339b.toString() + " )";
    }
}
