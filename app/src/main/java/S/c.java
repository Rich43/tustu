package S;

import G.C0134q;
import G.R;
import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: TunerStudioMS.jar:S/c.class */
class c extends Thread {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ R f1822a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ b f1823b;

    c(b bVar, R r2) {
        this.f1823b = bVar;
        this.f1822a = r2;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e2) {
            Logger.getLogger(b.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
        }
        try {
            this.f1823b.c(this.f1822a.c());
        } catch (C0134q e3) {
            Logger.getLogger(b.class.getName()).log(Level.SEVERE, "Failed to Activate at least some Event Triggers.", (Throwable) e3);
        }
    }
}
