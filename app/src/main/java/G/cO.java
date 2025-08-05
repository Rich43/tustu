package G;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/* loaded from: TunerStudioMS.jar:G/cO.class */
public class cO {

    /* renamed from: b, reason: collision with root package name */
    private static cO f1058b = null;

    /* renamed from: a, reason: collision with root package name */
    HashMap f1059a = new HashMap();

    private cO() {
    }

    public static cO a() {
        if (f1058b == null) {
            f1058b = new cO();
        }
        return f1058b;
    }

    public void a(String str, cL cLVar) {
        ArrayList arrayListB = b(str);
        if (arrayListB.contains(cLVar)) {
            return;
        }
        arrayListB.add(cLVar);
    }

    public void a(String str) {
        this.f1059a.remove(str);
    }

    private ArrayList b(String str) {
        ArrayList arrayList = (ArrayList) this.f1059a.get(str);
        if (arrayList == null) {
            arrayList = new ArrayList();
            this.f1059a.put(str, arrayList);
        }
        return arrayList;
    }

    public void a(boolean z2, String str, int i2, String str2, int[] iArr, int[] iArr2) {
        Iterator it = b(str).iterator();
        while (it.hasNext()) {
            try {
                ((cL) it.next()).a(z2, i2, str2, iArr, iArr2);
            } catch (Exception e2) {
                bH.C.b("Exception caught while trying to notify a ProtocolErrorListener. Stack to folow:");
                e2.printStackTrace();
            }
        }
    }

    public void a(String str, String str2) {
        Iterator it = b(str).iterator();
        while (it.hasNext()) {
            try {
                ((cL) it.next()).b(str2);
            } catch (Exception e2) {
                bH.C.b("Exception caught while trying to notify a ProtocolErrorListener. Stack to folow:");
                e2.printStackTrace();
            }
        }
    }
}
