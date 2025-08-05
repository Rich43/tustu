package bV;

import G.R;
import G.aM;
import G.cC;
import bN.l;
import bN.t;
import bN.u;
import bT.o;
import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: TunerStudioMS.jar:bV/g.class */
public class g implements h {

    /* renamed from: a, reason: collision with root package name */
    public static int f7641a = 0;

    /* renamed from: b, reason: collision with root package name */
    public static int f7642b = 1;

    @Override // bT.a
    public int a() {
        return 160;
    }

    @Override // bT.a
    public void a(o oVar, t tVar) throws bT.h {
        byte[] bArrC = tVar.c();
        try {
            if (bArrC.length < 1) {
                l lVarB = u.a().b();
                lVarB.a(254);
                lVarB.b("Missing Command code, message too short.".getBytes());
                oVar.a(lVarB);
            }
            byte b2 = bArrC[0];
            byte[] bArr = new byte[bArrC.length - 1];
            System.arraycopy(bArrC, 1, bArr, 0, bArr.length);
            if (b2 == f7641a) {
                String str = new String(bArr);
                R rF = oVar.f();
                aM aMVarC = rF.c(str);
                if (aMVarC != null) {
                    String strA = cC.a(rF, aMVarC);
                    if (strA != null) {
                        String str2 = aMVarC.aJ() + "=" + strA;
                        l lVarB2 = u.a().b();
                        lVarB2.a(255);
                        lVarB2.b(str2.getBytes());
                        oVar.a(lVarB2);
                    } else {
                        l lVarB3 = u.a().b();
                        lVarB3.a(254);
                        lVarB3.b(("Parameter does not exist in our configuration " + str).getBytes());
                        oVar.a(lVarB3);
                    }
                } else {
                    l lVarB4 = u.a().b();
                    lVarB4.a(254);
                    lVarB4.b(("Parameter does not exist in our configuration " + str).getBytes());
                    oVar.a(lVarB4);
                }
            } else if (b2 == f7642b) {
                try {
                    String strA2 = cC.a(oVar.f());
                    l lVarB5 = u.a().b();
                    lVarB5.a(255);
                    lVarB5.b(strA2.getBytes());
                    oVar.a(lVarB5);
                } catch (Exception e2) {
                    l lVarB6 = u.a().b();
                    lVarB6.a(254);
                    lVarB6.b(("Failed to get all PcVariable values:  " + e2.getLocalizedMessage()).getBytes());
                    oVar.a(lVarB6);
                }
            } else {
                l lVarB7 = u.a().b();
                lVarB7.a(254);
                lVarB7.b("unknown sub command code ".getBytes());
                oVar.a(lVarB7);
            }
        } catch (bN.o e3) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, (String) null, (Throwable) e3);
            throw new bT.h(e3.getLocalizedMessage());
        }
    }
}
