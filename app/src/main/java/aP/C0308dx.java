package aP;

import G.C0050aj;
import G.C0072be;
import G.C0076bi;
import G.C0079bl;
import G.C0088bu;
import G.C0113cs;
import G.InterfaceC0042ab;
import G.InterfaceC0109co;
import bH.C1018z;
import bl.C1190l;
import bq.C1220b;
import c.InterfaceC1386e;
import com.efiAnalytics.plugin.ApplicationPlugin;
import com.efiAnalytics.ui.BinTableView;
import com.efiAnalytics.ui.InterfaceC1581bs;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.Window;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javax.swing.Action;
import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.ToolTipManager;
import javax.swing.UIManager;
import r.C1798a;
import r.C1806i;
import s.C1816e;
import s.C1818g;
import s.InterfaceC1817f;
import sun.security.tools.policytool.ToolWindow;
import x.C1891a;

/* renamed from: aP.dx, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:aP/dx.class */
public final class C0308dx implements G.S, G.aG, InterfaceC0042ab, G.bT, aE.e {

    /* renamed from: c, reason: collision with root package name */
    C0338f f3264c;

    /* renamed from: h, reason: collision with root package name */
    JFrame f3270h;

    /* renamed from: m, reason: collision with root package name */
    gO f3275m;

    /* renamed from: a, reason: collision with root package name */
    public static String f3261a = C1798a.a().a(C1798a.dl, "Toolbar Style");

    /* renamed from: b, reason: collision with root package name */
    public static String f3263b = "Help";

    /* renamed from: p, reason: collision with root package name */
    private String f3262p = f3261a;

    /* renamed from: d, reason: collision with root package name */
    G.R f3265d = null;

    /* renamed from: q, reason: collision with root package name */
    private gL f3266q = new gL(this);

    /* renamed from: e, reason: collision with root package name */
    bA.d f3267e = null;

    /* renamed from: f, reason: collision with root package name */
    R f3268f = new R();

    /* renamed from: g, reason: collision with root package name */
    int f3269g = 3;

    /* renamed from: i, reason: collision with root package name */
    String[] f3271i = {"resources/cog.png", "resources/wrench2.png", "resources/tools3.png", "resources/tools2.png", "resources/connecting_rod_32.png"};

    /* renamed from: j, reason: collision with root package name */
    int f3272j = 0;

    /* renamed from: k, reason: collision with root package name */
    List f3273k = new ArrayList();

    /* renamed from: l, reason: collision with root package name */
    List f3274l = new ArrayList();

    /* renamed from: n, reason: collision with root package name */
    InterfaceC0109co f3276n = new C0309dy(this);

    /* renamed from: o, reason: collision with root package name */
    InterfaceC1386e f3277o = new eO(this);

    public C0308dx(JFrame jFrame, C0338f c0338f) {
        this.f3264c = null;
        this.f3270h = null;
        this.f3264c = c0338f;
        this.f3270h = jFrame;
        jFrame.setJMenuBar(this.f3266q);
        n();
        b(C1798a.a().c("navigationStyle", f3261a));
        ToolTipManager.sharedInstance().setDismissDelay(12000);
    }

    private void n() {
        if (C1806i.a().a("09fewlkm309glkfds09")) {
            this.f3266q.add((JMenu) f());
        }
        if (!C1798a.a().a(C1798a.cU, C1798a.cV) && !C1806i.a().a("-rewqjmgdlijyre")) {
            this.f3266q.add((JMenu) h());
        }
        if (!C1806i.a().a("ewq-0rfdrewewr")) {
            this.f3266q.add((JMenu) g());
        }
        if (!C1806i.a().a(";lgd;lgdhf[p")) {
            this.f3266q.add((JMenu) i());
        }
        if (!C1806i.a().a("w-0-[-egd;ls")) {
            this.f3266q.add((JMenu) j());
        }
        this.f3266q.add((JMenu) k());
        if (UIManager.getLookAndFeel().getName().contains("OS X") || !C1806i.a().a("432p'[pgd-0[p")) {
            return;
        }
        com.efiAnalytics.tunerStudio.search.l lVar = new com.efiAnalytics.tunerStudio.search.l();
        cZ.a().a(lVar);
        this.f3266q.add(lVar);
    }

    public void b(String str) {
        if (str.equals("Wrapping Main Menu") && C1798a.a().c("lookAndFeelClass", "").contains("TinyLookAndFeel")) {
            str = "Main Menu Style";
        }
        if (str.equals("MegaTune Style")) {
            str = "Main Menu Style";
        }
        if (str.equals(this.f3262p) && this.f3267e != null) {
            bH.C.c("updateMenuStyle ignoring, already set to " + str);
        } else {
            this.f3262p = str;
            c();
        }
    }

    public void c() {
        this.f3272j = 0;
        if (this.f3267e != null) {
            o();
        }
        if (this.f3262p.equals("Main Menu Style")) {
            if (this.f3267e != null && !this.f3267e.equals(this.f3266q)) {
                this.f3270h.remove(this.f3267e.getComponent());
            }
            this.f3267e = this.f3266q;
            this.f3269g = 3;
        } else if (this.f3262p.equals("Wrapping Main Menu")) {
            if (this.f3267e != null && !this.f3267e.equals(this.f3266q)) {
                this.f3270h.remove(this.f3267e.getComponent());
            }
            this.f3267e = this.f3266q;
            this.f3269g = 3;
        } else {
            String strC = C1798a.a().c("tuningToolbarLocation", "North");
            gU gUVar = new gU(this);
            gUVar.setOrientation(strC.equals("North") ? 0 : 1);
            this.f3267e = gUVar;
            this.f3269g = 0;
            this.f3270h.add(strC, this.f3267e.getComponent());
        }
        q();
        l();
    }

    public String d() {
        return this.f3262p;
    }

    private void o() {
        if (this.f3275m != null) {
            this.f3275m.b();
        }
        this.f3266q.removeAll();
        for (String str : this.f3267e.a().b()) {
            for (int iA = this.f3267e.a(str) - 1; iA >= 0; iA--) {
                bA.f fVarA = this.f3267e.a(str, iA);
                this.f3267e.a(fVarA.getComponent());
                fVarA.f();
            }
        }
        n();
    }

    private void p() {
        String strB = C1818g.b("Upgrade!");
        for (String str : G.T.a().d()) {
            if (a(str, strB) == null) {
                bA.f fVarA = a(C1818g.b(strB), false);
                fVarA.c("resources/upgrade24.png");
                fVarA.a(new eZ(this));
                this.f3267e.a(str, fVarA.getComponent(), d(str));
            }
        }
    }

    private void q() {
        if (this.f3265d == null) {
            return;
        }
        String[] strArrD = G.T.a().d();
        this.f3267e.a().a();
        for (int i2 = 0; i2 < strArrD.length; i2++) {
            G.R rC = G.T.a().c(strArrD[i2]);
            gA gAVar = new gA(this, rC);
            Iterator itB = rC.e().b();
            while (itB.hasNext()) {
                G.aA aAVar = (G.aA) itB.next();
                bA.f fVarA = a(strArrD[i2], C1818g.b(aAVar.e()));
                if (fVarA == null) {
                    String strB = C1818g.b(aAVar.h());
                    if (strB.equals(aAVar.h())) {
                        strB = aAVar.e();
                    }
                    fVarA = a(strB, false);
                    try {
                        fVarA.c(c(aAVar.e()));
                    } catch (Exception e2) {
                        bH.C.a("Failed to get icon image.");
                        e2.printStackTrace();
                    }
                    if (aAVar.aH() != null && !aAVar.aH().equals("")) {
                        fVarA.b(aAVar.aH());
                        if (fVarA instanceof InterfaceC1581bs) {
                            ((InterfaceC1581bs) fVarA).a(new gN(this, aAVar.aH(), rC));
                        }
                    }
                    if (aAVar.i() != null && !aAVar.i().equals("")) {
                        fVarA.b(new gN(this, aAVar.i(), rC));
                    }
                    if (aAVar.g() != null && !aAVar.g().equals("")) {
                        fVarA.setMnemonic(aAVar.g().toUpperCase().charAt(0));
                    }
                    this.f3267e.a(strArrD[i2], fVarA.getComponent(), d(strArrD[i2]));
                }
                fVarA.a(gAVar);
                a(rC, aAVar, fVarA, strArrD.length > 1 ? strArrD[i2] : null);
            }
        }
        if (this.f3267e.b() > 0 && C1806i.a().a("pokrepopoktrg9034")) {
            p();
        }
        this.f3267e.a().b(this.f3265d.c());
        e();
        RunnableC0349fk runnableC0349fk = new RunnableC0349fk(this);
        try {
            if (SwingUtilities.isEventDispatchThread()) {
                runnableC0349fk.run();
            } else {
                SwingUtilities.invokeAndWait(runnableC0349fk);
            }
        } catch (Exception e3) {
            Logger.getLogger(C0308dx.class.getName()).log(Level.SEVERE, "Failed to invoke menu topdown start", (Throwable) e3);
        }
    }

