package aP;

/* loaded from: TunerStudioMS.jar:aP/hT.class */
class hT implements S.m {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ hJ f3543a;

    hT(hJ hJVar) {
        this.f3543a = hJVar;
    }

    @Override // S.m
    public void a(String str) {
        G.R rC = G.T.a().c(str);
        double dA = U.b.a(rC);
        if (dA < 0.0d || dA > 30.0d) {
            U.b.a(rC, 30);
        } else {
            U.b.a(rC, (int) dA);
        }
    }
}
