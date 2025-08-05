package sun.nio.ch;

import java.io.IOException;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedAction;

/* loaded from: rt.jar:sun/nio/ch/Reflect.class */
class Reflect {
    private Reflect() {
    }

    /* loaded from: rt.jar:sun/nio/ch/Reflect$ReflectionError.class */
    private static class ReflectionError extends Error {
        private static final long serialVersionUID = -8659519328078164097L;

        ReflectionError(Throwable th) {
            super(th);
        }
    }

    private static void setAccessible(final AccessibleObject accessibleObject) {
        AccessController.doPrivileged(new PrivilegedAction<Void>() { // from class: sun.nio.ch.Reflect.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public Void run2() throws SecurityException {
                accessibleObject.setAccessible(true);
                return null;
            }
        });
    }

    static Constructor<?> lookupConstructor(String str, Class<?>[] clsArr) {
        try {
            Constructor<?> declaredConstructor = Class.forName(str).getDeclaredConstructor(clsArr);
            setAccessible(declaredConstructor);
            return declaredConstructor;
        } catch (ClassNotFoundException | NoSuchMethodException e2) {
            throw new ReflectionError(e2);
        }
    }

    static Object invoke(Constructor<?> constructor, Object[] objArr) {
        try {
            return constructor.newInstance(objArr);
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException e2) {
            throw new ReflectionError(e2);
        }
    }

    static Method lookupMethod(String str, String str2, Class... clsArr) {
        try {
            Method declaredMethod = Class.forName(str).getDeclaredMethod(str2, clsArr);
            setAccessible(declaredMethod);
            return declaredMethod;
        } catch (ClassNotFoundException | NoSuchMethodException e2) {
            throw new ReflectionError(e2);
        }
    }

    static Object invoke(Method method, Object obj, Object[] objArr) {
        try {
            return method.invoke(obj, objArr);
        } catch (IllegalAccessException | InvocationTargetException e2) {
            throw new ReflectionError(e2);
        }
    }

    static Object invokeIO(Method method, Object obj, Object[] objArr) throws IOException {
        try {
            return method.invoke(obj, objArr);
        } catch (IllegalAccessException e2) {
            throw new ReflectionError(e2);
        } catch (InvocationTargetException e3) {
            if (IOException.class.isInstance(e3.getCause())) {
                throw ((IOException) e3.getCause());
            }
            throw new ReflectionError(e3);
        }
    }

    static Field lookupField(String str, String str2) {
        try {
            Field declaredField = Class.forName(str).getDeclaredField(str2);
            setAccessible(declaredField);
            return declaredField;
        } catch (ClassNotFoundException | NoSuchFieldException e2) {
            throw new ReflectionError(e2);
        }
    }

    static Object get(Object obj, Field field) {
        try {
            return field.get(obj);
        } catch (IllegalAccessException e2) {
            throw new ReflectionError(e2);
        }
    }

    static Object get(Field field) {
        return get(null, field);
    }

    static void set(Object obj, Field field, Object obj2) throws IllegalArgumentException {
        try {
            field.set(obj, obj2);
        } catch (IllegalAccessException e2) {
            throw new ReflectionError(e2);
        }
    }

    static void setInt(Object obj, Field field, int i2) throws IllegalArgumentException {
        try {
            field.setInt(obj, i2);
        } catch (IllegalAccessException e2) {
            throw new ReflectionError(e2);
        }
    }

    static void setBoolean(Object obj, Field field, boolean z2) throws IllegalArgumentException {
        try {
            field.setBoolean(obj, z2);
        } catch (IllegalAccessException e2) {
            throw new ReflectionError(e2);
        }
    }
}
