package javax.annotation.processing;

import java.util.Set;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;

/* loaded from: rt.jar:javax/annotation/processing/Processor.class */
public interface Processor {
    Set<String> getSupportedOptions();

    Set<String> getSupportedAnnotationTypes();

    SourceVersion getSupportedSourceVersion();

    void init(ProcessingEnvironment processingEnvironment);

    boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment);

    Iterable<? extends Completion> getCompletions(Element element, AnnotationMirror annotationMirror, ExecutableElement executableElement, String str);
}
