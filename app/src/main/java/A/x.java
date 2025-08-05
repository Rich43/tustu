package A;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/* loaded from: TunerStudioMS.jar:A/x.class */
public class x {

    /* renamed from: c, reason: collision with root package name */
    private f f85c = null;

    /* renamed from: a, reason: collision with root package name */
    HashMap f86a = new HashMap();

    /* renamed from: b, reason: collision with root package name */
    List f87b = new ArrayList();

    public void a(String str) {
        this.f87b.add(str);
    }

    public List a() {
        return this.f87b;
    }

    public void a(String str, Object obj) {
        c(str).add(obj);
    }

    public List b() {
        return new ArrayList(this.f86a.keySet());
    }

    public List b(String str) {
        return (List) this.f86a.get(str);
    }

    public int c() {
        int size = -1;
        for (List list : this.f86a.values()) {
            size = size == -1 ? list.size() : size * list.size();
        }
        return size;
    }

    private List c(String str) {
        List arrayList = (List) this.f86a.get(str);
        if (arrayList == null) {
            arrayList = new ArrayList();
            this.f86a.put(str, arrayList);
        }
        return arrayList;
    }

    public f d() {
        return this.f85c;
    }

    public void a(f fVar) {
        this.f85c = fVar;
    }
}
