package aP;

import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: TunerStudioMS.jar:aP/iL.class */
class iL implements iQ {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ iK f3666a;

    iL(iK iKVar) {
        this.f3666a = iKVar;
    }

    @Override // aP.iQ
    public void a() {
        try {
            this.f3666a.d();
        } catch (V.a e2) {
            Logger.getLogger(iK.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
        }
    }
}
