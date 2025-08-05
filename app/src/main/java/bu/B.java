package bU;

import bH.C0995c;

/* loaded from: TunerStudioMS.jar:bU/B.class */
public class B implements bT.a {
    @Override // bT.a
    public int a() {
        return 244;
    }

    @Override // bT.a
    public void a(bT.o oVar, bN.t tVar) throws bT.h {
        if (tVar.a() != a()) {
            throw new bT.h("PID:STD_SHORT_UPLOAD Wrong handler! this handler is for 0x" + Integer.toHexString(a()).toUpperCase());
        }
        bN.l lVarB = bN.u.a().b();
        bN.k kVarD = oVar.d();
        lVarB.a(255);
        byte[] bArrC = tVar.c();
        if (bArrC == null) {
            throw new bT.h("PID:STD_SHORT_UPLOAD valid packet data not found.");
        }
        if (bArrC.length != 7) {
            throw new bT.h("PID:STD_SHORT_UPLOAD invalid packet data length, expected 8 bytes, found: " + (bArrC.length + 1));
        }
        oVar.d(C0995c.a(bArrC, 3, 4, kVarD.g(), false));
        ((bN.l) tVar).a(245);
        oVar.i().a(245).a(oVar, tVar);
    }
}
