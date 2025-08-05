package com.efiAnalytics.tuningwidgets.panels;

import com.efiAnalytics.ui.C1685fp;
import com.efiAnalytics.ui.Cdo;
import com.efiAnalytics.ui.eJ;
import java.awt.BorderLayout;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.JTextField;
import r.C1806i;
import s.C1818g;
import sun.security.validator.Validator;

/* renamed from: com.efiAnalytics.tuningwidgets.panels.k, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:com/efiAnalytics/tuningwidgets/panels/k.class */
class C1513k extends JPanel {

    /* renamed from: e, reason: collision with root package name */
    JSlider f10466e;

    /* renamed from: g, reason: collision with root package name */
    JTextField f10468g;

    /* renamed from: h, reason: collision with root package name */
    Cdo f10469h;

    /* renamed from: i, reason: collision with root package name */
    V f10470i;

    /* renamed from: j, reason: collision with root package name */
    JPanel f10471j;

    /* renamed from: k, reason: collision with root package name */
    JPanel f10472k;

    /* renamed from: l, reason: collision with root package name */
    JPanel f10473l;

    /* renamed from: m, reason: collision with root package name */
    JPanel f10474m;

    /* renamed from: n, reason: collision with root package name */
    boolean f10475n;

    /* renamed from: o, reason: collision with root package name */
    final /* synthetic */ C1509g f10476o;

    /* renamed from: a, reason: collision with root package name */
    JRadioButton f10462a = new JRadioButton(C1818g.b("Manual"));

    /* renamed from: b, reason: collision with root package name */
    JRadioButton f10463b = new JRadioButton(C1818g.b(Validator.TYPE_SIMPLE));

    /* renamed from: c, reason: collision with root package name */
    JRadioButton f10464c = new JRadioButton(C1818g.b("Expression"));

    /* renamed from: d, reason: collision with root package name */
    JRadioButton f10465d = new JRadioButton(C1818g.b("Log For"));

    /* renamed from: f, reason: collision with root package name */
    JComboBox f10467f = new JComboBox();

