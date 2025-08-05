package B;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/* loaded from: TunerStudioMS.jar:B/g.class */
public class g implements k {

    /* renamed from: g, reason: collision with root package name */
    private static g f146g = null;

    /* renamed from: f, reason: collision with root package name */
    private final List f145f = new ArrayList();

    /* renamed from: a, reason: collision with root package name */
    h f147a = null;

    /* renamed from: b, reason: collision with root package name */
    List f148b = new ArrayList();

    /* renamed from: c, reason: collision with root package name */
    Map f149c = new HashMap();

    /* renamed from: d, reason: collision with root package name */
    Map f150d = new HashMap();

    /* renamed from: e, reason: collision with root package name */
    Map f151e = new HashMap();

    private g() {
    }

    public static g a() {
        if (f146g == null) {
            f146g = new g();
        }
        return f146g;
    }

    public void a(k kVar) {
        this.f148b.add(kVar);
        if (b()) {
            Iterator it = this.f145f.iterator();
            while (it.hasNext()) {
                e.a(((Integer) it.next()).intValue()).a(kVar);
            }
        }
    }

    public void b(k kVar) {
        this.f148b.remove(kVar);
        if (b()) {
            Iterator it = this.f145f.iterator();
            while (it.hasNext()) {
                e.a(((Integer) it.next()).intValue()).b(kVar);
            }
        }
    }

    public void a(int i2) {
        this.f145f.add(Integer.valueOf(i2));
    }

    public boolean b() {
        return this.f147a != null && this.f147a.isAlive();
    }

    public void c() {
        Iterator it = this.f145f.iterator();
        while (it.hasNext()) {
            e eVarA = e.a(((Integer) it.next()).intValue());
            Iterator it2 = this.f148b.iterator();
            while (it2.hasNext()) {
                eVarA.b((k) it2.next());
            }
            eVarA.b(this);
        }
        if (this.f147a == null || !this.f147a.isAlive()) {
            return;
        }
        this.f147a.a();
    }

    public void d() {
        c();
        this.f147a = new h(this);
        Iterator it = this.f145f.iterator();
        while (it.hasNext()) {
            e eVarA = e.a(((Integer) it.next()).intValue());
            Iterator it2 = this.f148b.iterator();
            while (it2.hasNext()) {
                eVarA.a((k) it2.next());
            }
            eVarA.a(this);
        }
        this.f147a.start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void g() {
        Iterator it = this.f145f.iterator();
        while (it.hasNext()) {
            e.a(((Integer) it.next()).intValue()).a();
        }
    }

    @Override // B.k
    public void a(i iVar) {
        i iVar2;
        if (iVar.e() != null && !iVar.e().isEmpty() && ((iVar2 = (i) this.f150d.get(iVar.e())) == null || !iVar2.equals(iVar))) {
            a(iVar.e(), iVar);
            this.f150d.put(iVar.e(), iVar);
        }
        this.f151e.put(iVar.c(), iVar);
    }

    public void a(String str, j jVar) {
        List arrayList = (List) this.f149c.get(str);
        if (arrayList == null) {
            arrayList = new ArrayList();
            this.f149c.put(str, arrayList);
        }
        arrayList.add(jVar);
    }

    public void b(String str, j jVar) {
        List list = (List) this.f149c.get(str);
        if (list != null) {
            list.remove(jVar);
        }
    }

    private void a(String str, i iVar) {
        Iterator it = c(str).iterator();
        while (it.hasNext()) {
            ((j) it.next()).a(str, iVar);
        }
    }

    private List c(String str) {
        List arrayList = (List) this.f149c.get(str);
        if (arrayList == null) {
            arrayList = new ArrayList();
            this.f149c.put(str, arrayList);
        }
        return arrayList;
    }

    public i a(String str) {
        return (i) this.f151e.get(str);
    }

    public i b(String str) {
        return (i) this.f150d.get(str);
    }

    public Collection e() {
        return this.f150d.values();
    }

    public void f() {
        this.f149c.clear();
    }
}
