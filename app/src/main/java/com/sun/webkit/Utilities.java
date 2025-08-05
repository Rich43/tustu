package com.sun.webkit;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import sun.reflect.misc.MethodUtil;

/* loaded from: jfxrt.jar:com/sun/webkit/Utilities.class */
public abstract class Utilities {
    private static Utilities instance;
    private static final Set<String> classMethodsWhitelist = asSet("getCanonicalName", "getEnumConstants", "getFields", "getMethods", "getName", "getPackageName", "getSimpleName", "getSuperclass", "getTypeName", "getTypeParameters", "isAssignableFrom", "isArray", "isEnum", "isInstance", "isInterface", "isLocalClass", "isMemberClass", "isPrimitive", "isSynthetic", "toGenericString", "toString");
    private static final Set<String> classesBlacklist = asSet("java.lang.ClassLoader", "java.lang.Module", "java.lang.Runtime", "java.lang.System");
    private static final List<String> packagesBlacklist = Arrays.asList("java.lang.invoke", "java.lang.module", "java.lang.reflect", "java.security", "sun.misc");

    protected abstract Pasteboard createPasteboard();

    protected abstract PopupMenu createPopupMenu();

    protected abstract ContextMenu createContextMenu();

    public static synchronized void setUtilities(Utilities util) {
        instance = util;
    }

    public static synchronized Utilities getUtilities() {
        return instance;
    }

    private static final Set<String> asSet(String... items) {
        return new HashSet(Arrays.asList(items));
    }

    private static Object fwkInvokeWithContext(Method method, Object instance2, Object[] args, AccessControlContext acc) throws Throwable {
        Class<?> clazz = method.getDeclaringClass();
        if (clazz.equals(Class.class)) {
            if (!classMethodsWhitelist.contains(method.getName())) {
                throw new UnsupportedOperationException("invocation not supported");
            }
        } else {
            String className = clazz.getName();
            if (classesBlacklist.contains(className)) {
                throw new UnsupportedOperationException("invocation not supported");
            }
            packagesBlacklist.forEach(packageName -> {
                if (className.startsWith(packageName + ".")) {
                    throw new UnsupportedOperationException("invocation not supported");
                }
            });
        }
        try {
            return AccessController.doPrivileged(() -> {
                return MethodUtil.invoke(method, instance2, args);
            }, acc);
        } catch (PrivilegedActionException ex) {
            Throwable cause = ex.getCause();
            if (cause == null) {
                cause = ex;
            } else if ((cause instanceof InvocationTargetException) && cause.getCause() != null) {
                cause = cause.getCause();
            }
            throw cause;
        }
    }
}
