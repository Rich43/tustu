package bC;

import bH.aa;
import com.efiAnalytics.ui.C1685fp;
import com.efiAnalytics.ui.bV;
import com.efiAnalytics.ui.eJ;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import jdk.internal.dynalink.CallSiteDescriptor;

/* loaded from: TunerStudioMS.jar:bC/e.class */
public class e extends JPanel {

    /* renamed from: a, reason: collision with root package name */
    aa f6574a;

    /* renamed from: g, reason: collision with root package name */
    JButton f6581g;

    /* renamed from: h, reason: collision with root package name */
    JButton f6582h;

    /* renamed from: i, reason: collision with root package name */
    JButton f6583i;

    /* renamed from: b, reason: collision with root package name */
    JTextField f6575b = new JTextField("", 15);

    /* renamed from: k, reason: collision with root package name */
    private Z.c f6576k = null;

    /* renamed from: c, reason: collision with root package name */
    Z.e f6577c = null;

    /* renamed from: d, reason: collision with root package name */
    j f6578d = new j(this);

    /* renamed from: e, reason: collision with root package name */
    JList f6579e = new JList(this.f6578d);

    /* renamed from: f, reason: collision with root package name */
    List f6580f = new ArrayList();

    /* renamed from: j, reason: collision with root package name */
    List f6584j = new ArrayList();

