package aU;

import G.R;
import G.aM;
import V.g;
import V.j;
import W.E;
import java.io.File;
import r.C1807j;

/* loaded from: TunerStudioMS.jar:aU/c.class */
public class c implements d {
    @Override // aU.d
    public void a(R r2, int i2, int i3) throws V.a {
        new E().a(r2.F() + File.separatorChar + C1807j.f13472h, i2, i3);
        String strB = r2.G().b();
        String strC = r2.G().c();
        aM aMVarC = r2.c(strB);
        aM aMVarC2 = r2.c(strC);
        if (aMVarC != null) {
            try {
                aMVarC.a(r2.p(), i2);
            } catch (g e2) {
                throw new V.a("Error setting throttle ADC values to controller, file saved fine.\n" + e2.getMessage());
            } catch (j e3) {
                throw new V.a("Error setting throttle ADC values to controller, file saved fine.\n" + e3.getMessage());
            }
        }
        if (aMVarC2 != null) {
            aMVarC2.a(r2.p(), i3);
        }
    }
}
