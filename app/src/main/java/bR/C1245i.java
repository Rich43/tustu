package br;

import G.C0048ah;
import G.C0084bq;
import G.C0085br;
import G.bL;
import G.dc;
import G.dk;
import bH.C1007o;
import bt.C1324bf;
import bt.C1345d;
import com.efiAnalytics.apps.ts.dashboard.C1388aa;
import com.efiAnalytics.apps.ts.dashboard.C1425x;
import com.efiAnalytics.apps.ts.dashboard.Gauge;
import com.efiAnalytics.ui.C1630dn;
import com.efiAnalytics.ui.InterfaceC1565bc;
import com.efiAnalytics.ui.bV;
import com.efiAnalytics.ui.dQ;
import com.efiAnalytics.ui.eJ;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.KeyboardFocusManager;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.Window;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import n.InterfaceC1761a;
import r.C1798a;
import r.C1806i;
import s.C1818g;

/* renamed from: br.i, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:br/i.class */
public class C1245i extends C1345d implements InterfaceC1565bc, InterfaceC1761a {

    /* renamed from: d, reason: collision with root package name */
    dQ f8458d;

    /* renamed from: i, reason: collision with root package name */
    dk f8466i;

    /* renamed from: j, reason: collision with root package name */
    G.R f8467j;

    /* renamed from: k, reason: collision with root package name */
    String f8468k;

    /* renamed from: t, reason: collision with root package name */
    private static final String f8469t = eJ.a(200) + "";

    /* renamed from: l, reason: collision with root package name */
    static String f8470l = "_histogram";

    /* renamed from: a, reason: collision with root package name */
    n.n f8455a = null;

    /* renamed from: b, reason: collision with root package name */
    C1425x f8456b = null;

    /* renamed from: c, reason: collision with root package name */
    C1425x f8457c = null;

    /* renamed from: e, reason: collision with root package name */
    JSplitPane f8459e = new JSplitPane();

    /* renamed from: f, reason: collision with root package name */
    JSplitPane f8460f = new JSplitPane();

    /* renamed from: g, reason: collision with root package name */
    al f8461g = null;

    /* renamed from: q, reason: collision with root package name */
    private aE.a f8462q = null;

    /* renamed from: r, reason: collision with root package name */
    private Window f8463r = null;

    /* renamed from: s, reason: collision with root package name */
    private Container f8464s = null;

    /* renamed from: h, reason: collision with root package name */
    C1249m f8465h = null;

    /* renamed from: u, reason: collision with root package name */
    private String f8471u = null;

    /* renamed from: m, reason: collision with root package name */
    boolean f8472m = false;

    /* renamed from: n, reason: collision with root package name */
    boolean f8473n = false;

    /* renamed from: o, reason: collision with root package name */
    boolean f8474o = false;

    /* renamed from: p, reason: collision with root package name */
    boolean f8475p = false;

    public C1245i(G.R r2, dk dkVar) throws NumberFormatException {
        this.f8458d = null;
        this.f8466i = null;
        this.f8467j = null;
        this.f8468k = null;
        this.f8467j = r2;
        this.f8466i = dkVar;
        this.f8468k = bL.a(r2, dkVar.b());
        this.f8458d = new dQ(aE.a.A(), "VeAnalyzeConsole." + dkVar.b());
        try {
            if (this.f8468k == null || this.f8468k.equals("") || C1007o.a(this.f8468k, r2)) {
                f();
            }
        } catch (V.g e2) {
            bH.C.b("Unable to determine if table " + dkVar.b() + " should be active, so defaulting to yes...");
            f();
        }
    }

