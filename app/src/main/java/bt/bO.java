package bt;

import G.C0046af;
import G.C0072be;
import G.C0075bh;
import G.C0088bu;
import G.C0104cj;
import G.cZ;
import bF.C0970a;
import bF.C0972c;
import com.efiAnalytics.ui.C1677fh;
import com.efiAnalytics.ui.C1701s;
import com.efiAnalytics.ui.C1703u;
import com.efiAnalytics.ui.eY;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import r.C1807j;

/* loaded from: TunerStudioMS.jar:bt/bO.class */
public class bO implements aE.e {

    /* renamed from: a, reason: collision with root package name */
    HashMap f8920a = new HashMap();

    /* renamed from: b, reason: collision with root package name */
    HashMap f8921b = new HashMap();

    /* renamed from: c, reason: collision with root package name */
    HashMap f8922c = new HashMap();

    /* renamed from: d, reason: collision with root package name */
    HashMap f8923d = new HashMap();

    /* renamed from: e, reason: collision with root package name */
    ArrayList f8924e = new ArrayList();

    /* renamed from: f, reason: collision with root package name */
    ArrayList f8925f = new ArrayList();

    /* renamed from: k, reason: collision with root package name */
    private bH.aa f8926k = null;

    /* renamed from: l, reason: collision with root package name */
    private String f8927l = "lambdaDelay_";

    /* renamed from: m, reason: collision with root package name */
    private boolean f8928m = true;

    /* renamed from: n, reason: collision with root package name */
    private boolean f8929n = true;

    /* renamed from: j, reason: collision with root package name */
    private static bO f8919j = null;

    /* renamed from: g, reason: collision with root package name */
    public static int f8930g = 1;

    /* renamed from: h, reason: collision with root package name */
    public static int f8931h = 2;

    /* renamed from: i, reason: collision with root package name */
    public static int f8932i = -1;

    private bO() {
    }

    public static bO a() {
        if (f8919j == null) {
            f8919j = new bO();
        }
        return f8919j;
    }

    public String a(String str) {
        return this.f8926k != null ? this.f8926k.a(str) : str;
    }

    public C1701s a(G.R r2, String str) {
        return a(r2, str, "", r2.e().c("veTable1Tbl") != null ? "veTable1Tbl" : "");
    }

    public C1701s a(G.R r2, String str, String str2) {
        return a(r2, str, "", str2);
    }

