package com.sun.xml.internal.ws.spi.db;

/* loaded from: rt.jar:com/sun/xml/internal/ws/spi/db/PropertySetter.class */
public interface PropertySetter {
    Class getType();

    <A> A getAnnotation(Class<A> cls);

    void set(Object obj, Object obj2);
}
