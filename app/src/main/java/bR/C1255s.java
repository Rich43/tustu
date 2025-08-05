package br;

import G.C0072be;
import G.C0088bu;
import G.aH;
import G.bL;
import G.dk;
import aP.C0338f;
import bH.C1007o;
import bt.C1324bf;
import bt.bO;
import com.efiAnalytics.tuningwidgets.panels.aP;
import com.efiAnalytics.ui.C1562b;
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
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import org.icepdf.core.util.PdfOps;
import r.C1798a;
import s.C1818g;
import sun.security.pkcs11.wrapper.Constants;

/* renamed from: br.s, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:br/s.class */
public class C1255s extends al implements InterfaceC1565bc {

    /* renamed from: a, reason: collision with root package name */
    G.R f8512a;

    /* renamed from: b, reason: collision with root package name */
    dk f8513b;

    /* renamed from: c, reason: collision with root package name */
    C1324bf f8514c;

    /* renamed from: g, reason: collision with root package name */
    C1231I f8518g;

    /* renamed from: h, reason: collision with root package name */
    C1250n f8519h;

    /* renamed from: i, reason: collision with root package name */
    C1589c f8520i;

    /* renamed from: j, reason: collision with root package name */
    JButton f8521j;

    /* renamed from: m, reason: collision with root package name */
    dQ f8524m;

    /* renamed from: p, reason: collision with root package name */
    private String f8527p;

    /* renamed from: d, reason: collision with root package name */
    n.n f8515d = null;

    /* renamed from: e, reason: collision with root package name */
    C1226D f8516e = null;

    /* renamed from: f, reason: collision with root package name */
    JToggleButton f8517f = new JToggleButton(C1818g.b("Start"));

    /* renamed from: k, reason: collision with root package name */
    JCheckBox f8522k = null;

    /* renamed from: l, reason: collision with root package name */
    C1228F f8523l = null;

    /* renamed from: n, reason: collision with root package name */
    JPanel f8525n = null;

    /* renamed from: o, reason: collision with root package name */
    String f8526o = "15";

    public C1255s(G.R r2, dk dkVar) {
        this.f8512a = null;
        this.f8513b = null;
        this.f8514c = null;
        this.f8518g = null;
        this.f8519h = null;
        this.f8520i = null;
        this.f8524m = null;
        this.f8527p = null;
        this.f8512a = r2;
        this.f8513b = dkVar;
        this.f8527p = dkVar.b();
        this.f8524m = new dQ(aE.a.A(), "VeAnalyzePanel_" + e());
        String strB = this.f8524m.b("targetLambdaTableName", dkVar.n());
        if (strB != null && !strB.equals("")) {
            dkVar.c(strB);
        }
        String strB2 = this.f8524m.b("targetLambdaChannelName", dkVar.t());
        if (strB2 != null && !strB2.isEmpty()) {
            dkVar.r(strB2);
        }
        this.f8520i = C1232J.a().c(r2, this.f8527p);
        if (dkVar.o("disableMaxPercentLimit")) {
            this.f8520i.f();
        }
        this.f8519h = C1254r.a().a(r2, dkVar, this.f8520i);
        this.f8514c = new C1324bf();
        a((InterfaceC1565bc) this.f8514c);
        this.f8518g = new C1231I(this);
        this.f8519h.a(this.f8518g);
        f();
        a(dkVar);
    }

