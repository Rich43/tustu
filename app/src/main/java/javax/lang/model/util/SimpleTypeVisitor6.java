package javax.lang.model.util;

import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.type.ArrayType;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.ErrorType;
import javax.lang.model.type.ExecutableType;
import javax.lang.model.type.NoType;
import javax.lang.model.type.NullType;
import javax.lang.model.type.PrimitiveType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.TypeVariable;
import javax.lang.model.type.WildcardType;

@SupportedSourceVersion(SourceVersion.RELEASE_6)
/* loaded from: rt.jar:javax/lang/model/util/SimpleTypeVisitor6.class */
public class SimpleTypeVisitor6<R, P> extends AbstractTypeVisitor6<R, P> {
    protected final R DEFAULT_VALUE;

    protected SimpleTypeVisitor6() {
        this.DEFAULT_VALUE = null;
    }

    protected SimpleTypeVisitor6(R r2) {
        this.DEFAULT_VALUE = r2;
    }

    protected R defaultAction(TypeMirror typeMirror, P p2) {
        return this.DEFAULT_VALUE;
    }

    @Override // javax.lang.model.type.TypeVisitor
    public R visitPrimitive(PrimitiveType primitiveType, P p2) {
        return defaultAction(primitiveType, p2);
    }

    @Override // javax.lang.model.type.TypeVisitor
    public R visitNull(NullType nullType, P p2) {
        return defaultAction(nullType, p2);
    }

    @Override // javax.lang.model.type.TypeVisitor
    public R visitArray(ArrayType arrayType, P p2) {
        return defaultAction(arrayType, p2);
    }

    @Override // javax.lang.model.type.TypeVisitor
    public R visitDeclared(DeclaredType declaredType, P p2) {
        return defaultAction(declaredType, p2);
    }

    @Override // javax.lang.model.type.TypeVisitor
    public R visitError(ErrorType errorType, P p2) {
        return defaultAction(errorType, p2);
    }

    @Override // javax.lang.model.type.TypeVisitor
    public R visitTypeVariable(TypeVariable typeVariable, P p2) {
        return defaultAction(typeVariable, p2);
    }

    @Override // javax.lang.model.type.TypeVisitor
    public R visitWildcard(WildcardType wildcardType, P p2) {
        return defaultAction(wildcardType, p2);
    }

    @Override // javax.lang.model.type.TypeVisitor
    public R visitExecutable(ExecutableType executableType, P p2) {
        return defaultAction(executableType, p2);
    }

    @Override // javax.lang.model.type.TypeVisitor
    public R visitNoType(NoType noType, P p2) {
        return defaultAction(noType, p2);
    }
}
