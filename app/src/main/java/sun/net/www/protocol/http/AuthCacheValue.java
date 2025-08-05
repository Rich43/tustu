package sun.net.www.protocol.http;

import java.io.Serializable;
import java.net.PasswordAuthentication;

/* loaded from: rt.jar:sun/net/www/protocol/http/AuthCacheValue.class */
public abstract class AuthCacheValue implements Serializable {
    static final long serialVersionUID = 735249334068211611L;
    protected static AuthCache cache = new AuthCacheImpl();

    /* loaded from: rt.jar:sun/net/www/protocol/http/AuthCacheValue$Type.class */
    public enum Type {
        Proxy,
        Server
    }

    abstract Type getAuthType();

    abstract AuthScheme getAuthScheme();

    abstract String getHost();

    abstract int getPort();

    abstract String getRealm();

    abstract String getPath();

    abstract String getProtocolScheme();

    abstract PasswordAuthentication credentials();

    public static void setAuthCache(AuthCache authCache) {
        cache = authCache;
    }

    AuthCacheValue() {
    }
}
