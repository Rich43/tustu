package ax;

/* loaded from: TunerStudioMS.jar:ax/W.class */
class W extends ac {

    /* renamed from: a, reason: collision with root package name */
    private ab f6368a;

    /* renamed from: b, reason: collision with root package name */
    private ab f6369b;

    public W(ab abVar, ab abVar2) {
        this.f6368a = null;
        this.f6369b = null;
        this.f6368a = abVar;
        this.f6369b = abVar2;
    }

    public double a(S s2) {
        return this.f6368a.b(s2) * this.f6369b.b(s2);
    }

    @Override // ax.ab
    public double b(S s2) {
        return a(s2);
    }

    public String toString() {
        return "( " + this.f6368a.toString() + " * " + this.f6369b.toString() + " )";
    }
}
