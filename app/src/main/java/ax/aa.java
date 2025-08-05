package ax;

/* loaded from: TunerStudioMS.jar:ax/aa.class */
class aa extends ac {

    /* renamed from: a, reason: collision with root package name */
    private ab f6374a;

    /* renamed from: b, reason: collision with root package name */
    private ab f6375b;

    public aa(ab abVar, ab abVar2) {
        this.f6374a = null;
        this.f6375b = null;
        this.f6374a = abVar;
        this.f6375b = abVar2;
    }

    public double a(S s2) {
        double d2 = ad.f6377b;
        if (this.f6374a.b(s2) != this.f6375b.b(s2)) {
            d2 = ad.f6376a;
        }
        return d2;
    }

    @Override // ax.ab
    public double b(S s2) {
        return a(s2);
    }

    public String toString() {
        return "( " + this.f6374a.toString() + " != " + this.f6375b.toString() + " )";
    }
}
