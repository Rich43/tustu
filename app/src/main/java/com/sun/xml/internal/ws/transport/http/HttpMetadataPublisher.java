package com.sun.xml.internal.ws.transport.http;

import com.sun.istack.internal.NotNull;
import java.io.IOException;

/* loaded from: rt.jar:com/sun/xml/internal/ws/transport/http/HttpMetadataPublisher.class */
public abstract class HttpMetadataPublisher {
    public abstract boolean handleMetadataRequest(@NotNull HttpAdapter httpAdapter, @NotNull WSHTTPConnection wSHTTPConnection) throws IOException;
}
