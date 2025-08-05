package sun.reflect;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import org.icepdf.core.util.PdfOps;
import sun.misc.VM;

/* loaded from: rt.jar:sun/reflect/Reflection.class */
public class Reflection {
    private static volatile Map<Class<?>, String[]> fieldFilterMap;
    private static volatile Map<Class<?>, String[]> methodFilterMap;

    @CallerSensitive
    public static native Class<?> getCallerClass();

    @Deprecated
    public static native Class<?> getCallerClass(int i2);

    public static native int getClassAccessFlags(Class<?> cls);

    static {
        HashMap map = new HashMap();
        map.put(Reflection.class, new String[]{"fieldFilterMap", "methodFilterMap"});
        map.put(System.class, new String[]{"security"});
        map.put(Class.class, new String[]{"classLoader"});
        fieldFilterMap = map;
        methodFilterMap = new HashMap();
    }

    public static boolean quickCheckMemberAccess(Class<?> cls, int i2) {
        return Modifier.isPublic(getClassAccessFlags(cls) & i2);
    }

    public static void ensureMemberAccess(Class<?> cls, Class<?> cls2, Object obj, int i2) throws IllegalAccessException {
        if (cls == null || cls2 == null) {
            throw new InternalError();
        }
        if (!verifyMemberAccess(cls, cls2, obj, i2)) {
            throw new IllegalAccessException("Class " + cls.getName() + " can not access a member of class " + cls2.getName() + " with modifiers \"" + Modifier.toString(i2) + PdfOps.DOUBLE_QUOTE__TOKEN);
        }
    }

    public static boolean verifyMemberAccess(Class<?> cls, Class<?> cls2, Object obj, int i2) {
        boolean z2 = false;
        boolean zIsSameClassPackage = false;
        if (cls == cls2) {
            return true;
        }
        if (!Modifier.isPublic(getClassAccessFlags(cls2))) {
            zIsSameClassPackage = isSameClassPackage(cls, cls2);
            z2 = true;
            if (!zIsSameClassPackage) {
                return false;
            }
        }
        if (Modifier.isPublic(i2)) {
            return true;
        }
        boolean z3 = false;
        if (Modifier.isProtected(i2) && isSubclassOf(cls, cls2)) {
            z3 = true;
        }
        if (!z3 && !Modifier.isPrivate(i2)) {
            if (!z2) {
                zIsSameClassPackage = isSameClassPackage(cls, cls2);
                z2 = true;
            }
            if (zIsSameClassPackage) {
                z3 = true;
            }
        }
        if (!z3) {
            return false;
        }
        if (Modifier.isProtected(i2)) {
            Class<?> cls3 = obj == null ? cls2 : obj.getClass();
            if (cls3 != cls) {
                if (!z2) {
                    zIsSameClassPackage = isSameClassPackage(cls, cls2);
                }
                if (!zIsSameClassPackage && !isSubclassOf(cls3, cls)) {
                    return false;
                }
                return true;
            }
            return true;
        }
        return true;
    }

    private static boolean isSameClassPackage(Class<?> cls, Class<?> cls2) {
        return isSameClassPackage(cls.getClassLoader(), cls.getName(), cls2.getClassLoader(), cls2.getName());
    }

    private static boolean isSameClassPackage(ClassLoader classLoader, String str, ClassLoader classLoader2, String str2) {
        if (classLoader != classLoader2) {
            return false;
        }
        int iLastIndexOf = str.lastIndexOf(46);
        int iLastIndexOf2 = str2.lastIndexOf(46);
        if (iLastIndexOf == -1 || iLastIndexOf2 == -1) {
            return iLastIndexOf == iLastIndexOf2;
        }
        int i2 = 0;
        int i3 = 0;
        if (str.charAt(0) == '[') {
            do {
                i2++;
            } while (str.charAt(i2) == '[');
            if (str.charAt(i2) != 'L') {
                throw new InternalError("Illegal class name " + str);
            }
        }
        if (str2.charAt(0) == '[') {
            do {
                i3++;
            } while (str2.charAt(i3) == '[');
            if (str2.charAt(i3) != 'L') {
                throw new InternalError("Illegal class name " + str2);
            }
        }
        int i4 = iLastIndexOf - i2;
        if (i4 != iLastIndexOf2 - i3) {
            return false;
        }
        return str.regionMatches(false, i2, str2, i3, i4);
    }

    static boolean isSubclassOf(Class<?> cls, Class<?> cls2) {
        while (cls != null) {
            if (cls == cls2) {
                return true;
            }
            cls = cls.getSuperclass();
        }
        return false;
    }

    public static synchronized void registerFieldsToFilter(Class<?> cls, String... strArr) {
        fieldFilterMap = registerFilter(fieldFilterMap, cls, strArr);
    }

    public static synchronized void registerMethodsToFilter(Class<?> cls, String... strArr) {
        methodFilterMap = registerFilter(methodFilterMap, cls, strArr);
    }

    private static Map<Class<?>, String[]> registerFilter(Map<Class<?>, String[]> map, Class<?> cls, String... strArr) {
        if (map.get(cls) != null) {
            throw new IllegalArgumentException("Filter already registered: " + ((Object) cls));
        }
        HashMap map2 = new HashMap(map);
        map2.put(cls, strArr);
        return map2;
    }

    public static Field[] filterFields(Class<?> cls, Field[] fieldArr) {
        if (fieldFilterMap == null) {
            return fieldArr;
        }
        return (Field[]) filter(fieldArr, fieldFilterMap.get(cls));
    }

    public static Method[] filterMethods(Class<?> cls, Method[] methodArr) {
        if (methodFilterMap == null) {
            return methodArr;
        }
        return (Method[]) filter(methodArr, methodFilterMap.get(cls));
    }

    private static Member[] filter(Member[] memberArr, String[] strArr) {
        if (strArr == null || memberArr.length == 0) {
            return memberArr;
        }
        int i2 = 0;
        for (Member member : memberArr) {
            boolean z2 = false;
            int length = strArr.length;
            int i3 = 0;
            while (true) {
                if (i3 >= length) {
                    break;
                }
                if (member.getName() != strArr[i3]) {
                    i3++;
                } else {
                    z2 = true;
                    break;
                }
            }
            if (!z2) {
                i2++;
            }
        }
        Member[] memberArr2 = (Member[]) Array.newInstance(memberArr[0].getClass(), i2);
        int i4 = 0;
        for (Member member2 : memberArr) {
            boolean z3 = false;
            int length2 = strArr.length;
            int i5 = 0;
            while (true) {
                if (i5 >= length2) {
                    break;
                }
                if (member2.getName() != strArr[i5]) {
                    i5++;
                } else {
                    z3 = true;
                    break;
                }
            }
            if (!z3) {
                int i6 = i4;
                i4++;
                memberArr2[i6] = member2;
            }
        }
        return memberArr2;
    }

    public static boolean isCallerSensitive(Method method) {
        ClassLoader classLoader = method.getDeclaringClass().getClassLoader();
        if (VM.isSystemDomainLoader(classLoader) || isExtClassLoader(classLoader)) {
            return method.isAnnotationPresent(CallerSensitive.class);
        }
        return false;
    }

    private static boolean isExtClassLoader(ClassLoader classLoader) {
        ClassLoader systemClassLoader = ClassLoader.getSystemClassLoader();
        while (true) {
            ClassLoader classLoader2 = systemClassLoader;
            if (classLoader2 != null) {
                if (classLoader2.getParent() == null && classLoader2 == classLoader) {
                    return true;
                }
                systemClassLoader = classLoader2.getParent();
            } else {
                return false;
            }
        }
    }
}
