package ac;

import G.C0043ac;
import G.R;
import bH.C1007o;
import java.util.ArrayList;
import java.util.Iterator;

/* loaded from: TunerStudioMS.jar:ac/r.class */
public class r {
    public static ArrayList a(R[] rArr) {
        ArrayList arrayList = new ArrayList();
        for (int i2 = 0; i2 < rArr.length; i2++) {
            if (rArr[i2].S()) {
                Iterator it = rArr[i2].g().iterator();
                while (it.hasNext()) {
                    C0043ac c0043ac = (C0043ac) it.next();
                    bH.C.c(c0043ac.aJ());
                    if (a(rArr[i2], c0043ac)) {
                        q qVar = new q();
                        if (i2 == 0) {
                            qVar.a(c0043ac.b());
                        } else {
                            qVar.a(rArr[i2].c() + "." + c0043ac.b());
                        }
                        qVar.b(rArr[i2].c());
                        qVar.a(c0043ac);
                        qVar.a(i2);
                        qVar.a(rArr[i2].g(c0043ac.a()));
                        qVar.b(c0043ac.f());
                        arrayList.add(qVar);
                    }
                }
            }
        }
        return arrayList;
    }

    public static boolean a(R r2, C0043ac c0043ac) {
        try {
            if (c0043ac.aH() != null && !c0043ac.aH().equals("")) {
                if (!C1007o.a(c0043ac.aH(), r2)) {
                    return false;
                }
            }
            return true;
        } catch (V.g e2) {
            bH.C.b("Unable to process enable logic (Assuming true) for DataLog Field: " + ((Object) c0043ac));
            return true;
        }
    }

    public static boolean a() {
        return C0491c.a().u() || u.a().u();
    }

    public static String b() {
        if (C0491c.a().u()) {
            return C0491c.a().n();
        }
        if (u.a().u()) {
            return u.a().n();
        }
        return null;
    }
}
