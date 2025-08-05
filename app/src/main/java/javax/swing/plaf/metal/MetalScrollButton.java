package javax.swing.plaf.metal;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.UIManager;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.basic.BasicArrowButton;

/* loaded from: rt.jar:javax/swing/plaf/metal/MetalScrollButton.class */
public class MetalScrollButton extends BasicArrowButton {
    private static Color shadowColor;
    private static Color highlightColor;
    private boolean isFreeStanding;
    private int buttonWidth;

    public MetalScrollButton(int i2, int i3, boolean z2) {
        super(i2);
        this.isFreeStanding = false;
        shadowColor = UIManager.getColor("ScrollBar.darkShadow");
        highlightColor = UIManager.getColor("ScrollBar.highlight");
        this.buttonWidth = i3;
        this.isFreeStanding = z2;
    }

    public void setFreeStanding(boolean z2) {
        this.isFreeStanding = z2;
    }

    @Override // javax.swing.plaf.basic.BasicArrowButton, javax.swing.JComponent, java.awt.Container, java.awt.Component
    public void paint(Graphics graphics) {
        boolean zIsLeftToRight = MetalUtils.isLeftToRight(this);
        boolean zIsEnabled = getParent().isEnabled();
        ColorUIResource controlInfo = zIsEnabled ? MetalLookAndFeel.getControlInfo() : MetalLookAndFeel.getControlDisabled();
        boolean zIsPressed = getModel().isPressed();
        int width = getWidth();
        int height = getHeight();
        int i2 = (height + 1) / 4;
        int i3 = (height + 1) / 2;
        if (zIsPressed) {
            graphics.setColor(MetalLookAndFeel.getControlShadow());
        } else {
            graphics.setColor(getBackground());
        }
        graphics.fillRect(0, 0, width, height);
        if (getDirection() == 1) {
            if (!this.isFreeStanding) {
                height++;
                graphics.translate(0, -1);
                width += 2;
                if (!zIsLeftToRight) {
                    graphics.translate(-1, 0);
                }
            }
            graphics.setColor(controlInfo);
            int i4 = ((height + 1) - i2) / 2;
            int i5 = width / 2;
            for (int i6 = 0; i6 < i2; i6++) {
                graphics.drawLine(i5 - i6, i4 + i6, i5 + i6 + 1, i4 + i6);
            }
            if (zIsEnabled) {
                graphics.setColor(highlightColor);
                if (!zIsPressed) {
                    graphics.drawLine(1, 1, width - 3, 1);
                    graphics.drawLine(1, 1, 1, height - 1);
                }
                graphics.drawLine(width - 1, 1, width - 1, height - 1);
                graphics.setColor(shadowColor);
                graphics.drawLine(0, 0, width - 2, 0);
                graphics.drawLine(0, 0, 0, height - 1);
                graphics.drawLine(width - 2, 2, width - 2, height - 1);
            } else {
                MetalUtils.drawDisabledBorder(graphics, 0, 0, width, height + 1);
            }
            if (!this.isFreeStanding) {
                int i7 = height - 1;
                graphics.translate(0, 1);
                int i8 = width - 2;
                if (!zIsLeftToRight) {
                    graphics.translate(1, 0);
                    return;
                }
                return;
            }
            return;
        }
        if (getDirection() == 5) {
            if (!this.isFreeStanding) {
                height++;
                width += 2;
                if (!zIsLeftToRight) {
                    graphics.translate(-1, 0);
                }
            }
            graphics.setColor(controlInfo);
            int i9 = ((((height + 1) - i2) / 2) + i2) - 1;
            int i10 = width / 2;
            for (int i11 = 0; i11 < i2; i11++) {
                graphics.drawLine(i10 - i11, i9 - i11, i10 + i11 + 1, i9 - i11);
            }
            if (zIsEnabled) {
                graphics.setColor(highlightColor);
                if (!zIsPressed) {
                    graphics.drawLine(1, 0, width - 3, 0);
                    graphics.drawLine(1, 0, 1, height - 3);
                }
                graphics.drawLine(1, height - 1, width - 1, height - 1);
                graphics.drawLine(width - 1, 0, width - 1, height - 1);
                graphics.setColor(shadowColor);
                graphics.drawLine(0, 0, 0, height - 2);
                graphics.drawLine(width - 2, 0, width - 2, height - 2);
                graphics.drawLine(2, height - 2, width - 2, height - 2);
            } else {
                MetalUtils.drawDisabledBorder(graphics, 0, -1, width, height + 1);
            }
            if (!this.isFreeStanding) {
                int i12 = height - 1;
                int i13 = width - 2;
                if (!zIsLeftToRight) {
                    graphics.translate(1, 0);
                    return;
                }
                return;
            }
            return;
        }
        if (getDirection() == 3) {
            if (!this.isFreeStanding) {
                height += 2;
                width++;
            }
            graphics.setColor(controlInfo);
            int i14 = ((((width + 1) - i2) / 2) + i2) - 1;
            int i15 = height / 2;
            for (int i16 = 0; i16 < i2; i16++) {
                graphics.drawLine(i14 - i16, i15 - i16, i14 - i16, i15 + i16 + 1);
            }
            if (zIsEnabled) {
                graphics.setColor(highlightColor);
                if (!zIsPressed) {
                    graphics.drawLine(0, 1, width - 3, 1);
                    graphics.drawLine(0, 1, 0, height - 3);
                }
                graphics.drawLine(width - 1, 1, width - 1, height - 1);
                graphics.drawLine(0, height - 1, width - 1, height - 1);
                graphics.setColor(shadowColor);
                graphics.drawLine(0, 0, width - 2, 0);
                graphics.drawLine(width - 2, 2, width - 2, height - 2);
                graphics.drawLine(0, height - 2, width - 2, height - 2);
            } else {
                MetalUtils.drawDisabledBorder(graphics, -1, 0, width + 1, height);
            }
            if (!this.isFreeStanding) {
                int i17 = height - 2;
                int i18 = width - 1;
                return;
            }
            return;
        }
        if (getDirection() == 7) {
            if (!this.isFreeStanding) {
                height += 2;
                width++;
                graphics.translate(-1, 0);
            }
            graphics.setColor(controlInfo);
            int i19 = ((width + 1) - i2) / 2;
            int i20 = height / 2;
            for (int i21 = 0; i21 < i2; i21++) {
                graphics.drawLine(i19 + i21, i20 - i21, i19 + i21, i20 + i21 + 1);
            }
            if (zIsEnabled) {
                graphics.setColor(highlightColor);
                if (!zIsPressed) {
                    graphics.drawLine(1, 1, width - 1, 1);
                    graphics.drawLine(1, 1, 1, height - 3);
                }
                graphics.drawLine(1, height - 1, width - 1, height - 1);
                graphics.setColor(shadowColor);
                graphics.drawLine(0, 0, width - 1, 0);
                graphics.drawLine(0, 0, 0, height - 2);
                graphics.drawLine(2, height - 2, width - 1, height - 2);
            } else {
                MetalUtils.drawDisabledBorder(graphics, 0, 0, width + 1, height);
            }
            if (!this.isFreeStanding) {
                int i22 = height - 2;
                int i23 = width - 1;
                graphics.translate(1, 0);
            }
        }
    }

    @Override // javax.swing.plaf.basic.BasicArrowButton, javax.swing.JComponent, java.awt.Container, java.awt.Component
    public Dimension getPreferredSize() {
        if (getDirection() == 1) {
            return new Dimension(this.buttonWidth, this.buttonWidth - 2);
        }
        if (getDirection() == 5) {
            return new Dimension(this.buttonWidth, this.buttonWidth - (this.isFreeStanding ? 1 : 2));
        }
        if (getDirection() == 3) {
            return new Dimension(this.buttonWidth - (this.isFreeStanding ? 1 : 2), this.buttonWidth);
        }
        if (getDirection() == 7) {
            return new Dimension(this.buttonWidth - 2, this.buttonWidth);
        }
        return new Dimension(0, 0);
    }

    @Override // javax.swing.plaf.basic.BasicArrowButton, javax.swing.JComponent, java.awt.Container, java.awt.Component
    public Dimension getMinimumSize() {
        return getPreferredSize();
    }

    @Override // javax.swing.plaf.basic.BasicArrowButton, javax.swing.JComponent, java.awt.Container, java.awt.Component
    public Dimension getMaximumSize() {
        return new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE);
    }

    public int getButtonWidth() {
        return this.buttonWidth;
    }
}
