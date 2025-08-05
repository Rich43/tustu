package com.efiAnalytics.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Polygon;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.JPanel;
import javax.swing.UIManager;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/cK.class */
public class cK extends JPanel {

    /* renamed from: e, reason: collision with root package name */
    Color f11075e;

    /* renamed from: c, reason: collision with root package name */
    int f11071c = eJ.a(10);

    /* renamed from: f, reason: collision with root package name */
    private boolean f11072f = true;

    /* renamed from: g, reason: collision with root package name */
    private boolean f11073g = true;

    /* renamed from: d, reason: collision with root package name */
    List f11074d = new ArrayList();

    /* renamed from: a, reason: collision with root package name */
    Polygon f11069a = new Polygon();

    /* renamed from: b, reason: collision with root package name */
    Polygon f11070b = new Polygon();

    public cK() {
        this.f11075e = null;
        a();
        addMouseListener(new cL(this));
        Color color = UIManager.getColor("Button.foreground");
        if (color != null) {
            setForeground(color);
        }
        Color color2 = UIManager.getColor("SplitPane.background");
        if (color2 != null) {
            setBackground(color2);
        }
        this.f11075e = UIManager.getColor("SplitPane.highlight");
    }

    private void a() {
        int i2 = this.f11071c;
        this.f11069a.reset();
        this.f11069a.addPoint(i2, 1);
        this.f11069a.addPoint(i2 + (i2 / 2), 1 + (i2 / 2));
        this.f11069a.addPoint(2 * i2, 1);
        int i3 = i2 + (i2 / 2);
        this.f11070b.reset();
        this.f11070b.addPoint((i3 + i2) - 1, 1 + (i2 / 2));
        this.f11070b.addPoint(i3 + i2 + (i2 / 2), 1 - 1);
        this.f11070b.addPoint(i3 + (2 * i2) + 1, 1 + (i2 / 2));
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public void paint(Graphics graphics) {
        graphics.setColor(getBackground());
        graphics.fill3DRect(0, 0, getWidth(), getHeight(), true);
        graphics.setColor(this.f11075e);
        graphics.draw3DRect(0, 0, getWidth(), getHeight(), true);
        graphics.setColor(getForeground());
        if (this.f11072f) {
            graphics.fillPolygon(this.f11070b);
        }
        if (this.f11073g) {
            graphics.fillPolygon(this.f11069a);
        }
    }

    public void a(cM cMVar) {
        this.f11074d.add(cMVar);
    }

    @Override // javax.swing.JComponent, java.awt.Component
    public void setBackground(Color color) {
        super.setBackground(color);
    }

    @Override // javax.swing.JComponent, java.awt.Component
    public void setForeground(Color color) {
        super.setForeground(color);
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public Dimension getPreferredSize() {
        return new Dimension(4 * this.f11071c, 2 + (this.f11071c / 2));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void b() {
        Iterator it = this.f11074d.iterator();
        while (it.hasNext()) {
            ((cM) it.next()).b();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void c() {
        Iterator it = this.f11074d.iterator();
        while (it.hasNext()) {
            ((cM) it.next()).a();
        }
    }
}
