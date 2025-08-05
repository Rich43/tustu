package sun.security.rsa;

import com.sun.org.apache.xml.internal.security.keys.content.x509.XMLX509Certificate;
import java.math.BigInteger;
import java.security.AccessController;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactorySpi;
import java.security.PrivateKey;
import java.security.ProviderException;
import java.security.PublicKey;
import java.security.interfaces.RSAKey;
import java.security.interfaces.RSAPrivateCrtKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPrivateCrtKeySpec;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;
import sun.security.action.GetPropertyAction;
import sun.security.rsa.RSAUtil;

/* loaded from: rt.jar:sun/security/rsa/RSAKeyFactory.class */
public class RSAKeyFactory extends KeyFactorySpi {
    public static final int MIN_MODLEN = 512;
    public static final int MAX_MODLEN = 16384;
    private final RSAUtil.KeyType type;
    public static final int MAX_MODLEN_RESTRICT_EXP = 3072;
    public static final int MAX_RESTRICTED_EXPLEN = 64;
    private static final Class<?> RSA_PUB_KEYSPEC_CLS = RSAPublicKeySpec.class;
    private static final Class<?> RSA_PRIV_KEYSPEC_CLS = RSAPrivateKeySpec.class;
    private static final Class<?> RSA_PRIVCRT_KEYSPEC_CLS = RSAPrivateCrtKeySpec.class;
    private static final Class<?> X509_KEYSPEC_CLS = X509EncodedKeySpec.class;
    private static final Class<?> PKCS8_KEYSPEC_CLS = PKCS8EncodedKeySpec.class;
    private static final boolean restrictExpLen = "true".equalsIgnoreCase((String) AccessController.doPrivileged(new GetPropertyAction("sun.security.rsa.restrictRSAExponent", "true")));

    static RSAKeyFactory getInstance(RSAUtil.KeyType keyType) {
        return new RSAKeyFactory(keyType);
    }

    private static void checkKeyAlgo(Key key, String str) throws InvalidKeyException {
        String algorithm = key.getAlgorithm();
        if (algorithm == null || !algorithm.equalsIgnoreCase(str)) {
            throw new InvalidKeyException("Expected a " + str + " key, but got " + algorithm);
        }
    }

    public static RSAKey toRSAKey(Key key) throws InvalidKeyException {
        if (key == null) {
            throw new InvalidKeyException("Key must not be null");
        }
        if ((key instanceof RSAPrivateKeyImpl) || (key instanceof RSAPrivateCrtKeyImpl) || (key instanceof RSAPublicKeyImpl)) {
            return (RSAKey) key;
        }
        try {
            return (RSAKey) getInstance(RSAUtil.KeyType.lookup(key.getAlgorithm())).engineTranslateKey(key);
        } catch (ProviderException e2) {
            throw new InvalidKeyException(e2);
        }
    }

    static void checkRSAProviderKeyLengths(int i2, BigInteger bigInteger) throws InvalidKeyException {
        checkKeyLengths((i2 + 7) & (-8), bigInteger, 512, Integer.MAX_VALUE);
    }

    public static void checkKeyLengths(int i2, BigInteger bigInteger, int i3, int i4) throws InvalidKeyException {
        if (i3 > 0 && i2 < i3) {
            throw new InvalidKeyException("RSA keys must be at least " + i3 + " bits long");
        }
        int iMin = Math.min(i4, 16384);
        if (i2 > iMin) {
            throw new InvalidKeyException("RSA keys must be no longer than " + iMin + " bits");
        }
        if (restrictExpLen && bigInteger != null && i2 > 3072 && bigInteger.bitLength() > 64) {
            throw new InvalidKeyException("RSA exponents can be no longer than 64 bits  if modulus is greater than 3072 bits");
        }
    }

    private RSAKeyFactory() {
        this.type = RSAUtil.KeyType.RSA;
    }

    public RSAKeyFactory(RSAUtil.KeyType keyType) {
        this.type = keyType;
    }

    @Override // java.security.KeyFactorySpi
    protected Key engineTranslateKey(Key key) throws InvalidKeyException {
        if (key == null) {
            throw new InvalidKeyException("Key must not be null");
        }
        checkKeyAlgo(key, this.type.keyAlgo());
        if ((key instanceof RSAPrivateKeyImpl) || (key instanceof RSAPrivateCrtKeyImpl) || (key instanceof RSAPublicKeyImpl)) {
            return key;
        }
        if (key instanceof PublicKey) {
            return translatePublicKey((PublicKey) key);
        }
        if (key instanceof PrivateKey) {
            return translatePrivateKey((PrivateKey) key);
        }
        throw new InvalidKeyException("Neither a public nor a private key");
    }

    @Override // java.security.KeyFactorySpi
    protected PublicKey engineGeneratePublic(KeySpec keySpec) throws InvalidKeySpecException {
        try {
            return generatePublic(keySpec);
        } catch (InvalidKeySpecException e2) {
            throw e2;
        } catch (GeneralSecurityException e3) {
            throw new InvalidKeySpecException(e3);
        }
    }

