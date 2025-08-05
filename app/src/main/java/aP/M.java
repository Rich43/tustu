package aP;

import java.awt.HeadlessException;
import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: TunerStudioMS.jar:aP/M.class */
class M extends Thread {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ int f2760a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ L f2761b;

    M(L l2, int i2) {
        this.f2761b = l2;
        this.f2760a = i2;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() throws HeadlessException {
        try {
            Thread.sleep(this.f2760a);
        } catch (InterruptedException e2) {
            Logger.getLogger(L.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
        }
        this.f2761b.a();
    }
}
