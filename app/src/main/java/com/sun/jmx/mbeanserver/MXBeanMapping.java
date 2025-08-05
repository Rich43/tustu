package com.sun.jmx.mbeanserver;

import java.io.InvalidObjectException;
import java.lang.reflect.Type;
import javax.management.openmbean.OpenDataException;
import javax.management.openmbean.OpenType;

/* loaded from: rt.jar:com/sun/jmx/mbeanserver/MXBeanMapping.class */
public abstract class MXBeanMapping {
    private final Type javaType;
    private final OpenType<?> openType;
    private final Class<?> openClass;

    public abstract Object fromOpenValue(Object obj) throws InvalidObjectException;

    public abstract Object toOpenValue(Object obj) throws OpenDataException;

    protected MXBeanMapping(Type type, OpenType<?> openType) {
        if (type == null || openType == null) {
            throw new NullPointerException("Null argument");
        }
        this.javaType = type;
        this.openType = openType;
        this.openClass = makeOpenClass(type, openType);
    }

    public final Type getJavaType() {
        return this.javaType;
    }

    public final OpenType<?> getOpenType() {
        return this.openType;
    }

    public final Class<?> getOpenClass() {
        return this.openClass;
    }

    private static Class<?> makeOpenClass(Type type, OpenType<?> openType) {
        if ((type instanceof Class) && ((Class) type).isPrimitive()) {
            return (Class) type;
        }
        try {
            return Class.forName(openType.getClassName(), false, MXBeanMapping.class.getClassLoader());
        } catch (ClassNotFoundException e2) {
            throw new RuntimeException(e2);
        }
    }

    public void checkReconstructible() throws InvalidObjectException {
    }
}
