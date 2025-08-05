package bV;

import G.R;
import G.aM;
import G.cC;
import V.i;
import bH.C;
import bN.l;
import bN.t;
import bN.u;
import bT.o;
import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: TunerStudioMS.jar:bV/a.class */
public class a implements h {
    @Override // bT.a
    public int a() {
        return 161;
    }

    @Override // bT.a
    public void a(o oVar, t tVar) throws bT.h {
        try {
            String str = "";
            int i2 = 0;
            int i3 = 0;
            for (String str2 : new String(tVar.c()).split("~")) {
                i2++;
                if (str2.contains("=")) {
                    String strSubstring = str2.substring(0, str2.indexOf("="));
                    String strSubstring2 = str2.substring(str2.indexOf("=") + 1);
                    R rF = oVar.f();
                    aM aMVarC = rF.c(strSubstring);
                    if (aMVarC != null) {
                        try {
                            cC.a(rF, aMVarC, strSubstring2);
                            i3++;
                        } catch (i e2) {
                            str = str + e2.getLocalizedMessage() + "\n";
                        }
                    }
                } else {
                    String str3 = str2 + ", Invalid Format for USER_CMD:DOWNLOAD_PC_VARIABLE";
                    C.a(str3);
                    str = str + str3 + "\n";
                }
            }
            if (i3 == i2) {
                l lVarB = u.a().b();
                lVarB.a(255);
                lVarB.b(new byte[]{0});
                oVar.a(lVarB);
            } else if (i3 > 0) {
                l lVarB2 = u.a().b();
                lVarB2.a(254);
                byte[] bytes = str.getBytes();
                byte[] bArr = new byte[bytes.length + 1];
                bArr[0] = 1;
                System.arraycopy(bytes, 0, bArr, 1, bytes.length);
                lVarB2.b(bArr);
                oVar.a(lVarB2);
            } else {
                l lVarB3 = u.a().b();
                lVarB3.a(254);
                byte[] bytes2 = str.getBytes();
                byte[] bArr2 = new byte[bytes2.length + 1];
                bArr2[0] = 2;
                System.arraycopy(bytes2, 0, bArr2, 1, bytes2.length);
                lVarB3.b(bArr2);
                oVar.a(lVarB3);
            }
        } catch (bN.o e3) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, (String) null, (Throwable) e3);
            throw new bT.h(e3.getLocalizedMessage());
        }
    }
}
