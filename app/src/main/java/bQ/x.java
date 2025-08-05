package bQ;

import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: TunerStudioMS.jar:bQ/x.class */
class x extends Thread {

    /* renamed from: a, reason: collision with root package name */
    long f7490a;

    /* renamed from: b, reason: collision with root package name */
    int f7491b;

    /* renamed from: c, reason: collision with root package name */
    final /* synthetic */ u f7492c;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public x(u uVar) {
        super("Master DAQ List Update");
        this.f7492c = uVar;
        this.f7490a = -1L;
        this.f7491b = 100;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public synchronized void run() {
        while (true) {
            try {
                wait(10000L);
            } catch (InterruptedException e2) {
                Logger.getLogger(u.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            }
            if (this.f7490a > 0) {
                while (System.currentTimeMillis() < this.f7490a + this.f7491b) {
                    try {
                        wait(this.f7491b);
                    } catch (InterruptedException e3) {
                        Logger.getLogger(u.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e3);
                    }
                }
                if (this.f7490a + this.f7491b <= System.currentTimeMillis()) {
                    try {
                        this.f7492c.f();
                    } catch (Exception e4) {
                        e4.printStackTrace();
                    }
                    this.f7490a = -1L;
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void a() {
        this.f7490a = System.currentTimeMillis();
        notify();
    }
}
