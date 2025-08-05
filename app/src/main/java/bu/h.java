package bU;

import G.R;
import bH.C0995c;
import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: TunerStudioMS.jar:bU/h.class */
public class h implements bT.a {
    @Override // bT.a
    public int a() {
        return 237;
    }

    @Override // bT.a
    public void a(bT.o oVar, bN.t tVar) throws bT.h {
        if (tVar.a() != a()) {
            throw new bT.h("PID:CAL_SHORT_DOWNLOAD Wrong handler! this handler is for 0x" + Integer.toHexString(a()).toUpperCase());
        }
        try {
            bN.k kVarD = oVar.d();
            byte[] bArrC = tVar.c();
            if (bArrC == null) {
                throw new bT.h("PID:CAL_SHORT_DOWNLOAD valid packet data not found.");
            }
            if (bArrC.length < 7) {
                throw new bT.h("PID:CAL_SHORT_DOWNLOAD invalid packet data length, expected minimum 8 bytes, found: " + (bArrC.length + 1));
            }
            int iA = C0995c.a(bArrC, 3, 4, kVarD.g(), false);
            oVar.d(iA);
            byte b2 = bArrC[0];
            R rF = oVar.f();
            int iG = oVar.g();
            int iU = kVarD.u() * b2;
            int iY = iA - rF.O().y(iG);
            if (bArrC.length != iU + 7) {
                throw new bT.h("PID:CAL_SHORT_DOWNLOAD invalid packet data length, expected byte count: " + (iU + 7 + 1) + ", received: " + (bArrC.length + 1));
            }
            if (rF.O().l().length < iG || iG < 0) {
                bN.l lVarB = bN.u.a().b();
                lVarB.a(254);
                lVarB.b(new byte[]{38});
                oVar.c(38);
                oVar.a(lVarB);
            } else if (iY + iU > rF.O().f(iG) || iY < 0) {
                bN.l lVarB2 = bN.u.a().b();
                lVarB2.a(254);
                lVarB2.b(new byte[]{34});
                oVar.c(34);
                oVar.a(lVarB2);
            } else {
                try {
                    byte[] bArr = new byte[bArrC.length - 7];
                    System.arraycopy(bArrC, 7, bArr, 0, bArr.length);
                    rF.h().a(iG, iU, C0995c.b(bArr));
                    bN.l lVarB3 = bN.u.a().b();
                    lVarB3.a(255);
                    oVar.a(lVarB3);
                } catch (V.g e2) {
                    bN.l lVarB4 = bN.u.a().b();
                    lVarB4.a(254);
                    lVarB4.b(new byte[]{34});
                    oVar.c(34);
                    oVar.a(lVarB4);
                    bH.C.c("CAL_SHORT_DOWNLOAD: Error updating local data store from DownloadHandler");
                    e2.printStackTrace();
                }
            }
        } catch (bN.o e3) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, (String) null, (Throwable) e3);
            throw new bT.h(e3.getLocalizedMessage());
        }
    }
}
