package ao;

import L.C0169z;
import W.C0184j;
import W.C0188n;
import com.sun.org.apache.xerces.internal.impl.xs.SchemaSymbols;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import k.C1756d;

/* renamed from: ao.ea, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ao/ea.class */
class C0717ea implements bE.p {

    /* renamed from: g, reason: collision with root package name */
    private C0188n f5607g = null;

    /* renamed from: a, reason: collision with root package name */
    C0184j f5608a = null;

    /* renamed from: b, reason: collision with root package name */
    C0184j f5609b = null;

    /* renamed from: c, reason: collision with root package name */
    C0184j f5610c = null;

    /* renamed from: d, reason: collision with root package name */
    List f5611d = new ArrayList();

    /* renamed from: e, reason: collision with root package name */
    HashMap f5612e = new HashMap();

    /* renamed from: f, reason: collision with root package name */
    C0169z f5613f = new C0169z();

    /* renamed from: h, reason: collision with root package name */
    private final C0764fu f5614h;

    C0717ea(C0764fu c0764fu) {
        this.f5614h = c0764fu;
    }

    @Override // bE.p
    public double a() {
        if (this.f5608a == null) {
            return 0.0d;
        }
        return this.f5614h.f5876q.a() ? (this.f5609b == null || !n()) ? this.f5608a.e() : j() : this.f5614h.f5876q.b();
    }

    private double j() {
        double dA;
        double dA2;
        if (this.f5609b.a().toLowerCase().contains("lat")) {
            dA = this.f5613f.a(this.f5609b.h(), this.f5608a.h(), this.f5609b.g(), this.f5608a.h());
            dA2 = this.f5613f.a(this.f5609b.h(), this.f5608a.h(), this.f5609b.h(), this.f5608a.g());
        } else {
            dA = this.f5613f.a(this.f5608a.h(), this.f5609b.h(), this.f5608a.g(), this.f5609b.h());
            dA2 = this.f5613f.a(this.f5608a.h(), this.f5609b.h(), this.f5608a.h(), this.f5609b.g());
        }
        if (dA2 > dA) {
            return this.f5608a.h() - ((this.f5608a.g() - this.f5608a.h()) * 0.05d);
        }
        double d2 = (dA / dA2) * 1.05d;
        double dH = (this.f5608a.h() + this.f5608a.g()) / 2.0f;
        return dH - ((dH - this.f5608a.e()) * d2);
    }

    @Override // bE.p
    public double b() {
        if (this.f5608a == null) {
            return 100.0d;
        }
        return this.f5614h.f5876q.a() ? (this.f5609b == null || !n()) ? this.f5608a.f() : k() : this.f5614h.f5876q.c();
    }

    private double k() {
        double dA;
        double dA2;
        if (this.f5609b.a().toLowerCase().contains("lat")) {
            dA = this.f5613f.a(this.f5609b.h(), this.f5608a.h(), this.f5609b.g(), this.f5608a.h());
            dA2 = this.f5613f.a(this.f5609b.h(), this.f5608a.h(), this.f5609b.h(), this.f5608a.g());
        } else {
            dA = this.f5613f.a(this.f5608a.h(), this.f5609b.h(), this.f5608a.g(), this.f5609b.h());
            dA2 = this.f5613f.a(this.f5608a.h(), this.f5609b.h(), this.f5608a.h(), this.f5609b.g());
        }
        if (dA2 > dA) {
            return this.f5608a.g() + ((this.f5608a.g() - this.f5608a.h()) * 0.05d);
        }
        double d2 = (dA / dA2) * 1.05d;
        double dH = (this.f5608a.h() + this.f5608a.g()) / 2.0f;
        return dH + ((this.f5608a.f() - dH) * d2);
    }

    @Override // bE.p
    public double c() {
        if (this.f5609b == null) {
            return 0.0d;
        }
        return this.f5614h.f5876q.d() ? (this.f5608a == null || !n()) ? this.f5609b.e() : l() : this.f5614h.f5876q.e();
    }

    private double l() {
        double dA;
        double dA2;
        if (this.f5609b.a().toLowerCase().contains("lat")) {
            dA = this.f5613f.a(this.f5609b.h(), this.f5608a.h(), this.f5609b.g(), this.f5608a.h());
            dA2 = this.f5613f.a(this.f5609b.h(), this.f5608a.h(), this.f5609b.h(), this.f5608a.g());
        } else {
            dA = this.f5613f.a(this.f5608a.h(), this.f5609b.h(), this.f5608a.g(), this.f5609b.h());
            dA2 = this.f5613f.a(this.f5608a.h(), this.f5609b.h(), this.f5608a.h(), this.f5609b.g());
        }
        if (dA > dA2) {
            return this.f5609b.h() - ((this.f5609b.g() - this.f5609b.h()) * 0.05d);
        }
        double d2 = (dA2 / dA) * 1.05d;
        double dH = (this.f5609b.h() + this.f5609b.g()) / 2.0f;
        return dH - ((dH - this.f5609b.e()) * d2);
    }

