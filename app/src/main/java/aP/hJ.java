package aP;

import G.C0113cs;
import G.C0126i;
import L.C0151h;
import L.C0156m;
import L.C0157n;
import W.C0172ab;
import W.C0196v;
import ac.C0491c;
import ai.C0511a;
import ar.C0839f;
import ay.C0924a;
import ay.C0935l;
import bH.C1018z;
import bd.C1067a;
import bh.C1152l;
import bl.C1190l;
import bm.C1196a;
import bm.C1197b;
import bm.C1198c;
import bm.C1199d;
import bn.C1201b;
import bn.C1202c;
import bn.C1203d;
import br.C1232J;
import bt.C1283K;
import bt.C1337bs;
import com.efiAnalytics.ui.BinTableView;
import com.efiAnalytics.ui.C1606cq;
import com.efiAnalytics.ui.C1677fh;
import h.C1737b;
import java.awt.Frame;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import l.C1759a;
import r.C1798a;
import r.C1806i;
import r.C1807j;
import s.C1818g;
import v.C1883c;
import y.C1895c;
import z.C1899c;
import z.C1900d;

/* loaded from: TunerStudioMS.jar:aP/hJ.class */
public class hJ extends Thread {

    /* renamed from: a, reason: collision with root package name */
    Frame f3532a;

    /* renamed from: b, reason: collision with root package name */
    int f3533b;

    public hJ(Frame frame) {
        super("PreLoader");
        this.f3532a = null;
        this.f3533b = 2000;
        setDaemon(true);
        this.f3532a = frame;
        a();
    }

    public void a() {
        bH.C.a(new hK(this));
        n.j.a().a(C0404hl.a());
        g();
        C0404hl.a().a("Initializing File Dialogs.");
        bH.C.a(new hP(this));
        C0196v.a(new aZ.n());
        com.efiAnalytics.ui.bV.a(new hY());
        C1798a.f13271e = System.currentTimeMillis();
        C0126i.f1247b = C1798a.f13271e;
        hQ hQVar = new hQ(this);
        com.efiAnalytics.ui.bV.a(hQVar);
        bt.bO.a().a(hQVar);
        bH.ab.a().a(hQVar);
        X.c.a().b(new File(C1807j.A(), "cache"));
        C0113cs.a().a(I.f.f1358a, K.c.a());
        try {
            com.efiAnalytics.ui.bV.g();
        } catch (Exception e2) {
            bH.C.b("Error inilizing FileDialog");
            e2.printStackTrace();
        }
        jv jvVarA = jv.a();
        G.T.a().a(jvVarA);
        if (C1798a.f13268b.equals(C1798a.f13337as)) {
            jvVarA.a(new aT.a());
            G.T.a().a(new hA());
        }
        C1807j.u();
    }

