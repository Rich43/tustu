package bV;

import G.C0132o;
import G.R;
import bH.C;
import bH.C0995c;
import bN.k;
import bN.l;
import bN.t;
import bN.u;
import bT.o;
import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: TunerStudioMS.jar:bV/c.class */
public class c implements h {
    @Override // bT.a
    public int a() {
        return 163;
    }

    @Override // bT.a
    public void a(o oVar, t tVar) throws bT.h {
        if (tVar.a() != a()) {
            throw new bT.h("PID:USER_CMD_WRITE_RAW Wrong handler! this handler is for 0x" + Integer.toHexString(a()).toUpperCase());
        }
        byte[] bArrC = tVar.c();
        if (bArrC == null) {
            throw new bT.h("PID:USER_CMD_WRITE_RAW valid packet data not found.");
        }
        if (bArrC.length < 1) {
            throw new bT.h("PID:USER_CMD_WRITE_RAW invalid packet data length, must have atleast 1 byte");
        }
        k kVarD = oVar.d();
        d dVarA = a(oVar);
        int iA = C0995c.a(bArrC, 0, 2, kVarD.g(), false);
        dVarA.c(C0995c.a(bArrC, 2, 4, kVarD.g(), true));
        dVarA.b(C0995c.a(bArrC, 6, 4, kVarD.g(), true));
        dVarA.a((bArrC[14] & 1) == 1);
        byte[] bArr = new byte[bArrC.length - 15];
        System.arraycopy(bArrC, 15, bArr, 0, bArr.length);
        R rF = oVar.f();
        try {
            if (!kVarD.t().a() && bArr.length != iA) {
                throw new bT.h("PID:USER_CMD_WRITE_RAW invalid packet data length, expected byte count: " + iA + ", received: " + bArr.length + 1);
            }
            l lVarB = u.a().b();
            if (iA <= bArr.length) {
                C0132o c0132oA = a(oVar).a(rF, bArr);
                if (c0132oA.a() == 3) {
                    lVarB.a(254);
                    String str = "WriteRaw Failed: " + c0132oA.c();
                    lVarB.b(str.getBytes());
                    oVar.a(lVarB);
                    C.c(str);
                } else {
                    lVarB.a(255);
                    if (c0132oA.e() != null) {
                        lVarB.b(C0995c.a(c0132oA.e()));
                        oVar.a(lVarB);
                    }
                }
            } else {
                d dVarA2 = a(oVar);
                dVarA2.a(iA - bArr.length);
                byte[] bArr2 = new byte[iA];
                System.arraycopy(bArr, 0, bArr2, 0, bArr.length);
                dVarA2.a(bArr2);
                C.c("PID:USER_CMD_WRITE_RAW Downloaded: " + bArr.length + ", expectedRemaining: " + dVarA2.b());
            }
        } catch (bN.o e2) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
        }
    }

    private d a(o oVar) {
        return (d) ((bU.C) oVar.i().a(241)).a(164);
    }
}
