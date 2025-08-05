package bU;

import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: TunerStudioMS.jar:bU/q.class */
public class q implements bT.a {
    @Override // bT.a
    public void a(bT.o oVar, bN.t tVar) throws bT.h {
        if (tVar.a() != a()) {
            throw new bT.h("PID:GET_STATUS Wrong handler! this handler is for 0x" + Integer.toHexString(a()).toUpperCase());
        }
        bN.l lVarB = bN.u.a().b();
        bN.k kVarD = oVar.d();
        lVarB.a(255);
        byte[] bArr = new byte[5];
        bArr[0] = kVarD.o().a();
        bArr[1] = kVarD.p().a();
        if (kVarD.g()) {
            bArr[3] = (byte) (255 & (oVar.c() >> 8));
            bArr[4] = (byte) (255 & oVar.c());
        } else {
            bArr[3] = (byte) (255 & oVar.c());
            bArr[4] = (byte) (255 & (oVar.c() >> 8));
        }
        try {
            oVar.a(lVarB);
        } catch (bN.o e2) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            throw new bT.h(e2.getLocalizedMessage());
        }
    }

    @Override // bT.a
    public int a() {
        return 253;
    }
}
