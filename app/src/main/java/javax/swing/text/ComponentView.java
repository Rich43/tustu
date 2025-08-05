package javax.swing.text;

import java.awt.AWTKeyStroke;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.KeyboardFocusManager;
import java.awt.Rectangle;
import java.awt.Shape;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Set;
import javax.swing.SwingUtilities;
import javax.swing.text.Position;
import jdk.jfr.Enabled;

/* loaded from: rt.jar:javax/swing/text/ComponentView.class */
public class ComponentView extends View {
    private Component createdC;

    /* renamed from: c, reason: collision with root package name */
    private Invalidator f12832c;

    public ComponentView(Element element) {
        super(element);
    }

    protected Component createComponent() {
        return StyleConstants.getComponent(getElement().getAttributes());
    }

    public final Component getComponent() {
        return this.createdC;
    }

    @Override // javax.swing.text.View
    public void paint(Graphics graphics, Shape shape) {
        if (this.f12832c != null) {
            Rectangle bounds = shape instanceof Rectangle ? (Rectangle) shape : shape.getBounds();
            this.f12832c.setBounds(bounds.f12372x, bounds.f12373y, bounds.width, bounds.height);
        }
    }

    @Override // javax.swing.text.View
    public float getPreferredSpan(int i2) {
        if (i2 != 0 && i2 != 1) {
            throw new IllegalArgumentException("Invalid axis: " + i2);
        }
        if (this.f12832c != null) {
            Dimension preferredSize = this.f12832c.getPreferredSize();
            if (i2 == 0) {
                return preferredSize.width;
            }
            return preferredSize.height;
        }
        return 0.0f;
    }

    @Override // javax.swing.text.View
    public float getMinimumSpan(int i2) {
        if (i2 != 0 && i2 != 1) {
            throw new IllegalArgumentException("Invalid axis: " + i2);
        }
        if (this.f12832c != null) {
            Dimension minimumSize = this.f12832c.getMinimumSize();
            if (i2 == 0) {
                return minimumSize.width;
            }
            return minimumSize.height;
        }
        return 0.0f;
    }

    @Override // javax.swing.text.View
    public float getMaximumSpan(int i2) {
        if (i2 != 0 && i2 != 1) {
            throw new IllegalArgumentException("Invalid axis: " + i2);
        }
        if (this.f12832c != null) {
            Dimension maximumSize = this.f12832c.getMaximumSize();
            if (i2 == 0) {
                return maximumSize.width;
            }
            return maximumSize.height;
        }
        return 0.0f;
    }

    @Override // javax.swing.text.View
    public float getAlignment(int i2) {
        if (this.f12832c != null) {
            switch (i2) {
                case 0:
                    return this.f12832c.getAlignmentX();
                case 1:
                    return this.f12832c.getAlignmentY();
            }
        }
        return super.getAlignment(i2);
    }

    @Override // javax.swing.text.View
    public void setParent(View view) {
        super.setParent(view);
        if (SwingUtilities.isEventDispatchThread()) {
            setComponentParent();
        } else {
            SwingUtilities.invokeLater(new Runnable() { // from class: javax.swing.text.ComponentView.1
                @Override // java.lang.Runnable
                public void run() {
                    Document document = ComponentView.this.getDocument();
                    try {
                        if (document instanceof AbstractDocument) {
                            ((AbstractDocument) document).readLock();
                        }
                        ComponentView.this.setComponentParent();
                        Container container = ComponentView.this.getContainer();
                        if (container != null) {
                            ComponentView.this.preferenceChanged(null, true, true);
                            container.repaint();
                        }
                    } finally {
                        if (document instanceof AbstractDocument) {
                            ((AbstractDocument) document).readUnlock();
                        }
                    }
                }
            });
        }
    }

    void setComponentParent() {
        Container parent;
        Component componentCreateComponent;
        if (getParent() != null) {
            Container container = getContainer();
            if (container != null) {
                if (this.f12832c == null && (componentCreateComponent = createComponent()) != null) {
                    this.createdC = componentCreateComponent;
                    this.f12832c = new Invalidator(componentCreateComponent);
                }
                if (this.f12832c != null && this.f12832c.getParent() == null) {
                    container.add(this.f12832c, this);
                    container.addPropertyChangeListener(Enabled.NAME, this.f12832c);
                    return;
                }
                return;
            }
            return;
        }
        if (this.f12832c != null && (parent = this.f12832c.getParent()) != null) {
            parent.remove(this.f12832c);
            parent.removePropertyChangeListener(Enabled.NAME, this.f12832c);
        }
    }

