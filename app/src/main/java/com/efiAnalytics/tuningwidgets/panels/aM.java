package com.efiAnalytics.tuningwidgets.panels;

import G.bF;
import G.bG;
import com.efiAnalytics.ui.Cdo;
import com.efiAnalytics.ui.InterfaceC1662et;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.Iterator;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import org.icepdf.core.util.PdfOps;
import s.C1818g;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/tuningwidgets/panels/aM.class */
public class aM extends aL {

    /* renamed from: a, reason: collision with root package name */
    bF f10345a;

    /* renamed from: b, reason: collision with root package name */
    Cdo f10346b = new Cdo();

    /* renamed from: c, reason: collision with root package name */
    Cdo f10347c = new Cdo();

    /* renamed from: d, reason: collision with root package name */
    Cdo f10348d = new Cdo();

    /* renamed from: e, reason: collision with root package name */
    Cdo f10349e = new Cdo();

    /* renamed from: f, reason: collision with root package name */
    Cdo f10350f = new Cdo();

    /* renamed from: g, reason: collision with root package name */
    Cdo f10351g = new Cdo();

    /* renamed from: h, reason: collision with root package name */
    Cdo f10352h = new Cdo();

    /* renamed from: i, reason: collision with root package name */
    JRadioButton f10353i = new JRadioButton("Fahrenheit");

    /* renamed from: j, reason: collision with root package name */
    JRadioButton f10354j = new JRadioButton("Celsius");

    /* renamed from: k, reason: collision with root package name */
    JComboBox f10355k = new JComboBox();

    /* renamed from: n, reason: collision with root package name */
    JLabel f10358n = new JLabel(f10357m, 0);

    /* renamed from: p, reason: collision with root package name */
    double f10360p;

    /* renamed from: q, reason: collision with root package name */
    double f10361q;

    /* renamed from: r, reason: collision with root package name */
    double f10362r;

    /* renamed from: l, reason: collision with root package name */
    public static String f10356l = C1818g.b("Temperature") + "(" + bH.S.a() + "F)";

    /* renamed from: m, reason: collision with root package name */
    public static String f10357m = C1818g.b("Temperature") + "(" + bH.S.a() + "C)";

    /* renamed from: o, reason: collision with root package name */
    static String f10359o = "<html><font color=\"gray\">" + C1818g.b("Select a Common Sensor") + "</font></html>";

