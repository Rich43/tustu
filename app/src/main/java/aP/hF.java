package aP;

import s.C1818g;

/* loaded from: TunerStudioMS.jar:aP/hF.class */
class hF implements u.g {

    /* renamed from: a, reason: collision with root package name */
    G.R f3518a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ hC f3519b;

    hF(hC hCVar, G.R r2) {
        this.f3519b = hCVar;
        this.f3518a = r2;
    }

    @Override // u.g
    public String a() {
        return C1818g.b("Exit & Go offline");
    }

    @Override // u.g
    public String b() {
        return C1818g.b("Go offline and Exits the Difference Report with no changes to the currently loaded tune or Controller.");
    }

    @Override // u.g
    public boolean d() {
        this.f3518a.C().c();
        return true;
    }

    @Override // u.g
    public boolean c() {
        return false;
    }
}
