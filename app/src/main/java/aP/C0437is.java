package aP;

import java.util.logging.Level;
import java.util.logging.Logger;

/* renamed from: aP.is, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:aP/is.class */
class C0437is extends Thread {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C0436ir f3751a;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    C0437is(C0436ir c0436ir) {
        super("SendBlockCleanup");
        this.f3751a = c0436ir;
        setDaemon(true);
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        while (this.f3751a.f3746a.C().x()) {
            try {
                Thread.sleep(30L);
            } catch (InterruptedException e2) {
                Logger.getLogger(C0436ir.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            }
        }
        try {
            Thread.sleep(100L);
        } catch (InterruptedException e3) {
            Logger.getLogger(C0436ir.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e3);
        }
        this.f3751a.c();
    }
}
