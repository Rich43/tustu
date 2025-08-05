package javax.swing.text.html;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Shape;
import javax.swing.JSplitPane;
import javax.swing.SizeRequirements;
import javax.swing.event.DocumentEvent;
import javax.swing.text.AttributeSet;
import javax.swing.text.BoxView;
import javax.swing.text.Element;
import javax.swing.text.View;
import javax.swing.text.ViewFactory;
import javax.swing.text.html.CSS;
import javax.swing.text.html.StyleSheet;

/* loaded from: rt.jar:javax/swing/text/html/BlockView.class */
public class BlockView extends BoxView {
    private AttributeSet attr;
    private StyleSheet.BoxPainter painter;
    private CSS.LengthValue cssWidth;
    private CSS.LengthValue cssHeight;

    public BlockView(Element element, int i2) {
        super(element, i2);
    }

    @Override // javax.swing.text.CompositeView, javax.swing.text.View
    public void setParent(View view) {
        super.setParent(view);
        if (view != null) {
            setPropertiesFromAttributes();
        }
    }

    @Override // javax.swing.text.BoxView
    protected SizeRequirements calculateMajorAxisRequirements(int i2, SizeRequirements sizeRequirements) {
        if (sizeRequirements == null) {
            sizeRequirements = new SizeRequirements();
        }
        if (!spanSetFromAttributes(i2, sizeRequirements, this.cssWidth, this.cssHeight)) {
            sizeRequirements = super.calculateMajorAxisRequirements(i2, sizeRequirements);
        } else {
            SizeRequirements sizeRequirementsCalculateMajorAxisRequirements = super.calculateMajorAxisRequirements(i2, null);
            int leftInset = i2 == 0 ? getLeftInset() + getRightInset() : getTopInset() + getBottomInset();
            sizeRequirements.minimum -= leftInset;
            sizeRequirements.preferred -= leftInset;
            sizeRequirements.maximum -= leftInset;
            constrainSize(i2, sizeRequirements, sizeRequirementsCalculateMajorAxisRequirements);
        }
        return sizeRequirements;
    }

    @Override // javax.swing.text.BoxView
    protected SizeRequirements calculateMinorAxisRequirements(int i2, SizeRequirements sizeRequirements) {
        Object attribute;
        if (sizeRequirements == null) {
            sizeRequirements = new SizeRequirements();
        }
        if (!spanSetFromAttributes(i2, sizeRequirements, this.cssWidth, this.cssHeight)) {
            sizeRequirements = super.calculateMinorAxisRequirements(i2, sizeRequirements);
        } else {
            SizeRequirements sizeRequirementsCalculateMinorAxisRequirements = super.calculateMinorAxisRequirements(i2, null);
            int leftInset = i2 == 0 ? getLeftInset() + getRightInset() : getTopInset() + getBottomInset();
            sizeRequirements.minimum -= leftInset;
            sizeRequirements.preferred -= leftInset;
            sizeRequirements.maximum -= leftInset;
            constrainSize(i2, sizeRequirements, sizeRequirementsCalculateMinorAxisRequirements);
        }
        if (i2 == 0 && (attribute = getAttributes().getAttribute(CSS.Attribute.TEXT_ALIGN)) != null) {
            String string = attribute.toString();
            if (string.equals("center")) {
                sizeRequirements.alignment = 0.5f;
            } else if (string.equals(JSplitPane.RIGHT)) {
                sizeRequirements.alignment = 1.0f;
            } else {
                sizeRequirements.alignment = 0.0f;
            }
        }
        return sizeRequirements;
    }

    boolean isPercentage(int i2, AttributeSet attributeSet) {
        if (i2 == 0) {
            if (this.cssWidth != null) {
                return this.cssWidth.isPercentage();
            }
            return false;
        }
        if (this.cssHeight != null) {
            return this.cssHeight.isPercentage();
        }
        return false;
    }

    static boolean spanSetFromAttributes(int i2, SizeRequirements sizeRequirements, CSS.LengthValue lengthValue, CSS.LengthValue lengthValue2) {
        if (i2 == 0) {
            if (lengthValue != null && !lengthValue.isPercentage()) {
                int value = (int) lengthValue.getValue();
                sizeRequirements.maximum = value;
                sizeRequirements.preferred = value;
                sizeRequirements.minimum = value;
                return true;
            }
            return false;
        }
        if (lengthValue2 != null && !lengthValue2.isPercentage()) {
            int value2 = (int) lengthValue2.getValue();
            sizeRequirements.maximum = value2;
            sizeRequirements.preferred = value2;
            sizeRequirements.minimum = value2;
            return true;
        }
        return false;
    }

