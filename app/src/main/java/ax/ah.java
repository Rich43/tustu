package ax;

/* loaded from: TunerStudioMS.jar:ax/ah.class */
class ah extends ac {

    /* renamed from: a, reason: collision with root package name */
    private ab f6383a;

    public ah(ab abVar) {
        this.f6383a = null;
        this.f6383a = abVar;
    }

    public double a(S s2) {
        return Math.sin(this.f6383a.b(s2));
    }

    @Override // ax.ab
    public double b(S s2) {
        return a(s2);
    }

    public String toString() {
        return "sin( " + this.f6383a.toString() + " )";
    }
}
