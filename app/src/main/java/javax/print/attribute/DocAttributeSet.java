package javax.print.attribute;

/* loaded from: rt.jar:javax/print/attribute/DocAttributeSet.class */
public interface DocAttributeSet extends AttributeSet {
    @Override // javax.print.attribute.AttributeSet
    boolean add(Attribute attribute);

    @Override // javax.print.attribute.AttributeSet
    boolean addAll(AttributeSet attributeSet);
}