    @Override // bE.p
    public double d() {
        if (this.f5609b == null) {
            return 100.0d;
        }
        return this.f5614h.f5876q.d() ? (this.f5608a == null || !n()) ? this.f5609b.f() : m() : this.f5614h.f5876q.f();
    }

    private double m() {
        double dA;
        double dA2;
        if (this.f5609b.a().toLowerCase().contains("lat")) {
            dA = this.f5613f.a(this.f5609b.h(), this.f5608a.h(), this.f5609b.g(), this.f5608a.h());
            dA2 = this.f5613f.a(this.f5609b.h(), this.f5608a.h(), this.f5609b.h(), this.f5608a.g());
        } else {
            dA = this.f5613f.a(this.f5608a.h(), this.f5609b.h(), this.f5608a.g(), this.f5609b.h());
            dA2 = this.f5613f.a(this.f5608a.h(), this.f5609b.h(), this.f5608a.h(), this.f5609b.g());
        }
        if (dA > dA2) {
            return this.f5609b.g() + ((this.f5609b.g() - this.f5609b.h()) * 0.05d);
        }
        double d2 = (dA2 / dA) * 1.05d;
        double dH = (this.f5609b.h() + this.f5609b.g()) / 2.0f;
        return dH + ((this.f5609b.f() - dH) * d2);
    }

    private boolean n() {
        return (this.f5609b.a().toLowerCase().contains("lat") && this.f5608a.a().toLowerCase().contains(SchemaSymbols.ATTVAL_LONG)) || (this.f5609b.a().toLowerCase().contains(SchemaSymbols.ATTVAL_LONG) && this.f5608a.a().toLowerCase().contains("lat"));
    }

    private boolean b(int i2) {
        for (bx.j jVar : this.f5614h.f5849j.a()) {
            try {
            } catch (ax.U e2) {
                this.f5614h.f5849j.a(jVar.a(), false);
                e2.printStackTrace();
            }
            if (C1756d.a().a(d(jVar.c())).a(this.f5607g, i2) != 0.0d) {
                return true;
            }
        }
        return false;
    }

    private String d(String str) {
        String strC = (String) this.f5612e.get(str);
        if (strC == null) {
            strC = h.g.a().c(str);
            this.f5612e.put(str, strC);
        }
        return strC;
    }

    @Override // bE.p
    public bE.q a(int i2) {
        double dC = this.f5610c == null ? Double.NaN : this.f5610c.c(i2);
        return b(i2) ? new bE.c(this.f5608a.c(i2), this.f5609b.c(i2), dC) : new bE.b(this.f5608a.c(i2), this.f5609b.c(i2), dC);
    }

    @Override // bE.p
    public int e() {
        if (this.f5608a == null || this.f5609b == null) {
            return 0;
        }
        return this.f5614h.f();
    }

    @Override // bE.p
    public int f() {
        return this.f5614h.g();
    }

    public void a(String str) {
        C0184j c0184jA = g().a(str);
        this.f5608a = c0184jA;
        if (c0184jA != null) {
            this.f5614h.i().h(c0184jA.m());
        }
        o();
    }

    public void b(String str) {
        C0184j c0184jA = g().a(str);
        this.f5609b = c0184jA;
        if (c0184jA != null) {
            this.f5614h.i().i(c0184jA.m());
        }
        o();
    }

    public void c(String str) {
        C0184j c0184jA = g().a(str);
        this.f5610c = c0184jA;
        if (c0184jA != null) {
            this.f5614h.i().j(c0184jA.m());
        }
        o();
    }

    public C0188n g() {
        return this.f5607g;
    }

    public void a(C0188n c0188n) {
        this.f5607g = c0188n;
        this.f5612e.clear();
        o();
    }

    @Override // bE.p
    public void a(bE.l lVar) {
        this.f5611d.add(lVar);
    }

    @Override // bE.p
    public void b(bE.l lVar) {
        this.f5611d.remove(lVar);
    }

    private void o() {
        Iterator it = this.f5611d.iterator();
        while (it.hasNext()) {
            ((bE.l) it.next()).a();
        }
    }

    @Override // bE.p
    public double h() {
        if (this.f5610c == null) {
            return Double.NaN;
        }
        return this.f5614h.f5876q.g() ? this.f5610c.e() : this.f5614h.f5876q.h();
    }

    @Override // bE.p
    public double i() {
        if (this.f5610c == null) {
            return Double.NaN;
        }
        return this.f5614h.f5876q.g() ? this.f5610c.f() : this.f5614h.f5876q.i();
    }
}
