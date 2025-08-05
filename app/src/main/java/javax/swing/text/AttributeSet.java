package javax.swing.text;

import java.util.Enumeration;

/* loaded from: rt.jar:javax/swing/text/AttributeSet.class */
public interface AttributeSet {
    public static final Object NameAttribute = StyleConstants.NameAttribute;
    public static final Object ResolveAttribute = StyleConstants.ResolveAttribute;

    /* loaded from: rt.jar:javax/swing/text/AttributeSet$CharacterAttribute.class */
    public interface CharacterAttribute {
    }

    /* loaded from: rt.jar:javax/swing/text/AttributeSet$ColorAttribute.class */
    public interface ColorAttribute {
    }

    /* loaded from: rt.jar:javax/swing/text/AttributeSet$FontAttribute.class */
    public interface FontAttribute {
    }

    /* loaded from: rt.jar:javax/swing/text/AttributeSet$ParagraphAttribute.class */
    public interface ParagraphAttribute {
    }

    int getAttributeCount();

    boolean isDefined(Object obj);

    boolean isEqual(AttributeSet attributeSet);

    AttributeSet copyAttributes();

    Object getAttribute(Object obj);

    Enumeration<?> getAttributeNames();

    boolean containsAttribute(Object obj, Object obj2);

    boolean containsAttributes(AttributeSet attributeSet);

    AttributeSet getResolveParent();
}
