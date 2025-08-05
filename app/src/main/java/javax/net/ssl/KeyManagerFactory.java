package javax.net.ssl;

import java.security.AccessController;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivilegedAction;
import java.security.Provider;
import java.security.Security;
import java.security.UnrecoverableKeyException;
import sun.security.jca.GetInstance;

/* loaded from: rt.jar:javax/net/ssl/KeyManagerFactory.class */
public class KeyManagerFactory {
    private Provider provider;
    private KeyManagerFactorySpi factorySpi;
    private String algorithm;

    public static final String getDefaultAlgorithm() {
        String str = (String) AccessController.doPrivileged(new PrivilegedAction<String>() { // from class: javax.net.ssl.KeyManagerFactory.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public String run2() {
                return Security.getProperty("ssl.KeyManagerFactory.algorithm");
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
        GetInstance.Instance getInstance = GetInstance.getInstance("KeyManagerFactory", (Class<?>) KeyManagerFactorySpi.class, str);
        return new KeyManagerFactory((KeyManagerFactorySpi) getInstance.impl, getInstance.provider, str);
    }

    public static final KeyManagerFactory getInstance(String str, String str2) throws NoSuchAlgorithmException, NoSuchProviderException {
        GetInstance.Instance getInstance = GetInstance.getInstance("KeyManagerFactory", (Class<?>) KeyManagerFactorySpi.class, str, str2);
        return new KeyManagerFactory((KeyManagerFactorySpi) getInstance.impl, getInstance.provider, str);
    }

    public static final KeyManagerFactory getInstance(String str, Provider provider) throws NoSuchAlgorithmException {
        GetInstance.Instance getInstance = GetInstance.getInstance("KeyManagerFactory", (Class<?>) KeyManagerFactorySpi.class, str, provider);
        return new KeyManagerFactory((KeyManagerFactorySpi) getInstance.impl, getInstance.provider, str);
    }

    public final Provider getProvider() {
        return this.provider;
    }

    public final void init(KeyStore keyStore, char[] cArr) throws NoSuchAlgorithmException, UnrecoverableKeyException, KeyStoreException {
        this.factorySpi.engineInit(keyStore, cArr);
    }

    public final void init(ManagerFactoryParameters managerFactoryParameters) throws InvalidAlgorithmParameterException {
        this.factorySpi.engineInit(managerFactoryParameters);
    }

    public final KeyManager[] getKeyManagers() {
        return this.factorySpi.engineGetKeyManagers();
    }
}
