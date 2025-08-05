package sun.reflect.generics.tree;

import sun.reflect.generics.visitor.TypeTreeVisitor;

/* loaded from: rt.jar:sun/reflect/generics/tree/Wildcard.class */
public class Wildcard implements TypeArgument {
    private FieldTypeSignature[] upperBounds;
    private FieldTypeSignature[] lowerBounds;
    private static final FieldTypeSignature[] emptyBounds = new FieldTypeSignature[0];

    private Wildcard(FieldTypeSignature[] fieldTypeSignatureArr, FieldTypeSignature[] fieldTypeSignatureArr2) {
        this.upperBounds = fieldTypeSignatureArr;
        this.lowerBounds = fieldTypeSignatureArr2;
    }

    public static Wildcard make(FieldTypeSignature[] fieldTypeSignatureArr, FieldTypeSignature[] fieldTypeSignatureArr2) {
        return new Wildcard(fieldTypeSignatureArr, fieldTypeSignatureArr2);
    }

    public FieldTypeSignature[] getUpperBounds() {
        return this.upperBounds;
    }

    public FieldTypeSignature[] getLowerBounds() {
        if (this.lowerBounds.length == 1 && this.lowerBounds[0] == BottomSignature.make()) {
            return emptyBounds;
        }
        return this.lowerBounds;
    }

    @Override // sun.reflect.generics.tree.TypeTree
    public void accept(TypeTreeVisitor<?> typeTreeVisitor) {
        typeTreeVisitor.visitWildcard(this);
    }
}
