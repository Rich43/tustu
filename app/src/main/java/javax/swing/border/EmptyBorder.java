package javax.swing.border;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;
import java.beans.ConstructorProperties;
import java.io.Serializable;

/* loaded from: rt.jar:javax/swing/border/EmptyBorder.class */
public class EmptyBorder extends AbstractBorder implements Serializable {
    protected int left;
    protected int right;
    protected int top;
    protected int bottom;

    public EmptyBorder(int i2, int i3, int i4, int i5) {
        this.top = i2;
        this.right = i5;
        this.bottom = i4;
        this.left = i3;
    }

    @ConstructorProperties({"borderInsets"})
    public EmptyBorder(Insets insets) {
        this.top = insets.top;
        this.right = insets.right;
        this.bottom = insets.bottom;
        this.left = insets.left;
    }

    @Override // javax.swing.border.AbstractBorder, javax.swing.border.Border
    public void paintBorder(Component component, Graphics graphics, int i2, int i3, int i4, int i5) {
    }

    @Override // javax.swing.border.AbstractBorder
    public Insets getBorderInsets(Component component, Insets insets) {
        insets.left = this.left;
        insets.top = this.top;
        insets.right = this.right;
        insets.bottom = this.bottom;
        return insets;
    }

    public Insets getBorderInsets() {
        return new Insets(this.top, this.left, this.bottom, this.right);
    }

    @Override // javax.swing.border.AbstractBorder, javax.swing.border.Border
    public boolean isBorderOpaque() {
        return false;
    }
}
