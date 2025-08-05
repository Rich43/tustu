package com.efiAnalytics.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JPanel;

/* renamed from: com.efiAnalytics.ui.bd, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/bd.class */
public class C1566bd extends JPanel implements InterfaceC1565bc {

    /* renamed from: a, reason: collision with root package name */
    JColorChooser f10986a = new JColorChooser();

    /* renamed from: b, reason: collision with root package name */
    ArrayList f10987b = new ArrayList();

    /* renamed from: g, reason: collision with root package name */
    private Color f10988g = Color.WHITE;

    /* renamed from: c, reason: collision with root package name */
    JDialog f10989c = null;

    /* renamed from: d, reason: collision with root package name */
    JButton f10990d;

    /* renamed from: e, reason: collision with root package name */
    JButton f10991e;

    /* renamed from: f, reason: collision with root package name */
    JButton f10992f;

    public C1566bd() {
        setLayout(new BorderLayout());
        add(BorderLayout.CENTER, this.f10986a);
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new FlowLayout(2));
        this.f10990d = new JButton("Apply");
        this.f10990d.addActionListener(new C1567be(this));
        jPanel.add(this.f10990d);
        this.f10992f = new JButton("Transparent");
        this.f10992f.addActionListener(new C1568bf(this));
        jPanel.add(this.f10992f);
        this.f10991e = new JButton("Reset");
        this.f10991e.addActionListener(new C1569bg(this));
        jPanel.add(this.f10991e);
        this.f10991e.setEnabled(false);
        add("South", jPanel);
    }

    public JDialog a(Component component, String str, Color color) {
        a(color);
        if (this.f10989c == null || !this.f10989c.isVisible()) {
            this.f10989c = bV.a(this, component, str, this);
        } else {
            this.f10989c.setTitle(str);
            this.f10989c.setVisible(true);
        }
        return this.f10989c;
    }

    public void a(InterfaceC1570bh interfaceC1570bh) {
        this.f10987b.clear();
        this.f10987b.add(interfaceC1570bh);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void b(Color color) {
        Iterator it = this.f10987b.iterator();
        while (it.hasNext()) {
            ((InterfaceC1570bh) it.next()).a(color);
        }
    }

    public void a(Color color) {
        this.f10988g = color;
        this.f10986a.setColor(color);
        this.f10991e.setEnabled(false);
    }

    @Override // com.efiAnalytics.ui.InterfaceC1565bc
    public void close() {
        this.f10987b.clear();
    }
}
