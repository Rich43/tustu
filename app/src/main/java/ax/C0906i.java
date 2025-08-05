package ax;

/* renamed from: ax.i, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ax/i.class */
class C0906i extends ac {

    /* renamed from: a, reason: collision with root package name */
    private ab f6399a;

    /* renamed from: b, reason: collision with root package name */
    private ab f6400b;

    public C0906i(ab abVar, ab abVar2) {
        this.f6399a = null;
        this.f6400b = null;
        this.f6399a = abVar;
        this.f6400b = abVar2;
    }

    public double a(S s2) {
        return a(this.f6399a, s2) << a(this.f6400b, s2);
    }

    @Override // ax.ab
    public double b(S s2) {
        return a(s2);
    }

    public String toString() {
        return "( " + this.f6399a.toString() + " << " + this.f6400b.toString() + " )";
    }
}
