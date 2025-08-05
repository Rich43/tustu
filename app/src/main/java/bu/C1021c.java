package bU;

import bH.C0995c;
import java.util.logging.Level;
import java.util.logging.Logger;

/* renamed from: bU.c, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bU/c.class */
public class C1021c implements bT.a {
    @Override // bT.a
    public void a(bT.o oVar, bN.t tVar) throws bT.h {
        if (tVar.a() != a()) {
            throw new bT.h("PID:CONNECT Wrong handler! this handler is for 0x" + Integer.toHexString(a()).toUpperCase());
        }
        byte[] bArrC = tVar.c();
        if (bArrC.length != 1) {
            throw new bT.h("PID:CONNECT UnexpedataBytes[0] == XcpServer.STATE_CONNECTED_NORMALcted data size, expected 1 byte, found: " + bArrC.length);
        }
        if (bArrC[0] != 0 && bArrC[0] != 1) {
            throw new bT.h("PID:CONNECT Unsupported connect Mode requested: " + ((int) bArrC[0]));
        }
        oVar.a(bArrC[0]);
        bN.l lVarB = bN.u.a().b();
        bN.k kVarD = oVar.d();
        lVarB.a(255);
        byte[] bArrA = C0995c.a(new byte[]{5, kVarD.t().d(), (byte) kVarD.i(), 0, 0, 0, 0}, kVarD.j(), 3, 2, kVarD.g());
        bArrA[5] = 1;
        bArrA[6] = 1;
        lVarB.b(bArrA);
        try {
            oVar.a(lVarB);
        } catch (bN.o e2) {
            Logger.getLogger(C1021c.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
        }
    }

    @Override // bT.a
    public int a() {
        return 255;
    }
}
