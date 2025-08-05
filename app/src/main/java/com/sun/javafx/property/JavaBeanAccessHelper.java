package com.sun.javafx.property;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import javafx.beans.property.ReadOnlyObjectProperty;

/* loaded from: jfxrt.jar:com/sun/javafx/property/JavaBeanAccessHelper.class */
public final class JavaBeanAccessHelper {
    private static Method JAVA_BEAN_QUICK_ACCESSOR_CREATE_RO;
    private static boolean initialized;

    private JavaBeanAccessHelper() {
    }

    public static <T> ReadOnlyObjectProperty<T> createReadOnlyJavaBeanProperty(Object bean, String propertyName) throws NoSuchMethodException {
        init();
        if (JAVA_BEAN_QUICK_ACCESSOR_CREATE_RO == null) {
            throw new UnsupportedOperationException("Java beans are not supported.");
        }
        try {
            return (ReadOnlyObjectProperty) JAVA_BEAN_QUICK_ACCESSOR_CREATE_RO.invoke(null, bean, propertyName);
        } catch (IllegalAccessException e2) {
            throw new UnsupportedOperationException("Java beans are not supported.");
        } catch (InvocationTargetException ex) {
            if (ex.getCause() instanceof NoSuchMethodException) {
                throw ((NoSuchMethodException) ex.getCause());
            }
            throw new UnsupportedOperationException("Java beans are not supported.");
        }
    }

    private static void init() {
        if (!initialized) {
            try {
                Class accessor = Class.forName("com.sun.javafx.property.adapter.JavaBeanQuickAccessor", true, JavaBeanAccessHelper.class.getClassLoader());
                JAVA_BEAN_QUICK_ACCESSOR_CREATE_RO = accessor.getDeclaredMethod("createReadOnlyJavaBeanObjectProperty", Object.class, String.class);
            } catch (ClassNotFoundException e2) {
            } catch (NoSuchMethodException e3) {
            }
            initialized = true;
        }
    }
}
