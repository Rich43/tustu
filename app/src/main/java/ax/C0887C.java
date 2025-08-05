package ax;

/* renamed from: ax.C, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ax/C.class */
class C0887C extends ac {

    /* renamed from: a, reason: collision with root package name */
    private ab f6322a;

    /* renamed from: b, reason: collision with root package name */
    private ab f6323b;

    public C0887C(ab abVar, ab abVar2) {
        this.f6322a = null;
        this.f6323b = null;
        this.f6322a = abVar;
        this.f6323b = abVar2;
    }

    public double a(S s2) {
        double d2 = ad.f6377b;
        if (this.f6322a.b(s2) > this.f6323b.b(s2)) {
            d2 = ad.f6376a;
        }
        return d2;
    }

    @Override // ax.ab
    public double b(S s2) {
        return a(s2);
    }

    public String toString() {
        return "( " + this.f6322a.toString() + " > " + this.f6323b.toString() + " )";
    }
}
