package javax.crypto;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Provider;
import java.security.ProviderException;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Iterator;
import sun.security.jca.GetInstance;
import sun.security.util.Debug;

/* loaded from: jce.jar:javax/crypto/KeyAgreement.class */
public class KeyAgreement {
    private static final Debug debug = Debug.getInstance("jca", "KeyAgreement");
    private static final Debug pdebug = Debug.getInstance("provider", "Provider");
    private static final boolean skipDebug;
    private Provider provider;
    private KeyAgreementSpi spi;
    private final String algorithm;
    private Provider.Service firstService;
    private Iterator<Provider.Service> serviceIterator;
    private final Object lock = null;
    private static int warnCount;
    private static final int I_NO_PARAMS = 1;
    private static final int I_PARAMS = 2;

    static {
        skipDebug = Debug.isOn("engine=") && !Debug.isOn("keyagreement");
        warnCount = 10;
    }

    protected KeyAgreement(KeyAgreementSpi keyAgreementSpi, Provider provider, String str) {
        this.spi = keyAgreementSpi;
        this.provider = provider;
        this.algorithm = str;
    }

    private KeyAgreement(Provider.Service service, Iterator<Provider.Service> it, String str) {
        this.firstService = service;
        this.serviceIterator = it;
        this.algorithm = str;
    }

    public final String getAlgorithm() {
        return this.algorithm;
    }

    public static final KeyAgreement getInstance(String str) throws NoSuchAlgorithmException {
        Iterator<Provider.Service> it = GetInstance.getServices("KeyAgreement", str).iterator();
        while (it.hasNext()) {
            Provider.Service next = it.next();
            if (JceSecurity.canUseProvider(next.getProvider())) {
                return new KeyAgreement(next, it, str);
            }
        }
        throw new NoSuchAlgorithmException("Algorithm " + str + " not available");
    }

    public static final KeyAgreement getInstance(String str, String str2) throws NoSuchAlgorithmException, NoSuchProviderException {
        GetInstance.Instance jceSecurity = JceSecurity.getInstance("KeyAgreement", (Class<?>) KeyAgreementSpi.class, str, str2);
        return new KeyAgreement((KeyAgreementSpi) jceSecurity.impl, jceSecurity.provider, str);
    }

    public static final KeyAgreement getInstance(String str, Provider provider) throws NoSuchAlgorithmException {
        GetInstance.Instance jceSecurity = JceSecurity.getInstance("KeyAgreement", (Class<?>) KeyAgreementSpi.class, str, provider);
        return new KeyAgreement((KeyAgreementSpi) jceSecurity.impl, jceSecurity.provider, str);
    }

    void chooseFirstProvider() {
        Provider.Service next;
        if (this.spi != null) {
            return;
        }
        synchronized (this.lock) {
            if (this.spi != null) {
                return;
            }
            if (debug != null) {
                int i2 = warnCount - 1;
                warnCount = i2;
                if (i2 >= 0) {
                    debug.println("KeyAgreement.init() not first method called, disabling delayed provider selection");
                    if (i2 == 0) {
                        debug.println("Further warnings of this type will be suppressed");
                    }
                    new Exception("Call trace").printStackTrace();
                }
            }
            Exception exc = null;
            while (true) {
                if (this.firstService != null || this.serviceIterator.hasNext()) {
                    if (this.firstService != null) {
                        next = this.firstService;
                        this.firstService = null;
                    } else {
                        next = this.serviceIterator.next();
                    }
                    if (JceSecurity.canUseProvider(next.getProvider())) {
                        try {
                            Object objNewInstance = next.newInstance(null);
                            if (objNewInstance instanceof KeyAgreementSpi) {
                                this.spi = (KeyAgreementSpi) objNewInstance;
                                this.provider = next.getProvider();
                                this.firstService = null;
                                this.serviceIterator = null;
                                return;
                            }
                        } catch (Exception e2) {
                            exc = e2;
                        }
                    }
                } else {
                    ProviderException providerException = new ProviderException("Could not construct KeyAgreementSpi instance");
                    if (exc != null) {
                        providerException.initCause(exc);
                    }
                    throw providerException;
                }
            }
        }
    }

