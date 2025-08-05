package ax;

/* renamed from: ax.h, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ax/h.class */
class C0905h extends ac {

    /* renamed from: a, reason: collision with root package name */
    private ab f6397a;

    /* renamed from: b, reason: collision with root package name */
    private ab f6398b;

    public C0905h(ab abVar, ab abVar2) {
        this.f6397a = null;
        this.f6398b = null;
        this.f6397a = abVar;
        this.f6398b = abVar2;
    }

    public double a(S s2) {
        return a(this.f6397a, s2) & a(this.f6398b, s2);
    }

    @Override // ax.ab
    public double b(S s2) {
        return a(s2);
    }

    public String toString() {
        return "( " + this.f6397a.toString() + " & " + this.f6398b.toString() + " )";
    }
}
