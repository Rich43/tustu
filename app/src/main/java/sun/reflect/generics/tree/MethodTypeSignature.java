package sun.reflect.generics.tree;

import sun.reflect.generics.visitor.Visitor;

/* loaded from: rt.jar:sun/reflect/generics/tree/MethodTypeSignature.class */
public class MethodTypeSignature implements Signature {
    private final FormalTypeParameter[] formalTypeParams;
    private final TypeSignature[] parameterTypes;
    private final ReturnType returnType;
    private final FieldTypeSignature[] exceptionTypes;

    private MethodTypeSignature(FormalTypeParameter[] formalTypeParameterArr, TypeSignature[] typeSignatureArr, ReturnType returnType, FieldTypeSignature[] fieldTypeSignatureArr) {
        this.formalTypeParams = formalTypeParameterArr;
        this.parameterTypes = typeSignatureArr;
        this.returnType = returnType;
        this.exceptionTypes = fieldTypeSignatureArr;
    }

    public static MethodTypeSignature make(FormalTypeParameter[] formalTypeParameterArr, TypeSignature[] typeSignatureArr, ReturnType returnType, FieldTypeSignature[] fieldTypeSignatureArr) {
        return new MethodTypeSignature(formalTypeParameterArr, typeSignatureArr, returnType, fieldTypeSignatureArr);
    }

    @Override // sun.reflect.generics.tree.Signature
    public FormalTypeParameter[] getFormalTypeParameters() {
        return this.formalTypeParams;
    }

    public TypeSignature[] getParameterTypes() {
        return this.parameterTypes;
    }

    public ReturnType getReturnType() {
        return this.returnType;
    }

    public FieldTypeSignature[] getExceptionTypes() {
        return this.exceptionTypes;
    }

    public void accept(Visitor<?> visitor) {
        visitor.visitMethodTypeSignature(this);
    }
}
