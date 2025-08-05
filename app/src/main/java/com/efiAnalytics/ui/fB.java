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

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/fB.class */
public class fB extends JPanel {

    /* renamed from: e, reason: collision with root package name */
    Color f11628e;

    /* renamed from: c, reason: collision with root package name */
    int f11624c = 10;

    /* renamed from: f, reason: collision with root package name */
    private boolean f11625f = true;

    /* renamed from: g, reason: collision with root package name */
    private boolean f11626g = true;

    /* renamed from: d, reason: collision with root package name */
    List f11627d = new ArrayList();

    /* renamed from: a, reason: collision with root package name */
    Polygon f11622a = new Polygon();

    /* renamed from: b, reason: collision with root package name */
    Polygon f11623b = new Polygon();

    public fB() {
        this.f11628e = null;
        a();
        addMouseListener(new fC(this));
        Color color = UIManager.getColor("Button.foreground");
        if (color != null) {
            setForeground(color);
        }
        Color color2 = UIManager.getColor("SplitPane.background");
        if (color2 != null) {
            setBackground(color2);
        }
        this.f11628e = UIManager.getColor("SplitPane.highlight");
    }

    private void a() {
        int i2 = this.f11624c;
        this.f11622a.reset();
        this.f11622a.addPoint(1 + (i2 / 2), i2);
        this.f11622a.addPoint(1, i2 + (i2 / 2));
        this.f11622a.addPoint(1 + (i2 / 2), 2 * i2);
        int i3 = i2 + (i2 / 2);
        this.f11623b.reset();
        this.f11623b.addPoint(1, i3 + i2);
        this.f11623b.addPoint(1 + (i2 / 2), i3 + i2 + (i2 / 2));
        this.f11623b.addPoint(1, i3 + (2 * i2));
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public void paint(Graphics graphics) {
        graphics.setColor(getBackground());
        graphics.fill3DRect(0, 0, getWidth(), getHeight(), true);
        graphics.setColor(this.f11628e);
        graphics.draw3DRect(0, 0, getWidth(), getHeight(), true);
        graphics.setColor(getForeground());
        if (this.f11625f) {
            graphics.fillPolygon(this.f11623b);
        }
        if (this.f11626g) {
            graphics.fillPolygon(this.f11622a);
        }
    }

    public void a(fD fDVar) {
        this.f11627d.add(fDVar);
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public Dimension getPreferredSize() {
        return new Dimension(2 + (this.f11624c / 2), 4 * this.f11624c);
    }

    @Override // javax.swing.JComponent, java.awt.Component
    public void setBackground(Color color) {
        super.setBackground(color);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void b() {
        Iterator it = this.f11627d.iterator();
        while (it.hasNext()) {
            ((fD) it.next()).a();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void c() {
        Iterator it = this.f11627d.iterator();
        while (it.hasNext()) {
            ((fD) it.next()).b();
        }
    }
}