    @Override // javax.swing.text.View
    public Shape modelToView(int i2, Shape shape, Position.Bias bias) throws BadLocationException {
        int startOffset = getStartOffset();
        int endOffset = getEndOffset();
        if (i2 >= startOffset && i2 <= endOffset) {
            Rectangle bounds = shape.getBounds();
            if (i2 == endOffset) {
                bounds.f12372x += bounds.width;
            }
            bounds.width = 0;
            return bounds;
        }
        throw new BadLocationException(i2 + " not in range " + startOffset + "," + endOffset, i2);
    }

    @Override // javax.swing.text.View
    public int viewToModel(float f2, float f3, Shape shape, Position.Bias[] biasArr) {
        Rectangle rectangle = (Rectangle) shape;
        if (f2 < rectangle.f12372x + (rectangle.width / 2)) {
            biasArr[0] = Position.Bias.Forward;
            return getStartOffset();
        }
        biasArr[0] = Position.Bias.Backward;
        return getEndOffset();
    }

    /* loaded from: rt.jar:javax/swing/text/ComponentView$Invalidator.class */
    class Invalidator extends Container implements PropertyChangeListener {
        Dimension min;
        Dimension pref;
        Dimension max;
        float yalign;
        float xalign;

        Invalidator(Component component) {
            setLayout(null);
            add(component);
            cacheChildSizes();
        }

        @Override // java.awt.Container, java.awt.Component
        public void invalidate() {
            super.invalidate();
            if (getParent() != null) {
                ComponentView.this.preferenceChanged(null, true, true);
            }
        }

        @Override // java.awt.Container, java.awt.Component
        public void doLayout() {
            cacheChildSizes();
        }

        @Override // java.awt.Component
        public void setBounds(int i2, int i3, int i4, int i5) {
            super.setBounds(i2, i3, i4, i5);
            if (getComponentCount() > 0) {
                getComponent(0).setSize(i4, i5);
            }
            cacheChildSizes();
        }

        public void validateIfNecessary() {
            if (!isValid()) {
                validate();
            }
        }

        private void cacheChildSizes() {
            if (getComponentCount() > 0) {
                Component component = getComponent(0);
                this.min = component.getMinimumSize();
                this.pref = component.getPreferredSize();
                this.max = component.getMaximumSize();
                this.yalign = component.getAlignmentY();
                this.xalign = component.getAlignmentX();
                return;
            }
            Dimension dimension = new Dimension(0, 0);
            this.max = dimension;
            this.pref = dimension;
            this.min = dimension;
        }

        @Override // java.awt.Component
        public void setVisible(boolean z2) {
            super.setVisible(z2);
            if (getComponentCount() > 0) {
                getComponent(0).setVisible(z2);
            }
        }

        @Override // java.awt.Component
        public boolean isShowing() {
            return true;
        }

        @Override // java.awt.Container, java.awt.Component
        public Dimension getMinimumSize() {
            validateIfNecessary();
            return this.min;
        }

        @Override // java.awt.Container, java.awt.Component
        public Dimension getPreferredSize() {
            validateIfNecessary();
            return this.pref;
        }

        @Override // java.awt.Container, java.awt.Component
        public Dimension getMaximumSize() {
            validateIfNecessary();
            return this.max;
        }

        @Override // java.awt.Container, java.awt.Component
        public float getAlignmentX() {
            validateIfNecessary();
            return this.xalign;
        }

        @Override // java.awt.Container, java.awt.Component
        public float getAlignmentY() {
            validateIfNecessary();
            return this.yalign;
        }

        @Override // java.awt.Container, java.awt.Component
        public Set<AWTKeyStroke> getFocusTraversalKeys(int i2) {
            return KeyboardFocusManager.getCurrentKeyboardFocusManager().getDefaultFocusTraversalKeys(i2);
        }

        @Override // java.beans.PropertyChangeListener
        public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
            Boolean bool = (Boolean) propertyChangeEvent.getNewValue();
            if (getComponentCount() > 0) {
                getComponent(0).setEnabled(bool.booleanValue());
            }
        }
    }
}
