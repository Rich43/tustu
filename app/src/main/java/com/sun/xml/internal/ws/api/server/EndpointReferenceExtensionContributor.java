package com.sun.xml.internal.ws.api.server;

import com.sun.istack.internal.Nullable;
import com.sun.xml.internal.ws.api.addressing.WSEndpointReference;
import javax.xml.namespace.QName;

/* loaded from: rt.jar:com/sun/xml/internal/ws/api/server/EndpointReferenceExtensionContributor.class */
public abstract class EndpointReferenceExtensionContributor {
    public abstract WSEndpointReference.EPRExtension getEPRExtension(WSEndpoint wSEndpoint, @Nullable WSEndpointReference.EPRExtension ePRExtension);

    public abstract QName getQName();
}
