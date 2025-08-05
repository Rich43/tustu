package sun.security.pkcs11;

import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.InvalidParameterException;
import java.security.KeyPair;
import java.security.KeyPairGeneratorSpi;
import java.security.ProviderException;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.DSAParameterSpec;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.ECParameterSpec;
import java.security.spec.RSAKeyGenParameterSpec;
import javax.crypto.spec.DHParameterSpec;
import sun.security.pkcs11.wrapper.CK_ATTRIBUTE;
import sun.security.pkcs11.wrapper.CK_MECHANISM;
import sun.security.pkcs11.wrapper.CK_MECHANISM_INFO;
import sun.security.pkcs11.wrapper.PKCS11Exception;
import sun.security.provider.ParameterCache;
import sun.security.rsa.RSAKeyFactory;
import sun.security.util.SecurityProviderConstants;

/* loaded from: sunpkcs11.jar:sun/security/pkcs11/P11KeyPairGenerator.class */
final class P11KeyPairGenerator extends KeyPairGeneratorSpi {
    private final Token token;
    private final String algorithm;
    private final long mechanism;
    private int keySize;
    private AlgorithmParameterSpec params;
    private BigInteger rsaPublicExponent = RSAKeyGenParameterSpec.F4;
    private final int minKeySize;
    private final int maxKeySize;
    private SecureRandom random;

    P11KeyPairGenerator(Token token, String str, long j2) throws PKCS11Exception {
        int i2 = 0;
        int i3 = Integer.MAX_VALUE;
        try {
            CK_MECHANISM_INFO mechanismInfo = token.getMechanismInfo(j2);
            if (mechanismInfo != null) {
                i2 = mechanismInfo.iMinKeySize;
                i3 = mechanismInfo.iMaxKeySize;
            }
            if (str.equals("EC")) {
                this.keySize = SecurityProviderConstants.DEF_EC_KEY_SIZE;
                i2 = i2 < 112 ? 112 : i2;
                if (i3 > 2048) {
                    i3 = 2048;
                }
            } else {
                if (str.equals("DSA")) {
                    this.keySize = SecurityProviderConstants.DEF_DSA_KEY_SIZE;
                } else if (str.equals("RSA")) {
                    this.keySize = SecurityProviderConstants.DEF_RSA_KEY_SIZE;
                    if (i3 > 65536) {
                        i3 = 65536;
                    }
                } else {
                    this.keySize = SecurityProviderConstants.DEF_DH_KEY_SIZE;
                }
                if (i2 < 512) {
                    i2 = 512;
                }
            }
            if (this.keySize < i2) {
                this.keySize = i2;
            }
            if (this.keySize > i3) {
                this.keySize = i3;
            }
            this.token = token;
            this.algorithm = str;
            this.mechanism = j2;
            this.minKeySize = i2;
            this.maxKeySize = i3;
            initialize(this.keySize, (SecureRandom) null);
        } catch (PKCS11Exception e2) {
            throw new ProviderException("Unexpected error while getting mechanism info", e2);
        }
    }

    @Override // java.security.KeyPairGeneratorSpi
    public void initialize(int i2, SecureRandom secureRandom) {
        this.token.ensureValid();
        try {
            checkKeySize(i2, null);
            this.params = null;
            if (this.algorithm.equals("EC")) {
                this.params = P11ECKeyFactory.getECParameterSpec(i2);
                if (this.params == null) {
                    throw new InvalidParameterException("No EC parameters available for key size " + i2 + " bits");
                }
            }
            this.keySize = i2;
            this.random = secureRandom;
        } catch (InvalidAlgorithmParameterException e2) {
            throw new InvalidParameterException(e2.getMessage());
        }
    }

