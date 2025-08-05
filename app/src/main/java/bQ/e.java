package bQ;

import bH.C;
import java.util.ArrayList;
import java.util.List;

/* loaded from: TunerStudioMS.jar:bQ/e.class */
public class e {

    /* renamed from: c, reason: collision with root package name */
    private final u f7402c;

    /* renamed from: a, reason: collision with root package name */
    private final bO.a f7400a = new bO.a();

    /* renamed from: b, reason: collision with root package name */
    private final List f7401b = new ArrayList();

    /* renamed from: d, reason: collision with root package name */
    private int f7403d = -1;

    /* renamed from: e, reason: collision with root package name */
    private int f7404e = -1;

    /* renamed from: f, reason: collision with root package name */
    private int f7405f = -1;

    public e(String str, bN.k kVar) {
        this.f7402c = new u(str, kVar, this.f7400a);
    }

    public String a() {
        return this.f7402c.a();
    }

    public bO.a b() {
        return this.f7400a;
    }

    public bO.f c() {
        return this.f7400a.b();
    }

    public bO.h d() {
        return this.f7400a.c();
    }

    public List e() {
        return this.f7401b;
    }

    public u f() {
        return this.f7402c;
    }

    public void g() {
        this.f7402c.c();
    }

    public void a(int i2) {
        for (int i3 = 0; i3 < i2; i3++) {
            if (this.f7401b.size() <= i3) {
                this.f7401b.add(new bO.c());
            }
        }
        while (this.f7401b.size() > i2) {
            this.f7401b.remove(i2 - 1);
        }
        if (i2 >= 0) {
            this.f7402c.a(b(0));
        }
        if (i2 >= 1) {
            this.f7402c.b(b(1));
        }
    }

    public bO.c b(int i2) {
        if (i2 < 0 || i2 < this.f7401b.size()) {
            return (bO.c) this.f7401b.get(i2);
        }
        return null;
    }

    public bO.c a(bO.k kVar) {
        for (bO.c cVar : this.f7401b) {
            if (cVar.e().contains(kVar)) {
                return cVar;
            }
        }
        return null;
    }

    public bO.k c(int i2) {
        if (this.f7400a.b().e().a() != 0) {
            throw new bO.j("Only Absolute ODT Number currently supported.");
        }
        for (bO.c cVar : e()) {
            if (i2 >= cVar.k() && i2 <= cVar.k() + cVar.f()) {
                return cVar.c(i2 - cVar.k());
            }
        }
        C.c("No ODT found for ID: " + i2);
        return null;
    }

    public int h() {
        return this.f7403d;
    }

    public void d(int i2) {
        this.f7403d = i2;
    }

    public int i() {
        return this.f7404e;
    }

    public void e(int i2) {
        this.f7404e = i2;
    }

    public int j() {
        return this.f7405f;
    }

    public void f(int i2) {
        this.f7405f = i2;
    }

    public void k() {
        this.f7402c.b();
    }
}
