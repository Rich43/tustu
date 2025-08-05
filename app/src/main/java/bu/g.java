package bU;

import G.R;
import bH.C0995c;
import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: TunerStudioMS.jar:bU/g.class */
public class g implements bT.a {

    /* renamed from: a, reason: collision with root package name */
    private int f7628a = 0;

    @Override // bT.a
    public int a() {
        return 239;
    }

    @Override // bT.a
    public void a(bT.o oVar, bN.t tVar) throws bT.h {
        if (tVar.a() != a()) {
            throw new bT.h("PID:CAL_DOWNLOAD_NEXT Wrong handler! this handler is for 0x" + Integer.toHexString(a()).toUpperCase());
        }
        byte[] bArrC = tVar.c();
        if (bArrC == null) {
            throw new bT.h("PID:CAL_DOWNLOAD_NEXT valid packet data not found.");
        }
        if (bArrC.length < 1) {
            throw new bT.h("PID:CAL_DOWNLOAD_NEXT invalid packet data length, must have atleast 1 byte");
        }
        bN.k kVarD = oVar.d();
        int iA = C0995c.a(bArrC[0]);
        R rF = oVar.f();
        int i2 = kVarD.u() > 2 ? 3 : 1;
        int iG = oVar.g();
        int length = (bArrC.length - i2) * kVarD.u();
        int iU = kVarD.u() * length;
        int iH = oVar.h() - rF.O().y(iG);
        if (bQ.l.I()) {
            bH.C.c("DOWNLOAD_NEXT: Got: " + iU + ", expectedRemaining: " + b());
        }
        try {
            if (iA > this.f7628a) {
                bH.C.a("PID:CAL_DOWNLOAD_NEXT Element count not as expected: " + this.f7628a + ", received: " + iA);
                bN.l lVarB = bN.u.a().b();
                lVarB.a(254);
                lVarB.b(new byte[]{41, (byte) this.f7628a, (byte) iA});
                oVar.c(41);
                oVar.a(lVarB);
            } else if (rF.O().l().length < iG || iG < 0) {
                bN.l lVarB2 = bN.u.a().b();
                lVarB2.a(254);
                lVarB2.b(new byte[]{38});
                oVar.c(38);
                oVar.a(lVarB2);
            } else if (iH + iU > rF.O().f(iG) || iH < 0) {
                bN.l lVarB3 = bN.u.a().b();
                lVarB3.a(254);
                lVarB3.b(new byte[]{34});
                oVar.c(34);
                oVar.a(lVarB3);
            } else {
                try {
                    byte[] bArr = new byte[bArrC.length - i2];
                    System.arraycopy(bArrC, i2, bArr, 0, bArr.length);
                    rF.h().a(iG, iH, C0995c.b(bArr));
                    oVar.d(oVar.h() + iU);
                    bH.C.c("expectedRemainingElements = " + this.f7628a + ", elementCount=" + iA + ", packetElementCount=" + length + ", setting expectedRemainingElements to: " + (iA - length));
                    a(iA - length);
                    if (this.f7628a == 0) {
                        bN.l lVarB4 = bN.u.a().b();
                        lVarB4.a(255);
                        oVar.a(lVarB4);
                    }
                } catch (V.g e2) {
                    bN.l lVarB5 = bN.u.a().b();
                    lVarB5.a(254);
                    lVarB5.b(new byte[]{34});
                    oVar.c(34);
                    oVar.a(lVarB5);
                    bH.C.c("Error updating local data store from DownloadHandler");
                    e2.printStackTrace();
                }
            }
        } catch (bN.o e3) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, (String) null, (Throwable) e3);
            throw new bT.h(e3.getLocalizedMessage());
        }
    }

    public int b() {
        return this.f7628a;
    }

    public void a(int i2) {
        this.f7628a = i2;
    }
}
