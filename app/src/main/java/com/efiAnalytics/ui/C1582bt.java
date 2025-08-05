package com.efiAnalytics.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.Point;
import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JSlider;

/* renamed from: com.efiAnalytics.ui.bt, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/bt.class */
public class C1582bt extends JPanel implements eU {

    /* renamed from: b, reason: collision with root package name */
    JSlider f11013b;

    /* renamed from: c, reason: collision with root package name */
    JSlider f11014c;

    /* renamed from: d, reason: collision with root package name */
    JSlider f11015d;

    /* renamed from: t, reason: collision with root package name */
    private boolean f11028t;

    /* renamed from: l, reason: collision with root package name */
    public static String f11031l = "Black Color Theme";

    /* renamed from: m, reason: collision with root package name */
    public static String f11032m = "White Color Theme";

    /* renamed from: n, reason: collision with root package name */
    public static String f11033n = "Gray Color Theme";

    /* renamed from: a, reason: collision with root package name */
    eM f11012a = new eM();

    /* renamed from: e, reason: collision with root package name */
    JCheckBox f11016e = new JCheckBox("Color Shade");

    /* renamed from: f, reason: collision with root package name */
    JCheckBox f11017f = new JCheckBox("Antialiasing");

    /* renamed from: g, reason: collision with root package name */
    JCheckBox f11018g = new JCheckBox("Even Spacing");

    /* renamed from: h, reason: collision with root package name */
    JCheckBox f11019h = new JCheckBox("Follow Mode");

    /* renamed from: i, reason: collision with root package name */
    C1619dc f11020i = new C1619dc();

    /* renamed from: o, reason: collision with root package name */
    private int f11021o = 5;

    /* renamed from: p, reason: collision with root package name */
    private int f11022p = 1;

    /* renamed from: q, reason: collision with root package name */
    private boolean f11023q = false;

    /* renamed from: j, reason: collision with root package name */
    int f11024j = 0;

    /* renamed from: k, reason: collision with root package name */
    int f11025k = 0;

    /* renamed from: r, reason: collision with root package name */
    private boolean f11026r = false;

    /* renamed from: s, reason: collision with root package name */
    private double f11027s = 0.2d;

    /* renamed from: u, reason: collision with root package name */
    private bH.aa f11029u = null;

    /* renamed from: v, reason: collision with root package name */
    private InterfaceC1662et f11030v = null;

