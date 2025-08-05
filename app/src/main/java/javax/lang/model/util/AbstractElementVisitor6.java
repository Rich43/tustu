package javax.lang.model.util;

import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementVisitor;
import javax.lang.model.element.UnknownElementException;

@SupportedSourceVersion(SourceVersion.RELEASE_6)
/* loaded from: rt.jar:javax/lang/model/util/AbstractElementVisitor6.class */
public abstract class AbstractElementVisitor6<R, P> implements ElementVisitor<R, P> {
    protected AbstractElementVisitor6() {
    }

    @Override // javax.lang.model.element.ElementVisitor
    public final R visit(Element element, P p2) {
        return (R) element.accept(this, p2);
    }

    @Override // javax.lang.model.element.ElementVisitor
    public final R visit(Element element) {
        return (R) element.accept(this, null);
    }

    @Override // javax.lang.model.element.ElementVisitor
    public R visitUnknown(Element element, P p2) {
        throw new UnknownElementException(element, p2);
    }
}