    private void f() throws NumberFormatException {
        setLayout(new BorderLayout());
        this.f8471u = this.f8466i.b();
        this.f8459e.setOrientation(1);
        this.f8460f.setOrientation(0);
        try {
            if (this.f8466i instanceof dc) {
                this.f8461g = new P(this.f8467j, (dc) this.f8466i);
            } else {
                this.f8461g = new C1255s(this.f8467j, this.f8466i);
            }
            C1242f.a().a(this.f8461g);
        } catch (V.a e2) {
            bH.C.a("Error creating VE Analyze Console.", e2, this);
        }
        this.f8460f.setOneTouchExpandable(true);
        this.f8459e.setOneTouchExpandable(true);
        this.f8460f.setTopComponent(this.f8461g);
        C1324bf c1324bf = new C1324bf(this.f8467j, a(this.f8467j, this.f8466i));
        a(c1324bf);
        this.f8460f.setBottomComponent(c1324bf);
        if (C1806i.a().a("gd09ifdspokrwpo3209") || !C1798a.a().c(C1798a.f13410bN, C1798a.f13411bO)) {
            this.f8460f.remove(c1324bf);
            this.f8460f.setDividerSize(0);
            this.f8474o = true;
        }
        if (C1806i.a().a("h98oiu32lkpk3209") || !C1798a.a().c(C1798a.f13408bL, C1798a.f13409bM)) {
            add(BorderLayout.CENTER, this.f8460f);
            this.f8475p = true;
        } else {
            this.f8456b = new C1425x(this.f8467j);
            this.f8456b.setName("VeAnalyzeConsole." + this.f8466i.b());
            KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(this.f8456b.X());
            if (C1806i.a().a("64865e43s5hjhcurd")) {
                this.f8455a = new n.n();
                this.f8455a.setTabPlacement(3);
                this.f8459e.setTopComponent(this.f8455a);
                JPanel jPanel = new JPanel();
                jPanel.setLayout(new BorderLayout());
                jPanel.add(BorderLayout.CENTER, this.f8456b);
                this.f8455a.addTab(C1818g.b("Standard"), jPanel);
                this.f8456b.setMinimumSize(new Dimension(eJ.a(1), eJ.a(150)));
                this.f8457c = new C1425x(this.f8467j);
                this.f8457c.setName("VeAnalyzeConsole.Histogram." + this.f8466i.b());
                KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(this.f8457c.X());
                JPanel jPanel2 = new JPanel();
                jPanel2.setLayout(new BorderLayout());
                jPanel2.add(BorderLayout.CENTER, this.f8457c);
                this.f8455a.addTab(C1818g.b("Histogram"), jPanel2);
                this.f8457c.setMinimumSize(new Dimension(eJ.a(1), eJ.a(150)));
            } else {
                this.f8459e.setTopComponent(this.f8456b);
            }
            try {
                this.f8456b.a(new C1388aa().a(this.f8467j, aE.a.A(), "veAnalyze_" + this.f8471u, 6));
                this.f8456b.b(new C1388aa().a(this.f8467j, "veAnalyze_" + this.f8471u, 6));
                this.f8456b.n(aE.a.A().m());
                Component[] components = this.f8456b.getComponents();
                for (int i2 = 0; i2 < components.length; i2++) {
                    if (!(components[i2] instanceof Gauge) || ((Gauge) components[i2]).getOutputChannel().equals("veTuneValue")) {
                    }
                }
                if (this.f8457c != null) {
                    this.f8457c.a(new C1388aa().a(this.f8467j, aE.a.A(), "veAnalyze_" + this.f8471u + f8470l, 5));
                    this.f8457c.b(new C1388aa().a(this.f8467j, "veAnalyze_" + this.f8471u + f8470l, 5));
                    this.f8457c.n(aE.a.A().m());
                    Component[] components2 = this.f8457c.getComponents();
                    for (int i3 = 0; i3 < components2.length; i3++) {
                        if (!(components2[i3] instanceof Gauge) || ((Gauge) components2[i3]).getOutputChannel().equals("veTuneValue")) {
                        }
                    }
                    this.f8455a.g(this.f8458d.b("selectedDash", C1818g.b("Standard")));
                }
            } catch (Exception e3) {
                bV.d("Error setting gauges, check log for detail.\nMessage:\n" + e3.getMessage(), this);
                e3.printStackTrace();
            }
            this.f8459e.setBottomComponent(this.f8460f);
            add(BorderLayout.CENTER, this.f8459e);
        }
        this.f8461g.a(new JLabel("     "));
        JButton jButton = new JButton(null, new ImageIcon(eJ.a(Toolkit.getDefaultToolkit().getImage(getClass().getResource("fullscreen.png")).getScaledInstance(22, 22, 1), this)));
        jButton.setToolTipText(C1818g.b("Display VE Analyze Full Screen"));
        jButton.addActionListener(new C1246j(this));
        Dimension dimension = new Dimension(eJ.a(25), eJ.a(25));
        jButton.setPreferredSize(dimension);
        jButton.setMaximumSize(dimension);
        this.f8461g.a(jButton);
        g();
        this.f8473n = true;
    }

