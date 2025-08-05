package javax.net.ssl;

import java.security.InvalidAlgorithmParameterException;
import java.security.KeyStore;
import java.security.KeyStoreException;

/* loaded from: rt.jar:javax/net/ssl/TrustManagerFactorySpi.class */
public abstract class TrustManagerFactorySpi {
    protected abstract void engineInit(KeyStore keyStore) throws KeyStoreException;

    protected abstract void engineInit(ManagerFactoryParameters managerFactoryParameters) throws InvalidAlgorithmParameterException;

    protected abstract TrustManager[] engineGetTrustManagers();
}
