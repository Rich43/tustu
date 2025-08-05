package aP;

import r.C1798a;

/* renamed from: aP.dm, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:aP/dm.class */
class C0298dm extends Thread {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ C0293dh f3239a;

    C0298dm(C0293dh c0293dh) {
        this.f3239a = c0293dh;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() throws IllegalArgumentException {
        iE iEVarE = cZ.a().e();
        for (int i2 = 0; i2 < 5; i2++) {
            if (iEVarE.a().equals("")) {
                iEVarE.a(C1798a.f13268b + " Ready in Lite mode. Register today for the most powerful tuning solution available!");
            }
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e2) {
            }
        }
        iEVarE.dispose();
        cZ.a().a((iE) null);
    }
}
