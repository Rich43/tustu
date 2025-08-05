package sun.reflect.misc;

import java.lang.reflect.Constructor;

/* loaded from: rt.jar:sun/reflect/misc/ConstructorUtil.class */
public final class ConstructorUtil {
    private ConstructorUtil() {
    }

    public static Constructor<?> getConstructor(Class<?> cls, Class<?>[] clsArr) throws NoSuchMethodException {
        ReflectUtil.checkPackageAccess(cls);
        return cls.getConstructor(clsArr);
    }

    public static Constructor<?>[] getConstructors(Class<?> cls) {
        ReflectUtil.checkPackageAccess(cls);
        return cls.getConstructors();
    }
}
