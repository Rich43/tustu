package aB;

import A.e;
import A.f;
import A.y;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* loaded from: TunerStudioMS.jar:aB/a.class */
public abstract class a implements f {

    /* renamed from: a, reason: collision with root package name */
    ArrayList f2271a = new ArrayList();

    /* renamed from: b, reason: collision with root package name */
    ArrayList f2272b = new ArrayList();

    @Override // A.f
    public void a(y yVar) {
        if (this.f2272b.contains(yVar)) {
            return;
        }
        this.f2272b.add(yVar);
    }

    @Override // A.f
    public void b(y yVar) {
        this.f2272b.remove(yVar);
    }

    protected void a(List list) {
        Iterator it = this.f2272b.iterator();
        while (it.hasNext()) {
            ((y) it.next()).a(list);
        }
    }

    @Override // A.f
    public void a(e eVar) {
        this.f2271a.add(eVar);
    }

    @Override // A.f
    public void b(e eVar) {
        this.f2271a.remove(eVar);
    }

    protected void a() {
        Iterator it = this.f2271a.iterator();
        while (it.hasNext()) {
            ((e) it.next()).a();
        }
    }

    protected void b() {
        Iterator it = this.f2271a.iterator();
        while (it.hasNext()) {
            ((e) it.next()).b();
        }
    }

    protected void c() {
        Iterator it = this.f2271a.iterator();
        while (it.hasNext()) {
            ((e) it.next()).c();
        }
    }

    protected void d() {
        Iterator it = this.f2271a.iterator();
        while (it.hasNext()) {
            ((e) it.next()).d();
        }
    }

    protected void e() {
        Iterator it = this.f2271a.iterator();
        while (it.hasNext()) {
            ((e) it.next()).e();
        }
    }
}
