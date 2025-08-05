package javax.lang.model.element;

/* loaded from: rt.jar:javax/lang/model/element/AnnotationValue.class */
public interface AnnotationValue {
    Object getValue();

    String toString();

    <R, P> R accept(AnnotationValueVisitor<R, P> annotationValueVisitor, P p2);
}
