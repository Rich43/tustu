package t;

import com.efiAnalytics.apps.ts.dashboard.AbstractC1420s;
import com.efiAnalytics.apps.ts.dashboard.Gauge;
import com.efiAnalytics.apps.ts.dashboard.Indicator;
import com.efiAnalytics.apps.ts.dashboard.InterfaceC1407f;
import com.efiAnalytics.ui.C1566bd;
import com.efiAnalytics.ui.bV;
import com.efiAnalytics.ui.eJ;
import com.sun.org.apache.xml.internal.security.utils.Constants;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;
import java.util.ArrayList;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import o.C1765a;
import r.C1807j;
import s.C1818g;

/* renamed from: t.w, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:t/w.class */
public class C1875w extends JMenu implements InterfaceC1407f {

    /* renamed from: a, reason: collision with root package name */
    C1836ai f13910a;

    /* renamed from: b, reason: collision with root package name */
    aA f13911b;

    /* renamed from: c, reason: collision with root package name */
    bi f13912c;

    /* renamed from: d, reason: collision with root package name */
    aO f13913d;

    /* renamed from: e, reason: collision with root package name */
    aK f13914e;

    /* renamed from: f, reason: collision with root package name */
    aF f13915f;

    /* renamed from: g, reason: collision with root package name */
    be f13916g;

    /* renamed from: h, reason: collision with root package name */
    C1846as f13917h;

    /* renamed from: i, reason: collision with root package name */
    C1870r f13918i;

    /* renamed from: ak, reason: collision with root package name */
    private C1566bd f13919ak;

    /* renamed from: al, reason: collision with root package name */
    private bn f13920al;

    /* renamed from: am, reason: collision with root package name */
    private bn f13921am;

    /* renamed from: an, reason: collision with root package name */
    private bn f13922an;

    /* renamed from: ao, reason: collision with root package name */
    private String f13923ao;

    /* renamed from: j, reason: collision with root package name */
    JCheckBoxMenuItem f13924j;

    /* renamed from: k, reason: collision with root package name */
    JCheckBoxMenuItem f13925k;

    /* renamed from: l, reason: collision with root package name */
    JCheckBoxMenuItem f13926l;

    /* renamed from: m, reason: collision with root package name */
    JCheckBoxMenuItem f13927m;

    /* renamed from: n, reason: collision with root package name */
    JCheckBoxMenuItem f13928n;

    /* renamed from: o, reason: collision with root package name */
    JCheckBoxMenuItem f13929o;

    /* renamed from: p, reason: collision with root package name */
    C1849av f13930p;

    /* renamed from: q, reason: collision with root package name */
    C1856d f13931q;

    /* renamed from: r, reason: collision with root package name */
    public static String f13932r = C1818g.b("Value");

    /* renamed from: s, reason: collision with root package name */
    public static String f13933s = C1818g.b("Component ID");

    /* renamed from: t, reason: collision with root package name */
    public static String f13934t = C1818g.b("Face Angle");

    /* renamed from: u, reason: collision with root package name */
    public static String f13935u = C1818g.b("Face Start Angle");

    /* renamed from: v, reason: collision with root package name */
    public static String f13936v = C1818g.b("Needle Start Angle");

    /* renamed from: w, reason: collision with root package name */
    public static String f13937w = C1818g.b("Sweep Angle");

    /* renamed from: x, reason: collision with root package name */
    public static String f13938x = C1818g.b("Border Thickness");

    /* renamed from: y, reason: collision with root package name */
    public static String f13939y = C1818g.b("Font Size");

    /* renamed from: z, reason: collision with root package name */
    public static String f13940z = C1818g.b("Needle Smoothing Method");

    /* renamed from: A, reason: collision with root package name */
    public static String f13941A = C1818g.b("Border Color");

    /* renamed from: B, reason: collision with root package name */
    public static String f13942B = C1818g.b("Face Color");

    /* renamed from: C, reason: collision with root package name */
    public static String f13943C = C1818g.b("Font Color");

    /* renamed from: D, reason: collision with root package name */
    public static String f13944D = C1818g.b("Needle Color");

    /* renamed from: E, reason: collision with root package name */
    public static String f13945E = C1818g.b("Warning Color");

    /* renamed from: F, reason: collision with root package name */
    public static String f13946F = C1818g.b("Critical Color");

