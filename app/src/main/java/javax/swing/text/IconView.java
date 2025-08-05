package javax.swing.text;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Shape;
import javax.swing.Icon;
import javax.swing.text.Position;

/* loaded from: rt.jar:javax/swing/text/IconView.class */
public class IconView extends View {

    /* renamed from: c, reason: collision with root package name */
    private Icon f12837c;

    public IconView(Element element) {
        super(element);
        this.f12837c = StyleConstants.getIcon(element.getAttributes());
    }

    @Override // javax.swing.text.View
    public void paint(Graphics graphics, Shape shape) {
        Rectangle bounds = shape.getBounds();
        this.f12837c.paintIcon(getContainer(), graphics, bounds.f12372x, bounds.f12373y);
    }

    @Override // javax.swing.text.View
    public float getPreferredSpan(int i2) {
        switch (i2) {
            case 0:
                return this.f12837c.getIconWidth();
            case 1:
                return this.f12837c.getIconHeight();
            default:
                throw new IllegalArgumentException("Invalid axis: " + i2);
        }
    }

    @Override // javax.swing.text.View
    public float getAlignment(int i2) {
        switch (i2) {
            case 1:
                return 1.0f;
            default:
                return super.getAlignment(i2);
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
}
