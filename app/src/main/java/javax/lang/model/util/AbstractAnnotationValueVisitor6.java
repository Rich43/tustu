package javax.lang.model.util;

import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.AnnotationValueVisitor;
import javax.lang.model.element.UnknownAnnotationValueException;

@SupportedSourceVersion(SourceVersion.RELEASE_6)
/* loaded from: rt.jar:javax/lang/model/util/AbstractAnnotationValueVisitor6.class */
public abstract class AbstractAnnotationValueVisitor6<R, P> implements AnnotationValueVisitor<R, P> {
    protected AbstractAnnotationValueVisitor6() {
    }

    @Override // javax.lang.model.element.AnnotationValueVisitor
    public final R visit(AnnotationValue annotationValue, P p2) {
        return (R) annotationValue.accept(this, p2);
    }

    @Override // javax.lang.model.element.AnnotationValueVisitor
    public final R visit(AnnotationValue annotationValue) {
        return (R) annotationValue.accept(this, null);
    }

    @Override // javax.lang.model.element.AnnotationValueVisitor
    public R visitUnknown(AnnotationValue annotationValue, P p2) {
        throw new UnknownAnnotationValueException(annotationValue, p2);
    }
}
