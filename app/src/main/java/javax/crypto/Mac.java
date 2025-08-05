package javax.crypto;

import com.intel.bluetooth.BlueCoveImpl;
import com.sun.glass.ui.Platform;
import java.nio.ByteBuffer;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Provider;
import java.security.ProviderException;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Iterator;
import sun.security.jca.GetInstance;
import sun.security.util.Debug;

/* loaded from: jce.jar:javax/crypto/Mac.class */
public class Mac implements Cloneable {
    private static final Debug debug = Debug.getInstance("jca", Platform.MAC);
    private static final Debug pdebug = Debug.getInstance("provider", "Provider");
    private static final boolean skipDebug;
    private Provider provider;
    private MacSpi spi;
    private final String algorithm;
    private boolean initialized;
    private Provider.Service firstService;
    private Iterator<Provider.Service> serviceIterator;
    private final Object lock;
    private static int warnCount;

    static {
        skipDebug = Debug.isOn("engine=") && !Debug.isOn(BlueCoveImpl.STACK_OSX);
        warnCount = 10;
    }

    protected Mac(MacSpi macSpi, Provider provider, String str) {
        this.initialized = false;
        this.spi = macSpi;
        this.provider = provider;
        this.algorithm = str;
        this.serviceIterator = null;
        this.lock = null;
    }

    private Mac(Provider.Service service, Iterator<Provider.Service> it, String str) {
        this.initialized = false;
        this.firstService = service;
        this.serviceIterator = it;
        this.algorithm = str;
        this.lock = new Object();
    }

    public final String getAlgorithm() {
        return this.algorithm;
    }

    public static final Mac getInstance(String str) throws NoSuchAlgorithmException {
        Iterator<Provider.Service> it = GetInstance.getServices(Platform.MAC, str).iterator();
        while (it.hasNext()) {
            Provider.Service next = it.next();
            if (JceSecurity.canUseProvider(next.getProvider())) {
                return new Mac(next, it, str);
            }
        }
        throw new NoSuchAlgorithmException("Algorithm " + str + " not available");
    }

    public static final Mac getInstance(String str, String str2) throws NoSuchAlgorithmException, NoSuchProviderException {
        GetInstance.Instance jceSecurity = JceSecurity.getInstance(Platform.MAC, (Class<?>) MacSpi.class, str, str2);
        return new Mac((MacSpi) jceSecurity.impl, jceSecurity.provider, str);
    }

    public static final Mac getInstance(String str, Provider provider) throws NoSuchAlgorithmException {
        GetInstance.Instance jceSecurity = JceSecurity.getInstance(Platform.MAC, (Class<?>) MacSpi.class, str, provider);
        return new Mac((MacSpi) jceSecurity.impl, jceSecurity.provider, str);
    }

