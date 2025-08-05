package sun.reflect.generics.repository;

import sun.reflect.generics.factory.GenericsFactory;
import sun.reflect.generics.tree.Tree;
import sun.reflect.generics.visitor.Reifier;

/* loaded from: rt.jar:sun/reflect/generics/repository/AbstractRepository.class */
public abstract class AbstractRepository<T extends Tree> {
    private final GenericsFactory factory;
    private final T tree;

    protected abstract T parse(String str);

    private GenericsFactory getFactory() {
        return this.factory;
    }

    protected T getTree() {
        return this.tree;
    }

    protected Reifier getReifier() {
        return Reifier.make(getFactory());
    }

    protected AbstractRepository(String str, GenericsFactory genericsFactory) {
        this.tree = (T) parse(str);
        this.factory = genericsFactory;
    }
}
