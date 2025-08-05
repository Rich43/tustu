package ar;

import ao.C0645bi;
import bH.C;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.swing.Action;

/* renamed from: ar.f, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ar/f.class */
public class C0839f {

    /* renamed from: e, reason: collision with root package name */
    private static C0839f f6229e = null;

    /* renamed from: a, reason: collision with root package name */
    List f6225a = new ArrayList();

    /* renamed from: b, reason: collision with root package name */
    List f6226b = new CopyOnWriteArrayList();

    /* renamed from: d, reason: collision with root package name */
    private InterfaceC0845l f6227d = new C0834a();

    /* renamed from: c, reason: collision with root package name */
    List f6228c = new ArrayList();

    /* renamed from: f, reason: collision with root package name */
    private String f6230f = null;

    /* renamed from: g, reason: collision with root package name */
    private boolean f6231g = false;

    private C0839f() {
    }

    public static C0839f a() {
        if (f6229e == null) {
            f6229e = new C0839f();
        }
        return f6229e;
    }

    public List b() {
        if (this.f6225a.isEmpty()) {
            this.f6225a = f().a();
        }
        return this.f6225a;
    }

    public boolean a(String str) {
        long jCurrentTimeMillis = System.currentTimeMillis();
        for (C0836c c0836c : b()) {
            if (c0836c.b().equals(str)) {
                if (!i(c0836c)) {
                    return false;
                }
                if (!str.equals(Action.DEFAULT)) {
                }
                this.f6230f = c0836c.b();
                h(c0836c);
                C0645bi.a().c().k();
                C.c("Time to notify update: " + (System.currentTimeMillis() - jCurrentTimeMillis));
                return true;
            }
        }
        C.c("Invalid QuickView name.");
        return false;
    }

    public void a(InterfaceC0838e interfaceC0838e) {
        this.f6228c.add(interfaceC0838e);
    }

    public void a(C0836c c0836c) {
        if (this.f6231g) {
            return;
        }
        if (c(c0836c.b()) == null) {
            d(c0836c);
            return;
        }
        for (int i2 = 0; i2 < this.f6225a.size(); i2++) {
            if (((C0836c) this.f6225a.get(i2)).b().equals(c0836c.b()) && !((C0836c) this.f6225a.get(i2)).a(c0836c)) {
                this.f6225a.set(i2, c0836c);
                e(c0836c);
                h(c0836c.b());
                return;
            }
        }
    }

    private void e(C0836c c0836c) {
        if (this.f6231g) {
            return;
        }
        int i2 = 0;
        while (true) {
            if (i2 >= this.f6226b.size()) {
                break;
            }
            if (((C0836c) this.f6226b.get(i2)).b().equals(c0836c.b())) {
                this.f6226b.remove(i2);
                break;
            }
            i2++;
        }
        this.f6226b.add(c0836c);
        f(c0836c);
    }

    public List c() {
        return this.f6226b;
    }

    public boolean d() {
        return !this.f6226b.isEmpty();
    }

    public void b(C0836c c0836c) {
        if (this.f6231g) {
            return;
        }
        f().a(c0836c, e(c0836c.b()));
        c(c0836c);
    }

    private int e(String str) {
        for (int i2 = 0; i2 < this.f6225a.size(); i2++) {
            if (((C0836c) this.f6225a.get(i2)).b().equals(str)) {
                return i2;
            }
        }
        return -1;
    }

    public void e() {
        if (this.f6231g) {
            return;
        }
        Iterator it = this.f6226b.iterator();
        while (it.hasNext()) {
            b((C0836c) it.next());
        }
        this.f6226b.clear();
    }

    public void c(C0836c c0836c) {
        int i2 = 0;
        while (true) {
            if (i2 >= this.f6226b.size()) {
                break;
            }
            if (((C0836c) this.f6226b.get(i2)).b().equals(c0836c.b())) {
                this.f6226b.remove(i2);
                break;
            }
            i2++;
        }
        g(c0836c);
    }

    public void b(String str) {
        if (this.f6231g) {
            return;
        }
        String strG = g();
        C0836c c0836cC = c(str);
        int iE = c0836cC.e();
        f().b(str);
        this.f6226b.remove(c0836cC);
        if (c0836cC.f()) {
            C0836c c0836cA = this.f6227d.a(str);
            if (iE >= 0) {
                c0836cA.a(iE);
                this.f6225a.set(iE, c0836cA);
            }
            h(c0836cA);
            return;
        }
        if (c0836cC != null && c0836cC.b().equals(strG) && strG != null && c0836cC.b().equals(strG)) {
            a(((C0836c) this.f6225a.get(0)).b());
        }
        int i2 = 0;
        while (true) {
            if (i2 >= this.f6225a.size()) {
                break;
            }
            if (((C0836c) this.f6225a.get(i2)).b().equals(str)) {
                this.f6225a.remove(i2);
                break;
            }
            i2++;
        }
        g(str);
    }

    protected InterfaceC0845l f() {
        return this.f6227d;
    }

    public void a(InterfaceC0845l interfaceC0845l) {
        this.f6227d = interfaceC0845l;
    }

    public C0836c c(String str) {
        for (C0836c c0836c : b()) {
            if (c0836c.b().equals(str)) {
                return c0836c;
            }
        }
        return null;
    }

    public String g() {
        return this.f6230f;
    }

    public void d(String str) {
        C0836c c0836c = new C0836c(str);
        C0836c c0836cC = c(g());
        if (c0836cC != null) {
            Iterator it = c0836cC.c().iterator();
            while (it.hasNext()) {
                c0836c.a((C0837d) it.next());
            }
        }
        d(c0836c);
    }

    public void d(C0836c c0836c) {
        this.f6225a.add(c0836c);
        f().a(c0836c, this.f6225a.size() - 1);
        f(c0836c.b());
    }

    private void f(C0836c c0836c) {
        Iterator it = this.f6228c.iterator();
        while (it.hasNext()) {
            ((InterfaceC0838e) it.next()).b(c0836c);
        }
    }

    private void g(C0836c c0836c) {
        Iterator it = this.f6228c.iterator();
        while (it.hasNext()) {
            ((InterfaceC0838e) it.next()).c(c0836c);
        }
    }

    private void h(C0836c c0836c) {
        Iterator it = this.f6228c.iterator();
        while (it.hasNext()) {
            ((InterfaceC0838e) it.next()).a(c0836c);
        }
    }

    private boolean i(C0836c c0836c) {
        Iterator it = this.f6228c.iterator();
        while (it.hasNext()) {
            if (!((InterfaceC0838e) it.next()).a(this.f6230f, c0836c.b())) {
                return false;
            }
        }
        return true;
    }

    private void f(String str) {
        Iterator it = this.f6228c.iterator();
        while (it.hasNext()) {
            ((InterfaceC0838e) it.next()).a(str);
        }
    }

    private void g(String str) {
        Iterator it = this.f6228c.iterator();
        while (it.hasNext()) {
            ((InterfaceC0838e) it.next()).b(str);
        }
    }

    private void h(String str) {
        Iterator it = this.f6228c.iterator();
        while (it.hasNext()) {
            ((InterfaceC0838e) it.next()).c(str);
        }
    }

    public void a(boolean z2) {
        if (z2) {
            this.f6226b.clear();
        }
        this.f6231g = z2;
    }
}
