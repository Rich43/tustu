package O;

import G.bS;
import G.cV;

/* loaded from: TunerStudioMS.jar:O/j.class */
public class j implements cV {
    @Override // G.cV
    public boolean a(bS bSVar) {
        return !(bSVar == null || bSVar.b() == null || !bSVar.b().toLowerCase().startsWith("monsterfirmware")) || bSVar.b().toLowerCase().startsWith("monsterpwmod");
    }
}
