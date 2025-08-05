package ax;

/* renamed from: ax.K, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ax/K.class */
class C0895K extends ac {

    /* renamed from: a, reason: collision with root package name */
    private ab f6331a;

    /* renamed from: b, reason: collision with root package name */
    private ab f6332b;

    public C0895K(ab abVar, ab abVar2) {
        this.f6331a = null;
        this.f6332b = null;
        this.f6331a = abVar;
        this.f6332b = abVar2;
    }

    public double a(S s2) {
        double d2 = ad.f6377b;
        if (this.f6331a.b(s2) < this.f6332b.b(s2)) {
            d2 = ad.f6376a;
        }
        return d2;
    }

    @Override // ax.ab
    public double b(S s2) {
        return a(s2);
    }

    public String toString() {
        return "( " + this.f6331a.toString() + " < " + this.f6332b.toString() + " )";
    }
}
