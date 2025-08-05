package bU;

import G.R;
import G.aH;
import bH.C0995c;
import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: TunerStudioMS.jar:bU/k.class */
public class k implements bT.a {
    @Override // bT.a
    public int a() {
        return 215;
    }

    @Override // bT.a
    public void a(bT.o oVar, bN.t tVar) throws bT.h {
        if (tVar.a() != a()) {
            throw new bT.h("PID:DAQ_GET_DAQ_EVENT_INFO Wrong handler! this handler is for 0x" + Integer.toHexString(a()).toUpperCase());
        }
        bN.k kVarD = oVar.d();
        R rF = oVar.f();
        byte[] bArrC = tVar.c();
        if (bArrC == null) {
            throw new bT.h("PID:DAQ_GET_DAQ_EVENT_INFO valid packet data not found.");
        }
        if (bArrC.length != 3) {
            throw new bT.h("PID:DAQ_GET_DAQ_EVENT_INFO invalid packet data length, expected 4 bytes, found: " + (bArrC.length + 1));
        }
        try {
            bN.l lVarB = bN.u.a().b();
            aH aHVarB = rF.b(C0995c.a(bArrC, 1, 2, kVarD.g(), false));
            if (aHVarB == null) {
                lVarB.a(254);
                lVarB.b(new byte[]{34});
                oVar.a(tVar);
            } else {
                lVarB.a(255);
                byte[] bArr = {4, -1, (byte) aHVarB.aJ().length(), 0, 0, 15};
                oVar.d(bT.o.f7606c);
                oVar.a(aHVarB.aJ().getBytes());
                lVarB.b(bArr);
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
