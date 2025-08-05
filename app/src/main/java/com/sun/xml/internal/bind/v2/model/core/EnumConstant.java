package com.sun.xml.internal.bind.v2.model.core;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/model/core/EnumConstant.class */
public interface EnumConstant<T, C> {
    EnumLeafInfo<T, C> getEnclosingClass();

    String getLexicalValue();

    String getName();
}
