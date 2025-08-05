package javax.print.attribute;

/* loaded from: rt.jar:javax/print/attribute/AttributeSet.class */
public interface AttributeSet {
    Attribute get(Class<?> cls);

    boolean add(Attribute attribute);

    boolean remove(Class<?> cls);

    boolean remove(Attribute attribute);

    boolean containsKey(Class<?> cls);

    boolean containsValue(Attribute attribute);

    boolean addAll(AttributeSet attributeSet);

    int size();

    Attribute[] toArray();

    void clear();

    boolean isEmpty();

    boolean equals(Object obj);

    int hashCode();
}
