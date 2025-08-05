package com.sun.xml.internal.ws.transport.http.server;

import com.sun.xml.internal.ws.api.server.WSEndpoint;
import com.sun.xml.internal.ws.transport.http.HttpAdapter;
import com.sun.xml.internal.ws.transport.http.HttpAdapterList;

/* loaded from: rt.jar:com/sun/xml/internal/ws/transport/http/server/ServerAdapterList.class */
public class ServerAdapterList extends HttpAdapterList<ServerAdapter> {
    @Override // com.sun.xml.internal.ws.transport.http.HttpAdapterList
    protected /* bridge */ /* synthetic */ HttpAdapter createHttpAdapter(String str, String str2, WSEndpoint wSEndpoint) {
        return createHttpAdapter(str, str2, (WSEndpoint<?>) wSEndpoint);
    }

    @Override // com.sun.xml.internal.ws.transport.http.HttpAdapterList
    protected ServerAdapter createHttpAdapter(String name, String urlPattern, WSEndpoint<?> endpoint) {
        return new ServerAdapter(name, urlPattern, endpoint, this);
    }
}
