package javax.swing.text.html;

import java.awt.Component;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Shape;
import javax.swing.text.ComponentView;
import javax.swing.text.Element;
import javax.swing.text.JTextComponent;
import javax.swing.text.View;

/* loaded from: rt.jar:javax/swing/text/html/EditableView.class */
class EditableView extends ComponentView {
    private boolean isVisible;

    EditableView(Element element) {
        super(element);
    }

    @Override // javax.swing.text.ComponentView, javax.swing.text.View
    public float getMinimumSpan(int i2) {
        if (this.isVisible) {
            return super.getMinimumSpan(i2);
        }
        return 0.0f;
    }

    @Override // javax.swing.text.ComponentView, javax.swing.text.View
    public float getPreferredSpan(int i2) {
        if (this.isVisible) {
            return super.getPreferredSpan(i2);
        }
        return 0.0f;
    }

    @Override // javax.swing.text.ComponentView, javax.swing.text.View
    public float getMaximumSpan(int i2) {
        if (this.isVisible) {
            return super.getMaximumSpan(i2);
        }
        return 0.0f;
    }

    @Override // javax.swing.text.ComponentView, javax.swing.text.View
    public void paint(Graphics graphics, Shape shape) {
        Component component = getComponent();
        Container container = getContainer();
        if ((container instanceof JTextComponent) && this.isVisible != ((JTextComponent) container).isEditable()) {
            this.isVisible = ((JTextComponent) container).isEditable();
            preferenceChanged(null, true, true);
            container.repaint();
        }
        if (this.isVisible) {
            super.paint(graphics, shape);
        } else {
            setSize(0.0f, 0.0f);
        }
        if (component != null) {
            component.setFocusable(this.isVisible);
        }
    }

    @Override // javax.swing.text.ComponentView, javax.swing.text.View
    public void setParent(View view) {
        Container container;
        if (view != null && (container = view.getContainer()) != null) {
            if (container instanceof JTextComponent) {
                this.isVisible = ((JTextComponent) container).isEditable();
            } else {
                this.isVisible = false;
            }
        }
        super.setParent(view);
    }

    @Override // javax.swing.text.View
    public boolean isVisible() {
        return this.isVisible;
    }
}
