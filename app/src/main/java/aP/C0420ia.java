package aP;

/* renamed from: aP.ia, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:aP/ia.class */
class C0420ia implements G.aN {

    /* renamed from: a, reason: collision with root package name */
    aE.d f3711a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ hZ f3712b;

    C0420ia(hZ hZVar, aE.d dVar) {
        this.f3712b = hZVar;
        this.f3711a = dVar;
    }

    @Override // G.aN
    public void a(String str, String str2) {
        G.R rC;
        G.aM aMVarC;
        if (!str2.equals("tsCanId") || (aMVarC = (rC = G.T.a().c(this.f3711a.a())).c("tsCanId")) == null) {
            return;
        }
        try {
            if (aMVarC.n(rC.p())) {
                int iJ = (int) aMVarC.j(rC.h());
                this.f3711a.a(iJ);
                bH.C.d(aMVarC.aJ() + " changed, update Project CAN ID to: " + iJ);
            }
        } catch (Exception e2) {
            bH.C.b("Failed to update Project Can ID");
        }
    }
}
