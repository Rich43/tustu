package com.sun.net.ssl;

import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import java.security.UnrecoverableKeyException;

/* compiled from: SSLSecurity.java */
/* loaded from: rt.jar:com/sun/net/ssl/KeyManagerFactorySpiWrapper.class */
final class KeyManagerFactorySpiWrapper extends KeyManagerFactorySpi {
    private javax.net.ssl.KeyManagerFactory theKeyManagerFactory;

    KeyManagerFactorySpiWrapper(String str, Provider provider) throws NoSuchAlgorithmException {
        this.theKeyManagerFactory = javax.net.ssl.KeyManagerFactory.getInstance(str, provider);
    }

    @Override // com.sun.net.ssl.KeyManagerFactorySpi
    protected void engineInit(KeyStore keyStore, char[] cArr) throws NoSuchAlgorithmException, UnrecoverableKeyException, KeyStoreException {
        this.theKeyManagerFactory.init(keyStore, cArr);
    }

    @Override // com.sun.net.ssl.KeyManagerFactorySpi
    protected KeyManager[] engineGetKeyManagers() {
        javax.net.ssl.KeyManager[] keyManagers = this.theKeyManagerFactory.getKeyManagers();
        KeyManager[] keyManagerArr = new KeyManager[keyManagers.length];
        int i2 = 0;
        int i3 = 0;
        while (i2 < keyManagers.length) {
            if (!(keyManagers[i2] instanceof KeyManager)) {
                if (keyManagers[i2] instanceof javax.net.ssl.X509KeyManager) {
                    keyManagerArr[i3] = new X509KeyManagerComSunWrapper((javax.net.ssl.X509KeyManager) keyManagers[i2]);
                    i3++;
                }
            } else {
                keyManagerArr[i3] = (KeyManager) keyManagers[i2];
                i3++;
            }
            i2++;
        }
        if (i3 != i2) {
            keyManagerArr = (KeyManager[]) SSLSecurity.truncateArray(keyManagerArr, new KeyManager[i3]);
        }
        return keyManagerArr;
    }
}
