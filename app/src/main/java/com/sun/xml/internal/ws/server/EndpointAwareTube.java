package com.sun.xml.internal.ws.server;

import com.sun.xml.internal.ws.api.pipe.Tube;
import com.sun.xml.internal.ws.api.server.WSEndpoint;

/* loaded from: rt.jar:com/sun/xml/internal/ws/server/EndpointAwareTube.class */
public interface EndpointAwareTube extends Tube {
    void setEndpoint(WSEndpoint<?> wSEndpoint);
}
