package sun.reflect.generics.tree;

import java.util.List;
import sun.reflect.generics.visitor.TypeTreeVisitor;

/* loaded from: rt.jar:sun/reflect/generics/tree/ClassTypeSignature.class */
public class ClassTypeSignature implements FieldTypeSignature {
    private final List<SimpleClassTypeSignature> path;

    private ClassTypeSignature(List<SimpleClassTypeSignature> list) {
        this.path = list;
    }

    public static ClassTypeSignature make(List<SimpleClassTypeSignature> list) {
        return new ClassTypeSignature(list);
    }

    public List<SimpleClassTypeSignature> getPath() {
        return this.path;
    }

    @Override // sun.reflect.generics.tree.TypeTree
    public void accept(TypeTreeVisitor<?> typeTreeVisitor) {
        typeTreeVisitor.visitClassTypeSignature(this);
    }
}
