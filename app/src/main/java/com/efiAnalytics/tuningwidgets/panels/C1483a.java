package com.efiAnalytics.tuningwidgets.panels;

import W.C0184j;
import W.C0188n;
import com.efiAnalytics.ui.bV;
import com.efiAnalytics.ui.cO;
import com.efiAnalytics.ui.eJ;
import i.C1743c;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import s.C1818g;

/* renamed from: com.efiAnalytics.tuningwidgets.panels.a, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:com/efiAnalytics/tuningwidgets/panels/a.class */
public class C1483a extends JPanel {

    /* renamed from: c, reason: collision with root package name */
    JButton f10323c;

    /* renamed from: d, reason: collision with root package name */
    JButton f10324d;

    /* renamed from: a, reason: collision with root package name */
    JComboBox f10321a = new JComboBox();

    /* renamed from: b, reason: collision with root package name */
    JComboBox f10322b = new JComboBox();

    /* renamed from: e, reason: collision with root package name */
    List f10325e = new ArrayList();

    /* renamed from: f, reason: collision with root package name */
    boolean f10326f = false;

    /* renamed from: g, reason: collision with root package name */
    private String f10327g = "";

    public C1483a() {
        setLayout(new BorderLayout());
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new GridLayout(1, 0, eJ.a(5), eJ.a(5)));
        add("East", jPanel);
        JPanel jPanel2 = new JPanel();
        jPanel2.setLayout(new FlowLayout(2));
        try {
            this.f10323c = new JButton(null, new ImageIcon(cO.a().a(cO.f11126P, jPanel2, 16)));
            this.f10323c.setToolTipText(C1818g.b("Fit Curve to data"));
            this.f10323c.addActionListener(new C1504b(this));
            Dimension dimensionA = eJ.a(20, 20);
            this.f10323c.setPreferredSize(dimensionA);
            jPanel2.add(this.f10323c);
            this.f10324d = new JButton(null, new ImageIcon(cO.a().a(cO.f11125O, jPanel2, 16)));
            this.f10324d.setToolTipText(C1818g.b("Add / Edit Data Filter Expression"));
            this.f10324d.addActionListener(new C1505c(this));
            this.f10324d.setPreferredSize(dimensionA);
            jPanel2.add(this.f10324d);
        } catch (V.a e2) {
            bV.d(e2.getLocalizedMessage(), jPanel2);
        }
        add("West", jPanel2);
        JPanel jPanel3 = new JPanel();
        jPanel3.setLayout(new BorderLayout());
        jPanel3.add(BorderLayout.CENTER, new JLabel(C1818g.b("X Axis Field:")));
        JPanel jPanel4 = new JPanel();
        jPanel4.setLayout(new FlowLayout(1));
        jPanel4.add(this.f10321a);
        jPanel3.add("East", jPanel4);
        jPanel.add(jPanel3);
        JPanel jPanel5 = new JPanel();
        jPanel5.setLayout(new BorderLayout());
        jPanel5.add(BorderLayout.CENTER, new JLabel(C1818g.b("Y Axis Field:")));
        JPanel jPanel6 = new JPanel();
        jPanel6.setLayout(new FlowLayout(1));
        jPanel6.add(this.f10322b);
        jPanel5.add("East", jPanel6);
        jPanel.add(jPanel5);
        a();
        this.f10321a.addActionListener(new C1506d(this));
        this.f10322b.addActionListener(new C1507e(this));
        b();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void b() {
        this.f10323c.setEnabled((this.f10321a.getSelectedItem() == null || this.f10321a.getSelectedItem().equals("") || this.f10322b.getSelectedItem() == null || this.f10322b.getSelectedItem().equals("")) ? false : true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void c() {
        Iterator it = this.f10325e.iterator();
        while (it.hasNext()) {
            ((InterfaceC1508f) it.next()).a();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void d() {
        String strShowInputDialog;
        do {
            strShowInputDialog = JOptionPane.showInputDialog(this, C1818g.b("Filter Expression") + ": ", this.f10327g);
            if (strShowInputDialog == null) {
                break;
            }
        } while (!d(strShowInputDialog));
        if (strShowInputDialog != null) {
            this.f10327g = strShowInputDialog;
        } else {
            d(this.f10327g);
        }
    }

    private boolean d(String str) {
        Iterator it = this.f10325e.iterator();
        while (it.hasNext()) {
            if (!((InterfaceC1508f) it.next()).c(str)) {
                return false;
            }
        }
        return true;
    }

    public void a() {
        this.f10326f = true;
        this.f10321a.removeAllItems();
        this.f10322b.removeAllItems();
        C0188n c0188nE = C1743c.a().e();
        if (c0188nE != null) {
            ArrayList arrayList = new ArrayList();
            Iterator it = c0188nE.iterator();
            while (it.hasNext()) {
                arrayList.add(((C0184j) it.next()).a());
            }
            for (String str : bH.R.b(arrayList)) {
                this.f10321a.addItem(str);
                this.f10322b.addItem(str);
            }
        }
        this.f10326f = false;
    }

    public void a(String str) {
        if (this.f10321a.getSelectedItem() == null || !this.f10321a.getSelectedItem().equals(str)) {
            this.f10321a.setSelectedItem(str);
        }
    }

    public void b(String str) {
        if (this.f10322b.getSelectedItem() == null || !this.f10322b.getSelectedItem().equals(str)) {
            this.f10322b.setSelectedItem(str);
        }
    }

    public void a(InterfaceC1508f interfaceC1508f) {
        this.f10325e.add(interfaceC1508f);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void e(String str) {
        Iterator it = this.f10325e.iterator();
        while (it.hasNext()) {
            ((InterfaceC1508f) it.next()).a(str);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void f(String str) {
        Iterator it = this.f10325e.iterator();
        while (it.hasNext()) {
            ((InterfaceC1508f) it.next()).b(str);
        }
    }

    public void c(String str) {
        this.f10327g = str;
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public Dimension getPreferredSize() {
        return super.getPreferredSize();
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public Dimension getMinimumSize() {
        return super.getMinimumSize();
    }
}
