package ax;

/* loaded from: TunerStudioMS.jar:ax/V.class */
class V extends ac {

    /* renamed from: a, reason: collision with root package name */
    private ab f6366a;

    /* renamed from: b, reason: collision with root package name */
    private ab f6367b;

    public V(ab abVar, ab abVar2) {
        this.f6366a = null;
        this.f6367b = null;
        this.f6366a = abVar;
        this.f6367b = abVar2;
    }

    public double a(S s2) throws C0918u {
        if (this.f6367b.b(s2) == 0.0d) {
            throw new C0918u();
        }
        return a(this.f6366a, s2) % a(this.f6367b, s2);
    }

    @Override // ax.ab
    public double b(S s2) {
        return a(s2);
    }

    public String toString() {
        return "( " + this.f6366a.toString() + " % " + this.f6367b.toString() + " )";
    }
}
