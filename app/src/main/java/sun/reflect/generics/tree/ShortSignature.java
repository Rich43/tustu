package sun.reflect.generics.tree;

import sun.reflect.generics.visitor.TypeTreeVisitor;

/* loaded from: rt.jar:sun/reflect/generics/tree/ShortSignature.class */
public class ShortSignature implements BaseType {
    private static final ShortSignature singleton = new ShortSignature();

    private ShortSignature() {
    }

    public static ShortSignature make() {
        return singleton;
    }

    @Override // sun.reflect.generics.tree.TypeTree
    public void accept(TypeTreeVisitor<?> typeTreeVisitor) {
        typeTreeVisitor.visitShortSignature(this);
    }
}
