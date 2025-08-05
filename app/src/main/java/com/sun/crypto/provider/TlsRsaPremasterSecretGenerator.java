package com.sun.crypto.provider;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidParameterException;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import javax.crypto.KeyGeneratorSpi;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import sun.security.internal.spec.TlsRsaPremasterSecretParameterSpec;

/* loaded from: sunjce_provider.jar:com/sun/crypto/provider/TlsRsaPremasterSecretGenerator.class */
public final class TlsRsaPremasterSecretGenerator extends KeyGeneratorSpi {
    private static final String MSG = "TlsRsaPremasterSecretGenerator must be initialized using a TlsRsaPremasterSecretParameterSpec";
    private TlsRsaPremasterSecretParameterSpec spec;
    private SecureRandom random;

    @Override // javax.crypto.KeyGeneratorSpi
    protected void engineInit(SecureRandom secureRandom) {
        throw new InvalidParameterException(MSG);
    }

    @Override // javax.crypto.KeyGeneratorSpi
    protected void engineInit(AlgorithmParameterSpec algorithmParameterSpec, SecureRandom secureRandom) throws InvalidAlgorithmParameterException {
        if (!(algorithmParameterSpec instanceof TlsRsaPremasterSecretParameterSpec)) {
            throw new InvalidAlgorithmParameterException(MSG);
        }
        this.spec = (TlsRsaPremasterSecretParameterSpec) algorithmParameterSpec;
        this.random = secureRandom;
    }

    @Override // javax.crypto.KeyGeneratorSpi
    protected void engineInit(int i2, SecureRandom secureRandom) {
        throw new InvalidParameterException(MSG);
    }

    @Override // javax.crypto.KeyGeneratorSpi
    protected SecretKey engineGenerateKey() {
        if (this.spec == null) {
            throw new IllegalStateException("TlsRsaPremasterSecretGenerator must be initialized");
        }
        byte[] encodedSecret = this.spec.getEncodedSecret();
        if (encodedSecret == null) {
            if (this.random == null) {
                this.random = new SecureRandom();
            }
            encodedSecret = new byte[48];
            this.random.nextBytes(encodedSecret);
        }
        encodedSecret[0] = (byte) this.spec.getMajorVersion();
        encodedSecret[1] = (byte) this.spec.getMinorVersion();
        return new SecretKeySpec(encodedSecret, "TlsRsaPremasterSecret");
    }
}
