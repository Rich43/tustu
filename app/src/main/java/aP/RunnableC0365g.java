package aP;

import java.awt.Window;

/* renamed from: aP.g, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:aP/g.class */
class RunnableC0365g implements Runnable {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C0338f f3397a;

    RunnableC0365g(C0338f c0338f) {
        this.f3397a = c0338f;
    }

    @Override // java.lang.Runnable
    public void run() {
        aE.a aVarA = aE.a.A();
        if (aVarA == null) {
            return;
        }
        String strT = aVarA.t();
        C0338f.a().g();
        C0338f.a().a((Window) cZ.a().c(), strT);
    }
}
