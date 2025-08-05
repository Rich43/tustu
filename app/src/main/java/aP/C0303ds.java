package aP;

import java.util.logging.Level;
import java.util.logging.Logger;

/* renamed from: aP.ds, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:aP/ds.class */
class C0303ds extends Thread {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C0293dh f3247a;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    C0303ds(C0293dh c0293dh) {
        super("MainFrameDelayValidate");
        this.f3247a = c0293dh;
        setDaemon(true);
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        try {
            Thread.sleep(400L);
        } catch (InterruptedException e2) {
            Logger.getLogger(C0293dh.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
        }
        this.f3247a.validate();
        this.f3247a.doLayout();
    }
}
