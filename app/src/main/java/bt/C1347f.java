package bt;

import java.util.logging.Level;
import java.util.logging.Logger;

/* renamed from: bt.f, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bt/f.class */
class C1347f extends Thread {

    /* renamed from: a, reason: collision with root package name */
    int f9094a;

    /* renamed from: b, reason: collision with root package name */
    long f9095b;

    /* renamed from: c, reason: collision with root package name */
    final /* synthetic */ C1346e f9096c;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    C1347f(C1346e c1346e) {
        super("EnableDelay");
        this.f9096c = c1346e;
        this.f9094a = 100;
        this.f9095b = System.currentTimeMillis() + this.f9094a;
        setDaemon(true);
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        while (System.currentTimeMillis() < this.f9095b) {
            long jCurrentTimeMillis = (this.f9095b - System.currentTimeMillis()) + 1;
            if (jCurrentTimeMillis > 0) {
                try {
                    Thread.sleep(jCurrentTimeMillis);
                } catch (InterruptedException e2) {
                    Logger.getLogger(C1346e.class.getName()).log(Level.SEVERE, "Playback Thread stopped", (Throwable) e2);
                }
            }
        }
        this.f9096c.f9093c = null;
        this.f9096c.b();
    }

    public void a() {
        this.f9095b = System.currentTimeMillis() + this.f9094a;
    }
}
