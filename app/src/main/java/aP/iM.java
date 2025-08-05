package aP;

import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: TunerStudioMS.jar:aP/iM.class */
class iM implements iQ {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ iK f3667a;

    iM(iK iKVar) {
        this.f3667a = iKVar;
    }

    @Override // aP.iQ
    public void a() {
        try {
            this.f3667a.f();
        } catch (V.a e2) {
            Logger.getLogger(iK.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
        }
    }
}
