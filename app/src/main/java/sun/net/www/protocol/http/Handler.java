package sun.net.www.protocol.http;

import java.io.IOException;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;

/* loaded from: rt.jar:sun/net/www/protocol/http/Handler.class */
public class Handler extends URLStreamHandler {
    protected String proxy;
    protected int proxyPort;

    @Override // java.net.URLStreamHandler
    protected int getDefaultPort() {
        return 80;
    }

    public Handler() {
        this.proxy = null;
        this.proxyPort = -1;
    }

    public Handler(String str, int i2) {
        this.proxy = str;
        this.proxyPort = i2;
    }

    @Override // java.net.URLStreamHandler
    protected URLConnection openConnection(URL url) throws IOException {
        return openConnection(url, (Proxy) null);
    }

    @Override // java.net.URLStreamHandler
    protected URLConnection openConnection(URL url, Proxy proxy) throws IOException {
        return new HttpURLConnection(url, proxy, this);
    }
}
