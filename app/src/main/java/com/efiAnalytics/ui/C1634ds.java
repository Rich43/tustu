package com.efiAnalytics.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Window;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

/* renamed from: com.efiAnalytics.ui.ds, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/ds.class */
public class C1634ds extends JPanel {

    /* renamed from: c, reason: collision with root package name */
    String f11446c;

    /* renamed from: a, reason: collision with root package name */
    JButton f11444a = new JButton("Cancel");

    /* renamed from: b, reason: collision with root package name */
    JButton f11445b = new JButton("Ok");

    /* renamed from: d, reason: collision with root package name */
    JTabbedPane f11447d = new JTabbedPane();

    /* renamed from: e, reason: collision with root package name */
    ArrayList f11448e = new ArrayList();

    /* renamed from: f, reason: collision with root package name */
    boolean f11449f = false;

    public C1634ds(String str) {
        this.f11446c = null;
        this.f11446c = str == null ? "" : str;
        setLayout(new BorderLayout());
        add(BorderLayout.CENTER, this.f11447d);
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new FlowLayout(2));
        if (bV.d()) {
            jPanel.add(this.f11445b);
            jPanel.add(this.f11444a);
        } else {
            jPanel.add(this.f11444a);
            jPanel.add(this.f11445b);
        }
        this.f11445b.addActionListener(new C1635dt(this));
        this.f11444a.addActionListener(new C1636du(this));
        add("South", jPanel);
        this.f11447d.addChangeListener(new C1637dv(this));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void c() {
        a();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void d() {
        b();
    }

    public void a() {
        if (a(e())) {
            Iterator it = this.f11448e.iterator();
            while (it.hasNext() && ((fR) it.next()).b()) {
            }
        }
    }

    public boolean a(Container container) {
        Iterator it = this.f11448e.iterator();
        while (it.hasNext()) {
            if (!((fR) it.next()).a(container)) {
                return false;
            }
        }
        return true;
    }

    public void b() {
        Iterator it = this.f11448e.iterator();
        while (it.hasNext()) {
            ((fR) it.next()).c();
        }
    }

    private Container e() {
        return (Container) this.f11447d.getComponentAt(this.f11447d.getSelectedIndex());
    }

    public void a(Container container, String str) {
        if (container instanceof JScrollPane) {
            this.f11447d.add(str, container);
            return;
        }
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new C1639dx(this));
        jPanel.add(container);
        this.f11447d.add(str, jPanel);
    }

    public void a(fR fRVar) {
        this.f11448e.add(fRVar);
    }

    public void a(Window window, String str) {
        if (str == null || str.equals("")) {
            str = "Wizard";
        }
        JDialog jDialog = new JDialog(window, str);
        jDialog.add(BorderLayout.CENTER, this);
        jDialog.pack();
        a(new C1638dw(this, jDialog));
        bV.a(window, (Component) jDialog);
        jDialog.setVisible(true);
    }

    public void a(Component component) {
        this.f11447d.setSelectedComponent(component.getParent());
    }

    public boolean a(String str) {
        for (int i2 = 0; i2 < this.f11447d.getTabCount(); i2++) {
            if (this.f11447d.getTitleAt(i2).equals(str)) {
                this.f11447d.setSelectedIndex(i2);
                return true;
            }
        }
        return false;
    }

    public void a(int i2, boolean z2) {
        this.f11447d.setEnabledAt(i2, z2);
    }

    public boolean a(String str, boolean z2) {
        for (int i2 = 0; i2 < this.f11447d.getTabCount(); i2++) {
            if (this.f11447d.getTitleAt(i2).equals(str)) {
                a(i2, z2);
                return true;
            }
        }
        return false;
    }
}
