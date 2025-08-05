package bQ;

import G.C0113cs;
import G.C0129l;
import G.C0130m;
import G.C0132o;
import G.F;
import G.J;
import G.N;
import G.R;
import G.T;
import G.aB;
import G.aF;
import G.bS;
import G.cD;
import G.cE;
import bH.C;
import bH.C0995c;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: TunerStudioMS.jar:bQ/l.class */
public class l extends J implements A.g, A.h {

    /* renamed from: ao, reason: collision with root package name */
    private A.f f7435ao;

    /* renamed from: b, reason: collision with root package name */
    n f7437b;

    /* renamed from: c, reason: collision with root package name */
    final bN.k f7438c;

    /* renamed from: d, reason: collision with root package name */
    final f f7439d;

    /* renamed from: e, reason: collision with root package name */
    final m f7440e;

    /* renamed from: f, reason: collision with root package name */
    final bN.b f7441f;

    /* renamed from: g, reason: collision with root package name */
    final bR.b f7442g;

    /* renamed from: h, reason: collision with root package name */
    final List f7443h;

    /* renamed from: i, reason: collision with root package name */
    final J.j f7444i;

    /* renamed from: j, reason: collision with root package name */
    o f7445j;

    /* renamed from: k, reason: collision with root package name */
    a f7446k;

    /* renamed from: l, reason: collision with root package name */
    long f7447l;

    /* renamed from: m, reason: collision with root package name */
    long f7448m;

    /* renamed from: n, reason: collision with root package name */
    double f7449n;

    /* renamed from: o, reason: collision with root package name */
    p f7450o;

    /* renamed from: al, reason: collision with root package name */
    int f7452al;

    /* renamed from: an, reason: collision with root package name */
    float f7454an;

    /* renamed from: a, reason: collision with root package name */
    public static String f7436a = "XCP Master Driver";

    /* renamed from: ak, reason: collision with root package name */
    public static int f7451ak = 1;

    /* renamed from: am, reason: collision with root package name */
    static int f7453am = 0;

    /* renamed from: ap, reason: collision with root package name */
    private static final HashMap f7455ap = new HashMap();

    public l(F f2) {
        super(f2);
        this.f7435ao = null;
        this.f7437b = null;
        this.f7438c = new bN.k();
        this.f7439d = new f(this.f7438c);
        this.f7440e = new m(this);
        this.f7441f = new bN.b();
        this.f7442g = new bR.b();
        this.f7443h = new ArrayList();
        this.f7444i = new J.j();
        this.f7445j = null;
        this.f7447l = -1L;
        this.f7448m = 0L;
        this.f7449n = 0.0d;
        this.f7450o = null;
        this.f7452al = 0;
        this.f7454an = 0.0f;
        f(f2);
        f(false);
        this.f7439d.a(new q(this));
        this.f7439d.a(this.f7444i);
        this.f7446k = new a(this, this.f7444i);
        this.f7446k.a();
        this.f7450o = new p(this);
        ac.h.a(this.f7450o);
    }

