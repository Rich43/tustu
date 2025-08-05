package javax.lang.model.util;

import javax.lang.model.type.IntersectionType;

/* loaded from: rt.jar:javax/lang/model/util/AbstractTypeVisitor8.class */
public abstract class AbstractTypeVisitor8<R, P> extends AbstractTypeVisitor7<R, P> {
    @Override // javax.lang.model.util.AbstractTypeVisitor6, javax.lang.model.type.TypeVisitor
    public abstract R visitIntersection(IntersectionType intersectionType, P p2);

    protected AbstractTypeVisitor8() {
    }
}
