package bU;

import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: TunerStudioMS.jar:bU/w.class */
public class w implements bT.a {
    @Override // bT.a
    public int a() {
        return 230;
    }

    @Override // bT.a
    public void a(bT.o oVar, bN.t tVar) throws bT.h {
        if (tVar.a() != a()) {
            throw new bT.h("PID:STD_SET_SETMENT_MODE Wrong handler! this handler is for 0x" + Integer.toHexString(a()).toUpperCase());
        }
        oVar.d();
        byte[] bArrC = tVar.c();
        if (bArrC == null) {
            throw new bT.h("PID:STD_SET_SETMENT_MODE valid packet data not found.");
        }
        if (bArrC.length != 2) {
            throw new bT.h("PID:STD_SET_SETMENT_MODE invalid packet data length, expected 3 bytes, found: " + (bArrC.length + 1));
        }
        bN.l lVarB = bN.u.a().b();
        lVarB.a(255);
        lVarB.b(new byte[0]);
        try {
            oVar.a(lVarB);
        } catch (bN.o e2) {
            Logger.getLogger(u.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            throw new bT.h(e2.getLocalizedMessage());
        }
    }
}
