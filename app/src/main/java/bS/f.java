package bs;

import G.C0048ah;
import G.C0084bq;
import G.C0085br;
import G.R;
import G.bL;
import G.dn;
import bH.C1007o;
import bH.I;
import bH.J;
import bt.C1324bf;
import bt.C1345d;
import com.efiAnalytics.apps.ts.dashboard.C1388aa;
import com.efiAnalytics.apps.ts.dashboard.C1425x;
import com.efiAnalytics.apps.ts.dashboard.Gauge;
import com.efiAnalytics.ui.C1630dn;
import com.efiAnalytics.ui.InterfaceC1565bc;
import com.efiAnalytics.ui.bV;
import com.efiAnalytics.ui.dQ;
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

/* loaded from: TunerStudioMS.jar:bs/f.class */
public class f extends C1345d implements InterfaceC1565bc, InterfaceC1761a {

    /* renamed from: d, reason: collision with root package name */
    dQ f8583d;

    /* renamed from: i, reason: collision with root package name */
    dn f8591i;

    /* renamed from: j, reason: collision with root package name */
    R f8592j;

    /* renamed from: k, reason: collision with root package name */
    String f8593k;

    /* renamed from: l, reason: collision with root package name */
    static String f8594l = "_histogram";

    /* renamed from: a, reason: collision with root package name */
    n.n f8580a = null;

    /* renamed from: b, reason: collision with root package name */
    C1425x f8581b = null;

    /* renamed from: c, reason: collision with root package name */
    C1425x f8582c = null;

    /* renamed from: e, reason: collision with root package name */
    JSplitPane f8584e = new JSplitPane();

    /* renamed from: f, reason: collision with root package name */
    JSplitPane f8585f = new JSplitPane();

    /* renamed from: g, reason: collision with root package name */
    k f8586g = null;

    /* renamed from: q, reason: collision with root package name */
    private aE.a f8587q = null;

    /* renamed from: r, reason: collision with root package name */
    private Window f8588r = null;

    /* renamed from: s, reason: collision with root package name */
    private Container f8589s = null;

    /* renamed from: h, reason: collision with root package name */
    j f8590h = null;

    /* renamed from: t, reason: collision with root package name */
    private String f8595t = null;

    /* renamed from: m, reason: collision with root package name */
    boolean f8596m = false;

    /* renamed from: n, reason: collision with root package name */
    boolean f8597n = false;

    /* renamed from: o, reason: collision with root package name */
    boolean f8598o = false;

    /* renamed from: p, reason: collision with root package name */
    boolean f8599p = false;

    public f(R r2, dn dnVar) throws NumberFormatException {
        this.f8583d = null;
        this.f8591i = null;
        this.f8592j = null;
        this.f8593k = null;
        this.f8592j = r2;
        this.f8591i = dnVar;
        this.f8593k = bL.a(r2, dnVar.c());
        this.f8583d = new dQ(aE.a.A(), "WueAnalyzeConsole." + dnVar.c());
        try {
            if (this.f8593k == null || this.f8593k.equals("") || C1007o.a(this.f8593k, r2)) {
                f();
            }
        } catch (V.g e2) {
            bH.C.b("Unable to determine if table " + dnVar.c() + " should be active, so defaulting to yes...");
            f();
        }
    }

