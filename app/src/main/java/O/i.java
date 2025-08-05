package O;

import G.bS;
import G.cV;

/* loaded from: TunerStudioMS.jar:O/i.class */
public class i implements cV {
    @Override // G.cV
    public boolean a(bS bSVar) {
        return (bSVar == null || bSVar.b() == null || !bSVar.b().startsWith("O5E Format")) ? false : true;
    }
}
