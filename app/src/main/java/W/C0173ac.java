package W;

import java.util.logging.Level;
import java.util.logging.Logger;

/* renamed from: W.ac, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:W/ac.class */
class C0173ac extends Thread {

    /* renamed from: a, reason: collision with root package name */
    boolean f2062a = true;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ C0172ab f2063b;

    C0173ac(C0172ab c0172ab) {
        this.f2063b = c0172ab;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        while (this.f2062a) {
            this.f2062a = false;
            try {
                Thread.sleep(3000L);
            } catch (InterruptedException e2) {
                Logger.getLogger(C0172ab.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            }
            C0173ac unused = C0172ab.f2060n = null;
            C0172ab.f2061o.clear();
        }
    }

    public void a() {
        this.f2062a = true;
    }
}
