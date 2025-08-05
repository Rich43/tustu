package sun.reflect.generics.tree;

import sun.reflect.generics.visitor.TypeTreeVisitor;

/* loaded from: rt.jar:sun/reflect/generics/tree/SimpleClassTypeSignature.class */
public class SimpleClassTypeSignature implements FieldTypeSignature {
    private final boolean dollar;
    private final String name;
    private final TypeArgument[] typeArgs;

    private SimpleClassTypeSignature(String str, boolean z2, TypeArgument[] typeArgumentArr) {
        this.name = str;
        this.dollar = z2;
        this.typeArgs = typeArgumentArr;
    }

    public static SimpleClassTypeSignature make(String str, boolean z2, TypeArgument[] typeArgumentArr) {
        return new SimpleClassTypeSignature(str, z2, typeArgumentArr);
    }

    public boolean getDollar() {
        return this.dollar;
    }

    public String getName() {
        return this.name;
    }

    public TypeArgument[] getTypeArguments() {
        return this.typeArgs;
    }

    @Override // sun.reflect.generics.tree.TypeTree
    public void accept(TypeTreeVisitor<?> typeTreeVisitor) {
        typeTreeVisitor.visitSimpleClassTypeSignature(this);
    }
}
