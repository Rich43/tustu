package S;

import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: TunerStudioMS.jar:S/f.class */
class f extends Thread {

    /* renamed from: a, reason: collision with root package name */
    long f1827a;

    /* renamed from: b, reason: collision with root package name */
    int f1828b;

    /* renamed from: c, reason: collision with root package name */
    final /* synthetic */ e f1829c;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    f(e eVar) {
        super("Offline Event Monitor Thread");
        this.f1829c = eVar;
        this.f1827a = 0L;
        this.f1828b = 99;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        while (true) {
            if (System.currentTimeMillis() - this.f1827a > this.f1828b) {
                this.f1829c.b();
            }
            try {
                Thread.sleep(50L);
            } catch (InterruptedException e2) {
                Logger.getLogger(e.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            }
        }
    }

    public void a() {
        this.f1827a = System.currentTimeMillis();
    }
}
