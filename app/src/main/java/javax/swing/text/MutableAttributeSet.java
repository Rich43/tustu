package javax.swing.text;

import java.util.Enumeration;

/* loaded from: rt.jar:javax/swing/text/MutableAttributeSet.class */
public interface MutableAttributeSet extends AttributeSet {
    void addAttribute(Object obj, Object obj2);

    void addAttributes(AttributeSet attributeSet);

    void removeAttribute(Object obj);

    void removeAttributes(Enumeration<?> enumeration);

    void removeAttributes(AttributeSet attributeSet);

    void setResolveParent(AttributeSet attributeSet);
}
