package ax;

/* loaded from: TunerStudioMS.jar:ax/N.class */
class N extends ac {

    /* renamed from: a, reason: collision with root package name */
    private ab f6335a;

    public N(ab abVar) {
        this.f6335a = null;
        this.f6335a = abVar;
    }

    public double a(S s2) {
        return Math.log10(this.f6335a.b(s2));
    }

    @Override // ax.ab
    public double b(S s2) {
        return a(s2);
    }

    public String toString() {
        return "log10( " + this.f6335a.toString() + " )";
    }
}
