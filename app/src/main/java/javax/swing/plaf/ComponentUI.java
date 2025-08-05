package javax.swing.plaf;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.accessibility.Accessible;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;

/* loaded from: rt.jar:javax/swing/plaf/ComponentUI.class */
public abstract class ComponentUI {
    public void installUI(JComponent jComponent) {
    }

    public void uninstallUI(JComponent jComponent) {
    }

    public void paint(Graphics graphics, JComponent jComponent) {
    }

    public void update(Graphics graphics, JComponent jComponent) {
        if (jComponent.isOpaque()) {
            graphics.setColor(jComponent.getBackground());
            graphics.fillRect(0, 0, jComponent.getWidth(), jComponent.getHeight());
        }
        paint(graphics, jComponent);
    }

    public Dimension getPreferredSize(JComponent jComponent) {
        return null;
    }

    public Dimension getMinimumSize(JComponent jComponent) {
        return getPreferredSize(jComponent);
    }

    public Dimension getMaximumSize(JComponent jComponent) {
        return getPreferredSize(jComponent);
    }

    public boolean contains(JComponent jComponent, int i2, int i3) {
        return jComponent.inside(i2, i3);
    }

    public static ComponentUI createUI(JComponent jComponent) {
        throw new Error("ComponentUI.createUI not implemented.");
    }

    public int getBaseline(JComponent jComponent, int i2, int i3) {
        if (jComponent == null) {
            throw new NullPointerException("Component must be non-null");
        }
        if (i2 < 0 || i3 < 0) {
            throw new IllegalArgumentException("Width and height must be >= 0");
        }
        return -1;
    }

    public Component.BaselineResizeBehavior getBaselineResizeBehavior(JComponent jComponent) {
        if (jComponent == null) {
            throw new NullPointerException("Component must be non-null");
        }
        return Component.BaselineResizeBehavior.OTHER;
    }

    public int getAccessibleChildrenCount(JComponent jComponent) {
        return SwingUtilities.getAccessibleChildrenCount(jComponent);
    }

    public Accessible getAccessibleChild(JComponent jComponent, int i2) {
        return SwingUtilities.getAccessibleChild(jComponent, i2);
    }
}
