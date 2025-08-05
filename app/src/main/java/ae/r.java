package ae;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* loaded from: TunerStudioMS.jar:ae/r.class */
public class r {

    /* renamed from: a, reason: collision with root package name */
    List f4431a = new ArrayList();

    /* renamed from: b, reason: collision with root package name */
    private static r f4432b = null;

    private r() {
    }

    public static r a() {
        if (f4432b == null) {
            f4432b = new r();
        }
        return f4432b;
    }

    public void a(q qVar) {
        this.f4431a.add(qVar);
    }

    public List a(m mVar) {
        ArrayList arrayList = new ArrayList();
        for (q qVar : this.f4431a) {
            if (mVar != null && qVar.a(mVar)) {
                arrayList.add(qVar);
            }
        }
        return arrayList;
    }

    public List a(k kVar) {
        ArrayList arrayList = new ArrayList();
        for (q qVar : this.f4431a) {
            if (kVar != null && qVar.a(kVar)) {
                arrayList.add(qVar);
            }
        }
        return arrayList;
    }

    public List a(k kVar, m mVar) {
        ArrayList arrayList = new ArrayList();
        List<q> listA = a(mVar);
        List listA2 = a(kVar);
        for (q qVar : listA) {
            if (listA2.contains(qVar) && !arrayList.contains(qVar)) {
                arrayList.add(qVar);
            }
        }
        return arrayList;
    }

    public void b() {
        Iterator it = this.f4431a.iterator();
        while (it.hasNext()) {
            ((q) it.next()).f();
        }
    }
}
