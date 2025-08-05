package javax.print.attribute;

/* loaded from: rt.jar:javax/print/attribute/PrintServiceAttributeSet.class */
public interface PrintServiceAttributeSet extends AttributeSet {
    @Override // javax.print.attribute.AttributeSet
    boolean add(Attribute attribute);

    @Override // javax.print.attribute.AttributeSet
    boolean addAll(AttributeSet attributeSet);
}
