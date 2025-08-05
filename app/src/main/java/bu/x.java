package bU;

import bH.C0995c;
import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: TunerStudioMS.jar:bU/x.class */
public class x implements bT.a {
    @Override // bT.a
    public int a() {
        return 222;
    }

    @Override // bT.a
    public void a(bT.o oVar, bN.t tVar) throws bT.h {
        if (tVar.a() != a()) {
            throw new bT.h("PID:DAQ_START_STOP_DAQ_LIST Wrong handler! this handler is for 0x" + Integer.toHexString(a()).toUpperCase());
        }
        bN.k kVarD = oVar.d();
        oVar.f();
        byte[] bArrC = tVar.c();
        if (bArrC == null) {
            throw new bT.h("PID:DAQ_START_STOP_DAQ_LIST valid packet data not found.");
        }
        if (bArrC.length != 3) {
            throw new bT.h("PID:DAQ_START_STOP_DAQ_LIST invalid packet data length, expected 4 bytes, found: " + (bArrC.length + 1));
        }
        try {
            bN.l lVarB = bN.u.a().b();
            byte b2 = bArrC[0];
            int iA = C0995c.a(bArrC, 1, 2, kVarD.g(), false);
            bT.d dVarK = oVar.k();
            if (b2 > 2 || b2 < 0) {
                lVarB.a(254);
                lVarB.b(new byte[]{39});
                oVar.a(tVar);
                return;
            }
            if (iA > dVarK.b()) {
                lVarB.a(254);
                lVarB.b(new byte[]{34});
                oVar.a(tVar);
                return;
            }
            lVarB.a(255);
            lVarB.b(new byte[]{dVarK.g(iA)});
            if (b2 == 0) {
                dVarK.i(iA);
                bH.C.d("Stopped DAQ " + iA + " for client " + oVar.m());
            } else if (b2 == 1) {
                dVarK.h(iA);
                bH.C.d("Started DAQ " + iA + " for client " + oVar.m());
            } else {
                bH.C.d("Selecting DAQ " + iA + " for client " + oVar.m());
                dVarK.j(iA);
            }
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
