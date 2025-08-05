package com.efiAnalytics.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/* renamed from: com.efiAnalytics.ui.dz, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/dz.class */
public class C1641dz extends JPanel {

    /* renamed from: a, reason: collision with root package name */
    InterfaceC1640dy f11456a;

    /* renamed from: c, reason: collision with root package name */
    JButton f11458c;

    /* renamed from: d, reason: collision with root package name */
    JButton f11459d;

    /* renamed from: e, reason: collision with root package name */
    bH.aa f11460e;

    /* renamed from: b, reason: collision with root package name */
    JPanel f11457b = new JPanel();

    /* renamed from: f, reason: collision with root package name */
    JLabel f11461f = new JLabel("", 0);

    /* renamed from: h, reason: collision with root package name */
    private Component f11462h = null;

    /* renamed from: g, reason: collision with root package name */
    JScrollPane f11463g = new JScrollPane();

    public C1641dz(InterfaceC1640dy interfaceC1640dy, bH.aa aaVar) throws IllegalArgumentException {
        this.f11456a = interfaceC1640dy;
        this.f11460e = aaVar;
        setLayout(new BorderLayout());
        add(this.f11463g, BorderLayout.CENTER);
        this.f11463g.setViewportView(this.f11457b);
        this.f11457b.setLayout(new GridLayout(1, 1));
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new BorderLayout());
        this.f11458c = new JButton("<<  " + a("Previous"));
        this.f11459d = new JButton(a("Next") + "  >>");
        jPanel.add(this.f11458c, "West");
        jPanel.add(this.f11459d, "East");
        this.f11459d.addActionListener(new dA(this));
        this.f11458c.addActionListener(new dB(this));
        jPanel.add(this.f11461f, BorderLayout.CENTER);
        add(jPanel, "South");
        a(0);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void d() throws IllegalArgumentException {
        a(this.f11456a.d() + 1);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void e() throws IllegalArgumentException {
        a(this.f11456a.d() - 1);
    }

    public void a(int i2) throws IllegalArgumentException {
        Component componentA = this.f11456a.a(i2);
        if (this.f11462h != null) {
            this.f11457b.remove(this.f11462h);
            this.f11456a.b(this.f11462h);
            this.f11462h = null;
        }
        if (componentA != null) {
            this.f11456a.a(componentA);
            this.f11457b.add(componentA);
            this.f11462h = componentA;
        }
        this.f11459d.setEnabled(this.f11456a.b());
        this.f11458c.setEnabled(this.f11456a.c());
        int iD = this.f11456a.d();
        int iA = this.f11456a.a();
        String str = a("Page") + ": " + (iD + 1);
        if (iA > 0) {
            str = str + " " + a("of") + " " + iA;
        }
        this.f11461f.setText(str);
        this.f11457b.doLayout();
        this.f11463g.validate();
        this.f11457b.repaint();
    }

    private String a(String str) {
        return this.f11460e != null ? this.f11460e.a(str) : str;
    }

    public JScrollPane a() {
        return this.f11463g;
    }

    public JButton b() {
        return this.f11459d;
    }

    public JButton c() {
        return this.f11458c;
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public Dimension getPreferredSize() {
        return (this.f11456a == null || this.f11456a.a(this.f11456a.d()) == null) ? super.getPreferredSize() : this.f11456a.a(this.f11456a.d()).getPreferredSize();
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public Dimension getMinimumSize() {
        return (this.f11456a == null || this.f11456a.a(this.f11456a.d()) == null) ? super.getMinimumSize() : this.f11456a.a(this.f11456a.d()).getMinimumSize();
    }
}
