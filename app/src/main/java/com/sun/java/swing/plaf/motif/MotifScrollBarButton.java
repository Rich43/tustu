package com.sun.java.swing.plaf.motif;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.UIManager;
import javax.swing.plaf.basic.BasicArrowButton;

/* loaded from: rt.jar:com/sun/java/swing/plaf/motif/MotifScrollBarButton.class */
public class MotifScrollBarButton extends BasicArrowButton {
    private Color darkShadow;
    private Color lightShadow;

    public MotifScrollBarButton(int i2) {
        super(i2);
        this.darkShadow = UIManager.getColor("controlShadow");
        this.lightShadow = UIManager.getColor("controlLtHighlight");
        switch (i2) {
            case 1:
            case 3:
            case 5:
            case 7:
                this.direction = i2;
                setRequestFocusEnabled(false);
                setOpaque(true);
                setBackground(UIManager.getColor("ScrollBar.background"));
                setForeground(UIManager.getColor("ScrollBar.foreground"));
                return;
            case 2:
            case 4:
            case 6:
            default:
                throw new IllegalArgumentException("invalid direction");
        }
    }

    @Override // javax.swing.plaf.basic.BasicArrowButton, javax.swing.JComponent, java.awt.Container, java.awt.Component
    public Dimension getPreferredSize() {
        switch (this.direction) {
            case 1:
            case 5:
                return new Dimension(11, 12);
            case 2:
            case 3:
            case 4:
            case 6:
            case 7:
            default:
                return new Dimension(12, 11);
        }
    }

    @Override // javax.swing.plaf.basic.BasicArrowButton, javax.swing.JComponent, java.awt.Container, java.awt.Component
    public Dimension getMinimumSize() {
        return getPreferredSize();
    }

    @Override // javax.swing.plaf.basic.BasicArrowButton, javax.swing.JComponent, java.awt.Container, java.awt.Component
    public Dimension getMaximumSize() {
        return getPreferredSize();
    }

    @Override // javax.swing.plaf.basic.BasicArrowButton, java.awt.Component
    public boolean isFocusTraversable() {
        return false;
    }

    @Override // javax.swing.plaf.basic.BasicArrowButton, javax.swing.JComponent, java.awt.Container, java.awt.Component
    public void paint(Graphics graphics) {
        int width = getWidth();
        int height = getHeight();
        if (isOpaque()) {
            graphics.setColor(getBackground());
            graphics.fillRect(0, 0, width, height);
        }
        boolean zIsPressed = getModel().isPressed();
        Color color = zIsPressed ? this.darkShadow : this.lightShadow;
        Color color2 = zIsPressed ? this.lightShadow : this.darkShadow;
        Color background = getBackground();
        int i2 = width / 2;
        int i3 = height / 2;
        int iMin = Math.min(width, height);
        switch (this.direction) {
            case 1:
                graphics.setColor(color);
                graphics.drawLine(i2, 0, i2, 0);
                int i4 = i2 - 1;
                int i5 = 1;
                for (int i6 = 1; i6 <= iMin - 2; i6 += 2) {
                    graphics.setColor(color);
                    graphics.drawLine(i4, i6, i4, i6);
                    if (i6 >= iMin - 2) {
                        graphics.drawLine(i4, i6 + 1, i4, i6 + 1);
                    }
                    graphics.setColor(background);
                    graphics.drawLine(i4 + 1, i6, i4 + i5, i6);
                    if (i6 < iMin - 2) {
                        graphics.drawLine(i4, i6 + 1, i4 + i5 + 1, i6 + 1);
                    }
                    graphics.setColor(color2);
                    graphics.drawLine(i4 + i5 + 1, i6, i4 + i5 + 1, i6);
                    if (i6 >= iMin - 2) {
                        graphics.drawLine(i4 + 1, i6 + 1, i4 + i5 + 1, i6 + 1);
                    }
                    i5 += 2;
                    i4--;
                }
                break;
            case 3:
                graphics.setColor(color);
                graphics.drawLine(iMin, i3, iMin, i3);
                int i7 = i3 - 1;
                int i8 = 1;
                for (int i9 = iMin - 1; i9 >= 1; i9 -= 2) {
                    graphics.setColor(color);
                    graphics.drawLine(i9, i7, i9, i7);
                    if (i9 <= 2) {
                        graphics.drawLine(i9 - 1, i7, i9 - 1, i7 + i8 + 1);
                    }
                    graphics.setColor(background);
                    graphics.drawLine(i9, i7 + 1, i9, i7 + i8);
                    if (i9 > 2) {
                        graphics.drawLine(i9 - 1, i7, i9 - 1, i7 + i8 + 1);
                    }
                    graphics.setColor(color2);
                    graphics.drawLine(i9, i7 + i8 + 1, i9, i7 + i8 + 1);
                    i8 += 2;
                    i7--;
                }
                break;
            case 5:
                graphics.setColor(color2);
                graphics.drawLine(i2, iMin, i2, iMin);
                int i10 = i2 - 1;
                int i11 = 1;
                for (int i12 = iMin - 1; i12 >= 1; i12 -= 2) {
                    graphics.setColor(color);
                    graphics.drawLine(i10, i12, i10, i12);
                    if (i12 <= 2) {
                        graphics.drawLine(i10, i12 - 1, i10 + i11 + 1, i12 - 1);
                    }
                    graphics.setColor(background);
                    graphics.drawLine(i10 + 1, i12, i10 + i11, i12);
                    if (i12 > 2) {
                        graphics.drawLine(i10, i12 - 1, i10 + i11 + 1, i12 - 1);
                    }
                    graphics.setColor(color2);
                    graphics.drawLine(i10 + i11 + 1, i12, i10 + i11 + 1, i12);
                    i11 += 2;
                    i10--;
                }
                break;
            case 7:
                graphics.setColor(color2);
                graphics.drawLine(0, i3, 0, i3);
                int i13 = i3 - 1;
                int i14 = 1;
                for (int i15 = 1; i15 <= iMin - 2; i15 += 2) {
                    graphics.setColor(color);
                    graphics.drawLine(i15, i13, i15, i13);
                    if (i15 >= iMin - 2) {
                        graphics.drawLine(i15 + 1, i13, i15 + 1, i13);
                    }
                    graphics.setColor(background);
                    graphics.drawLine(i15, i13 + 1, i15, i13 + i14);
                    if (i15 < iMin - 2) {
                        graphics.drawLine(i15 + 1, i13, i15 + 1, i13 + i14 + 1);
                    }
                    graphics.setColor(color2);
                    graphics.drawLine(i15, i13 + i14 + 1, i15, i13 + i14 + 1);
                    if (i15 >= iMin - 2) {
                        graphics.drawLine(i15 + 1, i13 + 1, i15 + 1, i13 + i14 + 1);
                    }
                    i14 += 2;
                    i13--;
                }
                break;
        }
    }
}
