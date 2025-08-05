package sun.reflect.generics.tree;

import sun.reflect.generics.visitor.TypeTreeVisitor;

/* loaded from: rt.jar:sun/reflect/generics/tree/CharSignature.class */
public class CharSignature implements BaseType {
    private static final CharSignature singleton = new CharSignature();

    private CharSignature() {
    }

    public static CharSignature make() {
        return singleton;
    }

    @Override // sun.reflect.generics.tree.TypeTree
    public void accept(TypeTreeVisitor<?> typeTreeVisitor) {
        typeTreeVisitor.visitCharSignature(this);
    }
}
