package W;

import java.util.ArrayList;
import java.util.Iterator;

/* renamed from: W.c, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:W/c.class */
public class C0177c {

    /* renamed from: b, reason: collision with root package name */
    private int f2121b = 0;

    /* renamed from: c, reason: collision with root package name */
    private int f2122c = 0;

    /* renamed from: d, reason: collision with root package name */
    private int f2123d = 0;

    /* renamed from: e, reason: collision with root package name */
    private C0170a f2124e = null;

    /* renamed from: a, reason: collision with root package name */
    ArrayList f2125a = new ArrayList();

    public void a(C0170a c0170a) {
        this.f2125a.add(c0170a);
    }

    public C0170a a(int i2) {
        return (C0170a) this.f2125a.get(i2);
    }

    public int a() {
        return this.f2125a.size();
    }

    public int b() {
        return this.f2121b;
    }

    public void b(int i2) {
        this.f2121b = i2;
    }

    public int c() {
        return this.f2123d + this.f2122c;
    }

    public Iterator d() {
        return this.f2125a.iterator();
    }

    public int e() {
        return this.f2122c;
    }

    public void c(int i2) {
        this.f2122c = i2;
    }

    public int f() {
        return this.f2123d;
    }

    public void d(int i2) {
        this.f2123d = i2;
    }

    public C0170a g() {
        return this.f2124e;
    }

    public void b(C0170a c0170a) {
        this.f2124e = c0170a;
    }

    public void a(String str) {
        Iterator it = this.f2125a.iterator();
        while (it.hasNext()) {
            if (((C0170a) it.next()).a().equals(str)) {
                it.remove();
            }
        }
    }
}
