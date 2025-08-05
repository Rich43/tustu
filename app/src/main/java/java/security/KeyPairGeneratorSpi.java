package java.security;

import java.security.spec.AlgorithmParameterSpec;

/* loaded from: rt.jar:java/security/KeyPairGeneratorSpi.class */
public abstract class KeyPairGeneratorSpi {
    public abstract void initialize(int i2, SecureRandom secureRandom);

    public abstract KeyPair generateKeyPair();

    public void initialize(AlgorithmParameterSpec algorithmParameterSpec, SecureRandom secureRandom) throws InvalidAlgorithmParameterException {
        throw new UnsupportedOperationException();
    }
}