    @Override // java.security.KeyFactorySpi
    protected PrivateKey engineGeneratePrivate(KeySpec keySpec) throws InvalidKeySpecException {
        try {
            return generatePrivate(keySpec);
        } catch (InvalidKeySpecException e2) {
            throw e2;
        } catch (GeneralSecurityException e3) {
            throw new InvalidKeySpecException(e3);
        }
    }

    private PublicKey translatePublicKey(PublicKey publicKey) throws InvalidKeyException {
        if (publicKey instanceof RSAPublicKey) {
            RSAPublicKey rSAPublicKey = (RSAPublicKey) publicKey;
            try {
                return new RSAPublicKeyImpl(RSAUtil.createAlgorithmId(this.type, rSAPublicKey.getParams()), rSAPublicKey.getModulus(), rSAPublicKey.getPublicExponent());
            } catch (ProviderException e2) {
                throw new InvalidKeyException("Invalid key", e2);
            }
        }
        if (XMLX509Certificate.JCA_CERT_ID.equals(publicKey.getFormat())) {
            RSAPublicKeyImpl rSAPublicKeyImpl = new RSAPublicKeyImpl(publicKey.getEncoded());
            checkKeyAlgo(rSAPublicKeyImpl, this.type.keyAlgo());
            return rSAPublicKeyImpl;
        }
        throw new InvalidKeyException("Public keys must be instance of RSAPublicKey or have X.509 encoding");
    }

    private PrivateKey translatePrivateKey(PrivateKey privateKey) throws InvalidKeyException {
        if (privateKey instanceof RSAPrivateCrtKey) {
            RSAPrivateCrtKey rSAPrivateCrtKey = (RSAPrivateCrtKey) privateKey;
            try {
                return new RSAPrivateCrtKeyImpl(RSAUtil.createAlgorithmId(this.type, rSAPrivateCrtKey.getParams()), rSAPrivateCrtKey.getModulus(), rSAPrivateCrtKey.getPublicExponent(), rSAPrivateCrtKey.getPrivateExponent(), rSAPrivateCrtKey.getPrimeP(), rSAPrivateCrtKey.getPrimeQ(), rSAPrivateCrtKey.getPrimeExponentP(), rSAPrivateCrtKey.getPrimeExponentQ(), rSAPrivateCrtKey.getCrtCoefficient());
            } catch (ProviderException e2) {
                throw new InvalidKeyException("Invalid key", e2);
            }
        }
        if (privateKey instanceof RSAPrivateKey) {
            RSAPrivateKey rSAPrivateKey = (RSAPrivateKey) privateKey;
            try {
                return new RSAPrivateKeyImpl(RSAUtil.createAlgorithmId(this.type, rSAPrivateKey.getParams()), rSAPrivateKey.getModulus(), rSAPrivateKey.getPrivateExponent());
            } catch (ProviderException e3) {
                throw new InvalidKeyException("Invalid key", e3);
            }
        }
        if ("PKCS#8".equals(privateKey.getFormat())) {
            RSAPrivateKey rSAPrivateKeyNewKey = RSAPrivateCrtKeyImpl.newKey(privateKey.getEncoded());
            checkKeyAlgo(rSAPrivateKeyNewKey, this.type.keyAlgo());
            return rSAPrivateKeyNewKey;
        }
        throw new InvalidKeyException("Private keys must be instance of RSAPrivate(Crt)Key or have PKCS#8 encoding");
    }

    private PublicKey generatePublic(KeySpec keySpec) throws GeneralSecurityException {
        if (keySpec instanceof X509EncodedKeySpec) {
            RSAPublicKeyImpl rSAPublicKeyImpl = new RSAPublicKeyImpl(((X509EncodedKeySpec) keySpec).getEncoded());
            checkKeyAlgo(rSAPublicKeyImpl, this.type.keyAlgo());
            return rSAPublicKeyImpl;
        }
        if (keySpec instanceof RSAPublicKeySpec) {
            RSAPublicKeySpec rSAPublicKeySpec = (RSAPublicKeySpec) keySpec;
            try {
                return new RSAPublicKeyImpl(RSAUtil.createAlgorithmId(this.type, rSAPublicKeySpec.getParams()), rSAPublicKeySpec.getModulus(), rSAPublicKeySpec.getPublicExponent());
            } catch (ProviderException e2) {
                throw new InvalidKeySpecException(e2);
            }
        }
        throw new InvalidKeySpecException("Only RSAPublicKeySpec and X509EncodedKeySpec supported for RSA public keys");
    }

