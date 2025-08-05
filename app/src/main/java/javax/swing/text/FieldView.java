package javax.swing.text;

import java.awt.Container;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Shape;
import javax.swing.BoundedRangeModel;
import javax.swing.JComponent;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.text.Position;
import sun.swing.SwingUtilities2;

/* loaded from: rt.jar:javax/swing/text/FieldView.class */
public class FieldView extends PlainView {
    public FieldView(Element element) {
        super(element);
    }

    protected FontMetrics getFontMetrics() {
        Container container = getContainer();
        return container.getFontMetrics(container.getFont());
    }

    protected Shape adjustAllocation(Shape shape) {
        if (shape != null) {
            Rectangle bounds = shape.getBounds();
            int preferredSpan = (int) getPreferredSpan(1);
            int preferredSpan2 = (int) getPreferredSpan(0);
            if (bounds.height != preferredSpan) {
                int i2 = bounds.height - preferredSpan;
                bounds.f12373y += i2 / 2;
                bounds.height -= i2;
            }
            Container container = getContainer();
            if (container instanceof JTextField) {
                BoundedRangeModel horizontalVisibility = ((JTextField) container).getHorizontalVisibility();
                int iMax = Math.max(preferredSpan2, bounds.width);
                int value = horizontalVisibility.getValue();
                int iMin = Math.min(iMax, bounds.width - 1);
                if (value + iMin > iMax) {
                    value = iMax - iMin;
                }
                horizontalVisibility.setRangeProperties(value, iMin, horizontalVisibility.getMinimum(), iMax, false);
                if (preferredSpan2 < bounds.width) {
                    int i3 = (bounds.width - 1) - preferredSpan2;
                    int horizontalAlignment = ((JTextField) container).getHorizontalAlignment();
                    if (Utilities.isLeftToRight(container)) {
                        if (horizontalAlignment == 10) {
                            horizontalAlignment = 2;
                        } else if (horizontalAlignment == 11) {
                            horizontalAlignment = 4;
                        }
                    } else if (horizontalAlignment == 10) {
                        horizontalAlignment = 4;
                    } else if (horizontalAlignment == 11) {
                        horizontalAlignment = 2;
                    }
                    switch (horizontalAlignment) {
                        case 0:
                            bounds.f12372x += i3 / 2;
                            bounds.width -= i3;
                            break;
                        case 4:
                            bounds.f12372x += i3;
                            bounds.width -= i3;
                            break;
                    }
                } else {
                    bounds.width = preferredSpan2;
                    bounds.f12372x -= horizontalVisibility.getValue();
                }
            }
            return bounds;
        }
        return null;
    }

    void updateVisibilityModel() {
        Container container = getContainer();
        if (container instanceof JTextField) {
            BoundedRangeModel horizontalVisibility = ((JTextField) container).getHorizontalVisibility();
            int preferredSpan = (int) getPreferredSpan(0);
            int extent = horizontalVisibility.getExtent();
            int iMax = Math.max(preferredSpan, extent);
            int i2 = extent == 0 ? iMax : extent;
            int i3 = iMax - i2;
            int value = horizontalVisibility.getValue();
            if (value + i2 > iMax) {
                value = iMax - i2;
            }
            horizontalVisibility.setRangeProperties(Math.max(0, Math.min(i3, value)), i2, 0, iMax, false);
        }
    }

    @Override // javax.swing.text.PlainView, javax.swing.text.View
    public void paint(Graphics graphics, Shape shape) {
        Rectangle rectangle = (Rectangle) shape;
        graphics.clipRect(rectangle.f12372x, rectangle.f12373y, rectangle.width, rectangle.height);
        super.paint(graphics, shape);
    }

    @Override // javax.swing.text.PlainView
    Shape adjustPaintRegion(Shape shape) {
        return adjustAllocation(shape);
    }

    @Override // javax.swing.text.PlainView, javax.swing.text.View
    public float getPreferredSpan(int i2) {
        int tabbedTextWidth;
        switch (i2) {
            case 0:
                Segment sharedSegment = SegmentCache.getSharedSegment();
                Document document = getDocument();
                try {
                    FontMetrics fontMetrics = getFontMetrics();
                    document.getText(0, document.getLength(), sharedSegment);
                    tabbedTextWidth = Utilities.getTabbedTextWidth(sharedSegment, fontMetrics, 0, this, 0);
                    if (sharedSegment.count > 0) {
                        Container container = getContainer();
                        this.firstLineOffset = SwingUtilities2.getLeftSideBearing(container instanceof JComponent ? (JComponent) container : null, fontMetrics, sharedSegment.array[sharedSegment.offset]);
                        this.firstLineOffset = Math.max(0, -this.firstLineOffset);
                    } else {
                        this.firstLineOffset = 0;
                    }
                } catch (BadLocationException e2) {
                    tabbedTextWidth = 0;
                }
                SegmentCache.releaseSharedSegment(sharedSegment);
                return tabbedTextWidth + this.firstLineOffset;
            default:
                return super.getPreferredSpan(i2);
        }
    }

    @Override // javax.swing.text.View
    public int getResizeWeight(int i2) {
        if (i2 == 0) {
            return 1;
        }
        return 0;
    }

    @Override // javax.swing.text.PlainView, javax.swing.text.View
    public Shape modelToView(int i2, Shape shape, Position.Bias bias) throws BadLocationException {
        return super.modelToView(i2, adjustAllocation(shape), bias);
    }

    @Override // javax.swing.text.PlainView, javax.swing.text.View
    public int viewToModel(float f2, float f3, Shape shape, Position.Bias[] biasArr) {
        return super.viewToModel(f2, f3, adjustAllocation(shape), biasArr);
    }

    @Override // javax.swing.text.PlainView, javax.swing.text.View
    public void insertUpdate(DocumentEvent documentEvent, Shape shape, ViewFactory viewFactory) {
        super.insertUpdate(documentEvent, adjustAllocation(shape), viewFactory);
        updateVisibilityModel();
    }

    @Override // javax.swing.text.PlainView, javax.swing.text.View
    public void removeUpdate(DocumentEvent documentEvent, Shape shape, ViewFactory viewFactory) {
        super.removeUpdate(documentEvent, adjustAllocation(shape), viewFactory);
        updateVisibilityModel();
    }
}
