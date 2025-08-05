package ay;

import java.util.logging.Level;
import java.util.logging.Logger;

/* renamed from: ay.b, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ay/b.class */
class C0925b extends Thread {

    /* renamed from: a, reason: collision with root package name */
    boolean f6432a = false;

    /* renamed from: b, reason: collision with root package name */
    int f6433b = 2500;

    /* renamed from: c, reason: collision with root package name */
    final /* synthetic */ C0924a f6434c;

    C0925b(C0924a c0924a) {
        this.f6434c = c0924a;
        super.setName("HttpServices Scan Thread " + Math.random());
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        while (!this.f6432a) {
            this.f6434c.k();
            try {
                Thread.sleep(this.f6433b);
            } catch (InterruptedException e2) {
                Logger.getLogger(B.g.class.getName()).log(Level.INFO, "Shouldn't happen.", (Throwable) e2);
            }
        }
    }

    public void a() {
        this.f6432a = true;
    }
}
