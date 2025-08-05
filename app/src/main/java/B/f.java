package B;

import bH.C;
import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: TunerStudioMS.jar:B/f.class */
class f extends Thread {

    /* renamed from: a, reason: collision with root package name */
    boolean f142a = false;

    /* renamed from: b, reason: collision with root package name */
    boolean f143b = true;

    /* renamed from: c, reason: collision with root package name */
    final /* synthetic */ e f144c;

    f(e eVar) {
        this.f144c = eVar;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public synchronized void run() {
        while (!this.f142a) {
            if (this.f143b) {
                try {
                    this.f144c.d();
                    this.f143b = false;
                    this.f144c.c();
                } catch (Exception e2) {
                    C.c("Device Search Failed");
                }
            } else {
                try {
                    wait();
                } catch (InterruptedException e3) {
                    Logger.getLogger(e.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e3);
                }
            }
        }
    }

    public synchronized void a() {
        this.f143b = true;
        notify();
    }
}
