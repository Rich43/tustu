package ay;

import bH.C;
import java.util.logging.Level;
import java.util.logging.Logger;

/* renamed from: ay.g, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ay/g.class */
class C0930g extends Thread {

    /* renamed from: a, reason: collision with root package name */
    boolean f6448a = false;

    /* renamed from: b, reason: collision with root package name */
    boolean f6449b = true;

    /* renamed from: c, reason: collision with root package name */
    final /* synthetic */ C0929f f6450c;

    C0930g(C0929f c0929f) {
        this.f6450c = c0929f;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public synchronized void run() {
        while (!this.f6448a) {
            boolean z2 = false;
            if (this.f6449b) {
                try {
                    this.f6450c.e();
                    this.f6449b = false;
                    this.f6450c.d();
                    z2 = false;
                } catch (Exception e2) {
                    if (C0929f.f6447d) {
                        e2.printStackTrace();
                        C0929f.f6447d = false;
                    }
                    try {
                        Thread.sleep(250L);
                    } catch (InterruptedException e3) {
                        Logger.getLogger(C0929f.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e3);
                    }
                    if (!z2) {
                        C.c("HttpService Search Failed");
                    }
                }
            } else {
                try {
                    wait();
                } catch (InterruptedException e4) {
                    Logger.getLogger(C0929f.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e4);
                }
            }
        }
    }

    public synchronized void a() {
        this.f6449b = true;
        notify();
    }
}
