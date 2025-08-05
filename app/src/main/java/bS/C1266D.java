package bs;

import G.C0079bl;
import G.C0113cs;
import G.R;
import G.aF;
import G.aH;
import G.aM;
import G.dn;
import aP.C0404hl;
import bt.bO;
import com.efiAnalytics.ui.C1677fh;
import com.efiAnalytics.ui.C1701s;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/* renamed from: bs.D, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bs/D.class */
public final class C1266D implements aF {

    /* renamed from: p, reason: collision with root package name */
    private R f8540p;

    /* renamed from: q, reason: collision with root package name */
    private aM f8541q;

    /* renamed from: r, reason: collision with root package name */
    private aM f8542r;

    /* renamed from: s, reason: collision with root package name */
    private aM f8543s;

    /* renamed from: t, reason: collision with root package name */
    private aM f8544t;

    /* renamed from: a, reason: collision with root package name */
    ArrayList f8545a;

    /* renamed from: b, reason: collision with root package name */
    dn f8546b;

    /* renamed from: u, reason: collision with root package name */
    private C1701s f8547u;

    /* renamed from: c, reason: collision with root package name */
    aH f8548c;

    /* renamed from: d, reason: collision with root package name */
    aH f8549d;

    /* renamed from: e, reason: collision with root package name */
    aH f8550e;

    /* renamed from: f, reason: collision with root package name */
    aH f8551f;

    /* renamed from: g, reason: collision with root package name */
    aH f8552g;

    /* renamed from: h, reason: collision with root package name */
    aH f8553h;

    /* renamed from: j, reason: collision with root package name */
    double[] f8563j;

    /* renamed from: m, reason: collision with root package name */
    bM.b f8566m;

    /* renamed from: i, reason: collision with root package name */
    String f8554i = "";

    /* renamed from: v, reason: collision with root package name */
    private int f8555v = 0;

    /* renamed from: w, reason: collision with root package name */
    private int f8556w = 0;

    /* renamed from: x, reason: collision with root package name */
    private double f8557x = 100.0d;

    /* renamed from: y, reason: collision with root package name */
    private double f8558y = Double.NaN;

    /* renamed from: z, reason: collision with root package name */
    private double f8559z = Double.NaN;

    /* renamed from: A, reason: collision with root package name */
    private boolean f8560A = false;

    /* renamed from: B, reason: collision with root package name */
    private boolean f8561B = true;

    /* renamed from: C, reason: collision with root package name */
    private boolean f8562C = true;

    /* renamed from: k, reason: collision with root package name */
    long f8564k = 0;

    /* renamed from: l, reason: collision with root package name */
    C1267E f8565l = new C1267E(this);

    /* renamed from: n, reason: collision with root package name */
    List f8567n = new ArrayList();

    /* renamed from: o, reason: collision with root package name */
    bM.d f8568o = new bM.d();

