package br;

import G.C0072be;
import G.InterfaceC0042ab;
import G.aH;
import G.bL;
import G.dc;
import aP.C0338f;
import bt.bO;
import com.efiAnalytics.tuningwidgets.panels.aP;
import com.efiAnalytics.ui.C1580br;
import com.efiAnalytics.ui.C1589c;
import com.efiAnalytics.ui.C1701s;
import com.efiAnalytics.ui.C1704v;
import com.efiAnalytics.ui.InterfaceC1565bc;
import com.efiAnalytics.ui.bV;
import com.efiAnalytics.ui.cA;
import com.efiAnalytics.ui.dQ;
import com.efiAnalytics.ui.eJ;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JToggleButton;
import org.icepdf.core.util.PdfOps;
import r.C1798a;
import s.C1818g;
import sun.security.pkcs11.wrapper.Constants;

/* loaded from: TunerStudioMS.jar:br/P.class */
public class P extends al implements InterfaceC0042ab, InterfaceC1565bc {

    /* renamed from: a, reason: collision with root package name */
    G.R f8374a;

    /* renamed from: b, reason: collision with root package name */
    dc f8375b;

    /* renamed from: f, reason: collision with root package name */
    C1704v f8379f;

    /* renamed from: g, reason: collision with root package name */
    C1704v f8380g;

    /* renamed from: j, reason: collision with root package name */
    af f8383j;

    /* renamed from: m, reason: collision with root package name */
    dQ f8386m;

    /* renamed from: o, reason: collision with root package name */
    C1589c f8388o;

    /* renamed from: c, reason: collision with root package name */
    n.n f8376c = null;

    /* renamed from: d, reason: collision with root package name */
    JPanel f8377d = new JPanel();

    /* renamed from: e, reason: collision with root package name */
    JPanel f8378e = new JPanel();

    /* renamed from: h, reason: collision with root package name */
    aa f8381h = null;

    /* renamed from: i, reason: collision with root package name */
    JToggleButton f8382i = new JToggleButton(C1818g.b("Start"));

    /* renamed from: k, reason: collision with root package name */
    JCheckBox f8384k = null;

    /* renamed from: l, reason: collision with root package name */
    ac f8385l = null;

    /* renamed from: n, reason: collision with root package name */
    JPanel f8387n = null;

    /* renamed from: p, reason: collision with root package name */
    List f8389p = new ArrayList();

    /* renamed from: q, reason: collision with root package name */
    String f8390q = "15";

    public P(G.R r2, dc dcVar) {
        this.f8374a = null;
        this.f8375b = null;
        this.f8383j = null;
        this.f8386m = null;
        this.f8388o = null;
        this.f8374a = r2;
        this.f8375b = dcVar;
        this.f8386m = new dQ(aE.a.A(), "TrimAnalyzePanel_" + dcVar.b());
        String strB = this.f8386m.b("targetLambdaTableName", dcVar.n());
        if (strB != null && !strB.equals("")) {
            dcVar.c(strB);
        }
        String strB2 = this.f8386m.b("targetLambdaChannelName", dcVar.t());
        if (strB2 != null && !strB2.isEmpty()) {
            dcVar.r(strB2);
        }
        this.f8388o = C1232J.a().c(r2, dcVar.a(0));
        if (dcVar.o("disableMaxPercentLimit")) {
            this.f8388o.f();
        }
        a(dcVar);
        this.f8383j = new af(this);
        d();
        r2.h().a(this);
    }

