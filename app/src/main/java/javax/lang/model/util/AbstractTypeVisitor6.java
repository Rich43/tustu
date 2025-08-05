package javax.lang.model.util;

import javax.lang.model.type.IntersectionType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.TypeVisitor;
import javax.lang.model.type.UnionType;
import javax.lang.model.type.UnknownTypeException;

/* loaded from: rt.jar:javax/lang/model/util/AbstractTypeVisitor6.class */
public abstract class AbstractTypeVisitor6<R, P> implements TypeVisitor<R, P> {
    protected AbstractTypeVisitor6() {
    }

    @Override // javax.lang.model.type.TypeVisitor
    public final R visit(TypeMirror typeMirror, P p2) {
        return (R) typeMirror.accept(this, p2);
    }

    @Override // javax.lang.model.type.TypeVisitor
    public final R visit(TypeMirror typeMirror) {
        return (R) typeMirror.accept(this, null);
    }

    @Override // javax.lang.model.type.TypeVisitor
    public R visitUnion(UnionType unionType, P p2) {
        return visitUnknown(unionType, p2);
    }

    @Override // javax.lang.model.type.TypeVisitor
    public R visitIntersection(IntersectionType intersectionType, P p2) {
        return visitUnknown(intersectionType, p2);
    }

    @Override // javax.lang.model.type.TypeVisitor
    public R visitUnknown(TypeMirror typeMirror, P p2) {
        throw new UnknownTypeException(typeMirror, p2);
    }
}