    private void f(F f2) {
        String[] strArr = new String[f2.g()];
        for (int i2 = 0; i2 < strArr.length; i2++) {
            strArr[i2] = "XCP";
        }
        try {
            f2.f(strArr);
            f2.e((String[]) null);
            f2.b(strArr);
            f2.h(strArr);
            f2.c(strArr);
            f2.g(strArr);
        } catch (V.g e2) {
            Logger.getLogger(l.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
        }
        e eVar = new e(f2.u(), this.f7438c);
        a(eVar.f());
        this.f7443h.add(eVar);
        this.f7439d.a(new bR.c(f2.E(), this, eVar));
    }

    @Override // G.J
    protected void b(F f2) {
        super.b(f2);
    }

    @Override // G.J
    public void b(aF aFVar) {
        super.a(aFVar);
    }

    @Override // G.J
    public J.h D() {
        return this.f7444i;
    }

    @Override // G.J
    public String n() {
        return f7436a;
    }

    public u a(String str) {
        for (e eVar : this.f7443h) {
            if (eVar.f().a().equals(str)) {
                return eVar.f();
            }
        }
        return null;
    }

    @Override // G.J, A.h
    public void c() {
        if (this.f7437b != null) {
            this.f7437b.b();
            this.f7437b.a(true);
        }
        try {
            if (a() != null) {
                a().g();
            }
        } catch (Exception e2) {
        }
    }

    private bN.t T() throws V.b {
        bN.t tVarA = this.f7439d.a(t.a().b(), this.f7438c.q() / 4);
        if (tVarA == null || tVarA.a() != 255) {
            throw new V.b("Connect Failed");
        }
        if (tVarA.c().length == 7) {
            return tVarA;
        }
        C.d("Valid Connect response code, but invalid packet size.");
        throw new V.b("Connect Failed, Invalid packet size");
    }

    public void f() {
        if (this.f421F) {
            try {
                m();
                bN.l lVarC = t.a().c();
                k kVar = new k();
                kVar.a(lVarC);
                this.f7439d.a(kVar);
                a(100L);
            } catch (Exception e2) {
                Logger.getLogger(l.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            }
        }
    }

    @Override // G.J
    protected InputStream i() {
        return a().i();
    }

    @Override // G.J
    public boolean b() {
        return this.f7437b != null && this.f7437b.f7458g;
    }

    @Override // G.J, A.h
    public void d() {
        if (this.f7437b == null || !this.f7437b.isAlive() || this.f7437b.f7458g) {
            this.f7437b = new n(this);
            this.f7437b.start();
        }
        this.f7448m = System.currentTimeMillis();
    }

    @Override // G.J
    public void a(boolean z2) {
    }

    @Override // G.J
    protected C0132o a(C0130m c0130m) {
        C0132o c0132o = new C0132o();
        try {
            try {
                this.f7435ao.f();
                this.f7438c.b(this.f7435ao.s());
                bN.t tVarA = this.f7439d.a(t.a().b());
                C.c("Test: " + this.f7435ao.n());
                if (tVarA == null || tVarA.a() != 255) {
                    c0132o.a(3);
                } else {
                    c0132o.a(1);
                    c0132o.a("Received valid connect response.");
                    bN.l lVarC = t.a().c();
                    k kVar = new k();
                    kVar.a(lVarC);
                    this.f7439d.a(kVar);
                    a(100L);
                }
                c();
            } catch (Exception e2) {
                if (e2 instanceof V.b) {
                    C.d("Test Timed out, failed.");
                } else {
                    Logger.getLogger(l.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
                }
                c0132o.a(3);
                c();
            }
            return c0132o;
        } catch (Throwable th) {
            c();
            throw th;
        }
    }

    @Override // G.J
    protected byte[] a(byte[] bArr, long j2, long j3, int i2, C0130m c0130m, InputStream inputStream) {
        throw new UnsupportedOperationException("Not supported in this implementation.");
    }

    @Override // G.J
    protected void d(C0130m c0130m) {
        F fV = c0130m.v();
        if (fV == null) {
            fV = e();
        }
        if (c0130m.o() == -2) {
            c0130m.e(fV.A());
        }
        if (c0130m.o() < 0) {
            C.b("Burn requested for page:" + c0130m.o());
            return;
        }
        C0132o c0132o = new C0132o();
        c0132o.a(c0130m);
        c0130m.c(c0130m.h() + 1);
        try {
            c(true);
            bN.t tVarA = t.a().a(this.f7438c);
            try {
                long jCurrentTimeMillis = System.currentTimeMillis();
                bN.t tVarA2 = this.f7439d.a(tVarA);
                if (tVarA2.a() != 255 && c0130m.h() <= 3) {
                    C.b("Burn failed! Try Count: " + c0130m.h() + ", trying again.");
                    c(this.f7438c.r());
                    this.f7444i.t();
                    d(c0130m);
                    return;
                }
                if (tVarA2.a() != 255) {
                    this.f7444i.t();
                    c0132o.a(3);
                    c0132o.a("Store to flash failed!");
                    c0130m.b(1.0d);
                    c0130m.b(c0132o);
                    c(fV.u(), c0130m.o());
                    return;
                }
                c0132o.a(1);
                bR.a aVar = new bR.a();
                this.f7439d.j().a(aVar);
                try {
                    synchronized (aVar) {
                        try {
                            aVar.wait(4000L);
                            C.c("Burn time: " + (System.currentTimeMillis() - jCurrentTimeMillis));
                            this.f7439d.j().b(aVar);
                        } catch (InterruptedException e2) {
                            Logger.getLogger(l.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
                            this.f7439d.j().b(aVar);
                        }
                        if (aVar.a()) {
                            this.f7444i.r();
                        } else {
                            this.f7444i.t();
                        }
                    }
                    this.f7447l = -1L;
                    fV.t(-2);
                    fV.u(-2);
                    c0132o.a(1);
                    c0132o.b(fV.u());
                    C.c("burned page " + (c0130m.o() + 1));
                    c0130m.b(1.0d);
                    c0130m.b(c0132o);
                    c(fV.u(), c0130m.o());
                } catch (Throwable th) {
                    this.f7439d.j().b(aVar);
                    throw th;
                }
            } catch (V.b | bN.o e3) {
                if (c0130m.h() <= 3) {
                    C.b("Burn failed! Try Count: " + c0130m.h() + ", trying again.");
                    c(this.f7438c.r());
                    d(c0130m);
                } else {
                    c0132o.a(3);
                    c0132o.a(e3.getLocalizedMessage());
                    c0130m.b(1.0d);
                    c0130m.b(c0132o);
                    c(fV.u(), c0130m.o());
                }
            }
        } catch (C0129l | V.b | bN.o e4) {
            c0132o.a(3);
            c0132o.a(e4.getLocalizedMessage());
            c0130m.b(1.0d);
            c0130m.b(c0132o);
            c(fV.u(), c0130m.o());
        }
    }

    protected void c(boolean z2) throws C0129l {
        bN.t tVarA = null;
        int i2 = 0;
        String strA = "Unknown";
        bN.t tVarA2 = t.a().a(this.f7438c, z2);
        do {
            try {
                i2++;
                tVarA = this.f7439d.a(tVarA2);
                if (tVarA.a() != 255) {
                    C.a("SET_SEGMENT_MODE Error!");
                    strA = "None";
                    if (tVarA.c().length > 0) {
                        int iA = C0995c.a(tVarA.c()[0]);
                        C.d("Error Code: " + C0995c.d(tVarA.c()));
                        strA = bN.n.a(tVarA.c()[0]);
                        if (iA == 32 || iA == 33 || iA == 39 || iA == 40) {
                            C0129l c0129l = new C0129l("Fatal Error " + strA);
                            c0129l.a(iA);
                            c0129l.a(false);
                            throw c0129l;
                        }
                        C.b("Error setting address: " + strA);
                        c(this.f7438c.s());
                    }
                }
            } catch (V.b e2) {
                g();
            }
            if (tVarA != null && tVarA.a() == 255) {
                break;
            }
        } while (i2 < 3);
        if (tVarA.a() != 255) {
            throw new C0129l("Error: " + strA);
        }
    }

    private void U() {
        this.f7452al = 0;
    }

    @Override // G.J
    protected void c(C0130m c0130m) throws C0129l, IOException {
        int i2 = c0130m.n() == 5 ? 5 : 2;
        boolean z2 = false;
        U();
        do {
            try {
                this.f7452al++;
                super.c(c0130m);
                if (c0130m.j() == null || c0130m.j().a() == 1 || c0130m.j().a() == -1) {
                    z2 = true;
                }
                if (this.f7452al > 1) {
                    this.f7444i.i();
                }
            } catch (C0129l e2) {
                if (this.f7452al > 1) {
                    this.f7444i.h();
                }
                if (e2.a() == 16 || e2.a() == 18) {
                    this.f7452al = 0;
                    a(this.f7438c.r());
                    this.f7444i.u();
                } else {
                    if (!e2.b() || this.f7452al >= i2) {
                        throw e2;
                    }
                    C.d("Command failed, performing sync and retry: " + this.f7452al);
                    try {
                        g();
                    } catch (V.b e3) {
                        this.f7444i.h();
                        C.a("Sync Failed");
                        throw new C0129l("Sync Timeout on retry");
                    } catch (bN.o e4) {
                        Logger.getLogger(l.class.getName()).log(Level.INFO, "Sync failed on retry do to format exception", (Throwable) e4);
                        throw new C0129l("Sync Formate error on retry");
                    }
                }
            } catch (V.b e5) {
                if (this.f7452al > 1) {
                    this.f7444i.h();
                }
                C.d("Timeout, sync and retry: " + this.f7452al);
                try {
                    g();
                } catch (Exception e6) {
                    C.b("SYNC Failed");
                    try {
                        g();
                    } catch (Exception e7) {
                        C.b("SYNC Failed again");
                        this.f7444i.h();
                        throw new C0129l("Controller did not respond after 3 attempts, assumed not connected");
                    }
                }
            }
            if (z2) {
                break;
            }
        } while (this.f7452al < i2);
        if (z2 || this.f7452al < i2) {
            U();
        } else {
            String str = "Instruction failed after " + this.f7452al + " attempts... Giving up.";
            C.a(str);
            throw new C0129l(str);
        }
    }

    @Override // G.J
    protected void a(F f2, int i2) throws C0129l {
        bN.t tVarB;
        if (f2.A() == i2) {
            return;
        }
        try {
            if (this.f423H && f2.A() >= 0 && f2.B() >= 0 && f2.x(f2.A()) && f2.A() != i2) {
                C.c("Activate Page, burn page " + (f2.A() + 1) + " new active page=" + (i2 + 1));
                d(C0130m.a(f2, f2.A()));
            }
            f2.t(i2);
            C.c("Activate Page: " + (i2 + 1));
            this.f7447l = -1L;
            bN.t tVarA = t.a().a(i2);
            int i3 = 0;
            do {
                i3++;
                tVarB = this.f7439d.b(tVarA, 3);
                if (tVarB.a() != 255) {
                    C.a("SET_CAL_PAGE Error!");
                    if (tVarB.c().length > 0) {
                        int iA = C0995c.a(tVarB.c()[0]);
                        C.d("Error Code: " + C0995c.d(tVarB.c()));
                        String strA = bN.n.a(tVarB.c()[0]);
                        if (iA == 32 || iA == 33 || iA == 39 || iA == 40 || iA == 33 || iA == 38) {
                            C0129l c0129l = new C0129l("Fatal Error " + strA);
                            c0129l.a(iA);
                            c0129l.a(false);
                            throw c0129l;
                        }
                        C.b("Error setting cal page: " + strA);
                    } else if (i3 > 2) {
                        C0129l c0129l2 = new C0129l("Unknown Error");
                        c0129l2.a(false);
                        throw c0129l2;
                    }
                }
                f2.t(i2);
                if (tVarB.a() == 255) {
                    break;
                }
            } while (i3 < 3);
            if (tVarB.a() != 255) {
                C0129l c0129l3 = new C0129l("Unknown Error");
                c0129l3.a(false);
                throw c0129l3;
            }
        } catch (C0129l e2) {
            throw e2;
        } catch (Exception e3) {
            C.c("Failed to activate Page: " + i2);
            throw new C0129l("Activating page" + i2 + ": " + e3.getMessage());
        }
    }

    @Override // G.J
    protected void l(C0130m c0130m) throws C0129l, V.b {
        F fV = c0130m.v();
        int[][] iArr = new int[fV.g()][0];
        c0130m.b(0.0d);
        C0130m c0130m2 = new C0130m(fV);
        cE cEVar = new cE(fV, c0130m);
        c0130m2.b(cEVar);
        m();
        for (int i2 = 0; i2 < iArr.length; i2++) {
            try {
                cEVar.a(i2);
                long jCurrentTimeMillis = System.currentTimeMillis();
                iArr[i2] = a(fV, i2, c0130m2);
                C.c("Read page time: " + (System.currentTimeMillis() - jCurrentTimeMillis) + "ms.");
            } catch (Throwable th) {
                l();
                throw th;
            }
        }
        C0132o c0132o = new C0132o();
        c0132o.a(1);
        c0132o.a(iArr);
        c0130m.b(c0132o);
        l();
    }

    protected void g() throws C0129l {
        bN.t tVarA = this.f7439d.a(t.a().d(), 1000);
        if (tVarA.a() != 255) {
            this.f7444i.n();
            if (tVarA.c().length > 0) {
                int iA = C0995c.a(tVarA.c()[0]);
                String strA = bN.n.a(tVarA.c()[0]);
                if (iA != 0) {
                    if (iA != 32 && iA != 33) {
                        C0129l c0129l = new C0129l("Error getting sync: " + strA);
                        c0129l.a(iA);
                        c0129l.a(true);
                        throw c0129l;
                    }
                    C.d("Error Code: " + C0995c.d(tVarA.c()));
                    C0129l c0129l2 = new C0129l("Fatal Error " + strA);
                    c0129l2.a(iA);
                    c0129l2.a(false);
                    throw c0129l2;
                }
                if (e().av() > 0 && O() && System.currentTimeMillis() - this.f7448m > 3000) {
                    Q();
                } else {
                    if (O()) {
                        return;
                    }
                    if (e().av() == 0 || System.currentTimeMillis() - this.f7448m < 500) {
                        P();
                    }
                }
            }
        }
    }

    @Override // G.J
    protected void f(C0130m c0130m) {
        C.a("Write Value, should not be called with this protocol!!!");
    }

    @Override // G.J
    public synchronized void d(long j2) {
        if (h()) {
            long jCurrentTimeMillis = j2 - System.currentTimeMillis();
            if (jCurrentTimeMillis > 1000000) {
                jCurrentTimeMillis = 60000;
            }
            C.c("Client: Ignore runtime reads for: " + (j2 - System.currentTimeMillis()));
            try {
                if (this.f7439d.a(t.a().f(this.f7438c, (int) jCurrentTimeMillis)).a() != 255) {
                    C.c("IgnoreRuntimeUntil failed..");
                }
            } catch (V.b e2) {
                Logger.getLogger(l.class.getName()).log(Level.SEVERE, "Stop runtime for failed", (Throwable) e2);
            } catch (bN.o e3) {
                Logger.getLogger(l.class.getName()).log(Level.SEVERE, "Stop runtime for failed", (Throwable) e3);
            }
        }
        super.d(j2);
    }

    @Override // G.J
    protected void k(C0130m c0130m) {
        C0132o c0132o = new C0132o();
        c0132o.a(c0130m);
        try {
            try {
                int iB = 0;
                for (List<bN.t> list : t.a().a(this.f7438c, c0130m.v(), c0130m)) {
                    this.f7444i.q();
                    c0130m.m();
                    bN.t tVarA = this.f7439d.a(list);
                    if (tVarA == null) {
                        C.c("Raw Write response null!");
                        throw new C0129l("Raw Write response null!");
                    }
                    if (tVarA.a() == 255 || !c0130m.g()) {
                        c0132o.a(1);
                        for (bN.t tVar : list) {
                            iB += tVar.b() - (tVar.c()[0] == 163 ? 4 : 2);
                        }
                        if (tVarA.c().length > 0) {
                            if (c0130m.d()) {
                                c0132o.a(tVarA.c());
                            } else {
                                c0132o.a(C0995c.b(tVarA.c()));
                            }
                        }
                        c0130m.b(c0132o);
                    } else {
                        C.b("Raw Write Failed! retrying..");
                        bN.t tVarA2 = this.f7439d.a(list);
                        if (tVarA2.a() != 255) {
                            C.b("Raw Write Failed again!");
                            if (tVarA2.c().length <= 0) {
                                throw new C0129l("Raw Write response Error!");
                            }
                            throw new C0129l("Raw Write response Error! " + C0995c.d(tVarA2.d()));
                        }
                        c0132o.a(1);
                        for (bN.t tVar2 : list) {
                            iB += tVar2.b() - (tVar2.c()[0] == 163 ? 4 : 2);
                            c0130m.b(iB / c0130m.r());
                        }
                        if (tVarA2.c().length > 0) {
                            if (c0130m.d()) {
                                c0132o.a(tVarA2.c());
                            } else {
                                c0132o.a(C0995c.b(tVarA2.c()));
                            }
                        }
                        c0130m.b(c0132o);
                    }
                }
                c0130m.b(1.0d);
            } catch (V.b e2) {
                c0132o.a(3);
                c0132o.a("Controller Instruction failed!\n\nError: \n" + e2.getMessage());
                c0130m.b(1.0d);
            } catch (Exception e3) {
                c0132o.a(3);
                c0132o.a("Raw write failed!\nError: " + e3.getMessage());
                C.b("Raw write failed!\nError: " + e3.getMessage());
                c0130m.b(c0132o);
                c0130m.b(1.0d);
            }
        } catch (Throwable th) {
            c0130m.b(1.0d);
            throw th;
        }
    }

    @Override // G.J
    protected void j(C0130m c0130m) throws V.b {
        new C0132o().a(c0130m);
        c0130m.m();
        try {
            this.f7439d.b(t.a().e(this.f7438c, c0130m.p()[0]), 2);
        } catch (V.b e2) {
            throw e2;
        } catch (bN.o e3) {
            Logger.getLogger(l.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e3);
        }
    }

    @Override // G.J
    protected void h(C0130m c0130m) throws C0129l, V.b {
        if (h()) {
            F fV = c0130m.v();
            bN.t tVarB = null;
            C0132o c0132o = new C0132o();
            c0132o.a(c0130m);
            c0130m.m();
            try {
                tVarB = this.f7439d.b(t.a().c(this.f7438c, fV, c0130m), 2);
            } catch (V.b e2) {
                throw e2;
            } catch (bN.o e3) {
                Logger.getLogger(l.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e3);
            }
            if (tVarB == null) {
                C.c("Download PcVariable response null!");
                throw new C0129l("Download PcVariable response null!");
            }
            if (tVarB.a() != 255) {
                c0132o.a(3);
                c0130m.b(c0132o);
                return;
            }
            byte[] bArrC = tVarB.c();
            if (bArrC[0] == 0) {
                c0132o.a(1);
                c0130m.b(c0132o);
                return;
            }
            if (bArrC[0] == 1) {
                c0132o.a(2);
                if (bArrC.length > 1) {
                    byte[] bArr = new byte[bArrC.length - 1];
                    System.arraycopy(bArrC, 1, bArr, 1, bArr.length);
                    String str = new String(bArr);
                    c0132o.a(str);
                    C.b("WritePcVariable: " + str);
                }
                c0130m.b(c0132o);
                return;
            }
            c0132o.a(3);
            if (bArrC.length > 1) {
                byte[] bArr2 = new byte[bArrC.length - 1];
                System.arraycopy(bArrC, 1, bArr2, 1, bArr2.length);
                String str2 = new String(bArr2);
                c0132o.a(str2);
                C.a("WritePcVariable: " + str2);
            }
            c0130m.b(c0132o);
        }
    }

    @Override // G.J
    protected void i(C0130m c0130m) throws V.b {
        if (h()) {
            F fV = c0130m.v();
            bN.t tVarB = null;
            C0132o c0132o = new C0132o();
            c0132o.a(c0130m);
            c0130m.m();
            try {
                tVarB = this.f7439d.b(t.a().d(this.f7438c, fV, c0130m), 2);
            } catch (V.b e2) {
                throw e2;
            } catch (bN.o e3) {
                Logger.getLogger(l.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e3);
            }
            if (tVarB.a() != 255) {
                c0132o.a(3);
                byte[] bArrC = tVarB.c();
                if (bArrC.length > 0) {
                    c0132o.a(new String(bArrC));
                }
                c0130m.b(c0132o);
                return;
            }
            byte[] bArrC2 = tVarB.c();
            c0132o.a(1);
            if (bArrC2.length > 0) {
                c0132o.a(new String(bArrC2));
            }
            c0130m.b(c0132o);
        }
    }

    public boolean h() {
        return e().al().equals("basicRequestReply");
    }

    private boolean q(C0130m c0130m) {
        try {
            return C0995c.b(((N) this.f449ad.get(c0130m.v().u())).a(c0130m.o(), c0130m.q(), c0130m.r()), c0130m.p());
        } catch (Exception e2) {
            C.a(e2.getMessage());
            return false;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:11:0x0040  */
    @Override // G.J
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    protected void e(G.C0130m r7) {
        /*
            Method dump skipped, instructions count: 1085
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: bQ.l.e(G.m):void");
    }

    @Override // G.J
    protected int[] a(F f2, int i2, C0130m c0130m) {
        C.c("Upload / Read Page: " + i2);
        return a(f2, i2, 0, f2.f(i2), c0130m);
    }

    private void a(F f2, int i2, int i3) throws C0129l {
        bN.t tVarB;
        int iY = i3 + f2.y(i2);
        if (f430O) {
            C.c("Setting MTA, page= " + i2 + ", address= 0x" + Integer.toHexString(iY).toUpperCase());
        }
        bN.t tVarA = t.a().a(this.f7438c, iY);
        int i4 = 0;
        String strA = "Unknown";
        do {
            i4++;
            tVarB = this.f7439d.b(tVarA, 2);
            if (tVarB.a() != 255) {
                C.a("SET_MTA Error!");
                strA = "None";
                if (tVarB.c().length > 0) {
                    int iA = C0995c.a(tVarB.c()[0]);
                    C.d("Error Code: " + C0995c.d(tVarB.c()));
                    strA = bN.n.a(tVarB.c()[0]);
                    if (iA == 32 || iA == 33 || iA == 34) {
                        C0129l c0129l = new C0129l("Fatal Error " + strA);
                        c0129l.a(iA);
                        c0129l.a(false);
                        throw c0129l;
                    }
                    C.b("Error setting address: " + strA);
                }
            }
            if (tVarB.a() == 255) {
                break;
            }
        } while (i4 < 3);
        if (tVarB.a() != 255) {
            throw new C0129l("Error: " + strA);
        }
        this.f7447l = iY;
    }

    @Override // G.J
    protected void m(C0130m c0130m) {
        int[] iArrA = a(c0130m.v(), c0130m.o(), c0130m);
        C0132o c0132o = new C0132o();
        c0132o.a(iArrA);
        c0132o.a(1);
        c0132o.a(c0130m);
        c0130m.b(c0132o);
    }

    @Override // G.J
    protected void n(C0130m c0130m) throws C0129l {
        int[] iArrA;
        F fV = c0130m.v();
        int iO = c0130m.o();
        int iQ = c0130m.q();
        int iF = c0130m.f();
        c0130m.b(0.0d);
        try {
            iArrA = a(fV, iO, iQ, iF, c0130m);
        } catch (C0129l e2) {
            if (fV.i() > fV.m()) {
                C.d("Timeout reading chunk " + (iO + 1) + ", blockReadTimeout=" + fV.i() + ", giving up..");
                throw e2;
            }
            fV.c(fV.i() + 50);
            C.d("Timeout reading chunk " + (iO + 1) + ", increased blockReadTimeout to " + fV.i() + ", trying once more.");
            try {
                Thread.sleep(fV.i());
            } catch (InterruptedException e3) {
                Logger.getLogger(J.class.getName()).log(Level.INFO, (String) null, (Throwable) e3);
            }
            iArrA = a(fV, iO, iQ, iF, c0130m);
        }
        c0130m.b((iO + 1.0d) / iArrA.length);
        C0132o c0132o = new C0132o();
        c0132o.a(c0130m);
        c0132o.a(1);
        c0132o.a(iArrA);
        c0130m.b(c0132o);
    }

    @Override // G.J
    protected int[] a(F f2, int i2, int i3, int i4, C0130m c0130m) {
        return a(f2, i2, i3, i4, c0130m, true);
    }

    protected int[] a(F f2, int i2, int i3, int i4, C0130m c0130m, boolean z2) throws C0129l {
        int[] iArrB;
        if (i4 == 0) {
            return new int[0];
        }
        if (i4 <= 4096 && i4 > 30) {
            if (z2 && b(f2, i2, i3, i4)) {
                C.d("Checksum matches, using local data.");
                return ((N) this.f449ad.get(f2.u())).a(i2, i3, i4);
            }
            if (z2) {
                C.d("Checksum does not match, refreshing from controller.");
            }
            int[] iArrB2 = b(f2, i2, i3, i4, c0130m);
            if (I() && i4 > 30) {
                a(((N) this.f449ad.get(f2.u())).a(i2, i3, i4), iArrB2, i2, i3 + f2.y(i2));
            }
            U();
            return iArrB2;
        }
        int[] iArr = new int[i4];
        cD cDVar = new cD(f2, c0130m, i4);
        int length = 0;
        do {
            int i5 = i4 - length > 4096 ? 4096 : i4 - length;
            cDVar.j(i5);
            if (i5 > 30 && z2 && b(f2, i2, i3 + length, i5)) {
                C.d("Checksum matches, using local data.");
                iArrB = ((N) this.f449ad.get(f2.u())).a(i2, i3 + length, i5);
            } else {
                if (z2) {
                    C.d("Checksum does not match, refreshing from controller.");
                }
                if (!I() || i5 <= 30) {
                    iArrB = b(f2, i2, i3 + length, i5, cDVar);
                } else {
                    int[] iArrA = ((N) this.f449ad.get(f2.u())).a(i2, i3 + length, i5);
                    iArrB = b(f2, i2, i3 + length, i5, cDVar);
                    a(iArrA, iArrB, i2, length + i3 + f2.y(i2));
                }
                U();
            }
            cDVar.b(1.0d);
            try {
                System.arraycopy(iArrB, 0, iArr, length, iArrB.length);
                length += iArrB.length;
                cDVar.k(length);
            } catch (IndexOutOfBoundsException e2) {
                C.c("IndexOutOfBoundsException caught: chunkData.length=" + iArrB.length + ", offset=" + i3);
                e2.printStackTrace();
            }
        } while (i4 - length > 0);
        return iArr;
    }

    @Override // G.J
    protected byte[] a(int[] iArr) {
        return this.f7442g.a(C0995c.a(iArr));
    }

    private int[] b(F f2, int i2, int i3, int i4, C0130m c0130m) throws C0129l {
        y yVar;
        if (i4 <= 0) {
            return new int[0];
        }
        C.c("Upload / Read Chunk: page:" + (i2 + 1) + " len:" + i4);
        a(f2, i2);
        List listA = t.a().a(f2, this.f7438c, i2, i4);
        int i5 = i3;
        byte[] bArr = new byte[i4];
        try {
            a(f2, i2, i5);
            int i6 = 0;
            int i7 = 0;
            while (i7 < listA.size()) {
                this.f7448m = System.currentTimeMillis();
                k kVar = (k) listA.get(i7);
                this.f7444i.p();
                int i8 = i5;
                try {
                    C.c("calling upload, expecting " + kVar.b() + " packets");
                    yVar = this.f7439d.b(kVar);
                    if (yVar == null || yVar.c() == null) {
                        C.c("Got Crap");
                    } else {
                        C.c("Finished upload, got " + yVar.c().size() + " packets");
                    }
                } catch (V.b e2) {
                    yVar = new y();
                    yVar.a(3);
                } catch (bN.o e3) {
                    Logger.getLogger(l.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e3);
                    throw new C0129l("Invalid Packet: " + e3.getLocalizedMessage());
                }
                if (yVar.a() == 3) {
                    int i9 = i6;
                    i6++;
                    if (i9 > 2) {
                        String str = yVar.b() != null ? "\n" + yVar.b() : "";
                        C.b("Giving up. Fail Cout = " + i6);
                        throw new C0129l("Read chunk failed: " + i2 + str);
                    }
                    C.b("Packet Failed on UPLOAD, retrying.");
                    i7--;
                    try {
                        c(this.f7438c.r());
                        this.f7439d.i();
                        this.f7447l = -1L;
                        g();
                        a(f2, i2, i8);
                        i5 = i8;
                    } catch (V.b e4) {
                        throw new C0129l("Timeout on setMTA.");
                    } catch (bN.o e5) {
                        Logger.getLogger(l.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e5);
                        throw new C0129l("Invalid Packet: " + e5.getLocalizedMessage());
                    }
                } else if (yVar.a() != 1) {
                    int i10 = i6;
                    i6++;
                    if (i10 > 2) {
                        throw new C0129l("Read chunk Unknown response: 0x" + Integer.toHexString(yVar.a()).toUpperCase() + ", " + i2 + (yVar.b() != null ? "\n" + yVar.b() : ""));
                    }
                    C.b("Packet not successful on UPLOAD, retrying.");
                    i7--;
                    try {
                        c(this.f7438c.r());
                        this.f7439d.i();
                        this.f7447l = -1L;
                        g();
                        a(f2, i2, i8);
                        i5 = i8;
                    } catch (V.b e6) {
                        throw new C0129l("Timeout on setMTA.");
                    } catch (bN.o e7) {
                        Logger.getLogger(l.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e7);
                        throw new C0129l("Invalid Packet: " + e7.getLocalizedMessage());
                    }
                } else {
                    i6 = 0;
                    int length = 0;
                    Iterator it = yVar.c().iterator();
                    while (it.hasNext()) {
                        try {
                            byte[] bArrC = ((bN.t) it.next()).c();
                            System.arraycopy(bArrC, 0, bArr, (i5 - i3) + length, bArrC.length);
                            length += bArrC.length;
                            if (c0130m != null) {
                                c0130m.b(((i5 - i3) + length) / bArr.length);
                            }
                        } catch (Exception e8) {
                            throw new C0129l("Invalid data.");
                        }
                    }
                    i5 += length;
                    this.f7447l += i5;
                }
                i7++;
            }
            ((N) this.f449ad.get(f2.u())).a(i2, i3, C0995c.b(bArr), true);
            if (b(f2, i2, i3, i4)) {
                return C0995c.b(bArr);
            }
            C.b("CRC doesn't match after upload, raising error. p=" + (i2 + 1) + ", o=" + i3 + ", l=" + i4);
            throw new C0129l("CRC doesn't match after upload");
        } catch (V.b e9) {
            throw new C0129l("Timeout on setMTA.");
        } catch (bN.o e10) {
            Logger.getLogger(l.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e10);
            throw new C0129l("Invalid Packet: " + e10.getLocalizedMessage());
        }
    }

    @Override // G.J
    public boolean c(F f2, int i2) {
        return this.f7442g.a();
    }

    @Override // G.J
    protected boolean a(F f2, int i2, int i3, int i4) {
        return false;
    }

    protected boolean b(F f2, int i2, int i3, int i4) throws C0129l {
        if (!this.f7442g.a()) {
            return false;
        }
        if (f430O) {
            C.c("BUILD_CHECKSUM, page= " + i2 + ", offset= 0x" + Integer.toHexString(i3 + f2.y(i2)).toUpperCase() + ", BlockSize: 0x" + Integer.toHexString(i4));
        }
        a(f2, i2);
        try {
            a(f2, i2, i3);
            int i5 = i4;
            bN.t tVarB = null;
            int i6 = 0;
            do {
                int i7 = i5 > 4096 ? 4096 : i5;
                i5 -= 4096;
                try {
                    i6++;
                    tVarB = this.f7439d.b(t.a().b(this.f7438c, i7), 2);
                    if (tVarB.a() == 255) {
                        byte[] bArr = new byte[i7];
                        System.arraycopy(C0995c.a(((N) this.f449ad.get(f2.u())).b(i2)), i3, bArr, 0, bArr.length);
                        if (!this.f7442g.a(this.f7438c, bArr, tVarB)) {
                            this.f7444i.w();
                            return false;
                        }
                        this.f7444i.v();
                        i3 += bArr.length;
                    }
                } catch (C0129l e2) {
                    throw new C0129l("Connection Error: " + e2.getLocalizedMessage());
                } catch (V.b e3) {
                    if (i6 > 1) {
                        throw new C0129l("Timeout: " + e3.getLocalizedMessage());
                    }
                } catch (bN.o e4) {
                    throw new C0129l("Format: " + e4.getLocalizedMessage());
                }
            } while (i5 > 0);
            return tVarB != null && tVarB.a() == 255;
        } catch (V.b e5) {
            throw new C0129l("Timout: " + e5.getLocalizedMessage());
        } catch (bN.o e6) {
            Logger.getLogger(l.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e6);
            throw new C0129l("Format Error: " + e6.getMessage());
        }
    }

    @Override // G.J
    protected void o(String str) {
        super.o(str);
        R rC = T.a().c(str);
        if (rC != null) {
            this.f7439d.a(rC.O());
        }
    }

    @Override // G.J
    protected boolean k() {
        return this.f420E;
    }

    @Override // G.J, A.h
    public boolean r() {
        return this.f7437b != null && this.f7437b.isAlive();
    }

    @Override // G.J
    public boolean q() {
        return this.f421F && O();
    }

    @Override // G.J
    protected boolean p() {
        return this.f7435ao.m();
    }

    @Override // G.J
    protected boolean a(int i2) {
        return this.f7435ao.a(i2);
    }

    @Override // G.J
    public boolean a(Thread thread) {
        return false;
    }

    @Override // A.g, A.h
    public A.f a() {
        return this.f7435ao;
    }

    @Override // G.J
    public void b(C0130m c0130m) throws IOException {
        super.b(c0130m);
    }

    @Override // A.h
    public void a(A.f fVar) {
        if (this.f7435ao != null) {
            this.f7435ao.b(this.f7439d);
            this.f7435ao.b(this.f7440e);
        }
        this.f7435ao = fVar;
        try {
            this.f7438c.b(fVar.s());
        } catch (IOException e2) {
            Logger.getLogger(l.class.getName()).log(Level.SEVERE, "Unable to set proper Transport for XCP", (Throwable) e2);
        }
        this.f7439d.a(fVar);
        fVar.a(this.f7439d);
        fVar.a(this.f7440e);
    }

    public void j() {
        if (this.f7435ao != null) {
            this.f7435ao.b(this.f7439d);
            this.f7435ao.b(this.f7440e);
        }
        this.f7435ao = null;
    }

    private void V() throws bN.o, C0129l, V.b {
        for (e eVar : this.f7443h) {
            try {
                bN.t tVarB = t.a().b(this.f7438c);
                bN.t tVarB2 = this.f7439d.b(tVarB, 3);
                if (tVarB2 == null || (tVarB2.a() == 254 && tVarB2.c().length > 0)) {
                    while (true) {
                        if (tVarB2 != null && tVarB2.c()[0] != 16 && tVarB2.c()[0] != 18) {
                            break;
                        }
                        String strD = null;
                        if (tVarB2 != null) {
                            strD = C0995c.d(tVarB2.c());
                        }
                        C.b("GET_DAQ_PROCESSOR_INFO Error Code: " + strD + " waiting before retry");
                        a(this.f7438c.s());
                        tVarB2 = this.f7439d.a(tVarB);
                    }
                }
                if (tVarB2.a() == 255) {
                    byte[] bArrC = tVarB2.c();
                    if (bArrC.length != 7) {
                        String str = "GET_DAQ_PROCESSOR_INFO: Invalid Packet Length! " + C0995c.d(tVarB2.d());
                        C.b(str);
                        throw new C0129l(str);
                    }
                    eVar.c().a(bArrC[0]);
                    eVar.c().a(C0995c.a(bArrC, 1, 2, this.f7438c.g(), false));
                    eVar.c().b(C0995c.a(bArrC, 3, 2, this.f7438c.g(), false));
                    eVar.c().c(bArrC[5]);
                    eVar.c().b(bArrC[6]);
                    C.d("Set Processor Info: " + ((Object) eVar.c()));
                } else if (tVarB2.c().length > 0) {
                    C.b("GET_DAQ_PROCESSOR_INFO Error Code: " + C0995c.d(tVarB2.c()) + ", Using defaults");
                }
                for (int i2 = 0; i2 < eVar.c().b(); i2++) {
                    c(eVar, i2);
                }
                try {
                    bN.t tVarC = t.a().c(this.f7438c);
                    bN.t tVarB3 = this.f7439d.b(tVarC, 3);
                    if (tVarB3 == null || (tVarB3.a() == 254 && tVarB3.c().length > 0)) {
                        while (true) {
                            if (tVarB3 != null && tVarB3.c()[0] != 16 && tVarB3.c()[0] != 18) {
                                break;
                            }
                            String strD2 = null;
                            if (tVarB3 != null) {
                                strD2 = C0995c.d(tVarB3.c());
                            }
                            C.b("GET_DAQ_RESOLUTION_INFO Error Code: " + strD2 + " waiting before retry");
                            a(this.f7438c.s());
                            tVarB3 = this.f7439d.a(tVarC);
                        }
                    }
                    if (tVarB3.a() == 255) {
                        byte[] bArrC2 = tVarB3.c();
                        if (bArrC2.length != 7) {
                            String str2 = "GET_DAQ_RESOLUTION_INFO: Invalid Packet Length! " + C0995c.d(tVarB3.d());
                            C.b(str2);
                            throw new C0129l(str2);
                        }
                        eVar.d().e(C0995c.a(bArrC2[0]));
                        eVar.d().a(C0995c.a(bArrC2[1]));
                        eVar.d().b(C0995c.a(bArrC2[2]));
                        eVar.d().c(C0995c.a(bArrC2[3]));
                        eVar.d().d().a(bArrC2[4]);
                        int iA = C0995c.a(bArrC2, 5, 2, this.f7438c.g(), false);
                        eVar.d().d(iA);
                        this.f7438c.a(eVar.d().d().a());
                        C.d("Set DAQ Resolution Info: " + ((Object) eVar.d()));
                        C.c("TimeStamp: " + eVar.d().d().toString());
                        C.c("TimeStamp ticks = " + iA);
                    } else if (tVarB3.c().length > 0) {
                        C.b("GET_DAQ_RESOLUTION_INFO Error Code: " + C0995c.d(tVarB3.c()) + ", Using defaults");
                    }
                    eVar.a(eVar.c().b());
                    for (int iD = eVar.c().d(); iD < eVar.c().b(); iD++) {
                        try {
                            f(eVar, iD);
                        } catch (C0129l e2) {
                            f(eVar, iD);
                        }
                    }
                } catch (V.b e3) {
                    Logger.getLogger(l.class.getName()).log(Level.SEVERE, "Timeout initializing DAQ", (Throwable) e3);
                    throw e3;
                } catch (bN.o e4) {
                    Logger.getLogger(l.class.getName()).log(Level.SEVERE, "Failed to get DAQ Processor Info", (Throwable) e4);
                    throw e4;
                }
            } catch (V.b e5) {
                Logger.getLogger(l.class.getName()).log(Level.SEVERE, "Timeout initializing DAQ", (Throwable) e5);
                throw e5;
            } catch (bN.o e6) {
                Logger.getLogger(l.class.getName()).log(Level.SEVERE, "Failed to get DAQ Processor Info", (Throwable) e6);
                throw e6;
            }
        }
    }

    private e q(String str) {
        for (e eVar : this.f7443h) {
            if (eVar.a().equals(str)) {
                return eVar;
            }
        }
        return null;
    }

    @Override // G.J
    protected C0132o o(C0130m c0130m) throws C0129l, V.b {
        C0132o c0132o = new C0132o();
        c0132o.a(c0130m);
        c0130m.m();
        a(q(c0130m.v().u()), c0130m.i());
        c0132o.a(1);
        c0130m.b(c0132o);
        return c0132o;
    }

    protected void a(e eVar, int i2) throws C0129l, V.b {
        bO.c cVarB = eVar.b(i2);
        C.c("XcpMasterCommDriver:: updating DAQ List: " + cVarB.toString());
        try {
            c(eVar, i2);
            b(eVar, i2);
            int i3 = 0;
            do {
                a(eVar, i2, i3, 0);
                Iterator it = cVarB.c(i3).iterator();
                while (it.hasNext()) {
                    bO.l lVar = (bO.l) it.next();
                    a(eVar, (byte) -1, (byte) lVar.b(), (byte) 0, lVar.a());
                }
                i3++;
            } while (i3 < cVarB.f());
            if (cVarB.m()) {
                d(eVar, i2);
            }
            if (!this.f407r.isEmpty()) {
                Iterator it2 = this.f407r.iterator();
                while (it2.hasNext()) {
                    a((aF) it2.next());
                }
                this.f407r.clear();
            }
        } catch (bN.o e2) {
            Logger.getLogger(l.class.getName()).log(Level.SEVERE, "Invalid Command Structure, critical failure.", (Throwable) e2);
        } catch (bO.j e3) {
            String strU = e().u();
            String str = e3.getMessage() + " Channels active: " + C0113cs.a().b(strU);
            Logger.getLogger(l.class.getName()).log(Level.SEVERE, "Error writing DAQ.", (Throwable) e3);
            b(strU, str);
            throw new V.b("Error updating Runtime tables.");
        }
    }

    protected void a(e eVar, byte b2, byte b3, byte b4, long j2) throws bN.o, bO.j, V.b {
        try {
            bN.t tVarA = t.a().a(this.f7438c, b2, b3, b4, j2);
            bN.t tVarB = this.f7439d.b(tVarA, 3);
            if (tVarB == null || (tVarB.a() == 254 && tVarB.c().length > 0)) {
                while (true) {
                    if (tVarB != null && (tVarB.c().length <= 0 || (tVarB.c()[0] != 16 && tVarB.c()[0] != 18))) {
                        break;
                    }
                    String strD = null;
                    if (tVarB != null) {
                        strD = C0995c.d(tVarB.c());
                    }
                    C.b("WRITE_DAQ Error Code: " + strD + " waiting before retry");
                    a(this.f7438c.s());
                    tVarB = this.f7439d.a(tVarA);
                }
            }
            if (tVarB.a() != 255) {
                if (tVarB.c().length > 0) {
                    C.b("WRITE_DAQ Error Code: " + C0995c.d(tVarB.c()) + ", Using defaults");
                }
                eVar.d(-1);
                eVar.e(-1);
                eVar.f(-1);
                throw new bO.j("Error writing ODT Entry");
            }
            bO.c cVarB = eVar.b(eVar.h());
            int iJ = eVar.j() + 1;
            if (iJ < cVarB.c()) {
                eVar.f(iJ);
            } else {
                eVar.d(-1);
                eVar.e(-1);
                eVar.f(-1);
            }
        } catch (V.b e2) {
            C.a("Timeout initializing DAQ");
            throw e2;
        } catch (bN.o e3) {
            C.a("Failed to get DAQ Processor Info");
            throw e3;
        }
    }

    protected void b(e eVar, int i2) throws bN.o, bO.j, V.b {
        try {
            bN.t tVarD = t.a().d(this.f7438c, i2);
            bN.t tVarB = this.f7439d.b(tVarD, 3);
            if (tVarB == null || (tVarB.a() == 254 && tVarB.c().length > 0)) {
                while (true) {
                    if (tVarB != null && tVarB.c()[0] != 16 && tVarB.c()[0] != 18) {
                        break;
                    }
                    String strD = null;
                    if (tVarB != null) {
                        strD = C0995c.d(tVarB.c());
                    }
                    C.b("CLEAR_DAQ_LIST Error Code: " + strD + " waiting before retry");
                    a(this.f7438c.s());
                    tVarB = this.f7439d.a(tVarD);
                }
            }
            if (tVarB.a() == 255) {
                C.d("Cleared DAQ List " + i2);
            } else {
                if (tVarB.c().length > 0) {
                    C.b("CLEAR_DAQ_LIST Error Code: " + C0995c.d(tVarB.c()) + ", Using defaults");
                }
                throw new bO.j("Error Clearing DAQ List");
            }
        } catch (V.b e2) {
            C.a("Timeout initializing DAQ");
            throw e2;
        } catch (bN.o e3) {
            C.a("Failed to get DAQ Processor Info");
            throw e3;
        }
    }

    protected void a(e eVar, int i2, int i3, int i4) throws bN.o, bO.j, V.b {
        if (eVar.h() == i2 && eVar.i() == i3 && eVar.j() == i4) {
            return;
        }
        try {
            bN.t tVarA = t.a().a(this.f7438c, i2, i3, i4);
            bN.t tVarB = this.f7439d.b(tVarA, 3);
            if (tVarB == null || (tVarB.a() == 254 && tVarB.c().length > 0)) {
                while (true) {
                    if (tVarB != null && tVarB.c()[0] != 16 && tVarB.c()[0] != 18) {
                        break;
                    }
                    String strD = null;
                    if (tVarB != null) {
                        strD = C0995c.d(tVarB.c());
                    }
                    C.b("SET_DAQ_PTR Error Code: " + strD + " waiting before retry");
                    a(this.f7438c.s());
                    tVarB = this.f7439d.a(tVarA);
                }
            }
            if (tVarB.a() != 255) {
                if (tVarB.c().length > 0) {
                    C.b("SET_DAQ_PTR Error Code: " + C0995c.d(tVarB.c()) + ", Using defaults");
                }
                throw new bO.j("Error Setting DAQ PTR");
            }
            if (f430O) {
                C.d("Set DAQ PTR  list:" + i2 + ", odtNum:" + i3 + ", odtEntryNum: " + i4);
            }
            eVar.d(i2);
            eVar.e(i3);
            eVar.f(i4);
        } catch (V.b e2) {
            C.a("Timeout initializing DAQ");
            throw e2;
        } catch (bN.o e3) {
            C.a("Failed to get DAQ Processor Info");
            throw e3;
        }
    }

    @Override // G.J
    protected C0132o p(C0130m c0130m) throws V.b {
        C0132o c0132o = new C0132o();
        c0132o.a(c0130m);
        c0130m.m();
        try {
            c(q(c0130m.v().u()), c0130m.i());
        } catch (bN.o e2) {
            Logger.getLogger(l.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            c0132o.a(3);
        }
        c0130m.b(c0132o);
        return c0132o;
    }

    public void l() throws C0129l, V.b {
        int i2;
        if (e().av() > 0) {
            for (e eVar : this.f7443h) {
                for (int iD = eVar.b().b().d(); iD < eVar.b().b().b(); iD++) {
                    int i3 = 0;
                    do {
                        try {
                            d(eVar, iD);
                            if (1 != 0) {
                                break;
                            }
                            i2 = i3;
                            i3++;
                        } catch (bN.o e2) {
                            Logger.getLogger(l.class.getName()).log(Level.SEVERE, "FormatException??", (Throwable) e2);
                        }
                    } while (i2 < 3);
                }
            }
        }
    }

    public void m() throws V.b {
        int i2;
        for (e eVar : this.f7443h) {
            for (int iD = eVar.b().b().d(); iD < eVar.b().b().b(); iD++) {
                int i3 = 0;
                do {
                    try {
                        c(eVar, iD);
                        if (1 != 0) {
                            break;
                        }
                        i2 = i3;
                        i3++;
                    } catch (bN.o e2) {
                        Logger.getLogger(l.class.getName()).log(Level.SEVERE, "FormatException??", (Throwable) e2);
                    }
                } while (i2 < 3);
            }
        }
    }

    protected void a(byte b2) throws bN.o, V.b {
        try {
            bN.t tVarB = this.f7439d.b(t.a().a(this.f7438c, b2), 3);
            if (tVarB == null || tVarB.a() == 255) {
                if (b2 == 0) {
                    C.d("Stop Sync DAQ ");
                } else if (b2 == 1) {
                    C.d("Start Sync List ");
                }
                if (tVarB.c().length > 0 && tVarB.c()[0] == 0) {
                    Q();
                }
            } else {
                C.b("START_STOP_SYNC Failed, DAQ not running?");
            }
        } catch (V.b e2) {
            C.a("Timeout StartStop DAQ");
            throw e2;
        } catch (bN.o e3) {
            Logger.getLogger(l.class.getName()).log(Level.SEVERE, "Failed to get DAQ Processor Info", (Throwable) e3);
            throw e3;
        }
    }

    protected void o() throws bN.o, V.b {
        a((byte) 0);
    }

    protected void s() throws bN.o, V.b {
        a((byte) 1);
    }

    protected void c(e eVar, int i2) throws bN.o, V.b {
        C.c("################### Stopping DAQ " + i2);
        a(eVar, i2, (byte) 0);
    }

    protected void d(e eVar, int i2) throws bN.o, C0129l, V.b {
        if (e().av() > 0) {
            C.c("################### Starting DAQ " + i2);
            this.f7448m = System.currentTimeMillis();
            g(eVar, i2);
            a(eVar, i2, (byte) 2);
            e(eVar, i2);
            s();
            C.c("################### Started DAQ " + i2);
        }
    }

    protected void a(e eVar, int i2, byte b2) throws bN.o, V.b {
        try {
            bN.t tVarA = t.a().a(this.f7438c, i2, b2);
            bN.t tVarB = this.f7439d.b(tVarA, 3);
            this.f7448m = System.currentTimeMillis();
            if (tVarB == null || (tVarB.a() == 254 && tVarB.c().length > 0)) {
                while (true) {
                    if (tVarB != null && tVarB.c()[0] != 16 && tVarB.c()[0] != 18) {
                        break;
                    }
                    String strD = null;
                    if (tVarB != null) {
                        strD = C0995c.d(tVarB.c());
                    }
                    C.b("START_STOP_DAQ Error Code: " + strD + " waiting before retry");
                    a(this.f7438c.s());
                    tVarB = this.f7439d.a(tVarA);
                }
            }
            if (tVarB.a() == 255) {
                if (b2 == 0) {
                    C.d("Stopped DAQ List " + i2);
                } else if (b2 == 1) {
                    C.d("Started DAQ List " + i2);
                }
                byte[] bArrC = tVarB.c();
                bO.c cVarB = eVar.b(i2);
                if (cVarB == null) {
                    C.b("Invalid DAQ: " + i2);
                } else if (bArrC == null || bArrC.length < 1) {
                    C.b("No FIRST_PID on START_STOP_DAQ");
                } else {
                    cVarB.h(C0995c.a(bArrC[0]));
                }
            } else if (tVarB.c().length > 0) {
                C.b("START_STOP_DAQ Error Code: " + C0995c.d(tVarB.c()) + ", Using defaults");
            }
        } catch (V.b e2) {
            C.a("Timeout StartStop DAQ");
            throw e2;
        } catch (bN.o e3) {
            Logger.getLogger(l.class.getName()).log(Level.SEVERE, "Failed to get DAQ Processor Info", (Throwable) e3);
            throw e3;
        }
    }

    protected void e(e eVar, int i2) throws bN.o, V.b {
        if (eVar.d().d().a() == 0) {
            C.d("Timestamp not supported by connected device, using local clock.");
            return;
        }
        try {
            bN.t tVarD = t.a().d(this.f7438c);
            bN.t tVarB = this.f7439d.b(tVarD, 3);
            if (tVarB == null || (tVarB.a() == 254 && tVarB.c().length > 0)) {
                while (true) {
                    if (tVarB != null && tVarB.c()[0] != 16 && tVarB.c()[0] != 18) {
                        break;
                    }
                    String strD = null;
                    if (tVarB != null) {
                        strD = C0995c.d(tVarB.c());
                    }
                    C.b("GET_DAQ_CLOCK Error Code: " + strD + " waiting before retry");
                    a(this.f7438c.s());
                    tVarB = this.f7439d.a(tVarD);
                }
            }
            if (tVarB.a() == 255) {
                byte[] bArrC = tVarB.c();
                bO.c cVarB = eVar.b(i2);
                if (cVarB == null) {
                    C.b("Invalid DAQ: " + i2);
                } else if (bArrC == null || bArrC.length != 7) {
                    C.b("No Timestamp on GET_DAQ_CLOCK, wrong packet size");
                } else {
                    long jB = C0995c.b(bArrC, 3, 4, this.f7438c.g(), false);
                    C.c("Set timestamp of size: " + eVar.d().d().a() + " to value: " + jB);
                    cVarB.a(jB);
                }
            } else if (tVarB.c().length > 0) {
                C.b("GET_DAQ_CLOCK Error Code: " + C0995c.d(tVarB.c()) + ", Using defaults");
            } else {
                C.b("GET_DAQ_CLOCK Error");
            }
        } catch (V.b e2) {
            C.a("Timeout getting DAQ clock");
            throw e2;
        } catch (bN.o e3) {
            Logger.getLogger(l.class.getName()).log(Level.SEVERE, "Failed to get DAQ clock", (Throwable) e3);
            throw e3;
        }
    }

    private void f(e eVar, int i2) throws bN.o, C0129l, V.b {
        try {
            bN.t tVarC = t.a().c(this.f7438c, i2);
            bN.t tVarB = this.f7439d.b(tVarC, 3);
            if (tVarB == null || (tVarB.a() == 254 && tVarB.c().length > 0)) {
                while (true) {
                    if (tVarB != null && tVarB.c()[0] != 16 && tVarB.c()[0] != 18) {
                        break;
                    }
                    String strD = null;
                    if (tVarB != null) {
                        strD = C0995c.d(tVarB.c());
                    }
                    C.b("GET_DAQ_LIST_INFO Error Code: " + strD + " waiting before retry");
                    a(this.f7438c.s());
                    tVarB = this.f7439d.a(tVarC);
                }
            }
            if (tVarB.a() == 255) {
                byte[] bArrC = tVarB.c();
                if (bArrC.length != 5) {
                    String str = "GET_DAQ_LIST_INFO: Invalid Packet Length! " + C0995c.d(tVarB.d());
                    C.b(str);
                    throw new C0129l(str);
                }
                bO.c cVarB = eVar.b(i2);
                cVarB.b().a(bArrC[0]);
                cVarB.a(C0995c.a(bArrC[1]));
                cVarB.b(C0995c.a(bArrC[2]));
                cVarB.d(C0995c.a(bArrC, 3, 2, this.f7438c.g(), false));
                C.d("DAQ List " + i2 + " Info: " + ((Object) cVarB));
            } else if (tVarB.c().length > 0) {
                C.b("GET_DAQ_LIST_INFO Error Code: " + C0995c.d(tVarB.c()) + ", Using defaults");
            }
        } catch (V.b e2) {
            C.a("Timeout configuring DAQ");
            throw e2;
        } catch (bN.o e3) {
            Logger.getLogger(l.class.getName()).log(Level.SEVERE, "Failed to get DAQ Processor Info", (Throwable) e3);
            throw e3;
        }
    }

    private void g(e eVar, int i2) throws bN.o, C0129l, V.b {
        try {
            bN.t tVarA = t.a().a(this.f7438c, eVar.c().a().b(), false, i2, eVar.b(i2).d(), (byte) Math.max(100 / e().av(), 1), (byte) eVar.b(i2).j());
            C.c("SET_DAQ_LIST_MODE: " + C0995c.d(tVarA.d()));
            bN.t tVarB = this.f7439d.b(tVarA, 3);
            if (tVarB == null || (tVarB.a() == 254 && tVarB.c().length > 0)) {
                while (true) {
                    if (tVarB != null && tVarB.c()[0] != 16 && tVarB.c()[0] != 18) {
                        break;
                    }
                    String strD = null;
                    if (tVarB != null) {
                        strD = C0995c.d(tVarB.c());
                    }
                    C.b("SET_DAQ_LIST_MODE Error Code: " + strD + " Error: " + bN.n.a(tVarB.c()[0]) + " waiting before retry");
                    a(this.f7438c.s());
                    tVarB = this.f7439d.a(tVarA);
                }
            }
            if (tVarB.a() != 255) {
                if (tVarB.c().length > 0) {
                    C.b("SET_DAQ_LIST_MODE Error Code: " + C0995c.d(tVarB.c()) + " Error: " + bN.n.a(tVarB.c()[0]) + " , Using defaults");
                }
            } else if (tVarB.c().length != 0) {
                String str = "SET_DAQ_LIST_MODE: Invalid Packet Length! " + C0995c.d(tVarB.d());
                C.b(str);
                throw new C0129l(str);
            }
        } catch (V.b e2) {
            C.a("Timeout SET_LIST_MODE DAQ");
            throw e2;
        } catch (bN.o e3) {
            Logger.getLogger(l.class.getName()).log(Level.SEVERE, "Failed to get DAQ Processor Info", (Throwable) e3);
            throw e3;
        }
    }

    @Override // G.J
    protected boolean d(int i2) throws IOException, V.b {
        bN.t tVarT;
        try {
            v();
            try {
                tVarT = T();
            } catch (Exception e2) {
                tVarT = T();
            }
            if (tVarT == null) {
                return false;
            }
            this.f7447l = -1L;
            o();
            byte[] bArrC = tVarT.c();
            C.d("Connected with Controller, Protocol version: " + ((int) bArrC[5]) + " Transport version: " + ((int) bArrC[6]));
            this.f7438c.p().a(bArrC[0]);
            this.f7438c.t().a(bArrC[1]);
            this.f7438c.f(C0995c.a(bArrC[2]));
            this.f7438c.g(C0995c.a(bArrC, 3, 2, this.f7438c.g(), false));
            if (bArrC[5] > 1) {
                f("Unsupported Protocol Level: " + ((int) bArrC[5]));
                return false;
            }
            bN.t tVarB = this.f7439d.b(t.a().e(), 2);
            if (tVarB == null || tVarB.a() != 255) {
                C.a("Received Valid Connect, but failed GET_COMM_MODE_INFO. Using defaults.");
            } else if (tVarB.c().length < 7) {
                C.d("Valid GET_COMM_MODE_INFO, but invalid payload, using defaults.");
            } else {
                byte[] bArrC2 = tVarB.c();
                this.f7438c.h(C0995c.a(bArrC2[4]));
                if (C0995c.a(bArrC2[6]) > f7451ak) {
                    String str = "Dash Echo Server has greater driver version than client, app needs to be updated.\nServer version: " + C0995c.a(bArrC2[6]) + ", Client Version: " + f7451ak;
                    C.a(str);
                    aB.a().b(e().u(), str);
                    return false;
                }
                C.d("Slave Driver version: " + C0995c.a(bArrC2[6]));
            }
            bN.t tVarB2 = this.f7439d.b(t.a().a((byte) 0), 2);
            if (tVarB2 == null || tVarB2.a() != 255) {
                C.a("Received Valid Connect, but failed GET_ID type 1. Support for Type 1 Get_ID is required to connect.");
                return false;
            }
            if (tVarB2.c().length < 7) {
                C.d("Valid Connect response code, but invalid packet size.");
                return false;
            }
            byte[] bArrC3 = tVarB2.c();
            byte b2 = bArrC3[0];
            int iA = C0995c.a(bArrC3, 3, 4, this.f7438c.g(), false);
            if (iA == 0) {
                C.a("GET_ID: Len must be greater than 0");
                return false;
            }
            byte[] bArr = new byte[iA];
            if (b2 == 1) {
                System.arraycopy(bArrC3, 7, bArr, 0, bArr.length);
            } else {
                C.a("GET_ID:: Mode 1 not yet supported.");
            }
            F fE = e();
            String str2 = null;
            if (fE.al() != null && fE.al().equals("basicRequestReply")) {
                try {
                    bN.t tVarB3 = this.f7439d.b(t.a().b(bV.b.f7629a), 2);
                    if (tVarB3 == null || tVarB3.a() != 255) {
                        C.a("Received Valid Connect, but failed GetFirmwareinfo.");
                    }
                    str2 = new String(tVarB3.c());
                } catch (Exception e3) {
                    C.a("GetFirmwareInfo failed: " + e3.getLocalizedMessage());
                }
                try {
                    bN.t tVarB4 = this.f7439d.b(t.a().b(bV.b.f7630b), 2);
                    if (tVarB4 == null || tVarB4.a() != 255) {
                        C.a("Received Valid Connect, but failed Get Blocking factors.");
                    }
                    byte[] bArrC4 = tVarB4.c();
                    if (bArrC4 == null || bArrC4.length != 4) {
                        C.b("Unexpected response size for Get Blocking factors: " + C0995c.d(bArrC4));
                    } else {
                        int iA2 = C0995c.a(bArrC4, 0, 2, true, false);
                        int iA3 = C0995c.a(bArrC4, 2, 2, true, false);
                        fE.I(iA2);
                        fE.H(iA3);
                    }
                } catch (Exception e4) {
                    C.a("Get Blocking factors failed: " + e4.getLocalizedMessage());
                }
                try {
                    bN.t tVarB5 = this.f7439d.b(t.a().b(bV.b.f7631c), 2);
                    if (tVarB5 == null || tVarB5.a() != 255) {
                        C.a("Received Valid Connect, but failed Get Local CAN ID.");
                    }
                    byte[] bArrC5 = tVarB5.c();
                    if (bArrC5 == null || bArrC5.length != 1) {
                        C.b("Unexpected response size for Get Local CAN ID: " + C0995c.d(bArrC5));
                    } else {
                        fE.w(C0995c.a(bArrC5[0]));
                    }
                } catch (Exception e5) {
                    C.a("Get Local CAN ID failed: " + e5.getLocalizedMessage());
                }
            }
            try {
                V();
            } catch (C0129l e6) {
                try {
                    C.a("Initialize DAQ Failed, try again.");
                    V();
                } catch (C0129l e7) {
                    C.a("Initialize DAQ Failed again, raising error");
                    throw new IOException("Initialize DAQ Failed.");
                }
            }
            bS bSVar = new bS();
            bSVar.a(bArr);
            if (!b(bArr)) {
                C.b("Unsupported Controller Firmware.");
                b(bSVar.b(), "Invalid data received from controller.");
                return false;
            }
            this.f420E = true;
            this.f421F = true;
            this.f422G = true;
            this.f7448m = System.currentTimeMillis();
            u();
            s sVar = new s(this);
            sVar.start();
            if (str2 == null || str2.isEmpty()) {
                bSVar.b(bSVar.b());
            } else {
                bSVar.b(str2);
            }
            a(e().u(), bSVar.c(), bSVar);
            sVar.a();
            if (e().av() <= 0) {
                return true;
            }
            ((e) this.f7443h.get(0)).d(-1);
            ((e) this.f7443h.get(0)).e(-1);
            ((e) this.f7443h.get(0)).f(-1);
            ((e) this.f7443h.get(0)).k();
            return true;
        } catch (bN.o e8) {
            Logger.getLogger(l.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e8);
            return false;
        }
    }

    public void a(byte[] bArr, double d2) {
        if (1 == 0) {
            a(e().u(), bArr);
            return;
        }
        if (this.f7445j == null || !this.f7445j.isAlive()) {
            if (this.f7445j != null) {
                this.f7445j.a();
            }
            this.f7445j = new o(this);
            this.f7445j.start();
        }
        this.f7445j.a(bArr, d2);
        this.f7448m = System.currentTimeMillis();
    }

    public void P() {
        R rC = T.a().c();
        this.f422G = true;
        this.f7448m = System.currentTimeMillis();
        if (rC != null) {
            bS bSVar = new bS();
            bSVar.a(rC.i());
            bSVar.b(rC.P());
            super.a(rC.c(), rC.P(), bSVar);
        }
    }

    public void Q() {
        this.f422G = false;
        super.A();
    }

    public void p(String str) {
        super.a(str, true);
    }

    @Override // G.J
    protected void b(String str) {
        if (I()) {
            System.out.println(bN.k.v() + ": " + str);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean b(A.f fVar) {
        if (fVar == null) {
            return false;
        }
        Iterator it = f7455ap.values().iterator();
        while (it.hasNext()) {
            if (((A.f) it.next()).equals(fVar)) {
                return true;
            }
        }
        return false;
    }
}
