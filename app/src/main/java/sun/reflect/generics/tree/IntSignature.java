package sun.reflect.generics.tree;

import sun.reflect.generics.visitor.TypeTreeVisitor;

/* loaded from: rt.jar:sun/reflect/generics/tree/IntSignature.class */
public class IntSignature implements BaseType {
    private static final IntSignature singleton = new IntSignature();

    private IntSignature() {
    }

    public static IntSignature make() {
        return singleton;
    }

    @Override // sun.reflect.generics.tree.TypeTree
    public void accept(TypeTreeVisitor<?> typeTreeVisitor) {
        typeTreeVisitor.visitIntSignature(this);
    }
}
