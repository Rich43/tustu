package ao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

/* loaded from: TunerStudioMS.jar:ao/dZ.class */
public class dZ implements Z.c {
    @Override // Z.c
    public void a(Z.e eVar) {
        StringBuilder sb = new StringBuilder();
        Iterator it = eVar.b().iterator();
        while (it.hasNext()) {
            sb.append((String) it.next()).append(";");
        }
        h.i.c("ROOT_FIELD_" + eVar.a(), sb.toString());
    }

    @Override // Z.c
    public List a() {
        ArrayList arrayList = new ArrayList();
        for (String str : h.i.e("ROOT_FIELD_")) {
            String strF = h.i.f(str, "");
            if (!strF.isEmpty()) {
                Z.e eVar = new Z.e(bH.W.b(str, "ROOT_FIELD_", ""));
                StringTokenizer stringTokenizer = new StringTokenizer(strF, ";");
                while (stringTokenizer.hasMoreTokens()) {
                    String strTrim = stringTokenizer.nextToken().trim();
                    if (!strTrim.isEmpty()) {
                        eVar.a(strTrim);
                    }
                }
                arrayList.add(eVar);
            }
        }
        return arrayList;
    }

    @Override // Z.c
    public Z.e a(String str) {
        String strB = h.i.b("ROOT_FIELD_" + str, (String) null);
        if (strB == null) {
            return null;
        }
        Z.e eVar = new Z.e(str);
        StringTokenizer stringTokenizer = new StringTokenizer(strB, ";");
        while (stringTokenizer.hasMoreTokens()) {
            String strTrim = stringTokenizer.nextToken().trim();
            if (!strTrim.isEmpty()) {
                eVar.a(strTrim);
            }
        }
        return eVar;
    }
}
