package com.efiAnalytics.ui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Window;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/fK.class */
public class fK extends JPanel {

    /* renamed from: a, reason: collision with root package name */
    JButton f11643a;

    /* renamed from: b, reason: collision with root package name */
    JButton f11644b;

    /* renamed from: c, reason: collision with root package name */
    JPanel f11645c;

    /* renamed from: d, reason: collision with root package name */
    int f11646d;

    /* renamed from: e, reason: collision with root package name */
    CardLayout f11647e;

    /* renamed from: f, reason: collision with root package name */
    ArrayList f11648f;

    /* renamed from: g, reason: collision with root package name */
    boolean f11649g;

    /* renamed from: i, reason: collision with root package name */
    private bH.aa f11650i;

    /* renamed from: h, reason: collision with root package name */
    ArrayList f11651h;

    public fK() {
        this(null);
    }

    public fK(String str) {
        this(str, null);
    }

    public fK(String str, bH.aa aaVar) {
        this.f11643a = new JButton("Cancel");
        this.f11644b = new JButton("Next >");
        this.f11645c = new JPanel();
        this.f11646d = 0;
        this.f11647e = new CardLayout();
        this.f11648f = new ArrayList();
        this.f11649g = false;
        this.f11650i = null;
        this.f11651h = new ArrayList();
        a(aaVar);
        String strA = str == null ? "" : a(str);
        setBorder(BorderFactory.createEmptyBorder(5, 5, 25, 5));
        setLayout(new BorderLayout());
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new FlowLayout(2));
        jPanel.add(this.f11643a);
        this.f11643a.addActionListener(new fL(this));
        jPanel.add(new JLabel("   "));
        jPanel.add(this.f11644b);
        this.f11644b.addActionListener(new fM(this));
        add("South", jPanel);
        this.f11645c.setLayout(this.f11647e);
        if (strA != null && !strA.isEmpty()) {
            this.f11645c.setBorder(BorderFactory.createTitledBorder(a(strA)));
        }
        add(BorderLayout.CENTER, this.f11645c);
    }

    public void a(Container container) {
        if (this.f11651h.contains(container)) {
            return;
        }
        this.f11651h.add(container);
    }

    public void b(Container container) {
        this.f11651h.remove(container);
    }

    public void a(boolean z2) {
        this.f11643a.setEnabled(z2);
    }

    private String a(String str) {
        return g() != null ? g().a(str) : str;
    }

    public void a() {
        setCursor(Cursor.getPredefinedCursor(3));
        boolean z2 = false;
        if (this.f11646d >= this.f11645c.getComponentCount() - 1) {
            c();
        } else if (c(h())) {
            this.f11646d++;
            this.f11647e.next(this.f11645c);
            Container containerA = a(this.f11646d);
            if (containerA != null && this.f11651h.contains(containerA)) {
                this.f11646d++;
                this.f11647e.next(this.f11645c);
            }
            z2 = !d(h());
            f();
        }
        if (z2) {
            SwingUtilities.invokeLater(new fN(this));
        }
        setCursor(Cursor.getPredefinedCursor(0));
    }

    public void b() {
        if (this.f11646d <= 0) {
            d();
            return;
        }
        this.f11646d--;
        this.f11647e.previous(this.f11645c);
        Container containerA = a(this.f11646d);
        while (true) {
            Container container = containerA;
            if (container == null || !this.f11651h.contains(container)) {
                break;
            }
            this.f11646d--;
            this.f11647e.previous(this.f11645c);
            containerA = a(this.f11646d);
        }
        f();
    }

    public void c() {
        if (c(h())) {
            Iterator it = this.f11648f.iterator();
            while (it.hasNext() && ((fR) it.next()).b()) {
            }
        }
    }

    public boolean c(Container container) {
        Iterator it = this.f11648f.iterator();
        while (it.hasNext()) {
            if (!((fR) it.next()).a(container)) {
                return false;
            }
        }
        return true;
    }

    public boolean d(Container container) {
        Iterator it = this.f11648f.iterator();
        while (it.hasNext()) {
            if (!((fR) it.next()).b(container)) {
                return false;
            }
        }
        return true;
    }

    public void d() {
        Iterator it = this.f11648f.iterator();
        while (it.hasNext()) {
            ((fR) it.next()).c();
        }
        bH.C.c("Cancel Wizard");
    }

    public Container a(int i2) {
        if (i2 < 0 || i2 >= this.f11645c.getComponentCount()) {
            return null;
        }
        return (Container) ((Container) this.f11645c.getComponent(i2)).getComponent(0);
    }

    public int e() {
        return this.f11645c.getComponentCount();
    }

    private Container h() {
        for (int i2 = 0; i2 < this.f11645c.getComponentCount(); i2++) {
            if (this.f11645c.getComponent(i2).isVisible()) {
                return (Container) ((Container) this.f11645c.getComponent(i2)).getComponent(0);
            }
        }
        return null;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public void e(Container container) {
        JPanel jPanel = new JPanel();
        if ((container instanceof fS) && ((fS) container).g_()) {
            jPanel.setLayout(new GridLayout(1, 1));
        } else {
            jPanel.setLayout(new fQ(this));
        }
        jPanel.add(container);
        this.f11645c.add(jPanel, "" + this.f11645c.getComponentCount());
    }

    public void a(fR fRVar) {
        this.f11648f.add(fRVar);
    }

    public void f() {
        if (this.f11646d == 0) {
            this.f11643a.setText(a("Cancel"));
        } else {
            this.f11643a.setText("< " + a("Back"));
        }
        if (this.f11646d == this.f11645c.getComponentCount() - 1) {
            this.f11644b.setText(a("Finish"));
        } else {
            this.f11644b.setText(a("Next") + " >");
        }
    }

    public JDialog a(Window window, String str) {
        return a(window, str, true);
    }

    public JDialog a(Window window, String str, boolean z2) {
        if (str == null || str.equals("")) {
            str = "Wizard";
        }
        fP fPVar = new fP(this, window, a(str));
        fPVar.add(BorderLayout.CENTER, this);
        this.f11647e.first(this.f11645c);
        fPVar.pack();
        a(new fO(this, fPVar));
        bV.a(window, (Component) fPVar);
        if (z2) {
            fPVar.setModalityType(Dialog.ModalityType.DOCUMENT_MODAL);
        }
        return fPVar;
    }

    public bH.aa g() {
        return this.f11650i;
    }

    public void a(bH.aa aaVar) {
        this.f11650i = aaVar;
        this.f11643a.setText(a("Cancel"));
        this.f11644b.setText(a("Next") + " >");
    }
}
