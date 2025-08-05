package aP;

import java.util.logging.Level;
import java.util.logging.Logger;

/* renamed from: aP.hd, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:aP/hd.class */
class C0396hd extends Thread {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C0394hb f3570a;

    C0396hd(C0394hb c0394hb) {
        this.f3570a = c0394hb;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        try {
            Thread.sleep(2000L);
        } catch (InterruptedException e2) {
            Logger.getLogger(C0394hb.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
        }
        this.f3570a.f3556a.I();
        try {
            Thread.sleep(3000L);
        } catch (InterruptedException e3) {
            Logger.getLogger(C0394hb.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e3);
        }
        this.f3570a.f3556a.C().f(true);
    }
}