    public e(aa aaVar) {
        this.f6574a = aaVar;
        setBorder(BorderFactory.createTitledBorder(b("Standard Field Name Editor")));
        setLayout(new BorderLayout(eJ.a(5), eJ.a(5)));
        add("North", a("Standard Field Name", this.f6575b));
        this.f6575b.setEditable(false);
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new BorderLayout());
        JPanel jPanel2 = new JPanel();
        jPanel2.setLayout(new GridLayout(1, 2, eJ.a(5), eJ.a(5)));
        JPanel jPanel3 = new JPanel();
        jPanel3.setLayout(new BorderLayout());
        jPanel3.add("North", new JLabel(b("Imported Log Name") + CallSiteDescriptor.TOKEN_DELIMITER, 4));
        JPanel jPanel4 = new JPanel();
        jPanel4.setLayout(new GridLayout(0, 1, eJ.a(5), eJ.a(5)));
        this.f6581g = new JButton(b("Add Log Name"));
        jPanel4.add(this.f6581g);
        this.f6582h = new JButton(b("Remove Log Name"));
        jPanel4.add(this.f6582h);
        this.f6583i = new JButton(b("Load Default"));
        jPanel4.add(this.f6583i);
        JPanel jPanel5 = new JPanel();
        jPanel5.setLayout(new FlowLayout(1));
        jPanel5.add(jPanel4);
        jPanel3.add(BorderLayout.CENTER, jPanel5);
        jPanel2.add(jPanel3);
        this.f6581g.addActionListener(new f(this));
        this.f6582h.addActionListener(new g(this));
        this.f6583i.addActionListener(new h(this));
        this.f6579e.setBorder(BorderFactory.createEmptyBorder(0, eJ.a(3), 0, eJ.a(3)));
        Component jScrollPane = new JScrollPane(this.f6579e);
        jScrollPane.setPreferredSize(eJ.a(140, 90));
        jPanel2.add(jScrollPane);
        jPanel.add(BorderLayout.CENTER, jPanel2);
        this.f6579e.setSelectionMode(0);
        this.f6579e.addListSelectionListener(new i(this));
        add(BorderLayout.CENTER, jPanel);
    }

    public void a() {
        String strA = "";
        do {
            strA = bV.a((Component) this, false, "New Imported Log Name", strA);
            if (strA != null && !strA.isEmpty()) {
                if (a(strA)) {
                    this.f6580f.add(strA);
                    this.f6578d = new j(this);
                    this.f6579e.setModel(this.f6578d);
                    i();
                    return;
                }
                bV.d("Invalid characters in field name", this);
            }
            if (strA == null) {
                return;
            }
        } while (!strA.isEmpty());
    }

    public boolean a(String str) {
        return (str.contains("[") || str.contains("]")) ? false : true;
    }

    public void b() {
        int selectedIndex = this.f6579e.getSelectedIndex();
        if (selectedIndex >= 0) {
            this.f6580f.remove(selectedIndex);
            this.f6578d = new j(this);
            this.f6579e.setModel(this.f6578d);
            i();
        }
    }

    public void c() {
        Z.e eVarA = this.f6576k.a(this.f6575b.getText());
        if (eVarA != null) {
            this.f6580f.clear();
            if (eVarA != null) {
                Iterator it = eVarA.b().iterator();
                while (it.hasNext()) {
                    this.f6580f.add((String) it.next());
                }
            }
            this.f6578d = new j(this);
            this.f6579e.setModel(this.f6578d);
            i();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void h() {
        this.f6582h.setEnabled(this.f6579e.getSelectedIndex() >= 0);
    }

    public void a(Z.e eVar) {
        this.f6577c = eVar;
        this.f6575b.setText("");
        this.f6580f.clear();
        this.f6579e.clearSelection();
        if (eVar != null) {
            this.f6575b.setText(eVar.a());
            this.f6580f.addAll(eVar.b());
            C1685fp.a((Component) this, true);
        }
        this.f6578d = new j(this);
        this.f6579e.setModel(this.f6578d);
        this.f6582h.setEnabled(this.f6579e.getSelectedIndex() >= 0);
        this.f6579e.repaint();
        i();
    }

    public Z.e d() {
        Z.e eVar = null;
        if (this.f6575b.getText().trim().length() > 0) {
            eVar = new Z.e(this.f6575b.getText());
            Iterator it = this.f6580f.iterator();
            while (it.hasNext()) {
                eVar.a((String) it.next());
            }
        }
        return eVar;
    }

    private boolean a(Z.e eVar, Z.e eVar2) {
        if (eVar == null && eVar2 == null) {
            return true;
        }
        if (((eVar == null) ^ (eVar2 == null)) || !eVar.a().equals(eVar2.a()) || eVar.b().size() != eVar2.b().size()) {
            return false;
        }
        for (int i2 = 0; i2 < eVar.b().size(); i2++) {
            if (!eVar2.b((String) eVar.b().get(i2))) {
                return false;
            }
        }
        return true;
    }

    private JPanel a(String str, JComponent jComponent) {
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new GridLayout(1, 2, eJ.a(5), eJ.a(5)));
        JLabel jLabel = new JLabel(b(str) + CallSiteDescriptor.TOKEN_DELIMITER, 4);
        jLabel.setMinimumSize(eJ.a(120, 20));
        jLabel.setPreferredSize(eJ.a(120, 20));
        jPanel.add(jLabel, "West");
        jPanel.add(jComponent, BorderLayout.CENTER);
        return jPanel;
    }

    private String b(String str) {
        return this.f6574a != null ? this.f6574a.a(str) : str;
    }

    public boolean e() {
        return !a(this.f6577c, d());
    }

    public void f() {
        this.f6577c = d();
    }

    public void g() {
        this.f6580f.clear();
        this.f6575b.setText("");
        this.f6579e.repaint();
        this.f6577c = null;
        i();
        C1685fp.a((Component) this, false);
    }

    public void a(a aVar) {
        this.f6584j.add(aVar);
    }

    private void i() {
        this.f6583i.setEnabled((this.f6576k == null || this.f6576k.a(this.f6575b.getText()) == null || a(this.f6576k.a(this.f6575b.getText()), d())) ? false : true);
        boolean zE = e();
        for (a aVar : this.f6584j) {
            if (zE) {
                aVar.a();
            } else {
                aVar.b();
            }
        }
    }

    public void a(Z.c cVar) {
        this.f6576k = cVar;
    }
}
