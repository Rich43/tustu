package O;

import G.bS;
import G.cV;

/* loaded from: TunerStudioMS.jar:O/k.class */
public class k implements cV {
    @Override // G.cV
    public boolean a(bS bSVar) {
        return (bSVar == null || bSVar.b() == null || !bSVar.b().startsWith("Xenomorph")) ? false : true;
    }
}
