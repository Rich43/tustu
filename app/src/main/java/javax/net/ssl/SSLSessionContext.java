package javax.net.ssl;

import java.util.Enumeration;

/* loaded from: rt.jar:javax/net/ssl/SSLSessionContext.class */
public interface SSLSessionContext {
    SSLSession getSession(byte[] bArr);

    Enumeration<byte[]> getIds();

    void setSessionTimeout(int i2) throws IllegalArgumentException;

    int getSessionTimeout();

    void setSessionCacheSize(int i2) throws IllegalArgumentException;

    int getSessionCacheSize();
}
