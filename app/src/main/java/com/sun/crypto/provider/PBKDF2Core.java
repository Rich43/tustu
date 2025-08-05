package com.sun.crypto.provider;

import java.security.InvalidKeyException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactorySpi;
import javax.crypto.spec.PBEKeySpec;

/* loaded from: sunjce_provider.jar:com/sun/crypto/provider/PBKDF2Core.class */
abstract class PBKDF2Core extends SecretKeyFactorySpi {
    private final String prfAlgo;

    PBKDF2Core(String str) {
        this.prfAlgo = str;
    }

    @Override // javax.crypto.SecretKeyFactorySpi
    protected SecretKey engineGenerateSecret(KeySpec keySpec) throws InvalidKeySpecException {
        if (!(keySpec instanceof PBEKeySpec)) {
            throw new InvalidKeySpecException("Invalid key spec");
        }
        return new PBKDF2KeyImpl((PBEKeySpec) keySpec, this.prfAlgo);
    }

    @Override // javax.crypto.SecretKeyFactorySpi
    protected KeySpec engineGetKeySpec(SecretKey secretKey, Class<?> cls) throws InvalidKeySpecException {
        if (secretKey instanceof javax.crypto.interfaces.PBEKey) {
            if (cls != null && PBEKeySpec.class.isAssignableFrom(cls)) {
                javax.crypto.interfaces.PBEKey pBEKey = (javax.crypto.interfaces.PBEKey) secretKey;
                return new PBEKeySpec(pBEKey.getPassword(), pBEKey.getSalt(), pBEKey.getIterationCount(), pBEKey.getEncoded().length * 8);
            }
            throw new InvalidKeySpecException("Invalid key spec");
        }
        throw new InvalidKeySpecException("Invalid key format/algorithm");
    }

    @Override // javax.crypto.SecretKeyFactorySpi
    protected SecretKey engineTranslateKey(SecretKey secretKey) throws InvalidKeyException {
        if (secretKey != null && secretKey.getAlgorithm().equalsIgnoreCase("PBKDF2With" + this.prfAlgo) && secretKey.getFormat().equalsIgnoreCase("RAW")) {
            if (secretKey instanceof PBKDF2KeyImpl) {
                return secretKey;
            }
            if (secretKey instanceof javax.crypto.interfaces.PBEKey) {
                javax.crypto.interfaces.PBEKey pBEKey = (javax.crypto.interfaces.PBEKey) secretKey;
                try {
                    return new PBKDF2KeyImpl(new PBEKeySpec(pBEKey.getPassword(), pBEKey.getSalt(), pBEKey.getIterationCount(), pBEKey.getEncoded().length * 8), this.prfAlgo);
                } catch (InvalidKeySpecException e2) {
                    InvalidKeyException invalidKeyException = new InvalidKeyException("Invalid key component(s)");
                    invalidKeyException.initCause(e2);
                    throw invalidKeyException;
                }
            }
        }
        throw new InvalidKeyException("Invalid key format/algorithm");
    }

    /* loaded from: sunjce_provider.jar:com/sun/crypto/provider/PBKDF2Core$HmacSHA1.class */
    public static final class HmacSHA1 extends PBKDF2Core {
        public HmacSHA1() {
            super("HmacSHA1");
        }
    }

    /* loaded from: sunjce_provider.jar:com/sun/crypto/provider/PBKDF2Core$HmacSHA224.class */
    public static final class HmacSHA224 extends PBKDF2Core {
        public HmacSHA224() {
            super("HmacSHA224");
        }
    }

    /* loaded from: sunjce_provider.jar:com/sun/crypto/provider/PBKDF2Core$HmacSHA256.class */
    public static final class HmacSHA256 extends PBKDF2Core {
        public HmacSHA256() {
            super("HmacSHA256");
        }
    }

    /* loaded from: sunjce_provider.jar:com/sun/crypto/provider/PBKDF2Core$HmacSHA384.class */
    public static final class HmacSHA384 extends PBKDF2Core {
        public HmacSHA384() {
            super("HmacSHA384");
        }
    }

    /* loaded from: sunjce_provider.jar:com/sun/crypto/provider/PBKDF2Core$HmacSHA512.class */
    public static final class HmacSHA512 extends PBKDF2Core {
        public HmacSHA512() {
            super("HmacSHA512");
        }
    }
}
