package com.sun.xml.internal.ws.api.model.wsdl;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import javax.xml.namespace.QName;

/* loaded from: rt.jar:com/sun/xml/internal/ws/api/model/wsdl/WSDLService.class */
public interface WSDLService extends WSDLObject, WSDLExtensible {
    @NotNull
    WSDLModel getParent();

    @NotNull
    QName getName();

    WSDLPort get(QName qName);

    WSDLPort getFirstPort();

    @Nullable
    WSDLPort getMatchingPort(QName qName);

    Iterable<? extends WSDLPort> getPorts();
}
