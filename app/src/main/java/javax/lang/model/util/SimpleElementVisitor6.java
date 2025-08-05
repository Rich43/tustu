package javax.lang.model.util;

import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.element.VariableElement;

@SupportedSourceVersion(SourceVersion.RELEASE_6)
/* loaded from: rt.jar:javax/lang/model/util/SimpleElementVisitor6.class */
public class SimpleElementVisitor6<R, P> extends AbstractElementVisitor6<R, P> {
    protected final R DEFAULT_VALUE;

    protected SimpleElementVisitor6() {
        this.DEFAULT_VALUE = null;
    }

    protected SimpleElementVisitor6(R r2) {
        this.DEFAULT_VALUE = r2;
    }

    protected R defaultAction(Element element, P p2) {
        return this.DEFAULT_VALUE;
    }

    @Override // javax.lang.model.element.ElementVisitor
    public R visitPackage(PackageElement packageElement, P p2) {
        return defaultAction(packageElement, p2);
    }

    @Override // javax.lang.model.element.ElementVisitor
    public R visitType(TypeElement typeElement, P p2) {
        return defaultAction(typeElement, p2);
    }

    @Override // javax.lang.model.element.ElementVisitor
    public R visitVariable(VariableElement variableElement, P p2) {
        if (variableElement.getKind() != ElementKind.RESOURCE_VARIABLE) {
            return defaultAction(variableElement, p2);
        }
        return visitUnknown(variableElement, p2);
    }

    @Override // javax.lang.model.element.ElementVisitor
    public R visitExecutable(ExecutableElement executableElement, P p2) {
        return defaultAction(executableElement, p2);
    }

    @Override // javax.lang.model.element.ElementVisitor
    public R visitTypeParameter(TypeParameterElement typeParameterElement, P p2) {
        return defaultAction(typeParameterElement, p2);
    }
}
