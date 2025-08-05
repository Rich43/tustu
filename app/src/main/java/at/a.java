package aT;

import G.R;
import G.T;
import G.bS;
import aP.iC;
import bH.C;

/* loaded from: TunerStudioMS.jar:aT/a.class */
public class a extends iC {
    @Override // aP.iC, G.cU
    public boolean a(String str, String str2, bS bSVar) {
        int i2 = 0;
        try {
            if (str2.length() == 2 || str2.length() == 3) {
                i2 = Integer.parseInt(str2);
            }
        } catch (Exception e2) {
        }
        if (bSVar.a().length != 1 || bSVar.a()[0] > 98 || str2.length() != 2 || i2 > 98) {
            return super.a(str, str2, bSVar);
        }
        R rC = T.a().c(str);
        if (rC == null) {
            return true;
        }
        rC.e(bSVar.b());
        C.d("Updated Config Signature to match Controller.");
        return true;
    }
}
