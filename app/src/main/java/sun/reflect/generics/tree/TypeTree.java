package sun.reflect.generics.tree;

import sun.reflect.generics.visitor.TypeTreeVisitor;

/* loaded from: rt.jar:sun/reflect/generics/tree/TypeTree.class */
public interface TypeTree extends Tree {
    void accept(TypeTreeVisitor<?> typeTreeVisitor);
}
