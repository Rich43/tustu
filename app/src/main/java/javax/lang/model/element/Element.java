package javax.lang.model.element;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Set;
import javax.lang.model.AnnotatedConstruct;
import javax.lang.model.type.TypeMirror;

/* loaded from: rt.jar:javax/lang/model/element/Element.class */
public interface Element extends AnnotatedConstruct {
    TypeMirror asType();

    ElementKind getKind();

    Set<Modifier> getModifiers();

    Name getSimpleName();

    Element getEnclosingElement();

    List<? extends Element> getEnclosedElements();

    boolean equals(Object obj);

    int hashCode();

    @Override // javax.lang.model.AnnotatedConstruct
    List<? extends AnnotationMirror> getAnnotationMirrors();

    @Override // javax.lang.model.AnnotatedConstruct
    <A extends Annotation> A getAnnotation(Class<A> cls);

    <R, P> R accept(ElementVisitor<R, P> elementVisitor, P p2);
}
