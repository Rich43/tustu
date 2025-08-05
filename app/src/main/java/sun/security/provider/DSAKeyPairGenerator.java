package sun.security.provider;

import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.InvalidParameterException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.ProviderException;
import java.security.interfaces.DSAParams;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.DSAParameterSpec;
import sun.security.jca.JCAUtil;
import sun.security.util.SecurityProviderConstants;
import sun.security.x509.X509Key;

/* loaded from: rt.jar:sun/security/provider/DSAKeyPairGenerator.class */
class DSAKeyPairGenerator extends KeyPairGenerator {
    private int plen;
    private int qlen;
    boolean forceNewParameters;
    private DSAParameterSpec params;
    private java.security.SecureRandom random;

    DSAKeyPairGenerator(int i2) {
        super("DSA");
        initialize(i2, (java.security.SecureRandom) null);
    }

    private static void checkStrength(int i2, int i3) {
        if (i2 < 512 || i2 > 1024 || i2 % 64 != 0 || i3 != 160) {
            if (i2 != 2048 || (i3 != 224 && i3 != 256)) {
                if (i2 != 3072 || i3 != 256) {
                    throw new InvalidParameterException("Unsupported prime and subprime size combination: " + i2 + ", " + i3);
                }
            }
        }
    }

    @Override // java.security.KeyPairGenerator, java.security.KeyPairGeneratorSpi
    public void initialize(int i2, java.security.SecureRandom secureRandom) {
        init(i2, secureRandom, false);
    }

    @Override // java.security.KeyPairGenerator, java.security.KeyPairGeneratorSpi
    public void initialize(AlgorithmParameterSpec algorithmParameterSpec, java.security.SecureRandom secureRandom) throws InvalidAlgorithmParameterException {
        if (!(algorithmParameterSpec instanceof DSAParameterSpec)) {
            throw new InvalidAlgorithmParameterException("Inappropriate parameter");
        }
        init((DSAParameterSpec) algorithmParameterSpec, secureRandom, false);
    }

    void init(int i2, java.security.SecureRandom secureRandom, boolean z2) {
        int defDSASubprimeSize = SecurityProviderConstants.getDefDSASubprimeSize(i2);
        checkStrength(i2, defDSASubprimeSize);
        this.plen = i2;
        this.qlen = defDSASubprimeSize;
        this.params = null;
        this.random = secureRandom;
        this.forceNewParameters = z2;
    }

    void init(DSAParameterSpec dSAParameterSpec, java.security.SecureRandom secureRandom, boolean z2) {
        int iBitLength = dSAParameterSpec.getP().bitLength();
        int iBitLength2 = dSAParameterSpec.getQ().bitLength();
        checkStrength(iBitLength, iBitLength2);
        this.plen = iBitLength;
        this.qlen = iBitLength2;
        this.params = dSAParameterSpec;
        this.random = secureRandom;
        this.forceNewParameters = z2;
    }

    @Override // java.security.KeyPairGenerator, java.security.KeyPairGeneratorSpi
    public KeyPair generateKeyPair() {
        DSAParameterSpec newDSAParameterSpec;
        if (this.random == null) {
            this.random = JCAUtil.getSecureRandom();
        }
        try {
            if (this.forceNewParameters) {
                newDSAParameterSpec = ParameterCache.getNewDSAParameterSpec(this.plen, this.qlen, this.random);
            } else {
                if (this.params == null) {
                    this.params = ParameterCache.getDSAParameterSpec(this.plen, this.qlen, this.random);
                }
                newDSAParameterSpec = this.params;
            }
            return generateKeyPair(newDSAParameterSpec.getP(), newDSAParameterSpec.getQ(), newDSAParameterSpec.getG(), this.random);
        } catch (GeneralSecurityException e2) {
            throw new ProviderException(e2);
        }
    }

    private KeyPair generateKeyPair(BigInteger bigInteger, BigInteger bigInteger2, BigInteger bigInteger3, java.security.SecureRandom secureRandom) {
        X509Key dSAPublicKeyImpl;
        BigInteger bigIntegerGenerateX = generateX(secureRandom, bigInteger2);
        BigInteger bigIntegerGenerateY = generateY(bigIntegerGenerateX, bigInteger, bigInteger3);
        try {
            if (DSAKeyFactory.SERIAL_INTEROP) {
                dSAPublicKeyImpl = new DSAPublicKey(bigIntegerGenerateY, bigInteger, bigInteger2, bigInteger3);
            } else {
                dSAPublicKeyImpl = new DSAPublicKeyImpl(bigIntegerGenerateY, bigInteger, bigInteger2, bigInteger3);
            }
            return new KeyPair(dSAPublicKeyImpl, new DSAPrivateKey(bigIntegerGenerateX, bigInteger, bigInteger2, bigInteger3));
        } catch (InvalidKeyException e2) {
            throw new ProviderException(e2);
        }
    }

