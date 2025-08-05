package defpackage;

import aE.a;
import aP.C0338f;
import java.util.logging.Level;
import java.util.logging.Logger;
import r.C1798a;

/* loaded from: TunerStudioMS.jar:i.class */
class i extends Thread {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ h f12343a;

    i(h hVar) {
        this.f12343a = hVar;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        super.setName(getName() + " - TS Shutdown Hook");
        if (!C1798a.a().q()) {
            if (a.A() != null) {
                try {
                    a.A().b();
                } catch (V.a e2) {
                    Logger.getLogger(TunerStudio.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
                }
            }
            C0338f.a().g();
        }
        try {
            C1798a.a().e();
            h.i.g();
        } catch (V.a e3) {
            Logger.getLogger(TunerStudio.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e3);
        }
        Runtime.getRuntime().halt(0);
    }
}
