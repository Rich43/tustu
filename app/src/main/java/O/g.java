package O;

import G.bS;
import G.cV;

/* loaded from: TunerStudioMS.jar:O/g.class */
public class g implements cV {
    @Override // G.cV
    public boolean a(bS bSVar) {
        return (bSVar == null || bSVar.b() == null || !bSVar.b().startsWith("Ditron")) ? false : true;
    }
}
