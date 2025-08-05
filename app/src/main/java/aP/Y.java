package aP;

import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: TunerStudioMS.jar:aP/Y.class */
public class Y implements G.aN {

    /* renamed from: a, reason: collision with root package name */
    aE.a f2788a;

    public Y(aE.a aVar) {
        this.f2788a = null;
        this.f2788a = aVar;
    }

    @Override // G.aN
    public void a(String str, String str2) {
        G.R rC = G.T.a().c(str);
        if (rC == null) {
            bH.C.b("Unable to update CAN ID, no EcuConfiguration found for " + str);
            return;
        }
        G.aM aMVarC = rC.c(str2);
        try {
            aE.d dVarT = this.f2788a.t(str);
            int iJ = (int) aMVarC.j(rC.p());
            if (dVarT != null) {
                dVarT.a(iJ);
            } else {
                this.f2788a.a(iJ);
            }
        } catch (V.g e2) {
            Logger.getLogger(Y.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
        }
    }
}
