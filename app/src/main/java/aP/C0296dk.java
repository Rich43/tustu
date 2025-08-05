package aP;

import java.util.logging.Level;
import java.util.logging.Logger;

/* renamed from: aP.dk, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:aP/dk.class */
class C0296dk extends Thread {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ az.o f3236a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ C0293dh f3237b;

    C0296dk(C0293dh c0293dh, az.o oVar) {
        this.f3237b = c0293dh;
        this.f3236a = oVar;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        try {
            Thread.sleep(20000L);
        } catch (InterruptedException e2) {
            Logger.getLogger(C0293dh.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
        }
        this.f3236a.f();
    }
}
