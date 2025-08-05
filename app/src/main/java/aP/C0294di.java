package aP;

import java.util.logging.Level;
import java.util.logging.Logger;

/* renamed from: aP.di, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:aP/di.class */
class C0294di extends Thread {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C0293dh f3234a;

    C0294di(C0293dh c0293dh) {
        this.f3234a = c0293dh;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        try {
            sleep(3000L);
        } catch (InterruptedException e2) {
            Logger.getLogger(C0293dh.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
        }
        this.f3234a.j();
    }
}
