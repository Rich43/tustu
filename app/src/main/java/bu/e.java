package bU;

import G.R;
import bH.C0995c;
import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: TunerStudioMS.jar:bU/e.class */
public class e implements bT.a {
    @Override // bT.a
    public int a() {
        return 240;
    }

    @Override // bT.a
    public void a(bT.o oVar, bN.t tVar) throws bT.h {
        if (tVar.a() != a()) {
            throw new bT.h("PID:CAL_DOWNLOAD Wrong handler! this handler is for 0x" + Integer.toHexString(a()).toUpperCase());
        }
        byte[] bArrC = tVar.c();
        if (bArrC == null) {
            throw new bT.h("PID:CAL_DOWNLOAD valid packet data not found.");
        }
        if (bArrC.length < 1) {
            throw new bT.h("PID:CAL_DOWNLOAD invalid packet data length, must have atleast 1 byte");
        }
        bN.k kVarD = oVar.d();
        int iA = C0995c.a(bArrC[0]);
        R rF = oVar.f();
        int iG = oVar.g();
        int iU = kVarD.u() * iA;
        int iH = oVar.h() - rF.O().y(iG);
        int i2 = kVarD.u() > 2 ? 3 : 1;
        try {
            if (!kVarD.t().a() && bArrC.length != iU + i2) {
                throw new bT.h("PID:CAL_DOWNLOAD invalid packet data length, expected byte count: " + (iU + i2 + 1) + ", received: " + (bArrC.length + 1));
            }
            if (rF.O().l().length < iG || iG < 0) {
                bN.l lVarB = bN.u.a().b();
                lVarB.a(254);
                lVarB.b(new byte[]{38});
                oVar.c(38);
                oVar.a(lVarB);
            } else if (iH + iU > rF.O().f(iG) || iH < 0) {
                bN.l lVarB2 = bN.u.a().b();
                lVarB2.a(254);
                lVarB2.b(new byte[]{34});
                oVar.c(34);
                oVar.a(lVarB2);
            } else {
                try {
                    byte[] bArr = new byte[bArrC.length - i2];
                    System.arraycopy(bArrC, i2, bArr, 0, bArr.length);
                    rF.h().a(iG, iH, C0995c.b(bArr));
                    oVar.d(oVar.h() + bArr.length);
                    if (iU <= bArr.length) {
                        bN.l lVarB3 = bN.u.a().b();
                        lVarB3.a(255);
                        oVar.a(lVarB3);
                    } else {
                        g gVar = (g) oVar.i().a(239);
                        gVar.a(iA - (bArr.length / kVarD.u()));
                        bH.C.c("DOWNLOAD: Downloaded: " + iA + ", expectedRemaining: " + gVar.b());
                    }
                } catch (V.g e2) {
                    bN.l lVarB4 = bN.u.a().b();
                    lVarB4.a(254);
                    lVarB4.b(new byte[]{34});
                    oVar.c(34);
                    oVar.a(lVarB4);
                    bH.C.c("Error updating local data store from DownloadHandler");
                    e2.printStackTrace();
                }
            }
        } catch (bN.o e3) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, (String) null, (Throwable) e3);
        }
    }
}
