package bS;

import bH.C;
import bN.k;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* loaded from: TunerStudioMS.jar:bS/d.class */
public class d {

    /* renamed from: c, reason: collision with root package name */
    private static d f7550c = null;

    /* renamed from: a, reason: collision with root package name */
    Map f7551a = new HashMap();

    /* renamed from: b, reason: collision with root package name */
    Map f7552b = new HashMap();

    private d() {
    }

    public static d a() {
        if (f7550c == null) {
            f7550c = new d();
        }
        return f7550c;
    }

    public c a(k kVar) {
        List listC = c(kVar);
        synchronized (listC) {
            int size = listC.size() - 1;
            if (size < 0) {
                return new c(kVar);
            }
            c cVar = (c) listC.remove(size);
            List listD = d(kVar);
            cVar.a(true);
            if (listD.size() > 1000) {
                C.b("Checked Out msg count: " + listD.size());
            }
            return cVar;
        }
    }

    public void a(k kVar, c cVar) {
        List listC = c(kVar);
        d(kVar).remove(cVar);
        listC.add(cVar);
    }

    private List c(k kVar) {
        List arrayList = (List) this.f7551a.get(kVar);
        if (arrayList == null) {
            arrayList = new ArrayList();
            this.f7551a.put(kVar, arrayList);
        }
        return arrayList;
    }

    private List d(k kVar) {
        List arrayList = (List) this.f7552b.get(kVar);
        if (arrayList == null) {
            arrayList = new ArrayList();
            this.f7552b.put(kVar, arrayList);
        }
        return arrayList;
    }

    public void b(k kVar) {
        List list = (List) this.f7552b.get(kVar);
        if (list != null) {
            list.clear();
        }
        List list2 = (List) this.f7551a.get(kVar);
        if (list2 != null) {
            list2.clear();
        }
    }
}
