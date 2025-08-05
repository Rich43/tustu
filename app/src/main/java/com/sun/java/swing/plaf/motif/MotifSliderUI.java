package com.sun.java.swing.plaf.motif;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import javax.swing.JComponent;
import javax.swing.JSlider;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicSliderUI;
import sun.swing.SwingUtilities2;

/* loaded from: rt.jar:com/sun/java/swing/plaf/motif/MotifSliderUI.class */
public class MotifSliderUI extends BasicSliderUI {
    static final Dimension PREFERRED_HORIZONTAL_SIZE = new Dimension(164, 15);
    static final Dimension PREFERRED_VERTICAL_SIZE = new Dimension(15, 164);
    static final Dimension MINIMUM_HORIZONTAL_SIZE = new Dimension(43, 15);
    static final Dimension MINIMUM_VERTICAL_SIZE = new Dimension(15, 43);

    public MotifSliderUI(JSlider jSlider) {
        super(jSlider);
    }

    public static ComponentUI createUI(JComponent jComponent) {
        return new MotifSliderUI((JSlider) jComponent);
    }

    @Override // javax.swing.plaf.basic.BasicSliderUI
    public Dimension getPreferredHorizontalSize() {
        return PREFERRED_HORIZONTAL_SIZE;
    }

    @Override // javax.swing.plaf.basic.BasicSliderUI
    public Dimension getPreferredVerticalSize() {
        return PREFERRED_VERTICAL_SIZE;
    }

    @Override // javax.swing.plaf.basic.BasicSliderUI
    public Dimension getMinimumHorizontalSize() {
        return MINIMUM_HORIZONTAL_SIZE;
    }

    @Override // javax.swing.plaf.basic.BasicSliderUI
    public Dimension getMinimumVerticalSize() {
        return MINIMUM_VERTICAL_SIZE;
    }

    @Override // javax.swing.plaf.basic.BasicSliderUI
    protected Dimension getThumbSize() {
        if (this.slider.getOrientation() == 0) {
            return new Dimension(30, 15);
        }
        return new Dimension(15, 30);
    }

    @Override // javax.swing.plaf.basic.BasicSliderUI
    public void paintFocus(Graphics graphics) {
    }

    @Override // javax.swing.plaf.basic.BasicSliderUI
    public void paintTrack(Graphics graphics) {
    }

    @Override // javax.swing.plaf.basic.BasicSliderUI
    public void paintThumb(Graphics graphics) {
        Rectangle rectangle = this.thumbRect;
        int i2 = rectangle.f12372x;
        int i3 = rectangle.f12373y;
        int i4 = rectangle.width;
        int i5 = rectangle.height;
        if (this.slider.isEnabled()) {
            graphics.setColor(this.slider.getForeground());
        } else {
            graphics.setColor(this.slider.getForeground().darker());
        }
        if (this.slider.getOrientation() == 0) {
            graphics.translate(i2, rectangle.f12373y - 1);
            graphics.fillRect(0, 1, i4, i5 - 1);
            graphics.setColor(getHighlightColor());
            SwingUtilities2.drawHLine(graphics, 0, i4 - 1, 1);
            SwingUtilities2.drawVLine(graphics, 0, 1, i5);
            SwingUtilities2.drawVLine(graphics, i4 / 2, 2, i5 - 1);
            graphics.setColor(getShadowColor());
            SwingUtilities2.drawHLine(graphics, 0, i4 - 1, i5);
            SwingUtilities2.drawVLine(graphics, i4 - 1, 1, i5);
            SwingUtilities2.drawVLine(graphics, (i4 / 2) - 1, 2, i5);
            graphics.translate(-i2, -(rectangle.f12373y - 1));
            return;
        }
        graphics.translate(rectangle.f12372x - 1, 0);
        graphics.fillRect(1, i3, i4 - 1, i5);
        graphics.setColor(getHighlightColor());
        SwingUtilities2.drawHLine(graphics, 1, i4, i3);
        SwingUtilities2.drawVLine(graphics, 1, i3 + 1, (i3 + i5) - 1);
        SwingUtilities2.drawHLine(graphics, 2, i4 - 1, i3 + (i5 / 2));
        graphics.setColor(getShadowColor());
        SwingUtilities2.drawHLine(graphics, 2, i4, (i3 + i5) - 1);
        SwingUtilities2.drawVLine(graphics, i4, (i3 + i5) - 1, i3);
        SwingUtilities2.drawHLine(graphics, 2, i4 - 1, (i3 + (i5 / 2)) - 1);
        graphics.translate(-(rectangle.f12372x - 1), 0);
    }
}
