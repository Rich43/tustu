package aP;

import G.C0113cs;
import G.C0125h;
import G.C0126i;
import G.C0129l;
import G.C0135r;
import G.C0136s;
import W.C0172ab;
import W.C0196v;
import W.C0200z;
import bH.C1011s;
import com.efiAnalytics.apps.ts.dashboard.C1388aa;
import com.sun.org.apache.xml.internal.security.utils.Constants;
import java.awt.Container;
import java.awt.Window;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JDialog;
import r.C1798a;
import r.C1799b;
import r.C1807j;
import s.C1818g;
import v.C1883c;
import z.C1899c;

/* renamed from: aP.hx, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:aP/hx.class */
public class C0416hx implements com.efiAnalytics.ui.fR {

    /* renamed from: f, reason: collision with root package name */
    C0418hz f3627f;

    /* renamed from: g, reason: collision with root package name */
    com.efiAnalytics.ui.fK f3628g;

    /* renamed from: h, reason: collision with root package name */
    Window f3629h;

    /* renamed from: a, reason: collision with root package name */
    aE f3622a = new aE();

    /* renamed from: b, reason: collision with root package name */
    C0224at f3623b = new C0224at();

    /* renamed from: c, reason: collision with root package name */
    C1799b f3624c = new C1799b();

    /* renamed from: d, reason: collision with root package name */
    C0207ac f3625d = new C0207ac();

    /* renamed from: e, reason: collision with root package name */
    aF f3626e = null;

    /* renamed from: i, reason: collision with root package name */
    G.R f3630i = null;

    public C0416hx(Window window) {
        this.f3627f = null;
        this.f3628g = null;
        this.f3629h = null;
        this.f3629h = window;
        this.f3628g = new com.efiAnalytics.ui.fK("New " + C1798a.f13268b + " Project", new C0417hy(this));
        try {
            this.f3628g.e(this.f3623b);
            this.f3623b.a(false);
            this.f3627f = new C0418hz(this, this.f3622a);
            this.f3628g.e(this.f3627f);
            this.f3628g.e(this.f3625d);
            this.f3628g.e(this.f3624c);
            this.f3624c.a();
            this.f3628g.a(this);
        } catch (Exception e2) {
            com.efiAnalytics.ui.bV.d("Failed to build New Project Wizard, see log for details.", window);
            bH.C.a("Failed to build New Project Wizard, see log for details.");
            e2.printStackTrace();
        }
    }

    public void a() {
        JDialog jDialogA = this.f3628g.a(this.f3629h, C1818g.b("Create New Project"));
        if (G.T.a().c() != null) {
            G.T.a().c().C().c();
        }
        jDialogA.setVisible(true);
    }

