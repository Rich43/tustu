package com.sun.beans.finder;

import sun.reflect.misc.ReflectUtil;

/* loaded from: rt.jar:com/sun/beans/finder/ClassFinder.class */
public final class ClassFinder {
    public static Class<?> findClass(String str) throws ClassNotFoundException {
        ReflectUtil.checkPackageAccess(str);
        try {
            ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
            if (contextClassLoader == null) {
                contextClassLoader = ClassLoader.getSystemClassLoader();
            }
            if (contextClassLoader != null) {
                return Class.forName(str, false, contextClassLoader);
            }
        } catch (ClassNotFoundException e2) {
        } catch (SecurityException e3) {
        }
        return Class.forName(str);
    }

    public static Class<?> findClass(String str, ClassLoader classLoader) throws ClassNotFoundException {
        ReflectUtil.checkPackageAccess(str);
        if (classLoader != null) {
            try {
                return Class.forName(str, false, classLoader);
            } catch (ClassNotFoundException e2) {
            } catch (SecurityException e3) {
            }
        }
        return findClass(str);
    }

    public static Class<?> resolveClass(String str) throws ClassNotFoundException {
        return resolveClass(str, null);
    }

    public static Class<?> resolveClass(String str, ClassLoader classLoader) throws ClassNotFoundException {
        Class<?> type = PrimitiveTypeMap.getType(str);
        return type == null ? findClass(str, classLoader) : type;
    }

    private ClassFinder() {
    }
}