    public void b() {
        a(C1798a.f13268b, C1798a.f13269c);
        h();
        C0338f.a().a(bt.bO.a());
        C0338f.a().a(new hR(this));
        C0338f.a().a(C1232J.a());
        d.g.a().a(new C0311e());
        C0287db c0287db = new C0287db();
        if (C1806i.a().a("09jtrkgds;okfds")) {
            d();
        }
        C0404hl.a().a("Initializing Help.");
        c0287db.a();
        C0404hl.a().a("Initializing Edition Features.");
        j();
        C0404hl.a().a("Initializing App Events.");
        i();
        G.T.a().a(new aS.g());
        G.T.a().a(new aS.a());
        G.T.a().a(S.b.a());
        G.aB.a().a(bS.e());
        C0491c.a().a(new aS.f());
        bH.C.c("App Name:" + C1798a.f13268b + ", appEdition:" + C1798a.f13269c);
        C0338f.a().a(new hS(this));
        if (C1806i.a().a("oifgytrewalkfgyuewq87/ ")) {
            E.c.a();
        }
        if (C1806i.a().a("432;'g[pf-025;h;'")) {
            ax.Q.a(C0157n.a());
            C0151h.a(true);
            C0126i.f1248c = true;
            new I.q().a();
        } else {
            ax.Q.a(new C0156m());
            C0151h.a(false);
        }
        L.ab.a().a(C1806i.a().a("98ua7h9uh432987 432"));
        ax.Q.a(L.ab.a());
        L.ab.a().a(new C1759a());
        I.h.a();
        if (C1806i.a().a(" OKFDS09IFDSOK")) {
            C0404hl.a().a("Initializing Log Viewer Components.");
            h.i.f12255b = "LogViewer";
            C0839f.a().a(C1152l.b());
            if (C1798a.f13268b.equals(C1806i.f13458s) || C1798a.f13268b.equals(C1806i.f13459t)) {
                C1737b.a().a(C1737b.f12230j, C1737b.f12226f);
            } else {
                C1737b.a().a(C1737b.f12230j, C1737b.f12227g);
            }
            Z.f.a().a(new Z.a());
            ak.an.a(new hX(this));
        }
        C1677fh.a(C1798a.a().a(C1798a.f13414bR, C1798a.f13415bS));
        if (C1806i.a().a("sa0-0o0os-0o-0DS")) {
            S.b.a().a(new hT(this));
        }
        G.T.a().a(new hU(this));
        I.p.a().a(C1806i.a().a("lkjdsa0iu0-,jew"));
        C1883c.a(false);
        aE.a.c(C1807j.q());
        G.aR.a().a(new bP.a());
        k();
    }

    public void c() {
        C0511a.a().a("helpManuals", new C1067a());
        C1337bs.a(C1798a.a().a(C1798a.f13327ai, 100.0d));
        C0172ab.f2049c = C1807j.H();
        C0172ab.f2048b = C1807j.I();
        new bH.O(C0172ab.f2048b, bH.O.f7013b).a();
        try {
            new bH.O(C1807j.c(), bH.O.f7012a).a();
        } catch (V.a e2) {
            bH.C.a(e2.getLocalizedMessage());
        }
        ac.n.f4232a = C1798a.a().b() + " version " + C1798a.f13267a;
        C1018z.i().a("TunerStudio", "MS", "https://www.efianalytics.com/TunerStudio/download/");
        C1018z.i().a("TunerStudio", "MS Ultra", "http://www.efianalytics.com/TunerStudio/download/");
        C1018z.i().a("TunerStudio", "MS Dev", "http://www.efianalytics.com/TunerStudio/download/");
        C1018z.i().a("MegaLogViewer", "MS", "https://www.efianalytics.com/MegaLogViewer/download/");
        C1018z.i().a("MegaLogViewer", "HD", "https://www.efianalytics.com/MegaLogViewerHD/download/");
        C1018z.i().a("MegaLogViewer", "BigStuff3", "http://www.bigcommpro.com/downloads");
        C1018z.i().a("Shadow Dash MS", "", "http://www.tunerstudio.com/index.php/downloads");
        C1018z.i().a("Big Dash", "", "http://www.bigcommpro.com/software/bigdash");
        C1018z.i().a("Big Replay Upload", "", "http://www.bigcommpro.com/software/bigreplay");
        C1018z.i().a("BigComm", "Pro", "http://bigcommpro.com/software/bigcomm-pro");
        C1018z.i().a("BigComm", "Pro Single", "http://bigcommpro.com/software/bigcomm-pro");
        C1018z.i().a("TS Dash", "Pro", "https://www.efianalytics.com/TunerStudio/download/");
        C1018z.i().a("TS Dash", "Pro Ultra", "https://www.efianalytics.com/TunerStudio/download/");
        n.h.a().a(new hV(this));
        if (C1806i.a().a(" 09s98r32-po3q9264")) {
            ac.u.a().a(new n.m());
        }
        BinTableView.i(C1798a.a().a(C1798a.cg, C1798a.ch));
        C0404hl.a().a("Loading Font list.");
        e();
        C0404hl.a().a("");
        new R.b().start();
        f();
        C0404hl.a().a("Initializing UI Components.");
        q.f.a();
        if (C1806i.a().a("poij  fdsz poi9ure895 ms7(")) {
            p.z.a().b();
            d.g.a().a(p.z.a());
        }
        if (C1806i.a().a("f(*&rew0987LKJ098342")) {
            p.x.a().b();
            S.b.a().a(p.x.a());
            G.T.a().a(T.a.a());
        }
        if (C1806i.a().a("bd098fsdpokfdslk") && aV.x.a().c()) {
            C0404hl.a().a("Initializing GPS.");
            try {
                aV.x.a().d();
            } catch (Exception e3) {
                com.efiAnalytics.ui.bV.d("Failed to initalize GPS:\n" + e3.getLocalizedMessage(), cZ.a().c());
                Logger.getLogger(hJ.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e3);
            }
        }
        C0338f.a().a(W.D.a());
        if (C1806i.a().a("GD;';LFDS-0DSL;")) {
            aW.r.a().a("Not Listed Bluetooth", new aX.b());
            if (0 != 0) {
                new L().a(10000);
            }
        }
        bN.k.a(C1798a.a().j());
        try {
            C0935l.a().a(C1807j.c());
            n.k.a();
        } catch (V.a e4) {
            com.efiAnalytics.ui.bV.d(e4.getLocalizedMessage(), this.f3532a);
        }
        C1202c.a().a(C1202c.f8272d, new C1201b());
        C1202c.a().a(C1202c.f8271c, new C1203d());
        C1895c.b();
        if (!C0924a.c().e()) {
            C0924a.c().g();
        }
        C0404hl.a().a("Ready");
        if (C1806i.a().a("_(*UR98ewf098u 98EE 2  *(W")) {
            com.efiAnalytics.ui.dI.a().a(new hW(this));
        }
    }

