package com.sun.xml.internal.ws.api.server;

import com.sun.istack.internal.NotNull;
import com.sun.xml.internal.ws.transport.http.HttpAdapter;

/* loaded from: rt.jar:com/sun/xml/internal/ws/api/server/HttpEndpoint.class */
public abstract class HttpEndpoint {
    public abstract void publish(@NotNull String str);

    public abstract void stop();

    public static HttpEndpoint create(@NotNull WSEndpoint endpoint) {
        return new com.sun.xml.internal.ws.transport.http.server.HttpEndpoint(null, HttpAdapter.createAlone(endpoint));
    }
}
