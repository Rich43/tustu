package sun.reflect.generics.tree;

import sun.reflect.generics.visitor.TypeTreeVisitor;

/* loaded from: rt.jar:sun/reflect/generics/tree/FormalTypeParameter.class */
public class FormalTypeParameter implements TypeTree {
    private final String name;
    private final FieldTypeSignature[] bounds;

    private FormalTypeParameter(String str, FieldTypeSignature[] fieldTypeSignatureArr) {
        this.name = str;
        this.bounds = fieldTypeSignatureArr;
    }

    public static FormalTypeParameter make(String str, FieldTypeSignature[] fieldTypeSignatureArr) {
        return new FormalTypeParameter(str, fieldTypeSignatureArr);
    }

    public FieldTypeSignature[] getBounds() {
        return this.bounds;
    }

    public String getName() {
        return this.name;
    }

    @Override // sun.reflect.generics.tree.TypeTree
    public void accept(TypeTreeVisitor<?> typeTreeVisitor) {
        typeTreeVisitor.visitFormalTypeParameter(this);
    }
}
