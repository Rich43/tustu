package com.sun.xml.internal.ws.api.model.wsdl;

import com.sun.istack.internal.NotNull;
import javax.xml.namespace.QName;

/* loaded from: rt.jar:com/sun/xml/internal/ws/api/model/wsdl/WSDLFault.class */
public interface WSDLFault extends WSDLObject, WSDLExtensible {
    String getName();

    WSDLMessage getMessage();

    @NotNull
    WSDLOperation getOperation();

    @NotNull
    QName getQName();

    String getAction();

    boolean isDefaultAction();
}
