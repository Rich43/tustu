package bH;

import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: TunerStudioMS.jar:bH/P.class */
class P extends Thread {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ O f7016a;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    P(O o2) {
        super("INI Check");
        this.f7016a = o2;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        boolean zB = false;
        while (!zB) {
            zB = this.f7016a.b();
            try {
                Thread.sleep(60000L);
            } catch (InterruptedException e2) {
                Logger.getLogger(O.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            }
        }
    }
}
