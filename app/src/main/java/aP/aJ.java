package aP;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JPanel;

/* loaded from: TunerStudioMS.jar:aP/aJ.class */
public class aJ extends JPanel {

    /* renamed from: a, reason: collision with root package name */
    aL f2819a = new aL(this, "1st Gear");

    /* renamed from: b, reason: collision with root package name */
    aL f2820b = new aL(this, "2nd Gear");

    /* renamed from: c, reason: collision with root package name */
    aL f2821c = new aL(this, "3rd Gear");

    /* renamed from: d, reason: collision with root package name */
    aL f2822d = new aL(this, "4th Gear");

    /* renamed from: e, reason: collision with root package name */
    aL f2823e = new aL(this, "5th Gear");

    /* renamed from: f, reason: collision with root package name */
    aL f2824f = new aL(this, "6th Gear");

    /* renamed from: g, reason: collision with root package name */
    aL f2825g = new aL(this, "Converter Stall (RPM)");

    /* renamed from: h, reason: collision with root package name */
    aL f2826h = new aL(this, "Final Drive Ratio");

    /* renamed from: i, reason: collision with root package name */
    JCheckBox f2827i = new JCheckBox("Automatic Transmission");

    /* renamed from: j, reason: collision with root package name */
    com.efiAnalytics.tuningwidgets.panels.aQ f2828j = new com.efiAnalytics.tuningwidgets.panels.aQ("Vehicle Weight");

    public aJ() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder("Vehicle Information"));
        add("North", this.f2828j);
        JPanel jPanel = new JPanel();
        jPanel.setBorder(BorderFactory.createTitledBorder("Transmission Ratios"));
        jPanel.setLayout(new GridLayout(0, 2, 6, 0));
        jPanel.add(this.f2819a);
        jPanel.add(this.f2820b);
        jPanel.add(this.f2821c);
        jPanel.add(this.f2822d);
        jPanel.add(this.f2823e);
        jPanel.add(this.f2824f);
        add(BorderLayout.CENTER, jPanel);
        JPanel jPanel2 = new JPanel();
        jPanel2.setBorder(BorderFactory.createTitledBorder("Transmission Type"));
        jPanel2.setLayout(new GridLayout(0, 1));
        jPanel2.add(this.f2827i);
        this.f2827i.addChangeListener(new aK(this));
        jPanel2.add(this.f2825g);
        jPanel2.add(this.f2826h);
        add("South", jPanel2);
    }

    public void a(String str) {
        this.f2819a.a(str);
    }

    public String a() {
        return this.f2819a.a();
    }

    public void b(String str) {
        this.f2820b.a(str);
    }

    public String b() {
        return this.f2820b.a();
    }

    public void c(String str) {
        this.f2821c.a(str);
    }

    public String c() {
        return this.f2821c.a();
    }

    public void d(String str) {
        this.f2822d.a(str);
    }

    public String d() {
        return this.f2822d.a();
    }

    public void e(String str) {
        this.f2823e.a(str);
    }

    public String e() {
        return this.f2823e.a();
    }

    public void f(String str) {
        this.f2824f.a(str);
    }

    public String f() {
        return this.f2824f.a();
    }

    public void a(boolean z2) {
        this.f2827i.setSelected(z2);
        this.f2825g.setEnabled(z2);
    }

    public boolean g() {
        return this.f2827i.isSelected();
    }

    public void g(String str) {
        this.f2825g.a(str);
    }

    public String h() {
        return this.f2825g.a();
    }

    public void h(String str) {
        this.f2826h.a(str);
    }

    public String i() {
        return this.f2826h.a();
    }

    public String j() {
        return this.f2828j.a();
    }

    public void i(String str) {
        this.f2828j.a(str);
    }
}
