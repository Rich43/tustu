package ax;

/* renamed from: ax.n, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ax/n.class */
class C0911n extends ac {

    /* renamed from: a, reason: collision with root package name */
    private ab f6408a;

    /* renamed from: b, reason: collision with root package name */
    private ab f6409b;

    public C0911n(ab abVar, ab abVar2) {
        this.f6408a = null;
        this.f6409b = null;
        this.f6408a = abVar;
        this.f6409b = abVar2;
    }

    public double a(S s2) {
        return a(this.f6408a, s2) ^ a(this.f6409b, s2);
    }

    @Override // ax.ab
    public double b(S s2) {
        return a(s2);
    }

    public String toString() {
        return "( " + this.f6408a.toString() + " ^ " + this.f6409b.toString() + " )";
    }
}
