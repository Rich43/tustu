package bU;

import bH.C0995c;
import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: TunerStudioMS.jar:bU/s.class */
public class s implements bT.a {
    @Override // bT.a
    public int a() {
        return 224;
    }

    @Override // bT.a
    public void a(bT.o oVar, bN.t tVar) throws bT.h {
        if (tVar.a() != a()) {
            throw new bT.h("PID:DAQ_SET_DAQ_LIST_MODE Wrong handler! this handler is for 0x" + Integer.toHexString(a()).toUpperCase());
        }
        bN.k kVarD = oVar.d();
        oVar.f();
        byte[] bArrC = tVar.c();
        if (bArrC == null) {
            throw new bT.h("PID:DAQ_SET_DAQ_LIST_MODE valid packet data not found.");
        }
        if (bArrC.length != 7) {
            throw new bT.h("PID:DAQ_SET_DAQ_LIST_MODE invalid packet data length, expected 8 bytes, found: " + (bArrC.length + 1));
        }
        try {
            bN.l lVarB = bN.u.a().b();
            bT.d dVarK = oVar.k();
            byte b2 = bArrC[0];
            int iA = C0995c.a(bArrC, 1, 2, kVarD.g(), false);
            int iA2 = C0995c.a(bArrC, 3, 2, kVarD.g(), false);
            int iA3 = C0995c.a(bArrC[5]);
            int iA4 = C0995c.a(bArrC[6]);
            bT.b bVarC = dVarK.c(iA);
            if (bVarC == null || iA > dVarK.b()) {
                lVarB.a(254);
                lVarB.b(new byte[]{34});
                oVar.a(tVar);
                return;
            }
            lVarB.a(255);
            dVarK.d(iA);
            bO.c cVarA = bVarC.a();
            cVarA.a(b2);
            cVarA.e(iA2);
            cVarA.f(iA3);
            cVarA.g(iA4);
            lVarB.b(new byte[0]);
            oVar.a(lVarB);
        } catch (bN.o e2) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            bN.l lVarB2 = bN.u.a().b();
            lVarB2.a(254);
            lVarB2.b(new byte[]{34});
            try {
                oVar.a(lVarB2);
            } catch (bN.o e3) {
                bH.C.a("Unable to send response packet.");
                Logger.getLogger(l.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e3);
            }
        }
    }
}
