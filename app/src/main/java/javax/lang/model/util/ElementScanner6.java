package javax.lang.model.util;

import java.util.Iterator;
import java.util.List;
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
/* loaded from: rt.jar:javax/lang/model/util/ElementScanner6.class */
public class ElementScanner6<R, P> extends AbstractElementVisitor6<R, P> {
    protected final R DEFAULT_VALUE;

    protected ElementScanner6() {
        this.DEFAULT_VALUE = null;
    }

    protected ElementScanner6(R r2) {
        this.DEFAULT_VALUE = r2;
    }

    public final R scan(Iterable<? extends Element> iterable, P p2) {
        R rScan = this.DEFAULT_VALUE;
        Iterator<? extends Element> it = iterable.iterator();
        while (it.hasNext()) {
            rScan = scan(it.next(), (Element) p2);
        }
        return rScan;
    }

    public R scan(Element element, P p2) {
        return (R) element.accept(this, p2);
    }

    public final R scan(Element element) {
        return scan(element, (Element) null);
    }

    @Override // javax.lang.model.element.ElementVisitor
    public R visitPackage(PackageElement packageElement, P p2) {
        return scan(packageElement.getEnclosedElements(), (List<? extends Element>) p2);
    }

    @Override // javax.lang.model.element.ElementVisitor
    public R visitType(TypeElement typeElement, P p2) {
        return scan(typeElement.getEnclosedElements(), (List<? extends Element>) p2);
    }

    @Override // javax.lang.model.element.ElementVisitor
    public R visitVariable(VariableElement variableElement, P p2) {
        if (variableElement.getKind() != ElementKind.RESOURCE_VARIABLE) {
            return scan(variableElement.getEnclosedElements(), (List<? extends Element>) p2);
        }
        return visitUnknown(variableElement, p2);
    }

    @Override // javax.lang.model.element.ElementVisitor
    public R visitExecutable(ExecutableElement executableElement, P p2) {
        return scan(executableElement.getParameters(), (List<? extends VariableElement>) p2);
    }

    @Override // javax.lang.model.element.ElementVisitor
    public R visitTypeParameter(TypeParameterElement typeParameterElement, P p2) {
        return scan(typeParameterElement.getEnclosedElements(), (List<? extends Element>) p2);
    }
}
