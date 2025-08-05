package sun.security.mscapi;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.InvalidParameterException;
import java.security.KeyException;
import java.security.KeyPair;
import java.security.KeyPairGeneratorSpi;
import java.security.ProviderException;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.RSAKeyGenParameterSpec;
import java.util.UUID;
import org.apache.commons.math3.geometry.VectorFormat;
import sun.security.rsa.RSAKeyFactory;
import sun.security.util.SecurityProviderConstants;

/* loaded from: sunmscapi.jar:sun/security/mscapi/CKeyPairGenerator.class */
public abstract class CKeyPairGenerator extends KeyPairGeneratorSpi {
    protected String keyAlg;

    public CKeyPairGenerator(String str) {
        this.keyAlg = str;
    }

    /* loaded from: sunmscapi.jar:sun/security/mscapi/CKeyPairGenerator$RSA.class */
    public static class RSA extends CKeyPairGenerator {
        static final int KEY_SIZE_MIN = 512;
        static final int KEY_SIZE_MAX = 16384;
        private int keySize;

        private static native CKeyPair generateCKeyPair(String str, int i2, String str2) throws KeyException;

        public RSA() {
            super("RSA");
            initialize(SecurityProviderConstants.DEF_RSA_KEY_SIZE, (SecureRandom) null);
        }

        @Override // java.security.KeyPairGeneratorSpi
        public void initialize(int i2, SecureRandom secureRandom) {
            try {
                RSAKeyFactory.checkKeyLengths(i2, null, 512, 16384);
                this.keySize = i2;
            } catch (InvalidKeyException e2) {
                throw new InvalidParameterException(e2.getMessage());
            }
        }

        @Override // java.security.KeyPairGeneratorSpi
        public void initialize(AlgorithmParameterSpec algorithmParameterSpec, SecureRandom secureRandom) throws InvalidAlgorithmParameterException {
            int keysize;
            if (algorithmParameterSpec == null) {
                keysize = SecurityProviderConstants.DEF_RSA_KEY_SIZE;
            } else if (algorithmParameterSpec instanceof RSAKeyGenParameterSpec) {
                if (((RSAKeyGenParameterSpec) algorithmParameterSpec).getPublicExponent() != null) {
                    throw new InvalidAlgorithmParameterException("Exponent parameter is not supported");
                }
                keysize = ((RSAKeyGenParameterSpec) algorithmParameterSpec).getKeysize();
            } else {
                throw new InvalidAlgorithmParameterException("Params must be an instance of RSAKeyGenParameterSpec");
            }
            try {
                RSAKeyFactory.checkKeyLengths(keysize, null, 512, 16384);
                this.keySize = keysize;
            } catch (InvalidKeyException e2) {
                throw new InvalidAlgorithmParameterException("Invalid Key sizes", e2);
            }
        }

        @Override // java.security.KeyPairGeneratorSpi
        public KeyPair generateKeyPair() {
            try {
                CKeyPair cKeyPairGenerateCKeyPair = generateCKeyPair(this.keyAlg, this.keySize, VectorFormat.DEFAULT_PREFIX + UUID.randomUUID().toString() + "}");
                return new KeyPair(cKeyPairGenerateCKeyPair.getPublic(), cKeyPairGenerateCKeyPair.getPrivate());
            } catch (KeyException e2) {
                throw new ProviderException(e2);
            }
        }
    }
}
