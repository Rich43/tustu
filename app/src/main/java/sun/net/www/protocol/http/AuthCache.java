package sun.net.www.protocol.http;

/* loaded from: rt.jar:sun/net/www/protocol/http/AuthCache.class */
public interface AuthCache {
    void put(String str, AuthCacheValue authCacheValue);

    AuthCacheValue get(String str, String str2);

    void remove(String str, AuthCacheValue authCacheValue);
}
