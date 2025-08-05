package bU;

import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: TunerStudioMS.jar:bU/r.class */
public class r implements bT.a {
    @Override // bT.a
    public int a() {
        return 235;
    }

    @Override // bT.a
    public void a(bT.o oVar, bN.t tVar) throws bT.h {
        if (tVar.a() != a()) {
            throw new bT.h("PID:PAG_SET_CAL_PAGE Wrong handler! this handler is for 0x" + Integer.toHexString(a()).toUpperCase());
        }
        byte[] bArrC = tVar.c();
        if (bArrC == null) {
            throw new bT.h("PID:PAG_SET_CAL_PAGE valid packet data not found.");
        }
        if (bArrC.length != 3) {
            throw new bT.h("PID:PAG_SET_CAL_PAGE invalid packet data length, expected 4 bytes, found: " + (bArrC.length + 1));
        }
        byte b2 = bArrC[0];
        byte b3 = bArrC[1];
        byte b4 = bArrC[2];
        if (b4 >= 0 && b4 < oVar.f().O().g()) {
            oVar.j().a(b2);
            oVar.b(b4);
            bN.l lVarB = bN.u.a().b();
            lVarB.a(255);
            lVarB.b(new byte[0]);
            try {
                oVar.a(lVarB);
                return;
            } catch (bN.o e2) {
                Logger.getLogger(r.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
                throw new bT.h(e2.getLocalizedMessage());
            }
        }
        bN.l lVarB2 = bN.u.a().b();
        lVarB2.a(254);
        byte[] bArr = {38};
        lVarB2.b(bArr);
        oVar.c(38);
        lVarB2.b(bArr);
        try {
            oVar.a(lVarB2);
        } catch (bN.o e3) {
            Logger.getLogger(r.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e3);
            throw new bT.h(e3.getLocalizedMessage());
        }
    }
}
