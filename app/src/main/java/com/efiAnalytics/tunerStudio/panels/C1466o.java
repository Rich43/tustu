package com.efiAnalytics.tunerStudio.panels;

import G.C0096cb;
import W.C0188n;
import W.C0189o;
import W.aB;
import bt.C1351j;
import com.efiAnalytics.ui.C1603cn;
import com.efiAnalytics.ui.bV;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import r.C1798a;
import r.C1807j;
import s.C1818g;
import sun.security.pkcs11.wrapper.Constants;

/* renamed from: com.efiAnalytics.tunerStudio.panels.o, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:com/efiAnalytics/tunerStudio/panels/o.class */
class C1466o extends JPanel {

    /* renamed from: a, reason: collision with root package name */
    JButton f10127a;

    /* renamed from: b, reason: collision with root package name */
    JButton f10128b;

    /* renamed from: c, reason: collision with root package name */
    JButton f10129c;

    /* renamed from: e, reason: collision with root package name */
    JButton f10131e;

    /* renamed from: f, reason: collision with root package name */
    JButton f10132f;

    /* renamed from: j, reason: collision with root package name */
    JCheckBox f10136j;

    /* renamed from: k, reason: collision with root package name */
    TriggerLoggerPanel f10137k;

    /* renamed from: d, reason: collision with root package name */
    JButton f10130d = null;

    /* renamed from: g, reason: collision with root package name */
    JComboBox f10133g = new JComboBox();

    /* renamed from: h, reason: collision with root package name */
    JComboBox f10134h = new JComboBox();

    /* renamed from: i, reason: collision with root package name */
    C1603cn f10135i = new C1603cn();

