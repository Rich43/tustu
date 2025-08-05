package com.sun.xml.internal.ws.spi.db;

/* loaded from: rt.jar:com/sun/xml/internal/ws/spi/db/PropertyAccessor.class */
public interface PropertyAccessor<B, V> {
    V get(B b2) throws DatabindingException;

    void set(B b2, V v2) throws DatabindingException;
}
