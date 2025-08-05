package javax.swing.border;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;
import java.beans.ConstructorProperties;

/* loaded from: rt.jar:javax/swing/border/SoftBevelBorder.class */
public class SoftBevelBorder extends BevelBorder {
    public SoftBevelBorder(int i2) {
        super(i2);
    }

    public SoftBevelBorder(int i2, Color color, Color color2) {
        super(i2, color, color2);
    }

    @ConstructorProperties({"bevelType", "highlightOuterColor", "highlightInnerColor", "shadowOuterColor", "shadowInnerColor"})
    public SoftBevelBorder(int i2, Color color, Color color2, Color color3, Color color4) {
        super(i2, color, color2, color3, color4);
    }

    @Override // javax.swing.border.BevelBorder, javax.swing.border.AbstractBorder, javax.swing.border.Border
    public void paintBorder(Component component, Graphics graphics, int i2, int i3, int i4, int i5) {
        Color color = graphics.getColor();
        graphics.translate(i2, i3);
        if (this.bevelType == 0) {
            graphics.setColor(getHighlightOuterColor(component));
            graphics.drawLine(0, 0, i4 - 2, 0);
            graphics.drawLine(0, 0, 0, i5 - 2);
            graphics.drawLine(1, 1, 1, 1);
            graphics.setColor(getHighlightInnerColor(component));
            graphics.drawLine(2, 1, i4 - 2, 1);
            graphics.drawLine(1, 2, 1, i5 - 2);
            graphics.drawLine(2, 2, 2, 2);
            graphics.drawLine(0, i5 - 1, 0, i5 - 2);
            graphics.drawLine(i4 - 1, 0, i4 - 1, 0);
            graphics.setColor(getShadowOuterColor(component));
            graphics.drawLine(2, i5 - 1, i4 - 1, i5 - 1);
            graphics.drawLine(i4 - 1, 2, i4 - 1, i5 - 1);
            graphics.setColor(getShadowInnerColor(component));
            graphics.drawLine(i4 - 2, i5 - 2, i4 - 2, i5 - 2);
        } else if (this.bevelType == 1) {
            graphics.setColor(getShadowOuterColor(component));
            graphics.drawLine(0, 0, i4 - 2, 0);
            graphics.drawLine(0, 0, 0, i5 - 2);
            graphics.drawLine(1, 1, 1, 1);
            graphics.setColor(getShadowInnerColor(component));
            graphics.drawLine(2, 1, i4 - 2, 1);
            graphics.drawLine(1, 2, 1, i5 - 2);
            graphics.drawLine(2, 2, 2, 2);
            graphics.drawLine(0, i5 - 1, 0, i5 - 2);
            graphics.drawLine(i4 - 1, 0, i4 - 1, 0);
            graphics.setColor(getHighlightOuterColor(component));
            graphics.drawLine(2, i5 - 1, i4 - 1, i5 - 1);
            graphics.drawLine(i4 - 1, 2, i4 - 1, i5 - 1);
            graphics.setColor(getHighlightInnerColor(component));
            graphics.drawLine(i4 - 2, i5 - 2, i4 - 2, i5 - 2);
        }
        graphics.translate(-i2, -i3);
        graphics.setColor(color);
    }

    @Override // javax.swing.border.BevelBorder, javax.swing.border.AbstractBorder
    public Insets getBorderInsets(Component component, Insets insets) {
        insets.set(3, 3, 3, 3);
        return insets;
    }

    @Override // javax.swing.border.BevelBorder, javax.swing.border.AbstractBorder, javax.swing.border.Border
    public boolean isBorderOpaque() {
        return false;
    }
}
