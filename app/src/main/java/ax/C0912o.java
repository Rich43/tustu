package ax;

/* renamed from: ax.o, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ax/o.class */
class C0912o extends ac {

    /* renamed from: a, reason: collision with root package name */
    private ab f6410a;

    public C0912o(ab abVar) {
        this.f6410a = null;
        this.f6410a = abVar;
    }

    public double a(S s2) {
        return Math.cos(this.f6410a.b(s2));
    }

    @Override // ax.ab
    public double b(S s2) {
        return a(s2);
    }

    public String toString() {
        return "cos( " + this.f6410a.toString() + " )";
    }
}