    private PrivateKey generatePrivate(KeySpec keySpec) throws GeneralSecurityException {
        if (keySpec instanceof PKCS8EncodedKeySpec) {
            RSAPrivateKey rSAPrivateKeyNewKey = RSAPrivateCrtKeyImpl.newKey(((PKCS8EncodedKeySpec) keySpec).getEncoded());
            checkKeyAlgo(rSAPrivateKeyNewKey, this.type.keyAlgo());
            return rSAPrivateKeyNewKey;
        }
        if (keySpec instanceof RSAPrivateCrtKeySpec) {
            RSAPrivateCrtKeySpec rSAPrivateCrtKeySpec = (RSAPrivateCrtKeySpec) keySpec;
            try {
                return new RSAPrivateCrtKeyImpl(RSAUtil.createAlgorithmId(this.type, rSAPrivateCrtKeySpec.getParams()), rSAPrivateCrtKeySpec.getModulus(), rSAPrivateCrtKeySpec.getPublicExponent(), rSAPrivateCrtKeySpec.getPrivateExponent(), rSAPrivateCrtKeySpec.getPrimeP(), rSAPrivateCrtKeySpec.getPrimeQ(), rSAPrivateCrtKeySpec.getPrimeExponentP(), rSAPrivateCrtKeySpec.getPrimeExponentQ(), rSAPrivateCrtKeySpec.getCrtCoefficient());
            } catch (ProviderException e2) {
                throw new InvalidKeySpecException(e2);
            }
        }
        if (keySpec instanceof RSAPrivateKeySpec) {
            RSAPrivateKeySpec rSAPrivateKeySpec = (RSAPrivateKeySpec) keySpec;
            try {
                return new RSAPrivateKeyImpl(RSAUtil.createAlgorithmId(this.type, rSAPrivateKeySpec.getParams()), rSAPrivateKeySpec.getModulus(), rSAPrivateKeySpec.getPrivateExponent());
            } catch (ProviderException e3) {
                throw new InvalidKeySpecException(e3);
            }
        }
        throw new InvalidKeySpecException("Only RSAPrivate(Crt)KeySpec and PKCS8EncodedKeySpec supported for RSA private keys");
    }

    @Override // java.security.KeyFactorySpi
    protected <T extends KeySpec> T engineGetKeySpec(Key key, Class<T> cls) throws InvalidKeySpecException {
        try {
            Key keyEngineTranslateKey = engineTranslateKey(key);
            if (keyEngineTranslateKey instanceof RSAPublicKey) {
                RSAPublicKey rSAPublicKey = (RSAPublicKey) keyEngineTranslateKey;
                if (cls.isAssignableFrom(RSA_PUB_KEYSPEC_CLS)) {
                    return cls.cast(new RSAPublicKeySpec(rSAPublicKey.getModulus(), rSAPublicKey.getPublicExponent(), rSAPublicKey.getParams()));
                }
                if (cls.isAssignableFrom(X509_KEYSPEC_CLS)) {
                    return cls.cast(new X509EncodedKeySpec(keyEngineTranslateKey.getEncoded()));
                }
                throw new InvalidKeySpecException("KeySpec must be RSAPublicKeySpec or X509EncodedKeySpec for RSA public keys");
            }
            if (keyEngineTranslateKey instanceof RSAPrivateKey) {
                if (cls.isAssignableFrom(PKCS8_KEYSPEC_CLS)) {
                    return cls.cast(new PKCS8EncodedKeySpec(keyEngineTranslateKey.getEncoded()));
                }
                if (cls.isAssignableFrom(RSA_PRIVCRT_KEYSPEC_CLS)) {
                    if (keyEngineTranslateKey instanceof RSAPrivateCrtKey) {
                        RSAPrivateCrtKey rSAPrivateCrtKey = (RSAPrivateCrtKey) keyEngineTranslateKey;
                        return cls.cast(new RSAPrivateCrtKeySpec(rSAPrivateCrtKey.getModulus(), rSAPrivateCrtKey.getPublicExponent(), rSAPrivateCrtKey.getPrivateExponent(), rSAPrivateCrtKey.getPrimeP(), rSAPrivateCrtKey.getPrimeQ(), rSAPrivateCrtKey.getPrimeExponentP(), rSAPrivateCrtKey.getPrimeExponentQ(), rSAPrivateCrtKey.getCrtCoefficient(), rSAPrivateCrtKey.getParams()));
                    }
                    if (!cls.isAssignableFrom(RSA_PRIV_KEYSPEC_CLS)) {
                        throw new InvalidKeySpecException("RSAPrivateCrtKeySpec can only be used with CRT keys");
                    }
                    RSAPrivateKey rSAPrivateKey = (RSAPrivateKey) keyEngineTranslateKey;
                    return cls.cast(new RSAPrivateKeySpec(rSAPrivateKey.getModulus(), rSAPrivateKey.getPrivateExponent(), rSAPrivateKey.getParams()));
                }
                throw new InvalidKeySpecException("KeySpec must be RSAPrivate(Crt)KeySpec or PKCS8EncodedKeySpec for RSA private keys");
            }
            throw new InvalidKeySpecException("Neither public nor private key");
        } catch (InvalidKeyException e2) {
            throw new InvalidKeySpecException(e2);
        }
    }

    /* loaded from: rt.jar:sun/security/rsa/RSAKeyFactory$Legacy.class */
    public static final class Legacy extends RSAKeyFactory {
        public Legacy() {
            super(RSAUtil.KeyType.RSA);
        }
    }

    /* loaded from: rt.jar:sun/security/rsa/RSAKeyFactory$PSS.class */
    public static final class PSS extends RSAKeyFactory {
        public PSS() {
            super(RSAUtil.KeyType.PSS);
        }
    }
}
