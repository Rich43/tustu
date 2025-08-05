package bU;

import G.R;
import bH.C0995c;
import bH.C0998f;
import java.util.logging.Level;
import java.util.logging.Logger;

/* renamed from: bU.a, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bU/a.class */
public class C1019a implements bT.a {

    /* renamed from: a, reason: collision with root package name */
    C0998f f7627a = new C0998f();

    @Override // bT.a
    public int a() {
        return 243;
    }

    @Override // bT.a
    public void a(bT.o oVar, bN.t tVar) throws bT.h {
        if (tVar.a() != a()) {
            throw new bT.h("PID:STD_BUILD_CHECKSUM Wrong handler! this handler is for 0x" + Integer.toHexString(a()).toUpperCase());
        }
        bN.k kVarD = oVar.d();
        byte[] bArrC = tVar.c();
        if (bArrC == null) {
            throw new bT.h("PID:STD_BUILD_CHECKSUM valid packet data not found.");
        }
        if (bArrC.length != 7) {
            throw new bT.h("PID:STD_BUILD_CHECKSUM invalid packet data length, expected 8 bytes, found: " + (bArrC.length + 1));
        }
        R rF = oVar.f();
        int iG = oVar.g();
        int iH = oVar.h() - rF.O().y(iG);
        int iA = C0995c.a(bArrC, 3, 4, kVarD.g(), false) * oVar.d().u();
        try {
            if (iA > 4096) {
                bN.l lVarB = bN.u.a().b();
                lVarB.a(254);
                byte[] bArr = new byte[7];
                bArr[0] = 34;
                byte[] bArrA = C0995c.a(4096, new byte[4], oVar.d().g());
                System.arraycopy(bArrA, 0, bArr, 3, bArrA.length);
                lVarB.b(bArr);
                oVar.a(lVarB);
            } else if (iH + iA > rF.O().f(iG)) {
                bN.l lVarB2 = bN.u.a().b();
                lVarB2.a(254);
                byte[] bArr2 = new byte[7];
                bArr2[0] = 34;
                byte[] bArrA2 = C0995c.a(rF.O().f(iG) - iH, new byte[4], oVar.d().g());
                System.arraycopy(bArrA2, 0, bArr2, 3, bArrA2.length);
                lVarB2.b(bArr2);
                oVar.a(lVarB2);
            } else {
                byte[] bArrA3 = C0995c.a(rF.h().a(iG, iH, iA));
                this.f7627a.a();
                this.f7627a.a(bArrA3);
                byte[] bArrA4 = C0995c.a(this.f7627a.b(), new byte[4], kVarD.g());
                bN.l lVarB3 = bN.u.a().b();
                lVarB3.a(255);
                byte[] bArr3 = new byte[7];
                bArr3[0] = 8;
                System.arraycopy(bArrA4, 0, bArr3, 3, bArrA4.length);
                lVarB3.b(bArr3);
                oVar.a(lVarB3);
            }
        } catch (bN.o e2) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
        }
    }
}
