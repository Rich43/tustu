package com.sun.crypto.provider;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidParameterException;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import javax.crypto.KeyGeneratorSpi;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/* loaded from: sunjce_provider.jar:com/sun/crypto/provider/AESKeyGenerator.class */
public final class AESKeyGenerator extends KeyGeneratorSpi {
    private SecureRandom random = null;
    private int keySize = 16;

    @Override // javax.crypto.KeyGeneratorSpi
    protected void engineInit(SecureRandom secureRandom) {
        this.random = secureRandom;
    }

    @Override // javax.crypto.KeyGeneratorSpi
    protected void engineInit(AlgorithmParameterSpec algorithmParameterSpec, SecureRandom secureRandom) throws InvalidAlgorithmParameterException {
        throw new InvalidAlgorithmParameterException("AES key generation does not take any parameters");
    }

    @Override // javax.crypto.KeyGeneratorSpi
    protected void engineInit(int i2, SecureRandom secureRandom) {
        if (i2 % 8 != 0 || !AESCrypt.isKeySizeValid(i2 / 8)) {
            throw new InvalidParameterException("Wrong keysize: must be equal to 128, 192 or 256");
        }
        this.keySize = i2 / 8;
        engineInit(secureRandom);
    }

    @Override // javax.crypto.KeyGeneratorSpi
    protected SecretKey engineGenerateKey() {
        if (this.random == null) {
            this.random = SunJCE.getRandom();
        }
        byte[] bArr = new byte[this.keySize];
        this.random.nextBytes(bArr);
        return new SecretKeySpec(bArr, "AES");
    }
}
