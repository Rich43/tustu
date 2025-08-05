package com.sun.xml.internal.ws.spi.db;

/* loaded from: rt.jar:com/sun/xml/internal/ws/spi/db/PropertyGetter.class */
public interface PropertyGetter {
    Class getType();

    <A> A getAnnotation(Class<A> cls);

    Object get(Object obj);
}
