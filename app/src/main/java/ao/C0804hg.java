package ao;

import W.C0184j;
import W.C0188n;
import i.C1743c;
import i.InterfaceC1741a;
import i.InterfaceC1742b;
import i.InterfaceC1749i;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;

/* renamed from: ao.hg, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ao/hg.class */
public class C0804hg implements InterfaceC0747fd {

    /* renamed from: m, reason: collision with root package name */
    private C0649bm f6057m = null;

    /* renamed from: n, reason: collision with root package name */
    private int f6059n = 0;

    /* renamed from: o, reason: collision with root package name */
    private double f6060o = 2.0d;

    /* renamed from: p, reason: collision with root package name */
    private C0188n f6061p = null;

    /* renamed from: q, reason: collision with root package name */
    private C0188n f6062q = null;

    /* renamed from: r, reason: collision with root package name */
    private int f6063r = 0;

    /* renamed from: b, reason: collision with root package name */
    ArrayList f6064b = new ArrayList();

    /* renamed from: c, reason: collision with root package name */
    ArrayList f6065c = new ArrayList();

    /* renamed from: d, reason: collision with root package name */
    ArrayList f6066d = new ArrayList();

    /* renamed from: e, reason: collision with root package name */
    ArrayList f6067e = new ArrayList();

    /* renamed from: f, reason: collision with root package name */
    ArrayList f6068f = new ArrayList();

    /* renamed from: g, reason: collision with root package name */
    ArrayList f6069g = new ArrayList();

    /* renamed from: h, reason: collision with root package name */
    ArrayList f6070h = new ArrayList();

    /* renamed from: i, reason: collision with root package name */
    ArrayList f6071i = new ArrayList();

    /* renamed from: j, reason: collision with root package name */
    C0748fe f6072j = null;

    /* renamed from: s, reason: collision with root package name */
    private int f6073s = 3;

    /* renamed from: k, reason: collision with root package name */
    double[] f6074k = {0.125d, 0.25d, 0.5d, 1.0d, 2.0d, 4.0d, 8.0d};

    /* renamed from: t, reason: collision with root package name */
    private String[] f6075t = null;

    /* renamed from: l, reason: collision with root package name */
    private static C0804hg f6056l = null;

    /* renamed from: a, reason: collision with root package name */
    public static final int f6058a = com.efiAnalytics.ui.eJ.a(40);

    private C0804hg() {
        C1743c.a().a(new C0805hh(this));
    }

    public static C0804hg a() {
        if (f6056l == null) {
            f6056l = new C0804hg();
        }
        return f6056l;
    }

    public void a(gO gOVar) {
        this.f6069g.add(gOVar);
    }

    public void a(boolean z2) {
        Iterator it = this.f6069g.iterator();
        while (it.hasNext()) {
            gO gOVar = (gO) it.next();
            if (gOVar != null) {
                gOVar.b(z2);
            }
        }
    }

    public void b(gO gOVar) {
        this.f6068f.add(gOVar);
    }

    public void b(boolean z2) {
        Iterator it = this.f6068f.iterator();
        while (it.hasNext()) {
            ((gO) it.next()).b(z2);
        }
    }

    public void c(gO gOVar) {
        this.f6067e.add(gOVar);
    }

    public void c(boolean z2) {
        h.i.c("showDashboard", z2 + "");
        Iterator it = this.f6067e.iterator();
        while (it.hasNext()) {
            gO gOVar = (gO) it.next();
            if (gOVar != null) {
                gOVar.b(z2);
            }
        }
    }

    public void a(InterfaceC0763ft interfaceC0763ft) {
        this.f6070h.add(interfaceC0763ft);
    }

    public void b() {
        Iterator it = this.f6070h.iterator();
        while (it.hasNext()) {
            ((InterfaceC0763ft) it.next()).f();
        }
    }

    public void a(InterfaceC1742b interfaceC1742b) {
        this.f6066d.add(interfaceC1742b);
    }

    public void a(InterfaceC1749i interfaceC1749i) {
        C1743c.a().a(interfaceC1749i);
    }

    public void c() {
        C1743c.a().b();
    }

    public void d() {
        C1743c.a().c();
    }

    public void e() {
        if (this.f6061p == null) {
            return;
        }
        if (this.f6072j != null && this.f6072j.f() && this.f6072j.isAlive()) {
            this.f6072j.e();
            return;
        }
        if (this.f6072j != null) {
            this.f6072j.b();
        }
        this.f6072j = new C0748fe(this, this.f6074k[this.f6073s]);
        this.f6072j.start();
    }

    public void f() {
        if (this.f6072j == null || !this.f6072j.isAlive()) {
            return;
        }
        this.f6072j.c();
    }

