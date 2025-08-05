package com.sun.beans.finder;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import sun.reflect.misc.ReflectUtil;

/* loaded from: rt.jar:com/sun/beans/finder/FieldFinder.class */
public final class FieldFinder {
    public static Field findField(Class<?> cls, String str) throws NoSuchFieldException, SecurityException {
        if (str == null) {
            throw new IllegalArgumentException("Field name is not set");
        }
        Field field = cls.getField(str);
        if (!Modifier.isPublic(field.getModifiers())) {
            throw new NoSuchFieldException("Field '" + str + "' is not public");
        }
        Class<?> declaringClass = field.getDeclaringClass();
        if (!Modifier.isPublic(declaringClass.getModifiers()) || !ReflectUtil.isPackageAccessible(declaringClass)) {
            throw new NoSuchFieldException("Field '" + str + "' is not accessible");
        }
        return field;
    }

    public static Field findInstanceField(Class<?> cls, String str) throws NoSuchFieldException, SecurityException {
        Field fieldFindField = findField(cls, str);
        if (Modifier.isStatic(fieldFindField.getModifiers())) {
            throw new NoSuchFieldException("Field '" + str + "' is static");
        }
        return fieldFindField;
    }

    public static Field findStaticField(Class<?> cls, String str) throws NoSuchFieldException, SecurityException {
        Field fieldFindField = findField(cls, str);
        if (!Modifier.isStatic(fieldFindField.getModifiers())) {
            throw new NoSuchFieldException("Field '" + str + "' is not static");
        }
        return fieldFindField;
    }

    private FieldFinder() {
    }
}
