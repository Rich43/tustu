package com.sun.xml.internal.bind.v2.model.core;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/model/core/EnumLeafInfo.class */
public interface EnumLeafInfo<T, C> extends LeafInfo<T, C> {
    C getClazz();

    NonElement<T, C> getBaseType();

    Iterable<? extends EnumConstant> getConstants();
}
