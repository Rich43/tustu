package com.sun.crypto.provider;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidParameterException;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import javax.crypto.KeyGeneratorSpi;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/* loaded from: sunjce_provider.jar:com/sun/crypto/provider/BlowfishKeyGenerator.class */
public final class BlowfishKeyGenerator extends KeyGeneratorSpi {
    private SecureRandom random = null;
    private int keysize = 16;

    @Override // javax.crypto.KeyGeneratorSpi
    protected void engineInit(SecureRandom secureRandom) {
        this.random = secureRandom;
    }

    @Override // javax.crypto.KeyGeneratorSpi
    protected void engineInit(AlgorithmParameterSpec algorithmParameterSpec, SecureRandom secureRandom) throws InvalidAlgorithmParameterException {
        throw new InvalidAlgorithmParameterException("Blowfish key generation does not take any parameters");
    }

    @Override // javax.crypto.KeyGeneratorSpi
    protected void engineInit(int i2, SecureRandom secureRandom) {
        if (i2 % 8 != 0 || i2 < 32 || i2 > 448) {
            throw new InvalidParameterException("Keysize must be multiple of 8, and can only range from 32 to 448 (inclusive)");
        }
        this.keysize = i2 / 8;
        engineInit(secureRandom);
    }

    @Override // javax.crypto.KeyGeneratorSpi
    protected SecretKey engineGenerateKey() {
        if (this.random == null) {
            this.random = SunJCE.getRandom();
        }
        byte[] bArr = new byte[this.keysize];
        this.random.nextBytes(bArr);
        return new SecretKeySpec(bArr, "Blowfish");
    }
}
