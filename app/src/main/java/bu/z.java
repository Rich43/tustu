package bU;

import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: TunerStudioMS.jar:bU/z.class */
public class z implements bT.a {
    @Override // bT.a
    public void a(bT.o oVar, bN.t tVar) throws bT.h {
        if (tVar.a() != a()) {
            throw new bT.h("PID:SYNC Wrong handler! this handler is for 0x" + Integer.toHexString(a()).toUpperCase());
        }
        try {
            bN.l lVarB = bN.u.a().b();
            lVarB.a(254);
            lVarB.b(new byte[]{0});
            oVar.a(lVarB);
        } catch (bN.o e2) {
            Logger.getLogger(z.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            throw new bT.h(e2.getLocalizedMessage());
        }
    }

    @Override // bT.a
    public int a() {
        return 252;
    }
}
