package bs;

import G.C0072be;
import G.C0079bl;
import G.C0088bu;
import G.R;
import G.dn;
import aP.C0338f;
import bH.W;
import bt.C1324bf;
import bt.C1345d;
import bt.bO;
import com.efiAnalytics.tuningwidgets.panels.aP;
import com.efiAnalytics.ui.C1580br;
import com.efiAnalytics.ui.InterfaceC1565bc;
import com.efiAnalytics.ui.bV;
import com.efiAnalytics.ui.dD;
import com.efiAnalytics.ui.dQ;
import com.efiAnalytics.ui.fE;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
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

/* loaded from: TunerStudioMS.jar:bs/k.class */
public class k extends C1345d implements InterfaceC1565bc {

    /* renamed from: a, reason: collision with root package name */
    dn f8604a;

    /* renamed from: b, reason: collision with root package name */
    C1266D f8605b;

    /* renamed from: c, reason: collision with root package name */
    R f8606c;

    /* renamed from: j, reason: collision with root package name */
    C0079bl f8613j;

    /* renamed from: n, reason: collision with root package name */
    dQ f8617n;

    /* renamed from: d, reason: collision with root package name */
    bE.m f8607d = null;

    /* renamed from: e, reason: collision with root package name */
    C1263A f8608e = new C1263A(this);

    /* renamed from: f, reason: collision with root package name */
    dD f8609f = null;

    /* renamed from: g, reason: collision with root package name */
    bE.m f8610g = null;

    /* renamed from: h, reason: collision with root package name */
    C1264B f8611h = new C1264B(this);

    /* renamed from: i, reason: collision with root package name */
    dD f8612i = null;

    /* renamed from: k, reason: collision with root package name */
    w f8614k = null;

    /* renamed from: l, reason: collision with root package name */
    JToggleButton f8615l = new JToggleButton(C1818g.b("Start Auto Tune"));

    /* renamed from: m, reason: collision with root package name */
    JCheckBox f8616m = null;

    /* renamed from: o, reason: collision with root package name */
    JPanel f8618o = null;

    /* renamed from: p, reason: collision with root package name */
    String f8619p = "15";

    /* renamed from: q, reason: collision with root package name */
    y f8620q = null;

    /* renamed from: r, reason: collision with root package name */
    z f8621r = null;

    public k(R r2, dn dnVar) {
        this.f8605b = null;
        this.f8617n = null;
        this.f8604a = dnVar;
        this.f8606c = r2;
        this.f8613j = (C0079bl) r2.e().c(dnVar.c());
        this.f8617n = new dQ(aE.a.A(), "WueAnalyzePanel_" + dnVar.c());
        String strB = this.f8617n.b("targetLambdaTableName", dnVar.f());
        if (strB != null && !strB.equals("")) {
            dnVar.f(strB);
        }
        this.f8605b = new C1266D(r2, dnVar);
        r2.C().a(this.f8605b);
        this.f8605b.b(Boolean.parseBoolean(this.f8617n.b("extendBeyondData", "true")));
        setLayout(new BorderLayout());
        add("North", a());
        add(BorderLayout.CENTER, b());
        add("East", f());
        add("South", c());
    }

