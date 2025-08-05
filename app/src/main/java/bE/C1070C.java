package be;

import G.C0048ah;
import G.aH;
import aP.C0338f;
import bf.C1108a;
import bf.C1117j;
import com.efiAnalytics.ui.bV;
import com.efiAnalytics.ui.cO;
import com.efiAnalytics.ui.eJ;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Window;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import s.C1818g;

/* renamed from: be.C, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:be/C.class */
public class C1070C extends JPanel implements InterfaceC1100p {

    /* renamed from: a, reason: collision with root package name */
    C1108a f7887a;

    /* renamed from: d, reason: collision with root package name */
    JButton f7891d;

    /* renamed from: e, reason: collision with root package name */
    JButton f7892e;

    /* renamed from: f, reason: collision with root package name */
    JButton f7893f;

    /* renamed from: g, reason: collision with root package name */
    JButton f7894g;

    /* renamed from: b, reason: collision with root package name */
    C1103s f7888b = new C1103s();

    /* renamed from: j, reason: collision with root package name */
    private G.R f7889j = null;

    /* renamed from: c, reason: collision with root package name */
    C1069B f7890c = null;

    /* renamed from: h, reason: collision with root package name */
    JComboBox f7895h = new JComboBox();

    /* renamed from: i, reason: collision with root package name */
    C1079L f7896i = new C1079L(this);

