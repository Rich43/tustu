package com.sun.net.httpserver;

import javax.net.ssl.SSLSession;
import jdk.Exported;

@Exported
/* loaded from: rt.jar:com/sun/net/httpserver/HttpsExchange.class */
public abstract class HttpsExchange extends HttpExchange {
    public abstract SSLSession getSSLSession();

    protected HttpsExchange() {
    }
}
