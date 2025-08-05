package javax.swing;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.io.IOException;
import java.io.ObjectOutputStream;
import javax.accessibility.Accessible;
import javax.accessibility.AccessibleContext;
import javax.accessibility.AccessibleRole;

/* loaded from: rt.jar:javax/swing/CellRendererPane.class */
public class CellRendererPane extends Container implements Accessible {
    protected AccessibleContext accessibleContext = null;

    public CellRendererPane() {
        setLayout(null);
        setVisible(false);
    }

    @Override // java.awt.Container, java.awt.Component
    public void invalidate() {
    }

    @Override // java.awt.Container, java.awt.Component
    public void paint(Graphics graphics) {
    }

    @Override // java.awt.Container, java.awt.Component
    public void update(Graphics graphics) {
    }

    @Override // java.awt.Container
    protected void addImpl(Component component, Object obj, int i2) {
        if (component.getParent() == this) {
            return;
        }
        super.addImpl(component, obj, i2);
    }

    public void paintComponent(Graphics graphics, Component component, Container container, int i2, int i3, int i4, int i5, boolean z2) {
        if (component == null) {
            if (container != null) {
                Color color = graphics.getColor();
                graphics.setColor(container.getBackground());
                graphics.fillRect(i2, i3, i4, i5);
                graphics.setColor(color);
                return;
            }
            return;
        }
        if (component.getParent() != this) {
            add(component);
        }
        component.setBounds(i2, i3, i4, i5);
        if (z2) {
            component.validate();
        }
        boolean z3 = false;
        if ((component instanceof JComponent) && ((JComponent) component).isDoubleBuffered()) {
            z3 = true;
            ((JComponent) component).setDoubleBuffered(false);
        }
        Graphics graphicsCreate = graphics.create(i2, i3, i4, i5);
        try {
            component.paint(graphicsCreate);
            graphicsCreate.dispose();
            if (z3 && (component instanceof JComponent)) {
                ((JComponent) component).setDoubleBuffered(true);
            }
            component.setBounds(-i4, -i5, 0, 0);
        } catch (Throwable th) {
            graphicsCreate.dispose();
            throw th;
        }
    }

    public void paintComponent(Graphics graphics, Component component, Container container, int i2, int i3, int i4, int i5) {
        paintComponent(graphics, component, container, i2, i3, i4, i5, false);
    }

    public void paintComponent(Graphics graphics, Component component, Container container, Rectangle rectangle) {
        paintComponent(graphics, component, container, rectangle.f12372x, rectangle.f12373y, rectangle.width, rectangle.height);
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        removeAll();
        objectOutputStream.defaultWriteObject();
    }

    @Override // java.awt.Component
    public AccessibleContext getAccessibleContext() {
        if (this.accessibleContext == null) {
            this.accessibleContext = new AccessibleCellRendererPane();
        }
        return this.accessibleContext;
    }

    /* loaded from: rt.jar:javax/swing/CellRendererPane$AccessibleCellRendererPane.class */
    protected class AccessibleCellRendererPane extends Container.AccessibleAWTContainer {
        protected AccessibleCellRendererPane() {
            super();
        }

        @Override // java.awt.Component.AccessibleAWTComponent, javax.accessibility.AccessibleContext
        public AccessibleRole getAccessibleRole() {
            return AccessibleRole.PANEL;
        }
    }
}