    public C1070C() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder(C1818g.b("Custom Configuration Editor")));
        int iA = eJ.a(30);
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new GridLayout(1, 0, eJ.a(2), eJ.a(2)));
        try {
            this.f7891d = new JButton(new ImageIcon(cO.a().a(cO.f11116F, this)));
            this.f7891d.setToolTipText(C1818g.b("Create New Component"));
            this.f7891d.setPreferredSize(new Dimension(iA, iA));
            this.f7891d.addActionListener(new C1071D(this));
            jPanel.add(this.f7891d);
            this.f7892e = new JButton(new ImageIcon(cO.a().a(cO.f11103s, this)));
            this.f7892e.setToolTipText(C1818g.b("Edit Selected Component"));
            this.f7892e.setPreferredSize(new Dimension(iA, iA));
            this.f7892e.addActionListener(new C1072E(this));
            jPanel.add(this.f7892e);
            this.f7893f = new JButton(new ImageIcon(cO.a().a(cO.f11089e, this)));
            this.f7893f.setToolTipText(C1818g.b("Delete Selected Component"));
            this.f7893f.setPreferredSize(new Dimension(iA, iA));
            this.f7893f.addActionListener(new C1073F(this));
            jPanel.add(this.f7893f);
            jPanel.add(new JLabel());
            this.f7894g = new JButton(new ImageIcon(cO.a().a(cO.f11123M, this)));
            this.f7894g.setToolTipText(C1818g.b("Save Configuration"));
            this.f7894g.setPreferredSize(new Dimension(iA, iA));
            this.f7894g.addActionListener(new C1074G(this));
            jPanel.add(this.f7894g);
        } catch (V.a e2) {
        }
        JPanel jPanel2 = new JPanel();
        jPanel2.setLayout(new BorderLayout());
        jPanel2.add(jPanel, "West");
        String[] strArrD = G.T.a().d();
        if (strArrD.length > 1) {
            JPanel jPanel3 = new JPanel();
            jPanel3.setLayout(new FlowLayout(2));
            jPanel2.add(jPanel3, BorderLayout.CENTER);
            for (String str : strArrD) {
                this.f7895h.addItem(str);
            }
            this.f7895h.addActionListener(new C1075H(this));
            jPanel3.add(this.f7895h);
        }
        add(jPanel2, "North");
        this.f7887a = new C1108a();
        this.f7887a.b(C1108a.f8032a);
        this.f7887a.b(C1108a.f8033b);
        this.f7887a.b(C1108a.f8034c);
        this.f7887a.a(this.f7896i);
        this.f7887a.a(new C1078K(this));
        add(this.f7887a, "West");
        Component jScrollPane = new JScrollPane(this.f7888b);
        jScrollPane.setPreferredSize(new Dimension(eJ.a(640), eJ.a(500)));
        add(jScrollPane, BorderLayout.CENTER);
        b();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a() {
        String string = this.f7895h.getSelectedItem().toString();
        String strC = this.f7890c.d().c();
        if (strC.equals(string)) {
            return;
        }
        if (strC.equals(string) || !this.f7890c.c() || bV.a(C1818g.b("There are unsaved changes, Are you sure you want to close without saving?"), (Component) this, true)) {
            a(G.T.a().c(string));
        } else {
            this.f7895h.setSelectedItem(strC);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void b() {
        this.f7891d.setEnabled(this.f7896i.f7906a != null);
        this.f7892e.setEnabled(this.f7896i.f7907b != null);
        this.f7893f.setEnabled(this.f7896i.f7907b != null);
        this.f7894g.setEnabled(this.f7890c != null && this.f7890c.c());
    }

    public void a(Window window) {
        JDialog jDialog = new JDialog(window, C1818g.b("Custom Configuration Editor"));
        jDialog.add(BorderLayout.CENTER, this);
        jDialog.pack();
        bV.a(window, (Component) jDialog);
        jDialog.addWindowListener(new C1076I(this, jDialog));
        jDialog.setVisible(true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void c() {
        if (C0338f.a().b(this.f7889j)) {
            this.f7890c.b();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void d() throws NumberFormatException {
        C1117j c1117j = this.f7896i.f7906a.a() != null ? (C1117j) this.f7896i.f7906a.getParent() : this.f7896i.f7906a;
        G.Q qA = C1102r.a(c1117j.toString(), this.f7889j);
        if (qA == null) {
            bV.d("Failed to create new component", this);
            return;
        }
        if (qA instanceof C0048ah) {
            String strA = bV.a((Component) this, false, C1818g.b("Name of new " + c1117j.toString()), C1818g.b("My") + bH.W.b(c1117j.toString(), " ", ""));
            if (strA == null || strA.trim().isEmpty()) {
                return;
            }
            qA.v(bH.W.b(strA, " ", "_"));
            ((C0048ah) qA).d(C1818g.b("My Gauges"));
        } else {
            qA.v(bH.W.b(C1818g.b("My") + bH.W.b(c1117j.toString(), " ", ""), " ", "_"));
        }
        d(qA);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void e() throws NumberFormatException {
        d(this.f7896i.f7907b);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void f() {
        if (this.f7896i.f7907b != null) {
            if (!(this.f7896i.f7907b instanceof aH)) {
                this.f7890c.b(this.f7896i.f7907b);
                return;
            }
            aH aHVar = (aH) this.f7896i.f7907b;
            if (this.f7890c.a(aHVar.aJ()) && bV.a(C1818g.b("There are Gauge Templates referencing this OutputChannel.") + "\n" + C1818g.b("Would you like to delete these Gauge Templates also?."), (Component) this, true)) {
                this.f7890c.b(aHVar.aJ());
            }
            this.f7890c.b(aHVar);
        }
    }

    public void a(G.R r2) {
        this.f7889j = r2;
        this.f7887a.a(r2);
        this.f7888b.a(r2);
        if (this.f7890c != null) {
            this.f7890c.a(r2);
            return;
        }
        this.f7890c = new C1069B(r2);
        this.f7890c.a(true);
        this.f7888b.a(this.f7890c);
        this.f7890c.a(new C1077J(this));
        this.f7890c.a(this.f7887a.a());
        this.f7890c.a(this);
    }

    public void d(G.Q q2) throws NumberFormatException {
        this.f7888b.a(q2);
    }

    @Override // be.InterfaceC1100p
    public void a(G.Q q2) {
    }

    @Override // be.InterfaceC1100p
    public void b(G.Q q2) {
    }

    @Override // be.InterfaceC1100p
    public void c(G.Q q2) {
    }

    @Override // be.InterfaceC1100p
    public void a(boolean z2) {
        this.f7894g.setEnabled(z2);
        this.f7894g.repaint();
    }
}
