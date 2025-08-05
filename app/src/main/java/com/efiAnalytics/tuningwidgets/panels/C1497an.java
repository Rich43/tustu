package com.efiAnalytics.tuningwidgets.panels;

import bt.bX;
import com.efiAnalytics.ui.Cdo;
import com.efiAnalytics.ui.InterfaceC1662et;
import com.efiAnalytics.ui.bV;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Window;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import s.C1818g;

/* renamed from: com.efiAnalytics.tuningwidgets.panels.an, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:com/efiAnalytics/tuningwidgets/panels/an.class */
public class C1497an extends JDialog {

    /* renamed from: a, reason: collision with root package name */
    au f10407a;

    /* renamed from: b, reason: collision with root package name */
    Cdo f10408b;

    /* renamed from: c, reason: collision with root package name */
    av f10409c;

    /* renamed from: d, reason: collision with root package name */
    Cdo f10410d;

    /* renamed from: e, reason: collision with root package name */
    JRadioButton f10411e;

    /* renamed from: f, reason: collision with root package name */
    JRadioButton f10412f;

    /* renamed from: g, reason: collision with root package name */
    JRadioButton f10413g;

    /* renamed from: j, reason: collision with root package name */
    private bX f10414j;

    /* renamed from: k, reason: collision with root package name */
    private InterfaceC1662et f10415k;

    /* renamed from: h, reason: collision with root package name */
    G.R f10416h;

    /* renamed from: i, reason: collision with root package name */
    boolean f10417i;

    public C1497an(Window window, G.R r2, InterfaceC1662et interfaceC1662et, bX bXVar) {
        super(window, C1818g.b("Required Fuel Calculator"));
        this.f10407a = new au(this);
        this.f10408b = new Cdo();
        this.f10409c = new av(this);
        this.f10410d = new Cdo();
        this.f10411e = null;
        this.f10412f = null;
        this.f10413g = null;
        this.f10414j = null;
        this.f10415k = null;
        this.f10416h = null;
        this.f10417i = false;
        this.f10415k = interfaceC1662et;
        this.f10416h = r2;
        JPanel jPanel = new JPanel();
        setLayout(new BorderLayout());
        add(BorderLayout.CENTER, jPanel);
        jPanel.setBorder(BorderFactory.createTitledBorder(C1818g.b("Required Fuel Calculator")));
        jPanel.setLayout(new BorderLayout(10, 10));
        a(bXVar);
        JPanel jPanel2 = new JPanel();
        jPanel2.setLayout(new BorderLayout());
        jPanel2.add("East", c());
        jPanel2.add(BorderLayout.CENTER, b());
        jPanel.add(BorderLayout.CENTER, jPanel2);
        jPanel.add("South", h());
        if (b("displacementUnits", "CID").equals("CID")) {
            d();
        } else if (!this.f10417i) {
            e();
        }
        if (b("injectorUnits", "lb/hr").equals("lb/hr")) {
            g();
        } else if (!this.f10417i) {
            f();
        }
        pack();
        setResizable(false);
        setAlwaysOnTop(true);
    }

