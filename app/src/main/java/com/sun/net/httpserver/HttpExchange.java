package com.sun.net.httpserver;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import jdk.Exported;

@Exported
/* loaded from: rt.jar:com/sun/net/httpserver/HttpExchange.class */
public abstract class HttpExchange {
    public abstract Headers getRequestHeaders();

    public abstract Headers getResponseHeaders();

    public abstract URI getRequestURI();

    public abstract String getRequestMethod();

    public abstract HttpContext getHttpContext();

    public abstract void close();

    public abstract InputStream getRequestBody();

    public abstract OutputStream getResponseBody();

    public abstract void sendResponseHeaders(int i2, long j2) throws IOException;

    public abstract InetSocketAddress getRemoteAddress();

    public abstract int getResponseCode();

    public abstract InetSocketAddress getLocalAddress();

    public abstract String getProtocol();

    public abstract Object getAttribute(String str);

    public abstract void setAttribute(String str, Object obj);

    public abstract void setStreams(InputStream inputStream, OutputStream outputStream);

    public abstract HttpPrincipal getPrincipal();

    protected HttpExchange() {
    }
}
