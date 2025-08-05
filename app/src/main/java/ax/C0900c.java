package ax;

/* renamed from: ax.c, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ax/c.class */
class C0900c extends ac {

    /* renamed from: a, reason: collision with root package name */
    private ab f6393a;

    public C0900c(ab abVar) {
        this.f6393a = null;
        this.f6393a = abVar;
    }

    public double a(S s2) {
        return Math.asin(this.f6393a.b(s2));
    }

    @Override // ax.ab
    public double b(S s2) {
        return a(s2);
    }

    public String toString() {
        return "asin( " + this.f6393a.toString() + " )";
    }
}
