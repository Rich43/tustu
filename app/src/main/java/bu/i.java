package bU;

import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: TunerStudioMS.jar:bU/i.class */
public class i implements bT.a {
    @Override // bT.a
    public int a() {
        return 234;
    }

    @Override // bT.a
    public void a(bT.o oVar, bN.t tVar) throws bT.h {
        if (tVar.a() != a()) {
            throw new bT.h("PID:PAG_GET_CAL_PAGE Wrong handler! this handler is for 0x" + Integer.toHexString(a()).toUpperCase());
        }
        oVar.d();
        byte[] bArrC = tVar.c();
        if (bArrC == null) {
            throw new bT.h("PID:PAG_GET_CAL_PAGE valid packet data not found.");
        }
        if (bArrC.length != 2) {
            throw new bT.h("PID:PAG_GET_CAL_PAGE invalid packet data length, expected 3 bytes, found: " + (bArrC.length + 1));
        }
        byte b2 = bArrC[0];
        byte b3 = bArrC[1];
        try {
            if (b2 == 1 || b2 == 2) {
                bN.l lVarB = bN.u.a().b();
                lVarB.a(255);
                lVarB.b(new byte[]{0, 0, (byte) oVar.g()});
                oVar.a(lVarB);
            } else {
                bN.l lVarB2 = bN.u.a().b();
                lVarB2.a(254);
                lVarB2.b(new byte[]{39});
                oVar.c(39);
                oVar.a(lVarB2);
            }
        } catch (bN.o e2) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            throw new bT.h(e2.getLocalizedMessage());
        }
    }
}
