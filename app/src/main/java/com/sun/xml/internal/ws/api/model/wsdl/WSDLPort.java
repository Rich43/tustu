package com.sun.xml.internal.ws.api.model.wsdl;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import com.sun.xml.internal.ws.api.EndpointAddress;
import com.sun.xml.internal.ws.api.addressing.WSEndpointReference;
import javax.xml.namespace.QName;

/* loaded from: rt.jar:com/sun/xml/internal/ws/api/model/wsdl/WSDLPort.class */
public interface WSDLPort extends WSDLFeaturedObject, WSDLExtensible {
    QName getName();

    @NotNull
    WSDLBoundPortType getBinding();

    EndpointAddress getAddress();

    @NotNull
    WSDLService getOwner();

    @Nullable
    WSEndpointReference getEPR();
}
