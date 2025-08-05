package bU;

import bH.C0995c;
import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: TunerStudioMS.jar:bU/n.class */
public class n implements bT.a {
    @Override // bT.a
    public int a() {
        return 218;
    }

    @Override // bT.a
    public void a(bT.o oVar, bN.t tVar) throws bT.h {
        if (tVar.a() != a()) {
            throw new bT.h("PID:GET_DAQ_PROCESSOR_INFO Wrong handler! this handler is for 0x" + Integer.toHexString(a()).toUpperCase());
        }
        bN.k kVarD = oVar.d();
        try {
            bN.l lVarB = bN.u.a().b();
            lVarB.a(255);
            bO.f fVarB = oVar.k().a().b();
            byte[] bArr = {fVarB.a().a(), 0, 0, 0, 0, (byte) fVarB.d(), fVarB.e().b()};
            C0995c.a(bArr, fVarB.b(), 1, 2, kVarD.g());
            C0995c.a(bArr, fVarB.c(), 3, 2, kVarD.g());
            lVarB.b(bArr);
            oVar.a(lVarB);
        } catch (bN.o e2) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
        }
    }
}
