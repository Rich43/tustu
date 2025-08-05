package aP;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;

/* renamed from: aP.cf, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:aP/cf.class */
class C0264cf extends Thread {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ Runnable f3157a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ bZ f3158b;

    C0264cf(bZ bZVar, Runnable runnable) {
        this.f3158b = bZVar;
        this.f3157a = runnable;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        try {
            Thread.sleep(500L);
            bH.C.c("Waiting to enable Buttons.");
        } catch (InterruptedException e2) {
            Logger.getLogger(bZ.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
        }
        SwingUtilities.invokeLater(this.f3157a);
    }
}