    @Override // java.security.KeyPairGeneratorSpi
    public void initialize(AlgorithmParameterSpec algorithmParameterSpec, SecureRandom secureRandom) throws InvalidAlgorithmParameterException {
        ECParameterSpec eCParameterSpec;
        int fieldSize;
        this.token.ensureValid();
        if (this.algorithm.equals("DH")) {
            if (!(algorithmParameterSpec instanceof DHParameterSpec)) {
                throw new InvalidAlgorithmParameterException("DHParameterSpec required for Diffie-Hellman");
            }
            DHParameterSpec dHParameterSpec = (DHParameterSpec) algorithmParameterSpec;
            fieldSize = dHParameterSpec.getP().bitLength();
            checkKeySize(fieldSize, dHParameterSpec);
        } else if (this.algorithm.equals("RSA")) {
            if (!(algorithmParameterSpec instanceof RSAKeyGenParameterSpec)) {
                throw new InvalidAlgorithmParameterException("RSAKeyGenParameterSpec required for RSA");
            }
            RSAKeyGenParameterSpec rSAKeyGenParameterSpec = (RSAKeyGenParameterSpec) algorithmParameterSpec;
            fieldSize = rSAKeyGenParameterSpec.getKeysize();
            checkKeySize(fieldSize, rSAKeyGenParameterSpec);
            algorithmParameterSpec = null;
            this.rsaPublicExponent = rSAKeyGenParameterSpec.getPublicExponent();
        } else if (this.algorithm.equals("DSA")) {
            if (!(algorithmParameterSpec instanceof DSAParameterSpec)) {
                throw new InvalidAlgorithmParameterException("DSAParameterSpec required for DSA");
            }
            DSAParameterSpec dSAParameterSpec = (DSAParameterSpec) algorithmParameterSpec;
            fieldSize = dSAParameterSpec.getP().bitLength();
            checkKeySize(fieldSize, dSAParameterSpec);
        } else if (this.algorithm.equals("EC")) {
            if (algorithmParameterSpec instanceof ECParameterSpec) {
                eCParameterSpec = P11ECKeyFactory.getECParameterSpec((ECParameterSpec) algorithmParameterSpec);
                if (eCParameterSpec == null) {
                    throw new InvalidAlgorithmParameterException("Unsupported curve: " + ((Object) algorithmParameterSpec));
                }
            } else if (algorithmParameterSpec instanceof ECGenParameterSpec) {
                String name = ((ECGenParameterSpec) algorithmParameterSpec).getName();
                eCParameterSpec = P11ECKeyFactory.getECParameterSpec(name);
                if (eCParameterSpec == null) {
                    throw new InvalidAlgorithmParameterException("Unknown curve name: " + name);
                }
                algorithmParameterSpec = eCParameterSpec;
            } else {
                throw new InvalidAlgorithmParameterException("ECParameterSpec or ECGenParameterSpec required for EC");
            }
            fieldSize = eCParameterSpec.getCurve().getField().getFieldSize();
            checkKeySize(fieldSize, eCParameterSpec);
        } else {
            throw new ProviderException("Unknown algorithm: " + this.algorithm);
        }
        this.keySize = fieldSize;
        this.params = algorithmParameterSpec;
        this.random = secureRandom;
    }

    private void checkKeySize(int i2, AlgorithmParameterSpec algorithmParameterSpec) throws InvalidAlgorithmParameterException {
        if (i2 <= 0) {
            throw new InvalidAlgorithmParameterException("key size must be positive, got " + i2);
        }
        if (i2 < this.minKeySize) {
            throw new InvalidAlgorithmParameterException(this.algorithm + " key must be at least " + this.minKeySize + " bits. The specific key size " + i2 + " is not supported");
        }
        if (i2 > this.maxKeySize) {
            throw new InvalidAlgorithmParameterException(this.algorithm + " key must be at most " + this.maxKeySize + " bits. The specific key size " + i2 + " is not supported");
        }
        if (this.algorithm.equals("EC")) {
            if (i2 < 112) {
                throw new InvalidAlgorithmParameterException("EC key size must be at least 112 bit. The specific key size " + i2 + " is not supported");
            }
            if (i2 > 2048) {
                throw new InvalidAlgorithmParameterException("EC key size must be at most 2048 bit. The specific key size " + i2 + " is not supported");
            }
            return;
        }
        if (i2 < 512) {
            throw new InvalidAlgorithmParameterException(this.algorithm + " key size must be at least 512 bit. The specific key size " + i2 + " is not supported");
        }
        if (this.algorithm.equals("RSA")) {
            BigInteger publicExponent = this.rsaPublicExponent;
            if (algorithmParameterSpec != null) {
                publicExponent = ((RSAKeyGenParameterSpec) algorithmParameterSpec).getPublicExponent();
            }
            try {
                RSAKeyFactory.checkKeyLengths(i2, publicExponent, this.minKeySize, this.maxKeySize);
                return;
            } catch (InvalidKeyException e2) {
                throw new InvalidAlgorithmParameterException(e2);
            }
        }
        if (this.algorithm.equals("DH")) {
            if (algorithmParameterSpec != null) {
                if (i2 > 65536) {
                    throw new InvalidAlgorithmParameterException("DH key size must be at most 65536 bit. The specific key size " + i2 + " is not supported");
                }
                return;
            } else {
                if (i2 > 8192 || i2 < 512 || (i2 & 63) != 0) {
                    throw new InvalidAlgorithmParameterException("DH key size must be multiple of 64, and can only range from 512 to 8192 (inclusive). The specific key size " + i2 + " is not supported");
                }
                if (ParameterCache.getCachedDHParameterSpec(i2) == null && i2 > 1024) {
                    throw new InvalidAlgorithmParameterException("Unsupported " + i2 + "-bit DH parameter generation");
                }
                return;
            }
        }
        if (i2 == 3072 || i2 == 2048) {
            return;
        }
        if (i2 > 1024 || (i2 & 63) != 0) {
            throw new InvalidAlgorithmParameterException("DSA key must be multiples of 64 if less than 1024 bits, or 2048, 3072 bits. The specific key size " + i2 + " is not supported");
        }
    }

