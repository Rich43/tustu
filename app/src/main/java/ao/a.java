package aO;

import G.C0096cb;
import W.aB;
import bH.W;
import com.efiAnalytics.ui.C1603cn;
import com.efiAnalytics.ui.InterfaceC1602cm;
import com.efiAnalytics.ui.bV;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.io.File;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import sun.security.pkcs11.wrapper.Constants;

/* loaded from: TunerStudioMS.jar:aO/a.class */
class a extends JPanel {

    /* renamed from: d, reason: collision with root package name */
    JButton f2642d;

    /* renamed from: e, reason: collision with root package name */
    JButton f2643e;

    /* renamed from: j, reason: collision with root package name */
    JCheckBox f2648j;

    /* renamed from: k, reason: collision with root package name */
    k f2649k;

    /* renamed from: a, reason: collision with root package name */
    JButton f2639a = null;

    /* renamed from: b, reason: collision with root package name */
    JButton f2640b = null;

    /* renamed from: c, reason: collision with root package name */
    JButton f2641c = null;

    /* renamed from: f, reason: collision with root package name */
    JButton f2644f = null;

    /* renamed from: g, reason: collision with root package name */
    JComboBox f2645g = new JComboBox();

    /* renamed from: h, reason: collision with root package name */
    JComboBox f2646h = new JComboBox();

    /* renamed from: i, reason: collision with root package name */
    C1603cn f2647i = new C1603cn();

    /* renamed from: l, reason: collision with root package name */
    C.d f2650l = new C.c();

    /* renamed from: m, reason: collision with root package name */
    private InterfaceC1602cm f2651m = null;

    public a(k kVar) {
        this.f2642d = null;
        this.f2643e = null;
        this.f2648j = null;
        this.f2649k = kVar;
        setBorder(BorderFactory.createTitledBorder(this.f2650l.a("Ignition Logger Controls")));
        setLayout(new BorderLayout());
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new FlowLayout());
        jPanel.add(new JLabel(this.f2650l.a("Logger Type:")));
        jPanel.add(this.f2645g);
        this.f2645g.setEnabled(!kVar.f2701D);
        this.f2645g.addActionListener(new b(this));
        if (kVar.f()) {
            this.f2643e = new JButton(this.f2650l.a("Options"));
            jPanel.add(this.f2643e);
            this.f2643e.addActionListener(new c(this));
            this.f2642d = new JButton(this.f2650l.a("Open Log"));
            jPanel.add(this.f2642d);
            this.f2642d.addActionListener(new d(this));
        }
        add("West", jPanel);
        JPanel jPanel2 = new JPanel();
        jPanel2.setLayout(new BorderLayout());
        this.f2648j = new JCheckBox(this.f2650l.a("Capture to log file:") + Constants.INDENT);
        if (!kVar.f2701D) {
            jPanel2.add("West", this.f2648j);
        }
        this.f2648j.addActionListener(new e(this));
        jPanel2.add(BorderLayout.CENTER, this.f2647i);
        add(BorderLayout.CENTER, jPanel2);
    }

    public void a(boolean z2) {
        if (!z2) {
            this.f2647i.a("");
            this.f2649k.c();
            return;
        }
        String strA = bV.a(getParent(), this.f2650l.a("Set Ignition Logger File"), this.f2651m.b(), W.a() + "." + this.f2651m.c(), this.f2651m.a().getAbsolutePath());
        if (strA == null || strA.equals("")) {
            this.f2648j.setSelected(false);
            this.f2647i.setText("");
            return;
        }
        if (!strA.toLowerCase().endsWith(this.f2651m.c())) {
            strA = strA + "." + this.f2651m.c();
        }
        if (!new File(strA).exists() || bV.a("The selected file already exists.\nWould you like to over write:\n" + strA, (Component) this, true)) {
            b();
            this.f2647i.a(strA);
        }
    }

    protected void a() {
        JPopupMenu jPopupMenu = new JPopupMenu();
        JCheckBoxMenuItem jCheckBoxMenuItem = new JCheckBoxMenuItem(this.f2650l.a("Render Including Non Interrupt Data"), this.f2649k.j());
        jPopupMenu.add((JMenuItem) jCheckBoxMenuItem);
        jCheckBoxMenuItem.addActionListener(new f(this));
        jPopupMenu.show(this.f2643e, 0, this.f2643e.getHeight());
    }

    public void b() throws IllegalArgumentException {
        this.f2649k.g();
        this.f2647i.a("");
        this.f2649k.c();
    }

    public void c() {
        a(bV.b(getParent(), this.f2650l.a("Open Ignition Log File"), this.f2651m.b(), "", this.f2651m.a().getAbsolutePath()));
    }

    public void a(String str) throws IllegalArgumentException {
        if (str == null || str.equals("")) {
            return;
        }
        this.f2648j.setSelected(false);
        aB aBVar = new aB();
        aBVar.a(new g(this));
        File file = new File(str);
        if (!file.exists()) {
            bV.d("Ignition Log File not found:\n" + str, this);
            return;
        }
        aBVar.b(file, this.f2649k.i() + 1);
        this.f2651m.a(file.getParentFile());
        this.f2647i.a(str);
        this.f2648j.setSelected(false);
    }

    public void d() {
        i iVar = (i) this.f2645g.getSelectedItem();
        if (iVar == null || iVar.a() == null) {
            return;
        }
        this.f2649k.a(iVar.a());
        if (iVar.a().d().equals(C0096cb.f1065a)) {
            this.f2646h.setEnabled(false);
        } else {
            this.f2646h.setEnabled(true);
        }
        this.f2649k.d();
    }

    public void a(ArrayList arrayList) {
        this.f2645g.removeAllItems();
        for (int i2 = 0; i2 < arrayList.size(); i2++) {
            this.f2645g.addItem(new i(this, (C0096cb) arrayList.get(i2)));
        }
    }

    public void b(String str) {
        for (int i2 = 0; i2 < this.f2645g.getItemCount(); i2++) {
            if (this.f2645g.getItemAt(i2).toString().equals(str)) {
                this.f2645g.setSelectedIndex(i2);
            }
        }
    }

    public void a(InterfaceC1602cm interfaceC1602cm) {
        this.f2651m = interfaceC1602cm;
    }
}
