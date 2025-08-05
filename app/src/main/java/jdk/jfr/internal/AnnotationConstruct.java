package jdk.jfr.internal;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Collections;
import java.util.List;
import jdk.jfr.AnnotationElement;
import jdk.jfr.Description;
import jdk.jfr.Label;
import jdk.jfr.Unsigned;

/* loaded from: jfr.jar:jdk/jfr/internal/AnnotationConstruct.class */
public final class AnnotationConstruct {
    private List<AnnotationElement> annotationElements;
    private byte unsignedFlag;

    /* loaded from: jfr.jar:jdk/jfr/internal/AnnotationConstruct$AnnotationInvokationHandler.class */
    private static final class AnnotationInvokationHandler implements InvocationHandler {
        private final AnnotationElement annotationElement;

        AnnotationInvokationHandler(AnnotationElement annotationElement) {
            this.annotationElement = annotationElement;
        }

        @Override // java.lang.reflect.InvocationHandler
        public Object invoke(Object obj, Method method, Object[] objArr) throws Throwable {
            String name = method.getName();
            if (method.getTypeParameters().length == 0 && this.annotationElement.hasValue(name)) {
                return this.annotationElement.getValue(name);
            }
            throw new UnsupportedOperationException("Flight Recorder proxy only supports members declared in annotation interfaces, i.e. not toString, equals etc.");
        }
    }

    public AnnotationConstruct(List<AnnotationElement> list) {
        this.annotationElements = Collections.emptyList();
        this.unsignedFlag = (byte) -1;
        this.annotationElements = list;
    }

    public AnnotationConstruct() {
        this.annotationElements = Collections.emptyList();
        this.unsignedFlag = (byte) -1;
    }

    public void setAnnotationElements(List<AnnotationElement> list) {
        this.annotationElements = Utils.smallUnmodifiable(list);
    }

    public String getLabel() {
        Label label = (Label) getAnnotation(Label.class);
        if (label == null) {
            return null;
        }
        return label.value();
    }

    public String getDescription() {
        Description description = (Description) getAnnotation(Description.class);
        if (description == null) {
            return null;
        }
        return description.value();
    }

    public final <T> T getAnnotation(Class<? extends Annotation> cls) {
        AnnotationElement annotationElement = getAnnotationElement(cls);
        if (annotationElement != null) {
            return (T) Proxy.newProxyInstance(cls.getClassLoader(), new Class[]{cls}, new AnnotationInvokationHandler(annotationElement));
        }
        return null;
    }

    public List<AnnotationElement> getUnmodifiableAnnotationElements() {
        return this.annotationElements;
    }

    boolean remove(AnnotationElement annotationElement) {
        return this.annotationElements.remove(annotationElement);
    }

    private AnnotationElement getAnnotationElement(Class<? extends Annotation> cls) {
        long typeId = Type.getTypeId(cls);
        String name = cls.getName();
        for (AnnotationElement annotationElement : getUnmodifiableAnnotationElements()) {
            if (annotationElement.getTypeId() == typeId && annotationElement.getTypeName().equals(name)) {
                return annotationElement;
            }
        }
        for (AnnotationElement annotationElement2 : getUnmodifiableAnnotationElements()) {
            if (annotationElement2.getTypeName().equals(name)) {
                return annotationElement2;
            }
        }
        return null;
    }

    public boolean hasUnsigned() {
        if (this.unsignedFlag < 0) {
            this.unsignedFlag = (byte) (((Unsigned) getAnnotation(Unsigned.class)) == null ? 0 : 1);
        }
        return this.unsignedFlag == 1;
    }
}
