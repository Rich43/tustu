package javax.swing.border;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;
import java.beans.ConstructorProperties;

/* loaded from: rt.jar:javax/swing/border/CompoundBorder.class */
public class CompoundBorder extends AbstractBorder {
    protected Border outsideBorder;
    protected Border insideBorder;

    public CompoundBorder() {
        this.outsideBorder = null;
        this.insideBorder = null;
    }

    @ConstructorProperties({"outsideBorder", "insideBorder"})
    public CompoundBorder(Border border, Border border2) {
        this.outsideBorder = border;
        this.insideBorder = border2;
    }

    @Override // javax.swing.border.AbstractBorder, javax.swing.border.Border
    public boolean isBorderOpaque() {
        return (this.outsideBorder == null || this.outsideBorder.isBorderOpaque()) && (this.insideBorder == null || this.insideBorder.isBorderOpaque());
    }

    @Override // javax.swing.border.AbstractBorder, javax.swing.border.Border
    public void paintBorder(Component component, Graphics graphics, int i2, int i3, int i4, int i5) {
        int i6 = i2;
        int i7 = i3;
        int i8 = i4;
        int i9 = i5;
        if (this.outsideBorder != null) {
            this.outsideBorder.paintBorder(component, graphics, i6, i7, i8, i9);
            Insets borderInsets = this.outsideBorder.getBorderInsets(component);
            i6 += borderInsets.left;
            i7 += borderInsets.top;
            i8 = (i8 - borderInsets.right) - borderInsets.left;
            i9 = (i9 - borderInsets.bottom) - borderInsets.top;
        }
        if (this.insideBorder != null) {
            this.insideBorder.paintBorder(component, graphics, i6, i7, i8, i9);
        }
    }

    @Override // javax.swing.border.AbstractBorder
    public Insets getBorderInsets(Component component, Insets insets) {
        insets.bottom = 0;
        insets.right = 0;
        insets.left = 0;
        insets.top = 0;
        if (this.outsideBorder != null) {
            Insets borderInsets = this.outsideBorder.getBorderInsets(component);
            insets.top += borderInsets.top;
            insets.left += borderInsets.left;
            insets.right += borderInsets.right;
            insets.bottom += borderInsets.bottom;
        }
        if (this.insideBorder != null) {
            Insets borderInsets2 = this.insideBorder.getBorderInsets(component);
            insets.top += borderInsets2.top;
            insets.left += borderInsets2.left;
            insets.right += borderInsets2.right;
            insets.bottom += borderInsets2.bottom;
        }
        return insets;
    }

    public Border getOutsideBorder() {
        return this.outsideBorder;
    }

    public Border getInsideBorder() {
        return this.insideBorder;
    }
}
