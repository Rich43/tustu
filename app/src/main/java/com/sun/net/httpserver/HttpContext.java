package com.sun.net.httpserver;

import java.util.List;
import java.util.Map;
import jdk.Exported;

@Exported
/* loaded from: rt.jar:com/sun/net/httpserver/HttpContext.class */
public abstract class HttpContext {
    public abstract HttpHandler getHandler();

    public abstract void setHandler(HttpHandler httpHandler);

    public abstract String getPath();

    public abstract HttpServer getServer();

    public abstract Map<String, Object> getAttributes();

    public abstract List<Filter> getFilters();

    public abstract Authenticator setAuthenticator(Authenticator authenticator);

    public abstract Authenticator getAuthenticator();

    protected HttpContext() {
    }
}
