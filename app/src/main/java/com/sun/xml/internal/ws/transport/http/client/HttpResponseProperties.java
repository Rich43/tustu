package com.sun.xml.internal.ws.transport.http.client;

import com.oracle.webservices.internal.api.message.BasePropertySet;
import com.oracle.webservices.internal.api.message.PropertySet;
import com.sun.istack.internal.NotNull;
import java.util.List;
import java.util.Map;
import javax.xml.ws.handler.MessageContext;

/* loaded from: rt.jar:com/sun/xml/internal/ws/transport/http/client/HttpResponseProperties.class */
final class HttpResponseProperties extends BasePropertySet {
    private final HttpClientTransport deferedCon;
    private static final BasePropertySet.PropertyMap model = parse(HttpResponseProperties.class);

    public HttpResponseProperties(@NotNull HttpClientTransport con) {
        this.deferedCon = con;
    }

    @PropertySet.Property({MessageContext.HTTP_RESPONSE_HEADERS})
    public Map<String, List<String>> getResponseHeaders() {
        return this.deferedCon.getHeaders();
    }

    @PropertySet.Property({MessageContext.HTTP_RESPONSE_CODE})
    public int getResponseCode() {
        return this.deferedCon.statusCode;
    }

    @Override // com.oracle.webservices.internal.api.message.BasePropertySet
    protected BasePropertySet.PropertyMap getPropertyMap() {
        return model;
    }
}
