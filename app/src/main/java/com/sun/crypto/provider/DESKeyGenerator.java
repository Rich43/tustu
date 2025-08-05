package com.sun.crypto.provider;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.InvalidParameterException;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import javax.crypto.KeyGeneratorSpi;
import javax.crypto.SecretKey;
import javax.crypto.spec.DESKeySpec;

/* loaded from: sunjce_provider.jar:com/sun/crypto/provider/DESKeyGenerator.class */
public final class DESKeyGenerator extends KeyGeneratorSpi {
    private SecureRandom random = null;

    @Override // javax.crypto.KeyGeneratorSpi
    protected void engineInit(SecureRandom secureRandom) {
        this.random = secureRandom;
    }

    @Override // javax.crypto.KeyGeneratorSpi
    protected void engineInit(AlgorithmParameterSpec algorithmParameterSpec, SecureRandom secureRandom) throws InvalidAlgorithmParameterException {
        throw new InvalidAlgorithmParameterException("DES key generation does not take any parameters");
    }

    @Override // javax.crypto.KeyGeneratorSpi
    protected void engineInit(int i2, SecureRandom secureRandom) {
        if (i2 != 56) {
            throw new InvalidParameterException("Wrong keysize: must be equal to 56");
        }
        engineInit(secureRandom);
    }

    @Override // javax.crypto.KeyGeneratorSpi
    protected SecretKey engineGenerateKey() {
        DESKey dESKey = null;
        if (this.random == null) {
            this.random = SunJCE.getRandom();
        }
        try {
            byte[] bArr = new byte[8];
            do {
                this.random.nextBytes(bArr);
                setParityBit(bArr, 0);
            } while (DESKeySpec.isWeak(bArr, 0));
            dESKey = new DESKey(bArr);
        } catch (InvalidKeyException e2) {
        }
        return dESKey;
    }

    static void setParityBit(byte[] bArr, int i2) {
        if (bArr == null) {
            return;
        }
        for (int i3 = 0; i3 < 8; i3++) {
            int i4 = bArr[i2] & 254;
            int i5 = i2;
            i2++;
            bArr[i5] = (byte) (i4 | ((Integer.bitCount(i4) & 1) ^ 1));
        }
    }
}
