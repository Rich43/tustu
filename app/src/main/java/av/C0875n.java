package av;

import java.util.logging.Level;
import java.util.logging.Logger;

/* renamed from: av.n, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:av/n.class */
class C0875n extends Thread {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C0873l f6305a;

    C0875n(C0873l c0873l) {
        this.f6305a = c0873l;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        try {
            Thread.sleep(250L);
        } catch (InterruptedException e2) {
            Logger.getLogger(C0873l.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
        }
        this.f6305a.f6284b.h().g();
    }
}
