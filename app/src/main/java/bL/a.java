package bL;

import bH.W;
import com.efiAnalytics.ui.C1589c;
import com.efiAnalytics.ui.InterfaceC1662et;
import com.efiAnalytics.ui.bV;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import s.C1818g;

/* loaded from: TunerStudioMS.jar:bL/a.class */
public class a extends JPanel {

    /* renamed from: a, reason: collision with root package name */
    C1589c f7122a;

    /* renamed from: b, reason: collision with root package name */
    InterfaceC1662et f7123b;

    /* renamed from: c, reason: collision with root package name */
    static String f7124c = C1818g.b("Maximum Cell Value Change");

    /* renamed from: d, reason: collision with root package name */
    static String f7125d = C1818g.b("Maximum Cell Percentage Change");

    /* renamed from: e, reason: collision with root package name */
    JButton f7126e = new JButton(f7124c);

    /* renamed from: f, reason: collision with root package name */
    JButton f7127f = new JButton(f7125d);

    /* renamed from: g, reason: collision with root package name */
    JComboBox f7128g = new JComboBox();

    public a(C1589c c1589c, InterfaceC1662et interfaceC1662et) {
        this.f7122a = null;
        this.f7123b = null;
        this.f7122a = c1589c;
        this.f7123b = interfaceC1662et;
        c();
        setLayout(new BorderLayout());
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new BorderLayout());
        jPanel.add(BorderLayout.CENTER, new JLabel(C1818g.b("Cell Change Resistance") + ": ", 4));
        jPanel.add("East", this.f7128g);
        Iterator itE = e();
        while (itE.hasNext()) {
            this.f7128g.addItem(itE.next());
        }
        a();
        this.f7128g.addActionListener(new b(this));
        add("North", jPanel);
        JPanel jPanel2 = new JPanel();
        jPanel2.setBorder(BorderFactory.createTitledBorder(C1818g.b("Authority Limits")));
        jPanel2.setLayout(new GridLayout(2, 0));
        jPanel2.add(this.f7126e);
        if (!c1589c.g()) {
            jPanel2.add(this.f7127f);
        }
        add(BorderLayout.CENTER, jPanel2);
        this.f7126e.addActionListener(new c(this));
        a(this.f7126e);
        if (c1589c.g()) {
            return;
        }
        this.f7127f.addActionListener(new d(this));
        a(this.f7127f);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a(e eVar) {
        this.f7122a.f(eVar.d());
        this.f7122a.c(eVar.b());
        this.f7122a.a(eVar.a());
        a("minChangeThreshold", "" + this.f7122a.c());
        a("weightThreshold", "" + this.f7122a.a());
        a("baseWeight", "" + this.f7122a.h());
        a("maxWeight", "" + this.f7122a.i());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void b() throws NumberFormatException {
        String strA = bV.a((Component) this, true, f7124c, "" + W.a(this.f7122a.d()));
        if (strA == null || strA.equals("")) {
            return;
        }
        double d2 = Double.parseDouble(strA);
        this.f7122a.d(d2);
        a("maxValueChange", d2 + "");
        a(this.f7126e);
    }

    private void c() {
        this.f7122a.c(a("minChangeThreshold", this.f7122a.c()));
        this.f7122a.a(a("weightThreshold", this.f7122a.a()));
        this.f7122a.f(a("baseWeight", this.f7122a.h()));
        this.f7122a.g(a("maxWeight", this.f7122a.i()));
        this.f7122a.d(a("maxValueChange", this.f7122a.d()));
        if (this.f7122a.g()) {
            return;
        }
        this.f7122a.e(a("maxPercentChange", this.f7122a.e()));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void d() {
        String strA;
        if (this.f7122a.g() || (strA = bV.a((Component) this, true, f7125d, "" + W.a(this.f7122a.e() * 100.0d))) == null || strA.equals("")) {
            return;
        }
        double d2 = Double.parseDouble(strA) / 100.0d;
        this.f7122a.e(d2);
        a("maxPercentChange", d2 + "");
        a(this.f7127f);
    }

    protected void a() {
        for (int i2 = 0; i2 < this.f7128g.getItemCount(); i2++) {
            if (((e) this.f7128g.getItemAt(i2)).d() == this.f7122a.h()) {
                this.f7128g.setSelectedIndex(i2);
            }
        }
    }

    private void a(String str, String str2) {
        if (this.f7123b != null) {
            this.f7123b.a(str, str2);
        }
    }

    private double a(String str, double d2) {
        if (this.f7123b == null) {
            return d2;
        }
        String strA = this.f7123b.a(str);
        return (strA == null || strA.equals("")) ? d2 : Double.parseDouble(strA);
    }

    private void a(JButton jButton) {
        if (jButton.equals(this.f7126e)) {
            jButton.setText(f7124c + ": " + W.b(this.f7122a.d(), 1));
        } else if (jButton.equals(this.f7127f)) {
            jButton.setText(f7125d + ": " + W.a(this.f7122a.e() * 100.0d));
        }
    }

    private Iterator e() {
        ArrayList arrayList = new ArrayList();
        arrayList.add(new e(this, "Very Easy", 0.0d, 0.0d, 0.5d, 5.0d));
        arrayList.add(new e(this, "Easy", 0.0d, 0.0d, 3.0d, 100.0d));
        arrayList.add(new e(this, "Normal", 0.0d, 0.0d, 5.0d, 300.0d));
        arrayList.add(new e(this, "Hard", 1.0d, 0.1d, 20.0d, 1000.0d));
        arrayList.add(new e(this, "Very Hard", 1.0d, 0.25d, 100.0d, 100000.0d));
        return arrayList.iterator();
    }
}
