package bb;

import com.efiAnalytics.ui.bV;
import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: TunerStudioMS.jar:bb/z.class */
class z extends Thread {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ x f7860a;

    z(x xVar) {
        this.f7860a = xVar;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        try {
            this.f7860a.i();
        } catch (V.a e2) {
            Logger.getLogger(x.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            bV.d(e2.getMessage(), this.f7860a.f7842d);
            this.f7860a.f7842d.setEnabled(true);
        }
    }
}