    @Override // n.InterfaceC1761a
    public boolean a() throws NumberFormatException {
        try {
            if (!this.f8473n && this.f8471u == null && (this.f8468k == null || this.f8468k.equals("") || C1007o.a(this.f8468k, this.f8467j))) {
                f();
                this.f8472m = true;
                return true;
            }
            if (!this.f8472m) {
                this.f8472m = true;
            }
            return true;
        } catch (V.g e2) {
            Logger.getLogger(C1245i.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            return true;
        }
    }

    private void g() throws NumberFormatException {
        h();
        this.f8459e.addPropertyChangeListener(JSplitPane.DIVIDER_LOCATION_PROPERTY, new C1247k(this));
        this.f8460f.addPropertyChangeListener(JSplitPane.DIVIDER_LOCATION_PROPERTY, new C1248l(this));
    }

    private void h() throws NumberFormatException {
        this.f8459e.setDividerLocation(eJ.a(Integer.parseInt(this.f8458d.b("horizontalSplitPos", "" + f8469t))));
        int height = this.f8460f.getHeight() - (this.f8460f.getPreferredSize().height - this.f8461g.getPreferredSize().height);
        if (height < 0) {
            height = (int) (this.f8461g.getPreferredSize().height * 1.2d);
        }
        double d2 = Double.parseDouble(this.f8458d.b("verticalSplitPos", "" + height));
        if (d2 > 1.0d) {
            this.f8460f.setDividerLocation(eJ.a((int) d2));
        } else {
            this.f8460f.setDividerLocation(height);
            bH.C.c("Tried to set horizontal divider to negative value, set to default." + height);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void i() {
        int dividerLocation = this.f8460f.getDividerLocation();
        double height = dividerLocation / this.f8460f.getHeight();
        if (!isVisible() || height <= 0.0d) {
            return;
        }
        this.f8458d.a("verticalSplitPos", eJ.b(dividerLocation) + "");
    }

    private C0084bq a(G.R r2, dk dkVar) {
        C0084bq c0084bq = new C0084bq();
        C0085br c0085brA = a(r2, dkVar.g());
        if (c0085brA != null) {
            c0084bq.a(c0085brA);
        }
        C0085br c0085brA2 = a(r2, dkVar.h());
        if (c0085brA2 != null) {
            c0084bq.a(c0085brA2);
        }
        C0085br c0085brA3 = a(r2, dkVar.f());
        if (c0085brA3 != null) {
            c0084bq.a(c0085brA3);
        }
        C0085br c0085brA4 = a(r2, dkVar.e());
        if (c0085brA4 != null) {
            c0084bq.a(c0085brA4);
        }
        return c0084bq;
    }

    private C0085br a(G.R r2, String str) {
        C0048ah c0048ahA = new C1388aa().a(r2, str);
        C0085br c0085br = new C0085br();
        if (c0048ahA != null) {
            try {
                c0085br.a(c0048ahA.i());
                c0085br.a(c0048ahA.a());
                c0085br.b(c0048ahA.d());
                c0085br.b(c0048ahA.j().a());
            } catch (V.g e2) {
                Logger.getLogger(C1245i.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            }
        } else {
            c0085br.b(true);
            c0085br.a(true);
            c0085br.a(str);
        }
        return c0085br;
    }

    @Override // com.efiAnalytics.ui.InterfaceC1565bc
    public void close() {
        super.l();
        if (this.f8461g instanceof InterfaceC1565bc) {
            ((InterfaceC1565bc) this.f8461g).close();
        }
        if (this.f8456b != null && this.f8456b.getComponentCount() > 0) {
            this.f8456b.f();
            new C1388aa().a(this.f8456b, this.f8462q, "veAnalyze_" + this.f8471u);
            this.f8456b.c();
            KeyboardFocusManager.getCurrentKeyboardFocusManager().removeKeyEventDispatcher(this.f8456b.X());
        }
        if (this.f8457c != null && this.f8457c.getComponentCount() > 0) {
            this.f8456b.f();
            new C1388aa().a(this.f8457c, this.f8462q, "veAnalyze_" + this.f8471u + f8470l);
            this.f8457c.c();
            KeyboardFocusManager.getCurrentKeyboardFocusManager().removeKeyEventDispatcher(this.f8457c.X());
        }
        if (this.f8455a != null) {
            this.f8458d.a("selectedDash", this.f8455a.getTitleAt(this.f8455a.getSelectedIndex()));
        }
        this.f8471u = null;
        removeAll();
    }

    public void a(aE.a aVar) {
        this.f8462q = aVar;
    }

    public boolean b() {
        return this.f8463r != null;
    }

    public void c() throws HeadlessException {
        if (b()) {
            return;
        }
        this.f8464s = getParent();
        double dividerLocation = this.f8460f.getDividerLocation() / (this.f8459e.getRightComponent().getHeight() - this.f8460f.getDividerSize());
        double dividerLocation2 = this.f8459e.getDividerLocation() / (this.f8459e.getWidth() - this.f8459e.getDividerSize());
        JDialog jDialog = new JDialog(bV.a(this));
        jDialog.setUndecorated(true);
        this.f8463r = jDialog;
        this.f8463r.setLayout(new BorderLayout());
        Point location = MouseInfo.getPointerInfo().getLocation();
        GraphicsDevice defaultScreenDevice = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        Rectangle rectangleA = C1630dn.a(location.f12370x, location.f12371y);
        this.f8463r.setBounds(rectangleA.f12372x, rectangleA.f12373y, rectangleA.width, rectangleA.height);
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new GridLayout(1, 1));
        jPanel.add(this);
        this.f8463r.add(BorderLayout.CENTER, jPanel);
        if (bH.I.c()) {
            bH.J.a(this.f8463r);
            bH.J.b(this.f8463r);
        } else if (!bH.I.a() && defaultScreenDevice.isFullScreenSupported()) {
            defaultScreenDevice.setFullScreenWindow(this.f8463r);
        }
        this.f8463r.setVisible(true);
        this.f8463r.validate();
        if (dividerLocation2 >= 0.0d && dividerLocation2 <= 1.0d) {
            this.f8459e.setDividerLocation(dividerLocation2);
        }
        if (dividerLocation >= 0.0d && dividerLocation <= 1.0d) {
            this.f8460f.setDividerLocation(dividerLocation);
        }
        this.f8463r.validate();
        this.f8465h = new C1249m(this);
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(this.f8465h);
    }

    public void d() {
        if (this.f8464s == null || this.f8463r == null) {
            return;
        }
        double dividerLocation = this.f8460f.getDividerLocation() / (this.f8459e.getRightComponent().getHeight() - this.f8460f.getDividerSize());
        double dividerLocation2 = this.f8459e.getDividerLocation() / (this.f8459e.getWidth() - (this.f8459e.getDividerSize() / 2));
        this.f8464s.add(this);
        doLayout();
        this.f8464s.validate();
        if (dividerLocation2 >= 0.0d && dividerLocation2 <= 1.0d) {
            this.f8459e.setDividerLocation(dividerLocation2);
        }
        if (dividerLocation >= 0.0d && dividerLocation <= 1.0d) {
            this.f8460f.setDividerLocation(dividerLocation);
        }
        if (bH.I.c()) {
        }
        this.f8463r.dispose();
        this.f8463r = null;
        if (this.f8465h != null) {
            KeyboardFocusManager.getCurrentKeyboardFocusManager().removeKeyEventDispatcher(this.f8465h);
        }
    }

    public void e() {
        if (this.f8463r == null) {
            c();
        } else {
            d();
        }
    }
}