    /* renamed from: G, reason: collision with root package name */
    public static String f13947G = C1818g.b("Color Dialog");

    /* renamed from: H, reason: collision with root package name */
    public static String f13948H = C1818g.b("Decimal Places");

    /* renamed from: I, reason: collision with root package name */
    public static String f13949I = C1818g.b("History tell tail");

    /* renamed from: J, reason: collision with root package name */
    public static String f13950J = C1818g.b("Counter Clockwise Rotation");

    /* renamed from: K, reason: collision with root package name */
    public static String f13951K = C1818g.b("Show Value at 180 degrees");

    /* renamed from: L, reason: collision with root package name */
    public static String f13952L = C1818g.b("Peg Needle at Limits");

    /* renamed from: M, reason: collision with root package name */
    public static String f13953M = C1818g.b("Needle Movement Smoothing");

    /* renamed from: N, reason: collision with root package name */
    public static String f13954N = C1818g.b("None");

    /* renamed from: O, reason: collision with root package name */
    public static String f13955O = C1818g.b("Time Based Averaging");

    /* renamed from: P, reason: collision with root package name */
    public static String f13956P = C1818g.b("On Text Color");

    /* renamed from: Q, reason: collision with root package name */
    public static String f13957Q = C1818g.b("Off Text Color");

    /* renamed from: R, reason: collision with root package name */
    public static String f13958R = C1818g.b("On Background Color");

    /* renamed from: S, reason: collision with root package name */
    public static String f13959S = C1818g.b("Off Background Color");

    /* renamed from: T, reason: collision with root package name */
    public static String f13960T = C1818g.b("Gauge Limits & Values");

    /* renamed from: U, reason: collision with root package name */
    public static String f13961U = C1818g.b("Gauge Text");

    /* renamed from: V, reason: collision with root package name */
    public static String f13962V = C1818g.b("Gauge Colors");

    /* renamed from: W, reason: collision with root package name */
    public static String f13963W = C1818g.b("Gauge Angles");

    /* renamed from: X, reason: collision with root package name */
    public static String f13964X = C1818g.b("Indicator Colors");

    /* renamed from: Y, reason: collision with root package name */
    public static String f13965Y = C1818g.b("Indicator Text");

    /* renamed from: Z, reason: collision with root package name */
    public static String f13966Z = C1818g.b("Background Image");

    /* renamed from: aa, reason: collision with root package name */
    public static String f13967aa = C1818g.b("Needle Image");

    /* renamed from: ab, reason: collision with root package name */
    public static String f13968ab = C1818g.b("On Image");

    /* renamed from: ac, reason: collision with root package name */
    public static String f13969ac = C1818g.b("Off Image");

    /* renamed from: ad, reason: collision with root package name */
    public static String f13970ad = C1818g.b("Set Text");

    /* renamed from: ae, reason: collision with root package name */
    public static String f13971ae = C1818g.b("Size and Position");

    /* renamed from: af, reason: collision with root package name */
    public static String f13972af = C1818g.b("Background Color");

    /* renamed from: ag, reason: collision with root package name */
    public static String f13973ag = C1818g.b("Text Color");

    /* renamed from: ah, reason: collision with root package name */
    int f13974ah;

    /* renamed from: ai, reason: collision with root package name */
    int f13975ai;

    /* renamed from: aj, reason: collision with root package name */
    ButtonGroup f13976aj;

    public C1875w() {
        super(C1818g.b("Gauge Properties"));
        this.f13910a = new C1836ai();
        this.f13911b = null;
        this.f13912c = null;
        this.f13913d = null;
        this.f13914e = null;
        this.f13915f = null;
        this.f13916g = null;
        this.f13917h = null;
        this.f13918i = null;
        this.f13919ak = null;
        this.f13920al = null;
        this.f13921am = null;
        this.f13922an = null;
        this.f13923ao = "";
        this.f13924j = null;
        this.f13925k = null;
        this.f13926l = null;
        this.f13927m = null;
        this.f13928n = null;
        this.f13929o = null;
        this.f13930p = null;
        this.f13931q = null;
        this.f13974ah = eJ.a(-10);
        this.f13975ai = eJ.a(10);
        this.f13976aj = new ButtonGroup();
    }

