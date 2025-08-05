package aP;

import java.util.logging.Level;
import java.util.logging.Logger;

/* renamed from: aP.dg, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:aP/dg.class */
class C0292dg extends Thread {

    /* renamed from: a, reason: collision with root package name */
    boolean f3223a = true;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ C0289dd f3224b;

    C0292dg(C0289dd c0289dd) {
        this.f3224b = c0289dd;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        while (this.f3223a) {
            this.f3224b.repaint();
            try {
                sleep(30L);
            } catch (InterruptedException e2) {
                Logger.getLogger(com.efiAnalytics.apps.ts.tuningViews.J.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            }
            this.f3224b.repaint();
        }
    }

    public void a() {
        this.f3223a = false;
    }
}
