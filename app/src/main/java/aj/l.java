package aj;

import bH.C;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: TunerStudioMS.jar:aj/l.class */
class l extends Thread {

    /* renamed from: a, reason: collision with root package name */
    boolean f4576a = true;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ k f4577b;

    l(k kVar) {
        this.f4577b = kVar;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        int i2 = 0;
        while (this.f4576a) {
            try {
                this.f4577b.q();
                i2 = 0;
            } catch (IOException e2) {
                if (i2 > 3) {
                    C.c("3 consectutive errors, giving up log processing");
                    this.f4576a = false;
                }
                try {
                    sleep(1L);
                } catch (InterruptedException e3) {
                    Logger.getLogger(k.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e3);
                }
                i2++;
                if (this.f4576a) {
                    Logger.getLogger(k.class.getName()).log(Level.SEVERE, "Failed to read data set", (Throwable) e2);
                }
            }
        }
    }

    public synchronized void a() {
        this.f4576a = false;
        notify();
    }
}