    public C1582bt(boolean z2, bH.aa aaVar) {
        this.f11013b = null;
        this.f11014c = null;
        this.f11015d = null;
        this.f11028t = false;
        this.f11028t = z2;
        a(aaVar);
        setLayout(new BorderLayout());
        this.f11012a.a(this);
        add(BorderLayout.CENTER, this.f11012a);
        this.f11013b = new JSlider(0, -180, 179, 0);
        this.f11013b.addChangeListener(new C1583bu(this));
        add("South", this.f11013b);
        this.f11014c = new JSlider(1, 0, 359, 0);
        this.f11014c.addChangeListener(new bF(this));
        add("West", this.f11014c);
        JPanel jPanel = new JPanel();
        if (z2) {
            this.f11015d = new JSlider(1, 15, 90, 45);
            this.f11015d.addChangeListener(new bG(this));
            jPanel.setLayout(new GridLayout(3, 1));
            jPanel.add(new JLabel());
            jPanel.add(this.f11015d);
            jPanel.add(new JLabel());
            add("East", jPanel);
        }
        JPanel jPanel2 = new JPanel();
        jPanel2.setLayout(new FlowLayout(0, 10, 0));
        jPanel2.add(this.f11016e);
        this.f11016e.addActionListener(new bH(this));
        a(true);
        this.f11020i.c(f11032m);
        this.f11020i.c(f11033n);
        this.f11020i.c(f11031l);
        this.f11020i.addItemListener(new bI(this));
        jPanel2.add(this.f11020i);
        a(f11032m);
        d(false);
        jPanel2.add(this.f11018g);
        this.f11018g.setMnemonic(69);
        this.f11018g.addActionListener(new bJ(this));
        jPanel2.add(this.f11019h);
        this.f11019h.setMnemonic(70);
        this.f11019h.addActionListener(new bK(this));
        if (z2) {
            JMenuItem jMenuItem = new JMenuItem(b("3D Table Options"));
            jMenuItem.addActionListener(new bL(this));
            jPanel2.add(jMenuItem);
        }
        add("North", jPanel2);
        jPanel2.setBorder(BorderFactory.createEtchedBorder());
        this.f11012a.addMouseListener(new bM(this));
        if (getBackground().getRed() + getBackground().getBlue() + getBackground().getGreen() < 100) {
            jPanel2.setBackground(Color.BLACK);
            jPanel2.setForeground(Color.WHITE);
            jPanel2.setOpaque(true);
            this.f11016e.setBackground(Color.BLACK);
            this.f11016e.setForeground(Color.WHITE);
            this.f11016e.setOpaque(true);
            this.f11018g.setBackground(Color.BLACK);
            this.f11018g.setForeground(Color.WHITE);
            this.f11018g.setOpaque(true);
            this.f11019h.setBackground(Color.BLACK);
            this.f11019h.setForeground(Color.WHITE);
            this.f11019h.setOpaque(true);
            this.f11013b.setBackground(Color.BLACK);
            this.f11013b.setForeground(Color.WHITE);
            this.f11013b.setOpaque(true);
            this.f11014c.setBackground(Color.BLACK);
            this.f11014c.setForeground(Color.WHITE);
            this.f11014c.setOpaque(true);
            if (this.f11015d != null) {
                this.f11015d.setBackground(Color.BLACK);
                this.f11015d.setForeground(Color.WHITE);
                this.f11015d.setOpaque(true);
            }
            jPanel.setBackground(Color.BLACK);
            jPanel.setForeground(Color.WHITE);
            jPanel.setOpaque(true);
        }
    }

    private String b(String str) {
        if (this.f11029u != null) {
            str = this.f11029u.a(str);
        }
        return str;
    }

    public void a() {
        if (this.f11023q) {
            this.f11023q = false;
            this.f11012a.c(this.f11024j);
            this.f11012a.d(this.f11025k);
        } else {
            this.f11024j = this.f11013b.getValue();
            this.f11025k = this.f11014c.getValue();
            this.f11012a.c(0);
            this.f11012a.d(270);
            this.f11023q = true;
        }
    }