    @Override // javax.swing.text.BoxView
    protected void layoutMinorAxis(int i2, int i3, int[] iArr, int[] iArr2) {
        int maximumSpan;
        int viewCount = getViewCount();
        CSS.Attribute attribute = i3 == 0 ? CSS.Attribute.WIDTH : CSS.Attribute.HEIGHT;
        for (int i4 = 0; i4 < viewCount; i4++) {
            View view = getView(i4);
            int minimumSpan = (int) view.getMinimumSpan(i3);
            CSS.LengthValue lengthValue = (CSS.LengthValue) view.getAttributes().getAttribute(attribute);
            if (lengthValue != null && lengthValue.isPercentage()) {
                minimumSpan = Math.max((int) lengthValue.getValue(i2), minimumSpan);
                maximumSpan = minimumSpan;
            } else {
                maximumSpan = (int) view.getMaximumSpan(i3);
            }
            if (maximumSpan < i2) {
                iArr[i4] = (int) ((i2 - maximumSpan) * view.getAlignment(i3));
                iArr2[i4] = maximumSpan;
            } else {
                iArr[i4] = 0;
                iArr2[i4] = Math.max(minimumSpan, i2);
            }
        }
    }

    @Override // javax.swing.text.BoxView, javax.swing.text.View
    public void paint(Graphics graphics, Shape shape) {
        this.painter.paint(graphics, r0.f12372x, r0.f12373y, r0.width, r0.height, this);
        super.paint(graphics, (Rectangle) shape);
    }

    @Override // javax.swing.text.View
    public AttributeSet getAttributes() {
        if (this.attr == null) {
            this.attr = getStyleSheet().getViewAttributes(this);
        }
        return this.attr;
    }

    @Override // javax.swing.text.BoxView, javax.swing.text.View
    public int getResizeWeight(int i2) {
        switch (i2) {
            case 0:
                return 1;
            case 1:
                return 0;
            default:
                throw new IllegalArgumentException("Invalid axis: " + i2);
        }
    }

    @Override // javax.swing.text.BoxView, javax.swing.text.View
    public float getAlignment(int i2) {
        switch (i2) {
            case 0:
                return 0.0f;
            case 1:
                if (getViewCount() == 0) {
                    return 0.0f;
                }
                float preferredSpan = getPreferredSpan(1);
                View view = getView(0);
                return ((int) preferredSpan) != 0 ? (view.getPreferredSpan(1) * view.getAlignment(1)) / preferredSpan : 0.0f;
            default:
                throw new IllegalArgumentException("Invalid axis: " + i2);
        }
    }

    @Override // javax.swing.text.View
    public void changedUpdate(DocumentEvent documentEvent, Shape shape, ViewFactory viewFactory) {
        super.changedUpdate(documentEvent, shape, viewFactory);
        int offset = documentEvent.getOffset();
        if (offset <= getStartOffset() && offset + documentEvent.getLength() >= getEndOffset()) {
            setPropertiesFromAttributes();
        }
    }

    @Override // javax.swing.text.BoxView, javax.swing.text.View
    public float getPreferredSpan(int i2) {
        return super.getPreferredSpan(i2);
    }

    @Override // javax.swing.text.BoxView, javax.swing.text.View
    public float getMinimumSpan(int i2) {
        return super.getMinimumSpan(i2);
    }

    @Override // javax.swing.text.BoxView, javax.swing.text.View
    public float getMaximumSpan(int i2) {
        return super.getMaximumSpan(i2);
    }

    protected void setPropertiesFromAttributes() {
        StyleSheet styleSheet = getStyleSheet();
        this.attr = styleSheet.getViewAttributes(this);
        this.painter = styleSheet.getBoxPainter(this.attr);
        if (this.attr != null) {
            setInsets((short) this.painter.getInset(1, this), (short) this.painter.getInset(2, this), (short) this.painter.getInset(3, this), (short) this.painter.getInset(4, this));
        }
        this.cssWidth = (CSS.LengthValue) this.attr.getAttribute(CSS.Attribute.WIDTH);
        this.cssHeight = (CSS.LengthValue) this.attr.getAttribute(CSS.Attribute.HEIGHT);
    }

    protected StyleSheet getStyleSheet() {
        return ((HTMLDocument) getDocument()).getStyleSheet();
    }

    private void constrainSize(int i2, SizeRequirements sizeRequirements, SizeRequirements sizeRequirements2) {
        if (sizeRequirements2.minimum > sizeRequirements.minimum) {
            int i3 = sizeRequirements2.minimum;
            sizeRequirements.preferred = i3;
            sizeRequirements.minimum = i3;
            sizeRequirements.maximum = Math.max(sizeRequirements.maximum, sizeRequirements2.maximum);
        }
    }
}
