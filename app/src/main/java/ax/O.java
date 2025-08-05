package ax;

/* loaded from: TunerStudioMS.jar:ax/O.class */
class O extends ac {

    /* renamed from: a, reason: collision with root package name */
    private ab f6336a;

    /* renamed from: b, reason: collision with root package name */
    private ab f6337b;

    public O(ab abVar, ab abVar2) {
        this.f6336a = null;
        this.f6337b = null;
        this.f6336a = abVar;
        this.f6337b = abVar2;
    }

    public double a(S s2) {
        double d2 = ad.f6377b;
        if (this.f6336a.b(s2) != 0.0d && this.f6337b.b(s2) != 0.0d) {
            d2 = ad.f6376a;
        }
        return d2;
    }

    @Override // ax.ab
    public double b(S s2) {
        return a(s2);
    }

    public String toString() {
        return "( " + this.f6336a.toString() + " && " + this.f6337b.toString() + " )";
    }
}
