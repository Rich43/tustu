package com.sun.net.ssl.internal.www.protocol.https;

import java.io.IOException;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;

/* loaded from: rt.jar:com/sun/net/ssl/internal/www/protocol/https/Handler.class */
public class Handler extends sun.net.www.protocol.https.Handler {
    public Handler() {
    }

    public Handler(String str, int i2) {
        super(str, i2);
    }

    @Override // sun.net.www.protocol.https.Handler, sun.net.www.protocol.http.Handler, java.net.URLStreamHandler
    protected URLConnection openConnection(URL url) throws IOException {
        return openConnection(url, (Proxy) null);
    }

    @Override // sun.net.www.protocol.https.Handler, sun.net.www.protocol.http.Handler, java.net.URLStreamHandler
    protected URLConnection openConnection(URL url, Proxy proxy) throws IOException {
        return new HttpsURLConnectionOldImpl(url, proxy, this);
    }
}
