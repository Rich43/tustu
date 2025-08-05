package javax.lang.model.element;

import javax.lang.model.UnknownEntityException;

/* loaded from: rt.jar:javax/lang/model/element/UnknownAnnotationValueException.class */
public class UnknownAnnotationValueException extends UnknownEntityException {
    private static final long serialVersionUID = 269;

    /* renamed from: av, reason: collision with root package name */
    private transient AnnotationValue f12779av;
    private transient Object parameter;

    public UnknownAnnotationValueException(AnnotationValue annotationValue, Object obj) {
        super("Unknown annotation value: " + ((Object) annotationValue));
        this.f12779av = annotationValue;
        this.parameter = obj;
    }

    public AnnotationValue getUnknownAnnotationValue() {
        return this.f12779av;
    }

    public Object getArgument() {
        return this.parameter;
    }
}
