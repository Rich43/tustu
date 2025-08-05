package com.sun.xml.internal.ws.api.model.wsdl;

import javax.xml.namespace.QName;

/* loaded from: rt.jar:com/sun/xml/internal/ws/api/model/wsdl/WSDLPortType.class */
public interface WSDLPortType extends WSDLObject, WSDLExtensible {
    QName getName();

    WSDLOperation get(String str);

    Iterable<? extends WSDLOperation> getOperations();
}