    private void f() {
        if (C1798a.a().a(C1798a.dj, false) && !C1798a.a().a(C1798a.dk, false)) {
            C0338f.a().B();
            C1798a.a().b(C1798a.dk, "true");
        }
    }

    public void d() {
        C0404hl.a().a("Initializing Plugin Services.");
        try {
            C1190l.b();
        } catch (V.a e2) {
            com.efiAnalytics.ui.bV.d("Failed to initialize Plugin Server, plugin support is disabled.\nreported error:" + e2.getMessage() + "\nCheck log for more information.", this.f3532a);
        } catch (NoClassDefFoundError e3) {
            com.efiAnalytics.ui.bV.d("Failed to initialize Plugin Server, plugin support is disabled.\nreported error: TunerStudioPlugin.jar not found\nCheck log for more information.", this.f3532a);
            return;
        }
        C0404hl.a().a("Loading Plugins.");
        try {
            C1190l.a().d();
        } catch (V.a e4) {
            com.efiAnalytics.ui.bV.d("Failed to initialize Plugins.\nreported error:" + e4.getMessage() + "\nCheck log for more information.", this.f3532a);
        }
    }

    private void g() {
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        try {
            Thread.currentThread();
            Thread.sleep(this.f3533b);
            c();
        } catch (Exception e2) {
            bH.C.c("Preload Thread Died");
            e2.printStackTrace();
        }
    }

    public static String[] e() {
        C1606cq.a().a(C1807j.F());
        return C1606cq.a().c();
    }

