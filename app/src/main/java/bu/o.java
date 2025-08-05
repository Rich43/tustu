package bU;

import bH.C0995c;
import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: TunerStudioMS.jar:bU/o.class */
public class o implements bT.a {
    @Override // bT.a
    public int a() {
        return 217;
    }

    @Override // bT.a
    public void a(bT.o oVar, bN.t tVar) throws bT.h {
        if (tVar.a() != a()) {
            throw new bT.h("PID:DAQ_GET_DAQ_RESOLUTION_INFO Wrong handler! this handler is for 0x" + Integer.toHexString(a()).toUpperCase());
        }
        bN.k kVarD = oVar.d();
        oVar.f();
        try {
            bN.l lVarB = bN.u.a().b();
            lVarB.a(255);
            bO.h hVarC = oVar.k().a().c();
            byte[] bArr = {(byte) hVarC.f(), (byte) hVarC.a(), (byte) hVarC.b(), (byte) hVarC.c(), (byte) hVarC.d().d(), 0, 0};
            C0995c.a(bArr, hVarC.e(), 5, 2, kVarD.g());
            lVarB.b(bArr);
            oVar.a(lVarB);
        } catch (bN.o e2) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
        }
    }
}
