package aP;

import G.C0129l;
import bb.C1049n;
import java.awt.Window;
import java.util.logging.Level;
import java.util.logging.Logger;

/* renamed from: aP.n, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:aP/n.class */
class RunnableC0458n implements Runnable {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ Window f3828a;

    /* renamed from: b, reason: collision with root package name */
    final /* synthetic */ C0338f f3829b;

    RunnableC0458n(C0338f c0338f, Window window) {
        this.f3829b = c0338f;
        this.f3828a = window;
    }

    @Override // java.lang.Runnable
    public void run() {
        boolean z2 = false;
        G.R rC = G.T.a().c();
        if (rC != null && rC.R()) {
            z2 = true;
            rC.C().c();
        }
        new C1049n().a(this.f3828a);
        if (rC == null || !z2 || G.T.a().c() == null || !G.T.a().c().equals(rC)) {
            return;
        }
        try {
            rC.C().d();
        } catch (C0129l e2) {
            Logger.getLogger(C0338f.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
        }
    }
}
