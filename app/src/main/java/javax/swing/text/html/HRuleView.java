package javax.swing.text.html;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Shape;
import javax.swing.event.DocumentEvent;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import javax.swing.text.Position;
import javax.swing.text.StyleConstants;
import javax.swing.text.View;
import javax.swing.text.ViewFactory;
import javax.swing.text.html.CSS;
import javax.swing.text.html.HTML;

/* loaded from: rt.jar:javax/swing/text/html/HRuleView.class */
class HRuleView extends View {
    private float topMargin;
    private float bottomMargin;
    private float leftMargin;
    private float rightMargin;
    private int alignment;
    private String noshade;
    private int size;
    private CSS.LengthValue widthValue;
    private static final int SPACE_ABOVE = 3;
    private static final int SPACE_BELOW = 3;
    private AttributeSet attr;

    public HRuleView(Element element) {
        super(element);
        this.alignment = 1;
        this.noshade = null;
        this.size = 0;
        setPropertiesFromAttributes();
    }

    protected void setPropertiesFromAttributes() {
        StyleSheet styleSheet = ((HTMLDocument) getDocument()).getStyleSheet();
        AttributeSet attributes = getElement().getAttributes();
        this.attr = styleSheet.getViewAttributes(this);
        this.alignment = 1;
        this.size = 0;
        this.noshade = null;
        this.widthValue = null;
        if (this.attr != null) {
            if (this.attr.getAttribute(StyleConstants.Alignment) != null) {
                this.alignment = StyleConstants.getAlignment(this.attr);
            }
            this.noshade = (String) attributes.getAttribute(HTML.Attribute.NOSHADE);
            Object attribute = attributes.getAttribute(HTML.Attribute.SIZE);
            if (attribute != null && (attribute instanceof String)) {
                try {
                    this.size = Integer.parseInt((String) attribute);
                } catch (NumberFormatException e2) {
                    this.size = 1;
                }
            }
            Object attribute2 = this.attr.getAttribute(CSS.Attribute.WIDTH);
            if (attribute2 != null && (attribute2 instanceof CSS.LengthValue)) {
                this.widthValue = (CSS.LengthValue) attribute2;
            }
            this.topMargin = getLength(CSS.Attribute.MARGIN_TOP, this.attr);
            this.bottomMargin = getLength(CSS.Attribute.MARGIN_BOTTOM, this.attr);
            this.leftMargin = getLength(CSS.Attribute.MARGIN_LEFT, this.attr);
            this.rightMargin = getLength(CSS.Attribute.MARGIN_RIGHT, this.attr);
        } else {
            this.rightMargin = 0.0f;
            this.leftMargin = 0.0f;
            this.bottomMargin = 0.0f;
            this.topMargin = 0.0f;
        }
        this.size = Math.max(2, this.size);
    }

    private float getLength(CSS.Attribute attribute, AttributeSet attributeSet) {
        CSS.LengthValue lengthValue = (CSS.LengthValue) attributeSet.getAttribute(attribute);
        return lengthValue != null ? lengthValue.getValue() : 0.0f;
    }

    @Override // javax.swing.text.View
    public void paint(Graphics graphics, Shape shape) {
        int i2;
        Color color;
        Color color2;
        Rectangle bounds = shape instanceof Rectangle ? (Rectangle) shape : shape.getBounds();
        int i3 = bounds.f12373y + 3 + ((int) this.topMargin);
        int value = bounds.width - ((int) (this.leftMargin + this.rightMargin));
        if (this.widthValue != null) {
            value = (int) this.widthValue.getValue(value);
        }
        int i4 = bounds.height - ((6 + ((int) this.topMargin)) + ((int) this.bottomMargin));
        if (this.size > 0) {
            i4 = this.size;
        }
        switch (this.alignment) {
            case 0:
            default:
                i2 = bounds.f12372x + ((int) this.leftMargin);
                break;
            case 1:
                i2 = (bounds.f12372x + (bounds.width / 2)) - (value / 2);
                break;
            case 2:
                i2 = ((bounds.f12372x + bounds.width) - value) - ((int) this.rightMargin);
                break;
        }
        if (this.noshade != null) {
            graphics.setColor(Color.black);
            graphics.fillRect(i2, i3, value, i4);
            return;
        }
        Color background = getContainer().getBackground();
        if (background == null || background.equals(Color.white)) {
            color = Color.darkGray;
            color2 = Color.lightGray;
        } else {
            color = Color.darkGray;
            color2 = Color.white;
        }
        graphics.setColor(color2);
        graphics.drawLine((i2 + value) - 1, i3, (i2 + value) - 1, (i3 + i4) - 1);
        graphics.drawLine(i2, (i3 + i4) - 1, (i2 + value) - 1, (i3 + i4) - 1);
        graphics.setColor(color);
        graphics.drawLine(i2, i3, (i2 + value) - 1, i3);
        graphics.drawLine(i2, i3, i2, (i3 + i4) - 1);
    }

    @Override // javax.swing.text.View
    public float getPreferredSpan(int i2) {
        switch (i2) {
            case 0:
                return 1.0f;
            case 1:
                if (this.size > 0) {
                    return this.size + 3 + 3 + this.topMargin + this.bottomMargin;
                }
                if (this.noshade != null) {
                    return 8.0f + this.topMargin + this.bottomMargin;
                }
                return 6.0f + this.topMargin + this.bottomMargin;
            default:
                throw new IllegalArgumentException("Invalid axis: " + i2);
        }
    }

    @Override // javax.swing.text.View
    public int getResizeWeight(int i2) {
        if (i2 == 0) {
            return 1;
        }
        if (i2 == 1) {
            return 0;
        }
        return 0;
    }

    @Override // javax.swing.text.View
    public int getBreakWeight(int i2, float f2, float f3) {
        if (i2 == 0) {
            return 3000;
        }
        return 0;
    }

    @Override // javax.swing.text.View
    public View breakView(int i2, int i3, float f2, float f3) {
        return null;
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
        return null;
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

    @Override // javax.swing.text.View
    public AttributeSet getAttributes() {
        return this.attr;
    }

    @Override // javax.swing.text.View
    public void changedUpdate(DocumentEvent documentEvent, Shape shape, ViewFactory viewFactory) {
        super.changedUpdate(documentEvent, shape, viewFactory);
        int offset = documentEvent.getOffset();
        if (offset <= getStartOffset() && offset + documentEvent.getLength() >= getEndOffset()) {
            setPropertiesFromAttributes();
        }
    }
}
