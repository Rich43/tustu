package com.sun.xml.internal.bind.v2.model.core;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/model/core/ValuePropertyInfo.class */
public interface ValuePropertyInfo<T, C> extends PropertyInfo<T, C>, NonElementRef<T, C> {
    @Override // com.sun.xml.internal.bind.v2.model.core.PropertyInfo
    Adapter<T, C> getAdapter();
}
