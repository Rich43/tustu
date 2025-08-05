package javax.print.attribute;

/* loaded from: rt.jar:javax/print/attribute/PrintJobAttributeSet.class */
public interface PrintJobAttributeSet extends AttributeSet {
    @Override // javax.print.attribute.AttributeSet
    boolean add(Attribute attribute);

    @Override // javax.print.attribute.AttributeSet
    boolean addAll(AttributeSet attributeSet);
}
