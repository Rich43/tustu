package bP;

import G.C0130m;
import G.R;
import G.T;
import G.aM;
import G.cC;
import G.cG;
import java.io.IOException;

/* loaded from: TunerStudioMS.jar:bP/a.class */
public class a implements cG {
    @Override // G.cG
    public void a(String str, String str2) throws IOException {
        b(str, str2);
    }

    private void b(String str, String str2) throws IOException {
        aM aMVarC;
        R rC = T.a().c(str);
        if (rC == null || (aMVarC = rC.c(str2)) == null) {
            return;
        }
        rC.C().b(C0130m.a(rC.O(), aMVarC.aJ(), cC.a(rC, aMVarC)));
    }
}
