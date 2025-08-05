package ao;

import W.C0184j;
import W.C0188n;
import ar.C0836c;
import ar.C0839f;
import ar.C0840g;
import g.C1724b;
import h.C1737b;
import h.InterfaceC1736a;
import i.C1743c;
import i.InterfaceC1741a;
import i.InterfaceC1742b;
import j.C1750a;
import j.C1751b;
import j.C1752c;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.SwingUtilities;

/* loaded from: TunerStudioMS.jar:ao/aQ.class */
public class aQ extends JPanel implements InterfaceC0808hk, InterfaceC0813k, InterfaceC0814l, InterfaceC1742b {

    /* renamed from: c, reason: collision with root package name */
    JScrollPane f5144c;

    /* renamed from: d, reason: collision with root package name */
    InterfaceC0642bf f5145d;

    /* renamed from: n, reason: collision with root package name */
    private C0804hg f5141n = C0804hg.a();

    /* renamed from: a, reason: collision with root package name */
    C0625ap f5142a = new C0625ap(this.f5141n);

    /* renamed from: b, reason: collision with root package name */
    protected C0823u f5143b = new C0823u(this.f5141n);

    /* renamed from: e, reason: collision with root package name */
    C0815m f5146e = new C0815m(this.f5141n);

    /* renamed from: o, reason: collision with root package name */
    private C1724b f5147o = null;

    /* renamed from: f, reason: collision with root package name */
    C1724b f5148f = null;

    /* renamed from: g, reason: collision with root package name */
    hn f5149g = null;

    /* renamed from: h, reason: collision with root package name */
    String f5150h = null;

    /* renamed from: i, reason: collision with root package name */
    boolean f5151i = false;

    /* renamed from: p, reason: collision with root package name */
    private JSplitPane f5152p = new JSplitPane();

    /* renamed from: q, reason: collision with root package name */
    private JSplitPane f5153q = new JSplitPane();

    /* renamed from: j, reason: collision with root package name */
    boolean f5154j = true;

    /* renamed from: k, reason: collision with root package name */
    C0840g f5155k = new C0840g();

    /* renamed from: l, reason: collision with root package name */
    protected boolean f5156l = false;

    /* renamed from: m, reason: collision with root package name */
    boolean f5157m = false;

