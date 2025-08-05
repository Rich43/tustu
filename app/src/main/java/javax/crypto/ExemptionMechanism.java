package javax.crypto;

import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Provider;
import java.security.spec.AlgorithmParameterSpec;
import sun.security.jca.GetInstance;

/* loaded from: jce.jar:javax/crypto/ExemptionMechanism.class */
public class ExemptionMechanism {
    private Provider provider;
    private ExemptionMechanismSpi exmechSpi;
    private String mechanism;
    private boolean done = false;
    private boolean initialized = false;
    private Key keyStored = null;

    protected ExemptionMechanism(ExemptionMechanismSpi exemptionMechanismSpi, Provider provider, String str) {
        this.exmechSpi = exemptionMechanismSpi;
        this.provider = provider;
        this.mechanism = str;
    }

    public final String getName() {
        return this.mechanism;
    }

    public static final ExemptionMechanism getInstance(String str) throws NoSuchAlgorithmException {
        GetInstance.Instance jceSecurity = JceSecurity.getInstance("ExemptionMechanism", ExemptionMechanismSpi.class, str);
        return new ExemptionMechanism((ExemptionMechanismSpi) jceSecurity.impl, jceSecurity.provider, str);
    }

    public static final ExemptionMechanism getInstance(String str, String str2) throws NoSuchAlgorithmException, NoSuchProviderException {
        GetInstance.Instance jceSecurity = JceSecurity.getInstance("ExemptionMechanism", (Class<?>) ExemptionMechanismSpi.class, str, str2);
        return new ExemptionMechanism((ExemptionMechanismSpi) jceSecurity.impl, jceSecurity.provider, str);
    }

    public static final ExemptionMechanism getInstance(String str, Provider provider) throws NoSuchAlgorithmException {
        GetInstance.Instance jceSecurity = JceSecurity.getInstance("ExemptionMechanism", (Class<?>) ExemptionMechanismSpi.class, str, provider);
        return new ExemptionMechanism((ExemptionMechanismSpi) jceSecurity.impl, jceSecurity.provider, str);
    }

    public final Provider getProvider() {
        return this.provider;
    }

    public final boolean isCryptoAllowed(Key key) throws ExemptionMechanismException {
        boolean zEquals = false;
        if (this.done && key != null) {
            zEquals = this.keyStored.equals(key);
        }
        return zEquals;
    }

    public final int getOutputSize(int i2) throws IllegalStateException {
        if (!this.initialized) {
            throw new IllegalStateException("ExemptionMechanism not initialized");
        }
        if (i2 < 0) {
            throw new IllegalArgumentException("Input size must be equal to or greater than zero");
        }
        return this.exmechSpi.engineGetOutputSize(i2);
    }

    public final void init(Key key) throws ExemptionMechanismException, InvalidKeyException {
        this.done = false;
        this.initialized = false;
        this.keyStored = key;
        this.exmechSpi.engineInit(key);
        this.initialized = true;
    }

    public final void init(Key key, AlgorithmParameterSpec algorithmParameterSpec) throws ExemptionMechanismException, InvalidKeyException, InvalidAlgorithmParameterException {
        this.done = false;
        this.initialized = false;
        this.keyStored = key;
        this.exmechSpi.engineInit(key, algorithmParameterSpec);
        this.initialized = true;
    }

    public final void init(Key key, AlgorithmParameters algorithmParameters) throws ExemptionMechanismException, InvalidKeyException, InvalidAlgorithmParameterException {
        this.done = false;
        this.initialized = false;
        this.keyStored = key;
        this.exmechSpi.engineInit(key, algorithmParameters);
        this.initialized = true;
    }

    public final byte[] genExemptionBlob() throws IllegalStateException, ExemptionMechanismException {
        if (!this.initialized) {
            throw new IllegalStateException("ExemptionMechanism not initialized");
        }
        byte[] bArrEngineGenExemptionBlob = this.exmechSpi.engineGenExemptionBlob();
        this.done = true;
        return bArrEngineGenExemptionBlob;
    }

    public final int genExemptionBlob(byte[] bArr) throws IllegalStateException, ExemptionMechanismException, ShortBufferException {
        if (!this.initialized) {
            throw new IllegalStateException("ExemptionMechanism not initialized");
        }
        int iEngineGenExemptionBlob = this.exmechSpi.engineGenExemptionBlob(bArr, 0);
        this.done = true;
        return iEngineGenExemptionBlob;
    }

    public final int genExemptionBlob(byte[] bArr, int i2) throws IllegalStateException, ExemptionMechanismException, ShortBufferException {
        if (!this.initialized) {
            throw new IllegalStateException("ExemptionMechanism not initialized");
        }
        int iEngineGenExemptionBlob = this.exmechSpi.engineGenExemptionBlob(bArr, i2);
        this.done = true;
        return iEngineGenExemptionBlob;
    }

    protected void finalize() {
        this.keyStored = null;
    }
}
