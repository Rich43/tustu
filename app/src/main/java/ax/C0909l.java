package ax;

/* renamed from: ax.l, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ax/l.class */
class C0909l extends ac {

    /* renamed from: a, reason: collision with root package name */
    private ab f6404a;

    /* renamed from: b, reason: collision with root package name */
    private ab f6405b;

    public C0909l(ab abVar, ab abVar2) {
        this.f6404a = null;
        this.f6405b = null;
        this.f6404a = abVar;
        this.f6405b = abVar2;
    }

    public double a(S s2) {
        return a(this.f6404a, s2) >> a(this.f6405b, s2);
    }

    @Override // ax.ab
    public double b(S s2) {
        return a(s2);
    }

    public String toString() {
        return "( " + this.f6404a.toString() + " >> " + this.f6405b.toString() + " )";
    }
}
