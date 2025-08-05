package com.sun.xml.internal.bind.v2.model.core;

import java.util.Set;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/model/core/RegistryInfo.class */
public interface RegistryInfo<T, C> {
    Set<TypeInfo<T, C>> getReferences();

    C getClazz();
}
