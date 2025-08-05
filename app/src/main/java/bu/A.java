package bU;

import G.R;
import bH.C0995c;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: TunerStudioMS.jar:bU/A.class */
public class A implements bT.a {
    @Override // bT.a
    public int a() {
        return 245;
    }

    @Override // bT.a
    public void a(bT.o oVar, bN.t tVar) throws bT.h {
        byte[] bArrA;
        if (tVar.a() != a()) {
            throw new bT.h("PID:STD_UPLOAD Wrong handler! this handler is for 0x" + Integer.toHexString(a()).toUpperCase());
        }
        byte[] bArrC = tVar.c();
        if (bArrC == null) {
            throw new bT.h("PID:STD_UPLOAD valid packet data not found.");
        }
        if (bArrC.length != 1 && bArrC.length != 7) {
            throw new bT.h("PID:STD_UPLOAD invalid packet data length, expected 2 bytes, found: " + (bArrC.length + 1));
        }
        bN.k kVarD = oVar.d();
        int iA = C0995c.a(bArrC[0]);
        R rF = oVar.f();
        int iG = oVar.g();
        int iU = kVarD.u() * iA;
        int iH = oVar.h();
        if (iH >= 0) {
            iH -= rF.O().y(iG);
        }
        try {
            if (iH == bT.o.f7606c) {
                bArrA = oVar.l();
                if (bArrA == null) {
                    bN.l lVarB = bN.u.a().b();
                    lVarB.a(254);
                    lVarB.b(new byte[]{34});
                    oVar.c(34);
                    oVar.a(tVar);
                    return;
                }
            } else {
                if (rF.O().l().length < iG || iG < 0) {
                    bN.l lVarB2 = bN.u.a().b();
                    lVarB2.a(254);
                    lVarB2.b(new byte[]{38});
                    oVar.c(38);
                    oVar.a(tVar);
                    return;
                }
                if (iH + iU > rF.O().f(iG) || iH < 0) {
                    bN.l lVarB3 = bN.u.a().b();
                    lVarB3.a(254);
                    lVarB3.b(new byte[]{34});
                    oVar.c(34);
                    oVar.a(tVar);
                    return;
                }
                bArrA = C0995c.a(rF.h().a(iG, iH, iU));
            }
            if (bArrA.length > kVarD.i() - 1 && !kVarD.t().a()) {
                bN.l lVarB4 = bN.u.a().b();
                lVarB4.a(254);
                lVarB4.b(new byte[]{48});
                oVar.c(48);
                oVar.a(lVarB4);
            } else if (bArrA.length > kVarD.i() - 1) {
                List arrayList = new ArrayList();
                int i2 = 0;
                do {
                    int i3 = iU - i2 > kVarD.i() - 1 ? kVarD.i() - 1 : iU - i2;
                    byte[] bArr = new byte[i3];
                    System.arraycopy(bArrA, i2, bArr, 0, bArr.length);
                    i2 += i3;
                    bN.l lVarB5 = bN.u.a().b();
                    lVarB5.a(255);
                    lVarB5.b(bArr);
                    arrayList.add(lVarB5);
                } while (i2 < iU);
                oVar.d(oVar.h() + iU);
                oVar.a(arrayList);
            } else {
                bN.l lVarB6 = bN.u.a().b();
                lVarB6.a(255);
                lVarB6.b(bArrA);
                oVar.d(oVar.h() + iU);
                oVar.a(lVarB6);
            }
        } catch (bN.o e2) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            throw new bT.h(e2.getLocalizedMessage());
        }
    }
}
