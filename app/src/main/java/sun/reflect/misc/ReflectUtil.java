package sun.reflect.misc;

import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;
import sun.reflect.Reflection;
import sun.security.util.SecurityConstants;

/* loaded from: rt.jar:sun/reflect/misc/ReflectUtil.class */
public final class ReflectUtil {
    public static final String PROXY_PACKAGE = "com.sun.proxy";

    private ReflectUtil() {
    }

    public static Class<?> forName(String str) throws ClassNotFoundException {
        checkPackageAccess(str);
        return Class.forName(str);
    }

    public static Object newInstance(Class<?> cls) throws IllegalAccessException, InstantiationException {
        checkPackageAccess(cls);
        return cls.newInstance();
    }

    public static void ensureMemberAccess(Class<?> cls, Class<?> cls2, Object obj, int i2) throws IllegalAccessException {
        if (obj == null && Modifier.isProtected(i2)) {
            int i3 = (i2 & (-5)) | 1;
            Reflection.ensureMemberAccess(cls, cls2, obj, i3);
            try {
                Reflection.ensureMemberAccess(cls, cls2, obj, i3 & (-2));
                return;
            } catch (IllegalAccessException e2) {
                if (isSubclassOf(cls, cls2)) {
                    return;
                } else {
                    throw e2;
                }
            }
        }
        Reflection.ensureMemberAccess(cls, cls2, obj, i2);
    }

    private static boolean isSubclassOf(Class<?> cls, Class<?> cls2) {
        while (cls != null) {
            if (cls == cls2) {
                return true;
            }
            cls = cls.getSuperclass();
        }
        return false;
    }

    public static void conservativeCheckMemberAccess(Member member) throws SecurityException {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager == null) {
            return;
        }
        Class<?> declaringClass = member.getDeclaringClass();
        checkPackageAccess(declaringClass);
        if (Modifier.isPublic(member.getModifiers()) && Modifier.isPublic(declaringClass.getModifiers())) {
            return;
        }
        securityManager.checkPermission(SecurityConstants.CHECK_MEMBER_ACCESS_PERMISSION);
    }

    public static void checkPackageAccess(Class<?> cls) {
        checkPackageAccess(cls.getName());
        if (isNonPublicProxyClass(cls)) {
            checkProxyPackageAccess(cls);
        }
    }

    public static void checkPackageAccess(String str) {
        int iLastIndexOf;
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            String strReplace = str.replace('/', '.');
            if (strReplace.startsWith("[") && (iLastIndexOf = strReplace.lastIndexOf(91) + 2) > 1 && iLastIndexOf < strReplace.length()) {
                strReplace = strReplace.substring(iLastIndexOf);
            }
            int iLastIndexOf2 = strReplace.lastIndexOf(46);
            if (iLastIndexOf2 != -1) {
                securityManager.checkPackageAccess(strReplace.substring(0, iLastIndexOf2));
            }
        }
    }

    public static boolean isPackageAccessible(Class<?> cls) {
        try {
            checkPackageAccess(cls);
            return true;
        } catch (SecurityException e2) {
            return false;
        }
    }

    private static boolean isAncestor(ClassLoader classLoader, ClassLoader classLoader2) {
        ClassLoader parent = classLoader2;
        do {
            parent = parent.getParent();
            if (classLoader == parent) {
                return true;
            }
        } while (parent != null);
        return false;
    }

    public static boolean needsPackageAccessCheck(ClassLoader classLoader, ClassLoader classLoader2) {
        if (classLoader == null || classLoader == classLoader2) {
            return false;
        }
        return classLoader2 == null || !isAncestor(classLoader, classLoader2);
    }

    public static void checkProxyPackageAccess(Class<?> cls) {
        if (System.getSecurityManager() != null && Proxy.isProxyClass(cls)) {
            for (Class<?> cls2 : cls.getInterfaces()) {
                checkPackageAccess(cls2);
            }
        }
    }

    public static void checkProxyPackageAccess(ClassLoader classLoader, Class<?>... clsArr) {
        if (System.getSecurityManager() != null) {
            for (Class<?> cls : clsArr) {
                if (needsPackageAccessCheck(classLoader, cls.getClassLoader())) {
                    checkPackageAccess(cls);
                }
            }
        }
    }

    public static boolean isNonPublicProxyClass(Class<?> cls) {
        String name = cls.getName();
        int iLastIndexOf = name.lastIndexOf(46);
        return Proxy.isProxyClass(cls) && !(iLastIndexOf != -1 ? name.substring(0, iLastIndexOf) : "").equals(PROXY_PACKAGE);
    }

    public static void checkProxyMethod(Object obj, Method method) {
        if (obj == null || !Proxy.isProxyClass(obj.getClass())) {
            throw new IllegalArgumentException("Not a Proxy instance");
        }
        if (Modifier.isStatic(method.getModifiers())) {
            throw new IllegalArgumentException("Can't handle static method");
        }
        Class<?> declaringClass = method.getDeclaringClass();
        if (declaringClass == Object.class) {
            String name = method.getName();
            if (name.equals("hashCode") || name.equals("equals") || name.equals("toString")) {
                return;
            }
        }
        if (isSuperInterface(obj.getClass(), declaringClass)) {
        } else {
            throw new IllegalArgumentException("Can't handle: " + ((Object) method));
        }
    }

    private static boolean isSuperInterface(Class<?> cls, Class<?> cls2) {
        for (Class<?> cls3 : cls.getInterfaces()) {
            if (cls3 == cls2 || isSuperInterface(cls3, cls2)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isVMAnonymousClass(Class<?> cls) {
        return cls.getName().indexOf("/") > -1;
    }
}
