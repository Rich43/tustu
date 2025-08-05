package javax.swing.plaf.basic;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.plaf.UIResource;

/* loaded from: rt.jar:javax/swing/plaf/basic/BasicArrowButton.class */
public class BasicArrowButton extends JButton implements SwingConstants {
    protected int direction;
    private Color shadow;
    private Color darkShadow;
    private Color highlight;

    public BasicArrowButton(int i2, Color color, Color color2, Color color3, Color color4) {
        setRequestFocusEnabled(false);
        setDirection(i2);
        setBackground(color);
        this.shadow = color2;
        this.darkShadow = color3;
        this.highlight = color4;
    }

    public BasicArrowButton(int i2) {
        this(i2, UIManager.getColor("control"), UIManager.getColor("controlShadow"), UIManager.getColor("controlDkShadow"), UIManager.getColor("controlLtHighlight"));
    }

    public int getDirection() {
        return this.direction;
    }

    public void setDirection(int i2) {
        this.direction = i2;
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public void paint(Graphics graphics) {
        int i2 = getSize().width;
        int i3 = getSize().height;
        Color color = graphics.getColor();
        boolean zIsPressed = getModel().isPressed();
        boolean zIsEnabled = isEnabled();
        graphics.setColor(getBackground());
        graphics.fillRect(1, 1, i2 - 2, i3 - 2);
        if (getBorder() != null && !(getBorder() instanceof UIResource)) {
            paintBorder(graphics);
        } else if (zIsPressed) {
            graphics.setColor(this.shadow);
            graphics.drawRect(0, 0, i2 - 1, i3 - 1);
        } else {
            graphics.drawLine(0, 0, 0, i3 - 1);
            graphics.drawLine(1, 0, i2 - 2, 0);
            graphics.setColor(this.highlight);
            graphics.drawLine(1, 1, 1, i3 - 3);
            graphics.drawLine(2, 1, i2 - 3, 1);
            graphics.setColor(this.shadow);
            graphics.drawLine(1, i3 - 2, i2 - 2, i3 - 2);
            graphics.drawLine(i2 - 2, 1, i2 - 2, i3 - 3);
            graphics.setColor(this.darkShadow);
            graphics.drawLine(0, i3 - 1, i2 - 1, i3 - 1);
            graphics.drawLine(i2 - 1, i3 - 1, i2 - 1, 0);
        }
        if (i3 < 5 || i2 < 5) {
            graphics.setColor(color);
            return;
        }
        if (zIsPressed) {
            graphics.translate(1, 1);
        }
        int iMax = Math.max(Math.min((i3 - 4) / 3, (i2 - 4) / 3), 2);
        paintTriangle(graphics, (i2 - iMax) / 2, (i3 - iMax) / 2, iMax, this.direction, zIsEnabled);
        if (zIsPressed) {
            graphics.translate(-1, -1);
        }
        graphics.setColor(color);
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public Dimension getPreferredSize() {
        return new Dimension(16, 16);
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public Dimension getMinimumSize() {
        return new Dimension(5, 5);
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public Dimension getMaximumSize() {
        return new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE);
    }

    @Override // java.awt.Component
    public boolean isFocusTraversable() {
        return false;
    }

    public void paintTriangle(Graphics graphics, int i2, int i3, int i4, int i5, boolean z2) {
        Color color = graphics.getColor();
        int i6 = 0;
        int iMax = Math.max(i4, 2);
        int i7 = (iMax / 2) - 1;
        graphics.translate(i2, i3);
        if (z2) {
            graphics.setColor(this.darkShadow);
        } else {
            graphics.setColor(this.shadow);
        }
        switch (i5) {
            case 1:
                int i8 = 0;
                while (i8 < iMax) {
                    graphics.drawLine(i7 - i8, i8, i7 + i8, i8);
                    i8++;
                }
                if (!z2) {
                    graphics.setColor(this.highlight);
                    graphics.drawLine((i7 - i8) + 2, i8, i7 + i8, i8);
                    break;
                }
                break;
            case 3:
                if (!z2) {
                    graphics.translate(1, 1);
                    graphics.setColor(this.highlight);
                    for (int i9 = iMax - 1; i9 >= 0; i9--) {
                        graphics.drawLine(i6, i7 - i9, i6, i7 + i9);
                        i6++;
                    }
                    graphics.translate(-1, -1);
                    graphics.setColor(this.shadow);
                }
                int i10 = 0;
                for (int i11 = iMax - 1; i11 >= 0; i11--) {
                    graphics.drawLine(i10, i7 - i11, i10, i7 + i11);
                    i10++;
                }
                break;
            case 5:
                if (!z2) {
                    graphics.translate(1, 1);
                    graphics.setColor(this.highlight);
                    for (int i12 = iMax - 1; i12 >= 0; i12--) {
                        graphics.drawLine(i7 - i12, i6, i7 + i12, i6);
                        i6++;
                    }
                    graphics.translate(-1, -1);
                    graphics.setColor(this.shadow);
                }
                int i13 = 0;
                for (int i14 = iMax - 1; i14 >= 0; i14--) {
                    graphics.drawLine(i7 - i14, i13, i7 + i14, i13);
                    i13++;
                }
                break;
            case 7:
                int i15 = 0;
                while (i15 < iMax) {
                    graphics.drawLine(i15, i7 - i15, i15, i7 + i15);
                    i15++;
                }
                if (!z2) {
                    graphics.setColor(this.highlight);
                    graphics.drawLine(i15, (i7 - i15) + 2, i15, i7 + i15);
                    break;
                }
                break;
        }
        graphics.translate(-i2, -i3);
        graphics.setColor(color);
    }
}
