package H;

import G.T;
import G.aB;
import G.aD;
import G.aH;
import V.g;
import bH.C;

/* loaded from: TunerStudioMS.jar:H/b.class */
public class b implements aD {
    @Override // G.aD
    public boolean a(String str, byte[] bArr) {
        aH aHVarG = T.a().c(str).g("Vbatt");
        if (aHVarG == null) {
            return true;
        }
        try {
            if (aHVarG.b(bArr) >= 4.0d) {
                return true;
            }
            aB.a().a("Key Turned off, disconnecting");
            return false;
        } catch (g e2) {
            C.a("Unable to get value for Vbatt OutputChannel.");
            return true;
        }
    }
}
