package javax.lang.model.util;

import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.VariableElement;

@SupportedSourceVersion(SourceVersion.RELEASE_7)
/* loaded from: rt.jar:javax/lang/model/util/ElementKindVisitor7.class */
public class ElementKindVisitor7<R, P> extends ElementKindVisitor6<R, P> {
    protected ElementKindVisitor7() {
        super(null);
    }

    protected ElementKindVisitor7(R r2) {
        super(r2);
    }

    @Override // javax.lang.model.util.ElementKindVisitor6
    public R visitVariableAsResourceVariable(VariableElement variableElement, P p2) {
        return defaultAction(variableElement, p2);
    }
}
