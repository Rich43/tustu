package I;

import G.C0113cs;
import G.aG;
import G.bS;

/* loaded from: TunerStudioMS.jar:I/g.class */
public class g implements aG {
    @Override // G.aG
    public boolean a(String str, bS bSVar) {
        C0113cs.a().a("controllerOnline", 1.0d);
        return true;
    }

    @Override // G.aG
    public void a(String str) {
        C0113cs.a().a("controllerOnline", 0.0d);
    }
}
