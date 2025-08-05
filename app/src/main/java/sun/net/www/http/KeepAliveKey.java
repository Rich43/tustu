package sun.net.www.http;

import java.net.URL;

/* compiled from: KeepAliveCache.java */
/* loaded from: rt.jar:sun/net/www/http/KeepAliveKey.class */
class KeepAliveKey {
    private String protocol;
    private String host;
    private int port;
    private Object obj;

    public KeepAliveKey(URL url, Object obj) {
        this.protocol = null;
        this.host = null;
        this.port = 0;
        this.obj = null;
        this.protocol = url.getProtocol();
        this.host = url.getHost();
        this.port = url.getPort();
        this.obj = obj;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof KeepAliveKey)) {
            return false;
        }
        KeepAliveKey keepAliveKey = (KeepAliveKey) obj;
        return this.host.equals(keepAliveKey.host) && this.port == keepAliveKey.port && this.protocol.equals(keepAliveKey.protocol) && this.obj == keepAliveKey.obj;
    }

    public int hashCode() {
        String str = this.protocol + this.host + this.port;
        return this.obj == null ? str.hashCode() : str.hashCode() + this.obj.hashCode();
    }
}
