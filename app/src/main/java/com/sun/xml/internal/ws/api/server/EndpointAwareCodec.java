package com.sun.xml.internal.ws.api.server;

import com.sun.istack.internal.NotNull;
import com.sun.xml.internal.ws.api.pipe.Codec;

/* loaded from: rt.jar:com/sun/xml/internal/ws/api/server/EndpointAwareCodec.class */
public interface EndpointAwareCodec extends Codec {
    void setEndpoint(@NotNull WSEndpoint wSEndpoint);
}
