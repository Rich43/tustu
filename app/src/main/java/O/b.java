package O;

import G.bS;
import G.cV;
import bH.C;
import bH.C0995c;

/* loaded from: TunerStudioMS.jar:O/b.class */
public class b implements cV {
    @Override // G.cV
    public boolean a(bS bSVar) {
        if (bSVar != null && bSVar.b() != null && (bSVar.b().startsWith("BigStuff Gen4") || bSVar.b().startsWith("BigStuff Rim"))) {
            return true;
        }
        if (bSVar == null || bSVar.a() == null) {
            return false;
        }
        C.d("Invalid BigStuff4 signature: " + C0995c.d(bSVar.a()));
        return false;
    }
}
