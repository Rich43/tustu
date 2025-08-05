package com.sun.net.ssl;

import java.security.KeyStore;
import java.security.KeyStoreException;

@Deprecated
/* loaded from: rt.jar:com/sun/net/ssl/TrustManagerFactorySpi.class */
public abstract class TrustManagerFactorySpi {
    protected abstract void engineInit(KeyStore keyStore) throws KeyStoreException;

    protected abstract TrustManager[] engineGetTrustManagers();
}
