package O;

import G.bS;
import G.cV;
import bH.C;
import bH.C0995c;

/* loaded from: TunerStudioMS.jar:O/c.class */
public class c implements cV {
    @Override // G.cV
    public boolean a(bS bSVar) {
        if (bSVar == null || bSVar.a() == null || bSVar.a().length != 1 || bSVar.a()[0] == 20) {
            return false;
        }
        C.d("Valid BigStuff3 signature: " + C0995c.d(bSVar.a()));
        return true;
    }
}
