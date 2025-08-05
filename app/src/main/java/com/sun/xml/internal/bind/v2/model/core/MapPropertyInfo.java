package com.sun.xml.internal.bind.v2.model.core;

import javax.xml.namespace.QName;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/model/core/MapPropertyInfo.class */
public interface MapPropertyInfo<T, C> extends PropertyInfo<T, C> {
    QName getXmlName();

    boolean isCollectionNillable();

    NonElement<T, C> getKeyType();

    NonElement<T, C> getValueType();
}