    public aQ() {
        this.f5144c = null;
        this.f5145d = null;
        this.f5152p.setOrientation(1);
        this.f5153q.setOrientation(0);
        this.f5153q.setOneTouchExpandable(true);
        this.f5152p.setOneTouchExpandable(true);
        this.f5152p.setDividerSize(com.efiAnalytics.ui.eJ.a(8));
        this.f5153q.setDividerSize(com.efiAnalytics.ui.eJ.a(8));
        this.f5152p.setResizeWeight(1.0d);
        this.f5153q.setResizeWeight(1.0d);
        this.f5152p.setFont(getFont());
        C0645bi.a().a(this.f5146e);
        boolean zA = h.i.a("showDashboard", true);
        this.f5153q.addPropertyChangeListener(JSplitPane.DIVIDER_LOCATION_PROPERTY, new aR(this));
        this.f5152p.addPropertyChangeListener(JSplitPane.DIVIDER_LOCATION_PROPERTY, new aS(this));
        C0645bi.a().a(this.f5143b);
        C0645bi.a().a(this);
        this.f5142a.c(h.i.b("lineTraceSize", h.i.f12310ae));
        this.f5142a.b(h.i.a(h.i.f12311af, h.i.f12312ag));
        new JPanel().setLayout(new BorderLayout(1, 2));
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new BorderLayout(1, 2));
        setLayout(new BorderLayout(1, 2));
        this.f5152p.setTopComponent(jPanel);
        this.f5153q.setTopComponent(this.f5152p);
        add(BorderLayout.CENTER, this.f5153q);
        this.f5152p.setBottomComponent(new JLabel());
        SwingUtilities.invokeLater(new aT(this));
        if (C1737b.a().a("optionalQuickSelect") && h.i.a("fieldSelectionStyle", "standardSelection").equals("selectFromDash")) {
            C0583A c0583a = new C0583A();
            this.f5143b.a(c0583a);
            this.f5145d = c0583a;
        } else {
            this.f5145d = new gS(this.f5141n);
            jPanel.add((gS) this.f5145d, "West", 0);
        }
        C0645bi.a().a(this.f5145d);
        add(this.f5146e, "North");
        jPanel.add(this.f5142a, BorderLayout.CENTER);
        if (C1737b.a().a("tabbedQuickViews")) {
            jPanel.add("North", this.f5155k);
            this.f5155k.setVisible(false);
        }
        JPanel jPanel2 = new JPanel();
        jPanel2.setLayout(new BorderLayout());
        this.f5144c = new JScrollPane(this.f5143b);
        jPanel2.add(BorderLayout.CENTER, this.f5144c);
        this.f5153q.setBottomComponent(jPanel2);
        C0641be.a().a(this.f5142a);
        if (h.i.f12342aI != null) {
            this.f5142a.a(h.i.f12342aI);
        }
        this.f5146e.a(this.f5142a);
        this.f5146e.a(this);
        this.f5146e.a(this.f5143b);
        n().a((InterfaceC0813k) this);
        n().a(this.f5145d);
        if (this.f5145d instanceof gS) {
            n().a((gO) this.f5145d);
        }
        n().a((InterfaceC0813k) this.f5142a);
        n().a((InterfaceC0763ft) this.f5142a);
        n().a(aO.a());
        n().a((InterfaceC0813k) this.f5143b);
        n().a((InterfaceC1741a) this.f5143b);
        n().a((gR) this.f5143b);
        n().a((InterfaceC1741a) this.f5142a);
        n().a((InterfaceC0758fo) this.f5142a);
        n().a((InterfaceC1742b) this.f5142a);
        n().c(this.f5143b);
        this.f5142a.a(n());
        bL bLVar = new bL(n());
        this.f5142a.addMouseListener(bLVar);
        this.f5142a.addMouseMotionListener(bLVar);
        this.f5142a.addMouseWheelListener(bLVar);
        C0645bi.a().a(this.f5142a);
        if (C0645bi.a().f() != null) {
            C0645bi.a().f().a(this);
        }
        d(Boolean.parseBoolean(h.i.a("showGauges", "true")));
        c(h.i.a("adjustGaugeBackgroudToVal", false));
        n().b(h.i.a("zoom", 2.0d), false);
        this.f5146e.c(h.i.b("compareOffset", 0));
        int iA = h.i.a("numberOfOverlays", h.i.f12274u) * h.i.a("numberOfGraphs", h.i.f12273t);
        for (int i2 = 0; i2 < iA; i2++) {
            String str = "graphForeColor" + (i2 - 0);
            Color color = null;
            try {
                color = new Color(Integer.parseInt(h.i.c(str)));
            } catch (Exception e2) {
            }
            if (color != null && (this.f5145d instanceof C0583A)) {
                n().a(color, i2);
            } else if (h.i.a(str, (Color) null) != null) {
                n().a(h.i.a(str, Color.cyan), i2);
            }
        }
        n().c(zA);
        n().a(h.i.a("hideSelector", h.i.f12270q));
        C1743c.a().a(m());
        C1743c.a().a((InterfaceC1742b) this.f5142a);
        C1743c.a().a((InterfaceC1742b) this.f5143b);
        C0804hg.a().a((InterfaceC1742b) this.f5143b);
        C1743c.a().a(this);
        if (C1737b.a().a("tabbedQuickViews") && h.i.a(h.i.f12315aj, h.i.f12316ak)) {
            if (!C0839f.a().a(h.i.e("lastSelectedQuickViewName", Action.DEFAULT))) {
                C0839f.a().a(Action.DEFAULT);
            }
            this.f5142a.k();
        } else {
            C0839f.a().a(Action.DEFAULT);
            this.f5142a.k();
        }
        C0839f.a().a(new C0639bc(this, this.f5145d));
        o();
        new C0638bb(this).start();
    }

    protected JPanel e() {
        return this.f5149g;
    }

    public void a(String str, String str2, String str3) {
        if (str == null || str2 == null || str3 == null || str.equals("") || str2.equals("") || str3.equals("")) {
            this.f5142a.a("Support " + h.i.f12255b + ", register today.");
        } else {
            this.f5142a.a("Thank You for registering: " + str + " " + str2 + "<" + str3 + ">");
        }
    }

    public boolean f() {
        return this.f5151i;
    }

    public void c(C0188n c0188n) {
        if (this.f5147o != null) {
        }
        this.f5141n.c(0);
        if (C1737b.a().a("tabbedQuickViews")) {
            this.f5155k.setVisible(true);
        }
        n().b(c0188n);
        n().c((C0188n) null);
        this.f5142a.a(c0188n.a());
        this.f5142a.b(c0188n.c());
        int width = (int) (this.f5142a.getWidth() / (2.0d * this.f5141n.t()));
        if (width > c0188n.d()) {
            width = c0188n.d() - 1;
        }
        this.f5141n.c(width);
        C0184j c0184j = null;
        Iterator it = c0188n.iterator();
        while (it.hasNext()) {
            C0184j c0184j2 = (C0184j) it.next();
            if (c0184j2.a().equalsIgnoreCase(h.g.a().a("Time"))) {
                c0184j = c0184j2;
            }
        }
        this.f5142a.a(c0184j);
        this.f5142a.p();
        boolean zJ = this.f5142a.j();
        this.f5145d.a();
        if (!zJ) {
            this.f5142a.k();
        }
        b(h.i.a("showTuningConsole", h.i.f12269p));
        this.f5151i = false;
        Dimension preferredSize = this.f5143b.getPreferredSize();
        if (preferredSize.getHeight() > com.efiAnalytics.ui.eJ.a(240)) {
            this.f5144c.setPreferredSize(com.efiAnalytics.ui.eJ.a(200, 240));
        } else {
            this.f5144c.setPreferredSize(preferredSize);
        }
        int height = (getHeight() - this.f5144c.getPreferredSize().height) - this.f5153q.getDividerSize();
        int dividerLocation = this.f5153q.getDividerLocation();
        if (this.f5154j || height > dividerLocation) {
            if (this.f5154j) {
                n().b(new aU(this));
            }
            new aV(this).start();
            this.f5154j = false;
        }
        requestFocus();
    }

    public void g() {
        this.f5151i = false;
        n().c((C0188n) null);
        this.f5142a.b((C0188n) null);
        System.gc();
    }

    public boolean h() throws HeadlessException {
        if (this.f5149g != null && this.f5149g.isVisible() && this.f5149g.h()) {
            int iShowConfirmDialog = JOptionPane.showConfirmDialog(this, "Tune Settings changes have not been saved.\nWould you like to save before exiting?");
            if (iShowConfirmDialog == 0) {
                this.f5149g.k();
                System.out.println("Ignore following stack, it is generated on purpose to end shutdown.");
                ((String) null).toString();
            } else {
                if (iShowConfirmDialog == 1) {
                    return true;
                }
                if (iShowConfirmDialog == 2) {
                    System.out.println("Ignore following stack, it is generated on purpose to end shutdown.");
                    ((String) null).toString();
                }
            }
        }
        i();
        return true;
    }

    public void i() {
        if (C0645bi.a().c().j()) {
            String strG = C0839f.a().g();
            bH.C.c("Current Log View: " + strG);
            if (strG != null && !strG.isEmpty()) {
                C0836c c0836cC = C0839f.a().c(strG);
                C0836c c0836cB = this.f5142a.B();
                if (C0645bi.a().c().j()) {
                    bH.C.c("View Changed: " + C0645bi.a().c().j());
                    c0836cB.a(c0836cC.b());
                    C0839f.a().a(c0836cB);
                }
            }
        } else {
            bH.C.c("Graph Selection not changed.");
        }
        C0836c c0836cC2 = C0839f.a().c(Action.DEFAULT);
        if (c0836cC2 != null && C0804hg.a().r() != null && !C0804hg.a().r().isEmpty()) {
            C0839f.a().b(c0836cC2);
        }
        if (!C0839f.a().d() || C0804hg.a().r() == null || C0804hg.a().r().isEmpty()) {
            return;
        }
        String str = "The Following Quick Views have changed:";
        Iterator it = C0839f.a().c().iterator();
        while (it.hasNext()) {
            str = str + "\n" + ((C0836c) it.next()).b();
        }
        if (com.efiAnalytics.ui.bV.a(str + "\n\nWould you like to save the changes?", (Component) this, true)) {
            C0839f.a().e();
        }
    }

    public void j() {
        a("CTRL+N or Right - Advance 1 Record\nCTRL+B or Left - Back 1 Record\nPage UP - Advance a graph page worth of records\nPage Down - Decrease a graph page of records\nHome - Jump to 1st Record\nEnd - Jump to last Record\nShift + Right - Select Range to display Average\nUp - Rescale graph, Zoom in\nDown - Rescale graph, Zoom out\nCTRL+P - Play\nCTRL+S - Stop Playback\nSpace - Pause playback\nCTRL+U - Speed playback\nCTRL+D - Slow playback\nCTRL+Delete - Clear graphs\nF3 - Repeat Last Search\nF2 - Scale to Fit", getParent());
    }

    public void k() {
        a(h.i.i(), getParent());
    }

    public void a(String str, Component component) throws HeadlessException {
        com.efiAnalytics.ui.bV.c(str, component);
    }

    public String l() {
        return this.f5150h;
    }

    public void a(String[] strArr, boolean z2) {
        if (this.f5141n.r() != null && !this.f5141n.r().isEmpty()) {
            i();
        }
        this.f5150h = strArr[0];
        g();
        if (this.f5147o != null && this.f5147o.isAlive()) {
            this.f5147o.c();
        }
        C0804hg.a().a(strArr);
        this.f5147o = new C1724b(this);
        this.f5147o.b(h.i.a("fillNaN", h.i.f12279z));
        String strA = h.i.a("fieldMapping", "Auto");
        if (strA == null || strA.length() == 0 || strA.equals("Auto")) {
            bH.C.c("Auto Detect Field mapping");
            if (C1737b.f12235n != null) {
                try {
                    this.f5147o.a((InterfaceC1736a) C1737b.f12235n.newInstance());
                } catch (IllegalAccessException e2) {
                    Logger.getLogger(aQ.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
                } catch (InstantiationException e3) {
                    Logger.getLogger(aQ.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e3);
                }
            } else if (h.i.f12255b.equals(C1737b.f12230j)) {
                String strB = bH.W.b(h.i.f12256c, C1737b.f12222b, "");
                if (strB.equals(C1737b.f12229i)) {
                    this.f5147o.a(new C1751b());
                } else if (strB.equals(C1737b.f12225e)) {
                    this.f5147o.a(new C1750a());
                } else {
                    this.f5147o.a(new C1752c());
                }
            } else if (h.i.f12255b.equals(C1737b.f12231k)) {
                this.f5147o.a(new C1750a());
            } else {
                this.f5147o.a(new C1752c());
            }
        } else {
            bH.C.c("Setting Field mapping to:" + strA);
            h.g.a();
            h.g.b(strA);
        }
        this.f5147o.a(z2);
        Iterator itG = C1743c.a().g();
        while (itG.hasNext()) {
            this.f5147o.a((InterfaceC1742b) itG.next());
        }
        this.f5147o.a(strArr);
        this.f5147o.start();
    }

    public boolean b(boolean z2) {
        JButton jButton;
        if (!C1737b.a().a("tuningPanelVisible")) {
            return false;
        }
        if (this.f5149g == null) {
            JPanel jPanel = new JPanel();
            jPanel.setLayout(new BorderLayout());
            JPanel jPanel2 = new JPanel();
            jPanel2.setLayout(new BorderLayout());
            try {
                jButton = new JButton(new ImageIcon(com.efiAnalytics.ui.cO.a().a(com.efiAnalytics.ui.cO.f11152ap, jPanel2, 16)));
            } catch (V.a e2) {
                Logger.getLogger(aQ.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
                jButton = new JButton("X");
            }
            jButton.setToolTipText("Close any open tune file.");
            Dimension dimensionA = com.efiAnalytics.ui.eJ.a(16, 16);
            jButton.setMinimumSize(dimensionA);
            jButton.setPreferredSize(dimensionA);
            jButton.addActionListener(new aX(this));
            jPanel2.add("East", jButton);
            jPanel.add("North", jPanel2);
            this.f5149g = new hn();
            this.f5149g.d();
            n().a((InterfaceC1742b) this.f5149g);
            n().a((InterfaceC1741a) this.f5149g);
            if (n().r() != null) {
                this.f5149g.a(n().r());
            }
            c(h.i.a("prefFontSize", com.efiAnalytics.ui.eJ.a()));
            jPanel.add(BorderLayout.CENTER, this.f5149g);
            this.f5149g.setVisible(z2);
            q().setBottomComponent(jPanel);
            double dA = h.i.a(h.i.f12303X, -1.0d);
            if (dA > 0.2d && dA < 0.98d) {
                q().setDividerLocation(dA);
            }
            e(h.i.f(h.i.f12293N, h.i.f12296Q));
            this.f5156l = true;
        }
        this.f5149g.setVisible(z2);
        h.i.c("showTuningConsole", "" + z2);
        if (z2) {
            u();
        } else {
            q().setDividerLocation(1.0d);
        }
        return z2;
    }

    public void c(String str) {
        if (n().r() == null) {
            a("Please Open a file before the compare file", this);
            return;
        }
        if (this.f5148f != null && this.f5148f.isAlive()) {
            this.f5148f.c();
        }
        this.f5148f = new C1724b(this, true);
        if (m() != null) {
            this.f5148f.a(m());
        }
        this.f5148f.a(this.f5142a);
        this.f5148f.a(this);
        this.f5148f.a(this.f5143b);
        this.f5148f.a(str);
        this.f5148f.start();
    }

    public InterfaceC1742b m() {
        return C0645bi.a().f();
    }

    public C0804hg n() {
        return C0804hg.a();
    }

    @Override // ao.InterfaceC0813k
    public void a(Color color) {
    }

    public void c(boolean z2) {
        this.f5143b.d(z2);
    }

    public void d(boolean z2) {
        this.f5143b.e(z2);
    }

    @Override // ao.InterfaceC0813k
    public void a(Color color, int i2) {
    }

    @Override // i.InterfaceC1742b
    public void a() {
    }

    @Override // i.InterfaceC1742b
    public void a(double d2) {
        if (d2 == 1.0d) {
            n().c(n().p());
            if (n().s() != null) {
                this.f5146e.d(-n().s().d());
                this.f5146e.e(n().r().d());
                d(n().s());
                return;
            } else {
                if (n().r() != null) {
                    d(n().r());
                    return;
                }
                return;
            }
        }
        if (!h.i.a(h.i.f12300U, h.i.f12301V) || d2 < 0.05d || Double.isInfinite(d2)) {
            if (this.f5157m || d2 >= 0.05d) {
                return;
            }
            this.f5157m = true;
            return;
        }
        if (this.f5147o == null || !this.f5147o.e()) {
            if (this.f5141n.r() != null) {
                a((int) (this.f5141n.r().d() / d2));
            } else {
                bH.C.c("No DataSet, can't scale.");
            }
        }
    }

    public void a(int i2) {
        this.f5142a.j(i2);
    }

    @Override // i.InterfaceC1742b
    public void b() {
        this.f5151i = true;
    }

    public void d(C0188n c0188n) {
        if (!h.i.f12268o) {
            if (c()) {
                return;
            }
            Iterator it = c0188n.iterator();
            while (it.hasNext()) {
                ((C0184j) it.next()).a(true);
            }
            return;
        }
        Iterator it2 = c0188n.iterator();
        while (it2.hasNext()) {
            C0184j c0184j = (C0184j) it2.next();
            if (!c0184j.a().equalsIgnoreCase(h.g.a().a("Time"))) {
                c0184j.a(true);
            }
        }
    }

    private boolean c() {
        this.f5142a.C();
        return true;
    }

    @Override // i.InterfaceC1742b
    public void a(C0188n c0188n) {
        if (SwingUtilities.isEventDispatchThread()) {
            c(c0188n);
        } else {
            SwingUtilities.invokeLater(new aY(this, System.currentTimeMillis(), c0188n));
        }
    }

    @Override // i.InterfaceC1742b
    public void b(C0188n c0188n) {
        n().c(c0188n);
        this.f5146e.d(-c0188n.d());
        this.f5146e.e(n().r().d());
        this.f5146e.c(0);
        validate();
        doLayout();
    }

    @Override // java.awt.Component
    public void setBounds(int i2, int i3, int i4, int i5) {
        super.setBounds(i2, i3, i4, i5);
    }

    @Override // javax.swing.JComponent, java.awt.Component
    public void setBackground(Color color) {
        super.setBackground(color);
        if (this.f5146e != null) {
            this.f5146e.setBackground(color);
            this.f5146e.invalidate();
            this.f5146e.validate();
        }
    }

    public void o() {
        new C0588F(this.f5142a, 500L);
    }

    @Override // ao.InterfaceC0814l
    public void b(int i2) {
        h.i.c("compareOffset", i2 + "");
    }

    @Override // ao.InterfaceC0814l
    public void a(boolean z2) {
    }

    @Override // java.awt.Container, java.awt.Component
    public void doLayout() {
        this.f5146e.setVisible(n().s() != null);
        super.doLayout();
    }

    @Override // ao.InterfaceC0808hk
    public void b(double d2) {
        if (n().r() == null) {
            return;
        }
        n().c((int) ((n().r().d() - 1.0d) * d2));
        o();
    }

    void c(int i2) {
        if (this.f5149g != null) {
            this.f5149g.c(i2);
            doLayout();
            this.f5149g.doLayout();
        }
        float fA = i2 / com.efiAnalytics.ui.eJ.a();
    }

    public void d(String str) {
        if (b(true)) {
            this.f5149g.b(str);
        }
    }

    public C0625ap p() {
        return this.f5142a;
    }

    public JSplitPane q() {
        return this.f5152p;
    }

    public JSplitPane r() {
        return this.f5153q;
    }

    public C1724b s() {
        return this.f5147o;
    }

    public void e(String str) {
        if (str.equals(h.i.f12295P)) {
            this.f5149g.a(this.f5143b.d());
            this.f5149g.a(false);
            this.f5143b.doLayout();
            this.f5149g.c().doLayout();
            this.f5149g.c().validate();
            SwingUtilities.invokeLater(new aZ(this));
        } else {
            this.f5143b.a(this.f5143b.d());
            this.f5149g.a(true);
            this.f5149g.doLayout();
            this.f5143b.doLayout();
            SwingUtilities.invokeLater(new RunnableC0637ba(this));
        }
        validate();
    }

    public void t() {
        if (this.f5144c != null) {
            if (!h.i.a("showDashboard", true)) {
                r().setDividerLocation((getHeight() - this.f5144c.getMinimumSize().height) - r().getDividerSize());
                return;
            }
            Dimension preferredSize = this.f5143b.getPreferredSize();
            int height = getHeight() / 5;
            if (preferredSize.getHeight() > height * 1.5d) {
                this.f5144c.setPreferredSize(new Dimension(200, height));
            } else {
                this.f5144c.setPreferredSize(preferredSize);
            }
            double dA = h.i.a(h.i.f12302W, -1.0d);
            if (C0804hg.a().r() == null || dA < 0.1d || dA > 0.9d) {
                r().setDividerLocation((getHeight() - this.f5144c.getPreferredSize().height) - r().getDividerSize());
            } else {
                r().setDividerLocation(dA);
            }
        }
    }

    public void u() {
        if (e() == null || !h.i.a("showTuningConsole", h.i.f12269p)) {
            q().setDividerLocation(1.0d);
            return;
        }
        double dA = h.i.a(h.i.f12303X, -1.0d);
        if (dA < 0.05d || dA > 0.98d) {
            q().setDividerLocation((getWidth() - e().getPreferredSize().width) - q().getDividerSize());
        } else {
            q().setDividerLocation(dA);
        }
    }

    public void v() {
        String strG = C0839f.a().g();
        if (strG == null || strG.isEmpty()) {
            return;
        }
        f(strG);
    }

    public void f(String str) {
        if (str == null || str.isEmpty()) {
            return;
        }
        C0836c c0836cC = C0839f.a().c(str);
        C0836c c0836cB = this.f5142a.B();
        if (c0836cC == null || !C0645bi.a().c().j()) {
            return;
        }
        bH.C.c("View Changed: " + C0645bi.a().c().j());
        c0836cB.a(c0836cC.b());
        c0836cB.a(c0836cC.f());
        C0839f.a().a(c0836cB);
    }
}
