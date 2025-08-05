package be;

import G.C0048ah;
import G.C0126i;
import G.aI;
import G.dh;
import G.di;
import com.efiAnalytics.ui.C1647ee;
import com.efiAnalytics.ui.bV;
import com.efiAnalytics.ui.cF;
import com.efiAnalytics.ui.eJ;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import s.C1818g;

/* loaded from: TunerStudioMS.jar:be/y.class */
public class y extends JPanel implements InterfaceC1099o {

    /* renamed from: a, reason: collision with root package name */
    C1068A f8014a;

    /* renamed from: b, reason: collision with root package name */
    C1068A f8015b;

    /* renamed from: c, reason: collision with root package name */
    C1068A f8016c;

    /* renamed from: d, reason: collision with root package name */
    C1068A f8017d;

    /* renamed from: e, reason: collision with root package name */
    C1068A f8018e;

    /* renamed from: f, reason: collision with root package name */
    C1068A f8019f;

    /* renamed from: g, reason: collision with root package name */
    C1068A f8020g;

    /* renamed from: h, reason: collision with root package name */
    C1068A f8021h;

    /* renamed from: i, reason: collision with root package name */
    C1068A f8022i;

    /* renamed from: j, reason: collision with root package name */
    C1068A f8023j;

    /* renamed from: p, reason: collision with root package name */
    static final String f8029p = "[" + C1818g.b("New Category") + "]";

    /* renamed from: k, reason: collision with root package name */
    JComboBox f8024k = new JComboBox();

    /* renamed from: l, reason: collision with root package name */
    JComboBox f8025l = new JComboBox();

    /* renamed from: m, reason: collision with root package name */
    G.R f8026m = null;

    /* renamed from: n, reason: collision with root package name */
    C0048ah f8027n = null;

    /* renamed from: o, reason: collision with root package name */
    C1098n f8028o = new C1098n();

    /* renamed from: q, reason: collision with root package name */
    char[] f8030q = {' '};

    public y(G.R r2) {
        setBorder(BorderFactory.createTitledBorder(C1818g.b("Gauge Template")));
        setLayout(new GridLayout(0, 1, eJ.a(4), eJ.a(4)));
        C1647ee c1647ee = new C1647ee();
        this.f8014a = new C1068A(this, "", 18);
        this.f8014a.addKeyListener(this.f8028o);
        this.f8014a.addFocusListener(c1647ee);
        add(a("Name", this.f8014a, "A name that will be used to reference this Gauge Template. It must be unique or it will over-ride any existing gauge."));
        this.f8015b = new C1068A(this, "", 18);
        this.f8015b.addKeyListener(this.f8028o);
        this.f8015b.addFocusListener(c1647ee);
        add(a("Gauge Title", this.f8015b, "Title to be displayed on the Gauge."));
        this.f8016c = new C1068A(this, "", 18);
        this.f8016c.addKeyListener(this.f8028o);
        this.f8016c.addFocusListener(c1647ee);
        add(a("Units", this.f8016c, "(Optional) Units to be displayed on the Gauge Face."));
        this.f8025l.setPreferredSize(this.f8016c.getPreferredSize());
        add(a("Category", this.f8025l, "(Optional) The Category this gauge template will be placed under."));
        this.f8025l.addActionListener(new z(this));
        add(a("OutputChannel", this.f8024k, "The OutputChannel assigned to this Gauge Template, this is the value that will be displayed."));
        this.f8024k.setPreferredSize(this.f8016c.getPreferredSize());
        this.f8017d = new C1068A(this, "", 18);
        this.f8017d.addKeyListener(this.f8028o);
        this.f8017d.addFocusListener(c1647ee);
        add(a("Minimum", this.f8017d, "Minimum value on gauge. This can be either a number or an expression."));
        this.f8018e = new C1068A(this, "", 18);
        this.f8018e.addKeyListener(this.f8028o);
        this.f8018e.addFocusListener(c1647ee);
        add(a("Maximum", this.f8018e, "Maximum value on gauge. This can be either a number or an expression."));
        this.f8019f = new C1068A(this, "", 18);
        this.f8019f.addKeyListener(this.f8028o);
        this.f8019f.addFocusListener(c1647ee);
        add(a("Low Critical", this.f8019f, "Value to be displayed as critically low. This can be either a number or an expression."));
        this.f8020g = new C1068A(this, "", 18);
        this.f8020g.addKeyListener(this.f8028o);
        this.f8020g.addFocusListener(c1647ee);
        add(a("Low Warning", this.f8020g, "Value to be displayed as Warning level low. This can be either a number or an expression."));
        this.f8021h = new C1068A(this, "", 18);
        this.f8021h.addKeyListener(this.f8028o);
        this.f8021h.addFocusListener(c1647ee);
        add(a("High Warning", this.f8021h, "Value to be displayed as Warning level High. This can be either a number or an expression."));
        this.f8022i = new C1068A(this, "", 18);
        this.f8022i.addKeyListener(this.f8028o);
        this.f8022i.addFocusListener(c1647ee);
        add(a("High Critical", this.f8022i, "Value to be displayed as Critical level High. This can be either a number or an expression."));
        this.f8023j = new C1068A(this, "", 18);
        this.f8023j.addKeyListener(this.f8028o);
        this.f8023j.addFocusListener(c1647ee);
        add(a("Decimal Places", this.f8023j, "Number of decimal places to display on the Guage. This can be either a number or an expression."));
        a(r2);
    }

