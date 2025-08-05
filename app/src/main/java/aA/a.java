package aA;

import az.C0940a;
import bH.aa;
import com.efiAnalytics.ui.bV;
import com.efiAnalytics.ui.eJ;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.Window;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/* loaded from: TunerStudioMS.jar:aA/a.class */
public class a extends JPanel {

    /* renamed from: c, reason: collision with root package name */
    private String f2240c;

    /* renamed from: d, reason: collision with root package name */
    private String f2241d;

    /* renamed from: e, reason: collision with root package name */
    private aa f2244e;

    /* renamed from: a, reason: collision with root package name */
    JDialog f2242a = null;

    /* renamed from: b, reason: collision with root package name */
    D.a f2243b = null;

    /* renamed from: f, reason: collision with root package name */
    private e f2245f = null;

    /* renamed from: g, reason: collision with root package name */
    private e f2246g = null;

    /* renamed from: h, reason: collision with root package name */
    private e f2247h = null;

    /* renamed from: i, reason: collision with root package name */
    private e f2248i = null;

    /* renamed from: j, reason: collision with root package name */
    private e f2249j = null;

    /* renamed from: k, reason: collision with root package name */
    private e f2250k = null;

    public a(String str, String str2, aa aaVar) {
        this.f2244e = null;
        this.f2244e = aaVar;
        this.f2240c = str;
        this.f2241d = str2;
        c();
    }

    private void c() {
        setLayout(new BorderLayout(eJ.a(10), eJ.a(10)));
        JPanel jPanel = new JPanel();
        jPanel.setBorder(BorderFactory.createTitledBorder("Registration Information"));
        jPanel.setLayout(new GridLayout(0, 1, eJ.a(3), eJ.a(3)));
        this.f2245f = new e(this, "Device type:", this.f2240c, true);
        JPanel jPanel2 = new JPanel();
        jPanel2.setLayout(new FlowLayout(1));
        jPanel2.add(this.f2245f);
        jPanel.add(jPanel2);
        this.f2246g = new e(this, "Serial Number:", this.f2241d, true);
        JPanel jPanel3 = new JPanel();
        jPanel3.setLayout(new FlowLayout(1));
        jPanel3.add(this.f2246g);
        jPanel.add(jPanel3);
        this.f2247h = new e(this, "Owner First Name:", "");
        jPanel.add(this.f2247h);
        this.f2248i = new e(this, "Owner Last Name:", "");
        jPanel.add(this.f2248i);
        this.f2249j = new e(this, "Owner eMail Address:", "");
        jPanel.add(this.f2249j);
        this.f2250k = new e(this, "Owner Phone:", "");
        jPanel.add(this.f2250k);
        add(BorderLayout.CENTER, jPanel);
        JPanel jPanel4 = new JPanel();
        jPanel4.setLayout(new FlowLayout(2));
        JButton jButton = new JButton("Activate");
        jButton.addActionListener(new b(this));
        jPanel4.add(jButton);
        JButton jButton2 = new JButton("Cancel");
        jButton2.addActionListener(new c(this));
        jPanel4.add(jButton2);
        add("South", jPanel4);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String a(String str) {
        return b() != null ? b().a(str) : str;
    }

    public void a(D.a aVar) {
        this.f2243b = aVar;
        this.f2245f.a(aVar.a());
        this.f2246g.a(aVar.b());
        this.f2247h.a(aVar.e());
        this.f2248i.a(aVar.f());
        this.f2249j.a(aVar.d());
        this.f2250k.a(aVar.g());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void d() {
        if (this.f2242a != null) {
            this.f2242a.dispose();
        }
    }

    public D.a a() {
        return this.f2243b;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void e() throws HeadlessException {
        String str;
        str = "";
        str = this.f2247h.a().trim().equals("") ? str + "Invalid First Name\n" : "";
        if (this.f2248i.a().trim().equals("")) {
            str = str + "Invalid Last Name\n";
        }
        if (this.f2249j.a().trim().equals("") || !this.f2249j.a().contains("@") || !this.f2249j.a().contains(".")) {
            str = str + "Invalid eMail Address\n";
        }
        if (!str.equals("")) {
            JOptionPane.showMessageDialog(this, "Please correct the following:\n" + str);
            return;
        }
        if (this.f2243b == null) {
            this.f2243b = new D.a();
        }
        this.f2243b.a(this.f2240c);
        this.f2243b.b(this.f2241d);
        this.f2243b.e(this.f2247h.a());
        this.f2243b.f(this.f2248i.a());
        this.f2243b.d(this.f2249j.a());
        this.f2243b.g(this.f2250k.a());
        C0940a c0940a = new C0940a(this.f2242a, bV.a());
        bV.a((Window) this.f2242a, (Component) c0940a);
        c0940a.setVisible(true);
        new d(this, c0940a).start();
    }

    public JDialog a(Window window) {
        this.f2242a = new JDialog(window, a("Device Activation"));
        this.f2242a.add(BorderLayout.CENTER, this);
        this.f2242a.pack();
        bV.a(window, (Component) this.f2242a);
        this.f2242a.setModalityType(Dialog.ModalityType.DOCUMENT_MODAL);
        return this.f2242a;
    }

    public aa b() {
        return this.f2244e;
    }
}
