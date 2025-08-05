package com.sun.crypto.provider;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.InvalidParameterException;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Arrays;
import javax.crypto.KeyGeneratorSpi;
import javax.crypto.SecretKey;

/* loaded from: sunjce_provider.jar:com/sun/crypto/provider/DESedeKeyGenerator.class */
public final class DESedeKeyGenerator extends KeyGeneratorSpi {
    private SecureRandom random = null;
    private int keysize = 168;

    @Override // javax.crypto.KeyGeneratorSpi
    protected void engineInit(SecureRandom secureRandom) {
        this.random = secureRandom;
    }

    @Override // javax.crypto.KeyGeneratorSpi
    protected void engineInit(AlgorithmParameterSpec algorithmParameterSpec, SecureRandom secureRandom) throws InvalidAlgorithmParameterException {
        throw new InvalidAlgorithmParameterException("Triple DES key generation does not take any parameters");
    }

    @Override // javax.crypto.KeyGeneratorSpi
    protected void engineInit(int i2, SecureRandom secureRandom) {
        if (i2 != 112 && i2 != 168) {
            throw new InvalidParameterException("Wrong keysize: must be equal to 112 or 168");
        }
        this.keysize = i2;
        engineInit(secureRandom);
    }

    @Override // javax.crypto.KeyGeneratorSpi
    protected SecretKey engineGenerateKey() {
        if (this.random == null) {
            this.random = SunJCE.getRandom();
        }
        byte[] bArr = new byte[24];
        if (this.keysize == 168) {
            this.random.nextBytes(bArr);
            DESKeyGenerator.setParityBit(bArr, 0);
            DESKeyGenerator.setParityBit(bArr, 8);
            DESKeyGenerator.setParityBit(bArr, 16);
        } else {
            byte[] bArr2 = new byte[16];
            this.random.nextBytes(bArr2);
            DESKeyGenerator.setParityBit(bArr2, 0);
            DESKeyGenerator.setParityBit(bArr2, 8);
            System.arraycopy(bArr2, 0, bArr, 0, bArr2.length);
            System.arraycopy(bArr2, 0, bArr, 16, 8);
            Arrays.fill(bArr2, (byte) 0);
        }
        try {
            DESedeKey dESedeKey = new DESedeKey(bArr);
            Arrays.fill(bArr, (byte) 0);
            return dESedeKey;
        } catch (InvalidKeyException e2) {
            throw new RuntimeException(e2.getMessage());
        }
    }
}
