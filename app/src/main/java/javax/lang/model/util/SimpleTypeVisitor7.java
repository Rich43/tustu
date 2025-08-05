package javax.lang.model.util;

import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.type.UnionType;

@SupportedSourceVersion(SourceVersion.RELEASE_7)
/* loaded from: rt.jar:javax/lang/model/util/SimpleTypeVisitor7.class */
public class SimpleTypeVisitor7<R, P> extends SimpleTypeVisitor6<R, P> {
    protected SimpleTypeVisitor7() {
        super(null);
    }

    protected SimpleTypeVisitor7(R r2) {
        super(r2);
    }

    @Override // javax.lang.model.util.AbstractTypeVisitor6, javax.lang.model.type.TypeVisitor
    public R visitUnion(UnionType unionType, P p2) {
        return defaultAction(unionType, p2);
    }
}
