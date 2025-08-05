package com.google.gson.internal;

import java.lang.reflect.Type;

/* loaded from: gson-2.9.0.jar:com/google/gson/internal/Primitives.class */
public final class Primitives {
    private Primitives() {
    }

    public static boolean isPrimitive(Type type) {
        return (type instanceof Class) && ((Class) type).isPrimitive();
    }

    public static boolean isWrapperType(Type type) {
        return type == Integer.class || type == Float.class || type == Byte.class || type == Double.class || type == Long.class || type == Character.class || type == Boolean.class || type == Short.class || type == Void.class;
    }

    public static <T> Class<T> wrap(Class<T> type) {
        return type == Integer.TYPE ? Integer.class : type == Float.TYPE ? Float.class : type == Byte.TYPE ? Byte.class : type == Double.TYPE ? Double.class : type == Long.TYPE ? Long.class : type == Character.TYPE ? Character.class : type == Boolean.TYPE ? Boolean.class : type == Short.TYPE ? Short.class : type == Void.TYPE ? Void.class : type;
    }

    public static <T> Class<T> unwrap(Class<T> cls) {
        return cls == Integer.class ? (Class<T>) Integer.TYPE : cls == Float.class ? (Class<T>) Float.TYPE : cls == Byte.class ? (Class<T>) Byte.TYPE : cls == Double.class ? (Class<T>) Double.TYPE : cls == Long.class ? (Class<T>) Long.TYPE : cls == Character.class ? (Class<T>) Character.TYPE : cls == Boolean.class ? (Class<T>) Boolean.TYPE : cls == Short.class ? (Class<T>) Short.TYPE : cls == Void.class ? (Class<T>) Void.TYPE : cls;
    }
}
