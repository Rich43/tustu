package com.sun.crypto.provider;

import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidParameterException;
import java.security.KeyPair;
import java.security.KeyPairGeneratorSpi;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import javax.crypto.spec.DHParameterSpec;
import sun.security.provider.ParameterCache;
import sun.security.util.SecurityProviderConstants;

/* loaded from: sunjce_provider.jar:com/sun/crypto/provider/DHKeyPairGenerator.class */
public final class DHKeyPairGenerator extends KeyPairGeneratorSpi {
    private DHParameterSpec params;
    private int pSize;
    private SecureRandom random;

    public DHKeyPairGenerator() throws InvalidParameterException {
        initialize(SecurityProviderConstants.DEF_DH_KEY_SIZE, (SecureRandom) null);
    }

    static void checkKeySize(int i2, int i3) throws InvalidParameterException {
        if (i2 < 512 || i2 > 8192 || (i2 & 63) != 0) {
            throw new InvalidParameterException("DH key size must be multiple of 64, and can only range from 512 to 8192 (inclusive). The specific key size " + i2 + " is not supported");
        }
        if (i3 < 0 || i3 > i2) {
            throw new InvalidParameterException("Exponent size must be positive and no larger than modulus size");
        }
    }

    @Override // java.security.KeyPairGeneratorSpi
    public void initialize(int i2, SecureRandom secureRandom) throws InvalidParameterException {
        checkKeySize(i2, 0);
        try {
            this.params = ParameterCache.getDHParameterSpec(i2, secureRandom);
            this.pSize = i2;
            this.random = secureRandom;
        } catch (GeneralSecurityException e2) {
            throw new InvalidParameterException(e2.getMessage());
        }
    }

    @Override // java.security.KeyPairGeneratorSpi
    public void initialize(AlgorithmParameterSpec algorithmParameterSpec, SecureRandom secureRandom) throws InvalidAlgorithmParameterException {
        if (!(algorithmParameterSpec instanceof DHParameterSpec)) {
            throw new InvalidAlgorithmParameterException("Inappropriate parameter type");
        }
        this.params = (DHParameterSpec) algorithmParameterSpec;
        this.pSize = this.params.getP().bitLength();
        try {
            checkKeySize(this.pSize, this.params.getL());
            this.random = secureRandom;
        } catch (InvalidParameterException e2) {
            throw new InvalidAlgorithmParameterException(e2.getMessage());
        }
    }

    @Override // java.security.KeyPairGeneratorSpi
    public KeyPair generateKeyPair() {
        if (this.random == null) {
            this.random = SunJCE.getRandom();
        }
        BigInteger p2 = this.params.getP();
        BigInteger g2 = this.params.getG();
        int l2 = this.params.getL();
        if (l2 == 0) {
            l2 = SecurityProviderConstants.getDefDHPrivateExpSize(this.params);
        }
        BigInteger bigIntegerSubtract = p2.subtract(BigInteger.valueOf(2L));
        while (true) {
            BigInteger bigInteger = new BigInteger(l2, this.random);
            if (bigInteger.compareTo(BigInteger.ONE) >= 0 && bigInteger.compareTo(bigIntegerSubtract) <= 0 && bigInteger.bitLength() == l2) {
                return new KeyPair(new DHPublicKey(g2.modPow(bigInteger, p2), p2, g2, l2), new DHPrivateKey(bigInteger, p2, g2, l2));
            }
        }
    }
}
