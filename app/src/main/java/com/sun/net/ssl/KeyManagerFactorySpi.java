package com.sun.net.ssl;

import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;

@Deprecated
/* loaded from: rt.jar:com/sun/net/ssl/KeyManagerFactorySpi.class */
public abstract class KeyManagerFactorySpi {
    protected abstract void engineInit(KeyStore keyStore, char[] cArr) throws NoSuchAlgorithmException, UnrecoverableKeyException, KeyStoreException;

    protected abstract KeyManager[] engineGetKeyManagers();
}