    private void d() {
        ae aeVar = new ae(this, this.f8374a, this.f8375b.e());
        setLayout(new BorderLayout());
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new BorderLayout());
        jPanel.setBorder(BorderFactory.createTitledBorder(C1818g.b("Trim Table Auto Tune") + " " + C1818g.b("Control Panel")));
        JPanel jPanel2 = new JPanel();
        jPanel2.setLayout(new BoxLayout(jPanel2, 0));
        this.f8381h = new aa(this, C1818g.b("Idle"), 0);
        jPanel2.add(this.f8381h);
        jPanel2.add(new JLabel(Constants.INDENT));
        jPanel2.add(this.f8382i);
        this.f8382i.setMnemonic('A');
        this.f8382i.addActionListener(new Q(this));
        jPanel.add("East", jPanel2);
        JPanel jPanel3 = new JPanel();
        jPanel3.setLayout(new BoxLayout(jPanel3, 0));
        this.f8390q = this.f8386m.b("applyPeriod", "15");
        this.f8384k = new JCheckBox(C1818g.b("Update Controller") + "   ");
        this.f8384k.setToolTipText(C1818g.b("<html>Check to have VE Analyze Recommendations automatically <br>Sent to the controller. If unchecked, VE Analyze Live will <br>produce the recommended table, but not send changes until the Send button is clicked.</html>"));
        this.f8384k.setSelected(!this.f8390q.equals("Manually"));
        this.f8384k.addActionListener(new S(this));
        jPanel3.add(this.f8384k);
        JButton jButton = new JButton(C1818g.b("Apply"));
        jButton.setMnemonic('S');
        jButton.setToolTipText(C1818g.b("<html>Send the VE Analyze Recommendations<br>to the ECU Now. The engine will be running on the recommended <br>VE Table, but not nessecarily permenantly stored.</html>"));
        jButton.addActionListener(new T(this));
        jPanel3.add(jButton);
        JButton jButton2 = new JButton(C1818g.b("Save on ECU"));
        jButton2.setMnemonic('E');
        jButton2.addActionListener(new U(this));
        jPanel3.add(jButton2);
        jButton2.setToolTipText("<html>" + C1818g.b("Stores the Recommended VE table values to ECU<br>" + C1818g.b("FLASH for persistent storage.") + "</html>"));
        this.f8387n = new JPanel();
        this.f8387n.setLayout(new FlowLayout());
        jPanel3.add(this.f8387n);
        jPanel.add("West", jPanel3);
        add("North", jPanel);
        JPanel jPanel4 = new JPanel();
        jPanel4.setLayout(new BorderLayout());
        jPanel4.add("North", new bL.a(this.f8388o, this.f8386m));
        JPanel jPanel5 = new JPanel();
        jPanel5.setLayout(new BorderLayout());
        jPanel5.add(BorderLayout.CENTER, new C1237a(this.f8374a, this.f8375b));
        jPanel4.add(BorderLayout.CENTER, jPanel5);
        JPanel jPanel6 = new JPanel();
        jPanel6.setBorder(BorderFactory.createTitledBorder(C1818g.b("Reference Tables")));
        jPanel6.setLayout(new GridLayout(0, 2));
        JButton jButton3 = new JButton(C1818g.b("Lambda Delay"));
        jButton3.addActionListener(new V(this));
        jPanel6.add(jButton3);
        JButton jButton4 = new JButton(C1818g.b("AFR Targets"));
        jButton4.addActionListener(new W(this));
        jPanel6.add(jButton4);
        jPanel4.add("South", jPanel6);
        this.f8376c = new n.n();
        JPanel jPanel7 = new JPanel();
        C1701s c1701sH = ((ag) this.f8389p.get(0)).f8425a.h();
        jPanel7.setLayout(new GridLayout(2, 1));
        this.f8379f = new C1704v(c1701sH);
        cA cAVar = new cA(this.f8379f);
        aeVar.a(cAVar);
        this.f8379f.c(2);
        cAVar.a(0.0d, getBackground());
        cAVar.a(8.0d, Color.yellow);
        cAVar.a(50.0d, Color.GREEN);
        cAVar.a(150.0d, Color.GREEN.darker());
        cAVar.a(c1701sH.w());
        cAVar.b(c1701sH.v());
        cAVar.c(C1818g.b("Data Weighting"));
        JPanel jPanel8 = new JPanel();
        jPanel8.setLayout(new BorderLayout());
        jPanel8.setBorder(BorderFactory.createTitledBorder(C1818g.b("Cell Weighting")));
        jPanel8.add(BorderLayout.CENTER, cAVar);
        jPanel7.add(jPanel8);
        this.f8380g = new C1704v(c1701sH);
        cA cAVar2 = new cA(this.f8380g);
        aeVar.a(cAVar2);
        this.f8380g.c(3);
        cAVar2.a(-18.0d, Color.red.darker());
        cAVar2.a(-7.0d, Color.red);
        cAVar2.a(0.0d, getBackground());
        cAVar2.a(7.0d, Color.blue);
        cAVar2.a(18.0d, Color.blue.darker());
        cAVar2.a(c1701sH.w());
        cAVar2.b(c1701sH.v());
        cAVar2.c(C1818g.b("Cell Change"));
        JPanel jPanel9 = new JPanel();
        jPanel9.setLayout(new BorderLayout());
        jPanel9.setBorder(BorderFactory.createTitledBorder(C1818g.b("Cell Change")));
        jPanel9.add(BorderLayout.CENTER, cAVar2);
        jPanel7.add(jPanel9);
        this.f8376c.addTab(C1818g.b("Status"), jPanel7);
        JPanel jPanel10 = new JPanel();
        jPanel10.setLayout(new BorderLayout());
        jPanel10.add("North", jPanel4);
        this.f8376c.addTab(C1818g.b("Advanced Settings"), jPanel10);
        add("East", this.f8376c);
        add(BorderLayout.CENTER, this.f8378e);
        add("South", this.f8383j);
        a(false);
    }

    private void a(dc dcVar) {
        this.f8377d.setLayout(new CardLayout());
        this.f8389p.clear();
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new BorderLayout());
        this.f8378e.setLayout(new BorderLayout());
        this.f8378e.add(jPanel, BorderLayout.CENTER);
        jPanel.add(BorderLayout.CENTER, this.f8377d);
        JPanel jPanel2 = new JPanel();
        jPanel2.setLayout(new FlowLayout(0));
        jPanel.add("North", jPanel2);
        ButtonGroup buttonGroup = new ButtonGroup();
        int iB = 6;
        try {
            iB = this.f8374a.c(((C0072be) this.f8374a.e().c(dcVar.a(0))).a()).b();
        } catch (Exception e2) {
        }
        int i2 = iB > 8 ? 2 : 4;
        int iA = dcVar.a() > i2 ? i2 : dcVar.a();
        X x2 = new X(this);
        int i3 = 0;
        while (i3 < dcVar.a()) {
            int i4 = i3 + 1;
            int i5 = (i4 + iA) - 1;
            int iA2 = i5 > dcVar.a() ? dcVar.a() : i5;
            JRadioButton jRadioButton = new JRadioButton(C1818g.b("Trim Table") + " " + i4 + " - " + iA2);
            buttonGroup.add(jRadioButton);
            String string = Integer.toString(i3);
            jRadioButton.setActionCommand(string);
            jRadioButton.addActionListener(x2);
            jPanel2.add(jRadioButton);
            JPanel jPanel3 = new JPanel();
            if (iA2 - i4 <= 2) {
                jPanel3.setLayout(new GridLayout(1, 0));
            } else {
                jPanel3.setLayout(new GridLayout(2, 0));
            }
            this.f8377d.add(jPanel3, string);
            for (int i6 = 0; i6 < iA; i6++) {
                ag agVar = new ag(this.f8374a, dcVar, dcVar.a(i3), this.f8388o);
                this.f8389p.add(agVar);
                jPanel3.add(agVar);
                i3++;
            }
        }
        buttonGroup.getElements().nextElement2().setSelected(true);
        e();
    }

    private void e() {
        Iterator it = this.f8389p.iterator();
        while (it.hasNext()) {
            ((ag) it.next()).b();
        }
    }

    private boolean f() {
        Iterator it = this.f8389p.iterator();
        while (it.hasNext()) {
            if (!((ag) it.next()).c()) {
                return false;
            }
        }
        return true;
    }

    @Override // br.al
    public void a(JComponent jComponent) {
        this.f8387n.add(jComponent);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void g() {
        try {
            aP aPVar = new aP(this.f8374a, bO.a().a(bO.a().a(this.f8374a, this.f8375b.b()), this.f8375b.b()), this.f8375b.b());
            a((InterfaceC1565bc) aPVar);
            aPVar.setPreferredSize(new Dimension(eJ.a(290), eJ.a(170)));
            bV.a(aPVar, this, C1818g.b("Lambda Delay (ms)"), aPVar);
        } catch (V.g e2) {
            bH.C.a("Can not show Lambda delay Table.", e2, this);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void b(JComponent jComponent) {
        C1580br c1580br = new C1580br();
        Iterator itJ = this.f8375b.j();
        while (itJ.hasNext()) {
            String str = (String) itJ.next();
            boolean zEquals = str.equals(this.f8375b.s());
            if (str.equals("afrTSCustom")) {
                c1580br.add((JMenuItem) b(C1798a.f13268b + " " + C1818g.b("Custom"), str, zEquals));
            } else {
                C0072be c0072be = (C0072be) this.f8374a.e().c(str);
                if (c0072be != null) {
                    c1580br.add((JMenuItem) b(bH.W.b(C1818g.b(c0072be.M()), PdfOps.DOUBLE_QUOTE__TOKEN, ""), str, zEquals));
                } else {
                    bV.d(str + " is defined as an Target Lambda Table, but not found in the current config.", jComponent);
                }
            }
        }
        Iterator itK = this.f8375b.k();
        while (itK.hasNext()) {
            String str2 = (String) itK.next();
            boolean zEquals2 = str2.equals(this.f8375b.s());
            aH aHVarG = this.f8374a.g(str2);
            String strJ = bL.j(this.f8374a, str2);
            if (strJ == null) {
                strJ = str2;
            }
            if (aHVarG != null) {
                c1580br.add((JMenuItem) a(bH.W.b(C1818g.b(strJ), PdfOps.DOUBLE_QUOTE__TOKEN, ""), str2, zEquals2));
            } else {
                bV.d(str2 + " is defined as an Target Lambda Channel, but not found in the current config.", jComponent);
            }
        }
        add(c1580br);
        c1580br.show(jComponent, 0, jComponent.getHeight());
    }

    private JCheckBoxMenuItem a(String str, String str2, boolean z2) {
        String str3 = "";
        Y y2 = null;
        boolean z3 = this.f8375b.t() != null && this.f8375b.t().equals(str2);
        if (!z2) {
            str3 = C1818g.b("Switch to Channel") + " - " + str;
            y2 = new Y(this);
        }
        if (z3) {
            str3 = C1818g.b("Channel") + " " + str + " (" + C1818g.b(Action.DEFAULT) + ")";
        }
        JCheckBoxMenuItem jCheckBoxMenuItem = new JCheckBoxMenuItem(str3, z2);
        jCheckBoxMenuItem.addActionListener(y2);
        jCheckBoxMenuItem.setActionCommand(str2);
        return jCheckBoxMenuItem;
    }

    private JCheckBoxMenuItem b(String str, String str2, boolean z2) {
        String str3;
        ActionListener r2;
        boolean z3 = this.f8375b.n() != null && (this.f8375b.t() == null || this.f8375b.t().isEmpty()) && this.f8375b.n().equals(str2);
        if (z2) {
            str3 = C1818g.b("Edit / View") + " - " + str;
            r2 = new Z(this);
        } else {
            str3 = C1818g.b("Switch to Table") + " - " + str;
            r2 = new R(this);
        }
        if (z3) {
            str3 = str3 + " (" + C1818g.b(Action.DEFAULT) + ")";
        }
        JCheckBoxMenuItem jCheckBoxMenuItem = new JCheckBoxMenuItem(str3, z2);
        jCheckBoxMenuItem.addActionListener(r2);
        jCheckBoxMenuItem.setActionCommand(str2);
        return jCheckBoxMenuItem;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void h() {
        if (!this.f8375b.c().equals("afrTSCustom")) {
            C0338f.a().a(this.f8374a.c() + "." + this.f8375b.c(), "0", bV.a(this));
            return;
        }
        try {
            aP aPVar = new aP(this.f8374a, bO.a().a(this.f8374a, this.f8375b.c(), "", this.f8375b.b()), this.f8375b.b());
            a((InterfaceC1565bc) aPVar);
            bV.a(aPVar, this, C1818g.b("Target AFR Table"), aPVar);
        } catch (V.g e2) {
            bH.C.a("Can not show AFR Table.", e2, this);
        }
    }

    public void a(String str) {
        this.f8390q = str;
        this.f8386m.a("applyPeriod", this.f8390q);
        if (this.f8385l != null) {
            j();
        } else if (this.f8390q.equals("Manually")) {
            i();
        }
    }

    public void a() {
        Iterator it = this.f8389p.iterator();
        while (it.hasNext()) {
            ((ag) it.next()).a();
        }
    }

    public boolean b() {
        Iterator it = this.f8389p.iterator();
        while (it.hasNext()) {
            if (((ag) it.next()).d()) {
                return true;
            }
        }
        return false;
    }

    public void c() {
        a();
        this.f8374a.I();
    }

    private void i() {
        if (this.f8385l != null) {
            this.f8385l.a();
            this.f8385l = null;
            a();
        }
    }

    private void j() {
        i();
        if (this.f8390q.equals("Manually")) {
            return;
        }
        this.f8385l = new ac(this, Integer.parseInt(this.f8390q));
        this.f8385l.start();
    }

    @Override // br.al
    protected void a(boolean z2) {
        if (z2 && f()) {
            this.f8381h.setText(C1818g.b("Correcting VE"));
            this.f8382i.setText(C1818g.b("Stop Auto Tune"));
            j();
            Iterator it = this.f8389p.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                ag agVar = (ag) it.next();
                if (agVar.c()) {
                    agVar.f8425a.a(this.f8383j);
                    this.f8379f.a(agVar.f8425a.h());
                    this.f8380g.a(agVar.f8425a.h());
                    break;
                }
            }
        } else {
            this.f8381h.setText(C1818g.b("Idle"));
            this.f8382i.setText(C1818g.b("Start Auto Tune"));
            i();
            k();
            for (ag agVar2 : this.f8389p) {
                if (agVar2.c()) {
                    agVar2.f8425a.b(this.f8383j);
                }
            }
        }
        Iterator it2 = this.f8389p.iterator();
        while (it2.hasNext()) {
            try {
                ((ag) it2.next()).a(z2);
            } catch (V.a e2) {
                bV.d(e2.getMessage(), this);
                a(false);
                return;
            }
        }
        this.f8381h.a(z2);
    }

    private void k() {
        int iC = C1798a.a().c(C1798a.di, 0);
        if (iC == 25) {
            C1798a.a().b(C1798a.dj, "true");
        }
        C1798a.a().b(C1798a.di, "" + (iC + 1));
    }

    @Override // com.efiAnalytics.ui.InterfaceC1565bc
    public void close() {
        a(false);
        if (b() && bV.a(C1818g.b("VE Analysis has recommended VE changes that have not been sent to the controller.\n Would you like to save the recommended table to the controller now?"), (Component) this, true)) {
            c();
        }
        super.l();
        ((ag) this.f8389p.get(0)).f8425a.b(this.f8383j);
        Iterator it = this.f8389p.iterator();
        while (it.hasNext()) {
            ((ag) it.next()).e();
        }
        this.f8374a.h().b(this);
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public Dimension getMinimumSize() {
        return eJ.a(500, 410);
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public Dimension getPreferredSize() {
        return eJ.a(800, 450);
    }

    @Override // G.InterfaceC0042ab
    public void a(String str, int i2, int i3, int[] iArr) {
        e();
    }
}
