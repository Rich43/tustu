package sun.reflect.generics.tree;

import sun.reflect.generics.visitor.TypeTreeVisitor;

/* loaded from: rt.jar:sun/reflect/generics/tree/TypeVariableSignature.class */
public class TypeVariableSignature implements FieldTypeSignature {
    private final String identifier;

    private TypeVariableSignature(String str) {
        this.identifier = str;
    }

    public static TypeVariableSignature make(String str) {
        return new TypeVariableSignature(str);
    }

    public String getIdentifier() {
        return this.identifier;
    }

    @Override // sun.reflect.generics.tree.TypeTree
    public void accept(TypeTreeVisitor<?> typeTreeVisitor) {
        typeTreeVisitor.visitTypeVariableSignature(this);
    }
}
