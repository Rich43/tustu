package javax.lang.model;

import java.lang.annotation.Annotation;
import java.util.List;
import javax.lang.model.element.AnnotationMirror;

/* loaded from: rt.jar:javax/lang/model/AnnotatedConstruct.class */
public interface AnnotatedConstruct {
    List<? extends AnnotationMirror> getAnnotationMirrors();

    <A extends Annotation> A getAnnotation(Class<A> cls);

    <A extends Annotation> A[] getAnnotationsByType(Class<A> cls);
}
