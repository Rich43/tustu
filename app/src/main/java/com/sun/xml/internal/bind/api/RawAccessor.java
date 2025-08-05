package com.sun.xml.internal.bind.api;

/* loaded from: rt.jar:com/sun/xml/internal/bind/api/RawAccessor.class */
public abstract class RawAccessor<B, V> {
    public abstract V get(B b2) throws AccessorException;

    public abstract void set(B b2, V v2) throws AccessorException;
}
