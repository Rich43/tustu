package G;

import java.io.IOException;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

/* renamed from: G.av, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:G/av.class */
class C0062av extends Thread implements Serializable {

    /* renamed from: a, reason: collision with root package name */
    long f808a;

    /* renamed from: b, reason: collision with root package name */
    boolean f809b;

    /* renamed from: c, reason: collision with root package name */
    long f810c;

    /* renamed from: d, reason: collision with root package name */
    int f811d;

    /* renamed from: e, reason: collision with root package name */
    final C0066az f812e;

    /* renamed from: f, reason: collision with root package name */
    final /* synthetic */ C0054an f813f;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public C0062av(C0054an c0054an) {
        super("DataUpdateThread");
        this.f813f = c0054an;
        this.f808a = 70L;
        this.f809b = true;
        this.f810c = 0L;
        this.f811d = 250;
        this.f812e = new C0066az(this.f813f);
        setDaemon(true);
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() throws IOException {
        while (this.f809b) {
            try {
                synchronized (this.f812e) {
                    this.f812e.wait(this.f808a);
                }
            } catch (InterruptedException e2) {
                e2.printStackTrace();
            }
            if (System.currentTimeMillis() - this.f810c > this.f811d) {
                this.f813f.c();
                synchronized (this.f812e) {
                    this.f812e.notify();
                }
            }
        }
    }

    public void a() {
        synchronized (this.f812e) {
            this.f810c = 0L;
            this.f812e.notify();
        }
        try {
            bH.C.d("dirtyData.size(): " + this.f813f.f788g.size());
            synchronized (this.f812e) {
                this.f812e.wait();
            }
        } catch (InterruptedException e2) {
            Logger.getLogger(C0054an.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
        }
        bH.C.d("dirtyData.size(): " + this.f813f.f788g.size());
    }

    public void b() {
        this.f810c = System.currentTimeMillis();
    }

    public void c() {
        this.f809b = false;
    }
}
