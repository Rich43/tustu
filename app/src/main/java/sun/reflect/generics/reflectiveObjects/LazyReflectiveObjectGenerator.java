package sun.reflect.generics.reflectiveObjects;

import sun.reflect.generics.factory.GenericsFactory;
import sun.reflect.generics.visitor.Reifier;

/* loaded from: rt.jar:sun/reflect/generics/reflectiveObjects/LazyReflectiveObjectGenerator.class */
public abstract class LazyReflectiveObjectGenerator {
    private final GenericsFactory factory;

    protected LazyReflectiveObjectGenerator(GenericsFactory genericsFactory) {
        this.factory = genericsFactory;
    }

    private GenericsFactory getFactory() {
        return this.factory;
    }

    protected Reifier getReifier() {
        return Reifier.make(getFactory());
    }
}
