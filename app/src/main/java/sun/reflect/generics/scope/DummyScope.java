package sun.reflect.generics.scope;

import java.lang.reflect.TypeVariable;

/* loaded from: rt.jar:sun/reflect/generics/scope/DummyScope.class */
public class DummyScope implements Scope {
    private static final DummyScope singleton = new DummyScope();

    private DummyScope() {
    }

    public static DummyScope make() {
        return singleton;
    }

    @Override // sun.reflect.generics.scope.Scope
    public TypeVariable<?> lookup(String str) {
        return null;
    }
}
