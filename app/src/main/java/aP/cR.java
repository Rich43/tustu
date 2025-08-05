package aP;

import ao.C0804hg;
import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: TunerStudioMS.jar:aP/cR.class */
class cR extends Thread {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ bZ f3127a;

    cR(bZ bZVar) {
        this.f3127a = bZVar;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        try {
            sleep(500L);
        } catch (InterruptedException e2) {
            Logger.getLogger(bZ.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
        }
        if (!h.i.a(h.i.f12331ax, h.i.f12332ay)) {
            C0804hg.a().c(this.f3127a.f3027b.p().o());
        }
        C0804hg.a().f();
        this.f3127a.j();
    }
}
