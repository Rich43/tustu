package sun.reflect.generics.scope;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/* loaded from: rt.jar:sun/reflect/generics/scope/ClassScope.class */
public class ClassScope extends AbstractScope<Class<?>> implements Scope {
    private ClassScope(Class<?> cls) {
        super(cls);
    }

    @Override // sun.reflect.generics.scope.AbstractScope
    protected Scope computeEnclosingScope() throws SecurityException {
        Class<?> recvr = getRecvr();
        Method enclosingMethod = recvr.getEnclosingMethod();
        if (enclosingMethod != null) {
            return MethodScope.make(enclosingMethod);
        }
        Constructor<?> enclosingConstructor = recvr.getEnclosingConstructor();
        if (enclosingConstructor != null) {
            return ConstructorScope.make(enclosingConstructor);
        }
        Class<?> enclosingClass = recvr.getEnclosingClass();
        if (enclosingClass != null) {
            return make(enclosingClass);
        }
        return DummyScope.make();
    }

    public static ClassScope make(Class<?> cls) {
        return new ClassScope(cls);
    }
}
