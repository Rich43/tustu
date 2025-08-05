package com.sun.net.ssl;

import java.security.AccessController;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivilegedAction;
import java.security.Provider;
import java.security.Security;
import java.security.UnrecoverableKeyException;

@Deprecated
/* loaded from: rt.jar:com/sun/net/ssl/KeyManagerFactory.class */
public class KeyManagerFactory {
    private Provider provider;
    private KeyManagerFactorySpi factorySpi;
    private String algorithm;

    public static final String getDefaultAlgorithm() {
        String str = (String) AccessController.doPrivileged(new PrivilegedAction<String>() { // from class: com.sun.net.ssl.KeyManagerFactory.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public String run2() {
                return Security.getProperty("sun.ssl.keymanager.type");
            }
        });
        if (str == null) {
            str = "SunX509";
        }
        return str;
    }

    protected KeyManagerFactory(KeyManagerFactorySpi keyManagerFactorySpi, Provider provider, String str) {
        this.factorySpi = keyManagerFactorySpi;
        this.provider = provider;
        this.algorithm = str;
    }

    public final String getAlgorithm() {
        return this.algorithm;
    }

    public static final KeyManagerFactory getInstance(String str) throws NoSuchAlgorithmException {
        try {
            Object[] impl = SSLSecurity.getImpl(str, "KeyManagerFactory", (String) null);
            return new KeyManagerFactory((KeyManagerFactorySpi) impl[0], (Provider) impl[1], str);
        } catch (NoSuchProviderException e2) {
            throw new NoSuchAlgorithmException(str + " not found");
        }
    }

    public static final KeyManagerFactory getInstance(String str, String str2) throws NoSuchAlgorithmException, NoSuchProviderException {
        if (str2 == null || str2.length() == 0) {
            throw new IllegalArgumentException("missing provider");
        }
        Object[] impl = SSLSecurity.getImpl(str, "KeyManagerFactory", str2);
        return new KeyManagerFactory((KeyManagerFactorySpi) impl[0], (Provider) impl[1], str);
    }

    public static final KeyManagerFactory getInstance(String str, Provider provider) throws NoSuchAlgorithmException {
        if (provider == null) {
            throw new IllegalArgumentException("missing provider");
        }
        Object[] impl = SSLSecurity.getImpl(str, "KeyManagerFactory", provider);
        return new KeyManagerFactory((KeyManagerFactorySpi) impl[0], (Provider) impl[1], str);
    }

    public final Provider getProvider() {
        return this.provider;
    }

    public void init(KeyStore keyStore, char[] cArr) throws NoSuchAlgorithmException, UnrecoverableKeyException, KeyStoreException {
        this.factorySpi.engineInit(keyStore, cArr);
    }

    public KeyManager[] getKeyManagers() {
        return this.factorySpi.engineGetKeyManagers();
    }
}
