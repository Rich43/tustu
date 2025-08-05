package aU;

import G.R;
import G.aM;
import V.g;
import V.j;
import javax.swing.SwingUtilities;

/* loaded from: TunerStudioMS.jar:aU/a.class */
public class a implements d {
    @Override // aU.d
    public void a(R r2, int i2, int i3) throws V.a {
        String strB = r2.G().b();
        String strC = r2.G().c();
        aM aMVarC = r2.c(strB);
        aM aMVarC2 = r2.c(strC);
        if (!r2.C().q()) {
        }
        try {
            aMVarC.a(r2.p(), i2);
            aMVarC2.a(r2.p(), i3);
            SwingUtilities.invokeLater(new b(this, r2));
        } catch (g e2) {
            e2.printStackTrace();
            throw new V.a("Unable to set TPS Parameter:\n" + e2.getMessage());
        } catch (j e3) {
            e3.printStackTrace();
            throw new V.a("Unable to set TPS Parameter:\n" + e3.getMessage());
        }
    }
}
