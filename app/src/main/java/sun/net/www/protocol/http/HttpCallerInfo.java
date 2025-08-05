package sun.net.www.protocol.http;

import java.net.Authenticator;
import java.net.InetAddress;
import java.net.URL;

/* loaded from: rt.jar:sun/net/www/protocol/http/HttpCallerInfo.class */
public final class HttpCallerInfo {
    public final URL url;
    public final String host;
    public final String protocol;
    public final String prompt;
    public final String scheme;
    public final int port;
    public final InetAddress addr;
    public final Authenticator.RequestorType authType;

    public HttpCallerInfo(HttpCallerInfo httpCallerInfo, String str) {
        this.url = httpCallerInfo.url;
        this.host = httpCallerInfo.host;
        this.protocol = httpCallerInfo.protocol;
        this.prompt = httpCallerInfo.prompt;
        this.port = httpCallerInfo.port;
        this.addr = httpCallerInfo.addr;
        this.authType = httpCallerInfo.authType;
        this.scheme = str;
    }

    public HttpCallerInfo(URL url) {
        InetAddress byName;
        this.url = url;
        this.prompt = "";
        this.host = url.getHost();
        int port = url.getPort();
        if (port == -1) {
            this.port = url.getDefaultPort();
        } else {
            this.port = port;
        }
        try {
            byName = InetAddress.getByName(url.getHost());
        } catch (Exception e2) {
            byName = null;
        }
        this.addr = byName;
        this.protocol = url.getProtocol();
        this.authType = Authenticator.RequestorType.SERVER;
        this.scheme = "";
    }

    public HttpCallerInfo(URL url, String str, int i2) {
        this.url = url;
        this.host = str;
        this.port = i2;
        this.prompt = "";
        this.addr = null;
        this.protocol = url.getProtocol();
        this.authType = Authenticator.RequestorType.PROXY;
        this.scheme = "";
    }
}
