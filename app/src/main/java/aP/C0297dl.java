package aP;

import java.util.logging.Level;
import java.util.logging.Logger;

/* renamed from: aP.dl, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:aP/dl.class */
class C0297dl extends Thread {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C0293dh f3238a;

    C0297dl(C0293dh c0293dh) {
        this.f3238a = c0293dh;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        try {
            Thread.sleep(10000L);
        } catch (InterruptedException e2) {
            Logger.getLogger(C0293dh.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
        }
        com.efiAnalytics.ui.cS.a().e();
    }
}