    private JPanel a(String str, Component component, String str2) {
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new BorderLayout(eJ.a(5), eJ.a(5)));
        jPanel.add(new JLabel(C1818g.b(str), 4), BorderLayout.CENTER);
        JPanel jPanel2 = new JPanel();
        jPanel2.setLayout(new BorderLayout(eJ.a(5), eJ.a(5)));
        jPanel2.add(component, BorderLayout.CENTER);
        if (str2 != null && !str2.isEmpty()) {
            jPanel2.add(new cF(C1818g.b(str2), C1818g.d()), "East");
        }
        jPanel.add(jPanel2, "East");
        return jPanel;
    }

    public void a(C0048ah c0048ah) {
        this.f8027n = c0048ah;
        this.f8014a.setText(c0048ah.aJ());
        this.f8014a.setEnabled(false);
        this.f8015b.setText(c0048ah.k().toString());
        this.f8016c.setText(c0048ah.j().toString());
        this.f8025l.setSelectedItem(c0048ah.p());
        this.f8024k.setSelectedItem(c0048ah.i());
        this.f8017d.setText(c0048ah.b().toString());
        this.f8018e.setText(c0048ah.e().toString());
        this.f8019f.setText(c0048ah.o().toString());
        this.f8020g.setText(c0048ah.f().toString());
        this.f8021h.setText(c0048ah.g().toString());
        this.f8022i.setText(c0048ah.h().toString());
        this.f8023j.setText(c0048ah.m().toString());
        this.f8028o.b();
    }

    public C0048ah a() throws V.a, NumberFormatException {
        if (this.f8027n == null) {
            this.f8027n = new C0048ah();
        }
        this.f8027n.v(this.f8014a.getText());
        this.f8027n.c(this.f8015b.getText());
        this.f8027n.b(this.f8016c.getText());
        this.f8027n.d(this.f8025l.getSelectedItem().toString());
        this.f8027n.a(this.f8024k.getSelectedItem().toString());
        try {
            dh dhVarA = di.a(this.f8026m, this.f8017d.getText());
            C0126i.a(this.f8017d.getText(), (aI) this.f8026m);
            this.f8027n.a(dhVarA);
            dh dhVarA2 = di.a(this.f8026m, this.f8018e.getText());
            C0126i.a(this.f8018e.getText(), (aI) this.f8026m);
            this.f8027n.b(dhVarA2);
            dh dhVarA3 = di.a(this.f8026m, this.f8019f.getText());
            C0126i.a(this.f8019f.getText(), (aI) this.f8026m);
            this.f8027n.h(dhVarA3);
            dh dhVarA4 = di.a(this.f8026m, this.f8020g.getText());
            C0126i.a(this.f8020g.getText(), (aI) this.f8026m);
            this.f8027n.c(dhVarA4);
            dh dhVarA5 = di.a(this.f8026m, this.f8021h.getText());
            C0126i.a(this.f8021h.getText(), (aI) this.f8026m);
            this.f8027n.d(dhVarA5);
            dh dhVarA6 = di.a(this.f8026m, this.f8022i.getText());
            C0126i.a(this.f8022i.getText(), (aI) this.f8026m);
            this.f8027n.e(dhVarA6);
            dh dhVarA7 = di.a(this.f8026m, this.f8023j.getText());
            C0126i.a(this.f8023j.getText(), (aI) this.f8026m);
            this.f8027n.f(dhVarA7);
            return this.f8027n;
        } catch (V.g e2) {
            throw new V.a(e2.getMessage());
        } catch (ax.U e3) {
            throw new V.a(e3.getMessage());
        }
    }

    public void a(String str) {
        boolean z2 = false;
        int i2 = 0;
        while (true) {
            if (i2 >= this.f8024k.getItemCount()) {
                break;
            }
            if (this.f8024k.getItemAt(i2).equals(str)) {
                z2 = true;
                break;
            }
            i2++;
        }
        if (!z2) {
            this.f8024k.addItem(str);
        }
        this.f8024k.setSelectedItem(str);
    }

    public R b() {
        R r2 = new R();
        if (this.f8014a.getText().trim().isEmpty()) {
            r2.c();
            r2.a("Name is required.");
            return r2;
        }
        if (this.f8014a.getText().trim().contains(" ")) {
            r2.c();
            r2.a("Name cannot have white spaces.");
            return r2;
        }
        if (this.f8015b.getText().trim().isEmpty()) {
            r2.c();
            r2.a("Title is required.");
            return r2;
        }
        if (this.f8017d.getText().trim().isEmpty()) {
            r2.c();
            r2.a("Minimum is required.");
            return r2;
        }
        if (this.f8018e.getText().trim().isEmpty()) {
            r2.c();
            r2.a("Maximum is required.");
            return r2;
        }
        if (this.f8019f.getText().trim().isEmpty()) {
            r2.c();
            r2.a("Low Critical is required.");
            return r2;
        }
        if (this.f8020g.getText().trim().isEmpty()) {
            r2.c();
            r2.a("Low Warning is required.");
            return r2;
        }
        if (this.f8021h.getText().trim().isEmpty()) {
            r2.c();
            r2.a("High Warning is required.");
            return r2;
        }
        if (this.f8022i.getText().trim().isEmpty()) {
            r2.c();
            r2.a("High Critical is required.");
            return r2;
        }
        if (this.f8023j.getText().trim().isEmpty()) {
            r2.c();
            r2.a("Digits is required.");
            return r2;
        }
        try {
            C0126i.a(this.f8017d.getText(), (aI) this.f8026m);
            C0126i.a(this.f8018e.getText(), (aI) this.f8026m);
            C0126i.a(this.f8019f.getText(), (aI) this.f8026m);
            C0126i.a(this.f8020g.getText(), (aI) this.f8026m);
            C0126i.a(this.f8021h.getText(), (aI) this.f8026m);
            C0126i.a(this.f8022i.getText(), (aI) this.f8026m);
            C0126i.a(this.f8023j.getText(), (aI) this.f8026m);
            r2.b();
            return r2;
        } catch (ax.U e2) {
            r2.c();
            r2.a(e2.getMessage());
            return r2;
        }
    }

    public void a(G.R r2) {
        this.f8026m = r2;
        this.f8024k.removeAllItems();
        for (String str : bH.R.a(r2.s())) {
            if (!str.contains(" ")) {
                this.f8024k.addItem(str);
            }
        }
        ArrayList arrayList = new ArrayList();
        arrayList.add("");
        arrayList.add(f8029p);
        Iterator itB = r2.B();
        while (itB.hasNext()) {
            C0048ah c0048ah = (C0048ah) itB.next();
            if (c0048ah.p() != null && !arrayList.contains(c0048ah.p())) {
                arrayList.add(c0048ah.p());
            }
        }
        bH.R.b(arrayList);
        this.f8025l.removeAllItems();
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            this.f8025l.addItem((String) it.next());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void d() {
        String strA;
        do {
            strA = bV.a((Component) this, false, C1818g.b("New Category Name, No special characters") + ": ", "");
            if (strA == null || strA.isEmpty()) {
                this.f8025l.setSelectedItem("");
                return;
            }
        } while (C0126i.a(strA, this.f8030q));
        this.f8025l.addItem(strA);
        this.f8025l.setSelectedItem(strA);
    }

    @Override // be.InterfaceC1099o
    public boolean c() {
        return this.f8028o.a();
    }
}
