package ay;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* renamed from: ay.a, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ay/a.class */
public class C0924a implements InterfaceC0928e {

    /* renamed from: h, reason: collision with root package name */
    private static C0924a f6424h = null;

    /* renamed from: a, reason: collision with root package name */
    C0929f f6425a = C0929f.a();

    /* renamed from: b, reason: collision with root package name */
    List f6426b = new ArrayList();

    /* renamed from: c, reason: collision with root package name */
    List f6427c = new ArrayList();

    /* renamed from: d, reason: collision with root package name */
    List f6428d = new ArrayList();

    /* renamed from: e, reason: collision with root package name */
    List f6429e = new ArrayList();

    /* renamed from: f, reason: collision with root package name */
    C0925b f6430f = null;

    /* renamed from: g, reason: collision with root package name */
    final Object f6431g = new Object();

    private C0924a() {
    }

    public static C0924a c() {
        if (f6424h == null) {
            f6424h = new C0924a();
        }
        return f6424h;
    }

    public void a(InterfaceC0928e interfaceC0928e) {
        this.f6426b.add(interfaceC0928e);
    }

    public void a(InterfaceC0939p interfaceC0939p) {
        this.f6427c.add(interfaceC0939p);
        synchronized (this.f6431g) {
            Iterator it = this.f6428d.iterator();
            while (it.hasNext()) {
                interfaceC0939p.d((C0926c) it.next());
            }
        }
    }

    public void b(InterfaceC0939p interfaceC0939p) {
        this.f6427c.remove(interfaceC0939p);
    }

    public List d() {
        return this.f6428d;
    }

    private void h() {
        this.f6429e.clear();
        Iterator it = this.f6426b.iterator();
        while (it.hasNext()) {
            ((InterfaceC0928e) it.next()).a();
        }
    }

    private void i() {
        j();
        Iterator it = this.f6426b.iterator();
        while (it.hasNext()) {
            ((InterfaceC0928e) it.next()).b();
        }
    }

    private void j() {
        synchronized (this.f6431g) {
            List<C0926c> list = this.f6428d;
            List<C0926c> list2 = this.f6429e;
            this.f6428d = this.f6429e;
            this.f6429e = list;
            for (C0926c c0926c : list) {
                if (!a(list2, c0926c)) {
                    c(c0926c);
                }
            }
            for (C0926c c0926c2 : list2) {
                if (!a(list, c0926c2)) {
                    b(c0926c2);
                }
            }
        }
    }

    private void b(C0926c c0926c) {
        Iterator it = this.f6427c.iterator();
        while (it.hasNext()) {
            ((InterfaceC0939p) it.next()).d(c0926c);
        }
    }

    private void c(C0926c c0926c) {
        Iterator it = this.f6427c.iterator();
        while (it.hasNext()) {
            ((InterfaceC0939p) it.next()).c(c0926c);
        }
    }

    private boolean a(List list, C0926c c0926c) {
        Iterator it = list.iterator();
        while (it.hasNext()) {
            C0926c c0926c2 = (C0926c) it.next();
            if (c0926c2.a().equals(c0926c.a()) && c0926c2.c().equals(c0926c.c())) {
                return true;
            }
        }
        return false;
    }

    private void d(C0926c c0926c) {
        this.f6429e.add(c0926c);
        Iterator it = this.f6426b.iterator();
        while (it.hasNext()) {
            ((InterfaceC0928e) it.next()).a(c0926c);
        }
    }

    @Override // ay.InterfaceC0928e
    public void a(C0926c c0926c) {
        d(c0926c);
    }

    @Override // ay.InterfaceC0928e
    public void a() {
        h();
    }

    @Override // ay.InterfaceC0928e
    public void b() {
        i();
    }

    public boolean e() {
        return this.f6430f != null && this.f6430f.isAlive();
    }

    public void f() {
        if (this.f6430f == null || !this.f6430f.isAlive()) {
            return;
        }
        this.f6430f.a();
    }

    public void g() {
        f();
        this.f6430f = new C0925b(this);
        this.f6425a.a(this);
        this.f6430f.start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void k() {
        this.f6425a.b();
    }

    public boolean a(String str) {
        Iterator it = this.f6428d.iterator();
        while (it.hasNext()) {
            if (((C0926c) it.next()).a().equals(str)) {
                return true;
            }
        }
        return false;
    }
}
