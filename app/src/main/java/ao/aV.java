package ao;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;

/* loaded from: TunerStudioMS.jar:ao/aV.class */
class aV extends Thread {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ aQ f5162a;

    aV(aQ aQVar) {
        this.f5162a = aQVar;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        try {
            Thread.sleep(400L);
        } catch (InterruptedException e2) {
            Logger.getLogger(aQ.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
        }
        SwingUtilities.invokeLater(new aW(this));
    }
}
