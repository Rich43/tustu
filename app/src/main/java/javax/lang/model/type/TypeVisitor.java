package javax.lang.model.type;

/* loaded from: rt.jar:javax/lang/model/type/TypeVisitor.class */
public interface TypeVisitor<R, P> {
    R visit(TypeMirror typeMirror, P p2);

    R visit(TypeMirror typeMirror);

    R visitPrimitive(PrimitiveType primitiveType, P p2);

    R visitNull(NullType nullType, P p2);

    R visitArray(ArrayType arrayType, P p2);

    R visitDeclared(DeclaredType declaredType, P p2);

    R visitError(ErrorType errorType, P p2);

    R visitTypeVariable(TypeVariable typeVariable, P p2);

    R visitWildcard(WildcardType wildcardType, P p2);

    R visitExecutable(ExecutableType executableType, P p2);

    R visitNoType(NoType noType, P p2);

    R visitUnknown(TypeMirror typeMirror, P p2);

    R visitUnion(UnionType unionType, P p2);

    R visitIntersection(IntersectionType intersectionType, P p2);
}