    private void a(G.R r2, bA.f fVar, G.aA aAVar, String str) {
        String strA = C1818g.a(aAVar.h(), aAVar.e());
        gS gSVar = new gS(this, (str == null || str.length() <= 0 || !fVar.h()) ? strA : strA + " (" + str + ")", false);
        Icon iconA = a(aAVar);
        if (iconA != null) {
            gSVar.setIcon(iconA);
        }
        gSVar.setActionCommand(r2.c() + "." + aAVar.d());
        gSVar.setName("" + aAVar.f());
        if (aAVar.i() != null && !aAVar.i().equals("")) {
            gSVar.b(new gN(this, aAVar.i(), r2));
        }
        if (aAVar.aH() != null && !aAVar.aH().equals("")) {
            gSVar.a(new gN(this, aAVar.aH(), r2));
            if (C1798a.a().c(C1798a.f13382bl, C1798a.f13383bm)) {
                gSVar.setToolTipText("<html><body>" + C1818g.b("Enabled when") + ": " + aAVar.aH() + "<br><br>" + C1818g.b("Friendly") + ": " + bH.W.a(G.bL.e(r2, aAVar.aH()), 120, "<br>&nbsp;&nbsp;&nbsp;") + "</body></html>");
            }
        }
        if (aAVar.g() != null && !aAVar.g().equals("")) {
            gSVar.setMnemonic(aAVar.g().charAt(0));
        }
        if (fVar.getText().equals(f3263b)) {
            gSVar.addActionListener(new gE(this, r2));
        } else if (!aAVar.b()) {
            gSVar.addActionListener(new C0360fv(this));
        }
        fVar.add(gSVar);
    }

    private bA.f a(G.R r2, G.aA aAVar, bA.f fVar, String str) {
        Iterator itA = aAVar.a();
        while (itA.hasNext()) {
            G.aA aAVar2 = (G.aA) itA.next();
            if (aAVar2.c()) {
                fVar.addSeparator();
            } else if (aAVar2.b()) {
                String strA = C1818g.a(aAVar2.h(), aAVar2.e());
                gR gRVar = new gR(this, (str == null || str.length() <= 0) ? strA : strA + " (" + str + ")", false);
                Icon iconA = a(aAVar2);
                if (iconA != null) {
                    gRVar.setIcon(iconA);
                }
                if (aAVar2.i() != null && !aAVar2.i().equals("")) {
                    gRVar.b(new gN(this, aAVar2.i(), r2));
                }
                if (aAVar2.aH() != null && !aAVar2.aH().equals("")) {
                    gRVar.a(new gN(this, aAVar2.aH(), r2));
                    if (C1806i.a().a("lk098oijrepoijrgds98ugoi") && C1798a.a().c(C1798a.f13382bl, C1798a.f13383bm)) {
                        gRVar.setToolTipText("<html><body>" + C1818g.b("Enabled when") + ": " + aAVar2.aH() + "<br><br>" + C1818g.b("Friendly") + ": " + bH.W.a(G.bL.e(r2, aAVar2.aH()), 120, "<br>&nbsp;&nbsp;&nbsp;") + "</body></html>");
                    }
                }
                Iterator itA2 = aAVar2.a();
                while (itA2.hasNext()) {
                    G.aA aAVar3 = (G.aA) itA2.next();
                    if (aAVar3.c()) {
                        gRVar.addSeparator();
                    } else {
                        a(r2, gRVar, aAVar3, str);
                    }
                }
                fVar.add(gRVar);
            } else {
                a(r2, fVar, aAVar2, str);
            }
        }
        return fVar;
    }

    private int d(String str) {
        return this.f3267e.a(str) - this.f3269g;
    }

