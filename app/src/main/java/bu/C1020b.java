package bU;

import G.C0113cs;
import bH.C0995c;
import java.util.logging.Level;
import java.util.logging.Logger;

/* renamed from: bU.b, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bU/b.class */
public class C1020b implements bT.a {
    @Override // bT.a
    public int a() {
        return 227;
    }

    @Override // bT.a
    public void a(bT.o oVar, bN.t tVar) throws bT.h {
        if (tVar.a() != a()) {
            throw new bT.h("PID:DAQ_SET_DAQ_PTR Wrong handler! this handler is for 0x" + Integer.toHexString(a()).toUpperCase());
        }
        bN.k kVarD = oVar.d();
        byte[] bArrC = tVar.c();
        if (bArrC == null) {
            throw new bT.h("PID:DAQ_SET_DAQ_PTR valid packet data not found.");
        }
        if (bArrC.length != 3) {
            throw new bT.h("PID:DAQ_SET_DAQ_PTR invalid packet data length, expected 4 bytes, found: " + (bArrC.length + 1));
        }
        try {
            bN.l lVarB = bN.u.a().b();
            bT.d dVarK = oVar.k();
            int iA = C0995c.a(bArrC, 1, 2, kVarD.g(), false);
            bO.c cVarB = dVarK.b(iA);
            if (cVarB == null || iA > dVarK.b()) {
                lVarB.a(254);
                lVarB.b(new byte[]{34});
                oVar.a(tVar);
            } else {
                lVarB.a(255);
                C0113cs.a().a(cVarB.n());
                dVarK.i(iA);
                dVarK.a(iA);
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
