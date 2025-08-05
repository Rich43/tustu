package ax;

/* renamed from: ax.B, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ax/B.class */
class C0886B extends ac {

    /* renamed from: a, reason: collision with root package name */
    private ab f6320a;

    /* renamed from: b, reason: collision with root package name */
    private ab f6321b;

    public C0886B(ab abVar, ab abVar2) {
        this.f6320a = null;
        this.f6321b = null;
        this.f6320a = abVar;
        this.f6321b = abVar2;
    }

    public double a(S s2) {
        double d2 = ad.f6377b;
        if (this.f6320a.b(s2) >= this.f6321b.b(s2)) {
            d2 = ad.f6376a;
        }
        return d2;
    }

    @Override // ax.ab
    public double b(S s2) {
        return a(s2);
    }

    public String toString() {
        return "( " + this.f6320a.toString() + " >= " + this.f6321b.toString() + " )";
    }
}
