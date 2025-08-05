package javax.lang.model.util;

import java.util.List;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.VariableElement;

@SupportedSourceVersion(SourceVersion.RELEASE_7)
/* loaded from: rt.jar:javax/lang/model/util/ElementScanner7.class */
public class ElementScanner7<R, P> extends ElementScanner6<R, P> {
    protected ElementScanner7() {
        super(null);
    }

    protected ElementScanner7(R r2) {
        super(r2);
    }

    @Override // javax.lang.model.util.ElementScanner6, javax.lang.model.element.ElementVisitor
    public R visitVariable(VariableElement variableElement, P p2) {
        return scan(variableElement.getEnclosedElements(), (List<? extends Element>) p2);
    }
}
