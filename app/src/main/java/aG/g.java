package aG;

import aI.l;
import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: TunerStudioMS.jar:aG/g.class */
class g extends Thread {

    /* renamed from: a, reason: collision with root package name */
    long f2403a;

    /* renamed from: b, reason: collision with root package name */
    boolean f2404b;

    /* renamed from: c, reason: collision with root package name */
    int f2405c;

    /* renamed from: d, reason: collision with root package name */
    final /* synthetic */ e f2406d;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public g(e eVar) {
        super("FTD SD Refresh Monitor");
        this.f2406d = eVar;
        this.f2403a = Long.MAX_VALUE;
        this.f2404b = true;
        this.f2405c = 100;
        setDaemon(true);
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        while (this.f2404b) {
            if (this.f2403a < System.currentTimeMillis()) {
                this.f2403a = Long.MAX_VALUE;
                this.f2406d.g();
            }
            try {
                Thread.sleep(40L);
            } catch (InterruptedException e2) {
                Logger.getLogger(l.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            }
        }
    }

    public void a() {
        this.f2403a = this.f2405c + System.currentTimeMillis();
    }

    public void b() {
        this.f2403a = Long.MAX_VALUE;
    }
}
