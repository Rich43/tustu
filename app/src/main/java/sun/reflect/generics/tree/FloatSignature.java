package sun.reflect.generics.tree;

import sun.reflect.generics.visitor.TypeTreeVisitor;

/* loaded from: rt.jar:sun/reflect/generics/tree/FloatSignature.class */
public class FloatSignature implements BaseType {
    private static final FloatSignature singleton = new FloatSignature();

    private FloatSignature() {
    }

    public static FloatSignature make() {
        return singleton;
    }

    @Override // sun.reflect.generics.tree.TypeTree
    public void accept(TypeTreeVisitor<?> typeTreeVisitor) {
        typeTreeVisitor.visitFloatSignature(this);
    }
}