    public aM(bF bFVar) {
        this.f10345a = null;
        this.f10345a = bFVar;
        setBorder(BorderFactory.createTitledBorder(C1818g.b(bFVar.h())));
        setLayout(new BorderLayout(5, 5));
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new GridLayout(0, 1, 5, 5));
        this.f10355k.addItem(f10359o);
        this.f10355k.setEditable(false);
        Iterator itA = bFVar.a();
        while (itA.hasNext()) {
            this.f10355k.addItem(C1818g.b(((bG) itA.next()).a()));
        }
        this.f10355k.addItemListener(new aN(this));
        JPanel jPanel2 = new JPanel();
        jPanel2.setLayout(new BorderLayout(2, 2));
        jPanel2.add(BorderLayout.CENTER, new JLabel(C1818g.b("Common Sensor Values"), 4));
        jPanel2.add("East", this.f10355k);
        jPanel.add(jPanel2);
        JPanel jPanel3 = new JPanel();
        jPanel3.setLayout(new BorderLayout());
        jPanel3.add(BorderLayout.CENTER, new JLabel(C1818g.b("Bias Resistor Value (Ohms)"), 0));
        jPanel3.add("East", this.f10346b);
        jPanel.add(jPanel3);
        aO aOVar = new aO(this);
        JPanel jPanel4 = new JPanel();
        jPanel4.setLayout(new GridLayout(1, 2));
        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(this.f10353i);
        jPanel4.add(this.f10353i);
        this.f10353i.addActionListener(aOVar);
        this.f10354j.addActionListener(aOVar);
        buttonGroup.add(this.f10354j);
        jPanel4.add(this.f10354j);
        this.f10354j.setSelected(true);
        jPanel.add(jPanel4);
        add("North", jPanel);
        JPanel jPanel5 = new JPanel();
        jPanel5.setLayout(new FlowLayout(1));
        JPanel jPanel6 = new JPanel();
        jPanel6.setLayout(new GridLayout(0, 2, 5, 5));
        jPanel6.add(this.f10358n);
        jPanel6.add(new JLabel(C1818g.b("Resistance (Ohms)"), 0));
        jPanel6.add(this.f10350f);
        jPanel6.add(this.f10347c);
        jPanel6.add(this.f10351g);
        jPanel6.add(this.f10348d);
        jPanel6.add(this.f10352h);
        jPanel6.add(this.f10349e);
        jPanel5.add(jPanel6);
        add(BorderLayout.CENTER, jPanel5);
    }

    public void a(String str) {
        if (str == null || str.trim().equals("") || str.trim().equals(f10359o)) {
            this.f10346b.setText("");
            this.f10347c.setText("");
            this.f10348d.setText("");
            this.f10349e.setText("");
            this.f10350f.setText("");
            this.f10351g.setText("");
            this.f10352h.setText("");
            return;
        }
        bG bGVarB = b(str);
        if (bGVarB == null) {
            return;
        }
        this.f10346b.setText("" + bGVarB.b());
        this.f10347c.setText("" + bGVarB.d());
        this.f10348d.setText("" + bGVarB.f());
        this.f10349e.setText("" + bGVarB.h());
        if (!this.f10353i.isSelected()) {
            this.f10350f.setText("" + bGVarB.c());
            this.f10351g.setText("" + bGVarB.e());
            this.f10352h.setText("" + bGVarB.g());
        } else {
            double dRound = Math.round((((bGVarB.c() * 9.0d) / 5.0d) + 32.0d) * 1000.0d) / 1000.0d;
            double dRound2 = Math.round((((bGVarB.e() * 9.0d) / 5.0d) + 32.0d) * 1000.0d) / 1000.0d;
            this.f10350f.setText("" + dRound);
            this.f10351g.setText("" + dRound2);
            this.f10352h.setText("" + (Math.round((((bGVarB.g() * 9.0d) / 5.0d) + 32.0d) * 1000.0d) / 1000.0d));
        }
    }

    private bG b(String str) {
        Iterator itA = this.f10345a.a();
        while (itA.hasNext()) {
            bG bGVar = (bG) itA.next();
            if (C1818g.b(bGVar.a()).equals(str)) {
                return bGVar;
            }
        }
        return null;
    }

    private void c() {
        Iterator itA = this.f10345a.a();
        while (itA.hasNext()) {
            bG bGVar = (bG) itA.next();
            if (c(bGVar.a())) {
                this.f10355k.setSelectedItem(C1818g.b(bGVar.a()));
            }
        }
    }

    private boolean c(String str) throws NumberFormatException {
        bG bGVarB = b(str);
        if (bGVarB == null) {
            return false;
        }
        if (this.f10354j.isSelected()) {
            return this.f10346b.getText().equals(new StringBuilder().append("").append(bGVarB.b()).toString()) && this.f10347c.getText().equals(new StringBuilder().append("").append(bGVarB.d()).toString()) && this.f10348d.getText().equals(new StringBuilder().append("").append(bGVarB.f()).toString()) && this.f10349e.getText().equals(new StringBuilder().append("").append(bGVarB.h()).toString()) && this.f10350f.getText().equals(new StringBuilder().append("").append(bGVarB.c()).toString()) && this.f10351g.getText().equals(new StringBuilder().append("").append(bGVarB.e()).toString()) && this.f10352h.getText().equals(new StringBuilder().append("").append(bGVarB.g()).toString());
        }
        return this.f10346b.getText().equals(new StringBuilder().append("").append(bGVarB.b()).toString()) && this.f10347c.getText().equals(new StringBuilder().append("").append(bGVarB.d()).toString()) && this.f10348d.getText().equals(new StringBuilder().append("").append(bGVarB.f()).toString()) && this.f10349e.getText().equals(new StringBuilder().append("").append(bGVarB.h()).toString()) && ((double) Math.round((((Double.parseDouble(this.f10350f.getText()) - 32.0d) * 5.0d) / 9.0d) * 1000.0d)) / 1000.0d == bGVarB.c() && ((double) Math.round((((Double.parseDouble(this.f10351g.getText()) - 32.0d) * 5.0d) / 9.0d) * 1000.0d)) / 1000.0d == bGVarB.e() && ((double) Math.round((((Double.parseDouble(this.f10352h.getText()) - 32.0d) * 5.0d) / 9.0d) * 1000.0d)) / 1000.0d == bGVarB.g();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void d() throws IllegalArgumentException {
        if (this.f10353i.isSelected() && !this.f10358n.getText().equals(f10356l)) {
            this.f10358n.setText(f10356l);
            if (this.f10350f.getText().length() <= 0 || this.f10351g.getText().length() <= 0 || this.f10352h.getText().length() <= 0) {
                return;
            }
            double d2 = Double.parseDouble(this.f10350f.getText());
            double d3 = Double.parseDouble(this.f10351g.getText());
            double d4 = Double.parseDouble(this.f10352h.getText());
            double dRound = Math.round((((d2 * 9.0d) / 5.0d) + 32.0d) * 1000.0d) / 1000.0d;
            double dRound2 = Math.round((((d3 * 9.0d) / 5.0d) + 32.0d) * 1000.0d) / 1000.0d;
            this.f10350f.setText(bH.W.b(dRound, 3));
            this.f10351g.setText(bH.W.b(dRound2, 3));
            this.f10352h.setText(bH.W.b(Math.round((((d4 * 9.0d) / 5.0d) + 32.0d) * 1000.0d) / 1000.0d, 3));
            return;
        }
        if (!this.f10354j.isSelected() || this.f10358n.getText().equals(f10357m)) {
            return;
        }
        this.f10358n.setText(f10357m);
        if (this.f10350f.getText().length() <= 0 || this.f10351g.getText().length() <= 0 || this.f10352h.getText().length() <= 0) {
            return;
        }
        double d5 = Double.parseDouble(this.f10350f.getText());
        double d6 = Double.parseDouble(this.f10351g.getText());
        double d7 = Double.parseDouble(this.f10352h.getText());
        double dRound3 = Math.round((((d5 - 32.0d) * 5.0d) / 9.0d) * 1000.0d) / 1000.0d;
        double dRound4 = Math.round((((d6 - 32.0d) * 5.0d) / 9.0d) * 1000.0d) / 1000.0d;
        this.f10350f.a(dRound3);
        this.f10351g.a(dRound4);
        this.f10352h.a(Math.round((((d7 - 32.0d) * 5.0d) / 9.0d) * 1000.0d) / 1000.0d);
    }

    @Override // com.efiAnalytics.tuningwidgets.panels.aL
    public String a() {
        return "//--    Generated by ThermTableGenerator using user input values. \n//--    Bias Resistor value: " + this.f10346b.getText() + " Ohms\n//--    Temperature1: " + this.f10350f.getText() + "(" + (this.f10354j.isSelected() ? "C" : PdfOps.F_TOKEN) + "), \tResistance 1: " + this.f10347c.getText() + " Ohms \n//--    Temperature2: " + this.f10351g.getText() + "(" + (this.f10354j.isSelected() ? "C" : PdfOps.F_TOKEN) + "), \tResistance 2: " + this.f10348d.getText() + " Ohms \n//--    Temperature3: " + this.f10352h.getText() + "(" + (this.f10354j.isSelected() ? "C" : PdfOps.F_TOKEN) + "), \tResistance 3: " + this.f10349e.getText() + " Ohms \n//-- \n";
    }

    @Override // com.efiAnalytics.tuningwidgets.panels.aL
    public double[] a(int i2) throws V.a, NumberFormatException {
        try {
            if (!b()) {
                throw new V.a("You must populate all temperature and resistor values");
            }
            double[] dArr = new double[i2];
            double[] dArr2 = new double[3];
            dArr2[0] = Double.parseDouble(this.f10350f.getText());
            dArr2[1] = Double.parseDouble(this.f10351g.getText());
            dArr2[2] = Double.parseDouble(this.f10352h.getText());
            double d2 = Double.parseDouble(this.f10347c.getText());
            double d3 = Double.parseDouble(this.f10348d.getText());
            double d4 = Double.parseDouble(this.f10349e.getText());
            double d5 = Double.parseDouble(this.f10346b.getText());
            boolean zIsSelected = this.f10354j.isSelected();
            for (int i3 = 0; i3 < 3; i3++) {
                if (!zIsSelected) {
                    dArr2[i3] = ((dArr2[i3] - 32.0d) * 5.0d) / 9.0d;
                }
                int i4 = i3;
                dArr2[i4] = dArr2[i4] + 273.15d;
            }
            double dLog = Math.log(d2);
            double dPow = Math.pow(dLog, 3.0d);
            double d6 = 1.0d / dArr2[0];
            double dLog2 = Math.log(d3);
            double dPow2 = Math.pow(dLog2, 3.0d);
            double d7 = 1.0d / dArr2[1];
            double dLog3 = Math.log(d4);
            double dPow3 = Math.pow(dLog3, 3.0d);
            double d8 = 1.0d / dArr2[2];
            this.f10362r = ((d7 - d6) - (((d8 - d6) * (dLog2 - dLog)) / (dLog3 - dLog))) / ((dPow2 - dPow) - (((dPow3 - dPow) * (dLog2 - dLog)) / (dLog3 - dLog)));
            this.f10361q = ((d8 - d6) - (this.f10362r * (dPow3 - dPow))) / (dLog3 - dLog);
            this.f10360p = (d6 - (this.f10361q * dLog)) - (this.f10362r * dPow);
            int i5 = 0;
            while (i5 < i2) {
                dArr[i5] = c(d5 / (((i2 - 1) / (i5 == 0 ? 0.01d : i5)) - 1.0d));
                i5++;
            }
            return dArr;
        } catch (V.a e2) {
            throw e2;
        }
    }

    double a(double d2) {
        return ((d2 * 9.0d) / 5.0d) - 459.67d;
    }

    double b(double d2) {
        return 1.0d / ((this.f10360p + (this.f10361q * Math.log(d2))) + (this.f10362r * Math.pow(Math.log(d2), 3.0d)));
    }

    double c(double d2) {
        return a(b(d2));
    }

    public boolean b() throws V.a {
        if (this.f10347c.getText().length() <= 0 || this.f10350f.getText().length() <= 0 || this.f10348d.getText().length() <= 0 || this.f10351g.getText().length() <= 0 || this.f10348d.getText().length() <= 0 || this.f10351g.getText().length() <= 0 || this.f10346b.getText().length() <= 0) {
            throw new V.a("You must populate all temperature and resistor values.");
        }
        if (this.f10346b.e() < 0.0d) {
            throw new V.a("Bias Resistor cannot be negative.");
        }
        if (this.f10347c.e() < 0.0d || this.f10348d.e() < 0.0d || this.f10349e.e() < 0.0d) {
            throw new V.a("Resistor values cannot be negative.");
        }
        if (this.f10350f.e() > 1000.0d || this.f10351g.e() > 1000.0d || this.f10352h.e() > 1000.0d) {
            throw new V.a("Temperatures are unusual high, are they input correctly?");
        }
        if (this.f10350f.e() > this.f10351g.e() || this.f10351g.e() > this.f10352h.e()) {
            throw new V.a("Temperatures should go from low to high, are they input correctly?");
        }
        return true;
    }

    @Override // javax.swing.JComponent, java.awt.Component, c.InterfaceC1385d
    public void setEnabled(boolean z2) {
        a(this, z2);
    }

    private void a(Container container, boolean z2) {
        for (int i2 = 0; i2 < container.getComponentCount(); i2++) {
            Component component = container.getComponent(i2);
            if (component instanceof Container) {
                a((Container) component, z2);
            }
            component.setEnabled(z2);
        }
    }

    @Override // com.efiAnalytics.tuningwidgets.panels.aL
    public void a(InterfaceC1662et interfaceC1662et) {
        interfaceC1662et.a("txtBiasResistor", this.f10346b.getText());
        interfaceC1662et.a("txtResistor1", this.f10347c.getText());
        interfaceC1662et.a("txtResistor2", this.f10348d.getText());
        interfaceC1662et.a("txtResistor3", this.f10349e.getText());
        interfaceC1662et.a("txtTemp1", this.f10350f.getText());
        interfaceC1662et.a("txtTemp2", this.f10351g.getText());
        interfaceC1662et.a("txtTemp3", this.f10352h.getText());
        interfaceC1662et.a("chkFahrenheit", "" + this.f10353i.isSelected());
        interfaceC1662et.a("chkCelsius", "" + this.f10354j.isSelected());
    }

    @Override // com.efiAnalytics.tuningwidgets.panels.aL
    public void b(InterfaceC1662et interfaceC1662et) throws IllegalArgumentException {
        String strA = interfaceC1662et.a("chkFahrenheit");
        this.f10353i.setSelected(strA != null && strA.equals("true"));
        String strA2 = interfaceC1662et.a("chkCelsius");
        this.f10354j.setSelected(strA2 != null && strA2.equals("true"));
        if (this.f10354j.isSelected()) {
            this.f10358n.setText(f10357m);
        } else {
            this.f10358n.setText(f10356l);
        }
        String strA3 = interfaceC1662et.a("txtBiasResistor");
        if (strA3 != null && strA3.length() > 0) {
            this.f10346b.setText(strA3);
        }
        String strA4 = interfaceC1662et.a("txtResistor1");
        if (strA4 != null && strA4.length() > 0) {
            this.f10347c.setText(strA4);
        }
        String strA5 = interfaceC1662et.a("txtResistor2");
        if (strA5 != null && strA5.length() > 0) {
            this.f10348d.setText(strA5);
        }
        String strA6 = interfaceC1662et.a("txtResistor3");
        if (strA6 != null && strA6.length() > 0) {
            this.f10349e.setText(strA6);
        }
        String strA7 = interfaceC1662et.a("txtTemp1");
        if (strA7 != null && strA7.length() > 0) {
            this.f10350f.setText(strA7);
        }
        String strA8 = interfaceC1662et.a("txtTemp2");
        if (strA8 != null && strA8.length() > 0) {
            this.f10351g.setText(strA8);
        }
        String strA9 = interfaceC1662et.a("txtTemp3");
        if (strA9 != null && strA9.length() > 0) {
            this.f10352h.setText(strA9);
        }
        c();
    }
}
