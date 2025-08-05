package ax;

/* loaded from: TunerStudioMS.jar:ax/al.class */
class al implements ab {

    /* renamed from: a, reason: collision with root package name */
    private C0921x f6388a;

    /* renamed from: b, reason: collision with root package name */
    private C0921x f6389b;

    /* renamed from: c, reason: collision with root package name */
    private C0921x f6390c;

    public al(C0921x c0921x, C0921x c0921x2, C0921x c0921x3) {
        this.f6388a = null;
        this.f6389b = null;
        this.f6390c = null;
        this.f6388a = c0921x;
        this.f6389b = c0921x2;
        this.f6390c = c0921x3;
    }

    @Override // ax.ab
    public double b(S s2) {
        return this.f6388a.b(s2) == ad.f6377b ? this.f6390c.b(s2) : this.f6389b.b(s2);
    }

    public String toString() {
        return "( " + this.f6388a.toString() + " ) ? (" + this.f6389b.toString() + ") : (" + this.f6390c.toString() + ")";
    }
}
