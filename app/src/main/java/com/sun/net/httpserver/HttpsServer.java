package com.sun.net.httpserver;

import com.sun.net.httpserver.spi.HttpServerProvider;
import java.io.IOException;
import java.net.InetSocketAddress;
import jdk.Exported;

@Exported
/* loaded from: rt.jar:com/sun/net/httpserver/HttpsServer.class */
public abstract class HttpsServer extends HttpServer {
    public abstract void setHttpsConfigurator(HttpsConfigurator httpsConfigurator);

    public abstract HttpsConfigurator getHttpsConfigurator();

    protected HttpsServer() {
    }

    public static HttpsServer create() throws IOException {
        return create((InetSocketAddress) null, 0);
    }

    public static HttpsServer create(InetSocketAddress inetSocketAddress, int i2) throws IOException {
        return HttpServerProvider.provider().createHttpsServer(inetSocketAddress, i2);
    }
}
