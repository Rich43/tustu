package sun.reflect.generics.tree;

import sun.reflect.generics.visitor.Visitor;

/* loaded from: rt.jar:sun/reflect/generics/tree/ClassSignature.class */
public class ClassSignature implements Signature {
    private final FormalTypeParameter[] formalTypeParams;
    private final ClassTypeSignature superclass;
    private final ClassTypeSignature[] superInterfaces;

    private ClassSignature(FormalTypeParameter[] formalTypeParameterArr, ClassTypeSignature classTypeSignature, ClassTypeSignature[] classTypeSignatureArr) {
        this.formalTypeParams = formalTypeParameterArr;
        this.superclass = classTypeSignature;
        this.superInterfaces = classTypeSignatureArr;
    }

    public static ClassSignature make(FormalTypeParameter[] formalTypeParameterArr, ClassTypeSignature classTypeSignature, ClassTypeSignature[] classTypeSignatureArr) {
        return new ClassSignature(formalTypeParameterArr, classTypeSignature, classTypeSignatureArr);
    }

    @Override // sun.reflect.generics.tree.Signature
    public FormalTypeParameter[] getFormalTypeParameters() {
        return this.formalTypeParams;
    }

    public ClassTypeSignature getSuperclass() {
        return this.superclass;
    }

    public ClassTypeSignature[] getSuperInterfaces() {
        return this.superInterfaces;
    }

    public void accept(Visitor<?> visitor) {
        visitor.visitClassSignature(this);
    }
}
