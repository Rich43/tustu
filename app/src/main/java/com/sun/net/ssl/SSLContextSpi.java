package com.sun.net.ssl;

import java.security.KeyManagementException;
import java.security.SecureRandom;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocketFactory;

@Deprecated
/* loaded from: rt.jar:com/sun/net/ssl/SSLContextSpi.class */
public abstract class SSLContextSpi {
    protected abstract void engineInit(KeyManager[] keyManagerArr, TrustManager[] trustManagerArr, SecureRandom secureRandom) throws KeyManagementException;

    protected abstract SSLSocketFactory engineGetSocketFactory();

    protected abstract SSLServerSocketFactory engineGetServerSocketFactory();
}
