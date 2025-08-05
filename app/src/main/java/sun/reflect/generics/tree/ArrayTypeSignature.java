package sun.reflect.generics.tree;

import sun.reflect.generics.visitor.TypeTreeVisitor;

/* loaded from: rt.jar:sun/reflect/generics/tree/ArrayTypeSignature.class */
public class ArrayTypeSignature implements FieldTypeSignature {
    private final TypeSignature componentType;

    private ArrayTypeSignature(TypeSignature typeSignature) {
        this.componentType = typeSignature;
    }

    public static ArrayTypeSignature make(TypeSignature typeSignature) {
        return new ArrayTypeSignature(typeSignature);
    }

    public TypeSignature getComponentType() {
        return this.componentType;
    }

    @Override // sun.reflect.generics.tree.TypeTree
    public void accept(TypeTreeVisitor<?> typeTreeVisitor) {
        typeTreeVisitor.visitArrayTypeSignature(this);
    }
}
