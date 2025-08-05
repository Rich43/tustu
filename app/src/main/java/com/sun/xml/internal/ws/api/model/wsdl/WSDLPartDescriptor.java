package com.sun.xml.internal.ws.api.model.wsdl;

import javax.xml.namespace.QName;

/* loaded from: rt.jar:com/sun/xml/internal/ws/api/model/wsdl/WSDLPartDescriptor.class */
public interface WSDLPartDescriptor extends WSDLObject {
    QName name();

    WSDLDescriptorKind type();
}
