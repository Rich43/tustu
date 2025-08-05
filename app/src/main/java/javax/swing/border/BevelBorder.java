package javax.swing.border;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;
import java.beans.ConstructorProperties;

/* loaded from: rt.jar:javax/swing/border/BevelBorder.class */
public class BevelBorder extends AbstractBorder {
    public static final int RAISED = 0;
    public static final int LOWERED = 1;
    protected int bevelType;
    protected Color highlightOuter;
    protected Color highlightInner;
    protected Color shadowInner;
    protected Color shadowOuter;

    public BevelBorder(int i2) {
        this.bevelType = i2;
    }

    public BevelBorder(int i2, Color color, Color color2) {
        this(i2, color.brighter(), color, color2, color2.brighter());
    }

    @ConstructorProperties({"bevelType", "highlightOuterColor", "highlightInnerColor", "shadowOuterColor", "shadowInnerColor"})
    public BevelBorder(int i2, Color color, Color color2, Color color3, Color color4) {
        this(i2);
        this.highlightOuter = color;
        this.highlightInner = color2;
        this.shadowOuter = color3;
        this.shadowInner = color4;
    }

    @Override // javax.swing.border.AbstractBorder, javax.swing.border.Border
    public void paintBorder(Component component, Graphics graphics, int i2, int i3, int i4, int i5) {
        if (this.bevelType == 0) {
            paintRaisedBevel(component, graphics, i2, i3, i4, i5);
        } else if (this.bevelType == 1) {
            paintLoweredBevel(component, graphics, i2, i3, i4, i5);
        }
    }

    @Override // javax.swing.border.AbstractBorder
    public Insets getBorderInsets(Component component, Insets insets) {
        insets.set(2, 2, 2, 2);
        return insets;
    }

    public Color getHighlightOuterColor(Component component) {
        Color highlightOuterColor = getHighlightOuterColor();
        return highlightOuterColor != null ? highlightOuterColor : component.getBackground().brighter().brighter();
    }

    public Color getHighlightInnerColor(Component component) {
        Color highlightInnerColor = getHighlightInnerColor();
        return highlightInnerColor != null ? highlightInnerColor : component.getBackground().brighter();
    }

    public Color getShadowInnerColor(Component component) {
        Color shadowInnerColor = getShadowInnerColor();
        return shadowInnerColor != null ? shadowInnerColor : component.getBackground().darker();
    }

    public Color getShadowOuterColor(Component component) {
        Color shadowOuterColor = getShadowOuterColor();
        return shadowOuterColor != null ? shadowOuterColor : component.getBackground().darker().darker();
    }

    public Color getHighlightOuterColor() {
        return this.highlightOuter;
    }

    public Color getHighlightInnerColor() {
        return this.highlightInner;
    }

    public Color getShadowInnerColor() {
        return this.shadowInner;
    }

    public Color getShadowOuterColor() {
        return this.shadowOuter;
    }

    public int getBevelType() {
        return this.bevelType;
    }

    @Override // javax.swing.border.AbstractBorder, javax.swing.border.Border
    public boolean isBorderOpaque() {
        return true;
    }

    protected void paintRaisedBevel(Component component, Graphics graphics, int i2, int i3, int i4, int i5) {
        Color color = graphics.getColor();
        graphics.translate(i2, i3);
        graphics.setColor(getHighlightOuterColor(component));
        graphics.drawLine(0, 0, 0, i5 - 2);
        graphics.drawLine(1, 0, i4 - 2, 0);
        graphics.setColor(getHighlightInnerColor(component));
        graphics.drawLine(1, 1, 1, i5 - 3);
        graphics.drawLine(2, 1, i4 - 3, 1);
        graphics.setColor(getShadowOuterColor(component));
        graphics.drawLine(0, i5 - 1, i4 - 1, i5 - 1);
        graphics.drawLine(i4 - 1, 0, i4 - 1, i5 - 2);
        graphics.setColor(getShadowInnerColor(component));
        graphics.drawLine(1, i5 - 2, i4 - 2, i5 - 2);
        graphics.drawLine(i4 - 2, 1, i4 - 2, i5 - 3);
        graphics.translate(-i2, -i3);
        graphics.setColor(color);
    }

    protected void paintLoweredBevel(Component component, Graphics graphics, int i2, int i3, int i4, int i5) {
        Color color = graphics.getColor();
        graphics.translate(i2, i3);
        graphics.setColor(getShadowInnerColor(component));
        graphics.drawLine(0, 0, 0, i5 - 1);
        graphics.drawLine(1, 0, i4 - 1, 0);
        graphics.setColor(getShadowOuterColor(component));
        graphics.drawLine(1, 1, 1, i5 - 2);
        graphics.drawLine(2, 1, i4 - 2, 1);
        graphics.setColor(getHighlightOuterColor(component));
        graphics.drawLine(1, i5 - 1, i4 - 1, i5 - 1);
        graphics.drawLine(i4 - 1, 1, i4 - 1, i5 - 2);
        graphics.setColor(getHighlightInnerColor(component));
        graphics.drawLine(2, i5 - 2, i4 - 2, i5 - 2);
        graphics.drawLine(i4 - 2, 2, i4 - 2, i5 - 3);
        graphics.translate(-i2, -i3);
        graphics.setColor(color);
    }
}
