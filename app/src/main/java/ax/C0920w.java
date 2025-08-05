package ax;

/* renamed from: ax.w, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ax/w.class */
class C0920w extends ac {

    /* renamed from: a, reason: collision with root package name */
    private ab f6419a;

    /* renamed from: b, reason: collision with root package name */
    private ab f6420b;

    public C0920w(ab abVar, ab abVar2) {
        this.f6419a = null;
        this.f6420b = null;
        this.f6419a = abVar;
        this.f6420b = abVar2;
    }

    public double a(S s2) {
        double d2 = ad.f6377b;
        if (this.f6419a.b(s2) == this.f6420b.b(s2)) {
            d2 = ad.f6376a;
        }
        return d2;
    }

    @Override // ax.ab
    public double b(S s2) {
        return a(s2);
    }

    public String toString() {
        return "( " + this.f6419a.toString() + " == " + this.f6420b.toString() + " )";
    }
}
