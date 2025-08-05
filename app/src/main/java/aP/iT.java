package aP;

import com.efiAnalytics.ui.C1658ep;

/* loaded from: TunerStudioMS.jar:aP/iT.class */
class iT implements Runnable {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ G.R f3685a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ iR f3686b;

    /* renamed from: c, reason: collision with root package name */
    final /* synthetic */ iR f3687c;

    iT(iR iRVar, G.R r2, iR iRVar2) {
        this.f3687c = iRVar;
        this.f3685a = r2;
        this.f3686b = iRVar2;
    }

    @Override // java.lang.Runnable
    public void run() {
        if (this.f3685a.C() != null) {
            C1658ep c1658ep = new C1658ep(this.f3685a.c());
            this.f3685a.C().a((G.aV) c1658ep);
            this.f3685a.C().a((G.aG) c1658ep);
            this.f3687c.a(c1658ep);
            this.f3685a.a(this.f3686b);
            this.f3685a.c(this.f3686b);
        }
    }
}