    private BigInteger generateX(java.security.SecureRandom secureRandom, BigInteger bigInteger) {
        byte[] bArr = new byte[this.qlen];
        while (true) {
            secureRandom.nextBytes(bArr);
            BigInteger bigIntegerMod = new BigInteger(1, bArr).mod(bigInteger);
            if (bigIntegerMod.signum() > 0 && bigIntegerMod.compareTo(bigInteger) < 0) {
                return bigIntegerMod;
            }
        }
    }

    BigInteger generateY(BigInteger bigInteger, BigInteger bigInteger2, BigInteger bigInteger3) {
        return bigInteger3.modPow(bigInteger, bigInteger2);
    }

    /* loaded from: rt.jar:sun/security/provider/DSAKeyPairGenerator$Current.class */
    public static final class Current extends DSAKeyPairGenerator {
        @Override // sun.security.provider.DSAKeyPairGenerator, java.security.KeyPairGenerator, java.security.KeyPairGeneratorSpi
        public /* bridge */ /* synthetic */ KeyPair generateKeyPair() {
            return super.generateKeyPair();
        }

        @Override // sun.security.provider.DSAKeyPairGenerator, java.security.KeyPairGenerator, java.security.KeyPairGeneratorSpi
        public /* bridge */ /* synthetic */ void initialize(AlgorithmParameterSpec algorithmParameterSpec, java.security.SecureRandom secureRandom) throws InvalidAlgorithmParameterException {
            super.initialize(algorithmParameterSpec, secureRandom);
        }

        @Override // sun.security.provider.DSAKeyPairGenerator, java.security.KeyPairGenerator, java.security.KeyPairGeneratorSpi
        public /* bridge */ /* synthetic */ void initialize(int i2, java.security.SecureRandom secureRandom) {
            super.initialize(i2, secureRandom);
        }

        public Current() {
            super(SecurityProviderConstants.DEF_DSA_KEY_SIZE);
        }
    }

    /* loaded from: rt.jar:sun/security/provider/DSAKeyPairGenerator$Legacy.class */
    public static final class Legacy extends DSAKeyPairGenerator implements java.security.interfaces.DSAKeyPairGenerator {
        @Override // sun.security.provider.DSAKeyPairGenerator, java.security.KeyPairGenerator, java.security.KeyPairGeneratorSpi
        public /* bridge */ /* synthetic */ KeyPair generateKeyPair() {
            return super.generateKeyPair();
        }

        @Override // sun.security.provider.DSAKeyPairGenerator, java.security.KeyPairGenerator, java.security.KeyPairGeneratorSpi
        public /* bridge */ /* synthetic */ void initialize(AlgorithmParameterSpec algorithmParameterSpec, java.security.SecureRandom secureRandom) throws InvalidAlgorithmParameterException {
            super.initialize(algorithmParameterSpec, secureRandom);
        }

        @Override // sun.security.provider.DSAKeyPairGenerator, java.security.KeyPairGenerator, java.security.KeyPairGeneratorSpi
        public /* bridge */ /* synthetic */ void initialize(int i2, java.security.SecureRandom secureRandom) {
            super.initialize(i2, secureRandom);
        }

        public Legacy() {
            super(1024);
        }

        @Override // java.security.interfaces.DSAKeyPairGenerator
        public void initialize(int i2, boolean z2, java.security.SecureRandom secureRandom) throws InvalidParameterException {
            if (z2) {
                super.init(i2, secureRandom, true);
                return;
            }
            DSAParameterSpec cachedDSAParameterSpec = ParameterCache.getCachedDSAParameterSpec(i2, SecurityProviderConstants.getDefDSASubprimeSize(i2));
            if (cachedDSAParameterSpec == null) {
                throw new InvalidParameterException("No precomputed parameters for requested modulus size available");
            }
            super.init(cachedDSAParameterSpec, secureRandom, false);
        }

        @Override // java.security.interfaces.DSAKeyPairGenerator
        public void initialize(DSAParams dSAParams, java.security.SecureRandom secureRandom) throws InvalidParameterException {
            if (dSAParams == null) {
                throw new InvalidParameterException("Params must not be null");
            }
            super.init(new DSAParameterSpec(dSAParams.getP(), dSAParams.getQ(), dSAParams.getG()), secureRandom, false);
        }
    }
}
