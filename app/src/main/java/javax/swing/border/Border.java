package javax.swing.border;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;

/* loaded from: rt.jar:javax/swing/border/Border.class */
public interface Border {
    void paintBorder(Component component, Graphics graphics, int i2, int i3, int i4, int i5);

    Insets getBorderInsets(Component component);

    boolean isBorderOpaque();
}
