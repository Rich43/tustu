package javax.crypto;

import java.security.InvalidAlgorithmParameterException;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;

/* loaded from: jce.jar:javax/crypto/KeyGeneratorSpi.class */
public abstract class KeyGeneratorSpi {
    protected abstract void engineInit(SecureRandom secureRandom);

    protected abstract void engineInit(AlgorithmParameterSpec algorithmParameterSpec, SecureRandom secureRandom) throws InvalidAlgorithmParameterException;

    protected abstract void engineInit(int i2, SecureRandom secureRandom);

    protected abstract SecretKey engineGenerateKey();
}
