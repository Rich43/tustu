package com.sun.xml.internal.ws.api.model.wsdl;

import javax.xml.namespace.QName;

/* loaded from: rt.jar:com/sun/xml/internal/ws/api/model/wsdl/WSDLMessage.class */
public interface WSDLMessage extends WSDLObject, WSDLExtensible {
    QName getName();

    Iterable<? extends WSDLPart> parts();
}
