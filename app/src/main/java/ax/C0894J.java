package ax;

/* renamed from: ax.J, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ax/J.class */
class C0894J extends ac {

    /* renamed from: a, reason: collision with root package name */
    private ab f6329a;

    /* renamed from: b, reason: collision with root package name */
    private ab f6330b;

    public C0894J(ab abVar, ab abVar2) {
        this.f6329a = null;
        this.f6330b = null;
        this.f6329a = abVar;
        this.f6330b = abVar2;
    }

    public double a(S s2) {
        double d2 = ad.f6377b;
        if (this.f6329a.b(s2) <= this.f6330b.b(s2)) {
            d2 = ad.f6376a;
        }
        return d2;
    }

    @Override // ax.ab
    public double b(S s2) {
        return a(s2);
    }

    public String toString() {
        return "( " + this.f6329a.toString() + " <= " + this.f6330b.toString() + " )";
    }
}
