package javax.swing.border;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Rectangle;
import java.io.Serializable;

/* loaded from: rt.jar:javax/swing/border/AbstractBorder.class */
public abstract class AbstractBorder implements Border, Serializable {
    @Override // javax.swing.border.Border
    public void paintBorder(Component component, Graphics graphics, int i2, int i3, int i4, int i5) {
    }

    @Override // javax.swing.border.Border
    public Insets getBorderInsets(Component component) {
        return getBorderInsets(component, new Insets(0, 0, 0, 0));
    }

    public Insets getBorderInsets(Component component, Insets insets) {
        insets.bottom = 0;
        insets.right = 0;
        insets.top = 0;
        insets.left = 0;
        return insets;
    }

    @Override // javax.swing.border.Border
    public boolean isBorderOpaque() {
        return false;
    }

    public Rectangle getInteriorRectangle(Component component, int i2, int i3, int i4, int i5) {
        return getInteriorRectangle(component, this, i2, i3, i4, i5);
    }

    public static Rectangle getInteriorRectangle(Component component, Border border, int i2, int i3, int i4, int i5) {
        Insets insets;
        if (border != null) {
            insets = border.getBorderInsets(component);
        } else {
            insets = new Insets(0, 0, 0, 0);
        }
        return new Rectangle(i2 + insets.left, i3 + insets.top, (i4 - insets.right) - insets.left, (i5 - insets.top) - insets.bottom);
    }

    public int getBaseline(Component component, int i2, int i3) {
        if (i2 < 0 || i3 < 0) {
            throw new IllegalArgumentException("Width and height must be >= 0");
        }
        return -1;
    }

    public Component.BaselineResizeBehavior getBaselineResizeBehavior(Component component) {
        if (component == null) {
            throw new NullPointerException("Component must be non-null");
        }
        return Component.BaselineResizeBehavior.OTHER;
    }

    static boolean isLeftToRight(Component component) {
        return component.getComponentOrientation().isLeftToRight();
    }
}