    public void b(ArrayList arrayList) {
        this.f13910a.a(arrayList);
        removeAll();
        if (arrayList != null && arrayList.size() > 0) {
            f();
            addSeparator();
            if (this.f13910a.b(arrayList)) {
                h();
                if (this.f13910a.c(arrayList)) {
                    addSeparator();
                }
            }
            if (this.f13910a.c(arrayList)) {
                i();
            }
            if (this.f13910a.d(arrayList)) {
                g();
            }
        }
        if (this.f13913d != null && arrayList.size() <= 1) {
            this.f13913d.e(arrayList);
        }
        if (this.f13914e != null && arrayList.size() <= 1) {
            this.f13914e.e(arrayList);
        }
        if (this.f13915f != null && arrayList.size() <= 1) {
            this.f13915f.e(arrayList);
        }
        if (this.f13911b != null && arrayList.size() <= 1) {
            this.f13911b.e(arrayList);
        }
        if (this.f13912c != null && arrayList.size() <= 1) {
            this.f13912c.e(arrayList);
        }
        if (this.f13916g != null && arrayList.size() <= 1) {
            this.f13916g.e(arrayList);
        }
        if (this.f13917h != null && arrayList.size() <= 1) {
            this.f13917h.e(arrayList);
        }
        if (this.f13918i != null) {
            this.f13918i.e(arrayList);
        }
        if (this.f13924j != null && arrayList.size() >= 1 && (arrayList.get(0) instanceof Gauge)) {
            Gauge gauge = (Gauge) arrayList.get(0);
            this.f13924j.setState(gauge.isShowHistory());
            this.f13924j.setText(C1818g.b(f13949I) + " (" + (gauge.getHistoryDelay() / 1000) + "s.)");
        }
        if (this.f13929o != null && arrayList.size() >= 1 && (arrayList.get(0) instanceof Gauge)) {
            this.f13929o.setState(((Gauge) arrayList.get(0)).getNeedleSmoothing() == Gauge.f9324G);
        }
        if (this.f13928n != null && arrayList.size() >= 1 && (arrayList.get(0) instanceof Gauge)) {
            this.f13928n.setState(((Gauge) arrayList.get(0)).getNeedleSmoothing() == Gauge.f9323F);
        }
        if (this.f13925k != null && arrayList.size() >= 1 && (arrayList.get(0) instanceof Gauge)) {
            this.f13925k.setState(((Gauge) arrayList.get(0)).isCounterClockwise());
        }
        if (this.f13926l != null && arrayList.size() >= 1 && (arrayList.get(0) instanceof Gauge)) {
            this.f13926l.setState(((Gauge) arrayList.get(0)).isDisplayValueAt180());
        }
        if (this.f13927m != null && arrayList.size() >= 1 && (arrayList.get(0) instanceof Gauge)) {
            this.f13927m.setState(((Gauge) arrayList.get(0)).isPegLimits());
        }
        if (this.f13922an != null && arrayList.size() >= 1 && (arrayList.get(0) instanceof Gauge)) {
            this.f13922an.a(((Gauge) arrayList.get(0)).valueDigits(), 0, 5, "Display Digits");
        }
        if (this.f13920al != null && arrayList.size() >= 1 && (arrayList.get(0) instanceof Gauge)) {
            this.f13920al.a(((Gauge) arrayList.get(0)).getBorderWidth(), 0, 25, "Border Width");
        }
        if (this.f13921am != null && arrayList.size() >= 1 && (arrayList.get(0) instanceof Gauge)) {
            this.f13921am.a(((Gauge) arrayList.get(0)).getFontSizeAdjustment(), this.f13974ah, this.f13975ai, "Font Size");
        }
        if (this.f13930p != null) {
            this.f13930p.a(arrayList);
        }
        if (this.f13931q != null) {
            this.f13931q.a(this.f13910a.a());
        }
        this.f13923ao = "";
    }

    private void f() {
        new C1876x(this);
        JMenuItem jMenuItem = new JMenuItem(C1818g.b(f13933s));
        jMenuItem.addActionListener(new I(this));
        add(jMenuItem);
        JMenuItem jMenuItem2 = new JMenuItem(C1818g.b("Properties Dialog"));
        jMenuItem2.addActionListener(new T(this));
        add(jMenuItem2);
        JMenuItem jMenuItem3 = new JMenuItem(f13947G);
        jMenuItem3.addActionListener(new C1829ab(this));
        add(jMenuItem3);
        C1765a c1765a = new C1765a(C1818g.b("Set Single Click Action"), this.f13910a.a());
        c1765a.a(new C1830ac(this));
        add((JMenuItem) c1765a);
        C1765a c1765a2 = new C1765a(C1818g.b("Set Long Click Action"), this.f13910a.a());
        c1765a2.a(new C1831ad(this));
        add((JMenuItem) c1765a2);
    }

