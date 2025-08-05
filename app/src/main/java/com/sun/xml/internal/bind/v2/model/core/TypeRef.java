package com.sun.xml.internal.bind.v2.model.core;

import javax.xml.namespace.QName;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/model/core/TypeRef.class */
public interface TypeRef<T, C> extends NonElementRef<T, C> {
    QName getTagName();

    boolean isNillable();

    String getDefaultValue();
}
