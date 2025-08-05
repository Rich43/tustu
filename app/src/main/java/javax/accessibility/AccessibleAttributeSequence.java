package javax.accessibility;

import javax.swing.text.AttributeSet;

/* loaded from: rt.jar:javax/accessibility/AccessibleAttributeSequence.class */
public class AccessibleAttributeSequence {
    public int startIndex;
    public int endIndex;
    public AttributeSet attributes;

    public AccessibleAttributeSequence(int i2, int i3, AttributeSet attributeSet) {
        this.startIndex = i2;
        this.endIndex = i3;
        this.attributes = attributeSet;
    }
}
