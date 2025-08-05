package aP;

import ao.C0645bi;
import ao.C0659bw;
import ao.C0804hg;

/* renamed from: aP.cd, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:aP/cd.class */
class RunnableC0262cd implements Runnable {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ bZ f3155a;

    RunnableC0262cd(bZ bZVar) {
        this.f3155a = bZVar;
    }

    @Override // java.lang.Runnable
    public void run() {
        C0659bw c0659bwF = C0645bi.a().f();
        if (C0804hg.a().r() == null) {
            return;
        }
        if (this.f3155a.f3051z.isSelected() && cZ.a().n().getComponentCount() == 0) {
            cZ.a().n().add(c0659bwF);
            boolean zA = h.i.a("showDashboard", true);
            cZ.a().c().validate();
            C0804hg.a().c(zA);
            return;
        }
        if (this.f3155a.f3051z.isSelected() || C0645bi.a().h().getComponentCount() != 0) {
            return;
        }
        C0645bi.a().h().add(c0659bwF);
        cZ.a().c().validate();
    }
}