    @Override // com.efiAnalytics.ui.fR
    public boolean b() throws NumberFormatException {
        aE.a aVar = new aE.a();
        aVar.h(this.f3623b.g().getAbsolutePath());
        aVar.i(this.f3623b.f());
        if (aE.a.A() != null) {
            C0338f.a().g();
            if (this.f3630i != null) {
                try {
                    C0125h.a().a(this.f3630i);
                    G.T.a().a(this.f3630i);
                    G.T.a().a(this.f3630i.c());
                } catch (V.a e2) {
                    Logger.getLogger(C0416hx.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
                }
            }
        }
        try {
            aVar.a();
            File fileH = this.f3623b.h();
            aVar.l(fileH.getName().toLowerCase().endsWith(".ecu") ? "mainController.ecu" : "mainController.ini");
            aVar.remove("firmwareDescription");
            C1011s.a(fileH, aVar.j());
            aVar.p(this.f3623b.c());
            aVar.a(this.f3622a.b());
            File fileB = this.f3624c.b();
            com.efiAnalytics.apps.ts.dashboard.Z zE = this.f3624c.e();
            C1011s.a(fileB, aVar.l());
            zE.b(C0200z.a(fileH));
            new C1883c(C1807j.G()).a(aVar.l().getAbsolutePath(), zE);
            aVar.o(aVar.n());
            try {
                if (this.f3626e != null) {
                    this.f3626e.c();
                }
            } catch (V.a e3) {
                bH.C.a("Error saving throttle calibration.", e3, this.f3626e);
            }
            try {
                String strF = this.f3625d.f() == null ? "" : this.f3625d.f();
                aVar.a(aVar.u(), this.f3625d.d());
                aVar.b(aVar.u(), strF);
                this.f3625d.a(aVar);
            } catch (Exception e4) {
            }
            this.f3625d.a(aVar);
            aVar.b();
            G.T.a().b();
            this.f3624c.d();
            c();
            C0338f.a().a((Window) cZ.a().c(), this.f3623b.g().getAbsolutePath());
            return true;
        } catch (V.a e5) {
            com.efiAnalytics.ui.bV.d("Error creating Project " + this.f3623b.f() + "\nProject Location:" + this.f3623b.g().getAbsolutePath() + "\nError Message:\n" + e5.getMessage() + "\nSee log for more detail.\nBy restarting " + C1798a.f13268b + " and opening this project,\n all will most likely be fine.", this.f3624c);
            e5.printStackTrace();
            return false;
        }
    }

    @Override // com.efiAnalytics.ui.fR
    public void c() {
        this.f3624c.d();
        G.R r2 = this.f3630i;
        if (r2 != null) {
            G.J jC = r2.C();
            if (jC != null) {
                jC.c();
            }
            G.T.a().b(r2.c());
        }
        if (G.T.a().c() != null) {
            try {
                G.T.a().c().C().d();
            } catch (C0129l e2) {
            }
        }
        C0126i.a();
    }

    @Override // com.efiAnalytics.ui.fR
    public boolean a(Container container) {
        if (!(container instanceof C0224at)) {
            if (container instanceof aE) {
                return ((aE) container).e();
            }
            if (container instanceof C1799b) {
                return ((C1799b) container).f();
            }
            if (!(container instanceof aF)) {
                return true;
            }
            C0113cs.a().a((aF) container);
            return true;
        }
        if (!((C0224at) container).e()) {
            return false;
        }
        try {
            this.f3622a.a(new W.I().a(C0196v.a().b(this.f3623b.h().getAbsolutePath()), this.f3623b.h().getAbsolutePath()));
            return true;
        } catch (V.a e2) {
            com.efiAnalytics.ui.bV.d(e2.getMessage(), this.f3629h);
            return true;
        }
    }

    private G.R d() {
        if (this.f3630i != null) {
            this.f3630i.q(this.f3623b.b());
            return this.f3630i;
        }
        if (this.f3623b.h() == null) {
            return null;
        }
        G.R r2 = new G.R();
        r2.a(this.f3623b.f());
        r2.q(this.f3623b.b());
        for (C0135r c0135r : this.f3622a.b()) {
            r2.a(c0135r);
        }
        C0172ab c0172ab = new C0172ab();
        try {
            if (r2.c("tsCanId") == null) {
                r2 = c0172ab.a(r2, C1807j.f13497G, false);
            }
            G.R rA = c0172ab.a(r2, this.f3623b.h().getCanonicalPath());
            rA.q(this.f3623b.g().getAbsolutePath());
            rA.c(C1899c.a().a(rA, C1798a.f13371ba, aV.w.c(), null, null));
            G.T.a().a(rA);
            if (C1798a.f13268b.equals(C1798a.f13337as)) {
                rA.O().a(new J.a());
                rA.O().a(new J.b());
                rA.O().d(true);
            }
            this.f3630i = rA;
            return this.f3630i;
        } catch (Exception e2) {
            e2.printStackTrace();
            com.efiAnalytics.ui.bV.d("Error loading ECU configuration.\nMessage:\n" + e2.getMessage(), this.f3625d);
            return null;
        }
    }

    @Override // com.efiAnalytics.ui.fR
    public boolean b(Container container) throws IllegalArgumentException {
        if (container.equals(this.f3627f)) {
            C0136s[] c0136sArrC = this.f3622a.c();
            if (c0136sArrC != null && c0136sArrC.length != 0) {
                return true;
            }
            this.f3628g.a();
            return true;
        }
        if (!(container instanceof C0207ac)) {
            if (!(container instanceof C1799b)) {
                if (!(container instanceof aF)) {
                    return true;
                }
                ((aF) container).a(d());
                return true;
            }
            G.R rD = d();
            try {
                this.f3624c.a(this.f3623b.f(), (rD.i().startsWith("MS3 Format") && (rD.i().endsWith(Constants._TAG_P) || rD.i().endsWith("E") || rD.i().endsWith("U"))) ? new C1883c(C1807j.G()).a(new File(C1807j.h(), "AMPMS3_Pro_Default.dash").getAbsolutePath()) : (rD.i().startsWith("MS2Extra") && rD.i().endsWith(Constants._TAG_P)) ? new C1883c(C1807j.G()).a(new File(C1807j.h(), "AMP_MS2_PNP.dash").getAbsolutePath()) : new C1388aa().a(rD, "FrontPage", 2, 4));
            } catch (Exception e2) {
                com.efiAnalytics.ui.bV.d("Error in Front Page definition.", container);
            }
            this.f3624c.a(new String[]{rD.i()}, r.p.a().b());
            return true;
        }
        C0207ac c0207ac = (C0207ac) container;
        G.R rD2 = d();
        try {
            String strI = this.f3623b.i();
            List listJ = this.f3623b.j();
            G.bS bSVarK = this.f3623b.k();
            c0207ac.k();
            c0207ac.a(rD2);
            if (listJ == null || strI == null) {
                ArrayList arrayList = new ArrayList();
                arrayList.add(new A.c("Baud Rate", aD.a.f2297c));
                c0207ac.a("DetectedDevice", aD.a.f2298d, arrayList, new G.bS());
            } else {
                c0207ac.a("DetectedDevice", strI, listJ, bSVarK);
                c0207ac.c();
            }
            return true;
        } catch (V.a e3) {
            bH.C.a("Error setting configuration to comm settings.", e3, c0207ac);
            return true;
        }
    }
}
