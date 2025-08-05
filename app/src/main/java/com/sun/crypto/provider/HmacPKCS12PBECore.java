package com.sun.crypto.provider;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Arrays;
import javax.crypto.SecretKey;
import javax.crypto.spec.PBEParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/* loaded from: sunjce_provider.jar:com/sun/crypto/provider/HmacPKCS12PBECore.class */
abstract class HmacPKCS12PBECore extends HmacCore {
    private final String algorithm;

    /* renamed from: bl, reason: collision with root package name */
    private final int f11823bl;

    /* loaded from: sunjce_provider.jar:com/sun/crypto/provider/HmacPKCS12PBECore$HmacPKCS12PBE_SHA1.class */
    public static final class HmacPKCS12PBE_SHA1 extends HmacPKCS12PBECore {
        @Override // com.sun.crypto.provider.HmacCore, javax.crypto.MacSpi
        public /* bridge */ /* synthetic */ Object clone() throws CloneNotSupportedException {
            return super.clone();
        }

        public HmacPKCS12PBE_SHA1() throws NoSuchAlgorithmException {
            super("SHA1", 64);
        }
    }

    /* loaded from: sunjce_provider.jar:com/sun/crypto/provider/HmacPKCS12PBECore$HmacPKCS12PBE_SHA224.class */
    public static final class HmacPKCS12PBE_SHA224 extends HmacPKCS12PBECore {
        @Override // com.sun.crypto.provider.HmacCore, javax.crypto.MacSpi
        public /* bridge */ /* synthetic */ Object clone() throws CloneNotSupportedException {
            return super.clone();
        }

        public HmacPKCS12PBE_SHA224() throws NoSuchAlgorithmException {
            super("SHA-224", 64);
        }
    }

    /* loaded from: sunjce_provider.jar:com/sun/crypto/provider/HmacPKCS12PBECore$HmacPKCS12PBE_SHA256.class */
    public static final class HmacPKCS12PBE_SHA256 extends HmacPKCS12PBECore {
        @Override // com.sun.crypto.provider.HmacCore, javax.crypto.MacSpi
        public /* bridge */ /* synthetic */ Object clone() throws CloneNotSupportedException {
            return super.clone();
        }

        public HmacPKCS12PBE_SHA256() throws NoSuchAlgorithmException {
            super("SHA-256", 64);
        }
    }

    /* loaded from: sunjce_provider.jar:com/sun/crypto/provider/HmacPKCS12PBECore$HmacPKCS12PBE_SHA384.class */
    public static final class HmacPKCS12PBE_SHA384 extends HmacPKCS12PBECore {
        @Override // com.sun.crypto.provider.HmacCore, javax.crypto.MacSpi
        public /* bridge */ /* synthetic */ Object clone() throws CloneNotSupportedException {
            return super.clone();
        }

        public HmacPKCS12PBE_SHA384() throws NoSuchAlgorithmException {
            super("SHA-384", 128);
        }
    }

    /* loaded from: sunjce_provider.jar:com/sun/crypto/provider/HmacPKCS12PBECore$HmacPKCS12PBE_SHA512.class */
    public static final class HmacPKCS12PBE_SHA512 extends HmacPKCS12PBECore {
        @Override // com.sun.crypto.provider.HmacCore, javax.crypto.MacSpi
        public /* bridge */ /* synthetic */ Object clone() throws CloneNotSupportedException {
            return super.clone();
        }

        public HmacPKCS12PBE_SHA512() throws NoSuchAlgorithmException {
            super("SHA-512", 128);
        }
    }

    /* loaded from: sunjce_provider.jar:com/sun/crypto/provider/HmacPKCS12PBECore$HmacPKCS12PBE_SHA512_224.class */
    public static final class HmacPKCS12PBE_SHA512_224 extends HmacPKCS12PBECore {
        @Override // com.sun.crypto.provider.HmacCore, javax.crypto.MacSpi
        public /* bridge */ /* synthetic */ Object clone() throws CloneNotSupportedException {
            return super.clone();
        }

        public HmacPKCS12PBE_SHA512_224() throws NoSuchAlgorithmException {
            super("SHA-512/224", 128);
        }
    }

    /* loaded from: sunjce_provider.jar:com/sun/crypto/provider/HmacPKCS12PBECore$HmacPKCS12PBE_SHA512_256.class */
    public static final class HmacPKCS12PBE_SHA512_256 extends HmacPKCS12PBECore {
        @Override // com.sun.crypto.provider.HmacCore, javax.crypto.MacSpi
        public /* bridge */ /* synthetic */ Object clone() throws CloneNotSupportedException {
            return super.clone();
        }

        public HmacPKCS12PBE_SHA512_256() throws NoSuchAlgorithmException {
            super("SHA-512/256", 128);
        }
    }

    public HmacPKCS12PBECore(String str, int i2) throws NoSuchAlgorithmException {
        super(str, i2);
        this.algorithm = str;
        this.f11823bl = i2;
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
            byte[] bArrDerive = PKCS12PBECipherCore.derive(password, salt, iterationCount, engineGetMacLength(), 3, this.algorithm, this.f11823bl);
            Arrays.fill(password, (char) 0);
            super.engineInit(new SecretKeySpec(bArrDerive, "HmacSHA1"), null);
        } catch (Throwable th) {
            Arrays.fill(password, (char) 0);
            throw th;
        }
    }
}