    void chooseFirstProvider() {
        Provider.Service next;
        if (this.spi != null || this.serviceIterator == null) {
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
                    debug.println("Mac.init() not first method called, disabling delayed provider selection");
                    if (i2 == 0) {
                        debug.println("Further warnings of this type will be suppressed");
                    }
                    new Exception("Call trace").printStackTrace();
                }
            }
            NoSuchAlgorithmException noSuchAlgorithmException = null;
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
                            if (objNewInstance instanceof MacSpi) {
                                this.spi = (MacSpi) objNewInstance;
                                this.provider = next.getProvider();
                                this.firstService = null;
                                this.serviceIterator = null;
                                return;
                            }
                        } catch (NoSuchAlgorithmException e2) {
                            noSuchAlgorithmException = e2;
                        }
                    }
                } else {
                    ProviderException providerException = new ProviderException("Could not construct MacSpi instance");
                    if (noSuchAlgorithmException != null) {
                        providerException.initCause(noSuchAlgorithmException);
                    }
                    throw providerException;
                }
            }
        }
    }

    private void chooseProvider(Key key, AlgorithmParameterSpec algorithmParameterSpec) throws InvalidKeyException, InvalidAlgorithmParameterException {
        Provider.Service next;
        synchronized (this.lock) {
            if (this.spi != null) {
                this.spi.engineInit(key, algorithmParameterSpec);
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
                            MacSpi macSpi = (MacSpi) next.newInstance(null);
                            macSpi.engineInit(key, algorithmParameterSpec);
                            this.provider = next.getProvider();
                            this.spi = macSpi;
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

    public final int getMacLength() {
        chooseFirstProvider();
        return this.spi.engineGetMacLength();
    }

    public final void init(Key key) throws InvalidKeyException {
        try {
            if (this.spi != null) {
                this.spi.engineInit(key, null);
            } else {
                chooseProvider(key, null);
            }
            this.initialized = true;
            if (!skipDebug && pdebug != null) {
                pdebug.println("Mac." + this.algorithm + " algorithm from: " + this.provider.getName());
            }
        } catch (InvalidAlgorithmParameterException e2) {
            throw new InvalidKeyException("init() failed", e2);
        }
    }

    public final void init(Key key, AlgorithmParameterSpec algorithmParameterSpec) throws InvalidKeyException, InvalidAlgorithmParameterException {
        if (this.spi != null) {
            this.spi.engineInit(key, algorithmParameterSpec);
        } else {
            chooseProvider(key, algorithmParameterSpec);
        }
        this.initialized = true;
        if (!skipDebug && pdebug != null) {
            pdebug.println("Mac." + this.algorithm + " algorithm from: " + this.provider.getName());
        }
    }

    public final void update(byte b2) throws IllegalStateException {
        chooseFirstProvider();
        if (!this.initialized) {
            throw new IllegalStateException("MAC not initialized");
        }
        this.spi.engineUpdate(b2);
    }

    public final void update(byte[] bArr) throws IllegalStateException {
        chooseFirstProvider();
        if (!this.initialized) {
            throw new IllegalStateException("MAC not initialized");
        }
        if (bArr != null) {
            this.spi.engineUpdate(bArr, 0, bArr.length);
        }
    }

    public final void update(byte[] bArr, int i2, int i3) throws IllegalStateException {
        chooseFirstProvider();
        if (!this.initialized) {
            throw new IllegalStateException("MAC not initialized");
        }
        if (bArr != null) {
            if (i2 < 0 || i3 > bArr.length - i2 || i3 < 0) {
                throw new IllegalArgumentException("Bad arguments");
            }
            this.spi.engineUpdate(bArr, i2, i3);
        }
    }

    public final void update(ByteBuffer byteBuffer) {
        chooseFirstProvider();
        if (!this.initialized) {
            throw new IllegalStateException("MAC not initialized");
        }
        if (byteBuffer == null) {
            throw new IllegalArgumentException("Buffer must not be null");
        }
        this.spi.engineUpdate(byteBuffer);
    }

    public final byte[] doFinal() throws IllegalStateException {
        chooseFirstProvider();
        if (!this.initialized) {
            throw new IllegalStateException("MAC not initialized");
        }
        byte[] bArrEngineDoFinal = this.spi.engineDoFinal();
        this.spi.engineReset();
        return bArrEngineDoFinal;
    }

    public final void doFinal(byte[] bArr, int i2) throws IllegalStateException, ShortBufferException {
        chooseFirstProvider();
        if (!this.initialized) {
            throw new IllegalStateException("MAC not initialized");
        }
        int macLength = getMacLength();
        if (bArr == null || bArr.length - i2 < macLength) {
            throw new ShortBufferException("Cannot store MAC in output buffer");
        }
        System.arraycopy(doFinal(), 0, bArr, i2, macLength);
    }

    public final byte[] doFinal(byte[] bArr) throws IllegalStateException {
        chooseFirstProvider();
        if (!this.initialized) {
            throw new IllegalStateException("MAC not initialized");
        }
        update(bArr);
        return doFinal();
    }

    public final void reset() {
        chooseFirstProvider();
        this.spi.engineReset();
    }

    public final Object clone() throws CloneNotSupportedException {
        chooseFirstProvider();
        Mac mac = (Mac) super.clone();
        mac.spi = (MacSpi) this.spi.clone();
        return mac;
    }
}
