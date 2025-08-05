package aP;

import G.C0050aj;
import G.C0072be;
import G.C0076bi;
import G.C0079bl;
import G.C0088bu;
import G.C0102ch;
import G.C0113cs;
import G.C0116cv;
import G.C0126i;
import G.C0129l;
import G.C0130m;
import G.C0135r;
import G.C0136s;
import G.InterfaceC0109co;
import L.C0155l;
import W.C0171aa;
import W.C0188n;
import W.C0189o;
import W.C0196v;
import W.C0200z;
import aV.C0474a;
import aY.C0475a;
import aa.C0478a;
import aa.C0482e;
import ac.C0488D;
import ac.C0490b;
import ac.C0491c;
import ac.InterfaceC0489a;
import ai.C0512b;
import ai.C0514d;
import ai.C0516f;
import ao.C0804hg;
import bH.C1005m;
import bH.C1011s;
import bH.InterfaceC0994b;
import ba.C1023a;
import bd.C1067a;
import be.C1070C;
import be.C1085a;
import bg.C1121a;
import bl.C1186h;
import bl.C1190l;
import bn.C1202c;
import bn.C1204e;
import bw.C1370a;
import c.C1382a;
import com.efiAnalytics.apps.ts.dashboard.AbstractC1420s;
import com.efiAnalytics.apps.ts.dashboard.C1388aa;
import com.efiAnalytics.apps.ts.dashboard.C1389ab;
import com.efiAnalytics.apps.ts.dashboard.C1425x;
import com.efiAnalytics.plugin.ApplicationPlugin;
import com.efiAnalytics.tunerStudio.panels.C1458g;
import com.efiAnalytics.tuningwidgets.panels.C1486ac;
import com.efiAnalytics.tuningwidgets.panels.C1509g;
import com.efiAnalytics.ui.AbstractC1600ck;
import com.efiAnalytics.ui.C1606cq;
import com.efiAnalytics.ui.InterfaceC1565bc;
import com.sun.corba.se.impl.orbutil.ORBConstants;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.HeadlessException;
import java.awt.Insets;
import java.awt.KeyboardFocusManager;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.Window;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JRootPane;
import javax.swing.SwingUtilities;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import jdk.internal.dynalink.CallSiteDescriptor;
import org.icepdf.core.util.PdfOps;
import p.C1775a;
import r.C1798a;
import r.C1806i;
import r.C1807j;
import r.C1811n;
import s.C1818g;
import v.C1883c;
import z.C1901e;

/* renamed from: aP.f, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:aP/f.class */
public class C0338f {

    /* renamed from: c, reason: collision with root package name */
    private static C0338f f3334c = null;

    /* renamed from: d, reason: collision with root package name */
    private List f3335d = Collections.synchronizedList(new ArrayList());

    /* renamed from: e, reason: collision with root package name */
    private List f3336e = Collections.synchronizedList(new ArrayList());

    /* renamed from: f, reason: collision with root package name */
    private HashMap f3337f = new HashMap();

    /* renamed from: g, reason: collision with root package name */
    private HashMap f3338g = new HashMap();

    /* renamed from: h, reason: collision with root package name */
    private int f3339h = 0;

    /* renamed from: a, reason: collision with root package name */
    List f3340a = new ArrayList();

    /* renamed from: i, reason: collision with root package name */
    private boolean f3341i = false;

    /* renamed from: j, reason: collision with root package name */
    private Runnable f3342j = new RunnableC0365g(this);

    /* renamed from: b, reason: collision with root package name */
    boolean f3343b = false;

    /* renamed from: k, reason: collision with root package name */
    private HashMap f3344k = new HashMap();

    private C0338f() {
    }

    public static C0338f a() {
        if (f3334c == null) {
            f3334c = new C0338f();
        }
        return f3334c;
    }

    private void f(G.R r2) {
        Iterator it = this.f3340a.iterator();
        while (it.hasNext()) {
            ((jF) it.next()).a(r2);
        }
    }

    private void g(G.R r2) {
        Iterator it = this.f3340a.iterator();
        while (it.hasNext()) {
            ((jF) it.next()).b(r2);
        }
    }

    private void h(G.R r2) {
        Iterator it = this.f3340a.iterator();
        while (it.hasNext()) {
            ((jF) it.next()).c(r2);
        }
    }

    private void i(G.R r2) {
        Iterator it = this.f3340a.iterator();
        while (it.hasNext()) {
            ((jF) it.next()).d(r2);
        }
    }

    public void a(aE.e eVar) {
        this.f3336e.add(eVar);
    }

    public void b(aE.e eVar) {
        this.f3336e.remove(eVar);
    }

    private void U() {
        G.R rC = G.T.a().c();
        aE.a aVarA = aE.a.A();
        Iterator it = this.f3336e.iterator();
        while (it.hasNext()) {
            ((aE.e) it.next()).a(aVarA, rC);
        }
    }

    private void V() {
        Iterator it = this.f3336e.iterator();
        while (it.hasNext()) {
            ((aE.e) it.next()).e_();
        }
    }

    private void a(aE.a aVar) {
        Iterator it = this.f3336e.iterator();
        while (it.hasNext()) {
            ((aE.e) it.next()).a(aVar);
        }
    }

    public void a(Window window) {
        if (G.T.a().c() == null) {
            bH.C.c("No Configurations loaded to show DAQ Report on.");
        } else if (G.T.a().c().O().al().equals("XCP")) {
            new aS(window);
        } else {
            bH.C.c("Loaded Configuration loaded not using XCP.");
        }
    }

    public void b(Window window) {
        n.n nVarM = cZ.a().m();
        if (nVarM == null || !nVarM.getTitleAt(nVarM.getSelectedIndex()).equals(gW.f3467r)) {
            new C0416hx(window).a();
        }
    }

    public com.efiAnalytics.tunerStudio.search.f a(com.efiAnalytics.tunerStudio.search.f fVar) {
        boolean zEquals = fVar.b().equals(bT.o.f7604a);
        aA.a aVar = new aA.a(fVar.b(), fVar.a(), C1818g.d());
        if (!zEquals) {
            if (fVar.g() != null && fVar.g().c().equals("A")) {
                aVar.a(fVar.g());
            }
            aVar.a(cZ.a().c()).setVisible(true);
            fVar.a(aVar.a());
        } else {
            if (fVar.g() != null && fVar.g().c().equals("A")) {
                return fVar;
            }
            if (fVar.g() != null) {
                fVar.a(new D.a());
            }
            fVar.g().d(C1798a.a().c(C1798a.cE, ""));
            fVar.g().e(C1798a.a().c(C1798a.cC, ""));
            fVar.g().f(C1798a.a().c(C1798a.cD, ""));
            fVar.g().b(fVar.a());
            fVar.g().c("A");
        }
        return fVar;
    }

    public void a(B.i iVar, boolean z2) {
        com.efiAnalytics.tunerStudio.search.f fVar = new com.efiAnalytics.tunerStudio.search.f();
        fVar.a(iVar);
        D.c cVarC = aA.h.a().c(fVar.b(), fVar.a());
        if (cVarC.a() == 8) {
            com.efiAnalytics.ui.bV.d("This Device has been disabled, please Contact support for more information.", cZ.a().c());
            return;
        }
        fVar.a(cVarC.b());
        while (!fVar.g().c().equals("A")) {
            fVar = a(fVar);
            if (0 > 3) {
                return;
            }
        }
        a(fVar, z2);
    }

    public void b(com.efiAnalytics.tunerStudio.search.f fVar) {
    }

    public void a(com.efiAnalytics.tunerStudio.search.f fVar, boolean z2) {
        if (fVar.b().equals(bT.o.f7604a)) {
            b(fVar);
            return;
        }
        while (!fVar.g().c().equals("A")) {
            fVar = a(fVar);
            if (0 > 3) {
                return;
            }
        }
        File file = null;
        String strC = C1798a.a().c(C1798a.f13313U + fVar.a(), "");
        if (fVar.h() != null && fVar.h().b() != null && fVar.h().b().exists()) {
            file = fVar.h().b();
        } else if (strC != null && !strC.isEmpty()) {
            file = new File(strC);
        }
        JFrame jFrameC = cZ.a().c();
        B.i iVarD = C1807j.d(file);
        if (iVarD != null && iVarD.e() != null && !iVarD.e().isEmpty() && !iVarD.e().equals(fVar.a())) {
            bH.C.d("Wrong Project for serial #" + fVar.a());
            file = null;
        }
        if (file == null || !file.exists()) {
            List listB = C1807j.b(fVar.a());
            if (listB.size() == 1) {
                file = (File) listB.get(0);
            } else if (listB.size() > 1) {
                com.efiAnalytics.ui.bV.d("Multiple projects for this serial number found, using: " + ((File) listB.get(0)).getName() + "\nIf you wish to use a different project, select:\nFile --> Vehicle Projects --> Open Project", null);
                file = (File) listB.get(0);
            } else {
                file = a(fVar, jFrameC);
                if (file == null) {
                    return;
                }
            }
        }
        C1798a.a().b(C1798a.f13313U + fVar.a(), file.getAbsolutePath());
        G g2 = new G(this, jFrameC, file.getAbsolutePath());
        g2.a(z2);
        g2.start();
    }

    public File a(com.efiAnalytics.tunerStudio.search.f fVar, Window window) {
        B.b.c().a(fVar.f());
        String strD = fVar.d();
        try {
            File fileA = C1807j.a(window, strD);
            if (fileA == null || !fileA.exists()) {
                return null;
            }
            String strA = C0200z.a(fileA);
            if (strA != null && !strA.isEmpty()) {
                strD = strA;
            }
            hZ hZVar = new hZ();
            String strA2 = "";
            if (fVar.g() != null && fVar.g().e() != null && !fVar.g().e().isEmpty() && fVar.g().f() != null && !fVar.g().f().isEmpty()) {
                String str = fVar.g().e() + " " + fVar.g().f();
                strA2 = (fVar.f().e() == null || fVar.f().e().isEmpty()) ? str + " Car" : str + " Serial " + fVar.f().e();
            }
            if (new File(C1807j.u(), strA2).exists() || !C1011s.a(strD)) {
                boolean z2 = false;
                while (true) {
                    strA2 = com.efiAnalytics.ui.bV.a((Component) window, false, C1818g.b("A new project will be created for this " + C1382a.a(strD, C1798a.f13272f)) + "\n" + C1818g.b("This will help keep logs and calibration files related to this device organized.") + ".\n\n" + C1818g.b("What would you like to name this project?"), strA2);
                    if (strA2 == null || strA2.equals("")) {
                        z2 = false;
                    } else {
                        File file = new File(C1807j.u(), strA2);
                        if (file.exists()) {
                            if (com.efiAnalytics.ui.bV.a(C1818g.b("There is already a project by that name.") + "\n" + C1818g.b("Overwrite?"), (Component) window, true)) {
                                C1011s.b(file);
                                z2 = false;
                            } else {
                                strA2 = null;
                                z2 = true;
                            }
                        }
                    }
                    if (!z2 && (strA2 == null || strA2.equals("") || C1011s.a(strA2))) {
                        break;
                    }
                }
            }
            if (strA2 == null || strA2.equals("")) {
                return null;
            }
            aE.a aVarA = hZVar.a(strA2, strD, fileA, null);
            File file2 = new File(aVarA.t());
            if (fVar.f() != null && fVar.f().e() != null && !fVar.f().e().isEmpty()) {
                C1807j.a(file2, fVar.f());
            }
            if (!file2.exists()) {
                com.efiAnalytics.ui.bV.d("Project Directory not found?", window);
                return null;
            }
            aE.a.a(aVarA);
            g();
            if (C1806i.a().a("645fds64[=43098=32fsa  ")) {
                String strU = C1807j.u();
                for (String str2 : C1807j.v()) {
                    File file3 = new File(strU, str2);
                    if (file3.exists()) {
                        C1011s.b(file3);
                    }
                }
            }
            return file2;
        } catch (V.a e2) {
            com.efiAnalytics.ui.bV.d(e2.getMessage(), cZ.a().c());
            return null;
        }
    }

    public void c(Window window) {
        String strB = com.efiAnalytics.ui.bV.b(C1807j.u(), window);
        if (strB == null || strB.equals("")) {
            return;
        }
        a(window, strB);
    }

    public void a(Window window, String str) {
        if (new File(str).exists()) {
            new G(this, window, str).start();
        } else {
            com.efiAnalytics.ui.bV.d("A valid Project was not found at:\n" + str, window);
        }
    }

    public void b() {
        a((bH.L) null);
    }

    public String a(bH.L l2) {
        aE.a aVarA = aE.a.A();
        if (aVarA == null) {
            com.efiAnalytics.ui.bV.d("There is no project open to archive", cZ.a().c());
            return null;
        }
        File file = new File(aVarA.t());
        String[] strArr = {C1798a.f13286t};
        String strA = C1798a.a().a(C1798a.f13287u, C1807j.u());
        String str = aVarA.u() + "_" + bH.W.a() + "." + C1798a.f13286t;
        com.efiAnalytics.tuningwidgets.panels.X x2 = new com.efiAnalytics.tuningwidgets.panels.X();
        String strA2 = com.efiAnalytics.ui.bV.a((Component) cZ.a().c(), C1818g.b("Archive Project"), strArr, str, strA, false, (AbstractC1600ck) x2);
        if (strA2 != null && !strA2.equals("")) {
            if (!strA2.endsWith(strArr[0])) {
                strA2 = strA2 + "." + strArr[0];
            }
            FileFilter fileFilterC = x2.c();
            File file2 = new File(strA2);
            if (l2 == null) {
                l2 = new C0464t(this);
            }
            new C(this, file, file2, fileFilterC, l2).start();
        }
        return strA2;
    }

    public void c() {
        a(com.efiAnalytics.ui.bV.a((Component) cZ.a().c(), C1818g.b("Import Archive Project"), new String[]{C1798a.f13286t}, "*." + C1798a.f13286t, C1798a.a().a(C1798a.f13287u, C1807j.u()), false, (AbstractC1600ck) null));
    }

    public void a(String str) {
        String strA;
        if (!C1806i.a().a("09RGDKDG;LKIGD") || str == null || str.equals("")) {
            return;
        }
        File file = new File(str);
        boolean z2 = false;
        while (true) {
            strA = com.efiAnalytics.ui.bV.a((Component) cZ.a().c(), false, C1818g.b("Import Project as (Project Name):"), bH.W.b(file.getName(), "." + C1798a.f13286t, ""));
            if (strA == null || strA.equals("")) {
                z2 = false;
            } else if (new File(C1807j.u(), strA).exists()) {
                if (com.efiAnalytics.ui.bV.a(C1818g.b("There is already a project by that name.") + "\n" + C1818g.b("Overwrite?"), (Component) cZ.a().c(), true)) {
                    z2 = false;
                } else {
                    strA = null;
                    z2 = true;
                }
            }
            if (!z2 && (strA == null || strA.equals("") || C1011s.a(strA))) {
                break;
            }
        }
        if (strA == null || strA.equals("")) {
            return;
        }
        new I(this, file, strA, new C0466v(this)).start();
    }

