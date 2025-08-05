package c;

import java.util.logging.Level;
import java.util.logging.Logger;

/* renamed from: c.c, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:c/c.class */
class C1384c extends Thread {

    /* renamed from: a, reason: collision with root package name */
    long f9261a;

    /* renamed from: b, reason: collision with root package name */
    long f9262b;

    /* renamed from: c, reason: collision with root package name */
    final /* synthetic */ C1383b f9263c;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    C1384c(C1383b c1383b) {
        super("EnableByCondition Service");
        this.f9263c = c1383b;
        this.f9261a = Long.MAX_VALUE;
        this.f9262b = 0L;
        super.setDaemon(true);
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        while (true) {
            try {
                sleep(this.f9263c.f9257a);
            } catch (InterruptedException e2) {
                Logger.getLogger(C1383b.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            }
            if (System.currentTimeMillis() > this.f9261a && System.currentTimeMillis() - this.f9262b > this.f9263c.f9257a) {
                this.f9261a = Long.MAX_VALUE;
                this.f9262b = System.currentTimeMillis();
                this.f9263c.b();
            }
        }
    }

    public void a() {
        this.f9261a = System.currentTimeMillis() + this.f9263c.f9257a;
    }
}
