package bU;

import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: TunerStudioMS.jar:bU/j.class */
public class j implements bT.a {
    @Override // bT.a
    public void a(bT.o oVar, bN.t tVar) throws bT.h {
        if (tVar.a() != a()) {
            throw new bT.h("PID:GET_COMM_MODE_INFO Wrong handler! this handler is for 0x" + Integer.toHexString(a()).toUpperCase());
        }
        byte[] bArrC = tVar.c();
        if (bArrC.length != 0) {
            throw new bT.h("PID:GET_COMM_MODE_INFO Packet size. Expected 1, found: " + (bArrC.length + 1));
        }
        bN.l lVarB = bN.u.a().b();
        bN.k kVarD = oVar.d();
        lVarB.a(255);
        lVarB.b(new byte[]{0, 0, 0, 20, (byte) kVarD.k(), (byte) kVarD.n(), (byte) bQ.l.f7451ak});
        try {
            oVar.a(lVarB);
        } catch (bN.o e2) {
            Logger.getLogger(C1021c.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
        }
    }

    @Override // bT.a
    public int a() {
        return 251;
    }
}
