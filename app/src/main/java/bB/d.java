package bB;

import bH.R;
import bH.aa;
import com.efiAnalytics.ui.C1685fp;
import com.efiAnalytics.ui.Cdo;
import com.efiAnalytics.ui.eJ;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Container;
import java.awt.GridLayout;
import java.util.Iterator;
import java.util.List;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import jdk.internal.dynalink.CallSiteDescriptor;

/* loaded from: TunerStudioMS.jar:bB/d.class */
public class d extends JPanel {

    /* renamed from: a, reason: collision with root package name */
    aa f6532a;

    /* renamed from: d, reason: collision with root package name */
    JCheckBox f6535d;

    /* renamed from: e, reason: collision with root package name */
    JCheckBox f6536e;

    /* renamed from: f, reason: collision with root package name */
    JCheckBox f6537f;

    /* renamed from: l, reason: collision with root package name */
    CardLayout f6543l;

    /* renamed from: b, reason: collision with root package name */
    JTextField f6533b = new JTextField("", 15);

    /* renamed from: c, reason: collision with root package name */
    JComboBox f6534c = new JComboBox();

    /* renamed from: g, reason: collision with root package name */
    Cdo f6538g = new Cdo("", 15);

    /* renamed from: h, reason: collision with root package name */
    Cdo f6539h = new Cdo("", 15);

    /* renamed from: i, reason: collision with root package name */
    Cdo f6540i = new Cdo("", 15);

    /* renamed from: j, reason: collision with root package name */
    a f6541j = null;

    /* renamed from: k, reason: collision with root package name */
    JPanel f6542k = new JPanel();

    public d(aa aaVar) {
        this.f6532a = aaVar;
        setLayout(new GridLayout(0, 1));
        this.f6543l = new CardLayout();
        this.f6542k.setLayout(this.f6543l);
        this.f6542k.add(a("Field Name", this.f6533b), "showFieldNameTxtInput");
        this.f6533b.setEditable(false);
        this.f6534c.setEditable(false);
        this.f6542k.add(a("Field Name", this.f6534c), "showFieldNameSelector");
        add(this.f6542k);
        this.f6535d = new JCheckBox(b("Auto Scale Min"));
        this.f6535d.addActionListener(new e(this));
        add(this.f6535d);
        add(a("Min", this.f6538g));
        this.f6536e = new JCheckBox(b("Auto Scale Max"));
        this.f6536e.addActionListener(new f(this));
        add(this.f6536e);
        add(a("Max", this.f6539h));
        this.f6537f = new JCheckBox(b("Auto Decimal"));
        this.f6540i.b(0);
        this.f6537f.addActionListener(new g(this));
        add(this.f6537f);
        add(a("Decimal Places", this.f6540i));
    }

    private JPanel a(String str, JComponent jComponent) {
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new BorderLayout(eJ.a(5), eJ.a(5)));
        JLabel jLabel = new JLabel(b(str) + CallSiteDescriptor.TOKEN_DELIMITER, 4);
        jLabel.setMinimumSize(eJ.a(100, 20));
        jLabel.setPreferredSize(eJ.a(100, 20));
        jPanel.add(jLabel, "West");
        jPanel.add(jComponent, BorderLayout.CENTER);
        return jPanel;
    }

    public void a(boolean z2) {
        this.f6535d.setSelected(z2);
        this.f6538g.setEditable(!this.f6536e.isSelected());
    }

    public void b(boolean z2) {
        this.f6536e.setSelected(z2);
        this.f6539h.setEditable(!this.f6536e.isSelected());
    }

    public void c(boolean z2) {
        this.f6537f.setSelected(z2);
        this.f6540i.setEditable(!this.f6537f.isSelected());
    }

    public void a(List list) {
        List listB = R.b(list);
        this.f6534c.removeAllItems();
        this.f6534c.addItem(" ");
        Iterator it = listB.iterator();
        while (it.hasNext()) {
            this.f6534c.addItem((String) it.next());
        }
    }

    public void a(String str, double d2, double d3, int i2) {
        a(str);
        a(d2);
        b(d3);
        a(i2);
        d();
    }

    public void a(String str) {
        if (str.equals(" ")) {
            this.f6543l.show(this.f6542k, "showFieldNameSelector");
            this.f6533b.setText("");
        } else {
            this.f6543l.show(this.f6542k, "showFieldNameTxtInput");
            this.f6533b.setText(str);
        }
    }

    public void a(double d2) {
        this.f6538g.a(d2);
        a(Double.isNaN(d2));
        this.f6538g.setEditable(!this.f6535d.isSelected());
    }

    public void b(double d2) {
        this.f6539h.a(d2);
        b(Double.isNaN(d2));
        this.f6539h.setEditable(!this.f6536e.isSelected());
    }

    public void a(int i2) {
        if (i2 >= 0) {
            this.f6540i.a(i2);
        } else {
            this.f6540i.a(-1.0d);
        }
        c(i2 == -1);
    }

    public String[] a() {
        return this.f6533b.getText().split(", ");
    }

    public boolean b() {
        return this.f6541j != null && a(e(), this.f6541j);
    }

    public boolean c() {
        return !b();
    }

    public void d() {
        this.f6541j = g();
    }

    public boolean a(r rVar, r rVar2) {
        return rVar.e().equals(rVar2.e()) && (rVar.a() == rVar2.a() || (Double.isNaN(rVar.a()) && Double.isNaN(rVar2.a()))) && (rVar.b() == rVar2.b() || (Double.isNaN(rVar.b()) && Double.isNaN(rVar2.b())));
    }

    public r e() {
        return g();
    }

    private a g() {
        a aVar = new a();
        String text = this.f6533b.getText();
        if (text.contains(",")) {
            text = text.substring(0, text.indexOf(","));
        }
        aVar.a(text);
        if (this.f6535d.isSelected()) {
            aVar.a(Double.NaN);
        } else {
            aVar.a(this.f6538g.e());
        }
        if (this.f6536e.isSelected()) {
            aVar.b(Double.NaN);
        } else {
            aVar.b(this.f6539h.e());
        }
        if (this.f6537f.isSelected()) {
            aVar.a(-1);
        } else {
            aVar.a((int) this.f6540i.e());
        }
        return aVar;
    }

    private String b(String str) {
        return this.f6532a != null ? this.f6532a.a(str) : str;
    }

    void f() {
        a(" ", Double.NaN, Double.NaN, -1);
    }

    @Override // javax.swing.JComponent, java.awt.Component, c.InterfaceC1385d
    public void setEnabled(boolean z2) {
        C1685fp.a((Container) this, z2);
        super.setEnabled(z2);
    }
}
