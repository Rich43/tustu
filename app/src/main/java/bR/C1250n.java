package br;

import G.C0072be;
import G.C0113cs;
import G.C0126i;
import G.aF;
import G.aH;
import G.aM;
import G.dk;
import bH.C1007o;
import bt.bO;
import com.efiAnalytics.ui.C1589c;
import com.efiAnalytics.ui.C1677fh;
import com.efiAnalytics.ui.C1701s;
import com.efiAnalytics.ui.InterfaceC1565bc;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

/* renamed from: br.n, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:br/n.class */
public class C1250n implements aF, InterfaceC1565bc {

    /* renamed from: a, reason: collision with root package name */
    dk f8480a;

    /* renamed from: c, reason: collision with root package name */
    bL.n f8482c;

    /* renamed from: d, reason: collision with root package name */
    G.R f8483d;

    /* renamed from: e, reason: collision with root package name */
    aH f8484e;

    /* renamed from: f, reason: collision with root package name */
    aH f8485f;

    /* renamed from: g, reason: collision with root package name */
    aH f8486g;

    /* renamed from: h, reason: collision with root package name */
    aH f8487h;

    /* renamed from: i, reason: collision with root package name */
    aH f8488i;

    /* renamed from: j, reason: collision with root package name */
    aH f8489j;

    /* renamed from: m, reason: collision with root package name */
    private aH f8490m;

    /* renamed from: p, reason: collision with root package name */
    private C1701s f8493p;

    /* renamed from: q, reason: collision with root package name */
    private C1701s f8494q;

    /* renamed from: r, reason: collision with root package name */
    private C1701s f8495r;

    /* renamed from: s, reason: collision with root package name */
    private C1701s f8496s;

    /* renamed from: k, reason: collision with root package name */
    C1251o f8498k;

    /* renamed from: b, reason: collision with root package name */
    ArrayList f8481b = new ArrayList();

    /* renamed from: n, reason: collision with root package name */
    private int f8491n = 0;

    /* renamed from: o, reason: collision with root package name */
    private int f8492o = 0;

    /* renamed from: t, reason: collision with root package name */
    private String f8497t = "";

    /* renamed from: u, reason: collision with root package name */
    private long f8499u = 0;

    /* renamed from: v, reason: collision with root package name */
    private int f8500v = 40;

    /* renamed from: l, reason: collision with root package name */
    C1252p f8501l = new C1252p(this);

    protected C1250n(G.R r2, dk dkVar, String str, C1589c c1589c) throws V.a {
        this.f8480a = null;
        this.f8482c = null;
        this.f8483d = null;
        this.f8484e = null;
        this.f8485f = null;
        this.f8486g = null;
        this.f8487h = null;
        this.f8488i = null;
        this.f8489j = null;
        this.f8490m = null;
        this.f8493p = null;
        this.f8494q = null;
        this.f8495r = null;
        this.f8496s = null;
        this.f8498k = null;
        this.f8480a = dkVar;
        this.f8483d = r2;
        try {
            this.f8493p = bO.a().a(r2, str, "", str);
            this.f8494q = bO.a().a(r2, str, "veAnalyze_", str);
            this.f8495r = a(r2, dkVar);
            this.f8496s = bO.a().a(this.f8493p, str);
            this.f8494q.a(this.f8495r, c1589c);
            this.f8498k = new C1251o(this, this.f8494q, this.f8495r);
            this.f8495r.addTableModelListener(this.f8498k);
            this.f8482c = new bL.n(g(), h(), i(), j(), c1589c);
            this.f8482c.a(dkVar.o());
            Iterator it = C1241e.a().a(r2, this.f8480a).iterator();
            while (it.hasNext()) {
                a((bL.k) it.next());
            }
            if (dkVar.f() != null && !dkVar.f().equals("")) {
                this.f8489j = r2.g(dkVar.f());
            }
            this.f8488i = r2.g(dkVar.e());
            if (this.f8488i == null) {
                throw new V.a("VE Analyze can not locate OutputChannel:" + dkVar.e() + " in the current Configuration.");
            }
            this.f8484e = r2.g(dkVar.g());
            if (this.f8484e == null) {
                throw new V.a("VE Analyze can not locate OutputChannel:" + dkVar.g() + " in the current Configuration.");
            }
            this.f8485f = r2.g(dkVar.h());
            if (this.f8485f == null) {
                throw new V.a("VE Analyze can not locate OutputChannel:" + dkVar.h() + " in the current Configuration.");
            }
            this.f8486g = r2.g(dkVar.p());
            if (this.f8486g == null) {
                throw new V.a("VE Analyze can not locate OutputChannel:" + dkVar.p() + " in the current Configuration.");
            }
            this.f8487h = r2.g(dkVar.q());
            if (this.f8487h == null) {
                throw new V.a("VE Analyze can not locate OutputChannel:" + dkVar.q() + " in the current Configuration.");
            }
            this.f8490m = r2.g(dkVar.r());
            if (this.f8490m == null) {
            }
            r2.C().a(this);
        } catch (V.g e2) {
            e2.printStackTrace();
            throw new V.a(e2.getMessage());
        }
    }

