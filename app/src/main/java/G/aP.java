package G;

import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: TunerStudioMS.jar:G/aP.class */
class aP extends Thread {

    /* renamed from: b, reason: collision with root package name */
    private int f631b = 200;

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ aO f632a;

    aP(aO aOVar) {
        this.f632a = aOVar;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() throws NumberFormatException {
        try {
            sleep(this.f631b);
        } catch (InterruptedException e2) {
            Logger.getLogger(aO.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
        }
        this.f632a.f629e = null;
        this.f632a.b();
    }
}
