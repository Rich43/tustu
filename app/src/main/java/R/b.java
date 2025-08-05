package R;

import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: TunerStudioMS.jar:R/b.class */
public class b extends Thread {

    /* renamed from: a, reason: collision with root package name */
    String f1791a;

    public b() {
        super("MessageMonitor");
        this.f1791a = "http://services.efianalytics.com/efiaServices/UserServices";
        setDaemon(true);
    }

    public boolean a() {
        try {
            new URL(this.f1791a).openStream().close();
            return true;
        } catch (Exception e2) {
            return false;
        }
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        n nVar = new n();
        while (true) {
            if (nVar.b() && a()) {
                nVar.a();
            }
            try {
                Thread.sleep(60000L);
            } catch (InterruptedException e2) {
                Logger.getLogger(b.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            }
        }
    }
}
