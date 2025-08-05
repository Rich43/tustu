package sun.reflect.misc;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.AccessController;

/* compiled from: MethodUtil.java */
/* loaded from: rt.jar:sun/reflect/misc/Trampoline.class */
class Trampoline {
    Trampoline() {
    }

    static {
        if (Trampoline.class.getClassLoader() == null) {
            throw new Error("Trampoline must not be defined by the bootstrap classloader");
        }
    }

    private static void ensureInvocableMethod(Method method) throws InvocationTargetException {
        Class<?> declaringClass = method.getDeclaringClass();
        if (declaringClass.equals(AccessController.class) || declaringClass.equals(Method.class) || declaringClass.getName().startsWith("java.lang.invoke.")) {
            throw new InvocationTargetException(new UnsupportedOperationException("invocation not supported"));
        }
    }

    private static Object invoke(Method method, Object obj, Object[] objArr) throws IllegalAccessException, InvocationTargetException {
        ensureInvocableMethod(method);
        return method.invoke(obj, objArr);
    }
}
