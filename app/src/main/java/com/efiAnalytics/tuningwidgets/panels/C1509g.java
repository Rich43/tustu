package com.efiAnalytics.tuningwidgets.panels;

import G.C0126i;
import G.C0134q;
import bt.C1324bf;
import com.efiAnalytics.ui.C1685fp;
import com.efiAnalytics.ui.InterfaceC1565bc;
import com.efiAnalytics.ui.bV;
import com.sun.org.apache.xerces.internal.xinclude.XIncludeHandler;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Window;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JPanel;
import jdk.internal.dynalink.CallSiteDescriptor;
import r.C1806i;
import s.C1818g;

/* renamed from: com.efiAnalytics.tuningwidgets.panels.g, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:com/efiAnalytics/tuningwidgets/panels/g.class */
public class C1509g extends C1324bf implements InterfaceC1565bc {

    /* renamed from: a, reason: collision with root package name */
    G.R f10444a;

    /* renamed from: b, reason: collision with root package name */
    S.j f10445b;

    /* renamed from: c, reason: collision with root package name */
    S.j f10446c;

    /* renamed from: l, reason: collision with root package name */
    C1513k f10455l;

    /* renamed from: m, reason: collision with root package name */
    C1513k f10456m;

    /* renamed from: d, reason: collision with root package name */
    static String f10447d = U.b.f1890a;

    /* renamed from: e, reason: collision with root package name */
    static String f10448e = U.b.f1891b;

    /* renamed from: f, reason: collision with root package name */
    static String f10449f = U.b.f1892c;

    /* renamed from: g, reason: collision with root package name */
    static String f10450g = U.b.f1893d;

    /* renamed from: h, reason: collision with root package name */
    static String f10451h = U.b.f1894e;

    /* renamed from: i, reason: collision with root package name */
    static String f10452i = U.b.f1895f;

    /* renamed from: q, reason: collision with root package name */
    static String f10458q = "0";

    /* renamed from: j, reason: collision with root package name */
    JCheckBox f10453j = new JCheckBox(C1818g.b("Enabled"));

    /* renamed from: k, reason: collision with root package name */
    JPanel f10454k = new JPanel();

    /* renamed from: p, reason: collision with root package name */
    JDialog f10457p = null;

    public C1509g(G.R r2) {
        this.f10444a = null;
        this.f10444a = r2;
        this.f10445b = S.b.a().a(r2.c(), f10447d);
        this.f10446c = S.b.a().a(r2.c(), f10448e);
        d();
        String str = r2.g("TPS_Pct") != null ? "TPS_Pct > 80" : r2.g("tps") != null ? "tps > 80" : "";
        String strB = bH.W.b(bH.W.b(this.f10445b.d(), f10449f, ""), "&& AppEvent.dataLogTime == 0", "");
        this.f10455l.a(strB.equals("") ? str : strB);
        this.f10456m.a(U.b.a(this.f10446c, C1806i.a().a("sa0-0o0os-0o-0DS") ? 30 : -1));
    }

    private void d() {
        setLayout(new BorderLayout());
        JPanel jPanel = new JPanel();
        jPanel.setBorder(BorderFactory.createTitledBorder(C1818g.b("Automatic Logging Trigger")));
        jPanel.setLayout(new BorderLayout());
        jPanel.add("North", this.f10453j);
        boolean zA = C1806i.a().a("sa0-0o0os-0o-0DS");
        this.f10453j.setSelected(zA || this.f10445b.c());
        this.f10453j.setEnabled(!zA);
        this.f10453j.addActionListener(new C1510h(this));
        this.f10454k.setLayout(new BoxLayout(this.f10454k, 1));
        JPanel jPanel2 = new JPanel();
        this.f10454k.add(jPanel2);
        jPanel2.setLayout(new BorderLayout());
        this.f10455l = new C1513k(this, this.f10444a, false, true);
        this.f10455l.setBorder(BorderFactory.createTitledBorder(C1818g.b("Start Logging When")));
        jPanel2.add(BorderLayout.CENTER, this.f10455l);
        JPanel jPanel3 = new JPanel();
        this.f10454k.add(jPanel3);
        jPanel3.setLayout(new BorderLayout());
        this.f10456m = new C1513k(this, this.f10444a, true, !C1806i.a().a("sa0-0o0os-0o-0DS"));
        this.f10456m.setBorder(BorderFactory.createTitledBorder(C1818g.b("Stop Logging When")));
        jPanel3.add(BorderLayout.CENTER, this.f10456m);
        jPanel.add(BorderLayout.CENTER, this.f10454k);
        add(BorderLayout.CENTER, jPanel);
        e();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void e() {
        C1685fp.a((Component) this.f10454k, this.f10453j.isSelected());
        if (this.f10453j.isSelected()) {
            this.f10455l.b();
            this.f10456m.b();
        }
    }

    @Override // bt.C1324bf, com.efiAnalytics.ui.InterfaceC1565bc
    public void close() {
        S.e.a().a(this.f10444a.c(), this.f10445b.a());
        S.e.a().a(this.f10444a.c(), this.f10446c.a());
        if (this.f10453j.isSelected()) {
            try {
                S.e.a().a(this.f10444a.c(), this.f10445b);
                S.e.a().a(this.f10444a.c(), this.f10446c);
            } catch (C0134q e2) {
                bV.d("No Configuration Found: " + this.f10444a.c(), this.f10453j);
            }
        }
        super.close();
    }

    public boolean c() throws NumberFormatException {
        try {
            C0126i.a(this.f10455l.a(), (G.aI) this.f10444a);
            try {
                C0126i.a(this.f10456m.a(), (G.aI) this.f10444a);
                return true;
            } catch (ax.U e2) {
                bV.d(C1818g.b("Invalid Stop Logging Expression") + CallSiteDescriptor.TOKEN_DELIMITER + this.f10456m.a(), this);
                return false;
            }
        } catch (ax.U e3) {
            bV.d(C1818g.b("Invalid Start Logging Expression") + CallSiteDescriptor.TOKEN_DELIMITER + this.f10455l.a(), this);
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void f() {
        this.f10445b.e(this.f10455l.a() + f10449f);
        this.f10446c.e(this.f10456m.a() + f10450g);
        this.f10445b.f(f10451h);
        this.f10446c.f(f10452i);
    }

    public void a(Component component) {
        this.f10457p = new JDialog(bV.a(component), C1818g.b("Triggered Logging"));
        this.f10457p.add(BorderLayout.CENTER, this);
        JButton jButton = new JButton(C1818g.b("Cancel"));
        jButton.addActionListener(new C1511i(this));
        JButton jButton2 = new JButton(C1818g.b(XIncludeHandler.HTTP_ACCEPT));
        jButton2.addActionListener(new C1512j(this));
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new FlowLayout(2));
        if (bV.d()) {
            jPanel.add(jButton2);
            jPanel.add(jButton);
        } else {
            jPanel.add(jButton);
            jPanel.add(jButton2);
        }
        this.f10457p.add("South", jPanel);
        this.f10457p.pack();
        bV.a((Window) bV.a(component), (Component) this.f10457p);
        this.f10457p.setVisible(true);
        validate();
        this.f10457p.pack();
        this.f10457p.setResizable(false);
    }
}