    private Icon a(G.aA aAVar) {
        G.R rC = G.T.a().c();
        C0088bu c0088buC = rC.e().c(aAVar.d());
        return aAVar.b() ? new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource("resources/folder16.png"))) : ((c0088buC instanceof C0072be) || (c0088buC != null && c0088buC.S())) ? new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource("resources/table.png"))) : ((c0088buC instanceof C0079bl) || (c0088buC != null && c0088buC.T())) ? new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource("resources/curve.png"))) : rC.e().b(aAVar.d()) instanceof C0050aj ? new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource("resources/help16.gif"))) : c0088buC instanceof C0076bi ? new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource("resources/table3d.png"))) : new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource("resources/settings.gif")));
    }

    public bA.f a(String str, String str2) {
        for (int i2 = 0; i2 < this.f3266q.getMenuCount(); i2++) {
            bA.f fVar = (bA.f) this.f3266q.getMenu(i2);
            if (fVar instanceof InterfaceC1817f) {
                if (((InterfaceC1817f) fVar).a().equals(str2)) {
                    return fVar;
                }
            } else if (fVar != null && fVar.getText().equals(str2)) {
                return fVar;
            }
        }
        for (int i3 = 0; i3 < this.f3267e.a(str); i3++) {
            bA.f fVarA = this.f3267e.a(str, i3);
            if (fVarA instanceof InterfaceC1817f) {
                if (((InterfaceC1817f) fVarA).a().equals(str2)) {
                    return fVarA;
                }
            } else if (fVarA != null && fVarA.getText().equals(str2)) {
                return fVarA;
            }
        }
        return null;
    }

    public bA.f e() {
        int iA = C1798a.a().a(C1798a.f13352aH, C1798a.a().o());
        Font font = this.f3266q.getFont();
        Font font2 = new Font(font.getFamily(), font.getStyle(), iA);
        for (String str : G.T.a().d()) {
            for (int i2 = 0; i2 < this.f3267e.a(str); i2++) {
                Object objA = this.f3267e.a(str, i2);
                if (objA instanceof Component) {
                    ((Component) objA).setFont(font2);
                }
            }
        }
        return null;
    }

    public C1891a f() {
        gI gIVar = new gI(this, "File", true);
        gIVar.setMnemonic('F');
        fH fHVar = new fH(this);
        gR gRVar = new gR(this, "Vehicle Projects", true);
        gRVar.a(this.f3277o);
        if (!C1806i.a().a("4320432porepo09")) {
            gS gSVar = new gS(this, "New Project", true);
            gSVar.setAccelerator(KeyStroke.getKeyStroke(78, 8));
            gSVar.addActionListener(new fS(this));
            gRVar.add((JMenuItem) gSVar);
        }
        gS gSVar2 = new gS(this, "Open Project", true);
        gSVar2.setAccelerator(KeyStroke.getKeyStroke(79, 8));
        gSVar2.addActionListener(new C0369gd(this));
        gRVar.add((JMenuItem) gSVar2);
        gS gSVar3 = new gS(this, "Close Project", true);
        gSVar3.setAccelerator(KeyStroke.getKeyStroke(88, 8));
        gSVar3.a(this.f3277o);
        gSVar3.addActionListener(new C0380go(this));
        gRVar.add((JMenuItem) gSVar3);
        gRVar.addSeparator();
        gS gSVar4 = new gS(this, "Create Project Backup", true);
        gSVar4.addActionListener(new C0310dz(this));
        gRVar.add((JMenuItem) gSVar4);
        gS gSVar5 = new gS(this, "Import Project Backup", true);
        gSVar5.addActionListener(new dK(this));
        gSVar5.a(new dV(this));
        gRVar.add((JMenuItem) gSVar5);
        if (Desktop.isDesktopSupported()) {
            gS gSVar6 = new gS(this, "Show Project Folder", true);
            gSVar6.addActionListener(new C0318eg(this));
            gRVar.add((JMenuItem) gSVar6);
        }
        gRVar.addSeparator();
        gS gSVar7 = new gS(this, "Project Properties", true);
        gSVar7.setAccelerator(KeyStroke.getKeyStroke(80, 2));
        gSVar7.a(this.f3277o);
        gSVar7.addActionListener(new C0329er(this));
        gRVar.add((JMenuItem) gSVar7);
        gIVar.add((JMenuItem) gRVar);
        if (!C1806i.a().a("4320432porepo09")) {
            gS gSVar8 = new gS(this, "New Project", true);
            gSVar8.setAccelerator(KeyStroke.getKeyStroke(78, 8));
            gSVar8.a(fHVar);
            gSVar8.addActionListener(new eC(this));
            gIVar.add((JMenuItem) gSVar8);
        }
        if (!C1806i.a().a("h-0ewkfd[pfd[pew")) {
            gIVar.add(new gJ(this));
        }
        gS gSVar9 = new gS(this, "Open Project", true);
        gSVar9.setAccelerator(KeyStroke.getKeyStroke(79, 8));
        gSVar9.a(fHVar);
        gSVar9.addActionListener(new eK(this));
        gIVar.add((JMenuItem) gSVar9);
        gS gSVar10 = new gS(this, "Import Project Backup", true);
        gSVar10.a(fHVar);
        gSVar10.addActionListener(new eL(this));
        gIVar.add((JMenuItem) gSVar10);
        gSVar10.setEnabled(C1806i.a().a("09RGDKDG;LKIGD"));
        gIVar.addSeparator();
        gS gSVar11 = new gS(this, "Tune Restore Points", true);
        gSVar11.setAccelerator(KeyStroke.getKeyStroke(82, 2));
        gSVar11.addActionListener(new eM(this));
        gIVar.add((JMenuItem) gSVar11);
        if (C1806i.a().a("-=fds[pfds[pgd-0")) {
            gSVar11.a(this.f3277o);
        } else {
            gSVar11.setEnabled(false);
        }
        JMenuItem gSVar12 = new gS(this, C1818g.b("Load Tune") + " (" + C1798a.cw + ")", true);
        gSVar12.setAccelerator(KeyStroke.getKeyStroke(79, 2));
        gSVar12.addActionListener(new eN(this));
        gIVar.add(gSVar12);
        gS gSVar13 = new gS(this, "Save Tune", true);
        gSVar13.setAccelerator(KeyStroke.getKeyStroke(83, 2));
        gSVar13.a(this.f3277o);
        gSVar13.addActionListener(new eP(this));
        gIVar.add((JMenuItem) gSVar13);
        gS gSVar14 = new gS(this, "Save Tune As", true);
        gSVar14.a(this.f3277o);
        gSVar14.addActionListener(new eQ(this));
        gIVar.add((JMenuItem) gSVar14);
        if (C1806i.a().a("67r67r8yhdrtrbyuk")) {
            gS gSVar15 = new gS(this, "Compare Tune", true);
            gSVar15.setAccelerator(KeyStroke.getKeyStroke(77, 2));
            gSVar15.a(new eR(this));
            gSVar15.addActionListener(new eS(this));
            gIVar.add((JMenuItem) gSVar15);
        }
        if (!C1798a.a().a(C1798a.da, C1798a.db)) {
            gD gDVar = new gD(this, "Gauge Cluster");
            gDVar.a(new eT(this));
            gIVar.add((JMenuItem) gDVar);
        }
        if (C1806i.a().a(";LFDS;LFDS0943;L")) {
            gT gTVar = new gT(this, "Tuning Views");
            gTVar.a(new eU(this));
            gIVar.add((JMenuItem) gTVar);
        }
        eV eVVar = new eV(this);
        gQ gQVar = new gQ(this, "Work Offline", false, false);
        gQVar.setAccelerator(KeyStroke.getKeyStroke(87, 2));
        gQVar.a(eVVar);
        gQVar.a(new eW(this));
        gQVar.addActionListener(new eX(this));
        gIVar.add((JMenuItem) gQVar);
        gIVar.addSeparator();
        JMenuItem gSVar16 = new gS(this, ToolWindow.QUIT, true);
        gSVar16.addActionListener(new eY(this));
        gIVar.add(gSVar16);
        return gIVar;
    }

    public C1891a g() {
        gR gRVar = new gR(this, "Data Logging", true);
        if (this.f3262p.equals("Wrapping Main Menu")) {
            gRVar.b(true);
        }
        gRVar.a(this.f3277o);
        gRVar.setMnemonic('D');
        boolean zA = C1806i.a().a(";'rew-043;lh/lhoi");
        gS gSVar = new gS(this, "Start Logging", true);
        gSVar.setAccelerator(KeyStroke.getKeyStroke(76, 2));
        gSVar.a(new C0339fa(this));
        gSVar.setEnabled(zA);
        gSVar.addActionListener(new C0340fb(this));
        gRVar.add((JMenuItem) gSVar);
        gS gSVar2 = new gS(this, "Stop", true);
        gSVar2.setAccelerator(KeyStroke.getKeyStroke(75, 2));
        gSVar2.a(new C0341fc(this));
        gSVar2.addActionListener(new C0342fd(this));
        gRVar.add((JMenuItem) gSVar2);
        gS gSVar3 = new gS(this, "Logging Profiles", true);
        if (C1806i.a().a("fg;';'[PD;LSAG")) {
            gSVar3.a(this.f3277o);
            gSVar3.addActionListener(new C0343fe(this));
        } else {
            gSVar3.setEnabled(false);
        }
        gRVar.add((JMenuItem) gSVar3);
        gS gSVar4 = new gS(this, "Triggered Logging", true);
        if (C1806i.a().a("FDSDSA-0;L;l0") || C1806i.a().a("sa0-0o0os-0o-0DS")) {
            gSVar4.a(this.f3277o);
            gSVar4.addActionListener(new C0344ff(this));
        } else {
            gSVar4.setEnabled(false);
        }
        gRVar.add((JMenuItem) gSVar4);
        gRVar.addSeparator();
        C1891a c1891a = new C1891a(C1818g.b("Data Logging Preferences"));
        C1891a c1891a2 = new C1891a(C1818g.b("Data Log Format"));
        ButtonGroup buttonGroup = new ButtonGroup();
        String strC = C1798a.a().c(C1798a.cA, C1798a.cB);
        JCheckBoxMenuItem jCheckBoxMenuItem = new JCheckBoxMenuItem(C1818g.b("ASCII Format") + " (" + C1798a.cs + ")");
        jCheckBoxMenuItem.setSelected(strC.equals(C1798a.cs));
        jCheckBoxMenuItem.addActionListener(new C0345fg(this));
        c1891a2.add((JMenuItem) jCheckBoxMenuItem);
        buttonGroup.add(jCheckBoxMenuItem);
        JCheckBoxMenuItem jCheckBoxMenuItem2 = new JCheckBoxMenuItem(C1818g.b("Enhanced Logging Format") + " (" + C1798a.cr + ")");
        jCheckBoxMenuItem2.setSelected(strC.equals(C1798a.cr));
        jCheckBoxMenuItem2.addActionListener(new C0346fh(this));
        c1891a2.add((JMenuItem) jCheckBoxMenuItem2);
        buttonGroup.add(jCheckBoxMenuItem2);
        c1891a.add((JMenuItem) c1891a2);
        gRVar.add((JMenuItem) c1891a);
        if (C1806i.a().a("8754JUREJYFD87")) {
            JMenu jMenu = new JMenu("Data Log Naming");
            String strC2 = C1798a.a().c(C1798a.ce, C1798a.cf);
            ButtonGroup buttonGroup2 = new ButtonGroup();
            JCheckBoxMenuItem jCheckBoxMenuItem3 = new JCheckBoxMenuItem(C1818g.b("Name Data Logs On Stop"), strC2.equals(C1798a.f13420bX));
            jCheckBoxMenuItem3.addActionListener(new C0347fi(this));
            buttonGroup2.add(jCheckBoxMenuItem3);
            jMenu.add((JMenuItem) jCheckBoxMenuItem3);
            JCheckBoxMenuItem jCheckBoxMenuItem4 = new JCheckBoxMenuItem(C1818g.b("Name Data Logs On Start"), strC2.equals(C1798a.bY));
            jCheckBoxMenuItem4.addActionListener(new C0348fj(this));
            buttonGroup2.add(jCheckBoxMenuItem4);
            jMenu.add((JMenuItem) jCheckBoxMenuItem4);
            JCheckBoxMenuItem jCheckBoxMenuItem5 = new JCheckBoxMenuItem(C1818g.b("Silently Auto Name Log Files"), strC2.equals(C1798a.bZ));
            jCheckBoxMenuItem5.addActionListener(new C0350fl(this));
            buttonGroup2.add(jCheckBoxMenuItem5);
            jMenu.add((JMenuItem) jCheckBoxMenuItem5);
            c1891a.add((JMenuItem) jMenu);
            bA.c cVar = new bA.c(C1818g.b("Show Rename Dialog on SD Download"), true, C1798a.a().c(C1798a.ca, C1798a.cb));
            cVar.a(new C0351fm(this));
            c1891a.add((JMenuItem) cVar);
            if (C1806i.a().a(" 09s98r32-po3q9264")) {
                bA.c cVar2 = new bA.c(C1818g.b("Save Tune to Data Log"), true, C1798a.a().c(C1798a.f13323ae, C1798a.f13324af));
                cVar2.a(new C0352fn(this));
                cVar2.addActionListener(new C0353fo(this));
                cVar2.a(new C0354fp(this));
                cVar2.setToolTipText(C1818g.b("Enhanced Logging Format") + " (" + C1798a.cr + ") " + C1818g.b("Required"));
                c1891a.add((JMenuItem) cVar2);
            }
        }
        if (C1806i.a().a("sa0-0o0os-0o-0DS")) {
            gS gSVar5 = new gS(this, "Re ARM Logging", true);
            gSVar5.addActionListener(new C0355fq(this));
            gSVar5.setAccelerator(KeyStroke.getKeyStroke(65, 2));
            gSVar5.a(new C0356fr(this));
            gRVar.add((JMenuItem) gSVar5);
        }
        if (!C1798a.a().a(C1798a.cY, C1798a.cZ)) {
            gRVar.addSeparator();
            gR gRVar2 = new gR(this, "Import / Conversion");
            gS gSVar6 = new gS(this, "PalmLog Import Utility", true);
            gSVar6.a(this.f3277o);
            gSVar6.addActionListener(new C0357fs(this));
            gRVar2.add((JMenuItem) gSVar6);
            gS gSVar7 = new gS(this, C1798a.a().a(C1798a.dn, "Convert Binary Log (MS3, FRD)"), true);
            gSVar7.a(this.f3277o);
            gSVar7.addActionListener(new C0358ft(this));
            gRVar2.add((JMenuItem) gSVar7);
            gRVar.add((JMenuItem) gRVar2);
        } else if (C1806i.a().a("098532oiutewlkjg098")) {
            gS gSVar8 = new gS(this, C1798a.a().a(C1798a.dn, "Convert Binary Log (MS3, FRD)"), true);
            gSVar8.a(this.f3277o);
            gSVar8.addActionListener(new C0359fu(this));
            gRVar.add((JMenuItem) gSVar8);
        }
        if (C1806i.a().a("6509h;l;lhfoi")) {
            JMenuItem gSVar9 = new gS(this, "Audio Recording Configuration", true);
            gSVar9.addActionListener(new C0361fw(this));
            gRVar.add(gSVar9);
        }
        JMenuItem gSVar10 = new gS(this, "View with MegaLogViewer", true);
        gSVar10.setAccelerator(KeyStroke.getKeyStroke(86, 8));
        gSVar10.addMouseListener(new C0362fx(this));
        gRVar.add(gSVar10);
        if (Desktop.isDesktopSupported()) {
            gS gSVar11 = new gS(this, "Show DataLog Folder", true);
            gSVar11.setAccelerator(KeyStroke.getKeyStroke(76, 8));
            gSVar11.addActionListener(new C0364fz(this));
            gSVar11.a(this.f3277o);
            gRVar.add((JMenuItem) gSVar11);
        }
        return gRVar;
    }

    public C1891a h() {
        gR gRVar = new gR(this, "Options", true);
        gRVar.setMnemonic('P');
        boolean zA = C1806i.a().a(";';';'0-=pfpdpd");
        if (!C1806i.a().a("r-0gds-=-=fd43;ds")) {
            gR gRVar2 = new gR(this, "Language", true);
            ArrayList arrayListB = C1818g.b();
            ButtonGroup buttonGroup = new ButtonGroup();
            Iterator it = arrayListB.iterator();
            while (it.hasNext()) {
                C1816e c1816e = (C1816e) it.next();
                String strB = c1816e.b();
                if (c1816e.a().equals("en")) {
                    strB = strB + " (Default)";
                }
                JCheckBoxMenuItem jCheckBoxMenuItem = new JCheckBoxMenuItem(strB, true);
                buttonGroup.add(jCheckBoxMenuItem);
                jCheckBoxMenuItem.setActionCommand(c1816e.a());
                jCheckBoxMenuItem.setSelected(c1816e.a().equals(C1798a.a().c("viewLanguageCode", "en")));
                jCheckBoxMenuItem.addActionListener(new fA(this));
                gRVar2.add((JMenuItem) jCheckBoxMenuItem);
            }
            gRVar.add(gRVar2.getComponent());
        }
        gR gRVar3 = new gR(this, "Look and Feel", true);
        UIManager.LookAndFeelInfo[] installedLookAndFeels = UIManager.getInstalledLookAndFeels();
        ButtonGroup buttonGroup2 = new ButtonGroup();
        for (UIManager.LookAndFeelInfo lookAndFeelInfo : installedLookAndFeels) {
            String name = lookAndFeelInfo.getName();
            String strC = C1798a.a().c("defaultLookAndFeelClass", UIManager.getCrossPlatformLookAndFeelClassName());
            if (lookAndFeelInfo.getClassName().equals(strC)) {
                name = name + " (" + C1818g.b(Action.DEFAULT) + ")";
            }
            if (!name.equals("Windows Classic") && !name.startsWith("TinyLookAndFeel")) {
                JCheckBoxMenuItem jCheckBoxMenuItem2 = new JCheckBoxMenuItem(name, true);
                buttonGroup2.add(jCheckBoxMenuItem2);
                jCheckBoxMenuItem2.setActionCommand(lookAndFeelInfo.getClassName());
                jCheckBoxMenuItem2.setSelected(lookAndFeelInfo.getClassName().equals(C1798a.a().c("lookAndFeelClass", strC)));
                jCheckBoxMenuItem2.addActionListener(new fB(this));
                gRVar3.add((JMenuItem) jCheckBoxMenuItem2);
            }
        }
        if (C1806i.a().a("FS-0FDS;L4")) {
            gRVar.add(gRVar3.getComponent());
        }
        if (!zA) {
            gR gRVar4 = new gR(this, "Navigation", true);
            ButtonGroup buttonGroup3 = new ButtonGroup();
            JCheckBoxMenuItem jCheckBoxMenuItem3 = new JCheckBoxMenuItem(C1818g.b("Main Menu Style"));
            jCheckBoxMenuItem3.setActionCommand("Main Menu Style");
            jCheckBoxMenuItem3.setState(C1798a.a().c("navigationStyle", f3261a).equals("Main Menu Style"));
            buttonGroup3.add(jCheckBoxMenuItem3);
            jCheckBoxMenuItem3.addActionListener(new fC(this));
            gRVar4.add((JMenuItem) jCheckBoxMenuItem3);
            if (!C1798a.a().c("lookAndFeelClass", "").contains("TinyLookAndFeel")) {
                JCheckBoxMenuItem jCheckBoxMenuItem4 = new JCheckBoxMenuItem(C1818g.b("Wrapping Main Menu"));
                jCheckBoxMenuItem4.setActionCommand("Wrapping Main Menu");
                jCheckBoxMenuItem4.setState(C1798a.a().c("navigationStyle", f3261a).equals("Wrapping Main Menu"));
                buttonGroup3.add(jCheckBoxMenuItem4);
                jCheckBoxMenuItem4.addActionListener(new fD(this));
                gRVar4.add((JMenuItem) jCheckBoxMenuItem4);
            }
            JCheckBoxMenuItem jCheckBoxMenuItem5 = new JCheckBoxMenuItem(C1818g.b("Toolbar Style"));
            jCheckBoxMenuItem5.setActionCommand("Toolbar Style");
            jCheckBoxMenuItem5.setState(C1798a.a().c("navigationStyle", f3261a).equals("Toolbar Style"));
            buttonGroup3.add(jCheckBoxMenuItem5);
            jCheckBoxMenuItem5.addActionListener(new fE(this));
            gRVar4.add((JMenuItem) jCheckBoxMenuItem5);
            gR gRVar5 = new gR(this, "Navigation options");
            boolean zC = C1798a.a().c(C1798a.f13378bh, true);
            JCheckBoxMenuItem jCheckBoxMenuItem6 = new JCheckBoxMenuItem(C1818g.b("Show Disabled Menu"));
            jCheckBoxMenuItem6.setState(zC);
            jCheckBoxMenuItem6.addActionListener(new fF(this));
            gRVar5.add((JMenuItem) jCheckBoxMenuItem6);
            gRVar4.add((JMenuItem) gRVar5);
            gRVar.add((JMenuItem) gRVar4);
            gR gRVar6 = new gR(this, "View");
            gQ gQVar = new gQ(this, "Show Gauges on VE Analyze", true, C1798a.a().c(C1798a.f13408bL, C1798a.f13409bM));
            gQVar.addActionListener(new fG(this));
            gQVar.a(new fI(this));
            gRVar6.add((JMenuItem) gQVar);
            gQ gQVar2 = new gQ(this, "Show Live Graphs on VE Analyze", true, C1798a.a().c(C1798a.f13410bN, C1798a.f13411bO));
            gQVar2.addActionListener(new fJ(this));
            gQVar2.a(new fK(this));
            gRVar6.add((JMenuItem) gQVar2);
            gRVar.add((JMenuItem) gRVar6);
            gQ gQVar3 = new gQ(this, "Show Enable Condition in Menu Tooltips", true, C1798a.a().c(C1798a.f13382bl, C1798a.f13383bm));
            gQVar3.addActionListener(new fL(this));
            gQVar3.a(new fM(this));
            gRVar6.add((JMenuItem) gQVar3);
            gRVar.add((JMenuItem) gRVar6);
            gR gRVar7 = new gR(this, "Advanced");
            JCheckBoxMenuItem jCheckBoxMenuItem7 = new JCheckBoxMenuItem(C1818g.b("Report INI Warnings"));
            jCheckBoxMenuItem7.addActionListener(new fN(this));
            jCheckBoxMenuItem7.setState(C1798a.a().c(C1798a.f13373bc, C1798a.f13374bd));
            gRVar7.add((JMenuItem) jCheckBoxMenuItem7);
            if (bH.I.a()) {
                JMenu jMenu = new JMenu(C1818g.b("Video driver work arounds"));
                ButtonGroup buttonGroup4 = new ButtonGroup();
                JCheckBoxMenuItem jCheckBoxMenuItem8 = new JCheckBoxMenuItem(C1818g.b("Force OpenGL Active"));
                jCheckBoxMenuItem8.addActionListener(new fO(this));
                boolean zC2 = C1798a.a().c(C1798a.f13298F, C1798a.f13299G);
                jCheckBoxMenuItem8.setState(zC2);
                buttonGroup4.add(jCheckBoxMenuItem8);
                jMenu.add((JMenuItem) jCheckBoxMenuItem8);
                JCheckBoxMenuItem jCheckBoxMenuItem9 = new JCheckBoxMenuItem(C1818g.b("Disable Direct 3D"));
                jCheckBoxMenuItem9.addActionListener(new fP(this));
                boolean zC3 = C1798a.a().c(C1798a.f13300H, C1798a.f13301I);
                jCheckBoxMenuItem9.setState(zC3);
                buttonGroup4.add(jCheckBoxMenuItem9);
                jMenu.add((JMenuItem) jCheckBoxMenuItem9);
                JCheckBoxMenuItem jCheckBoxMenuItem10 = new JCheckBoxMenuItem(C1818g.b("Java Default (D3D Enabled)"));
                jCheckBoxMenuItem10.addActionListener(new fQ(this));
                jCheckBoxMenuItem10.setState((zC2 || zC3) ? false : true);
                buttonGroup4.add(jCheckBoxMenuItem10);
                jMenu.add((JMenuItem) jCheckBoxMenuItem10);
                gRVar7.add((JMenuItem) jMenu);
            }
            JCheckBoxMenuItem jCheckBoxMenuItem11 = new JCheckBoxMenuItem(C1818g.b("Always Allow Multiple Instances"));
            jCheckBoxMenuItem11.addActionListener(new fR(this));
            jCheckBoxMenuItem11.setState(C1798a.a().c(C1798a.f13412bP, C1798a.f13413bQ));
            gRVar7.add((JMenuItem) jCheckBoxMenuItem11);
            if (C1806i.a().a("98fg54lklk")) {
                bA.c cVar = new bA.c(C1818g.b("Run Slave Server"), true, aE.a.A() != null && aE.a.A().Q());
                cVar.addActionListener(new fT(this));
                cVar.a(new fU(this));
                gRVar7.add((JMenuItem) cVar);
                cVar.a(this.f3277o);
            }
            gRVar.add((JMenuItem) gRVar7);
        }
        gR gRVar8 = new gR(this, "Preferences");
        if (C1806i.a().a(";'GDS0[p'pgd[p")) {
            JMenuItem jCheckBoxMenuItem12 = new JCheckBoxMenuItem(C1818g.b("Load Last Project on startup"), C1798a.a().c(C1798a.f13376bf, C1798a.cP));
            jCheckBoxMenuItem12.addActionListener(new fV(this));
            gRVar8.add(jCheckBoxMenuItem12);
            gQ gQVar4 = new gQ(this, "Make Dashboards Full Screen", true, C1798a.a().c(C1798a.f13377bg, false));
            gQVar4.addActionListener(new fW(this));
            gQVar4.a(new fX(this));
            gQVar4.a(new fY(this));
            gRVar8.add((JMenuItem) gQVar4);
        }
        gR gRVar9 = new gR(this, "Settings Dialog Font Size");
        int iA = C1798a.a().a(C1798a.f13352aH, C1798a.a().o());
        C0391gz c0391gz = new C0391gz(this);
        int iO = C1798a.a().o();
        int i2 = 8;
        while (i2 < 41) {
            gQ gQVar5 = new gQ(this, i2 == iO ? i2 + "(" + C1818g.b(Action.DEFAULT) + ")" : i2 + "", true, iA == i2);
            c0391gz.a(gQVar5);
            gQVar5.setActionCommand("" + i2);
            gQVar5.addActionListener(new fZ(this));
            gRVar9.add((JMenuItem) gQVar5);
            i2++;
        }
        gRVar8.add((JMenuItem) gRVar9);
        if (C1806i.a().a("fv-7rkf74nfd67whn5iuchqj")) {
            gS gSVar = new gS(this, "Password Caching Timeout", true);
            gSVar.addActionListener(new C0366ga(this));
            gSVar.b(new C0367gb(this));
            gRVar8.add((JMenuItem) gSVar);
        }
        if (BinTableView.S()) {
            gQ gQVar6 = new gQ(this, C1818g.b("Mouse wheel scroll changes selected Table cells"), true, C1798a.a().a(C1798a.cg, C1798a.ch));
            gQVar6.addActionListener(new C0368gc(this));
            gQVar6.a(new C0370ge(this));
            gRVar8.add((JMenuItem) gQVar6);
        }
        if (C1806i.a().a("67r67r8yhdrtrbyuk")) {
            gQ gQVar7 = new gQ(this, C1818g.b("Perform Difference Report On Connect"), true, C1798a.a().c(C1798a.f13391bu, C1798a.f13392bv));
            gQVar7.addActionListener(new C0371gf(this));
            gQVar7.a(new C0372gg(this));
            gRVar8.add((JMenuItem) gQVar7);
        }
        gQ gQVar8 = new gQ(this, C1818g.b("Automatically Load and Save Current Tune"), true, C1798a.a().c(C1798a.f13393bw, true));
        gQVar8.addActionListener(new C0373gh(this));
        gQVar8.a(new C0374gi(this));
        gRVar8.add((JMenuItem) gQVar8);
        if (C1806i.a().a(",.fesokdsoi4309")) {
            gQ gQVar9 = new gQ(this, C1818g.b("Prompt if CurrentTune altered by other program"), true, C1798a.a().c(C1798a.f13380bj, C1798a.f13381bk));
            gQVar9.addActionListener(new C0375gj(this));
            gQVar9.a(new C0376gk(this));
            gRVar8.add((JMenuItem) gQVar9);
        }
        gRVar.add((JMenuItem) gRVar8);
        if (C1806i.a().a("-=fds[pfds[pgd-0")) {
            gR gRVar10 = new gR(this, "Restore Points");
            JCheckBoxMenuItem jCheckBoxMenuItem13 = new JCheckBoxMenuItem(C1818g.b("Save Restore Point on Close"), C1798a.a().c(C1798a.f13396bz, C1798a.f13397bA));
            jCheckBoxMenuItem13.setToolTipText(C1818g.b("When enabled a Restore Point will be created on Project close"));
            jCheckBoxMenuItem13.addActionListener(new C0377gl(this));
            gRVar10.add((JMenuItem) jCheckBoxMenuItem13);
            JCheckBoxMenuItem jCheckBoxMenuItem14 = new JCheckBoxMenuItem(C1818g.b("Save Restore Point on Connect"), C1798a.a().c(C1798a.f13400bD, C1798a.f13401bE));
            jCheckBoxMenuItem14.setToolTipText(C1818g.b("When enabled a Restore Point will be created when connecting to the ECU if it is different from the last restore point"));
            jCheckBoxMenuItem14.addActionListener(new C0378gm(this));
            gRVar10.add((JMenuItem) jCheckBoxMenuItem14);
            JCheckBoxMenuItem jCheckBoxMenuItem15 = new JCheckBoxMenuItem(C1818g.b("Save Restore Point on Tune Save"), C1798a.a().c(C1798a.f13398bB, C1798a.f13399bC));
            jCheckBoxMenuItem15.setToolTipText(C1818g.b("When enabled a Restore Point will be created on Tune Save or CTRL+S"));
            jCheckBoxMenuItem15.addActionListener(new C0379gn(this));
            gRVar10.add((JMenuItem) jCheckBoxMenuItem15);
            JCheckBoxMenuItem jCheckBoxMenuItem16 = new JCheckBoxMenuItem(C1818g.b("Save Restore Point on Tune Open"), C1798a.a().c(C1798a.f13402bF, C1798a.f13403bG));
            jCheckBoxMenuItem16.setToolTipText(C1818g.b("When enabled a Restore Point will be created when loading a Tune file to a controller"));
            jCheckBoxMenuItem16.addActionListener(new C0381gp(this));
            gRVar10.add((JMenuItem) jCheckBoxMenuItem16);
            JCheckBoxMenuItem jCheckBoxMenuItem17 = new JCheckBoxMenuItem(C1818g.b("Skip when no setting changes"), C1798a.a().c(C1798a.f13406bJ, C1798a.f13407bK));
            jCheckBoxMenuItem17.setToolTipText(C1818g.b("When enabled a Restore Point will not be saved if a there have been no changes since the last Restore Point"));
            jCheckBoxMenuItem17.addActionListener(new C0382gq(this));
            gRVar10.add((JMenuItem) jCheckBoxMenuItem17);
            gS gSVar2 = new gS(this, C1818g.b("Maximum Megabytes Disk Space") + ": " + bH.W.a(C1798a.a().a(C1798a.f13404bH, C1798a.f13405bI)), true);
            gSVar2.addActionListener(new C0383gr(this));
            gRVar10.add((JMenuItem) gSVar2);
            gRVar.add((JMenuItem) gRVar10);
        }
        if (!zA) {
            gR gRVar11 = new gR(this, "Performance");
            JCheckBoxMenuItem jCheckBoxMenuItem18 = new JCheckBoxMenuItem(C1818g.b("Gauge Float down on start"), C1798a.a().c(C1798a.f13385bo, true));
            jCheckBoxMenuItem18.setToolTipText(C1818g.b("Enable / Disable gauge float down on project open."));
            jCheckBoxMenuItem18.addActionListener(new C0384gs(this));
            gRVar11.add((JMenuItem) jCheckBoxMenuItem18);
            boolean zC4 = C1798a.a().c(C1798a.f13386bp, false);
            if (zC4) {
                JCheckBoxMenuItem jCheckBoxMenuItem19 = new JCheckBoxMenuItem(C1818g.b("Run In Lite Mode"), zC4);
                jCheckBoxMenuItem19.setToolTipText(C1818g.b("Turns off features to conserve resources."));
                jCheckBoxMenuItem19.addActionListener(new C0385gt(this));
                if (!C1806i.a().a("poij0um098u8oiukj")) {
                    gRVar11.add((JMenuItem) jCheckBoxMenuItem19);
                    jCheckBoxMenuItem19.setEnabled(false);
                    jCheckBoxMenuItem19.setState(false);
                }
            }
            if (C1798a.a().b(C1798a.f13387bq, C1798a.f13388br)) {
                JCheckBoxMenuItem jCheckBoxMenuItem20 = new JCheckBoxMenuItem(C1818g.b("Dash Only Mode"), C1798a.a().c(C1798a.f13389bs, C1798a.f13390bt));
                jCheckBoxMenuItem20.setToolTipText(C1818g.b("Disables Tuning functions for faster project load and to conserve resources."));
                jCheckBoxMenuItem20.addActionListener(new C0386gu(this));
                gRVar11.add((JMenuItem) jCheckBoxMenuItem20);
            } else {
                C1798a.a().b(C1798a.f13387bq, Boolean.toString(false));
            }
            if (C1806i.a().a("_(*UR98ewf098u 98EE 2  *(W")) {
                JCheckBoxMenuItem jCheckBoxMenuItem21 = new JCheckBoxMenuItem(C1818g.b("Prevent Sleep when Online"), C1798a.a().c(C1798a.f13319aa, C1798a.f13320ab));
                jCheckBoxMenuItem21.setToolTipText("<html>" + C1818g.b("When active, Screen saver and sleeping will be prevented while online with a controller.") + "<br>" + C1818g.b("When not connected to a controller, sleeping will be allowed."));
                jCheckBoxMenuItem21.addActionListener(new C0387gv(this));
                gRVar11.add((JMenuItem) jCheckBoxMenuItem21);
            }
            gRVar.add((JMenuItem) gRVar11);
        } else if (C1806i.a().a("_(*UR98ewf098u 98EE 2  *(W") && C1806i.a().a("_(*UR98ewf098u 98EE 2  *(W")) {
            JMenuItem jCheckBoxMenuItem22 = new JCheckBoxMenuItem(C1818g.b("Prevent Sleep when Online"), C1798a.a().c(C1798a.f13319aa, C1798a.f13320ab));
            jCheckBoxMenuItem22.setToolTipText("<html>" + C1818g.b("When active, Screen saver and sleeping will be prevented while online with a controller.") + "<br>" + C1818g.b("When not connected to a controller, sleeping will be allowed."));
            jCheckBoxMenuItem22.addActionListener(new C0388gw(this));
            gRVar8.add(jCheckBoxMenuItem22);
        }
        if (C1806i.a().a("645fds645fds  fdsd098532#@")) {
            C1891a c1891a = new C1891a("Preferred Units");
            gRVar.add((JMenuItem) c1891a);
            bA.c cVar2 = new bA.c(C1818g.b("Convert Table Y Axis kPa to PSI"), true, C1798a.a().c(C1798a.cc, C1798a.cd));
            cVar2.a(new C0389gx(this));
            cVar2.addActionListener(new C0390gy(this));
            c1891a.add((JMenuItem) cVar2);
        }
        if (C1798a.a().c(C1798a.f13302J, false)) {
            gRVar.add(C1818g.b("Enable Registration")).addActionListener(new dA(this));
        } else if (C1798a.a().s()) {
            JMenu jMenu2 = new JMenu("Run as Edition");
            dB dBVar = new dB(this);
            String strC2 = C1798a.a().c(C1798a.cn, C1798a.f13269c);
            ButtonGroup buttonGroup5 = new ButtonGroup();
            if (C1798a.f0do.length > 0) {
                String str = C1798a.f0do[0] + C1806i.f13441c;
                JCheckBoxMenuItem jCheckBoxMenuItem23 = new JCheckBoxMenuItem(str);
                jCheckBoxMenuItem23.setSelected(strC2.equals(str));
                buttonGroup5.add(jCheckBoxMenuItem23);
                jCheckBoxMenuItem23.addActionListener(dBVar);
                jMenu2.add((JMenuItem) jCheckBoxMenuItem23);
            }
            for (String str2 : C1798a.f0do) {
                JCheckBoxMenuItem jCheckBoxMenuItem24 = new JCheckBoxMenuItem(str2);
                jCheckBoxMenuItem24.setSelected(strC2.equals(str2));
                buttonGroup5.add(jCheckBoxMenuItem24);
                jCheckBoxMenuItem24.addActionListener(dBVar);
                jMenu2.add((JMenuItem) jCheckBoxMenuItem24);
            }
            gRVar.add((JMenuItem) jMenu2);
        }
        return gRVar;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void r() {
        if (com.efiAnalytics.ui.bV.a(C1818g.b("Must Restart for changes to take effect.") + "\n" + C1818g.b("Restart Now?"), (Component) this.f3270h, true)) {
            C0338f.a().d((Window) this.f3270h);
        }
    }

    public C1891a i() {
        gR gRVar = new gR(this, "Communications", true);
        gRVar.setMnemonic('C');
        dC dCVar = new dC(this);
        gS gSVar = new gS(this, "Settings", true);
        gSVar.a(dCVar);
        gSVar.addActionListener(new dD(this));
        gRVar.add((JMenuItem) gSVar);
        gS gSVar2 = new gS(this, "Data Rate", true);
        if (C1806i.a().a(" 98 98  0gep9gds09kfg09")) {
            gSVar2.a(dCVar);
        } else {
            gRVar.setEnabled(false);
        }
        gSVar2.addActionListener(new dE(this));
        gRVar.add((JMenuItem) gSVar2);
        ArrayList arrayListB = Q.a().b();
        if (!arrayListB.isEmpty()) {
            gR gRVar2 = new gR(this, "Disabled COM Ports");
            Iterator it = arrayListB.iterator();
            while (it.hasNext()) {
                String str = (String) it.next();
                gS gSVar3 = new gS(this, str, true);
                gSVar3.setName(str);
                gSVar3.addActionListener(new dF(this));
                gRVar2.add((JMenuItem) gSVar3);
            }
            gRVar.add((JMenuItem) gRVar2);
        }
        if (C1806i.a().a("bd098fsdpokfdslk")) {
            gS gSVar4 = new gS(this, "GPS Configuration", true);
            gSVar4.addActionListener(new dG(this));
            gRVar.add((JMenuItem) gSVar4);
        } else {
            gS gSVar5 = new gS(this, "Enable GPS Support", true);
            gSVar5.addActionListener(new dH(this));
            gRVar.add((JMenuItem) gSVar5);
        }
        if (!C1798a.a().a(C1798a.dc, C1798a.dd)) {
            gS gSVar6 = new gS(this, "Mini Terminal", true);
            gSVar6.addActionListener(new dI(this));
            gRVar.add((JMenuItem) gSVar6);
        }
        gQ gQVar = new gQ(this, "Verify Data on Burn", false, true);
        gQVar.a(new dJ(this));
        gQVar.a(new dL(this));
        gQVar.addActionListener(new dM(this));
        if (!C1798a.a().a(C1798a.f13421de, C1798a.df)) {
            gQ gQVar2 = new gQ(this, "Comm Debug Log", false, true);
            gQVar2.a(new dN(this));
            gQVar2.addActionListener(new dO(this));
            gRVar.add((JMenuItem) gQVar2);
        }
        return gRVar;
    }

    public C1891a j() {
        gR gRVar = new gR(this, "Tools", true);
        gRVar.setMnemonic('T');
        String strC = C1798a.a().c(C1798a.cE, (String) null);
        boolean z2 = strC != null && (strC.equalsIgnoreCase("philip.tobin@yahoo.com") || strC.equalsIgnoreCase("p_tobin@yahoo.com") || strC.equalsIgnoreCase("brian@efianalytics.com") || ((C1798a.f13268b.equals("BigComm") && (strC.equalsIgnoreCase("kjmeaney@msn.com") || strC.equalsIgnoreCase("pmosman@hyconinc.com") || strC.equalsIgnoreCase("BigStuff3@comcast.net"))) || (C1798a.f13268b.equals("TunerStudio") && (strC.equalsIgnoreCase("agrippo1@verizon.net") || strC.equalsIgnoreCase("jsm@jsm-net.demon.co.uk")))));
        if (C1806i.a().a("GD[PP-0REP")) {
            gS gSVar = new gS(this, "Controller RAM Editor", true);
            gSVar.a(new dP(this));
            gSVar.addActionListener(new dQ(this));
            gRVar.add((JMenuItem) gSVar);
            JMenuItem gSVar2 = new gS(this, "Binary Log Viewer", true);
            gSVar2.addActionListener(new dR(this));
            gRVar.add(gSVar2);
            gS gSVar3 = new gS(this, "Expression Evaluator", true);
            gSVar3.addActionListener(new dS(this));
            gSVar3.a(new dT(this));
            gRVar.add((JMenuItem) gSVar3);
            JMenuItem gSVar4 = new gS(this, "Binary Diff Report", true);
            gSVar4.addActionListener(new dU(this));
            gRVar.add(gSVar4);
            JMenuItem gSVar5 = new gS(this, "Memory Address Report", true);
            gSVar5.addActionListener(new dW(this));
            gRVar.add(gSVar5);
            JMenuItem gSVar6 = new gS(this, "Encrypt / Decrypt INI File", true);
            gSVar6.addActionListener(new dX(this));
            gRVar.add(gSVar6);
            if (C1806i.a().a("098po;l;lklkjj")) {
                gS gSVar7 = new gS(this, "Import ODT Addressing", true);
                gSVar7.addActionListener(new dY(this));
                gSVar7.a(new dZ(this));
                gRVar.add((JMenuItem) gSVar7);
                gS gSVar8 = new gS(this, "Import Cal Addressing", true);
                gSVar8.addActionListener(new C0312ea(this));
                gSVar8.a(new C0313eb(this));
                gRVar.add((JMenuItem) gSVar8);
            }
            gRVar.addSeparator();
        }
        gRVar.add(new jp());
        if (!C1806i.a().a("09;lgdlgd432;okg0")) {
            gS gSVar9 = new gS(this, "Protocol Stats", true);
            gSVar9.addActionListener(new C0314ec(this));
            gSVar9.b(new C0315ed(this));
            gRVar.add((JMenuItem) gSVar9);
            gRVar.addSeparator();
        }
        if (C1806i.a().a(";LKFDS;LK09")) {
            JMenuItem gSVar10 = new gS(this, "Update / Install Firmware", true);
            gSVar10.addActionListener(new C0316ee(this));
            gRVar.add(gSVar10);
        }
        if (C1806i.a().a("098432lkjgd0932=- ")) {
            gS gSVar11 = new gS(this, "Manage Ini Tuning Views", true);
            gSVar11.addActionListener(new C0317ef(this));
            gSVar11.a(this.f3277o);
            gRVar.add((JMenuItem) gSVar11);
        }
        gS gSVar12 = new gS(this, "Add Custom Channel Wizard", true);
        gSVar12.addActionListener(new C0319eh(this));
        if (C1806i.a().a(";lfds09pofs,54w09")) {
            gSVar12.a(new C0320ei(this));
        } else {
            gSVar12.setEnabled(false);
            gSVar12.setToolTipText(C1818g.b("Upgrade to enable"));
        }
        gRVar.add((JMenuItem) gSVar12);
        gS gSVar13 = new gS(this, "Custom Channel Editor", true);
        gSVar13.addActionListener(new C0321ej(this));
        if (C1806i.a().a(";lfds09pofs,54w09")) {
            gSVar13.a(new C0322ek(this));
        } else {
            gSVar13.setEnabled(false);
            gSVar13.setToolTipText(C1818g.b("Upgrade to enable"));
        }
        gRVar.add((JMenuItem) gSVar13);
        gS gSVar14 = new gS(this, "User Action Editor", true);
        gSVar14.addActionListener(new C0323el(this));
        if (!C1806i.a().a("poij  fdsz poi9ure895 ms7(")) {
            gSVar14.setEnabled(false);
            gSVar14.setToolTipText(C1818g.b("Upgrade to enable"));
        }
        gS gSVar15 = new gS(this, "Action Management", true);
        gSVar15.addActionListener(new C0324em(this));
        if (C1806i.a().a("poij  fdsz poi9ure895 ms7(")) {
            gSVar15.a(this.f3277o);
        } else {
            gSVar15.setEnabled(false);
            gSVar15.setToolTipText(C1818g.b("Upgrade to enable"));
        }
        gRVar.add((JMenuItem) gSVar15);
        gR gRVar2 = new gR(this, "Calculators", true);
        if (C1806i.a().a(";oij fds poi fd u ou43t wer3287")) {
            gS gSVar16 = new gS(this, "Calculator", true);
            gSVar16.addActionListener(new C0325en(this));
            gRVar2.add((JMenuItem) gSVar16);
            gS gSVar17 = new gS(this, "Linear Two Point Calculator", true);
            gSVar17.addActionListener(new C0326eo(this));
            gRVar2.add((JMenuItem) gSVar17);
            gS gSVar18 = new gS(this, "Unit Conversion Calculator", true);
            gSVar18.addActionListener(new C0327ep(this));
            gRVar2.add((JMenuItem) gSVar18);
        } else {
            gRVar2.setEnabled(false);
            gRVar2.setToolTipText(C1818g.b("Upgrade to enable"));
        }
        gRVar.add((JMenuItem) gRVar2);
        boolean zA = C1806i.a().a("09jtrkgds;okfds");
        if (!C1806i.a().a(";,fes;p9rew;o")) {
            gR gRVar3 = new gR(this, C1798a.f13268b + " Plug-ins", true);
            G.R rC = G.T.a().c();
            String strI = rC != null ? rC.i() : "";
            int i2 = 0;
            for (ApplicationPlugin applicationPlugin : C1190l.a().c()) {
                if (applicationPlugin.displayPlugin(strI) && applicationPlugin.getPluginType() == 2) {
                    gS gSVar19 = new gS(this, applicationPlugin.getDisplayName(), true);
                    gSVar19.setName(applicationPlugin.getIdName());
                    gSVar19.addActionListener(new C0328eq(this));
                    if (!zA) {
                        gSVar19.setToolTipText(C1818g.b("Application Plugin Support not available in this edition"));
                    }
                    gSVar19.a(new gH(this, applicationPlugin));
                    gRVar3.add((JMenuItem) gSVar19);
                    i2++;
                }
            }
            if (i2 == 0) {
                gS gSVar20 = new gS(this, "No Plugins Installed", true);
                gSVar20.setEnabled(false);
                gRVar3.add((JMenuItem) gSVar20);
            }
            gRVar3.addSeparator();
            gS gSVar21 = new gS(this, "Add or Update a Plugin", true);
            gSVar21.setEnabled(zA);
            gSVar21.addActionListener(new C0330es(this));
            gRVar3.add((JMenuItem) gSVar21);
            gS gSVar22 = new gS(this, "Online Plugin Information", true);
            gSVar22.addActionListener(new C0331et(this));
            gRVar3.add((JMenuItem) gSVar22);
            gRVar.add((JMenuItem) gRVar3);
            gRVar.addSeparator();
        }
        if (C1798a.f13268b.equals(C1806i.f13460u)) {
            gS gSVar23 = new gS(this, "Calibrate TPS", true);
            gSVar23.a(new C0332eu(this));
            gSVar23.addActionListener(new C0333ev(this));
            gRVar.add((JMenuItem) gSVar23);
        }
        return gRVar;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void s() {
        this.f3264c.F();
    }

    public C1891a k() {
        gR gRVar = new gR(this, f3263b, true);
        gRVar.setMnemonic('H');
        if (!C1798a.a().a(C1798a.dg, C1798a.dh)) {
            gS gSVar = new gS(this, C1798a.f13268b + " " + C1818g.b(f3263b), true);
            gSVar.addActionListener(new C0334ew(this));
            gRVar.add((JMenuItem) gSVar);
            gRVar.addSeparator();
        }
        gS gSVar2 = new gS(this, "Check For Update", true);
        gSVar2.addActionListener(new C0335ex(this));
        gRVar.add((JMenuItem) gSVar2);
        boolean z2 = C1798a.a().c(C1798a.cK, false) && C1018z.i().a(C1798a.a().c(C1798a.cF, ""));
        boolean zA = C1018z.i().a(C1798a.a().c(C1798a.cF, ""));
        if (!z2) {
            C1798a.a().b(C1798a.f13361aQ, "true");
        }
        JCheckBoxMenuItem jCheckBoxMenuItem = new JCheckBoxMenuItem(C1818g.b("Automatic Update Check"), C1798a.a().c(C1798a.f13361aQ, false));
        jCheckBoxMenuItem.setEnabled(z2);
        jCheckBoxMenuItem.addItemListener(new C0336ey(this));
        gRVar.add((JMenuItem) jCheckBoxMenuItem);
        if (zA) {
            gS gSVar3 = new gS(this, "Update Registration", true);
            gSVar3.addActionListener(new eB(this));
            gRVar.add((JMenuItem) gSVar3);
            gS gSVar4 = new gS(this, "Remove Registration Information", true);
            gSVar4.addActionListener(new eD(this));
            gRVar.add((JMenuItem) gSVar4);
        } else {
            gS gSVar5 = new gS(this, "Enter Registration", true);
            gSVar5.addActionListener(new C0337ez(this));
            gRVar.add((JMenuItem) gSVar5);
            gS gSVar6 = new gS(this, "Upgrade for more features!", true);
            gSVar6.addActionListener(new eA(this));
            gRVar.add((JMenuItem) gSVar6);
        }
        if (C1806i.a().a("87gdjkjd98fes")) {
            gS gSVar7 = new gS(this, "Upgrade for even more Features!", true);
            gSVar7.addActionListener(new eE(this));
            gRVar.add((JMenuItem) gSVar7);
        }
        gRVar.addSeparator();
        if (C1798a.a().a(C1798a.dj, false)) {
            gS gSVar8 = new gS(this, "Submit " + C1798a.f13268b + " Review", true);
            gSVar8.addActionListener(new eF(this));
            gRVar.add((JMenuItem) gSVar8);
        }
        gS gSVar9 = new gS(this, "Create " + C1798a.f13268b + " Debug Package", true);
        gSVar9.addActionListener(new eG(this));
        gRVar.add((JMenuItem) gSVar9);
        gS gSVar10 = new gS(this, "About", true);
        gSVar10.addActionListener(new eH(this));
        gRVar.add((JMenuItem) gSVar10);
        return gRVar;
    }

    @Override // G.S
    public void a(G.R r2) {
        if (this.f3265d != null) {
            this.f3265d.p().b(this);
        }
        this.f3265d = r2;
        o();
        q();
        l();
        this.f3266q.validate();
        if (this.f3267e == null || this.f3267e.equals(this.f3266q)) {
            return;
        }
        this.f3267e.validate();
    }

    public void l() {
        if (this.f3275m != null) {
            this.f3275m.a();
        }
    }

    @Override // G.S
    public void c(G.R r2) {
        r2.p().a(this);
    }

    @Override // G.S
    public void b(G.R r2) {
        o();
        this.f3267e.b(r2.c());
        q();
        this.f3265d.p().b(this);
        r2.b(this);
        r2.C().b(this);
        C0113cs.a().a(this.f3276n);
    }

    public void b(boolean z2) {
        if (!SwingUtilities.isEventDispatchThread()) {
            try {
                SwingUtilities.invokeAndWait(new eI(this, z2));
                return;
            } catch (InterruptedException | InvocationTargetException e2) {
                Logger.getLogger(C0308dx.class.getName()).log(Level.SEVERE, (String) null, e2);
                return;
            }
        }
        this.f3266q.setEnabled(z2);
        if (this.f3267e == null || this.f3267e.getComponent() == null) {
            return;
        }
        this.f3267e.getComponent().setEnabled(z2);
    }

    @Override // G.InterfaceC0042ab
    public void a(String str, int i2, int i3, int[] iArr) {
        l();
    }

    @Override // G.bT
    public void a() {
        b(false);
        new eJ(this).start();
    }

    @Override // G.bT
    public void a(boolean z2) {
        b(true);
    }

    @Override // G.aG
    public boolean a(String str, G.bS bSVar) {
        return true;
    }

    @Override // G.aG
    public void a(String str) {
    }

    @Override // aE.e
    public void a(aE.a aVar, G.R r2) {
    }

    @Override // aE.e
    public void e_() {
        this.f3272j = 0;
    }

    @Override // aE.e
    public void a(aE.a aVar) {
    }

    public gL m() {
        return this.f3266q;
    }

    public bA.f a(String str, boolean z2) {
        if (this.f3262p.equals("Main Menu Style")) {
            return new gR(this, str, z2);
        }
        if (this.f3262p.equals("Wrapping Main Menu")) {
            gR gRVar = new gR(this, str, z2);
            gRVar.b(true);
            return gRVar;
        }
        C1220b c1220b = new C1220b(str, z2);
        c1220b.setToolTipText(str);
        c1220b.addMouseListener(new gP(this, c1220b));
        this.f3268f.a(c1220b);
        return c1220b;
    }

    protected String c(String str) {
        if (str.contains("Spark") || str.contains("Igni") || str.contains("spark") || str.contains("igni")) {
            return "resources/sparkplug32.png";
        }
        if (str.contains("Fuel") || str.contains("Inj")) {
            return "resources/injector2.png";
        }
        if (str.contains(FXMLLoader.CONTROLLER_SUFFIX)) {
            return "resources/Circuit32.png";
        }
        if (str.contains("Shift") || str.contains("Trans")) {
            return "resources/Shifter32.png";
        }
        if (str.contains("Boost") || str.contains("Turbo")) {
            return "resources/turbocharger1.png";
        }
        if (str.contains("Nitrous") || str.contains("NOS")) {
            return "resources/nitrous.png";
        }
        if (str.contains("Advanced")) {
            return "resources/two-gears.png";
        }
        if (str.contains("Table")) {
            return "resources/tableFeather.png";
        }
        if (str.contains("SR2") || str.toLowerCase().contains("traction")) {
            return "resources/traction_32.png";
        }
        if (str.toLowerCase().contains("log")) {
            return "resources/graph_32a.png";
        }
        if (str.toLowerCase().contains("line")) {
            return "resources/dragTree_32.png";
        }
        if (str.toLowerCase().contains("e85")) {
            return "resources/gaspump_32.png";
        }
        if (str.toLowerCase().contains("afr")) {
            return "resources/O2Sensor4_64.png";
        }
        if (str.toLowerCase().contains("start")) {
            return "resources/crank_64.png";
        }
        if (str.toLowerCase().contains("accel")) {
            return "resources/accel_pedal_64.png";
        }
        if (str.contains("ICF")) {
            return "resources/icon-piston_64.png";
        }
        if (str.contains("CAN")) {
            return "resources/Network_64.png";
        }
        String str2 = this.f3271i[this.f3272j % this.f3271i.length];
        this.f3272j++;
        return str2;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public gF a(bA.f fVar, boolean z2) {
        gF gFVar = this.f3273k.size() > 0 ? (gF) this.f3273k.remove(0) : new gF(this, null);
        gFVar.a(z2);
        gFVar.a(fVar);
        return gFVar;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public gG b(bA.f fVar, boolean z2) {
        gG gGVar = this.f3274l.size() > 0 ? (gG) this.f3274l.remove(0) : new gG(this, null);
        gGVar.a(z2);
        gGVar.a(fVar);
        return gGVar;
    }
}
