package sun.reflect.generics.scope;

import java.lang.reflect.Constructor;

/* loaded from: rt.jar:sun/reflect/generics/scope/ConstructorScope.class */
public class ConstructorScope extends AbstractScope<Constructor<?>> {
    private ConstructorScope(Constructor<?> constructor) {
        super(constructor);
    }

    private Class<?> getEnclosingClass() {
        return getRecvr().getDeclaringClass();
    }

    @Override // sun.reflect.generics.scope.AbstractScope
    protected Scope computeEnclosingScope() {
        return ClassScope.make(getEnclosingClass());
    }

    public static ConstructorScope make(Constructor<?> constructor) {
        return new ConstructorScope(constructor);
    }
}
