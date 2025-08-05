package com.sun.xml.internal.bind.v2.model.core;

import com.sun.org.apache.xerces.internal.impl.xs.SchemaSymbols;
import javax.xml.namespace.QName;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/model/core/NonElement.class */
public interface NonElement<T, C> extends TypeInfo<T, C> {
    public static final QName ANYTYPE_NAME = new QName("http://www.w3.org/2001/XMLSchema", SchemaSymbols.ATTVAL_ANYTYPE);

    QName getTypeName();

    boolean isSimpleType();
}
