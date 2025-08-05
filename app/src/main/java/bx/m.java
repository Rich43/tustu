package bx;

import bH.W;
import bH.aa;
import com.efiAnalytics.ui.eJ;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import jdk.internal.dynalink.CallSiteDescriptor;
import sun.security.tools.policytool.ToolWindow;

/* loaded from: TunerStudioMS.jar:bx/m.class */
class m extends JPanel {

    /* renamed from: e, reason: collision with root package name */
    JButton f9208e;

    /* renamed from: f, reason: collision with root package name */
    aa f9209f;

    /* renamed from: a, reason: collision with root package name */
    j f9203a = null;

    /* renamed from: i, reason: collision with root package name */
    private k f9204i = null;

    /* renamed from: b, reason: collision with root package name */
    JTextField f9205b = new q("", 15);

    /* renamed from: c, reason: collision with root package name */
    JTextField f9206c = new JTextField("", 50);

    /* renamed from: d, reason: collision with root package name */
    JTextPane f9207d = new JTextPane();

    /* renamed from: g, reason: collision with root package name */
    boolean f9210g = true;

    /* renamed from: h, reason: collision with root package name */
    List f9211h = new ArrayList();

    m(aa aaVar) {
        this.f9208e = null;
        this.f9209f = null;
        this.f9209f = aaVar;
        setLayout(new BorderLayout());
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new GridLayout(2, 0, eJ.a(3), eJ.a(3)));
        jPanel.add(a(a("Unique Name"), this.f9205b));
        jPanel.add(a(a("Description"), this.f9206c));
        add("North", jPanel);
        this.f9207d.setEditable(true);
        JScrollPane jScrollPane = new JScrollPane(this.f9207d);
        jScrollPane.setPreferredSize(eJ.a(320, 80));
        JPanel jPanel2 = new JPanel();
        jPanel2.setLayout(new BorderLayout());
        jPanel2.add("North", new JLabel("<html>" + a("Expression, Filter Data When:") + "          " + a("Example") + ": [RPM] > 6000 && [TPS] < 90<br>Press CTRL+Space for Field List"));
        jPanel2.add(BorderLayout.CENTER, jScrollPane);
        add(BorderLayout.CENTER, jPanel2);
        this.f9208e = new JButton(a(ToolWindow.SAVE_POLICY_FILE));
        this.f9208e.addActionListener(new n(this));
        JPanel jPanel3 = new JPanel();
        jPanel3.setLayout(new FlowLayout(2));
        jPanel3.add(this.f9208e);
        add("South", jPanel3);
        b();
    }

    private JPanel a(String str, JTextField jTextField) {
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new BorderLayout());
        JLabel jLabel = new JLabel(str + CallSiteDescriptor.TOKEN_DELIMITER);
        jLabel.setHorizontalAlignment(4);
        jLabel.setPreferredSize(eJ.a(90, 20));
        jPanel.add("West", jLabel);
        JPanel jPanel2 = new JPanel();
        jPanel2.setLayout(new FlowLayout(0));
        jPanel2.add(jTextField);
        jPanel.add(BorderLayout.CENTER, jPanel2);
        return jPanel;
    }

    public void a(j jVar) {
        if (jVar == null) {
            b();
            return;
        }
        this.f9203a = jVar;
        this.f9205b.setText(jVar.a());
        this.f9205b.setEnabled(false);
        this.f9206c.setText(jVar.b());
        this.f9206c.setEnabled(true);
        this.f9207d.setText(jVar.c());
        this.f9207d.setEnabled(true);
        this.f9207d.requestFocus();
        this.f9207d.selectAll();
    }

    public void b(j jVar) {
        this.f9203a = jVar;
        this.f9205b.setText(jVar.a());
        this.f9205b.setEnabled(true);
        this.f9206c.setText(jVar.b());
        this.f9206c.setEnabled(true);
        this.f9207d.setText(jVar.c());
        this.f9207d.setEnabled(true);
        this.f9205b.requestFocus();
    }

    public j a() {
        this.f9203a.a(this.f9205b.getText().trim());
        this.f9203a.b(this.f9206c.getText());
        String strB = W.b(this.f9207d.getText(), "\n", " ");
        if (this.f9204i != null) {
            strB = this.f9204i.a(strB);
        }
        this.f9203a.c(strB);
        return this.f9203a;
    }

    public void b() {
        this.f9203a = null;
        this.f9205b.setEnabled(false);
        this.f9205b.setText("");
        this.f9206c.setEnabled(false);
        this.f9206c.setText("");
        this.f9207d.setEnabled(false);
        this.f9207d.setText("");
    }

    public void a(y yVar) {
        this.f9211h.add(yVar);
    }

    public boolean c() {
        Iterator it = this.f9211h.iterator();
        while (it.hasNext()) {
            if (!((y) it.next()).a()) {
                return false;
            }
        }
        return true;
    }

    private String a(String str) {
        return this.f9209f != null ? this.f9209f.a(str) : str;
    }

    public boolean d() {
        return (this.f9203a == null || (this.f9203a.a().equals(this.f9205b.getText().trim()) && this.f9203a.b().equals(this.f9206c.getText()) && this.f9203a.c().equals(this.f9207d.getText()))) ? false : true;
    }

    public void a(k kVar) {
        this.f9204i = kVar;
    }

    public JTextPane e() {
        return this.f9207d;
    }
}
