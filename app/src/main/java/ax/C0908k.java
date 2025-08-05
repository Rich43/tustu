package ax;

/* renamed from: ax.k, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ax/k.class */
class C0908k extends ac {

    /* renamed from: a, reason: collision with root package name */
    private ab f6402a;

    /* renamed from: b, reason: collision with root package name */
    private ab f6403b;

    public C0908k(ab abVar, ab abVar2) {
        this.f6402a = null;
        this.f6403b = null;
        this.f6402a = abVar;
        this.f6403b = abVar2;
    }

    public double a(S s2) {
        return a(this.f6402a, s2) | a(this.f6403b, s2);
    }

    @Override // ax.ab
    public double b(S s2) {
        return a(s2);
    }

    public String toString() {
        return "( " + this.f6402a.toString() + " | " + this.f6403b.toString() + " )";
    }
}
