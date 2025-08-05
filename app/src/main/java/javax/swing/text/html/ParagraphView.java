package javax.swing.text.html;

import java.awt.Container;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Shape;
import javax.swing.JSplitPane;
import javax.swing.SizeRequirements;
import javax.swing.text.AttributeSet;
import javax.swing.text.Element;
import javax.swing.text.JTextComponent;
import javax.swing.text.View;
import javax.swing.text.html.CSS;
import javax.swing.text.html.StyleSheet;

/* loaded from: rt.jar:javax/swing/text/html/ParagraphView.class */
public class ParagraphView extends javax.swing.text.ParagraphView {
    private AttributeSet attr;
    private StyleSheet.BoxPainter painter;
    private CSS.LengthValue cssWidth;
    private CSS.LengthValue cssHeight;

    public ParagraphView(Element element) {
        super(element);
    }

    @Override // javax.swing.text.FlowView, javax.swing.text.CompositeView, javax.swing.text.View
    public void setParent(View view) {
        super.setParent(view);
        if (view != null) {
            setPropertiesFromAttributes();
        }
    }

    @Override // javax.swing.text.View
    public AttributeSet getAttributes() {
        if (this.attr == null) {
            this.attr = getStyleSheet().getViewAttributes(this);
        }
        return this.attr;
    }

    @Override // javax.swing.text.ParagraphView
    protected void setPropertiesFromAttributes() {
        StyleSheet styleSheet = getStyleSheet();
        this.attr = styleSheet.getViewAttributes(this);
        this.painter = styleSheet.getBoxPainter(this.attr);
        if (this.attr != null) {
            super.setPropertiesFromAttributes();
            setInsets((short) this.painter.getInset(1, this), (short) this.painter.getInset(2, this), (short) this.painter.getInset(3, this), (short) this.painter.getInset(4, this));
            Object attribute = this.attr.getAttribute(CSS.Attribute.TEXT_ALIGN);
            if (attribute != null) {
                String string = attribute.toString();
                if (string.equals(JSplitPane.LEFT)) {
                    setJustification(0);
                } else if (string.equals("center")) {
                    setJustification(1);
                } else if (string.equals(JSplitPane.RIGHT)) {
                    setJustification(2);
                } else if (string.equals("justify")) {
                    setJustification(3);
                }
            }
            this.cssWidth = (CSS.LengthValue) this.attr.getAttribute(CSS.Attribute.WIDTH);
            this.cssHeight = (CSS.LengthValue) this.attr.getAttribute(CSS.Attribute.HEIGHT);
        }
    }

    protected StyleSheet getStyleSheet() {
        return ((HTMLDocument) getDocument()).getStyleSheet();
    }

    @Override // javax.swing.text.ParagraphView, javax.swing.text.FlowView, javax.swing.text.BoxView
    protected SizeRequirements calculateMinorAxisRequirements(int i2, SizeRequirements sizeRequirements) {
        SizeRequirements sizeRequirementsCalculateMinorAxisRequirements = super.calculateMinorAxisRequirements(i2, sizeRequirements);
        if (BlockView.spanSetFromAttributes(i2, sizeRequirementsCalculateMinorAxisRequirements, this.cssWidth, this.cssHeight)) {
            int leftInset = i2 == 0 ? getLeftInset() + getRightInset() : getTopInset() + getBottomInset();
            sizeRequirementsCalculateMinorAxisRequirements.minimum -= leftInset;
            sizeRequirementsCalculateMinorAxisRequirements.preferred -= leftInset;
            sizeRequirementsCalculateMinorAxisRequirements.maximum -= leftInset;
        }
        return sizeRequirementsCalculateMinorAxisRequirements;
    }

    @Override // javax.swing.text.View
    public boolean isVisible() {
        int layoutViewCount = getLayoutViewCount() - 1;
        for (int i2 = 0; i2 < layoutViewCount; i2++) {
            if (getLayoutView(i2).isVisible()) {
                return true;
            }
        }
        if (layoutViewCount > 0) {
            View layoutView = getLayoutView(layoutViewCount);
            if (layoutView.getEndOffset() - layoutView.getStartOffset() == 1) {
                return false;
            }
        }
        if (getStartOffset() == getDocument().getLength()) {
            boolean zIsEditable = false;
            Container container = getContainer();
            if (container instanceof JTextComponent) {
                zIsEditable = ((JTextComponent) container).isEditable();
            }
            if (!zIsEditable) {
                return false;
            }
            return true;
        }
        return true;
    }

    @Override // javax.swing.text.ParagraphView, javax.swing.text.BoxView, javax.swing.text.View
    public void paint(Graphics graphics, Shape shape) {
        Rectangle bounds;
        if (shape == null) {
            return;
        }
        if (shape instanceof Rectangle) {
            bounds = (Rectangle) shape;
        } else {
            bounds = shape.getBounds();
        }
        this.painter.paint(graphics, bounds.f12372x, bounds.f12373y, bounds.width, bounds.height, this);
        super.paint(graphics, shape);
    }

    @Override // javax.swing.text.BoxView, javax.swing.text.View
    public float getPreferredSpan(int i2) {
        if (!isVisible()) {
            return 0.0f;
        }
        return super.getPreferredSpan(i2);
    }

    @Override // javax.swing.text.BoxView, javax.swing.text.View
    public float getMinimumSpan(int i2) {
        if (!isVisible()) {
            return 0.0f;
        }
        return super.getMinimumSpan(i2);
    }

    @Override // javax.swing.text.BoxView, javax.swing.text.View
    public float getMaximumSpan(int i2) {
        if (!isVisible()) {
            return 0.0f;
        }
        return super.getMaximumSpan(i2);
    }
}
