package G;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* loaded from: TunerStudioMS.jar:G/aJ.class */
public class aJ {
    public static int a(R r2, int i2, int i3) {
        int iF = r2.O().f(i2) - 1;
        int iY = 0;
        Iterator itA = r2.a(i2);
        while (true) {
            if (!itA.hasNext()) {
                break;
            }
            aM aMVar = (aM) itA.next();
            if (!aMVar.O()) {
                if (!aMVar.h() && aMVar.g() > i3) {
                    iY += aMVar.y();
                } else if (aMVar.h()) {
                    iF = aMVar.g() - 1;
                    break;
                }
            }
        }
        return (iF - i3) - iY;
    }

    public static boolean a(R r2, List list) {
        boolean z2 = false;
        ArrayList arrayList = new ArrayList(list);
        Iterator itA = r2.a(((aM) list.get(0)).d());
        while (itA.hasNext()) {
            aM aMVar = (aM) itA.next();
            if (a(arrayList, aMVar)) {
                z2 = true;
                arrayList.remove(aMVar);
                if (arrayList.isEmpty()) {
                    return true;
                }
            } else if (z2) {
                return false;
            }
        }
        return list.isEmpty();
    }

    private static boolean a(List list, aM aMVar) {
        Iterator it = list.iterator();
        while (it.hasNext()) {
            if (((aM) it.next()).equals(aMVar)) {
                return true;
            }
        }
        return false;
    }
}
