package com.sun.xml.internal.bind.v2.runtime.property;

import com.sun.istack.internal.localization.Localizable;
import com.sun.xml.internal.bind.v2.runtime.unmarshaller.ChildLoader;
import com.sun.xml.internal.bind.v2.util.QNameMap;
import javax.xml.namespace.QName;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/runtime/property/StructureLoaderBuilder.class */
public interface StructureLoaderBuilder {
    public static final QName TEXT_HANDLER = new QName(Localizable.NOT_LOCALIZABLE, "text");
    public static final QName CATCH_ALL = new QName(Localizable.NOT_LOCALIZABLE, "catchAll");

    void buildChildElementUnmarshallers(UnmarshallerChain unmarshallerChain, QNameMap<ChildLoader> qNameMap);
}
