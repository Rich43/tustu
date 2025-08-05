package com.sun.net.ssl;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import java.security.SecureRandom;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocketFactory;

/* compiled from: SSLSecurity.java */
/* loaded from: rt.jar:com/sun/net/ssl/SSLContextSpiWrapper.class */
final class SSLContextSpiWrapper extends SSLContextSpi {
    private javax.net.ssl.SSLContext theSSLContext;

    SSLContextSpiWrapper(String str, Provider provider) throws NoSuchAlgorithmException {
        this.theSSLContext = javax.net.ssl.SSLContext.getInstance(str, provider);
    }

    @Override // com.sun.net.ssl.SSLContextSpi
    protected void engineInit(KeyManager[] keyManagerArr, TrustManager[] trustManagerArr, SecureRandom secureRandom) throws KeyManagementException {
        javax.net.ssl.KeyManager[] keyManagerArr2;
        javax.net.ssl.TrustManager[] trustManagerArr2;
        if (keyManagerArr != null) {
            keyManagerArr2 = new javax.net.ssl.KeyManager[keyManagerArr.length];
            int i2 = 0;
            int i3 = 0;
            while (i2 < keyManagerArr.length) {
                if (!(keyManagerArr[i2] instanceof javax.net.ssl.KeyManager)) {
                    if (keyManagerArr[i2] instanceof X509KeyManager) {
                        keyManagerArr2[i3] = new X509KeyManagerJavaxWrapper((X509KeyManager) keyManagerArr[i2]);
                        i3++;
                    }
                } else {
                    keyManagerArr2[i3] = (javax.net.ssl.KeyManager) keyManagerArr[i2];
                    i3++;
                }
                i2++;
            }
            if (i3 != i2) {
                keyManagerArr2 = (javax.net.ssl.KeyManager[]) SSLSecurity.truncateArray(keyManagerArr2, new javax.net.ssl.KeyManager[i3]);
            }
        } else {
            keyManagerArr2 = null;
        }
        if (trustManagerArr != null) {
            trustManagerArr2 = new javax.net.ssl.TrustManager[trustManagerArr.length];
            int i4 = 0;
            int i5 = 0;
            while (i4 < trustManagerArr.length) {
                if (!(trustManagerArr[i4] instanceof javax.net.ssl.TrustManager)) {
                    if (trustManagerArr[i4] instanceof X509TrustManager) {
                        trustManagerArr2[i5] = new X509TrustManagerJavaxWrapper((X509TrustManager) trustManagerArr[i4]);
                        i5++;
                    }
                } else {
                    trustManagerArr2[i5] = (javax.net.ssl.TrustManager) trustManagerArr[i4];
                    i5++;
                }
                i4++;
            }
            if (i5 != i4) {
                trustManagerArr2 = (javax.net.ssl.TrustManager[]) SSLSecurity.truncateArray(trustManagerArr2, new javax.net.ssl.TrustManager[i5]);
            }
        } else {
            trustManagerArr2 = null;
        }
        this.theSSLContext.init(keyManagerArr2, trustManagerArr2, secureRandom);
    }

    @Override // com.sun.net.ssl.SSLContextSpi
    protected SSLSocketFactory engineGetSocketFactory() {
        return this.theSSLContext.getSocketFactory();
    }

    @Override // com.sun.net.ssl.SSLContextSpi
    protected SSLServerSocketFactory engineGetServerSocketFactory() {
        return this.theSSLContext.getServerSocketFactory();
    }
}
