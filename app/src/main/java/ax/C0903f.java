package ax;

/* renamed from: ax.f, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ax/f.class */
class C0903f extends ac {

    /* renamed from: a, reason: collision with root package name */
    private ab f6395a;

    /* renamed from: b, reason: collision with root package name */
    private ab f6396b;

    public C0903f(ab abVar, ab abVar2) {
        this.f6395a = null;
        this.f6396b = null;
        this.f6395a = abVar;
        this.f6396b = abVar2;
    }

    public double a(S s2) {
        return this.f6395a.b(s2) + this.f6396b.b(s2);
    }

    @Override // ax.ab
    public double b(S s2) {
        return a(s2);
    }

    public String toString() {
        return "( " + this.f6395a.toString() + " + " + this.f6396b.toString() + " )";
    }
}
