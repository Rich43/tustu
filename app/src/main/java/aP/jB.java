package aP;

import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: TunerStudioMS.jar:aP/jB.class */
class jB implements G.aN {

    /* renamed from: a, reason: collision with root package name */
    final /* synthetic */ jv f3768a;

    jB(jv jvVar) {
        this.f3768a = jvVar;
    }

    @Override // G.aN
    public void a(String str, String str2) throws SecurityException {
        G.R rC = G.T.a().c(str);
        if (rC != null) {
            try {
                rC.O().r((int) rC.c("tsCanId").j(rC.p()));
            } catch (V.g e2) {
                Logger.getLogger(jv.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            }
        }
    }
}
