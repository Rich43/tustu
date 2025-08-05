package be;

import G.C0043ac;
import G.C0126i;
import G.aI;
import G.cY;
import G.di;
import com.efiAnalytics.ui.C1647ee;
import com.efiAnalytics.ui.Cdo;
import com.efiAnalytics.ui.cF;
import com.efiAnalytics.ui.eJ;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import s.C1818g;

/* renamed from: be.g, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:be/g.class */
public class C1091g extends JPanel implements InterfaceC1099o {

    /* renamed from: a, reason: collision with root package name */
    C1093i f7970a;

    /* renamed from: b, reason: collision with root package name */
    C1093i f7971b;

    /* renamed from: c, reason: collision with root package name */
    C1093i f7972c;

    /* renamed from: d, reason: collision with root package name */
    Cdo f7973d;

    /* renamed from: e, reason: collision with root package name */
    JComboBox f7974e = new JComboBox();

    /* renamed from: f, reason: collision with root package name */
    JComboBox f7975f = new JComboBox();

    /* renamed from: g, reason: collision with root package name */
    G.R f7976g = null;

    /* renamed from: h, reason: collision with root package name */
    C0043ac f7977h = null;

    /* renamed from: i, reason: collision with root package name */
    C1098n f7978i = new C1098n();

    public C1091g(G.R r2) {
        setBorder(BorderFactory.createTitledBorder(C1818g.b("DataLog Field")));
        setLayout(new GridLayout(0, 1, eJ.a(4), eJ.a(4)));
        C1647ee c1647ee = new C1647ee();
        this.f7970a = new C1093i(this, "", 18);
        add(a("Log Field Name", this.f7970a, "The name this field will have in captured data logs."));
        this.f7970a.addKeyListener(this.f7978i);
        this.f7970a.addFocusListener(c1647ee);
        add(a("OutputChannel", this.f7974e, "The OutputChannel assigned to this data log field, this is the value that will be logged."));
        this.f7974e.setPreferredSize(this.f7970a.getPreferredSize());
        this.f7974e.addActionListener(this.f7978i);
        this.f7975f.addItem(C0043ac.f699e);
        this.f7975f.addItem(C0043ac.f698d);
        this.f7975f.addItem(C0043ac.f700f);
        this.f7975f.addItem(C0043ac.f702h);
        this.f7975f.addItem(C0043ac.f697c);
        this.f7975f.addItem(C0043ac.f696b);
        this.f7975f.setSelectedItem(0);
        this.f7975f.setPreferredSize(this.f7970a.getPreferredSize());
        this.f7975f.addActionListener(new C1092h(this));
        this.f7975f.addActionListener(this.f7978i);
        add(a("Value Format", this.f7975f, "Select the format this field will be displayed in the log file. It can be the typical Numeric or one of the optional boolean formats."));
        this.f7973d = new Cdo("", 18);
        this.f7973d.b(0);
        this.f7973d.addKeyListener(this.f7978i);
        this.f7973d.addFocusListener(c1647ee);
        add(a("Decimal Place", this.f7973d, "The number of decimal places to log."));
        this.f7971b = new C1093i(this, "", 18);
        this.f7971b.addKeyListener(this.f7978i);
        this.f7971b.addFocusListener(c1647ee);
        add(a("Enabled Expression", this.f7971b, "(Optional) If provided, this condition must be true for this field to be logged."));
        this.f7972c = new C1093i(this, "", 18);
        this.f7972c.addKeyListener(this.f7978i);
        this.f7972c.addFocusListener(c1647ee);
        add(a("Record Lag", this.f7972c, "(Optional) A number or expression resolving to the number of records to lag this field behind others. By default and in most cases this is 0."));
        a(r2);
    }

    public C0043ac a() throws V.a {
        if (this.f7977h == null) {
            this.f7977h = new C0043ac();
        }
        try {
            this.f7977h.b(this.f7974e.getSelectedItem().toString());
        } catch (Exception e2) {
        }
        try {
            this.f7977h.a(cY.a().a(this.f7976g, this.f7970a.getText()));
            if (!this.f7971b.getText().trim().isEmpty()) {
                this.f7977h.u(this.f7971b.getText());
            }
            if (!this.f7972c.getText().trim().isEmpty()) {
                try {
                    this.f7977h.a(di.a(this.f7976g, this.f7972c.getText()));
                } catch (V.g e3) {
                    throw new V.a(e3.getMessage());
                }
            }
            if (this.f7973d.isEnabled()) {
                this.f7977h.a((int) Math.round(this.f7973d.e()));
            } else {
                this.f7977h.a(0);
                this.f7977h.a(this.f7975f.getSelectedItem().toString());
            }
            return this.f7977h;
        } catch (V.g e4) {
            throw new V.a(e4.getMessage());
        }
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

    public void a(String str) {
        boolean z2 = false;
        if (str == null) {
            this.f7974e.setSelectedIndex(0);
            return;
        }
        int i2 = 0;
        while (true) {
            if (i2 < this.f7974e.getItemCount()) {
                if (this.f7974e.getItemAt(i2) != null && this.f7974e.getItemAt(i2).equals(str)) {
                    z2 = true;
                    break;
                }
                i2++;
            } else {
                break;
            }
        }
        if (!z2) {
            this.f7974e.addItem(str);
        }
        this.f7974e.setSelectedItem(str);
    }

    public R b() throws NumberFormatException {
        R rA = C1086b.a(this.f7970a.getText().trim());
        if (!rA.a()) {
            return rA;
        }
        String strTrim = this.f7971b.getText().trim();
        if (!strTrim.isEmpty()) {
            try {
                C0126i.a(strTrim, (aI) this.f7976g);
            } catch (ax.U e2) {
                rA.c();
                rA.a(e2.getLocalizedMessage());
                return rA;
            }
        }
        String strTrim2 = this.f7972c.getText().trim();
        if (!strTrim2.isEmpty()) {
            try {
                C0126i.a(strTrim2, (aI) this.f7976g);
            } catch (ax.U e3) {
                rA.c();
                rA.a(e3.getLocalizedMessage());
                return rA;
            }
        }
        if (!Double.isNaN(this.f7973d.e())) {
            rA.b();
            return rA;
        }
        rA.c();
        rA.a("Please set the number of decimal places.");
        return rA;
    }

    public void a(G.R r2) {
        this.f7976g = r2;
        this.f7974e.removeAllItems();
        for (String str : bH.R.a(r2.s())) {
            if (!str.contains(" ")) {
                this.f7974e.addItem(str);
            }
        }
    }

    public void a(C0043ac c0043ac) {
        this.f7977h = c0043ac;
        this.f7970a.setText(this.f7977h.b());
        this.f7971b.setText(this.f7977h.aH());
        this.f7972c.setText(this.f7977h.g() != null ? this.f7977h.g().toString() : "");
        this.f7973d.a(this.f7977h.d());
        a(this.f7977h.a());
        this.f7975f.setSelectedItem(this.f7977h.k());
        this.f7978i.b();
    }

    @Override // be.InterfaceC1099o
    public boolean c() {
        return this.f7978i.a();
    }
}
