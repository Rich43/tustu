package sun.net.www.protocol.https;

import java.io.IOException;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;

/* loaded from: rt.jar:sun/net/www/protocol/https/Handler.class */
public class Handler extends sun.net.www.protocol.http.Handler {
    protected String proxy;
    protected int proxyPort;

    @Override // sun.net.www.protocol.http.Handler, java.net.URLStreamHandler
    protected int getDefaultPort() {
        return 443;
    }

    public Handler() {
        this.proxy = null;
        this.proxyPort = -1;
    }

    public Handler(String str, int i2) {
        this.proxy = str;
        this.proxyPort = i2;
    }

    @Override // sun.net.www.protocol.http.Handler, java.net.URLStreamHandler
    protected URLConnection openConnection(URL url) throws IOException {
        return openConnection(url, (Proxy) null);
    }

    @Override // sun.net.www.protocol.http.Handler, java.net.URLStreamHandler
    protected URLConnection openConnection(URL url, Proxy proxy) throws IOException {
        return new HttpsURLConnectionImpl(url, proxy, this);
    }
}
