package javax.lang.model.util;

import javax.lang.model.type.UnionType;

/* loaded from: rt.jar:javax/lang/model/util/AbstractTypeVisitor7.class */
public abstract class AbstractTypeVisitor7<R, P> extends AbstractTypeVisitor6<R, P> {
    @Override // javax.lang.model.util.AbstractTypeVisitor6, javax.lang.model.type.TypeVisitor
    public abstract R visitUnion(UnionType unionType, P p2);

    protected AbstractTypeVisitor7() {
    }
}