    private void f() throws NumberFormatException {
        setLayout(new BorderLayout());
        this.f8595t = this.f8591i.c();
        this.f8584e.setOrientation(1);
        this.f8585f.setOrientation(0);
        try {
            this.f8586g = new k(this.f8592j, this.f8591i);
        } catch (V.g e2) {
            Logger.getLogger(f.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
        }
        this.f8585f.setOneTouchExpandable(true);
        this.f8584e.setOneTouchExpandable(true);
        this.f8585f.setTopComponent(this.f8586g);
        C1324bf c1324bf = new C1324bf(this.f8592j, a(this.f8592j, this.f8591i));
        a(c1324bf);
        this.f8585f.setBottomComponent(c1324bf);
        if (C1806i.a().a("gd09ifdspokrwpo3209") || !C1798a.a().c(C1798a.f13410bN, C1798a.f13411bO)) {
            this.f8585f.remove(c1324bf);
            this.f8585f.setDividerSize(0);
            this.f8598o = true;
        }
        if (C1806i.a().a("h98oiu32lkpk3209") || !C1798a.a().c(C1798a.f13408bL, C1798a.f13409bM)) {
            add(BorderLayout.CENTER, this.f8585f);
            this.f8599p = true;
        } else {
            this.f8581b = new C1425x(this.f8592j);
            this.f8581b.setName("WueAnalyzeConsole." + this.f8591i.c());
            if (C1806i.a().a("64865e43s5hjhcurd")) {
                this.f8580a = new n.n();
                this.f8580a.setTabPlacement(3);
                this.f8584e.setTopComponent(this.f8580a);
                JPanel jPanel = new JPanel();
                jPanel.setLayout(new BorderLayout());
                jPanel.add(BorderLayout.CENTER, this.f8581b);
                this.f8580a.addTab(C1818g.b("Standard"), jPanel);
                this.f8581b.setMinimumSize(new Dimension(1, 150));
                this.f8582c = new C1425x(this.f8592j);
                this.f8582c.setName("WueAnalyzeConsole.Histogram." + this.f8591i.c());
                JPanel jPanel2 = new JPanel();
                jPanel2.setLayout(new BorderLayout());
                jPanel2.add(BorderLayout.CENTER, this.f8582c);
                this.f8580a.addTab(C1818g.b("Histogram"), jPanel2);
                this.f8582c.setMinimumSize(new Dimension(1, 150));
            } else {
                this.f8584e.setTopComponent(this.f8581b);
            }
            try {
                this.f8581b.a(new C1388aa().a(this.f8592j, aE.a.A(), "wueAnalyze_" + this.f8595t, 6));
                this.f8581b.b(new C1388aa().a(this.f8592j, "wueAnalyze_" + this.f8595t, 6));
                this.f8581b.n(aE.a.A().m());
                Component[] components = this.f8581b.getComponents();
                for (int i2 = 0; i2 < components.length; i2++) {
                    if (!(components[i2] instanceof Gauge) || ((Gauge) components[i2]).getOutputChannel().equals("veTuneValue")) {
                    }
                }
                if (this.f8582c != null) {
                    this.f8582c.a(new C1388aa().a(this.f8592j, aE.a.A(), "wueAnalyze_" + this.f8595t + f8594l, 5));
                    this.f8582c.b(new C1388aa().a(this.f8592j, "wueAnalyze_" + this.f8595t + f8594l, 5));
                    this.f8582c.n(aE.a.A().m());
                    Component[] components2 = this.f8582c.getComponents();
                    for (int i3 = 0; i3 < components2.length; i3++) {
                        if (!(components2[i3] instanceof Gauge) || ((Gauge) components2[i3]).getOutputChannel().equals("veTuneValue")) {
                        }
                    }
                    this.f8580a.g(this.f8583d.b("selectedDash", C1818g.b("Standard")));
                }
            } catch (Exception e3) {
                bV.d("Error setting gauges, check log for detail.\nMessage:\n" + e3.getMessage(), this);
                e3.printStackTrace();
            }
            this.f8584e.setBottomComponent(this.f8585f);
            add(BorderLayout.CENTER, this.f8584e);
        }
        this.f8586g.a(new JLabel("     "));
        JButton jButton = new JButton(null, new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource("fullscreen.png")).getScaledInstance(22, 22, 1)));
        jButton.setToolTipText(C1818g.b("Display VE Analyze Full Screen"));
        jButton.addActionListener(new g(this));
        Dimension dimension = new Dimension(25, 25);
        jButton.setPreferredSize(dimension);
        jButton.setMaximumSize(dimension);
        this.f8586g.a(jButton);
        g();
        this.f8597n = true;
    }

    @Override // n.InterfaceC1761a
    public boolean a() throws NumberFormatException {
        try {
            if (!this.f8597n || (this.f8595t == null && (this.f8593k == null || this.f8593k.equals("") || C1007o.a(this.f8593k, this.f8592j)))) {
                f();
                this.f8596m = true;
                return true;
            }
            if (!this.f8596m) {
                this.f8596m = true;
            }
            return true;
        } catch (V.a e2) {
            bV.d(e2.getLocalizedMessage(), this);
            return true;
        } catch (V.g e3) {
            Logger.getLogger(f.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e3);
            return true;
        }
    }

    private void g() throws NumberFormatException {
        h();
        this.f8584e.addPropertyChangeListener(JSplitPane.DIVIDER_LOCATION_PROPERTY, new h(this));
        this.f8585f.addPropertyChangeListener(JSplitPane.DIVIDER_LOCATION_PROPERTY, new i(this));
    }

    private void h() throws NumberFormatException {
        this.f8584e.setDividerLocation(Integer.parseInt(this.f8583d.b("horizontalSplitPos", "200")));
        int height = this.f8585f.getHeight() - (this.f8585f.getPreferredSize().height - this.f8586g.getPreferredSize().height);
        if (height < 0) {
            height = (int) (this.f8586g.getPreferredSize().height * 1.2d);
        }
        double d2 = Double.parseDouble(this.f8583d.b("verticalSplitPos", "" + height));
        if (d2 > 1.0d) {
            this.f8585f.setDividerLocation((int) d2);
        } else {
            this.f8585f.setDividerLocation(height);
            bH.C.c("Tried to set horizontal divider to negative value, set to default." + height);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void i() {
        int dividerLocation = this.f8585f.getDividerLocation();
        double height = dividerLocation / this.f8585f.getHeight();
        if (!isVisible() || height <= 0.0d) {
            return;
        }
        this.f8583d.a("verticalSplitPos", dividerLocation + "");
    }

    private C0084bq a(R r2, dn dnVar) {
        C0085br c0085brA;
        C0085br c0085brA2;
        C0084bq c0084bq = new C0084bq();
        C0085br c0085brA3 = a(r2, dnVar.d());
        if (c0085brA3 != null) {
            c0084bq.a(c0085brA3);
        }
        C0085br c0085brA4 = a(r2, dnVar.e());
        if (c0085brA4 != null) {
            c0084bq.a(c0085brA4);
        }
        if (dnVar.k() != null && (c0085brA2 = a(r2, dnVar.k())) != null) {
            c0084bq.a(c0085brA2);
        }
        if (dnVar.h() != null && (c0085brA = a(r2, dnVar.h())) != null) {
            c0084bq.a(c0085brA);
        }
        return c0084bq;
    }

    private C0085br a(R r2, String str) {
        C0048ah c0048ahA = new C1388aa().a(r2, str);
        C0085br c0085br = new C0085br();
        if (c0048ahA != null) {
            c0085br.a(c0048ahA.i());
            c0085br.a(c0048ahA.a());
            c0085br.b(c0048ahA.d());
            c0085br.b(c0048ahA.j().toString());
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
        if (this.f8586g != null) {
            this.f8586g.close();
        }
        if (this.f8581b != null && this.f8581b.getComponentCount() > 0) {
            this.f8581b.f();
            new C1388aa().a(this.f8581b, this.f8587q, "wueAnalyze_" + this.f8595t);
            this.f8581b.c();
        }
        if (this.f8582c != null && this.f8582c.getComponentCount() > 0) {
            this.f8581b.f();
            new C1388aa().a(this.f8582c, this.f8587q, "wueAnalyze_" + this.f8595t + f8594l);
            this.f8582c.c();
        }
        if (this.f8580a != null) {
            this.f8583d.a("selectedDash", this.f8580a.getTitleAt(this.f8580a.getSelectedIndex()));
        }
        this.f8595t = null;
        removeAll();
    }

    public void a(aE.a aVar) {
        this.f8587q = aVar;
    }

    public boolean b() {
        return this.f8588r != null;
    }

    public void c() throws HeadlessException {
        if (b()) {
            return;
        }
        this.f8589s = getParent();
        double dividerLocation = this.f8585f.getDividerLocation() / (this.f8584e.getRightComponent().getHeight() - this.f8585f.getDividerSize());
        double dividerLocation2 = this.f8584e.getDividerLocation() / (this.f8584e.getWidth() - this.f8584e.getDividerSize());
        JDialog jDialog = new JDialog(bV.a(this));
        jDialog.setUndecorated(true);
        this.f8588r = jDialog;
        this.f8588r.setLayout(new BorderLayout());
        Point location = MouseInfo.getPointerInfo().getLocation();
        GraphicsDevice defaultScreenDevice = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        Rectangle rectangleA = C1630dn.a(location.f12370x, location.f12371y);
        this.f8588r.setBounds(rectangleA.f12372x, rectangleA.f12373y, rectangleA.width, rectangleA.height);
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new GridLayout(1, 1));
        jPanel.add(this);
        this.f8588r.add(BorderLayout.CENTER, jPanel);
        if (I.c()) {
            J.a(this.f8588r);
            J.b(this.f8588r);
        } else if (I.a() || !defaultScreenDevice.isFullScreenSupported()) {
            this.f8588r.setVisible(true);
        } else {
            defaultScreenDevice.setFullScreenWindow(this.f8588r);
        }
        if (dividerLocation2 >= 0.0d && dividerLocation2 <= 1.0d) {
            this.f8584e.setDividerLocation(dividerLocation2);
        }
        if (dividerLocation >= 0.0d && dividerLocation <= 1.0d) {
            this.f8585f.setDividerLocation(dividerLocation);
        }
        this.f8588r.validate();
        this.f8590h = new j(this);
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(this.f8590h);
    }

    public void d() {
        if (this.f8589s == null || this.f8588r == null) {
            return;
        }
        double dividerLocation = this.f8585f.getDividerLocation() / (this.f8584e.getRightComponent().getHeight() - this.f8585f.getDividerSize());
        double dividerLocation2 = this.f8584e.getDividerLocation() / (this.f8584e.getWidth() - (this.f8584e.getDividerSize() / 2));
        this.f8589s.add(this);
        doLayout();
        this.f8589s.validate();
        if (dividerLocation2 >= 0.0d && dividerLocation2 <= 1.0d) {
            this.f8584e.setDividerLocation(dividerLocation2);
        }
        if (dividerLocation >= 0.0d && dividerLocation <= 1.0d) {
            this.f8585f.setDividerLocation(dividerLocation);
        }
        if (I.c()) {
        }
        this.f8588r.dispose();
        this.f8588r = null;
        if (this.f8590h != null) {
            KeyboardFocusManager.getCurrentKeyboardFocusManager().removeKeyEventDispatcher(this.f8590h);
        }
    }

    public void e() {
        if (this.f8588r == null) {
            c();
        } else {
            d();
        }
    }
}
