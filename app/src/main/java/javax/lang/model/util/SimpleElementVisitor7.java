package javax.lang.model.util;

import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.VariableElement;

@SupportedSourceVersion(SourceVersion.RELEASE_7)
/* loaded from: rt.jar:javax/lang/model/util/SimpleElementVisitor7.class */
public class SimpleElementVisitor7<R, P> extends SimpleElementVisitor6<R, P> {
    protected SimpleElementVisitor7() {
        super(null);
    }

    protected SimpleElementVisitor7(R r2) {
        super(r2);
    }

    @Override // javax.lang.model.util.SimpleElementVisitor6, javax.lang.model.element.ElementVisitor
    public R visitVariable(VariableElement variableElement, P p2) {
        return defaultAction(variableElement, p2);
    }
}