    @Override // java.security.KeyPairGeneratorSpi
    public KeyPair generateKeyPair() {
        long j2;
        CK_ATTRIBUTE[] ck_attributeArr;
        CK_ATTRIBUTE[] ck_attributeArr2;
        DHParameterSpec dHParameterSpec;
        int l2;
        DSAParameterSpec dSAParameterSpec;
        this.token.ensureValid();
        if (this.algorithm.equals("RSA")) {
            j2 = 0;
            ck_attributeArr = new CK_ATTRIBUTE[]{new CK_ATTRIBUTE(289L, this.keySize), new CK_ATTRIBUTE(290L, this.rsaPublicExponent)};
            ck_attributeArr2 = new CK_ATTRIBUTE[0];
        } else if (this.algorithm.equals("DSA")) {
            j2 = 1;
            if (this.params == null) {
                try {
                    dSAParameterSpec = ParameterCache.getDSAParameterSpec(this.keySize, this.random);
                } catch (GeneralSecurityException e2) {
                    throw new ProviderException("Could not generate DSA parameters", e2);
                }
            } else {
                dSAParameterSpec = (DSAParameterSpec) this.params;
            }
            ck_attributeArr = new CK_ATTRIBUTE[]{new CK_ATTRIBUTE(304L, dSAParameterSpec.getP()), new CK_ATTRIBUTE(305L, dSAParameterSpec.getQ()), new CK_ATTRIBUTE(306L, dSAParameterSpec.getG())};
            ck_attributeArr2 = new CK_ATTRIBUTE[0];
        } else if (this.algorithm.equals("DH")) {
            j2 = 2;
            if (this.params == null) {
                try {
                    dHParameterSpec = ParameterCache.getDHParameterSpec(this.keySize, this.random);
                    l2 = 0;
                } catch (GeneralSecurityException e3) {
                    throw new ProviderException("Could not generate DH parameters", e3);
                }
            } else {
                dHParameterSpec = (DHParameterSpec) this.params;
                l2 = dHParameterSpec.getL();
            }
            if (l2 <= 0) {
                l2 = this.keySize >= 1024 ? 768 : 512;
            }
            ck_attributeArr = new CK_ATTRIBUTE[]{new CK_ATTRIBUTE(304L, dHParameterSpec.getP()), new CK_ATTRIBUTE(306L, dHParameterSpec.getG())};
            ck_attributeArr2 = new CK_ATTRIBUTE[]{new CK_ATTRIBUTE(352L, l2)};
        } else if (this.algorithm.equals("EC")) {
            j2 = 3;
            ck_attributeArr = new CK_ATTRIBUTE[]{new CK_ATTRIBUTE(384L, P11ECKeyFactory.encodeParameters((ECParameterSpec) this.params))};
            ck_attributeArr2 = new CK_ATTRIBUTE[0];
        } else {
            throw new ProviderException("Unknown algorithm: " + this.algorithm);
        }
        Session objSession = null;
        try {
            try {
                objSession = this.token.getObjSession();
                CK_ATTRIBUTE[] attributes = this.token.getAttributes("generate", 2L, j2, ck_attributeArr);
                CK_ATTRIBUTE[] attributes2 = this.token.getAttributes("generate", 3L, j2, ck_attributeArr2);
                long[] jArrC_GenerateKeyPair = this.token.p11.C_GenerateKeyPair(objSession.id(), new CK_MECHANISM(this.mechanism), attributes, attributes2);
                KeyPair keyPair = new KeyPair(P11Key.publicKey(objSession, jArrC_GenerateKeyPair[0], this.algorithm, this.keySize, attributes), P11Key.privateKey(objSession, jArrC_GenerateKeyPair[1], this.algorithm, this.keySize, attributes2));
                this.token.releaseSession(objSession);
                return keyPair;
            } catch (PKCS11Exception e4) {
                throw new ProviderException(e4);
            }
        } catch (Throwable th) {
            this.token.releaseSession(objSession);
            throw th;
        }
    }
}
