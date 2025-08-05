package javax.swing.text.html;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Shape;
import javax.swing.text.Element;
import javax.swing.text.html.StyleSheet;

/* loaded from: rt.jar:javax/swing/text/html/ListView.class */
public class ListView extends BlockView {
    private StyleSheet.ListPainter listPainter;

    public ListView(Element element) {
        super(element, 1);
    }

    @Override // javax.swing.text.html.BlockView, javax.swing.text.BoxView, javax.swing.text.View
    public float getAlignment(int i2) {
        switch (i2) {
            case 0:
                return 0.5f;
            case 1:
                return 0.5f;
            default:
                throw new IllegalArgumentException("Invalid axis: " + i2);
        }
    }

    @Override // javax.swing.text.html.BlockView, javax.swing.text.BoxView, javax.swing.text.View
    public void paint(Graphics graphics, Shape shape) {
        super.paint(graphics, shape);
        Rectangle bounds = shape.getBounds();
        Rectangle clipBounds = graphics.getClipBounds();
        if (clipBounds.f12372x + clipBounds.width < bounds.f12372x + getLeftInset()) {
            Rectangle insideAllocation = getInsideAllocation(shape);
            int viewCount = getViewCount();
            int i2 = clipBounds.f12373y + clipBounds.height;
            for (int i3 = 0; i3 < viewCount; i3++) {
                bounds.setBounds(insideAllocation);
                childAllocation(i3, bounds);
                if (bounds.f12373y < i2) {
                    if (bounds.f12373y + bounds.height >= clipBounds.f12373y) {
                        this.listPainter.paint(graphics, bounds.f12372x, bounds.f12373y, bounds.width, bounds.height, this, i3);
                    }
                } else {
                    return;
                }
            }
        }
    }

    @Override // javax.swing.text.BoxView
    protected void paintChild(Graphics graphics, Rectangle rectangle, int i2) {
        this.listPainter.paint(graphics, rectangle.f12372x, rectangle.f12373y, rectangle.width, rectangle.height, this, i2);
        super.paintChild(graphics, rectangle, i2);
    }

    @Override // javax.swing.text.html.BlockView
    protected void setPropertiesFromAttributes() {
        super.setPropertiesFromAttributes();
        this.listPainter = getStyleSheet().getListPainter(getAttributes());
    }
}
