package sun.reflect.generics.tree;

import sun.reflect.generics.visitor.TypeTreeVisitor;

/* loaded from: rt.jar:sun/reflect/generics/tree/BottomSignature.class */
public class BottomSignature implements FieldTypeSignature {
    private static final BottomSignature singleton = new BottomSignature();

    private BottomSignature() {
    }

    public static BottomSignature make() {
        return singleton;
    }

    @Override // sun.reflect.generics.tree.TypeTree
    public void accept(TypeTreeVisitor<?> typeTreeVisitor) {
        typeTreeVisitor.visitBottomSignature(this);
    }
}