    private void implInit(KeyAgreementSpi keyAgreementSpi, int i2, Key key, AlgorithmParameterSpec algorithmParameterSpec, SecureRandom secureRandom) throws InvalidKeyException, InvalidAlgorithmParameterException {
        if (i2 == 1) {
            keyAgreementSpi.engineInit(key, secureRandom);
        } else {
            keyAgreementSpi.engineInit(key, algorithmParameterSpec, secureRandom);
        }
    }

    private void chooseProvider(int i2, Key key, AlgorithmParameterSpec algorithmParameterSpec, SecureRandom secureRandom) throws InvalidKeyException, InvalidAlgorithmParameterException {
        Provider.Service next;
        synchronized (this.lock) {
            if (this.spi != null) {
                implInit(this.spi, i2, key, algorithmParameterSpec, secureRandom);
                return;
            }
            Exception exc = null;
            while (true) {
                if (this.firstService != null || this.serviceIterator.hasNext()) {
                    if (this.firstService != null) {
                        next = this.firstService;
                        this.firstService = null;
                    } else {
                        next = this.serviceIterator.next();
                    }
                    if (next.supportsParameter(key) && JceSecurity.canUseProvider(next.getProvider())) {
                        try {
                            KeyAgreementSpi keyAgreementSpi = (KeyAgreementSpi) next.newInstance(null);
                            implInit(keyAgreementSpi, i2, key, algorithmParameterSpec, secureRandom);
                            this.provider = next.getProvider();
                            this.spi = keyAgreementSpi;
                            this.firstService = null;
                            this.serviceIterator = null;
                            return;
                        } catch (Exception e2) {
                            if (exc == null) {
                                exc = e2;
                            }
                        }
                    }
                } else {
                    if (exc instanceof InvalidKeyException) {
                        throw ((InvalidKeyException) exc);
                    }
                    if (exc instanceof InvalidAlgorithmParameterException) {
                        throw ((InvalidAlgorithmParameterException) exc);
                    }
                    if (exc instanceof RuntimeException) {
                        throw ((RuntimeException) exc);
                    }
                    throw new InvalidKeyException("No installed provider supports this key: " + (key != null ? key.getClass().getName() : "(null)"), exc);
                }
            }
        }
    }

    public final Provider getProvider() {
        chooseFirstProvider();
        return this.provider;
    }

    public final void init(Key key) throws InvalidKeyException {
        init(key, JceSecurity.RANDOM);
    }

    public final void init(Key key, SecureRandom secureRandom) throws InvalidKeyException {
        if (this.spi != null) {
            this.spi.engineInit(key, secureRandom);
        } else {
            try {
                chooseProvider(1, key, null, secureRandom);
            } catch (InvalidAlgorithmParameterException e2) {
                throw new InvalidKeyException(e2);
            }
        }
        if (!skipDebug && pdebug != null) {
            pdebug.println("KeyAgreement." + this.algorithm + " algorithm from: " + this.provider.getName());
        }
    }

    public final void init(Key key, AlgorithmParameterSpec algorithmParameterSpec) throws InvalidKeyException, InvalidAlgorithmParameterException {
        init(key, algorithmParameterSpec, JceSecurity.RANDOM);
    }

    public final void init(Key key, AlgorithmParameterSpec algorithmParameterSpec, SecureRandom secureRandom) throws InvalidKeyException, InvalidAlgorithmParameterException {
        if (this.spi != null) {
            this.spi.engineInit(key, algorithmParameterSpec, secureRandom);
        } else {
            chooseProvider(2, key, algorithmParameterSpec, secureRandom);
        }
        if (!skipDebug && pdebug != null) {
            pdebug.println("KeyAgreement." + this.algorithm + " algorithm from: " + this.provider.getName());
        }
    }

    public final Key doPhase(Key key, boolean z2) throws IllegalStateException, InvalidKeyException {
        chooseFirstProvider();
        return this.spi.engineDoPhase(key, z2);
    }

    public final byte[] generateSecret() throws IllegalStateException {
        chooseFirstProvider();
        return this.spi.engineGenerateSecret();
    }

    public final int generateSecret(byte[] bArr, int i2) throws IllegalStateException, ShortBufferException {
        chooseFirstProvider();
        return this.spi.engineGenerateSecret(bArr, i2);
    }

    public final SecretKey generateSecret(String str) throws IllegalStateException, NoSuchAlgorithmException, InvalidKeyException {
        chooseFirstProvider();
        return this.spi.engineGenerateSecret(str);
    }
}