    private void a(String str, String str2) {
        if (str2.equals(C1806i.f13444f) || str2.equals(C1806i.f13450l) || str2.equals(C1806i.f13445g) || str2.equals(C1806i.f13451m) || str.equals(C1806i.f13464y)) {
            G.X.a().a(new O.e());
            G.X.a().a(new O.g());
            G.X.a().a(new O.i());
            G.X.a().a(new O.k());
            G.X.a().a(new O.d());
            G.X.a().a(new O.h());
            if (C1806i.a().a("kjlkgoi098")) {
                B.g.a().a(21848);
                B.g.a().d();
            }
            if (str2.equals(C1806i.f13445g) || str2.equals(C1806i.f13451m)) {
                G.X.a().a(new O.a());
                return;
            }
            return;
        }
        if (str2.equals(C1806i.f13446h) || str2.equals(C1806i.f13447i)) {
            G.X.a().a(new O.e());
            G.X.a().a(new O.g());
            G.X.a().a(new O.i());
            G.X.a().a(new O.k());
            G.X.a().a(new O.d());
            G.X.a().a(new O.h());
            G.X.a().a(new O.a());
            G.X.a().a(new O.f());
            if (C1806i.a().a("kjlkgoi098")) {
                B.g.a().a(21848);
                B.g.a().d();
                aA.h.a(new hL(this));
                aA.h.a().a(true);
                return;
            }
            return;
        }
        if (str.equals(C1798a.f13340av) || str.equals(C1798a.f13341aw)) {
            G.X.a().a(new O.j());
            return;
        }
        if (str.equals(C1798a.f13337as)) {
            G.X.a().a(new O.c());
            if (C1806i.a().a("H;';'0FD;RE")) {
                G.X.a().a(new O.b());
                aA.h.a(new hM(this));
                B.g.a().a(21846);
                B.g.a().a(21847);
                B.g.a().d();
            }
            C0491c.a().a(new ac.e());
            G.T.a().a(new hN(this));
            return;
        }
        if (!str.equals(C1798a.f13338at)) {
            G.X.a().a(new O.h());
            G.X.a().a(new O.d());
            return;
        }
        G.X.a().a(new O.b());
        C0491c.a().a(new ac.e());
        aA.h.a(new hO(this));
        B.g.a().a(21846);
        B.g.a().a(21847);
        B.g.a().d();
    }

    private void h() {
        String strA = C1798a.a().a(C1798a.f13349aE, "MegaSquirt_001");
        if (strA.equals("MegaSquirt_001")) {
            try {
                G.cN.a().a(new J.d());
            } catch (V.g e2) {
                bH.C.b("Failed to create ProtocolInitializer: " + strA);
                Logger.getLogger(hJ.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            }
        } else if (strA.equals("disabled") || strA.equals("")) {
            bH.C.d("ProtocolInitializer disabled");
        } else {
            bH.C.d("Unknown ProtocolInitializer: '" + strA + "', disabled");
        }
        if (!C1806i.a().a(";lgd;lgd09h;l ")) {
            try {
                C1900d c1900d = new C1900d();
                c1900d.a(C1899c.f14084e);
                c1900d.b(C1818g.b("Standard Protocols Driver"));
                C1899c.a().a(c1900d);
            } catch (Exception e3) {
                bH.C.a("Failed to load Common MegaSquirt Driver, it will be unavailable.");
            }
        }
        if (C1806i.a().a("HF-0[PEPHF0H;LJGPO0")) {
            try {
                C1900d c1900d2 = new C1900d();
                c1900d2.a(C1899c.f14085f);
                c1900d2.b(C1818g.b("Dash Echo Client"));
                C1899c.a().a(c1900d2);
            } catch (Exception e4) {
                bH.C.a("Failed to load Remote Connection Driver, it will be unavailable.");
            }
        }
        if (C1806i.a().a("HF-05[P54;'FD")) {
            try {
                C1900d c1900d3 = new C1900d();
                c1900d3.a(C1899c.f14085f);
                c1900d3.b(C1818g.b("BigStuff Gen4 Driver"));
                C1899c.a().a(c1900d3);
            } catch (Exception e5) {
                bH.C.a("Failed to load Common MegaSquirt Driver, it will be unavailable.");
            }
        }
        A.j.a().a(Q.a());
        A.j.a().a(C1798a.f13272f);
    }

    private void i() {
        C0113cs.a().d("controllerSettingsLoaded");
    }

    private void j() {
        if (C1806i.a().a("joijt;i609tr0932")) {
            BinTableView.f(true);
        }
    }

    private void k() {
        C1283K.a().a(new C1198c());
        C1283K.a().a(new C1197b());
        C1283K.a().a(new C1196a());
        C1283K.a().a(new C1199d());
    }
}
