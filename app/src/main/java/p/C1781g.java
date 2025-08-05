package p;

import G.C0126i;
import G.C0134q;
import G.T;
import G.aI;
import ax.U;
import bH.W;
import bH.aa;
import bt.C1324bf;
import com.efiAnalytics.ui.C1685fp;
import com.efiAnalytics.ui.InterfaceC1565bc;
import com.efiAnalytics.ui.bV;
import com.efiAnalytics.ui.cF;
import com.efiAnalytics.ui.eJ;
import d.InterfaceC1711c;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import jdk.internal.dynalink.CallSiteDescriptor;
import s.C1818g;
import sun.security.pkcs11.wrapper.Constants;
import sun.security.tools.policytool.ToolWindow;

/* renamed from: p.g, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:p/g.class */
public class C1781g extends C1324bf implements InterfaceC1565bc {

    /* renamed from: a, reason: collision with root package name */
    public static String f13198a = "TimedResetSeconds:";

    /* renamed from: b, reason: collision with root package name */
    G.R f13199b;

    /* renamed from: c, reason: collision with root package name */
    S.n f13200c;

    /* renamed from: j, reason: collision with root package name */
    C1787m f13207j;

    /* renamed from: k, reason: collision with root package name */
    C1787m f13208k;

    /* renamed from: m, reason: collision with root package name */
    aa f13210m;

    /* renamed from: p, reason: collision with root package name */
    private String f13194p = "Define the name of this Event Trigger. It must be unique to all Event based triggers. Should not contain spaces or special characters.";

    /* renamed from: q, reason: collision with root package name */
    private String f13195q = "The target Action that will be triggered based on your Trigger conditions.";

    /* renamed from: r, reason: collision with root package name */
    private String f13196r = "When this condition becomes true, the Target Action will be triggered and run. The target Action is only executed 1 time until the reset condition is met.";

    /* renamed from: s, reason: collision with root package name */
    private String f13197s = "After trigger condition becomes true and fires the target action, the target action will not be fired again until this Reset condition is met. Once the reset condition is met, it will fire again upon the trigger condition being met";

    /* renamed from: d, reason: collision with root package name */
    List f13201d = new ArrayList();

    /* renamed from: e, reason: collision with root package name */
    JCheckBox f13202e = new JCheckBox(C1818g.b("Enabled"));

    /* renamed from: f, reason: collision with root package name */
    JComboBox f13203f = new JComboBox();

    /* renamed from: g, reason: collision with root package name */
    JTextField f13204g = new JTextField("", 12);

    /* renamed from: h, reason: collision with root package name */
    JPanel f13205h = new JPanel();

    /* renamed from: i, reason: collision with root package name */
    JButton f13206i = null;

    /* renamed from: l, reason: collision with root package name */
    JDialog f13209l = null;

    public C1781g(aa aaVar) {
        this.f13199b = null;
        this.f13210m = null;
        this.f13210m = aaVar;
        this.f13199b = T.a().c();
        q();
    }

