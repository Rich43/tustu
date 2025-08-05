package bU;

import G.R;
import bH.C0995c;
import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: TunerStudioMS.jar:bU/f.class */
public class f implements bT.a {
    @Override // bT.a
    public int a() {
        return 238;
    }

    @Override // bT.a
    public void a(bT.o oVar, bN.t tVar) throws bT.h {
        if (tVar.a() != a()) {
            throw new bT.h("PID:CAL_DOWNLOAD_MAX Wrong handler! this handler is for 0x" + Integer.toHexString(a()).toUpperCase());
        }
        byte[] bArrC = tVar.c();
        if (bArrC == null) {
            throw new bT.h("PID:CAL_DOWNLOAD_MAX valid packet data not found.");
        }
        if (bArrC.length < 1) {
            throw new bT.h("PID:CAL_DOWNLOAD_MAX invalid packet data length, must have atleast 1 byte");
        }
        bN.k kVarD = oVar.d();
        int i2 = kVarD.i() - (kVarD.u() - 1);
        R rF = oVar.f();
        int iG = oVar.g();
        int iU = kVarD.u() * i2;
        int iH = oVar.h() - rF.O().y(iG);
        int i3 = kVarD.u() > 2 ? 3 : 1;
        try {
            if (bArrC.length != iU + i3) {
                throw new bT.h("PID:CAL_DOWNLOAD_MAX invalid packet data length, expected byte count: " + (iU + i3 + 1) + ", received: " + (bArrC.length + 1));
            }
            if (rF.O().l().length < iG || iG < 0) {
                bN.l lVarB = bN.u.a().b();
                lVarB.a(254);
                lVarB.b(new byte[]{38});
                oVar.c(38);
                oVar.a(tVar);
            } else if (iH + iU > rF.O().f(iG) || iH < 0) {
                bN.l lVarB2 = bN.u.a().b();
                lVarB2.a(254);
                lVarB2.b(new byte[]{34});
                oVar.c(34);
                oVar.a(tVar);
            } else {
                try {
                    byte[] bArr = new byte[bArrC.length - i3];
                    System.arraycopy(bArrC, i3, bArr, 0, bArr.length);
                    rF.h().a(iG, iU, C0995c.b(bArr));
                    bN.u.a().b().a(255);
                    oVar.a(tVar);
                    oVar.d(oVar.h() + iU);
                } catch (V.g e2) {
                    bN.l lVarB3 = bN.u.a().b();
                    lVarB3.a(254);
                    lVarB3.b(new byte[]{34});
                    oVar.c(34);
                    oVar.a(tVar);
                    bH.C.c("CAL_DOWNLOAD_MAX: Error updating local data store from DownloadMaxHandler");
                    e2.printStackTrace();
                }
            }
        } catch (bN.o e3) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, (String) null, (Throwable) e3);
            throw new bT.h(e3.getLocalizedMessage());
        }
    }
}