    private void g() {
        C1832ae c1832ae = new C1832ae(this);
        add(f13972af).addActionListener(c1832ae);
        add(f13973ag).addActionListener(c1832ae);
        add(f13970ad).addActionListener(c1832ae);
    }

    private void h() {
        C1833af c1833af = new C1833af(this);
        add(f13968ab).addActionListener(c1833af);
        add(f13969ac).addActionListener(c1833af);
    }

    private void i() {
        new C1834ag(this);
        JMenuItem jMenuItem = new JMenuItem(C1818g.b(f13966Z));
        jMenuItem.setActionCommand(f13966Z);
        jMenuItem.addActionListener(new C1877y(this));
        add(jMenuItem);
        JMenuItem jMenuItem2 = new JMenuItem(C1818g.b(f13967aa));
        jMenuItem2.setActionCommand(f13967aa);
        jMenuItem2.addActionListener(new C1878z(this));
        add(jMenuItem2);
        this.f13924j = new JCheckBoxMenuItem(f13949I);
        this.f13924j.addActionListener(new C1822A(this));
        add(this.f13924j);
        this.f13925k = new JCheckBoxMenuItem(f13950J);
        this.f13925k.addActionListener(new C1823B(this));
        add(this.f13925k);
        this.f13928n = new JCheckBoxMenuItem(f13954N);
        this.f13928n.addActionListener(new C1824C(this));
        this.f13929o = new JCheckBoxMenuItem(f13955O);
        this.f13929o.addActionListener(new C1825D(this));
        this.f13976aj.add(this.f13928n);
        this.f13976aj.add(this.f13929o);
        JMenu jMenu = new JMenu(f13953M);
        add((JMenuItem) jMenu);
        jMenu.add((JMenuItem) this.f13928n);
        jMenu.add((JMenuItem) this.f13929o);
        this.f13926l = new JCheckBoxMenuItem(f13951K);
        this.f13926l.addActionListener(new C1826E(this));
        add(this.f13926l);
        this.f13927m = new JCheckBoxMenuItem(f13952L);
        this.f13927m.addActionListener(new F(this));
        add(this.f13927m);
    }

    protected void a(boolean z2) {
        this.f13910a.c(z2);
    }

    protected void b(boolean z2) {
        this.f13910a.d(z2);
    }

    protected void c(boolean z2) {
        this.f13910a.e(z2);
    }

    protected void d(boolean z2) {
        this.f13910a.b(z2);
    }

