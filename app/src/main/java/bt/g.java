package bT;

import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: TunerStudioMS.jar:bT/g.class */
class g extends Thread {

    /* renamed from: a, reason: collision with root package name */
    int f7585a = 400;

    /* renamed from: b, reason: collision with root package name */
    boolean f7586b = false;

    /* renamed from: c, reason: collision with root package name */
    final /* synthetic */ e f7587c;

    public g(e eVar) {
        this.f7587c = eVar;
        setName("Slave Data Notify");
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        while (!this.f7586b) {
            if (this.f7587c.f7575b.isEmpty() || System.currentTimeMillis() - this.f7587c.f7576c <= this.f7585a) {
                synchronized (this.f7587c.f7577d) {
                    try {
                        this.f7587c.f7577d.wait(this.f7585a);
                    } catch (InterruptedException e2) {
                        Logger.getLogger(e.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
                    }
                }
            } else {
                try {
                    this.f7587c.b();
                } catch (bN.o e3) {
                    Logger.getLogger(e.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e3);
                }
            }
        }
    }

    public void a() {
        this.f7586b = true;
    }
}