    public C1701s a(G.R r2, String str, String str2, String str3) throws V.g {
        if (!this.f8928m) {
            try {
                C1701s c1701sB = b(r2, str);
                c1701sB.addTableModelListener(new bQ(this, r2, c1701sB, str));
                return c1701sB;
            } catch (V.a e2) {
                Logger.getLogger(bO.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
                throw new V.g("Unable to create BinTable.", e2);
            }
        }
        String str4 = r2.c() + "." + str2 + str;
        if (!this.f8920a.containsKey(str4)) {
            if (!b(str)) {
                try {
                    C1701s c1701sB2 = b(r2, str);
                    bQ bQVar = new bQ(this, r2, c1701sB2, str);
                    this.f8924e.add(bQVar);
                    c1701sB2.addTableModelListener(bQVar);
                    this.f8920a.put(str4, c1701sB2);
                } catch (V.g e3) {
                    throw e3;
                } catch (Exception e4) {
                    e4.printStackTrace();
                    throw new V.g("Unable to create BinTable.", e4);
                }
            } else if (str.equals("afrTSCustom")) {
                try {
                    this.f8920a.put(str4, d(r2, str, str3));
                } catch (V.a e5) {
                    e5.printStackTrace();
                    throw new V.g(e5.getMessage());
                }
            }
        }
        return (C1701s) this.f8920a.get(str4);
    }

    public C1701s b(G.R r2, String str, String str2) {
        return (C1701s) this.f8920a.get(r2.c() + "." + str2 + str);
    }

    public boolean b(String str) {
        return str == null || str.equals("afrTSCustom");
    }

    private C1701s d(G.R r2, String str, String str2) {
        String str3 = "afr_" + str;
        C1701s c1701sA = a().a(r2, str2, str2);
        C1701s c1701sB = null;
        String strT = aE.a.A().t();
        File fileA = C1807j.a(strT, str3);
        if (fileA.exists()) {
            c1701sB = null;
            try {
                c1701sB = new eY().a(fileA.getAbsolutePath());
                if (c1701sA != null) {
                    c1701sB.d(c1701sA.v());
                    c1701sB.e(c1701sA.w());
                }
            } catch (V.a e2) {
                bH.C.a("Error loading AFR Table from file:\n" + ((Object) fileA) + "\nUsing default.");
                e2.printStackTrace();
            }
        }
        if (c1701sB == null) {
            C1701s c1701s = new C1701s();
            c1701s.a(c1701sA.getRowCount(), c1701sA.getColumnCount());
            C1677fh.a(c1701sA, c1701s);
            c1701sB = C1703u.b(c1701s);
        }
        c1701sB.addTableModelListener(new bP(this, c1701sB, strT, str3));
        c1701sB.f(1);
        c1701sB.q();
        return c1701sB;
    }

    public C1701s b(G.R r2, String str) throws V.g {
        C0072be c0072be = (C0072be) r2.e().c(str);
        if (c0072be == null) {
            throw new V.g(str + " not found in current Configuration, can not create Model.");
        }
        C1701s c1701s = new C1701s();
        G.aM aMVarC = r2.c(c0072be.c());
        G.aM aMVarC2 = r2.c(c0072be.b());
        G.aM aMVarC3 = r2.c(c0072be.a());
        c1701s.a(aMVarC.i(r2.p()));
        c1701s.d(c0072be.f());
        c1701s.e(c0072be.d());
        c1701s.d(a(aMVarC2.i(r2.p()), aMVarC2.u()));
        c1701s.c(a(aMVarC3.i(r2.p()), aMVarC3.u()));
        c1701s.q();
        bU bUVar = new bU(this, r2, str, c1701s);
        bUVar.d();
        this.f8922c.put(str, bUVar);
        return c1701s;
    }

    public bF.y c(G.R r2, String str) throws V.g {
        C0088bu c0088buC = r2.e().c(str);
        if (c0088buC != null && !(c0088buC instanceof C0075bh)) {
            throw new V.g(str + ", name already used but not defined as a 1D Table");
        }
        C0075bh c0075bh = (C0075bh) c0088buC;
        if (c0075bh == null) {
            throw new V.g(str + " not found in current Configuration, can not create Model.");
        }
        bF.y yVar = new bF.y();
        yVar.a(c0075bh.i());
        if (c0075bh.h()) {
            yVar.a(new C0970a(r2, c0075bh.d(0)));
        } else {
            yVar.b(c0075bh.i());
            yVar.d(!c0075bh.i());
            yVar.c(c0075bh.h());
            if (yVar.g()) {
                G.aM aMVarC = r2.c(c0075bh.d(0));
                C0972c c0972c = new C0972c(aMVarC.b());
                c0972c.a(aMVarC.v());
                c0972c.b(aMVarC.t());
                c0972c.c(aMVarC.s());
                c0972c.d(new C0104cj(aMVarC));
                cZ cZVarF = c0075bh.f(0);
                if (cZVarF != null) {
                    c0972c.a(cZVarF);
                }
                yVar.a(c0972c);
                double[][] dArrI = aMVarC.i(r2.h());
                for (int i2 = 0; i2 < dArrI.length; i2++) {
                    c0972c.a(i2, Double.valueOf(dArrI[i2][0]));
                }
                c0972c.f();
            }
        }
        for (int i3 = 0; i3 < c0075bh.a(); i3++) {
            G.aM aMVarC2 = r2.c(c0075bh.b(i3));
            C0972c c0972c2 = new C0972c(aMVarC2.b());
            c0972c2.a(aMVarC2.v());
            c0972c2.b(aMVarC2.t());
            c0972c2.c(aMVarC2.s());
            c0972c2.d(new C0104cj(aMVarC2));
            cZ cZVarC = c0075bh.c(i3);
            if (cZVarC != null) {
                c0972c2.a(cZVarC);
            }
            c0972c2.a(new C0046af(r2, c0075bh.a(i3)));
            yVar.a(c0972c2);
            double[][] dArrI2 = aMVarC2.i(r2.h());
            for (int i4 = 0; i4 < dArrI2.length; i4++) {
                c0972c2.a(i4, Double.valueOf(dArrI2[i4][0]));
            }
            c0972c2.f();
        }
        if (yVar.h()) {
            G.aM aMVarC3 = r2.c(c0075bh.d(0));
            C0972c c0972c3 = new C0972c(aMVarC3.b());
            c0972c3.a(aMVarC3.v());
            c0972c3.b(aMVarC3.t());
            c0972c3.c(aMVarC3.s());
            c0972c3.d(new C0104cj(aMVarC3));
            cZ cZVarF2 = c0075bh.f(0);
            if (cZVarF2 != null) {
                c0972c3.a(cZVarF2);
            }
            yVar.a(c0972c3);
            double[][] dArrI3 = aMVarC3.i(r2.h());
            for (int i5 = 0; i5 < dArrI3.length; i5++) {
                c0972c3.a(i5, Double.valueOf(dArrI3[i5][0]));
            }
            c0972c3.f();
        }
        return yVar;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String[] a(double[][] dArr, int i2) {
        String[] strArr = new String[dArr.length];
        for (int i3 = 0; i3 < dArr.length; i3++) {
            strArr[i3] = "" + bH.W.b(dArr[i3][0], i2);
        }
        return strArr;
    }

    public C1701s a(C1701s c1701s, String str) {
        String str2 = "lambdaDelay_" + str;
        C1701s c1701sA = (C1701s) this.f8920a.get(str2);
        if (c1701sA == null) {
            String strT = aE.a.A() != null ? aE.a.A().t() : h.h.a().getAbsolutePath();
            File fileA = C1807j.a(strT, str2);
            if (fileA.exists()) {
                c1701sA = null;
                try {
                    c1701sA = new eY().a(fileA.getAbsolutePath());
                } catch (V.a e2) {
                    bH.C.a("Error loading Lambda Delay Table from file:\n" + ((Object) fileA) + "\nUsing default.");
                    e2.printStackTrace();
                }
            }
            if (c1701sA == null) {
                c1701sA = C1703u.a(c1701s);
            }
            c1701sA.addTableModelListener(new bP(this, c1701sA, strT, str2));
            c1701sA.f(0);
            c1701sA.q();
            this.f8920a.put(str2, c1701sA);
        }
        return c1701sA;
    }

    public void c() {
        Iterator it = this.f8920a.values().iterator();
        while (it.hasNext()) {
            ((C1701s) it.next()).q();
        }
    }

    @Override // aE.e
    public void a(aE.a aVar, G.R r2) {
    }

    @Override // aE.e
    public void e_() {
        this.f8920a.clear();
        this.f8921b.clear();
        this.f8923d.clear();
        Iterator it = this.f8924e.iterator();
        while (it.hasNext()) {
            ((bQ) it.next()).a();
        }
        this.f8924e.clear();
        Iterator it2 = this.f8925f.iterator();
        while (it2.hasNext()) {
            ((bS) it2.next()).a();
        }
        this.f8925f.clear();
        Iterator it3 = this.f8922c.values().iterator();
        while (it3.hasNext()) {
            ((bU) it3.next()).c();
        }
        this.f8922c.clear();
    }

    @Override // aE.e
    public void a(aE.a aVar) {
    }

    public bF.y c(G.R r2, String str, String str2) {
        if (!this.f8928m) {
            try {
                bF.y yVarC = c(r2, str);
                bS bSVar = new bS(this, r2, yVarC, str);
                this.f8925f.add(bSVar);
                yVarC.a(bSVar);
            } catch (V.a e2) {
                Logger.getLogger(bO.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            } catch (V.g e3) {
                Logger.getLogger(bO.class.getName()).log(Level.SEVERE, "Error creating 1D unshared Model", (Throwable) e3);
            }
        }
        String str3 = r2.c() + "." + str2 + str;
        if (!this.f8921b.containsKey(str3)) {
            try {
                bF.y yVarC2 = c(r2, str);
                bS bSVar2 = new bS(this, r2, yVarC2, str);
                this.f8925f.add(bSVar2);
                yVarC2.a(bSVar2);
                this.f8921b.put(str3, yVarC2);
            } catch (V.g e4) {
            } catch (Exception e5) {
                e5.printStackTrace();
            }
        }
        return (bF.y) this.f8921b.get(str3);
    }

    public void a(bH.aa aaVar) {
        this.f8926k = aaVar;
    }

    public void a(boolean z2) {
        this.f8928m = z2;
    }

    public void b(boolean z2) {
        this.f8929n = z2;
    }
}
