package com.sun.crypto.provider;

import com.sun.crypto.provider.PBKDF2Core;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.ProviderException;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import javax.crypto.SecretKey;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/* loaded from: sunjce_provider.jar:com/sun/crypto/provider/PBMAC1Core.class */
abstract class PBMAC1Core extends HmacCore {
    private final String kdfAlgo;
    private final String hashAlgo;
    private final int blockLength;

    PBMAC1Core(String str, String str2, int i2) throws NoSuchAlgorithmException {
        super(str2, i2);
        this.kdfAlgo = str;
        this.hashAlgo = str2;
        this.blockLength = i2;
    }

    private static PBKDF2Core getKDFImpl(String str) {
        PBKDF2Core hmacSHA512;
        switch (str) {
            case "HmacSHA1":
                hmacSHA512 = new PBKDF2Core.HmacSHA1();
                break;
            case "HmacSHA224":
                hmacSHA512 = new PBKDF2Core.HmacSHA224();
                break;
            case "HmacSHA256":
                hmacSHA512 = new PBKDF2Core.HmacSHA256();
                break;
            case "HmacSHA384":
                hmacSHA512 = new PBKDF2Core.HmacSHA384();
                break;
            case "HmacSHA512":
                hmacSHA512 = new PBKDF2Core.HmacSHA512();
                break;
            default:
                throw new ProviderException("No MAC implementation for " + str);
        }
        return hmacSHA512;
    }

    /* JADX WARN: Finally extract failed */
    @Override // com.sun.crypto.provider.HmacCore, javax.crypto.MacSpi
    protected void engineInit(Key key, AlgorithmParameterSpec algorithmParameterSpec) throws InvalidKeyException, InvalidAlgorithmParameterException {
        byte[] encoded;
        char[] password;
        byte[] salt = null;
        int iterationCount = 0;
        if (key instanceof javax.crypto.interfaces.PBEKey) {
            javax.crypto.interfaces.PBEKey pBEKey = (javax.crypto.interfaces.PBEKey) key;
            password = pBEKey.getPassword();
            salt = pBEKey.getSalt();
            iterationCount = pBEKey.getIterationCount();
        } else if (key instanceof SecretKey) {
            if (!key.getAlgorithm().regionMatches(true, 0, "PBE", 0, 3) || (encoded = key.getEncoded()) == null) {
                throw new InvalidKeyException("Missing password");
            }
            password = new char[encoded.length];
            for (int i2 = 0; i2 < password.length; i2++) {
                password[i2] = (char) (encoded[i2] & Byte.MAX_VALUE);
            }
            Arrays.fill(encoded, (byte) 0);
        } else {
            throw new InvalidKeyException("SecretKey of PBE type required");
        }
        try {
            if (algorithmParameterSpec == null) {
                if (salt == null || iterationCount == 0) {
                    throw new InvalidAlgorithmParameterException("PBEParameterSpec required for salt and iteration count");
                }
            } else {
                if (!(algorithmParameterSpec instanceof PBEParameterSpec)) {
                    throw new InvalidAlgorithmParameterException("PBEParameterSpec type required");
                }
                PBEParameterSpec pBEParameterSpec = (PBEParameterSpec) algorithmParameterSpec;
                if (salt != null) {
                    if (!Arrays.equals(salt, pBEParameterSpec.getSalt())) {
                        throw new InvalidAlgorithmParameterException("Inconsistent value of salt between key and params");
                    }
                } else {
                    salt = pBEParameterSpec.getSalt();
                }
                if (iterationCount != 0) {
                    if (iterationCount != pBEParameterSpec.getIterationCount()) {
                        throw new InvalidAlgorithmParameterException("Different iteration count between key and params");
                    }
                } else {
                    iterationCount = pBEParameterSpec.getIterationCount();
                }
            }
            if (salt.length < 8) {
                throw new InvalidAlgorithmParameterException("Salt must be at least 8 bytes long");
            }
            if (iterationCount <= 0) {
                throw new InvalidAlgorithmParameterException("IterationCount must be a positive number");
            }
            PBEKeySpec pBEKeySpec = new PBEKeySpec(password, salt, iterationCount, this.blockLength);
            Arrays.fill(password, (char) 0);
            try {
                super.engineInit(new SecretKeySpec(getKDFImpl(this.kdfAlgo).engineGenerateSecret(pBEKeySpec).getEncoded(), this.kdfAlgo), null);
            } catch (InvalidKeySpecException e2) {
                InvalidKeyException invalidKeyException = new InvalidKeyException("Cannot construct PBE key");
                invalidKeyException.initCause(e2);
                throw invalidKeyException;
            }
        } catch (Throwable th) {
            Arrays.fill(password, (char) 0);
            throw th;
        }
    }

    /* loaded from: sunjce_provider.jar:com/sun/crypto/provider/PBMAC1Core$HmacSHA1.class */
    public static final class HmacSHA1 extends PBMAC1Core {
        @Override // com.sun.crypto.provider.HmacCore, javax.crypto.MacSpi
        public /* bridge */ /* synthetic */ Object clone() throws CloneNotSupportedException {
            return super.clone();
        }

        public HmacSHA1() throws NoSuchAlgorithmException {
            super("HmacSHA1", "SHA1", 64);
        }
    }

    /* loaded from: sunjce_provider.jar:com/sun/crypto/provider/PBMAC1Core$HmacSHA224.class */
    public static final class HmacSHA224 extends PBMAC1Core {
        @Override // com.sun.crypto.provider.HmacCore, javax.crypto.MacSpi
        public /* bridge */ /* synthetic */ Object clone() throws CloneNotSupportedException {
            return super.clone();
        }

        public HmacSHA224() throws NoSuchAlgorithmException {
            super("HmacSHA224", "SHA-224", 64);
        }
    }

    /* loaded from: sunjce_provider.jar:com/sun/crypto/provider/PBMAC1Core$HmacSHA256.class */
    public static final class HmacSHA256 extends PBMAC1Core {
        @Override // com.sun.crypto.provider.HmacCore, javax.crypto.MacSpi
        public /* bridge */ /* synthetic */ Object clone() throws CloneNotSupportedException {
            return super.clone();
        }

        public HmacSHA256() throws NoSuchAlgorithmException {
            super("HmacSHA256", "SHA-256", 64);
        }
    }

    /* loaded from: sunjce_provider.jar:com/sun/crypto/provider/PBMAC1Core$HmacSHA384.class */
    public static final class HmacSHA384 extends PBMAC1Core {
        @Override // com.sun.crypto.provider.HmacCore, javax.crypto.MacSpi
        public /* bridge */ /* synthetic */ Object clone() throws CloneNotSupportedException {
            return super.clone();
        }

        public HmacSHA384() throws NoSuchAlgorithmException {
            super("HmacSHA384", "SHA-384", 128);
        }
    }

    /* loaded from: sunjce_provider.jar:com/sun/crypto/provider/PBMAC1Core$HmacSHA512.class */
    public static final class HmacSHA512 extends PBMAC1Core {
        @Override // com.sun.crypto.provider.HmacCore, javax.crypto.MacSpi
        public /* bridge */ /* synthetic */ Object clone() throws CloneNotSupportedException {
            return super.clone();
        }

        public HmacSHA512() throws NoSuchAlgorithmException {
            super("HmacSHA512", "SHA-512", 128);
        }
    }
}