    public void a(int i2, boolean z2) {
        this.f6073s = i2;
        if (this.f6072j != null && this.f6072j.isAlive()) {
            this.f6072j.a(this.f6074k[this.f6073s]);
            this.f6072j.e();
        }
        Iterator it = this.f6065c.iterator();
        while (it.hasNext()) {
            gR gRVar = (gR) it.next();
            if (gRVar != null) {
                gRVar.c(this.f6074k[this.f6073s]);
            }
        }
        if (z2) {
            h.i.c("playbackSpeed", "" + this.f6074k[this.f6073s]);
        }
    }

    public void a(double d2, boolean z2) {
        for (int i2 = 0; i2 < this.f6074k.length; i2++) {
            if (this.f6074k[i2] == d2) {
                a(i2, z2);
                if (z2) {
                    h.i.c("playbackSpeed", d2 + "");
                    return;
                }
                return;
            }
        }
    }

    public void g() {
        if (this.f6073s < this.f6074k.length - 1) {
            int i2 = this.f6073s + 1;
            this.f6073s = i2;
            a(i2, true);
        }
    }

    public void h() {
        if (this.f6073s > 0) {
            int i2 = this.f6073s - 1;
            this.f6073s = i2;
            a(i2, true);
        }
    }

    public double i() {
        return this.f6074k[this.f6073s];
    }

    public void j() {
        if (this.f6061p == null || this.f6072j == null) {
            return;
        }
        this.f6072j.b();
    }

    public void k() {
        if (this.f6061p == null || this.f6072j == null) {
            return;
        }
        this.f6072j.d();
    }

    public void a(InterfaceC0813k interfaceC0813k) {
        this.f6064b.add(interfaceC0813k);
    }

    public void a(Color color, int i2) {
        Iterator it = this.f6064b.iterator();
        while (it.hasNext()) {
            ((InterfaceC0813k) it.next()).a(color, i2);
        }
    }

    public void a(Color color) {
        Iterator it = this.f6064b.iterator();
        while (it.hasNext()) {
            ((InterfaceC0813k) it.next()).a(color);
        }
    }

    public void a(InterfaceC1741a interfaceC1741a) {
        C1743c.a().a(interfaceC1741a);
    }

    protected void b(int i2) {
        C1743c.a().a(i2);
        q();
    }

    public void l() {
        if (r() == null || p() >= r().d() - 1) {
            return;
        }
        c(p() + 1);
    }

    public void m() {
        if (r() == null || p() <= 0) {
            return;
        }
        c(p() - 1);
        f();
    }

    public void n() {
        if (r() != null) {
            c(0);
            f();
        }
    }

    public void o() {
        if (r() != null) {
            c(this.f6061p.d() - 1);
            f();
        }
    }

    public int p() {
        return this.f6059n;
    }

    public void c(int i2) {
        this.f6059n = i2;
        b(this.f6059n);
    }

    protected void q() {
        Iterator it = this.f6066d.iterator();
        while (it.hasNext()) {
            InterfaceC1742b interfaceC1742b = (InterfaceC1742b) it.next();
            if (interfaceC1742b != null && this.f6061p != null) {
                interfaceC1742b.a((this.f6059n + 1) / this.f6061p.d());
            }
        }
    }

    protected void a(C0188n c0188n) {
        Iterator it = this.f6066d.iterator();
        while (it.hasNext()) {
            InterfaceC1742b interfaceC1742b = (InterfaceC1742b) it.next();
            if (interfaceC1742b != null && c0188n != null) {
                interfaceC1742b.a(c0188n);
            }
        }
    }

    public C0188n r() {
        return this.f6061p;
    }

    public void b(C0188n c0188n) {
        this.f6061p = c0188n;
        a(c0188n);
        if (this.f6072j != null) {
            this.f6072j.a();
            this.f6072j.c();
        }
        if (c0188n == null || c0188n.isEmpty()) {
            this.f6075t = null;
        }
    }

    public C0188n s() {
        return this.f6062q;
    }

    public void c(C0188n c0188n) {
        this.f6062q = c0188n;
    }

    public double t() {
        return this.f6060o;
    }

    public void u() {
        if (r() != null && this.f6060o <= 0.019d) {
            b(a(this.f6060o + 0.001d), true);
            return;
        }
        if (r() != null && this.f6060o <= 0.19d) {
            b(a(this.f6060o + 0.01d), true);
            return;
        }
        if (r() != null && this.f6060o < 1.0d) {
            b(a(this.f6060o + 0.1d), true);
            return;
        }
        if (r() != null && this.f6060o < 2.0d) {
            b(a(this.f6060o + 0.25d), true);
            return;
        }
        if (r() != null && this.f6060o < 4.0d) {
            b(a(this.f6060o + 0.5d), true);
        } else {
            if (r() == null || this.f6060o >= f6058a) {
                return;
            }
            b(this.f6060o + 1.0d, true);
        }
    }

