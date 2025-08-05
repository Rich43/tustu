package ac;

import G.C0113cs;
import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: TunerStudioMS.jar:ac/k.class */
public class k extends Thread {

    /* renamed from: a, reason: collision with root package name */
    boolean f4222a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ h f4223b;

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        while (this.f4222a) {
            C0113cs.a().a("dataLoggingActive", 1.0d);
            if (h.f4217v == null) {
                C0113cs.a().a("dataLogTime", (System.currentTimeMillis() - this.f4223b.f4205j) / 1000.0d);
            }
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e2) {
                Logger.getLogger(h.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            }
        }
        C0113cs.a().a("dataLoggingActive", 0.0d);
    }

    public void a() {
        this.f4222a = false;
    }
}
