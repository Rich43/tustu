package com.sun.net.ssl;

import java.security.AccessController;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivilegedAction;
import java.security.Provider;
import java.security.Security;

@Deprecated
/* loaded from: rt.jar:com/sun/net/ssl/TrustManagerFactory.class */
public class TrustManagerFactory {
    private Provider provider;
    private TrustManagerFactorySpi factorySpi;
    private String algorithm;

    public static final String getDefaultAlgorithm() {
        String str = (String) AccessController.doPrivileged(new PrivilegedAction<String>() { // from class: com.sun.net.ssl.TrustManagerFactory.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public String run2() {
                return Security.getProperty("sun.ssl.trustmanager.type");
            }
        });
        if (str == null) {
            str = "SunX509";
        }
        return str;
    }

    protected TrustManagerFactory(TrustManagerFactorySpi trustManagerFactorySpi, Provider provider, String str) {
        this.factorySpi = trustManagerFactorySpi;
        this.provider = provider;
        this.algorithm = str;
    }

    public final String getAlgorithm() {
        return this.algorithm;
    }

    public static final TrustManagerFactory getInstance(String str) throws NoSuchAlgorithmException {
        try {
            Object[] impl = SSLSecurity.getImpl(str, "TrustManagerFactory", (String) null);
            return new TrustManagerFactory((TrustManagerFactorySpi) impl[0], (Provider) impl[1], str);
        } catch (NoSuchProviderException e2) {
            throw new NoSuchAlgorithmException(str + " not found");
        }
    }

    public static final TrustManagerFactory getInstance(String str, String str2) throws NoSuchAlgorithmException, NoSuchProviderException {
        if (str2 == null || str2.length() == 0) {
            throw new IllegalArgumentException("missing provider");
        }
        Object[] impl = SSLSecurity.getImpl(str, "TrustManagerFactory", str2);
        return new TrustManagerFactory((TrustManagerFactorySpi) impl[0], (Provider) impl[1], str);
    }

    public static final TrustManagerFactory getInstance(String str, Provider provider) throws NoSuchAlgorithmException {
        if (provider == null) {
            throw new IllegalArgumentException("missing provider");
        }
        Object[] impl = SSLSecurity.getImpl(str, "TrustManagerFactory", provider);
        return new TrustManagerFactory((TrustManagerFactorySpi) impl[0], (Provider) impl[1], str);
    }

    public final Provider getProvider() {
        return this.provider;
    }

    public void init(KeyStore keyStore) throws KeyStoreException {
        this.factorySpi.engineInit(keyStore);
    }

    public TrustManager[] getTrustManagers() {
        return this.factorySpi.engineGetTrustManagers();
    }
}
