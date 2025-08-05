package aP;

import com.efiAnalytics.ui.InterfaceC1663eu;

/* loaded from: TunerStudioMS.jar:aP/hW.class */
class hW implements InterfaceC1663eu {

    /* renamed from: a, reason: collision with root package name */
    long f3547a = 0;

    /* renamed from: b, reason: collision with root package name */
    int f3548b = 60000;

    /* renamed from: c, reason: collision with root package name */
    final /* synthetic */ hJ f3549c;

    hW(hJ hJVar) {
        this.f3549c = hJVar;
    }

    @Override // com.efiAnalytics.ui.InterfaceC1663eu
    public boolean a() {
        G.T tA = G.T.a();
        if (!((tA == null || tA.c() == null || !tA.c().R()) ? false : true)) {
            return System.currentTimeMillis() - this.f3547a > ((long) this.f3548b);
        }
        this.f3547a = System.currentTimeMillis();
        return false;
    }
}
