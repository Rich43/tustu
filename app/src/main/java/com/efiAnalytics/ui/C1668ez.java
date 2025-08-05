package com.efiAnalytics.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Polygon;
import javax.swing.JButton;

/* renamed from: com.efiAnalytics.ui.ez, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/ez.class */
class C1668ez extends JButton {

    /* renamed from: a, reason: collision with root package name */
    Dimension f11615a = new Dimension(14, 8);

    /* renamed from: b, reason: collision with root package name */
    int f11616b;

    /* renamed from: c, reason: collision with root package name */
    final /* synthetic */ C1665ew f11617c;

    C1668ez(C1665ew c1665ew, int i2) {
        this.f11617c = c1665ew;
        this.f11616b = 1;
        this.f11616b = i2;
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public void paint(Graphics graphics) {
        super.paint(graphics);
        if (isEnabled()) {
            graphics.setColor(Color.BLACK);
        } else {
            graphics.setColor(Color.GRAY);
        }
        Polygon polygon = new Polygon();
        if (this.f11616b == 1) {
            polygon.addPoint(getWidth() / 2, getHeight() / 3);
            polygon.addPoint((getWidth() * 2) / 3, (getHeight() * 2) / 3);
            polygon.addPoint(getWidth() / 3, (getHeight() * 2) / 3);
        } else {
            polygon.addPoint(getWidth() / 2, (getHeight() * 2) / 3);
            polygon.addPoint((getWidth() * 2) / 3, getHeight() / 3);
            polygon.addPoint(getWidth() / 3, getHeight() / 3);
        }
        graphics.fillPolygon(polygon);
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public Dimension getPreferredSize() {
        return this.f11615a;
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public Dimension getMinimumSize() {
        return this.f11615a;
    }
}
