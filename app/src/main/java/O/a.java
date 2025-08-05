package O;

import G.bS;
import G.cV;

/* loaded from: TunerStudioMS.jar:O/a.class */
public class a implements cV {
    @Override // G.cV
    public boolean a(bS bSVar) {
        return (bSVar == null || bSVar.b() == null || bSVar.b().length() <= 4 || bSVar.b().isEmpty() || a(bSVar.a())) ? false : true;
    }

    private boolean a(byte[] bArr) {
        for (byte b2 : bArr) {
            if (b2 == -1) {
                return true;
            }
        }
        return false;
    }
}
