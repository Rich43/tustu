package ax;

/* loaded from: TunerStudioMS.jar:ax/Y.class */
class Y extends ac {

    /* renamed from: a, reason: collision with root package name */
    private ab f6371a;

    public Y(ab abVar) {
        this.f6371a = null;
        this.f6371a = abVar;
    }

    public double a(S s2) {
        double d2 = ad.f6377b;
        if (this.f6371a.b(s2) == ad.f6377b) {
            d2 = ad.f6376a;
        }
        return d2;
    }

    @Override // ax.ab
    public double b(S s2) {
        return a(s2);
    }

    public String toString() {
        return " !" + this.f6371a.toString();
    }
}
