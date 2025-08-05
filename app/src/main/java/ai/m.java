package aI;

import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: TunerStudioMS.jar:aI/m.class */
class m extends Thread {

    /* renamed from: a, reason: collision with root package name */
    long f2478a;

    /* renamed from: b, reason: collision with root package name */
    boolean f2479b;

    /* renamed from: c, reason: collision with root package name */
    int f2480c;

    /* renamed from: d, reason: collision with root package name */
    long f2481d;

    /* renamed from: e, reason: collision with root package name */
    final /* synthetic */ l f2482e;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public m(l lVar) {
        super("MS3SD Refresh Monitor");
        this.f2482e = lVar;
        this.f2478a = Long.MAX_VALUE;
        this.f2479b = true;
        this.f2480c = 400;
        this.f2481d = Long.MAX_VALUE;
        setDaemon(true);
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        while (this.f2479b) {
            if (this.f2482e.f2472g != null) {
                if (this.f2482e.f2472g.c() && this.f2481d > System.currentTimeMillis()) {
                    this.f2481d = System.currentTimeMillis();
                } else if (!this.f2482e.f2472g.c()) {
                    this.f2481d = Long.MAX_VALUE;
                }
                if (System.currentTimeMillis() - this.f2481d > 100 && this.f2478a < System.currentTimeMillis()) {
                    this.f2478a = Long.MAX_VALUE;
                    this.f2482e.h();
                }
            }
            try {
                Thread.sleep(40L);
            } catch (InterruptedException e2) {
                Logger.getLogger(l.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            }
        }
    }

    public void a() {
        this.f2478a = this.f2480c + System.currentTimeMillis();
    }

    public void b() {
        this.f2478a = Long.MAX_VALUE;
    }
}