    private void q() {
        setLayout(new BorderLayout());
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new BorderLayout());
        JPanel jPanel2 = new JPanel();
        jPanel2.setLayout(new BoxLayout(jPanel2, 0));
        jPanel.add("North", jPanel2);
        jPanel2.add(this.f13202e);
        jPanel2.add(new JLabel(Constants.INDENT));
        this.f13204g.setEditable(false);
        this.f13202e.setEnabled(false);
        this.f13203f.setEnabled(false);
        jPanel2.add(a(C1818g.b("Trigger Name"), this.f13204g, this.f13194p));
        jPanel2.add(a(C1818g.b("Target Action"), this.f13203f, this.f13195q));
        j();
        this.f13202e.addActionListener(new C1782h(this));
        this.f13205h.setLayout(new BoxLayout(this.f13205h, 1));
        JPanel jPanel3 = new JPanel();
        this.f13205h.add(jPanel3);
        jPanel3.setLayout(new BorderLayout());
        this.f13207j = new C1787m(this, this.f13199b, false, b(this.f13196r));
        this.f13207j.setBorder(BorderFactory.createTitledBorder(C1818g.b("Trigger Action When")));
        jPanel3.add(BorderLayout.CENTER, this.f13207j);
        cF cFVar = new cF(this.f13196r, this.f13210m);
        JPanel jPanel4 = new JPanel();
        jPanel4.setLayout(new BorderLayout());
        jPanel4.add("North", cFVar);
        jPanel3.add("East", jPanel4);
        JPanel jPanel5 = new JPanel();
        this.f13205h.add(jPanel5);
        jPanel5.setLayout(new BorderLayout());
        this.f13208k = new C1787m(this, this.f13199b, true, b(this.f13197s));
        this.f13208k.setBorder(BorderFactory.createTitledBorder(C1818g.b("Reset Condition")));
        jPanel5.add(BorderLayout.CENTER, this.f13208k);
        cF cFVar2 = new cF(this.f13197s, this.f13210m);
        JPanel jPanel6 = new JPanel();
        jPanel6.setLayout(new BorderLayout());
        jPanel6.add("North", cFVar2);
        jPanel5.add("East", jPanel6);
        jPanel.add(BorderLayout.CENTER, this.f13205h);
        add(BorderLayout.CENTER, jPanel);
        this.f13206i = new JButton(b(ToolWindow.SAVE_POLICY_FILE));
        this.f13206i.addActionListener(new C1783i(this));
        JPanel jPanel7 = new JPanel();
        jPanel7.setLayout(new FlowLayout(2));
        jPanel7.add(this.f13206i);
        add("South", jPanel7);
        r();
    }

    private JPanel a(String str, JComponent jComponent, String str2) {
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new BorderLayout());
        JLabel jLabel = new JLabel(str + CallSiteDescriptor.TOKEN_DELIMITER);
        jLabel.setHorizontalAlignment(4);
        jLabel.setPreferredSize(eJ.a(100, 20));
        jPanel.add("West", jLabel);
        JPanel jPanel2 = new JPanel();
        jPanel2.setLayout(new FlowLayout(0));
        jPanel2.add(jComponent);
        jPanel.add(BorderLayout.CENTER, jPanel2);
        if (str2 != null && !str2.isEmpty()) {
            jPanel.add("East", new cF(str2, this.f13210m));
        }
        return jPanel;
    }

    private String b(String str) {
        return this.f13210m != null ? this.f13210m.a(str) : str;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void r() {
        if (this.f13202e.isSelected()) {
            C1685fp.a((Component) this, true);
            this.f13207j.b();
            this.f13208k.b();
        } else {
            C1685fp.a((Component) this, false);
        }
        this.f13202e.setEnabled(true);
        this.f13206i.setEnabled(this.f13200c != null);
    }

    @Override // bt.C1324bf, com.efiAnalytics.ui.InterfaceC1565bc
    public void close() {
        S.e.a().a(this.f13199b.c(), this.f13200c.a());
        if (this.f13202e.isSelected()) {
            try {
                S.e.a().a(this.f13199b.c(), this.f13200c);
            } catch (C0134q e2) {
                bV.d("No Configuration Found: " + this.f13199b.c(), this.f13202e);
            }
        }
        super.close();
    }

    public void c() throws NumberFormatException, d.e {
        a(e());
    }

    public void a(S.h hVar) throws NumberFormatException, d.e {
        String str;
        str = "";
        str = (hVar.a() == null || hVar.a().trim().isEmpty()) ? str + C1818g.b("Name is required.") + "\n" : "";
        if (W.f(hVar.a()) || hVar.a().contains(" ")) {
            str = str + C1818g.b("Name cannot contain special characters!") + "\n";
        }
        try {
            C0126i.a(hVar.d(), (aI) this.f13199b);
        } catch (U e2) {
            str = str + C1818g.b("Invalid Trigger Expression") + CallSiteDescriptor.TOKEN_DELIMITER + this.f13207j.a() + "\n";
            if (e2.getMessage() != null && !e2.getMessage().isEmpty()) {
                str = str + "     Err: " + C1818g.b(e2.getLocalizedMessage()) + "\n";
            }
        }
        if (!hVar.f()) {
            try {
                C0126i.a(hVar.e(), (aI) this.f13199b);
            } catch (U e3) {
                str = str + C1818g.b("Invalid Reset Expression") + CallSiteDescriptor.TOKEN_DELIMITER + this.f13208k.a() + "\n";
                if (e3.getMessage() != null && !e3.getMessage().isEmpty()) {
                    str = str + "     Err: " + C1818g.b(e3.getLocalizedMessage()) + "\n";
                }
            }
        }
        if (!str.isEmpty()) {
            throw new d.e(str);
        }
    }

    public void a(InterfaceC1780f interfaceC1780f) {
        this.f13201d.add(interfaceC1780f);
    }

    public boolean d() {
        Iterator it = this.f13201d.iterator();
        while (it.hasNext()) {
            if (!((InterfaceC1780f) it.next()).a()) {
                return false;
            }
        }
        return true;
    }

    public S.h e() {
        S.h hVar = new S.h();
        hVar.g(this.f13204g.getText());
        hVar.a(this.f13202e.isSelected());
        hVar.e(this.f13207j.a());
        if (this.f13208k.a().contains(f13198a)) {
            hVar.a(Integer.parseInt(W.b(this.f13208k.a(), f13198a, "")) * 1000);
        } else {
            hVar.f(this.f13208k.a());
        }
        return hVar;
    }

    public S.n f() throws NumberFormatException, d.e {
        c();
        this.f13200c.g(this.f13204g.getText());
        this.f13200c.a(this.f13202e.isSelected());
        this.f13200c.e(this.f13207j.a());
        this.f13200c.f(this.f13208k.a());
        if (this.f13208k.a().contains(f13198a)) {
            this.f13200c.a(Integer.parseInt(W.b(this.f13208k.a(), f13198a, "")) * 1000);
        } else {
            this.f13200c.f(this.f13208k.a());
        }
        this.f13200c.j(((C1786l) this.f13203f.getSelectedItem()).a().a());
        return this.f13200c;
    }

    void a(S.n nVar) {
        nVar.g("TriggeredEvent");
        this.f13200c = nVar;
        this.f13204g.setText(nVar.a());
        this.f13204g.setEditable(true);
        this.f13202e.setEnabled(true);
        this.f13203f.setEnabled(true);
        this.f13207j.a(nVar.d());
        this.f13208k.a(nVar.e());
        this.f13202e.setSelected(nVar.c());
        r();
        this.f13204g.selectAll();
        this.f13204g.requestFocus();
    }

    boolean g() {
        return this.f13204g.isEditable();
    }

    void h() {
        this.f13200c = null;
        this.f13204g.setEditable(false);
        this.f13204g.setText("");
        this.f13202e.setEnabled(false);
        this.f13203f.setEnabled(false);
        this.f13207j.a("");
        this.f13208k.a("");
        r();
    }

    boolean i() {
        return (this.f13200c == null || (this.f13200c.a().equals(this.f13204g.getText()) && this.f13200c.d().equals(this.f13207j.a()) && this.f13200c.e().equals(this.f13208k.a()) && this.f13200c.i().equals(((C1786l) this.f13203f.getSelectedItem()).a().a()) && this.f13200c.c() == this.f13202e.isSelected())) ? false : true;
    }

    void b(S.n nVar) {
        this.f13200c = nVar;
        if (nVar == null) {
            h();
            return;
        }
        this.f13204g.setText(nVar.a());
        this.f13204g.setEditable(false);
        this.f13202e.setEnabled(true);
        this.f13203f.setEnabled(true);
        InterfaceC1711c interfaceC1711cB = d.g.a().b(nVar.i());
        if (interfaceC1711cB != null) {
            int i2 = 0;
            while (true) {
                if (i2 < this.f13203f.getItemCount()) {
                    C1786l c1786l = (C1786l) this.f13203f.getItemAt(i2);
                    if (c1786l != null && c1786l.a() != null && c1786l.a().a().equals(interfaceC1711cB.a())) {
                        this.f13203f.setSelectedIndex(i2);
                        break;
                    }
                    i2++;
                } else {
                    break;
                }
            }
        }
        this.f13207j.a(nVar.d());
        if (nVar.f()) {
            this.f13208k.a(nVar.g() / 1000);
        } else {
            this.f13208k.a(nVar.e());
        }
        this.f13202e.setSelected(nVar.c());
        r();
    }

    public void j() {
        C1786l c1786l = (C1786l) this.f13203f.getSelectedItem();
        this.f13203f.removeAllItems();
        List listA = d.g.a().a(new C1784j(this));
        Collections.sort(listA, new C1785k(this));
        Iterator it = listA.iterator();
        while (it.hasNext()) {
            this.f13203f.addItem(new C1786l(this, (InterfaceC1711c) it.next()));
        }
        if (c1786l != null) {
            for (int i2 = 0; i2 < this.f13203f.getItemCount(); i2++) {
                if (((C1786l) this.f13203f.getItemAt(i2)).a().a().equals(c1786l.a().a())) {
                    this.f13203f.setSelectedIndex(i2);
                    return;
                }
            }
        }
    }

    void a(boolean z2) {
        this.f13202e.setSelected(z2);
        r();
    }
}
