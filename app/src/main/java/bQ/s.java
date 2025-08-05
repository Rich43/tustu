package bQ;

import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: TunerStudioMS.jar:bQ/s.class */
public class s extends Thread {

    /* renamed from: a, reason: collision with root package name */
    boolean f7473a = false;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ l f7474b;

    public s(l lVar) {
        this.f7474b = lVar;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        while (!this.f7473a) {
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e2) {
                Logger.getLogger(l.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            }
            if (!this.f7473a) {
                try {
                    this.f7474b.g();
                } catch (Exception e3) {
                    Logger.getLogger(l.class.getName()).log(Level.WARNING, "Failed to send sync", (Throwable) e3);
                }
            }
        }
    }

    public void a() {
        this.f7473a = true;
    }
}
