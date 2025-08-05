package p;

import bH.W;
import bH.aa;
import com.efiAnalytics.ui.bV;
import com.efiAnalytics.ui.cF;
import com.efiAnalytics.ui.eJ;
import d.C1710b;
import d.InterfaceC1711c;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import jdk.internal.dynalink.CallSiteDescriptor;
import sun.security.tools.policytool.ToolWindow;

/* loaded from: TunerStudioMS.jar:p/D.class */
public class D extends JPanel {

    /* renamed from: a, reason: collision with root package name */
    aa f13143a;

    /* renamed from: g, reason: collision with root package name */
    JButton f13149g;

    /* renamed from: h, reason: collision with root package name */
    JScrollPane f13150h;

    /* renamed from: b, reason: collision with root package name */
    List f13144b = new ArrayList();

    /* renamed from: c, reason: collision with root package name */
    JTextField f13145c = new bx.q("", 18);

    /* renamed from: d, reason: collision with root package name */
    JTextField f13146d = new JTextField("", 20);

    /* renamed from: e, reason: collision with root package name */
    JTextField f13147e = new JTextField("", 50);

    /* renamed from: f, reason: collision with root package name */
    JComboBox f13148f = new JComboBox();

    /* renamed from: l, reason: collision with root package name */
    private String f13151l = "Define the action command used to fire this action. This name must be unique not only to User Actions, but to all defined Actions. Should not contain spaces or special characters.";

    /* renamed from: m, reason: collision with root package name */
    private String f13152m = "A more descriptive name that will be displayed as the Action name.";

    /* renamed from: n, reason: collision with root package name */
    private String f13153n = "The target Action that your User Action will trigger with the parameters you provide.";

    /* renamed from: i, reason: collision with root package name */
    JPanel f13154i = new JPanel();

    /* renamed from: j, reason: collision with root package name */
    List f13155j = new ArrayList();

    /* renamed from: k, reason: collision with root package name */
    d.m f13156k = null;

