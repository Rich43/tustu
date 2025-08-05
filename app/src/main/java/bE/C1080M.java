package be;

import G.C0126i;
import G.aH;
import bH.C1011s;
import bt.C1351j;
import com.efiAnalytics.ui.Cdo;
import com.efiAnalytics.ui.bV;
import com.efiAnalytics.ui.eJ;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.io.File;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import org.icepdf.core.util.PdfOps;
import org.slf4j.Marker;
import s.C1818g;
import sun.util.locale.LanguageTag;

/* renamed from: be.M, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:be/M.class */
public class C1080M extends JPanel implements InterfaceC1099o {

    /* renamed from: c, reason: collision with root package name */
    JRadioButton f7911c;

    /* renamed from: d, reason: collision with root package name */
    JRadioButton f7912d;

    /* renamed from: i, reason: collision with root package name */
    Cdo f7917i;

    /* renamed from: j, reason: collision with root package name */
    Cdo f7918j;

    /* renamed from: k, reason: collision with root package name */
    Cdo f7919k;

    /* renamed from: l, reason: collision with root package name */
    Cdo f7920l;

    /* renamed from: n, reason: collision with root package name */
    JPanel f7922n;

    /* renamed from: a, reason: collision with root package name */
    JComboBox f7909a = new JComboBox();

    /* renamed from: b, reason: collision with root package name */
    JComboBox f7910b = new JComboBox();

    /* renamed from: e, reason: collision with root package name */
    ButtonGroup f7913e = new ButtonGroup();

    /* renamed from: f, reason: collision with root package name */
    JTextField f7914f = new JTextField("", 15);

    /* renamed from: g, reason: collision with root package name */
    JTextField f7915g = new JTextField("", 3);

    /* renamed from: h, reason: collision with root package name */
    com.efiAnalytics.tuningwidgets.panels.G f7916h = new com.efiAnalytics.tuningwidgets.panels.G(C1818g.b("Browse for Inc File"));

    /* renamed from: m, reason: collision with root package name */
    CardLayout f7921m = new CardLayout();

    /* renamed from: o, reason: collision with root package name */
    JLabel f7923o = new JLabel("", 0);

    /* renamed from: q, reason: collision with root package name */
    private G.R f7924q = null;

    /* renamed from: p, reason: collision with root package name */
    C1098n f7925p = new C1098n();

    /* renamed from: r, reason: collision with root package name */
    private int f7926r = 1023;