    private void f() {
        C1230H c1230h = new C1230H(this, this.f8512a, this.f8513b.e());
        setLayout(new BorderLayout());
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new BorderLayout());
        JPanel jPanel2 = new JPanel();
        jPanel2.setLayout(new BorderLayout());
        jPanel2.setBorder(BorderFactory.createTitledBorder(C1818g.b(bL.c(this.f8512a, e())) + " " + C1818g.b("Control Panel")));
        JPanel jPanel3 = new JPanel();
        jPanel3.setLayout(new BoxLayout(jPanel3, 0));
        this.f8516e = new C1226D(this, C1818g.b("Idle"), 0);
        jPanel3.add(this.f8516e);
        jPanel3.add(new JLabel(Constants.INDENT));
        jPanel3.add(this.f8517f);
        this.f8517f.setMnemonic('A');
        this.f8517f.addActionListener(new C1256t(this));
        jPanel2.add("East", jPanel3);
        JPanel jPanel4 = new JPanel();
        jPanel4.setLayout(new BoxLayout(jPanel4, 0));
        this.f8526o = this.f8524m.b("applyPeriod", "15");
        this.f8522k = new JCheckBox(C1818g.b("Update Controller") + "   ");
        this.f8522k.setToolTipText(C1818g.b("<html>Check to have VE Analyze Recommendations automatically <br>Sent to the controller. If unchecked, VE Analyze Live will <br>produce the recommended table, but not send changes until the Send button is clicked.</html>"));
        this.f8522k.setSelected(!this.f8526o.equals("Manually"));
        this.f8522k.addActionListener(new C1258v(this));
        jPanel4.add(this.f8522k);
        JButton jButton = new JButton(C1818g.b("Apply"));
        jButton.setMnemonic('S');
        jButton.setToolTipText(C1818g.b("<html>Send the VE Analyze Recommendations<br>to the ECU Now. The engine will be running on the recommended <br>VE Table, but not nessecarily permenantly stored.</html>"));
        jButton.addActionListener(new C1259w(this));
        jPanel4.add(jButton);
        JButton jButton2 = new JButton(C1818g.b("Save on ECU"));
        jButton2.setMnemonic('E');
        jButton2.addActionListener(new C1260x(this));
        jPanel4.add(jButton2);
        jButton2.setToolTipText("<html>" + C1818g.b("Stores the Recommended VE table values to ECU<br>" + C1818g.b("FLASH for persistent storage.") + "</html>"));
        this.f8525n = new JPanel();
        this.f8525n.setLayout(new FlowLayout());
        jPanel4.add(this.f8525n);
        jPanel4.add(new JLabel(Constants.INDENT));
        this.f8521j = new JButton(C1818g.b("Reset"));
        this.f8521j.addActionListener(new C1261y(this));
        this.f8521j.setToolTipText("<html>" + C1818g.b("Resets Heat Maps and underlying data that has produced table changes.<br>" + C1818g.b("The VE Table will remain as it is.") + "</html>"));
        jPanel4.add(this.f8521j);
        this.f8521j.setEnabled(false);
        jPanel2.add("West", jPanel4);
        add("North", jPanel2);
        jPanel.add(BorderLayout.CENTER, this.f8514c);
        JPanel jPanel5 = new JPanel();
        jPanel5.setLayout(new BorderLayout());
        jPanel5.add("North", new bL.a(this.f8520i, this.f8524m));
        JPanel jPanel6 = new JPanel();
        jPanel6.setLayout(new BorderLayout());
        jPanel6.add(BorderLayout.CENTER, new C1237a(this.f8512a, this.f8513b));
        jPanel5.add(BorderLayout.CENTER, jPanel6);
        JPanel jPanel7 = new JPanel();
        jPanel7.setBorder(BorderFactory.createTitledBorder(C1818g.b("Reference Tables")));
        jPanel7.setLayout(new GridLayout(0, 2));
        JButton jButton3 = new JButton(C1818g.b("Lambda Delay"));
        jButton3.addActionListener(new C1262z(this));
        jPanel7.add(jButton3);
        JButton jButton4 = new JButton(C1818g.b("AFR Targets"));
        jButton4.addActionListener(new C1223A(this));
        jPanel7.add(jButton4);
        jPanel5.add("South", jPanel7);
        this.f8515d = new n.n();
        JPanel jPanel8 = new JPanel();
        C1701s c1701sH = this.f8519h.h();
        jPanel8.setLayout(new GridLayout(2, 1));
        C1704v c1704v = new C1704v(c1701sH);
        cA cAVar = new cA(c1704v);
        c1230h.a(cAVar);
        c1704v.c(2);
        cAVar.a(0.0d, getBackground());
        cAVar.a(8.0d, Color.yellow);
        cAVar.a(50.0d, Color.GREEN);
        cAVar.a(150.0d, Color.GREEN.darker());
        cAVar.a(c1701sH.w());
        cAVar.b(c1701sH.v());
        cAVar.c(C1818g.b("Data Weighting"));
        JPanel jPanel9 = new JPanel();
        jPanel9.setLayout(new BorderLayout());
        jPanel9.setBorder(BorderFactory.createTitledBorder(C1818g.b("Cell Weighting")));
        jPanel9.add(BorderLayout.CENTER, cAVar);
        jPanel8.add(jPanel9);
        C1704v c1704v2 = new C1704v(c1701sH);
        cA cAVar2 = new cA(c1704v2);
        c1230h.a(cAVar2);
        c1704v2.c(3);
        cAVar2.a(-18.0d, Color.red.darker());
        cAVar2.a(-7.0d, Color.red);
        cAVar2.a(0.0d, getBackground());
        cAVar2.a(7.0d, Color.blue);
        cAVar2.a(18.0d, Color.blue.darker());
        cAVar2.a(c1701sH.w());
        cAVar2.b(c1701sH.v());
        cAVar2.c(C1818g.b("Cell Change"));
        JPanel jPanel10 = new JPanel();
        jPanel10.setLayout(new BorderLayout());
        jPanel10.setBorder(BorderFactory.createTitledBorder(C1818g.b("Cell Change")));
        jPanel10.add(BorderLayout.CENTER, cAVar2);
        jPanel8.add(jPanel10);
        jPanel8.setPreferredSize(eJ.a(265, 265));
        this.f8515d.addTab(C1818g.b("Status"), jPanel8);
        JPanel jPanel11 = new JPanel();
        jPanel11.setLayout(new BorderLayout());
        jPanel11.add("North", jPanel5);
        this.f8515d.addTab(C1818g.b("Advanced Settings"), jPanel11);
        add("East", this.f8515d);
        add(BorderLayout.CENTER, jPanel);
        add("South", this.f8518g);
        a(false);
    }

    public boolean a() {
        return this.f8519h.b();
    }

    @Override // br.al
    public void a(JComponent jComponent) {
        this.f8525n.add(jComponent);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void g() {
        try {
            aP aPVar = new aP(this.f8512a, bO.a().a(bO.a().a(this.f8512a, e()), this.f8513b.b()), this.f8513b.b());
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
        Iterator itJ = this.f8513b.j();
        while (itJ.hasNext()) {
            String str = (String) itJ.next();
            boolean zEquals = str.equals(this.f8513b.s());
            if (str.equals("afrTSCustom")) {
                c1580br.add((JMenuItem) b(C1798a.f13268b + " " + C1818g.b("Custom"), str, zEquals));
            } else {
                C0072be c0072be = (C0072be) this.f8512a.e().c(str);
                if (c0072be != null) {
                    c1580br.add((JMenuItem) b(bH.W.b(C1818g.b(c0072be.M()), PdfOps.DOUBLE_QUOTE__TOKEN, ""), str, zEquals));
                } else {
                    bV.d(str + " is defined as an Target Lambda Table, but not found in the current config.", jComponent);
                }
            }
        }
        Iterator itK = this.f8513b.k();
        while (itK.hasNext()) {
            String str2 = (String) itK.next();
            boolean zEquals2 = str2.equals(this.f8513b.s());
            aH aHVarG = this.f8512a.g(str2);
            String strJ = bL.j(this.f8512a, str2);
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
        String str3;
        C1224B c1224b = null;
        boolean z3 = this.f8513b.t() != null && this.f8513b.t().equals(str2);
        if (z2) {
            str3 = C1818g.b("Channel") + " " + str;
        } else {
            str3 = C1818g.b("Switch to Channel") + " - " + str;
            c1224b = new C1224B(this);
        }
        if (z3) {
            str3 = str3 + " (" + C1818g.b(Action.DEFAULT) + ")";
        }
        JCheckBoxMenuItem jCheckBoxMenuItem = new JCheckBoxMenuItem(str3, z2);
        jCheckBoxMenuItem.addActionListener(c1224b);
        jCheckBoxMenuItem.setActionCommand(str2);
        return jCheckBoxMenuItem;
    }

    private JCheckBoxMenuItem b(String str, String str2, boolean z2) {
        String str3;
        ActionListener c1257u;
        boolean z3 = this.f8513b.n() != null && (this.f8513b.t() == null || this.f8513b.t().isEmpty()) && this.f8513b.n().equals(str2);
        if (z2) {
            str3 = C1818g.b("Edit / View") + " - " + str;
            c1257u = new C1225C(this);
        } else {
            str3 = C1818g.b("Switch to Table") + " - " + str;
            c1257u = new C1257u(this);
        }
        if (z3) {
            str3 = str3 + " (" + C1818g.b(Action.DEFAULT) + ")";
        }
        JCheckBoxMenuItem jCheckBoxMenuItem = new JCheckBoxMenuItem(str3, z2);
        jCheckBoxMenuItem.addActionListener(c1257u);
        jCheckBoxMenuItem.setActionCommand(str2);
        return jCheckBoxMenuItem;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void h() {
        if (!this.f8513b.c().equals("afrTSCustom")) {
            C0338f.a().a(this.f8512a.c() + "." + this.f8513b.c(), "0", bV.a(this));
            return;
        }
        try {
            aP aPVar = new aP(this.f8512a, bO.a().a(this.f8512a, this.f8513b.c(), "", this.f8513b.b()), this.f8513b.b());
            a((InterfaceC1565bc) aPVar);
            bV.a(aPVar, this, C1818g.b("Target AFR Table"), aPVar);
        } catch (V.g e2) {
            bH.C.a("Can not show AFR Table.", e2, this);
        }
    }

    private void a(dk dkVar) {
        C0072be c0072beClone = ((C0072be) this.f8512a.e().c(e())).clone();
        c0072beClone.h("veAnalyze_");
        try {
            bO.a().a(this.f8512a, c0072beClone.aJ(), c0072beClone.l(), c0072beClone.aJ()).c(1);
        } catch (V.g e2) {
            bH.C.a("Unable to get Table Model for " + c0072beClone.aJ() + " with prefix:" + c0072beClone.l());
            e2.printStackTrace();
        }
        this.f8514c.a(this.f8512a, (C0088bu) c0072beClone);
    }

    public void a(String str) {
        this.f8526o = str;
        this.f8524m.a("applyPeriod", this.f8526o);
        if (this.f8523l != null) {
            j();
        } else if (this.f8526o.equals("Manually")) {
            i();
        }
    }

    public void b() {
        try {
            C1562b[][] c1562bArrD = bO.a().a(this.f8512a, this.f8513b.b(), "veAnalyze_", this.f8513b.b()).D();
            C1701s c1701sA = bO.a().a(this.f8512a, this.f8513b.b(), "", this.f8513b.b());
            for (int i2 = 0; i2 < c1701sA.getRowCount(); i2++) {
                for (int i3 = 0; i3 < c1701sA.getColumnCount(); i3++) {
                    c1701sA.setValueAt(c1562bArrD[(c1562bArrD.length - i2) - 1][i3].i(), i2, i3);
                }
            }
        } catch (V.g e2) {
            bH.C.a("Unable to get Table Model for " + e() + " with prefix:veAnalyze_");
            e2.printStackTrace();
        }
    }

    public boolean c() {
        try {
            C1701s c1701sB = bO.a().b(this.f8512a, this.f8513b.b(), "");
            C1701s c1701sB2 = bO.a().b(this.f8512a, this.f8513b.b(), "veAnalyze_");
            if (c1701sB != null && c1701sB2 != null) {
                C1562b[][] c1562bArrD = c1701sB2.D();
                if (c1562bArrD == null) {
                    return false;
                }
                for (int i2 = 0; i2 < c1701sB.getRowCount(); i2++) {
                    for (int i3 = 0; i3 < c1701sB.getColumnCount(); i3++) {
                        if (Math.abs(c1701sB.getValueAt(i2, i3).doubleValue() - c1562bArrD[(c1562bArrD.length - i2) - 1][i3].i().doubleValue()) > 1.0E-6d) {
                            return true;
                        }
                    }
                }
            }
            return false;
        } catch (Exception e2) {
            bH.C.a("Unable to get Table Model for " + e() + " with prefix:veAnalyze_");
            e2.printStackTrace();
            return false;
        }
    }

    public void d() {
        b();
        this.f8512a.I();
    }

    private void i() {
        if (this.f8523l != null) {
            this.f8523l.a();
            this.f8523l = null;
            b();
        }
    }

    private void j() {
        i();
        if (this.f8526o.equals("Manually")) {
            return;
        }
        this.f8523l = new C1228F(this, Integer.parseInt(this.f8526o));
        this.f8523l.start();
    }

    @Override // br.al
    protected void a(boolean z2) {
        boolean zA = true;
        try {
            zA = C1007o.a(bL.a(this.f8512a, e()), this.f8512a);
        } catch (V.g e2) {
            Logger.getLogger(C1255s.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
        }
        if (z2 && zA) {
            try {
                this.f8516e.setText(C1818g.b("Correcting VE"));
                this.f8517f.setText(C1818g.b("Stop Auto Tune"));
                this.f8519h.c();
                j();
            } catch (V.a e3) {
                bV.d(e3.getMessage(), this);
            }
            aS.l.a().a(e());
        } else {
            this.f8516e.setText(C1818g.b("Idle"));
            this.f8517f.setText(C1818g.b("Start Auto Tune"));
            this.f8519h.e();
            i();
            k();
            aS.l.a().b(e());
        }
        this.f8516e.a(z2);
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
        if (c() && bV.a(C1818g.b("VE Analysis has recommended VE changes that have not been sent to the controller.\n Would you like to save the recommended table to the controller now?"), (Component) this, true)) {
            d();
        }
        try {
            super.l();
        } catch (Exception e2) {
            bH.C.a("Exception on VE Analyze Close: " + e2.getMessage());
        }
        try {
            this.f8519h.b(this.f8518g);
            this.f8519h.close();
        } catch (Exception e3) {
            bH.C.a("Exception on veAnalyzeLive close: " + e3.getMessage());
        }
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public Dimension getMinimumSize() {
        return new Dimension(eJ.a(500), eJ.a(410));
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public Dimension getPreferredSize() {
        return new Dimension(eJ.a(800), eJ.a(450));
    }

    public String e() {
        return this.f8527p;
    }
}
