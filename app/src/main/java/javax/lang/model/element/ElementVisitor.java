package javax.lang.model.element;

/* loaded from: rt.jar:javax/lang/model/element/ElementVisitor.class */
public interface ElementVisitor<R, P> {
    R visit(Element element, P p2);

    R visit(Element element);

    R visitPackage(PackageElement packageElement, P p2);

    R visitType(TypeElement typeElement, P p2);

    R visitVariable(VariableElement variableElement, P p2);

    R visitExecutable(ExecutableElement executableElement, P p2);

    R visitTypeParameter(TypeParameterElement typeParameterElement, P p2);

    R visitUnknown(Element element, P p2);
}
