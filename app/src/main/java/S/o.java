package S;

import bH.C;

/* loaded from: TunerStudioMS.jar:S/o.class */
class o implements d {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ n f1844a;

    o(n nVar) {
        this.f1844a = nVar;
    }

    @Override // S.d
    public void a() {
        try {
            d.g.a().c(this.f1844a.f1843d);
        } catch (d.e e2) {
            C.b("Failed to fire AppAction: " + this.f1844a.f1843d + "\nError: " + e2.getLocalizedMessage());
        }
    }

    @Override // S.d
    public void b() {
    }
}
