package L;

import G.cX;

/* renamed from: L.i, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:L/i.class */
class C0152i implements cX {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C0151h f1662a;

    C0152i(C0151h c0151h) {
        this.f1662a = c0151h;
    }

    @Override // G.cX
    public String a() {
        G.R rC = G.T.a().c();
        String strC = this.f1662a.f1659b.c();
        if (rC != null && strC != null && strC.equals(rC.c())) {
            strC = "";
        }
        return strC;
    }
}
