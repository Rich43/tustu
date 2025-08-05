package bh;

import ao.C0804hg;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;

/* renamed from: bh.g, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bh/g.class */
class C1147g extends Thread {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ Runnable f8109a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ C1142b f8110b;

    C1147g(C1142b c1142b, Runnable runnable) {
        this.f8110b = c1142b;
        this.f8109a = runnable;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        SwingUtilities.invokeLater(this.f8109a);
        try {
            sleep(850L);
        } catch (InterruptedException e2) {
            Logger.getLogger(C1142b.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
        }
        if (C0804hg.a().r() != null) {
            C0804hg.a().a(1.0d, false);
            C0804hg.a().c(C0804hg.a().r().d() - 1);
        }
        if (h.i.a(h.i.f12300U, h.i.f12301V)) {
            this.f8110b.y();
        }
    }
}
