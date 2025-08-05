package aj;

import G.C0043ac;
import G.C0113cs;
import G.R;
import G.T;
import W.C0184j;
import W.C0188n;
import bH.C;
import java.util.Iterator;

/* renamed from: aj.a, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:aj/a.class */
public class C0520a {
    public void a(int i2, C0188n c0188n) {
        String strC;
        if (c0188n == null || a()) {
            return;
        }
        Iterator it = c0188n.iterator();
        while (it.hasNext()) {
            String strA = ((C0184j) it.next()).a();
            if (strA.contains(".")) {
                strC = strA.substring(0, strA.indexOf(46));
                strA = strA.substring(strA.indexOf(46) + 1);
            } else {
                strC = T.a().c().c();
            }
            R rC = T.a().c(strC);
            if (rC == null) {
                C.d("EcuConfig '" + strC + "' not found, using working config");
                rC = T.a().c();
            }
            C0043ac c0043acA = a(rC, strA);
            if (c0043acA != null) {
                try {
                    C0113cs.a().a(rC.c(), c0043acA.a(), r0.d(i2));
                } catch (Exception e2) {
                }
            }
        }
    }

    private C0043ac a(R r2, String str) {
        Iterator it = r2.g().iterator();
        while (it.hasNext()) {
            C0043ac c0043ac = (C0043ac) it.next();
            if (c0043ac.b().equals(str)) {
                return c0043ac;
            }
        }
        return null;
    }

    private boolean a() {
        R rC = T.a().c();
        return rC != null && rC.R();
    }
}