    public C1513k(C1509g c1509g, G.R r2, boolean z2, boolean z3) {
        this.f10476o = c1509g;
        this.f10466e = null;
        this.f10474m = null;
        ButtonGroup buttonGroup = new ButtonGroup();
        this.f10475n = C1806i.a().a("sa0-0o0os-0o-0DS");
        C1514l c1514l = new C1514l(this, c1509g);
        if (z3) {
            buttonGroup.add(this.f10462a);
            buttonGroup.add(this.f10463b);
        }
        if (z3) {
            this.f10462a.addActionListener(c1514l);
            this.f10463b.addActionListener(c1514l);
        }
        if (!this.f10475n) {
            buttonGroup.add(this.f10464c);
            this.f10464c.addActionListener(c1514l);
        }
        setLayout(new BoxLayout(this, 1));
        this.f10472k = a(this.f10462a);
        this.f10472k.add(BorderLayout.CENTER, new JLabel());
        if (z3) {
            add(this.f10472k);
        }
        this.f10471j = a(this.f10463b);
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new BorderLayout(3, 3));
        this.f10470i = new V(r2);
        if (this.f10475n) {
            this.f10470i.removeAllItems();
            this.f10470i.a("TPS_Pct");
        }
        jPanel.add("West", this.f10470i);
        this.f10467f.setEditable(false);
        this.f10467f.addItem(">");
        this.f10467f.addItem("=");
        this.f10467f.addItem("<");
        jPanel.add(BorderLayout.CENTER, this.f10467f);
        this.f10469h = new Cdo("", 4);
        jPanel.add("East", this.f10469h);
        this.f10471j.add(BorderLayout.CENTER, jPanel);
        if (z3) {
            add(this.f10471j);
        }
        this.f10473l = a(this.f10464c);
        this.f10468g = new JTextField("", 25);
        this.f10468g.setBorder(BorderFactory.createLoweredBevelBorder());
        this.f10473l.add(BorderLayout.CENTER, this.f10468g);
        if (!this.f10475n) {
            add(this.f10473l);
        }
        if (z2) {
            this.f10474m = a(this.f10465d);
            if (C1806i.a().a("sa0-0o0os-0o-0DS")) {
                this.f10466e = new JSlider(0, 0, 30, 30);
                this.f10466e.setMajorTickSpacing(10);
                this.f10466e.setMinorTickSpacing(1);
            } else {
                this.f10466e = new JSlider(0, 0, 600, 30);
                this.f10466e.setMajorTickSpacing(100);
                this.f10466e.setMinorTickSpacing(10);
            }
            this.f10466e.setPaintLabels(true);
            this.f10466e.setPaintTicks(true);
            buttonGroup.add(this.f10465d);
            this.f10465d.addActionListener(c1514l);
            this.f10474m.add(BorderLayout.CENTER, this.f10466e);
            JLabel jLabel = new JLabel("   " + this.f10466e.getValue() + " s.");
            this.f10466e.addChangeListener(new C1515m(this, c1509g, jLabel));
            this.f10474m.add("East", jLabel);
            add(this.f10474m);
        }
    }

    public void a(String str) {
        if (str == null || str.trim().equals("")) {
            str = C1509g.f10458q;
        }
        if (str.equals(C1509g.f10458q)) {
            this.f10462a.setSelected(true);
        } else {
            this.f10468g.setText(str);
            String[] strArrSplit = str.split(" ");
            if (strArrSplit.length == 3 && ((strArrSplit[1].equals("<") || strArrSplit[1].equals("=") || strArrSplit[1].equals(">")) && bH.H.a(strArrSplit[2]))) {
                String strTrim = strArrSplit[0].trim();
                if (this.f10466e == null || !strTrim.equals("AppEvent.dataLogTime")) {
                    this.f10470i.setSelectedItem(strTrim);
                    this.f10467f.setSelectedItem(strArrSplit[1].trim());
                    this.f10469h.setText(strArrSplit[2].trim());
                    this.f10463b.setSelected(true);
                } else {
                    this.f10466e.setValue((int) Double.parseDouble(strArrSplit[2]));
                    this.f10465d.setSelected(true);
                }
            } else {
                this.f10464c.setSelected(true);
            }
        }
        b();
    }

    public String a() {
        if (this.f10462a.isSelected()) {
            return C1509g.f10458q;
        }
        if (this.f10465d != null && this.f10465d.isSelected()) {
            StringBuilder sb = new StringBuilder();
            sb.append("AppEvent.dataLogTime").append(" > ").append(this.f10466e.getValue());
            return sb.toString();
        }
        if (!this.f10463b.isSelected()) {
            return this.f10468g.getText();
        }
        StringBuilder sb2 = new StringBuilder();
        sb2.append(this.f10470i.getSelectedItem().toString()).append(" ").append(this.f10467f.getSelectedItem().toString()).append(" ").append(this.f10469h.getText());
        return sb2.toString();
    }

    public void b() {
        if (this.f10462a.isSelected()) {
            C1685fp.a(this.f10472k.getComponent(1), true);
            C1685fp.a(this.f10471j.getComponent(1), false);
            C1685fp.a(this.f10473l.getComponent(1), false);
            if (this.f10474m != null) {
                C1685fp.a(this.f10474m.getComponent(1), false);
                C1685fp.a(this.f10474m.getComponent(2), false);
                return;
            }
            return;
        }
        if (this.f10463b.isSelected()) {
            C1685fp.a(this.f10472k.getComponent(1), false);
            C1685fp.a(this.f10471j.getComponent(1), true);
            C1685fp.a(this.f10473l.getComponent(1), false);
            if (this.f10474m != null) {
                C1685fp.a(this.f10474m.getComponent(1), false);
                C1685fp.a(this.f10474m.getComponent(2), false);
                return;
            }
            return;
        }
        if (this.f10465d.isSelected()) {
            C1685fp.a(this.f10472k.getComponent(1), false);
            C1685fp.a(this.f10471j.getComponent(1), false);
            C1685fp.a(this.f10473l.getComponent(1), false);
            C1685fp.a(this.f10474m.getComponent(1), true);
            C1685fp.a(this.f10474m.getComponent(2), true);
            return;
        }
        C1685fp.a(this.f10472k.getComponent(1), false);
        C1685fp.a(this.f10471j.getComponent(1), false);
        C1685fp.a(this.f10473l.getComponent(1), true);
        if (this.f10474m != null) {
            C1685fp.a(this.f10474m.getComponent(1), false);
            C1685fp.a(this.f10474m.getComponent(2), false);
        }
    }

    private JPanel a(JRadioButton jRadioButton) {
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new BorderLayout());
        jRadioButton.setPreferredSize(eJ.a(120, 25));
        jPanel.add("West", jRadioButton);
        return jPanel;
    }
}
