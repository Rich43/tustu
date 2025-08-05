package javax.crypto;

import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Provider;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Iterator;
import sun.security.jca.GetInstance;
import sun.security.util.Debug;

/* loaded from: jce.jar:javax/crypto/KeyGenerator.class */
public class KeyGenerator {
    private static final Debug pdebug = Debug.getInstance("provider", "Provider");
    private static final boolean skipDebug;
    private static final int I_NONE = 1;
    private static final int I_RANDOM = 2;
    private static final int I_PARAMS = 3;
    private static final int I_SIZE = 4;
    private Provider provider;
    private volatile KeyGeneratorSpi spi;
    private final String algorithm;
    private final Object lock;
    private Iterator<Provider.Service> serviceIterator;
    private int initType;
    private int initKeySize;
    private AlgorithmParameterSpec initParams;
    private SecureRandom initRandom;

    static {
        skipDebug = Debug.isOn("engine=") && !Debug.isOn("keygenerator");
    }

    protected KeyGenerator(KeyGeneratorSpi keyGeneratorSpi, Provider provider, String str) {
        this.lock = new Object();
        this.spi = keyGeneratorSpi;
        this.provider = provider;
        this.algorithm = str;
        if (!skipDebug && pdebug != null) {
            pdebug.println("KeyGenerator." + str + " algorithm from: " + this.provider.getName());
        }
    }

    private KeyGenerator(String str) throws NoSuchAlgorithmException {
        this.lock = new Object();
        this.algorithm = str;
        this.serviceIterator = GetInstance.getServices("KeyGenerator", str).iterator();
        this.initType = 1;
        if (nextSpi(null, false) == null) {
            throw new NoSuchAlgorithmException(str + " KeyGenerator not available");
        }
        if (!skipDebug && pdebug != null) {
            pdebug.println("KeyGenerator." + str + " algorithm from: " + this.provider.getName());
        }
    }

    public final String getAlgorithm() {
        return this.algorithm;
    }

    public static final KeyGenerator getInstance(String str) throws NoSuchAlgorithmException {
        return new KeyGenerator(str);
    }

    public static final KeyGenerator getInstance(String str, String str2) throws NoSuchAlgorithmException, NoSuchProviderException {
        GetInstance.Instance jceSecurity = JceSecurity.getInstance("KeyGenerator", (Class<?>) KeyGeneratorSpi.class, str, str2);
        return new KeyGenerator((KeyGeneratorSpi) jceSecurity.impl, jceSecurity.provider, str);
    }

    public static final KeyGenerator getInstance(String str, Provider provider) throws NoSuchAlgorithmException {
        GetInstance.Instance jceSecurity = JceSecurity.getInstance("KeyGenerator", (Class<?>) KeyGeneratorSpi.class, str, provider);
        return new KeyGenerator((KeyGeneratorSpi) jceSecurity.impl, jceSecurity.provider, str);
    }

    public final Provider getProvider() {
        Provider provider;
        synchronized (this.lock) {
            disableFailover();
            provider = this.provider;
        }
        return provider;
    }

    private KeyGeneratorSpi nextSpi(KeyGeneratorSpi keyGeneratorSpi, boolean z2) {
        synchronized (this.lock) {
            if (keyGeneratorSpi != null) {
                if (keyGeneratorSpi != this.spi) {
                    return this.spi;
                }
            }
            if (this.serviceIterator == null) {
                return null;
            }
            while (this.serviceIterator.hasNext()) {
                Provider.Service next = this.serviceIterator.next();
                if (JceSecurity.canUseProvider(next.getProvider())) {
                    try {
                        Object objNewInstance = next.newInstance(null);
                        if (objNewInstance instanceof KeyGeneratorSpi) {
                            KeyGeneratorSpi keyGeneratorSpi2 = (KeyGeneratorSpi) objNewInstance;
                            if (z2) {
                                if (this.initType == 4) {
                                    keyGeneratorSpi2.engineInit(this.initKeySize, this.initRandom);
                                } else if (this.initType == 3) {
                                    keyGeneratorSpi2.engineInit(this.initParams, this.initRandom);
                                } else if (this.initType == 2) {
                                    keyGeneratorSpi2.engineInit(this.initRandom);
                                } else if (this.initType != 1) {
                                    throw new AssertionError((Object) ("KeyGenerator initType: " + this.initType));
                                }
                            }
                            this.provider = next.getProvider();
                            this.spi = keyGeneratorSpi2;
                            return keyGeneratorSpi2;
                        }
                    } catch (Exception e2) {
                    }
                }
            }
            disableFailover();
            return null;
        }
    }

