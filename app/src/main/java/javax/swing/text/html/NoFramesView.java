package javax.swing.text.html;

import java.awt.Container;
import java.awt.Graphics;
import java.awt.Shape;
import javax.swing.text.Element;
import javax.swing.text.JTextComponent;
import javax.swing.text.View;

/* loaded from: rt.jar:javax/swing/text/html/NoFramesView.class */
class NoFramesView extends BlockView {
    boolean visible;

    public NoFramesView(Element element, int i2) {
        super(element, i2);
        this.visible = false;
    }

    @Override // javax.swing.text.html.BlockView, javax.swing.text.BoxView, javax.swing.text.View
    public void paint(Graphics graphics, Shape shape) {
        Container container = getContainer();
        if (container != null && this.visible != ((JTextComponent) container).isEditable()) {
            this.visible = ((JTextComponent) container).isEditable();
        }
        if (!isVisible()) {
            return;
        }
        super.paint(graphics, shape);
    }

    @Override // javax.swing.text.html.BlockView, javax.swing.text.CompositeView, javax.swing.text.View
    public void setParent(View view) {
        Container container;
        if (view != null && (container = view.getContainer()) != null) {
            this.visible = ((JTextComponent) container).isEditable();
        }
        super.setParent(view);
    }

    @Override // javax.swing.text.View
    public boolean isVisible() {
        return this.visible;
    }

    @Override // javax.swing.text.BoxView
    protected void layout(int i2, int i3) {
        if (!isVisible()) {
            return;
        }
        super.layout(i2, i3);
    }

    @Override // javax.swing.text.html.BlockView, javax.swing.text.BoxView, javax.swing.text.View
    public float getPreferredSpan(int i2) {
        if (!this.visible) {
            return 0.0f;
        }
        return super.getPreferredSpan(i2);
    }

    @Override // javax.swing.text.html.BlockView, javax.swing.text.BoxView, javax.swing.text.View
    public float getMinimumSpan(int i2) {
        if (!this.visible) {
            return 0.0f;
        }
        return super.getMinimumSpan(i2);
    }

    @Override // javax.swing.text.html.BlockView, javax.swing.text.BoxView, javax.swing.text.View
    public float getMaximumSpan(int i2) {
        if (!this.visible) {
            return 0.0f;
        }
        return super.getMaximumSpan(i2);
    }
}
