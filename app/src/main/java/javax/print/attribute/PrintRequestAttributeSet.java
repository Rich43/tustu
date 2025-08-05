package javax.print.attribute;

/* loaded from: rt.jar:javax/print/attribute/PrintRequestAttributeSet.class */
public interface PrintRequestAttributeSet extends AttributeSet {
    @Override // javax.print.attribute.AttributeSet
    boolean add(Attribute attribute);

    @Override // javax.print.attribute.AttributeSet
    boolean addAll(AttributeSet attributeSet);
}
