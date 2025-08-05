package i;

import W.C0188n;
import bH.C;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/* renamed from: i.c, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:i/c.class */
public class C1743c {

    /* renamed from: a, reason: collision with root package name */
    List f12344a = new CopyOnWriteArrayList();

    /* renamed from: b, reason: collision with root package name */
    List f12345b = new CopyOnWriteArrayList();

    /* renamed from: c, reason: collision with root package name */
    List f12346c = new CopyOnWriteArrayList();

    /* renamed from: d, reason: collision with root package name */
    private InterfaceC1744d f12347d = null;

    /* renamed from: e, reason: collision with root package name */
    private static C1743c f12348e = null;

    private C1743c() {
    }

    public static C1743c a() {
        if (f12348e == null) {
            f12348e = new C1743c();
        }
        return f12348e;
    }

    public void a(InterfaceC1749i interfaceC1749i) {
        this.f12345b.add(interfaceC1749i);
    }

    public void b(InterfaceC1749i interfaceC1749i) {
        this.f12345b.remove(interfaceC1749i);
    }

    public void b() {
        Iterator it = this.f12345b.iterator();
        while (it.hasNext()) {
            ((InterfaceC1749i) it.next()).a();
        }
    }

    public void c() {
        Iterator it = this.f12345b.iterator();
        while (it.hasNext()) {
            ((InterfaceC1749i) it.next()).b();
        }
    }

    public void a(InterfaceC1741a interfaceC1741a) {
        if (this.f12344a.contains(interfaceC1741a)) {
            return;
        }
        this.f12344a.add(interfaceC1741a);
    }

    public void b(InterfaceC1741a interfaceC1741a) {
        this.f12344a.remove(interfaceC1741a);
    }

    public void a(int i2) {
        for (InterfaceC1741a interfaceC1741a : this.f12344a) {
            if (interfaceC1741a != null) {
                try {
                    interfaceC1741a.a(i2);
                } catch (Exception e2) {
                    C.a(e2);
                }
            }
        }
    }

    public boolean d() {
        if (this.f12347d == null) {
            return false;
        }
        return this.f12347d.a();
    }

    public void a(InterfaceC1744d interfaceC1744d) {
        this.f12347d = interfaceC1744d;
    }

    public C0188n e() {
        if (this.f12347d == null) {
            return null;
        }
        return this.f12347d.b();
    }

    public int f() {
        if (this.f12347d == null) {
            return -1;
        }
        return this.f12347d.c();
    }

    public void a(InterfaceC1742b interfaceC1742b) {
        this.f12346c.add(interfaceC1742b);
    }

    public Iterator g() {
        return this.f12346c.iterator();
    }

    public void b(InterfaceC1742b interfaceC1742b) {
        this.f12346c.remove(interfaceC1742b);
    }
}
