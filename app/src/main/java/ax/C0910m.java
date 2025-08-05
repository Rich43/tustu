package ax;

/* renamed from: ax.m, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ax/m.class */
class C0910m extends ac {

    /* renamed from: a, reason: collision with root package name */
    private ab f6406a;

    /* renamed from: b, reason: collision with root package name */
    private ab f6407b;

    public C0910m(ab abVar, ab abVar2) {
        this.f6406a = null;
        this.f6407b = null;
        this.f6406a = abVar;
        this.f6407b = abVar2;
    }

    public double a(S s2) {
        return a(this.f6406a, s2) >>> a(this.f6407b, s2);
    }

    @Override // ax.ab
    public double b(S s2) {
        return a(s2);
    }

    public String toString() {
        return "( " + this.f6406a.toString() + " >>> " + this.f6407b.toString() + " )";
    }
}