    public C1266D(R r2, dn dnVar) throws V.a {
        this.f8540p = null;
        this.f8541q = null;
        this.f8542r = null;
        this.f8543s = null;
        this.f8544t = null;
        this.f8546b = null;
        this.f8547u = null;
        this.f8548c = null;
        this.f8549d = null;
        this.f8550e = null;
        this.f8551f = null;
        this.f8552g = null;
        this.f8553h = null;
        this.f8563j = null;
        this.f8540p = r2;
        this.f8546b = dnVar;
        this.f8566m = new bM.b(r2);
        this.f8541q = r2.c(dnVar.a());
        this.f8542r = r2.c(dnVar.b());
        C0079bl c0079bl = (C0079bl) r2.e().c(dnVar.g());
        if (c0079bl == null) {
            throw new V.a("Lambda Temperature compensation CurvePanel not found. " + dnVar.g());
        }
        this.f8543s = r2.c(c0079bl.b(0));
        if (this.f8543s == null) {
            throw new V.a("Lambda Temperature compensation Parameter not found. " + c0079bl.d(0));
        }
        this.f8544t = r2.c(dnVar.l());
        this.f8547u = bO.a().a(r2, dnVar.f());
        this.f8545a = C1272e.a().a(r2, dnVar);
        this.f8563j = K.d.a(r2, this.f8541q);
        if (dnVar.h() != null && !dnVar.h().equals("")) {
            this.f8551f = r2.g(dnVar.h());
        }
        this.f8550e = r2.g(dnVar.d());
        if (this.f8550e == null) {
            throw new V.a("WUE Analyze can not locate OutputChannel: " + dnVar.d() + " in the current Configuration.");
        }
        if (dnVar.n() == null) {
            dnVar.p(this.f8547u.w());
        }
        this.f8548c = r2.g(dnVar.n());
        if (this.f8548c == null) {
            throw new V.a("WUE Analyze can not locate AFR Table X Axis OutputChannel: " + dnVar.n() + " in the current Configuration.");
        }
        if (dnVar.o() == null) {
            dnVar.q(this.f8547u.v());
        }
        this.f8549d = r2.g(dnVar.o());
        if (this.f8549d == null) {
            throw new V.a("WUE Analyze can not locate AFR Table Y Axis OutputChannel: " + dnVar.o() + " in the current Configuration.");
        }
        this.f8552g = r2.g(dnVar.e());
        if (this.f8552g == null) {
            throw new V.a("WUE Analyze can not locate OutputChannel: " + dnVar.e() + " in the current Configuration.");
        }
        this.f8553h = r2.g(dnVar.k());
        if (this.f8552g == null) {
            throw new V.a("WUE Analyze can not locate OutputChannel: " + dnVar.k() + " in the current Configuration.");
        }
        try {
            m();
            n();
        } catch (V.g e2) {
            Logger.getLogger(C1266D.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            throw new V.a("Failed to start WUE Analyze.\n" + e2.getMessage());
        }
    }

    private void j() {
        Iterator it = this.f8567n.iterator();
        while (it.hasNext()) {
            ((bM.e) it.next()).a(this.f8568o);
        }
    }

    public void a(bM.e eVar) {
        this.f8567n.add(eVar);
    }

    public boolean a() {
        return this.f8560A;
    }

    public String b() {
        return this.f8554i;
    }

    void c() {
        this.f8547u = bO.a().a(this.f8540p, this.f8546b.f());
    }

    @Override // G.aF
    public void a(String str, byte[] bArr) {
        if (!this.f8560A || System.currentTimeMillis() - this.f8564k <= 60) {
            return;
        }
        this.f8564k = System.currentTimeMillis();
        bM.a aVar = new bM.a();
        try {
            double dB = this.f8552g.b(bArr);
            aVar.e(dB);
            if (Double.isNaN(g())) {
                this.f8558y = this.f8550e.b(bArr);
            } else {
                this.f8558y = (this.f8550e.b(bArr) + (g() * 40.0f)) / (40.0f + 1.0f);
            }
            aVar.c(g());
            this.f8559z = C1677fh.a(this.f8547u, this.f8548c.b(bArr), this.f8549d.b(bArr)) + a((int) Math.round(dB));
            aVar.d(h());
            if (this.f8551f != null) {
                if (Double.isNaN(f())) {
                    this.f8557x = this.f8551f.b(bArr);
                } else {
                    this.f8557x = (this.f8551f.b(bArr) + (f() * 40.0f)) / (40.0f + 1.0f);
                }
                aVar.b(f());
            }
            if (this.f8561B) {
                aVar.a(this.f8553h.b(bArr));
            } else {
                aVar.a(a(dB));
            }
            boolean zA = a(bArr);
            aVar.a(zA);
            this.f8568o.a(zA);
            this.f8566m.a(aVar);
            if (zA) {
                this.f8555v++;
            } else {
                if (Float.isNaN(this.f8568o.f()) || this.f8568o.f() > this.f8568o.e()) {
                    this.f8568o.e(this.f8568o.e());
                }
                if (Float.isNaN(this.f8568o.g()) || this.f8568o.g() < this.f8568o.e()) {
                    this.f8568o.f(this.f8568o.e());
                }
            }
            this.f8556w++;
            this.f8568o.b((float) this.f8558y);
            this.f8568o.c((float) dB);
            this.f8568o.a((float) this.f8559z);
            this.f8568o.b(this.f8555v);
            this.f8568o.a(this.f8556w);
            this.f8568o.d(o());
            k();
            j();
        } catch (V.g e2) {
            Logger.getLogger(C1266D.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            C0404hl.a().d("WUE Analyze Error!! Could not retrieve all data. " + e2.getMessage());
        }
    }

    private void k() {
        double dA;
        if (this.f8568o.e() > l()) {
            try {
                double[][] dArrI = this.f8542r.i(this.f8540p.h());
                int i2 = 0;
                while (i2 < dArrI.length && (i2 == 0 || dArrI[i2 - 1][0] <= this.f8568o.e())) {
                    if (i() || dArrI[i2][0] > this.f8568o.e()) {
                        dA = this.f8566m.a((int) Math.round(dArrI[i2][0]));
                    } else {
                        bM.c cVarC = this.f8566m.c((int) Math.round(dArrI[i2][0]));
                        dA = cVarC != null ? cVarC.c() : Double.NaN;
                    }
                    try {
                        if (!Double.isNaN(dA)) {
                            this.f8544t.a(this.f8540p.h(), (dA < 100.0d || i2 == dArrI.length - 1) ? 100.0d : dA, i2, 0);
                        }
                    } catch (V.j e2) {
                        Logger.getLogger(C1266D.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
                    }
                    i2++;
                }
            } catch (V.g e3) {
                e3.printStackTrace();
            }
        }
    }

    private double l() {
        if (Float.isNaN(this.f8568o.f())) {
            return Double.NaN;
        }
        try {
            double[][] dArrI = this.f8542r.i(this.f8540p.h());
            for (int i2 = 0; i2 < dArrI.length; i2++) {
                if (dArrI[i2][0] > this.f8568o.f()) {
                    return dArrI[i2][0];
                }
            }
            return Double.NaN;
        } catch (V.g e2) {
            Logger.getLogger(C1266D.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            return Double.NaN;
        }
    }

    protected boolean a(byte[] bArr) {
        Iterator it = this.f8545a.iterator();
        while (it.hasNext()) {
            bL.k kVar = (bL.k) it.next();
            if (kVar.a(this.f8540p, bArr)) {
                this.f8554i = kVar.a();
                return true;
            }
        }
        this.f8554i = "";
        return false;
    }

    public double a(double d2) {
        return K.d.a(this.f8542r.i(this.f8540p.h()), this.f8541q.i(this.f8540p.h()), d2);
    }

    public double b(double d2) {
        return this.f8566m.a((int) Math.round(d2));
    }

    private double a(int i2) {
        return K.d.a(this.f8542r.i(this.f8540p.h()), this.f8543s.i(this.f8540p.h()), i2);
    }

    public double d() throws V.g {
        double[][] dArrI = this.f8542r.i(this.f8540p.h());
        return dArrI[dArrI.length - 1][0];
    }

    public void a(boolean z2) throws V.a {
        if (!z2) {
            this.f8560A = z2;
            C0113cs.a().a(this.f8565l);
            return;
        }
        C0113cs.a().a(this.f8540p.c(), this.f8552g.aJ(), this.f8565l);
        C0113cs.a().a(this.f8540p.c(), this.f8550e.aJ(), this.f8565l);
        C0113cs.a().a(this.f8540p.c(), this.f8548c.aJ(), this.f8565l);
        C0113cs.a().a(this.f8540p.c(), this.f8549d.aJ(), this.f8565l);
        if (this.f8551f != null) {
            C0113cs.a().a(this.f8540p.c(), this.f8551f.aJ(), this.f8565l);
        }
        try {
            m();
            this.f8560A = z2;
        } catch (V.g e2) {
            Logger.getLogger(C1266D.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            a(false);
            throw new V.a("Failed to start WUE Analyze.\n" + e2.getMessage());
        }
    }

    private void m() throws V.g {
        try {
            this.f8544t.a(this.f8540p.p(), this.f8541q.i(this.f8540p.p()));
        } catch (V.j e2) {
            Logger.getLogger(C1266D.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
        }
    }

    private void n() {
        this.f8566m.c();
    }

    public void e() throws V.g {
        try {
            this.f8541q.a(this.f8540p.p(), this.f8544t.i(this.f8540p.p()));
        } catch (V.j e2) {
            Logger.getLogger(C1266D.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
        }
    }

    public double f() {
        return this.f8557x;
    }

    public double g() {
        return this.f8558y;
    }

    public double h() {
        return this.f8559z;
    }

    private float o() {
        return 0.0f;
    }

    public int a(float f2) {
        return this.f8566m.e(Math.round(f2));
    }

    public boolean i() {
        return this.f8562C;
    }

    public void b(boolean z2) {
        this.f8562C = z2;
    }
}
