package com.sun.xml.internal.ws.api.config.management;

import com.sun.xml.internal.ws.api.server.WSEndpoint;

/* loaded from: rt.jar:com/sun/xml/internal/ws/api/config/management/ManagedEndpointFactory.class */
public interface ManagedEndpointFactory {
    <T> WSEndpoint<T> createEndpoint(WSEndpoint<T> wSEndpoint, EndpointCreationAttributes endpointCreationAttributes);
}
