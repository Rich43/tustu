package aP;

/* loaded from: TunerStudioMS.jar:aP/gY.class */
class gY implements G.S {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ gW f3473a;

    gY(gW gWVar) {
        this.f3473a = gWVar;
    }

    @Override // G.S
    public void a(G.R r2) {
        this.f3473a.f3456g.e(r2);
    }

    @Override // G.S
    public void b(G.R r2) {
        G.R rA = this.f3473a.f3456g.a();
        if (rA == null || !rA.equals(r2)) {
            return;
        }
        this.f3473a.f3456g.e(null);
    }

    @Override // G.S
    public void c(G.R r2) {
    }
}
