package aP;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import s.C1818g;

/* loaded from: TunerStudioMS.jar:aP/E.class */
class E extends Thread {

    /* renamed from: a, reason: collision with root package name */
    File[] f2741a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ C0338f f2742b;

    E(C0338f c0338f, File[] fileArr) {
        this.f2742b = c0338f;
        this.f2741a = fileArr;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() throws IllegalArgumentException {
        bZ bZVarJ = cZ.a().j();
        for (int i2 = 0; i2 < 100 && bZVarJ == null; i2++) {
            try {
                Thread.sleep(100L);
                bH.C.c("Waiting for GraphPanel (files)");
            } catch (InterruptedException e2) {
                Logger.getLogger(C0338f.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            }
            bZVarJ = cZ.a().j();
        }
        if (bZVarJ == null) {
            com.efiAnalytics.ui.bV.d(C1818g.b("Graph Panel not initialized!"), cZ.a().c());
        } else {
            bZVarJ.a(this.f2741a);
        }
    }
}
