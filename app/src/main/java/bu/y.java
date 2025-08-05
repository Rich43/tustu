package bU;

import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: TunerStudioMS.jar:bU/y.class */
public class y implements bT.a {
    @Override // bT.a
    public int a() {
        return 221;
    }

    @Override // bT.a
    public void a(bT.o oVar, bN.t tVar) throws bT.h {
        if (tVar.a() != a()) {
            throw new bT.h("PID:DAQ_START_STOP_SYNCH Wrong handler! this handler is for 0x" + Integer.toHexString(a()).toUpperCase());
        }
        byte[] bArrC = tVar.c();
        if (bArrC == null) {
            throw new bT.h("PID:DAQ_START_STOP_SYNCH valid packet data not found.");
        }
        if (bArrC.length != 1) {
            throw new bT.h("PID:DAQ_START_STOP_SYNCH invalid packet data length, expected 2 bytes, found: " + (bArrC.length + 1));
        }
        try {
            bN.l lVarB = bN.u.a().b();
            byte b2 = bArrC[0];
            bT.d dVarK = oVar.k();
            if (b2 > 2 || b2 < 0) {
                lVarB.a(254);
                lVarB.b(new byte[]{39});
                oVar.a(tVar);
                return;
            }
            lVarB.a(255);
            byte[] bArr = new byte[1];
            if (oVar.f().R()) {
                bArr[0] = 1;
            } else {
                bArr[0] = 0;
            }
            lVarB.b(bArr);
            if (b2 == 0) {
                dVarK.i();
            } else if (b2 == 1) {
                dVarK.g();
            } else {
                dVarK.h();
            }
            dVarK.f();
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
