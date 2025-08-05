package javax.annotation.processing;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.tools.Diagnostic;

/* loaded from: rt.jar:javax/annotation/processing/Messager.class */
public interface Messager {
    void printMessage(Diagnostic.Kind kind, CharSequence charSequence);

    void printMessage(Diagnostic.Kind kind, CharSequence charSequence, Element element);

    void printMessage(Diagnostic.Kind kind, CharSequence charSequence, Element element, AnnotationMirror annotationMirror);

    void printMessage(Diagnostic.Kind kind, CharSequence charSequence, Element element, AnnotationMirror annotationMirror, AnnotationValue annotationValue);
}