    public C1466o(TriggerLoggerPanel triggerLoggerPanel) {
        this.f10127a = null;
        this.f10128b = null;
        this.f10129c = null;
        this.f10131e = null;
        this.f10132f = null;
        this.f10136j = null;
        this.f10137k = triggerLoggerPanel;
        setBorder(BorderFactory.createTitledBorder(C1818g.b("Ignition Logger Controls")));
        setLayout(new BorderLayout());
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new FlowLayout());
        jPanel.add(new C1351j(C1818g.b("Attention!! For a typical continuous Data Log, use the Data Logging menu or the Graphing & Logging tab when available. This tab is for Specialty High Speed Loggers such as trigger wheel, ignition and pin states. Logging is performed using a series of snapshots as the events occur too fast to log in a continuous fashion.")));
        jPanel.add(new JLabel(C1818g.b("Logger Type:")));
        jPanel.add(this.f10133g);
        this.f10133g.addActionListener(new C1467p(this));
        this.f10127a = new JButton(C1818g.b("Start"));
        this.f10127a.addActionListener(new C1472u(this));
        jPanel.add(this.f10127a);
        this.f10128b = new JButton(C1818g.b("Stop"));
        this.f10128b.addActionListener(new C1473v(this));
        jPanel.add(this.f10128b);
        this.f10129c = new JButton(C1818g.b("Clear"));
        this.f10129c.addActionListener(new C1474w(this));
        jPanel.add(this.f10129c);
        this.f10134h.addItem("250 ms");
        this.f10134h.addItem("500 ms");
        this.f10134h.addItem("750 ms");
        this.f10134h.addItem("1000 ms");
        this.f10134h.addItem("2000 ms");
        this.f10134h.addItem("3000 ms");
        this.f10134h.addItem("4000 ms");
        this.f10134h.setSelectedItem("1000 ms");
        this.f10134h.addActionListener(new C1475x(this));
        jPanel.add(this.f10134h);
        if (triggerLoggerPanel.j()) {
            if (!TriggerLoggerPanel.f10021Q) {
                this.f10131e = new JButton(C1818g.b("Options"));
                jPanel.add(this.f10131e);
                this.f10131e.addActionListener(new C1476y(this));
            }
            this.f10132f = new JButton(C1818g.b("File"));
            jPanel.add(this.f10132f);
            this.f10132f.addActionListener(new C1477z(this));
        }
        add("West", jPanel);
        JPanel jPanel2 = new JPanel();
        jPanel2.setLayout(new BorderLayout());
        this.f10136j = new JCheckBox(C1818g.b("Capture to log file:") + Constants.INDENT);
        jPanel2.add("West", this.f10136j);
        this.f10136j.addActionListener(new A(this));
        jPanel2.add(BorderLayout.CENTER, this.f10135i);
        add(BorderLayout.CENTER, jPanel2);
    }

    public void a() {
        JPopupMenu jPopupMenu = new JPopupMenu();
        if (this.f10137k.j()) {
            jPopupMenu.add(C1818g.b("Open Log File")).addActionListener(new B(this));
        }
        if (this.f10137k.l()) {
            jPopupMenu.add(C1818g.b("Save Current Data to Log")).addActionListener(new C1468q(this));
        }
        jPopupMenu.show(this.f10132f, 0, this.f10132f.getHeight());
    }

    public void a(boolean z2) {
        if (!z2) {
            this.f10135i.a("");
            this.f10137k.g();
            return;
        }
        String[] strArr = {C1798a.ct};
        String str = bH.W.a() + "." + C1798a.ct;
        aE.a aVarA = aE.a.A();
        if (aVarA == null || aVarA.q() == null) {
            bV.d(C1818g.b("There is no project open.\nPlease open a project first."), getParent());
            return;
        }
        String strA = bV.a(getParent(), C1818g.b("Set Ignition Logger File"), strArr, str, aVarA.q());
        if (strA == null || strA.equals("")) {
            this.f10136j.setSelected(false);
            this.f10135i.setText("");
            return;
        }
        if (!strA.toLowerCase().endsWith(C1798a.ct)) {
            strA = strA + "." + C1798a.ct;
        }
        if (!new File(strA).exists() || bV.a(C1818g.b("The selected file already exists.") + "\n" + C1818g.b("Would you like to over write:") + "\n" + strA, (Component) this, true)) {
            c();
            this.f10135i.a(strA);
        }
    }

    protected void b() {
        JPopupMenu jPopupMenu = new JPopupMenu();
        bA.c cVar = new bA.c(C1818g.b("Render Including Non Interrupt Data"), true, this.f10137k.m());
        jPopupMenu.add((JMenuItem) cVar);
        cVar.addActionListener(new C1469r(this));
        jPopupMenu.show(this.f10131e, 0, this.f10131e.getHeight());
    }

    public void c() throws IllegalArgumentException {
        this.f10137k.k();
        this.f10135i.a("");
        this.f10137k.g();
    }

    public void d() throws IllegalArgumentException {
        try {
            ai aiVarN = this.f10137k.n();
            if (aiVarN == null || aiVarN.isEmpty()) {
                bV.d(C1818g.b("No data loaded or captured to save!"), this);
                return;
            }
            String[] strArr = {C1798a.ct};
            String str = bH.W.a() + "." + C1798a.ct;
            aE.a aVarA = aE.a.A();
            if (aVarA == null || aVarA.q() == null) {
                bV.d(C1818g.b("There is no project open.\nPlease open a project first."), getParent());
                return;
            }
            String strA = bV.a(getParent(), C1818g.b("Save log data"), strArr, str, aVarA.q());
            if (strA == null || strA.equals("")) {
                this.f10136j.setSelected(false);
                this.f10135i.setText("");
                return;
            }
            if (!strA.toLowerCase().endsWith(C1798a.ct)) {
                strA = strA + "." + C1798a.ct;
            }
            if (!new File(strA).exists() || bV.a(C1818g.b("The selected file already exists.") + "\n" + C1818g.b("Would you like to over write:") + "\n" + strA, (Component) this, true)) {
                int i2 = 0;
                C0189o c0189oA = C0189o.a((C0188n) aiVarN.get(0), strA, ",");
                do {
                    int i3 = i2;
                    i2++;
                    c0189oA.a((C0188n) aiVarN.get(i3));
                } while (i2 < aiVarN.size());
                c0189oA.a();
                this.f10135i.a(strA);
            }
        } catch (IOException e2) {
            bV.d(C1818g.b("Failed to save Log Data") + "\n" + e2.getMessage(), this);
            Logger.getLogger(C1466o.class.getName()).log(Level.SEVERE, "Failed to save log data.", (Throwable) e2);
        }
    }

    public void e() {
        String[] strArr = {C1798a.ct};
        aE.a aVarA = aE.a.A();
        String strU = C1807j.u();
        if (aVarA != null && aVarA.q() != null) {
            strU = aVarA.q();
        }
        String strB = bV.b(getParent(), C1818g.b("Open Ignition Log File"), strArr, "", strU);
        if (strB == null || strB.equals("")) {
            return;
        }
        this.f10136j.setSelected(false);
        a(strB);
    }

    public void a(String str) {
        aB aBVar = new aB();
        aBVar.a(new C(this));
        File file = new File(str);
        if (file.exists()) {
            new C1470s(this, aBVar, file, str).start();
        } else {
            bV.d(C1818g.b("Ignition Log File not found:") + "\n" + str, this);
        }
    }

    public boolean f() {
        return this.f10136j.isSelected() && this.f10135i.getText().length() > 0;
    }

    public void g() throws IllegalArgumentException {
        E e2 = (E) this.f10133g.getSelectedItem();
        if (e2 == null || e2.a() == null) {
            return;
        }
        this.f10137k.a(e2.a());
        C0096cb c0096cbA = e2.a();
        if ((c0096cbA.e() == null || c0096cbA.e().trim().length() <= 0) && !c0096cbA.n().equals("UDP_Stream")) {
            this.f10134h.setEnabled(true);
        } else {
            this.f10134h.setEnabled(false);
        }
        if (this.f10131e != null) {
            if (c0096cbA == null || !(c0096cbA.d().equals(C0096cb.f1065a) || c0096cbA.d().equals(C0096cb.f1069e) || c0096cbA.d().equals(C0096cb.f1071g) || c0096cbA.d().equals(C0096cb.f1070f) || c0096cbA.d().equals(C0096cb.f1072h))) {
                this.f10131e.setVisible(false);
            } else {
                this.f10131e.setVisible(true);
            }
        }
        this.f10137k.h();
    }

    public ArrayList h() {
        ArrayList arrayList = new ArrayList();
        for (int i2 = 0; i2 < this.f10133g.getItemCount(); i2++) {
            if (this.f10133g.getItemAt(i2) instanceof E) {
                arrayList.add(((E) this.f10133g.getItemAt(i2)).a());
            }
        }
        return arrayList;
    }

    public void a(ArrayList arrayList) {
        this.f10133g.removeAllItems();
        for (int i2 = 0; i2 < arrayList.size(); i2++) {
            this.f10133g.addItem(new E(this, (C0096cb) arrayList.get(i2)));
        }
    }

    public void b(boolean z2) {
        this.f10127a.setEnabled(!z2);
        this.f10128b.setEnabled(z2);
    }

    public void i() {
        new C1471t(this).start();
    }

    public C0096cb j() {
        E e2 = (E) this.f10133g.getSelectedItem();
        if (e2 == null) {
            return null;
        }
        return e2.a();
    }

    protected void k() {
        this.f10137k.a(Integer.parseInt(bH.W.b((String) this.f10134h.getSelectedItem(), " ms", "")));
    }

    public void l() {
        this.f10137k.f();
    }

    String m() {
        return this.f10135i.a();
    }

    public void n() throws IllegalArgumentException {
        this.f10136j.setSelected(false);
        this.f10135i.a("");
        this.f10137k.g();
    }

    public void a(int i2) {
        this.f10134h.setSelectedItem(i2 + " ms");
        k();
    }

    public void b(String str) {
        for (int i2 = 0; i2 < this.f10133g.getItemCount(); i2++) {
            if (this.f10133g.getItemAt(i2).toString().equals(str)) {
                this.f10133g.setSelectedIndex(i2);
            }
        }
    }

    public int o() {
        return Integer.parseInt(bH.W.b((String) this.f10134h.getSelectedItem(), " ms", ""));
    }
}
