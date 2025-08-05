package javax.annotation.processing;

import java.lang.annotation.Annotation;
import java.util.Set;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;

/* loaded from: rt.jar:javax/annotation/processing/RoundEnvironment.class */
public interface RoundEnvironment {
    boolean processingOver();

    boolean errorRaised();

    Set<? extends Element> getRootElements();

    Set<? extends Element> getElementsAnnotatedWith(TypeElement typeElement);

    Set<? extends Element> getElementsAnnotatedWith(Class<? extends Annotation> cls);
}
