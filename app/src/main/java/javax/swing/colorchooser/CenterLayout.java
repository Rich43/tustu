package javax.swing.colorchooser;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.io.Serializable;

/* loaded from: rt.jar:javax/swing/colorchooser/CenterLayout.class */
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
            preferredSize.width += insets.left + insets.right;
            preferredSize.height += insets.top + insets.bottom;
            return preferredSize;
        }
        return new Dimension(0, 0);
    }

    @Override // java.awt.LayoutManager
    public Dimension minimumLayoutSize(Container container) {
        return preferredLayoutSize(container);
    }

    @Override // java.awt.LayoutManager
    public void layoutContainer(Container container) {
        try {
            Component component = container.getComponent(0);
            component.setSize(component.getPreferredSize());
            Dimension size = component.getSize();
            Dimension size2 = container.getSize();
            Insets insets = container.getInsets();
            size2.width -= insets.left + insets.right;
            size2.height -= insets.top + insets.bottom;
            component.setBounds(((size2.width / 2) - (size.width / 2)) + insets.left, ((size2.height / 2) - (size.height / 2)) + insets.top, size.width, size.height);
        } catch (Exception e2) {
        }
    }
}
