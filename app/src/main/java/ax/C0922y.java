package ax;

/* renamed from: ax.y, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ax/y.class */
class C0922y extends ac {

    /* renamed from: a, reason: collision with root package name */
    private ab f6422a;

    public C0922y(ab abVar) {
        this.f6422a = null;
        this.f6422a = abVar;
    }

    public double a(S s2) {
        return Math.floor(this.f6422a.b(s2));
    }

    @Override // ax.ab
    public double b(S s2) {
        return a(s2);
    }

    public String toString() {
        return "floor( " + this.f6422a.toString() + " )";
    }
}