    private JPanel b() {
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new GridLayout(4, 2, 5, 5));
        jPanel.add(new JLabel(C1818g.b("Engine Displacement")));
        jPanel.add(this.f10407a);
        if (this.f10416h.c("enginesize") != null) {
            this.f10407a.setText(this.f10416h.c("enginesize").e(this.f10416h.p()));
            this.f10417i = true;
            this.f10407a.f10424a = true;
        } else {
            this.f10407a.f10424a = this.f10412f.isSelected();
            this.f10407a.setText(b("displacement", "350"));
        }
        jPanel.add(new JLabel(C1818g.b("Number of Cylinders")));
        jPanel.add(this.f10408b);
        if (this.f10416h.c("nCylinders") != null) {
            this.f10408b.setText(this.f10416h.c("nCylinders").e(this.f10416h.p()));
        } else {
            this.f10408b.setText(b("nCylinders", "8"));
        }
        jPanel.add(new JLabel(C1818g.b("Injector Flow")));
        jPanel.add(this.f10409c);
        if (this.f10416h.c("staged_pri_size") != null) {
            this.f10409c.setText(this.f10416h.c("staged_pri_size").e(this.f10416h.p()));
            this.f10417i = true;
            this.f10409c.f10426a = true;
        } else {
            this.f10409c.f10426a = this.f10413g.isSelected();
            this.f10409c.setText(b("injectorFlow", "30"));
        }
        jPanel.add(new JLabel(C1818g.b("Air-Fuel Ratio")));
        jPanel.add(this.f10410d);
        if (this.f10416h.c("stoich") != null) {
            this.f10410d.setText(this.f10416h.c("stoich").e(this.f10416h.p()));
        } else {
            this.f10410d.setText(b("afr", "14.7"));
        }
        return jPanel;
    }

    private JPanel c() {
        JPanel jPanel = new JPanel();
        jPanel.setBorder(BorderFactory.createTitledBorder(C1818g.b("Units")));
        boolean z2 = !this.f10417i && b("displacementUnits", "CID").equals("CID");
        jPanel.setLayout(new GridLayout(0, 2));
        ButtonGroup buttonGroup = new ButtonGroup();
        this.f10411e = new JRadioButton(C1818g.b("CID"));
        buttonGroup.add(this.f10411e);
        this.f10411e.addActionListener(new C1498ao(this));
        this.f10411e.setSelected(z2);
        jPanel.add(this.f10411e);
        this.f10412f = new JRadioButton(C1818g.b("CC"));
        buttonGroup.add(this.f10412f);
        this.f10412f.addActionListener(new C1499ap(this));
        this.f10412f.setSelected(!z2);
        jPanel.add(this.f10412f);
        ButtonGroup buttonGroup2 = new ButtonGroup();
        JRadioButton jRadioButton = new JRadioButton(C1818g.b("lb/hr"));
        buttonGroup2.add(jRadioButton);
        jRadioButton.addActionListener(new C1500aq(this));
        boolean z3 = !this.f10417i && b("injectorUnits", "lb/hr").equals("lb/hr");
        jRadioButton.setSelected(z3);
        jPanel.add(jRadioButton);
        this.f10413g = new JRadioButton(C1818g.b("cc/min"));
        buttonGroup2.add(this.f10413g);
        this.f10413g.addActionListener(new C1501ar(this));
        this.f10413g.setSelected(!z3);
        jPanel.add(this.f10413g);
        return jPanel;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void d() {
        a("displacementUnits", "CID");
        this.f10407a.b();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void e() {
        a("displacementUnits", "CC");
        this.f10407a.a();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void f() {
        this.f10409c.a();
        a("injectorUnits", "cc/min");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void g() {
        a("injectorUnits", "lb/hr");
        this.f10409c.b();
    }

    private JPanel h() {
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new FlowLayout(2));
        JButton jButton = new JButton(C1818g.b("Cancel"));
        jButton.addActionListener(new C1502as(this));
        JButton jButton2 = new JButton(C1818g.b("Ok"));
        jButton2.addActionListener(new C1503at(this));
        if (bV.d()) {
            jPanel.add(jButton2);
            jPanel.add(jButton);
        } else {
            jPanel.add(jButton);
            jPanel.add(jButton2);
        }
        return jPanel;
    }

    public boolean a() {
        a("displacement", this.f10407a.getText());
        a("nCylinders", this.f10408b.getText());
        a("injectorFlow", this.f10409c.getText());
        a("afr", this.f10410d.getText());
        double d2 = b("displacementUnits", "CID").equals("CID") ? 1.0d : 16.38706d;
        double d3 = b("injectorUnits", "lb/hr").equals("lb/hr") ? 1.0d : 10.5d;
        double d4 = Double.parseDouble(this.f10407a.getText()) / d2;
        if (d4 <= 0.0d) {
            bV.d(C1818g.b("Displacement must be a positive number."), this);
            return false;
        }
        double d5 = Double.parseDouble(this.f10409c.getText()) / d3;
        if (d5 <= 0.0d) {
            bV.d(C1818g.b("Injector Flow must be a positive number."), this);
            return false;
        }
        double d6 = Double.parseDouble(this.f10410d.getText());
        if (d6 <= 0.0d) {
            bV.d(C1818g.b("AFR must be a positive number."), this);
            return false;
        }
        int i2 = Integer.parseInt(this.f10408b.getText());
        if (i2 <= 0) {
            bV.d(C1818g.b("Number of Cylinders must be a positive whole number."), this);
            return false;
        }
        double d7 = (((3.6E7d * d4) * 4.27793E-5d) / ((i2 * d6) * d5)) / 10.0d;
        if (this.f10416h.c("enginesize") != null) {
            try {
                this.f10416h.c("enginesize").a(this.f10416h.h(), d4 * 16.38706d);
            } catch (V.g e2) {
                Logger.getLogger(C1497an.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            } catch (V.j e3) {
                Logger.getLogger(C1497an.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e3);
            }
        }
        if (this.f10416h.c("nCylinders") != null) {
            try {
                this.f10416h.c("nCylinders").a(this.f10416h.h(), i2);
            } catch (V.g e4) {
                Logger.getLogger(C1497an.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e4);
            } catch (V.j e5) {
                Logger.getLogger(C1497an.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e5);
            }
        }
        if (this.f10416h.c("staged_pri_size") != null) {
            try {
                this.f10416h.c("staged_pri_size").a(this.f10416h.h(), d5 * 10.5d);
            } catch (V.g e6) {
                Logger.getLogger(C1497an.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e6);
            } catch (V.j e7) {
                Logger.getLogger(C1497an.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e7);
            }
        }
        if (this.f10416h.c("stoich") != null) {
            try {
                this.f10416h.c("stoich").a(this.f10416h.h(), d6);
            } catch (V.g e8) {
                Logger.getLogger(C1497an.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e8);
            } catch (V.j e9) {
                Logger.getLogger(C1497an.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e9);
            }
        }
        this.f10414j.b(bH.W.b(d7, 1));
        dispose();
        return true;
    }

    private void a(String str, String str2) {
        if (this.f10415k == null) {
            return;
        }
        this.f10415k.a(str, str2);
    }

    private String b(String str, String str2) {
        if (this.f10415k == null) {
            return str2;
        }
        String strA = this.f10415k.a(str);
        return (strA == null || strA.equals("")) ? str2 : strA;
    }

    public void a(bX bXVar) {
        this.f10414j = bXVar;
    }
}
