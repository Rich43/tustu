package java.security;

import java.security.spec.AlgorithmParameterSpec;

/* loaded from: rt.jar:java/security/AlgorithmParameterGeneratorSpi.class */
public abstract class AlgorithmParameterGeneratorSpi {
    protected abstract void engineInit(int i2, SecureRandom secureRandom);

    protected abstract void engineInit(AlgorithmParameterSpec algorithmParameterSpec, SecureRandom secureRandom) throws InvalidAlgorithmParameterException;

    protected abstract AlgorithmParameters engineGenerateParameters();
}
