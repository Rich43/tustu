package com.sun.java.swing.plaf.motif;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Rectangle;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicScrollBarUI;
import sun.swing.SwingUtilities2;

/* loaded from: rt.jar:com/sun/java/swing/plaf/motif/MotifScrollBarUI.class */
public class MotifScrollBarUI extends BasicScrollBarUI {
    public static ComponentUI createUI(JComponent jComponent) {
        return new MotifScrollBarUI();
    }

    @Override // javax.swing.plaf.basic.BasicScrollBarUI, javax.swing.plaf.ComponentUI
    public Dimension getPreferredSize(JComponent jComponent) {
        Insets insets = jComponent.getInsets();
        int i2 = insets.left + insets.right;
        int i3 = insets.top + insets.bottom;
        return this.scrollbar.getOrientation() == 1 ? new Dimension(i2 + 11, i3 + 33) : new Dimension(i2 + 33, i3 + 11);
    }

    @Override // javax.swing.plaf.basic.BasicScrollBarUI
    protected JButton createDecreaseButton(int i2) {
        return new MotifScrollBarButton(i2);
    }

    @Override // javax.swing.plaf.basic.BasicScrollBarUI
    protected JButton createIncreaseButton(int i2) {
        return new MotifScrollBarButton(i2);
    }

    @Override // javax.swing.plaf.basic.BasicScrollBarUI
    public void paintTrack(Graphics graphics, JComponent jComponent, Rectangle rectangle) {
        graphics.setColor(this.trackColor);
        graphics.fillRect(rectangle.f12372x, rectangle.f12373y, rectangle.width, rectangle.height);
    }

    @Override // javax.swing.plaf.basic.BasicScrollBarUI
    public void paintThumb(Graphics graphics, JComponent jComponent, Rectangle rectangle) {
        if (rectangle.isEmpty() || !this.scrollbar.isEnabled()) {
            return;
        }
        int i2 = rectangle.width;
        int i3 = rectangle.height;
        graphics.translate(rectangle.f12372x, rectangle.f12373y);
        graphics.setColor(this.thumbColor);
        graphics.fillRect(0, 0, i2 - 1, i3 - 1);
        graphics.setColor(this.thumbHighlightColor);
        SwingUtilities2.drawVLine(graphics, 0, 0, i3 - 1);
        SwingUtilities2.drawHLine(graphics, 1, i2 - 1, 0);
        graphics.setColor(this.thumbLightShadowColor);
        SwingUtilities2.drawHLine(graphics, 1, i2 - 1, i3 - 1);
        SwingUtilities2.drawVLine(graphics, i2 - 1, 1, i3 - 2);
        graphics.translate(-rectangle.f12372x, -rectangle.f12373y);
    }
}
