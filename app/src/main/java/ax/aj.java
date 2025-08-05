package ax;

/* loaded from: TunerStudioMS.jar:ax/aj.class */
class aj extends ac {

    /* renamed from: a, reason: collision with root package name */
    private ab f6385a;

    /* renamed from: b, reason: collision with root package name */
    private ab f6386b;

    public aj(ab abVar, ab abVar2) {
        this.f6385a = null;
        this.f6386b = null;
        this.f6385a = abVar;
        this.f6386b = abVar2;
    }

    public double a(S s2) {
        return this.f6385a.b(s2) - this.f6386b.b(s2);
    }

    @Override // ax.ab
    public double b(S s2) {
        return a(s2);
    }

    public String toString() {
        return "( " + this.f6385a.toString() + " - " + this.f6386b.toString() + " )";
    }
}
