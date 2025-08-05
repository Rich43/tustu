package sun.security.ssl;

import java.security.InvalidAlgorithmParameterException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.util.Collections;
import java.util.List;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactorySpi;
import javax.net.ssl.KeyStoreBuilderParameters;
import javax.net.ssl.ManagerFactoryParameters;
import javax.net.ssl.X509ExtendedKeyManager;

/* loaded from: jsse.jar:sun/security/ssl/KeyManagerFactoryImpl.class */
abstract class KeyManagerFactoryImpl extends KeyManagerFactorySpi {
    X509ExtendedKeyManager keyManager;
    boolean isInitialized;

    KeyManagerFactoryImpl() {
    }

    @Override // javax.net.ssl.KeyManagerFactorySpi
    protected KeyManager[] engineGetKeyManagers() {
        if (!this.isInitialized) {
            throw new IllegalStateException("KeyManagerFactoryImpl is not initialized");
        }
        return new KeyManager[]{this.keyManager};
    }

    /* loaded from: jsse.jar:sun/security/ssl/KeyManagerFactoryImpl$SunX509.class */
    public static final class SunX509 extends KeyManagerFactoryImpl {
        @Override // javax.net.ssl.KeyManagerFactorySpi
        protected void engineInit(KeyStore keyStore, char[] cArr) throws NoSuchAlgorithmException, UnrecoverableKeyException, KeyStoreException {
            if (keyStore != null && SunJSSE.isFIPS() && keyStore.getProvider() != SunJSSE.cryptoProvider) {
                throw new KeyStoreException("FIPS mode: KeyStore must be from provider " + SunJSSE.cryptoProvider.getName());
            }
            this.keyManager = new SunX509KeyManagerImpl(keyStore, cArr);
            this.isInitialized = true;
        }

        @Override // javax.net.ssl.KeyManagerFactorySpi
        protected void engineInit(ManagerFactoryParameters managerFactoryParameters) throws InvalidAlgorithmParameterException {
            throw new InvalidAlgorithmParameterException("SunX509KeyManager does not use ManagerFactoryParameters");
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/KeyManagerFactoryImpl$X509.class */
    public static final class X509 extends KeyManagerFactoryImpl {
        @Override // javax.net.ssl.KeyManagerFactorySpi
        protected void engineInit(KeyStore keyStore, char[] cArr) throws NoSuchAlgorithmException, UnrecoverableKeyException, KeyStoreException {
            if (keyStore == null) {
                this.keyManager = new X509KeyManagerImpl((List<KeyStore.Builder>) Collections.emptyList());
            } else {
                if (SunJSSE.isFIPS() && keyStore.getProvider() != SunJSSE.cryptoProvider) {
                    throw new KeyStoreException("FIPS mode: KeyStore must be from provider " + SunJSSE.cryptoProvider.getName());
                }
                try {
                    this.keyManager = new X509KeyManagerImpl(KeyStore.Builder.newInstance(keyStore, new KeyStore.PasswordProtection(cArr)));
                } catch (RuntimeException e2) {
                    throw new KeyStoreException("initialization failed", e2);
                }
            }
            this.isInitialized = true;
        }

        @Override // javax.net.ssl.KeyManagerFactorySpi
        protected void engineInit(ManagerFactoryParameters managerFactoryParameters) throws InvalidAlgorithmParameterException {
            if (!(managerFactoryParameters instanceof KeyStoreBuilderParameters)) {
                throw new InvalidAlgorithmParameterException("Parameters must be instance of KeyStoreBuilderParameters");
            }
            if (SunJSSE.isFIPS()) {
                throw new InvalidAlgorithmParameterException("FIPS mode: KeyStoreBuilderParameters not supported");
            }
            this.keyManager = new X509KeyManagerImpl(((KeyStoreBuilderParameters) managerFactoryParameters).getParameters());
            this.isInitialized = true;
        }
    }
}
