package Z;

import G.C0043ac;
import G.C0126i;
import G.R;
import G.T;
import G.aH;
import V.g;
import W.C0184j;
import bH.C;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* loaded from: TunerStudioMS.jar:Z/a.class */
public class a implements d {
    @Override // Z.d
    public List a(List list) {
        ArrayList arrayList = new ArrayList();
        R rC = T.a().c();
        if (rC != null) {
            for (C0043ac c0043ac : rC.g()) {
                aH aHVarG = rC.g(c0043ac.a());
                if (aHVarG != null && aHVarG.b().equals("formula") && !a(c0043ac.b(), list)) {
                    try {
                        String strA = C0126i.a(aHVarG.k(), rC);
                        C0184j c0184j = new C0184j();
                        c0184j.a(c0043ac.b());
                        c0184j.c(strA);
                        c0184j.e(c0043ac.d());
                        arrayList.add(c0184j);
                    } catch (g e2) {
                        C.b("Unable to convert DataLogField \"" + c0043ac.b() + "\" Error: " + e2.getLocalizedMessage());
                    }
                }
            }
        }
        return arrayList;
    }

    private boolean a(String str, List list) {
        Iterator it = list.iterator();
        while (it.hasNext()) {
            if (((C0184j) it.next()).a().equals(str)) {
                return true;
            }
        }
        return false;
    }
}
