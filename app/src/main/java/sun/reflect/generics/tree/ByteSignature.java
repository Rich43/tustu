package sun.reflect.generics.tree;

import sun.reflect.generics.visitor.TypeTreeVisitor;

/* loaded from: rt.jar:sun/reflect/generics/tree/ByteSignature.class */
public class ByteSignature implements BaseType {
    private static final ByteSignature singleton = new ByteSignature();

    private ByteSignature() {
    }

    public static ByteSignature make() {
        return singleton;
    }

    @Override // sun.reflect.generics.tree.TypeTree
    public void accept(TypeTreeVisitor<?> typeTreeVisitor) {
        typeTreeVisitor.visitByteSignature(this);
    }
}
