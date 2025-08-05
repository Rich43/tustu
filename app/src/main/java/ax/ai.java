package ax;

/* loaded from: TunerStudioMS.jar:ax/ai.class */
class ai extends ac {

    /* renamed from: a, reason: collision with root package name */
    private ab f6384a;

    public ai(ab abVar) {
        this.f6384a = null;
        this.f6384a = abVar;
    }

    public double a(S s2) {
        return Math.sqrt(this.f6384a.b(s2));
    }

    @Override // ax.ab
    public double b(S s2) {
        return a(s2);
    }

    public String toString() {
        return "sqrt( " + this.f6384a.toString() + " )";
    }
}
