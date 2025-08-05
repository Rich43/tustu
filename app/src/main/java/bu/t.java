package bU;

import bH.C0995c;
import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: TunerStudioMS.jar:bU/t.class */
public class t implements bT.a {
    @Override // bT.a
    public int a() {
        return 226;
    }

    @Override // bT.a
    public void a(bT.o oVar, bN.t tVar) throws bT.h {
        if (tVar.a() != a()) {
            throw new bT.h("PID:DAQ_SET_DAQ_PTR Wrong handler! this handler is for 0x" + Integer.toHexString(a()).toUpperCase());
        }
        bN.k kVarD = oVar.d();
        oVar.f();
        byte[] bArrC = tVar.c();
        if (bArrC == null) {
            throw new bT.h("PID:DAQ_SET_DAQ_PTR valid packet data not found.");
        }
        if (bArrC.length != 5) {
            throw new bT.h("PID:DAQ_SET_DAQ_PTR invalid packet data length, expected 6 bytes, found: " + (bArrC.length + 1));
        }
        try {
            bN.l lVarB = bN.u.a().b();
            bT.d dVarK = oVar.k();
            int iA = C0995c.a(bArrC, 1, 2, kVarD.g(), false);
            int iA2 = C0995c.a(bArrC[3]);
            int iA3 = C0995c.a(bArrC[4]);
            bO.c cVarB = dVarK.b(iA);
            if (cVarB == null || iA > dVarK.b() || iA2 > cVarB.a() || iA3 > cVarB.c()) {
                lVarB.a(254);
                lVarB.b(new byte[]{34});
                oVar.a(tVar);
            } else {
                lVarB.a(255);
                dVarK.d(iA);
                dVarK.e(iA2);
                dVarK.f(iA3);
                lVarB.b(new byte[0]);
                oVar.a(lVarB);
            }
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
