package br;

import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: TunerStudioMS.jar:br/O.class */
class O extends Thread {

    /* renamed from: a, reason: collision with root package name */
    boolean f8372a = true;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ C1233K f8373b;

    O(C1233K c1233k) {
        this.f8373b = c1233k;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        while (this.f8372a) {
            this.f8373b.repaint();
            try {
                sleep(30L);
            } catch (InterruptedException e2) {
                Logger.getLogger(C1233K.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            }
        }
    }

    public void a() {
        this.f8372a = false;
    }
}
