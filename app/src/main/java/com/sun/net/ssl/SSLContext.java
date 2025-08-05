package com.sun.net.ssl;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Provider;
import java.security.SecureRandom;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocketFactory;

@Deprecated
/* loaded from: rt.jar:com/sun/net/ssl/SSLContext.class */
public class SSLContext {
    private Provider provider;
    private SSLContextSpi contextSpi;
    private String protocol;

    protected SSLContext(SSLContextSpi sSLContextSpi, Provider provider, String str) {
        this.contextSpi = sSLContextSpi;
        this.provider = provider;
        this.protocol = str;
    }

    public static SSLContext getInstance(String str) throws NoSuchAlgorithmException {
        try {
            Object[] impl = SSLSecurity.getImpl(str, "SSLContext", (String) null);
            return new SSLContext((SSLContextSpi) impl[0], (Provider) impl[1], str);
        } catch (NoSuchProviderException e2) {
            throw new NoSuchAlgorithmException(str + " not found");
        }
    }

    public static SSLContext getInstance(String str, String str2) throws NoSuchAlgorithmException, NoSuchProviderException {
        if (str2 == null || str2.length() == 0) {
            throw new IllegalArgumentException("missing provider");
        }
        Object[] impl = SSLSecurity.getImpl(str, "SSLContext", str2);
        return new SSLContext((SSLContextSpi) impl[0], (Provider) impl[1], str);
    }

    public static SSLContext getInstance(String str, Provider provider) throws NoSuchAlgorithmException {
        if (provider == null) {
            throw new IllegalArgumentException("missing provider");
        }
        Object[] impl = SSLSecurity.getImpl(str, "SSLContext", provider);
        return new SSLContext((SSLContextSpi) impl[0], (Provider) impl[1], str);
    }

    public final String getProtocol() {
        return this.protocol;
    }

    public final Provider getProvider() {
        return this.provider;
    }

    public final void init(KeyManager[] keyManagerArr, TrustManager[] trustManagerArr, SecureRandom secureRandom) throws KeyManagementException {
        this.contextSpi.engineInit(keyManagerArr, trustManagerArr, secureRandom);
    }

    public final SSLSocketFactory getSocketFactory() {
        return this.contextSpi.engineGetSocketFactory();
    }

    public final SSLServerSocketFactory getServerSocketFactory() {
        return this.contextSpi.engineGetServerSocketFactory();
    }
}
