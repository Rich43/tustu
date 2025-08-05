package av;

import java.util.logging.Level;
import java.util.logging.Logger;

/* renamed from: av.e, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:av/e.class */
class C0866e extends Thread {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C0864c f6282a;

    C0866e(C0864c c0864c) {
        this.f6282a = c0864c;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        try {
            Thread.sleep(250L);
        } catch (InterruptedException e2) {
            Logger.getLogger(C0864c.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
        }
        this.f6282a.f6284b.h().g();
    }
}
