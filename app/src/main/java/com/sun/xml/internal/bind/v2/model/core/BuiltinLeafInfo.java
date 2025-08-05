package com.sun.xml.internal.bind.v2.model.core;

import javax.xml.namespace.QName;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/model/core/BuiltinLeafInfo.class */
public interface BuiltinLeafInfo<T, C> extends LeafInfo<T, C> {
    @Override // com.sun.xml.internal.bind.v2.model.core.NonElement
    QName getTypeName();
}
