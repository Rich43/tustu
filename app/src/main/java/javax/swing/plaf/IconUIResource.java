package javax.swing.plaf;

import java.awt.Component;
import java.awt.Graphics;
import java.io.Serializable;
import javax.swing.Icon;

/* loaded from: rt.jar:javax/swing/plaf/IconUIResource.class */
public class IconUIResource implements Icon, UIResource, Serializable {
    private Icon delegate;

    public IconUIResource(Icon icon) {
        if (icon == null) {
            throw new IllegalArgumentException("null delegate icon argument");
        }
        this.delegate = icon;
    }

    @Override // javax.swing.Icon
    public void paintIcon(Component component, Graphics graphics, int i2, int i3) {
        this.delegate.paintIcon(component, graphics, i2, i3);
    }

    @Override // javax.swing.Icon
    public int getIconWidth() {
        return this.delegate.getIconWidth();
    }

    @Override // javax.swing.Icon
    public int getIconHeight() {
        return this.delegate.getIconHeight();
    }
}
