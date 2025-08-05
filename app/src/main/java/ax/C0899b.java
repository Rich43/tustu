package ax;

/* renamed from: ax.b, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ax/b.class */
class C0899b extends ac {

    /* renamed from: a, reason: collision with root package name */
    private ab f6392a;

    public C0899b(ab abVar) {
        this.f6392a = null;
        this.f6392a = abVar;
    }

    public double a(S s2) {
        return Math.acos(this.f6392a.b(s2));
    }

    @Override // ax.ab
    public double b(S s2) {
        return a(s2);
    }

    public String toString() {
        return "acos( " + this.f6392a.toString() + " )";
    }
}
