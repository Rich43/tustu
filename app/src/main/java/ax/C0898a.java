package ax;

/* renamed from: ax.a, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ax/a.class */
class C0898a extends ac {

    /* renamed from: a, reason: collision with root package name */
    private ab f6373a;

    public C0898a(ab abVar) {
        this.f6373a = null;
        this.f6373a = abVar;
    }

    public double a(S s2) {
        return Math.abs(this.f6373a.b(s2));
    }

    @Override // ax.ab
    public double b(S s2) {
        return a(s2);
    }

    public String toString() {
        return "abs( " + this.f6373a.toString() + " )";
    }
}