    public C1080M() {
        setBorder(BorderFactory.createTitledBorder(C1818g.b("Simple Channel")));
        setLayout(new BorderLayout());
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new GridLayout(0, 1, eJ.a(3), eJ.a(3)));
        JPanel jPanel2 = new JPanel();
        jPanel2.setLayout(new FlowLayout(1));
        jPanel2.add(new JLabel(C1818g.b("Input Channel")));
        jPanel2.add(this.f7909a);
        this.f7909a.addActionListener(this.f7925p);
        jPanel.add(jPanel2);
        JPanel jPanel3 = new JPanel();
        jPanel3.setLayout(new FlowLayout(1));
        jPanel3.add(new JLabel(C1818g.b("New Channel Name")));
        jPanel3.add(this.f7914f);
        this.f7914f.addKeyListener(this.f7925p);
        this.f7914f.setBorder(BorderFactory.createBevelBorder(1));
        jPanel.add(jPanel3);
        JPanel jPanel4 = new JPanel();
        jPanel4.setLayout(new FlowLayout(1));
        jPanel4.add(new JLabel(C1818g.b("Units")));
        jPanel4.add(this.f7915g);
        this.f7915g.addKeyListener(this.f7925p);
        this.f7915g.setBorder(BorderFactory.createBevelBorder(1));
        jPanel.add(jPanel4);
        JPanel jPanel5 = new JPanel();
        jPanel5.setLayout(new FlowLayout(1));
        JPanel jPanel6 = new JPanel();
        jPanel6.add(new JLabel(C1818g.b("Transformation")));
        jPanel6.setLayout(new GridLayout(1, 0));
        C1081N c1081n = new C1081N(this);
        this.f7911c = new JRadioButton(C1818g.b("Linear"));
        this.f7911c.addActionListener(c1081n);
        this.f7913e.add(this.f7911c);
        jPanel6.add(this.f7911c);
        this.f7912d = new JRadioButton(C1818g.b("inc File"));
        this.f7912d.addActionListener(c1081n);
        this.f7913e.add(this.f7912d);
        jPanel6.add(this.f7912d);
        jPanel5.add(jPanel6);
        jPanel.add(jPanel5);
        add(jPanel, "North");
        this.f7922n = new JPanel();
        this.f7922n.setLayout(this.f7921m);
        this.f7922n.add(g(), "Linear");
        this.f7922n.add(this.f7916h, "Inc");
        add(this.f7922n, BorderLayout.CENTER);
        this.f7911c.setSelected(true);
        h();
    }

    private JPanel g() throws IllegalArgumentException {
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new BorderLayout());
        jPanel.setBorder(BorderFactory.createTitledBorder(C1818g.b("Linear Transformation")));
        this.f7910b.addItem(C1818g.b("No Voltage to ADC Conversion"));
        this.f7910b.addItem("8 bit: 0-255");
        this.f7910b.addItem("10 bit: 0-1023");
        this.f7910b.addItem("12 bit: 0-4095");
        this.f7910b.setSelectedIndex(2);
        this.f7910b.addActionListener(new C1082O(this));
        JPanel jPanel2 = new JPanel();
        jPanel2.setLayout(new BorderLayout(eJ.a(5), eJ.a(5)));
        jPanel2.add(new JLabel(C1818g.b("Volt to ADC Conversion")), "West");
        jPanel2.add(this.f7910b, BorderLayout.CENTER);
        jPanel2.add(new C1351j(C1818g.b("If you have selected a raw ADC channel, but your Input values are in volts, set this to the desired ADC bit size. If the selected Input Channel units match your input value units, set to No ADC to Voltage Conversion")), "East");
        jPanel.add(jPanel2, "North");
        JPanel jPanel3 = new JPanel();
        jPanel.add(jPanel3, BorderLayout.CENTER);
        jPanel3.setLayout(new GridLayout(3, 3, eJ.a(3), eJ.a(3)));
        jPanel3.add(new JLabel());
        this.f7923o.setText(C1818g.b("Input Voltage"));
        jPanel3.add(this.f7923o);
        jPanel3.add(new JLabel(C1818g.b("Output Value"), 0));
        jPanel3.add(new JLabel(C1818g.b("Point 1"), 4));
        this.f7917i = new Cdo();
        this.f7917i.addKeyListener(this.f7925p);
        jPanel3.add(this.f7917i);
        this.f7919k = new Cdo();
        this.f7919k.addKeyListener(this.f7925p);
        jPanel3.add(this.f7919k);
        jPanel3.add(new JLabel(C1818g.b("Point 2"), 4));
        this.f7918j = new Cdo();
        this.f7918j.addKeyListener(this.f7925p);
        jPanel3.add(this.f7918j);
        this.f7920l = new Cdo();
        this.f7920l.addKeyListener(this.f7925p);
        jPanel3.add(this.f7920l);
        this.f7917i.b(2);
        this.f7918j.b(2);
        this.f7919k.b(2);
        this.f7920l.b(2);
        return jPanel;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void h() {
        if (this.f7911c.isSelected()) {
            this.f7921m.show(this.f7922n, "Linear");
        } else {
            this.f7921m.show(this.f7922n, "Inc");
        }
    }

    public boolean a() {
        String text = this.f7914f.getText();
        if (text.isEmpty()) {
            bV.d(C1818g.b("You must enter a name for the new OutputChannel."), this);
            return false;
        }
        aH aHVarG = this.f7924q.g(text);
        if (aHVarG != null && !aHVarG.aL() && !bV.a(C1818g.b("There is already a channel by the same name in the firmware definition.") + "\n" + C1818g.b("Your new Channel will over ride the existing channel.") + "\n\n" + C1818g.b("Are you sure you wish to use this channel name?"), (Component) this, true)) {
            return false;
        }
        if (C0126i.c(text)) {
            bV.d(C1818g.b("OutputChannel Name cannot contain special characters."), this);
            return false;
        }
        if (!this.f7911c.isSelected()) {
            if (this.f7916h.b() != null) {
                return true;
            }
            bV.d(C1818g.b("You must browse to a valid inc file."), this);
            return false;
        }
        if (!Double.isNaN(this.f7919k.e()) && !Double.isNaN(this.f7917i.e()) && !Double.isNaN(this.f7920l.e()) && !Double.isNaN(this.f7918j.e())) {
            return true;
        }
        bV.d(C1818g.b("Please enter 4 values."), this);
        return false;
    }

    public String b() {
        return this.f7914f.getText();
    }

    public String d() {
        String str;
        if (this.f7911c.isSelected() && !Double.isNaN(this.f7919k.e()) && !Double.isNaN(this.f7920l.e()) && !Double.isNaN(this.f7917i.e()) && !Double.isNaN(this.f7918j.e())) {
            double dE = (this.f7917i.e() * this.f7926r) / 5.0d;
            str = this.f7919k.e() + " + (" + this.f7920l.e() + " - " + this.f7919k.e() + ") * ((" + this.f7909a.getSelectedItem().toString() + " - " + dE + " )/( " + ((this.f7918j.e() * this.f7926r) / 5.0d) + " - " + dE + " ))";
        } else if (this.f7912d.isSelected()) {
            File fileB = this.f7916h.b();
            str = "table( " + this.f7909a.getSelectedItem().toString() + ", \"" + fileB.getName() + "\" )";
            File file = new File(aE.a.A().p(), fileB.getName());
            if (!file.equals(fileB)) {
                try {
                    C1011s.a(fileB, file);
                } catch (V.a e2) {
                    bV.d(C1818g.b("Unable to copy inc to project folder!") + "\n" + C1818g.b("Please copy the inc file to the folder:") + "\n\n" + aE.a.A().p(), this);
                }
            }
        } else {
            str = "";
        }
        return str;
    }

    public void a(G.R r2) {
        this.f7924q = r2;
        this.f7909a.removeAllItems();
        for (String str : bH.R.a(r2.s())) {
            if (!str.contains(" ") && !str.endsWith("OC")) {
                this.f7909a.addItem(str);
            }
        }
    }

    public boolean a(String str, String str2) {
        if (str.contains("table(")) {
            try {
                C1084Q c1084qD = d(str);
                this.f7912d.setSelected(true);
                this.f7909a.setSelectedItem(c1084qD.f7936b);
                this.f7915g.setText(str2);
                this.f7916h.a(new File(aE.a.A().p(), c1084qD.f7935a));
            } catch (Exception e2) {
                return false;
            }
        } else {
            try {
                C1083P c1083pC = c(str);
                this.f7911c.setSelected(true);
                this.f7914f.setEditable(false);
                this.f7915g.setText(str2);
                this.f7909a.setSelectedItem(c1083pC.f7933e);
                if (c1083pC.f7929a > 1500.0d || c1083pC.f7931c > 1500.0d) {
                    this.f7910b.setSelectedIndex(3);
                } else if (c1083pC.f7929a > 300.0d || c1083pC.f7931c > 300.0d) {
                    this.f7910b.setSelectedIndex(2);
                } else if (c1083pC.f7929a > 50.0d || c1083pC.f7931c > 50.0d) {
                    this.f7910b.setSelectedIndex(1);
                } else {
                    this.f7910b.setSelectedIndex(0);
                }
                a(c1083pC);
            } catch (Exception e3) {
                return false;
            }
        }
        h();
        this.f7925p.b();
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a(C1083P c1083p) {
        this.f7917i.a((5.0d * c1083p.f7929a) / this.f7926r);
        this.f7918j.a((5.0d * c1083p.f7931c) / this.f7926r);
        this.f7919k.a(c1083p.f7930b);
        this.f7920l.a(c1083p.f7932d);
    }

    public void a(String str, boolean z2) {
        this.f7914f.setText(str);
        this.f7914f.setEditable(z2);
    }

    public void a(String str) {
        this.f7915g.setText(str);
    }

    public String e() {
        return this.f7915g.getText();
    }

    public boolean b(String str) {
        if (str.contains("table(")) {
            try {
                if (bH.W.e(str, Marker.ANY_NON_NULL_MARKER) != 0 || bH.W.e(str, LanguageTag.SEP) != 0 || bH.W.e(str, "/") != 0 || bH.W.e(str, "*") != 0) {
                    return false;
                }
                d(str);
                return true;
            } catch (Exception e2) {
                return false;
            }
        }
        try {
            if (bH.W.e(str, "(") != 4 || bH.W.e(str, "*") != 1 || bH.W.e(str, LanguageTag.SEP) != 3 || bH.W.e(str, "/") != 1) {
                return false;
            }
            c(str);
            return true;
        } catch (Exception e3) {
            return false;
        }
    }

    protected C1083P c(String str) {
        String strB = bH.W.b(str, " ", "");
        C1083P c1083p = new C1083P(this);
        c1083p.f7930b = Double.parseDouble(strB.substring(0, strB.indexOf(Marker.ANY_NON_NULL_MARKER)).trim());
        c1083p.f7932d = Double.parseDouble(strB.substring(strB.indexOf("(") + 1, strB.indexOf(LanguageTag.SEP)));
        c1083p.f7929a = Double.parseDouble(strB.substring(strB.lastIndexOf(LanguageTag.SEP) + 1, strB.lastIndexOf("))")));
        c1083p.f7931c = Double.parseDouble(strB.substring(strB.lastIndexOf(")/(") + 3, strB.lastIndexOf(LanguageTag.SEP)));
        int iIndexOf = strB.indexOf("((") + 2;
        c1083p.f7933e = strB.substring(iIndexOf, strB.indexOf(LanguageTag.SEP, iIndexOf));
        return c1083p;
    }

    public C1084Q d(String str) {
        String strTrim = str.trim();
        C1084Q c1084q = new C1084Q(this);
        String strTrim2 = strTrim.substring("table(".length(), strTrim.indexOf(",")).trim();
        String strTrim3 = strTrim.substring(strTrim.indexOf(",") + 1, strTrim.indexOf(")")).trim();
        if (strTrim3.startsWith(PdfOps.DOUBLE_QUOTE__TOKEN)) {
            strTrim3 = strTrim3.substring(1);
        }
        if (strTrim3.endsWith(PdfOps.DOUBLE_QUOTE__TOKEN)) {
            strTrim3 = strTrim3.substring(0, strTrim3.length() - 1);
        }
        c1084q.f7936b = strTrim2;
        c1084q.f7935a = strTrim3;
        return c1084q;
    }

    @Override // be.InterfaceC1099o
    public boolean c() {
        return this.f7925p.a();
    }

    public void f() {
        this.f7925p.b();
    }
}