    protected void a(String str) {
        if (str == null) {
            bH.C.a("DashCompPropertiesMenu::menuItemClicked called with null for command, that shouldn't happen");
            return;
        }
        if (str.equals(f13944D)) {
            e().a(new G(this));
            Gauge gauge = (Gauge) this.f13910a.a().get(0);
            if (a(gauge, f13944D)) {
                e().a(gauge, C1818g.b("Select") + " " + f13944D, gauge.getNeedleColor());
                return;
            }
            return;
        }
        if (str.equals(f13941A)) {
            e().a(new H(this));
            Gauge gauge2 = (Gauge) this.f13910a.a().get(0);
            if (a(gauge2, f13941A)) {
                e().a(gauge2, C1818g.b("Select") + " " + f13941A, gauge2.getTrimColor());
                return;
            }
            return;
        }
        if (str.equals(f13942B)) {
            e().a(new J(this));
            Gauge gauge3 = (Gauge) this.f13910a.a().get(0);
            if (a(gauge3, f13942B)) {
                e().a(gauge3, C1818g.b("Select") + " " + f13942B, gauge3.getBackColor());
                return;
            }
            return;
        }
        if (str.equals(f13943C)) {
            e().a(new K(this));
            Gauge gauge4 = (Gauge) this.f13910a.a().get(0);
            if (a(gauge4, f13943C)) {
                e().a(gauge4, C1818g.b("Select") + " " + f13943C, gauge4.getFontColor());
                return;
            }
            return;
        }
        if (str.equals(f13945E)) {
            e().a(new L(this));
            Gauge gauge5 = (Gauge) this.f13910a.a().get(0);
            if (a(gauge5, f13945E)) {
                e().a(gauge5, C1818g.b("Select") + " " + f13945E, gauge5.getWarnColor());
                return;
            }
            return;
        }
        if (str.equals(f13946F)) {
            e().a(new M(this));
            Gauge gauge6 = (Gauge) this.f13910a.a().get(0);
            if (a(gauge6, f13946F)) {
                e().a(gauge6, C1818g.b("Select") + " " + f13946F, gauge6.getCriticalColor());
                return;
            }
            return;
        }
        if (str.equals(f13956P)) {
            e().a(new N(this));
            Indicator indicator = (Indicator) this.f13910a.a().get(0);
            if (a(indicator, f13956P)) {
                e().a(indicator, C1818g.b("Select") + " " + f13956P, indicator.getOnTextColor());
                return;
            }
            return;
        }
        if (str.equals(f13957Q)) {
            e().a(new O(this));
            Indicator indicator2 = (Indicator) this.f13910a.a().get(0);
            if (a(indicator2, f13957Q)) {
                e().a(indicator2, C1818g.b("Select") + " " + f13957Q, indicator2.getOffTextColor());
                return;
            }
            return;
        }
        if (str.equals(f13958R)) {
            e().a(new P(this));
            Indicator indicator3 = (Indicator) this.f13910a.a().get(0);
            if (a(indicator3, f13958R)) {
                e().a(indicator3, C1818g.b("Select") + " " + f13958R, indicator3.getOnBackgroundColor());
                return;
            }
            return;
        }
        if (str.equals(f13959S)) {
            e().a(new Q(this));
            Indicator indicator4 = (Indicator) this.f13910a.a().get(0);
            if (a(indicator4, f13959S)) {
                e().a(indicator4, C1818g.b("Select") + " " + f13959S, indicator4.getOffBackgroundColor());
                return;
            }
            return;
        }
        if (str.equals(f13938x)) {
            Gauge gauge7 = (Gauge) this.f13910a.a().get(0);
            this.f13920al = a(gauge7, gauge7.getBorderWidth(), 0, 25, f13938x);
            return;
        }
        if (str.equals(f13966Z)) {
            p();
            return;
        }
        if (str.equals(f13967aa)) {
            q();
            return;
        }
        if (str.equals(f13969ac)) {
            s();
            return;
        }
        if (str.equals(f13968ab)) {
            r();
            return;
        }
        if (str.equals(f13939y)) {
            Gauge gauge8 = (Gauge) this.f13910a.a().get(0);
            this.f13921am = b(gauge8, gauge8.getFontSizeAdjustment(), this.f13974ah, this.f13975ai, f13939y);
            return;
        }
        if (str.equals(f13960T)) {
            j();
            return;
        }
        if (str.equals(f13961U)) {
            k();
            return;
        }
        if (str.equals(f13965Y)) {
            m();
            return;
        }
        if (str.equals(f13963W)) {
            l();
            return;
        }
        if (str.equals(f13948H)) {
            b();
            return;
        }
        if (str.equals(f13972af)) {
            e().a(new R(this));
            return;
        }
        if (str.equals(f13973ag)) {
            e().a(new S(this));
            return;
        }
        if (str.equals(f13970ad)) {
            n();
        } else if (str.equals(f13971ae)) {
            o();
        } else if (str.equals(f13933s)) {
            a();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean a(Component component, String str) {
        if (this.f13910a.a().size() <= 1 || this.f13923ao.equals(str)) {
            return true;
        }
        this.f13923ao = str;
        return true;
    }

    public void a() {
        if (this.f13910a.a().size() > 0) {
            String strA = bV.a((Component) this.f13910a.a().get(0), false, C1818g.b("Set Dashboard Component ID (not Required)"), ((AbstractC1420s) this.f13910a.a().get(0)).getId());
            if (strA == null || strA.trim().length() <= 0) {
                return;
            }
            this.f13910a.h(strA);
        }
    }

    protected void b() throws IllegalArgumentException {
        Gauge gauge = (Gauge) this.f13910a.a().get(0);
        if (this.f13922an == null) {
            this.f13922an = new bn(bV.b(gauge), C1818g.b("Display Digits"));
            this.f13922an.a(gauge.getLabelDigits(), 0, 5, C1818g.b("Display Digits"));
            this.f13922an.a(new C1835ah(this, this.f13922an.getTitle()));
            this.f13922an.pack();
            a(this.f13922an, (Component) this.f13910a.a().get(0));
        }
        this.f13922an.a(gauge.getLabelDigits(), 0, 5, C1818g.b("Display Digits"));
        this.f13922an.a(new U(this));
        this.f13922an.setVisible(true);
    }

    public static void a(AbstractC1827a abstractC1827a, Component component) {
        int x2 = component.getX() + (component.getWidth() / 2);
        int y2 = component.getY();
        try {
            C1835ah c1835ah = (C1835ah) abstractC1827a.b();
            String strB = c1835ah.b("X", x2 + "");
            String strB2 = c1835ah.b(Constants._TAG_Y, x2 + "");
            x2 = Integer.parseInt(strB);
            y2 = Integer.parseInt(strB2);
        } catch (Exception e2) {
        }
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int i2 = x2 < 0 ? 0 : x2;
        int width = i2 > screenSize.width - abstractC1827a.getWidth() ? screenSize.width - abstractC1827a.getWidth() : i2;
        int i3 = y2 < 0 ? 0 : y2;
        abstractC1827a.setLocation(width, i3 > screenSize.height - abstractC1827a.getHeight() ? screenSize.height - abstractC1827a.getHeight() : i3);
    }

    protected void c() {
        if (this.f13910a.a().isEmpty()) {
            return;
        }
        if (this.f13931q == null) {
            this.f13931q = new C1856d(bV.b((Component) this.f13910a.a().get(0)), this.f13910a, C1818g.b("Component Colors"));
            this.f13931q.pack();
            a(this.f13931q, (Component) this.f13910a.a().get(0));
        }
        this.f13931q.a(this.f13910a.a());
        this.f13931q.setVisible(true);
    }

    protected void d() {
        if (this.f13910a.a().isEmpty()) {
            return;
        }
        if (this.f13930p == null) {
            this.f13930p = new C1849av(bV.a((Component) this.f13910a.a().get(0)), this.f13910a, C1818g.b("Properties"));
            this.f13930p.a(new C1835ah(this, "DashComp " + this.f13930p.getTitle()));
            this.f13930p.pack();
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            if (this.f13930p.getHeight() > screenSize.height - 50) {
                this.f13930p.setSize(this.f13930p.getWidth() + 40, screenSize.height - 50);
            }
            a(this.f13930p, (Component) this.f13910a.a().get(0));
        }
        this.f13930p.a(this.f13910a.a());
        this.f13930p.setVisible(true);
    }

    protected bn a(Component component, int i2, int i3, int i4, String str) throws IllegalArgumentException {
        if (this.f13920al == null) {
            this.f13920al = new bn(bV.b(component), str);
            this.f13920al.a(i2, i3, i4, str);
            this.f13920al.a(new C1835ah(this, this.f13920al.getTitle()));
            this.f13920al.pack();
            a(this.f13920al, (Component) this.f13910a.a().get(0));
            this.f13920al.a(new V(this));
        }
        this.f13920al.a(i2, i3, i4, str);
        this.f13920al.setVisible(true);
        return this.f13920al;
    }

    protected bn b(Component component, int i2, int i3, int i4, String str) throws IllegalArgumentException {
        if (this.f13921am == null) {
            this.f13921am = new bn(bV.b(component), str);
            this.f13921am.a(i2, i3, i4, str);
            this.f13921am.a(new C1835ah(this, this.f13921am.getTitle()));
            this.f13921am.pack();
            a(this.f13921am, (Component) this.f13910a.a().get(0));
            this.f13921am.a(new W(this));
        }
        this.f13921am.a(i2, i3, i4, str);
        this.f13921am.setVisible(true);
        return this.f13921am;
    }

    public C1566bd e() {
        if (this.f13919ak == null) {
            this.f13919ak = new C1566bd();
        }
        return this.f13919ak;
    }

    @Override // com.efiAnalytics.apps.ts.dashboard.InterfaceC1407f
    public void a(ArrayList arrayList) {
        b(arrayList);
    }

    private aO j() {
        if (this.f13913d == null) {
            this.f13913d = new aO(bV.b((AbstractC1420s) this.f13910a.a().get(0)), this.f13910a);
            this.f13913d.a(new C1835ah(this, this.f13913d.getTitle()));
            this.f13913d.pack();
            a(this.f13913d, (Component) this.f13910a.a().get(0));
        }
        this.f13913d.e(this.f13910a.a());
        this.f13913d.setVisible(true);
        return this.f13913d;
    }

    private aK k() {
        if (this.f13914e == null) {
            this.f13914e = new aK(bV.b((AbstractC1420s) this.f13910a.a().get(0)), this.f13910a);
            this.f13914e.a(new C1835ah(this, this.f13914e.getTitle()));
            this.f13914e.pack();
            a(this.f13914e, (Component) this.f13910a.a().get(0));
        }
        this.f13914e.e(this.f13910a.a());
        this.f13914e.setVisible(true);
        return this.f13914e;
    }

    private aF l() throws IllegalArgumentException {
        if (this.f13915f == null) {
            this.f13915f = new aF(bV.b((AbstractC1420s) this.f13910a.a().get(0)), this.f13910a);
            this.f13915f.a(new C1835ah(this, this.f13915f.getTitle()));
            this.f13915f.pack();
            a(this.f13915f, (Component) this.f13910a.a().get(0));
        }
        this.f13915f.e(this.f13910a.a());
        this.f13915f.setVisible(true);
        return this.f13915f;
    }

    private be m() {
        if (this.f13916g == null) {
            this.f13916g = new be(bV.b((AbstractC1420s) this.f13910a.a().get(0)), this.f13910a);
            this.f13916g.a(new C1835ah(this, this.f13916g.getTitle()));
            this.f13916g.pack();
            a(this.f13916g, (Component) this.f13910a.a().get(0));
        }
        this.f13916g.e(this.f13910a.a());
        this.f13916g.setVisible(true);
        return this.f13916g;
    }

    private C1846as n() {
        if (this.f13917h == null) {
            this.f13917h = new C1846as(bV.b((AbstractC1420s) this.f13910a.a().get(0)), this.f13910a);
            this.f13917h.a(new C1835ah(this, this.f13917h.getTitle()));
            this.f13917h.pack();
            a(this.f13917h, (Component) this.f13910a.a().get(0));
        }
        this.f13917h.e(this.f13910a.a());
        this.f13917h.setVisible(true);
        return this.f13917h;
    }

    private C1870r o() {
        if (this.f13918i == null) {
            this.f13918i = new C1870r(bV.b((AbstractC1420s) this.f13910a.a().get(0)), this.f13910a, C1818g.b("Component Position"));
            this.f13918i.a(new C1835ah(this, this.f13918i.getTitle()));
            this.f13918i.pack();
            a(this.f13918i, (Component) this.f13910a.a().get(0));
        }
        this.f13918i.e(this.f13910a.a());
        this.f13918i.setVisible(true);
        return this.f13918i;
    }

    private void p() {
        C1837aj c1837aj = new C1837aj(bV.c(), C1807j.G(), "Select Gauge background Image");
        a(c1837aj, (Component) this.f13910a.a().get(0));
        c1837aj.a(new C1835ah(this, "Select Gauge background Image"));
        c1837aj.a(new X(this));
        c1837aj.setVisible(true);
    }

    private void q() {
        ArrayList arrayList = new ArrayList();
        arrayList.add(new File(C1807j.x()));
        arrayList.add(C1807j.G());
        C1837aj c1837aj = new C1837aj(bV.c(), arrayList, "Select Gauge Needle Image");
        c1837aj.a(new C1835ah(this, "Select Gauge Needle Image"));
        a(c1837aj, (Component) this.f13910a.a().get(0));
        c1837aj.a(new Y(this));
        c1837aj.setVisible(true);
    }

    private void r() {
        C1837aj c1837aj = new C1837aj(bV.c(), C1807j.G(), "Select Indicator On Image");
        a(c1837aj, (Component) this.f13910a.a().get(0));
        c1837aj.a(new C1835ah(this, "Select Indicator On Image"));
        c1837aj.a(new Z(this));
        c1837aj.setVisible(true);
    }

    private void s() {
        C1837aj c1837aj = new C1837aj(bV.c(), C1807j.G(), "Select Indicator Off Image");
        a(c1837aj, (Component) this.f13910a.a().get(0));
        c1837aj.a(new C1835ah(this, "Select Indicator Off Image"));
        c1837aj.a(new C1828aa(this));
        c1837aj.setVisible(true);
    }
}
