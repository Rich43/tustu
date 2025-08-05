package com.sun.xml.internal.bind.v2.model.core;

import javax.xml.namespace.QName;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/model/core/AttributePropertyInfo.class */
public interface AttributePropertyInfo<T, C> extends PropertyInfo<T, C>, NonElementRef<T, C> {
    @Override // com.sun.xml.internal.bind.v2.model.core.NonElementRef
    NonElement<T, C> getTarget();

    boolean isRequired();

    QName getXmlName();

    @Override // com.sun.xml.internal.bind.v2.model.core.PropertyInfo
    Adapter<T, C> getAdapter();
}
