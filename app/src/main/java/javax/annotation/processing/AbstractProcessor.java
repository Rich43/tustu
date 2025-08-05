package javax.annotation.processing;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;

/* loaded from: rt.jar:javax/annotation/processing/AbstractProcessor.class */
public abstract class AbstractProcessor implements Processor {
    protected ProcessingEnvironment processingEnv;
    private boolean initialized = false;
    static final /* synthetic */ boolean $assertionsDisabled;

    @Override // javax.annotation.processing.Processor
    public abstract boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment);

    static {
        $assertionsDisabled = !AbstractProcessor.class.desiredAssertionStatus();
    }

    protected AbstractProcessor() {
    }

    @Override // javax.annotation.processing.Processor
    public Set<String> getSupportedOptions() {
        SupportedOptions supportedOptions = (SupportedOptions) getClass().getAnnotation(SupportedOptions.class);
        if (supportedOptions == null) {
            return Collections.emptySet();
        }
        return arrayToSet(supportedOptions.value());
    }

    @Override // javax.annotation.processing.Processor
    public Set<String> getSupportedAnnotationTypes() {
        SupportedAnnotationTypes supportedAnnotationTypes = (SupportedAnnotationTypes) getClass().getAnnotation(SupportedAnnotationTypes.class);
        if (supportedAnnotationTypes == null) {
            if (isInitialized()) {
                this.processingEnv.getMessager().printMessage(Diagnostic.Kind.WARNING, "No SupportedAnnotationTypes annotation found on " + getClass().getName() + ", returning an empty set.");
            }
            return Collections.emptySet();
        }
        return arrayToSet(supportedAnnotationTypes.value());
    }

    @Override // javax.annotation.processing.Processor
    public SourceVersion getSupportedSourceVersion() {
        SourceVersion sourceVersionValue;
        SupportedSourceVersion supportedSourceVersion = (SupportedSourceVersion) getClass().getAnnotation(SupportedSourceVersion.class);
        if (supportedSourceVersion == null) {
            sourceVersionValue = SourceVersion.RELEASE_6;
            if (isInitialized()) {
                this.processingEnv.getMessager().printMessage(Diagnostic.Kind.WARNING, "No SupportedSourceVersion annotation found on " + getClass().getName() + ", returning " + ((Object) sourceVersionValue) + ".");
            }
        } else {
            sourceVersionValue = supportedSourceVersion.value();
        }
        return sourceVersionValue;
    }

    @Override // javax.annotation.processing.Processor
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        if (this.initialized) {
            throw new IllegalStateException("Cannot call init more than once.");
        }
        Objects.requireNonNull(processingEnvironment, "Tool provided null ProcessingEnvironment");
        this.processingEnv = processingEnvironment;
        this.initialized = true;
    }

    @Override // javax.annotation.processing.Processor
    public Iterable<? extends Completion> getCompletions(Element element, AnnotationMirror annotationMirror, ExecutableElement executableElement, String str) {
        return Collections.emptyList();
    }

    protected synchronized boolean isInitialized() {
        return this.initialized;
    }

    private static Set<String> arrayToSet(String[] strArr) {
        if (!$assertionsDisabled && strArr == null) {
            throw new AssertionError();
        }
        HashSet hashSet = new HashSet(strArr.length);
        for (String str : strArr) {
            hashSet.add(str);
        }
        return Collections.unmodifiableSet(hashSet);
    }
}