    void disableFailover() {
        this.serviceIterator = null;
        this.initType = 0;
        this.initParams = null;
        this.initRandom = null;
    }

    public final void init(SecureRandom secureRandom) {
        if (this.serviceIterator == null) {
            this.spi.engineInit(secureRandom);
            return;
        }
        RuntimeException runtimeException = null;
        KeyGeneratorSpi keyGeneratorSpiNextSpi = this.spi;
        do {
            try {
                keyGeneratorSpiNextSpi.engineInit(secureRandom);
                this.initType = 2;
                this.initKeySize = 0;
                this.initParams = null;
                this.initRandom = secureRandom;
                return;
            } catch (RuntimeException e2) {
                if (runtimeException == null) {
                    runtimeException = e2;
                }
                keyGeneratorSpiNextSpi = nextSpi(keyGeneratorSpiNextSpi, false);
            }
        } while (keyGeneratorSpiNextSpi != null);
        throw runtimeException;
    }

    public final void init(AlgorithmParameterSpec algorithmParameterSpec) throws InvalidAlgorithmParameterException {
        init(algorithmParameterSpec, JceSecurity.RANDOM);
    }

    public final void init(AlgorithmParameterSpec algorithmParameterSpec, SecureRandom secureRandom) throws InvalidAlgorithmParameterException {
        if (this.serviceIterator == null) {
            this.spi.engineInit(algorithmParameterSpec, secureRandom);
            return;
        }
        Exception exc = null;
        KeyGeneratorSpi keyGeneratorSpiNextSpi = this.spi;
        do {
            try {
                keyGeneratorSpiNextSpi.engineInit(algorithmParameterSpec, secureRandom);
                this.initType = 3;
                this.initKeySize = 0;
                this.initParams = algorithmParameterSpec;
                this.initRandom = secureRandom;
                return;
            } catch (Exception e2) {
                if (exc == null) {
                    exc = e2;
                }
                keyGeneratorSpiNextSpi = nextSpi(keyGeneratorSpiNextSpi, false);
            }
        } while (keyGeneratorSpiNextSpi != null);
        if (exc instanceof InvalidAlgorithmParameterException) {
            throw ((InvalidAlgorithmParameterException) exc);
        }
        if (exc instanceof RuntimeException) {
            throw ((RuntimeException) exc);
        }
        throw new InvalidAlgorithmParameterException("init() failed", exc);
    }

    public final void init(int i2) {
        init(i2, JceSecurity.RANDOM);
    }

    public final void init(int i2, SecureRandom secureRandom) {
        if (this.serviceIterator == null) {
            this.spi.engineInit(i2, secureRandom);
            return;
        }
        RuntimeException runtimeException = null;
        KeyGeneratorSpi keyGeneratorSpiNextSpi = this.spi;
        do {
            try {
                keyGeneratorSpiNextSpi.engineInit(i2, secureRandom);
                this.initType = 4;
                this.initKeySize = i2;
                this.initParams = null;
                this.initRandom = secureRandom;
                return;
            } catch (RuntimeException e2) {
                if (runtimeException == null) {
                    runtimeException = e2;
                }
                keyGeneratorSpiNextSpi = nextSpi(keyGeneratorSpiNextSpi, false);
            }
        } while (keyGeneratorSpiNextSpi != null);
        throw runtimeException;
    }

    public final SecretKey generateKey() {
        if (this.serviceIterator == null) {
            return this.spi.engineGenerateKey();
        }
        RuntimeException runtimeException = null;
        KeyGeneratorSpi keyGeneratorSpiNextSpi = this.spi;
        do {
            try {
                return keyGeneratorSpiNextSpi.engineGenerateKey();
            } catch (RuntimeException e2) {
                if (runtimeException == null) {
                    runtimeException = e2;
                }
                keyGeneratorSpiNextSpi = nextSpi(keyGeneratorSpiNextSpi, true);
            }
        } while (keyGeneratorSpiNextSpi != null);
        throw runtimeException;
    }
}
