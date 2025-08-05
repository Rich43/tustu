package B;

import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: TunerStudioMS.jar:B/h.class */
class h extends Thread {

    /* renamed from: a, reason: collision with root package name */
    boolean f152a = false;

    /* renamed from: b, reason: collision with root package name */
    int f153b = 2000;

    /* renamed from: c, reason: collision with root package name */
    final /* synthetic */ g f154c;

    h(g gVar) {
        this.f154c = gVar;
        super.setName("UDP Scan Thread " + Math.random());
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        while (!this.f152a) {
            this.f154c.g();
            try {
                Thread.sleep(this.f153b);
            } catch (InterruptedException e2) {
                Logger.getLogger(g.class.getName()).log(Level.INFO, "Shouldn't happen.", (Throwable) e2);
            }
        }
    }

    public void a() {
        this.f152a = true;
    }
}
