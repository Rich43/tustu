package java.security;

import java.security.Provider;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Iterator;
import sun.security.jca.GetInstance;
import sun.security.jca.JCAUtil;
import sun.security.util.Debug;

/* loaded from: rt.jar:java/security/KeyPairGenerator.class */
public abstract class KeyPairGenerator extends KeyPairGeneratorSpi {
    private static final Debug pdebug = Debug.getInstance("provider", "Provider");
    private static final boolean skipDebug;
    private final String algorithm;
    Provider provider;

    static {
        skipDebug = Debug.isOn("engine=") && !Debug.isOn("keypairgenerator");
    }

    protected KeyPairGenerator(String str) {
        this.algorithm = str;
    }

    public String getAlgorithm() {
        return this.algorithm;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v14, types: [java.security.KeyPairGenerator] */
    private static KeyPairGenerator getInstance(GetInstance.Instance instance, String str) {
        Delegate delegate;
        if (instance.impl instanceof KeyPairGenerator) {
            delegate = (KeyPairGenerator) instance.impl;
        } else {
            delegate = new Delegate((KeyPairGeneratorSpi) instance.impl, str);
        }
        delegate.provider = instance.provider;
        if (!skipDebug && pdebug != null) {
            pdebug.println("KeyPairGenerator." + str + " algorithm from: " + delegate.provider.getName());
        }
        return delegate;
    }

    public static KeyPairGenerator getInstance(String str) throws NoSuchAlgorithmException {
        Iterator<Provider.Service> it = GetInstance.getServices("KeyPairGenerator", str).iterator();
        if (!it.hasNext()) {
            throw new NoSuchAlgorithmException(str + " KeyPairGenerator not available");
        }
        NoSuchAlgorithmException noSuchAlgorithmException = null;
        do {
            try {
                GetInstance.Instance getInstance = GetInstance.getInstance(it.next(), KeyPairGeneratorSpi.class);
                if (getInstance.impl instanceof KeyPairGenerator) {
                    return getInstance(getInstance, str);
                }
                return new Delegate(getInstance, it, str);
            } catch (NoSuchAlgorithmException e2) {
                if (noSuchAlgorithmException == null) {
                    noSuchAlgorithmException = e2;
                }
            }
        } while (it.hasNext());
        throw noSuchAlgorithmException;
    }

    public static KeyPairGenerator getInstance(String str, String str2) throws NoSuchAlgorithmException, NoSuchProviderException {
        return getInstance(GetInstance.getInstance("KeyPairGenerator", (Class<?>) KeyPairGeneratorSpi.class, str, str2), str);
    }

    public static KeyPairGenerator getInstance(String str, Provider provider) throws NoSuchAlgorithmException {
        return getInstance(GetInstance.getInstance("KeyPairGenerator", (Class<?>) KeyPairGeneratorSpi.class, str, provider), str);
    }

    public final Provider getProvider() {
        disableFailover();
        return this.provider;
    }

    void disableFailover() {
    }

    public void initialize(int i2) {
        initialize(i2, JCAUtil.getSecureRandom());
    }

    @Override // java.security.KeyPairGeneratorSpi
    public void initialize(int i2, SecureRandom secureRandom) {
    }

    public void initialize(AlgorithmParameterSpec algorithmParameterSpec) throws InvalidAlgorithmParameterException {
        initialize(algorithmParameterSpec, JCAUtil.getSecureRandom());
    }

    @Override // java.security.KeyPairGeneratorSpi
    public void initialize(AlgorithmParameterSpec algorithmParameterSpec, SecureRandom secureRandom) throws InvalidAlgorithmParameterException {
    }

    public final KeyPair genKeyPair() {
        return generateKeyPair();
    }

    @Override // java.security.KeyPairGeneratorSpi
    public KeyPair generateKeyPair() {
        return null;
    }

    /* loaded from: rt.jar:java/security/KeyPairGenerator$Delegate.class */
    private static final class Delegate extends KeyPairGenerator {
        private volatile KeyPairGeneratorSpi spi;
        private final Object lock;
        private Iterator<Provider.Service> serviceIterator;
        private static final int I_NONE = 1;
        private static final int I_SIZE = 2;
        private static final int I_PARAMS = 3;
        private int initType;
        private int initKeySize;
        private AlgorithmParameterSpec initParams;
        private SecureRandom initRandom;

        Delegate(KeyPairGeneratorSpi keyPairGeneratorSpi, String str) {
            super(str);
            this.lock = new Object();
            this.spi = keyPairGeneratorSpi;
        }

        Delegate(GetInstance.Instance instance, Iterator<Provider.Service> it, String str) {
            super(str);
            this.lock = new Object();
            this.spi = (KeyPairGeneratorSpi) instance.impl;
            this.provider = instance.provider;
            this.serviceIterator = it;
            this.initType = 1;
            if (!KeyPairGenerator.skipDebug && KeyPairGenerator.pdebug != null) {
                KeyPairGenerator.pdebug.println("KeyPairGenerator." + str + " algorithm from: " + this.provider.getName());
            }
        }

        private KeyPairGeneratorSpi nextSpi(KeyPairGeneratorSpi keyPairGeneratorSpi, boolean z2) {
            synchronized (this.lock) {
                if (keyPairGeneratorSpi != null) {
                    if (keyPairGeneratorSpi != this.spi) {
                        return this.spi;
                    }
                }
                if (this.serviceIterator == null) {
                    return null;
                }
                while (this.serviceIterator.hasNext()) {
                    Provider.Service next = this.serviceIterator.next();
                    try {
                        Object objNewInstance = next.newInstance(null);
                        if ((objNewInstance instanceof KeyPairGeneratorSpi) && !(objNewInstance instanceof KeyPairGenerator)) {
                            KeyPairGeneratorSpi keyPairGeneratorSpi2 = (KeyPairGeneratorSpi) objNewInstance;
                            if (z2) {
                                if (this.initType == 2) {
                                    keyPairGeneratorSpi2.initialize(this.initKeySize, this.initRandom);
                                } else if (this.initType == 3) {
                                    keyPairGeneratorSpi2.initialize(this.initParams, this.initRandom);
                                } else if (this.initType != 1) {
                                    throw new AssertionError((Object) ("KeyPairGenerator initType: " + this.initType));
                                }
                            }
                            this.provider = next.getProvider();
                            this.spi = keyPairGeneratorSpi2;
                            return keyPairGeneratorSpi2;
                        }
                    } catch (Exception e2) {
                    }
                }
                disableFailover();
                return null;
            }
        }

        @Override // java.security.KeyPairGenerator
        void disableFailover() {
            this.serviceIterator = null;
            this.initType = 0;
            this.initParams = null;
            this.initRandom = null;
        }

        @Override // java.security.KeyPairGenerator, java.security.KeyPairGeneratorSpi
        public void initialize(int i2, SecureRandom secureRandom) {
            if (this.serviceIterator == null) {
                this.spi.initialize(i2, secureRandom);
                return;
            }
            RuntimeException runtimeException = null;
            KeyPairGeneratorSpi keyPairGeneratorSpiNextSpi = this.spi;
            do {
                try {
                    keyPairGeneratorSpiNextSpi.initialize(i2, secureRandom);
                    this.initType = 2;
                    this.initKeySize = i2;
                    this.initParams = null;
                    this.initRandom = secureRandom;
                    return;
                } catch (RuntimeException e2) {
                    if (runtimeException == null) {
                        runtimeException = e2;
                    }
                    keyPairGeneratorSpiNextSpi = nextSpi(keyPairGeneratorSpiNextSpi, false);
                }
            } while (keyPairGeneratorSpiNextSpi != null);
            throw runtimeException;
        }

        @Override // java.security.KeyPairGenerator, java.security.KeyPairGeneratorSpi
        public void initialize(AlgorithmParameterSpec algorithmParameterSpec, SecureRandom secureRandom) throws InvalidAlgorithmParameterException {
            if (this.serviceIterator == null) {
                this.spi.initialize(algorithmParameterSpec, secureRandom);
                return;
            }
            Exception exc = null;
            KeyPairGeneratorSpi keyPairGeneratorSpiNextSpi = this.spi;
            do {
                try {
                    keyPairGeneratorSpiNextSpi.initialize(algorithmParameterSpec, secureRandom);
                    this.initType = 3;
                    this.initKeySize = 0;
                    this.initParams = algorithmParameterSpec;
                    this.initRandom = secureRandom;
                    return;
                } catch (Exception e2) {
                    if (exc == null) {
                        exc = e2;
                    }
                    keyPairGeneratorSpiNextSpi = nextSpi(keyPairGeneratorSpiNextSpi, false);
                }
            } while (keyPairGeneratorSpiNextSpi != null);
            if (exc instanceof RuntimeException) {
                throw ((RuntimeException) exc);
            }
            throw ((InvalidAlgorithmParameterException) exc);
        }

        @Override // java.security.KeyPairGenerator, java.security.KeyPairGeneratorSpi
        public KeyPair generateKeyPair() {
            if (this.serviceIterator == null) {
                return this.spi.generateKeyPair();
            }
            RuntimeException runtimeException = null;
            KeyPairGeneratorSpi keyPairGeneratorSpiNextSpi = this.spi;
            do {
                try {
                    return keyPairGeneratorSpiNextSpi.generateKeyPair();
                } catch (RuntimeException e2) {
                    if (runtimeException == null) {
                        runtimeException = e2;
                    }
                    keyPairGeneratorSpiNextSpi = nextSpi(keyPairGeneratorSpiNextSpi, true);
                }
            } while (keyPairGeneratorSpiNextSpi != null);
            throw runtimeException;
        }
    }
}
