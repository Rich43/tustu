package ax;

/* renamed from: ax.t, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ax/t.class */
class C0917t extends ac {

    /* renamed from: a, reason: collision with root package name */
    private ab f6416a;

    /* renamed from: b, reason: collision with root package name */
    private ab f6417b;

    public C0917t(ab abVar, ab abVar2) {
        this.f6416a = null;
        this.f6417b = null;
        this.f6416a = abVar;
        this.f6417b = abVar2;
    }

    public double a(S s2) throws C0918u {
        if (this.f6417b.b(s2) == 0.0d) {
            throw new C0918u();
        }
        return this.f6416a.b(s2) / this.f6417b.b(s2);
    }

    @Override // ax.ab
    public double b(S s2) {
        return a(s2);
    }

    public String toString() {
        return this.f6416a.toString() + " / " + this.f6417b.toString();
    }
}