    public D(aa aaVar) {
        this.f13149g = null;
        this.f13143a = aaVar;
        setLayout(new BorderLayout());
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new GridLayout(0, 1, eJ.a(1), eJ.a(1)));
        jPanel.add(a(a("Unique Action Name"), this.f13145c, this.f13151l));
        jPanel.add(a(a("Display Name"), this.f13146d, this.f13152m));
        jPanel.add(a(a("Target Action"), this.f13148f, this.f13153n));
        List listA = d.g.a().a(new E(this));
        Collections.sort(listA, new F(this));
        Iterator it = listA.iterator();
        while (it.hasNext()) {
            this.f13148f.addItem(new I(this, (InterfaceC1711c) it.next()));
        }
        this.f13148f.addActionListener(new G(this));
        jPanel.add(a(a("Description"), this.f13147e, "Description for this User Action."));
        add("North", jPanel);
        this.f13154i.setLayout(new BoxLayout(this.f13154i, 1));
        this.f13150h = new JScrollPane(this.f13154i);
        this.f13150h.setPreferredSize(eJ.a(320, 80));
        this.f13150h.setHorizontalScrollBarPolicy(31);
        this.f13150h.setVerticalScrollBarPolicy(22);
        JPanel jPanel2 = new JPanel();
        jPanel2.setLayout(new BorderLayout());
        jPanel2.add("North", new JLabel(a("User Parameters") + CallSiteDescriptor.TOKEN_DELIMITER));
        jPanel2.add(BorderLayout.CENTER, this.f13150h);
        add(BorderLayout.CENTER, jPanel2);
        this.f13149g = new JButton(a(ToolWindow.SAVE_POLICY_FILE));
        this.f13149g.addActionListener(new H(this));
        JPanel jPanel3 = new JPanel();
        jPanel3.setLayout(new FlowLayout(2));
        jPanel3.add(this.f13149g);
        add("South", jPanel3);
        d();
    }

    public void a(InterfaceC1780f interfaceC1780f) {
        this.f13144b.add(interfaceC1780f);
    }

    public boolean a() {
        Iterator it = this.f13144b.iterator();
        while (it.hasNext()) {
            if (!((InterfaceC1780f) it.next()).a()) {
                return false;
            }
        }
        return true;
    }

    private String a(String str) {
        return this.f13143a != null ? this.f13143a.a(str) : str;
    }

    public d.m b() {
        d.m mVar = new d.m();
        mVar.d(this.f13145c.getText());
        mVar.c(this.f13146d.getText());
        mVar.b(this.f13147e.getText());
        I i2 = (I) this.f13148f.getSelectedItem();
        if (i2 == null || !(i2.a() instanceof InterfaceC1711c)) {
            mVar.a("");
        } else {
            mVar.a(i2.a().a());
        }
        C1710b c1710b = new C1710b();
        for (C1777c c1777c : this.f13155j) {
            c1710b.setProperty(c1777c.a().c(), c1777c.b());
        }
        mVar.a(c1710b);
        return mVar;
    }

    public boolean c() {
        d.m mVarB = b();
        return (this.f13156k == null || (W.a(this.f13156k.a(), mVarB.a()) && W.a(this.f13156k.b(), mVarB.b()) && W.a(this.f13156k.j(), mVarB.j()) && W.a(this.f13156k.h(), mVarB.h()) && a(this.f13156k.i(), mVarB.i()))) ? false : true;
    }

    private boolean a(C1710b c1710b, C1710b c1710b2) {
        if (c1710b.size() != c1710b2.size()) {
            return false;
        }
        for (String str : c1710b.stringPropertyNames()) {
            String property = c1710b2.getProperty(str);
            if (property == null || !property.equals(c1710b.getProperty(str))) {
                return false;
            }
        }
        return true;
    }

    void d() {
        this.f13145c.setEnabled(false);
        this.f13145c.setText("");
        this.f13146d.setEnabled(false);
        this.f13146d.setText("");
        this.f13147e.setEnabled(false);
        this.f13147e.setText("");
        this.f13148f.setEnabled(false);
        this.f13148f.setSelectedItem("");
        this.f13154i.removeAll();
        this.f13154i.validate();
        this.f13154i.repaint();
        this.f13155j.clear();
        this.f13156k = null;
    }

    public boolean e() {
        return this.f13145c.isEnabled();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a(String str, d.k kVar) {
        d.k kVarE;
        if (str == null || str.isEmpty()) {
            this.f13155j.clear();
            this.f13154i.removeAll();
            return;
        }
        InterfaceC1711c interfaceC1711cB = d.g.a().b(str);
        if (interfaceC1711cB == null) {
            bV.d(a("This action is not supported on this device.") + "\n" + str, this);
            this.f13155j.clear();
            this.f13154i.removeAll();
            return;
        }
        if (kVar == null || kVar.isEmpty()) {
            kVar = f();
        }
        this.f13155j.clear();
        this.f13154i.removeAll();
        int i2 = 0;
        if (interfaceC1711cB != null && (kVarE = interfaceC1711cB.e()) != null) {
            Iterator it = kVarE.iterator();
            while (it.hasNext()) {
                d.i iVar = (d.i) it.next();
                C1777c c1777c = (kVar == null || kVar.a(iVar.c()) == null) ? new C1777c(iVar.a(iVar, ""), this.f13143a) : new C1777c(kVar.a(iVar.c()), this.f13143a);
                this.f13154i.add(c1777c);
                this.f13155j.add(c1777c);
                i2 += c1777c.getPreferredSize().height;
            }
        }
        JLabel jLabel = new JLabel();
        Dimension preferredSize = this.f13150h.getPreferredSize();
        if (i2 < preferredSize.height) {
            preferredSize.height -= i2;
            if (preferredSize.height < 0) {
                preferredSize.height = 0;
            }
            jLabel.setPreferredSize(preferredSize);
            this.f13154i.add(jLabel);
        }
        this.f13150h.getVerticalScrollBar().setValue(0);
        this.f13154i.validate();
        this.f13154i.doLayout();
        this.f13150h.validate();
        this.f13154i.repaint();
    }

    private void a(C1710b c1710b) {
        for (C1777c c1777c : this.f13155j) {
            String property = c1710b.getProperty(c1777c.a().c());
            if (property != null) {
                c1777c.a(property);
            }
        }
    }

    private d.k f() {
        d.k kVar = new d.k();
        for (C1777c c1777c : this.f13155j) {
            kVar.add(c1777c.a().a(c1777c.a(), c1777c.b()));
        }
        return kVar;
    }

    private JPanel a(String str, JComponent jComponent, String str2) {
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new BorderLayout());
        JLabel jLabel = new JLabel(str + CallSiteDescriptor.TOKEN_DELIMITER);
        jLabel.setHorizontalAlignment(4);
        jLabel.setPreferredSize(eJ.a(150, 20));
        jPanel.add("West", jLabel);
        JPanel jPanel2 = new JPanel();
        jPanel2.setLayout(new FlowLayout(0));
        jPanel2.add(jComponent);
        jPanel.add(BorderLayout.CENTER, jPanel2);
        if (str2 != null && !str2.isEmpty()) {
            jPanel.add("East", new cF(str2, this.f13143a));
        }
        return jPanel;
    }

    public void a(d.m mVar) {
        d();
        this.f13145c.setEnabled(true);
        this.f13145c.setText(mVar.a());
        this.f13146d.setEnabled(true);
        this.f13146d.setText(mVar.b());
        this.f13147e.setEnabled(true);
        this.f13147e.setText(mVar.j());
        this.f13148f.setEnabled(true);
        this.f13148f.setSelectedItem(mVar.h());
        this.f13156k = b();
        this.f13145c.requestFocus();
    }

    public void b(d.m mVar) {
        d();
        if (mVar == null) {
            return;
        }
        this.f13145c.setEnabled(false);
        this.f13145c.setText(mVar.a());
        this.f13146d.setEnabled(true);
        this.f13146d.setText(mVar.b());
        this.f13147e.setEnabled(true);
        this.f13147e.setText(mVar.j());
        this.f13148f.setEnabled(true);
        int i2 = 0;
        while (true) {
            if (i2 < this.f13148f.getItemCount()) {
                I i3 = (I) this.f13148f.getItemAt(i2);
                if (i3 != null && i3.a() != null && i3.a().a().equals(mVar.h())) {
                    this.f13148f.setSelectedIndex(i2);
                    break;
                }
                i2++;
            } else {
                break;
            }
        }
        this.f13148f.setSelectedItem(d.g.a().b(mVar.h()));
        a(mVar.i());
        this.f13156k = mVar;
    }
}
