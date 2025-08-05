package aP;

import java.util.logging.Level;
import java.util.logging.Logger;

/* renamed from: aP.hf, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:aP/hf.class */
class C0398hf extends Thread {

    /* renamed from: a, reason: collision with root package name */
    int f3572a;

    /* renamed from: b, reason: collision with root package name */
    long f3573b;

    /* renamed from: c, reason: collision with root package name */
    final /* synthetic */ C0394hb f3574c;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    C0398hf(C0394hb c0394hb) {
        super("EnableDelay");
        this.f3574c = c0394hb;
        this.f3572a = 100;
        this.f3573b = System.currentTimeMillis() + this.f3572a;
        setDaemon(true);
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        while (System.currentTimeMillis() < this.f3573b) {
            long jCurrentTimeMillis = (this.f3573b - System.currentTimeMillis()) + 1;
            if (jCurrentTimeMillis > 0) {
                try {
                    Thread.sleep(jCurrentTimeMillis);
                } catch (InterruptedException e2) {
                    Logger.getLogger(C0394hb.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
                }
            }
        }
        this.f3574c.f3562g = null;
        this.f3574c.k();
    }

    public void a() {
        this.f3573b = System.currentTimeMillis() + this.f3572a;
    }
}
