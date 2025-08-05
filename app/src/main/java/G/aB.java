package G;

import java.util.ArrayList;
import java.util.Iterator;

/* loaded from: TunerStudioMS.jar:G/aB.class */
public class aB {

    /* renamed from: a, reason: collision with root package name */
    private static aB f526a = null;

    /* renamed from: b, reason: collision with root package name */
    private ArrayList f527b = new ArrayList();

    /* renamed from: c, reason: collision with root package name */
    private ArrayList f528c = new ArrayList();

    /* renamed from: d, reason: collision with root package name */
    private ArrayList f529d = new ArrayList();

    private aB() {
    }

    public static aB a() {
        if (f526a == null) {
            f526a = new aB();
        }
        return f526a;
    }

    public void a(bM bMVar) {
        this.f527b.add(bMVar);
    }

    public void a(dm dmVar) {
        this.f529d.add(dmVar);
    }

    public void a(aT aTVar) {
        this.f528c.add(aTVar);
    }

    public void b() {
        Iterator it = this.f529d.iterator();
        while (it.hasNext()) {
            ((dm) it.next()).c();
        }
    }

    public void a(String str, double d2) {
        Iterator it = this.f529d.iterator();
        while (it.hasNext()) {
            ((dm) it.next()).a(str, d2);
        }
    }

    public void c() {
        Iterator it = this.f529d.iterator();
        while (it.hasNext()) {
            ((dm) it.next()).d();
        }
    }

    public void d() {
        Iterator it = this.f529d.iterator();
        while (it.hasNext()) {
            ((dm) it.next()).a();
        }
    }

    public void e() {
        Iterator it = this.f529d.iterator();
        while (it.hasNext()) {
            ((dm) it.next()).b();
        }
    }

    public void a(String str) {
        a(T.a().c().c(), str);
    }

    protected void a(String str, String str2) {
        Iterator it = this.f527b.iterator();
        while (it.hasNext()) {
            ((bM) it.next()).b(str, str2);
        }
    }

    public void b(String str, String str2) {
        Iterator it = this.f528c.iterator();
        while (it.hasNext()) {
            ((aT) it.next()).a(str, str2);
        }
    }
}