    public void a() {
        try {
            this.f8495r = a(this.f8483d, this.f8480a);
            this.f8482c.a(this.f8495r);
            this.f8494q.a(this.f8495r, this.f8482c.f());
        } catch (V.g e2) {
            Logger.getLogger(C1250n.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
        }
    }

    private C1701s a(G.R r2, dk dkVar) throws V.g {
        C1701s c1701sA = bO.a().a(r2, dkVar.c(), dkVar.b());
        if (bO.a().b(dkVar.c())) {
            return c1701sA;
        }
        C0072be c0072be = (C0072be) r2.e().c(dkVar.c());
        if (c0072be == null) {
            return null;
        }
        aM aMVarC = r2.c(c0072be.c());
        if (aMVarC.o() == null || !aMVarC.o().equalsIgnoreCase("Volts")) {
            return c1701sA;
        }
        C1701s c1701s = new C1701s();
        c1701s.a(c1701sA.getRowCount(), c1701sA.getColumnCount());
        C1677fh.a(c1701sA, c1701s);
        aH aHVarG = r2.g(dkVar.e());
        if (aHVarG == null) {
            throw new V.g("Unable to calculate AFR based on current sensor configuration.");
        }
        String strK = aHVarG.k();
        for (int i2 = 0; i2 < c1701s.getRowCount(); i2++) {
            for (int i3 = 0; i3 < c1701s.getColumnCount(); i3++) {
                double dDoubleValue = c1701s.getValueAt(i2, i3).doubleValue();
                String strC = C0126i.c(bH.W.b(bH.W.b(strK, "egoADC", ((int) Math.round((dDoubleValue * 255.0d) / 5.0d)) + ""), "egoVoltage", dDoubleValue + ""), r2);
                try {
                    c1701s.setValueAt(Double.valueOf(bH.F.g(strC)), i2, i3);
                } catch (Exception e2) {
                    throw new V.g("Error calculating target AFR with formula:\n" + strK + "\nUsing input values of:\n" + strC);
                }
            }
        }
        c1701s.a(false);
        c1701sA.addTableModelListener(new C1253q(this, strK, c1701s, c1701sA));
        return c1701s;
    }

    protected boolean a(byte[] bArr) {
        Iterator it = this.f8481b.iterator();
        while (it.hasNext()) {
            bL.k kVar = (bL.k) it.next();
            if (kVar.a(this.f8483d, bArr)) {
                this.f8497t = kVar.a();
                this.f8492o++;
                return true;
            }
        }
        this.f8497t = "";
        return false;
    }

    public void a(bL.l lVar) {
        this.f8482c.a(lVar);
    }

    public void b(bL.l lVar) {
        this.f8482c.b(lVar);
    }

    @Override // G.aF
    public void a(String str, byte[] bArr) {
        if (b() && f() && str.equals(this.f8483d.c()) && System.currentTimeMillis() - this.f8499u >= this.f8500v) {
            bL.p pVar = null;
            if (0 == 0) {
                pVar = new bL.p();
                pVar.a(System.currentTimeMillis());
                if (this.f8489j == null) {
                    pVar.c(100.0d);
                }
            }
            try {
                if (this.f8489j != null) {
                    pVar.c(this.f8489j.b(bArr));
                }
                pVar.a(this.f8484e.b(bArr));
                pVar.b(this.f8485f.b(bArr));
                pVar.e(this.f8486g.b(bArr));
                pVar.f(this.f8487h.b(bArr));
                pVar.d(this.f8488i.b(bArr));
                if (this.f8490m != null) {
                    pVar.g(this.f8490m.b(bArr));
                }
            } catch (V.g e2) {
                e2.printStackTrace();
                e();
            }
            if (pVar.f()) {
                pVar.a(a(bArr));
                this.f8491n++;
                this.f8499u = System.currentTimeMillis();
                pVar.a(this.f8499u);
                this.f8482c.a(pVar);
            }
        }
    }

    public void a(bL.k kVar) {
        this.f8481b.add(kVar);
    }

    public boolean b() {
        return this.f8482c != null && this.f8482c.e();
    }

    public void c() throws V.a {
        C0113cs.a().a(this.f8483d.c(), this.f8488i.aJ(), this.f8501l);
        C0113cs.a().a(this.f8483d.c(), this.f8484e.aJ(), this.f8501l);
        C0113cs.a().a(this.f8483d.c(), this.f8485f.aJ(), this.f8501l);
        C0113cs.a().a(this.f8483d.c(), this.f8486g.aJ(), this.f8501l);
        C0113cs.a().a(this.f8483d.c(), this.f8487h.aJ(), this.f8501l);
        if (this.f8489j != null) {
            C0113cs.a().a(this.f8483d.c(), this.f8489j.aJ(), this.f8501l);
        }
        this.f8482c.b();
        a();
        this.f8482c.c();
    }

    public void d() {
        this.f8482c.b();
    }

    public void e() {
        this.f8482c.d();
        C0113cs.a().a(this.f8501l);
    }

    public boolean f() {
        if (this.f8480a.d() == null || this.f8480a.d().equals("")) {
            return true;
        }
        try {
            return C1007o.a(C0126i.d(this.f8480a.d(), this.f8483d), this.f8483d);
        } catch (V.g e2) {
            bH.C.c("VE Analyze Live is Unable to evaluate TableActiveCondition :\n\t" + this.f8480a.d() + "\n\tAssuming the table is active");
            return true;
        } catch (Exception e3) {
            bH.C.c("VE Analyze Live is Unable to evaluate TableActiveCondition :\n\t" + this.f8480a.d() + "\n\tAssuming the table is active");
            e3.printStackTrace();
            return true;
        }
    }

    public C1701s g() {
        return this.f8493p;
    }

    public C1701s h() {
        return this.f8494q;
    }

    public C1701s i() {
        return this.f8495r;
    }

    public C1701s j() {
        return this.f8496s;
    }

    public String k() {
        return this.f8497t;
    }

    @Override // com.efiAnalytics.ui.InterfaceC1565bc
    public void close() {
        this.f8495r.removeTableModelListener(this.f8498k);
    }

    public void a(aH aHVar) {
        this.f8490m = aHVar;
    }

    public void a(String str) throws V.g {
        aH aHVarG = this.f8483d.g(str);
        if (aHVarG == null) {
            throw new V.g(str + " is not a valid channel in configuration " + this.f8483d.c());
        }
        this.f8488i = aHVarG;
    }

    public void b(String str) throws V.g {
        aH aHVarG = this.f8483d.g(str);
        if (aHVarG == null) {
            throw new V.g(str + " is no a valid channel in configuration " + this.f8483d.c());
        }
        this.f8489j = aHVarG;
    }
}
