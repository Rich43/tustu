package sun.reflect.generics.tree;

import sun.reflect.generics.visitor.TypeTreeVisitor;

/* loaded from: rt.jar:sun/reflect/generics/tree/DoubleSignature.class */
public class DoubleSignature implements BaseType {
    private static final DoubleSignature singleton = new DoubleSignature();

    private DoubleSignature() {
    }

    public static DoubleSignature make() {
        return singleton;
    }

    @Override // sun.reflect.generics.tree.TypeTree
    public void accept(TypeTreeVisitor<?> typeTreeVisitor) {
        typeTreeVisitor.visitDoubleSignature(this);
    }
}
