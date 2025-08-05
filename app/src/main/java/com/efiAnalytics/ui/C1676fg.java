package com.efiAnalytics.ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;

/* renamed from: com.efiAnalytics.ui.fg, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/fg.class */
class C1676fg extends JPanel {

    /* renamed from: a, reason: collision with root package name */
    int f11668a;

    /* renamed from: b, reason: collision with root package name */
    int f11669b;

    /* renamed from: c, reason: collision with root package name */
    final /* synthetic */ C1672fc f11670c;

    C1676fg(C1672fc c1672fc, int i2, int i3) {
        this.f11670c = c1672fc;
        this.f11668a = i2;
        this.f11669b = i3;
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public void paint(Graphics graphics) {
        graphics.setColor(Color.BLACK);
        graphics.fillRect(0, 0, getWidth(), getHeight());
        if (!this.f11670c.a(this.f11668a, this.f11669b)) {
            graphics.setColor(Color.WHITE);
            ((Graphics2D) graphics).setStroke(new BasicStroke(1.0f));
            graphics.drawRect(0, 0, getWidth(), getHeight());
        } else {
            graphics.setColor(this.f11670c.f11661c);
            int iA = eJ.a(4);
            ((Graphics2D) graphics).setStroke(new BasicStroke(iA));
            graphics.drawRect(iA / 2, iA / 2, getWidth() - iA, getHeight() - iA);
        }
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public Dimension getPreferredSize() {
        return new Dimension(eJ.a(24), eJ.a(24));
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public Dimension getMinimumSize() {
        return new Dimension(eJ.a(24), eJ.a(24));
    }
}
