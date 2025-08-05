package ae;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* renamed from: ae.c, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ae/c.class */
public class C0499c {
    public static List a(List list, k kVar) {
        ArrayList arrayList = new ArrayList();
        Iterator it = list.iterator();
        while (it.hasNext()) {
            s sVar = (s) it.next();
            if (!sVar.a(kVar).isEmpty()) {
                arrayList.add(sVar);
            }
        }
        return arrayList;
    }

    public static List b(List list, k kVar) {
        ArrayList arrayList = new ArrayList();
        Iterator it = list.iterator();
        while (it.hasNext()) {
            s sVar = (s) it.next();
            if (!sVar.b(kVar).isEmpty()) {
                arrayList.add(sVar);
            }
        }
        return arrayList;
    }

    public static boolean c(List list, k kVar) {
        Iterator it = list.iterator();
        while (it.hasNext()) {
            if (!((s) it.next()).a(kVar).isEmpty()) {
                return true;
            }
        }
        return false;
    }

    public static boolean d(List list, k kVar) {
        Iterator it = list.iterator();
        while (it.hasNext()) {
            if (!((s) it.next()).b(kVar).isEmpty()) {
                return true;
            }
        }
        return false;
    }
}
