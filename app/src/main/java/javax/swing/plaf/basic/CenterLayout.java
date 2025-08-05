package javax.swing.plaf.basic;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.io.Serializable;

/* loaded from: rt.jar:javax/swing/plaf/basic/CenterLayout.class */
class CenterLayout implements LayoutManager, Serializable {
    CenterLayout() {
    }

    @Override // java.awt.LayoutManager
    public void addLayoutComponent(String str, Component component) {
    }

    @Override // java.awt.LayoutManager
    public void removeLayoutComponent(Component component) {
    }

    @Override // java.awt.LayoutManager
    public Dimension preferredLayoutSize(Container container) {
        Component component = container.getComponent(0);
        if (component != null) {
            Dimension preferredSize = component.getPreferredSize();
            Insets insets = container.getInsets();
            return new Dimension(preferredSize.width + insets.left + insets.right, preferredSize.height + insets.top + insets.bottom);
        }
        return new Dimension(0, 0);
    }

    @Override // java.awt.LayoutManager
    public Dimension minimumLayoutSize(Container container) {
        return preferredLayoutSize(container);
    }

    @Override // java.awt.LayoutManager
    public void layoutContainer(Container container) {
        if (container.getComponentCount() > 0) {
            Component component = container.getComponent(0);
            Dimension preferredSize = component.getPreferredSize();
            int width = container.getWidth();
            int height = container.getHeight();
            Insets insets = container.getInsets();
            component.setBounds((((width - (insets.left + insets.right)) - preferredSize.width) / 2) + insets.left, (((height - (insets.top + insets.bottom)) - preferredSize.height) / 2) + insets.top, preferredSize.width, preferredSize.height);
        }
    }
}