    public void d() {
        JFrame jFrameC = cZ.a().c();
        G.R rC = G.T.a().c();
        if (rC == null) {
            com.efiAnalytics.ui.bV.d("There must be a Controller Configuration loaded to use the Expression Evaluator.", jFrameC);
            return;
        }
        aY.l lVar = new aY.l(jFrameC, rC);
        com.efiAnalytics.ui.bV.a((Window) jFrameC, (Component) lVar);
        lVar.setVisible(true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public synchronized void a(Window window, String str, boolean z2) {
        boolean z3;
        iC iCVar;
        File fileA;
        if (C1806i.a().a("09fewlkm309glkfds09")) {
            com.efiAnalytics.ui.dM dMVarD = cZ.a().d();
            if (cZ.a().m().getSelectedIndex() == 0) {
                z3 = false;
            } else {
                e(C1818g.b("Loading Project"));
                z3 = true;
            }
            C1425x c1425xB = cZ.a().b();
            C0404hl.a().a(C1818g.b("Opening Project") + " ...");
            c1425xB.k(C1818g.b("Loading Project"));
            c1425xB.repaint();
            Thread.yield();
            C1606cq.a().d();
            if (z3) {
                a(0.1d);
            }
            dMVarD.b(0.1d);
            C1798a c1798aA = C1798a.a();
            if (aE.a.A() != null) {
                g();
            }
            bH.C.c("Opening project: " + str);
            long jCurrentTimeMillis = System.currentTimeMillis();
            if (str == null || str.equals("")) {
                return;
            }
            c1798aA.b(C1798a.f13366aV, new File(str).getParent());
            aE.a.c(C1807j.q());
            bH.C.c();
            if (z3) {
                a(0.15d);
            }
            dMVarD.b(0.15d);
            aE.a aVar = new aE.a();
            aVar.h(str);
            File file = new File(str);
            aVar.i(file.getName());
            try {
                aVar.a(str);
                if (C1806i.a().a("FDSDSA-0;L;l0")) {
                    S.b.a().a(new W.ar(aVar, "Triggers."));
                    C0256by.a().b();
                }
                aVar = new hZ().a(str);
                if (C1806i.a().a("FDSDSA-0;L;l0")) {
                    S.b.a().a(new W.ar(aVar, "Triggers."));
                }
                if (z3) {
                    a(0.4d);
                }
                dMVarD.b(0.4d);
                aE.a.a(aVar);
                G.T tA = G.T.a();
                if (z3) {
                    try {
                        a(0.5d);
                    } finally {
                        l();
                        cZ.a().b().repaint();
                    }
                }
                dMVarD.b(0.5d);
                boolean zC = C1798a.a().c(C1798a.f13373bc, C1798a.f13374bd);
                if ((!C1798a.f13375be || bH.C.d() <= 0) && (bH.C.e() <= 0 || !zC)) {
                    bH.C.f();
                    bH.C.c();
                } else if (com.efiAnalytics.ui.bV.a("There were " + bH.C.d() + " errors and " + bH.C.e() + " warnings during Project  load.\nWould you like to review them now?", (Component) window, true)) {
                    new J(bH.C.f()).a(window);
                } else {
                    bH.C.f();
                    bH.C.c();
                }
                for (int i2 = 0; i2 < aVar.D().size(); i2++) {
                    G.R r2 = (G.R) aVar.D().get(i2);
                    if (tA.c(r2.c()) == null) {
                        tA.a(r2);
                        bH.C.b("Config not in Config Manager, adding it: " + r2.c());
                    }
                    if (aE.a.u(str) || C1798a.a().c(C1798a.f13393bw, true)) {
                        a(aVar, r2);
                        try {
                            File fileC = aVar.c(r2.c());
                            if (C1798a.a().c(C1798a.f13380bj, C1798a.f13381bk) && C1806i.a().a(",.fesokdsoi4309") && !aE.a.u(str)) {
                                W.C.a().a(fileC, new C1023a(r2));
                            }
                            if (z3) {
                                a(0.6d);
                            }
                            dMVarD.b(0.6d);
                        } catch (Exception e2) {
                            bH.C.c("Failed to get offline Tune File for " + r2.c());
                            e2.printStackTrace();
                        }
                    }
                    r2.h().a(new C0467w(this));
                }
                bH.C.d("!!! Loaded config in " + (System.currentTimeMillis() - jCurrentTimeMillis));
                b(window, aVar.u());
                bH.C.d("!!! Activated Project " + (System.currentTimeMillis() - jCurrentTimeMillis));
                boolean zQ = aVar.Q();
                if (C1806i.a().a("98fg54lklk") && zQ) {
                    a().J();
                }
                a(tA.c(), aVar.o().getAbsolutePath());
                if (z3) {
                    a(0.65d);
                }
                dMVarD.b(0.65d);
                R();
                for (int i3 = 1; i3 < aVar.D().size(); i3++) {
                    try {
                        G.R r3 = (G.R) aVar.D().get(i3);
                        com.efiAnalytics.apps.ts.dashboard.Z zA = null;
                        if (r3.S()) {
                            String str2 = aVar.m() + r3.c() + "." + C1798a.co;
                            File file2 = new File(str2);
                            if (C1806i.a().a(" 098u 98u498u98ug") && file2.exists()) {
                                try {
                                    zA = new C1883c(C1807j.G()).a(str2);
                                } catch (V.a e3) {
                                    com.efiAnalytics.ui.bV.d("Gauge Cluster for " + r3.c() + " is corrupt,\nthe default will be used instead.\nLoad error:\n" + e3.getMessage(), window);
                                }
                            }
                            if (zA == null) {
                                zA = new C1388aa().a(r3, "FrontPage", 1);
                                Component[] componentArrC = zA.c();
                                for (int i4 = 0; i4 < componentArrC.length; i4++) {
                                    if (componentArrC[i4] instanceof AbstractC1420s) {
                                        AbstractC1420s abstractC1420s = (AbstractC1420s) componentArrC[i4];
                                        if ((abstractC1420s.getEcuConfigurationName() == null || abstractC1420s.getEcuConfigurationName().equals("")) && !abstractC1420s.getEcuConfigurationName().equals(C0113cs.f1154a)) {
                                            abstractC1420s.setEcuConfigurationName(r3.c());
                                        }
                                    }
                                }
                            }
                            if (C1806i.a().a("64865e43s5hjhcurd")) {
                                C1425x c1425xA = cZ.a().h().a(zA, r3.c());
                                c1425xA.m(str2);
                                C1389ab.a(c1425xA);
                            }
                        }
                    } catch (V.a e4) {
                        bH.C.a("Failed to load project", e4, window);
                    }
                }
                if (z3) {
                    a(1.0d);
                }
                if (z2) {
                    a(tA.c());
                }
                if (dMVarD != null) {
                    dMVarD.b(0.65d);
                }
                G.R rC = tA.c();
                if (C1806i.a().a("64865e43s5hjhcurd")) {
                    if (C1806i.a().a("fdsa0987fdsa43298 fds") && rC.g("etDistanceFt") != null && rC.g("timeTo330Ft") != null) {
                        File fileG = C1807j.g();
                        aE.f fVar = new aE.f();
                        fVar.a(C1818g.b("Acceleration Performance"));
                        fVar.b("Acceleration_Performance.dash");
                        File file3 = new File(fVar.a(aVar));
                        if (fileG.exists() && !file3.exists()) {
                            try {
                                C1011s.a(fileG, file3);
                                aVar.a(fVar);
                            } catch (V.a e5) {
                                bH.C.a("Failed to copy Performance dash to project!");
                                e5.printStackTrace();
                            }
                        } else if (!fileG.exists()) {
                            bH.C.d("Performance Dash file not found in installer, so not loaded.");
                        }
                    }
                    int iG = aVar.G();
                    int i5 = 0;
                    Iterator itF = aVar.F();
                    while (itF.hasNext()) {
                        aE.f fVar2 = (aE.f) itF.next();
                        C1425x c1425x = new C1425x(aVar.E());
                        C1389ab.a(c1425x);
                        cZ.a().h().a(c1425x, fVar2.a());
                        try {
                            c1425x.f(fVar2.a(aVar));
                        } catch (V.a e6) {
                            bH.C.a("Error loading User Dashboard \"" + fVar2.a() + PdfOps.DOUBLE_QUOTE__TOKEN, e6, window);
                        }
                        int i6 = i5;
                        i5++;
                        double d2 = (0.2d * i6) / iG;
                        if (dMVarD != null) {
                            dMVarD.b(0.7d + d2);
                        }
                    }
                }
                bH.C.c("!!! Opened Dash " + (System.currentTimeMillis() - jCurrentTimeMillis));
                if (!aE.a.u(str)) {
                    String strB = str;
                    if (!str.startsWith("\\\\")) {
                        strB = bH.W.b(str, File.separator + File.separator, File.separator);
                    }
                    C1798a.a().b("lastProjectPath", strB);
                    new C1811n().a(strB);
                }
                if (dMVarD != null) {
                    dMVarD.b(0.9d);
                }
                try {
                    if (G.T.a().c().C().q()) {
                        c1425xB.ab();
                    } else {
                        c1425xB.k(C1818g.b("Not Connected"));
                    }
                } catch (Exception e7) {
                    e7.printStackTrace();
                }
                C0404hl.a().a(aVar.u() + " " + C1818g.b("Ready"));
                if (dMVarD != null) {
                    dMVarD.b(1.0d);
                }
                U();
            } catch (V.a e8) {
                bH.C.a("A problem was encountered loading the project " + file.getName() + ".\n", e8, window);
                new J(bH.C.f()).a(window);
                if (z3) {
                    l();
                }
            } catch (V.e e9) {
                bH.C.a("A problem was encountered loading the project " + file.getName() + ".\n", e9, window);
                if (com.efiAnalytics.ui.bV.a("The project  " + file.getName() + " appears to be corrupt. \nWould you like to create a new Project?", (Component) window, true)) {
                    b(window);
                }
                if (z3) {
                    l();
                }
            } catch (V.g e10) {
                bH.C.a("A problem was encountered loading the project " + file.getName() + ".\n", e10, window);
                new J(bH.C.f()).a(window);
                if (e10.a() != 2 && com.efiAnalytics.ui.bV.a("Would you like to update your Controller configuration file?", (Component) window, true) && (fileA = (iCVar = new iC()).a()) != null && fileA.exists()) {
                    iCVar.a(aVar.u(), aVar, fileA);
                    a(window, str);
                }
                l();
            }
        }
    }

    public void e() {
        C0113cs.a().a("dataLogTime", 0.0d);
    }

    public void f() {
        Window[] windows = Window.getWindows();
        for (int i2 = 0; i2 < windows.length; i2++) {
            try {
                if (windows[i2].isVisible() && cZ.a().c(windows[i2])) {
                    windows[i2].dispose();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
                return;
            }
        }
    }

    public boolean g() {
        G.T tA;
        aE.a aVarA;
        bT bTVarH;
        C1425x c1425xB = cZ.a().b();
        try {
            K();
        } catch (Exception e2) {
            bH.C.c("Failed to stop Slave Server");
        }
        I.k.a().c();
        boolean z2 = false;
        try {
            if (c1425xB.isVisible() && (c1425xB.aa() == null || c1425xB.aa().indexOf(C1818g.b("Shut down....")) == -1)) {
                c1425xB.k(C1818g.b("Closing Project"));
                c1425xB.paint(c1425xB.getGraphics());
                z2 = true;
            }
        } catch (Exception e3) {
        }
        try {
            if (aE.a.A() != null) {
                a(aE.a.A());
            }
            f();
            tA = G.T.a();
            aVarA = aE.a.A();
            if (aVarA != null && C1806i.a().a("-=fds[pfds[pgd-0") && C1798a.a().c(C1798a.f13396bz, C1798a.f13397bA)) {
                c("Project close");
            }
            g((Window) cZ.a().c());
            bTVarH = cZ.a().h();
        } catch (Exception e4) {
            bH.C.b("Caught this exception, clean this up:");
            e4.printStackTrace();
        }
        if (aVarA != null && !C1011s.a(aVarA.t(), 1024L)) {
            com.efiAnalytics.ui.bV.d(C1818g.b("Unable to save current project.") + "\n" + C1818g.b("Either the disk is full or the you have insufficient rights."), cZ.a().c());
            return true;
        }
        C0804hg.a().j();
        if (aVarA != null) {
            aVarA.y(cZ.a().f().a());
            Iterator it = aVarA.D().iterator();
            while (it.hasNext()) {
                G.R r2 = (G.R) it.next();
                if (r2 != null && r2.p().aM() && ((!C1798a.a().c(C1798a.f13393bw, true) || C1798a.a().c(C1798a.f13394bx, C1798a.f13395by)) && com.efiAnalytics.ui.bV.a("The tune for " + r2.c() + " has changed.\nWould you like to save a backup with the changes?", (Component) cZ.a().c(), true))) {
                    e((Window) cZ.a().c());
                }
                if (r2 != null) {
                    if (!C1798a.a().c(C1798a.f13393bw, true)) {
                        File fileD = aVarA.d(r2.c());
                        new C0171aa().b(r2, fileD.getAbsolutePath(), new n.o());
                        bH.C.d("Saved PC variables for " + r2.c() + " to:\n" + fileD.getAbsolutePath());
                    }
                    if (aVarA != null && !aVarA.J()) {
                        e(r2);
                        try {
                            W.C.a().b(aVarA.c(r2.c()));
                        } catch (Exception e5) {
                            bH.C.c("Failed to get offline Tune File for " + r2.c());
                            e5.printStackTrace();
                        }
                    }
                    C0113cs.a().i(r2.c());
                    if (r2.c("Dash_Pad") != null) {
                        try {
                            Thread.currentThread();
                            Thread.sleep(1000L);
                        } catch (InterruptedException e6) {
                        }
                    }
                    r2.C().c();
                    G.cO.a().a(r2.c());
                }
                G.aR.a().b(r2.c());
            }
        }
        if (cZ.a().f() != null) {
            cZ.a().f().b("");
        }
        if (aVarA != null) {
            if (bTVarH != null) {
                Iterator itF = bTVarH.f();
                while (itF.hasNext()) {
                    C1425x c1425x = (C1425x) itF.next();
                    if (!c1425x.e("")) {
                        return false;
                    }
                    c1425x.c();
                }
            } else if (c1425xB != null) {
                c1425xB.c();
            }
        }
        if (bTVarH != null) {
            bTVarH.f_();
        }
        jv.a().b();
        tA.b();
        aE.a.a((aE.a) null);
        this.f3337f.clear();
        if (cZ.a().f() != null) {
            cZ.a().f().b("");
        }
        if (cZ.a().b() != null) {
            cZ.a().b().ab();
        }
        bH.E.c();
        C0126i.a();
        C0155l.a().b();
        V();
        if (aVarA != null) {
            try {
                aVarA.b();
            } catch (Exception e7) {
                try {
                    Thread.sleep(100L);
                    if (aVarA != null) {
                        aVarA.b();
                    }
                } catch (Exception e8) {
                    bH.C.a("Failed to save current Project", e8, cZ.a().c());
                }
            }
        }
        if (z2) {
            c1425xB.ab();
        }
        B.b.c().a(null);
        return true;
    }

    public void b(Window window, String str) {
        try {
            G.T.a().a(str);
        } catch (V.a e2) {
            bH.C.a(e2.getMessage(), e2, window);
        }
    }

    public void a(File file) {
        if (!W.U.a(file)) {
            if (C1806i.a().a(" OKFDS09IFDSOK")) {
                SwingUtilities.invokeLater(new RunnableC0469y(this, file));
                return;
            } else {
                b(file.getAbsolutePath());
                return;
            }
        }
        RunnableC0468x runnableC0468x = new RunnableC0468x(this);
        if (SwingUtilities.isEventDispatchThread()) {
            runnableC0468x.run();
        } else {
            try {
                SwingUtilities.invokeAndWait(runnableC0468x);
            } catch (InterruptedException e2) {
                Logger.getLogger(C0338f.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            } catch (InvocationTargetException e3) {
                Logger.getLogger(C0338f.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e3);
            }
        }
        cZ.a().i().f3452c.f3071a.a(file);
    }

    public void a(File[] fileArr) {
        if (!C1806i.a().a(" OKFDS09IFDSOK") || fileArr.length <= 0) {
            return;
        }
        SwingUtilities.invokeLater(new RunnableC0470z(this, fileArr));
    }

    public void a(G.R r2) {
        if (aE.a.A().J()) {
            return;
        }
        try {
            G.T tA = G.T.a();
            String[] strArrD = tA.d();
            for (String str : strArrD) {
                G.R rC = tA.c(str);
                if (rC != null) {
                    rC.d();
                }
            }
            r2.C().d();
            for (String str2 : strArrD) {
                G.R rC2 = tA.c(str2);
                if (!rC2.c().equals(r2.c()) && (rC2.C() instanceof bQ.l)) {
                    rC2.C().d();
                }
            }
        } catch (C0129l e2) {
            if (r2.C() instanceof C1901e) {
                C1798a.a();
                if (r2.C() instanceof C1901e) {
                    C1901e c1901e = (C1901e) r2.C();
                    C1901e.a();
                    String str3 = "promptOnPortNotDetected" + c1901e.g();
                }
            }
        }
    }

    public void a(G.R r2, String str) {
        C0404hl.a().a("Opening Gauge Cluster..");
        C1425x c1425xB = cZ.a().b();
        c1425xB.N();
        try {
            if (C1806i.a().a(" 098u 98u498u98ug")) {
                c1425xB.f(str);
            } else {
                c1425xB.a(c1425xB.b(r2));
                Z z2 = new Z(r2.c(), c1425xB);
                z2.a(c1425xB);
                c1425xB.a(z2);
                c1425xB.d(true);
            }
            C0404hl.a().a("");
        } catch (V.a e2) {
            bH.C.a("Error opening saved dashboard.", e2, c1425xB);
        }
    }

    public void h() {
        JFrame jFrameC = cZ.a().c();
        String[] strArr = {C1798a.cw};
        aE.a aVarA = aE.a.A();
        if (aVarA == null) {
            com.efiAnalytics.ui.bV.d("There is no project open.\nPlease open a project first.", jFrameC);
            return;
        }
        String strS = aVarA.s();
        G.T tA = G.T.a();
        C1486ac c1486ac = new C1486ac(tA.d(), aVarA.u());
        c1486ac.a(true);
        c1486ac.setPreferredSize(com.efiAnalytics.ui.eJ.a(200, 150));
        String strB = com.efiAnalytics.ui.bV.b(jFrameC, "Compare Saved Tune", strArr, "", strS, c1486ac);
        if (strB == null || strB.equals("")) {
            return;
        }
        b(tA.c(c1486ac.a()), strB);
    }

    public void b(G.R r2, String str) {
        JFrame jFrameC = cZ.a().c();
        if (r2 == null) {
            com.efiAnalytics.ui.bV.d("No open and active ECU Configuration.\nPlease open or create a project before loading a saved tune.", jFrameC);
        } else {
            if (new hC().a(jFrameC, r2, str)) {
                return;
            }
            com.efiAnalytics.ui.bV.d("No Differences Found!\nThe settings in the file are the same as the current loaded tune.", jFrameC);
        }
    }

    public void a(Frame frame, List list) throws HeadlessException {
        a(frame, (G.R) null, list);
    }

    public void a(Frame frame, G.R r2, List list) throws HeadlessException {
        String[] strArrSplit = list != null ? new String[]{C1798a.cw, C1798a.a().a("tuneFileExt", C1798a.cw) + C1798a.cy} : C1798a.a().a(C1798a.cx, C1798a.cw).split(";");
        aE.a aVarA = aE.a.A();
        if (aVarA == null) {
            a(frame);
            return;
        }
        String strS = aVarA.s();
        G.T tA = G.T.a();
        C1486ac c1486ac = new C1486ac(tA.d(), aVarA.u());
        c1486ac.a(true);
        c1486ac.setPreferredSize(com.efiAnalytics.ui.eJ.a(200, 150));
        if (r2 != null) {
            c1486ac.a(r2.c());
        }
        String strB = com.efiAnalytics.ui.bV.b(frame, list != null ? C1818g.b("Load Dialog Settings") : C1818g.b("Open Saved Tune"), strArrSplit, "", strS, c1486ac);
        if (strB == null || strB.equals("")) {
            return;
        }
        File file = new File(strB);
        if (!file.exists()) {
            com.efiAnalytics.ui.bV.d(C1818g.b("File not found") + ":\n" + strB, frame);
            return;
        }
        if (r2 == null) {
            r2 = tA.c(c1486ac.a());
        }
        if (r2 == null) {
            com.efiAnalytics.ui.bV.d("No open and active ECU Configuration.\nPlease open or create a project before loading a saved tune.", frame);
            return;
        }
        aVarA.g(strB.substring(0, strB.lastIndexOf(File.separatorChar)));
        String strA = C0200z.a(strB);
        if (strA != null && !strA.equals(FXMLLoader.NULL_KEYWORD) && !r2.i().equals(strA)) {
            if (!com.efiAnalytics.ui.bV.a(C1818g.b("Signatures do not match!") + "\nTune File signature: " + strA + "\nConfiguration signature: " + r2.i() + "\n \n" + C1818g.b("Do you want to load it anyway?"), (Component) frame, true)) {
                return;
            } else {
                r2.O().m("");
            }
        }
        if (r2.C().q()) {
            String strB2 = C1818g.b("Would you like to send & burn configuration to controller?");
            if (!r2.p().h() && list == null) {
                strB2 = strB2 + "\n\nThe current settings will be replaced by the\n settings in " + file.getName();
                if (C1806i.a().a("-=fds[pfds[pgd-0") && C1798a.a().c(C1798a.f13402bF, C1798a.f13403bG)) {
                    strB2 = strB2 + "\nA restore point with the current " + C1382a.a(strA, C1798a.f13272f) + " settings will be created.";
                }
            }
            if (!com.efiAnalytics.ui.bV.a(strB2, (Component) frame, true)) {
                return;
            }
            if (C1806i.a().a("-=fds[pfds[pgd-0") && C1798a.a().c(C1798a.f13402bF, C1798a.f13403bG)) {
                d(r2, "Before loading " + strB + " to " + r2.c());
            }
        } else if (list == null && !r2.p().h() && C1798a.a().c(C1798a.f13393bw, true)) {
            String str = C1818g.b("This will load all settings from the file:") + "\n" + file.getName() + "\nCurrentTune." + C1798a.cw + " will be be updated with all settings from " + file.getName();
            if (C1806i.a().a("-=fds[pfds[pgd-0") && C1798a.a().c(C1798a.f13402bF, C1798a.f13403bG)) {
                str = str + "\nA restore point with current CurrentTune." + C1798a.cw + " settings will be created.";
            }
            if (!com.efiAnalytics.ui.bV.a(str + "\n\n" + C1818g.b("Would you like to proceed?"), (Component) frame, true)) {
                return;
            }
            if (C1806i.a().a("-=fds[pfds[pgd-0") && C1798a.a().c(C1798a.f13402bF, C1798a.f13403bG)) {
                d(r2, "Before loading " + strB + " to " + r2.c());
            }
        }
        a(frame, r2, strB, list);
    }

    public void a(Window window, File file) throws HeadlessException {
        if (W.U.b(file)) {
            if (aE.a.A() == null) {
                c(window, file.getAbsolutePath());
                return;
            } else {
                a(window, (G.R) null, file.getAbsolutePath());
                return;
            }
        }
        if (!W.U.c(file) || aE.a.A() == null) {
            return;
        }
        a(file);
    }

    public void a(Frame frame) {
        C1486ac c1486ac = new C1486ac(null, "");
        c1486ac.a(true);
        c1486ac.setPreferredSize(com.efiAnalytics.ui.eJ.a(200, 150));
        String strB = com.efiAnalytics.ui.bV.b(frame, "Open Saved Tune", C1798a.a().a(C1798a.cx, C1798a.cw).split(";"), "", C1807j.u(), c1486ac);
        if (strB == null || strB.equals("")) {
            return;
        }
        if (new File(strB).exists()) {
            c(frame, strB);
        } else {
            com.efiAnalytics.ui.bV.d("File not found:\n" + strB, frame);
        }
    }

    public void c(Window window, String str) {
        C0135r[] c0135rArr;
        String strA = C0200z.a(str);
        String str2 = C1818g.b("Would you like to use one of your existing projects to open this " + C1798a.cw + "?") + "\n\n" + C1818g.b("Alternatively, a temporary project can be used for viewing purposes only.") + "\n\n" + C1818g.b("Note:") + "\n" + C1818g.b("For working on a controller you should always create and open your own Project.") + "\n" + C1818g.b("Temporary projects disable saving the " + C1798a.cw + " and establishing communication with the controller.");
        String[] strArr = {C1818g.b("Use an existing Project"), C1818g.b("Use a Temporary Project")};
        if (0 == JOptionPane.showOptionDialog(window, str2, C1818g.b("Tune File Project"), 0, 3, null, strArr, strArr[1])) {
            String strB = com.efiAnalytics.ui.bV.b(C1807j.u(), window);
            if (strB == null || strB.equals("")) {
                return;
            }
            new G(this, window, strB, str).run();
            return;
        }
        try {
            File fileA = C1807j.a(window, strA);
            String[] strArrC = null;
            try {
                strArrC = new C0171aa().c(new File(str));
            } catch (V.g e2) {
                bH.C.a("Failed to retrieve settings, loading without.");
            }
            boolean z2 = false;
            if (strArrC != null) {
                c0135rArr = new C0135r[strArrC.length];
                for (int i2 = 0; i2 < strArrC.length; i2++) {
                    c0135rArr[i2] = new C0135r();
                    c0135rArr[i2].v(strArrC[i2]);
                }
            } else {
                C0136s[] c0136sArrA = new W.I().a(C0196v.a().b(fileA.getAbsolutePath()), fileA.getAbsolutePath());
                c0135rArr = new C0135r[c0136sArrA.length];
                String strSubstring = str.substring(str.lastIndexOf("."));
                if (c0136sArrA != null && c0136sArrA.length > 0) {
                    com.efiAnalytics.ui.bV.d("Warning!\nThis " + strSubstring + " is an older format and does not contain project settings.\nYou will need to set them on the Settings Tab of the\nProject Properties dialog after opening the " + strSubstring + ".", window);
                    z2 = true;
                    for (int i3 = 0; i3 < c0135rArr.length; i3++) {
                        c0135rArr[i3] = c0136sArrA[i3].b();
                    }
                }
            }
            File file = new File(C1807j.u() + "Temp");
            if (file.exists()) {
                C1011s.b(file);
            }
            aE.a aVarA = new hZ().a(strA, fileA, c0135rArr);
            File fileC = aVarA.c(aVarA.u());
            if (fileC.exists()) {
                fileC.delete();
            }
            W.D.a().a(new File(aVarA.t()));
            new G(this, window, aVarA.t(), str).run();
            if (z2) {
                j(window).a("Settings");
                C1011s.a(new File(str), aVarA.c(aVarA.u()));
            }
        } catch (V.a e3) {
            bH.C.a("Unable to get create working project.", e3, window);
        }
    }

    public void a(Window window, G.R r2, String str) throws HeadlessException {
        f(r2);
        a(window, r2, str, (List) null);
        g(r2);
    }

    public void a(Window window, G.R r2, String str, List list) throws HeadlessException {
        if (r2 == null) {
            if (G.T.a().d() == null) {
                c(window, str);
            } else if (G.T.a().d().length == 1) {
                r2 = G.T.a().c();
            } else {
                String strA = C0200z.a(new File(str));
                if (strA == null || strA.isEmpty()) {
                    bH.C.a("Attempt to load invalid tune file: " + str);
                    return;
                }
                String[] strArrD = G.T.a().d();
                int length = strArrD.length;
                int i2 = 0;
                while (true) {
                    if (i2 >= length) {
                        break;
                    }
                    G.R rC = G.T.a().c(strArrD[i2]);
                    if (rC.i().equals(strA)) {
                        r2 = rC;
                        break;
                    }
                    i2++;
                }
                if (r2 == null) {
                    Object objShowInputDialog = JOptionPane.showInputDialog(null, "Select Target Controller", "Available Controllers", -1, null, G.T.a().d(), null);
                    if (objShowInputDialog == null) {
                        return;
                    }
                    r2 = G.T.a().c(objShowInputDialog.toString());
                    if (r2 == null) {
                        bH.C.b("Contrller not found: " + objShowInputDialog);
                        return;
                    } else if (!r2.i().equals(strA) && !com.efiAnalytics.ui.bV.a(C1818g.b("This tune file was saved from a different firmware than the selected Controller.") + "\n" + C1818g.b("Are you sure you want to load this tune?"), (Component) window, true)) {
                        return;
                    }
                }
            }
        }
        K.b bVar = new K.b();
        r2.p().a(bVar);
        bH.C.c();
        try {
            try {
                if (!C1806i.a().a("FSD;LDSALKPOIERW") || C0171aa.a(str)) {
                    new C0171aa().a(r2, str, list);
                } else {
                    new W.av().a(r2, str, list);
                }
                if (bVar.a() > 2) {
                    new C0436ir(r2, cZ.a().b(), bVar.b()).a();
                }
                if (list == null) {
                    String str2 = "Tune opened, " + bVar.a() + " bytes updated. File:" + str;
                    C0404hl.a().a(str2);
                    bH.C.d(str2);
                    new K.e(r2).a(r2);
                } else {
                    String str3 = "Dialog Settings Loaded, " + bVar.a() + " bytes updated. File:" + str;
                    C0404hl.a().a(str3);
                    bH.C.d(str3);
                }
                r2.ah();
                r2.I();
                r2.p().b(bVar);
            } catch (V.a e2) {
                bH.C.a("Error opening Saved tune.", e2, window);
                r2.ah();
                r2.I();
                r2.p().b(bVar);
            } catch (V.g e3) {
                bH.C.a("Error opening Saved tune.", e3, window);
                r2.ah();
                r2.I();
                r2.p().b(bVar);
            } catch (W.aj e4) {
                com.efiAnalytics.ui.bV.d("Password Protected, Invalid Password!", window);
                r2.ah();
                r2.I();
                r2.p().b(bVar);
                return;
            }
            aE.a aVarA = aE.a.A();
            if (aVarA != null && r2.c().equals(aVarA.u()) && cZ.a().f() != null && !str.contains("CurrentTune")) {
                cZ.a().f().b(str);
            }
            if (!this.f3341i && (bH.C.d() > 0 || (bH.C.e() > 0 && !C1806i.a().a("FESLKJGD09432LK ")))) {
                String str4 = "There were errors or warnings during " + C1798a.cw + " load.\nWould you like to review them now?";
                String[] strArr = {C1818g.b("View"), C1818g.b("Ignore")};
                if (0 == JOptionPane.showOptionDialog(window, str4, C1818g.b("Audit Log"), 0, 3, null, strArr, strArr[1])) {
                    new J(bH.C.f()).a(window);
                }
            }
            if (list == null) {
                r2.p().g();
                this.f3337f.put(r2.c(), str);
            }
        } catch (Throwable th) {
            r2.ah();
            r2.I();
            r2.p().b(bVar);
            throw th;
        }
    }

    public void a(aE.a aVar, G.R r2) throws HeadlessException {
        File fileC = aVar.c(r2.c());
        if (fileC.exists()) {
            a(cZ.a().c(), r2, fileC.getAbsolutePath());
            cZ.a().f().b(aVar.W());
        }
    }

    public String i() {
        String name;
        name = "TunerStudioMS.jar";
        try {
            File file = new File(getClass().getProtectionDomain().getCodeSource().getLocation().toURI());
            name = file.getName().equals("classes") ? "TunerStudioMS.jar" : file.getName();
            System.out.println("Jar Name:" + name);
        } catch (URISyntaxException e2) {
            e2.printStackTrace();
        }
        return name;
    }

    public void d(Window window) {
        bH.W.b(bH.W.b(bH.W.b(C1798a.f13269c, "(Beta)", ""), " Lite!", ""), C1806i.f13442d, "");
        String strI = i();
        String str = bH.W.b(C1798a.f13268b, " ", "") + ".exe";
        try {
            C1798a.a().e();
            h.i.g();
        } catch (V.a e2) {
            bH.C.b("Unable to save user properties.");
        }
        try {
            if (bH.I.a()) {
                Runtime.getRuntime().exec("javaw.exe Staging \"" + ("javaw.exe -cp .;./plugins/;lib;./lib/*.jar -Djava.library.path=lib -jar " + strI) + PdfOps.DOUBLE_QUOTE__TOKEN);
            } else if (bH.I.b()) {
                File file = new File(".", "runtime/bin/java");
                if (file.exists()) {
                    bH.C.d("Bundled Java found, using that for restart.");
                    String strB = bH.W.b(file.getAbsolutePath(), " ", "\\ ");
                    String str2 = file.getAbsolutePath() + " -cp .:./plugins/:lib:./lib/*.jar -Djava.library.path=lib -jar " + strI;
                    bH.C.d("Bundled Java found, using that for restart. Restart Command:\n" + str2);
                    Runtime.getRuntime().exec(new String[]{strB, "Staging", str2});
                } else {
                    bH.C.d("Bundled Java not found, using standard installed Java.");
                    String str3 = "/usr/bin/java -cp \".:plugins/:lib\" -Djava.library.path=lib -jar " + strI;
                    bH.C.c("Restarting with command: '/usr/bin/java', then '" + str3);
                    Runtime.getRuntime().exec(new String[]{"/usr/bin/java", "Staging", str3});
                }
            } else if (bH.I.d()) {
                String canonicalPath = "/usr/bin/java -cp \".:plugins/:lib\" -Djava.library.path=lib -jar " + strI;
                File[] fileArrListFiles = new File(".").listFiles(new A(this));
                if (fileArrListFiles.length > 0) {
                    File file2 = null;
                    for (File file3 : fileArrListFiles) {
                        if (file2 == null || file3.getName().startsWith(C1798a.f13268b)) {
                            file2 = file3;
                        }
                    }
                    canonicalPath = file2.getCanonicalPath();
                }
                File absoluteFile = new File(".").getAbsoluteFile();
                bH.C.c("Restarting with command: '/usr/bin/java', then '" + canonicalPath);
                Runtime.getRuntime().exec(new String[]{"/usr/bin/java", "Staging", canonicalPath}, (String[]) null, absoluteFile);
            } else {
                String str4 = "/usr/bin/java -cp .:./plugins/:lib:./lib/*.jar -Djava.library.path=lib -jar " + strI;
                bH.C.c("Restarting 3 with command: '/usr/bin/java', then '" + str4);
                Runtime.getRuntime().exec(new String[]{"/usr/bin/java", "Staging", str4});
            }
            s();
        } catch (IOException e3) {
            bH.C.a("Failed to restart Application!", e3, window);
        }
    }

    public void j() {
        a(false);
    }

    public void a(boolean z2) {
        bZ bZVarJ;
        File fileH;
        String strB = null;
        if (!z2) {
            strB = C0491c.a().n();
            if ((strB == null || strB.equals("")) && (bZVarJ = cZ.a().j()) != null && (fileH = bZVarJ.h()) != null && fileH.exists()) {
                strB = fileH.getAbsolutePath();
            }
        }
        if (strB == null || strB.equals("")) {
            JFrame jFrameC = cZ.a().c();
            String[] strArr = {"msl", "mlg", "csv"};
            aE.a aVarA = aE.a.A();
            strB = com.efiAnalytics.ui.bV.b(jFrameC, "Open Log File", strArr, "", aVarA != null ? aVarA.q() : "");
        }
        if (strB == null || strB.equals("")) {
            return;
        }
        b(strB);
    }

    public void b(String str) {
        new C0403hk(new B(this)).a(str);
    }

    public void k() {
        new C0403hk(new C0392h(this)).c("");
    }

    public boolean c(G.R r2, String str) {
        K.b bVar = new K.b();
        r2.p().a(bVar);
        try {
            try {
                new C0171aa().a(r2, str);
                if (bVar.a() > 10) {
                    new C0436ir(r2, cZ.a().b(), bVar.b()).a();
                }
                r2.I();
                String str2 = "Sending, " + bVar.a() + " bytes.";
                C0404hl.a().a(str2);
                bH.C.d("Loaded Restore point " + str + "\n\t" + str2);
                r2.p().b(bVar);
                r2.I();
                r2.p().g();
                return true;
            } catch (V.g e2) {
                bH.C.a("Error opening Tune Restore Point.", e2, cZ.a().c());
                r2.p().b(bVar);
                r2.I();
                return false;
            } catch (W.aj e3) {
                com.efiAnalytics.ui.bV.d("Password Protected, Invalid Password!", cZ.a().c());
                r2.p().b(bVar);
                r2.I();
                return false;
            }
        } catch (Throwable th) {
            r2.p().b(bVar);
            r2.I();
            throw th;
        }
    }

    public void c(String str) {
        G.T tA = G.T.a();
        for (String str2 : tA.d()) {
            d(tA.c(str2), str);
        }
    }

    public File d(G.R r2, String str) {
        aE.a aVarA = aE.a.A();
        JFrame jFrameC = cZ.a().c();
        if (aVarA == null) {
            com.efiAnalytics.ui.bV.d("There is no project open.\nPlease open a project first.", jFrameC);
            return null;
        }
        if (C1798a.a().c(C1798a.f13406bJ, C1798a.f13407bK)) {
            File fileA = C1807j.a(C1807j.b(aVarA), new C0430il(r2.c()));
            if (fileA != null && fileA.length() > 4096 && !new hC().a(r2, fileA.getAbsolutePath())) {
                bH.C.d("Skip Restore Point for '" + r2.c() + "' because nothing changed since: " + fileA.getName());
                return fileA;
            }
        }
        C1807j.a(C1807j.b(aVarA), C1798a.a().a(C1798a.f13404bH, C1798a.f13405bI));
        File fileA2 = C1807j.a(aVarA, r2);
        String strQ = r2.Q();
        r2.v("<html>" + C1818g.b("Restore Point") + ": " + str + " - <br>" + bH.W.b(strQ, "<html>", ""));
        C0171aa c0171aa = new C0171aa();
        try {
            try {
                try {
                    if (!fileA2.getParentFile().exists()) {
                        fileA2.getParentFile().mkdirs();
                    }
                    c0171aa.a(r2, fileA2.getAbsolutePath(), new n.o());
                    C0404hl.a().a(r2.c() + " Restore point saved");
                    bH.C.d(r2.c() + " Restore point saved to: " + fileA2.getName());
                    r2.v(strQ);
                } catch (Exception e2) {
                    bH.C.a("Unhandled error occured saving tune.\nSee log for more detail", e2, jFrameC);
                    r2.v(strQ);
                }
            } catch (V.g e3) {
                com.efiAnalytics.ui.bV.d(e3.getMessage(), jFrameC);
                r2.v(strQ);
            }
            return fileA2;
        } catch (Throwable th) {
            r2.v(strQ);
            throw th;
        }
    }

    public void d(String str) {
        JRootPane rootPane = cZ.a().c().getRootPane();
        if (!(rootPane.getGlassPane() instanceof com.efiAnalytics.ui.dO)) {
            com.efiAnalytics.ui.dO dOVar = new com.efiAnalytics.ui.dO();
            dOVar.b(true);
            rootPane.setGlassPane(dOVar);
        }
        com.efiAnalytics.ui.dO dOVar2 = (com.efiAnalytics.ui.dO) rootPane.getGlassPane();
        if (str != null) {
            dOVar2.a(C1818g.b(str));
        }
        dOVar2.a(false);
        dOVar2.a(C1818g.b("Dismiss"), new C0419i(this));
        dOVar2.setVisible(true);
        this.f3339h++;
    }

    public void e(String str) {
        JRootPane rootPane = cZ.a().c().getRootPane();
        if (!(rootPane.getGlassPane() instanceof com.efiAnalytics.ui.dO)) {
            com.efiAnalytics.ui.dO dOVar = new com.efiAnalytics.ui.dO();
            dOVar.b(true);
            rootPane.setGlassPane(dOVar);
        }
        com.efiAnalytics.ui.dO dOVar2 = (com.efiAnalytics.ui.dO) rootPane.getGlassPane();
        if (str != null) {
            dOVar2.a(C1818g.b(str));
        }
        dOVar2.a(true);
        dOVar2.a();
        dOVar2.b();
        dOVar2.setVisible(true);
        this.f3339h++;
    }

    public void l() {
        b(false);
    }

    public void b(boolean z2) {
        JRootPane rootPane = cZ.a().c().getRootPane();
        int i2 = this.f3339h - 1;
        this.f3339h = i2;
        if (i2 > 0 && !z2) {
            bH.C.d("endModalBlock called, but additional block requests in place.");
            return;
        }
        this.f3339h = 0;
        if (rootPane.getGlassPane() instanceof com.efiAnalytics.ui.dO) {
            ((com.efiAnalytics.ui.dO) rootPane.getGlassPane()).setVisible(false);
        } else {
            bH.C.d("endModalBlock called, but Root Pane is not a ProgressPane.");
        }
    }

    public void a(double d2) {
        JRootPane rootPane = cZ.a().c().getRootPane();
        if (rootPane.getGlassPane() instanceof com.efiAnalytics.ui.dO) {
            ((com.efiAnalytics.ui.dO) rootPane.getGlassPane()).a(d2);
        } else {
            bH.C.d("updateModalPercent called, but Root Pane is not a ProgressPane.");
        }
    }

    public void f(String str) {
        JRootPane rootPane = cZ.a().c().getRootPane();
        if (rootPane.getGlassPane() instanceof com.efiAnalytics.ui.dO) {
            ((com.efiAnalytics.ui.dO) rootPane.getGlassPane()).a(C1818g.b(str));
        } else {
            bH.C.d("updateModalPercent called, but Root Pane is not a ProgressPane.");
        }
    }

    public boolean b(G.R r2) {
        if (aE.a.A() == null) {
            com.efiAnalytics.ui.bV.d(C1818g.b("No Project Open, cannot save custom.ini"), cZ.a().c());
            return false;
        }
        File file = new File(aE.a.A().k(r2.c()));
        C0445j c0445j = new C0445j(this);
        W.P pA = W.P.a();
        pA.a(true);
        try {
            pA.a(file, r2, c0445j);
            return true;
        } catch (IOException e2) {
            Logger.getLogger(C0338f.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            com.efiAnalytics.ui.bV.d(C1818g.b("Error saving custom.ini") + "\n" + e2.getMessage(), cZ.a().c());
            return false;
        }
    }

    public boolean a(G.R r2, List list) {
        if (aE.a.A() == null) {
            com.efiAnalytics.ui.bV.d(C1818g.b("No Project Open, cannot save ECU Definition. "), cZ.a().c());
            return false;
        }
        File fileF = aE.a.A().f(r2.c());
        File file = new File(fileF.getParentFile(), fileF.getName() + "~");
        try {
            C1011s.a(fileF, file);
        } catch (V.a e2) {
            bH.C.a("Failed to backup " + fileF.getName() + ", Error: " + e2.getLocalizedMessage());
        }
        C0455k c0455k = new C0455k(this);
        W.P p2 = new W.P();
        Iterator it = list.iterator();
        while (it.hasNext()) {
            p2.a((W.O) it.next());
        }
        p2.a(true);
        try {
            p2.a(fileF, r2, c0455k);
            file.delete();
            return true;
        } catch (IOException e3) {
            Logger.getLogger(C0338f.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e3);
            com.efiAnalytics.ui.bV.d(C1818g.b("Error saving ini") + "\nWill attempt to restore previous file.\n" + e3.getMessage(), cZ.a().c());
            File file2 = new File(fileF.getParentFile(), "Failed_" + fileF.getName());
            file2.delete();
            try {
                C1011s.a(fileF, file2);
            } catch (V.a e4) {
                bH.C.d("Failed to copy failed " + fileF.getName() + ", Error: " + e4.getLocalizedMessage());
            }
            if (!file.exists() || file.length() <= 100) {
                return false;
            }
            fileF.delete();
            try {
                C1011s.a(file, fileF);
                file.delete();
                com.efiAnalytics.ui.bV.d(C1818g.b("Previous ini restored."), cZ.a().c());
                return false;
            } catch (V.a e5) {
                bH.C.d("Failed to restore main ini backup " + fileF.getName() + ", Error: " + e5.getLocalizedMessage());
                return false;
            }
        }
    }

    public void c(G.R r2) {
        String strB = com.efiAnalytics.ui.bV.b(cZ.a().c(), "Import Channel Updates", new String[]{""}, "", C1798a.a().c(C1798a.f13307O, ""));
        if (strB == null || strB.equals("")) {
            return;
        }
        File file = new File(strB);
        C1798a.a().b(C1798a.f13307O, file.getParentFile().getAbsolutePath());
        try {
            new Y.a().a(r2, file, false);
            ArrayList arrayList = new ArrayList();
            arrayList.add(new C0482e());
            a(r2, arrayList);
            new K.e(r2).a(r2);
            C0113cs.a().a(r2);
        } catch (IOException e2) {
            com.efiAnalytics.ui.bV.d(C1818g.b("Error saving ECU Definition") + "\n" + e2.getMessage(), cZ.a().c());
        }
    }

    public void d(G.R r2) {
        String strB = com.efiAnalytics.ui.bV.b(cZ.a().c(), "Import Calibration Updates", new String[]{""}, "", C1798a.a().c(C1798a.f13307O, ""));
        if (strB == null || strB.equals("")) {
            return;
        }
        File file = new File(strB);
        C1798a.a().b(C1798a.f13307O, file.getParentFile().getAbsolutePath());
        try {
            List listA = new Y.c().a(file);
            ArrayList arrayList = new ArrayList();
            arrayList.add(new C0478a(listA));
            a(r2, arrayList);
        } catch (IOException e2) {
            com.efiAnalytics.ui.bV.d(C1818g.b("Error saving ECU Definition") + "\n" + e2.getMessage(), cZ.a().c());
        }
    }

    public void b(Frame frame) {
        C1070C c1070c = new C1070C();
        c1070c.a(G.T.a().c());
        c1070c.a(frame);
    }

    public void m() {
        G.T tA = G.T.a();
        String[] strArrD = tA.d();
        for (int i2 = 0; i2 < strArrD.length; i2++) {
            G.R rC = tA.c(strArrD[i2]);
            e(rC);
            String absolutePath = (String) this.f3337f.get(strArrD[i2]);
            if (absolutePath == null && C1798a.a().c(C1798a.f13393bw, true)) {
                absolutePath = aE.a.A().c(strArrD[i2]).getAbsolutePath();
            }
            if (absolutePath != null) {
                e(rC, absolutePath);
            }
            if (absolutePath == null || C1798a.a().c(C1798a.f13394bx, C1798a.f13395by)) {
                e((Window) cZ.a().c());
            }
        }
        if (C1806i.a().a("-=fds[pfds[pgd-0") && C1798a.a().c(C1798a.f13398bB, C1798a.f13399bC)) {
            c("User Requested Save of Settings");
        }
    }

    public void a(G.R r2, String str, List list) {
        JFrame jFrameC = cZ.a().c();
        String str2 = C1798a.a().a("tuneFileExt", C1798a.cw) + C1798a.cy;
        String[] strArr = {str2};
        aE.a aVarA = aE.a.A();
        if (aVarA == null) {
            com.efiAnalytics.ui.bV.d("There is no project open.\nPlease open a project first.", jFrameC);
            return;
        }
        String strA = com.efiAnalytics.ui.bV.a(jFrameC, "Save Dialog Tune Settings", strArr, str + "_" + bH.W.a() + "." + str2, aVarA.s(), (AbstractC1600ck) null);
        if (strA == null || strA.length() <= 0) {
            return;
        }
        bH.C.c("Save partial msq to: " + strA);
        if (!strA.endsWith(str2)) {
            strA = strA + "." + str2;
        }
        b(r2, strA, list);
    }

    public void e(G.R r2, String str) {
        h(r2);
        b(r2, str, null);
        i(r2);
    }

    public void b(G.R r2, String str, List list) {
        aE.a aVarA = aE.a.A();
        JFrame jFrameC = cZ.a().c();
        if (aVarA == null) {
            com.efiAnalytics.ui.bV.d("There is no project open.\nPlease open a project first.", jFrameC);
            return;
        }
        aVarA.g(str.substring(0, str.lastIndexOf(File.separatorChar)));
        File file = new File(str);
        W.C.a().c(file);
        if (str.endsWith(C1798a.f13295C)) {
            try {
                new W.av().a(str, r2);
            } catch (IOException e2) {
                Logger.getLogger(C0338f.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
                com.efiAnalytics.ui.bV.d("Error Saving Tune.\n" + e2.getMessage(), jFrameC);
                return;
            }
        } else {
            C0171aa c0171aa = new C0171aa();
            boolean zB = false;
            try {
                zB = W.ak.b(aVarA.j());
            } catch (Exception e3) {
                e3.printStackTrace();
            }
            try {
                boolean z2 = C0171aa.f2044d;
                if (zB) {
                    C0171aa.f2044d = zB;
                }
                c0171aa.a(r2, str, new n.o(), list);
                C0171aa.f2044d = z2;
                if (!str.contains("CurrentTune")) {
                    cZ.a().f().b(str);
                }
                if (list == null) {
                    C0404hl.a().a(r2.c() + " Tune Saved!");
                } else {
                    C0404hl.a().a(r2.c() + " Dialog settings Saved!");
                }
                bt.bO.a().c();
            } catch (V.g e4) {
                com.efiAnalytics.ui.bV.d(e4.getMessage(), jFrameC);
            } catch (Exception e5) {
                bH.C.a("Unhandled error occured saving tune.\nSee log for more detail", e5, jFrameC);
            }
        }
        r2.p().g();
        W.C.a().d(file);
        this.f3337f.put(r2.c(), str);
    }

    public void n() {
        try {
            if (aE.a.A() != null && aE.a.A().D().size() > 0) {
                Iterator it = aE.a.A().D().iterator();
                while (it.hasNext()) {
                    e((G.R) it.next());
                }
            }
        } catch (Exception e2) {
            bH.C.a("Problem saving CurrentTune files, stack to follow.");
            e2.printStackTrace();
        }
    }

    public void e(G.R r2) {
        a(r2, false);
    }

    public File a(G.R r2, boolean z2) {
        aE.a aVarA = aE.a.A();
        JFrame jFrameC = cZ.a().c();
        if (aVarA == null) {
            bH.C.b("Request to save offline tune when project is null!");
            return null;
        }
        C0171aa.f2044d = W.ak.b(aVarA.j());
        if (!z2 && (aE.a.u(aVarA.t()) || !C1798a.a().c(C1798a.f13393bw, true))) {
            return null;
        }
        String absolutePath = aVarA.c(r2.c()).getAbsolutePath();
        File file = new File(absolutePath);
        W.C.a().c(file);
        try {
            try {
                new C0171aa().a(r2, absolutePath, new n.o());
                W.C.a().d(file);
            } catch (V.g e2) {
                com.efiAnalytics.ui.bV.d(e2.getMessage(), jFrameC);
                W.C.a().d(file);
            } catch (Exception e3) {
                bH.C.a("Unhandled error occured saving offline tune.\nSee log for more detail", e3, jFrameC);
                W.C.a().d(file);
            }
            return new File(absolutePath);
        } catch (Throwable th) {
            W.C.a().d(file);
            throw th;
        }
    }

    public void e(Window window) {
        String[] strArr = {C1798a.a().a("tuneFileExt", C1798a.cw)};
        aE.a aVarA = aE.a.A();
        G.T tA = G.T.a();
        if (aVarA == null) {
            com.efiAnalytics.ui.bV.d("There is no project open.\nPlease open a project first.", window);
            return;
        }
        String strS = aVarA.s();
        String str = bH.W.a() + "." + C1798a.cw;
        C1486ac c1486ac = new C1486ac(tA.d(), aVarA.u());
        c1486ac.c("Select Controller to save tune for.");
        String strA = com.efiAnalytics.ui.bV.a(window, "Save Tune Configuration", strArr, str, strS, c1486ac);
        if (strA == null || strA.equals("")) {
            return;
        }
        File file = new File(strA);
        String str2 = C1818g.b("File already exists!") + "\n" + C1818g.b("Are you sure you want to over write this file?") + "\n\n" + file.getName();
        if ((!file.exists() || com.efiAnalytics.ui.bV.a(str2, (Component) window, true)) && strA != null && strA.length() > 0) {
            bH.C.c("EcuConfigName for Save:" + c1486ac.a());
            if (!strA.endsWith(C1798a.cw) && !strA.endsWith(C1798a.f13295C)) {
                strA = strA + "." + C1798a.cw;
            }
            e(tA.c(c1486ac.a()), strA);
        }
    }

    public void c(Frame frame) {
        String[] strArr = {"msl"};
        String str = bH.W.a() + "." + C1798a.cs;
        aE.a aVarA = aE.a.A();
        if (aVarA == null || aVarA.q() == null) {
            com.efiAnalytics.ui.bV.d("There is no project open.\nPlease open a project first.", frame);
            return;
        }
        G.R rC = G.T.a().c();
        if ((rC == null || rC.h().h()) && !com.efiAnalytics.ui.bV.a(C1818g.b("Attention!!") + "\n" + C1818g.b("There is no tune loaded.") + "\n" + C1818g.b("Before generating a log file, it is recommended to either load an (" + C1798a.cw + ") or connect to a controller.") + "\n" + C1818g.b("Otherwise some field values will be incorrect.") + "\n\n" + C1818g.b("Would you like to process this log file anyway?"), (Component) frame, true)) {
            return;
        }
        String strA = com.efiAnalytics.ui.bV.a(frame, "Target Data Log file name", strArr, str, aVarA.q());
        if (strA == null || strA.equals("")) {
            return;
        }
        aVarA.j(strA.substring(0, strA.lastIndexOf(File.separatorChar)));
        if (!strA.endsWith("." + C1798a.cs)) {
            strA = strA + "." + C1798a.cs;
        }
        File file = new File(strA);
        boolean zQ = rC.C().q();
        rC.C().c();
        C1425x c1425xB = cZ.a().b();
        if (c1425xB != null) {
            c1425xB.k(C1818g.b("Palm Extract Mode"));
        }
        aY.f fVar = new aY.f(frame, rC, file, true);
        com.efiAnalytics.ui.bV.a((Window) frame, (Component) fVar);
        fVar.setVisible(true);
        fVar.c();
        if (c1425xB != null) {
            c1425xB.k(C1818g.b("Not Connected"));
        }
        if (zQ) {
            try {
                rC.C().d();
            } catch (C0129l e2) {
                bH.C.b("failed to go back online");
            }
        }
        System.out.println("Palm Extract Dialog Closed");
    }

    public void d(Frame frame) {
        String[] strArrA;
        String[] strArr = {C1798a.cu, C1798a.cv};
        aE.a aVarA = aE.a.A();
        if (aVarA == null) {
            com.efiAnalytics.ui.bV.d("There is no project open.\nPlease open a project first.", frame);
            return;
        }
        G.R rC = G.T.a().c();
        if (((rC == null || rC.h().h()) && !com.efiAnalytics.ui.bV.a("Attention!!\nThere is no tune loaded. Before generating a log file,\nit is recommended to either load an (" + C1798a.cw + ") or connect to a controller.\nOtherwise some field values will be incorrect.\n\nWould you like to process this log file anyway?", (Component) frame, true)) || (strArrA = com.efiAnalytics.ui.bV.a((Component) frame, "Transform Raw DataLog", strArr, "", aVarA.k(), true, (AbstractC1600ck) null, true)) == null || strArrA.length == 0 || strArrA[0] == null) {
            return;
        }
        try {
            aVarA.e(strArrA[0].substring(0, strArrA[0].lastIndexOf(File.separator)));
        } catch (Exception e2) {
            bH.C.b("Unable to save Log File Folder for:" + ((Object) strArrA));
        }
        a(frame, strArrA);
    }

    public W.aA a(Window window, String[] strArr) {
        W.aA aAVar = new W.aA();
        aE.a aVarA = aE.a.A();
        if (aVarA == null) {
            com.efiAnalytics.ui.bV.d("There is no project open.\nPlease open a project first.", window);
            return aAVar;
        }
        ArrayList arrayListD = aE.a.A().D();
        bO bOVar = new bO(window, (G.R[]) arrayListD.toArray(new G.R[arrayListD.size()]));
        for (int i2 = 0; i2 < strArr.length; i2++) {
            File file = new File(strArr[i2]);
            String strA = bH.W.a();
            if (!strArr[i2].toLowerCase().endsWith("mlg") && !strArr[i2].toLowerCase().endsWith("csv") && !strArr[i2].toLowerCase().endsWith("msl")) {
                if (strArr[i2].toLowerCase().endsWith("ms3")) {
                    try {
                        Date dateA = W.af.a(file);
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(2010, 0, 1);
                        if (dateA.after(calendar.getTime())) {
                            strA = bH.W.a(dateA);
                        }
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                }
                String strSubstring = strArr[i2].substring(strArr[i2].lastIndexOf(File.separator) + 1);
                if (strSubstring.indexOf(".") != -1) {
                    strSubstring = strSubstring.substring(0, strSubstring.lastIndexOf("."));
                }
                String strSubstring2 = strArr[i2].substring(0, strArr[i2].lastIndexOf(File.separator) + 1) + (strA + "_" + strSubstring + "." + C1798a.cs);
                if (strSubstring2.lastIndexOf(File.separator) != -1) {
                    strSubstring2 = strSubstring2.substring(strSubstring2.lastIndexOf(File.separator));
                }
                bOVar.a(file, new File(aVarA.q() + File.separator + strSubstring2));
            }
        }
        if (bOVar.f2994h.isEmpty()) {
            File[] fileArr = new File[strArr.length];
            for (int i3 = 0; i3 < fileArr.length; i3++) {
                fileArr[i3] = new File(strArr[i3]);
            }
            File[] fileArrA = a(window, fileArr);
            if (com.efiAnalytics.ui.bV.a(C1818g.b("File(s) ready for viewing, would you like to open it now?"), (Component) cZ.a().c(), true)) {
                a().a(fileArrA);
            }
        } else {
            File[] fileArr2 = new File[bOVar.f2994h.size()];
            for (int i4 = 0; i4 < fileArr2.length; i4++) {
                fileArr2[i4] = ((bR) bOVar.f2994h.get(i4)).b();
            }
            File[] fileArrA2 = a(window, fileArr2);
            for (int i5 = 0; i5 < fileArrA2.length; i5++) {
                ((bR) bOVar.f2994h.get(i5)).b(fileArrA2[i5]);
            }
            bOVar.start();
        }
        return aAVar;
    }

    private File[] a(Window window, File[] fileArr) {
        return C1798a.a().c(C1798a.ca, C1798a.cb) ? new bD.I(window, C1818g.b("Rename Log Files"), C1818g.b("Set the log file names downloaded to the Project DataLog Folder.")).a(fileArr) : fileArr;
    }

    public void f(Window window) {
        if (!C1806i.a().a(";'rew-043;lh/lhoi")) {
            com.efiAnalytics.ui.bV.d("Data Logging is not enabled in this edition of " + C1798a.f13268b + "\n\nUpgrade Now!", window);
            return;
        }
        if (!C1798a.a().c(C1798a.ce, C1798a.cf).equals(C1798a.bY)) {
            g(window);
            try {
                o();
                return;
            } catch (V.a e2) {
                com.efiAnalytics.ui.bV.d(e2.getLocalizedMessage(), window);
                return;
            }
        }
        g(window);
        String strC = C1798a.a().c(C1798a.cA, C1798a.cB);
        String[] strArr = new String[2];
        if (strC.equals(C1798a.cs)) {
            strArr[0] = C1798a.cs;
            strArr[1] = C1798a.cr;
        } else {
            strArr[0] = C1798a.cr;
            strArr[1] = C1798a.cs;
        }
        String str = bH.W.a() + "." + strC;
        aE.a aVarA = aE.a.A();
        if (aVarA == null || aVarA.q() == null) {
            com.efiAnalytics.ui.bV.d("There is no project open.\nPlease open a project first.", window);
            return;
        }
        String strA = com.efiAnalytics.ui.bV.a(window, "Start New Data Log", strArr, str, aVarA.q());
        if (strA == null || strA.equals("")) {
            return;
        }
        if (!strA.toLowerCase().endsWith(C1798a.cr) && !strA.toLowerCase().endsWith(C1798a.cs) && !strA.toLowerCase().endsWith(".frd") && !strA.toLowerCase().endsWith(".mlg")) {
            strA = strA + "." + strC;
        }
        aVarA.j(strA.substring(0, strA.lastIndexOf(File.separatorChar)));
        g(strA);
    }

    public void o() throws V.a {
        if (!C1806i.a().a(";'rew-043;lh/lhoi")) {
            com.efiAnalytics.ui.bV.d("Data Logging is not enabled in this edition of " + C1798a.f13268b + "\n\nUpgrade Now!", cZ.a().c());
            return;
        }
        String str = bH.W.a() + "." + C1798a.a().c(C1798a.cA, C1798a.cB);
        aE.a aVarA = aE.a.A();
        if (aVarA == null || aVarA.q() == null) {
            throw new V.a("There is no project open. Please open a project first.");
        }
        String absolutePath = new File(aVarA.q(), str).getAbsolutePath();
        if (absolutePath == null || absolutePath.equals("")) {
            return;
        }
        aVarA.j(absolutePath.substring(0, absolutePath.lastIndexOf(File.separatorChar)));
        g(absolutePath);
    }

    public File g(String str) {
        ac.h hVarA;
        G.T tA = G.T.a();
        String strC = C1798a.a().c(C1798a.cA, C1798a.cB);
        if (!str.toLowerCase().endsWith("." + strC) && !str.toLowerCase().endsWith(".msl") && !str.toLowerCase().endsWith(".csv") && !str.toLowerCase().endsWith(".frd") && !str.toLowerCase().endsWith(".mlg")) {
            str = str + "." + strC;
        }
        String[] strArrD = tA.d();
        ArrayList arrayList = new ArrayList();
        for (int i2 = 0; i2 < strArrD.length; i2++) {
            G.R rC = tA.c(strArrD[i2]);
            if (i2 == 0 || (!rC.O().ap() && rC.S())) {
                arrayList.add(strArrD[i2]);
            }
        }
        String[] strArr = (String[]) arrayList.toArray(new String[arrayList.size()]);
        try {
            if (str.endsWith(".frd")) {
                hVarA = aZ.e.a();
            } else if (str.toLowerCase().endsWith(".mlg")) {
                hVarA = ac.u.a();
            } else {
                C0491c c0491cA = C0491c.a();
                hVarA = c0491cA;
                c0491cA.c(C1798a.a().a(C1798a.f13355aK, "\t"));
            }
            C0126i.b();
            if (hVarA.u()) {
                C0404hl.a().a(C1818g.b("Already Logging."));
                return new File(hVarA.n());
            }
            hVarA.i();
            if (aV.x.a().c() && aV.x.a().g() != null) {
                Iterator it = aV.g.a().iterator();
                while (it.hasNext()) {
                    hVarA.a((InterfaceC0489a) it.next());
                }
            }
            Iterator it2 = C0490b.a().iterator();
            while (it2.hasNext()) {
                hVarA.a((C0488D) it2.next());
            }
            if (aE.a.A() != null) {
                aE.a aVarA = aE.a.A();
                hVarA.a(aVarA.w(aVarA.O()));
            }
            if (Runtime.getRuntime().availableProcessors() == 1 && Integer.parseInt(G.T.a().c().J()) > 20) {
                G.T.a().c().c(20);
            }
            try {
                S.j jVarA = S.b.a().a(tA.c().c(), U.b.f1891b);
                if (jVarA != null) {
                    jVarA.b(true);
                }
            } catch (Exception e2) {
                bH.C.a("Error trying to reset Log Stop Trigger. " + e2.getLocalizedMessage());
            }
            hVarA.a(strArr, str);
            C0404hl.a().a("Capturing DataLog");
            return new File(hVarA.n());
        } catch (Exception e3) {
            com.efiAnalytics.ui.bV.d("Failed to start log file. Error:\n" + e3.getMessage(), cZ.a().c());
            e3.printStackTrace();
            return null;
        }
    }

    public void p() {
        ac.h hVarK = ac.h.k();
        if (hVarK == null || !hVarK.u()) {
            return;
        }
        hVarK.d("Manual");
        C0404hl.a().a("Manual MARK");
    }

    private void W() {
        C0188n c0188nR = C0804hg.a().r();
        if (c0188nR == null || !c0188nR.h() || c0188nR.k() == null || !c0188nR.k().exists()) {
            return;
        }
        try {
            if (c0188nR.k().getAbsolutePath().toLowerCase().endsWith(".mlg")) {
                ac.y.a(c0188nR, c0188nR.k().getAbsolutePath());
            } else {
                C0189o.b(c0188nR, c0188nR.k().getAbsolutePath(), h.i.f12275v);
            }
        } catch (V.a e2) {
            e2.printStackTrace();
            com.efiAnalytics.ui.bV.d(e2.getLocalizedMessage(), com.efiAnalytics.ui.bV.c());
        } catch (IOException e3) {
            e3.printStackTrace();
        }
    }

    public void q() {
        ac.h hVarA = C0491c.a();
        if (!hVarA.u()) {
            hVarA = aZ.e.a();
        }
        if (!hVarA.u()) {
            hVarA = ac.u.a();
        }
        if (hVarA.u()) {
            try {
                hVarA.l();
                W();
                C0404hl.a().a("Data log stopped");
            } catch (Exception e2) {
                Logger.getLogger(C0338f.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            }
        }
    }

    public void g(Window window) {
        File file;
        G.T.a().c();
        ac.h hVarA = C0491c.a();
        if (!hVarA.u()) {
            hVarA = aZ.e.a();
        }
        if (!hVarA.u()) {
            hVarA = ac.u.a();
        }
        try {
            if (hVarA.u()) {
                String strN = hVarA.n();
                hVarA.l();
                W();
                C0404hl.a().a("Data log stopped");
                if (G.T.a().c() != null && aE.a.A() != null) {
                    int iC = 15;
                    try {
                        iC = aE.a.A().c(G.T.a().c().O().as());
                    } catch (Exception e2) {
                    }
                    G.T.a().c().c(iC);
                }
                if (C1798a.a().c(C1798a.ce, C1798a.cf).equals(C1798a.f13420bX) && strN != null && !strN.isEmpty()) {
                    File file2 = new File(strN);
                    String str = (C1818g.b("File already exists.") + C1818g.b("Overwrite File?")) + "\n";
                    if (file2.exists() && file2.length() > 1000) {
                        String strC = C1798a.a().c(C1798a.cA, C1798a.cB);
                        String[] strArr = {strC};
                        do {
                            String strA = com.efiAnalytics.ui.bV.a(window, "Rename Data Log File", strArr, strN, file2.getParent());
                            if (strA == null || strA.equals("")) {
                                return;
                            }
                            if (!strA.toLowerCase().endsWith(strC)) {
                                strA = strA + "." + strC;
                            }
                            file = new File(strA);
                            if (!file.exists() || file.equals(file2)) {
                                break;
                            }
                        } while (!com.efiAnalytics.ui.bV.a(str + file.getName(), (Component) window, true));
                        if (!file.equals(file2)) {
                            if (file.exists()) {
                                file.delete();
                            }
                            if (file2.renameTo(file)) {
                                C0404hl.a().a("Data log saved as " + file.getName());
                                hVarA.a(file);
                            } else {
                                C0404hl.a().a("Unable to rename Data log");
                            }
                        }
                    }
                }
            }
        } catch (Exception e3) {
            bH.C.b("Something happened stopping the log??? " + e3.getMessage());
        }
    }

    public void r() {
        JFrame jFrameC = cZ.a().c();
        if (jFrameC instanceof C0293dh) {
            ((C0293dh) jFrameC).k();
        }
    }

    public boolean s() {
        if (!g()) {
            return false;
        }
        a().y();
        System.exit(0);
        return true;
    }

    public boolean t() {
        if (this.f3343b) {
            return true;
        }
        this.f3343b = true;
        RunnableC0456l runnableC0456l = new RunnableC0456l(this);
        try {
            if (SwingUtilities.isEventDispatchThread()) {
                runnableC0456l.run();
            } else {
                SwingUtilities.invokeAndWait(runnableC0456l);
            }
            return true;
        } catch (InterruptedException e2) {
            Logger.getLogger(C0338f.class.getName()).log(Level.SEVERE, "Error waiting for shutdown", (Throwable) e2);
            return true;
        } catch (InvocationTargetException e3) {
            Logger.getLogger(C0338f.class.getName()).log(Level.SEVERE, "Error on shutdown command", (Throwable) e3);
            return true;
        }
    }

    public C0207ac e(Frame frame) {
        frame.setCursor(Cursor.getPredefinedCursor(3));
        try {
            C0207ac c0207ac = new C0207ac();
            aE.a aVarA = aE.a.A();
            if (aVarA != null) {
                String strC = aVarA.C();
                if (strC == null || strC.length() == 0) {
                    strC = C1798a.f13371ba;
                }
                c0207ac.c(strC);
            }
            c0207ac.a(true);
            c0207ac.a(frame);
            frame.setCursor(Cursor.getDefaultCursor());
            return c0207ac;
        } catch (Throwable th) {
            frame.setCursor(Cursor.getDefaultCursor());
            throw th;
        }
    }

    public void f(Frame frame) {
        boolean zQ = false;
        G.R rC = G.T.a().c();
        if (rC != null) {
            zQ = rC.C().q();
            rC.C().c();
        }
        C0406hn c0406hn = new C0406hn(frame, C1806i.a().a("GD[PP-0REP"));
        com.efiAnalytics.ui.bV.a((Window) frame, (Component) c0406hn);
        if (rC != null && zQ) {
            c0406hn.b(rC.O().r() + "");
            c0406hn.a(rC.O().s());
            try {
                c0406hn.c();
            } catch (z.m e2) {
                bH.C.c("Unable to open port: " + rC.O().s());
            }
        }
        c0406hn.setVisible(true);
        if (rC == null || !zQ) {
            return;
        }
        try {
            rC.C().d();
        } catch (C0129l e3) {
            bH.C.a("Unable to go back online after closing Mini-Terminal");
        }
    }

    public void g(Frame frame) {
        if (aE.a.A() == null) {
            com.efiAnalytics.ui.bV.d("No Project Open.", frame);
            return;
        }
        if (W.ak.b(aE.a.A().j())) {
            W.ak akVar = new W.ak();
            String strA = com.efiAnalytics.ui.bV.a((Component) frame, "ECU Definition Password: ");
            if (strA == null) {
                return;
            }
            try {
                akVar.a(aE.a.A().j(), strA);
            } catch (W.aj e2) {
                com.efiAnalytics.ui.bV.d("Invalid Password!", frame);
                return;
            } catch (FileNotFoundException e3) {
                Logger.getLogger(C0338f.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e3);
                com.efiAnalytics.ui.bV.d("File Not Found!", frame);
                return;
            } catch (IOException e4) {
                Logger.getLogger(C0338f.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e4);
                com.efiAnalytics.ui.bV.d("Unable to read file.", frame);
                return;
            }
        }
        new iX(frame, G.T.a().c().p()).setVisible(true);
    }

    public void h(Frame frame) {
        new C0423id(frame).setVisible(true);
    }

    public void i(Frame frame) {
        G.R rC = G.T.a().c();
        if (rC != null) {
            aF aFVar = new aF();
            aFVar.a(rC);
            aFVar.a(frame);
        }
    }

    public void h(String str) {
        String str2 = "file:///" + new File(".").getAbsolutePath() + "/help/learnMore.html";
        if (str != null && !str.isEmpty()) {
            str2 = str2 + FXMLLoader.CONTROLLER_METHOD_PREFIX + str;
        }
        com.efiAnalytics.ui.aN.a(str2);
    }

    public void j(Frame frame) throws HeadlessException {
        JOptionPane.showMessageDialog(frame, C1798a.a().k(), "About " + C1798a.f13268b, 0, new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource("resources/companyLogo.png"))));
    }

    public void h(Window window) {
        new aM.a(window, C1818g.d()).a(C1807j.A());
    }

    public void a(G.R r2, String str, Frame frame) {
        G.T tA = G.T.a();
        G.R rC = r2 == null ? tA.c() : r2;
        if (str.indexOf("http:") == -1 && str.indexOf("file:") == -1 && str.indexOf(".") != -1) {
            rC = tA.c(str.substring(0, str.lastIndexOf(".")));
            str = str.substring(str.lastIndexOf(".") + 1);
        }
        C0050aj c0050ajB = rC.e().b(str);
        if (c0050ajB == null && rC.e().c(str) != null) {
            a(str, "-1", frame);
            return;
        }
        if (c0050ajB == null) {
            com.efiAnalytics.ui.bV.d("Help:" + str + " not found in current configuration.", frame);
            return;
        }
        if (c0050ajB.c() != null && !c0050ajB.c().equals("")) {
            C0516f c0516f = new C0516f(c0050ajB);
            c0516f.a(new com.efiAnalytics.ui.dQ(C1798a.a().d(), "HelpViewer"));
            c0516f.a(frame, c0050ajB.d());
            return;
        }
        if (c0050ajB.b() == null || c0050ajB.b().equals("")) {
            return;
        }
        String strB = c0050ajB.b();
        if (strB.toLowerCase().indexOf("www.megasquirt.info") != -1) {
            strB = bH.W.b(strB, "www.megasquirt.info", "www.megamanual.com");
        }
        URL url = null;
        try {
            url = new URL(strB);
        } catch (MalformedURLException e2) {
            Logger.getLogger(C0338f.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
        }
        if (C1011s.a(url) || r2.V() == null) {
            com.efiAnalytics.ui.aN.a(strB);
            return;
        }
        File fileB = C1011s.b(url);
        String str2 = r2.V() + fileB.getName();
        if (!C1005m.b()) {
            com.efiAnalytics.ui.bV.d(C1818g.b("Help Manual not found on your computer.") + "\n" + C1818g.b("Try again while connected to Internet and it can be downloaded for future offline use."), frame);
        } else if (!C1005m.b(str2)) {
            com.efiAnalytics.ui.bV.d(C1818g.b("Help Manual not found on your computer.") + "\n" + C1818g.b("Also not found on server.") + "\n" + C1818g.b("Check with your firmware provider and place manual on your computer at") + ":\n" + fileB.getAbsolutePath(), frame);
        } else if (com.efiAnalytics.ui.bV.a(C1818g.b("The help manual for your firmware is not installed.") + "\n\n" + C1818g.b("Would you like to download it now?"), (Component) frame, true)) {
            a(C1818g.b("Downloading Help Manual"), str2, fileB, new F(this, strB));
        }
    }

    public void a(String str, String str2, File file, InterfaceC0253bv interfaceC0253bv) {
        new C0457m(this, file, str2, interfaceC0253bv, com.efiAnalytics.ui.bV.a((Window) cZ.a().c(), "<html>" + C1818g.b(str) + "<br> " + file.getName() + "</html>")).start();
    }

    public void u() {
        C0512b c0512bA = new C1067a().a();
        if (c0512bA == null || !C1806i.a().a(";'FG;'-05;'FG0-=")) {
            c0512bA.a(C1798a.f13268b + " " + C1798a.f13269c + " Help");
            c0512bA.b(C0514d.a("/help/learnMore.html"));
        }
        a().a(c0512bA, cZ.a().c());
    }

    public void i(Window window) {
        SwingUtilities.invokeLater(new RunnableC0458n(this, window));
    }

    public void a(C0512b c0512b, Window window) {
        if (c0512b == null || c0512b.b() == null) {
            com.efiAnalytics.ui.bV.d("Invalid help reference.", window);
            return;
        }
        if (c0512b.b() == null || c0512b.b().equals("")) {
            return;
        }
        C0516f c0516f = new C0516f();
        c0516f.a(C0514d.a("/help/index.html"));
        c0516f.a(new com.efiAnalytics.ui.dQ(C1798a.a().d(), "AppHelpViewer"));
        try {
            c0516f.b(c0512b.b());
        } catch (V.a e2) {
            bH.C.a("Unable to open help:\n" + c0512b.b(), e2, window);
        }
        c0516f.a(window, c0512b.a());
    }

    public void a(String str, String str2, Frame frame) {
        G.T tA = G.T.a();
        G.R rC = tA.c();
        if (str.indexOf(".") != -1) {
            String strSubstring = str.substring(0, str.lastIndexOf("."));
            rC = tA.c(strSubstring);
            if (rC == null) {
                bH.C.d("Configuration " + strSubstring + " not found using working configuation to open dialog.");
                rC = tA.c();
            }
            str = str.substring(str.lastIndexOf(".") + 1);
        }
        if (rC.h().h()) {
            com.efiAnalytics.ui.bV.d(C1818g.b("Attention!!") + "\n" + C1818g.b("There is no tune loaded. Before doing any configuration") + ",\n" + C1818g.b("it is recommended to either load an (" + C1798a.cw + ") or connect to a controller.") + "\n\n" + C1818g.b("It is not advised to create a tune (" + C1798a.cw + ") from scratch."), frame);
        }
        C0088bu c0088buC = rC.e().c(str);
        if (c0088buC != null) {
            if (c0088buC instanceof G.aX) {
                a(rC, (G.aX) c0088buC, frame);
                return;
            } else {
                a(rC, c0088buC, frame);
                return;
            }
        }
        try {
            if (!iG.a().a(frame, rC, str, str2)) {
                com.efiAnalytics.ui.bV.d(str + " " + C1818g.b("not found in current configuration.") + "\n" + C1818g.b("Perhaps it hasn't been created."), frame);
            }
        } catch (V.a e2) {
            com.efiAnalytics.ui.bV.d("Error showing dialog " + str + "\n\n" + e2.getMessage(), frame);
        }
    }

    public void v() throws NumberFormatException {
        String strA = com.efiAnalytics.ui.bV.a("{OutputChannel},{Value}", false, (Component) cZ.a().c());
        if (strA == null || strA.equals("")) {
            return;
        }
        StringTokenizer stringTokenizer = new StringTokenizer(strA, ",");
        String strNextToken = stringTokenizer.nextToken();
        double d2 = Double.parseDouble(stringTokenizer.nextToken());
        G.R rC = G.T.a().c();
        if (rC == null) {
            com.efiAnalytics.ui.bV.d("No Config open", cZ.a().c());
            return;
        }
        Iterator it = C0113cs.a().a(rC.c(), strNextToken).iterator();
        while (it.hasNext()) {
            ((InterfaceC0109co) it.next()).setCurrentOutputChannelValue(strNextToken, d2);
        }
    }

    public void w() {
        JFrame jFrameC = cZ.a().c();
        String strB = com.efiAnalytics.ui.bV.b(jFrameC, "Install " + C1798a.f13268b + " Plugin", new String[]{"jar"}, "", C1798a.a().c(C1798a.dm, ""));
        if (strB == null || strB.equals("")) {
            return;
        }
        File file = new File(strB);
        C1798a.a().b(C1798a.dm, file.getParentFile().getAbsolutePath());
        ApplicationPlugin applicationPluginA = null;
        boolean z2 = false;
        try {
            applicationPluginA = C1190l.a().a(file);
            if (C1190l.a().c(applicationPluginA.getIdName())) {
                if (!com.efiAnalytics.ui.bV.a("A plugin with a matching ID: '" + applicationPluginA.getIdName() + "' is already installed.\n\nDo you want to replace it with the new one?", (Component) cZ.a().c(), true)) {
                    return;
                } else {
                    z2 = true;
                }
            }
            if (!com.efiAnalytics.ui.bV.a("<html><body><h2>Install " + C1798a.f13268b + " plugin!</h2><b>Plugin Name:</b> '" + applicationPluginA.getDisplayName() + "'<br><b>Plugin ID:</b> '" + applicationPluginA.getIdName() + "'<br><b>Description:</b> <br>" + bH.W.a(applicationPluginA.getDescription(), 60, "<br>") + "<br><br>Do you want to Install this plugin?</body></html>", (Component) cZ.a().c(), true)) {
                return;
            }
        } catch (V.a e2) {
            com.efiAnalytics.ui.bV.d("Error loading Plugin:\n" + e2.getMessage(), cZ.a().c());
        } catch (Exception e3) {
            com.efiAnalytics.ui.bV.d("Unknown Error loading Plugin.\nIs this a valid Plugin file?\nError:\n" + e3.getMessage(), cZ.a().c());
        } catch (UnsupportedClassVersionError e4) {
            com.efiAnalytics.ui.bV.d("Unsupported Class Vesion loading Plugin.\nError:\n" + e4.getMessage(), cZ.a().c());
        } catch (Error e5) {
            com.efiAnalytics.ui.bV.d("Unknown Error loading Plugin.\nIs this a valid Plugin file?\nError:\n" + e5.getMessage(), cZ.a().c());
        }
        try {
            File fileB = C1807j.b();
            File fileE = C1190l.a().c(applicationPluginA.getIdName()) ? C1190l.a().e(applicationPluginA.getIdName()) : new File(fileB, file.getName());
            if (fileE.exists() && C1190l.a().e(applicationPluginA.getIdName()) == null && C1190l.a().b(fileE)) {
                fileE = new File(fileB, applicationPluginA.getIdName() + ".jar");
            }
            try {
                C1011s.a(file, fileE);
                if (z2) {
                    try {
                        C1190l.a().d(applicationPluginA.getIdName());
                    } catch (V.a e6) {
                        Logger.getLogger(C0338f.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e6);
                        z2 = false;
                    }
                }
                if (z2) {
                    com.efiAnalytics.ui.bV.d("<html><body><h2>" + C1798a.f13268b + " plugin Installation</h2>Plugin Name: '" + applicationPluginA.getDisplayName() + "<br>Has successfully Upgraded and should be ready for use.<br></body></html>", jFrameC);
                } else {
                    com.efiAnalytics.ui.bV.d("<html><body><h2>" + C1798a.f13268b + " plugin Installation</h2>Plugin Name: '" + applicationPluginA.getDisplayName() + "<br>Has successfully Installed.<br>A restart of " + C1798a.f13268b + " is required to use this plugin.<br></body></html>", jFrameC);
                }
            } catch (V.a e7) {
                bH.C.a("Unable to copy Plugin to Plugin Dir", e7, cZ.a().c());
            }
        } catch (V.a e8) {
            bH.C.a("Unable to access Plugin Dir", e8, cZ.a().c());
        }
    }

    public void f(G.R r2, String str) {
        try {
            ApplicationPlugin applicationPluginA = C1190l.a().a(str);
            C1186h c1186h = new C1186h(applicationPluginA);
            com.efiAnalytics.ui.bV.a(c1186h, cZ.a().c(), applicationPluginA.getDisplayName(), c1186h);
        } catch (V.a e2) {
            Logger.getLogger(C0338f.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            com.efiAnalytics.ui.bV.d(C1818g.b("Unable to display Plugin") + "\nError:\n" + e2.getMessage(), cZ.a().c());
        } catch (UnsupportedClassVersionError e3) {
            com.efiAnalytics.ui.bV.d("Unsupported Class Vesion loading Plugin.\nError:\n" + e3.getMessage(), cZ.a().c());
        } catch (Error e4) {
            com.efiAnalytics.ui.bV.d("Unknown Error loading Plugin.\nIs this a valid Plugin file?\nError:\n" + e4.getMessage(), cZ.a().c());
        }
    }

    /* JADX WARN: Finally extract failed */
    public void a(G.R r2, C0088bu c0088bu, Window window) {
        if (window == null) {
            window = cZ.a().c();
        }
        if (c0088bu.Y()) {
            String strA = com.efiAnalytics.ui.bV.a((Component) window, "Password protected Dialog. Enter the password.");
            String strX = c0088bu.X();
            if (strA == null) {
                return;
            }
            if (!strA.equals(strX)) {
                com.efiAnalytics.ui.bV.d("Invalid Password", window);
                return;
            }
        }
        if (com.efiAnalytics.tuningwidgets.panels.W.a(r2, c0088bu, window)) {
            try {
                C0394hb c0394hb = new C0394hb(r2, c0088bu);
                String strB = bH.W.b(c0088bu.M(), PdfOps.DOUBLE_QUOTE__TOKEN, "");
                if (strB.trim().isEmpty()) {
                    strB = G.bL.c(r2, c0088bu.aJ());
                    if (strB == null || strB.equals(c0088bu.aJ())) {
                        strB = C1818g.b("Settings");
                    }
                }
                String strB2 = C1818g.b(strB);
                com.efiAnalytics.ui.dF dFVar = new com.efiAnalytics.ui.dF(window, strB2, c0394hb, c0088bu.n() || c0394hb.h());
                dFVar.add(BorderLayout.CENTER, c0394hb);
                c0394hb.a((InterfaceC1565bc) dFVar);
                G.cZ cZVarN = c0088bu.N();
                try {
                    C0126i.a(r2.c(), cZVarN, new C0459o(this, cZVarN, dFVar));
                } catch (V.a e2) {
                    com.efiAnalytics.ui.bV.d("Invalid Title Defined: " + cZVarN.toString() + "\nError: " + e2.getLocalizedMessage(), dFVar);
                }
                boolean z2 = (c0088bu instanceof C0079bl) || (c0088bu instanceof C0076bi) || (c0088bu instanceof K.a) || (c0088bu instanceof K.i) || (c0088bu instanceof C0072be) || G.bL.a(c0088bu);
                String strAJ = c0088bu.aJ();
                if (c0088bu instanceof C0076bi) {
                    strAJ = strAJ + "3D";
                }
                c0394hb.a(new H(this, dFVar, strAJ));
                Rectangle rectangleA = a(strAJ + strB2, dFVar.getBounds());
                if (rectangleA.height == 0) {
                    dFVar.pack();
                    com.efiAnalytics.ui.bV.a(window, (Component) dFVar);
                    rectangleA = dFVar.getBounds();
                }
                if (z2) {
                    Rectangle rectangleA2 = com.efiAnalytics.ui.bV.a(rectangleA);
                    if (rectangleA2.height < dFVar.getHeight()) {
                        rectangleA2.height = dFVar.getHeight();
                    }
                    if (rectangleA2.width < dFVar.getWidth()) {
                        rectangleA2.width = dFVar.getWidth();
                    }
                    dFVar.setBounds(rectangleA2);
                } else {
                    dFVar.pack();
                    dFVar.setLocation(rectangleA.f12372x, rectangleA.f12373y);
                    dFVar.setBounds(com.efiAnalytics.ui.bV.a(dFVar.getBounds()));
                }
                Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
                screenSize.height -= 30;
                if (dFVar.getWidth() > screenSize.width) {
                    dFVar.setSize(screenSize.width, dFVar.getHeight());
                    dFVar.setLocation(0, dFVar.getLocation().f12371y);
                    dFVar.doLayout();
                }
                if (dFVar.getHeight() > screenSize.height) {
                    dFVar.setSize(dFVar.getWidth(), screenSize.height);
                    dFVar.setLocation(dFVar.getLocation().f12370x, 0);
                    dFVar.doLayout();
                }
                if ((dFVar.getX() > screenSize.width || dFVar.getY() > screenSize.height) && !com.efiAnalytics.ui.bV.h()) {
                    com.efiAnalytics.ui.bV.a(window, (Component) dFVar);
                }
                if (r2.O().aj()) {
                    a(r2, G.bL.b(r2, c0088bu));
                }
                c0394hb.k();
                try {
                    dFVar.setVisible(true);
                    cZ.a().b().c(true);
                } catch (Throwable th) {
                    cZ.a().b().c(true);
                    throw th;
                }
            } catch (Exception e3) {
                bH.C.a("Error showing Dialog", e3, cZ.a().c());
            }
        }
    }

    public void a(G.R r2, C0102ch[] c0102chArr) {
        if (c0102chArr != null) {
            try {
                ArrayList arrayList = new ArrayList();
                for (int i2 = 0; i2 < c0102chArr.length; i2++) {
                    if (c0102chArr[i2].a() >= 0) {
                        if (r2.O().B(c0102chArr[i2].a())) {
                            a(r2, c0102chArr[i2]);
                            bH.C.d("Initiating read page " + (c0102chArr[i2].a() + 1) + " : " + c0102chArr[i2].b() + CallSiteDescriptor.TOKEN_DELIMITER + c0102chArr[i2].c());
                        } else if (!arrayList.contains(Integer.valueOf(c0102chArr[i2].a()))) {
                            a(r2, c0102chArr[i2].a());
                            arrayList.add(Integer.valueOf(c0102chArr[i2].a()));
                            bH.C.d("Initiating read page " + (c0102chArr[i2].a() + 1));
                        }
                    }
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }

    public void a(G.R r2, G.aX aXVar, Frame frame) {
        JDialog jDialogJ = j(aXVar.M());
        if (jDialogJ != null && jDialogJ.isDisplayable()) {
            jDialogJ.requestFocus();
        } else {
            if (r2 == null) {
                com.efiAnalytics.ui.bV.d(C1818g.b("You must have a working configuration open."), frame);
                return;
            }
            JDialog jDialogA = com.efiAnalytics.ui.bV.a(new com.efiAnalytics.tunerStudio.panels.J(r2, aXVar), frame, C1818g.b(aXVar.M()), (InterfaceC1565bc) null);
            jDialogA.setDefaultCloseOperation(2);
            this.f3338g.put(aXVar.M(), jDialogA);
        }
    }

    private JDialog j(String str) {
        for (JDialog jDialog : this.f3338g.values()) {
            if (!jDialog.isDisplayable()) {
                this.f3338g.remove(jDialog.getTitle());
            }
        }
        return (JDialog) this.f3338g.get(str);
    }

    public void a(Frame frame, String str) {
        String strC = C1798a.a().c("lookAndFeelClass", "defaultLookAndFeelClass");
        C1798a.a().b("lookAndFeelClass", str);
        if (str.contains("TinyLookAndFeel") || strC.contains("TinyLookAndFeel")) {
            if (com.efiAnalytics.ui.bV.a(C1818g.b("You must restart for changes to take effect. Restart Now?"), (Component) frame, true)) {
                try {
                    C1798a.a().e();
                } catch (V.a e2) {
                    Logger.getLogger(C0338f.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
                }
                d((Window) frame);
                return;
            }
            return;
        }
        try {
            UIManager.setLookAndFeel(str);
            if (str.contains("Nimbus")) {
                UIDefaults defaults = UIManager.getLookAndFeel().getDefaults();
                defaults.put("Table.cellNoFocusBorder", new Insets(0, 0, 0, 0));
                defaults.put("Table.focusCellHighlightBorder", new Insets(0, 0, 0, 0));
                UIManager.getLookAndFeelDefaults().put("Table.cellNoFocusBorder", new Insets(0, 0, 0, 0));
                UIManager.getLookAndFeelDefaults().put("Table.focusCellHighlightBorder", new Insets(0, 0, 0, 0));
            }
            x();
            SwingUtilities.updateComponentTreeUI(frame);
            String strD = cZ.a().g().d();
            cZ.a().g().b("Main Menu Style");
            cZ.a().g().b(strD);
            com.efiAnalytics.ui.bV.e();
            com.efiAnalytics.ui.bV.a(new hY());
        } catch (Exception e3) {
            bH.C.c("Exception setting look to " + str);
        }
    }

    public void x() {
        boolean z2 = bH.I.a() && UIManager.getLookAndFeel().isNativeLookAndFeel();
        try {
            UIManager.setLookAndFeel(UIManager.getLookAndFeel());
        } catch (UnsupportedLookAndFeelException e2) {
            Logger.getLogger(C0338f.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
        }
        Font font = UIManager.getFont("Label.font");
        if (font != null && font.getSize() < 14) {
            com.efiAnalytics.ui.eJ.c(font.getSize());
        }
        float fA = C1798a.a().a(C1798a.f13352aH, C1798a.a().o()) / com.efiAnalytics.ui.eJ.a();
        Set<Object> setKeySet = UIManager.getLookAndFeelDefaults().keySet();
        Object[] array = setKeySet.toArray(new Object[setKeySet.size()]);
        Object[] objArr = new Object[array.length + 1];
        System.arraycopy(array, 0, objArr, 0, array.length);
        objArr[objArr.length - 1] = "defaultFont";
        Font font2 = UIManager.getFont("defaultFont");
        for (Object obj : objArr) {
            if (obj != null && obj.toString().toLowerCase().contains("font")) {
                Font font3 = UIManager.getFont(obj);
                if (font3 != null && !z2) {
                    Float fValueOf = (Float) this.f3344k.get(obj);
                    if (fValueOf == null) {
                        this.f3344k.put(obj, Float.valueOf(font3.getSize2D()));
                        fValueOf = Float.valueOf(font3.getSize2D());
                    }
                    UIManager.put(obj, new Font(font3.getFamily(), font3.getStyle(), Math.round(com.efiAnalytics.ui.eJ.a(fValueOf.floatValue() * fA))));
                } else if (font3 == null) {
                    bH.C.c("no update:" + obj);
                }
            } else if (obj != null && obj.toString().equals("ScrollBar.width")) {
                System.out.println(obj);
                if (UIManager.getInt(obj) < 20) {
                    UIManager.put(obj, Integer.valueOf(com.efiAnalytics.ui.eJ.a(UIManager.getInt(obj))));
                }
            } else if (UIManager.get(obj) instanceof Font) {
                bH.C.c("no update:" + obj);
            }
        }
        if (font2 != null) {
            UIManager.getLookAndFeel().getDefaults().put("defaultFont", new Font(font2.getFamily(), font2.getStyle(), com.efiAnalytics.ui.eJ.a(12)));
        }
        if (cZ.a().c() != null) {
            SwingUtilities.updateComponentTreeUI(cZ.a().c());
            String strD = cZ.a().g().d();
            cZ.a().g().b("Main Menu Style");
            cZ.a().g().b(strD);
            com.efiAnalytics.ui.bV.e();
            com.efiAnalytics.ui.bV.a(new hY());
        }
    }

    public void a(InterfaceC0994b interfaceC0994b) {
        this.f3335d.add(interfaceC0994b);
    }

    protected void y() {
        C1425x c1425xB = cZ.a().b();
        try {
            if (c1425xB.isVisible()) {
                c1425xB.k(C1798a.a().b() + " " + C1818g.b("Shut down...."));
                c1425xB.paint(c1425xB.getGraphics());
            }
        } catch (Exception e2) {
        }
        n();
        Thread.yield();
        Iterator it = this.f3335d.iterator();
        while (it.hasNext()) {
            if (!((InterfaceC0994b) it.next()).b()) {
                bH.C.b("App shut down has been asked to stop.\nI will kill event thread. Ignore stack trace.");
                if (c1425xB != null) {
                    c1425xB.ab();
                }
                cZ.a().c().setVisible(true);
                ((String) null).toString();
            }
        }
        bH.C.d("Finalizing Sensors");
        aV.x.a().f();
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v26, types: [java.util.Properties] */
    private Rectangle a(String str, Rectangle rectangle) {
        Rectangle rectangle2 = new Rectangle();
        aE.a aVarA = aE.a.A();
        if (aVarA == null) {
            aVarA = C1798a.a().d();
        }
        try {
            rectangle2.f12372x = Integer.parseInt(aVarA.getProperty(str + "_X", "0"));
            if (rectangle2.f12372x == 0) {
                rectangle2.f12372x = rectangle.f12372x;
            } else {
                rectangle2.f12372x = com.efiAnalytics.ui.eJ.a(rectangle2.f12372x);
            }
            rectangle2.f12373y = Integer.parseInt(aVarA.getProperty(str + "_Y", "0"));
            if (rectangle2.f12373y == 0) {
                rectangle2.f12373y = rectangle.f12373y;
            } else {
                rectangle2.f12373y = com.efiAnalytics.ui.eJ.a(rectangle2.f12373y);
            }
            rectangle2.width = Integer.parseInt(aVarA.getProperty(str + "_width", "0"));
            if (rectangle2.width == 0) {
                rectangle2.width = rectangle.width;
            } else {
                rectangle2.width = com.efiAnalytics.ui.eJ.a(rectangle2.width);
            }
            rectangle2.height = Integer.parseInt(aVarA.getProperty(str + "_height", "0"));
            if (rectangle2.height == 0) {
                rectangle2.height = rectangle.height;
            } else {
                rectangle2.height = com.efiAnalytics.ui.eJ.a(rectangle2.height);
            }
            return rectangle2;
        } catch (Exception e2) {
            return rectangle;
        }
    }

    public void k(Frame frame) {
        if (C1806i.a().a("09fewlkm309glkfds09")) {
            String strC = C1798a.a().c("lastProjectPath", "");
            if (strC.equals("") || aE.a.u(strC)) {
                return;
            }
            a((Window) frame, strC);
        }
    }

    public C0421ib j(Window window) {
        n.n nVarM = cZ.a().m();
        if (nVarM != null && nVarM.getTitleAt(nVarM.getSelectedIndex()).equals(gW.f3467r)) {
            return null;
        }
        aE.a aVarA = aE.a.A();
        if (aVarA == null) {
            com.efiAnalytics.ui.bV.d("There must be an open project to show Project Properties.", window);
            return null;
        }
        C0421ib c0421ib = new C0421ib(window);
        try {
            c0421ib.a(aVarA);
        } catch (V.a e2) {
            bH.C.a("Error setting current project.", e2, window);
        }
        c0421ib.a();
        c0421ib.a(new C0460p(this));
        return c0421ib;
    }

    public void z() {
        this.f3342j.run();
    }

    public void k(Window window) {
        aE.a aVarA = aE.a.A();
        if (aVarA == null) {
            return;
        }
        new aY.j("" + aVarA.c(G.T.a().c().O().as())).a(window);
    }

    public void a(bT bTVar) {
        boolean z2;
        aE.a aVarA = aE.a.A();
        if (aVarA == null) {
            com.efiAnalytics.ui.bV.d("There must be a project open to add additional Gauge Clusters.", bTVar);
            return;
        }
        bT bTVarH = cZ.a().h();
        String strShowInputDialog = "";
        do {
            strShowInputDialog = (strShowInputDialog == null || strShowInputDialog.trim().length() == 0) ? JOptionPane.showInputDialog(bTVar, C1818g.b("New Gauge Cluster Name"), C1818g.b("Add New Gauge Cluster Tab"), 3) : JOptionPane.showInputDialog(bTVar, C1818g.b("New Gauge Cluster Name"), strShowInputDialog);
            if (strShowInputDialog == null || strShowInputDialog.trim().equals("")) {
                return;
            }
            if (bTVarH.d(strShowInputDialog)) {
                com.efiAnalytics.ui.bV.d(C1818g.b("Name Already In Use."), bTVarH);
                z2 = true;
            } else if (C1011s.a(strShowInputDialog)) {
                z2 = false;
            } else {
                com.efiAnalytics.ui.bV.d(C1818g.b("Invalid Characters in gauge cluster name."), bTVarH);
                z2 = true;
            }
        } while (z2);
        C1425x c1425x = new C1425x(G.T.a().c());
        c1425x.setName(strShowInputDialog);
        c1425x.l(true);
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(c1425x.X());
        c1425x.setBackground(Color.DARK_GRAY);
        aE.f fVar = new aE.f();
        fVar.a(strShowInputDialog);
        fVar.b(strShowInputDialog + "." + C1798a.co);
        c1425x.m(fVar.a(aVarA));
        bTVarH.a(c1425x, strShowInputDialog);
        bTVarH.g(strShowInputDialog);
        aVarA.a(fVar);
        c1425x.a(new D(this, strShowInputDialog));
    }

    public void A() {
        if (com.efiAnalytics.ui.bV.a("The application will be closed after removing the Registration.\n \nThis computer will be removed from the count of active computers using \nyour registration within 30-60 days.\n\n Are you sure you want to remove the registration information Now?", (Component) cZ.a().c(), true)) {
            C1798a.a().d(C1798a.cC, "");
            C1798a.a().d(C1798a.cD, "");
            C1798a.a().d(C1798a.cF, "");
            C1798a.a().d(C1798a.cE, "");
            C1798a.a().d(C1798a.f13276j, "");
            C1798a.a().d(C1798a.f13280n, "");
            C1798a.a().b(C1798a.cL, "false");
            try {
                C1798a.a().i();
            } catch (V.a e2) {
                bH.C.a("Failed to save registration file.", e2, cZ.a().c());
            }
            com.efiAnalytics.ui.bV.d(C1798a.f13268b + " " + C1798a.f13269c + " registration has been removed.\n" + C1798a.f13268b + " will now close.", cZ.a().c());
            s();
        }
    }

    public void l(Frame frame) {
        if (C1798a.a().a(C1798a.cF, (String) null) != null) {
            bD.a(frame);
        }
    }

    public void b(Frame frame, String str) {
        C1798a.a().b("viewLanguageCode", str);
        if (com.efiAnalytics.ui.bV.a(C1818g.b("You must restart for changes to take effect. Restart Now?"), (Component) frame, true)) {
            d((Window) frame);
        }
    }

    public void m(Frame frame) {
        aE.a aVarA = aE.a.A();
        if (aVarA == null) {
            com.efiAnalytics.ui.bV.d("You must have a Project Open for Restore Points.", frame);
        } else {
            new aY.s(aVarA).a(frame);
        }
    }

    public void B() {
        new Q.a(C1798a.a().c(C1798a.f13358aN, ""), C1798a.a().c(C1798a.cE, ""), "", C1798a.f13268b, C1798a.f13269c, C1798a.f13267a).a(cZ.a().c());
    }

    public void C() {
        G.R rC = G.T.a().c();
        if (rC == null) {
            com.efiAnalytics.ui.bV.d("There are no Active Protocol Stats.", cZ.a().c());
            return;
        }
        J.h hVarD = rC.C().D();
        if (hVarD == null) {
            com.efiAnalytics.ui.bV.d("There are no Active Protocol Stats.", cZ.a().c());
        } else {
            com.efiAnalytics.tunerStudio.panels.F f2 = new com.efiAnalytics.tunerStudio.panels.F(hVarD);
            f2.a(cZ.a().c(), C1818g.b("Protocol Stats"), f2);
        }
    }

    public void D() {
        JFrame jFrameC = cZ.a().c();
        C0475a c0475a = new C0475a(cZ.a().c());
        c0475a.setSize(com.efiAnalytics.ui.eJ.a(600), com.efiAnalytics.ui.eJ.a(280));
        com.efiAnalytics.ui.bV.a((Window) jFrameC, (Component) c0475a);
        c0475a.setVisible(true);
    }

    public void a(G.R r2, G.bH bHVar, Component component) {
        try {
            C1202c.a().a(r2, bHVar).a(component);
        } catch (C1204e e2) {
            com.efiAnalytics.ui.bV.d(e2.getMessage(), component);
            Logger.getLogger(C0338f.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
        }
    }

    public void E() {
        new C0474a(aV.x.a().b()).a(cZ.a().c());
    }

    public void F() {
        C0516f c0516f = new C0516f();
        String strA = C0514d.a("/help/learnMore.html");
        try {
            com.efiAnalytics.ui.bV.a((Window) cZ.a().c(), (Component) c0516f);
            c0516f.b(strA);
        } catch (V.a e2) {
            bH.C.a("Unable to open help:\n" + strA, e2, cZ.a().c());
        }
        c0516f.setPreferredSize(com.efiAnalytics.ui.eJ.a(ORBConstants.RI_NAMESERVICE_PORT, 800));
        c0516f.a(cZ.a().c(), C1818g.b("Upgrade Now!!"));
    }

    public void G() {
        if (C1806i.a().a("h-=ds[]gdsgds[p")) {
            try {
                iK.a().a(cZ.a().b());
            } catch (V.a e2) {
                C0404hl.a().d("Error: " + e2.getMessage());
            }
        }
    }

    public void l(Window window) {
        G.R rC = G.T.a().c();
        if (rC == null) {
            bH.C.b("EcuConfig is null, not showing DataLogEvent");
        } else {
            new C1509g(rC).a(window);
        }
    }

    public void m(Window window) {
        new C1458g(aE.a.A()).a(window);
    }

    public void n(Frame frame) {
        aN.a aVar = new aN.a(frame, new W.ar(C1798a.a().d(), "BinaryDiff"));
        aVar.setSize(com.efiAnalytics.ui.eJ.a(1000), com.efiAnalytics.ui.eJ.a(600));
        com.efiAnalytics.ui.bV.a((Component) cZ.a().c(), (Component) aVar);
        aVar.setVisible(true);
    }

    public void H() {
        if (aE.a.A() == null) {
            com.efiAnalytics.ui.bV.d("A Project must be loaded to add a channel", cZ.a().c());
        }
        new C1085a().a();
    }

    public void o(Frame frame) {
        G.R rC = G.T.a().c();
        if (rC != null) {
            if (W.ak.b(aE.a.A().j())) {
                W.ak akVar = new W.ak();
                String strA = com.efiAnalytics.ui.bV.a((Component) frame, "ECU Definition Password: ");
                if (strA == null) {
                    return;
                }
                try {
                    akVar.a(aE.a.A().j(), strA);
                } catch (W.aj e2) {
                    com.efiAnalytics.ui.bV.d("Invalid Password!", frame);
                    return;
                } catch (FileNotFoundException e3) {
                    Logger.getLogger(C0338f.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e3);
                    com.efiAnalytics.ui.bV.d("File Not Found!", frame);
                    return;
                } catch (IOException e4) {
                    Logger.getLogger(C0338f.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e4);
                    com.efiAnalytics.ui.bV.d("Unable to read file.", frame);
                    return;
                }
            }
            aN.e eVar = new aN.e(frame);
            eVar.setSize(com.efiAnalytics.ui.eJ.a(1000), com.efiAnalytics.ui.eJ.a(600));
            com.efiAnalytics.ui.bV.a((Component) frame, (Component) eVar);
            eVar.setVisible(true);
            eVar.a(rC);
        }
    }

    public void c(boolean z2) {
        this.f3341i = z2;
    }

    public boolean I() {
        G.R rC = G.T.a().c();
        return rC != null && (rC.C() instanceof bQ.l) && ((bQ.l) rC.C()).h();
    }

    public void J() {
        int i2 = 0;
        for (String str : G.T.a().d()) {
            G.R rC = G.T.a().c(str);
            if (C1806i.a().a("98fg54lklk") && !I() && !N.a.a(rC).e() && N.a.a(rC).b() == 0 && G.T.a().c() != null) {
                if (1 != 0) {
                    B.m mVar = new B.m(rC);
                    try {
                        int i3 = i2;
                        i2++;
                        mVar.a(B.m.f179k, Integer.valueOf(21848 + i3));
                    } catch (A.s e2) {
                        Logger.getLogger(C0338f.class.getName()).log(Level.WARNING, "Failed to set ECU Hub Port on TCP", (Throwable) e2);
                    }
                    bH.C.d("Starting TCP Slave Server");
                    N.a.a(rC).a(mVar);
                } else {
                    B.s sVar = new B.s(rC);
                    try {
                        int i4 = i2;
                        i2++;
                        sVar.a(B.m.f179k, Integer.valueOf(21848 + i4));
                    } catch (A.s e3) {
                        Logger.getLogger(C0338f.class.getName()).log(Level.WARNING, "Failed to set ECU Hub Port on UDP", (Throwable) e3);
                    }
                    N.a.a(rC).a(sVar);
                    bH.C.d("Starting UDP Slave Server");
                }
            }
            N.a.a(rC).a();
        }
    }

    public void K() {
        if (C1806i.a().a("98fg54lklk")) {
            N.a.c();
        }
    }

    public void d(boolean z2) {
        C1798a.a().b(C1798a.ci, Boolean.toString(z2));
        bt.bF.a().a(z2);
    }

    public void L() {
        new C0461q(this).start();
    }

    public void n(Window window) {
        new aL.a(new ju(), C1818g.d()).a(window);
    }

    public void M() {
        if (Desktop.isDesktopSupported()) {
            try {
                if (aE.a.A() != null) {
                    Desktop.getDesktop().open(new File(aE.a.A().t()));
                } else {
                    Desktop.getDesktop().open(new File(C1807j.u()));
                }
            } catch (IOException e2) {
                Logger.getLogger(C0338f.class.getName()).log(Level.INFO, "Failed to open Project folder", (Throwable) e2);
            }
        }
    }

    public void N() {
        if (C1806i.a().a("poij  fdsz poi9ure895 ms7(")) {
            try {
                new p.J(p.z.a(), com.efiAnalytics.ui.bV.a()).a(cZ.a().c());
            } catch (V.a e2) {
                com.efiAnalytics.ui.bV.d(C1818g.b(e2.getLocalizedMessage()), cZ.a().c());
            }
        }
    }

    public void O() {
        if (C1806i.a().a("poij  fdsz poi9ure895 ms7(")) {
            try {
                new C1775a(p.x.a(), p.z.a(), com.efiAnalytics.ui.bV.a()).a(cZ.a().c());
            } catch (V.a e2) {
                com.efiAnalytics.ui.bV.d(C1818g.b(e2.getLocalizedMessage()), cZ.a().c());
            }
        }
    }

    public void P() {
        if (!Desktop.isDesktopSupported() || aE.a.A() == null) {
            return;
        }
        try {
            Desktop.getDesktop().open(aE.a.A().L());
        } catch (IOException e2) {
            Logger.getLogger(C0338f.class.getName()).log(Level.INFO, "Failed to open Data Log folder", (Throwable) e2);
        }
    }

    public void Q() {
        if (C1806i.a().a("kjlkgoi098")) {
            cZ.a().m().g(gW.f3470u);
        } else {
            cZ.a().m().setSelectedIndex(0);
        }
    }

    public void R() {
        if (cZ.a().m() != null) {
            cZ.a().m().setSelectedIndex(0);
        }
    }

    public void S() {
        if (G.T.a().c() == null) {
            com.efiAnalytics.ui.bV.d("No EcuConfiguration Loaded!", com.efiAnalytics.ui.bV.c());
        }
        new C1121a(G.T.a().c()).a(com.efiAnalytics.ui.bV.c());
    }

    public void i(String str) {
        C1370a.a(cZ.a().c(), str);
    }

    public void T() {
        C0465u c0465u = new C0465u(this);
        c0465u.b("userParameter_Timeout in Minutes", C1798a.a().c(C1798a.f13325ag, C1798a.f13326ah));
        String strA = com.efiAnalytics.ui.bV.a("{Timeout in Minutes}", true, "Once you enter a password for a password protected dialog\nit will be cached for the set number of minutes without\nthe need to re-enter for any dialog using the same password.\n\nNote: The Cache is cleared when closing the project.", true, (Component) cZ.a().c(), (com.efiAnalytics.ui.fw) c0465u, (com.efiAnalytics.ui.fx) null);
        if (strA == null || strA.isEmpty()) {
            return;
        }
        try {
            int i2 = Integer.parseInt(strA);
            C1798a.a().b(C1798a.f13325ag, strA);
            com.efiAnalytics.tuningwidgets.panels.W.a(i2);
        } catch (NumberFormatException e2) {
            com.efiAnalytics.ui.bV.d("Time must be a valid integer.", cZ.a().c());
        }
    }

    public void a(G.R r2, C0102ch c0102ch) throws IOException {
        C0130m c0130mB = C0130m.b(r2.O(), c0102ch.a(), c0102ch.b(), c0102ch.c());
        C0116cv c0116cv = new C0116cv(r2);
        c0116cv.b(c0102ch.b());
        c0130mB.b(c0116cv);
        r2.C().b(c0130mB);
    }

    public void a(G.R r2, int i2) throws IOException {
        C0130m c0130mD = C0130m.d(r2.O(), i2);
        c0130mD.b(new C0116cv(r2));
        r2.C().b(c0130mD);
    }
}
