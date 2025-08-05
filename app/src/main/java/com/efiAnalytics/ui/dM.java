package com.efiAnalytics.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JComponent;

/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/dM.class */
public class dM extends JComponent {

    /* renamed from: a, reason: collision with root package name */
    long f11344a = 0;

    /* renamed from: b, reason: collision with root package name */
    long f11345b = 800;

    /* renamed from: c, reason: collision with root package name */
    double f11346c = 0.0d;

    /* renamed from: d, reason: collision with root package name */
    int f11347d = 4;

    /* renamed from: e, reason: collision with root package name */
    int f11348e = 4;

    /* renamed from: f, reason: collision with root package name */
    boolean f11349f = false;

    /* renamed from: g, reason: collision with root package name */
    Color f11350g = Color.red;

    public dM() {
        dN dNVar = new dN(this);
        addMouseListener(dNVar);
        addMouseMotionListener(dNVar);
    }

    private int a() {
        return (getWidth() - this.f11348e) - this.f11347d;
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public void paint(Graphics graphics) {
        graphics.setColor(getBackground());
        graphics.fillRect(0, 0, getWidth(), getHeight());
        graphics.setColor(new Color(10, 120, 253));
        graphics.fill3DRect(this.f11348e, 5, ((int) (a() * this.f11346c)) + 1, getHeight() - 10, true);
        graphics.setColor(Color.gray);
        graphics.draw3DRect(1, 2, getWidth() - 3, getHeight() - 5, false);
    }

    public void a(double d2) {
        this.f11346c = d2;
        repaint();
    }

    public void b(double d2) {
        if (System.currentTimeMillis() - this.f11344a > this.f11345b) {
            this.f11346c = d2;
            b();
        }
    }

    private void b() {
        repaint(30L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a(int i2) {
        double d2 = i2 - this.f11348e;
        int iA = a();
        if (d2 / iA < 0.0d || d2 / iA <= 1.0d) {
        }
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public Dimension getMinimumSize() {
        return new Dimension(50, 15);
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public Dimension getPreferredSize() {
        return new Dimension(150, 20);
    }
}
