package javax.swing;

import java.awt.AWTError;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.LayoutManager;
import java.beans.ConstructorProperties;
import javax.accessibility.Accessible;
import javax.accessibility.AccessibleContext;
import javax.accessibility.AccessibleRole;

/* loaded from: rt.jar:javax/swing/Box.class */
public class Box extends JComponent implements Accessible {
    public Box(int i2) {
        super.setLayout(new BoxLayout(this, i2));
    }

    public static Box createHorizontalBox() {
        return new Box(0);
    }

    public static Box createVerticalBox() {
        return new Box(1);
    }

    public static Component createRigidArea(Dimension dimension) {
        return new Filler(dimension, dimension, dimension);
    }

    public static Component createHorizontalStrut(int i2) {
        return new Filler(new Dimension(i2, 0), new Dimension(i2, 0), new Dimension(i2, Short.MAX_VALUE));
    }

    public static Component createVerticalStrut(int i2) {
        return new Filler(new Dimension(0, i2), new Dimension(0, i2), new Dimension(Short.MAX_VALUE, i2));
    }

    public static Component createGlue() {
        return new Filler(new Dimension(0, 0), new Dimension(0, 0), new Dimension(Short.MAX_VALUE, Short.MAX_VALUE));
    }

    public static Component createHorizontalGlue() {
        return new Filler(new Dimension(0, 0), new Dimension(0, 0), new Dimension(Short.MAX_VALUE, 0));
    }

    public static Component createVerticalGlue() {
        return new Filler(new Dimension(0, 0), new Dimension(0, 0), new Dimension(0, Short.MAX_VALUE));
    }

    @Override // java.awt.Container
    public void setLayout(LayoutManager layoutManager) {
        throw new AWTError("Illegal request");
    }

    @Override // javax.swing.JComponent
    protected void paintComponent(Graphics graphics) {
        if (this.ui != null) {
            super.paintComponent(graphics);
        } else if (isOpaque()) {
            graphics.setColor(getBackground());
            graphics.fillRect(0, 0, getWidth(), getHeight());
        }
    }

    /* loaded from: rt.jar:javax/swing/Box$Filler.class */
    public static class Filler extends JComponent implements Accessible {
        @ConstructorProperties({"minimumSize", "preferredSize", "maximumSize"})
        public Filler(Dimension dimension, Dimension dimension2, Dimension dimension3) {
            setMinimumSize(dimension);
            setPreferredSize(dimension2);
            setMaximumSize(dimension3);
        }

        public void changeShape(Dimension dimension, Dimension dimension2, Dimension dimension3) {
            setMinimumSize(dimension);
            setPreferredSize(dimension2);
            setMaximumSize(dimension3);
            revalidate();
        }

        @Override // javax.swing.JComponent
        protected void paintComponent(Graphics graphics) {
            if (this.ui != null) {
                super.paintComponent(graphics);
            } else if (isOpaque()) {
                graphics.setColor(getBackground());
                graphics.fillRect(0, 0, getWidth(), getHeight());
            }
        }

        @Override // java.awt.Component
        public AccessibleContext getAccessibleContext() {
            if (this.accessibleContext == null) {
                this.accessibleContext = new AccessibleBoxFiller();
            }
            return this.accessibleContext;
        }

        /* loaded from: rt.jar:javax/swing/Box$Filler$AccessibleBoxFiller.class */
        protected class AccessibleBoxFiller extends Component.AccessibleAWTComponent {
            protected AccessibleBoxFiller() {
                super();
            }

            @Override // java.awt.Component.AccessibleAWTComponent, javax.accessibility.AccessibleContext
            public AccessibleRole getAccessibleRole() {
                return AccessibleRole.FILLER;
            }
        }
    }

    @Override // java.awt.Component
    public AccessibleContext getAccessibleContext() {
        if (this.accessibleContext == null) {
            this.accessibleContext = new AccessibleBox();
        }
        return this.accessibleContext;
    }

    /* loaded from: rt.jar:javax/swing/Box$AccessibleBox.class */
    protected class AccessibleBox extends Container.AccessibleAWTContainer {
        protected AccessibleBox() {
            super();
        }

        @Override // java.awt.Component.AccessibleAWTComponent, javax.accessibility.AccessibleContext
        public AccessibleRole getAccessibleRole() {
            return AccessibleRole.FILLER;
        }
    }
}
