package com.sun.net.ssl;

import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;

/* compiled from: SSLSecurity.java */
/* loaded from: rt.jar:com/sun/net/ssl/TrustManagerFactorySpiWrapper.class */
final class TrustManagerFactorySpiWrapper extends TrustManagerFactorySpi {
    private javax.net.ssl.TrustManagerFactory theTrustManagerFactory;

    TrustManagerFactorySpiWrapper(String str, Provider provider) throws NoSuchAlgorithmException {
        this.theTrustManagerFactory = javax.net.ssl.TrustManagerFactory.getInstance(str, provider);
    }

    @Override // com.sun.net.ssl.TrustManagerFactorySpi
    protected void engineInit(KeyStore keyStore) throws KeyStoreException {
        this.theTrustManagerFactory.init(keyStore);
    }

    @Override // com.sun.net.ssl.TrustManagerFactorySpi
    protected TrustManager[] engineGetTrustManagers() {
        javax.net.ssl.TrustManager[] trustManagers = this.theTrustManagerFactory.getTrustManagers();
        TrustManager[] trustManagerArr = new TrustManager[trustManagers.length];
        int i2 = 0;
        int i3 = 0;
        while (i2 < trustManagers.length) {
            if (!(trustManagers[i2] instanceof TrustManager)) {
                if (trustManagers[i2] instanceof javax.net.ssl.X509TrustManager) {
                    trustManagerArr[i3] = new X509TrustManagerComSunWrapper((javax.net.ssl.X509TrustManager) trustManagers[i2]);
                    i3++;
                }
            } else {
                trustManagerArr[i3] = (TrustManager) trustManagers[i2];
                i3++;
            }
            i2++;
        }
        if (i3 != i2) {
            trustManagerArr = (TrustManager[]) SSLSecurity.truncateArray(trustManagerArr, new TrustManager[i3]);
        }
        return trustManagerArr;
    }
}
