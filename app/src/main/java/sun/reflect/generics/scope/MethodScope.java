package sun.reflect.generics.scope;

import java.lang.reflect.Method;

/* loaded from: rt.jar:sun/reflect/generics/scope/MethodScope.class */
public class MethodScope extends AbstractScope<Method> {
    private MethodScope(Method method) {
        super(method);
    }

    private Class<?> getEnclosingClass() {
        return getRecvr().getDeclaringClass();
    }

    @Override // sun.reflect.generics.scope.AbstractScope
    protected Scope computeEnclosingScope() {
        return ClassScope.make(getEnclosingClass());
    }

    public static MethodScope make(Method method) {
        return new MethodScope(method);
    }
}
