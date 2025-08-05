package bU;

import G.C0113cs;
import G.R;
import G.aH;
import bH.C0995c;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: TunerStudioMS.jar:bU/D.class */
public class D implements bT.a {
    @Override // bT.a
    public int a() {
        return 225;
    }

    @Override // bT.a
    public void a(bT.o oVar, bN.t tVar) throws bT.h {
        if (tVar.a() != a()) {
            throw new bT.h("PID:DAQ_WRITE_DAQ Wrong handler! this handler is for 0x" + Integer.toHexString(a()).toUpperCase());
        }
        bN.k kVarD = oVar.d();
        R rF = oVar.f();
        byte[] bArrC = tVar.c();
        if (bArrC == null) {
            throw new bT.h("PID:DAQ_WRITE_DAQ valid packet data not found.");
        }
        if (bArrC.length != 7) {
            throw new bT.h("PID:DAQ_WRITE_DAQ invalid packet data length, expected 7 bytes, found: " + (bArrC.length + 1));
        }
        try {
            bN.l lVarB = bN.u.a().b();
            bT.d dVarK = oVar.k();
            int iC = dVarK.c();
            int iD = dVarK.d();
            int iE = dVarK.e();
            byte b2 = bArrC[0];
            int iA = C0995c.a(bArrC[1]);
            long jB = C0995c.b(bArrC, 3, 4, kVarD.g(), false);
            bO.c cVarB = dVarK.b(iC);
            if (cVarB == null || iC > dVarK.b() || iD > cVarB.a() || iE > cVarB.c()) {
                lVarB.a(254);
                lVarB.b(new byte[]{34});
                oVar.a(tVar);
                return;
            }
            lVarB.a(255);
            byte[] bArr = new byte[0];
            bO.l lVarA = dVarK.a(iC, iD, iE);
            int iA2 = a(rF, jB);
            lVarA.a(iA2);
            lVarA.a(iA);
            System.currentTimeMillis();
            List listA = a(rF, iA2, iA);
            C0113cs.a().d();
            Iterator it = listA.iterator();
            while (it.hasNext()) {
                try {
                    C0113cs.a().a(rF.c(), ((aH) it.next()).aJ(), cVarB.n());
                } catch (V.a e2) {
                    Logger.getLogger(D.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
                }
            }
            C0113cs.a().e();
            if (iE < cVarB.c() - 1) {
                dVarK.f(iE + 1);
            } else {
                dVarK.d(-1);
                dVarK.e(-1);
                dVarK.f(-1);
            }
            lVarB.b(bArr);
            oVar.a(lVarB);
        } catch (bN.o | bO.j e3) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, (String) null, e3);
            bN.l lVarB2 = bN.u.a().b();
            lVarB2.a(254);
            lVarB2.b(new byte[]{34});
            try {
                oVar.a(lVarB2);
            } catch (bN.o e4) {
                bH.C.a("Unable to send response packet.");
                Logger.getLogger(l.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e4);
            }
        }
    }

    private List a(R r2, int i2, int i3) {
        ArrayList arrayList = new ArrayList();
        Iterator itQ = r2.q();
        while (itQ.hasNext()) {
            aH aHVar = (aH) itQ.next();
            if (aHVar.a() >= i2 && aHVar.a() < i2 + i3) {
                arrayList.add(aHVar);
            }
        }
        return arrayList;
    }

    private int a(R r2, long j2) {
        if (j2 < r2.O().n()) {
            return (int) j2;
        }
        aH aHVarB = b(r2, j2);
        if (aHVarB != null) {
            return aHVarB.a();
        }
        bH.C.a("Can not find a channel at address 0x" + Long.toHexString(j2).toUpperCase());
        return (int) j2;
    }

    private aH b(R r2, long j2) {
        Iterator itQ = r2.q();
        while (itQ.hasNext()) {
            aH aHVar = (aH) itQ.next();
            if (aHVar.x() == j2) {
                return aHVar;
            }
        }
        return null;
    }
}
