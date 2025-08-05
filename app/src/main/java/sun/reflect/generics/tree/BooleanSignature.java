package sun.reflect.generics.tree;

import sun.reflect.generics.visitor.TypeTreeVisitor;

/* loaded from: rt.jar:sun/reflect/generics/tree/BooleanSignature.class */
public class BooleanSignature implements BaseType {
    private static final BooleanSignature singleton = new BooleanSignature();

    private BooleanSignature() {
    }

    public static BooleanSignature make() {
        return singleton;
    }

    @Override // sun.reflect.generics.tree.TypeTree
    public void accept(TypeTreeVisitor<?> typeTreeVisitor) {
        typeTreeVisitor.visitBooleanSignature(this);
    }
}
