package javax.swing.border;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;
import java.beans.ConstructorProperties;

/* loaded from: rt.jar:javax/swing/border/EtchedBorder.class */
public class EtchedBorder extends AbstractBorder {
    public static final int RAISED = 0;
    public static final int LOWERED = 1;
    protected int etchType;
    protected Color highlight;
    protected Color shadow;

    public EtchedBorder() {
        this(1);
    }

    public EtchedBorder(int i2) {
        this(i2, null, null);
    }

    public EtchedBorder(Color color, Color color2) {
        this(1, color, color2);
    }

    @ConstructorProperties({"etchType", "highlightColor", "shadowColor"})
    public EtchedBorder(int i2, Color color, Color color2) {
        this.etchType = i2;
        this.highlight = color;
        this.shadow = color2;
    }

    @Override // javax.swing.border.AbstractBorder, javax.swing.border.Border
    public void paintBorder(Component component, Graphics graphics, int i2, int i3, int i4, int i5) {
        graphics.translate(i2, i3);
        graphics.setColor(this.etchType == 1 ? getShadowColor(component) : getHighlightColor(component));
        graphics.drawRect(0, 0, i4 - 2, i5 - 2);
        graphics.setColor(this.etchType == 1 ? getHighlightColor(component) : getShadowColor(component));
        graphics.drawLine(1, i5 - 3, 1, 1);
        graphics.drawLine(1, 1, i4 - 3, 1);
        graphics.drawLine(0, i5 - 1, i4 - 1, i5 - 1);
        graphics.drawLine(i4 - 1, i5 - 1, i4 - 1, 0);
        graphics.translate(-i2, -i3);
    }

    @Override // javax.swing.border.AbstractBorder
    public Insets getBorderInsets(Component component, Insets insets) {
        insets.set(2, 2, 2, 2);
        return insets;
    }

    @Override // javax.swing.border.AbstractBorder, javax.swing.border.Border
    public boolean isBorderOpaque() {
        return true;
    }

    public int getEtchType() {
        return this.etchType;
    }

    public Color getHighlightColor(Component component) {
        return this.highlight != null ? this.highlight : component.getBackground().brighter();
    }

    public Color getHighlightColor() {
        return this.highlight;
    }

    public Color getShadowColor(Component component) {
        return this.shadow != null ? this.shadow : component.getBackground().darker();
    }

    public Color getShadowColor() {
        return this.shadow;
    }
}
