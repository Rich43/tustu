package com.sun.xml.internal.bind.v2.model.core;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/model/core/NonElementRef.class */
public interface NonElementRef<T, C> {
    NonElement<T, C> getTarget();

    PropertyInfo<T, C> getSource();
}
