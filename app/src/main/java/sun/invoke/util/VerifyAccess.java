package sun.invoke.util;

import java.lang.invoke.MethodType;
import java.lang.reflect.Modifier;
import java.security.AccessController;
import java.security.PrivilegedAction;
import sun.reflect.Reflection;

/* loaded from: rt.jar:sun/invoke/util/VerifyAccess.class */
public class VerifyAccess {
    private static final int PACKAGE_ONLY = 0;
    private static final int PACKAGE_ALLOWED = 8;
    private static final int PROTECTED_OR_PACKAGE_ALLOWED = 12;
    private static final int ALL_ACCESS_MODES = 7;
    private static final boolean ALLOW_NESTMATE_ACCESS = false;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !VerifyAccess.class.desiredAssertionStatus();
    }

    private VerifyAccess() {
    }

    public static boolean isMemberAccessible(Class<?> cls, Class<?> cls2, int i2, Class<?> cls3, int i3) {
        if (i3 == 0) {
            return false;
        }
        if (!$assertionsDisabled && ((i3 & 1) == 0 || (i3 & (-16)) != 0)) {
            throw new AssertionError();
        }
        if (!isClassAccessible(cls, cls3, i3)) {
            return false;
        }
        if (cls2 == cls3 && (i3 & 2) != 0) {
            return true;
        }
        switch (i2 & 7) {
            case 0:
                if ($assertionsDisabled || !cls2.isInterface()) {
                    return (i3 & 8) != 0 && isSamePackage(cls2, cls3);
                }
                throw new AssertionError();
            case 1:
                return true;
            case 2:
                return false;
            case 3:
            default:
                throw new IllegalArgumentException("bad modifiers: " + Modifier.toString(i2));
            case 4:
                if (!$assertionsDisabled && cls2.isInterface()) {
                    throw new AssertionError();
                }
                if ((i3 & 12) != 0 && isSamePackage(cls2, cls3)) {
                    return true;
                }
                if ((i3 & 4) == 0) {
                    return false;
                }
                if (((i2 & 8) == 0 || isRelatedClass(cls, cls3)) && (i3 & 4) != 0 && isSubClass(cls3, cls2)) {
                    return true;
                }
                return false;
        }
    }

    static boolean isRelatedClass(Class<?> cls, Class<?> cls2) {
        return cls == cls2 || isSubClass(cls, cls2) || isSubClass(cls2, cls);
    }

    static boolean isSubClass(Class<?> cls, Class<?> cls2) {
        return cls2.isAssignableFrom(cls) && !cls.isInterface();
    }

    static int getClassModifiers(Class<?> cls) {
        if (cls.isArray() || cls.isPrimitive()) {
            return cls.getModifiers();
        }
        return Reflection.getClassAccessFlags(cls);
    }

    public static boolean isClassAccessible(Class<?> cls, Class<?> cls2, int i2) {
        if (i2 == 0) {
            return false;
        }
        if (!$assertionsDisabled && ((i2 & 1) == 0 || (i2 & (-16)) != 0)) {
            throw new AssertionError();
        }
        if (Modifier.isPublic(getClassModifiers(cls))) {
            return true;
        }
        if ((i2 & 8) != 0 && isSamePackage(cls2, cls)) {
            return true;
        }
        return false;
    }

    public static boolean isTypeVisible(Class<?> cls, Class<?> cls2) {
        ClassLoader classLoader;
        final ClassLoader classLoader2;
        if (cls == cls2) {
            return true;
        }
        while (cls.isArray()) {
            cls = cls.getComponentType();
        }
        if (cls.isPrimitive() || cls == Object.class || (classLoader = cls.getClassLoader()) == (classLoader2 = cls2.getClassLoader())) {
            return true;
        }
        if (classLoader2 == null && classLoader != null) {
            return false;
        }
        if (classLoader == null && cls.getName().startsWith("java.")) {
            return true;
        }
        final String name = cls.getName();
        return cls == ((Class) AccessController.doPrivileged(new PrivilegedAction<Class>() { // from class: sun.invoke.util.VerifyAccess.1
            @Override // java.security.PrivilegedAction
            /* renamed from: run, reason: merged with bridge method [inline-methods] */
            public Class run2() {
                try {
                    return Class.forName(name, false, classLoader2);
                } catch (ClassNotFoundException | LinkageError e2) {
                    return null;
                }
            }
        }));
    }

    public static boolean isTypeVisible(MethodType methodType, Class<?> cls) {
        int i2 = -1;
        int iParameterCount = methodType.parameterCount();
        while (i2 < iParameterCount) {
            if (isTypeVisible(i2 < 0 ? methodType.returnType() : methodType.parameterType(i2), cls)) {
                i2++;
            } else {
                return false;
            }
        }
        return true;
    }

    public static boolean isSamePackage(Class<?> cls, Class<?> cls2) {
        if (!$assertionsDisabled && (cls.isArray() || cls2.isArray())) {
            throw new AssertionError();
        }
        if (cls == cls2) {
            return true;
        }
        if (cls.getClassLoader() != cls2.getClassLoader()) {
            return false;
        }
        String name = cls.getName();
        String name2 = cls2.getName();
        int iLastIndexOf = name.lastIndexOf(46);
        if (iLastIndexOf != name2.lastIndexOf(46)) {
            return false;
        }
        for (int i2 = 0; i2 < iLastIndexOf; i2++) {
            if (name.charAt(i2) != name2.charAt(i2)) {
                return false;
            }
        }
        return true;
    }

    public static String getPackageName(Class<?> cls) {
        if (!$assertionsDisabled && cls.isArray()) {
            throw new AssertionError();
        }
        String name = cls.getName();
        int iLastIndexOf = name.lastIndexOf(46);
        return iLastIndexOf < 0 ? "" : name.substring(0, iLastIndexOf);
    }

    public static boolean isSamePackageMember(Class<?> cls, Class<?> cls2) {
        if (cls == cls2) {
            return true;
        }
        if (!isSamePackage(cls, cls2) || getOutermostEnclosingClass(cls) != getOutermostEnclosingClass(cls2)) {
            return false;
        }
        return true;
    }

    private static Class<?> getOutermostEnclosingClass(Class<?> cls) throws SecurityException {
        Class<?> cls2 = cls;
        Class<?> cls3 = cls;
        while (true) {
            Class<?> enclosingClass = cls3.getEnclosingClass();
            cls3 = enclosingClass;
            if (enclosingClass != null) {
                cls2 = cls3;
            } else {
                return cls2;
            }
        }
    }

    private static boolean loadersAreRelated(ClassLoader classLoader, ClassLoader classLoader2, boolean z2) {
        if (classLoader == classLoader2 || classLoader == null) {
            return true;
        }
        if (classLoader2 == null && !z2) {
            return true;
        }
        ClassLoader parent = classLoader2;
        while (true) {
            ClassLoader classLoader3 = parent;
            if (classLoader3 != null) {
                if (classLoader3 == classLoader) {
                    return true;
                }
                parent = classLoader3.getParent();
            } else {
                if (z2) {
                    return false;
                }
                ClassLoader parent2 = classLoader;
                while (true) {
                    ClassLoader classLoader4 = parent2;
                    if (classLoader4 != null) {
                        if (classLoader4 == classLoader2) {
                            return true;
                        }
                        parent2 = classLoader4.getParent();
                    } else {
                        return false;
                    }
                }
            }
        }
    }

    public static boolean classLoaderIsAncestor(Class<?> cls, Class<?> cls2) {
        return loadersAreRelated(cls.getClassLoader(), cls2.getClassLoader(), true);
    }
}
