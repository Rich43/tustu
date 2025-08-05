package com.sun.net.httpserver;

import com.sun.net.httpserver.spi.HttpServerProvider;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executor;
import jdk.Exported;

@Exported
/* loaded from: rt.jar:com/sun/net/httpserver/HttpServer.class */
public abstract class HttpServer {
    public abstract void bind(InetSocketAddress inetSocketAddress, int i2) throws IOException;

    public abstract void start();

    public abstract void setExecutor(Executor executor);

    public abstract Executor getExecutor();

    public abstract void stop(int i2);

    public abstract HttpContext createContext(String str, HttpHandler httpHandler);

    public abstract HttpContext createContext(String str);

    public abstract void removeContext(String str) throws IllegalArgumentException;

    public abstract void removeContext(HttpContext httpContext);

    public abstract InetSocketAddress getAddress();

    protected HttpServer() {
    }

    public static HttpServer create() throws IOException {
        return create(null, 0);
    }

    public static HttpServer create(InetSocketAddress inetSocketAddress, int i2) throws IOException {
        return HttpServerProvider.provider().createHttpServer(inetSocketAddress, i2);
    }
}