    private JPanel a() {
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new BorderLayout());
        jPanel.setBorder(BorderFactory.createTitledBorder(C1818g.b(this.f8613j.M()) + " - " + C1818g.b("Control Panel")));
        JPanel jPanel2 = new JPanel();
        jPanel2.setLayout(new BoxLayout(jPanel2, 0));
        this.f8614k = new w(this, C1818g.b("Idle"), 0);
        jPanel2.add(this.f8614k);
        jPanel2.add(new JLabel(Constants.INDENT));
        jPanel2.add(this.f8615l);
        this.f8615l.setMnemonic('A');
        this.f8615l.addActionListener(new l(this));
        jPanel.add("East", jPanel2);
        JPanel jPanel3 = new JPanel();
        jPanel3.setLayout(new BoxLayout(jPanel3, 0));
        this.f8619p = this.f8617n.b("applyPeriod", "15");
        this.f8616m = new JCheckBox(C1818g.b("Update Controller") + "   ");
        this.f8616m.setToolTipText("<html>" + C1818g.b("Check to have WUE Analyze Recommendations automatically <br>sent to the controller. If unchecked, WUE Analyze Live will <br>produce the recommended warmup enrichment curve, but not send changes until the Apply button is clicked.</html>"));
        this.f8616m.setSelected(!this.f8619p.equals("Manually"));
        this.f8616m.addActionListener(new o(this));
        if (this.f8604a.l(dn.f1238d)) {
            this.f8616m.setSelected(false);
            this.f8619p = "Manually";
            a(this.f8619p);
        } else {
            jPanel3.add(this.f8616m);
        }
        JButton jButton = new JButton(C1818g.b("Apply"));
        jButton.setMnemonic('A');
        jButton.setToolTipText(C1818g.b("<html>Send the VE Analyze Recommendations<br>to the ECU Now. The engine will be running on the recommended <br>VE Table, but not nessecarily permenantly stored.</html>"));
        jButton.addActionListener(new p(this));
        jPanel3.add(jButton);
        JButton jButton2 = new JButton(C1818g.b("Save on ECU"));
        jButton2.setMnemonic('E');
        jButton2.addActionListener(new q(this));
        jButton2.setToolTipText(C1818g.b("<html>Stores the Recommended VE table values to ECU <br>FLASH for persistent storage.</html>"));
        jPanel3.add(jButton2);
        this.f8618o = new JPanel();
        this.f8618o.setLayout(new FlowLayout());
        jPanel3.add(this.f8618o);
        JCheckBox jCheckBox = new JCheckBox(C1818g.b("Extended Prediction") + "   ");
        jCheckBox.setToolTipText("<html>" + C1818g.b("Predict warmup values beyond the temperature range <br>of data collected during this session.<br>This will produce best guess estimates for very cold temperatures <br>that may be difficult to collect data for.</html>"));
        jCheckBox.setSelected(Boolean.parseBoolean(this.f8617n.b("extendBeyondData", "true")));
        jCheckBox.addActionListener(new r(this));
        jPanel3.add(jCheckBox);
        JButton jButton3 = new JButton(null, new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource("Help32.png"))));
        jButton3.setFocusable(false);
        jButton3.addActionListener(new s(this));
        jButton3.setPreferredSize(new Dimension(28, 24));
        jButton3.setMinimumSize(new Dimension(28, 24));
        jButton3.setMaximumSize(new Dimension(28, 26));
        jPanel3.add(jButton3);
        jPanel.add("West", jPanel3);
        return jPanel;
    }

    private JPanel b() throws V.g {
        if (this.f8606c.e().c(this.f8604a.g()) == null) {
            throw new V.g("WUE Analyzer: Can not locate AFR Compensation Curve " + this.f8604a.g() + " in " + this.f8606c.c());
        }
        if (this.f8613j == null) {
            throw new V.g("WUE Analyzer: Can not locate Warm up Curve " + this.f8604a.c() + " in " + this.f8606c.c());
        }
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new GridLayout(1, 1));
        C0088bu c0088bu = new C0088bu();
        c0088bu.i(2);
        C0088bu c0088bu2 = new C0088bu();
        c0088bu2.t(BorderLayout.CENTER);
        c0088bu2.s(C1818g.b("Warmup Enrichment Curve"));
        this.f8613j.d(true);
        c0088bu2.a(this.f8613j);
        c0088bu.a(c0088bu2);
        jPanel.add(new C1324bf(this.f8606c, c0088bu));
        return jPanel;
    }

    private JPanel c() {
        this.f8621r = new z(this);
        this.f8605b.a(this.f8621r);
        return this.f8621r;
    }

    private JPanel d() {
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new BorderLayout());
        this.f8607d = new bE.m();
        this.f8609f = new dD(this.f8607d);
        this.f8609f.b(3000);
        this.f8609f.a(new t(this));
        this.f8607d.a(this.f8608e);
        this.f8608e.a(this.f8613j.h());
        this.f8608e.b(this.f8605b.d() + 10.0d);
        this.f8608e.c(-40.0d);
        this.f8608e.d(40.0d);
        this.f8608e.e(-25.0d);
        this.f8608e.f(25.0d);
        this.f8605b.a(this.f8608e);
        this.f8607d.a(C1818g.b("Coolant"));
        this.f8607d.b(C1818g.b("Lambda Error") + " %");
        this.f8607d.c(C1818g.b("WUE Error") + " %");
        jPanel.add(BorderLayout.CENTER, this.f8607d);
        JLabel jLabel = new JLabel(C1818g.b("Coolant"), 0);
        jLabel.setBackground(Color.BLACK);
        jLabel.setOpaque(true);
        jLabel.setForeground(Color.WHITE);
        jPanel.add("South", jLabel);
        fE fEVar = new fE(C1818g.b("Lambda Error") + " %");
        fEVar.setBackground(Color.BLACK);
        fEVar.setOpaque(true);
        fEVar.setForeground(Color.WHITE);
        jPanel.add("West", fEVar);
        fE fEVar2 = new fE(C1818g.b("WUE Error") + " %");
        fEVar2.setBackground(Color.BLACK);
        fEVar2.setOpaque(true);
        fEVar2.setForeground(Color.WHITE);
        jPanel.add("East", fEVar2);
        jPanel.setBorder(BorderFactory.createTitledBorder(C1818g.b("WUE Error vs Coolant")));
        return jPanel;
    }

    private JPanel e() {
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new BorderLayout());
        this.f8610g = new bE.m();
        this.f8612i = new dD(this.f8610g);
        this.f8612i.b(3000);
        this.f8612i.a(new u(this));
        this.f8610g.a(this.f8611h);
        this.f8611h.a(this.f8613j.h());
        this.f8611h.b(this.f8605b.d() + 10.0d);
        this.f8611h.c(90.0d);
        this.f8611h.d(200.0d);
        this.f8611h.e(-3.0d);
        this.f8611h.f(3.0d);
        this.f8605b.a(this.f8611h);
        this.f8610g.a(C1818g.b("Coolant"));
        this.f8610g.b(C1818g.b("Recommended WUE") + " %");
        this.f8610g.c(C1818g.b("AFR Error"));
        jPanel.add(BorderLayout.CENTER, this.f8610g);
        JLabel jLabel = new JLabel(C1818g.b("Coolant"), 0);
        jLabel.setBackground(Color.BLACK);
        jLabel.setOpaque(true);
        jLabel.setForeground(Color.WHITE);
        jPanel.add("South", jLabel);
        fE fEVar = new fE(C1818g.b("Recommended WUE") + " %");
        fEVar.setBackground(Color.BLACK);
        fEVar.setOpaque(true);
        fEVar.setForeground(Color.WHITE);
        jPanel.add("West", fEVar);
        fE fEVar2 = new fE(C1818g.b("AFR Error"));
        fEVar2.setBackground(Color.BLACK);
        fEVar2.setOpaque(true);
        fEVar2.setForeground(Color.WHITE);
        jPanel.add("East", fEVar2);
        jPanel.setBorder(BorderFactory.createTitledBorder(C1818g.b("Recommended WUE Data")));
        return jPanel;
    }

    private n.n f() throws V.g {
        n.n nVar = new n.n();
        if (this.f8606c.e().c(this.f8604a.g()) == null) {
            throw new V.g("WUE Analyzer: Can not locate AFR Compensation Curve " + this.f8604a.g() + " in " + this.f8606c.c());
        }
        nVar.addTab(C1818g.b("Error Plot"), d());
        nVar.addTab(C1818g.b("Recommended WUE Plot"), e());
        C0088bu c0088bu = new C0088bu();
        c0088bu.i(2);
        C0088bu c0088bu2 = new C0088bu();
        c0088bu2.s(C1818g.b("Temperature based adjustment to target Lambda"));
        c0088bu2.t(BorderLayout.CENTER);
        c0088bu2.a(this.f8606c.e().c(this.f8604a.g()));
        c0088bu.a(c0088bu2);
        Component c1324bf = new C1324bf(this.f8606c, c0088bu);
        c1324bf.setPreferredSize(new Dimension(400, 280));
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new BorderLayout());
        jPanel.add(BorderLayout.CENTER, c1324bf);
        JPanel jPanel2 = new JPanel();
        jPanel2.setLayout(new BorderLayout());
        jPanel2.add("North", new C1268a(this.f8606c, this.f8604a));
        JPanel jPanel3 = new JPanel();
        jPanel3.setBorder(BorderFactory.createTitledBorder(C1818g.b("Reference Tables")));
        jPanel3.setLayout(new GridLayout(0, 2));
        jPanel3.add(new JPanel());
        JButton jButton = new JButton(C1818g.b("AFR Targets"));
        jButton.addActionListener(new v(this));
        jPanel3.add(jButton);
        jPanel2.add("South", jPanel3);
        jPanel.add("South", jPanel2);
        nVar.addTab(C1818g.b("Advanced Settings"), jPanel);
        return nVar;
    }

    public void a(JComponent jComponent) {
        this.f8618o.add(jComponent);
    }

    public void a(String str) {
        this.f8619p = str;
        this.f8617n.a("applyPeriod", this.f8619p);
        if (this.f8620q != null) {
            k();
        } else if (this.f8619p.equals("Manually")) {
            j();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void b(JComponent jComponent) {
        C1580br c1580br = new C1580br();
        Iterator itJ = this.f8604a.j();
        while (itJ.hasNext()) {
            String str = (String) itJ.next();
            boolean zEquals = str.equals(this.f8604a.f());
            if (str.equals("afrTSCustom")) {
                c1580br.add((JMenuItem) a(C1798a.f13268b + " " + C1818g.b("Custom"), str, zEquals));
            } else {
                C0072be c0072be = (C0072be) this.f8606c.e().c(str);
                if (c0072be != null) {
                    c1580br.add((JMenuItem) a(W.b(C1818g.b(c0072be.M()), PdfOps.DOUBLE_QUOTE__TOKEN, ""), str, zEquals));
                } else {
                    bV.d(str + " is defined as an Target Lambda Table, but not found in the current config.", jComponent);
                }
            }
        }
        add(c1580br);
        c1580br.show(jComponent, 0, jComponent.getHeight());
    }

    private JCheckBoxMenuItem a(String str, String str2, boolean z2) {
        String str3;
        ActionListener nVar;
        boolean z3 = this.f8604a.m() != null && this.f8604a.m().equals(str2);
        if (z2) {
            str3 = C1818g.b("Edit / View") + " - " + str;
            nVar = new m(this);
        } else {
            str3 = C1818g.b("Switch to") + " - " + str;
            nVar = new n(this);
        }
        if (z3) {
            str3 = str3 + " (" + C1818g.b(Action.DEFAULT) + ")";
        }
        JCheckBoxMenuItem jCheckBoxMenuItem = new JCheckBoxMenuItem(str3, z2);
        jCheckBoxMenuItem.addActionListener(nVar);
        jCheckBoxMenuItem.setActionCommand(str2);
        return jCheckBoxMenuItem;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void g() {
        if (!this.f8604a.f().equals("afrTSCustom")) {
            C0338f.a().a(this.f8606c.c() + "." + this.f8604a.f(), "0", bV.a(this));
            return;
        }
        try {
            Iterator itL = this.f8606c.L();
            String str = itL.hasNext() ? (String) itL.next() : "veTable1Tbl";
            aP aPVar = new aP(this.f8606c, bO.a().a(this.f8606c, this.f8604a.f(), "", str), str);
            a((InterfaceC1565bc) aPVar);
            bV.a(aPVar, this, C1818g.b("Target AFR Table"), aPVar);
        } catch (V.g e2) {
            bH.C.a("Can not show AFR Table.", e2, this);
        }
    }

    @Override // com.efiAnalytics.ui.InterfaceC1565bc
    public void close() {
        this.f8606c.C().c(this.f8605b);
        super.l();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a(boolean z2) {
        if (z2) {
            this.f8614k.setText(C1818g.b("Correcting WUE"));
            this.f8615l.setText(C1818g.b("Stop Auto Tune"));
            try {
                this.f8605b.a(z2);
                k();
            } catch (V.a e2) {
                Logger.getLogger(k.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
                bV.d("Could not start WUE Analyze!\n" + e2.getLocalizedMessage(), this);
                a(false);
            }
        } else {
            this.f8614k.setText(C1818g.b("Idle"));
            this.f8615l.setText(C1818g.b("Start Auto Tune"));
            try {
                this.f8605b.a(z2);
            } catch (V.a e3) {
                Logger.getLogger(k.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e3);
                bV.d("Could not stop WUE Analyze!\n" + e3.getLocalizedMessage(), this);
            }
            j();
        }
        this.f8614k.a(z2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void h() {
        try {
            this.f8605b.e();
            if (this.f8604a.l(dn.f1239e)) {
                i();
            }
        } catch (V.g e2) {
            Logger.getLogger(k.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            bV.d("Error Applying Recommended WUE.\n" + e2.getMessage(), this);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void i() {
        this.f8606c.I();
    }

    private void j() {
        if (this.f8620q != null) {
            this.f8620q.a();
            this.f8620q = null;
            h();
        }
    }

    private void k() {
        j();
        if (this.f8619p.equals("Manually")) {
            return;
        }
        this.f8620q = new y(this, Integer.parseInt(this.f8619p));
        this.f8620q.start();
    }
}
