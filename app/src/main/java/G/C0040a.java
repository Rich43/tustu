package G;

import java.util.ArrayList;
import java.util.Iterator;

/* renamed from: G.a, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:G/a.class */
public class C0040a implements InterfaceC0120cz {
    @Override // G.InterfaceC0120cz
    public boolean a(ArrayList arrayList, R r2, Y y2, String str) {
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            aM aMVar = (aM) it.next();
            try {
                r2.h().a(aMVar.d(), aMVar.g(), aMVar.g(y2));
            } catch (V.g e2) {
                e2.printStackTrace();
                bH.C.a("Unable to take difference data.");
            }
        }
        return false;
    }
}
