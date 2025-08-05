package sun.reflect.generics.tree;

import sun.reflect.generics.visitor.TypeTreeVisitor;

/* loaded from: rt.jar:sun/reflect/generics/tree/VoidDescriptor.class */
public class VoidDescriptor implements ReturnType {
    private static final VoidDescriptor singleton = new VoidDescriptor();

    private VoidDescriptor() {
    }

    public static VoidDescriptor make() {
        return singleton;
    }

    @Override // sun.reflect.generics.tree.TypeTree
    public void accept(TypeTreeVisitor<?> typeTreeVisitor) {
        typeTreeVisitor.visitVoidDescriptor(this);
    }
}
