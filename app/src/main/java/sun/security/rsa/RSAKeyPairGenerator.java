package sun.security.rsa;

import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.InvalidParameterException;
import java.security.KeyPair;
import java.security.KeyPairGeneratorSpi;
import java.security.ProviderException;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.RSAKeyGenParameterSpec;
import sun.security.jca.JCAUtil;
import sun.security.rsa.RSAUtil;
import sun.security.util.SecurityProviderConstants;
import sun.security.x509.AlgorithmId;

/* loaded from: rt.jar:sun/security/rsa/RSAKeyPairGenerator.class */
public abstract class RSAKeyPairGenerator extends KeyPairGeneratorSpi {
    private BigInteger publicExponent;
    private int keySize;
    private final RSAUtil.KeyType type;
    private AlgorithmId rsaId;
    private SecureRandom random;

    RSAKeyPairGenerator(RSAUtil.KeyType keyType, int i2) {
        this.type = keyType;
        initialize(i2, (SecureRandom) null);
    }

    @Override // java.security.KeyPairGeneratorSpi
    public void initialize(int i2, SecureRandom secureRandom) {
        try {
            initialize(new RSAKeyGenParameterSpec(i2, RSAKeyGenParameterSpec.F4), secureRandom);
        } catch (InvalidAlgorithmParameterException e2) {
            throw new InvalidParameterException(e2.getMessage());
        }
    }

    @Override // java.security.KeyPairGeneratorSpi
    public void initialize(AlgorithmParameterSpec algorithmParameterSpec, SecureRandom secureRandom) throws InvalidAlgorithmParameterException {
        if (!(algorithmParameterSpec instanceof RSAKeyGenParameterSpec)) {
            throw new InvalidAlgorithmParameterException("Params must be instance of RSAKeyGenParameterSpec");
        }
        RSAKeyGenParameterSpec rSAKeyGenParameterSpec = (RSAKeyGenParameterSpec) algorithmParameterSpec;
        int keysize = rSAKeyGenParameterSpec.getKeysize();
        BigInteger publicExponent = rSAKeyGenParameterSpec.getPublicExponent();
        AlgorithmParameterSpec keyParams = rSAKeyGenParameterSpec.getKeyParams();
        if (publicExponent == null) {
            publicExponent = RSAKeyGenParameterSpec.F4;
        } else {
            if (publicExponent.compareTo(RSAKeyGenParameterSpec.F0) < 0) {
                throw new InvalidAlgorithmParameterException("Public exponent must be 3 or larger");
            }
            if (publicExponent.bitLength() > keysize) {
                throw new InvalidAlgorithmParameterException("Public exponent must be smaller than key size");
            }
        }
        try {
            RSAKeyFactory.checkKeyLengths(keysize, publicExponent, 512, 65536);
            try {
                this.rsaId = RSAUtil.createAlgorithmId(this.type, keyParams);
                this.keySize = keysize;
                this.publicExponent = publicExponent;
                this.random = secureRandom;
            } catch (ProviderException e2) {
                throw new InvalidAlgorithmParameterException("Invalid key parameters", e2);
            }
        } catch (InvalidKeyException e3) {
            throw new InvalidAlgorithmParameterException("Invalid key sizes", e3);
        }
    }

    @Override // java.security.KeyPairGeneratorSpi
    public KeyPair generateKeyPair() {
        BigInteger bigIntegerProbablePrime;
        BigInteger bigIntegerProbablePrime2;
        BigInteger bigIntegerMultiply;
        BigInteger bigIntegerSubtract;
        BigInteger bigIntegerSubtract2;
        BigInteger bigIntegerMultiply2;
        int i2 = (this.keySize + 1) >> 1;
        int i3 = this.keySize - i2;
        if (this.random == null) {
            this.random = JCAUtil.getSecureRandom();
        }
        BigInteger bigInteger = this.publicExponent;
        do {
            bigIntegerProbablePrime = BigInteger.probablePrime(i2, this.random);
            do {
                bigIntegerProbablePrime2 = BigInteger.probablePrime(i3, this.random);
                if (bigIntegerProbablePrime.compareTo(bigIntegerProbablePrime2) < 0) {
                    BigInteger bigInteger2 = bigIntegerProbablePrime;
                    bigIntegerProbablePrime = bigIntegerProbablePrime2;
                    bigIntegerProbablePrime2 = bigInteger2;
                }
                bigIntegerMultiply = bigIntegerProbablePrime.multiply(bigIntegerProbablePrime2);
            } while (bigIntegerMultiply.bitLength() < this.keySize);
            bigIntegerSubtract = bigIntegerProbablePrime.subtract(BigInteger.ONE);
            bigIntegerSubtract2 = bigIntegerProbablePrime2.subtract(BigInteger.ONE);
            bigIntegerMultiply2 = bigIntegerSubtract.multiply(bigIntegerSubtract2);
        } while (!bigInteger.gcd(bigIntegerMultiply2).equals(BigInteger.ONE));
        BigInteger bigIntegerModInverse = bigInteger.modInverse(bigIntegerMultiply2);
        try {
            return new KeyPair(new RSAPublicKeyImpl(this.rsaId, bigIntegerMultiply, bigInteger), new RSAPrivateCrtKeyImpl(this.rsaId, bigIntegerMultiply, bigInteger, bigIntegerModInverse, bigIntegerProbablePrime, bigIntegerProbablePrime2, bigIntegerModInverse.mod(bigIntegerSubtract), bigIntegerModInverse.mod(bigIntegerSubtract2), bigIntegerProbablePrime2.modInverse(bigIntegerProbablePrime)));
        } catch (InvalidKeyException e2) {
            throw new RuntimeException(e2);
        }
    }

    /* loaded from: rt.jar:sun/security/rsa/RSAKeyPairGenerator$Legacy.class */
    public static final class Legacy extends RSAKeyPairGenerator {
        public Legacy() {
            super(RSAUtil.KeyType.RSA, SecurityProviderConstants.DEF_RSA_KEY_SIZE);
        }
    }

    /* loaded from: rt.jar:sun/security/rsa/RSAKeyPairGenerator$PSS.class */
    public static final class PSS extends RSAKeyPairGenerator {
        public PSS() {
            super(RSAUtil.KeyType.PSS, SecurityProviderConstants.DEF_RSASSA_PSS_KEY_SIZE);
        }
    }
}
