package sun.reflect.generics.tree;

import sun.reflect.generics.visitor.TypeTreeVisitor;

/* loaded from: rt.jar:sun/reflect/generics/tree/LongSignature.class */
public class LongSignature implements BaseType {
    private static final LongSignature singleton = new LongSignature();

    private LongSignature() {
    }

    public static LongSignature make() {
        return singleton;
    }

    @Override // sun.reflect.generics.tree.TypeTree
    public void accept(TypeTreeVisitor<?> typeTreeVisitor) {
        typeTreeVisitor.visitLongSignature(this);
    }
}
