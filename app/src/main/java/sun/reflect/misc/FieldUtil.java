package sun.reflect.misc;

import java.lang.reflect.Field;

/* loaded from: rt.jar:sun/reflect/misc/FieldUtil.class */
public final class FieldUtil {
    private FieldUtil() {
    }

    public static Field getField(Class<?> cls, String str) throws NoSuchFieldException {
        ReflectUtil.checkPackageAccess(cls);
        return cls.getField(str);
    }

    public static Field[] getFields(Class<?> cls) {
        ReflectUtil.checkPackageAccess(cls);
        return cls.getFields();
    }

    public static Field[] getDeclaredFields(Class<?> cls) {
        ReflectUtil.checkPackageAccess(cls);
        return cls.getDeclaredFields();
    }
}
