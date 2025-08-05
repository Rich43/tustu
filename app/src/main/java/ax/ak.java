package ax;

/* loaded from: TunerStudioMS.jar:ax/ak.class */
class ak extends ac {

    /* renamed from: a, reason: collision with root package name */
    private ab f6387a;

    public ak(ab abVar) {
        this.f6387a = null;
        this.f6387a = abVar;
    }

    public double a(S s2) {
        return Math.tan(this.f6387a.b(s2));
    }

    @Override // ax.ab
    public double b(S s2) {
        return a(s2);
    }

    public String toString() {
        return "tan( " + this.f6387a.toString() + " )";
    }
}