    public void v() {
        if (r() != null && this.f6060o >= 4.0d) {
            b(this.f6060o - 1.0d, true);
            return;
        }
        if (r() != null && this.f6060o >= 2.0d) {
            b(this.f6060o - 0.5d, true);
            return;
        }
        if (r() != null && this.f6060o > 1.0d) {
            b(this.f6060o - 0.25d, true);
            return;
        }
        if (r() != null && this.f6060o >= 0.125d) {
            b(a(this.f6060o - 0.1d), true);
            return;
        }
        if (r() != null && this.f6060o >= 0.02d) {
            b(a(this.f6060o - 0.01d), true);
        } else {
            if (r() == null || this.f6060o < 0.0015d) {
                return;
            }
            b(a(this.f6060o - 0.001d), true);
        }
    }

    private double a(double d2) {
        return Math.round(d2 * 10000.0d) / 10000.0d;
    }

    public void w() {
        if (r() != null) {
            C0625ap c0625apC = C0645bi.a().c();
            if (this.f6059n + this.f6063r < this.f6061p.d()) {
                c(c0625apC.A() + this.f6063r);
                c0625apC.q();
            } else {
                c(this.f6061p.d() - 1);
            }
            f();
        }
    }

    public void x() {
        if (r() != null) {
            if (this.f6059n + (this.f6063r / 4) < this.f6061p.d()) {
                c(this.f6059n + (this.f6063r / 4));
            } else {
                c(this.f6061p.d() - 1);
            }
            f();
        }
    }

    public void y() {
        if (r() == null || this.f6059n - (this.f6063r / 4) <= 0) {
            return;
        }
        c(this.f6059n - (this.f6063r / 4));
        f();
    }

    public void z() {
        C0625ap c0625apC = C0645bi.a().c();
        if (r() == null || c0625apC.A() <= 0) {
            c(0);
            c0625apC.q();
        } else {
            c(c0625apC.A());
            c0625apC.q();
        }
        f();
    }

    public void b(double d2, boolean z2) {
        if (this.f6060o != d2) {
            this.f6060o = d2;
            if (z2) {
                h.i.c("zoom", d2 + "");
            }
            Iterator it = this.f6071i.iterator();
            while (it.hasNext()) {
                InterfaceC0758fo interfaceC0758fo = (InterfaceC0758fo) it.next();
                if (interfaceC0758fo != null) {
                    interfaceC0758fo.b(d2);
                }
            }
        }
    }

    public void a(InterfaceC0758fo interfaceC0758fo) {
        this.f6071i.add(interfaceC0758fo);
    }

    @Override // ao.InterfaceC0747fd
    public void a(int i2) {
        this.f6063r = i2;
    }

    public void a(gR gRVar) {
        this.f6065c.add(gRVar);
    }

    public C0649bm A() {
        if (this.f6057m == null) {
            this.f6057m = new C0649bm();
        }
        return this.f6057m;
    }

    public void B() {
        if (this.f6061p != null) {
            c(0);
            this.f6061p = null;
        }
    }

    public void C() {
        if (this.f6061p == null || this.f6061p.d() <= 10) {
            return;
        }
        C0645bi.a().e().a(this.f6061p.d());
        c(this.f6061p.d() / 2);
    }

    public boolean D() {
        return this.f6072j != null && !this.f6072j.f() && this.f6072j.f5798c && this.f6072j.isAlive();
    }

    public void a(C0184j c0184j) {
        h.i.c("fieldSmoothingFactor_" + c0184j.a(), "0");
        c0184j.b(false);
        if (s() != null && s().a(c0184j.a()) != null) {
            s().a(c0184j.a()).b(false);
        }
        C0645bi.a().c().i();
        C0645bi.a().c().repaint();
    }

    public void a(C0184j c0184j, int i2) {
        h.i.c("fieldSmoothingFactor_" + c0184j.a(), Integer.toString(i2));
        c0184j.b(true);
        c0184j.g(i2);
        if (s() != null && s().a(c0184j.a()) != null) {
            C0184j c0184jA = s().a(c0184j.a());
            c0184jA.g(i2);
            c0184jA.b(true);
        }
        C0645bi.a().c().i();
        C0645bi.a().c().repaint();
    }

    public String[] E() {
        return this.f6075t;
    }

    public void a(String[] strArr) {
        this.f6075t = strArr;
    }
}
