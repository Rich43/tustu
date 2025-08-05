package bU;

import bH.C0995c;
import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: TunerStudioMS.jar:bU/u.class */
public class u implements bT.a {
    @Override // bT.a
    public int a() {
        return 246;
    }

    @Override // bT.a
    public void a(bT.o oVar, bN.t tVar) throws bT.h {
        if (tVar.a() != a()) {
            throw new bT.h("PID:STD_SET_MTA Wrong handler! this handler is for 0x" + Integer.toHexString(a()).toUpperCase());
        }
        bN.k kVarD = oVar.d();
        byte[] bArrC = tVar.c();
        if (bArrC == null) {
            throw new bT.h("PID:STD_SET_MTA valid packet data not found.");
        }
        if (bArrC.length != 7) {
            throw new bT.h("PID:STD_SET_MTA invalid packet data length, expected 8 bytes, found: " + (bArrC.length + 1));
        }
        oVar.d(C0995c.a(bArrC, 3, 4, kVarD.g(), false));
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
