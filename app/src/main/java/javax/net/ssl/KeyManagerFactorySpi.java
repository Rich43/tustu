package javax.net.ssl;

import java.security.InvalidAlgorithmParameterException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;

/* loaded from: rt.jar:javax/net/ssl/KeyManagerFactorySpi.class */
public abstract class KeyManagerFactorySpi {
    protected abstract void engineInit(KeyStore keyStore, char[] cArr) throws NoSuchAlgorithmException, UnrecoverableKeyException, KeyStoreException;

    protected abstract void engineInit(ManagerFactoryParameters managerFactoryParameters) throws InvalidAlgorithmParameterException;

    protected abstract KeyManager[] engineGetKeyManagers();
}