    public void a(Point point) {
        JPopupMenu jPopupMenu = new JPopupMenu();
        JCheckBoxMenuItem jCheckBoxMenuItem = new JCheckBoxMenuItem(b("Smart Select Movement"));
        jCheckBoxMenuItem.setState(this.f11012a.E());
        jCheckBoxMenuItem.addActionListener(new C1584bv(this));
        jPopupMenu.add((JMenuItem) jCheckBoxMenuItem);
        JCheckBoxMenuItem jCheckBoxMenuItem2 = new JCheckBoxMenuItem(b("Show Active Table Values"));
        jCheckBoxMenuItem2.setState(this.f11012a.v());
        jCheckBoxMenuItem2.addActionListener(new C1585bw(this));
        jPopupMenu.add((JMenuItem) jCheckBoxMenuItem2);
        JCheckBoxMenuItem jCheckBoxMenuItem3 = new JCheckBoxMenuItem(b("Show Selected X & Y Values"));
        jCheckBoxMenuItem3.setState(this.f11012a.u());
        jCheckBoxMenuItem3.addActionListener(new C1586bx(this));
        jPopupMenu.add((JMenuItem) jCheckBoxMenuItem3);
        JCheckBoxMenuItem jCheckBoxMenuItem4 = new JCheckBoxMenuItem(b("Increment All Active Cells"));
        jCheckBoxMenuItem4.setState(k());
        jCheckBoxMenuItem4.addActionListener(new C1587by(this));
        jPopupMenu.add((JMenuItem) jCheckBoxMenuItem4);
        JMenuItem jMenuItem = new JMenuItem(b("Active Weight Threshold") + " (" + bH.W.a(l() * 100.0d) + "%)");
        jMenuItem.setEnabled(this.f11026r);
        jMenuItem.addActionListener(new C1588bz(this));
        jPopupMenu.add(jMenuItem);
        JMenuItem jMenuItem2 = new JMenuItem(b("Select Active Color"));
        jMenuItem2.addActionListener(new bA(this));
        jPopupMenu.add(jMenuItem2);
        JCheckBoxMenuItem jCheckBoxMenuItem5 = new JCheckBoxMenuItem(b("Antialiasing"));
        jCheckBoxMenuItem5.addActionListener(new bB(this));
        jPopupMenu.add((JMenuItem) jCheckBoxMenuItem5);
        jCheckBoxMenuItem5.setState(this.f11012a.w());
        JMenuItem jMenuItem3 = new JMenuItem("Select Selected Color");
        jMenuItem3.addActionListener(new bC(this));
        jPopupMenu.add(jMenuItem3);
        JMenuItem jMenuItem4 = new JMenuItem(b("Normal Increment") + " # " + j());
        jMenuItem4.addActionListener(new bD(this));
        jPopupMenu.add(jMenuItem4);
        JMenuItem jMenuItem5 = new JMenuItem(b("CTRL Increment") + " # " + i());
        jMenuItem5.addActionListener(new bE(this));
        jPopupMenu.add(jMenuItem5);
        jPopupMenu.show(this, point.f12370x, point.f12371y);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void m() {
        String strA = bV.a((Component) this, true, b("Number of increment by with CTRL Pressed"), i() + "");
        if (strA == null || strA.equals("")) {
            return;
        }
        e((int) Double.parseDouble(strA));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void n() {
        String strA = bV.a((Component) this, true, b("Number of increments using arrow keys") + " ", j() + "");
        if (strA == null || strA.equals("")) {
            return;
        }
        f((int) Double.parseDouble(strA));
    }

    protected void b() throws HeadlessException {
        Color colorA = bV.a(this, b("Select Active Color"), this.f11012a.t());
        if (colorA != null) {
            a(colorA);
        }
    }

    protected void c() throws HeadlessException {
        Color colorA = bV.a(this, b("Select Selected Color"), this.f11012a.s());
        if (colorA != null) {
            b(colorA);
        }
    }

    public void d() {
        Color colorC;
        Color colorC2;
        if (this.f11030v == null) {
            bH.C.b("3DTable Settings Persistance not intialized");
        }
        String strA = this.f11030v.a("tableYawAngle");
        if (strA != null && strA.length() > 0) {
            a(Integer.parseInt(strA));
        }
        String strA2 = this.f11030v.a("tableRollAngle");
        if (strA2 != null && strA2.length() > 0) {
            b(Integer.parseInt(strA2));
        }
        String strA3 = this.f11030v.a("tableColorFill");
        if (strA3 != null && strA3.length() > 0) {
            a(Boolean.parseBoolean(strA3));
        }
        String strA4 = this.f11030v.a("colorTheme");
        if (strA4 != null && strA4.length() > 0) {
            a(strA4);
        }
        String strA5 = this.f11030v.a("table3DAntiAlias");
        if (strA5 != null && strA5.length() > 0) {
            b(Boolean.parseBoolean(strA5));
        }
        String strA6 = this.f11030v.a("spaceByIndex");
        if (strA6 != null && strA6.length() > 0) {
            d(Boolean.parseBoolean(strA6));
        }
        String strA7 = this.f11030v.a("dynamicSelectIncrement");
        if (strA7 != null && strA7.length() > 0) {
            c(Boolean.parseBoolean(strA7));
        }
        String strA8 = this.f11030v.a("followMode");
        if (strA8 != null && strA8.length() > 0) {
            this.f11019h.setSelected(Boolean.parseBoolean(strA8));
        }
        String strA9 = this.f11030v.a("activeColor");
        if (strA9 != null && strA9.length() > 0 && (colorC2 = c(strA9)) != null) {
            this.f11012a.b(colorC2);
        }
        String strA10 = this.f11030v.a("selectedColor");
        if (strA10 != null && strA10.length() > 0 && (colorC = c(strA10)) != null) {
            this.f11012a.a(colorC);
        }
        String strA11 = this.f11030v.a("showZvalue");
        if (strA11 != null && strA11.length() > 0) {
            this.f11012a.c(strA11 != null && strA11.equals("true"));
        }
        String strA12 = this.f11030v.a("showXYvalues");
        if (strA12 != null && strA12.length() > 0) {
            this.f11012a.b(strA12 != null && strA12.equals("true"));
        }
        String strA13 = this.f11030v.a("ctrlIncrementSize");
        if (strA13 != null && strA13.length() > 0) {
            e(Integer.parseInt(strA13));
        }
        String strA14 = this.f11030v.a("incrementSize");
        if (strA14 != null && strA14.length() > 0) {
            f(Integer.parseInt(strA14));
        }
        String strA15 = this.f11030v.a("zHeightScale");
        if (strA15 != null && strA15.length() > 0 && this.f11015d != null) {
            double d2 = Double.parseDouble(strA15);
            if (d2 > 0.1d && d2 < 1.01d) {
                this.f11012a.c(d2);
                this.f11015d.setValue((int) (d2 * 100.0d));
            }
        }
        String strA16 = this.f11030v.a("incrementAllVertextCells");
        if (strA16 != null && strA16.length() > 0) {
            this.f11026r = strA16.equals("true");
        }
        String strA17 = this.f11030v.a("incrementAllVertextWeight");
        if (strA17 == null || strA17.length() <= 0) {
            return;
        }
        this.f11027s = Double.parseDouble(strA17);
    }

    private Color c(String str) {
        int i2 = -1;
        try {
            i2 = Integer.parseInt(str);
        } catch (Exception e2) {
        }
        if (i2 != -1) {
            return new Color(i2);
        }
        return null;
    }

    public boolean e() {
        return this.f11019h.isSelected();
    }

    public void f() {
        this.f11012a.x();
        double dRound = Math.round(this.f11012a.a().a(this.f11012a.x()));
        double dRound2 = Math.round(this.f11012a.a().b(this.f11012a.y()));
        this.f11012a.e((int) dRound);
        this.f11012a.f((int) dRound2);
        this.f11012a.repaint();
    }

    public void a(int i2) {
        this.f11013b.setValue(i2);
    }

    public void b(int i2) {
        this.f11014c.setValue(i2);
    }

    public void a(boolean z2) {
        this.f11016e.setSelected(z2);
        this.f11012a.a(z2);
        a("tableColorFill", "" + z2);
        this.f11012a.repaint();
    }

    public void b(boolean z2) {
        this.f11017f.setSelected(z2);
        this.f11012a.d(z2);
        a("table3DAntiAlias", "" + z2);
        this.f11012a.repaint();
    }

    public void c(boolean z2) {
        this.f11012a.f(z2);
        a("dynamicSelectIncrement", "" + z2);
    }

    public void d(boolean z2) {
        this.f11018g.setSelected(z2);
        this.f11012a.e(z2);
        a("spaceByIndex", "" + z2);
        this.f11012a.repaint();
    }

    public void a(InterfaceC1662et interfaceC1662et) {
        this.f11030v = interfaceC1662et;
    }

    public void g() {
        this.f11019h.setSelected(!this.f11019h.isSelected());
        a("followMode", "" + this.f11019h.isSelected());
    }

    @Override // com.efiAnalytics.ui.eU
    public void c(int i2) {
        while (i2 < -180) {
            i2 += 360;
        }
        int i3 = ((i2 + 180) % 360) - 180;
        this.f11013b.setValue(i3);
        a("tableYawAngle", "" + i3);
    }

    @Override // com.efiAnalytics.ui.eU
    public void d(int i2) {
        int i3 = (i2 + 360) % 360;
        this.f11014c.setValue(i3);
        a("tableRollAngle", "" + i3);
    }

    protected void a(String str, String str2) {
        if (this.f11030v != null) {
            this.f11030v.a(str, str2);
        }
    }

    protected String b(String str, String str2) {
        String strA;
        return (this.f11030v == null || (strA = this.f11030v.a(str)) == null || strA.isEmpty()) ? str2 : strA;
    }

    public void a(String str) {
        if (str.equals(f11031l)) {
            this.f11012a.setBackground(Color.BLACK);
            this.f11012a.setForeground(new Color(96, 96, 96));
            String strB = b("activeColor", "");
            if (strB == null || strB.isEmpty()) {
                this.f11012a.b(Color.CYAN);
            }
        } else if (str.equals(f11032m)) {
            this.f11012a.setBackground(Color.WHITE);
            this.f11012a.setForeground(Color.BLACK);
            String strB2 = b("activeColor", "");
            if (strB2 == null || strB2.isEmpty()) {
                this.f11012a.b(Color.BLUE);
            }
        } else {
            if (!str.equals(f11033n)) {
                bH.C.c("No defined Color Theme:" + str);
                return;
            }
            this.f11012a.setBackground(new Color(220, 220, 220));
            this.f11012a.setForeground(Color.BLACK);
            String strB3 = b("activeColor", "");
            if (strB3 == null || strB3.isEmpty()) {
                this.f11012a.b(Color.BLUE);
            }
        }
        this.f11020i.b(str);
        a("colorTheme", str);
        this.f11012a.z();
        this.f11012a.repaint();
    }

    public void e(boolean z2) {
        this.f11012a.c(z2);
        a("showZvalue", z2 + "");
        this.f11012a.z();
        this.f11012a.repaint();
    }

    public void f(boolean z2) {
        this.f11012a.b(z2);
        a("showXYvalues", z2 + "");
        this.f11012a.z();
        this.f11012a.repaint();
    }

    public eM h() {
        return this.f11012a;
    }

    private void a(Color color) {
        this.f11012a.b(color);
        a("activeColor", color.getRGB() + "");
        this.f11012a.z();
        this.f11012a.repaint();
    }

    private void b(Color color) {
        this.f11012a.a(color);
        a("selectedColor", color.getRGB() + "");
        this.f11012a.z();
        this.f11012a.repaint();
    }

    public int i() {
        return this.f11021o;
    }

    public void e(int i2) {
        this.f11021o = i2;
        a("ctrlIncrementSize", i2 + "");
    }

    public int j() {
        return this.f11022p;
    }

    public void f(int i2) {
        this.f11022p = i2;
        a("incrementSize", i2 + "");
    }

    public boolean k() {
        return this.f11026r && e();
    }

    public void g(boolean z2) {
        this.f11026r = z2;
        if (z2) {
            return;
        }
        this.f11030v.a("incrementAllVertextCells", z2 + "");
    }

    public double l() {
        return this.f11027s;
    }

    public void a(double d2) {
        this.f11027s = d2;
        a("incrementAllVertextWeight", d2 + "");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void o() {
        String strA = bV.a((Component) this, true, b("Minimum Active Increment Weight(%)"), bH.W.b(this.f11027s * 100.0d, 1));
        if (strA == null || strA.equals("")) {
            return;
        }
        a(Double.parseDouble(strA) / 100.0d);
    }

    public void a(bH.aa aaVar) {
        this.f11029u = aaVar;
        f11031l = b(f11031l);
        f11032m = b(f11032m);
        f11033n = b(f11033n);
        this.f11016e.setText(b("Color Shade"));
        this.f11017f.setText(b("Antialiasing"));
        this.f11018g.setText(b("Even Spacing"));
        this.f11019h.setText(b("Follow Mode"));
        this.f11012a.a(aaVar);
    }
}
