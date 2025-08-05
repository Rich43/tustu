package aP;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/* renamed from: aP.l, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:aP/l.class */
class RunnableC0456l implements Runnable {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C0338f f3822a;

    RunnableC0456l(C0338f c0338f) {
        this.f3822a = c0338f;
    }

    @Override // java.lang.Runnable
    public void run() {
        if (this.f3822a.g()) {
            C0338f.a().y();
            try {
                Runtime.getRuntime().exec(bH.I.a() ? "shutdown /p" : bH.I.b() ? "sudo shutdown -h now" : "sudo shutdown now");
                System.exit(0);
            } catch (IOException e2) {
                Logger.getLogger(C0338f.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            } finally {
                this.f3822a.f3343b = false;
            }
        }
    }
}
